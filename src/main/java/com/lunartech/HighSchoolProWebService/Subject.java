package com.lunartech.HighSchoolProWebService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Subject {

    String schoolDb;
Globals globals;
ReportGenerator reportGenerator;
    public Subject(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        reportGenerator=new ReportGenerator(schoolDb);
    }

    public  String allocateSubject(String className, String subjectName, String stream, String academicYear, String admissionNumber, String mode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String classcode=globals.classCode(className);
        String subjectcode=globals.subjectCode(subjectName);

        try {

            if(mode.equalsIgnoreCase("by class"))
            {

                String sql="Select admissionnumber from admission where  currentform='"+classcode+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next())
                {

                    String adm=rs.getString("AdmissionNumber");
                    String sql2="Select * from studentsubjectallocation where admnumber='"+adm+"' and academicyear='"+academicYear+"' and subjectcode='"+subjectcode+"'";
                    ps=con.prepareStatement(sql2);
                    ResultSet RS=ps.executeQuery();
                    if(RS.next())
                    {

                    }
                    else{
                        String sql3="Insert into studentsubjectallocation values('"+subjectcode+"','"+adm+"','"+academicYear+"')";
                        ps=con.prepareStatement(sql3);
                        ps.execute();
                    }
                }
                return ( "success");
            }
            else if (mode.equalsIgnoreCase("by stream")){


String streamcode=globals.streamcode(stream);
                String sql="Select admissionnumber from admission where  currentform='"+classcode+"' and currentstream='"+streamcode+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next())
                {

                    String adm=rs.getString("AdmissionNumber");
                    String sql2="Select * from studentsubjectallocation where academicyear='"+academicYear+"' and subjectcode='"+subjectcode+"' and subjectcode='"+subjectcode+"' and admnumber='"+adm+"'";
                    ps=con.prepareStatement(sql2);
                    ResultSet RS=ps.executeQuery();
                    if(RS.next())
                    {
                     // return (globals.fullName(adm)+" Was Previously Allocated This Subject In This Academic Year");
                    }
                    else{
                        String sql3="Insert into studentsubjectallocation values('"+subjectcode+"','"+adm+"','"+academicYear+"')";
                        ps=con.prepareStatement(sql3);
                        ps.execute();
                    }
                }
               return ("success");


            }
            else if(mode.equalsIgnoreCase("by admission"))
            {

                String sql="Select admissionnumber from admission where admissionnumber='"+admissionNumber+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    subjectcode=globals.subjectCode(subjectName);
                    String adm=admissionNumber;
                    String sql2="Select * from studentsubjectallocation where academicyear='"+academicYear+"' and subjectcode='"+subjectcode+"' and admnumber='"+adm+"'";
                    ps=con.prepareStatement(sql2);
                    ResultSet RS=ps.executeQuery();
                    if(RS.next())
                    {
                      //  return ( globals.fullName(adm)+" Was Previously Allocated This Subject In This Academic Year");
                    }
                    else{
                        String sql3="Insert into studentsubjectallocation values('"+subjectcode+"','"+adm+"','"+academicYear+"')";
                        ps=con.prepareStatement(sql3);
                        ps.execute();
                    }

                    return (globals.fullName(adm)+" success");
                }
                else{
                   return ( "Invalid Admission Number");
                }

            }
            else {
                return "No Implementation";
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return ""+e.getMessage();
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public  String deAllocateSubject(String className,String subjectName,String stream,String academicYear,String admissionNumber,String mode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String classcode=globals.classCode(className);
        String subjectcode=globals.subjectCode(subjectName);

        try {

            if(mode.equalsIgnoreCase("by class"))
            {


                String sql="Select admissionnumber from admission where  currentform='"+classcode+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next())
                {

                    String adm=rs.getString("admissionNumber");
                    String sql2="Delete from studentsubjectallocation where subjectcode='"+subjectcode+"' and admnumber='"+adm+"' and academicyear='"+academicYear+"'";
                    ps=con.prepareStatement(sql2);
                    ps.execute();
                }
                return ( "success");
            }
            else if (mode.equalsIgnoreCase("by stream")){
                String streamcode=globals.streamcode(stream);


                String sql="Select admissionnumber from admission where  currentform='"+classcode+"' and currentstream='"+streamcode+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next())
                {

                    String adm=rs.getString("admissionNumber");
                    String sql2="Delete from studentsubjectallocation where subjectcode='"+subjectcode+"' and admnumber='"+adm+"' and academicyear='"+academicYear+"'";
                    ps=con.prepareStatement(sql2);
                    ps.execute();
                }
                return ("success");


            }
            else if(mode.equalsIgnoreCase("by admission"))
            {

                String sql="Select admissionnumber from admission where admissionnumber='"+admissionNumber+"'";
                ps=con.prepareStatement(sql);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    subjectcode=globals.subjectCode(subjectName);

                    String adm=rs.getString("admissionNumber");
                    String sql2="Delete from studentsubjectallocation where subjectcode='"+subjectcode+"' and admnumber='"+adm+"' and academicyear='"+academicYear+"'";
                    ps=con.prepareStatement(sql2);
                    ps.execute();

                    return (" success");
                }
                else{
                    return ( "Invalid Admission Number");
                }

            }
            else {
                return "No Implementation";
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return ""+e.getMessage();
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public  String configureGradingSystem(String subjectName,String className,int increamentalValue)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);


        try {
            String subcode=globals.subjectCode(subjectName);
            String classcode=globals.classCode(className);

            int increametvalue=increamentalValue;
            String sql="Select start_from,end_at,grade,classcode,subjectcode,sortcode from subjectgrading where  subjectgrading.subjectcode='"+subcode+"' and classcode='"+classcode+"'  order by sortcode";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                String sortcode=rs.getString("Sortcode");
                int min=rs.getInt("Start_from");
                int max=rs.getInt("End_at");
                min+=increametvalue;
                max+=increametvalue;
                if(max>100)
                {
                    max=100;
                }
                if(min<0)
                {
                    min=0;
                }
                if(sortcode.equalsIgnoreCase("1"))
                {
                    max=100;
                }
                if(sortcode.equalsIgnoreCase("12"))
                {
                    min=0;
                }
                String sql2="Update subjectgrading set start_from='"+min+"',end_at='"+max+"' where classcode='"+classcode+"' and subjectcode='"+subcode+"' and sortcode='"+sortcode+"'";
                ps=con.prepareStatement(sql2);
                ps.execute();

            }
           return "success";

        } catch (Exception sq) {
            sq.printStackTrace();
            return "Failed "+sq.getMessage();
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();

            }

        }


    }
public   void allocationReport(String className,String academicYear,String stream)
{
    reportGenerator.subjectAllocationReport(className, academicYear,stream);
}




public  Map performanceIndicator(String className,String subjectName,String examCode,double score,double outof)
{

    Connection con;
    PreparedStatement ps = null;
    ResultSet rs;
    con = DbConnection.connectDb(schoolDb);
    Map map=new HashMap();
int percent= (int) ((score/outof)*100);
map.put("percent",percent);
    try {
        double weight=0;
        String sql2 = "Select weight from examweights where examcode='" + examCode + "'";
        ps = con.prepareStatement(sql2);
        rs = ps.executeQuery();
        if (rs.next()) {


            weight=rs.getInt("Weight");
            map.put("convertedscoreoutof",weight);
        }
        int converted = (int) ((score * weight) / outof);

        map.put("convertedscore",converted);

        String sql = "Select grade,end_at,start_from from subjectgrading where classcode='" + globals.classCode(className) + "' and subjectcode='" + globals.subjectCode(subjectName) + "' and '" + percent+ "'>=start_from and '" + percent + "'<=end_at  group by sortcode";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        if (rs.next()) {


            map.put("grade",rs.getString("grade"));
            String qq = "Select points from points_for_each_grade where grade='" + rs.getString("grade") + "'";
            ps = con.prepareStatement(qq);
            rs = ps.executeQuery();
            if (rs.next()) {

                map.put("points",rs.getString("points"));
            }
        }

    }
    catch (Exception sq)
    {
        sq.printStackTrace();
    }
    finally {
        try {
            con.close();

        } catch (SQLException sq) {

            sq.printStackTrace();
        }

    }

    return map;

}
}
