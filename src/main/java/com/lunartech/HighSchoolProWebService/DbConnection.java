package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

public  String HOST_DB="";

    public  static Connection connectDb(String hostDb) {
        try {
String connectionString="jdbc:mysql://localhost:3306/"+hostDb+"?autoReconnect=true&useSSL=false";
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connection con = DriverManager.getConnection("jdbc:mysql://" + databaseLink + "/Church_database", username,password);
            Connection con  = DriverManager.getConnection(connectionString, "admin", "admin@lunartech");

          //  System.err.println("connection Sucess");
            return con;
        } catch (Exception sq) {

            sq.printStackTrace();

            return null;
        }

    }



//        public  Connection connectDb() {
//            try {
//
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                // Connection con = DriverManager.getConnection("jdbc:mysql://" + databaseLink + "/Church_database", username,password);
//                Connection con  = DriverManager.getConnection("jdbc:mysql://93.104.213.103:3306/"+HOST_DB, "admin", "admin@lunartech");
//
//               // System.err.println("connection Sucess");
//                return con;
//            } catch (Exception sq) {
//
//              sq.printStackTrace();
//
//                return null;
//            }
//
//        }



}
