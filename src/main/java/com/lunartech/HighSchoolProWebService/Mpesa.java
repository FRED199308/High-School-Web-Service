package com.lunartech.HighSchoolProWebService;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mpesa {
String schoolDb;
    SMS sms;
    public Mpesa(String schoolDb) {
        this.schoolDb = schoolDb;
        sms=new SMS(schoolDb);
    }

    public ArrayList unreadMpesaRecords()
    {


        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {

            String sqls="Select *  from mobile_payments where readstatus is null order by transtime";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("responseCode","200");
                map.put("responseDescription","success");
                map.put("transLoID", rs.getString("transLoID"));
                map.put("transactionType", rs.getString("TransactionType"));
                map.put("transId", rs.getString("TransID"));
                map.put("transTime", rs.getString("TransTime"));
                map.put("transAmount", rs.getString("TransAmount"));
                map.put("businessShortCode", rs.getString("BusinessShortCode"));
                map.put("billRefNumber", rs.getString("BillRefNumber"));
                map.put("invoiceNumber", rs.getString("InvoiceNumber"));
                map.put("orgAccountBalance", rs.getString("OrgAccountBalance"));
                map.put("thirdPartyTransID", rs.getString("ThirdPartyTransID"));
                map.put("mSISDN", rs.getString("MSISDN"));
                map.put("firstName", rs.getString("FirstName"));
                map.put("MiddleName", rs.getString("MiddleName"));
                map.put("LastName", rs.getString("LastName"));
                map.put("readStatus", rs.getString("readStatus"));
                map.put("adm", rs.getString("adm"));
                map.put("finalBalance", rs.getString("FinalBalance"));

                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }
    public ArrayList allMpesaRecords()
    {


        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {

            String sqls="Select *  from mobile_payments order by transtime";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

            while(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("responseCode","200");
                map.put("responseDescription","success");
                map.put("transLoID", rs.getString("transLoID"));
                map.put("transactionType", rs.getString("TransactionType"));
                map.put("transId", rs.getString("TransID"));
                map.put("transTime", rs.getString("TransTime"));
                map.put("transAmount", rs.getString("TransAmount"));
                map.put("businessShortCode", rs.getString("BusinessShortCode"));
                map.put("billRefNumber", rs.getString("BillRefNumber"));
                map.put("invoiceNumber", rs.getString("InvoiceNumber"));
                map.put("orgAccountBalance", rs.getString("OrgAccountBalance"));
                map.put("thirdPartyTransID", rs.getString("ThirdPartyTransID"));
                map.put("mSISDN", rs.getString("MSISDN"));
                map.put("firstName", rs.getString("FirstName"));
                map.put("MiddleName", rs.getString("MiddleName"));
                map.put("LastName", rs.getString("LastName"));
                map.put("readStatus", rs.getString("readStatus"));
                map.put("adm", rs.getString("adm"));
                map.put("finalBalance", rs.getString("FinalBalance"));

                arr.add(map);

            }

            return arr;
        } catch (Exception sq)
        {

            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
            sq.printStackTrace();
            return arr;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }


    public Map<String,String> retrieveTransactionStatus(String mpesaCode)
    {


        ArrayList<Map> arr=new ArrayList<Map>();
        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {

            String sqls="Select *  from transactionstatus  where receiptNo='"+mpesaCode+"'";
            ps=con.prepareStatement(sqls);
            rs=ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();

           if(rs.next())
            {
                map=new HashMap<String,String>();
                map.put("responseCode","200");
                map.put("responseDescription","success");
                map.put("OriginatorConversationID", rs.getString("OriginatorConversationID"));
                map.put("ResultCode", rs.getString("ResultCode"));
                map.put("ResultDesc", rs.getString("ResultDesc"));
                map.put("DebitPartyName", rs.getString("DebitPartyName"));
                map.put("CreditPartyName", rs.getString("CreditPartyName"));
                map.put("InitiatedTime", rs.getString("InitiatedTime"));
                map.put("TransactionReason", rs.getString("TransactionReason"));
                map.put("ReasonType", rs.getString("ReasonType"));
                map.put("FinalisedTime", rs.getString("FinalisedTime"));
                map.put("Amount", rs.getString("Amount"));
                map.put("ReceiptNo", rs.getString("ReceiptNo"));
                map.put("ResultType", rs.getString("ResultType"));
                map.put("TransactionID", rs.getString("TransactionID"));
//                map.put("readStatus", rs.getString("readStatus"));
//                map.put("adm", rs.getString("adm"));
//                map.put("finalBalance", rs.getString("FinalBalance"));

                arr.add(map);

            }

            return map;
        } catch (Exception sq)
        {
map=new HashMap<String,String>();
            map.put("responseCode","501");
            map.put("responseDescription","Internal Error :"+sq.getMessage());
            arr.add(map);
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

    public  Map recordMpesaTransaction(Map transaction)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {



            String sqll="INSERT  into mobile_payments ( `TransactionType`, `TransID`, `TransTime`, `TransAmount`, `BusinessShortCode`," +
                    " `BillRefNumber`, `InvoiceNumber`, `OrgAccountBalance`, `ThirdPartyTransID`, `MSISDN`, `FirstName`, `MiddleName`, `LastName`)  values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            ps=con.prepareStatement(sqll);
            ps.setObject(1,transaction.get("TransactionType"));
            ps.setObject(2,transaction.get("TransID"));

            ps.setObject(3,transaction.get("TransTime"));
            ps.setObject(4,transaction.get("TransAmount"));
            ps.setObject(5,transaction.get("BusinessShortCode"));
            ps.setObject(6,transaction.get("BillRefNumber"));
            ps.setObject(7,transaction.get("InvoiceNumber"));
            ps.setObject(8,transaction.get("OrgAccountBalance"));
            ps.setObject(9,transaction.get("ThirdPartyTransID"));
            ps.setObject(10,transaction.get("MSISDN"));
            ps.setObject(11,transaction.get("FirstName"));
            ps.setObject(12,transaction.get("MiddleName"));
            ps.setObject(13,transaction.get("LastName"));

ps.execute();
String message="Dear "+transaction.get("FirstName")+" "+transaction.get("LastName")+" "+" We Have Received Your Payment Of Ksh. "+transaction.get("TransAmount")+" to Admission Number "+transaction.get("BillRefNumber");

            ArrayList   messagesList=new ArrayList<Map>();
            HashMap messageData=new HashMap<String,String>();
messageData.put("phone",new String[]{transaction.get("MSISDN").toString()});
messageData.put("message",message);
messagesList.add(messageData);
sms.sendSms(messagesList);

            map.put("responseCode","200");
            map.put("responseDescription","Record Saved");
            return map;
        }
        catch ( SQLException sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.toString());
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

    public  Map recordMpesaTransactionStatus(Map transaction)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            File fileTwo=new File("filetwo.txt");
            FileOutputStream fos=new FileOutputStream(fileTwo);
            PrintWriter pw=new PrintWriter(fos);


            pw.println(transaction);


            pw.flush();
            pw.close();
            fos.close();
Map result=(Map)((Map)transaction.get("Result"));
Map resultparameters= (Map) result.get("ResultParameters");
System.err.println(transaction);

ArrayList list= (ArrayList) resultparameters.get("ResultParameter");
String debitname="";

System.err.println(result);

            String sqll="INSERT  into transactionstatus ( `ConversationID`, `OriginatorConversationID`, `ResultCode`, `ResultDesc`, `DebitPartyName`," +
                    " `CreditPartyName`, `InitiatedTime`, `DebitAccountType`, `DebitPartyCharges`, `TransactionReason`, `ReasonType`, `TransactionStatus`, `FinalisedTime`   , `Amount` , `ReceiptNo` , `ResultType` , `TransactionID`  )  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            ps=con.prepareStatement(sqll);
            ps.setObject(1,(result.get("ConversationID")));
            ps.setObject(2,(result.get("OriginatorConversationID")));
System.err.println(((Map)list.get(8)).get("Value"));
            ps.setObject(3,result.get("ResultCode"));
            ps.setObject(4, result.get("ResultDesc"));
            ps.setObject(5,((Map)list.get(0)).get("Value"));
            ps.setObject(6,((Map)list.get(1)).get("Value"));
            ps.setObject(7,((Map)list.get(3)).get("Value"));
            ps.setObject(8,((Map)list.get(4)).get("Value"));
            ps.setObject(9,((Map)list.get(5)).get("Value"));
            ps.setObject(10,((Map)list.get(6)).get("Value"));
            ps.setObject(11,((Map)list.get(7)).get("Value"));
            ps.setObject(12,((Map)list.get(8)).get("Value"));
            ps.setObject(13,((Map)list.get(9)).get("Value"));
            ps.setObject(14,((Map)list.get(10)).get("Value"));
            ps.setObject(15,((Map)list.get(12)).get("Value"));
            ps.setObject(16,result.get("ResultType"));
            ps.setObject(17,result.get("TransactionID"));


            ps.execute();


            map.put("responseCode","200");
            map.put("responseDescription","Record Saved");
            return map;
        }
        catch ( Exception sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.toString());
            return map;
        }
        finally {
            try {
                //con.close();
               //
            } catch (Exception sq) {

                sq.printStackTrace();
            }

        }
    }

    public  Map updateTransactionReadStatus(Map transaction)
    {
        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {



            String sql="Update mobile_payments set readstatus='"+"1"+"',finalbalance='"+transaction.get("finalBalance")+"' where TransID='"+transaction.get("transId")+"'";
            ps=con.prepareStatement(sql);

            ps.execute();
            System.err.println(transaction);

            map.put("responseCode","200");
            map.put("responseDescription","Record Updated");
            return map;
        }
        catch ( SQLException sq) {

            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+sq.toString());
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
