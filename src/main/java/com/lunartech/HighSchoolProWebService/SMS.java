package com.lunartech.HighSchoolProWebService;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SMS {
    ConfigurationIntialiser configurationIntialiser;
    Globals globals;
    public SMS(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    String schoolDb;
    public  Map smsClass(String recipient, String message)
    {

        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        String CONTACTS="";

try {
    ArrayList   messagesList=new ArrayList<Map>();
    HashMap messageData=new HashMap<String,String>();

    if(recipient.equalsIgnoreCase("All Active Parents"))
    {

        String querr="select parentfullnames,telephone1,admissionnumber  from admission where currentform like '"+"FM"+"%'";
        con=DbConnection.connectDb(schoolDb);
        ps=con.prepareStatement(querr);
        rs=ps.executeQuery();
        ResultSetMetaData meta=rs.getMetaData();


        while(rs.next())
        {

            boolean testa=false;

            String p=rs.getString("telephone1");


            if(p.equalsIgnoreCase(""))
            {

            }
            else{


                p="254"+p.substring(1);
                messageData=new HashMap<String,String>();
                messageData.put("message", message);


                messageData.put("phone", p);
                messagesList.add(messageData);
            }




        }
        String response="";
        if(!messagesList.isEmpty())
        {
          response= batchSaver(messagesList);
        }




    if(response.contains("Error"))
    {
        map.put("responseCode","501");
        map.put("responseDescription",response);
        return map;
    }
    else {
        map.put("responseCode","200");
        map.put("responseDescription",response);
        return map;


    }






    }
    else{

        String classcode=globals.classCode(recipient);

        String querr="select parentfullnames,telephone1,admissionnumber  from admission where currentform='"+classcode+"'";
        con=DbConnection.connectDb(schoolDb);
        ps=con.prepareStatement(querr);
        rs=ps.executeQuery();
        ResultSetMetaData meta=rs.getMetaData();



        while(rs.next())
        {

            boolean testa=false;

            String p=rs.getString("telephone1");


            if(p.equalsIgnoreCase(""))
            {

            }
            else{
                p="254"+p.substring(1);
                messageData=new HashMap<String,String>();
                messageData.put("message", message);

                messageData.put("phone", p);
                messagesList.add(messageData);
            }




        }
        String response="";
        if(!messagesList.isEmpty())
        {
         response=   batchSaver(messagesList);
        }

        if(configurationIntialiser.smsOfflineSender())
        {
            Map mp=new HashMap<>();
            ArrayList arrayList=new ArrayList();
            mp.put("responseCode","200");
            mp.put("responseDescription","Contacts Fetched");
            mp.put("message",message);
            mp.put("contacts",CONTACTS.split(":"));
            return mp;

        }
        else{

            if(response.contains("Error"))
            {
                map.put("responseCode","501");
                map.put("responseDescription",response);
                return map;
            }
            else {
                map.put("responseCode","200");
                map.put("responseDescription",response);
                return map;
            }

        }





    }



}
catch(Exception sq)
{
 map=new HashMap();
        map.put("responseCode","510");
        map.put("responseDescription","Error:"+sq.getMessage());
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

    public  Map smsExamToParents(String academicYear,String className,String term,String examName,String examCode,String admissionNumber,boolean smsFee)
    {

        Map<String,String> map=new HashMap<String,String>();
        Connection con=DbConnection.connectDb(schoolDb);;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            String admissionFilter="";
            if(admissionNumber.isEmpty())
            {

            }
            else {

                admissionFilter=" and admNumber='"+admissionNumber+"'";




            }
                ArrayList   messagesList=new ArrayList<Map>();
                HashMap messageData=new HashMap<String,String>();


                    String nextTermFee="";
                    int year=Integer.parseInt(academicYear);
                    String termcode="";
                    String termname="";
                    String classcodefee=globals.classCode(className);

                    String sql="Select * from terms where status='"+"Next"+"'";
                    ps=con.prepareStatement(sql);
                    ResultSet rr=ps.executeQuery();
                    if(rr.next())
                    {
                        termcode=rr.getString("Termcode");
                        termname=rr.getString("TermName");
                    }
                    if(termname.equalsIgnoreCase("Term 1"))
                    {
                        year+=1;
                    }
                    else{

                    }


                    String sqla="Select classpositionthisterm,classpositionthistermoutof,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,totalmarks,totalpoints from examanalysistable where academicyear='"+academicYear+"' and  classname='"+className+"' and examcode='"+examCode+"' "+admissionFilter+" group by admnumber order by classpositionthisterm";
                    ps=con.prepareStatement(sqla,ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    rs=ps.executeQuery();
                    while(rs.next())
                    {

                        String grade="",examscore="",meangrade="",totalmarks,totalpoints,meanpoints,subjectName;
                        int entries=0;
                        meangrade=rs.getString("meangrade");
                        totalmarks=rs.getString("totalmarks");
                        totalpoints=rs.getString("totalpoints");
                        String adm=rs.getString("admnumber");
                        String ovrposition=rs.getString("classpositionthisterm");
                        String streamposition=rs.getString("Streampositionthisterm");
                        String ovroutof=rs.getString("classpositionthistermoutof");
                        String streamoutof="";
                        String Name=rs.getString("fullName") ;

                        String Examname="";
                        if(examName.equalsIgnoreCase("Total"))
                        {
                            Examname="AVERAGE";
                        }
                        else{
                            Examname=examName;
                        }


                        String subjectResults=term+" "+Examname+" Results For :"+Name+" ";


                        String sqlb="SElect Distinct subjectcode from subjects order by subjectcode";
                        ps=con.prepareCall(sqlb);
                        ResultSet rsb=ps.executeQuery();

                        while(rsb.next())
                        {String subcode=rsb.getString("Subjectcode");

                            String sqlc="Select  exampercentage,subjectexamgrade,subjectName from examanalysistable where academicyear='"+academicYear+"' and examcode='"+examCode+"'  and subjectcode='"+subcode+"' and admnumber='"+adm+"' and classname='"+className+"'";
                            ps=con.prepareStatement(sqlc);
                            ResultSet rsc=ps.executeQuery();
                            if(rsc.next())
                            {
                                examscore=rsc.getString("ExamPercentage");
                                grade=rsc.getString("subjectexamgrade");

                                subjectResults=subjectResults+" "+rsc.getString("subjectName")+": "+examscore+" "+grade+",";
                            }
                            else{
                                examscore="";
                                grade="";
                            }

                        }


                        subjectResults=subjectResults+" Total Points:"+totalpoints+" Mean Grade:"+rs.getString("meangrade")+" Overall Pos:"+rs.getString("classpositionthisterm")+ " Out Of :"+ovroutof+".  "+nextTermFee;

                        sql="Select gender,parentfullNames,telephone1 from admission where admissionnumber='"+adm+"'";
                        ps=con.prepareStatement(sql);
                        ResultSet RS=ps.executeQuery();
                        if(RS.next())
                        {


                            if(!RS.getString("telephone1").equalsIgnoreCase(""))
                            {
                                String p=RS.getString("telephone1");
                                String phone;
                                String message;
                                String name=RS.getString("parentfullnames");
                                message=subjectResults;

//                                if(option2.isSelected())
//                                {
//                                    message=message+".Next Term Payable Fee KSH "+(globals.balanceCalculator(adm)+globals.nextTermFee(adm, termcode, String.valueOf(year), classcodefee));
//                                }

                                p="254"+p.substring(1);
                                phone=p;
                                messageData=new HashMap<String,String>();
                                messageData.put("message", message);
                                messageData.put("phone", phone);
                                messagesList.add(messageData);




                            }
                            else{

                            }

                        }


                    }


                    if(rs.last())
                    {
                     String response=   batchSaver(messagesList);
                        map.put("responseCode","200");
                        map.put("responseDescription",response);

                    }
                    else{
                        map.put("responseCode","404");
                        map.put("responseDescription","Exam Results Not Found\n Please Analyse The Exam if You Have Already Entered The Marks");

                    }
            return map;


        } catch (Exception sq) {

            map.put("responseCode","501");
            map.put("responseDescription","Error:"+sq.toString());
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


    public    String sendSms(ArrayList<Map> MessageLists) {
        Gson g=new Gson();
        Map data =new HashMap();
        data.put("senderid", "Wyzer");
        data.put("secretkey", configurationIntialiser.smsKey());
        data.put("userid", configurationIntialiser.smsUsername1());
        data.put("messageParameters", MessageLists);


        String query_url = "http://api.lunar.cyou/api/Multsendsms.php";




        System.err.println(g.toJson(data));

        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(g.toJson(data).getBytes("UTF-8"));
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = "";
            //   String result = IOUtils.toString(in, "UTF-8");





            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            for (int res = bis.read(); res != -1; res = bis.read()) {
                buf.write((byte) res);
            }
// StandardCharsets.UTF_8.name() > JDK 7
            result= buf.toString("UTF-8");

            in.close();

            conn.disconnect();
            System.err.println(result);

            if(result.contains("["))
            {
//                 JSONArray jsonArray = new JSONArray(result);
//         // jsonArray.put(myResponse);
//               System.out.println(jsonArray);
//               for(int i =0;i<=jsonArray.length();++i)
//               {
//                   System.out.println(jsonArray.getJSONObject(i).get("status"));
//               }
                return MessageLists.size()+" Message(s) Sent SuccessFully";
            }
            else{
                JSONObject myResponse = new JSONObject(result);

                if(myResponse.getString("status").equalsIgnoreCase("201"))
                {
                    return "Error: Invalid Credentials,Please Check The Credentials And Try Again";
                }
                else if(myResponse.getString("status").equalsIgnoreCase("101")){
                    return "Error :You Have Insufficient Balance,Please Recharge and Try Again.Balance ksh:"+myResponse.getString("Balance");

                }
                else if(myResponse.getString("status").equalsIgnoreCase("301")){
                    return "Error :Invalid senderID!";

                }
                else if(myResponse.getString("status").equalsIgnoreCase("0")){
                    return "Messages Sent Successfully";

                }
                else{
                    return "Error: Unknown,Please Contact Developer";
                }
            }



        } catch (Exception e) {

            e.printStackTrace();
            return "Error Occured"+e.toString();
        }

    }



//    public  String smsBalance() {
//        String query_url = configurationIntialiser.smsBalanceUrl();
//        String json = "{ \"data\":{\"senderid\":\""+"22136"+"\",\"secretkey\":\""+configurationIntialiser.smsKey()+"\",\"userid\":\""+configurationIntialiser.smsUsername1()+"\"";
//
//        String m="";
//
//        m=m+"}}";
//        json=json+m;
//        System.out.println(json);
//        try {
//            URL url = new URL(query_url);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            OutputStream os = conn.getOutputStream();
//            os.write(json.getBytes("UTF-8"));
//            os.close();
//            // read the response
//            InputStream in = new BufferedInputStream(conn.getInputStream());
//            String result = IOUtils.toString(in, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//            System.out.println(result);
//            JSONParser jsonParser = new JSONParser();
//            org.json.simple.JSONObject myResponse = (org.json.simple.JSONObject)jsonParser.parse(bufferedReader);
//            Gson gson=new Gson();
//
//
//            in.close();
//            conn.disconnect();
//
//
//
//            if(myResponse.get("ResponseCode").toString().equalsIgnoreCase("0")){
//                return " Ksh "+myResponse.get("actual_Balance")+",Or Sms: "+myResponse.get("sms_Balance")+" Item(s)";
//
//            }
//            else{
//                return "Error";
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//            return "Error Occured"+e.toString();
//        }
//    }


    public  String smsBalance() {
        String query_url = "http://api.lunar.cyou/api/smsbalance.php";
        String json = "{ \"data\":{\"senderid\":\""+"22136"+"\",\"secretkey\":\""+configurationIntialiser.smsKey()+"\",\"userid\":\""+configurationIntialiser.smsUsername1()+"\"";

        String m="";

        m=m+"}}";
        json=json+m;
        // System.err.println(json);
        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            JSONParser jsonParser = new JSONParser();

            org.json.simple.JSONObject myResponse = (org.json.simple.JSONObject)jsonParser.parse(bufferedReader);






            conn.disconnect();
           // return (Map) myResponse;



            if(myResponse.get("ResponseCode").toString().equalsIgnoreCase("0")){
              //  return " Ksh "+myResponse.get("actual_Balance")+",Or Sms: "+myResponse.get("sms_Balance")+" Item(s)";
                return " Ksh "+myResponse.get("actual_Balance");

           }
            else{
               return "Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map map=new HashMap();
            map.put("responseCode","501");
            map.put("responseDescription","An Error Occured:"+e.getMessage());
            return "An Error Occured:"+e.getMessage();
        }
    }





    public    String batchSaver(ArrayList<Map> messageList)
    {
        String responce="";

        responce =sendSms(messageList);

        String finalResponce = responce;
        new Thread(){

            public void run()
            {

                try {


                    SimpleDateFormat dateformat;
                    dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String da = dateformat.format(date);

                    Connection con;
                    PreparedStatement ps = null;
                    ResultSet rs;
                    con = DbConnection.connectDb(schoolDb);

                    try {






                        if(messageList.size()>2)
                        {
                            adminMessageConfirmation("Messages Sent Successfully");
                        }

                        if(finalResponce.startsWith("Error"))
                        {
                            for (int i=0;i<messageList.size();++i) {
                                String querry4 = "insert into smsrecord  (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone) values(?,'" + da + "','"+""+"','" + IdGenerator.keyGen() + "','" + globals.CurrentUser+ "','" + "Failed" + "','" + ((Map)messageList.get(i)).get("phone").toString() + "')";

                                PreparedStatement Fine;

                                Fine = con.prepareStatement(querry4);
                                Fine.setString(1, ((Map)messageList.get(i)).get("message").toString());
                                Fine.execute();

                            }


                        }
                        else{
                            for (int i=0;i<messageList.size();++i) {
                                String querry4 = "insert into smsrecord (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone)  values(?,'" + da + "','"+""+"','" + IdGenerator.keyGen() + "','" + globals.CurrentUser+ "','" + "Sent" + "','" + ((Map)messageList.get(i)).get("phone").toString() + "')";

                                PreparedStatement Fine;
                                Fine = con.prepareStatement(querry4);
                                Fine.setString(1, ((Map)messageList.get(i)).get("message").toString());
                                Fine.execute();


                            }
                        }







                    } catch(Exception ex) {

                        // JOptionPane.showMessageDialog(null, "System Unable To Queue Message check Internet Possibly\n Kindly fix and Retry"+ex.getMessage());
                        ex.printStackTrace();
                        for(int i=0;i<messageList.size()-1;++i)
                        {
                            IdGenerator k = new IdGenerator();
                            String id = "SMS" + IdGenerator.keyGen();
                            String querry5 = "insert into smsrecord values('" + ((Map)messageList.get(i)).get("message").toString() + "',Now(),'"+""+"','" + id + "','" + globals.CurrentUser+ "','" + "Failed" + "','" + ((Map)messageList.get(i)).get("phone").toString()+ "')";
                            ps = con.prepareStatement(querry5);
                            ps.execute();

                        }

                    }










                } catch (Exception sq) {

                    sq.printStackTrace();

                }



            }
        }.start();
        return responce;

    }


    public  Map oneForeignMessageQueue(String phone,String message)
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String pf = phone.replaceFirst("0", "254");
        ArrayList   messagesList=new ArrayList<Map>();
        HashMap messageData=new HashMap<String,String>();
        messageData.put("phone",pf);
        messageData.put("message",message);
        messagesList.add(messageData);
        phone = pf;
        try {
            if(configurationIntialiser.smsOfflineSender())
            {

                PreparedStatement ps1;

                String sqlInsert =
                        "INSERT INTO "+
                                "ozekimessageout (receiver,msg,status) "+
                                "VALUES "+
                                "('" + pf + "',?,'send')";
                ps1=con.prepareStatement(sqlInsert);
                ps1.setString(1, message);
                ps1.execute();
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","Message Queued");
                return map;
            }
            else{

                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);


                String[] recipients = new String[] {
                        phone
                };

                try {


                    String responce=batchSaver(messagesList);

                    if(responce.startsWith("Error"))
                    {
                        for (int i=0;i<recipients.length;++i) {
                            String querry4 = "insert into smsrecord  (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone) values(?,'" + da + "','"+""+"','" + IdGenerator.keyGen() + "','" + globals.CurrentUser+ "','" + "Failed" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;

                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                    }
                    else{
                        for (int i=0;i<recipients.length;++i) {
                            String querry4 = "insert into smsrecord (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone)  values(?,'" + da + "','"+""+"','" + IdGenerator.keyGen() + "','" + globals.CurrentUser+ "','" + "Sent" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;
                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                    }
               if(responce.contains("Error"))
               {
                   Map map=new HashMap();
                   map.put("responseCode","501");
                   map.put("responseDescription",responce);
                   return map;
               }
               else{
                   Map map=new HashMap();
                   map.put("responseCode","200");
                   map.put("responseDescription",responce);
                   return map;
               }


                } catch(Exception ex) {
                    IdGenerator k = new IdGenerator();
                    String id = "SMS" + IdGenerator.keyGen();
                    String querry5 = "insert into smsrecord values('" + message + "','" + da + "','"+""+"','" + id + "','" + globals.CurrentUser + "','" + "Failed" + "','" + phone+ "')";
                    ps = con.prepareStatement(querry5);
                    ps.execute();
                    ex.printStackTrace();
                    Map map=new HashMap();
                    map.put("responseCode","510");
                    map.put("responseDescription","Error:"+ex.getMessage());
                    return map;
                }
            }



        } catch (Exception sq) {
            sq.printStackTrace();
            Map map=new HashMap();
            map.put("responseCode","501");
            map.put("responseDescription","Error:"+sq.getMessage());
            return map;
        }

    }



    public  void adminMessageConfirmation(String message)
    {

        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);


        String pf ="",pi;
        try {

            String sq="Select PhoneNumber from staffs,useraccounts where level='"+"Admin"+"' and staffs.employeecode=useraccounts.EmployeeCode";
            ps=con.prepareStatement(sq);
            rs=ps.executeQuery();
            while(rs.next())
            {

                pi=rs.getString("PhoneNumber").replaceFirst("0", "+254")+":";
                pf=pf.concat(pi+":");



            }

            String [] phones=pf.split(":");





            if(configurationIntialiser.smsOfflineSender())
            {
                for(int i=0;i<phones.length;i++)
                {
                    if(phones[i].equals(""))
                    {

                    }
                    else{
                        String phone="";
                        pf = phones[i];

                        PreparedStatement ps1;

                        String sqlInsert =
                                "INSERT INTO "+
                                        "ozekimessageout (receiver,msg,status) "+
                                        "VALUES "+
                                        "('" + pf + "',?,'send')";
                        ps1=con.prepareStatement(sqlInsert);
                        ps1.setString(1, message);
                        ps1.execute();

                    }

                }

                { //Developer notification...............


                    PreparedStatement ps1;

                    String sqlInsert =
                            "INSERT INTO "+
                                    "ozekimessageout (receiver,msg,status) "+
                                    "VALUES "+
                                    "('" + "+254707353225" + "','"+ "Developer Alert System In Use At "+configurationIntialiser.schoolInfor() +"','send')";
                    ps1=con.prepareStatement(sqlInsert);
                    ps1.execute();
                }
            }
            else{


                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);

//                String[] recipients = phones;
//                String responce=sendSms(message, recipients);
//                String developercontact[]={"+254707353225"};
//                sendSms("Developer Alert System In Use At "+configurationIntialiser.schoolInfor(), developercontact);

                try {



                    try {








                    } catch (Exception e) {
                    }



                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "System Unable To Queue Message check Internet Possibly\n Kindly fix and Retry"+ex.getMessage());
                    ex.printStackTrace();
                    for(int i=0;i<phones.length-1;++i)
                    {
                        IdGenerator k = new IdGenerator();
                        String id = "SMS" + IdGenerator.keyGen();
                        String querry5 = "insert into smsrecord values('" + message + "',Now(),'"+""+"','" + id + "','" + globals.CurrentUser + "','" + "Failed" + "','" + phones[i]+ "')";
                        ps = con.prepareStatement(querry5);
                        ps.execute();

                    }
                }


            }







        } catch (Exception sq) {
            sq.printStackTrace();
        }


    }

    public  ArrayList getAllSms(String status)
    {
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {
            String sqll="Select * from smsrecord  order by date";
            if(status.equalsIgnoreCase("All"))
            {
              sqll="Select * from smsrecord  order by date";
            }
            else{
                sqll="Select * from smsrecord where status='"+status+"' order by date";
            }



            ps=con.prepareStatement(sqll);
            rs= ps.executeQuery();
            while(rs.next())
            {
                Map map=new HashMap();
                map.put("responseCode","200");
                map.put("responseDescription","success");


                map.put("sms",rs.getString("smscontent"));
                map.put("date",rs.getString("date"));
                map.put("status",rs.getString("status"));
                map.put("phone",rs.getString("phone"));
                map.put("smsid",rs.getString("smsid"));
                String name;
              String  phone="0"+rs.getString("phone").substring(4, rs.getString("phone").length()).trim();
                String sql2="Select firstname,middlename,lastname from admission where telephone1='"+phone+"'";
                ps=con.prepareStatement(sql2.toLowerCase());
                ResultSet RS=ps.executeQuery();
                if(RS.next())
                {
                    name=RS.getString("firstName")+"    "+RS.getString("Middlename")+"    "+RS.getString("LastName");
                }
                else {
                    name="Un Known";
                }
                map.put("studentName",name);

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
    public  Map deleteSms(String smsRef)
    {

        Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;

        con = DbConnection.connectDb(schoolDb);

        try {

if(smsRef.equalsIgnoreCase("all"))
{
    String querry1 = "DELETE from  smsrecord ";
    ps = con.prepareStatement(querry1);
    ps.execute();
}
else{
    String querry1 = "DELETE from  smsrecord where smsid='" + smsRef + "' ";
    ps = con.prepareStatement(querry1);
    ps.execute();
}



            map.put("responseCode","200");
            map.put("responseDescription","SMS Delete Success");
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
