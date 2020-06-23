/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;

import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.ovc.business.CommunityBasedOrganization;
import com.nomis.ovc.business.Partner;
import com.nomis.ovc.business.SiteSetup;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.metadata.OrganizationUnitHierarchy;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smomoh
 */
public class OrganizationUnitAttributesManager 
{
    public List getPartnerListForReports(ReportParameterTemplate rpt)
    {
        List partnerList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            List partnerCodeList=util.getHouseholdEnrollmentDaoInstance().getDistinctPartnerCodes(rpt);
            if(partnerCodeList !=null)
            {
                for(Object obj:partnerCodeList)
                {
                    partnerList.add(util.getPartnerDaoInstance().getPartner((String)obj));
                }
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return partnerList;
    }
    public String getOrganizationUnitName(String uid)
    {
        String organizationUnitName=null;
        try
        {
            DaoUtility util=new DaoUtility();
            OrganizationUnit ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(uid);
            if(ou !=null)
            organizationUnitName=ou.getName();
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return organizationUnitName;
    }
    public String getCommunityBasedOrganizationName(String uid)
    {
        String communityBasedOrganizationName=null;
        try
        {
            DaoUtility util=new DaoUtility();
            CommunityBasedOrganization cbo=util.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganization(uid);
            if(cbo !=null)
            communityBasedOrganizationName=cbo.getCboName();
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return communityBasedOrganizationName;
    }
    public String getPartnerName(String uid)
    {
        String partnerName=null;
        try
        {
            DaoUtility util=new DaoUtility();
            Partner partner=util.getPartnerDaoInstance().getPartner(uid);
            if(partner !=null)
            partnerName=partner.getPartnerName();
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return partnerName;
    }
    public OrganizationUnit getOrganizationUnitWithParentNames(OrganizationUnit ou)
    {
        //OrganizationUnit ou=null;
        try
        {
            DaoUtility util=new DaoUtility();
            //ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(uid);
            if(ou !=null)
            {
                String ouPath=ou.getOuPath();
                if(ouPath !=null)
                {
                    String seperator="/";
                    //if(ouPath.indexOf("/") !=-1)
                    //seperator="/";
                    ouPath=ouPath.replace("\\","/");
                    String[] ouPathArray=ouPath.split(seperator);
                    if(ouPathArray !=null)
                    {
                        OrganizationUnit parent=null;
                        String ouPathByName="";
                        String ouPathId=null;
                        for(int i=0; i<ouPathArray.length; i++)
                        {
                            ouPathId=ouPathArray[i];
                            parent=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ouPathId);
                            if(parent !=null)
                            ouPathByName=ouPathByName+seperator+parent.getName();
                        }
                        ou.setOuPathByNames(ouPathByName);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ou;
    }
    public List getOrganizationUnitsWithParentNames(int ouLevel)
    {
        List ouList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            List list=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ouLevel);
            if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    OrganizationUnit ou=(OrganizationUnit)obj;
                    ouList.add(getOrganizationUnitWithParentNames(ou));
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ouList;
    }
    public List getLevel2OrganizationUnitForReports(HttpSession session)
    {
        List level2OuList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            level2OuList=util.getReportOrganizationUnitDaoInstance().getLevel2OuForReports();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        session.setAttribute("allLevel2OuList", level2OuList);
        //allLevel2OuList
        return level2OuList;
    }
    public List getLevel3OrganizationUnitForReports(HttpSession session,String level2OuId)
    {
        List level3OuList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            level3OuList=util.getReportOrganizationUnitDaoInstance().getLevel3OuForReports(level2OuId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        session.setAttribute("level3OuListForReports", level3OuList);
        //allLevel2OuList
        return level3OuList;
    }
    public List getLevel4OrganizationUnitForReports(HttpSession session,String level3OuId)
    {
        List level4OuList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            level4OuList=util.getReportOrganizationUnitDaoInstance().getLevel4OuForReports(level3OuId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        session.setAttribute("level4OuListForReports", level4OuList);
        //allLevel2OuList
        return level4OuList;
    }
    public void setOrganizationUnitAttributes(HttpSession session, String level3OuId,String userName,String cboId)
    {
        setLevel2OuWithLevel3OuList(session, userName);
        setLevel4OuList(session, level3OuId);
        setCBOListByAssignedLevel3Ou(session, level3OuId);
        setLevel2OuHierarchy(session);
        setLevel3OuHierarchy(session);
        setLevel4OuHierarchy(session);
        //setAssignedLevel3Ou(session, cboId);
    }
    /*public void setOrganizationUnitAttributes(HttpSession session, String level3OuId,String userName,String cboId)
    {
        setLevel2Ou(session, userName);
        setLevel2OuHierarchy(session);
        setCBOListByAssignedLevel3Ou(session, level3OuId);
        setLevel3OuHierarchy(session);
        setLevel4OuHierarchy(session);
        setAssignedLevel3Ou(session, cboId);
    }*/
    public void setOrganizationUnitAttributesByOuIdForReferralFacility(HttpSession session,String level2OuId,String level3OuId,String level4OuId)
    {
        try
        {
            OrganizationUnit level2Ou=getOrganizationUnitForReports(level2OuId);
            OrganizationUnit level3Ou=getOrganizationUnitForReports(level3OuId);
            OrganizationUnit level4Ou=getOrganizationUnitForReports(level4OuId);
            session.setAttribute("level2OuForReferralFacility", level2Ou);
            session.setAttribute("level3OuForReferralFacility", level3Ou);
            session.setAttribute("level4OuForReferralFacility", level4Ou);  
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //OrganizationUnitHierarchy 
    }
    public void setOrganizationUnitAttributesByOuId(HttpServletRequest request,String level2OuId,String level3OuId,String level4OuId,String cboId)
    {
        try
        {
            OrganizationUnit level2Ou=getOrganizationUnitForReports(level2OuId);
            OrganizationUnit level3Ou=getOrganizationUnitForReports(level3OuId);
            OrganizationUnit level4Ou=getOrganizationUnitForReports(level4OuId);
            CommunityBasedOrganization cbo=getCBOForReports(cboId);
            request.setAttribute("level2OuForReport", level2Ou);
            request.setAttribute("level3OuForReport", level3Ou);
            request.setAttribute("level4OuForReport", level4Ou);
            request.setAttribute("cboForReport", cbo);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //OrganizationUnitHierarchy 
    }
    public OrganizationUnit getLevel2OrganizationUnitFromOuPart(String ouPath)
    {
        OrganizationUnit ou=null;
        //System.err.println("Level 2 ouPath in util.OrganizationUnitAttributesManager  is "+ouPath);
        try
        {
            if(ouPath !=null && (ouPath.indexOf("/") !=-1 || ouPath.indexOf("\\") !=-1))
            {
                ouPath=ouPath.replace("\\","/");
                //String backSlash="\\";
                DaoUtility util=new DaoUtility();
                String[] ouPathItems=ouPath.split("/");
                //if(ouPath.indexOf(backSlash) !=-1)
                //ouPathItems=ouPath.split(backSlash);
                if(ouPathItems !=null && ouPathItems.length>1)
                {
                    ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ouPathItems[1]);
                }
                else
                {
                   ou=getNewOrganizationUnit();
                }
            }
            if(ou==null)
            {
                ou=getNewOrganizationUnit();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ou; 
    }
    public OrganizationUnit getLevel3OrganizationUnitFromOuPart(String ouPath)
    {
        //System.err.println("Level 3 ouPath in util.OrganizationUnitAttributesManager is "+ouPath);
        OrganizationUnit ou=null;
        try
        {
            if(ouPath !=null && (ouPath.indexOf("/") !=-1 || ouPath.indexOf("\\") !=-1))
            {
                ouPath=ouPath.replace("\\","/");
                DaoUtility util=new DaoUtility();
                String[] ouPathItems=ouPath.split("/");
                
                if(ouPathItems !=null && ouPathItems.length>2)
                {
                    ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ouPathItems[2]);
                    //System.err.println("Level 3 ou.getName() in util.OrganizationUnitAttributesManager is "+ou.getName());
                }
                else
                {
                    ou=getNewOrganizationUnit();
                }
            }
            if(ou==null)
            {
                ou=getNewOrganizationUnit();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ou; 
    }
    public OrganizationUnit getNewOrganizationUnit()
    {
        OrganizationUnit ou=null;
        try
        {
            ou=new OrganizationUnit();
            ou.setUid("All");
            ou.setName("All");
            ou.setOuPath("All");          
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ou; 
    }
    public OrganizationUnit getOrganizationUnitForReports(String uid)
    {
        OrganizationUnit ou=null;
        try
        {
            DaoUtility util=new DaoUtility();
            
            if(uid !=null && !uid.equalsIgnoreCase("All"))
            ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(uid);
            if(ou==null)
            {
                ou=getNewOrganizationUnit();
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ou; 
    }
    public CommunityBasedOrganization getCBOForReports(String cboId)
    {
        CommunityBasedOrganization cbo=null;
        try
        {
            DaoUtility util=new DaoUtility();
            
            if(cboId !=null && !cboId.equalsIgnoreCase("All"))
            cbo=util.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganization(cboId);
            else
            {
                cbo=new CommunityBasedOrganization();
                cbo.setUniqueId("All");
                cbo.setCboName("All");
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return cbo; 
    }
    public void setOrganizationUnitHierarchyAttributes(HttpSession session)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            List ouHierarchyList=util.getOrganizationUnitHierarchyDaoInstance().getAllOrganizationUnitHierarchyRecords();
            if(ouHierarchyList !=null && !ouHierarchyList.isEmpty())
            {
                OrganizationUnitHierarchy ouh=null;
                String ouhAttributeName="ouhLevel";
                for(int i=0; i<ouHierarchyList.size(); i++)
                {
                    ouh=(OrganizationUnitHierarchy)ouHierarchyList.get(i);
                    session.setAttribute(ouhAttributeName+ouh.getOulevel()+"Name", ouh);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //OrganizationUnitHierarchy 
    }
    public void setOrganizationUnitAttributesForReports(HttpSession session)
    {
        //setLevel2Ou(session, userName);
        setLevel2OuHierarchy(session);
        //setCBOListByAssignedLevel3Ou(session, level3OuId);
        setLevel3OuHierarchy(session);
        setLevel4OuHierarchy(session);
        //setAssignedLevel3Ou(session, cboId);
        setAllLevel2OuList(session);
    }
    public void setAllLevel2OuList(HttpSession session)
    {
        try
        {
            OrganizationUnit ou=new OrganizationUnit();
            DaoUtility daoutil=new DaoUtility();
            List level2OuList=daoutil.getOrganizationUnitDaoInstance().getOrganizationUnit(2);
            if(level2OuList==null)
            level2OuList=new ArrayList();
            session.setAttribute("allLevel2OuList", level2OuList);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel4OuList(HttpSession session, String level3OuId)
    {
        try
        {
            OrganizationUnit ou=new OrganizationUnit();
            DaoUtility daoutil=new DaoUtility();
            List level4OuList=daoutil.getOrganizationUnitDaoInstance().getOrganizationUnityByParentId(level3OuId);
            if(level4OuList==null)
            level4OuList=new ArrayList();
            session.setAttribute("level4OuList", level4OuList);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setAssignedLevel3Ou(HttpSession session, String cboId)
    {
        List level3OuList=new ArrayList();
        try
        {
            String assignedLevel3OuIds=null;
            DaoUtility daoutil=new DaoUtility();
           CommunityBasedOrganization cbo=daoutil.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganization(cboId);
           if(cbo !=null)
           assignedLevel3OuIds=cbo.getAssignedLevel3OuIds();
           if(assignedLevel3OuIds !=null)
           {
               assignedLevel3OuIds=assignedLevel3OuIds.replace(";", ",");
               String[] assignedLevel3OuIdArray=assignedLevel3OuIds.split(",");
               if(assignedLevel3OuIdArray !=null)
               {
                   String level3OuId=null;
                   OrganizationUnit ou=null;
                   for(int i=0; i<assignedLevel3OuIdArray.length; i++)
                   {
                       level3OuId=assignedLevel3OuIdArray[i];
                       ou=daoutil.getOrganizationUnitDaoInstance().getOrganizationUnit(level3OuId);
                       if(ou !=null)
                       level3OuList.add(ou);
                   }
               }
           }
           
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        session.setAttribute("assignedLevel3OuList", level3OuList); 
    }
    
    public List getCBOListByLevel2Ou(String level2OuId)
    {
        List cboList=null;
        try
        {
            DaoUtility daoutil=new DaoUtility();
            cboList=daoutil.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganizationByLevel2OrgUnit(level2OuId);
            if(cboList ==null)
            cboList=new ArrayList();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return cboList;
    }
    public void setCBOListByLevel2Ou(HttpSession session,String level2OuId)
    {
        List cboList=getCBOListByLevel2Ou(level2OuId);
        session.setAttribute("cboList", cboList);
        
    }
    public List getCBOListByAssignedLevel3Ou(String level3OuId)
    {
        List cboList=null;
        try
        {
            DaoUtility daoutil=new DaoUtility();
            cboList=daoutil.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganizationByAssignedLevel3OrgUnit(level3OuId);
            if(cboList ==null)
            cboList=new ArrayList();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return cboList;
    }
    public void setCBOListByAssignedLevel3Ou(HttpSession session,String level3OuId)
    {
        try
        {
            DaoUtility daoutil=new DaoUtility();
            List cboList=daoutil.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganizationByAssignedLevel3OrgUnit(level3OuId);
            if(cboList ==null)
            cboList=new ArrayList();
            session.setAttribute("assignedCboList",cboList);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel2OuWithLevel3OuList(HttpSession session, String userName)
    {
        try
        {
            OrganizationUnit ou=new OrganizationUnit();
            DaoUtility daoutil=new DaoUtility();
            SiteSetup siteSetup=getSiteSetup(userName);
            if(siteSetup !=null)
            {
                String level2OuId=siteSetup.getOrgUnitId();
                StartupResourceManager srm=new StartupResourceManager();
                List level3OuList=srm.getLevel3OrgUnits(level2OuId);
                if(level3OuList==null)
                level3OuList=new ArrayList();
                if(!level3OuList.isEmpty())
                {
                    String firstLevel3OuId=level3OuList.get(0).toString();
                    setCBOListByAssignedLevel3Ou(session,firstLevel3OuId);
                }
                session.setAttribute("assignedLevel3OuList", level3OuList);
                ou=daoutil.getOrganizationUnitDaoInstance().getOrganizationUnit(level2OuId);
                session.setAttribute("siteSetupOu", ou);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel2Ou(HttpSession session, String userName)
    {
        try
        {
            OrganizationUnit ou=new OrganizationUnit();
            DaoUtility daoutil=new DaoUtility();
            SiteSetup siteSetup=getSiteSetup(userName);//daoutil.getSiteSetupDaoInstance().getSiteSetup(userName);
            //OrganizationUnitHierarchy ouh=daoutil.getOrganizationUnitHierarchyDaoInstance().getOrganizationUnitHierarchyByLevel(2);
            if(siteSetup !=null)
            {
                String level2OuId=siteSetup.getOrgUnitId();
                setCBOListByLevel2Ou(session,level2OuId);
                StartupResourceManager srm=new StartupResourceManager();
                List level3OuList=srm.getLevel3OrgUnits(level2OuId);
                if(level3OuList==null)
                level3OuList=new ArrayList();
                
                session.setAttribute("level3OuList", level3OuList);
                ou=daoutil.getOrganizationUnitDaoInstance().getOrganizationUnit(level2OuId);
                session.setAttribute("siteSetupOu", ou);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel2OuHierarchy(HttpSession session)
    {
        try
        {
            DaoUtility daoutil=new DaoUtility();
            OrganizationUnitHierarchy ouh=daoutil.getOrganizationUnitHierarchyDaoInstance().getOrganizationUnitHierarchyByLevel(2);
            if(ouh ==null)
            ouh=new OrganizationUnitHierarchy();
            session.setAttribute("level2Ouh", ouh);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel3OuHierarchy(HttpSession session)
    {
        try
        {
            DaoUtility daoutil=new DaoUtility();
            OrganizationUnitHierarchy ouh=daoutil.getOrganizationUnitHierarchyDaoInstance().getOrganizationUnitHierarchyByLevel(3);
            if(ouh ==null)
            ouh=new OrganizationUnitHierarchy();
            session.setAttribute("level3Ouh", ouh);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void setLevel4OuHierarchy(HttpSession session)
    {
        try
        {
            DaoUtility daoutil=new DaoUtility();
            OrganizationUnitHierarchy ouh=daoutil.getOrganizationUnitHierarchyDaoInstance().getOrganizationUnitHierarchyByLevel(4);
            if(ouh ==null)
            ouh=new OrganizationUnitHierarchy();
            session.setAttribute("level4Ouh", ouh);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public SiteSetup getSiteSetup(String userName)
    {
        SiteSetup siteSetup=null;
        try
        {
            DaoUtility daoutil=new DaoUtility();
            siteSetup=daoutil.getSiteSetupDaoInstance().getSiteSetup(userName);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return siteSetup;
    }
}
