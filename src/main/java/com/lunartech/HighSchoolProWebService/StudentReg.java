package com.lunartech.HighSchoolProWebService;


import java.awt.*;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentReg {
    String schoolDb;
    Globals globals;
ConfigurationIntialiser configurationIntialiser;
    public StudentReg(String schoolDb) {
        this.schoolDb = schoolDb;
        globals= new Globals(schoolDb);
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);

    }

    public String  register(String adm, String upi, String firstName, String middleName, String lastName, String gender, String dob, String doa, String classAdmited, String streamadmitted, String termadmitted, String currentTerm, String currentClass, String currentStream, String kcpe, String country, String county, String province, String constituency, String ward, String parent, String tell1 , String tell2, String imagefolder, String program)

    {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        String DOB=dob;
        String DOA=doa;
        String constituencycode="",wardcode="",provincecode="",countycode="",countrycode="",formcodea="",formcodec="",termcodea="",termcodec="",streamcodea="",streamcodec="";
        boolean comply=true;



        try {
            String sql="Select countrycode from countries where countryname='"+country+"'";
            ps=con.prepareStatement(sql);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                countrycode=rs.getString("Countrycode");

            }
            String sql2="Select provincecode from provinces where provincename='"+province+"'";
            ps=con.prepareStatement(sql2);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                provincecode=rs.getString("provincecode");
            }

            String sql3="Select countycode from counties where countyname='"+county+"'";
            ps=con.prepareStatement(sql3);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                countycode=rs.getString("Countycode");
            }
            String sql4="Select constituencycode from constituencies where constituencyname='"+constituency+"'";
            ps=con.prepareStatement(sql4);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                constituencycode=rs.getString("constituencycode");
            }
            String sql5="Select wardcode from ward where wardname='"+ward+"'";
            ps=con.prepareStatement(sql5);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                wardcode=rs.getString("wardcode");
            }
            String sql6="Select  classcode from classes where classname='"+classAdmited+"'";
            ps=con.prepareStatement(sql6);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                formcodea=rs.getString("classcode");
            }
            String sql7="Select  classcode from classes where classname='"+currentClass+"'";
            ps=con.prepareStatement(sql7);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                formcodec=rs.getString("classcode");
            }
            String sql8="Select* from streams where streamname='"+streamadmitted+"'";
            ps=con.prepareStatement(sql8);
            rs=ps.executeQuery();
            while(rs.next())
            {
                streamcodea=rs.getString("Streamcode");

            }
            String sql9="Select* from streams where streamname='"+currentStream+"'";
            ps=con.prepareStatement(sql9);
            rs=ps.executeQuery();
            while(rs.next())
            {
                streamcodec=rs.getString("Streamcode");
            }
            String querry="Select termcode from terms where termname='"+termadmitted+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                termcodea=rs.getString("Termcode");

            }


                String querry3="Select firstname,lastname,middlename from admission where admissionnumber='"+adm+"'";
                ps=con.prepareStatement(querry3);
                ResultSet Rs=ps.executeQuery();
                if(Rs.next())
                {
                    return ( "Admission Number "+adm+" Is Bieng Used By "+Rs.getString("firstName")+"  "+Rs.getString("MiddleName")+" "+Rs.getString("LastName")+" \n Admission Numbers Can Never Be Shared");
                }
                else{

                    if(imagefolder==null||imagefolder.isEmpty())
                    {
                        if(DOB.isEmpty())
                        {
                            DOB="2000:01:01";
                        }
                        if(DOA.isEmpty())
                        {
                            DOA="2015:01:01";
                        }
                        String SQL="Insert into admission values('"+firstName.toUpperCase()+"','"+middleName.toUpperCase()+"','"+lastName.toUpperCase()+"','"+gender+"'"
                                + ",'"+DOB+"','"+DOA+"','"+formcodea+"','"+termcodea+"','"+streamcodea+"','"+formcodec+"','"+globals.currentTerm()+"','"+streamcodec+"','"+kcpe+"','"+countrycode+"','"+provincecode+"',"
                                + "'"+countycode+"','"+constituencycode+"','"+wardcode+"','"+parent+"','"+tell1+"','"+tell2+"','"+adm+"',?,'"+upi+"','"+program+"')";

                        ps=con.prepareStatement(SQL);
                        ps.setString(1, configurationIntialiser.imageFolder());
                        ps.execute();
                    }
                    else{


                        if(DOB.isEmpty())
                        {
                            DOB="2000:01:01";
                        }
                        if(DOA.isEmpty())
                        {
                            DOA="2015:01:01";
                        }
                        String SQL="Insert into admission values('"+firstName.toUpperCase()+"','"+middleName.toUpperCase()+"','"+lastName.toUpperCase()+"','"+gender+"'"
                                + ",'"+DOB+"','"+DOA+"','"+formcodea+"','"+termcodea+"','"+streamcodea+"','"+formcodec+"','"+globals.currentTerm()+"','"+streamcodec+"','"+kcpe+"','"+countrycode+"','"+provincecode+"',"
                                + "'"+countycode+"','"+constituencycode+"','"+wardcode+"','"+parent+"','"+tell1+"','"+tell2+"','"+adm+"',?,'"+upi+"','"+program+"')";

                        ps=con.prepareStatement(SQL);
                        ps.setString(1, configurationIntialiser.imageFolder()+"/"+imagefolder+".jpg");
                        ps.execute();



                    }


                    return  "success";
                }




        } catch ( SQLException sq) {

                sq.printStackTrace();
                return sq.getMessage();
            }
        finally {
                try {
                    con.close();

                } catch (SQLException sq) {

                    sq.printStackTrace();
                }

            }










    }


    public  void updateStudentImage(String image,String ADM)
    {Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {



            String SQL="update admission set ImageLocation=? where admissionNumber='"+ADM+"'";

            ps=con.prepareStatement(SQL);
            ps.setString(1, configurationIntialiser.imageFolder()+"/"+ADM+".jpg");
            ps.execute();


        }
        catch(Exception sq)
        {
            sq.printStackTrace();
        }finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }
}
