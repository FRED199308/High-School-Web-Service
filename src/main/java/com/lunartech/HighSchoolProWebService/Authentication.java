package com.lunartech.HighSchoolProWebService;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    String username,psssword;
String schoolDb;
Globals globals;
    public Authentication(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    public Authentication(String username, String psssword) {
        this.username = username;
        this.psssword = psssword;
    }

    public String getUsername() {
        return username;
    }

    public String getPsssword() {
        return psssword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPsssword(String psssword) {
        this.psssword = psssword;
    }

    public  Map authenticate(String username, String password)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con= DbConnection.connectDb(schoolDb);
        Map map=new HashMap();
        try {
            //System.out.println("Izi Zinafika huku "+username+"  "+password+" Alfu natumia hii db :"+DbConnection.HOST_DB);
            con= DbConnection.connectDb(schoolDb);
            String empcode,departcode="",name="";
            String sql="Select * from useraccounts where username='"+username+"' and password='"+ DataEncriptor.encript(password)+"'";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next())
            {String level=rs.getString("level");
                globals.Level=level;

map.put("level",level);
               String status=rs.getString("Status");
                String pass= DataEncriptor.decriptor(rs.getString("Password"));
                globals.CurrentUser=username;
                empcode=rs.getString("Employeecode");
                String querry="Select initials from staffs where employeecode='"+empcode+"' ";
                ps=con.prepareStatement(querry);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    globals.initials=rs.getString("Initials");
                }
                map.put("employeeCode",empcode);

                globals.empcode=empcode;
                String sql4="Select departmentcode from staffs where employeecode='"+empcode+"' ";
                ps=con.prepareStatement(sql4);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    departcode=rs.getString("Departmentcode");

                }
                globals.depcode=departcode;
                String sql5="Select name from departments where departmentcode='"+departcode+"'";
                ps=con.prepareStatement(sql5);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    name=rs.getString("Name");
                }
                globals.depName=name;

map.put("department",name);
map.put("departmentCode",departcode);
                if(status.equalsIgnoreCase("Active"))
                {
                    if (pass.equalsIgnoreCase("pass")) {
String allowDefaultPassword="true";
                        String querry8 = "Select status from systemconfiguration where configurationid='" + "CO007" + "'";
                        ps = con.prepareStatement(querry8);
                        rs = ps.executeQuery();
                      if (rs.next()) {

                          allowDefaultPassword=rs.getString("Status");

                      }
                            if (allowDefaultPassword.equalsIgnoreCase("false")) {

                                map.put("responseCode","200");
                                map.put("responseDescription","Login Success");
                                return map;
                            } else {

                                map.put("responseCode","200");
                                map.put("responseDescription","Login Success");
                                return map;

                            }


                    } else {

                        map.put("responseCode","200");
                        map.put("responseDescription","Login Success");
                        return map;
                    }
                }
                else{
                    map.put("responseCode","501");
                    map.put("responseDescription","User Account Deactivated");
                    return map;
                }







            }
            else{
                map.put("responseCode","501");
                map.put("responseDescription","Login Failed\nWrong Username Password Combination");
                return map;
            }



        } catch (HeadlessException | SQLException sq) {
            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription",sq.getMessage());
            return map;

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
