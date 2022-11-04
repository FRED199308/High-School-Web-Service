package com.lunartech.HighSchoolProWebService;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SchoolInfor {
    String schoolDb;
DocPath docPath;
    public SchoolInfor(String schoolDb) {
        this.schoolDb = schoolDb;
        docPath=new DocPath(schoolDb);

    }

    public  Map schoolDetails()
    {


        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();
        try {
            String sq = "Select * from schooldetails";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            String name = "";
            if (rs.next()) {
                map.put("schoolName",rs.getString("name"));
                map.put("motto",rs.getString("motto"));
                map.put("vission",rs.getString("vision"));
                map.put("mission",rs.getString("mission"));
                map.put("contact",rs.getString("contact"));
                map.put("email",rs.getString("email"));
                map.put("adress",rs.getString("adress"));
                map.put("location",rs.getString("location"));
                map.put("image",rs.getString("name"));
                map.put("responseCode","200");
                map.put("responseDescription","success");
                //map.put("Logo",rs.getBlob("logo"));
                try {
                    Blob imm=rs.getBlob("logo");
                    String imagename="logo";
                    FileOutputStream fos = new FileOutputStream(docPath.documentFolderPath()+"logo.jpg");
                    int length = (int) imm.length();
                    byte[] bf = imm.getBytes(1, length);
                    fos.write(bf, 0, length);
                    fos.close();
                }
                catch (Exception e)
                {

                }


                return map;

            }
            else {
                map.put("responseCode","404");
                map.put("responseDescription","Not Found");
                return  map;
            }
        } catch (SQLException e) {
            map.put("responseCode","501");
            map.put("responseDescription","Error:"+e.toString());
            return  map;
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }
//    public void schoolLogo()
//    {
//
//
//        Connection con;
//        PreparedStatement ps = null;
//        ResultSet rs;
//        con = DbConnection.connectDb(schoolDb);
//
//        try {
//            String sq = "Select * from schooldetails";
//            ps = con.prepareStatement(sq);
//            rs = ps.executeQuery();
//            String name = "";
//            if (rs.next()) {
//
//
//                try {
//                    Blob imm=rs.getBlob("logo");
//                    String imagename="logo";
//                    FileOutputStream fos = new FileOutputStream(DocPath.documentFolderPath()+"logo.jpg");
//                    int length = (int) imm.length();
//                    byte[] bf = imm.getBytes(1, length);
//                    fos.write(bf, 0, length);
//                    fos.close();
//                }
//                catch (Exception e)
//                {
//e.printStackTrace();
//                }
//
//
//
//
//            }
//
//        } catch (SQLException e) {
//
//        } finally {
//            try {
//                con.close();
//
//            } catch (SQLException sq) {
//
//                sq.printStackTrace();
//            }
//
//        }
//
//
//    }



public Map saveSchoolDetails(String schoolName,String schoolMission,String schoolVision,String schoolMotto,String schoolContact,String schoolLocation, String schoolEmail,String schoolAdress) {

    Connection con;
    PreparedStatement ps = null;
    ResultSet rs;
    con = DbConnection.connectDb(schoolDb);
    Map<String,String> map=new HashMap<>();

    FileInputStream in = null;
    try {
        String sql1 = "select * from schooldetails";
        ps = con.prepareStatement(sql1);
        rs = ps.executeQuery();
        if (rs.next()) {

                String sql2 = "delete from schooldetails";
                ps = con.prepareStatement(sql2);
                ps.execute();
                String sql = "insert into schooldetails values('" + schoolName.toUpperCase() + "','" + schoolAdress.toUpperCase() + "','" + schoolContact.toUpperCase() + "','" + schoolEmail.toUpperCase() + "','" + schoolMotto.toUpperCase()+ "','" + schoolMission.toUpperCase() + "','" + schoolVision.toUpperCase() + "','" + schoolLocation.toUpperCase() + "','"+""+"')";
                ps = con.prepareStatement(sql);
                ps.execute();
            map.put("responseCode","200");
            map.put("responseDescription","School Details Updated Successfuly");
              return map;


        } else {



            String sql = "insert into schooldetails values('" + schoolName.toUpperCase() + "','" + schoolAdress.toUpperCase() + "','" + schoolContact.toUpperCase() + "','" + schoolEmail.toUpperCase() + "','" + schoolMotto.toUpperCase() + "','" + schoolMission.toUpperCase() + "','" + schoolVision.toUpperCase() + "','" + schoolLocation.toUpperCase() + "','"+""+"')";
            ps = con.prepareStatement(sql);
            ps.execute();
            map.put("responseCode","200");
            map.put("responseDescription","Registration successfull");
            return map;

        }

    } catch (HeadlessException |SQLException sq) {
        sq.printStackTrace();
        map.put("responseCode","501");
        map.put("responseDescription","Error:"+sq.getMessage());
        return map;
    } finally {
        try {
            con.close();
        } catch (Exception sq) {

        }

    }
}


}
