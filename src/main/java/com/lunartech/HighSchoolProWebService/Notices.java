package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notices {
    public Notices(String schoolDb) {
        this.schoolDb = schoolDb;
    }

    String schoolDb;
    public ArrayList parentNotices()
    {
        ArrayList arrayList=new ArrayList<Map>();
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String sql ="Select * from notices where adressed='"+"Parents"+"'";
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("content",rs.getString("content"));
                map.put("title",rs.getString("noticeTitle"));
                map.put("date",rs.getString("noticeDate"));
                map.put("noticeid",rs.getString("noticeId"));
                map.put("responseCode","200");
                map.put("responseDescription","success");
                arrayList.add(map);


            }


        }
        catch (Exception sq)
        { Map map=new HashMap();
            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","Error:"+sq.getMessage());
            arrayList.add(map);


        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


        return  arrayList;
    }


    public  ArrayList  notices(String adressee)
    {
        ArrayList arrayList=new ArrayList<Map>();
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String sql ;
        if(adressee.equalsIgnoreCase("all"))
        {
            sql ="Select * from notices ";
        }
        else{
            sql ="Select * from notices where adressed='"+adressee+"'";
        }

        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("content",rs.getString("content"));
                map.put("title",rs.getString("noticeTitle"));
                map.put("date",rs.getString("noticeDate"));
                map.put("noticeid",rs.getString("noticeId"));
                map.put("responseCode","200");
                map.put("responseDescription","success");
                arrayList.add(map);


            }


        }
        catch (Exception sq)
        { Map map=new HashMap();
            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","Error:"+sq.getMessage());
            arrayList.add(map);


        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


        return  arrayList;
    }
    public  Map registerNotice(String title,String content,String employeeCode,String date,String adressee)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            String deptcode="";
            String qq="Select Departmentcode from departments where name='"+"Academics"+"'";
            ps=con.prepareStatement(qq);
            rs=ps.executeQuery();
            if(rs.next())
            {
                deptcode=rs.getString("DepartmentCode");
            }

            String sqll="INSERT  into notices values (?,?,?,?,?,?)";

            ps=con.prepareStatement(sqll);
            ps.setString(1,title);
            ps.setString(2,date);
            ps.setString(3,employeeCode);
            ps.setString(4,adressee);
            ps.setString(5,IdGenerator.keyGen());
            ps.setString(6,content);
            map.put("responseCode","200");
            map.put("responseDescription","Teacher Registered SuccessFully");
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
