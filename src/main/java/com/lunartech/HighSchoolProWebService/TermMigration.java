package com.lunartech.HighSchoolProWebService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TermMigration {
    Globals globals;
    ConfigurationIntialiser configurationIntialiser;
    public TermMigration(String schoolDb) {
        this.schoolDb = schoolDb;
        globals =new Globals(schoolDb);
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    String schoolDb;
    public   Map migrateTerm()
    {


        Map map=new HashMap();

        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);




                    String termcode="";
                    String classcode;
                    String voteheadid;

                    try {
                        String CURRENTTERM="";

                        String sql="Select termcode from terms where status='"+"next"+"'";
                        ps=con.prepareStatement(sql);
                        rs=ps.executeQuery();
                        while(rs.next())
                        {
                            termcode=rs.getString("termcode");
                        }
                        boolean proceed=true;


                        if(configurationIntialiser.migrationReadiness())
                        {



                                if(globals.currentTermName().equalsIgnoreCase("Term 3"))
                                {
                                    map.put("responseCode","301");
                                    map.put("responseDescription","End Of Academic Year,Kindly Consinder Doing Class Transfer");
                                    return map;
                                }
                                else{

                                    String ccode="",pcode="",ncode="",termname="";
                                    double fee,openingbal=0;
                                    String sql4="Select termcode,termname from terms where status='"+"current"+"'";
                                    ps=con.prepareStatement(sql4);
                                    rs=ps.executeQuery();
                                    if(rs.next())
                                    {CURRENTTERM=rs.getString("Termname");
                                        ccode=rs.getString("Termcode");
                                    }

                                    String sql5="Select termcode from terms where status='"+"next"+"'";
                                    ps=con.prepareStatement(sql5);
                                    rs=ps.executeQuery();
                                    if(rs.next())
                                    {
                                        ncode=rs.getString("Termcode");
                                    }

                                    String sql6="Select termcode from terms where status='"+"previous"+"'";
                                    ps=con.prepareStatement(sql6);
                                    rs=ps.executeQuery();
                                    if(rs.next())
                                    {
                                        pcode=rs.getString("Termcode");
                                    }
                                    String querry="Update terms set status='"+"current"+"' where termcode='"+ncode+"'";
                                    ps=con.prepareStatement(querry);
                                    ps.execute();

                                    String querry1="Update terms set status='"+"previous"+"' where termcode='"+ccode+"'";
                                    ps=con.prepareStatement(querry1);
                                    ps.execute();
                                    String querry2="Update terms set status='"+"next"+"' where termcode='"+pcode+"'";
                                    ps=con.prepareStatement(querry2);
                                    ps.execute();
                                    String querry3="Select termcode,termname from terms where status='"+"current"+"'";
                                    ps=con.prepareStatement(querry3);
                                    rs=ps.executeQuery();
                                    if(rs.next())
                                    {
                                        ccode=rs.getString("termcode");
                                        termname=rs.getString("termname");
                                    }


                                    String sql9="Update admission set currentterm='"+ccode+"'";
                                    ps=con.prepareStatement(sql9);
                                    ps.execute();



                                    String sqlb="Select distinct examname from exams where transferable='"+"true"+"'";
                                    ps=con.prepareStatement(sqlb);
                                    ResultSet rss=ps.executeQuery();
                                    while(rss.next())
                                    {
                                        String name=rss.getString("ExamName");
                                        String sqld = "select * from classes  where precision1<5 order by precision1";
                                        ps = con.prepareStatement(sqld);
                                        rs = ps.executeQuery();
                                        while (rs.next()) {
                                            String classname=rs.getString("ClassName");
                                            classcode=rs.getString("Classcode");
                                            int weight;

                                            String examcode1=ExamCodesGenerator.generatecode(classname, String.valueOf((globals.academicYear())), CURRENTTERM, name.toUpperCase());
                                            String qrr="Select weight from examweights where examcode='"+examcode1+"'";
                                            ps=con.prepareStatement(qrr);
                                            ResultSet rr=ps.executeQuery();
                                            if(rr.next())
                                            {
                                                weight=rr.getInt("weight");

                                            }
                                            else{
                                                weight=100;

                                            }

                                            String examcode=ExamCodesGenerator.generatecode(classname, String.valueOf(globals.academicYear()), globals.currentTermName(), name.toUpperCase());

                                            ResultSet Rs;
                                            String sql2="Insert Into exams values('"+name+"','"+examcode+"','"+globals.currentTerm()+"','"+globals.academicYear()+"','"+"true"+"','"+classcode+"')";
                                            ps=con.prepareStatement(sql2);
                                            ps.execute();

                                            String querry5="Insert into examweights values('"+examcode+"','"+weight+"')";
                                            ps=con.prepareStatement(querry5);
                                            ps.execute();


                                        }

                                    }

                                    String adm,pycode="",streamcode="";

                                    int counter=0;

                                    String className="";
                                    String sql8="select * from classes where precision1<5 order by precision1";
                                    ps=con.prepareStatement(sql8);
                                    ResultSet RS;
                                    RS=ps.executeQuery();
                                    while(RS.next())
                                    {
                                        className=RS.getString("classname");
                                        classcode=RS.getString("Classcode");



                                        ResultSet rx;
                                        String sql10="Select admissionNumber from admission where currentform='"+classcode+"' and currentterm='"+ccode+"' ";
                                        ps=con.prepareStatement(sql10);
                                        rx=ps.executeQuery();
                                        while(rx.next())
                                        {


                                            counter++;
                                            adm=rx.getString("AdmissionNumber");
                                            String program=globals.studentProgram(adm);



                                            String sql7="Select amountperhead,voteheadid from studentpayablevoteheads where termcode='"+termcode+"' and academicYear='"+globals.academicYear()+"'  and amountperhead!='"+""+"' and amountPerhead!='"+"0"+"' and program='"+program+"' and classcode='"+classcode+"'";
                                            ps=con.prepareStatement(sql7);
                                            rs=ps.executeQuery();
                                            while(rs.next())
                                            {

                                                fee=rs.getDouble("amountperhead");
                                                voteheadid=rs.getString("voteheadid");

                                                String sql12="Insert into payablevoteheadperstudent values('"+adm+"','"+globals.termname(ccode)+"','"+globals.academicYear()+"','"+voteheadid+"','"+fee+"',curDate(),'"+"INV"+"')";
                                                ps=con.prepareStatement(sql12);
                                                ps.execute();

                                            }




                                        }









                                    }
                                    String querry22="update systemconfiguration set status='"+"False"+"' where configurationid='"+"CO024"+"'";
                                    ps=con.prepareStatement(querry22);
                                    ps.execute();

                                    map.put("responseCode","200");
                                    map.put("responseDescription","Term Migration Succesfull");
                                    return map;




                                }





                        }
                        else{
                            map.put("responseCode","301");
                            map.put("responseDescription","Finance Department Not Ready For Term Migration");
                            return map;
                        }





                    } catch (SQLException sq) {

                        sq.printStackTrace();
                        map.put("responseCode","501");
                        map.put("responseDescription","An Error Occured:"+sq.getMessage());
                        return map;




                    }








    }
}
