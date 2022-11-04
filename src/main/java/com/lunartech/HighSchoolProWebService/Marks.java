package com.lunartech.HighSchoolProWebService;

import jdk.nashorn.internal.objects.Global;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Marks {
    String schoolDb;
Globals globals;
  ClassProgressTracker  classProgressTracker;
    ReportGenerator reportGenerator;
    Subject subject;
    public Marks(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        reportGenerator=new ReportGenerator(schoolDb);
        classProgressTracker=new ClassProgressTracker(schoolDb);
        subject=new Subject(schoolDb);
    }

    public  Map deleteInBatch(String adm, String className, String streamName, String subjectName, String termName, String year , String examname, String examcode)
{
    Connection con;
    PreparedStatement ps = null;
    ResultSet rs;
    con = DbConnection.connectDb(schoolDb);
    Map response=new HashMap();


    String sub;
    String st="";
    String subjectname="";
    if(streamName.equalsIgnoreCase("select Stream"))
    {
        st="" ;
    }
    else{
        st=st;
    }

    if(subjectName.equalsIgnoreCase("select Subject"))
    {
        sub="";
    }
    else{
        sub=globals.subjectCode(subjectName);
    }

    try {

        if(adm.isEmpty())
        {

            if(st.isEmpty())
            {
                if(sub.isEmpty())
                {

                        String sql="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"' and classname='"+className+"' and term='"+termName+"'";
                        ps=con.prepareStatement(sql);
                        ps.execute();
                        String sql2="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and termcode='"+globals.termcode(termName)+"'";
                        ps=con.prepareStatement(sql2);
                        ps.execute();
                        response.put("responseCode","200");
                    response.put("responseDescription","success");
                    return response;


                }
                else{

                        String sql="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"' and classname='"+className+"' and term='"+termName+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sql);
                        ps.execute();
                        String sql2="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and termcode='"+globals.termcode(termName)+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sql2);
                        ps.execute();
                    response.put("responseCode","200");
                    response.put("responseDescription","success");
                    return response;



                }




            }
            else{
                if(sub.isEmpty())
                {


                        String sql="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"' and classname='"+className+"' and term='"+termName+"' and stream='"+streamName+"'";
                        ps=con.prepareStatement(sql);
                        ps.execute();
                        String sql2="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and streamcode='"+globals.streamcode(streamName)+"' and termcode='"+globals.termcode(termName)+"'";
                        ps=con.prepareStatement(sql2);
                        ps.execute();

                    response.put("responseCode","200");
                    response.put("responseDescription","success");
                    return response;

                }
                else{



                        String sql="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"' and classname='"+className+"' and term='"+termName+"' and stream='"+streamName+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sql);
                        ps.execute();
                        String sql2="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and streamcode='"+globals.streamcode(streamName)+"' and termcode='"+globals.termcode(termName)+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sql2);
                        ps.execute();


                    response.put("responseCode","200");
                    response.put("responseDescription","success");
                    return response;

                }

            }


        }
        else{

            String sql="Select admissionNumber from admission where admissionnumber='"+adm+"'";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next())
            {
                String sql2="select admnumber from markstable where examcode='"+examcode+"' and academicyear='"+className+"' and termcode='"+globals.termcode(termName)+"'";
                ps=con.prepareStatement(sql2);
                rs=ps.executeQuery();
                if(rs.next())
                {
                    if(sub.isEmpty())
                    {
                        String sqll="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"'  and term='"+termName+"'  and admnumber='"+adm+"'";
                        ps=con.prepareStatement(sqll);
                        ps.execute();
                        String sql22="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and termcode='"+globals.termcode(termName)+"' and admnumber='"+adm+"'";
                        ps=con.prepareStatement(sql22);
                        ps.execute();
                        response.put("responseCode","200");
                        response.put("responseDescription","success");
                        return response;
                    }
                    else{
                        String sqll="Delete from examanalysistable where examcode='"+examcode+"' and academicyear='"+year+"'  and term='"+termName+"'  and admnumber='"+adm+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sqll);
                        ps.execute();
                        String sql22="Delete from markstable where examcode='"+examcode+"' and academicyear='"+year+"' and classcode='"+globals.classCode(className)+"' and termcode='"+globals.termcode(termName)+"' and admnumber='"+adm+"' and subjectcode='"+sub+"'";
                        ps=con.prepareStatement(sql22);
                        ps.execute();
                        response.put("responseCode","200");
                        response.put("responseDescription","success");
                        return response;
                    }

                }
                else{
                    response.put("responseCode","201");
                    response.put("responseDescription","No Marks Found");
                    return response;
                }

            }
            else{
                response.put("responseCode","404");
                response.put("responseDescription","Invalid Admission Number");
                return response;
            }
        }





    } catch (Exception sq) {
        sq.printStackTrace();
        response.put("responseCode","501");
        response.put("responseDescription",sq.getMessage());
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

    public  Map marksDelete(String adm, String subjectName, String termName, String year, String className, String streamName, String examname, String examcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map response=new HashMap();
        try {



            String sql2 = "Delete from markstable  where academicyear='" + year + "' and subjectcode='" + globals.subjectCode(subjectName) + "'  and admnumber='" + adm + "' and examcode='" + examcode + "'";
            ps = con.prepareStatement(sql2);
            ps.execute();

            response.put("resposeCode","200");
            response.put("responseDescription","success");
return  response;

        } catch (Exception sq) {
            sq.printStackTrace();
            response.put("resposeCode","501");

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

    public  Map  checkMarks(String examcode,String subjectName,String admissionNumber)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

      Map mp=new HashMap();

            try {

                String sq = "Select * from markstable where subjectcode='" +globals.subjectCode(subjectName)  + "' and examcode='" + examcode + "' and admnumber='" + admissionNumber + "'";
                ps = con.prepareStatement(sq);
                rs = ps.executeQuery();
                if (rs.next()) {
                   mp.put("examPoints",rs.getString("examPoints"));
                    mp.put("weightedScore",rs.getInt("convertedscore"));
                    mp.put("score",rs.getString("examscore"));
                    mp.put("outOf",rs.getString("Examoutof"));
                    mp.put("percent",rs.getInt("Exampercentage"));
                    mp.put("grade",rs.getString("examgrade"));
                }


            }
            catch (Exception sq)
            {
                sq.printStackTrace();
                mp.put("error: ",sq.getMessage());
                return mp;
            }

            finally {
                try {
                    con.close();

                } catch (SQLException sq) {

                    sq.printStackTrace();
                }

            }


        return mp;


    }





    public  ArrayList studentTakingASubject(String subjectName, String academicYear,  String className, String streamName)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet result;
        con = DbConnection.connectDb(schoolDb);
ArrayList<Map> data=new ArrayList();
        Map map=new HashMap();

        try {
            String subjectcode=globals.subjectCode(subjectName);
            String streamCode=globals.streamcode(streamName);
            String classcode=globals.classCode(className);
            String tname="",tinitials="";
            ResultSet rs;
            String sqla = "Select firstname,initials,teachercode from subjectrights,staffs where classcode='" + classcode + "' and subjectcode='" + subjectcode + "'  and  streamcode='" + streamCode + "' and teachercode=staffs.employeecode";
            ps = con.prepareStatement(sqla);
            rs = ps.executeQuery();
            if (rs.next()) {
tinitials=rs.getString("Initials");
tname=rs.getString("firstname");


            }





            String sql2 = "Select admnumber,firstname,middlename,ImageLocation,lastname from studentsubjectallocation,admission where admnumber=admissionNumber and currentform='" + globals.classCode(classProgressTracker.currentClass(Integer.parseInt(academicYear), className)) + "' and currentstream='" + globals.streamcode(streamName) + "' and subjectcode='" + subjectcode+ "' and academicyear='" + academicYear + "' order by admnumber";
            ps = con.prepareStatement(sql2);
            result = ps.executeQuery();

            while (result.next()) {
                map=new HashMap();
                map.put("admissionNumber",result.getString("admNumber"));
                map.put("name",result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                map.put("teacherInitials",tinitials);
                map.put("teacherName",tname);
                data.add(map);
            }




        }
        catch (Exception sq)
        {
            sq.printStackTrace();
            map.put("Error",sq.getMessage());
            data.add(map);
            return data;
        }

        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


return data;


    }

    public  void sujectMarks(String examcode, String examname, String stream, String term, String academicyear, String classname, String subjectname,String sort)
    {

        reportGenerator.subjectResults(examcode, examname, stream, term, academicyear,globals.subjectCode(subjectname), classname, subjectname,sort);

    }
public  void missingSubjectMark(String examcode, String examname, String stream, String term, String academicyear, String classname, String subjectname)
{

    reportGenerator.missingMarks(examcode, examname, stream, term, academicyear,globals.subjectCode(subjectname), classname, subjectname);

}
public  void overallMarks(String className,String academicYear,String streamName,String examCode,String examName,String termName)
{
    reportGenerator.missingResultsOverall(className, academicYear, streamName, examCode,examName,termName);
}
    public  void emptyScoreSheet(String className,String academicYear,String streamName,String examCode,String examName,String termName)
    {
        reportGenerator.emptyScoreSheet(className, academicYear, streamName, examCode,examName,termName);
    }

    public  Map marksSaver(String adm, String className, String streamName,String subjectName, String termName, String year, double score, double outof , String teachername, String examname, String examcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        Map map=new HashMap();
        Map performanceMap;

        try {
            String classCode=globals.classCode(className);
            String subjectCode=globals.subjectCode(subjectName);
            performanceMap=subject.performanceIndicator(className,subjectName,examcode,score,outof);
            System.out.println("Grade"+performanceMap);
            String sq = "Select * from markstable where subjectcode='" + subjectCode + "' and academicyear='" + year + "' and examcode='" + examcode + "' and admnumber='" + adm + "'";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            if (rs.next()) {
                String sql2 = "update markstable set examscore='" + score + "',examoutof='" + outof + "',convertedscore='" + performanceMap.get("convertedscore") + "',exampercentage='" + performanceMap.get("percent") + "',examgrade='" + performanceMap.get("grade") + "',exampoints='" + performanceMap.get("points")+ "' where academicyear='" + year + "' and subjectcode='" + subjectCode + "'  and admnumber='" + adm + "' and examcode='" + examcode + "'";
                ps = con.prepareStatement(sql2);
                ps.execute();
                map.put("responseCode","200");
                map.put("responseDescription","success");
                return map;

            } else {
System.out.println("Grades"+performanceMap);
                String sql = "Insert into markstable values('" + adm + "','" + subjectCode + "','" + classCode + "','" + globals.streamcode(streamName) + "','" + globals.termcode(termName)+ "','" + year + "','" + examname + "','" + examcode + "','" + score + "','" + outof + "','" + performanceMap.get("convertedscore") + "','" + performanceMap.get("convertedscoreoutof") + "','" + performanceMap.get("percent") + "','" + performanceMap.get("grade") + "','" + performanceMap.get("points")+ "','" + "1" + "','" + "1" + "','" + teachername + "')";
                ps = con.prepareStatement(sql);
                ps.execute();
                map.put("responseCode","200");
                map.put("responseDescription","success");
                return map;

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            map.put("responseCode","501");
            map.put("responseDescription","Failed "+sq.getMessage());
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
