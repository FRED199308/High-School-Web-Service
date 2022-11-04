package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exams {
    String schoolDb;
Globals globals;
    public Exams(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    public   ArrayList<Map> getExams()
    {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        ArrayList <Map> arrayList= new ArrayList<>();
        try {




            String sql2="Select * from exams group by examname";
            ps=con.prepareStatement(sql2);
            rs=ps.executeQuery();
            while(rs.next())
            {

                Map response=new HashMap();

                response.put("examName",rs.getString("ExamName"));
                response.put("transferable",rs.getString("transferable"));

                response.put("responseCode","200");
                response.put("responseDescription","School Set Successfuly");
                arrayList.add(response);
            }
            return arrayList;

        }
        catch (Exception sq)
        {
            Map response=new HashMap();
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sq.getLocalizedMessage());
            arrayList.add(response);
            return arrayList;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }


    public  ArrayList examWeight(String exam)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select examname,examweights.examcode,weight,classcode from exams,examweights where  examweights.examcode=exams.examcode and examName='"+exam+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("examCode", rs.getString("examcode"));
                map.put("examName", rs.getString("examname"));
                map.put("className",globals.className(rs.getString("classcode")));
                map.put("weight",rs.getString("weight"));

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

    public  ArrayList examWeight(String exam,String className)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select examname,examweights.examcode,weight,classcode from exams,examweights where  examweights.examcode=exams.examcode and examName='"+exam+"' and classcode='"+globals.classCode(className)+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("examCode", rs.getString("examcode"));
                map.put("examName", rs.getString("examname"));
                map.put("className",globals.className(rs.getString("classcode")));
                map.put("weight",rs.getString("weight"));

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

    public  ArrayList examWeight()
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select examname,examweights.examcode,weight,classcode from exams,examweights where examweights.examcode=exams.examcode";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("examCode", rs.getString("examcode"));
                map.put("examName", rs.getString("examname"));
                map.put("className",globals.className(rs.getString("classcode")));
                map.put("weight",rs.getString("weight"));

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

    public  ArrayList classExamWeight(String className)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry="Select examname,examweights.examcode,weight,classcode from exams,examweights where examweights.examcode=exams.examcode and classcode='"+globals.classCode(className)+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("examCode", rs.getString("examcode"));
                map.put("examName", rs.getString("examname"));
                map.put("className",globals.className(rs.getString("classcode")));
                map.put("weight",rs.getString("weight"));

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

    public  Map editExamWeight(String examName,String className,String weigth)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

System.out.println("Class Name:"+className);

int p=1;

            String sqq="Select precision1 from classes where classname='"+className+"'";
            ps=con.prepareStatement(sqq);
            rs=ps.executeQuery();
            if(rs.next())
            {
                p=rs.getInt("precision1");
            }
            String cname="FM"+p+examName;
System.out.println("Frm:"+cname);

            String sql="Update examweights set weight='"+weigth+"' where examcode like '"+cname+"%'";
            ps=con.prepareStatement(sql);
            ps.execute();
            map.put("responseCode","200");
            map.put("responseDescription","Exam Weight Editted Successfully");
            return map;
        }
        catch ( SQLException sq) {

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
}
