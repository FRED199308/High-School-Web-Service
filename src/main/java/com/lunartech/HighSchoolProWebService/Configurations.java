package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Configurations {
    String schoolDb;

    public Configurations(String schoolDb) {
        this.schoolDb = schoolDb;
    }

    public  Map getConfigurations()
    {

        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry = "Select * from systemconfiguration";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {

                map.put(rs.getString("configurationName").toLowerCase(), rs.getString("status"));
                map.put(rs.getString("configurationId").toLowerCase(), rs.getString("status"));

            }
            map.put("responseCode","200");
            map.put("responseDescription","success");
            return map;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());

            sq.printStackTrace();
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


    public  Map saveConfigurations(Map mapp)
    {

        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {





            String querry = "Select * from systemconfiguration";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
String configurationName=rs.getString("configurationName").toLowerCase();
String configurationId=rs.getString("configurationId").toLowerCase();
if(mapp.containsKey(configurationId))
{
    String sql="Update systemconfiguration set status='"+mapp.get(configurationId)+"' where configurationid='"+configurationId+"'";
    ps=con.prepareStatement(sql);
    ps.execute();

}
else if(mapp.containsKey(configurationName))
{

    String sql="Update systemconfiguration set status='"+mapp.get(configurationName)+"' where configurationname='"+configurationName+"'";
    ps=con.prepareStatement(sql);
    ps.execute();
}





            }
            map.put("responseCode","200");
            map.put("responseDescription","Configuration Updated Successfully");
            return map;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());

            sq.printStackTrace();
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
