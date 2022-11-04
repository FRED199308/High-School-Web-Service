package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Students {
    Globals globals;
    ConfigurationIntialiser configurationIntialiser;
    public Students(String schoolDb) {

        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        configurationIntialiser= new ConfigurationIntialiser(schoolDb);
    }

    String schoolDb;

public   ArrayList getClassSStudents(String className)
{
    ArrayList<Map> list=new ArrayList<Map>();
    Connection con;
    PreparedStatement ps = null;
    ResultSet rs;
    con = DbConnection.connectDb(schoolDb);
    try {

        String sql;
        if(className.equalsIgnoreCase("All"))
        {
            sql = "Select * from admission  where currentform like '"+"FM"+"' order by admissionNumber ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map map=new HashMap();

                map.put("studentName",rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName"));
                map.put("admissionNumber",rs.getString("AdmissionNumber"));
                map.put("upi", globals.streamName(rs.getString("upi")));
                map.put("className", globals.className(rs.getString("CurrentForm")));
                map.put("stream", globals.streamName(rs.getString("CurrentStream")));
                list.add(map);


            }
        }
        else{


            sql = "Select * from admission where currentform ='" + globals.classCode(className) + "' order by admissionNumber";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Map map=new HashMap();

            map.put("studentName",rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName"));
            map.put("admissionNumber",rs.getString("AdmissionNumber"));
            map.put("upi", globals.streamName(rs.getString("upi")));
            map.put("className", globals.className(rs.getString("CurrentForm")));
            map.put("stream", globals.streamName(rs.getString("CurrentStream")));
            list.add(map);


        }
        }
        return list;
    }
    catch ( SQLException sq) {

        sq.printStackTrace();
        list.add((Map) new HashMap().put("error",sq.getMessage()));
        return list;

    }
    finally {
        try {
            con.close();

        } catch (SQLException sq) {

            sq.printStackTrace();
        }

    }


}

    public   ArrayList searchStudent(String search)
    {
        ArrayList<Map> list=new ArrayList<Map>();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try {

            String sql;
      
                sql = "Select * from admission where admissionnumber like '" + search + "%' or firstname like '" + search + "%' or middlename like '" + search + "%' or lastname like '" + search + "%' and currentform like '" + "FM" + "%' order by admissionnumber";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Map map=new HashMap();

                    map.put("studentName",rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName"));
                    map.put("admissionNumber",rs.getString("AdmissionNumber"));
                    map.put("upi", rs.getString("upi"));
                    map.put("className", globals.className(rs.getString("CurrentForm")));
                    map.put("stream", globals.streamName(rs.getString("CurrentStream")));
                    list.add(map);


                }
           
            return list;
        }
        catch ( SQLException sq) {

            sq.printStackTrace();
            list.add((Map) new HashMap().put("error",sq.getMessage()));
            return list;

        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }

    public   Map getStudentDetails(String adm)
    {
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();


        response.put("responseCode","200");
        response.put("responseDescription","Student Found");
        try {

            String sql = "Select * from admission Where admissionNumber ='" + adm + "' || upi='"+adm+"' order by admissionNumber";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next())
            {
                //im.setIcon(resizeImage(configurationIntialiser.imageFolder()+"/"+jadm.getText()+".jpg"));
                response.put("firstName",rs.getString("FirstName"));
                response.put("middleName",rs.getString("middleName"));
                response.put("lastName",rs.getString("lastName"));
                response.put("admissionNumber",rs.getString("admissionNumber"));
                response.put("gender",rs.getString("Gender"));
                response.put("doa",rs.getDate("DateofAdmission"));
                response.put("dob",rs.getDate("DateOfBirth"));
                response.put("upi",rs.getString("upi"));
                response.put("currentClass",globals.className(rs.getString("CurrentForm")));

                response.put("classAdmitted",globals.className(rs.getString("classcode")));
                response.put("streamAdmitted",globals.streamName(rs.getString("streamAdmitted")));

                response.put("currentTerm",globals.termname(rs.getString("currentterm")));
                response.put("termAdmitted",globals.termname(rs.getString("termadmitted")));
                response.put("streamAdmitted",globals.streamName(rs.getString("StreamAdmitted")));
                response.put("currentStream",globals.streamName(rs.getString("currentstream")));
                response.put("parentNames",rs.getString("parentfullNames"));
                response.put("tel1",rs.getString("telephone1"));
                response.put("tel2",rs.getString("telephone2"));
                response.put("kcpeMarks",rs.getString("kcpemarks"));
                String countryid = rs.getString("Country");
                String countyid = rs.getString("County");
                String constituencyid = rs.getString("constituency");
                String provinceid = rs.getString("province");
                String wardid = rs.getString("ward");

                String sql1=" Select countryName from countries where countrycode='"+countryid+"'";;
                ps=con.prepareStatement(sql1);
                rs=ps.executeQuery();
                if(rs.next())
                {

                    response.put("country",rs.getString("CountryName"));

                }
                else {
                    response.put("country","");
                }
                String sql2="Select Provincename from provinces where provincecode='"+provinceid+"'";
                ps=con.prepareStatement(sql2);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    response.put("province",rs.getString("ProvinceName"));

                }
                else {
                    response.put("province","");
                }
                String sql7="Select Countyname from counties where countycode='"+countyid+"'";
                ps=con.prepareStatement(sql7);
                rs=ps.executeQuery();
                if(rs.next())
                {

                    response.put("county",rs.getString("Countyname"));

                }
                else {
                    response.put("county","");
                }
                String sql4="Select ConstituencyName from constituencies where constituencycode='"+constituencyid+"'";
                ps=con.prepareStatement(sql4);
                rs=ps.executeQuery();
                if(rs.next())
                {

                    response.put("constituency",rs.getString("Constituencyname"));

                }
                else{
                    response.put("constituency","");
                }
                String sql5="Select WardName from ward where wardcode='"+wardid+"'";
                ps=con.prepareStatement(sql5);
                rs=ps.executeQuery();
                if(rs.next())
                {

                    response.put("ward",rs.getString("WardName"));

                }
                else{
                    response.put("ward","");
                }

                response.put("responseCode","200");
                response.put("responseDescription","success");

                return response;

            }
            else {
                response.put("responseCode","404");
                response.put("responseDescription","Student Not Found,Invalid admissionNumber Or UPI");
                return response;

            }

        }
        catch (Exception sq)
        {
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sq.getLocalizedMessage());
            return response;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }
//

    public   Map deleteStudent(String adm)
    {
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);



        response.put("responseCode","200");
        response.put("responseDescription","Student Found");
        try {

            String sql = "Delete from admission Where admissionNumber ='" + adm + "' || upi='"+adm+"' order by admissionNumber";
            ps = con.prepareStatement(sql);
             ps.execute();

                response.put("responseCode","200");
                response.put("responseDescription","Student Deleted");
                return response;



        }
        catch (Exception sq)
        {
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sq.getLocalizedMessage());
            return response;
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }

    public  Map  editStudentDetails(String ADM,String adm,String upi,String firstName,String middleName,String lastName, String gender,String dob,String doa,String classAdmited,String streamadmitted,String termadmitted, String currentTerm,String currentClass,String currentStream,String kcpe,String country,String county,String province,String constituency,String ward,String parent,String tell1 ,String tell2, String imagefolder,String program)

    {
Map map=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        String DOB=dob;
        String DOA=doa;
        String constituencycode="",wardcode="",provincecode="",countycode="",countrycode="",formcodea="",formcodec="",termcodea="",termcodec="",streamcodea="",streamcodec="";
        boolean comply=true;



        try {
            String sql="Select countrycode from countries where countryname='"+country+"'";
            ps=con.prepareStatement(sql);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                countrycode=rs.getString("Countrycode");

            }
            String sql2="Select provincecode from provinces where provincename='"+province+"'";
            ps=con.prepareStatement(sql2);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                provincecode=rs.getString("provincecode");
            }

            String sql3="Select countycode from counties where countyname='"+county+"'";
            ps=con.prepareStatement(sql3);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                countycode=rs.getString("Countycode");
            }
            String sql4="Select constituencycode from constituencies where constituencyname='"+constituency+"'";
            ps=con.prepareStatement(sql4);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                constituencycode=rs.getString("constituencycode");
            }
            String sql5="Select wardcode from ward where wardname='"+ward+"'";
            ps=con.prepareStatement(sql5);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                wardcode=rs.getString("wardcode");
            }
            String sql6="Select  classcode from classes where classname='"+classAdmited+"'";
            ps=con.prepareStatement(sql6);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                formcodea=rs.getString("classcode");
            }
            String sql7="Select  classcode from classes where classname='"+currentClass+"'";
            ps=con.prepareStatement(sql7);
            rs=  ps.executeQuery();
            if(rs.next())
            {
                formcodec=rs.getString("classcode");
            }
            String sql8="Select* from streams where streamname='"+streamadmitted+"'";
            ps=con.prepareStatement(sql8);
            rs=ps.executeQuery();
            while(rs.next())
            {
                streamcodea=rs.getString("Streamcode");

            }
            String sql9="Select* from streams where streamname='"+currentStream+"'";
            ps=con.prepareStatement(sql9);
            rs=ps.executeQuery();
            while(rs.next())
            {
                streamcodec=rs.getString("Streamcode");
            }
            String querry="Select termcode from terms where termname='"+termadmitted+"'";
            ps=con.prepareStatement(querry);
            rs=ps.executeQuery();
            while(rs.next())
            {
                termcodea=rs.getString("Termcode");

            }

            if(ADM.equalsIgnoreCase(adm))
            {
                if(imagefolder==null||imagefolder.isEmpty())
                {

                }
                else{


                    if(DOB.isEmpty())
                    {
                        DOB="2000:01:01";
                    }
                    if(DOA.isEmpty())
                    {
                        DOA="2015:01:01";
                    }
                    String SQL="update admission set firstName='"+firstName.toUpperCase()+"',middleName='"+middleName.toUpperCase()+"',lastName='"+lastName.toUpperCase()+"',gender='"+gender+"'"
                            + ",DateOfBirth='"+DOB+"',DateofAdmission='"+DOA+"',classcode='"+formcodea+"',TermAdmitted='"+termcodea+"',StreamAdmitted='"+streamcodea+"',CurrentForm='"+formcodec+"',CurrentTerm='"+globals.currentTerm()+"',CurrentStream='"+streamcodec+"',KcpeMarks='"+kcpe+"',Country='"+countrycode+"',province='"+provincecode+"',"
                            + "county='"+countycode+"',Constituency='"+constituencycode+"',ward='"+wardcode+"',parentfullNames='"+parent+"',Telephone1='"+tell1+"',telephone2='"+tell2+"',AdmissionNumber='"+adm+"',ImageLocation=?,UPI='"+upi+"',program='"+program+"' where admissionNumber='"+ADM+"'";

                    ps=con.prepareStatement(SQL);
                    ps.setString(1, configurationIntialiser.imageFolder()+"/"+imagefolder+".jpg");
                    ps.execute();


                }
                map.put("responseCode","200");
                map.put("responseDescription","Student Details Updated Successfully");

                return  map;
            }
            else{
                String querry3="Select firstname,lastname,middlename from admission where admissionnumber='"+adm+"'";
                ps=con.prepareStatement(querry3);
                ResultSet Rs=ps.executeQuery();
                if(Rs.next())
                {
                    map.put("responseCode","301");
                    map.put("responseDescription", "Admission Number "+adm+" Is Bieng Used By "+Rs.getString("firstName")+"  "+Rs.getString("MiddleName")+" "+Rs.getString("LastName")+" \n Admission Numbers Can Never Be Shared");
                    return map;
                }
                else{

                    if(imagefolder==null||imagefolder.isEmpty())
                    {

                    }
                    else{


                        if(DOB.isEmpty())
                        {
                            DOB="2000:01:01";
                        }
                        if(DOA.isEmpty())
                        {
                            DOA="2015:01:01";
                        }
                        String SQL="update admission set firstName='"+firstName.toUpperCase()+"',middleName='"+middleName.toUpperCase()+"',lastName='"+lastName.toUpperCase()+"',gender='"+gender+"'"
                                + ",DateOfBirth='"+DOB+"',DateofAdmission='"+DOA+"',classcode='"+formcodea+"',TermAdmitted='"+termcodea+"',StreamAdmitted='"+streamcodea+"',CurrentForm='"+formcodec+"',CurrentTerm='"+globals.currentTerm()+"',CurrentStream='"+streamcodec+"',KcpeMarks='"+kcpe+"',Country='"+countrycode+"',province='"+provincecode+"',"
                                + "county='"+countycode+"',Constituency='"+constituencycode+"',ward='"+wardcode+"',parentfullNames='"+parent+"',Telephone1='"+tell1+"',telephone2='"+tell2+"',AdmissionNumber='"+adm+"',ImageLocation=?,UPI='"+upi+"',program='"+program+"' where admissionNumber='"+ADM+"'";

                        ps=con.prepareStatement(SQL);
                        ps.setString(1, configurationIntialiser.imageFolder()+"/"+imagefolder+".jpg");
                        ps.execute();


                    }
                    map.put("responseCode","200");
                    map.put("responseDescription","Student Details Updated Successfully");

                    return  map;
                }

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
}
