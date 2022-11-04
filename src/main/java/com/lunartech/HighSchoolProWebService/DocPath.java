package com.lunartech.HighSchoolProWebService;

public class DocPath {
    ConfigurationIntialiser configurationIntialiser;
    String schoolDb;
    public DocPath(String schoolDb) {
        this.schoolDb=schoolDb;
       configurationIntialiser=new ConfigurationIntialiser(schoolDb);
    }

    public     String documentFolderPath()
    {
        return  configurationIntialiser.imageFolder()+"/";
    }
}
