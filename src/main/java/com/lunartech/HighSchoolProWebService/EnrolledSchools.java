package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnrolledSchools {

    public static    ArrayList getSchools()
    {
        ArrayList<Map> list=new ArrayList();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb("lunartech_database");
        try {

            String sqlquerry="Select * from schools";
            ps=con.prepareStatement(sqlquerry);
            rs= ps.executeQuery();
            while(rs.next())
            {

                Map map=new HashMap();
                map.put("schoolName",rs.getString("schoolName"));
                map.put("databaseName",rs.getString("schooldatabaseName"));
                map.put("responseCode","200");
                map.put("responseDescription","success");
               list.add(map);
            }
return list;
        }
        catch (Exception sq)
        {
            sq.printStackTrace();
            Map map=new HashMap();
            map.put("responseCode","501");
            map.put("responseDescription","Error :"+sq.getMessage());
            list.add(map);
            return list;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }


    public static   Map getSchoolsDetails(String schoolName)
    {
    Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb("lunartech_database");
        try {

            String sqlquerry="Select * from schools where schoolName='"+schoolName+"'";
            ps=con.prepareStatement(sqlquerry);
            rs= ps.executeQuery();
         if(rs.next())
            {
                Map map=new HashMap();

              response.put("databaseName",rs.getString("schooldatabaseName"));
               response.put("schoolName",rs.getString("schoolName"));
                response.put("responseCode","200");
                response.put("responseDescription","School Set Successfuly");
                return response;

            }
         else {
             response.put("responseCode","404");
             response.put("responseDescription","School Not Registered\n Consult Lunar Tech For Registration on 0707353225");
             return response;

         }

        }
        catch (Exception sq)
        {
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sq.getLocalizedMessage());
            return response;
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
