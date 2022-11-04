package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneBook {
Globals globals;
    public PhoneBook(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    String schoolDb;

    public  ArrayList getPhoneBook(String className,String contact)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String querry;
       try {
           if(className.isEmpty()&& contact.isEmpty())
           {

                  querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission";

           }
           else if(contact.isEmpty()||className.isEmpty()) {
               if(contact.isEmpty())
               {
                   if(className.equalsIgnoreCase("Select Class"))
                   {
                       querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission  ";

                   }
                   else{
                       querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where CurrentForm='"+globals.classCode(className)+"' ";

                   }

               }
               else {
                   if(contact.equalsIgnoreCase("with Contact"))
                   {
                       querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where telephone1 is not null";

                   }
                   else {
                       querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where telephone1 is null ";

                   }
               }
           }
           else {
               if(contact.equalsIgnoreCase("with Contact"))
               {
                   contact=" is not null";
               }
               else {
                   contact="is null";
               }
               querry ="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where CurrentForm='"+globals.classCode(className)+"' and telephone1 "+contact;

           }


           ps=con.prepareStatement(querry);
           rs=ps.executeQuery();
           while(rs.next())
           {
               Map map=new HashMap();
               map.put("admissionNumber",rs.getString("AdmissionNumber"));
               map.put("studentName",rs.getString("FirstName")+"  "+rs.getString("MIddleName")+"  "+rs.getString("LastName"));
               map.put("parentName",rs.getString("parentfullnames"));
               map.put("tel1",rs.getString("telephone1"));
               map.put("tel2",rs.getString("telephone2"));
               map.put("responseCode","200");
               map.put("responseDescription","success");
               arrayList.add(map);

           }
           return arrayList;

       }
       catch (Exception sq)
       {
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

    public  ArrayList searchPhoneBook(String search)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String querry;
        try {
            querry="Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where admissionNumber  like '" + search + "%' or firstname  like '" + search + "%' or middlename  like '" + search + "%'  or lastname  like '" + search + "%' or telephone1  like '" + search + "%' or telephone2  like '" + search + "%'";



            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            { Map map=new HashMap();
                map.put("admissionNumber",rs.getString("AdmissionNumber"));
                map.put("studentName",rs.getString("FirstName")+"  "+rs.getString("MIddleName")+"  "+rs.getString("LastName"));
                map.put("parentName",rs.getString("parentfullnames"));
                map.put("tel1",rs.getString("telephone1"));
                map.put("tel2",rs.getString("telephone2"));
                map.put("responseCode","200");
                map.put("responseDescription","success");
                arrayList.add(map);

            }
            return arrayList;

        }
        catch (Exception sq)
        {
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

    public  Map editPhoneBook(String tel1, String tel2,String adm) {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String querry1 = "update  admission set telephone1='"+tel1+"',telephone2='"+tel2+"' where admissionNumber='"+adm+"'";
            ps = con.prepareStatement(querry1);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","Phone Updated");
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
}
