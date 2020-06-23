/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.exportimport.controller;

import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.exportimport.XMLDataExportManager;
import com.nomis.exportimport.ZipHandler;
import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.ovc.business.CommunityBasedOrganization;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author smomoh
 */
public class XMLDataExportAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        XMLDataExportForm xmldeform=(XMLDataExportForm)form;
        HttpSession session=request.getSession();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        AppManager appManager=new AppManager();
        String moduleName="Data export";
        User user=appManager.getCurrentUser(session);
        if(AccessManager.isUserInDataExportRole(user))
        {
            setButtonState(session,"false","true");
        }
        else
        {
            setButtonState(session,"true","true");
            request.setAttribute("accessErrorMsg", AppConstant.DEFAULT_ACCESS_MSG);
            return mapping.findForward(SUCCESS);
        }
        String userName=appManager.getCurrentUserName(session);
        ouaManager.setOrganizationUnitAttributesForReports(session);
        ouaManager.setOrganizationUnitHierarchyAttributes(session);
        
        String level2OuId=xmldeform.getLevel2OuId();
        String level3OuId=xmldeform.getLevel3OuId();
        String level4OuId=xmldeform.getOrganizationUnitId();
        String cboId=xmldeform.getCboId();
        String startDate=DateManager.processMthDayYearToMysqlFormat(xmldeform.getStartDate());
        String endDate=DateManager.processMthDayYearToMysqlFormat(xmldeform.getEndDate());
        ouaManager.setOrganizationUnitAttributes(session, level3OuId, userName, cboId);
        //ouaManager.setOrganizationUnitAttributesByOuId(session, level2OuId, level3OuId, level4OuId,cboId);
        
        String requiredAction=xmldeform.getActionName();
        System.err.println("requiredAction is "+requiredAction);
        
        if(requiredAction==null)
        {
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level3OuList") || requiredAction.equalsIgnoreCase("cboList"))
        {
            ouaManager.getLevel3OrganizationUnitForReports(session,level2OuId);
            ouaManager.getLevel4OrganizationUnitForReports(session,null);
            ouaManager.setCBOListByLevel2Ou(session, level2OuId);
            session.setAttribute("assignedLevel3OuList", new ArrayList());
            ouaManager.setLevel4OuList(session, null);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level4OuList"))
        {
            ouaManager.getLevel4OrganizationUnitForReports(session,level3OuId);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("dbexport"))
        {
            
            AppUtility appUtil=new AppUtility();
            ReportParameterTemplate rpt=new ReportParameterTemplate();
            rpt.setCboId(cboId);
            rpt.setLevel2OuId(level2OuId);
            rpt.setLevel3OuId(level3OuId);
            rpt.setLevel4OuId(level4OuId);
            rpt.setStartDate(startDate);
            rpt.setEndDate(endDate);
            
            appUtil.deleteFiles(appUtil.getExportFilePath());
            appUtil.createExportImportDirectories();
            XMLDataExportManager xmlde=new XMLDataExportManager();
            xmlde.exportHouseholdEnrollmentRecordsInXml(null, rpt);
            xmlde.exportAdultHouseholdMemberRecordsInXml(null, rpt);
            xmlde.exportHouseholdVulnerabilityAssessmentRecordsInXml(null, rpt);
            xmlde.exportRevisedHouseholdVulnerabilityAssessmentRecordsInXml(null, rpt);
            xmlde.exportOvcRecordsInXml(null, rpt);
            xmlde.exportOvcServiceRecordsInXml(null, rpt);
            xmlde.exportHouseholdServiceRecordsInXml(null, rpt);
            xmlde.exportHivRiskAssessmentRecordsInXml(null, rpt);
            xmlde.exportReferralRecordsInXml(null, rpt);
            xmlde.exportCaregiverAccessToEmergencyFundRecordsInXml(null, rpt);
            xmlde.exportCareplanAchievementChecklistRecordsInXml(null, rpt);
            xmlde.exportChildEducationPerformanceAssessmentRecordsInXml(null, rpt);
            xmlde.exportHivCareAreSupportRecordsInXml(null, rpt);
            xmlde.exportBeneficiaryStatusUpdateRecordsInXml(null, rpt);
            xmlde.exportNutritionAssessmentRecordsInXml(null, rpt);
            xmlde.exportHouseholdCareplanRecordsInXml(null, rpt);
            
            String zipFileName=getZipFileName(level2OuId,level3OuId,level4OuId,cboId,startDate,endDate);
            zipFiles(zipFileName);
            saveUserActivity(userName,moduleName,"Exported file named "+zipFileName);
            request.setAttribute("dbexportmsg", "Data exported to "+appUtil.getDbExportZipDirectory());
        }
        return mapping.findForward(SUCCESS);
    }
private void zipFiles(String fileName)
{
    ZipHandler zipHandler=new ZipHandler();
    AppUtility appUtil=new AppUtility();
    List listOfFilesToBeZipped=appUtil.copyFilePathsIntoList(appUtil.getExportFilePath());
    zipHandler.zipFile(listOfFilesToBeZipped, appUtil.getDbExportZipDirectory(), fileName);
}
private void saveUserActivity(String userName,String userAction,String description)
{
    UserActivityManager uam=new UserActivityManager();
    uam.saveUserActivity(userName, userAction,description);
}
private String getZipFileName(String level2OuId,String level3OuId,String level4OuId,String cboId,String startDate,String endDate)
{
    String zipFileName="";
    try
    {
        DaoUtility util=new DaoUtility();
        String provinceName="All";
        String districtName="All";
        String wardName="All";
        String cboName="All";
        OrganizationUnit ou=null;
        ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(level2OuId);
        if(ou !=null)
        provinceName=ou.getName();
        ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(level3OuId);
        if(ou !=null)
        districtName=ou.getName();
        ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(level4OuId);
        if(ou !=null)
        wardName=ou.getName();
        CommunityBasedOrganization cbo=util.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganization(cboId);
        if(cbo !=null)
        cboName=cbo.getCboName();
        zipFileName="Prov-"+provinceName+"-Dist-"+districtName+"-Ward-"+wardName+"-LIP-"+cboName+"-Period-"+startDate+"-"+endDate;
            
    }
    catch(Exception ex)
    {
        NomisLogManager.logStackTrace(ex);
    }
    return zipFileName;
}
private void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
{
    session.setAttribute("xmlDataExportButtonDisabled", saveDisabled);
    //session.setAttribute("userModifyDisabled", modifyDisabled);
}
}
