package com.lunartech.HighSchoolProWebService;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author FRED
 */
public class Globals {
    public  final String basePath="";

    public  String CurrentUser="FREDDY";

    public  String activationStatus;
    public  String Level="ADmin";
    public  String empcode;
    public  String depcode;
    public  String depName;
    public  String initials;
    public  String feel;
    public  String moduleName;
    public  String look="";
    public  TrayIcon systemTray;
    public  String composerRecipient="";
    public  String singleAdmissionAllocator="";
    public  String singleyearAllocator="";
    public  int pictureWidth=1280;
    public  int pictureHeight=800;

    public  String sortcode=" order by admissionNumber ";
    public  String phoneCollection="";
    ConfigurationIntialiser configurationIntialiser;

    public Globals(String schoolDb) {
        this.schoolDb = schoolDb;
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    String schoolDb;

    public  double totalTargetPoints(String classcode,String academicYear) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select points from examtargets where classcode ='" + classcode + "' and academicYear='"+academicYear+"' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getDouble("points");
            } else {

                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }

    public  String targetGrade(String ExamCode,String subjectCode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select grade from examtargets where examcode ='" + ExamCode + "' and subjectcode='"+subjectCode+"' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("grade");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }
    public  double targetPoints(String ExamCode,String subjectCode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select points from examtargets where classcode ='" + ExamCode + "' and subjectcode='"+subjectCode+"' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getDouble("points");
            } else {

                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }



    public  void SortIntialiser() {
        if(configurationIntialiser.admissionSort())
        {
            sortcode=" order by admissionNumber  ";
        }
        else{
            sortcode=" order by upi  ";
        }


    }



    public  String Grade(String score) {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            if(score==null || score.isEmpty())
            {
                return "";
            }
            else{
                String sqll = "Select grade from kcpetable where  '" + score + "'>=starting_from and '" + score + "'<=ending_at  group by sort_code";
                ps = con.prepareStatement(sqll);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {

                    return (RS.getString("grade"));

                }
                else{
                    return "";
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();
                //
            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }


    public  boolean gradableeditted(String adm,String examcode,String academicYear) {
        Connection con;
        String analysisMode;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            int entries=0;
            String sql = "Select count(*) from examanalysistable where examcode='" + examcode + "' and admnumber='"+adm+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                entries=rs.getInt("count(*)");
            }

            int subtaking=subjectAllocationCounter(adm, academicYear);
            String sql2="Select AnalysisMode from examanalysismodes  where examcode='"+examcode+"'";
            ps=con.prepareStatement(sql2);
            rs=ps.executeQuery();
            if(rs.next())
            {
                analysisMode=rs.getString("AnalysisMode");
            }
            else{
                analysisMode="All";
            }

            if(analysisMode.equalsIgnoreCase("By Seven"))
            {
                int langcount=0, sciencecount=0,humancount=0,mathcount=0,techcount=0;
                int count=0;
                ps=con.prepareStatement("Select distinct category from subjects");
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String category=rs.getString("Category");

                    int subcount=0;
//                    ps=con.prepareStatement("Select subjectcode from subjects where category='"+category+"'");
//                    ResultSet rx=ps.executeQuery();
//                    while(rx.next())
//                    {
//                        String subjectcode=rx.getString("SubjectCode");
//
//                                             ps=con.prepareStatement("Select DISTINCT examanalysistable.SUBJECTCODE from examanalysistable,subjects where examcode='"+examcode+"'  and subjectcode='"+category+"' and admNumber='"+adm+"' and  subjects.subjectcode =examanalysistable.subjectcode");
//                      ResultSet rr=ps.executeQuery();
//                      while(rr.next())
//                      {
//
//                        subcount++;
//                          System.err.println(rr.getString("examanalysistable.SUBJECTCODE"));
//                      }
//                        System.err.println("adm:"+adm);
//                        System.err.println("The subuject Count "+subcount);
//                    }

                    if(category.equalsIgnoreCase("Languages"))
                    {
                        ps=con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='"+examcode+"'  and category='"+category+"' and admNumber='"+adm+"' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {

                            langcount++;

                        }
                        if(langcount>2)
                        {
                            langcount=2;
                        }
                        count=count+langcount;

                    }
                    else if(category.equalsIgnoreCase("Mathematics"))
                    {
                        ps=con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='"+examcode+"'  and category='"+category+"' and admNumber='"+adm+"' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {

                            mathcount++;

                        }
                        if(mathcount>1)
                        {
                            mathcount=1;
                        }
                        count=count+mathcount;

                    }
                    else if(category.equalsIgnoreCase("Sciences"))
                    {
                        ps=con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='"+examcode+"'  and category='"+category+"' and admNumber='"+adm+"' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {

                            sciencecount++;

                        }
                        if(sciencecount>2)
                        {
                            sciencecount=2;
                        }
                        count=count+sciencecount;

                    }
                    else if(category.equalsIgnoreCase("Humanities")||category.equalsIgnoreCase("Technical"))
                    {
                        ps=con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='"+examcode+"'  and category='"+category+"' and admNumber='"+adm+"' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {

                            humancount=humancount+1;

                        }


                    }


                }

                if(humancount>2)
                {
                    humancount=2;
                }
                count=count+humancount;
                entries=count;






                if(entries<7)
                {
                    return false;
                }
                else{
                    return  true;
                }
            }
            else{
                System.err.println("Entries"+entries);
                System.err.println("Allocation"+subtaking);
                if(subtaking==entries|| entries>subtaking)
                {
                    return true;
                }
                else{
                    return false;
                }

            }




        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }
    }


    public  String subjectCode(String subjectname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select subjectcode from subjects where subjectname='" + subjectname + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("subjectcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }



    public  boolean gradable(String adm,String examcode,String academicYear) {
        Connection con;
        String analysisMode;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            int entries=0;
            String sql = "Select count(*) from examanalysistable where examcode='" + examcode + "' and admnumber='"+adm+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                entries=rs.getInt("count(*)");
            }

            int subtaking=subjectAllocationCounter(adm, academicYear);
            String sql2="Select AnalysisMode from examanalysismodes  where examcode='"+examcode+"'";
            ps=con.prepareStatement(sql2);
            rs=ps.executeQuery();
            if(rs.next())
            {
                analysisMode=rs.getString("AnalysisMode");
            }
            else{
                analysisMode="All";
            }

            if(analysisMode.equalsIgnoreCase("By Seven"))
            {
                if(entries<7)
                {
                    return false;
                }
                else{
                    return  true;
                }
            }
            else{

                if(subtaking==entries|| entries>subtaking)
                {
                    return true;
                }
                else{
                    return false;
                }

            }




        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }


    public  int subjectAllocationCounter(String adm,String academicYear)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select count(*) from studentsubjectallocation where admnumber='"+adm+"'  and academicyear='"+academicYear+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("Count(*)");
            } else {
                return 11;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 11;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }


    public  double feeArrears(String adm,String academicYear,String term)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select amount from feearrears where adm='"+adm+"'  and academicyear='"+academicYear+"' and term='"+term+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getDouble("amount");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }



    public  double nextTermFee(String admNumber,String termCode,String year,String classcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String program=studentProgram(admNumber);
            double nextTermFee=0;
            String ss = "Select sum(AmountPerHead) from studentpayablevoteheads where   termcode='" + termCode + "' and academicYear='" + year + "' and classcode='"+classcode+"' and program='"+program+"'";
            ps = con.prepareStatement(ss);
            rs = ps.executeQuery();
            if (rs.next()) {
                nextTermFee =  rs.getDouble("sum(AmountPerHead)") ;
            }




            return nextTermFee;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }


    public  int nextAdmissionNumber()
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        int max=0,next;
        try {

            String sql = "Select max(AdmissionNumber) from admission ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                max= rs.getInt("max(admissionNumber)");
            }

            next=max+1;
            return next;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }

    public  void main(String[] args) {
        kcpeAnalysis(classCode("Form 1"));
    }
    public  boolean takingSubject(String adm,String academicYear,String subcode)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select * from studentsubjectallocation where admnumber='"+adm+"' and subjectcode='"+subcode+"' and academicyear='"+academicYear+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }


    public  String subjectName(String subjectcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select subjectname from subjects where subjectcode='" + subjectcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("subjectNAme");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }
    public  String studentProgram(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select program from admission where admissionNumber='" + adm + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("program");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }
     int presentsmsCounter=0;
     int presentsentsmsCounter=0;

    public  void MessageinTracker()
    {

        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql = "Select count(*) from ozekimessagein ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                presentsmsCounter= rs.getInt("count(*)");
            }

            new Thread(){
                public void run()
                {
                    for(;;)
                    {
                        try {

                            sleep(1000);
                            int presentsmsCounter2=0;
                            String sql = "Select count(*) from ozekimessagein ";
                            PreparedStatement   pss = con.prepareStatement(sql);
                            ResultSet  rss = pss.executeQuery();
                            if (rss.next()) {
                                presentsmsCounter2= rss.getInt("count(*)");
                            }
                            int messages;
                            messages=presentsmsCounter2-presentsmsCounter;


                            if(presentsmsCounter2>presentsmsCounter)
                            {
                                systemTray.displayMessage("New Message",messages+ " New Message(s) Received", TrayIcon.MessageType.INFO);
                            }
                            presentsmsCounter=presentsmsCounter2;
                        } catch (Exception e) {
                        }
                    }
                }

            }.start();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }



    public  void MessageOutTracker()
    {

        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql = "Select count(*) from messagelog where statuscode='"+"200"+"' or statuscode='"+"201"+"'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                presentsentsmsCounter= rs.getInt("count(*)");
            }

            new Thread(){
                public void run()
                {
                    for(;;)
                    {
                        try {

                            sleep(1000);
                            int presentsmsCounter2=0;
                            String sql = "Select count(*) from messagelog where statuscode='"+"200"+"' or statuscode='"+"201"+"'";
                            PreparedStatement   pss = con.prepareStatement(sql);
                            ResultSet  rss = pss.executeQuery();
                            if (rss.next()) {
                                presentsmsCounter2= rss.getInt("count(*)");
                            }

                            int messages;
                            messages=presentsmsCounter2-presentsentsmsCounter;


                            if(presentsmsCounter2>presentsentsmsCounter)
                            {
                                systemTray.displayMessage("Message Sent",messages+ " New Message(s) sent", TrayIcon.MessageType.INFO);
                            }
                            presentsentsmsCounter=presentsmsCounter2;
                        } catch (Exception e) {
                        }
                    }
                }

            }.start();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    public  ArrayList exams() {
        ArrayList<Map> resultArrayList=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con=DbConnection.connectDb(schoolDb);
        try {


            String sql9="Select * from exams group by examname";
            ps=con.prepareStatement(sql9);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("examName",rs.getString("examname")) ;
                map.put("examCode",rs.getString("examcode")) ;
                resultArrayList.add(map);

            }
            return resultArrayList;
        }
        catch (Exception sq)
        {

            map.put("responcecode","501");
            map.put("responceDescription","Internal Error :"+sq.getMessage());
            resultArrayList.add(map);
            sq.printStackTrace();
            return resultArrayList;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }

    public  ArrayList years() {
        ArrayList<Map> resultArrayList=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();

        try {


            for(int k=2015;k<=academicYear();++k)
            {
                map=new HashMap<String,String>();
                map.put("year",String.valueOf(k)) ;
                resultArrayList.add(map);


            }

            return resultArrayList;
        }
        catch (Exception sq)
        {

            map.put("responcecode","501");
            map.put("responceDescription","Internal Error :"+sq.getMessage());
            resultArrayList.add(map);
            sq.printStackTrace();
            return resultArrayList;
        }

    }

    public  ArrayList streams() {
        ArrayList<Map> resultArrayList=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con=DbConnection.connectDb(schoolDb);
        try {


            String sql9="Select * from streams";
            ps=con.prepareStatement(sql9);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("streamName",rs.getString("streamname")) ;
                resultArrayList.add(map);

            }
            return resultArrayList;
        }
        catch (Exception sq)
        {

            map.put("responcecode","501");
            map.put("responceDescription","Internal Error :"+sq.getMessage());
            resultArrayList.add(map);
            sq.printStackTrace();
            return resultArrayList;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }

    public  ArrayList terms() {
        ArrayList<Map> resultArrayList=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con=DbConnection.connectDb(schoolDb);
        try {


            String sql9="Select * from terms order by precisions";
            ps=con.prepareStatement(sql9);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
               map.put("termName",rs.getString("termname")) ;
               resultArrayList.add(map);

            }
            return resultArrayList;
        }
        catch (Exception sq)
        {

            map.put("responcecode","501");
            map.put("responceDescription","Internal Error :"+sq.getMessage());
            resultArrayList.add(map);
            sq.printStackTrace();
            return resultArrayList;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }
    }

    public  ArrayList classlevels()
{
    ArrayList<Map> arr=new ArrayList<Map>();
    Map<String,String> map=new HashMap<String,String>();
    Connection con=DbConnection.connectDb(schoolDb);;
    PreparedStatement ps = null;
    ResultSet rs;
    try {





        String sqls="Select *  from classes order by precision1 ";
        ps=con.prepareStatement(sqls);
        rs=ps.executeQuery();
        ResultSetMetaData data= rs.getMetaData();

        while(rs.next())
        {
             map=new HashMap<String,String>();
map.put("classCode", rs.getString("Classcode"));
            map.put("className", rs.getString("Classname"));
            arr.add(map);
           
        }

        return arr;
    } catch (Exception sq)
    {

        map.put("responcecode","501");
        map.put("responceDescription","Internal Error :"+sq.getMessage());
        arr.add(map);
        sq.printStackTrace();
        return arr;
    }
    finally {
        try {
            con.close();

        } catch (SQLException sq) {
            
            sq.printStackTrace();
        }

    }

}


public  ArrayList subjects()
{
    ArrayList<Map> arr=new ArrayList<Map>();
    Map<String,String> map=new HashMap<String,String>();
    Connection con=DbConnection.connectDb(schoolDb);;
    PreparedStatement ps = null;
    ResultSet rs;
    try {





        String sqls="Select *  from subjects order by subjectcode";
        ps=con.prepareStatement(sqls);
        rs=ps.executeQuery();
        ResultSetMetaData data= rs.getMetaData();

        while(rs.next())
        {
            map=new HashMap<String,String>();
            map.put("subjectCode", rs.getString("subjectcode"));
            map.put("subjectName", rs.getString("subjectname"));
            arr.add(map);

        }

        return arr;
    } catch (Exception sq)
    {

        map.put("responseCode","501");
        map.put("responseDescription","Internal Error :"+sq.getMessage());
        arr.add(map);
        sq.printStackTrace();
        return arr;
    }
    finally {
        try {
            con.close();

        } catch (SQLException sq) {

            sq.printStackTrace();
        }

    }

}


    public  ArrayList subjectsGradingSystem(String subjectName)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select start_from,end_at,grade,subjectgrading.classcode,subjects.subjectcode,subjectname,classname from subjectgrading,subjects,classes where subjects.subjectcode=subjectgrading.subjectcode and classes.classcode=subjectgrading.classcode and subjectgrading.subjectcode='"+subjectCode(subjectName)+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("subjectCode", rs.getString("subjectcode"));
                map.put("subjectName", rs.getString("subjectname"));
                map.put("startAt",rs.getString("start_from"));
                map.put("endAt",rs.getString("end_at"));
                map.put("className",rs.getString("className"));
                map.put("grade",rs.getString("grade"));
                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  ArrayList classGradingSystem(String className)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select start_from,end_at,grade,subjectgrading.classcode,subjects.subjectcode,subjectname,classname from subjectgrading,subjects,classes where subjects.subjectcode=subjectgrading.subjectcode and classes.classcode=subjectgrading.classcode and subjectgrading.classcode='"+classCode(className)+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("subjectCode", rs.getString("subjectcode"));
                map.put("subjectName", rs.getString("subjectname"));
                map.put("startAt",rs.getString("start_from"));
                map.put("endAt",rs.getString("end_at"));
                map.put("className",rs.getString("className"));
                map.put("grade",rs.getString("grade"));
                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public  ArrayList classTeacherComment(String className)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            String querry;


if(className.equalsIgnoreCase("All"))
{
     querry="SElect * from teachersgeneralcomments";

}
else{
    querry="SElect * from teachersgeneralcomments where classcode='"+classCode(className)+"'";
}


            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("className", className(rs.getString("classcode")));
                map.put("grade", rs.getString("grade"));
                map.put("comment",rs.getString("comments"));

                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  ArrayList subjectComment(String subjectName)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            String querry;


            if(subjectName.equalsIgnoreCase("All"))
            {
                querry="SElect * from subjectcomments";

            }
            else{
                querry="SElect * from subjectcomments where subjectCode='"+subjectCode(subjectName)+"'";
            }


            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("subjectName", subjectName(rs.getString("subjectCode")));
                map.put("grade", rs.getString("grade"));
                map.put("comment",rs.getString("comment"));

                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  ArrayList principalComment()
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            String querry;


                querry="SElect * from principalcomments ";



            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();

                map.put("grade", rs.getString("grade"));
                map.put("comment",rs.getString("comments"));

                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public  Map saveTeacherComment(String className,String grade,String comment)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String sql="UPdate teachersgeneralcomments set comments='"+comment+"' where grade='"+grade+"' and classcode='"+classCode(className)+"'";
            ps=con.prepareStatement(sql);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","Comment Saved");
            return map;


        } catch ( SQLException sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.getMessage());
            return map;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  Map saveSubjectComment(String subjectName,String grade,String comment)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String sql="UPdate subjectComments set comment='"+comment+"' where grade='"+grade+"' and subjectCode='"+subjectCode(subjectName)+"'";
            ps=con.prepareStatement(sql);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","Comment Saved");
            return map;


        } catch ( SQLException sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.getMessage());
            return map;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  Map savePrincipalComment(String grade,String comment)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String sql="UPdate principalComments set comments='"+comment+"' where grade='"+grade+"' ";
            ps=con.prepareStatement(sql);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","Comment Saved");
            return map;


        } catch ( SQLException sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.getMessage());
            return map;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public  ArrayList classSubjectsGradingSystem(String subjectname,String classname)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select start_from,end_at,grade,subjectgrading.classcode,subjects.subjectcode,subjectname,classname from subjectgrading,subjects,classes where subjects.subjectcode=subjectgrading.subjectcode and classes.classcode=subjectgrading.classcode and subjectgrading.subjectcode='"+subjectCode(subjectname)+"' and subjectgrading.classcode='"+classCode(classname)+"' order  by sortcode";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("subjectCode", rs.getString("subjectcode"));
                map.put("subjectName", rs.getString("subjectname"));
                map.put("startAt",rs.getString("start_from"));
                map.put("endAt",rs.getString("end_at"));
                map.put("className",rs.getString("className"));
                map.put("grade",rs.getString("grade"));
                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }
    public  ArrayList subjectsGradingSystems()
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select start_from,end_at,grade,subjectgrading.classcode,subjects.subjectcode,subjectname,classname from subjectgrading,subjects,classes where subjects.subjectcode=subjectgrading.subjectcode and classes.classcode=subjectgrading.classcode";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("subjectCode", rs.getString("subjectcode"));
                map.put("subjectName", rs.getString("subjectname"));
                map.put("startAt",rs.getString("start_from"));
                map.put("endAt",rs.getString("end_at"));
                map.put("className",rs.getString("className"));
                map.put("grade",rs.getString("grade"));
                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }


public   String schoolName()
{


    Connection con;
    PreparedStatement ps = null;
    ResultSet rs;
    con = DbConnection.connectDb(schoolDb);
    try {
        String sq = "Select * from schooldetails";
        ps = con.prepareStatement(sq);
        rs = ps.executeQuery();
        String name = "";
        if (rs.next()) {
           return name = rs.getString("Name");
        }
        else {
            return "";
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "";
    } finally {
        try {
            con.close();

        } catch (SQLException sq) {

            sq.printStackTrace();
        }

    }


}





    public  String currentTerm() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql = "Select termcode from terms where status='" + "Current" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }


    public  int termPrecisionDeterminer(String termIdentifier) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select Precisions from terms where termcode='" + termIdentifier + "' or termname='"+termIdentifier+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("Precisions");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }

    public  int classPrecisionDeterminer(String classIdentifier) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select precision1 from classes where classcode='" + classIdentifier + "' or classname='"+classIdentifier+"'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("precision1");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }
    public  double balanceCalculator(String admNumber) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            double paid=0,payable=0;
            String sql = "Select sum(amount) from payablevoteheadperstudent where admnumber ='" + admNumber + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                payable= rs.getInt("sum(amount)");
            }

            ps=con.prepareStatement("Select sum(amount) from reciepts where admnumber ='" + admNumber + "' ")  ;


            rs = ps.executeQuery();
            if (rs.next()) {

                paid= rs.getDouble("sum(amount)");
            }

            double bal=(payable-paid);

            return bal;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }
    public  String VoteHeadName(String voteheadid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select voteheadname from voteheads where voteheadid='" + voteheadid + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("voteheadname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }


    public  double OpeningBalance(String academicYear,String voteheadid,String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String year=academicYear;
        if(voteheadid==null)
        {
            try {
                double expected=0,paid=0;
                String sql = "Select sum(amount) from reciepts where academicYear<'"+year+"' and admnumber='"+adm+"'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    paid= rs.getDouble("sum(amount)");
                }
                sql = "Select sum(Amount) from payablevoteheadperstudent where  academicYear<'"+year+"' and admnumber='"+adm+"'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    expected= rs.getDouble("sum(Amount)");
                }

                return expected-paid;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.close();

                } catch (SQLException sq) {
                    
                    sq.printStackTrace();
                }

            }
        }
        else{
            try {
                double expected=0,paid=0;
                String sql = "Select sum(amount) from reciepts where voteheadname ='" + voteheadid + "'  and academicYear<'"+year+"' and admnumber='"+adm+"'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    paid= rs.getDouble("sum(amount)");
                }
                sql = "Select sum(Amount) from payablevoteheadperstudent where voteheadname ='" + voteheadid + "'  and academicYear<'"+year+"'  and admnumber='"+adm+"'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    expected= rs.getDouble("sum(Amount)");
                }

                return expected-paid;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.close();

                } catch (SQLException sq) {
                    
                    sq.printStackTrace();
                }

            }
        }


    }
    public  String voteHeadId(String Voteheadname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select voteheadid from voteheads where voteheadname ='" + Voteheadname + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("voteheadid");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String grade(String mean) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sqll = " Select grade from points_for_each_grade where points='" + mean + "'";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("grade");
            }
            else{
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }


    public  int point(String grade) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sqll = " Select points from points_for_each_grade where grade='" + grade+ "'";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("points");
            }
            else{
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }



    public  String termname(String termcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select termname from terms where termcode='" + termcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String termcode(String termname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select termcode from terms where termname='" + termname + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String currentTermName() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select termname from terms where status='" + "Current" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }
    public  String TermOpeningDate(String year,String termcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            int yr=Integer.parseInt(year);
            int term=3;
            String sq="Select Precisions from terms where termcode='"+termcode+"'";
            ps=con.prepareStatement(sq);
            rs=ps.executeQuery();
            if(rs.next())
            {
                term=rs.getInt("Precisions");

            }
            if(term==3)
            {
                term=1;
                yr=(yr+1);
            }
            else{
                term=(term+1);
            }
            String sql2="Select termcode from terms where precisions='"+term+"'";
            ps=con.prepareStatement(sql2);
            rs=ps.executeQuery();
            if(rs.next())
            {
                termcode=rs.getString("Termcode");
            }
            String sql = "Select openingDate from termdates where academicYear='" + yr + "' and termcode='"+termcode+"'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("openingDate");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String classCode(String className) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select classcode from classes where classname='" + className + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("classcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
                sq.printStackTrace();
            }

        }

    }

    public  String pyCode(String payname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select pycode from expectedpayments where name ='" + payname + "' and termcode='" + currentTerm() + "' and date='" + academicYear() + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("pycode");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String paymentName(String paycode, int academic) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select name from expectedpayments where pycode='" + paycode + "' and termcode='" + currentTerm() + "' and date='" + academic + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String currentdate() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String d = currentTermName() + "   Academic Year : " + academicYear() + " Current Date: " + date.toString()+" Powered By Lunar Tech Solutions";
        return d;

    }

    public  String paymentNameonly(String paycode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql = "Select name from expectedpayments where pycode='" + paycode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }
    public  int examWeightChecker(String examcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql = "Select weight  from examweights where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("weight");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  int academicYear() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select academicyear from terms where status='" + "Current" + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("academicyear");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String streamName(String streamcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select StreamName from streams where streamcode='" + streamcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("streamname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String streamcode(String streamname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select streamcode from streams where streamname='" + streamname + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("streamcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String fullName(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "   " + rs.getString("Middlename") + "   " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }
    public  String studentInfor() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select firstname,middlename,lastname,admissionNumber from admission ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }



    public  String studentUpi(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select upi from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("UPI");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  boolean admissionVerifier(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String  studentImage(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String image="";
            String sql = "Select imageLocation from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                image= rs.getString("imageLocation");
            }
            return image;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }


    public  String kcpeMarks(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select kcpemarks from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                if(rs.getString("kcpemarks").equalsIgnoreCase("null"))
                {
                    return "";
                }
                else{
                    return rs.getString("kcpemarks") ;
                }
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String kcpePosition(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select position from kcperanking where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("position") ;
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  void kcpeAnalysis(String classcode)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);


        try {
            int previous=0;
            int pos=0;
            int tiecheck=0;
            String sql="Select kcpemarks,admissionnumber from admission where currentform='"+classcode+"' order by kcpemarks desc";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            { int kcpemarks;
                String kk=rs.getString("kcpemarks");
                if(kk.equalsIgnoreCase("null"))
                {
                    kcpemarks=0;
                }
                else{
                    kcpemarks=   rs.getInt("Kcpemarks");
                }

                String adm=rs.getString("AdmissionNumber");
                ps=con.prepareStatement("Delete from kcperanking where admissionnumber='"+adm+"'");
                ps.execute();

                if(kcpemarks==previous)
                {
                    tiecheck++;

                }
                else{
                    pos++;
                    pos=tiecheck+pos;
                    tiecheck=0;
                }

                ps=con.prepareStatement("Insert into kcperanking values ('"+kcpemarks+"','"+pos+"','"+adm+"')");
                ps.execute();

                previous=rs.getInt("Kcpemarks");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  int kcpePoints(double TOTALmarks) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            double   avg=TOTALmarks/5;
            String kcpegrade;
            int kcpepoints=0;
            String sqll = "Select grade from kcpetable where  '" + avg + "'>=starting_from and '" + avg + "'<=ending_at  group by sort_code";
            ps = con.prepareStatement(sqll);
            ResultSet RS = ps.executeQuery();
            if (RS.next()) {

                kcpegrade=(RS.getString("grade"));
                String qq = "Select points from points_for_each_grade where grade='" + RS.getString("grade") + "'";
                ps = con.prepareStatement(qq);
                RS = ps.executeQuery();
                if (RS.next()) {
                    return  kcpepoints=(RS.getInt("points"));
                }
                else return 0;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }



    public  String className(String classcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            String sql = "Select classname from classes where classcode='" + classcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("classname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String fullStaffName(String employeecode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select firstname,middlename,lastname from staffs where employeecode='" + employeecode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "  " + rs.getString("Middlename") + "  " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                
                sq.printStackTrace();
            }

        }

    }

    public  String employeeCode(String staffname) {
        String Staffcode = "";
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sql = "Select firstname,middlename,lastname,employeecode from staffs";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if ((rs.getString("firstName") + "  " + rs.getString("Middlename") + "  " + rs.getString("LastName")).equalsIgnoreCase(staffname)) {
                    Staffcode = rs.getString("employeecode");
                    break;
                }
            }
            return Staffcode;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

    }





    public  void rankSalesProfitablity(String date)
    {

        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            ps=con.prepareStatement("Delete from ProductSalesProfitabilityRanking");
            ps.execute();
            String sql="Select productname,products.productid,buyingprice from products  ";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {


                double value=0;


                int unitss=0;
                int unitsSold=0;
                String prodid=rs.getString("productid");
                double buyingprice=rs.getDouble("buyingprice");

                ps=con.prepareStatement("Select count(productid) as items,sum(amount) from sales where productid='"+prodid+"'");
                ResultSet Rs=ps.executeQuery();
                if(Rs.next())
                {
                    unitsSold=Rs.getInt("items");
                    value=Rs.getDouble("Sum(amount)");
                }
                buyingprice=buyingprice*unitsSold;


                double profit=value-buyingprice;


                ps=con.prepareStatement("Insert into ProductSalesProfitabilityRanking values ('"+rs.getString("productname")+"','"+prodid+"','"+profit+"','"+rs.getString("Categoryid")+"','"+rs.getString("Supplierid")+"')");
                ps.execute();

            }





        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }

}
