package com.lunartech.HighSchoolProWebService;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectRights {

    String schoolDb;
Globals globals;
    public SubjectRights(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    public  ArrayList getSubjectRights(String className, String subjectName, String tearcherName, String streamName)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            if(className.equalsIgnoreCase("All")&& streamName.equalsIgnoreCase("All")&& subjectName.equalsIgnoreCase("All")&& tearcherName.equalsIgnoreCase("All"))
            {

                String sql1="Select * from  classes where precision1<5 order by precision1 ";
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String cl=rs.getString("classCode");
                    String cls=rs.getString("className");
                    String sql2="Select * from streams order by streamName";

                    ps=con.prepareStatement(sql2);
                    ResultSet rx=ps.executeQuery();
                    while (rx.next())
                    {
                     String streamid=rx.getString("streamcode");
                     String stName=rx.getString("streamName");


                     sql1="select * from subjects order by subjectCode";
                     ps=con.prepareStatement(sql1);
                     ResultSet resultSet=ps.executeQuery();
                     while(resultSet.next())
                     {
                         String subcode=resultSet.getString("subjectCode");
                         String subName=resultSet.getString("subjectName");

                         sql1="Select * from subjectrights where classcode='"+cl+"' and streamCode='"+streamid+"' and subjectcode='"+subcode+"'";
                         ps=con.prepareStatement(sql1);
                         ResultSet RS=ps.executeQuery();
                         if(RS.next())
                         {
                             Map map=new HashMap();
                             map.put("className",cls);
                             map.put("subjectName",subName);
                             map.put("subjectCode",cls);
                             map.put("status","Assigned");
                             String clear=RS.getString("status");
                             if(clear.equalsIgnoreCase("true"))
                             {
                                 map.put("clearance","Yes");
                             }
                             else{
                                 map.put("clearance","No");
                             }

                             map.put("streamName",stName);
                             map.put("teacherName",globals.fullStaffName(RS.getString("teacherCode")));
                             map.put("responseCode","200");
                             map.put("responseDescription","sucess");
                             arrayList.add(map);

                         }
                         else{
                             Map map=new HashMap();
                             map.put("className",cls);
                             map.put("status","UNAssigned");
                             map.put("streamName",stName);
                             map.put("subjectName",subName);
                             map.put("clearance","No");
                             map.put("subjectCode",cls);
                             map.put("teacherName","");
                             map.put("responseCode","200");
                             map.put("responseDescription","sucess");
                             arrayList.add(map);

                         }

                     }




                    }
                }
                return arrayList;
            }
else  if(!className.equalsIgnoreCase("All")&& streamName.equalsIgnoreCase("All")&& subjectName.equalsIgnoreCase("All")&& tearcherName.equalsIgnoreCase("All"))
            {

                String sql1="Select * from classes where className='"+className+"' order by precision1";
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String cl=rs.getString("classCode");
                    String cls=rs.getString("className");
                    String sql2="Select * from streams order by streamName";

                    ps=con.prepareStatement(sql2);
                    ResultSet rx=ps.executeQuery();
                    while (rx.next())
                    {
                        String streamid=rx.getString("streamcode");
                        String stName=rx.getString("streamName");


                        sql1="select * from subjects order by subjectCode";
                        ps=con.prepareStatement(sql1);
                        ResultSet resultSet=ps.executeQuery();
                        while(resultSet.next())
                        {
                            String subcode=resultSet.getString("subjectCode");
                            String subName=resultSet.getString("subjectName");

                            sql1="Select * from subjectrights where classcode='"+cl+"' and streamCode='"+streamid+"'  and subjectcode='"+subcode+"'";
                            ps=con.prepareStatement(sql1);
                            ResultSet RS=ps.executeQuery();
                            if(RS.next())
                            {
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("subjectName",subName);
                                map.put("subjectCode",cls);
                                map.put("status","Assigned");
                                String clear=RS.getString("status");
                                if(clear.equalsIgnoreCase("true"))
                                {
                                    map.put("clearance","Yes");
                                }
                                else{
                                    map.put("clearance","No");
                                }

                                map.put("streamName",stName);
                                map.put("teacherName",globals.fullStaffName(RS.getString("teacherCode")));
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }
                            else{
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("status","UNAssigned");
                                map.put("streamName",stName);
                                map.put("subjectName",subName);
                                map.put("clearance","No");
                                map.put("subjectCode",cls);
                                map.put("teacherName","");
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }

                        }




                    }
                }
                return arrayList;
            }
else  if(!className.equalsIgnoreCase("All")&& !streamName.equalsIgnoreCase("All")&& subjectName.equalsIgnoreCase("All")&& tearcherName.equalsIgnoreCase("All"))
            {

                String sql1="Select * from classes where  className='"+className+"' order by precision1";
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String cl=rs.getString("classCode");
                    String cls=rs.getString("className");
                    String sql2="Select * from streams  where streamName='"+streamName+"' order by streamName ";

                    ps=con.prepareStatement(sql2);
                    ResultSet rx=ps.executeQuery();
                    while (rx.next())
                    {
                        String streamid=rx.getString("streamcode");
                        String stName=rx.getString("streamName");


                        sql1="select * from subjects order by subjectCode";
                        ps=con.prepareStatement(sql1);
                        ResultSet resultSet=ps.executeQuery();
                        while(resultSet.next())
                        {
                            String subcode=resultSet.getString("subjectCode");
                            String subName=resultSet.getString("subjectName");

                            sql1="Select * from subjectrights where classcode='"+cl+"' and streamCode='"+streamid+"'  and subjectcode='"+subcode+"'";
                            ps=con.prepareStatement(sql1);
                            ResultSet RS=ps.executeQuery();
                            if(RS.next())
                            {
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("subjectName",subName);
                                map.put("subjectCode",cls);
                                map.put("status","Assigned");
                                String clear=RS.getString("status");
                                if(clear.equalsIgnoreCase("true"))
                                {
                                    map.put("clearance","Yes");
                                }
                                else{
                                    map.put("clearance","No");
                                }

                                map.put("streamName",stName);
                                map.put("teacherName",globals.fullStaffName(RS.getString("teacherCode")));
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }
                            else{
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("status","UNAssigned");
                                map.put("streamName",stName);
                                map.put("subjectName",subName);
                                map.put("clearance","No");
                                map.put("subjectCode",cls);
                                map.put("teacherName","");
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }

                        }




                    }
                }
                return arrayList;
            }
else if(!className.equalsIgnoreCase("All")&& !streamName.equalsIgnoreCase("All")&& !subjectName.equalsIgnoreCase("All")&& tearcherName.equalsIgnoreCase("All"))
            {

                String sql1="Select * from classes where  className='"+className+"' order by precision1";
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String cl=rs.getString("classCode");
                    String cls=rs.getString("className");
                    String sql2="Select * from streams  where streamName='"+streamName+"' order by streamName ";

                    ps=con.prepareStatement(sql2);
                    ResultSet rx=ps.executeQuery();
                    while (rx.next())
                    {
                        String streamid=rx.getString("streamcode");
                        String stName=rx.getString("streamName");


                        sql1="select * from subjects where subjectCode='"+globals.subjectCode(subjectName)+"' order by subjectCode";
                        ps=con.prepareStatement(sql1);
                        ResultSet resultSet=ps.executeQuery();
                        while(resultSet.next())
                        {
                            String subcode=resultSet.getString("subjectCode");
                            String subName=resultSet.getString("subjectName");

                            sql1="Select * from subjectrights where classcode='"+cl+"' and streamCode='"+streamid+"'  and subjectcode='"+subcode+"'";
                            ps=con.prepareStatement(sql1);
                            ResultSet RS=ps.executeQuery();
                            if(RS.next())
                            {
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("subjectName",subName);
                                map.put("subjectCode",cls);
                                map.put("status","Assigned");
                                String clear=RS.getString("status");
                                if(clear.equalsIgnoreCase("true"))
                                {
                                    map.put("clearance","Yes");
                                }
                                else{
                                    map.put("clearance","No");
                                }

                                map.put("streamName",stName);
                                map.put("teacherName",globals.fullStaffName(RS.getString("teacherCode")));
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }
                            else{
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("status","UNAssigned");
                                map.put("streamName",stName);
                                map.put("subjectName",subName);
                                map.put("clearance","No");
                                map.put("subjectCode",cls);
                                map.put("teacherName","");
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }

                        }




                    }
                }
                return arrayList;
            }
            else if(!className.equalsIgnoreCase("All")&& !streamName.equalsIgnoreCase("All")&& !subjectName.equalsIgnoreCase("All")&& !tearcherName.equalsIgnoreCase("All"))
            {

                String sql1="Select * from classes where  className='"+className+"' order by precision1";
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                while(rs.next())
                {
                    String cl=rs.getString("classCode");
                    String cls=rs.getString("className");
                    String sql2="Select * from streams  where streamName='"+streamName+"' order by streamName ";

                    ps=con.prepareStatement(sql2);
                    ResultSet rx=ps.executeQuery();
                    while (rx.next())
                    {
                        String streamid=rx.getString("streamcode");
                        String stName=rx.getString("streamName");


                        sql1="select * from subjects where subjectCode='"+globals.subjectCode(subjectName)+"' order by subjectCode";
                        ps=con.prepareStatement(sql1);
                        ResultSet resultSet=ps.executeQuery();
                        while(resultSet.next())
                        {
                            String subcode=resultSet.getString("subjectCode");
                            String subName=resultSet.getString("subjectName");

                            sql1="Select * from subjectRights where classcode='"+cl+"' and streamCode='"+streamid+"' and teachercode='"+globals.employeeCode(tearcherName)+"'  and subjectcode='"+subcode+"'";
                            ps=con.prepareStatement(sql1);
                            ResultSet RS=ps.executeQuery();
                            if(RS.next())
                            {
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("subjectName",subName);
                                map.put("subjectCode",cls);
                                map.put("status","Assigned");
                                String clear=RS.getString("status");
                                if(clear.equalsIgnoreCase("true"))
                                {
                                    map.put("clearance","Yes");
                                }
                                else{
                                    map.put("clearance","No");
                                }

                                map.put("streamName",stName);
                                map.put("teacherName",globals.fullStaffName(RS.getString("teacherCode")));
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }
                            else{
                                Map map=new HashMap();
                                map.put("className",cls);
                                map.put("status","UNAssigned");
                                map.put("streamName",stName);
                                map.put("subjectName",subName);
                                map.put("clearance","No");
                                map.put("subjectCode",cls);
                                map.put("teacherName","");
                                map.put("responseCode","200");
                                map.put("responseDescription","sucess");
                                arrayList.add(map);

                            }

                        }




                    }
                }
                return arrayList;
            }

            return arrayList;


        }
        catch ( SQLException sq) {

            sq.printStackTrace();
            Map map=new HashMap<>();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.getMessage());
            arrayList.add(map);
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

    public  Map saveSubjectRight(String className,String subjectName,String tearcherName,String streamName,boolean clear)
    {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();
        try {
           String subcode=globals.subjectCode(subjectName);
          String  streamcode=globals.streamcode(streamName);
          String  classcode=globals.classCode(className);
         String   empcode=globals.employeeCode(tearcherName);
         System.out.println(subcode);

         System.out.println(classcode);
         System.out.println(empcode);
         System.out.println(tearcherName);
            System.out.println(streamcode);
            String sqla="Select * from subjectrights where classcode='"+classcode+"' and subjectcode='"+subcode+"' and teachercode='"+empcode+"' and streamcode='"+streamcode+"'";
            ps=con.prepareStatement(sqla);
            rs=ps.executeQuery();
            if(rs.next())
            {
                map.put("responseCode","501");
                map.put("responseDescription","The Selected Class Stream Subject Combination Has Already Been Assigned To "+globals.fullStaffName(rs.getString("teachercode")));
                return  map;

            }
            else{
                String status="";
                if(clear)
                {
                    status="True".toUpperCase();
                }
                else{
                    status="False".toUpperCase();
                }
                String sql="Insert into subjectrights values('"+subcode+"','"+classcode+"','"+streamcode+"','"+empcode+"','"+status+"')";
                ps=con.prepareStatement(sql);
                ps.execute();
                map.put("responseCode","200");
                map.put("responseDescription","Subject Right Assigned SuccessFully");
                return  map;


            }





        } catch (HeadlessException | SQLException sq) {
          sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription",sq.toString());
            return  map;
        }
    }

    public  Map revockSubjectRight(String className,String subjectName,String tearcherName,String streamName)
    {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();
        try {
            String subcode=globals.subjectCode(subjectName);
            String  streamcode=globals.streamcode(streamName);
            String  classcode=globals.classCode(className);
            String   empcode=globals.employeeCode(tearcherName);
            System.out.println(empcode);
            System.out.println(classcode);
            System.out.println(streamcode);
            System.out.println(subcode);

                String sql="Delete from subjectrights where subjectcode='"+subcode+"' and classcode='"+classcode+"'  and teachercode='"+empcode+"' and streamcode='"+streamcode+"'";
                ps=con.prepareStatement(sql);
                ps.execute();

                map.put("responseCode","200");
                map.put("responseDescription","Subject Right Revocked SuccessFully");
                return  map;








        } catch (HeadlessException | SQLException sq) {
            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription",sq.toString());
            return  map;
        }
    }

}
