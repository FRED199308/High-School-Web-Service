package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    String schoolDb;
ConfigurationIntialiser configurationIntialiser;
    public User(String schoolDb) {
        this.schoolDb = schoolDb;
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    public  Map registerUser(String username, String level, String user)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
String employeeCode="";

            String querr = "select firstname,middlename,lastname,employeecode  from staffs";
            ps = con.prepareStatement(querr);
            rs = ps.executeQuery();
            while (rs.next()) {

                employeeCode = rs.getString("employeecode");
                String combined =  rs.getString("firstname")+ "  " + rs.getString("middlename") + "  " + rs.getString("lastname");
                if (combined.equals(user)) {
                    break;
                }

            }


            String querry2 = "Select * from useraccounts where  employeecode='" +  employeeCode + "'";
            ps = con.prepareStatement(querry2);
            rs = ps.executeQuery();

            if (rs.next()) {


                    map.put("responseCode","501");
                    map.put("responseDescription","The Person Selected Already Has an Account");

                    return map;


            }
            else {




                 querry2 = "Select * from useraccounts where username='" + username + "' ";
                ps = con.prepareStatement(querry2);
                rs = ps.executeQuery();

                if (rs.next()) {

                    map.put("responseCode","501");
                    map.put("responseDescription","TUsername Already Taken");
                    return map;

                }
                else{
                    String querry3 = "insert into useraccounts Values('" + username + "','" + employeeCode + "','" + level + "','" + DataEncriptor.encript("pass") + "','" + "1000" + "','" + "ACTIVE" + "')";
                    ps = con.prepareStatement(querry3);
                    ps.execute();
                    if(level.equalsIgnoreCase("normal"))
                    {
                        for (int i = 1; i < 11; ++i) {

                            String querry4 = "Insert into userrights values('" + username + "','" + "RG000" + i + "','" + "False" + "')";
                            ps = con.prepareStatement(querry4);
                            ps.execute();

                        }
                    }
                    else{

                        for (int i = 1; i < 11; ++i) {

                            String querry4 = "Insert into userrights values('" + username + "','" + "RG000" + i + "','" + "true" + "')";
                            ps = con.prepareStatement(querry4);
                            ps.execute();

                        }
                    }
                    map.put("responseCode","200");
                    map.put("responseDescription","Teacher Registered SuccessFully");
                    return map;
                }




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





    public  Map changeUserCredentials(String currentUserName,String currentPassword,String newUserName,String newPassword)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from useraccounts where UserName='" + currentUserName + "' and Password='" + DataEncriptor.encript(currentPassword) + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

                boolean status;
                status=configurationIntialiser.passwordConstraint();
                if(status==false)
                {
                    String querry1 = "Update useraccounts set password='" + DataEncriptor.encript(newPassword) + "',username='" + newUserName + "' where username='" + currentUserName + "' and password='" + DataEncriptor.encript(currentPassword )+ "'";
                    ps = con.prepareStatement(querry1);
                    ps.execute();

                    map.put("responseCode","200");
                    map.put("responseDescription","Password and UserName Change Successful");
                    return map;
                }
                else{
                    if(DataValidation.password(newPassword))
                    {
                        String querry1 = "Update useraccounts set password='" + DataEncriptor.encript(newPassword) + "',username='" +newUserName + "' where username='" + currentUserName + "' and password='" + DataEncriptor.encript(currentPassword) + "'";
                        ps = con.prepareStatement(querry1);
                        ps.execute();
                        map.put("responseCode","200");
                        map.put("responseDescription","Password and UserName Change Successful");
return map;
                    }
                    else{
                        map.put("responseCode","501");
                        map.put("responseDescription","Password Weak Kindly Choose a new Password\n Password Must contain  Atleast "
                                + "\n An Uppercase Letter ,A Lowercase letter,A Digit \n A Special Character and Atleast Six Characters");

                    return map;

                    }
                }




            } else {


                map.put("responseCode","501");
                map.put("responseDescription","No such UserName Password Combination  Found ");
                return map;
            }
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


    public  Map changeUserStatus(String userName)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {

            con = DbConnection.connectDb(schoolDb);
            String querry = "Select * from useraccounts where UserName='" + userName + "' ";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {

             String status=rs.getString("Status");

                if(status.equalsIgnoreCase("INACTIVE"))
                {
                    String querry1 = "Update useraccounts set status='" + "ACTIVE" + "' where username='" + userName + "'";
                    ps = con.prepareStatement(querry1);
                    ps.execute();

                    map.put("responseCode","200");
                    map.put("responseDescription","User Status Changed");
                    return map;
                }
                else{
                    String querry1 = "Update useraccounts set status='" + "INACTIVE" + "' where username='" + userName + "'";
                    ps = con.prepareStatement(querry1);
                    ps.execute();

                    map.put("responseCode","200");
                    map.put("responseDescription","User Status Changed");
                    return map;
                }




            } else {


                map.put("responseCode","501");
                map.put("responseDescription","No such UserName Password Combination  Found ");
                return map;
            }
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


    public  Map resetAccountPassword(String userName)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String querry1 = "Update useraccounts set password='" + DataEncriptor.encript("pass") + "' where username='" + userName + "' ";
            ps = con.prepareStatement(querry1);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","Password Reset SuccessFul");
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


    public  ArrayList getAllUsers()
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
            String sqll="Select firstname,middlename,lastname,username,useraccounts.status,level  from useraccounts,staffs where staffs.employeeCode=useraccounts.employeeCode and Departmentcode='"+deptcode
                    +"' order by username ";
            ps=con.prepareStatement(sqll);
            rs= ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","success");

                map.put("user",rs.getString("firstname")+"  "+rs.getString("middlename")+"  "+rs.getString("lastname"));

                map.put("userName",rs.getString("username"));
                map.put("status",rs.getString("status"));
                map.put("level",rs.getString("level"));

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


    public  ArrayList getAllUsers(String searchValue)
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
            String sqll="Select firstname,middlename,lastname,username,useraccounts.status,level  from useraccounts,staffs where (userName like '"+searchValue+"%' or useraccounts.status like '"+searchValue+"%') and staffs.employeeCode=useraccounts.employeeCode and Departmentcode='"+deptcode
                    +"'  order by username";
            ps=con.prepareStatement(sqll);
            rs= ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","success");

                map.put("user",rs.getString("firstname")+"  "+rs.getString("middlename")+"  "+rs.getString("lastname"));

                map.put("userName",rs.getString("username"));
                map.put("status",rs.getString("status"));
                map.put("level",rs.getString("level"));

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

    public  Map deleteUser(String userName)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String querry1 = "DELETE from  useraccounts where username='" + userName + "' ";
            ps = con.prepareStatement(querry1);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","User Account Deleted");
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


    public  Map deactivateUserAccount(String userName)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {



            String querry1 = "Update   useraccounts set Status='"+"IN ACTIVE"+"' where username='" + userName + "' ";
            ps = con.prepareStatement(querry1);
            ps.execute();

            map.put("responseCode","200");
            map.put("responseDescription","User Account Deleted");
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
