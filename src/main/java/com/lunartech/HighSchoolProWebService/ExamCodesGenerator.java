/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lunartech.HighSchoolProWebService;

import javax.swing.*;

/**
 *
 * @author FRED
 */
public class ExamCodesGenerator {
    public static String  generatecode(String classl,String year,String term,String exam)
    {
       if(classl==null)
       {

           return "";
       }
       else{
              if(term==null)
       {

           return "";
       }
       else{
              if(year==null)
       {

           return "";
       }
       else{
          String classpart="FM"+classl.charAt(classl.length()-1);
       String termpart="TM"+term.charAt(term.length()-1);
       
       return classpart+exam+termpart+year; 
       }
       }
       }
       
       
    }
    
}
