package com.lunartech.HighSchoolProWebService;

import org.springframework.stereotype.Service;

import javax.swing.*;

import java.sql.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


public class ExamAnalysis {
    String schoolDb;
    Globals globals;
    int rows = 0;
    AnalysisTasks tasks;
    public ExamAnalysis(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
        tasks=new AnalysisTasks(schoolDb);
    }

    double value = 0.0000;
    int counter = 0;
    String analysisMode="";


    NumberFormat nf = NumberFormat.getNumberInstance();
    NumberFormat nf2 = NumberFormat.getNumberInstance();
    public   void analyseExam(String subjectAnalysisMode,String positionAllocationMode,String meanGradeAllocationMode,String examCode,String academicYear,String termName,String className,String examName,String analysisId)
    {

      





                Connection con;
                PreparedStatement ps;
                ResultSet rs;
                con = DbConnection.connectDb(schoolDb);
             
                try {

                    Map map=isAnalysable(examCode);



                    String sql22 = "Select count(*)  from markstable where examcode='" + examCode + "' and termcode='" + globals.termcode(termName) + "' and academicyear='" + academicYear+ "' and classcode='" + globals.classCode(className.toString()) + "'";
                    ps = con.prepareStatement(sql22);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        rows = rs.getInt("count(*)");
                    }



                    tasks.startAnalysis(examCode,analysisId,"examanalysisprogress");

                    String sqlstart = "select Admnumber from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear+ "'";
                    ps = con.prepareStatement(sqlstart);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        
                            String state = "";
                            String sq = "Select * from examanalysislock where examcode='" + examCode + "'";
                            ps = con.prepareStatement(sq);
                            ResultSet result = ps.executeQuery();
                            if (result.next()) {
                                state = result.getString("Lock");

                            } else {
                                String SQ = "insert into examanalysislock values('" + examCode + "','" + "ON" + "')";
                                ps = con.prepareStatement(SQ);
                                ps.execute();
                            }
String analysisObject;
                            if(meanGradeAllocationMode.equalsIgnoreCase("avg Marks"))
                            {
                                analysisObject="Average Marks";
                            }
                            else{
                                analysisObject="Average Points";
                            }

                            String sq1 = "Select * from examanalysismodes where examcode='" + examCode + "'";
                            ps = con.prepareStatement(sq1);
                            result = ps.executeQuery();
                            if (result.next()) {
                                ps = con.prepareStatement("Update examanalysismodes set AnalysisMode='" + analysisMode + "',analysisObject='"+analysisObject+"' where examcode='" + examCode + "'");
                                ps.execute();

                            } else {
                                String SQ = "insert into examanalysismodes values('" + examCode + "','" + analysisMode + "','"+analysisObject+"')";
                                ps = con.prepareStatement(SQ);
                                ps.execute();
                            }



                            String sql = "Delete from examanalysistable where examcode='" + examCode + "'";
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            int i = 0;

                            String sql2 = "Select * from  markstable where examcode='" + examCode + "' ";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                value += (40.0000) / rows;

                                tasks.analysisUpdater(examCode,(int) value,analysisId);

                                String sql3 = "Insert into examanalysistable (admnumber,fullname,classname,stream,academicyear,examcode,examname,term,subjectcode,subjectname,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,subjectexamgrade,Sujectexampoints,teacherinitials) values("
                                        + "'" + rs.getString("Admnumber") + "','" + globals.fullName(rs.getString("AdmNumber")) + "','" + globals.className(rs.getString("Classcode")) + "','" + globals.streamName(rs.getString("streamcode")) + "','" + rs.getString("AcademicYear") + "'"
                                        + ",'" + rs.getString("Examcode") + "','" + rs.getString("Examname") + "','" + globals.termname(rs.getString("Termcode")) + "','" + rs.getString("Subjectcode") + "','" + globals.subjectName(rs.getString("subjectcode")) + "','" + rs.getString("Examscore") + "','" + rs.getString("Examoutof") + "','" + rs.getString("Convertedscore") + "','" + rs.getString("convertedscoreoutof") + "'"
                                        + ",'" + rs.getString("Exampercentage") + "','" + rs.getString("Examgrade") + "','" + rs.getString("Exampoints") + "','" + rs.getString("class_teacher_initials") + "')";
                                ps = con.prepareStatement(sql3);
                                ps.execute();

                            }
                            subjectpositionAssigner(academicYear,className,examCode,examName,termName,analysisId);
                            meanGradeAssigner(academicYear,className,examCode,examName,termName,meanGradeAllocationMode,subjectAnalysisMode,analysisId);
                            positionAssigner(academicYear,className,examCode,examName,termName,positionAllocationMode,analysisId);

//                            String sqq = "Delete from examanalysislock where examcode='" + examCode + "'";
//                            ps = con.prepareStatement(sqq);
//                            ps.execute();
                        tasks.endAnalysis(examCode,"Completed","Safe",analysisId);



                    }
                    else {


                        String state = "";
//                        String sq = "Select * from examanalysislock where examcode='" + examCode + "'";
//                        ps = con.prepareStatement(sq);
                        ResultSet result ;
//                        if (result.next()) {
//                            state = result.getString("Lock");
//
//                        } else {
//                            String SQ = "insert into examanalysislock values('" + examCode + "','" + "ON" + "')";
//                            ps = con.prepareStatement(SQ);
//                            ps.execute();
//                        }
//                                                if (state.equalsIgnoreCase("ON")) {
//                                                    dia.dispose();
//                                                    JOptionPane.showMessageDialog(null, "Another Computer On The Network Is Currently Analysing The Exam");
//                                                } else {...


                        String analysisObject;
                        if(meanGradeAllocationMode.equalsIgnoreCase("avg Marks"))
                        {
                            analysisObject="Average Marks";
                        }
                        else{
                            analysisObject="Average Points";
                        }


                        String sq1 = "Select * from examanalysismodes where examcode='" + examCode + "'";
                        ps = con.prepareStatement(sq1);
                        result = ps.executeQuery();
                        if (result.next()) {
                            ps = con.prepareStatement("Update examanalysismodes set AnalysisMode='" + analysisMode + "',analysisObject='"+analysisObject+"' where examcode='" + examCode + "'");
                            ps.execute();

                        } else {
                            String SQ = "insert into examanalysismodes values('" + examCode + "','" + analysisMode + "','"+analysisObject+"')";
                            ps = con.prepareStatement(SQ);
                            ps.execute();
                        }

                        String sql = "Delete from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear+ "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        int i = 0;

                        String sql2 = "Select * from  markstable where examcode='" + examCode + "' and termcode='" + globals.termcode(termName) + "' and academicyear='" + academicYear+ "' and classcode='" + globals.classCode(className.toString()) + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            value += (40.0000) / rows;
                            tasks.analysisUpdater(examCode,(int) value,analysisId);

                            String sql3 = "Insert into examanalysistable (admnumber,fullname,classname,stream,academicyear,examcode,examname,term,subjectcode,subjectname,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,subjectexamgrade,Sujectexampoints,teacherinitials) values("
                                    + "'" + rs.getString("Admnumber") + "','" + globals.fullName(rs.getString("AdmNumber")) + "','" + globals.className(rs.getString("Classcode")) + "','" + globals.streamName(rs.getString("streamcode")) + "','" + rs.getString("AcademicYear") + "'"
                                    + ",'" + rs.getString("Examcode") + "','" + rs.getString("Examname") + "','" + globals.termname(rs.getString("Termcode")) + "','" + rs.getString("Subjectcode") + "','" + globals.subjectName(rs.getString("subjectcode")) + "','" + rs.getString("Examscore") + "','" + rs.getString("Examoutof") + "','" + rs.getString("Convertedscore") + "','" + rs.getString("convertedscoreoutof") + "'"
                                    + ",'" + rs.getString("Exampercentage") + "','" + rs.getString("Examgrade") + "','" + rs.getString("Exampoints") + "','" + rs.getString("class_teacher_initials") + "')";
                            ps = con.prepareStatement(sql3);
                            ps.execute();

                        }
                        subjectpositionAssigner(academicYear,className,examCode,examName,termName,analysisId);
                        meanGradeAssigner(academicYear,className,examCode,examName,termName,meanGradeAllocationMode,subjectAnalysisMode,analysisId);
                        positionAssigner(academicYear,className,examCode,examName,termName,positionAllocationMode,analysisId);

                        String sqq = "Delete from examanalysislock where examcode='" + examCode + "'";
                        ps = con.prepareStatement(sqq);
                        ps.execute();
                        tasks.endAnalysis(examCode,"Completed","Safe",analysisId);

                    }

                } catch (Exception sq) {

                    sq.printStackTrace();
                    tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);

                }




    }



    public void subjectpositionAssigner(String academicYear,String className,String examCode,String examName,String termName,String analysisId) {
        int rows;
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {
            rows = 0;

            String sql1 = "Select count(distinct subjectcode) as total from examanalysistable where classname='" + className + "'  and examcode='" + examCode + "' and academicyear='" + academicYear + "' ";
            ps = con.prepareStatement(sql1);
            ResultSet RS = ps.executeQuery();
       if (RS.next()) {
                rows=RS.getInt("total");
            }
            counter = 1;

            String sql = "Select subjectcode  from examanalysistable where classname='" + className + "'  and examcode='" + examCode + "' and academicyear='" + academicYear + "' group by subjectcode order by subjectcode";
            ps = con.prepareStatement(sql);
            RS = ps.executeQuery();
            while (RS.next()) {
                value += (20.0000 / rows);
                tasks.analysisUpdater(examCode,(int) value,analysisId);
                counter++;

                String subcode = RS.getString("subjectcode");

                int headcount = 0;
                int previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select count(*) from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' and subjectcode='" + subcode + "' order by examscore";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalentries = rs.getInt("count(*)");
                }
                int tiechck = 0;

                String sql3 = "Select examscore,admnumber,subjectexamgrade from examanalysistable where examcode='" + examCode + "' and academicyear='" + academicYear + "' and subjectcode='" + subcode + "' order by examscore desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();


                while (rs.next()) {
                    String adm = rs.getString("admNumber");
                    String grade = rs.getString("subjectexamgrade");

                    int examresult = rs.getInt("Examscore");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }
                    String comment = "";
                    String querry = "Select comment from subjectcomments where subjectcode='" + subcode + "' and grade='" + grade + "'";
                    ps = con.prepareStatement(querry);
                    ResultSet rx = ps.executeQuery();
                    if (rx.next()) {
                        comment = rx.getString("comment");
                    }
                    String sql4 = "Update examanalysistable  set subjectposition='" + headcount + "',subjectpositionoutof='" + totalentries + "',teacherscomment='" + comment + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "' and subjectcode='" + subcode + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getInt("Examscore");
                }
            }
        } catch (Exception sq) {

            sq.printStackTrace();
            tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);

        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }

    }

    public void meanGradeAssigner(String academicYear,String className,String examCode,String examName,String termName,String meanGradeAllocationMode,String analysiSubjectMode,String analysisId) {
        int rows=0;
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
       nf2.setMaximumFractionDigits(0);
        try {
            rows=0;

            String sqlstart1 = "select count(distinct admnumber) as total from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' ";
            ps = con.prepareStatement(sqlstart1);
            rs = ps.executeQuery();
            if (rs.next()) {
                rows =rs.getInt("total");
            }

            counter = 1;
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


        if (analysiSubjectMode.equalsIgnoreCase("By Seven")) {

            try {

                String sqlstart = "select admnumber from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' group by admNumber";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int codepointer=0;
                    String codes[] = new String[5];
                    value += 20.0000 / rows;
                    tasks.analysisUpdater(examCode,(int) value,analysisId);
                    counter++;
                    String adm = rs.getString("admnumber");
                    int subtp1 = 0, subtp2 = 0, subtp3 = 0, subtp4 = 0;
                    int subtm1 = 0, subtm2 = 0, subtm3 = 0, subtm4 = 0;
                    double totalpoint = 0;
                    double totalmarks = 0;
                    int totalsubject;
                    if(globals.gradableeditted(adm, examCode.toString(), academicYear.toString()))
                    {


                        String sql = "select sum(exampercentage),sum(Sujectexampoints) from examanalysistable,subjects where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "languages" + "' ";
                        ps = con.prepareStatement(sql);
                        ResultSet rx = ps.executeQuery();
                        if (rx.next()) {
                            subtp1 = rx.getInt("sum(Sujectexampoints)");
                            subtm1 = rx.getInt("sum(exampercentage)");

                        }
                        codes[codepointer]="101";
                        codepointer++;
                        codes[codepointer]="102";
                        int subcounter = 1;
                        String sqly = "select exampercentage,Sujectexampoints,examanalysistable.subjectcode from examanalysistable,subjects where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "sciences" + "' order by exampercentage desc";
                        ps = con.prepareStatement(sqly);
                        rx = ps.executeQuery();
                        while (rx.next()) {
                            if (subcounter > 2) {
                                break;
                            }
                            codepointer++;
                            codes[codepointer]=rx.getString("SubjectCode");

                            subtp2 += rx.getInt("Sujectexampoints");
                            subtm2 += rx.getInt("exampercentage");

                            subcounter++;
                        }
                        String sqlxx = "select exampercentage,Sujectexampoints,examanalysistable.subjectcode from examanalysistable,subjects where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "mathematics" + "' order by exampercentage desc";
                        ps = con.prepareStatement(sqlxx);
                        rx = ps.executeQuery();
                        while (rx.next()) {
                            codepointer++;
                            codes[codepointer]=rx.getString("SubjectCode");
                            subtp4 += rx.getInt("Sujectexampoints");
                            subtm4 += rx.getInt("exampercentage");
                            break;
                        }
                        int sub=1;
                        String sqlz = "select exampercentage,Sujectexampoints from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and subjectcode!='" + codes[0] + "' and subjectcode!='" + codes[1] + "' and subjectcode!='" + codes[2] + "' and subjectcode!='" + codes[3] + "' and subjectcode!='" + codes[4] + "'  order by exampercentage desc";
                        ps = con.prepareStatement(sqlz);
                        rx = ps.executeQuery();
                        while (rx.next()) {

                            subtp3 += rx.getInt("Sujectexampoints");
                            subtm3 += rx.getInt("exampercentage");
                            sub++;
                            if (sub > 2) {
                                break;
                            }
                        }



                        totalpoint += subtp1 + subtp2 + subtp3 + subtp4;
                        totalmarks += subtm1 + subtm2 + subtm3 + subtm4;

                        totalsubject = 7;

                        String grade = "";
                        if (meanGradeAllocationMode.equalsIgnoreCase("avg Marks")) {

                            String sqla = "Select grade from meangrade_table where classcode='" + globals.classCode(className.toString()) + "' and '" + nf2.format((totalmarks / totalsubject)) + "'>=start_from and '" + nf2.format((totalmarks / totalsubject)) + "'<=end_at ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        } else {
                            double p=(totalpoint / totalsubject);
                            int point = (int)Math.round(p);

                            String sqla = "Select grade from points_for_each_grade where points='" + point + "' ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        }
                        String teachercomment = "";
                        String principalcomment = "";
                        String sqla = "Select comments from teachersgeneralcomments where classcode='" + globals.classCode(className.toString()) + "' and grade='" + grade + "' ";
                        ps = con.prepareStatement(sqla);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            teachercomment = rx.getString("comments");
                        }
                        String sqlb = "Select comments from principalcomments where grade='" + grade + "' ";
                        ps = con.prepareStatement(sqlb);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            principalcomment = rx.getString("comments");
                        }
                        double avgp = (totalpoint / totalsubject);
                        double avgm = (totalmarks / totalsubject);
                        String kcpe=globals.kcpeMarks(adm);
                        double kcpem;
                        double kcperatio,exammeanratio;
                        if(kcpe.isEmpty())
                        {
                            kcperatio=0;exammeanratio=0;
                        }
                        else{
                            kcpem=Double.parseDouble(kcpe);


                            kcperatio=kcpem/500.00;
                            exammeanratio=avgp/12.00;
                        }
                        double vap=(exammeanratio-kcperatio);



                        String sql3 = "Update examanalysistable set totalmarks='" + totalmarks + "',principalscomment='" + principalcomment + "',classteachergeneralcomment='" + teachercomment + "',totalpoints='" + totalpoint + "',meanpoints='" + nf.format(avgp) + "',meanmarks='" + nf.format(avgm) + "',meangrade='" + grade + "' ,vap='"+nf.format(vap)+"' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();
                    }else{
                        String sql3 = "Update examanalysistable set principalscomment='" + "You Should Not Miss Exams" + "',classteachergeneralcomment='" + "Missing Exams Negatively Affects Your Academic Potential" + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();

                    }
                }

            } catch (Exception sq) {
                sq.printStackTrace();
                tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);
            }
            finally {
                try {
                    con.close();

                } catch (SQLException sq) {

                    sq.printStackTrace();
                }

            }

        } else {
            try {
                String sqlstart = "select admnumber from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' group by admNumber";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {

                    value += 20.0000 / rows;
                    tasks.analysisUpdater(examCode,(int) value,analysisId);
                    counter++;
                    double avgp, avgm;
                    double totalpoint = 0;
                    double totalmarks = 0;

                    String adm = rs.getString("admnumber");

                    if(globals.gradableeditted(adm, examCode.toString(), academicYear.toString()))
                    {





                        int totalsubject = globals.subjectAllocationCounter(adm, academicYear.toString());
                        String sql = "select sum(exampercentage),sum(Sujectexampoints) from examanalysistable where classname='" + className + "' and examcode='" + examCode + "' and term='" + termName + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet rx = ps.executeQuery();
                        if (rx.next()) {
                            totalpoint = rx.getInt("sum(Sujectexampoints)");
                            totalmarks = rx.getInt("sum(exampercentage)");

                        }

                        String grade = "";
                        if (meanGradeAllocationMode.equalsIgnoreCase("avg Marks")) {
                            String sqla = "Select grade from meangrade_table where classcode='" + globals.classCode(className.toString()) + "' and '" + nf2.format((totalmarks / totalsubject)) + "'>=start_from and '" + nf2.format((totalmarks / totalsubject)) + "'<=end_at ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }

                        } else {

                            double p=(totalpoint / totalsubject);
                            int point = (int)Math.round(p);

                            String sqla = "Select grade from points_for_each_grade where points='" + point + "' ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        }
                        String teachercomment = "";
                        String principalcomment = "";
                        String sqla = "Select comments from teachersgeneralcomments where classcode='" + globals.classCode(className.toString()) + "' and grade='" + grade + "' ";
                        ps = con.prepareStatement(sqla);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            teachercomment = rx.getString("comments");
                        }
                        String sqlb = "Select comments from principalcomments where grade='" + grade + "' ";
                        ps = con.prepareStatement(sqlb);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            principalcomment = rx.getString("comments");
                        }
                        avgp = totalpoint / totalsubject;
                        avgm = (totalmarks / totalsubject);
                        String kcpe=globals.kcpeMarks(adm);
                        double kcpem;
                        double kcperatio,exammeanratio;
                        if(kcpe.isEmpty())
                        {
                            kcperatio=0;exammeanratio=0;
                        }
                        else{
                            kcpem=Double.parseDouble(kcpe);


                            kcperatio=kcpem/500.00;
                            exammeanratio=avgp/12.00;
                        }

                        double vap=(exammeanratio-kcperatio);

                        String sql3 = "Update examanalysistable set totalmarks='" + totalmarks + "',principalscomment='" + principalcomment + "',vap='"+nf.format(vap)+"',classteachergeneralcomment='" + teachercomment + "',totalpoints='" + totalpoint + "',meanpoints='" + nf.format(avgp) + "',meanmarks='" + nf.format(avgm) + "',meangrade='" + grade + "'  where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();
                    }else{

                        String sql3 = "Update examanalysistable set principalscomment='" + "You Should Not Miss Exams" + "',classteachergeneralcomment='" + "Missing Exams Negatively Affects Your Academic Potential" + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();

                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);

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

    public void positionAssigner(String academicYear,String className,String examCode,String examName,String termName,String positionAllocationMode,String analysisId) {

        int rows;
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        if (positionAllocationMode.equalsIgnoreCase("avg points")) {
            try {
                int press = 0;
                int year = Integer.parseInt( academicYear);
                ps = con.prepareStatement("Select precisions from terms where termname='" + termName + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    press = rs.getInt("precisions");
                }
                int lastpress = press - 1;
                int headcount = 0;
                double previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select count(distinct admnumber ) as total from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' ";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if(rs.next()) {
                    totalentries=rs.getInt("total");
                }
                int tiechck = 0;
                rows = totalentries;
                counter = 1;
                String sql3 = "Select meanpoints,admnumber from examanalysistable where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "'   group by admnumber order by meanpoints desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    value += 10.0000 / rows;
                    tasks.analysisUpdater(examCode,(int) value,analysisId);
                    counter++;
                    double examresult = rs.getDouble("meanpoints");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }

                    String lastexamcode;
                    String lasttermposition;
                    String lastposstreampos;
                    String lastermposoutof;
                    String lasttermstreamposoutof;
                    if (termName.toString().equalsIgnoreCase("Term 1")) {
                        lastexamcode = examCode.toString().replaceAll(String.valueOf(year), String.valueOf((year - 1)));
                        lastexamcode = lastexamcode.replaceAll(examName + "TM" + press, examName + "TM3");
                    } else {

                        lastexamcode = examCode.toString().replaceAll(examName + "TM" + press, examName + "TM" + lastpress);
                    }

                    String sqla = "Select classpositionthisterm,Streampositionthisterm,Streampositionthistermoutof,classpositionthistermoutof from examanalysistable where examcode='" + lastexamcode + "' and admnumber='" + adm + "' group by admnumber ";
                    ps = con.prepareStatement(sqla);
                    ResultSet last = ps.executeQuery();
                    if (last.next()) {
                        lasttermposition = last.getString("classpositionthisterm");
                        lastposstreampos = last.getString("Streampositionthisterm");
                        lastermposoutof = last.getString("classpositionthistermoutof");
                        lasttermstreamposoutof = last.getString("Streampositionthistermoutof");

                    } else {
                        lasttermposition = " ";
                        lastposstreampos = " ";
                        lastermposoutof = " ";
                        lasttermstreamposoutof = " ";
                    }

                    String sql4 = "Update examanalysistable  set classpositionlasttermoutof='" + lastermposoutof + "',Streampositionlasttermoutof='" + lasttermstreamposoutof + "',classpositionlastterm='" + lasttermposition + "', streampositionlastterm='" + lastposstreampos + "', classpositionthisterm='" + headcount + "',classpositionthistermoutof='" + totalentries + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "'  and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getDouble("meanpoints");
                }

                String sql = "Select streamname from streams";
                ps = con.prepareStatement(sql);
                ResultSet Rx = ps.executeQuery();
                while (Rx.next()) {
                    String name = Rx.getString("StreamName");
                    headcount = 0;
                    previousscore = 0;
                    totalentries = 0;

                    sql2 = "Select count(distinct admnumber ) as total  from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' and stream='" + name + "' ";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                   if(rs.next()) {
                        totalentries=rs.getInt("total");
                    }
                    tiechck = 0;
                    rows = totalentries;
                    counter = 1;
                    sql3 = "Select meanpoints,admnumber from examanalysistable where examcode='" + examCode + "' and academicyear='" + academicYear + "' and stream='" + name + "'  group by admnumber order by meanpoints desc";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        value += 2.5000 / rows;
                        tasks.analysisUpdater(examCode,(int) value,analysisId);

                        counter++;
                        String adm = rs.getString("admNumber");

                        double examresult = rs.getDouble("meanpoints");
                        if (examresult == previousscore) {
                            tiechck++;
                        } else {
                            headcount++;
                            headcount += tiechck;
                            tiechck = 0;
                        }

                        String sql4 = "Update examanalysistable  set Streampositionthisterm='" + headcount + "',Streampositionthistermoutof='" + totalentries + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "'  and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql4);
                        ps.execute();

                        previousscore = rs.getDouble("meanpoints");
                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);
            }
            finally {
                try {
                    con.close();

                } catch (SQLException sq) {

                    sq.printStackTrace();
                }

            }

        } else {
            try {
                int press = 0;
                int year = Integer.valueOf(academicYear);
                ps = con.prepareStatement("Select precisions from terms where termname='" + termName + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    press = rs.getInt("precisions");
                }
                int lastpress = press - 1;
                int headcount = 0;
                double previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select count(distinct admnumber ) as total from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' ";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
              if(rs.next()) {
                    totalentries=rs.getInt("total");
                }
                int tiechck = 0;
                rows = totalentries;
                counter = 1;

                String sql3 = "Select meanmarks,admnumber from examanalysistable where examcode='" + examCode + "' and academicyear='" + academicYear + "'  group by admnumber order by meanmarks desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    value += 10.0000 / rows;

                    tasks.analysisUpdater(examCode,(int) value,analysisId);

                    counter++;
                    double examresult = rs.getDouble("meanmarks");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount = (headcount + tiechck);
                        tiechck = 0;
                    }
                    String lastexamcode;
                    String lasttermposition;
                    String lastposstreampos;
                    String lastermposoutof;
                    String lasttermstreamposoutof;
                    if (termName.toString().equalsIgnoreCase("Term 1")) {
                        lastexamcode = examCode.toString().replaceAll(String.valueOf(year), String.valueOf((year - 1)));
                        lastexamcode = lastexamcode.replaceAll(examName + "TM" + press, examName + "TM3");
                    } else {

                        lastexamcode = examCode.toString().replaceAll(examName + "TM" + press, examName + "TM" + lastpress);
                    }

                    String sqla = "Select classpositionthisterm,Streampositionthisterm,Streampositionthistermoutof,classpositionthistermoutof from examanalysistable where examcode='" + lastexamcode + "' and admnumber='" + adm + "' group by admnumber ";
                    ps = con.prepareStatement(sqla);
                    ResultSet last = ps.executeQuery();
                    if (last.next()) {
                        lasttermposition = last.getString("classpositionthisterm");
                        lastposstreampos = last.getString("Streampositionthisterm");
                        lastermposoutof = last.getString("classpositionthistermoutof");
                        lasttermstreamposoutof = last.getString("Streampositionthistermoutof");

                    } else {
                        lasttermposition = " ";
                        lastposstreampos = " ";
                        lastermposoutof = " ";
                        lasttermstreamposoutof = " ";
                    }
                    String sql4 = "Update examanalysistable  set classpositionlasttermoutof='" + lastermposoutof + "',Streampositionlasttermoutof='" + lasttermstreamposoutof + "',classpositionlastterm='" + lasttermposition + "',streampositionlastterm='" + lastposstreampos + "', classpositionthisterm='" + headcount + "',classpositionthistermoutof='" + totalentries + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "'  and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getDouble("meanmarks");
                }

                String sql = "Select streamname from streams ";
                ps = con.prepareStatement(sql);
                ResultSet Rx = ps.executeQuery();
                while (Rx.next()) {
                    String name = Rx.getString("StreamName");
                    headcount = 0;
                    previousscore = 0;
                    totalentries = 0;

                    sql2 = "Select count(distinct admnumber) as total from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' and stream='" + name + "' ";
                    ps = con.prepareStatement(sql2);
                    ResultSet re = ps.executeQuery();
                    if (re.next()) {

                        totalentries=re.getInt("total");
                    }

                    tiechck = 0;
                    rows = totalentries;
                    counter = 1;
                    sql3 = "Select meanmarks,admnumber from examanalysistable where examcode='" + examCode + "' and academicyear='" + academicYear + "' and stream='" + name + "'  group by admnumber order by meanmarks desc";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("admNumber");
                        value += 2.5000 / rows;
                        tasks.analysisUpdater(examCode,(int) value,analysisId);
                        counter++;

                        double examresult = rs.getDouble("meanmarks");
                        if (examresult == previousscore) {
                            tiechck++;
                        } else {
                            headcount++;
                            headcount = (headcount + tiechck);
                            tiechck = 0;
                        }

                        String sql4 = "Update examanalysistable  set Streampositionthisterm='" + headcount + "',Streampositionthistermoutof='" + totalentries + "' where examcode='" + examCode + "' and academicyear='" + academicYear + "' and classname='" + className + "'  and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql4);
                        ps.execute();

                        previousscore = rs.getDouble("meanmarks");
                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);
                value = 0;


            }
            finally {
                try {
                    con.close();

                } catch (SQLException sq) {

                    sq.printStackTrace();
                }

            }
        }

        value = 0;

    }

    public void overallPerformance(String academicYear,String className,String examCode,String examName,String termName,String positionAllocationMode,String analysisId) {
        Connection con=DbConnection.connectDb(schoolDb);
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            if (positionAllocationMode.equalsIgnoreCase("avg Points")) {
                double total = 0;
                String sqla = "Select distint subjectcode from subjects order by subjectcode";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String subcode = rs.getString("SubjectCode");
                    String sql = "Select avg(totalpoints) from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "' and subjectcode='" + subcode + "'";
                    ps = con.prepareStatement(sql);
                    ResultSet rsb = ps.executeQuery();
                    if (rs.next()) {
                        total = rsb.getDouble("avg(totalpoints)");
                    }

                }
                String sql = "Select sum(totalpoints) from examanalysistable where examcode='" + examCode + "' and classname='" + className + "' and academicyear='" + academicYear + "'";

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisId);

        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }
    }


    public  Map isAnalysable(String examCode)
    {
        int rows=0;
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {




            String sql22 = "Select count(*) from  markstable where examcode='" + examCode + "' ";
            ps = con.prepareStatement(sql22);
            rs = ps.executeQuery();
            if (rs.next()) {

                rows = rs.getInt("count(*)");
            }

if(rows>2)
{
    String analysisId= IdGenerator.keyGen();
    response.put("analysisId",analysisId);

    response.put("responseCode","200");
    response.put("responseDescription","Yes");
    return response;

}
else{
    response.put("responseCode","404");
    response.put("responseDescription","Failed No Marks Found");
    return response;
}
        }
        catch (Exception sq)
        {
            sq.printStackTrace();
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sq.getMessage());

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



}
