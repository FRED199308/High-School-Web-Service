package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeeBalances {
    String schoolDb;
Globals globals;
    public FeeBalances(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    public  ArrayList getFeeBalances(String className, String streamName)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
          double  totalpaid = 0;
            double total = 0;

            if(className.equalsIgnoreCase("all")&& streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  ");

            }
            else if(!className.equalsIgnoreCase("All")&& streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentform='"+globals.classCode(className)+"'");

            }
            else  if(className.equalsIgnoreCase("All")&&! streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentstream='"+globals.streamcode(streamName)+"'");

            }
            else  if(!className.equalsIgnoreCase("All")&&! streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentstream='"+globals.streamcode(streamName)+"' and  currentform='"+globals.classCode(className)+"'");

            }
            rs=ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                String adm=rs.getString("admissionnumber");
                map.put("admissionNumber",adm);
                map.put("studentName",rs.getString("FirstName") + " " + rs.getString("MiddleName") + " " + rs.getString("LastName"));
map.put("className",globals.className(rs.getString("classcode")));
map.put("streamName",globals.streamName(rs.getString("streamcode")));
map.put("balance", globals.balanceCalculator(adm));




                ps=con.prepareStatement("Select sum(amount) from reciepts where admnumber='"+adm+"'");
                ResultSet rx=ps.executeQuery();
                if(rx.next())
                {
                    if(rx.getString("Sum(amount)")==null)
                        map.put("totalPaid",0);
                    else
                    map.put("totalPaid",rx.getString("Sum(amount)"));


                }
                else{
                    map.put("totalPaid",0);
                }

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
    public  ArrayList getFeeBalancesOverAFigure(String className,String streamName,double amount)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            double  totalpaid = 0;
            double total = 0;

            if(className.equalsIgnoreCase("all")&& streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  ");

            }
            else if(!className.equalsIgnoreCase("All")&& streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentform='"+globals.classCode(className)+"'");

            }
            else  if(className.equalsIgnoreCase("All")&&! streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentstream='"+globals.streamcode(streamName)+"'");

            }
            else  if(!className.equalsIgnoreCase("All")&&! streamName.equalsIgnoreCase("All"))
            {
                ps=con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode,firstName,middleName,lastName from admission  where  currentstream='"+globals.streamcode(streamName)+"' and  currentform='"+globals.classCode(className)+"'");

            }
            rs=ps.executeQuery();
            while(rs.next())
            {

                Map map=new HashMap();
                String adm=rs.getString("admissionnumber");
                double bal=globals.balanceCalculator(adm);
                map.put("admissionNumber",adm);
                map.put("studentName",rs.getString("FirstName") + " " + rs.getString("MiddleName") + " " + rs.getString("LastName"));
                map.put("className",globals.className(rs.getString("classcode")));
                map.put("streamName",globals.streamName(rs.getString("streamcode")));
                map.put("balance", bal);





                ps=con.prepareStatement("Select sum(amount) from reciepts where admnumber='"+adm+"'");
                ResultSet rx=ps.executeQuery();
                if(rx.next())
                {

if(bal>amount)
{
    if(rx.getString("Sum(amount)")==null)
        map.put("totalPaid",0);
    else
        map.put("totalPaid",rx.getString("Sum(amount)"));
}

                }
                else{
                    map.put("totalPaid",0);
                }
if(bal>amount)
{
    arrayList.add(map);
}

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


}
