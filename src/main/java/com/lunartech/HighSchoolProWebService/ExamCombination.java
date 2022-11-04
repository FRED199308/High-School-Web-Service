package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class ExamCombination {

    String schoolDb;
Globals globals;
    public ExamCombination(String schoolDb) {
        this.schoolDb = schoolDb;
        globals=new Globals(schoolDb);
    }

    public  Map isCombinable(String className, String academicYear, String termName)
    {
        int rows=0;
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try {



            String sqll="Select count(distinct admnumber) as total2 from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"'";
            ps=con.prepareStatement(sqll);
            ResultSet ss=ps.executeQuery();
            if(ss.next())
            {

               rows=ss.getInt("total2");
               System.out.println("rows:"+rows);
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
    
    public  void combineExams(String termName,String academicYear,String className,String analysisid,boolean reverse)
    { AnalysisTasks tasks=new AnalysisTasks(schoolDb);
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        String querry="";
        String examCode= ExamCodesGenerator.generatecode(className, academicYear, termName, "TOTAL").toUpperCase();
        try {

       
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(0);
            nf.setMinimumFractionDigits(0);
            String currentTerm=termName;
            int currentyear=Integer.parseInt(academicYear);
            int previousYear;
            int currenttermNumber=Integer.parseInt(termName.substring(5));
            int currentClassNumber=Integer.parseInt(className.substring(5));
            int previousClassNumber=currentClassNumber;


            int previoustermNumber;
            if(currenttermNumber==1)
            {
                previoustermNumber=3;

                previousYear=currentyear-1;
                previousClassNumber=previousClassNumber-1;
            }
            else{
                previoustermNumber=currenttermNumber-1;
                previousYear=currentyear;
                previousClassNumber=currentClassNumber;
            }

            String previousTerm="TERM "+previoustermNumber;
            String previuosClass="Form "+previousClassNumber;
            previousYear=previousYear;

            String previousExamCode= ExamCodesGenerator.generatecode(previuosClass,String.valueOf( previousYear), previousTerm, "END TERM").toUpperCase();
            tasks.startAnalysis(examCode,analysisid,"CombineMarks");
            if(reverse)
            {

                ps=con.prepareStatement("Select * from examcombinationmodes where examcode='"+examCode+"'");
                rs=ps.executeQuery();
                if(rs.next())
                {
                    ps=con.prepareStatement("Update examcombinationmodes set CombineMode='"+"REVERSAL"+"' where examcode='"+examCode+"'");
                    ps.execute();
                }
                else{
                    ps=con.prepareStatement("Insert into examcombinationmodes values('"+examCode+"','"+"REVERSAL"+"')");
                    ps.execute();
                }
               
                int counter=0;
                String sqll="Select count(distinct admnumber) as total2 from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"'";
                ps=con.prepareStatement(sqll);
                ResultSet ss=ps.executeQuery();
                if(ss.next())
                {

                    counter=ss.getInt("total");
                }
                if(counter==0)
                {
                   // JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Marks Were Found For This Class In The Selected Term And Academic Year");
                }
                String sqll2="Delete from  markstable where  examcode='"+examCode+"'";
                ps=con.prepareStatement(sqll2);
                ps.execute();





                int value,counter2=0;

                String sql2="Select distinct admnumber from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"'";
                ps=con.prepareStatement(sql2);
                ss=ps.executeQuery();
                while(ss.next())
                {
                    counter2++;
                    value=(counter2*100)/counter;
                 
                       tasks.analysisUpdater(examCode,value,analysisid);
                    String adm=ss.getString("Admnumber");

                    String sql=" select * from subjects";
                    ps=con.prepareStatement(sql);
                    ResultSet rx=ps.executeQuery();
                    while(rx.next())
                    {

                        int entrycounter=0;
                        String subcode=rx.getString("Subjectcode");
                        boolean openerchecker=false,midtermchecker=false,endtermchercker=false;
                        int cummulativeWeight=0;
                        double score=0,points=0,openerscore,midtermscore,endtermscore;  String grade="",streamcode="",classCode="",techcom="",exname="";
                        String sql3="Select * from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"' and admnumber='"+adm+"' and subjectcode='"+subcode+"' and examName !='"+"End Term"+"'";
                        ps=con.prepareStatement(sql3);
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {
                            String ex=rr.getString("Examcode");

                            if(rr.getString("Examname").equalsIgnoreCase("Opener"))
                            {
                                openerscore= rr.getInt("convertedscore");
                                cummulativeWeight+=globals.examWeightChecker(ex);
                                //  System.err.println("Adm:"+adm+" cummulative weight  in Opener"+globals.subjectName(subcode)+"  "+cummulativeWeight );

                                System.err.println("Adm:"+adm+" Opener Score In "+globals.subjectName(subcode)+"  "+openerscore );
                                score+=rr.getInt("convertedscore");
                            }
                            else if(rr.getString("Examname").equalsIgnoreCase("Mid Term"))
                            {cummulativeWeight+=globals.examWeightChecker(ex);
                                midtermscore=rr.getInt("convertedscore");
                                score+=rr.getInt("convertedscore");
                                //  System.err.println("Adm:"+adm+" cummulative weight in Midterm "+globals.subjectName(subcode)+"  "+cummulativeWeight );

                                System.err.println("Adm:"+adm+" midterm Score In "+globals.subjectName(subcode)+"  "+midtermscore );
                            }



                            streamcode=rr.getString("Streamcode");
                            classCode=rr.getString("Classcode");


                            techcom=rr.getString("class_teacher_initials");
                            entrycounter++;
                        }
                        String sqla="Select * from  markstable where  termcode='"+globals.termcode(previousTerm)+"' and academicyear='"+previousYear+"' and classcode='"+globals.classCode(previuosClass)+"' and admnumber='"+adm+"' and subjectcode='"+subcode+"' and examName ='"+"End Term"+"'";
                        ps=con.prepareStatement(sqla);
                        ResultSet RS=ps.executeQuery();
                        if(RS.next())
                        {
                            cummulativeWeight+=globals.examWeightChecker(previousExamCode);
                            endtermscore= RS.getInt("convertedscore");
                            score+=RS.getInt("convertedscore");
                            // System.err.println("Adm:"+adm+" cummulative weight In Endterm "+globals.subjectName(subcode)+"  "+cummulativeWeight );

                            System.err.println("Adm:"+adm+" Endterm Score In "+globals.subjectName(subcode)+"  "+endtermscore );


                        }

                        if(cummulativeWeight==0)
                        {
                            cummulativeWeight=100;
                        }
                        score=(score*100)/cummulativeWeight;
                        System.err.println("Adm:"+adm+" cummulative Score In "+globals.subjectName(subcode)+"  "+score );
                        //  System.err.println("Adm:"+adm+"Total cummulative weight "+globals.subjectName(subcode)+"  "+cummulativeWeight );

                        String sql5="Select grade,end_at,start_from from subjectgrading where classcode='"+classCode+"' and subjectcode='"+subcode+"' and '"+nf.format(score)+"'>=start_from and '"+nf.format(score)+"'<=end_at  group by sortcode";
                        ps=con.prepareStatement(sql5);
                        rs=ps.executeQuery();
                        if(rs.next())
                        {

                            grade=rs.getString("grade");

                            String qq="Select points from points_for_each_grade where grade='"+rs.getString("grade")+"'";
                            ps=con.prepareStatement(qq);
                            rs=ps.executeQuery();
                            if(rs.next())
                            {
                                points=rs.getInt("points");
                            }
                        }

                        String sql4="Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials)"
                                + " values('"+adm+"','"+subcode+"','"+classCode+"','"+streamcode+"','"+globals.termcode(termName)+"','"+academicYear.toString()+"','"+"TOTAL"+"','"+examCode+"','"+score+"','"+"100"+"','"+"100"+"','"+"100"+"','"+score+"','"+grade+"','"+points+"','"+techcom+"')";
                        ps=con.prepareStatement(sql4);
                        ps.execute();

                    }




                }


            }
            else{

                ps=con.prepareStatement("Select * from examcombinationmodes where examcode='"+examCode+"'");
                rs=ps.executeQuery();
                if(rs.next())
                {
                    ps=con.prepareStatement("Update examcombinationmodes set combinemode='"+"NORMAL"+"' where examcode='"+examCode+"'");
                    ps.execute();
                }
                else{
                    ps=con.prepareStatement("Insert into examcombinationmodes values('"+examCode+"','"+"NORMAL"+"')");
                    ps.execute();
                }


                int counter=0;
                String sqll="Select distinct admnumber from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"'";
                ps=con.prepareStatement(sqll);
                ResultSet ss=ps.executeQuery();
                while(ss.next())
                {

                    counter++;
                }
                if(counter==0)
                {
                   // JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Marks Were Found For This Class In The Selected Term And Academic Year");
                }
                String sqll2="Delete from  markstable where  examcode='"+examCode+"'";
                ps=con.prepareStatement(sqll2);
                ps.execute();





                int value,counter2=0;

                String sql2="Select distinct admnumber from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"'";
                ps=con.prepareStatement(sql2);
                ss=ps.executeQuery();
                while(ss.next())
                {
                    counter2++;
                    value=(counter2*100)/counter;
                       tasks.analysisUpdater(examCode,value,analysisid);
                    String adm=ss.getString("Admnumber");

                    String sql=" select * from subjects";
                    ps=con.prepareStatement(sql);
                    ResultSet rx=ps.executeQuery();
                    while(rx.next())
                    {

                        int entrycounter=0;
                        String subcode=rx.getString("Subjectcode");
                        boolean openerchecker=false,midtermchecker=false,endtermchercker=false;
                        int cummulativeWeight=0;
                        double score=0,points=0,openerscore=0,midtermscore=0,endtermscore=0;  String grade="",streamcode="",classCode="",techcom="",exname="";
                        String sql3="Select * from  markstable where  termcode='"+globals.termcode(termName)+"' and academicyear='"+academicYear+"' and classcode='"+globals.classCode(className)+"' and admnumber='"+adm+"' and subjectcode='"+subcode+"'";
                        ps=con.prepareStatement(sql3);
                        ResultSet rr=ps.executeQuery();
                        while(rr.next())
                        {

                            String ex=rr.getString("Examcode");

                            if(rr.getString("Examname").equalsIgnoreCase("Opener"))
                            {
                                openerscore= rr.getInt("convertedscore");
                                cummulativeWeight+=globals.examWeightChecker(ex);
                                score+=rr.getInt("convertedscore");
                            }
                            else if(rr.getString("Examname").equalsIgnoreCase("Mid Term"))
                            {cummulativeWeight+=globals.examWeightChecker(ex);
                                midtermscore=rr.getInt("convertedscore");
                                score+=rr.getInt("convertedscore");
                            }
                            else if(rr.getString("Examname").equalsIgnoreCase("End Term"))
                            {cummulativeWeight+=globals.examWeightChecker(ex);
                                endtermscore= rr.getInt("convertedscore");
                                score+=rr.getInt("convertedscore");
                            }
                            streamcode=rr.getString("Streamcode");
                            classCode=rr.getString("Classcode");


                            techcom=rr.getString("class_teacher_initials");
                            entrycounter++;
                        }

                        if(cummulativeWeight==0)
                        {
                            cummulativeWeight=100;
                        }
                        score=(score*100)/cummulativeWeight;


                        String sql5="Select grade,end_at,start_from from subjectgrading where classcode='"+classCode+"' and subjectcode='"+subcode+"' and '"+nf.format(score)+"'>=start_from and '"+nf.format(score)+"'<=end_at  group by sortcode";
                        ps=con.prepareStatement(sql5);
                        rs=ps.executeQuery();
                        if(rs.next())
                        {

                            grade=rs.getString("grade");

                            String qq="Select points from points_for_each_grade where grade='"+rs.getString("grade")+"'";
                            ps=con.prepareStatement(qq);
                            rs=ps.executeQuery();
                            if(rs.next())
                            {
                                points=rs.getInt("points");
                            }
                        }

                        String sql4="Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials) values('"+adm+"','"+subcode+"','"+classCode+"','"+streamcode+"','"+globals.termcode(termName)+"','"+academicYear.toString()+"','"+"TOTAL"+"','"+examCode+"','"+score+"','"+"100"+"','"+"100"+"','"+"100"+"','"+score+"','"+grade+"','"+points+"','"+techcom+"')";
                        ps=con.prepareStatement(sql4);
                        ps.execute();

                    }




                }

            }



tasks.endAnalysis(examCode,"Completed","Safe",analysisid);

           // JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Exams Combined Successfully,\n Open The Marks Analysis Pane And Analyse Total Exam For Report Form Generation");
        } catch (Exception sq) {
            tasks.endAnalysis(examCode,"turminated",sq.getMessage(),analysisid);
            sq.printStackTrace();
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
