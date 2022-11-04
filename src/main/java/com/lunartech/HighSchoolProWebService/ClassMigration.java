package com.lunartech.HighSchoolProWebService;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClassMigration {
    String schoolDb;
Globals globals;
ConfigurationIntialiser configurationIntialiser;
    public ClassMigration(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    public  Map MigrateClass(boolean form1, boolean form2, boolean form3)
    {

        Connection con;
        PreparedStatement ps ;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();

                String CURRENTTERM="";
                String f1code="",f2code="",f3code="",voteheadid="";
                double fee=0,openingbal=0;
                if(form1)
                {
                    f1code=globals.classCode("Form 1");
                }
                if(form2)
                {
                    f2code=globals.classCode("Form 2");
                }
                if(form3)
                {
                    f3code=globals.classCode("Form 3");
                }
                try {int press=0;

                    String sql="select precisions from terms where termcode='"+globals.currentTerm()+"'";
                    ps=con.prepareStatement(sql);
                    rs=ps.executeQuery();
                    if(rs.next())
                    {
                        press=rs.getInt("precisions");
                    }
                    if(press==3)
                    {
                        String termcode="";
                        String classcode="";

                        try {
                            String sqlz="Select termcode from terms where status='"+"next"+"'";
                            ps=con.prepareStatement(sqlz);
                            ResultSet rs7=ps.executeQuery();
                            while(rs7.next())
                            {
                                termcode=rs7.getString("termcode");

                            }
                            boolean proceed=true;

                            if(configurationIntialiser.migrationReadiness())
                            {
                                proceed=true;


                                    int classOder=0;
                                    String CLASSCODE="";
                                    String qq="Select * from subjectrights";
                                    ps=con.prepareStatement(qq);
                                    rs=ps.executeQuery();
                                    while(rs.next())
                                    {
                                        String CLASS=rs.getString("Classcode");
                                        String teachercode=rs.getString("teachercode");
                                        String subcode=rs.getString("subjectcode");
                                        String dd="Select precision1 from classes where classcode='"+CLASS+"'";
                                        ps=con.prepareStatement(dd);

                                        ResultSet rr=ps.executeQuery();
                                        if(rr.next())
                                        {
                                            classOder=rr.getInt("Precision1")+1;
                                        }
                                        String oo="Select classcode from classes where precision1='"+classOder+"'";
                                        ps=con.prepareStatement(oo);
                                        rr=ps.executeQuery();
                                        if(rr.next())
                                        {
                                            CLASSCODE=rr.getString("Classcode");
                                        }
                                        if(classOder>4)
                                        {
                                            String qq2="Delete from subjectrights where subjectcode='"+subcode+"' and teachercode='"+teachercode+"' and classcode='"+CLASS+"'";
                                            ps=con.prepareStatement(qq2);
                                            ps.execute();
                                        }
                                        else{
                                            String qq2="update subjectrights set classcode='"+CLASSCODE+"' where subjectcode='"+subcode+"' and teachercode='"+teachercode+"'";
                                            ps=con.prepareStatement(qq2);
                                            ps.execute();

                                        }
                                    }



                                    String ccode="",pcode="",ncode="",termname="";String pyear="";

                                    String sql4="Select termcode,termname,academicyear from terms where status='"+"current"+"'";
                                    ps=con.prepareStatement(sql4);
                                    ResultSet  rs2=ps.executeQuery();
                                    ResultSetMetaData meta=rs2.getMetaData();


                                    if(rs2.next())
                                    {
                                        CURRENTTERM=rs2.getString("termname");
                                        ccode=rs2.getString("Termcode");

                                        pyear=rs2.getString("academicYear");
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
                                    int nextyear=globals.academicYear()+1;

                                    String querry="Update terms set status='"+"current"+"',academicyear='"+nextyear+"' where termcode='"+ncode+"'";
                                    ps=con.prepareStatement(querry);
                                    ps.execute();

                                    String querry1="Update terms set status='"+"previous"+"',academicyear='"+nextyear+"' where termcode='"+ccode+"'";
                                    ps=con.prepareStatement(querry1);
                                    ps.execute();
                                    String querry2="Update terms set status='"+"next"+"',academicyear='"+nextyear+"' where termcode='"+pcode+"'";
                                    ps=con.prepareStatement(querry2);
                                    ps.execute();
                                    pcode=ccode;
                                    String querry3="Select termcode,termname,academicyear from terms where status='"+"current"+"'";
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


                                    String adm="",pycode="",streamcode="";

                                    int counter=0,classPress=0;

                                    String className="";
                                    String sql8="select * from admission   where currentform like '"+"FM"+"%' order by admissionnumber";
                                    ps=con.prepareStatement(sql8);
                                    ResultSet RS;
                                    RS=ps.executeQuery();
                                    while(RS.next())
                                    {

                                        adm=RS.getString("AdmissionNumber");
                                        streamcode=RS.getString("currentstream");
                                        classcode=RS.getString("Currentform");
                                        String sqlb="Select precision1 from classes where classcode='"+classcode+"' ";
                                        ps=con.prepareStatement(sqlb);
                                        ResultSet RSB=ps.executeQuery();
                                        if(RSB.next())
                                        {
                                            classPress=RSB.getInt("precision1");
                                        }

                                        String nclasscode="";
                                        int nclass=classPress+1;
                                        String sqk="Select classcode from classes where precision1='"+nclass+"'";
                                        ps=con.prepareStatement(sqk);
                                        ResultSet rsa=ps.executeQuery();
                                        if(rsa.next())
                                        {
                                            nclasscode=rsa.getString("Classcode");
                                        }


                                        if(nclass==5)
                                        {




                                            int year=globals.academicYear()-1;
                                            counter++;
                                            String sqlbook="Select * from issuedBooks where admnumber='"+adm+"'";
                                            ps=con.prepareStatement(sqlbook);
                                            rs=ps.executeQuery();
                                            while(rs.next())
                                            {
                                                String bkserial="",dateissued="",status="",datereturned="",issueid="";
                                                bkserial=rs.getString("Bookserial");
                                                dateissued=rs.getString("DateIssued");
                                                datereturned=rs.getString("Datereturned");
                                                status=rs.getString("Status");
                                                issueid=rs.getString("Issueid");
                                                if(datereturned==null)
                                                {
                                                    String sqltranfer="Insert into alumnibooksrecord (bookserial,dateissued,admnumber,status,issueid,yearofcompletion) values('"+bkserial+"','"+dateissued+"','"+adm+"','"+status+"','"+issueid+"','"+year+"')";
                                                    ps=con.prepareStatement(sqltranfer);
                                                    ps.execute();
                                                    String deleter="Delete from issuedbooks where issueid='"+issueid+"'";
                                                    ps=con.prepareStatement(deleter);
                                                    ps.execute();
                                                }
                                                else{
                                                    String sqltranfer="Insert into alumnibooksrecord (bookserial,dateissued,admnumber,status,issueid,datereturned,yearofcompletion) values('"+bkserial+"','"+dateissued+"','"+adm+"','"+status+"','"+issueid+"','"+datereturned+"','"+year+"')";
                                                    ps=con.prepareStatement(sqltranfer);
                                                    ps.execute();
                                                    String deleter="Delete from issuedbooks where issueid='"+issueid+"'";
                                                    ps=con.prepareStatement(deleter);
                                                    ps.execute();
                                                }

                                            }
                                            String sqs="Insert into completionclasslists values('"+adm+"','"+year+"')";
                                            ps=con.prepareStatement(sqs);
                                            ps.execute();
                                            String sqla="Update admission set currentterm='"+ccode+"',currentform='"+nclasscode+"' where admissionnumber='"+adm+"'";
                                            ps=con.prepareStatement(sqla);
                                            ps.execute();

                                        }
                                        else if(nclass<5){


                                            String sqla="Update admission set currentterm='"+ccode+"',currentform='"+nclasscode+"' where admissionnumber='"+adm+"'";
                                            ps=con.prepareStatement(sqla);
                                            ps.execute();

                                            if(classcode.equalsIgnoreCase(f1code)||classcode.equalsIgnoreCase(f2code)||classcode.equalsIgnoreCase(f3code))
                                            {
                                                String qrry="Select * from studentsubjectallocation where admnumber='"+adm+"' and academicyear='"+(globals.academicYear()-1)+"'"  ;
                                                ps=con.prepareStatement(qrry);
                                                ResultSet rx=ps.executeQuery();
                                                while(rx.next())
                                                {String subcode=rx.getString("Subjectcode");
                                                    String qr="Insert into studentsubjectallocation values('"+subcode+"','"+adm+"','"+globals.academicYear()+"')";
                                                    ps=con.prepareStatement(qr);
                                                    ps.execute();
                                                }
                                            }

                                            String program=globals.studentProgram(adm);
                                            String sql7 = "Select amountperhead,voteheadid from studentpayablevoteheads where termcode='" + globals.currentTerm() + "' and academicYear='" + globals.academicYear() + "'  and amountperhead!='" + "" + "' and amountperhead!='" + "0" + "' and classcode='"+nclasscode+"' and program='"+program+"'";
                                            ps = con.prepareStatement(sql7);
                                            rs = ps.executeQuery();
                                            while (rs.next()) {
                                                fee = rs.getDouble("amountperhead");
                                                voteheadid = rs.getString("voteheadid");

                                                String sql12 = "Insert into payablevoteheadperstudent values('" + adm + "','" + globals.termname(ccode) + "','" + globals.academicYear() + "','" + voteheadid + "','" + fee + "',curDate(),'" + "INV" + "')";
                                                ps = con.prepareStatement(sql12);
                                                ps.execute();
                                                openingbal = 0;

                                                sql9 = "Update admission set currentterm='" + ccode + "',currentform='" + nclasscode + "' where admissionnumber='" + adm + "'";
                                                ps = con.prepareStatement(sql9);
                                                ps.execute();

                                            }


                                        }


                                    }
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
                                            int weight=0;

                                            String examcode1=ExamCodesGenerator.generatecode(classname, String.valueOf((globals.academicYear()-1)), CURRENTTERM, name.toUpperCase());
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
                                    String querry22="update systemconfiguration set status='"+"False"+"' where configurationid='"+"CO024"+"'";
                                    ps=con.prepareStatement(querry22);
                                    ps.execute();
                                    map.put("responseCode","200");
                                    map.put("responseDescription","Class Migration Succesfull");
                                    return map;





                            }
                            else{
                                map.put("responseCode","300");
                                map.put("responseDescription","Finance Department Not Ready For Class Migration");
                                return map;

                            }










                        } catch (SQLException ex) {
                            map.put("responseCode","501");
                            map.put("responseDescription","Error:"+ex.toString());
                            return map;
                        }






                    }
                    else{
                        map.put("responseCode","501");
                        map.put("responseDescription","Error:"+"Cannot Perform class migration at Term "+press);
                        return map;

                    }
                } catch (SQLException sq) {
                    map.put("responseCode","501");
                    map.put("responseDescription","Error:"+sq.toString());
                    return map;
                }







    }
}
