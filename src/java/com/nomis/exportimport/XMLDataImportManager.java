/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.exportimport;
import com.nomis.operationsManagement.EnrollmentStatusManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.BeneficiaryStatusUpdate;
import com.nomis.ovc.business.CareAndSupportChecklist;
import com.nomis.ovc.business.CaregiverAccessToEmergencyFund;
import com.nomis.ovc.business.CareplanAchievementChecklist;
import com.nomis.ovc.business.ChildEducationPerformanceAssessment;
import com.nomis.ovc.business.ChildService;
import com.nomis.ovc.business.HivRiskAssessment;
import com.nomis.ovc.business.HouseholdCareplan;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.HouseholdReferral;
import com.nomis.ovc.business.HouseholdService;
import com.nomis.ovc.business.HouseholdVulnerabilityAssessment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.business.RevisedHouseholdVulnerabilityAssessment;
import com.nomis.ovc.dao.AdultHouseholdMemberDao;
import com.nomis.ovc.dao.BeneficiaryStatusUpdateDao;
import com.nomis.ovc.dao.CareAndSupportChecklistDao;
import com.nomis.ovc.dao.CaregiverAccessToEmergencyFundDao;
import com.nomis.ovc.dao.CareplanAchievementChecklistDao;
import com.nomis.ovc.dao.ChildEducationPerformanceAssessmentDao;
import com.nomis.ovc.dao.ChildEnrollmentDao;
import com.nomis.ovc.dao.ChildServiceDao;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.HivRiskAssessmentDao;
import com.nomis.ovc.dao.HouseholdCareplanDao;
import com.nomis.ovc.dao.HouseholdEnrollmentDao;
import com.nomis.ovc.dao.HouseholdReferralDao;
import com.nomis.ovc.dao.HouseholdServiceDao;
import com.nomis.ovc.dao.HouseholdVulnerabilityAssessmentDao;
import com.nomis.ovc.dao.RevisedHouseholdVulnerabilityAssessmentDao;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;

import com.nomis.ovc.util.FileManager;
import com.nomis.ovc.util.TaskManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 *
 * @author smomoh
 */
public class XMLDataImportManager
{
    DaoUtility util=new DaoUtility();
    AppUtility appUtil=new AppUtility();
    List childList=new ArrayList();
    List householdList=new ArrayList();
    List childServiceList=new ArrayList();
    List householdServiceList=new ArrayList();
    String userName=null;
public XMLDataImportManager(String alternateFilePath,String fileName)
{
    ZipHandler zipHandler;
    appUtil=new AppUtility();
    AppUtility.getResourceLocation();
    //AppUtility.setCurrentImportRecordName("Extracting data from zip files...");
    String destinationDirectory=appUtil.getExportFilePath();
    //appUtil.createExportImportDirectories();
    //zipHandler=new ZipHandler();
    //zipHandler.unZipFile(appUtil.getImportFilePath()+appUtil.getFileSeperator()+fileName,destinationDirectory);
    //System.err.println("appUtil.getImportFilePath()+\\+fileName is "+appUtil.getImportFilePath()+appUtil.getFileSeperator()+fileName);
}
public XMLDataImportManager()
{
    ZipHandler zipHandler;
    appUtil=new AppUtility();
    AppUtility.getResourceLocation();
}
public List importHouseholdEnrollmentRecordsFromXML(String destinationDirectory,int hheOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdEnrollments";
    AppUtility.setCurrentImportRecordName("Household enrollment");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Household enrollment records saved");
    duplicateRecordsList.add("Number of Household enrollment records saved as updates");
    if(hheOption==4)
    {
        newRecordsList.add(0);
        duplicateRecordsList.add(0);
        list.add(newRecordsList);
        list.add(duplicateRecordsList);
        return list;
    }
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;//destinationDirectory
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HouseholdEnrollmentDao hhedao=util.getHouseholdEnrollmentDaoInstance();
                    HouseholdEnrollment hhe=null;
                    HouseholdEnrollment existinghhe=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("Household");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            hhe=new HouseholdEnrollment();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String hhUniqueId=getNodeValue("hhUniqueId",s,listOfObjects);
                                hhe.setHhUniqueId(hhUniqueId);
                                hhe.setEnrollmentId(getNodeValue("enrollmentId",s,listOfObjects));
                                hhe.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                hhe.setAddress(getNodeValue("address",s,listOfObjects));
                                hhe.setCboId(getNodeValue("cboId",s,listOfObjects));
                                hhe.setOrganizationUnit(getNodeValue("organizationUnit",s,listOfObjects));
                                hhe.setPartnerCode(getNodeValue("partnerCode",s,listOfObjects));
                                hhe.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                hhe.setCurrentEnrollmentStatus(getIntegerNodeValue(getNodeValue("currentEnrollmentStatus",s,listOfObjects)));
                                hhe.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                hhe.setDateOfCurrentStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentStatus",s,listOfObjects)));
                                hhe.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                hhe.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                hhe.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                hhe.setHhHasCasePlan(getIntegerNodeValue(getNodeValue("hhHasCasePlan",s,listOfObjects)));
                                hhe.setDateCasePlanDeveloped(DateManager.getDateInstance(getNodeValue("dateCasePlanDeveloped",s,listOfObjects)));
                                
                                                                
                                existinghhe=hhedao.getHouseholdEnrollment(hhe.getHhUniqueId());
                                if(existinghhe==null)
                                {
                                    if(hheOption==2 || hheOption==3)
                                    {
                                        count++;
                                        hhedao.saveHouseholdEnrollment(hhe);
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Household enrollment record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(hheOption==1 || hheOption==3)
                                    {
                                        if(existinghhe.getLastModifiedDate().before(hhe.getLastModifiedDate()) || existinghhe.getLastModifiedDate().equals(hhe.getLastModifiedDate()))
                                        {
                                            hhe.setCboId(existinghhe.getCboId());
                                            if(hhe.getAddress()==null)
                                            hhe.setAddress(existinghhe.getAddress());
                                            count++;
                                            hhedao.updateHouseholdEnrollment(hhe);
                                            numberUpdated++;
                                            AppUtility.setCurrentImportRecordName("Household enrollment record: "+count+" saved");
                                        }
                                    }
                                }
                                
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importHouseholdVulnerabilityAssessmentRecordsFromXML(String destinationDirectory,int hheOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdVulnerabilityAssessments";
    AppUtility.setCurrentImportRecordName("Household Vulnerability Assessment");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Household vulnerability assessment records saved");
    duplicateRecordsList.add("Number of Household vulnerability assessment records saved as updates");
    if(hheOption==4)
    {
        newRecordsList.add(0);
        duplicateRecordsList.add(0);
        list.add(newRecordsList);
        list.add(duplicateRecordsList);
        return list;
    }
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;//destinationDirectory
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HouseholdVulnerabilityAssessmentDao hhedao=util.getHouseholdVulnerabilityAssessmentDaoInstance();
                    HouseholdVulnerabilityAssessment hva=null;
                    HouseholdVulnerabilityAssessment existingHva=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("HouseholdAssessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            hva=new HouseholdVulnerabilityAssessment();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String hhUniqueId=getNodeValue("hhUniqueId",s,listOfObjects);
                                hva.setHhUniqueId(hhUniqueId);
                                hva.setId(getIntegerNodeValue(getNodeValue("id",s,listOfObjects)));
                                hva.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                hva.setAssessmentNo(getIntegerNodeValue(getNodeValue("assessmentNo",s,listOfObjects)));
                                hva.setEducationLevel(getIntegerNodeValue(getNodeValue("educationLevel",s,listOfObjects)));
                                hva.setFoodSecurityAndNutrition(getIntegerNodeValue(getNodeValue("foodSecurityAndNutrition",s,listOfObjects)));
                                hva.setHealth(getIntegerNodeValue(getNodeValue("health",s,listOfObjects)));
                                hva.setHhHeadship(getIntegerNodeValue(getNodeValue("hhHeadship",s,listOfObjects)));
                                hva.setHhIncome(getIntegerNodeValue(getNodeValue("hhIncome",s,listOfObjects)));
                                hva.setMeansOfLivelihood(getIntegerNodeValue(getNodeValue("meansOfLivelihood",s,listOfObjects)));
                                hva.setShelterAndHousing(getIntegerNodeValue(getNodeValue("shelterAndHousing",s,listOfObjects)));
                                hva.setVulnerabilityScore(getIntegerNodeValue(getNodeValue("vulnerabilityScore",s,listOfObjects)));
                                hva.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                hva.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                hva.setNameOfAssessor(getNodeValue("nameOfAssessor",s,listOfObjects));
                                hva.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                hva.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                
                                existingHva=hhedao.getHouseholdVulnerabilityAssessment(hva.getHhUniqueId(),hva.getDateOfAssessment());
                                if(existingHva==null)
                                {
                                    if(hheOption==2 || hheOption==3)
                                    {
                                        count++;
                                        hhedao.saveHouseholdVulnerabilityAssessment(hva);
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Household vulnerability assessment record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(hheOption==1 || hheOption==3)
                                    {
                                        if(existingHva.getLastModifiedDate().before(hva.getLastModifiedDate()) || existingHva.getLastModifiedDate().equals(hva.getLastModifiedDate()))
                                        {
                                            count++;
                                            hhedao.updateHouseholdVulnerabilityAssessment(hva);
                                            numberUpdated++;
                                            AppUtility.setCurrentImportRecordName("Household vulnerability assessment record: "+count+" saved");
                                        }
                                    }
                                }
                                
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importRevisedHouseholdVulnerabilityAssessmentRecordsFromXML(String destinationDirectory,int hheOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="RevisedHouseholdVulnerabilityAssessments";
    AppUtility.setCurrentImportRecordName("Revised Household Vulnerability Assessment");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Revised Household vulnerability assessment records saved");
    duplicateRecordsList.add("Number of Revised Household vulnerability assessment records saved as updates");
    if(hheOption==4)
    {
        newRecordsList.add(0);
        duplicateRecordsList.add(0);
        list.add(newRecordsList);
        list.add(duplicateRecordsList);
        return list;
    }
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;//destinationDirectory
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    RevisedHouseholdVulnerabilityAssessmentDao hvadao=util.getRevisedHouseholdVulnerabilityAssessmentDaoInstance();
                    RevisedHouseholdVulnerabilityAssessment hva=null;
                    RevisedHouseholdVulnerabilityAssessment existingHva=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("HouseholdAssessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            hva=new RevisedHouseholdVulnerabilityAssessment();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String hhUniqueId=getNodeValue("hhUniqueId",s,listOfObjects);
                                hva.setHhUniqueId(hhUniqueId);
                                hva.setId(getIntegerNodeValue(getNodeValue("id",s,listOfObjects)));
                                hva.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                hva.setCgEngagedInEconomicActivities(getIntegerNodeValue(getNodeValue("cgEngagedInEconomicActivities",s,listOfObjects)));
                                hva.setChildUndernourished(getIntegerNodeValue(getNodeValue("childUndernourished",s,listOfObjects)));
                                hva.setChildrenEnrolledInSchool(getIntegerNodeValue(getNodeValue("childrenEnrolledInSchool",s,listOfObjects)));
                                hva.setChildrenHasBirthCertificate(getIntegerNodeValue(getNodeValue("childrenHasBirthCertificate",s,listOfObjects)));
                                hva.setHasViralLoadResult(getIntegerNodeValue(getNodeValue("hasViralLoadResult",s,listOfObjects)));
                                hva.setHivPositiveLinked(getIntegerNodeValue(getNodeValue("hivPositiveLinked",s,listOfObjects)));
                                hva.setHivPreventionKnowledge(getIntegerNodeValue(getNodeValue("hivPreventionKnowledge",s,listOfObjects)));
                                hva.setHivStatusKnown(getIntegerNodeValue(getNodeValue("hivStatusKnown",s,listOfObjects)));
                                hva.setRegularSchoolAttendance(getIntegerNodeValue(getNodeValue("regularSchoolAttendance",s,listOfObjects)));
                                hva.setSocialEmotionalSupport(getIntegerNodeValue(getNodeValue("socialEmotionalSupport",s,listOfObjects)));
                                hva.setStableAdult(getIntegerNodeValue(getNodeValue("stableAdult",s,listOfObjects)));
                                hva.setViolenceExperienceReported(getIntegerNodeValue(getNodeValue("violenceExperienceReported",s,listOfObjects)));
                                hva.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                hva.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                hva.setVolunteerName(getNodeValue("nameOfAssessor",s,listOfObjects));
                                hva.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                hva.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                
                                existingHva=hvadao.getHouseholdVulnerabilityAssessment(hva.getHhUniqueId(),hva.getDateOfAssessment());
                                if(existingHva==null)
                                {
                                    if(hheOption==2 || hheOption==3)
                                    {
                                        count++;
                                        hvadao.saveHouseholdVulnerabilityAssessment(hva);
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Revised Household vulnerability assessment record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(hheOption==1 || hheOption==3)
                                    {
                                        if(existingHva.getLastModifiedDate().before(hva.getLastModifiedDate()) || existingHva.getLastModifiedDate().equals(hva.getLastModifiedDate()))
                                        {
                                            count++;
                                            hvadao.updateHouseholdVulnerabilityAssessment(hva);
                                            numberUpdated++;
                                            AppUtility.setCurrentImportRecordName("Revised Household vulnerability assessment record: "+count+" saved");
                                        }
                                    }
                                }
                                
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importAdultHouseholdMembersRecordsFromXML(String destinationDirectory,int hheOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="AdultHouseholdMembers";
    AppUtility.setCurrentImportRecordName("Adult Household Members");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Adult Household Members records saved");
    duplicateRecordsList.add("Number of Adult Household Members records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    if(hheOption==4)
    {
        newRecordsList.add(0);
        duplicateRecordsList.add(0);
        list.add(newRecordsList);
        list.add(duplicateRecordsList);
        return list;
    }
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    AdultHouseholdMemberDao ahmdao=util.getAdultHouseholdMemberDaoInstance();
                    AdultHouseholdMember ahm=null;
                    AdultHouseholdMember existingAhm=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("AdultHouseholdMember");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            ahm=new AdultHouseholdMember();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String hhUniqueId=getNodeValue("hhUniqueId",s,listOfObjects);
                                ahm.setHhUniqueId(hhUniqueId);
                                ahm.setEnrollmentId(getNodeValue("enrollmentId",s,listOfObjects));
                                ahm.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                ahm.setDateOfEnrollment(DateManager.getDateInstance(getNodeValue("dateOfEnrollment",s,listOfObjects)));
                                ahm.setEnrolledOnTreatment(getIntegerNodeValue(getNodeValue("enrolledOnTreatment",s,listOfObjects)));
                                ahm.setDateEnrolledOnTreatment(DateManager.getDateInstance(getNodeValue("dateEnrolledOnTreatment",s,listOfObjects)));
                                ahm.setTreatmentId(getNodeValue("treatmentId",s,listOfObjects));
                                ahm.setHivTreatmentFacilityId(getNodeValue("hivTreatmentFacility",s,listOfObjects));
                                ahm.setFirstName(getNodeValue("firstName",s,listOfObjects));
                                ahm.setSurname(getNodeValue("surname",s,listOfObjects));
                                ahm.setSex(getNodeValue("sex",s,listOfObjects));
                                ahm.setPhoneNumber(getNodeValue("phoneNumber",s,listOfObjects));
                                ahm.setOccupation(getIntegerNodeValue(getNodeValue("occupation",s,listOfObjects)));
                                ahm.setBaselineHivStatus(getIntegerNodeValue(getNodeValue("baselineHivStatus",s,listOfObjects)));
                                ahm.setDateOfBaselineHivStatus(DateManager.getDateInstance(getNodeValue("dateOfBaselineHivStatus",s,listOfObjects)));
                                ahm.setEducationLevel(getIntegerNodeValue(getNodeValue("educationLevel",s,listOfObjects)));
                                ahm.setIsCaregiver(getIntegerNodeValue(getNodeValue("isCaregiver",s,listOfObjects)));
                                
                                ahm.setMaritalStatus(getIntegerNodeValue(getNodeValue("maritalStatus",s,listOfObjects)));
                                ahm.setCurrentEnrollmentStatus(getIntegerNodeValue(getNodeValue("currentEnrollmentStatus",s,listOfObjects)));
                                ahm.setDateOfCurrentEnrollmentStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentEnrollmentStatus",s,listOfObjects)));
                                ahm.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                ahm.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                ahm.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                ahm.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                
                                ahm.setCurrentHivStatus(getIntegerNodeValue(getNodeValue("currentHivStatus",s,listOfObjects)));
                                ahm.setDateOfCurrentHivStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentHivStatus",s,listOfObjects)));
                                ahm.setAgeAtBaseline(getIntegerNodeValue(getNodeValue("ageAtBaseline",s,listOfObjects)));
                                ahm.setAgeUnitAtBaseline(getIntegerNodeValue(getNodeValue("ageUnitAtBaseline",s,listOfObjects)));
                                ahm.setCurrentAge(getIntegerNodeValue(getNodeValue("currentAge",s,listOfObjects)));
                                ahm.setCurrentAgeUnit(getIntegerNodeValue(getNodeValue("currentAgeUnit",s,listOfObjects)));
                                                                
                                //count++;
                                //System.err.println("Adult Household Members record: "+count+" saved");
                                existingAhm=ahmdao.getAdultHouseholdMember(ahm.getBeneficiaryId());
                                if(existingAhm==null)
                                {
                                    if(hheOption==2 || hheOption==3)
                                    {
                                        count++;
                                        ahmdao.saveAdultHouseholdMember(ahm);
                                        numberSaved++;
                                        System.err.println("Adult Household Members record: "+count+" saved");
                                        AppUtility.setCurrentImportRecordName("Adult Household Members record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(hheOption==1 || hheOption==3)
                                    {
                                        if(existingAhm.getLastModifiedDate().before(ahm.getLastModifiedDate()) || existingAhm.getLastModifiedDate().equals(ahm.getLastModifiedDate()))
                                        {
                                            count++;
                                            ahm.setDateCreated(existingAhm.getDateCreated());
                                            ahmdao.updateAdultHouseholdMemberWithoutDuplicateCheck(ahm,existingAhm);
                                            numberUpdated++;
                                            System.err.println("Adult Household Members record: "+count+" updated");
                                            AppUtility.setCurrentImportRecordName("Adult Household Members record: "+count+" saved");
                                        }
                                    }
                                }
                                householdList.add(ahm);
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importOvcRecordsFromXML(String destinationDirectory,int ovcOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="ChildEnrollment";
    AppUtility.setCurrentImportRecordName("Children");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new OVC records saved");
    duplicateRecordsList.add("Number of OVC records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    if(ovcOption==4)
    {
        newRecordsList.add(0);
        duplicateRecordsList.add(0);
        list.add(newRecordsList);
        list.add(duplicateRecordsList);
        return list;
    }
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    ChildEnrollmentDao ovcdao=util.getChildEnrollmentDaoInstance();
                    Ovc ovc=null;
                    Ovc existingOvc=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("ovc");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            ovc=new Ovc();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String hhUniqueId=getNodeValue("hhUniqueId",s,listOfObjects);
                                ovc.setHhUniqueId(hhUniqueId);
                                ovc.setEnrollmentId(getNodeValue("enrollmentId",s,listOfObjects));
                                ovc.setOvcId(getNodeValue("ovcId",s,listOfObjects));
                                ovc.setCaregiverId(getNodeValue("caregiverId",s,listOfObjects));
                                ovc.setCaregiverRelationship(getIntegerNodeValue(getNodeValue("caregiverRelationship",s,listOfObjects)));
                                ovc.setFirstName(getNodeValue("firstName",s,listOfObjects));
                                ovc.setSurname(getNodeValue("surname",s,listOfObjects));
                                ovc.setSex(getNodeValue("sex",s,listOfObjects));
                                ovc.setPhoneNumber(getNodeValue("phoneNumber",s,listOfObjects));
                                ovc.setDateOfEnrollment(DateManager.getDateInstance(getNodeValue("dateOfEnrollment",s,listOfObjects)));
                                ovc.setAgeAtBaseline(getIntegerNodeValue(getNodeValue("ageAtBaseline",s,listOfObjects)));
                                ovc.setAgeUnitAtBaseline(getIntegerNodeValue(getNodeValue("ageUnitAtBaseline",s,listOfObjects)));
                                ovc.setCurrentAge(getIntegerNodeValue(getNodeValue("currentAge",s,listOfObjects)));
                                ovc.setCurrentAgeUnit(getIntegerNodeValue(getNodeValue("currentAgeUnit",s,listOfObjects)));
                                ovc.setBaselineHivStatus(getIntegerNodeValue(getNodeValue("baselineHivStatus",s,listOfObjects)));
                                ovc.setDateOfBaselineHivStatus(DateManager.getDateInstance(getNodeValue("dateOfBaselineHivStatus",s,listOfObjects)));
                                ovc.setCurrentHivStatus(getIntegerNodeValue(getNodeValue("currentHivStatus",s,listOfObjects)));
                                ovc.setDateOfCurrentHivStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentHivStatus",s,listOfObjects)));
                                ovc.setEnrolledOnTreatment(getIntegerNodeValue(getNodeValue("enrolledOnTreatment",s,listOfObjects)));
                                ovc.setDateEnrolledOnTreatment(DateManager.getDateInstance(getNodeValue("dateEnrolledOnTreatment",s,listOfObjects)));
                                ovc.setTreatmentId(getNodeValue("treatmentId",s,listOfObjects));
                                ovc.setHivTreatmentFacilityId(getNodeValue("hivTreatmentFacility",s,listOfObjects));ovc.setVulnerabilityStatusCode(getNodeValue("vulnerabilityStatusCode",s,listOfObjects));
                                ovc.setBaselineBirthRegistrationStatus(getIntegerNodeValue(getNodeValue("baselineBirthRegistrationStatus",s,listOfObjects)));
                                ovc.setCurrentBirthRegistrationStatus(getIntegerNodeValue(getNodeValue("currentBirthRegistrationStatus",s,listOfObjects)));
                                ovc.setDateOfCurrentBirthRegStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentBirthRegStatus",s,listOfObjects)));
                                ovc.setBaselineSchoolStatus(getIntegerNodeValue(getNodeValue("baselineSchoolStatus",s,listOfObjects)));
                                ovc.setCurrentSchoolStatus(getIntegerNodeValue(getNodeValue("currentSchoolStatus",s,listOfObjects)));
                                ovc.setDateOfCurrentSchoolStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentSchoolStatus",s,listOfObjects)));
                                ovc.setSchoolName(getNodeValue("schoolName",s,listOfObjects));
                                ovc.setSchoolGrade(getNodeValue("schoolGrade",s,listOfObjects));
                                ovc.setSourceOfInfo(getIntegerNodeValue(getNodeValue("sourceOfInfo",s,listOfObjects)));
                                ovc.setHeight(Double.parseDouble(getNodeValue("height",s,listOfObjects)));
                                ovc.setWeight(Double.parseDouble(getNodeValue("weight",s,listOfObjects)));
                                ovc.setCommunityWorkerName(getNodeValue("communityWorkerName",s,listOfObjects));
                                ovc.setCurrentEnrollmentStatus(getIntegerNodeValue(getNodeValue("currentEnrollmentStatus",s,listOfObjects)));
                                ovc.setDateOfCurrentStatus(DateManager.getDateInstance(getNodeValue("dateOfCurrentEnrollmentStatus",s,listOfObjects)));
                                ovc.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                ovc.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                ovc.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                ovc.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                ovc.setCurrentSchoolStatus(getIntegerNodeValue(getNodeValue("currentSchoolStatus",s,listOfObjects)));
                                ovc.setChildHasCasePlan(getIntegerNodeValue(getNodeValue("childHasCasePlan",s,listOfObjects)));
                                ovc.setDateCasePlanDeveloped(DateManager.getDateInstance(getNodeValue("dateCasePlanDeveloped",s,listOfObjects)));
                                System.err.println("ovc.getChildHasCasePlan() is "+ovc.getChildHasCasePlan()+" and ovc.getDateCasePlanDeveloped() is "+ovc.getDateCasePlanDeveloped());
                                
                                if(ovc.getAgeUnitAtBaseline()==0)
                                ovc.setAgeAtBaseline(AppConstant.AGEUNIT_YEAR_NUM);
                                if(ovc.getCurrentAgeUnit()==0)
                                ovc.setCurrentAgeUnit(AppConstant.AGEUNIT_YEAR_NUM);
                                existingOvc=ovcdao.getOvc(ovc.getOvcId());
                                if(existingOvc==null)
                                {
                                    if(ovcOption==2 || ovcOption==3)
                                    {
                                        count++;
                                        ovcdao.saveOvc(ovc, false, false);
                                        numberSaved++;
                                        System.err.println("OVC record: "+count+" saved");
                                        AppUtility.setCurrentImportRecordName("OVC record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(ovcOption==1 || ovcOption==3)
                                    {
                                        if(existingOvc.getLastModifiedDate().before(ovc.getLastModifiedDate()) || existingOvc.getLastModifiedDate().equals(ovc.getLastModifiedDate()))
                                        {
                                            count++;
                                            ovc.setVulnerabilityStatusCode(existingOvc.getVulnerabilityStatusCode());
                                            ovcdao.updateOvc(ovc, false, false);
                                            numberUpdated++;
                                            System.err.println("OVC record: "+count+" updated");
                                            AppUtility.setCurrentImportRecordName("OVC record: "+count+" saved");
                                        }
                                    }
                                }
                                childList.add(ovc);
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importOvcServiceRecordsFromXML(String destinationDirectory,int saveOption)//HouseholdService
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="ChildServices";
    AppUtility.setCurrentImportRecordName("Child services");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Child service records saved");
    duplicateRecordsList.add("Number of Child service records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    ChildServiceDao servicedao=util.getChildServiceDaoInstance();
                    ChildService service=null;
                    ChildService existingService=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("ChildService");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            service=new ChildService();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                                               
                                service.setOvcId(getNodeValue("ovcId",s,listOfObjects));
                                service.setServiceDate(DateManager.getDateInstance(getNodeValue("serviceDate",s,listOfObjects)));
                                service.setStableServices(getNodeValue("stableServices",s,listOfObjects));
                                service.setHealthServices(getNodeValue("healthServices",s,listOfObjects));
                                service.setSafetyServices(getNodeValue("safetyServices",s,listOfObjects));
                                service.setSchooledServices(getNodeValue("schooledServices",s,listOfObjects));
                                service.setReferralServices(getNodeValue("referralServices",s,listOfObjects));
                                service.setNumberOfServices(getIntegerNodeValue(getNodeValue("numberOfServices",s,listOfObjects)));
                                service.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                service.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                service.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                service.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                service.setAgeAtService(getIntegerNodeValue(getNodeValue("ageAtService",s,listOfObjects)));
                                service.setAgeUnitAtService(getIntegerNodeValue(getNodeValue("ageUnitAtService",s,listOfObjects)));
                                
                                existingService=servicedao.getChildService(service.getOvcId(),service.getServiceDate());
                                if(existingService==null)
                                {
                                    if(saveOption==2 || saveOption==3)
                                    {
                                        count++;
                                        servicedao.saveChildService(service,false);
                                        System.err.println("Child service record: "+count+" saved");
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Child service record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(saveOption==1 || saveOption==3)
                                    {
                                        count++;
                                        servicedao.updateChildService(service, false);
                                        System.err.println("Child service record: "+count+" updated");
                                        numberUpdated++;
                                        AppUtility.setCurrentImportRecordName("Child service record: "+count+" saved");
                                    }
                                }
                                childServiceList.add(service);
                               
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    //processQuarterlyServiceTracker(childServiceList,AppConstant.OVC_TYPE_NUM,userName); 
    return list;
}
public List importHouseholdServiceRecordsFromXML(String destinationDirectory,int saveOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdServices";
    AppUtility.setCurrentImportRecordName("Household services");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Household service records saved");
    duplicateRecordsList.add("Number of Household service records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HouseholdServiceDao servicedao=util.getHouseholdServiceDaoInstance();
                    HouseholdService service=null;
                    HouseholdService existingService=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("HouseholdService");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            service=new HouseholdService();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {                          
                                service.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                service.setServiceDate(DateManager.getDateInstance(getNodeValue("serviceDate",s,listOfObjects)));
                                service.setStableServices(getNodeValue("stableServices",s,listOfObjects));
                                service.setHealthServices(getNodeValue("healthServices",s,listOfObjects));
                                service.setSafetyServices(getNodeValue("safetyServices",s,listOfObjects));
                                service.setReferralServices(getNodeValue("referralServices",s,listOfObjects));
                                service.setNumberOfServices(getIntegerNodeValue(getNodeValue("numberOfServices",s,listOfObjects)));
                                service.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                service.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                service.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                service.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                service.setAgeAtService(getIntegerNodeValue(getNodeValue("ageAtService",s,listOfObjects)));
                                existingService=servicedao.getHouseholdService(service.getBeneficiaryId(),service.getServiceDate());
                                if(existingService==null)
                                {
                                    if(saveOption==2 || saveOption==3)
                                    {
                                        count++;
                                        servicedao.saveHouseholdService(service,false);
                                        System.err.println("Household service record: "+count+" saved");
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Household service record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(saveOption==1 || saveOption==3)
                                    {
                                        count++;
                                        servicedao.updateHouseholdService(service,false);
                                        System.err.println("Household service record: "+count+" updated");
                                        numberUpdated++;
                                        AppUtility.setCurrentImportRecordName("Household service record: "+count+" saved");
                                    }
                                } 
                                householdServiceList.add(service);
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    //processQuarterlyServiceTracker(householdServiceList,AppConstant.CAREGIVER_TYPE_NUM,userName); 
    return list;
}

public List importHivRiskAssessmentRecordsFromXML(String destinationDirectory,int saveOption)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HivRiskAssessment";
    AppUtility.setCurrentImportRecordName("Hiv Risk assessment records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Hiv Risk assessment records saved");
    duplicateRecordsList.add("Number of Hiv Risk assessment records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HivRiskAssessmentDao hradao=util.getHivRiskAssessmentDaoInstance();
                    HivRiskAssessment hra=null;
                    HivRiskAssessment existingHra=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("elementName");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            hra=new HivRiskAssessment();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                hra.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                hra.setOvcId(getNodeValue("ovcId",s,listOfObjects));
                                hra.setAgeAtRiskAssessment(getIntegerNodeValue(getNodeValue("ageAtRiskAssessment",s,listOfObjects)));
                                hra.setAgeUnitAtRiskAssessment(getIntegerNodeValue(getNodeValue("ageUnitAtRiskAssessment",s,listOfObjects)));
                                hra.setChildAgeQuestion(getIntegerNodeValue(getNodeValue("childAgeQuestion",s,listOfObjects)));
                                hra.setChildCircumcisedOrEarPierced(getIntegerNodeValue(getNodeValue("childCircumcisedOrEarPierced",s,listOfObjects)));
                                hra.setChildEverPregnantQuestion(getIntegerNodeValue(getNodeValue("childEverPregnantQuestion",s,listOfObjects)));
                                hra.setChildHasMoreThanTwoIllnessQuestion(getIntegerNodeValue(getNodeValue("childHasMoreThanTwoIllnessQuestion",s,listOfObjects)));
                                hra.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                hra.setBloodTransfusionQuestion(getIntegerNodeValue(getNodeValue("bloodTransfusionQuestion",s,listOfObjects)));
                                hra.setChildSickQuestion(getIntegerNodeValue(getNodeValue("childSickQuestion",s,listOfObjects)));
                                hra.setChildAtRiskQuestion(getIntegerNodeValue(getNodeValue("childAtRiskQuestion",s,listOfObjects)));
                                hra.setChildTestedQuestion(getIntegerNodeValue(getNodeValue("childTestedQuestion",s,listOfObjects)));
                                hra.setChronicallyIllQuestion(getIntegerNodeValue(getNodeValue("chronicallyIllQuestion",s,listOfObjects)));
                                hra.setHivSibblingQuestion(getIntegerNodeValue(getNodeValue("hivSibblingQuestion",s,listOfObjects)));
                                hra.setHivStatusAtRiskAssessment(getIntegerNodeValue(getNodeValue("hivStatusAtRiskAssessment",s,listOfObjects)));
                                hra.setHivParentQuestion(getIntegerNodeValue(getNodeValue("hivParentQuestion",s,listOfObjects)));
                                hra.setHivStatusQuestion(getIntegerNodeValue(getNodeValue("hivStatusQuestion",s,listOfObjects)));
                                hra.setMotherSicknessQuestion(getIntegerNodeValue(getNodeValue("motherSicknessQuestion",s,listOfObjects)));
                                hra.setMuacbmiQuestion(getIntegerNodeValue(getNodeValue("muacbmiQuestion",s,listOfObjects)));
                                hra.setSibblingSicknessQuestion(getIntegerNodeValue(getNodeValue("sibblingSicknessQuestion",s,listOfObjects)));
                                hra.setSexualAbuseQuestion(getIntegerNodeValue(getNodeValue("sexualAbuseQuestion",s,listOfObjects)));
                                hra.setSexuallyActiveQuestion(getIntegerNodeValue(getNodeValue("sexuallyActiveQuestion",s,listOfObjects)));
                                hra.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                hra.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                hra.setNameOfAssessor(getNodeValue("nameOfAssessor",s,listOfObjects));
                                hra.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                hra.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                                               
                                
                                existingHra=hradao.getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment());
                                if(existingHra==null)
                                {
                                    if(saveOption==2 || saveOption==3)
                                    {
                                        count++;
                                        hradao.saveHivRiskAssessment(hra);
                                        System.err.println("Hiv Risk assessment record: "+count+" saved");
                                        numberSaved++;
                                        AppUtility.setCurrentImportRecordName("Hiv Risk assessment record: "+count+" saved");
                                    }
                                }
                                else
                                {
                                    if(saveOption==1 || saveOption==3)
                                    {
                                        count++;
                                        hra.setRecordId(existingHra.getRecordId());
                                        hradao.updateHivRiskAssessment(hra);
                                        System.err.println("Hiv Risk assessment record: "+count+" updated");
                                        numberUpdated++;
                                        AppUtility.setCurrentImportRecordName("Hiv Risk assessment record: "+count+" saved");
                                    }
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importReferralRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdReferral";
    AppUtility.setCurrentImportRecordName("Referral Service records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Referral Service records saved");
    duplicateRecordsList.add("Number of Referral Service records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HouseholdReferralDao rsdao=util.getHouseholdReferralDaoInstance();
                    HouseholdReferral referral=null;
                    HouseholdReferral existingReferral=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("ReferralService");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            referral=new HouseholdReferral();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                referral.setId(getIntegerNodeValue(getNodeValue("id",s,listOfObjects)));
                                referral.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                referral.setAgeAtReferral(getIntegerNodeValue(getNodeValue("ageAtReferral",s,listOfObjects)));
                                referral.setAgeUnitAtReferral(getIntegerNodeValue(getNodeValue("ageUnitAtReferral",s,listOfObjects)));
                                referral.setBeneficiaryType(getIntegerNodeValue(getNodeValue("beneficiaryType",s,listOfObjects)));
                                referral.setDateOfReferral(DateManager.getDateInstance(getNodeValue("dateOfReferral",s,listOfObjects)));
                                referral.setReferralComplete(getIntegerNodeValue(getNodeValue("referralComplete",s,listOfObjects)));
                                referral.setHealthServices(getNodeValue("healthServices",s,listOfObjects));
                                referral.setSafetyServices(getNodeValue("safetyServices",s,listOfObjects));
                                referral.setStableServices(getNodeValue("stableServices",s,listOfObjects));
                                referral.setSchooledServices(getNodeValue("schooledServices",s,listOfObjects));
                                referral.setReferringOrganization(getNodeValue("referringOrganization",s,listOfObjects));
                                referral.setReceivingOrganization(getNodeValue("receivingOrganization",s,listOfObjects));
                                referral.setNumberOfServices(getIntegerNodeValue(getNodeValue("numberOfServices",s,listOfObjects)));
                                referral.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                referral.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                referral.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                referral.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                referral.setCommunityWorker(getNodeValue("communityWorker",s,listOfObjects));
                                
                                existingReferral=rsdao.getHouseholdReferral(referral.getBeneficiaryId(), referral.getDateOfReferral());
                                if(existingReferral==null)
                                {
                                    count++;
                                    rsdao.saveHouseholdReferral(referral);
                                    System.err.println("Referral Service record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Referral Service record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    rsdao.updateHouseholdReferral(referral);
                                    System.err.println("Referral Service record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Referral Service record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importCareAndSupportRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="CareAndSupportChecklist";
    AppUtility.setCurrentImportRecordName("Referral Service records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new CareAndSupport records saved");
    duplicateRecordsList.add("Number of CareAndSupport records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    CareAndSupportChecklistDao dao=util.getCareAndSupportChecklistDaoInstance();
                    CareAndSupportChecklist casc=null;
                    CareAndSupportChecklist existingCasc=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("CareAndSupportAssessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            casc=new CareAndSupportChecklist();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                casc.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                casc.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                casc.setChildHasFever(getIntegerNodeValue(getNodeValue("childHasFever",s,listOfObjects)));
                                casc.setChildHasNightSweat(getIntegerNodeValue(getNodeValue("childHasNightSweat",s,listOfObjects)));
                                casc.setChildHasSwelling(getIntegerNodeValue(getNodeValue("childHasSwelling",s,listOfObjects)));
                                casc.setChildLossinWeight(getIntegerNodeValue(getNodeValue("childLossinWeight",s,listOfObjects)));
                                casc.setCoughSymptom(getIntegerNodeValue(getNodeValue("coughSymptom",s,listOfObjects)));
                                casc.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                casc.setDateOfNextAppointment(DateManager.getDateInstance(getNodeValue("dateOfNextAppointment",s,listOfObjects)));
                                casc.setDateOfViralLoadTest(DateManager.getDateInstance(getNodeValue("dateOfViralLoadTest",s,listOfObjects)));
                                casc.setEnrolledOnTreatment(getIntegerNodeValue(getNodeValue("enrolledOnTreatment",s,listOfObjects)));
                                casc.setFacilityId(getNodeValue("facilityId",s,listOfObjects));
                                casc.setMissedARVsRecently(getIntegerNodeValue(getNodeValue("missedARVsRecently",s,listOfObjects)));
                                casc.setMonthsOfTransportationSupport(getIntegerNodeValue(getNodeValue("monthsOfTransportationSupport",s,listOfObjects)));
                                casc.setPickedUpMedication(getIntegerNodeValue(getNodeValue("pickedUpMedication",s,listOfObjects)));
                                casc.setReasonViralLoadNotDone(getNodeValue("reasonViralLoadNotDone",s,listOfObjects));
                                casc.setReasonsPeopleSkipARV(getNodeValue("reasonsPeopleSkipARV",s,listOfObjects));
                                casc.setReceivedTransportationSupport(getIntegerNodeValue(getNodeValue("receivedTransportationSupport",s,listOfObjects)));
                                casc.setSoresRashPainExperience(getIntegerNodeValue(getNodeValue("soresRashPainExperience",s,listOfObjects)));
                                casc.setViralLoadResult(getIntegerNodeValue(getNodeValue("viralLoadResult",s,listOfObjects)));
                                casc.setViralLoadResultKnown(getIntegerNodeValue(getNodeValue("viralLoadResultKnown",s,listOfObjects)));
                                casc.setViralLoadTestDone(getIntegerNodeValue(getNodeValue("viralLoadTestDone",s,listOfObjects)));
                                
                                casc.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                casc.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                casc.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                casc.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                casc.setVolunteerName(getNodeValue("communityWorker",s,listOfObjects));
                                
                                existingCasc=dao.getCareAndSupportChecklist(casc.getBeneficiaryId(), casc.getDateOfAssessment());
                                if(existingCasc==null)
                                {
                                    count++;
                                    dao.saveCareAndSupportChecklist(casc);
                                    System.err.println("CareAndSupport record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("CareAndSupport record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateCareAndSupportChecklist(casc);
                                    System.err.println("CareAndSupport record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("CareAndSupport record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importCaregiverAccessToEmergencyFundRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="CaregiverAccessToEmergencyFund";
    AppUtility.setCurrentImportRecordName("Caregiver access to emergency fund records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Caregiver access to emergency fund records saved");
    duplicateRecordsList.add("Number of Caregiver access to emergency fund records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    CaregiverAccessToEmergencyFundDao dao=util.getCaregiverAccessToEmergencyFundDaoInstance();
                    CaregiverAccessToEmergencyFund caef=null;
                    CaregiverAccessToEmergencyFund existingCaef=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("Assessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            caef=new CaregiverAccessToEmergencyFund();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                caef.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                caef.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                caef.setAccessMoneyToPay(getIntegerNodeValue(getNodeValue("accessMoneyToPay",s,listOfObjects)));
                                caef.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));
                                caef.setSourceOfMoney(getNodeValue("sourceOfMoney",s,listOfObjects));
                                caef.setUnexpectedExpenditure(getIntegerNodeValue(getNodeValue("unexpectedExpenditure",s,listOfObjects)));
                                caef.setUrgentHhNeeds(getNodeValue("urgentHhNeeds",s,listOfObjects));
                                caef.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                caef.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                caef.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                caef.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                caef.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                
                                existingCaef=dao.getCaregiverAccessToEmergencyFund(caef.getBeneficiaryId(), caef.getDateOfAssessment());
                                if(existingCaef==null)
                                {
                                    count++;
                                    dao.saveCaregiverAccessToEmergencyFund(caef);
                                    System.err.println("Caregiver access to emergency fund record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Caregiver access to emergency fund record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateCaregiverAccessToEmergencyFund(caef);
                                    System.err.println("Caregiver access to emergency fund record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Caregiver access to emergency fund record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importCareplanAchievementChecklistRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="CareplanAchievementChecklist";
    AppUtility.setCurrentImportRecordName("Careplan achievement checklist records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Careplan achievement checklist records saved");
    duplicateRecordsList.add("Number of Careplan achievement checklist records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    CareplanAchievementChecklistDao dao=util.getCareplanAchievementChecklistDaoInstance();
                    CareplanAchievementChecklist cpa=null;
                    CareplanAchievementChecklist existingCpa=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("Assessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            cpa=new CareplanAchievementChecklist();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                cpa.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                cpa.setHhUniqueId(getNodeValue("hhUniqueId",s,listOfObjects));
                                cpa.setAllChildrenHaveBirthCert(getIntegerNodeValue(getNodeValue("allChildrenHaveBirthCert",s,listOfObjects)));
                                cpa.setCgiversEconomicActivity(getIntegerNodeValue(getNodeValue("cgiversEconomicActivity",s,listOfObjects)));
                                cpa.setChildrenEnrolledInSchool(getIntegerNodeValue(getNodeValue("childrenEnrolledInSchool",s,listOfObjects)));
                                cpa.setChildrenHivStatusknown(getIntegerNodeValue(getNodeValue("childrenHivStatusknown",s,listOfObjects)));
                                cpa.setChildrenNotUndernourished(getIntegerNodeValue(getNodeValue("childrenNotUndernourished",s,listOfObjects)));
                                cpa.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));                      
                                cpa.setDocumentedViralLoadResult(getIntegerNodeValue(getNodeValue("documentedViralLoadResult",s,listOfObjects)));
                                cpa.setEmotionalSupportTeamIdentification(getIntegerNodeValue(getNodeValue("emotionalSupportTeamIdentification",s,listOfObjects)));
                                cpa.setHivPosAdolscentsLinked(getIntegerNodeValue(getNodeValue("hivPosAdolscentsLinked",s,listOfObjects)));
                                cpa.setHivPreventionKnowledge(getIntegerNodeValue(getNodeValue("hivPreventionKnowledge",s,listOfObjects)));
                                cpa.setRegularSchoolAttendance(getIntegerNodeValue(getNodeValue("regularSchoolAttendance",s,listOfObjects)));
                                cpa.setScore(getIntegerNodeValue(getNodeValue("score",s,listOfObjects)));
                                cpa.setStableAdultInHousehold(getIntegerNodeValue(getNodeValue("stableAdultInHousehold",s,listOfObjects)));
                                cpa.setViolenceIncidenceReport(getIntegerNodeValue(getNodeValue("violenceIncidenceReport",s,listOfObjects)));
                                
                                cpa.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                cpa.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                cpa.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                cpa.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                cpa.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                
                                existingCpa=dao.getCareplanAchievementChecklist(cpa.getHhUniqueId(), cpa.getDateOfAssessment());
                                if(existingCpa==null)
                                {
                                    count++;
                                    dao.saveCareplanAchievementChecklist(cpa);
                                    System.err.println("Careplan achievement checklist record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Careplan achievement checklist record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateCareplanAchievementChecklist(cpa);
                                    System.err.println("Careplan achievement checklist record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Careplan achievement checklist record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importChildEducationPerformanceAssessmentRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="ChildEducationPerformanceAssessment";
    AppUtility.setCurrentImportRecordName("Child education performance assessment records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Child education performance assessment records saved");
    duplicateRecordsList.add("Number of Child education performance assessment records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    ChildEducationPerformanceAssessmentDao dao=util.getChildEducationPerformanceAssessmentDaoInstance();
                    ChildEducationPerformanceAssessment cepa=null;
                    ChildEducationPerformanceAssessment existingCepa=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("Assessment");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            cepa=new ChildEducationPerformanceAssessment();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                cepa.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                cepa.setOvcId(getNodeValue("ovcId",s,listOfObjects));
                                cepa.setChildHasInjuriesOrMarks(getIntegerNodeValue(getNodeValue("childHasInjuriesOrMarks",s,listOfObjects)));
                                cepa.setChildIsSociallyWithdrawn(getIntegerNodeValue(getNodeValue("childIsSociallyWithdrawn",s,listOfObjects)));
                                cepa.setChildMissVocTraining(getIntegerNodeValue(getNodeValue("childMissVocTraining",s,listOfObjects)));
                                cepa.setChildProgressedInSchool(getIntegerNodeValue(getNodeValue("childProgressedInSchool",s,listOfObjects)));
                                cepa.setClassTeacherComment(getNodeValue("classTeacherComment",s,listOfObjects));
                                cepa.setDateOfAssessment(DateManager.getDateInstance(getNodeValue("dateOfAssessment",s,listOfObjects)));                      
                                cepa.setClassTeacherName(getNodeValue("classTeacherName",s,listOfObjects));
                                cepa.setEarlyResumptionInSchool(getIntegerNodeValue(getNodeValue("earlyResumptionInSchool",s,listOfObjects)));
                                cepa.setEarlyResumptionInTrainingCenter(getIntegerNodeValue(getNodeValue("earlyResumptionInTrainingCenter",s,listOfObjects)));
                                cepa.setGoodPerformanceInLastExam(getIntegerNodeValue(getNodeValue("goodPerformanceInLastExam",s,listOfObjects)));
                                cepa.setRegularSchoolAttendance(getIntegerNodeValue(getNodeValue("regularSchoolAttendance",s,listOfObjects)));
                                cepa.setSignsOfFatigueAndTiredness(getIntegerNodeValue(getNodeValue("signsOfFatigueAndTiredness",s,listOfObjects)));
                                cepa.setSteadyImprovementInClassWork(getIntegerNodeValue(getNodeValue("steadyImprovementInClassWork",s,listOfObjects)));
                                cepa.setSteadyImprovementInVocWork(getIntegerNodeValue(getNodeValue("steadyImprovementInVocWork",s,listOfObjects)));
                                
                                cepa.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                cepa.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                cepa.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                cepa.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                cepa.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                
                                existingCepa=dao.getChildEducationPerformanceAssessment(cepa.getOvcId(), cepa.getDateOfAssessment());
                                if(existingCepa==null)
                                {
                                    count++;
                                    dao.saveChildEducationPerformanceAssessment(cepa);
                                    System.err.println("Child education performance assessment record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Child education performance assessment record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateChildEducationPerformanceAssessment(cepa);
                                    System.err.println("Child education performance assessment record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Child education performance assessment record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importBeneficiaryStatusUpdateRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="BeneficiaryStatusUpdates";
    AppUtility.setCurrentImportRecordName("Beneficiary status update records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Beneficiary status update records saved");
    duplicateRecordsList.add("Number of Beneficiary status update records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    BeneficiaryStatusUpdateDao dao=util.getBeneficiaryStatusUpdateDaoInstance();
                    BeneficiaryStatusUpdate bsu=null;
                    BeneficiaryStatusUpdate existingBsu=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("BeneficiaryStatusUpdate");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            bsu=new BeneficiaryStatusUpdate();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                //bsu.setBaselineHivStatus(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                bsu.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                bsu.setBeneficiaryType(getIntegerNodeValue(getNodeValue("beneficiaryType",s,listOfObjects)));
                                bsu.setBirthCertificate(getIntegerNodeValue(getNodeValue("birthCertificate",s,listOfObjects)));
                                bsu.setNewHivStatus(getIntegerNodeValue(getNodeValue("newHivStatus",s,listOfObjects)));
                                bsu.setDateEnrolledOnTreatment(DateManager.getDateInstance(getNodeValue("dateEnrolledOnTreatment",s,listOfObjects)));
                                bsu.setDateOfNewStatus(DateManager.getDateInstance(getNodeValue("dateOfNewStatus",s,listOfObjects)));
                                bsu.setEnrolledOnTreatment(getIntegerNodeValue(getNodeValue("enrolledOnTreatment",s,listOfObjects)));
                                bsu.setFacilityId(getNodeValue("facilityId",s,listOfObjects));
                                bsu.setPointOfUpdateValue(getIntegerNodeValue(getNodeValue("pointOfUpdateValue",s,listOfObjects)));
                                bsu.setSchoolStatus(getIntegerNodeValue(getNodeValue("schoolStatus",s,listOfObjects)));
                                bsu.setSchoolName(getNodeValue("schoolName",s,listOfObjects));
                                bsu.setGrade(getNodeValue("grade",s,listOfObjects));
                                bsu.setEnrolledInVocationalTraining(getIntegerNodeValue(getNodeValue("enrolledInVocationalTraining",s,listOfObjects)));
                                bsu.setNameOfVocationalTraining(getNodeValue("nameOfVocationalTraining",s,listOfObjects));
                                bsu.setUpdateCaregiverHivStatus(getIntegerNodeValue(getNodeValue("updateCaregiverHivStatus",s,listOfObjects)));
                                bsu.setUpdateChildBirthRegStatus(getIntegerNodeValue(getNodeValue("updateChildBirthRegStatus",s,listOfObjects)));
                                bsu.setUpdateChildHivStatus(getIntegerNodeValue(getNodeValue("updateChildHivStatus",s,listOfObjects)));
                                
                                bsu.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                bsu.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                bsu.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                //bsu.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                //bsu.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                
                                existingBsu=dao.getBeneficiaryStatusUpdate(bsu.getBeneficiaryId());
                                if(existingBsu==null)
                                {
                                    count++;
                                    dao.saveBeneficiaryStatusUpdate(bsu, true);
                                    System.err.println("Beneficiary status update record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Beneficiary status update record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateBeneficiaryStatusUpdate(bsu, true);
                                    System.err.println("Beneficiary status update record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Beneficiary status update record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importHouseholdCareplanRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdCareplan";
    AppUtility.setCurrentImportRecordName("Household Care plan records");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Household Care plan records saved");
    duplicateRecordsList.add("Number of Household Care plan records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    //String filePath=appUtil.getExportFilePath()+fileSeperator+exportFileName+fileSeperator;
    String filePath=destinationDirectory+fileSeperator+exportFileName+fileSeperator;
    System.err.println("filePath is "+filePath);
    try
	{
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                File file=null;
                Document doc = null;
		String fileName=filePath+exportFileName+".xml";
                List files=getFiles(filePath);
                if(files !=null)
                {
                    HouseholdCareplanDao dao=util.getHouseholdCareplanDaoInstance();
                    HouseholdCareplan hcp=null;
                    HouseholdCareplan existingHcp=null;
                    for(int i=0; i<files.size(); i++)
                    {
                        //System.err.println(filePath+files.get(i).toString());
                        fileName=filePath+files.get(i).toString();
                         file=new File(fileName);
                        if(!file.exists())
                        {
                            continue;
                        }
                        doc = docBuilder.parse (file);
                        // normalize text representation
                        doc.getDocumentElement().normalize();
                        NodeList listOfObjects = doc.getElementsByTagName("careplan");

                        for(int s=0; s<listOfObjects.getLength() ; s++)
                        {
                            hcp=new HouseholdCareplan();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {  
                                hcp.setRecordId(getIntegerNodeValue(getNodeValue("recordId",s,listOfObjects)));
                                hcp.setBeneficiaryId(getNodeValue("beneficiaryId",s,listOfObjects));
                                hcp.setCareplanDate(DateManager.getDateInstance(getNodeValue("careplanDate",s,listOfObjects)));
                                hcp.setFollowupForHealthServices(getNodeValue("followupForHealthServices",s,listOfObjects));
                                hcp.setFollowupForSafetyServices(getNodeValue("followupForSafetyServices",s,listOfObjects));
                                hcp.setFollowupForSchooledServices(getNodeValue("followupForSchooledServices",s,listOfObjects));
                                hcp.setFollowupForStableServices(getNodeValue("followupForStableServices",s,listOfObjects));
                                hcp.setHealthServicesToBeProvided(getNodeValue("healthServicesToBeProvided",s,listOfObjects));
                                hcp.setSafetyServicesToBeProvided(getNodeValue("safetyServicesToBeProvided",s,listOfObjects));
                                hcp.setSchooledServicesToBeProvided(getNodeValue("schooledServicesToBeProvided",s,listOfObjects));
                                hcp.setStableServicesToBeProvided(getNodeValue("stableServicesToBeProvided",s,listOfObjects));
                                hcp.setHouseholdHealthGoals(getNodeValue("householdHealthGoals",s,listOfObjects));
                                hcp.setHouseholdSafetyGoals(getNodeValue("householdSafetyGoals",s,listOfObjects));
                                hcp.setHouseholdSchooledGoals(getNodeValue("householdSchooledGoals",s,listOfObjects));
                                hcp.setHouseholdStableGoals(getNodeValue("householdStableGoals",s,listOfObjects));
                                hcp.setIdentifiedHealthIssues(getNodeValue("identifiedHealthIssues",s,listOfObjects));
                                hcp.setIdentifiedSafetyIssues(getNodeValue("identifiedSafetyIssues",s,listOfObjects));
                                hcp.setIdentifiedSchooledIssues(getNodeValue("identifiedSchooledIssues",s,listOfObjects));
                                hcp.setIdentifiedStableIssues(getNodeValue("identifiedStableIssues",s,listOfObjects));
                                hcp.setPriorityHealthGoals(getNodeValue("priorityHealthGoals",s,listOfObjects));
                                hcp.setPrioritySafetyGoals(getNodeValue("prioritySafetyGoals",s,listOfObjects));
                                hcp.setPrioritySchooledGoals(getNodeValue("prioritySchooledGoals",s,listOfObjects));
                                hcp.setPriorityStableGoals(getNodeValue("priorityStableGoals",s,listOfObjects));
                                hcp.setTimeFrameForHealthServices(getNodeValue("timeFrameForHealthServices",s,listOfObjects));
                                hcp.setTimeFrameForSafetyServices(getNodeValue("timeFrameForSafetyServices",s,listOfObjects));
                                hcp.setTimeFrameForSchooledServices(getNodeValue("timeFrameForSchooledServices",s,listOfObjects));
                                hcp.setTimeFrameForStableServices(getNodeValue("timeFrameForStableServices",s,listOfObjects));
                                hcp.setDateCreated(DateManager.getDateInstance(getNodeValue("dateCreated",s,listOfObjects)));
                                hcp.setLastModifiedDate(DateManager.getDateInstance(getNodeValue("lastModifiedDate",s,listOfObjects)));
                                hcp.setRecordedBy(getNodeValue("recordedBy",s,listOfObjects));
                                hcp.setMarkedForDelete(getIntegerNodeValue(getNodeValue("markedForDelete",s,listOfObjects)));
                                hcp.setVolunteerName(getNodeValue("volunteerName",s,listOfObjects));
                                
                                existingHcp=dao.getHouseholdCareplan(hcp.getBeneficiaryId(),hcp.getCareplanDate());
                                if(existingHcp==null)
                                {
                                    count++;
                                    dao.saveHouseholdCareplan(hcp);
                                    System.err.println("Household Care plan record: "+count+" saved");
                                    numberSaved++;
                                    AppUtility.setCurrentImportRecordName("Household Care plan record: "+count+" saved");
                                }
                                else
                                {
                                    count++;
                                    dao.updateHouseholdCareplan(hcp);
                                    System.err.println("Household Care plan record: "+count+" updated");
                                    numberUpdated++;
                                    AppUtility.setCurrentImportRecordName("Household Care plan record: "+count+" saved");
                                } 
                             }
                        }
                    }
	        }
             newRecordsList.add(numberSaved);
            duplicateRecordsList.add(numberUpdated);       
	}

	catch (SAXParseException err)
	{
		err.printStackTrace();
        }
        catch (SAXException e)
        {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}
	catch (Exception ex)
	{
		ex.printStackTrace ();
	}
    list.add(newRecordsList);
    list.add(duplicateRecordsList);
    return list;
}
public List importData(String currentUserName,int hheOption,int ovcOption,int metadataOption,int householdServiceOption,int childServiceOption,int riskAssessmentOption, boolean updateQuarterlyEnrollmentStatus)
{
    userName=currentUserName;
    List mainList=new ArrayList();
    if(!TaskManager.isDatabaseLocked())
    {
        int fileHheOption=hheOption;
        int fileOvcOption=ovcOption;
        int fileMetadataOption=metadataOption;
        int fileHouseholdServiceOption=householdServiceOption;
        int fileChildServiceOption=childServiceOption;
        int fileRiskAssessmentOption=riskAssessmentOption;
        int quarterlyEnrollmentStatusOption=1;
        DataImportFileUploadManager difum=null;
        try
        {
            List uploadedFileList=util.getDataImportUploadManagerDaoInstance().getAllDataImportFileUploadManager(0);
            //String[] uploadedFileList=appUtil.listFiles(appUtil.getImportFilePath());
            if(uploadedFileList !=null && !uploadedFileList.isEmpty())
            {

                String importFilePath=null;
                String destinationDirectory=null;
                String fileName=null;
                for(int i=0; i<uploadedFileList.size(); i++)
                {
                    difum=(DataImportFileUploadManager)uploadedFileList.get(i);
                    fileName=difum.getImportFileName();
                    if(fileName !=null)
                    {
                        importFilePath=appUtil.getImportFilePath()+appUtil.getFileSeperator()+fileName;
                        System.err.println("importFilePath in XMLDataImportManager is "+importFilePath);
                        File file=new File(importFilePath);
                        if(!file.exists())
                        {
                            difum.setImportStatus(2);
                            updateDataImportFileUploadManager(difum);
                            continue;
                        }
                        AppUtility.setCurrentImportFileName(fileName);
                        TaskManager.setDatabaseLocked(true);
                        if(fileName.indexOf(".zip") !=-1)
                        fileName=fileName.substring(0, fileName.indexOf(".zip"));
                        destinationDirectory=appUtil.getZipExtractsFilePath()+appUtil.getFileSeperator()+fileName;
                        unzipFile(importFilePath,destinationDirectory);
                        file=null;
                        String options=difum.getImportOptions();
                        System.err.println("options is "+options);
                        if(options !=null && options.indexOf(",") !=-1)
                        {
                            String[] optionsArray=options.split(",");
                            if(optionsArray !=null && optionsArray.length>2)
                            {
                                fileHheOption=Integer.parseInt(optionsArray[0]);
                                fileOvcOption=Integer.parseInt(optionsArray[1]);
                                fileMetadataOption=Integer.parseInt(optionsArray[2]);
                                if(optionsArray.length>3)
                                {
                                    fileHouseholdServiceOption=Integer.parseInt(optionsArray[3]);
                                    fileChildServiceOption=Integer.parseInt(optionsArray[4]);
                                    fileRiskAssessmentOption=Integer.parseInt(optionsArray[5]);
                                }
                                if(optionsArray.length>6)
                                {
                                    quarterlyEnrollmentStatusOption=Integer.parseInt(optionsArray[6]);
                                    if(quarterlyEnrollmentStatusOption==1)
                                    {
                                        updateQuarterlyEnrollmentStatus=true;
                                    }
                                    else
                                        updateQuarterlyEnrollmentStatus=false;
                                }
                            }
                            else
                            {
                                fileHheOption=hheOption;;
                                fileOvcOption=ovcOption;
                                fileMetadataOption=metadataOption;
                                fileHouseholdServiceOption=householdServiceOption;
                                fileChildServiceOption=childServiceOption;
                                fileRiskAssessmentOption=riskAssessmentOption;
                            }
                        }
                        else
                        {
                            fileHheOption=hheOption;
                            fileOvcOption=ovcOption;
                            fileMetadataOption=metadataOption;
                            fileHouseholdServiceOption=householdServiceOption;
                            fileChildServiceOption=childServiceOption;
                            fileRiskAssessmentOption=riskAssessmentOption;
                        }
                        mainList.addAll(importHouseholdEnrollmentRecordsFromXML(destinationDirectory,fileHheOption));
                        mainList.addAll(importAdultHouseholdMembersRecordsFromXML(destinationDirectory,fileHheOption));
                        mainList.addAll(importOvcRecordsFromXML(destinationDirectory,fileOvcOption));
                        mainList.addAll(importHouseholdServiceRecordsFromXML(destinationDirectory,fileHouseholdServiceOption));
                        mainList.addAll(importOvcServiceRecordsFromXML(destinationDirectory,fileChildServiceOption));
                        mainList.addAll(importHivRiskAssessmentRecordsFromXML(destinationDirectory,fileRiskAssessmentOption));
                        mainList.addAll(importReferralRecordsFromXML(destinationDirectory));
                        mainList.addAll(importCareAndSupportRecordsFromXML(destinationDirectory));
                        mainList.addAll(importCaregiverAccessToEmergencyFundRecordsFromXML(destinationDirectory));
                        mainList.addAll(importHouseholdVulnerabilityAssessmentRecordsFromXML(destinationDirectory,fileHheOption));
                        mainList.addAll(importRevisedHouseholdVulnerabilityAssessmentRecordsFromXML(destinationDirectory,fileHheOption));
                        mainList.addAll(importCareplanAchievementChecklistRecordsFromXML(destinationDirectory));
                        mainList.addAll(importChildEducationPerformanceAssessmentRecordsFromXML(destinationDirectory));
                        mainList.addAll(importBeneficiaryStatusUpdateRecordsFromXML(destinationDirectory));
                        mainList.addAll(importHouseholdCareplanRecordsFromXML(destinationDirectory));
                        System.err.println("fileMetadataOption is "+fileMetadataOption);
                        //if(fileMetadataOption !=4)
                        //importMetadata(destinationDirectory,fileMetadataOption);
                        difum.setImportStatus(1);
                        updateDataImportFileUploadManager(difum);
                        FileManager.moveFileToAnotherDirectory(importFilePath, appUtil.getImportDoneDirectoryPath());
                    }
                    
                }
                //AppUtility.setCurrentImportFileName(null);
                TaskManager.setDatabaseLocked(false);
                importData(currentUserName,hheOption,ovcOption,metadataOption,fileHouseholdServiceOption,fileChildServiceOption,fileRiskAssessmentOption,updateQuarterlyEnrollmentStatus);
            }
            //uploadedFileList=util.getDataImportUploadManagerDaoInstance().getAllDataImportFileUploadManager(0);
        }
        catch(Exception ex)
        {
            difum.setImportStatus(3);
            updateDataImportFileUploadManager(difum);
            ex.printStackTrace();
        }
        //AppUtility.setCurrentImportRecordName("All files imported");
    }
    if(updateQuarterlyEnrollmentStatus)
    {
        processQuarterlyEnrollmentStatus(childServiceList,AppConstant.OVC_TYPE_NUM,userName);
        processQuarterlyEnrollmentStatus(householdServiceList,AppConstant.CAREGIVER_TYPE_NUM,userName);
    }
    
    AppUtility.setCurrentImportFileName(null);
    //setInitialEnrollmentStatusManager(childList,userName,AppConstant.OVC_TYPE_NUM);
    //setInitialEnrollmentStatusManager(householdList,userName,AppConstant.CAREGIVER_TYPE_NUM);
    return mainList;
}
private static String getNodeName(String value,int s,NodeList listOfObjects)
{
    String elementName=null;
    Node firstPersonNode = listOfObjects.item(s);
    Element firstPersonElement = (Element)firstPersonNode;
    if(firstPersonElement !=null)
    {
        NodeList firstNameList = firstPersonElement.getElementsByTagName(value);
        Element firstNameElement = (Element)firstNameList.item(0);

        try
        {
            if(firstNameElement !=null)
            elementName=firstNameElement.getNodeName();
        }
        catch(NullPointerException npe)
        {
            elementName=null;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    return elementName;
}
private static String getNodeValue(String value,int s,NodeList listOfObjects)
{
    String elementValue="";
    if(getNodeName(value,s,listOfObjects) !=null)
    {
        Node firstPersonNode = listOfObjects.item(s);
        Element firstPersonElement = (Element)firstPersonNode;
        if(firstPersonElement !=null)
        {
            NodeList firstNameList = firstPersonElement.getElementsByTagName(value);
            Element firstNameElement = (Element)firstNameList.item(0);

            NodeList textFNList =null;
            if(firstNameElement !=null)
            {
                textFNList =firstNameElement.getChildNodes();
            }
            try
            {
                if((Node)textFNList==null)
                elementValue="";
                else if((Node)textFNList.item(0)==null)
                elementValue="";
                else if(((Node)textFNList.item(0)).getNodeValue()==null || (((Node)textFNList.item(0)).getNodeValue()).equals("") || (((Node)textFNList.item(0)).getNodeValue()).equals(" ") || (((Node)textFNList.item(0)).getNodeValue()).equals("none"))
                elementValue="";
                else
                elementValue=((Node)textFNList.item(0)).getNodeValue();
            }
            catch(NullPointerException npe)
            {
                elementValue="";
            }
        }
    }
    return elementValue;
}
private int getIntegerNodeValue(String value)
{
    int intValue=0;
    try
    {
        if(value !=null && value.trim().length()>0)
        intValue=Integer.parseInt(value);
    }
    catch(NumberFormatException nfe)
    {
        intValue=0;
    }
    
    return intValue;
}
private List getFiles(String filePath)
{
    AppUtility appUtil=new AppUtility();
    String[] files=appUtil.listFiles(filePath);
    List fileList=new ArrayList();
    if(files !=null)
    {
        for(int i=0; i<files.length; i++)
        {
            if(files[i].indexOf(".xml") ==-1)
            continue;
            fileList.add(files[i]);
        }
    }
    return fileList;
}
private void updateDataImportFileUploadManager(DataImportFileUploadManager difum)
{
    try
    {
        util.getDataImportUploadManagerDaoInstance().updateDataImportFileUploadManager(difum);
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        //difum.setImportStatus(3);
        //util.getDataImportUploadManagerDaoInstance().updateDataImportFileUploadManager(difum);
    }
}

public void unzipFile(String fileName,String destinationDirectory)
{
    AppUtility.setCurrentImportRecordName("Extracting data from zip files...");
    appUtil.createDirectories(destinationDirectory);
    appUtil.createExportImportDirectories();
    ZipHandler zipHandler=new ZipHandler();
    zipHandler.unZipFile(fileName,destinationDirectory);
}

private void setInitialEnrollmentStatusManager(List list,String userName,int beneficiaryType)
{
    EnrollmentStatusManager esm=new EnrollmentStatusManager();//childList
    if(beneficiaryType==AppConstant.OVC_TYPE_NUM)
    {
        esm.processChildrenEnrollmentStatus(list, userName);
    }
    else if(beneficiaryType==AppConstant.CAREGIVER_TYPE_NUM)
    {
        esm.processAdultHouseholdMemberEnrollmentStatus(list, userName);
    }
}
private void processQuarterlyEnrollmentStatus(List serviceList,int beneficiaryType,String userName)
{
    if(serviceList !=null)
    {
        int ovcCount=0;
        int adultMemberCount=0;
        //QuarterlyServiceTrackerManager qstm=new QuarterlyServiceTrackerManager();
        EnrollmentStatusManager esm=new EnrollmentStatusManager();                        
        if(beneficiaryType==AppConstant.OVC_TYPE_NUM)
        {
            for(Object obj:serviceList)
            {
                ChildService service=(ChildService)obj;
                esm.updateQuarterlyEnrollmentStatusByServiceParameters(userName, service.getOvcId(), beneficiaryType, service.getServiceDate());
                ovcCount++;
                AppUtility.setCurrentImportRecordName("Child enrollment status record: "+ovcCount+" saved");
                //qstm.saveQuarterlyService(service.getBeneficiaryId(), service.getServiceDate(),AppConstant.OVC_TYPE_NUM, service.getAgeAtService(),true,userName);
            }
        }
        else if(beneficiaryType==AppConstant.CAREGIVER_TYPE_NUM)
        {
            for(Object obj:serviceList)
            {
                HouseholdService service=(HouseholdService)obj;
                esm.updateQuarterlyEnrollmentStatusByServiceParameters(userName, service.getBeneficiaryId(), beneficiaryType, service.getServiceDate());
                adultMemberCount++;
                AppUtility.setCurrentImportRecordName(adultMemberCount+" Adult members enrollment status record saved");
                //qstm.saveQuarterlyService(service.getOvcId(), service.getServiceDate(),AppConstant.CAREGIVER_TYPE_NUM, service.getAgeAtService(),true,userName);
            }
        }
    }
}
/*private void cleanUpIMBCServices()
{
    AppUtility.setCurrentImportRecordName("Cleaning Mother Baby course service records...");
    DataCleanupUtility dcu=new DataCleanupUtility();
    int count=dcu.cleanServiceRecords(AppConstant.HEALTH_DOMAIN, OvcServiceAttributesManager.getMotherBabyCourseServices().getServiceCode(), 2);
    AppUtility.setCurrentImportRecordName(count+" Mother Baby course service records cleaned");
}*/
}
