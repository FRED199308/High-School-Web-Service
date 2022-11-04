/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lunartech.HighSchoolProWebService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author FRED
 */
public class RightsAnnouncer {
    String schoolDb;
Globals globals;
    public RightsAnnouncer(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    private  Connection con;
    private  PreparedStatement ps;
    private  ResultSet rs;


    public  boolean editRights() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" + globals.CurrentUser+ "' and RightId='" + "RG0001" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean DeleteRights() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0002" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean paySalaries() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser+ "' and RightId='" + "RG0003" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            //JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            sq.printStackTrace();
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean expenseRecord() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0004" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean recievePaymentRights() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0005" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean createCustomPay() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0006" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean communication() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser+ "' and RightId='" + "RG0007" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean reviewJobGroups() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0008" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean promote() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG0009" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean writeoffbal() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from userrights  where username='" +globals.CurrentUser + "' and RightId='" + "RG00010" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("Status").equalsIgnoreCase("true")) {
                    con.close();
                    return true;
                } else {
                    con.close();
                    return false;
                }
            } else {
                con.close();
                return false;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public  boolean subjectright(String subcode, String classcode, String teachercode) {
        try {
            con=DbConnection.connectDb(schoolDb);
            String sqla="Select * from subjectrights where classcode='"+classcode+"' and subjectcode='"+subcode+"'  and teachercode='"+teachercode+"'";
            ps=con.prepareStatement(sqla);
            rs=ps.executeQuery();
            if(rs.next())
            {con.close();
            return true;
            }
            else
                con.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
        
    }
      public  boolean subjectMarksEntry(String subcode, String classcode,String streamcode ,String teachercode) {
        try {
            con=DbConnection.connectDb(schoolDb);
            String sqla="Select * from subjectrights where classcode='"+classcode+"' and subjectcode='"+subcode+"'  and teachercode='"+teachercode+"' and streamcode='"+streamcode+"'";
            ps=con.prepareStatement(sqla);
            rs=ps.executeQuery();
            if(rs.next())
            {con.close();
            return true;
            }
            else
                con.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
        
    }
    
    public RightsAnnouncer()
    {
        con = DbConnection.connectDb(schoolDb);
    }

    }



