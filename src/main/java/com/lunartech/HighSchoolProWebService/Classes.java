package com.lunartech.HighSchoolProWebService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.Date;

public class Classes {
    String schoolDb;
    ClassListReport classListReport;
    public Classes(String schoolDb) {
        this.schoolDb=schoolDb;
        classListReport=new ClassListReport(schoolDb);
    }

    public   void classList(String streamName, String className, String category, String gender)
    {
        classListReport.generalClassList(streamName, className, category,gender);
    }


    public   void subjectClassList(String streamName,String className,String subjectCode)
    {
        classListReport.subjectClassList(subjectCode,streamName,className);
    }



}
