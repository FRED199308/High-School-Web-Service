/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lunartech.HighSchoolProWebService;


import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author FRED
 */
@RestController
public class Controller {


    @RequestMapping(
            value = "/hello",
            method = RequestMethod.GET)
    public  String sayHi()
    {

       return "hello 2...";
    }

    @RequestMapping(
            value = "/{schoolName}/hello",
            method = RequestMethod.POST)
    public  String sayHi2(@PathVariable("schoolName") String schoolName)
    {
        for(int i=0;i<100;++i)
        {
            try {
                System.out.println("Countring...");
                Thread.sleep(2000);
                return "Hello "+ schoolName + ",Your Database Name is:"+ EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName");
            }
            catch(Exception sq)
            {
                sq.printStackTrace();
                return sq.getMessage();
            }

        }
        return "Done";
    }



// in another thread


    @RequestMapping("/{schoolName}/terms")
    public ArrayList terms(@PathVariable("schoolName") String schoolName)
    {

Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  globals.terms();
    }
    @RequestMapping("/{schoolName}/classes")
    public ArrayList classes(@PathVariable("schoolName") String schoolName)
    {
        System.err.println();
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  globals.classlevels();
    }
    @RequestMapping("/{schoolName}/years")
    public ArrayList years(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  globals.years();
    }
    @RequestMapping("/{schoolName}/streams")
    public ArrayList streams(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  globals.streams();
    }
    @RequestMapping("/{schoolName}/examsDetails")
    public ArrayList getExams(@PathVariable("schoolName") String schoolName)
    {
        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  exams.getExams();
    }

    @RequestMapping("/schools")
    public ArrayList enrolledSchools()

    {

        return  EnrolledSchools.getSchools();
    }
    @RequestMapping("/{schoolName}/exams")
    public ArrayList exams(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  globals.exams();
    }

    @RequestMapping(
            value = "/{schoolName}/authenticate",
            method = RequestMethod.POST)
    public Map authenticate(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName)
    {

        Authentication authentication=new Authentication(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return  authentication.authenticate(payload.get("username").toString(),payload.get("password").toString());



    }






    @RequestMapping(
            value = "/{schoolName}/download/{fileName:.+}",
            method = RequestMethod.POST)
    public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName,@RequestBody Map<String, Object> json,@PathVariable("schoolName") String schoolName) throws IOException, IOException {
        DocPath docPath=new DocPath(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String path = docPath.documentFolderPath() + fileName+".pdf";
        Map map=new HashMap();
        map.put("responseCode","200");
        map.put("responseDescription","Success");

        File file = new File(path);

        if(fileName.equalsIgnoreCase("meritList"))
        {

            String selectedClass,term,year,stream,examcode,exam,adm;
            selectedClass=json.get("selectedClass").toString();
            term=json.get("term").toString();
            stream=json.get("stream").toString();
            year=json.get("year").toString();
            examcode=json.get("examcode").toString();
            exam=json.get("exam").toString();
            ReportGenerator reportGenerator=new ReportGenerator(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            reportGenerator.MeritListReport(exam,examcode,year,selectedClass,term,stream);


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }
        else if(fileName.equalsIgnoreCase("reportForm")){

                System.out.println("Exam code"+json.get("examcode"));
                String selectedClass,term,year,stream,examcode,exam,adm;
                selectedClass=json.get("selectedClass").toString();
                term=json.get("term").toString();
                stream=json.get("stream").toString();
                year=json.get("year").toString();
                examcode=json.get("examcode").toString();
                exam=json.get("exam").toString();
                adm=json.get("admissionNumber").toString();
            ReportFormGenerator reportFormGenerator=new ReportFormGenerator(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            reportFormGenerator.reportForms(exam,examcode,year,selectedClass,term,adm);


                if (file.exists()) {
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                    if (mimeType == null) mimeType = "application/octet-stream";
                    response.setContentType(mimeType);
                    response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                    response.setContentLength((int) file.length());
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                }

        }


        else if(fileName.equalsIgnoreCase("studentImage")){


         String adm=json.get("admissionNumber").toString();
file=new File(docPath.documentFolderPath()+adm+".jpg");

            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("subjectResults")){

            Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


            marks.sujectMarks(json.get("examCode").toString(),json.get("examName").toString(),json.get("streamName").toString(),json.get("termName").toString(),json.get("academicYear").toString(),json.get("className").toString(),json.get("subjectName").toString(),json.get("sort").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("missingMarks")){


            Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());



            marks.missingSubjectMark(json.get("examCode").toString(),json.get("examName").toString(),json.get("streamName").toString(),json.get("termName").toString(),json.get("academicYear").toString(),json.get("className").toString(),json.get("subjectName").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("subjectallocationReport")){



            Subject subject=new Subject(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


            subject.allocationReport(json.get("className").toString(),json.get("academicYear").toString(),json.get("streamName").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());

                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("missingmarksoverall")){

System.out.println(json);

            Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


            marks.overallMarks(json.get("className").toString(),json.get("academicYear").toString(),json.get("streamName").toString(),json.get("examCode").toString(),json.get("examName").toString(),json.get("termName").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("BestStudentOverallReport")){

            System.out.println(json);

            BestStudentOverallReport bestStudentOverallReport=new BestStudentOverallReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


            bestStudentOverallReport.beststudents(json.get("examCode").toString(), json.get("examName").toString(), json.get("termName").toString(),json.get("academicYear").toString(), json.get("className").toString(), json.get("streamName").toString(),Integer.parseInt(json.get("number").toString()));



            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("MostImprovedOverallReport")){
            BestStudentOverallReport bestStudentOverallReport=new BestStudentOverallReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            System.out.println(json);
            Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
            String analyse;
            analyse=json.get("analyse").toString();
            if(analyse.equalsIgnoreCase("Yes"))
            {




                bestStudentOverallReport.mostImprovedArithmetics(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() );

                bestStudentOverallReport.improvedStudentsReport(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() ,json.get("streamName1").toString(),json.get("className").toString(),Integer.parseInt(json.get("number").toString()));

            }
            else {
                bestStudentOverallReport.improvedStudentsReport(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() ,json.get("streamName1").toString(),json.get("className").toString(),Integer.parseInt(json.get("number").toString()));

            }



            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("MostImprovedPerSubjectReport")){
            BestStudentOverallReport bestStudentOverallReport=new BestStudentOverallReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
            Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
            System.out.println("received this:"+json);

            String analyse;
            analyse=json.get("analyse").toString();
            if(analyse.equalsIgnoreCase("Yes"))
            {



                bestStudentOverallReport.mostImprovedArithmetics(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() );

                bestStudentOverallReport.improvedStudentsReportPerSubject(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() ,json.get("streamName1").toString(),json.get("className1").toString(),json.get("subjectName").toString(),Integer.parseInt(json.get("number").toString()));

            }
            else {
                bestStudentOverallReport.improvedStudentsReportPerSubject(json.get("examCode").toString(), json.get("examName").toString(), json.get("academicYear").toString(),globals.classCode(json.get("className").toString()), json.get("termName").toString(),json.get("examCode1").toString(), json.get("examName1").toString(), json.get("academicYear1").toString(),globals.classCode(json.get("className1").toString()), json.get("termName1").toString() ,json.get("streamName1").toString(),json.get("className1").toString(),json.get("subjectName").toString(),Integer.parseInt(json.get("number").toString()));

            }



            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("BestStudentperSubject")){

            System.out.println(json);

            BestStudentReport bestStudentReport=new BestStudentReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

           bestStudentReport.subjectResults(json.get("examCode").toString(), json.get("examName").toString(), json.get("termName").toString(),json.get("academicYear").toString(), json.get("className").toString(), json.get("subjectName").toString(),json.get("number").toString());



            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("emptyscoresheet")){

            Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


            marks.emptyScoreSheet(json.get("className").toString(),json.get("academicYear").toString(),json.get("streamName").toString(),json.get("examCode").toString(),json.get("examName").toString(),json.get("termName").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }



        else if(fileName.equalsIgnoreCase("generalclasslist")){

System.out.println(json);
            Classes classes=new Classes(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

           classes.classList(json.get("streamName").toString(),json.get("className").toString(),json.get("category").toString(),json.get("gender").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("generalclasslistPhotos")){

            System.out.println(json);
            ClassListReport classListReport=new ClassListReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            classListReport.generalClassListPhotoViewer(schoolName,json.get("streamName").toString(),json.get("className").toString(),json.get("gender").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("SubjectClasslist")){
            Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            System.out.println(json);
            Classes classes=new Classes(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            classes.subjectClassList(json.get("streamName").toString(),json.get("className").toString(),globals.subjectCode(json.get("subjectName").toString()));


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }

        else if(fileName.equalsIgnoreCase("gradeDistributionReport")){
boolean targets;
            GradeDistributionReport gradeDistributionReport=new GradeDistributionReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
            Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            System.out.println(json);
            if(json.get("targets").toString().equalsIgnoreCase("false"))
            {
                targets=false;
            }
            else {
                targets=true;
            }
            String action;
            action=json.get("action").toString();
            if(action.equalsIgnoreCase("Meangrade"))
            {
                GradeDistributionReport gradeDistributionReport1=new GradeDistributionReport(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

                gradeDistributionReport.gradedistributionReport(json.get("examCode").toString(), json.get("examName").toString(), json.get("termName").toString(), json.get("className").toString(), json.get("academicYear").toString(), json.get("streamName").toString(),json.get("distributionMode").toString(),targets);

            }
            else {
                System.out.println("went to fetch Mean GradeDistribution per subject");
                String subjectCode=json.get("subjectName").toString();

                if(subjectCode.equalsIgnoreCase("Select subject"))
                {
                    subjectCode="All";
                    System.out.println("subject Was Not Selected");
                }
                else{
                    subjectCode=globals.subjectCode(json.get("subjectName").toString());
                }
                System.out.println("subject Code"+subjectCode);
                gradeDistributionReport.distributionPerSubject(json.get("examCode").toString(), json.get("examName").toString(), json.get("termName").toString(), json.get("className").toString(), json.get("academicYear").toString(), json.get("streamName").toString(),subjectCode,json.get("distributionMode").toString(),targets);

            }
          // Classes.classList(json.get("streamName").toString(),json.get("className").toString(),json.get("category").toString(),json.get("gender").toString());


            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


        else if(fileName.equalsIgnoreCase("Fee Statement")){

            System.out.println(json);
            ReportGenerator reportGenerator=new ReportGenerator(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

reportGenerator.FeeStatementGenerator(json.get("admissionNumber").toString());

            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }
        else if(fileName.equalsIgnoreCase("totalFee Statement")){

            ReportGenerator reportGenerator=new ReportGenerator(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            file=new File(docPath.documentFolderPath() + "Fee Statement"+".pdf");
            reportGenerator.totalFeeStatementGenerator(json.get("admissionNumber").toString());

            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }
        else if(fileName.equalsIgnoreCase("FeeBalancesReport")){


            ReportGenerator reportGenerator=new ReportGenerator(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

            reportGenerator.feeBalanceReport(json.get("amount").toString(),json.get("streamName").toString(),json.get("className").toString());

            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }

        }


    }


    @RequestMapping("/{province}/counties")
    public ArrayList counties(@PathVariable("province") String provinceName,@PathVariable("schoolName") String schoolName)
    {
        GeographicalInfor geographicalInfor=new GeographicalInfor(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return  geographicalInfor.counties(provinceName);
    }
    @RequestMapping("/{schoolName}/{country}/provinces")
    public ArrayList provices(@PathVariable("country") String countryName,@PathVariable("schoolName") String schoolName)
    {
        GeographicalInfor geographicalInfor=new GeographicalInfor(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return geographicalInfor.provinces(countryName);
    }
    @RequestMapping("/{schoolName}/students/{className}")
    public ArrayList students(@PathVariable("className") String className,@PathVariable("schoolName") String schoolName)
    {
        Students students=new Students(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return students.getClassSStudents(className);
    }



    @RequestMapping("/{schoolName}/studentsSearch/{search}")
    public ArrayList studentSearch(@PathVariable("search") String search,@PathVariable("schoolName") String schoolName)
    {
        Students students=new Students(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return students.searchStudent(search);
    }


    @RequestMapping("/SchoolRegistrationStatus/{schoolName}")
    public Map schoolStatus(@PathVariable("schoolName") String schoolName)
    {
       return EnrolledSchools.getSchoolsDetails(schoolName);
    }
    @RequestMapping(value= "/{schoolName}/student/{adm}",method = RequestMethod.GET)
    public Map studentInformation(@PathVariable("schoolName") String schoolName,@PathVariable("adm") String admissionNumber)
    {
        Students students=new Students(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return students.getStudentDetails(admissionNumber);
    }
    @RequestMapping(value = "/{schoolName}/student/{adm}" ,method=RequestMethod.PUT)
    public Map editStudentInformation(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName,@PathVariable("adm") String admissionNumber)
    {
        Students students=new Students(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        DocPath docPath=new DocPath(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return students.editStudentDetails(admissionNumber,payload.get("adm").toString(),payload.get("upi").toString(),payload.get("firstName").toString(),payload.get("middleName").toString(),payload.get("lastName").toString(),
                payload.get("gender").toString(),payload.get("dob").toString(),payload.get("doa").toString(),payload.get("classAdmitted").toString(),payload.get("streamAdmitted").toString(),globals.currentTermName(),globals.currentTermName(),
                payload.get("currentClass").toString(),payload.get("currentStream").toString(),payload.get("kcpe").toString(),payload.get("country").toString(),payload.get("county").toString(),payload.get("province").toString(),payload.get("constituency").toString(),payload.get("ward").toString()
                ,payload.get("parent").toString(),payload.get("tel1").toString(),payload.get("tel2").toString(),docPath.documentFolderPath(),payload.get("program").toString());



    }

    @RequestMapping(value="/{schoolName}/student/{adm}", method=RequestMethod.DELETE)
    public Map deleteStudent(@PathVariable("schoolName") String schoolName,@PathVariable("adm") String admissionNumber)
    {
        Students students=new Students(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return students.deleteStudent(admissionNumber);
    }

    @RequestMapping("/{schoolName}/countries")
    public ArrayList countries(@PathVariable("schoolName") String schoolName)
    {
        GeographicalInfor geographicalInfor=new GeographicalInfor(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return geographicalInfor.countries();
    }
    @RequestMapping("/{schoolName}/subjects")
    public ArrayList subjects(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.subjects();

    }


    @RequestMapping("/{schoolName}/subjectgradingSystem")
    public ArrayList gradingSystem(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.subjectsGradingSystems();

    }
    @RequestMapping("/{schoolName}/gradingSystem/{subject}")
    public ArrayList subjectGradingSystem(@PathVariable("subject") String subjectName,@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.subjectsGradingSystem(subjectName);

    }
    @RequestMapping("/{schoolName}/classTeacherComments/{class}")
    public ArrayList classTeacherComments(@PathVariable("class") String className,@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.classTeacherComment(className);

    }
    @RequestMapping("/{schoolName}/subjectComments/{subject}")
    public ArrayList subjectComments(@PathVariable("subject") String subjectName,@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.subjectComment(subjectName);

    }
    @RequestMapping("/{schoolName}/principalComments")
    public ArrayList principalComments(@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.principalComment();

    }


    @RequestMapping(
            value = "/{schoolName}/comment/{className}/{grade}/{comment}",
            method = RequestMethod.PUT)
    public Map saveComment(@PathVariable("schoolName") String schoolName,@PathVariable("comment") String comment,@PathVariable("grade") String grade,@PathVariable("className") String className)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return globals.saveTeacherComment(className,grade,comment);
    }



    @RequestMapping(
            value = "/{schoolName}/subjectcomment/{subjectName}/{grade}/{comment}",
            method = RequestMethod.PUT)
    public Map saveSubjectComment(@PathVariable("schoolName") String schoolName,@PathVariable("comment") String comment,@PathVariable("grade") String grade,@PathVariable("subjectName") String subjectName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.saveSubjectComment(subjectName,grade,comment);
    }

    @RequestMapping(
            value = "/{schoolName}/principalcomment/{grade}/{comment}",
            method = RequestMethod.PUT)
    public Map savePrincipalComment(@PathVariable("schoolName") String schoolName,@PathVariable("comment") String comment,@PathVariable("grade") String grade)
    { Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.savePrincipalComment(grade,comment);
    }

    @RequestMapping(
            value = "/{schoolName}/configurations",
            method = RequestMethod.GET)
    public Map systemConfiguration(@PathVariable("schoolName") String schoolName )
    { Configurations configurations=new Configurations(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return configurations.getConfigurations();
    }

    @RequestMapping(
            value = "/{schoolName}/saveConfigurations",
            method = RequestMethod.POST)
    public Map saveConfigurations(@PathVariable("schoolName") String schoolName,@RequestBody Map<String, Object> load )
    {
        Configurations configurations=new Configurations(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return configurations.saveConfigurations(load);
    }


    @RequestMapping("/{schoolName}/gradingSystemperClass/{classs}")
    public ArrayList classGradingSystem(@PathVariable("classs") String className,@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.classGradingSystem(className);

    }



    @RequestMapping("/{schoolName}/gradingSystem/{subject}/{classname}")
    public ArrayList classSubjectGradingSystem(@PathVariable("subject") String subjecName,@PathVariable("classname") String className,@PathVariable("schoolName") String schoolName)
    {
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return globals.classSubjectsGradingSystem(subjecName,className);

    }

    @RequestMapping("/{schoolName}/studentsEligibleForMarksEntry")
    public ArrayList studentsEligibleForMarksEntry(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return marks.studentTakingASubject(load.get("subjectName").toString(),load.get("academicYear").toString(),load.get("className").toString(),load.get("streamName").toString());

    }

    @RequestMapping("/{schoolName}/getSavedmarks")
    public Map studentSubjectMark(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        System.out.println(load);
        return marks.checkMarks(load.get("examcode").toString(),load.get("subjectName").toString(),load.get("adm").toString());

    }

    @RequestMapping(
            value = "/{schoolName}/marksSave",
            method = RequestMethod.POST)
    public Map saveMarks(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        System.out.println(load);
        return marks.marksSaver(load.get("adm").toString(),load.get("className").toString(),load.get("streamName").toString(),load.get("subjectName").toString(),load.get("termName").toString(),load.get("academicYear").toString(),Double.valueOf(load.get("score").toString()),Double.valueOf(load.get("outOf").toString()),load.get("teacherInitials").toString(),load.get("examName").toString(),load.get("examcode").toString());

    }

    @RequestMapping(
            value = "/{schoolName}/deleteMarks",
            method = RequestMethod.DELETE)
    public Map deleteMarks(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        System.out.println(load);
        return marks.marksDelete(load.get("adm").toString(),load.get("subjectName").toString(),load.get("termName").toString(),load.get("academicYear").toString(),load.get("className").toString(),load.get("streamName").toString(),load.get("examName").toString(),load.get("examcode").toString());

    }


    @RequestMapping(
            value = "/{schoolName}/checkTaskProgress/{taskId}/{task}",
            method = RequestMethod.GET)
    public Map analysisProgress(@PathVariable("taskId") String taskid,@PathVariable("task") String task,@PathVariable("schoolName") String schoolName)
    {
        AnalysisTasks analysisTasks=new AnalysisTasks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        Map map= analysisTasks.getTaskStatus(taskid,task);
        System.out.println("This This is tghe map returned:"+map);
        return map ;

    }
    @RequestMapping(
            value = "/{schoolName}/checkmarks/{examCode}",
            method = RequestMethod.GET)
    public Map checkMarks(@PathVariable("examCode") String examCode,@PathVariable("schoolName") String schoolName)
    {
        ExamAnalysis examAnalysis=new ExamAnalysis(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return examAnalysis.isAnalysable(examCode);

    }

    @RequestMapping(
            value = "/{schoolName}/checkWhetherMarksAreCombinable",
            method = RequestMethod.POST)
    public Map checkWhetherMarksAreCombinable(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        ExamCombination examCombination=new ExamCombination(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return examCombination.isCombinable(load.get("className").toString(),load.get("academicYear").toString(),load.get("termName").toString());

    }



    @RequestMapping(
            value = "/{schoolName}/examAnalysis",
            method = RequestMethod.POST)
    public Map analysMarks(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {


     Map map=new HashMap();
     new Thread(){
         @Override
         public void run() {
             ExamAnalysis examAnalysis=new ExamAnalysis(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

             examAnalysis.analyseExam(load.get("subjectAnalysisMode").toString(),load.get("positionAllocationMode").toString(),load.get("meanGradeAllocationMode").toString(),load.get("examCode").toString(),load.get("academicYear").toString(),load.get("termName").toString(),load.get("className").toString(),load.get("examName").toString(),load.get("analysisId").toString());

         }
     }.start();
        map.put("responseCode","200");
        map.put("responseDescription","Analysis Started");
       return map;
    }


    @RequestMapping(
            value = "/{schoolName}/reviewEnteredMarks",
            method = RequestMethod.POST)
    public Map reviewEnteredMarks(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {

        Map map=new HashMap();
        new Thread(){
            @Override
            public void run() {
                EnteredMarksReview enteredMarksReview=new EnteredMarksReview(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

                enteredMarksReview.review(load.get("examCode").toString(),load.get("academicYear").toString(),load.get("analysisId").toString());

            }
        }.start();
        map.put("responseCode","200");
        map.put("responseDescription","Review Started");
        return map;
    }
    @RequestMapping(
            value = "/{schoolName}/combineMarks",
            method = RequestMethod.POST)
    public Map combineMarks(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {


        Map map=new HashMap();
        new Thread(){
            @Override
            public void run() {
                ExamCombination examCombination=new ExamCombination(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

                examCombination.combineExams(load.get("termName").toString(),load.get("academicYear").toString(),load.get("className").toString(),load.get("analysisId").toString(), (Boolean) load.get("reverse"));
            }
        }.start();
        map.put("responseCode","200");
        map.put("responseDescription","Combination Started");
        return map;
    }


    @RequestMapping(
            value = "/{schoolName}/deleteMarksInBatch",
            method = RequestMethod.DELETE)
    public Map deleteMarksInBatch(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Marks marks=new Marks(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return marks.deleteInBatch(load.get("adm").toString(),load.get("className").toString(),load.get("streamName").toString(),load.get("subjectName").toString(),load.get("termName").toString(),load.get("academicYear").toString(),load.get("examName").toString(),load.get("examCode").toString());

    }




    @RequestMapping(
            value = "/{schoolName}/student",
            method = RequestMethod.POST)
    public Map registerStudent(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName)
    {

        StudentReg studentReg=new StudentReg(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        Globals globals=new Globals(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
     String response=   studentReg.register(payload.get("adm").toString(),payload.get("upi").toString(),payload.get("firstName").toString(),payload.get("middleName").toString(),payload.get("lastName").toString(),
                payload.get("gender").toString(),payload.get("dob").toString(),payload.get("doa").toString(),payload.get("classAdmitted").toString(),payload.get("streamAdmitted").toString(),globals.currentTermName(),globals.currentTermName(),
                payload.get("currentClass").toString(),payload.get("currentStream").toString(),payload.get("kcpe").toString(),payload.get("country").toString(),payload.get("county").toString(),payload.get("province").toString(),payload.get("constituency").toString(),payload.get("ward").toString()
                ,payload.get("parent").toString(),payload.get("tel1").toString(),payload.get("tel2").toString(),"C/"+globals.schoolName()+"/StudentImages",payload.get("program").toString());
System.out.println("Response Sent back:"+response);
     Map map=new HashMap();
     if(response.equalsIgnoreCase("success"))
     {
        map.put("responseCode","200");
        map.put("responseDescription","Student Saved Successfully");
        return map;
     }
     else{
         map.put("responseCode","501");
         map.put("responseDescription","Error: "+response);
         return map;
     }

    }


    @RequestMapping(
            value = "/{schoolName}/increamentGrade",
            method = RequestMethod.POST)
    public Map increamentGrade(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName)
    {
        Subject subject=new Subject(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String response=   subject.configureGradingSystem(payload.get("subject").toString(),payload.get("className").toString(),Integer.parseInt(payload.get("increamentalValue").toString()));
        Map map=new HashMap();
        if(response.equalsIgnoreCase("success"))
        {
            map.put("responseCode","200");
            map.put("responseDescription","Student Saved Successfully");
            return map;
        }
        else{
            map.put("responseCode","501");
            map.put("responseDescription","Error: "+response);
            return map;
        }

    }



    @RequestMapping(
            value = "/{schoolName}/allocatestudentsubject",
            method = RequestMethod.POST)
    public Map allocateSubject(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName)
    {
        Subject subject=new Subject(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String response=  subject.allocateSubject(payload.get("className").toString(),payload.get("subjectName").toString(),payload.get("stream").toString(),payload.get("academicYear").toString(),payload.get("adm").toString(),payload.get("mode").toString());
        Map map=new HashMap();
        if(response.contains("success"))
        {
            map.put("responseCode","200");
            map.put("responseDescription","subject Allocation Sucess");
            return map;
        }
        else{
            map.put("responseCode","501");
            map.put("responseDescription","Error: "+response);
            return map;
        }

    }


    @RequestMapping(
            value = "/{schoolName}/deallocatestudentsubject",
            method = RequestMethod.POST)
    public Map deAllocateSubject(@RequestBody Map<String, Object> payload,@PathVariable("schoolName") String schoolName)
    {
        Subject subject=new Subject(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String response=  subject.deAllocateSubject(payload.get("className").toString(),payload.get("subjectName").toString(),payload.get("stream").toString(),payload.get("academicYear").toString(),payload.get("adm").toString(),payload.get("mode").toString());
        Map map=new HashMap();
        if(response.contains("success"))
        {
            map.put("responseCode","200");
            map.put("responseDescription","subject Allocation Sucess");
            return map;
        }
        else{
            map.put("responseCode","501");
            map.put("responseDescription","Error: "+response);
            return map;
        }

    }

    @RequestMapping(
            value = "/{schoolName}/tabulatesubjectScore",
            method = RequestMethod.POST)

    public Map tabulateScore(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Subject subject=new Subject(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return    subject.performanceIndicator(load.get("className").toString(),load.get("subjectName").toString(),load.get("examcode").toString(),Double.valueOf(load.get("score").toString()),Double.valueOf(load.get("outOf").toString()));



    }

    @RequestMapping(value= "/{schoolName}/teacher/{teacherNumber}",method = RequestMethod.GET)
    public Map teacherInformation(@PathVariable("schoolName") String schoolName,@PathVariable("teacherNumber") String teacherNumber)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.teacherDetails(teacherNumber);
    }
    @RequestMapping(value= "/{schoolName}/searchteacher/{searchValue}",method = RequestMethod.GET)
    public ArrayList searchTeachers(@PathVariable("schoolName") String schoolName,@PathVariable("searchValue") String searchValue)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.searchTeachers(searchValue);
    }
    @RequestMapping(value= "/{schoolName}/teacher/{teacherNumber}",method = RequestMethod.PUT)
    public Map editTeacherDetails(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName,@PathVariable("teacherNumber") String teacherNumber)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.editTeacherDetails(teacherNumber,load.get("firstName").toString(),load.get("middleName").toString(),load.get("lastName").toString(),load.get("gender").toString(),load.get("initials").toString(),load.get("teacherNumber").toString(),load.get("tel").toString(),load.get("email").toString());

    }
    @RequestMapping(value= "/{schoolName}/teacher/{teacherNumber}",method = RequestMethod.DELETE)
    public Map deleteTeacherDetails(@PathVariable("schoolName") String schoolName,@PathVariable("teacherNumber") String teacherNumber)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.deleteTeacher(teacherNumber);

    }

    @RequestMapping(value = "/{schoolName}/teachers",method = RequestMethod.GET)
    public ArrayList getTeachers(@PathVariable("schoolName") String schoolName)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.getAllTeachers();

    }

    @RequestMapping(value = "/{schoolName}/teacher",method = RequestMethod.POST)
    public Map registerTeacher(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        Teacher teacher=new Teacher(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return teacher.registerTeacher(load.get("firstName").toString(),load.get("middleName").toString(),load.get("lastName").toString(),load.get("gender").toString(),load.get("initials").toString(),load.get("teacherNumber").toString(),load.get("tel").toString(),load.get("email").toString());

    }
    @RequestMapping(value = "/{schoolName}/messageParents",method = RequestMethod.POST)
    public Map smsParents(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return sMS.smsClass(load.get("className").toString(),load.get("message").toString());

    }

    @RequestMapping(value = "/{schoolName}/messageunknown",method = RequestMethod.POST)
    public Map smsunknown(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return sMS.oneForeignMessageQueue(load.get("phone").toString(),load.get("message").toString());

    }
    @RequestMapping(value = "/{schoolName}/smsResults",method = RequestMethod.POST)
    public Map smsExamResults(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

       return sMS.smsExamToParents(load.get("academicYear").toString(),load.get("className").toString(),load.get("termName").toString(),load.get("examName").toString(),load.get("examCode").toString(),load.get("admissionNumber").toString(), (Boolean) load.get("smsFee"));

    }




    @RequestMapping(value = "/{schoolName}/phoneBook",method = RequestMethod.POST)
    public ArrayList getPhoneBook(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        PhoneBook phoneBook=new PhoneBook(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return phoneBook.getPhoneBook(load.get("className").toString(),load.get("contact").toString());

    }

    @RequestMapping(value = "/{schoolName}/editphoneBook",method = RequestMethod.POST)
    public Map editPhoneBook(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        PhoneBook phoneBook=new PhoneBook(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return phoneBook.editPhoneBook(load.get("tel1").toString(),load.get("tel2").toString(),load.get("admissionNumber").toString());

    }


    @RequestMapping(value = "/{schoolName}/phoneBook/{search}",method = RequestMethod.GET)
    public ArrayList searchPhoneBook(@PathVariable("schoolName") String schoolName,@PathVariable("search") String search)
    {
        PhoneBook phoneBook=new PhoneBook(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return phoneBook.searchPhoneBook(search);

    }





    @RequestMapping(value= "/{schoolName}/changeUserStatus/{userNumber}",method = RequestMethod.PUT)
    public Map userStatusChange(@PathVariable("schoolName") String schoolName,@PathVariable("userNumber") String userNumber)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.changeUserStatus(userNumber);

    }
    @RequestMapping(value= "/{schoolName}/searchUsers/{searchValue}",method = RequestMethod.GET)
    public ArrayList searchuser(@PathVariable("schoolName") String schoolName,@PathVariable("searchValue") String searchValue)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.getAllUsers(searchValue);

    }
    @RequestMapping(value= "/{schoolName}/passwordReset/{userNumber}",method = RequestMethod.PUT)
    public Map resetPassword(@PathVariable("schoolName") String schoolName,@PathVariable("userNumber") String userNumber)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return  user.resetAccountPassword(userNumber);
    }
    @RequestMapping(value= "/{schoolName}/user/{userNumber}",method = RequestMethod.DELETE)
    public Map deleteUserDetails(@PathVariable("schoolName") String schoolName,@PathVariable("userNumber") String userNumber)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.deleteUser(userNumber);


    }

    @RequestMapping(value = "/{schoolName}/users",method = RequestMethod.GET)
    public ArrayList getUsers(@PathVariable("schoolName") String schoolName)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.getAllUsers();

    }

    @RequestMapping(value = "/{schoolName}/user",method = RequestMethod.POST)
    public Map registerUser(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.registerUser(load.get("userName").toString(),load.get("level").toString(),load.get("user").toString());

    }



    @RequestMapping(value= "/{schoolName}/user/passwordChange/{userNumber}",method = RequestMethod.PUT)
    public Map changePassword(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName,@PathVariable("userNumber") String userName)
    {
        User user=new User(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return user.changeUserCredentials(userName,load.get("currentPassword").toString(),load.get("newUserName").toString(),load.get("newPassword").toString());

    }



//    @RequestMapping(value= "/{schoolName}/userRights/{user}",method = RequestMethod.GET)
//    public ArrayList searchuser(@PathVariable("schoolName") String schoolName,@PathVariable("user") String user)
//    {
//        DbConnection.HOST_DB=EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString();
//       // return User.userRights(user);
//        return  null;
//    }


    @RequestMapping(value = "/{schoolName}/migrateTerm",method = RequestMethod.GET)
    public Map termMigration(@PathVariable("schoolName") String schoolName)
    {
        TermMigration termMigration=new TermMigration(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return termMigration.migrateTerm();

    }



    @RequestMapping(value = "/{schoolName}/migrateClass",method = RequestMethod.POST)
    public Map classMigration(@RequestBody Map<String, Boolean> load,@PathVariable("schoolName") String schoolName)
    {
        ClassMigration classMigration=new ClassMigration(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return classMigration.MigrateClass(load.get("form1"),load.get("form2"),load.get("form3"));

    }



    @PostMapping("/{schoolName}/studentImage/{admissionNumber}")
    public Map saveStudentImage(MultipartFile filename,@PathVariable("schoolName") String schoolName,@PathVariable("admissionNumber") String admissionNumber){
        ConfigurationIntialiser configurationIntialiser=new ConfigurationIntialiser(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String destinationfilename = configurationIntialiser.imageFolder()+"/" +admissionNumber+".jpg";

        File fileout=new File(destinationfilename);

        String fileName = filename.getOriginalFilename();
        fileName=fileName.substring(fileName.lastIndexOf("/"));System.out.println("gsgsg:"+fileName);
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;
        try {

            file = File.createTempFile(fileName.substring(fileName.lastIndexOf("/")), prefix);
           filename.transferTo(file);
            FileUtils.copyFile(file, fileout);


            Map map=new HashMap();
            map.put("responseCode","200");
            map.put("responseDescription","Student Details Saved SuccessFully");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map map=new HashMap();
            map.put("responseCode","501");
            map.put("responseDescription","Unable To Save Student Image \nAn Error Occured:"+e.getMessage());
            return map;

        } finally {
            // After operating the above files, you need to delete the temporary files generated in the root directory
            File f = new File(file.toURI());
            f.delete();




    }}



    @PostMapping("/{schoolName}/schoolLogoUpload")
    public Map saveSchoolLogo(MultipartFile filename,@PathVariable("schoolName") String schoolName){
ConfigurationIntialiser configurationIntialiser=new ConfigurationIntialiser(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        String destinationfilename = configurationIntialiser.imageFolder()+"logo.jpg";

        File fileout=new File(destinationfilename);

        String fileName = filename.getOriginalFilename();
        fileName=fileName.substring(fileName.lastIndexOf("/"));
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;
        try {

            file = File.createTempFile(fileName.substring(fileName.lastIndexOf("/")), prefix);
            filename.transferTo(file);
            FileUtils.copyFile(file, fileout);


            Map map=new HashMap();
            map.put("responseCode","200");
            map.put("responseDescription","Logo Udpated");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map map=new HashMap();
            map.put("responseCode","501");
            map.put("responseDescription","Unable To Save Logo \nAn Error Occured:"+e.getMessage());
            return map;

        } finally {
            // After operating the above files, you need to delete the temporary files generated in the root directory
            File f = new File(file.toURI());
            f.delete();




        }}




    @RequestMapping("/{schoolName}/examweight/{exam}/{classname}")
    public ArrayList classExamWeight(@PathVariable("exam") String examName,@PathVariable("classname") String className,@PathVariable("schoolName") String schoolName)
    {
        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return exams.examWeight(examName,className);

    }
    @RequestMapping("/{schoolName}/examweight")
    public ArrayList examWeight(@PathVariable("schoolName") String schoolName)
    {
        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return exams.examWeight();

    }
    @RequestMapping("/{schoolName}/classexamweight/{className}")
    public ArrayList classExamWeight(@PathVariable("schoolName") String schoolName,@PathVariable("className") String className)
    {
        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return exams.classExamWeight(className);

    }
    @RequestMapping("/{schoolName}/examweight/{exam}")
    public ArrayList examWeightPerExam(@PathVariable("exam") String examName,@PathVariable("schoolName") String schoolName)
    {
        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return exams.examWeight(examName);

    }


    @RequestMapping(value= "/{schoolName}/editExamWeight/{examName}/{className}./{weight}",method = RequestMethod.PUT)
    public Map editExamWeight(@PathVariable("schoolName") String schoolName,@PathVariable("className") String className,@PathVariable("examName") String examName,@PathVariable("weight") String weight)

    {

        Exams exams=new Exams(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
return exams.editExamWeight(examName,className,weight);
    }



    @RequestMapping("/{schoolName}/parentNotices")
    public ArrayList parentNotices(@PathVariable("schoolName") String schoolName)
    {
        Notices notices=new Notices(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return notices.parentNotices();

    }



    @RequestMapping("/{schoolName}/schoolDetails")
    public Map getSchoolDetails(@PathVariable("schoolName") String schoolName)
    {

        SchoolInfor schoolInfor=new SchoolInfor(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


        return schoolInfor.schoolDetails();

    }
    @RequestMapping("/{schoolName}/saveSchoolDetails")
    public Map saveSchoolDetails(@RequestBody Map<String, Object> load,@PathVariable("schoolName") String schoolName)
    {


        SchoolInfor schoolInfor=new SchoolInfor(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());


        Map map = schoolInfor.saveSchoolDetails(load.get("schoolName").toString(),load.get("schoolMission").toString(),load.get("schoolVission").toString(),load.get("schoolMotto").toString(),load.get("schoolContact").toString(),load.get("schoolLocation").toString(),load.get("schoolEmail").toString(),load.get("schoolAdress").toString());
        return map;

    }

    @RequestMapping("/{schoolName}/schoollogo")
    public void getSchoolLogo(HttpServletResponse response,@PathVariable("schoolName") String schoolName)
    {
        DocPath docPath=new DocPath(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        String path = docPath.documentFolderPath() + "logo"+".jpg";

        File file=new File(path);
        if(file.exists())
        {

            try {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName()); // for you it would be application/pdf
                if (mimeType == null) mimeType = "application/octet-stream";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                System.out.println("Accessed");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("File Doesnt Exist:"+file.getAbsolutePath());
        }




    }
    @RequestMapping(value = "/{schoolName}/parentNotice",method = RequestMethod.POST)
    public Map registerParentNotice(@PathVariable("schoolName") String schoolName)
    {



        return  null;

    }


    @RequestMapping(value= "/{schoolName}/subjectRights",method = RequestMethod.POST)
    public ArrayList getSubjectRights(@RequestBody Map<String, String> load,@PathVariable("schoolName") String schoolName)
    {
        SubjectRights subjectRights=new SubjectRights(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return subjectRights.getSubjectRights(load.get("className"),load.get("subjectName"),load.get("teacherName"),load.get("streamName"));
    }
    @RequestMapping(value= "/{schoolName}/assignSubjectRights",method = RequestMethod.POST)
    public Map assignSubjectRight(@RequestBody Map<String, String> load,@PathVariable("schoolName") String schoolName)
    {
        SubjectRights subjectRights=new SubjectRights(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return subjectRights.saveSubjectRight(load.get("className"),load.get("subjectName"),load.get("teacherName"),load.get("streamName"),Boolean.valueOf(load.get("clearance")));
    }

    @RequestMapping(value= "/{schoolName}/revockSubjectRights",method = RequestMethod.POST)
    public Map revockSubjectRight(@RequestBody Map<String, String> load,@PathVariable("schoolName") String schoolName)
    { SubjectRights subjectRights=new SubjectRights(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return subjectRights.revockSubjectRight(load.get("className"),load.get("subjectName"),load.get("teacherName"),load.get("streamName"));
    }



    @RequestMapping(
            value = "/{schoolName}/balances/{className}/{streamName}",
            method = RequestMethod.GET)
    public ArrayList getStudentBalances(@PathVariable("schoolName") String schoolName,@PathVariable("className") String className,@PathVariable("streamName") String streamName)
    {
        FeeBalances feeBalances=new FeeBalances(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return feeBalances.getFeeBalances(className,streamName);


    }
    @RequestMapping(
            value = "/{schoolName}/balancesOverFigure/{className}/{streamName}/{amount}",
            method = RequestMethod.GET)
    public ArrayList getStudentBalancesOverFigure(@PathVariable("schoolName") String schoolName,@PathVariable("className") String className,@PathVariable("streamName") String streamName,@PathVariable("amount") double amountOver)
    {
        FeeBalances feeBalances=new FeeBalances(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return feeBalances.getFeeBalancesOverAFigure(className,streamName,amountOver);


    }

    @RequestMapping(
            value = "/{schoolName}/sms/{status}",
            method = RequestMethod.GET)
    public ArrayList getSmses(@PathVariable("schoolName") String schoolName,@PathVariable("status") String status )
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return sMS.getAllSms(status);


    }


    @RequestMapping(
            value = "/{schoolName}/sms/{smsref}",
            method = RequestMethod.DELETE)
    public Map deleteSMS(@PathVariable("schoolName") String schoolName,@PathVariable("smsref") String smsRef )
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return sMS.deleteSms(smsRef);


    }

    @RequestMapping(
            value = "/{schoolName}/smsbalance/{smsUserName}",
            method = RequestMethod.DELETE)
    public Map smsBalance(@PathVariable("schoolName") String schoolName,@PathVariable("smsUserName") String smsUserName )
    {
        SMS sMS=new SMS(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        Map map=new HashMap();
        map.put("balance",sMS.smsBalance());

return map;
    }


    @RequestMapping(
            value = "/{schoolName}/notices/{adressee}",
            method = RequestMethod.GET)
    public ArrayList getNotices(@PathVariable("schoolName") String schoolName,@PathVariable("adressee") String addressee )
    {
        Notices notices=new Notices(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());

        return notices.notices(addressee);


    }

    @RequestMapping("/{schoolName}/mpesaRecords")
    public ArrayList unReadMpesaRecords(@PathVariable("schoolName") String schoolName)
    {

        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.unreadMpesaRecords();

    }
    @RequestMapping("/{schoolName}/allMpesaRecords")
    public ArrayList allMpesaRecords(@PathVariable("schoolName") String schoolName)
    {

        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.allMpesaRecords();

    }


    @RequestMapping(value="/{schoolName}/confirmation_url",method = RequestMethod.POST)
    public Map recordMpesaTransaction(@PathVariable("schoolName") String schoolName,@RequestBody Map<String, Object> load)
    {
        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.recordMpesaTransaction(load);
    }

    @RequestMapping(value="/{schoolName}/result_url",method = RequestMethod.POST)
    public Map recordMpesaTransactionStatus(@PathVariable("schoolName") String schoolName,@RequestBody Map<String, Object> load)
    {
        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.recordMpesaTransactionStatus(load);
    }


    @RequestMapping(value="/{schoolName}/transactionStatus_url",method = RequestMethod.POST)
    public Map mpesaTransactionStatus(@PathVariable("schoolName") String schoolName,@RequestBody Map<String, Object> load)
    {
        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.retrieveTransactionStatus(load.get("mpesaCode").toString());
    }

    @RequestMapping(value="/{schoolName}/mpesaReadStatus",method = RequestMethod.PUT)
    public Map updateMpesaTransactionReadStatus(@PathVariable("schoolName") String schoolName,@RequestBody Map<String, Object> load)
    {
        Mpesa payments=new Mpesa(EnrolledSchools.getSchoolsDetails(schoolName).get("databaseName").toString());
        return payments.updateTransactionReadStatus(load);
    }


}