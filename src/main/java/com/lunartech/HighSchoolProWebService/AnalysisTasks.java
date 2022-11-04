package com.lunartech.HighSchoolProWebService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AnalysisTasks {
    String schoolDb;
    public AnalysisTasks(String schoolDb) {
        this.schoolDb=schoolDb;
    }

    public void analysisUpdater(String examCode, int status, String analysisId)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try{

            String querry="Update examanalysisprogress set progress='"+status+"' where examCode='"+examCode+"' and analysisId='"+analysisId+"'";
            ps=con.prepareStatement(querry);
            ps.execute();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

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

    public void startAnalysis(String examCode,String analysisId,String taskCategory)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);

        try{

            String querry="insert into examanalysisprogress  value ('"+examCode+"','"+"0"+"','"+"On Progress"+"','"+"Started"+"','"+analysisId+"',now(),'"+taskCategory+"')";
            ps=con.prepareStatement(querry);
            ps.execute();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }



    }

    public void endAnalysis(String examCode,String finalStatus,String comment,String analysisId)
    {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try{

            String querry="update examanalysisprogress set status='"+finalStatus+"',comment='"+comment+"' where examcode='"+examCode+"' and analysisId='"+analysisId+"'";
            ps=con.prepareStatement(querry);
            ps.execute();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        finally {
            try {
                con.close();

            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }



    }

    public  Map getTaskStatus(String taskId, String task)
    {
        Map response=new HashMap();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb(schoolDb);
        try{

            String querry="select progress,status,comment from examanalysisprogress  where analysisId='"+taskId+"'  and taskcategory='"+task+"' ";
            ps=con.prepareStatement(querry);
            rs=   ps.executeQuery();
            if(rs.next())
            {
                if(rs.getString("status").equalsIgnoreCase("completed"))
                {


                    response.put("progress",rs.getString("progress"));
                    response.put("status",rs.getString("status"));
                    response.put("responseCode","200");
                    response.put("responseDescription",rs.getString("status"));

                    return response;

                }
                else if(rs.getString("status").equalsIgnoreCase("on progress")){


                    response.put("progress",rs.getString("progress"));
                    response.put("status",rs.getString("status"));
                    response.put("responseCode","200");
                    response.put("responseDescription","Analysis On Progress");
                    return response;
                }
                if(rs.getString("status").equalsIgnoreCase("turminated"))
                {

                    response.put("progress",rs.getString("progress"));

                    response.put("responseCode","200");
                    response.put("responseDescription","Error Occured During Analysis:"+rs.getString("comment"));
                    response.put("status",rs.getString("status"));

                    return response;

                }
                else {



                    response.put("progress",rs.getString("progress"));
                    response.put("status",rs.getString("status"));
                    response.put("responseCode","200");
                    response.put("responseDescription","Analysis On Progress");
                    return response;

                }

            }
            else{
                response.put("progress","No Analysis On Progress");

                response.put("responseCode","404");
                response.put("responseDescription","No Analysis On Progress");
                return response;
            }
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            response.put("responseCode","501");
            response.put("responseDescription","Error Occured:"+sqlException.getMessage());

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
