package com.lunartech.HighSchoolProWebService;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author FRED
 */
public class ConfigurationIntialiser {
     PreparedStatement ps=null;
     ResultSet rs=null;
     Connection con=null;
     String status=null;
    String schoolDb;

    public ConfigurationIntialiser(String schoolDb) {
        this.schoolDb = schoolDb;
    }

    public  String senderId() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select status from systemconfiguration where configurationid='" + "CO023" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next());
            {
                status = rs.getString("status");
                return status;
            }

        } catch (Exception sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    public  String schoolInfor() {
        try {
            con = DbConnection.connectDb(schoolDb);
            String querry = "Select name from schooldetails";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next());
            {

                status = rs.getString("name");
                return status;
            }

        } catch (Exception sq) {

            return null;
        }

    }

    public  boolean nimbus()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO001"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }

        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }


    }
    public  boolean windows()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO002"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean windowsClassic()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO003"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean cde()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO004"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }


        }
        catch(SQLException sq)
        {
            con=DbConnection.connectDb(schoolDb);
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean metal()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO005"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {

            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean mac()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO006"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean passwordChange()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO007"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean passwordConstraint()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO008"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean usernameChange()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO009"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean multipleAdmins()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO010"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  String reminderTime()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO011"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  null;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public  boolean AutoClearance()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO012"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");

            }
            if(status.equalsIgnoreCase("true"))
                return true;
            else
                return false;

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  String smsUsername1()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO013"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  null;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }
    public  String imageFolder()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO023"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
            sq.printStackTrace();
            return  "";
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  String smsKey()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO014"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
            sq.toString();
           // JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  null;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  String smsSendUrl()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationname='"+"SmsSendUrl"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
           // JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  "";
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  String smsBalanceUrl()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationname='"+"SmsSendUrl"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                return status;
            }

        }
        catch(SQLException sq)
        {
            // JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  "";
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean lastName()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO015"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean parentDetails()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO016"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }
    public  boolean residentialDetails()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO017"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean dateOfBirth()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO018"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean phone()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO019"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean image()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO020"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }

        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean docOpener()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO021"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean smsOfflineSender()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO022"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }
    public  boolean migrationReadiness()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO024"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public  boolean admissionSort()
    {
        try{
            con=DbConnection.connectDb(schoolDb);
            String querry="Select status from systemconfiguration where configurationid='"+"CO025"+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            if(rs.next());
            {
                status=rs.getString("status");
                if(status.equalsIgnoreCase("false"))
                {
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        catch(SQLException sq)
        {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        finally{
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }


}
