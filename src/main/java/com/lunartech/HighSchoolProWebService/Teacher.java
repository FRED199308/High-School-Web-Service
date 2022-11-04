package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Teacher {
    String schoolDb;

    public Teacher(String schoolDb) {
        this.schoolDb = schoolDb;
    }

    public  Map registerTeacher(String firstName, String middleName, String lastName , String gender, String initials, String teacherNumber, String phone, String email)
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

            String sqll="Insert into staffs values('"+firstName.toUpperCase()+"','"+middleName.toUpperCase()+"','"+lastName.toUpperCase()+"','"+initials.toUpperCase()+"','"+gender+"','"+""+"',"
                    + "'"+phone+"','"+email+"','"+deptcode+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"',curDate(),'"+teacherNumber+"','"+"Active"+"')";
            ps=con.prepareStatement(sqll);
            ps.execute();
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

    public  Map editTeacherDetails(String TEACHERNUMBER,String firstName,String middleName,String lastName ,String gender,String initials,String teacherNumber,String phone,String email)
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

            String sqll="UPDATE staffs set firstname='"+firstName.toUpperCase()+"',middleName='"+middleName.toUpperCase()+"',lastName='"+lastName.toUpperCase()+"',Initials='"+initials.toUpperCase()+"',gender='"+gender+"',phoneNumber='"+phone+"',email='"+email+"',employeeCode='"+teacherNumber+"' where employeeCode='"+TEACHERNUMBER+"'";
            ps=con.prepareStatement(sqll);
            ps.execute();
            map.put("responseCode","200");
            map.put("responseDescription","Teacher Details Editted Successfully");
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

    public  ArrayList getAllTeachers()
    {
       ArrayList<Map> arrayList=new ArrayList<Map>();
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

            String sqll="Select * from staffs where departmentCode='"+deptcode+"'";
            ps=con.prepareStatement(sqll);
            rs= ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","success");

                map.put("teacherName",rs.getString("firstname")+"  "+rs.getString("middlename")+"  "+rs.getString("lastname"));

                map.put("teacherNumber",rs.getString("employeecode"));
                map.put("email",rs.getString("email"));
                map.put("tel",rs.getString("phoneNumber"));
                map.put("gender",rs.getString("gender"));
                map.put("initials",rs.getString("Initials"));
                arrayList.add(map);


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


    public  ArrayList searchTeachers(String search)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
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

            String sqll="Select * from staffs where  (firstName like  '"+search+"%'  or lastname like '"+search+"%' or middleName like '"+search+"%' or PhoneNumber like '"+search+"%' or initials like '"+search+"%') and departmentCode='"+deptcode+"'";
            ps=con.prepareStatement(sqll);
            rs= ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","success");

                map.put("teacherName",rs.getString("firstname")+"  "+rs.getString("middlename")+"  "+rs.getString("lastname"));

                map.put("teacherNumber",rs.getString("employeecode"));
                map.put("email",rs.getString("email"));
                map.put("tel",rs.getString("phoneNumber"));
                map.put("gender",rs.getString("gender"));
                map.put("initials",rs.getString("Initials"));
                arrayList.add(map);


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

    public  Map teacherDetails(String teacherNumber)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            String sqll="Select * from staffs where employeecode='"+teacherNumber+"'";
            ps=con.prepareStatement(sqll);
           rs= ps.executeQuery();
           if(rs.next())
           {
               map.put("responseCode","200");
               map.put("responseDescription","success");

               map.put("firstName",rs.getString("firstname"));
               map.put("middleName",rs.getString("middlename"));
               map.put("lastName",rs.getString("lastname"));
               map.put("teacherNumber",rs.getString("employeecode"));
               map.put("email",rs.getString("email"));
               map.put("tel",rs.getString("phoneNumber"));
               map.put("gender",rs.getString("gender"));
               map.put("initials",rs.getString("Initials"));
               return map;

           }
           else{
               map.put("responseCode","404");
               map.put("responseDescription","Teacher Not Found");
               return map;
           }

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

    public  Map deleteTeacher(String teacherNumber)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {


            String sqll="Delete from staffs where employeecode='"+teacherNumber+"'";
            ps=con.prepareStatement(sqll);
            ps.execute();

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
