package com.lunartech.HighSchoolProWebService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeographicalInfor {
    String schoolDb;

    public GeographicalInfor(String schoolDb) {
        this.schoolDb = schoolDb;
    }

    public  ArrayList countries()
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String sqls="Select countryName  from countries order by countryName";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("countryName", rs.getString("countryName"));

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

    public  ArrayList provinces(String countryName)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String sqls="Select provinceName  from countries,provinces where countries.countrycode=provinces.countrycode  and countryname='"+countryName+"' order by provinceName  ";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

            while(rs.next())
            {
                map=new HashMap<String,String>();

                map.put("provinceName", rs.getString("provinceName"));
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

    public  ArrayList counties(String provinceName)
    {
        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String sqls="Select countyName from counties,provinces where provincename='"+provinceName+"' and counties.provincecode=provinces.provincecode order by countyName ";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("countyName", rs.getString("countyName"));

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


}
