/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lunartech.HighSchoolProWebService;

import com.itextpdf.text.Image;

import javax.swing.*;

/**
 *
 * @author FRED
 */
public class DocHead {
    DocPath docPath;
String schoolDb;
    public DocHead(String schoolDb) {
        this.schoolDb = schoolDb;
        docPath=new DocPath(schoolDb);
    }

    public Image receiptHeader ()
    {Image img=null;
        try {
             img = Image.getInstance(docPath.documentFolderPath()+"ReceiptHeader.png");
             return img;
        } catch (Exception e) {
        }
        return img;
    }
          public ImageIcon trayIcon()
    {
        ImageIcon img=null;
        try {
             img = new ImageIcon(getClass().getResource("/icons/icon.png")); 
             return img;
        } catch (Exception e) {
        }
        return img;
    }
    public Image im()
    {Image img=null;
        try {
             img = Image.getInstance(docPath.documentFolderPath()+"overdocheader.png");
             return img;
        } catch (Exception e) {
        }
        return img;
    }
    public Image reportFormHeader()
    {Image img=null;
        try {
            
             img = Image.getInstance(docPath.documentFolderPath()+"reportlogo.png");
             return img;
        } catch (Exception e) {
        }
        return img;
    }
    
    public ImageIcon framesIcon()
    {
        ImageIcon img=null;
        try {
             img = new ImageIcon(getClass().getResource("/icons/index6.jpg")); 
             return img;
        } catch (Exception e) {
        }
        return img;
    }
    
     public Image reportHead ()
    {Image img=null;
        try {
             img = Image.getInstance(docPath.documentFolderPath()+"reportlogo.png");
             return img;
        } catch (Exception e) {
        }
        return img;
    }
    
}
