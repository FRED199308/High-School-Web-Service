package com.lunartech.HighSchoolProWebService;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EnteredMarksReview {
    AnalysisTasks task;
    public EnteredMarksReview(String schoolDb) {
        this.schoolDb = schoolDb;
        task=new AnalysisTasks(schoolDb);
    }

    String schoolDb;
    public  void review(String examCode,String academicYear,String analysisId)
    {



task.startAnalysis(examCode,analysisId,"Marks Review");

        Map<String,String> map=new HashMap<String,String>();
        Connection con;
        PreparedStatement ps;
ResultSet rs;
        con = DbConnection.connectDb(schoolDb);


        String excode =examCode;
        String STREAM, teacherintial;
        try {
            int max = 0, value, counter = 0;
            String sq1 = "Select * from markstable where academicyear='" + academicYear + "' and examcode='" + excode + "' group by subjectcode,admnumber";

            ps = con.prepareStatement(sq1);
            ResultSet rx = ps.executeQuery();
            while (rx.next()) {

                max++;

            }
            int weight;
//String sqlw="Select Examweight from exams where examname='"+jexame.getSelectedItem()+"' and classcode='"+globals.classCode(jclass.getSelectedItem().toString())+"'";
//ps=con.prepareStatement(sqlw);
//rx=ps.executeQuery();
//if(rx.next())
//{
//    weight = 100;
//}
            String sq = "Select * from markstable where academicyear='" + academicYear + "' and examcode='" + excode + "' group by subjectcode,admnumber";

            ps = con.prepareStatement(sq);
            rx = ps.executeQuery();
            while (rx.next()) {
                counter++;
                value = (counter * 100) / max;
task.analysisUpdater(examCode,value,analysisId);

                String subcode, grade = "", classcode, point = "", adm;
                int perce, outof, score;
                subcode = rx.getString("Subjectcode");
                classcode = rx.getString("classcode");
                perce = rx.getInt("exampercentage");
                adm = rx.getString("AdmNumber");
                outof = rx.getInt("examoutof");
                score = rx.getInt("examscore");
                STREAM = rx.getString("Streamcode");
                String sql2 = "Select weight from examweights where examcode='" + excode + "'";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {

                    weight = rs.getInt("Weight");
                }
                else{
                    weight=100;

                }

                int converted = (score * weight) / outof;

                String sql = "Select grade,end_at,start_from from subjectgrading where classcode='" + classcode + "' and subjectcode='" + subcode + "' and '" + perce + "'>=start_from and '" + perce + "'<=end_at  group by sortcode";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    grade = rs.getString("grade");
                    String qq = "Select points from points_for_each_grade where grade='" + rs.getString("grade") + "'";
                    ps = con.prepareStatement(qq);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        point = rs.getString("points");
                    }
                }
                String sqla = "Select initials,teachercode from subjectrights,staffs where classcode='" + classcode + "' and subjectcode='" + subcode + "'  and   streamcode='" + STREAM + "' and teachercode=staffs.employeecode";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                if (rs.next()) {
                    teacherintial = rs.getString("Initials");

                } else {
                    teacherintial = "";
                }
                String qrry = "Update markstable set examgrade='" + grade + "' ,class_teacher_initials='" + teacherintial + "',exampoints='" + point + "' ,convertedscore='" + converted + "',convertedscoreoutof='" + weight + "' where academicyear='" + academicYear + "' and examcode='" + excode + "' and subjectcode='" + subcode + "' and admnumber='" + adm + "'";
                ps = con.prepareStatement(qrry);
                ps.execute();

            }
            task.endAnalysis(examCode,"Completed","safe",analysisId);
            map.put("responseCode","200");
            map.put("responseDescription","Review Started");

           // dia.dispose();
           // JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "New Subject Grading System Applied Successfully\n Kindly Reanalyse This Exam To Affect The Merit List");

        } catch (Exception sq) {
            sq.printStackTrace();
            task.endAnalysis(examCode,"Turminated",sq.getMessage(),analysisId);
            map.put("responseCode","501");
            map.put("responseDescription","Error:"+sq.getMessage());

          //  JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }
}
