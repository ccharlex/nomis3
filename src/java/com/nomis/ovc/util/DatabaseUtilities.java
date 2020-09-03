/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.util;

import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.ovc.dao.DaoUtility;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class DatabaseUtilities 
{
    private static List connectionParameters; 
    String successTag="<label style='color: green; font-size: 14px; font-weight: bold'>";
    String failureTag="<label style='color: red; font-size: 14px; font-weight: bold'>";
   public static void setConnectionParameters(List connectionParameters)
   {
       DatabaseUtilities.connectionParameters=connectionParameters;
   }
   public boolean revertHivUnknownDueToRiskAssessmentToBaselineHivStatusInChildEnrollment()
    {
        boolean executed=false;
        try
        {
            String tableName="CHILDENROLLMENT";
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                String updateScript="UPDATE APP.CHILDENROLLMENT SET CURRENTHIVSTATUS=BASELINEHIVSTATUS,DATEOFCURRENTHIVSTATUS=DATEOFBASELINEHIVSTATUS WHERE CURRENTHIVSTATUS="+AppConstant.HIV_TEST_REQUIRED_NUM;
                int updateSuccess=util.updateDatabase(updateScript); 
                
                if(updateSuccess==1)
                executed=true;
                    
            }
            
        }       
        catch(Exception ex)
        {
            System.err.println("Error: Unable to update CHILDENROLLMENT table with HIV information. "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public boolean addAgeAtAssessmentToNutritionalAssessmentTable()
    {
        boolean executed=false;
        String tableName="NUTRITIONASSESSMENT";
        String columnName="AGEATASSESSMENT";
        String columnName2="AGEUNITATASSESSMENT";
        String query="";
        int updateSuccess=0;
        try
        {
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" NUMERIC(2) NOT NULL DEFAULT 0";
                    updateSuccess=util.updateDatabase(query);  
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName2+" NUMERIC(1) NOT NULL DEFAULT 0";
                    updateSuccess=util.updateDatabase(query);  
                    createIndexQuery("APP."+tableName,columnName,"idx_na_age");
                    createIndexQuery("APP."+tableName,columnName2,"idx_na_ageu");
                }
                
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+columnName+" on "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public boolean addReferralCompletedToHouseholdReferral()
    {
        boolean executed=false;
        String tableName="HOUSEHOLDREFERRAL";
        String columnName="REFERRALCOMPLETED";
        String query="";
        int updateSuccess=0;
        try
        {
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" NUMERIC(1) NOT NULL DEFAULT 0";
                    updateSuccess=util.updateDatabase(query);  
                    createIndexQuery("APP."+tableName,columnName,"idx_refs_rcom");
                }
                
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+columnName+" on "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public boolean addDateOfStatusToChildEnrollment()
    {
        boolean executed=false;
        String tableName="BENEFICIARYSTATUSUPDATE";
        String columnName="DATEOFSTATUS";
        String query="";
        int updateSuccess=0;
        try
        {
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" DATE NOT NULL DEFAULT '1900-01-01'";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="DateOfCaregiverHivStatus";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" DATE NOT NULL DEFAULT '1900-01-01'";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="caregiverEnrolledOnTreatment";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" NUMERIC(1) NOT NULL DEFAULT 0";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="DateCaregiverEnrolledOnTreatment";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" DATE NOT NULL DEFAULT '1900-01-01'";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="facilityCaregiverEnrolled";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" VARCHAR(11)";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="childTreatmentId";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" VARCHAR(25)";
                    updateSuccess=util.updateDatabase(query);  
                }
                columnName="caregiverTreatmentId";
                if(!columnExists(tableName,columnName))
                {
                    query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" VARCHAR(25)";
                    updateSuccess=util.updateDatabase(query);  
                }
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+columnName+" on "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public String createAccessPrivilegeTable()
    {
        String tableName="ACCESSPRIVILEGE";
        String schemaAndTableName="APP."+tableName;
        String message="";
        
        try
        {
            DaoUtility util=new DaoUtility();
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                + "PRIVILEGEID VARCHAR(11) NOT NULL PRIMARY KEY,"
                + "PRIVILEGENAME VARCHAR(100) NOT NULL, PRIVILEGETYPE VARCHAR(25))";
                                                     
                int response=updateDatabase(query);
                if(response==1)
                {
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('001', 'Data entry', 'dataEntry')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('002', 'Add user role', 'userrole')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('003', 'Add User', 'user')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('004', 'Generate reports', 'generateReport')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('005', 'Export data', 'dataExport')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('006', 'Import Data', 'dataImport')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('007', 'Add/Update School', 'schoolSetup')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('008', 'Setup sites', 'siteSetup')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('009', 'Add Vulnerability Status', 'vulnerabilityStatus')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('010', 'Add/Update Organization unit', 'ousetup')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('011', 'Add Beneficiary services', 'beneficiaryservices')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('012', 'Add Community Based Organizations', 'cbo')");
                    util.updateDatabase("INSERT INTO APP.ACCESSPRIVILEGE (PRIVILEGEID, PRIVILEGENAME, PRIVILEGETYPE) VALUES ('013', 'Add Partners', 'partners')");
                }
                message=getMessage(tableName,response);
                System.err.println(message);
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
   public String createIndexesOnHouseholdEnrollmentTable()
   {
       String message="";
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","DATEOFASSESSMENT","idx_hhe_dta");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","ORGANIZATIONUNIT","idx_hhe_ou");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","MARKEDFORDELETE","idx_hhe_mfd");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","CBOID","idx_hhe_cbo");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","PARTNERCODE","idx_hhe_ptc");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","ENROLLMENTID","idx_hhe_enid");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","CURRENTENROLLMENTSTATUS","idx_hhe_cens");
       message=createIndexQuery("APP.HOUSEHOLDENROLLMENT","DATEOFCURRENTSTATUS","idx_hhe_cend");
        return message;
   }
   public String createIndexesOnChildEnrollmentTable()
   {
       String message="";
       message=createIndexQuery("APP.CHILDENROLLMENT","DATEOFENROLLMENT","idx_che_dte");
       message=createIndexQuery("APP.CHILDENROLLMENT","SEX","idx_che_sex");
       message=createIndexQuery("APP.CHILDENROLLMENT","MARKEDFORDELETE","idx_che_mfd");
       message=createIndexQuery("APP.CHILDENROLLMENT","BASELINEHIVSTATUS","idx_che_bhiv");
       message=createIndexQuery("APP.CHILDENROLLMENT","DATEOFBASELINEHIVSTATUS","idx_che_bhvd");
       message=createIndexQuery("APP.CHILDENROLLMENT","VULNERABILITYSTATUS","idx_che_vul");
       message=createIndexQuery("APP.CHILDENROLLMENT","BASELINESCHOOLSTATUS","idx_che_bsch");
       message=createIndexQuery("APP.CHILDENROLLMENT","CURRENTSCHOOLSTATUS","idx_che_csch");
       message=createIndexQuery("APP.CHILDENROLLMENT","DATEOFCURRENTSCHOOLSTATUS","idx_che_cscd");
        return message;
   }
   public String createIndexesOnChildServiceTable()
   {
       String message="";
       message=createIndexQuery("APP.CHILDSERVICE","OVCID","idx_chs_vcid");
       message=createIndexQuery("APP.CHILDSERVICE","SERVICEDATE","idx_chs_sdt");
       message=createIndexQuery("APP.CHILDSERVICE","MARKEDFORDELETE","idx_chs_mfd");
       message=createIndexQuery("APP.CHILDSERVICE","STABLESERVICES","idx_chs_stb");
       message=createIndexQuery("APP.CHILDSERVICE","HEALTHSERVICES","idx_chs_hlt");
       message=createIndexQuery("APP.CHILDSERVICE","SAFETYSERVICES","idx_chs_sft");
       message=createIndexQuery("APP.CHILDSERVICE","SCHOOLEDSERVICES","idx_chs_sch");
       message=createIndexQuery("APP.CHILDSERVICE","AGEATSERVICE","idx_chs_age");
                
        return message;
   }
   public String createNutritionalStatusTable()
    {
        String tableName="NUTRITIONSTATUS";
        String schemaAndTableName="APP."+tableName;
        String message="";
        
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                        + "OVCID VARCHAR(25)  NOT NULL PRIMARY KEY, "
                        + "CURRENTNUTRITIONSTATUS NUMERIC(1) NOT NULL DEFAULT 0, "
                        + "DATEOFCURRENTNUTRITIONSTATUS DATE NOT NULL,"
                        + "LASTMODIFIEDDATE DATE NOT NULL,"
                        + "MARKEDFORDELETE NUMERIC(1) NOT NULL DEFAULT 0)";
                                                                             
                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"CURRENTNUTRITIONSTATUS","idx_nut_cns");
                message=createIndexQuery(schemaAndTableName,"DATEOFCURRENTNUTRITIONSTATUS","idx_nut_dtns");
                
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
    public String createDatimReportTable()
    {
        String tableName="DATIMREPORT";
        String schemaAndTableName="APP."+tableName;
        String message="";
        
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                        + "RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY, "
                        + "LEVEL2OU VARCHAR(100) NOT NULL, LEVEL3OU VARCHAR(100) NOT NULL, CBO VARCHAR(100) NOT NULL, "
                        + "DPERIOD VARCHAR(50) NOT NULL, OVC_SERVLESSTHAN1 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "OVC_SERV1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, OVC_SERV5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "OVC_SERV1TO9 NUMERIC(11) DEFAULT 0  NOT NULL, M10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "M15TO17 NUMERIC(11) DEFAULT 0  NOT NULL, M18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "M25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, F1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "F5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, F10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "F15TO17 NUMERIC(11) DEFAULT 0  NOT NULL, F18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "F25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, OVC_SERV NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "MALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, FEMALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "GRANDTOTAL NUMERIC(11) DEFAULT 0  NOT NULL, HIV_STATNUMERATOR NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "TOTALPOSITIVE NUMERIC(11) DEFAULT 0  NOT NULL, ENROLLEDONART NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "NOTENROLLEDONART NUMERIC(11) DEFAULT 0  NOT NULL, TOTALNEGATIVE NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "TOTALUNKNOWN NUMERIC(11) DEFAULT 0  NOT NULL, TESTNOTINDICATED NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "OTHERREASONS NUMERIC(11) DEFAULT 0  NOT NULL, OVC_SERVNUMERATOR NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "OVC_SERVACTIVE NUMERIC(11) DEFAULT 0  NOT NULL, OVC_SERVGRADUATED NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "OVC_SERVTRANSFERED NUMERIC(11) DEFAULT 0  NOT NULL, OVC_SERVEXITEDWITHOUTGRADUATION NUMERIC(11) DEFAULT 0  NOT NULL, "
                        + "DATECREATED DATE DEFAULT '1900-01-01'  NOT NULL, LASTMODIFIEDDATE DATE DEFAULT '1900-01-01'  NOT NULL, "
                        + "USERNAME VARCHAR(100) NOT NULL, PARTNERCODE VARCHAR(25), PARTNERNAME VARCHAR(200))";
                                                     
                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"LEVEL2OU","idx_dtm_l2ou");
                message=createIndexQuery(schemaAndTableName,"LEVEL3OU","idx_dtm_l3ou");
                message+=createIndexQuery(schemaAndTableName,"CBO","idx_dtm_cbo");
                message+=createIndexQuery(schemaAndTableName,"DPERIOD","idx_dtm_prd");
                
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
    public String createChildStatusIndexTable()
    {
        String tableName="CHILDSTATUSINDEX";
        String schemaAndTableName="APP."+tableName;
        String message="";
        
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                        + "ID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY, "
                        + "OVCID VARCHAR(25) NOT NULL, CSIDATE DATE, emotionalHealth NUMERIC(1) DEFAULT 0  NOT NULL, "
                        + "socialBehaviour NUMERIC(1) DEFAULT 0  NOT NULL, foodSecurity NUMERIC(1) DEFAULT 0  NOT NULL, "
                        + "nutritionAndGrowth NUMERIC(1) DEFAULT 0  NOT NULL, wellness NUMERIC(1) DEFAULT 0  NOT NULL, "
                        + "healthCareServices NUMERIC(1) DEFAULT 0  NOT NULL, developmentAndPerformance NUMERIC(1) DEFAULT 0 , "
                        + "educationAndWork NUMERIC(1) DEFAULT 0  NOT NULL, abuseAndExploitation NUMERIC(1) DEFAULT 0  NOT NULL, "
                        + "legalProtection NUMERIC(1) DEFAULT 0  NOT NULL, shelter NUMERIC(1) DEFAULT 0  NOT NULL, "
                        + "care NUMERIC(1) DEFAULT 0  NOT NULL, ASSESSMENTNUMBER NUMERIC(2) DEFAULT 0  NOT NULL, "
                        + "LASTMODIFIEDDATE DATE, TOTALCSISCORE NUMERIC(3) DEFAULT 0  NOT NULL,MARKEDFORDELETE NUMERIC(1) DEFAULT 0  NOT NULL)";
                                                     
                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"OVCID","idx_csi_ovcid");
                message=createIndexQuery(schemaAndTableName,"CSIDATE","idx_csi_csidt");
                message+=createIndexQuery(schemaAndTableName,"emotionalHealth","idx_csi_emh");
                message+=createIndexQuery(schemaAndTableName,"socialBehaviour","idx_csi_sbh");
                message+=createIndexQuery(schemaAndTableName,"foodSecurity","idx_csi_fsec");
                message+=createIndexQuery(schemaAndTableName,"nutritionAndGrowth","idx_csi_ngrt");
                message+=createIndexQuery(schemaAndTableName,"wellness","idx_csi_wel");
                message+=createIndexQuery(schemaAndTableName,"healthCareServices","idx_csi_hlt");
                
                message+=createIndexQuery(schemaAndTableName,"developmentAndPerformance","idx_csi_dev");
                message+=createIndexQuery(schemaAndTableName,"educationAndWork","idx_csi_eaw");
                message+=createIndexQuery(schemaAndTableName,"abuseAndExploitation","idx_csi_abu");
                message+=createIndexQuery(schemaAndTableName,"legalProtection","idx_csi_leg");
                message+=createIndexQuery(schemaAndTableName,"shelter","idx_csi_shlt");
                message+=createIndexQuery(schemaAndTableName,"care","idx_csi_care");
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }//CREATE TABLE ChildStatusIndex (ID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY, OVC_ID VARCHAR(25) DEFAULT ' '  NOT NULL, CSI_DATE DATE, CSI_FACTOR1 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR2 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR3 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR4 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR5 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR6 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR7 INTEGER DEFAULT 0 , CSI_FACTOR8 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR9 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR10 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR11 INTEGER DEFAULT 0  NOT NULL, CSI_FACTOR12 INTEGER DEFAULT 0  NOT NULL, SURVEY_NUMBER INTEGER DEFAULT 0  NOT NULL, DATEOFENTRY DATE, TOTALCSISCORE NUMERIC(3) DEFAULT 0  NOT NULL);
    public String createCustomsIndicatorsReportTable()
    {
        String tableName="CUSTOMINDICATORSREPORT";
        String schemaAndTableName="APP."+tableName;
        String message="";
        
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                +"RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY,"
                +"LEVEL2OUID VARCHAR(11) NOT NULL, LEVEL3OUID VARCHAR(11) NOT NULL, LEVEL4OUID VARCHAR(11) NOT NULL,"
                +" CBOID VARCHAR(11) NOT NULL, PARTNERCODE VARCHAR(11), MERCODE VARCHAR(20) NOT NULL, "
                +"INDICATORNAME VARCHAR(200) NOT NULL, OTHERDISAGGREGATION VARCHAR(50) NOT NULL, "
                +"REPORTPERIOD VARCHAR(20) NOT NULL, MALELESSTHAN1 NUMERIC(11) DEFAULT 0  NOT NULL, "
                + "MALE1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, MALE5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"MALE10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, MALE15TO17 NUMERIC(11) DEFAULT 0  NOT NULL,"
                +"MALE18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, MALE25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"FEMALELESSTHAN1 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"FEMALE5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"FEMALE15TO17 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"FEMALE25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, MALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"FEMALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, GRANDTOTAL NUMERIC(11) DEFAULT 0  NOT NULL, "
                +"DATECREATED DATE DEFAULT '1900-01-01'  NOT NULL, USERNAME VARCHAR(100) NOT NULL)";
                
                                                      
                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"LEVEL2OUID","idx_cir_l2ou");
                message+=createIndexQuery(schemaAndTableName,"LEVEL3OUID","idx_cir_l3ou");
                message+=createIndexQuery(schemaAndTableName,"CBOID","idx_cir_cbo");
                message+=createIndexQuery(schemaAndTableName,"PARTNERCODE","idx_cir_pcd");
                message+=createIndexQuery(schemaAndTableName,"MERCODE","idx_cir_mer");
                message+=createIndexQuery(schemaAndTableName,"INDICATORNAME","idx_cir_ind");
                message+=createIndexQuery(schemaAndTableName,"REPORTPERIOD","idx_cir_prd");
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }//CREATE TABLE APP.CUSTOMINDICATORSREPORT2 (RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment  by 1)  NOT NULL PRIMARY KEY, LEVEL2OUID VARCHAR(11) NOT NULL, LEVEL3OUID VARCHAR(11) NOT NULL, LEVEL4OUID VARCHAR(11) NOT NULL, CBOID VARCHAR(11) NOT NULL, PARTNERCODE VARCHAR(11), MERCODE VARCHAR(20) NOT NULL, INDICATORNAME VARCHAR(200) NOT NULL, OTHERDISAGGREGATION VARCHAR(50) NOT NULL, "PERIOD" VARCHAR(20) NOT NULL, MALELESSTHAN1 NUMERIC(11) DEFAULT 0  NOT NULL, MALE1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, MALE5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, MALE10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, MALE15TO17 NUMERIC(11) DEFAULT 0  NOT NULL, MALE18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, MALE25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, FEMALELESSTHAN1 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE1TO4 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE5TO9 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE10TO14 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE15TO17 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE18TO24 NUMERIC(11) DEFAULT 0  NOT NULL, FEMALE25PLUS NUMERIC(11) DEFAULT 0  NOT NULL, MALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, FEMALETOTAL NUMERIC(11) DEFAULT 0  NOT NULL, GRANDTOTAL NUMERIC(11) DEFAULT 0  NOT NULL, DATECREATED DATE DEFAULT '1900-01-01'  NOT NULL, USERNAME VARCHAR(100) NOT NULL);
    public String updateBaselineAndCurrentAgeUnitsInChildEnrollment()
    {
        String query="update APP.CHILDENROLLMENT SET AGEUNITATBASELINE=2, CURRENTAGEUNIT=2 WHERE AGEUNITATBASELINE=0";
        int result=updateDatabase(query);
        String message=result+"Child age units updated. ";
        System.err.println(message);
        return message;
    }
    public String updateEnrollmentStatusHistory()
    {
        String query="update APP.ENROLLMENTSTATUSHISTORY SET CURRENTAGEUNIT=2 WHERE CURRENTAGEUNIT=0";
        int result=updateDatabase(query);
        String message=result+"Current age unit updated in enrollment status history. ";
        System.err.println(message);
        return message;
    }
    public String updateCurrentEnrollmentStatus()
    {
        String query="update APP.CHILDENROLLMENT SET CURRENTENROLLMENTSTATUS=1 WHERE CURRENTENROLLMENTSTATUS=0";
        int result=updateDatabase(query);
        query="update APP.ADULTHOUSEHOLDMEMBER SET CURRENTENROLLMENTSTATUS=1 WHERE CURRENTENROLLMENTSTATUS=0";
        int adultResult=result=updateDatabase(query);
        String message=result+"Child current enrollment status updated; "+adultResult+"Adult current enrollment status updated; ";
        System.err.println(message);
        return message;
    }
    public String createHivStatusHistoryTable()
    {
        String tableName="HIVSTATUSHISTORY";
        String schemaAndTableName="APP."+tableName;
        String message="";
        String processId="hivhistytbl";
        String processName="Hiv Status History table created";
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                +"RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY,"
                +"BENEFICIARYID VARCHAR(25) NOT NULL, NEWHIVSTATUS NUMERIC(1) DEFAULT 0  NOT NULL, DATEOFNEWSTATUS DATE NOT NULL, "
                +"ENROLLEDONTREATMENT NUMERIC(1) DEFAULT 0  NOT NULL, FACILITYID VARCHAR(11), DATECREATED DATE NOT NULL,"
                +"LASTMODIFIEDDATE DATE NOT NULL, RECORDEDBY VARCHAR(25) NOT NULL, BENEFICIARYTYPE NUMERIC(1) DEFAULT 0  NOT NULL,"
                +"POINTOFUPDATE NUMERIC(1) DEFAULT 0  NOT NULL)";
                                                      
                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"BENEFICIARYID","idx_hsh_bid");
                message+=createIndexQuery(schemaAndTableName,"NEWHIVSTATUS","idx_hsh_nhs");
                message+=createIndexQuery(schemaAndTableName,"DATEOFNEWSTATUS","idx_hsh_dns");
                message+=createIndexQuery(schemaAndTableName,"ENROLLEDONTREATMENT","idx_hsh_trmt");
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
    public String createEnrollmentStatusHistoryTable()
    {
        String tableName="ENROLLMENTSTATUSHISTORY";
        String schemaAndTableName="APP."+tableName;
        String message="";
        String processId="enrhistytbl";
        String processName="Enrollment Status History table created";
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                +"RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY,"
                +"BENEFICIARYID VARCHAR(25) NOT NULL, ENROLLMENTSTATUS NUMERIC(2) DEFAULT 0  NOT NULL, DATEOFENROLLMENTSTATUS DATE NOT NULL, "
                +"HIVSTATUS NUMERIC(1) DEFAULT 0  NOT NULL, DATEOFHIVSTATUS DATE NOT NULL, ENROLLEDONTREATMENT NUMERIC(1) DEFAULT 0  NOT NULL, FACILITYID VARCHAR(11), DATECREATED DATE NOT NULL,"
                +"CURRENTAGE NUMERIC(3) DEFAULT 0  NOT NULL,CURRENTAGEUNIT VARCHAR(1) NOT NULL, LASTMODIFIEDDATE DATE NOT NULL, RECORDEDBY VARCHAR(25) NOT NULL, BENEFICIARYTYPE NUMERIC(1) DEFAULT 0  NOT NULL,"
                +"POINTOFUPDATE NUMERIC(1) DEFAULT 0  NOT NULL)";

                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"BENEFICIARYID","idx_esh_bid");
                message+=createIndexQuery(schemaAndTableName,"ENROLLMENTSTATUS","idx_esh_ens");
                message+=createIndexQuery(schemaAndTableName,"DATEOFENROLLMENTSTATUS","idx_esh_des");
                message+=createIndexQuery(schemaAndTableName,"ENROLLEDONTREATMENT","idx_esh_trmt");
                message+=createIndexQuery(schemaAndTableName,"HIVSTATUS","idx_esh_hivs");
                message+=createIndexQuery(schemaAndTableName,"DATEOFHIVSTATUS","idx_esh_dhiv");
                message+=createIndexQuery(schemaAndTableName,"CURRENTAGE","idx_esh_cage");
                message+=createIndexQuery(schemaAndTableName,"CURRENTAGEUNIT","idx_esh_ageu");
                System.err.println(message);
                //createIndexOnHivStatusTables();
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
    public String createQuarterlyStatusTrackerTable()
    {
        String tableName="QUARTERLYSTATUSTRACKER";
        String schemaAndTableName="APP."+tableName;
        String message="";
        String processId="qstrackrtbl";
        String processName="Quarterly Status tracker table created";
        try
        {
            if(!tableExists(tableName))
            {
                String query="CREATE TABLE "+schemaAndTableName+"("
                +"RECORDID INTEGER GENERATED ALWAYS AS IDENTITY(start with 1, increment by 1)  NOT NULL PRIMARY KEY,"
                +"BENEFICIARYID VARCHAR(25) NOT NULL, ENROLLMENTSTATUS NUMERIC(2) DEFAULT 0  NOT NULL, DATEOFENROLLMENTSTATUS DATE NOT NULL, "
                +"HIVSTATUS NUMERIC(1) DEFAULT 0  NOT NULL, DATEOFHIVSTATUS DATE NOT NULL, ENROLLEDONTREATMENT NUMERIC(1) DEFAULT 0  NOT NULL, FACILITYID VARCHAR(11), DATECREATED DATE NOT NULL,"
                +"CURRENTAGE NUMERIC(3) DEFAULT 0  NOT NULL,CURRENTAGEUNIT NUMERIC(1) NOT NULL DEFAULT 0, LASTMODIFIEDDATE DATE NOT NULL, RECORDEDBY VARCHAR(25) NOT NULL, BENEFICIARYTYPE NUMERIC(1) DEFAULT 0  NOT NULL,"
                +"POINTOFUPDATE NUMERIC(1) DEFAULT 0  NOT NULL,REPORTQUARTER VARCHAR(6) NOT NULL,AUTOGEN NUMERIC(1) DEFAULT 0  NOT NULL)";

                int response=updateDatabase(query);
                message=getMessage(tableName,response);
                System.err.println(message);
                message=createIndexQuery(schemaAndTableName,"BENEFICIARYID","idx_qst_bnid");
                message+=createIndexQuery(schemaAndTableName,"ENROLLMENTSTATUS","idx_qst_ens");
                message+=createIndexQuery(schemaAndTableName,"DATEOFENROLLMENTSTATUS","idx_qst_des");
                message+=createIndexQuery(schemaAndTableName,"ENROLLEDONTREATMENT","idx_qst_trmt");
                message+=createIndexQuery(schemaAndTableName,"HIVSTATUS","idx_qst_hivs");
                message+=createIndexQuery(schemaAndTableName,"DATEOFHIVSTATUS","idx_qst_dhiv");
                message+=createIndexQuery(schemaAndTableName,"CURRENTAGE","idx_qst_cage");
                message+=createIndexQuery(schemaAndTableName,"CURRENTAGEUNIT","idx_qst_ageu");
                message+=createIndexQuery(schemaAndTableName,"REPORTQUARTER","idx_qst_rqt");
                System.err.println(message);
            }  
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            //return false;
        }
        return message;
    }
    public boolean addTreatmentIdToChildEnrollment()
    {
        boolean executed=false;
        String tableName="CHILDENROLLMENT";
        String columnName="TREATMENTID";
        try
        {
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,columnName))
                {
                    String query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" VARCHAR(25)";
                    int updateSuccess=util.updateDatabase(query);  
                }
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+columnName+" on "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
    public boolean addTreatmentIdToAdultHouseholdMember()
    {
        boolean executed=false;
        String tableName="ADULTHOUSEHOLDMEMBER";
        String columnName="TREATMENTID";
        try
        {
            
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,columnName))
                {
                    String query="ALTER TABLE APP."+tableName+" ADD COLUMN "+columnName+" VARCHAR(25)";
                    int updateSuccess=util.updateDatabase(query);  
                }
            }
            
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+columnName+" on "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }//hivTreatmentId
   public boolean addDateOfCurrentSchoolStatusToChildEnrollment()
    {
        boolean executed=false;
        try
        {
            String tableName="CHILDENROLLMENT";
            DaoUtility util=new DaoUtility();
            if(tableExists(tableName))
            {   
                if(!columnExists(tableName,"DATEOFCURRENTSCHOOLSTATUS"))
                {
                    String query="ALTER TABLE APP."+tableName+" ADD COLUMN DATEOFCURRENTSCHOOLSTATUS DATE";
                    int updateSuccess=util.updateDatabase(query); 
                    if(updateSuccess==1)
                    {
                        String updateScript="UPDATE APP.CHILDENROLLMENT SET DATEOFCURRENTSCHOOLSTATUS=DATEOFENROLLMENT";
                        util.updateDatabase(updateScript); 
                        if(updateSuccess==1)
                        executed=true;
                    }
                }
            }
            
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating DATASETSETTING table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }//
   public boolean createDataImportFileUploadTable()
    {
        boolean executed=false;
        String tableName="DATAIMPORTFILEUPLOAD";
        try
        {
            DaoUtility util=new DaoUtility();
            if(!tableExists(tableName))
            {   
                
                String query="CREATE TABLE APP.DATAIMPORTFILEUPLOAD "
                        + "(RECORDID INTEGER GENERATED ALWAYS AS IDENTITY (Start with 1,increment by 1) NOT NULL PRIMARY KEY, "
                        + "IMPORTFILENAME VARCHAR(1000), IMPORTDIRECTORYPATH VARCHAR(1000), USERNAME VARCHAR(25) NOT NULL, "
                        + "PARTNERCODE VARCHAR(11) NOT NULL, DATEOFUPLOAD DATE NOT NULL, DATEIMPORTCOMPLETED DATE NOT NULL, "
                        + "TIMEIMPORTCOMPLETED VARCHAR(25) DEFAULT '00-00-00'  NOT NULL, "
                        + "IMPORTSTATUS NUMERIC(2) DEFAULT 0  NOT NULL, SELECTEDTABLECODES VARCHAR(1000), "
                        + "LASTPROCESSEDTABLEINDEX NUMERIC(3) DEFAULT 0  NOT NULL, IMPORTOPTIONS VARCHAR(1000))";
                int updateSuccess=util.updateDatabase(query); 
                if(updateSuccess==1)
                executed=true;
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating "+tableName+" table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public boolean createDatasetSettingTable()
    {
        boolean executed=false;
        try
        {
            DaoUtility util=new DaoUtility();
            if(!tableExists("DATASETSETTING"))
            {   
                
                String query="CREATE TABLE APP.DATASETSETTING (MODULEID VARCHAR(11) NOT NULL  PRIMARY KEY, DATASETID VARCHAR(11) NOT NULL, LASTMODIFIEDDATE DATE NOT NULL, RECORDEDBY VARCHAR(25) NOT NULL)";
                int updateSuccess=util.updateDatabase(query); 
                if(updateSuccess==1)
                executed=true;
            }
            else if(!columnExists("DATASETSETTING","MODULEID"))
            {
                util.updateDatabase("drop table APP.DATASETSETTING"); 
                createDatasetSettingTable();
            }
        }       
        catch(Exception ex)
        {
            System.err.println("Error creating DATASETSETTING table "+ex.getMessage());
            NomisLogManager.logStackTrace(ex);
            return false;
        }
        return executed;
    }
   public boolean tableExists(String tableName)
    {
        DaoUtility util=new DaoUtility();
        tableName=tableName.toUpperCase();
        boolean tableExists=false;
        String query="SELECT TABLENAME FROM SYS.SYSTABLES WHERE UPPER(TABLENAME)='"+tableName+"'";
        List list=util.execSqlQuery(query);
        if(list !=null && !list.isEmpty())
        {
            tableExists=true;
        }
        return tableExists;
    }
    public boolean columnExists(String tableName,String desiredColumn)
     {
         DaoUtility util=new DaoUtility();
         boolean columnExists=false;
         String columnName=null;
         String query="select c.COLUMNNAME from SYS.SYSTABLES t, SYS.SYSCOLUMNS c WHERE t.TABLEID=c.REFERENCEID AND t.TABLENAME='"+tableName+"' and c.COLUMNNAME='"+desiredColumn+"'";
         List list=util.execSqlQuery(query);
         if(list !=null && !list.isEmpty())
         {
             columnName=(String)list.get(0);
             if(columnName !=null)
             columnExists=true;
             //System.err.println("columnName is "+columnName);
         }
         return columnExists;
     }
    public String createIndexQuery(String tableName,String columnName,String indexName)
   {
       AppUtility appUtil=new AppUtility();
        String message="";
        try
        {
            String processName="index "+indexName+" created on "+tableName+" for "+columnName;
            String query="create index "+indexName+" on "+tableName+"("+columnName+")";
            if(indexName.length()>11)
            indexName=indexName.replace("_", "");
            if(indexName.length()>11)
            indexName=appUtil.generateUniqueId(11);
            int response=updateDatabase(query); 
            /*if(response==0)
            message=failureTag+"Failed to perform action</label>";
            else if(response==1)
            message=successTag+" Successfully created index "+indexName+" on "+columnName+" </label>";
            if(response==2)
            message=failureTag+"An error occured. Unable to create index "+indexName+" on column "+columnName+" </label>";
            System.err.println(message);*/
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return message;
   }
    private int updateDatabase(String query)
   {
       int response=0;
       try
       {
           DaoUtility util=new DaoUtility();
           response=util.updateDatabase(query);
       }
       catch(Exception ex)
       {
           response=2;
           ex.printStackTrace();
       }
       return response;
   }
    private String getMessage(String tableName,int returnValue)
   {
        String message="";
        if(returnValue==0)
        message=failureTag+"Failed to perform action</label>";
        else if(returnValue==1)
        message=successTag+tableName+" table created successfully</label>";
        if(returnValue==2)
        message=failureTag+"An error occured. Unable to create table"+tableName+" </label>";
       return message;
   }
    public boolean executeDatabaseUpdate()
    {
        boolean executed=createDatasetSettingTable();
        addDateOfCurrentSchoolStatusToChildEnrollment();
        addTreatmentIdToChildEnrollment();
        addTreatmentIdToAdultHouseholdMember();
        createDataImportFileUploadTable();
        createQuarterlyStatusTrackerTable();
        createEnrollmentStatusHistoryTable();
        createHivStatusHistoryTable();
        updateCurrentEnrollmentStatus();
        createCustomsIndicatorsReportTable();
        updateBaselineAndCurrentAgeUnitsInChildEnrollment();
        createChildStatusIndexTable();
        createDatimReportTable();
        createIndexesOnHouseholdEnrollmentTable();
        createIndexesOnChildEnrollmentTable();
        createIndexesOnChildServiceTable();
        createNutritionalStatusTable();
        addAgeAtAssessmentToNutritionalAssessmentTable();
        addReferralCompletedToHouseholdReferral();
        //addDateOfStatusToChildEnrollment();
        //createAccessPrivilegeTable();
        return executed;
    }
}
