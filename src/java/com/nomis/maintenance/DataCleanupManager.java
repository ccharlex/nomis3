/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.maintenance;

import com.nomis.exportimport.ZipHandler;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.dao.AdultHouseholdMemberDao;
import com.nomis.ovc.dao.ChildEnrollmentDao;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.HouseholdEnrollmentDao;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.TaskManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author smomoh
 */
public class DataCleanupManager 
{
    DaoUtility util=new DaoUtility();
    AppUtility appUtil=new AppUtility();
    
    String userName=null;
public DataCleanupManager()
{
    ZipHandler zipHandler;
    appUtil=new AppUtility();
    AppUtility.getResourceLocation();
    String destinationDirectory=appUtil.getExportFilePath();
    
}
public List importHouseholdEnrollmentRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="HouseholdRecordsForDelete";
    AppUtility.setCurrentImportRecordName("Household enrollment");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Household enrollment records saved");
    duplicateRecordsList.add("Number of Household enrollment records saved as updates");
    
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
                                //markedForDelete value of 9 means this has been approved for delete
                                int markedForDelete=Integer.parseInt(getNodeValue("markedForDelete",s,listOfObjects));
                                count++;
                                System.err.println("markedForDelete at "+count+" is "+markedForDelete);
                                if(markedForDelete==9)
                                {                                                                
                                    existinghhe=hhedao.getHouseholdEnrollment(hhUniqueId);
                                    if(existinghhe !=null)
                                    {
                                        if(existinghhe.getMarkedForDelete()==1)
                                        {
                                            hhedao.deleteHouseholdEnrollment(existinghhe);
                                            count++;
                                            System.err.println("Household record "+count+" deleted");
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
public List importAdultHouseholdMembersRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    List householdList=new ArrayList();
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="AdultHouseholdMemberRecordsForDelete";
    AppUtility.setCurrentImportRecordName("Adult Household Members");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new Adult Household Members records saved");
    duplicateRecordsList.add("Number of Adult Household Members records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    
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
                            //ahm=new AdultHouseholdMember();
                            Node firstNode = listOfObjects.item(s);
                             if(firstNode.getNodeType() == Node.ELEMENT_NODE)
                             {
                                String beneficiaryId=getNodeValue("beneficiaryId",s,listOfObjects);
                                //markedForDelete value of 9 means this has been approved for delete
                                int markedForDelete=Integer.parseInt(getNodeValue("markedForDelete",s,listOfObjects));
                                count++;
                                System.err.println("markedForDelete at "+count+" is "+markedForDelete);
                                if(markedForDelete==9)
                                {
                                    existingAhm=ahmdao.getAdultHouseholdMember(beneficiaryId);
                                    if(existingAhm !=null)
                                    {
                                        if(existingAhm.getMarkedForDelete()==1)
                                        {
                                            ahmdao.deleteAdultHouseholdMember(existingAhm);
                                            count++;
                                            System.err.println("Adult Household member record "+count+" deleted");
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
public List importOvcRecordsFromXML(String destinationDirectory)
{
    AppUtility appUtil=new AppUtility();
    List list=new ArrayList();
    
    String fileSeperator=appUtil.getFileSeperator();
    String exportFileName="OvcRecordsForDelete";
    AppUtility.setCurrentImportRecordName("Children");
    List newRecordsList=new ArrayList();
    List duplicateRecordsList=new ArrayList();
    newRecordsList.add("Number of new OVC records saved");
    duplicateRecordsList.add("Number of OVC records saved as updates");
    int numberSaved=0;
    int numberUpdated=0;
    int count=0;
    
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
                                String ovcId=getNodeValue("ovcId",s,listOfObjects);
                                //markedForDelete value of 9 means this has been approved for delete
                                int markedForDelete=Integer.parseInt(getNodeValue("markedForDelete",s,listOfObjects));
                                count++;
                                System.err.println("markedForDelete at "+count+" is "+markedForDelete);
                                if(markedForDelete==9)
                                {
                                    existingOvc=ovcdao.getOvc(ovcId);
                                    if(existingOvc !=null)
                                    {
                                        if(existingOvc.getMarkedForDelete()==1)
                                        {
                                            ovcdao.deleteOvc(existingOvc);
                                            count++;
                                            System.err.println("OVC record "+count+" deleted");
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
public int importAndProcessDataDeleteRequest(String metadataImportFilePath,HttpServletRequest request)
{
    int success=0;
    //String currentUserName=appUtil.getCurrentUser(request.getSession());
    if(!TaskManager.isDatabaseLocked())
    {  
        try
        {
           TaskManager.setDatabaseLocked(true);
           String destinationDirectory=appUtil.getZipExtractsFilePath(); 
           
           if(metadataImportFilePath !=null)
           {
               File importFile=new File(metadataImportFilePath);
               if(importFile.exists())
               {
                   createDestinationFolderAndUnzipMetadataFile(destinationDirectory,metadataImportFilePath);
                   importHouseholdEnrollmentRecordsFromXML(destinationDirectory);
                   importAdultHouseholdMembersRecordsFromXML(destinationDirectory);
                   importOvcRecordsFromXML(destinationDirectory);
                   
                   success=1;
               }
           }
           TaskManager.setDatabaseLocked(false);
        }
        catch(Exception ex)
        {
            TaskManager.setDatabaseLocked(false);
            success=0;
            ex.printStackTrace();
        }
        
    }
    return success;
}
public void createDestinationFolderAndUnzipMetadataFile(String destinationDirectory,String filePath)
{
    appUtil=new AppUtility();
    appUtil.createZipExtractDirectories();
    appUtil.createDirectories(destinationDirectory);
    ZipHandler zipHandler=new ZipHandler();
    zipHandler.unZipFile(filePath,destinationDirectory);
    zipHandler=null;
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
}
