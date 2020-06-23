/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.controller;

import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.ovc.business.Partner;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.CustomIndicatorsReportDao;
import com.nomis.ovc.dao.CustomIndicatorsReportDaoImpl;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import com.nomis.reports.utils.CustomIndicatorsReportManager;
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
public class CustomIndicatorsDataGenerationAction extends org.apache.struts.action.Action {

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
        CustomIndicatorsDataGenerationForm cidgform=(CustomIndicatorsDataGenerationForm)form;
        AppUtility appUtil=new AppUtility();
        DaoUtility util=new DaoUtility();
        DateManager dm=new DateManager();
        //cidgform
        HttpSession session=request.getSession();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        ouaManager.getLevel2OrganizationUnitForReports(session);
        ReportParameterTemplate rpt=new ReportParameterTemplate();
        AccessManager acm=new AccessManager();
        //OrganizationUnitAttributesManager ouam=new OrganizationUnitAttributesManager();
        //String partnerCode=cidgform.getPartnerCode();
        
        String userName=appUtil.getCurrentUser(session);
        
        //if(!acm.isUserInRole("cirb"))
        session.setAttribute("cirbGenButtonDisabled", "true");
        //else
        session.setAttribute("cirbGenButtonDisabled", "false");
        
        //acm.setStateListForReports(session);
        
        int startMth=appUtil.getMonthAsNumber(cidgform.getStartMth());
        int startYear=cidgform.getStartYear();
        int endMth=appUtil.getMonthAsNumber(cidgform.getEndMth());
        int endYear=cidgform.getEndYear();
        String selectedPartner=cidgform.getPartnerCode();
        
        rpt.setStartMth(startMth);
        rpt.setStartYear(startYear);
        rpt.setEndMth(endMth);
        rpt.setEndYear(endYear);
        rpt.setStartDate(dm.getStartDate(startMth, startYear));
        rpt.setEndDate(dm.getEndDate(endMth, endYear));
        rpt.setPartnerCode(selectedPartner);
        //String[] dateParams={startMth+"",startYear+"",endMth+"",endYear+""};
        //String startDate=util.getStartDate(dateParams);
        //String endDate=util.getEndDate(dateParams);
        String[] selectedStates=cidgform.getLevel2OuCodes();
        String[] selectedIndicators=cidgform.getIndicators();
        
        String requiredAction=cidgform.getActionName();
        List listOfMonths=dm.generateMonths(1);
        List listOfYears=DateManager.generateYears();
        session.setAttribute("generatedYears", listOfYears);
        session.setAttribute("generatedMonths", listOfMonths);
        //String currentUser=appUtil.getCurrentUser(session);
        
        List partnerList=ouaManager.getPartnerListForReports(rpt);
        session.setAttribute("cidgPartnerList", partnerList);
        //if(currentUser==null)
        //return mapping.findForward("login");
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        setPartnerList(user,session);
        List indicatorList=CustomIndicatorsReportManager.getCustomIndicators();
        session.setAttribute("customIndicatorList", indicatorList);
        /*List partnerList=acm.getPartnerListForReports(session);
        session.setAttribute("partnerList", partnerList);*/
        System.err.println("requiredAction is "+requiredAction);
        
        if(requiredAction==null)
        {
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level2OuList"))
        {
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("deleteData"))
        {
            String level2OuId=null;
            int count=0;
            String level2OuName=null;
            CustomIndicatorsReportDao cirbdao=new CustomIndicatorsReportDaoImpl();
            for(int j=0; j<selectedStates.length; j++)
            {
               level2OuId=selectedStates[j];
               level2OuName=ouaManager.getOrganizationUnitName(level2OuId);
               count+=cirbdao.deleteAllCustomIndicatorsReports(level2OuName);
            }
            System.err.println(count+" deleted");
        }
        else if(requiredAction.equalsIgnoreCase("generateData"))
        {
            try
                {
                    CustomIndicatorsReportManager cirm=new CustomIndicatorsReportManager();
                    //ReportParameterTemplate rpt=new ReportParameterTemplate();
                    List mainList=new ArrayList();
                    String level2OuId=null;
                    String level3OuId="All";
                    String cboId="All";
                    String partnerCode=selectedPartner;
                    rpt.setCboId(cboId);
                    List listFromArray=getListFromArray(selectedIndicators);
                    if(selectedStates !=null && selectedStates.length>0)
                    {
                        //List paramList=getParamList(level2OuId,level3OuId,cboId,startMth,startYear,endMth,endYear,partnerCode);
                        //OvcQuarterlyReport quarterlyReport=new OvcQuarterlyReport();
                        List level3OuList=null;
                        List level4OuList=null;
                        List partnerCodeList=new ArrayList();
                        List list=null;
                        if(selectedStates.length==1)
                        {
                            level2OuId=selectedStates[0];
                            rpt.setLevel2OuId(level2OuId);
                            //paramList.set(0, level2OuId);
                            String level2OuName=ouaManager.getOrganizationUnitName(level2OuId);
                            if(level2OuName !=null)
                            level2OuName=level2OuName.replaceAll(" ", "_");
                            if(selectedPartner==null || selectedPartner.equalsIgnoreCase("All") || selectedPartner.equalsIgnoreCase("select") || selectedPartner.trim().length()==0)
                            partnerCodeList=util.getHouseholdEnrollmentDaoInstance().getDistinctPartnerCodes(rpt);
                            else
                            partnerCodeList.add(selectedPartner);
                            if(partnerCodeList !=null)
                            {
                                OrganizationUnit ou=null;
                                for(Object s:partnerCodeList)
                                {
                                    partnerCode=(String)s;
                                    rpt.setPartnerCode(partnerCode);
                                    level4OuList=util.getHouseholdEnrollmentDaoInstance().getDistinctLevel4OrganizationUnit(rpt);
                                    level3OuList=util.getOrganizationUnitDaoInstance().getParentOuList(level4OuList);
                                    for(int i=0; i<level3OuList.size(); i++)
                                    {
                                      ou=(OrganizationUnit)level3OuList.get(i);
                                      level3OuId=ou.getUid();
                                      rpt.setLevel3OuId(level3OuId);
                                      //paramList.set(1, level3OuId);
                                      //paramList.set(8, partnerCode);
                                      cirm.processCustomIndicatorsByLga(rpt,listFromArray,userName);
                                      //mainList.add(quarterlyReport.getQuarterlyReport(paramList, level2OuId)); 
                                    }
                                }
                            }
                        }
                        else
                        {
                            for(int j=0; j<selectedStates.length; j++)
                            {
                               level2OuId=selectedStates[j];
                               rpt.setLevel2OuId(level2OuId);
                               if(selectedPartner==null || selectedPartner.equalsIgnoreCase("All"))
                                partnerCodeList=util.getHouseholdEnrollmentDaoInstance().getDistinctPartnerCodes(rpt);
                                else
                                partnerCodeList.add(selectedPartner);
                               
                                if(partnerCodeList !=null)
                                {
                                    for(Object s:partnerCodeList)
                                    {
                                        partnerCode=(String)s;
                                        rpt.setPartnerCode(partnerCode);
                                        //paramList.set(0, level2OuId);
                                        //paramList.set(8, partnerCode);
                                        level4OuList=util.getHouseholdEnrollmentDaoInstance().getDistinctLevel4OrganizationUnit();
                                        level3OuList=util.getOrganizationUnitDaoInstance().getParentOuList(level4OuList);
                                        for(int i=0; i<level3OuList.size(); i++)
                                        {
                                          level3OuId=(String)level3OuList.get(i);
                                          rpt.setLevel3OuId(level3OuId);
                                          //paramList.set(1, level3OuId);
                                          cirm.processCustomIndicatorsByLga(rpt,listFromArray,userName);
                                          //mainList.add(quarterlyReport.getQuarterlyReport(paramList, level2OuId)); 
                                        }
                                       System.err.println("Quaterly_Report size is "+mainList.size());
                                    }
                                }
                            }
                        }
                    }
                }
                catch(Exception ex)
                {
                    NomisLogManager.logStackTrace(ex);
                    //ex.printStackTrace();
                }
                cidgform.reset(mapping, request);
                return mapping.findForward(SUCCESS);
        }
        return mapping.findForward(SUCCESS);
    }
    private List getParamList(String level2OuId,String level3OuId,String cboId,int startMth,int startYr,int endMth,int endYr,String partnerCode)
    {
        List paramList=new ArrayList();
        
        paramList.add(level2OuId);
        paramList.add(level3OuId);
        paramList.add(cboId);
        paramList.add("All");
        paramList.add(startMth);
        paramList.add(startYr);
        paramList.add(endMth);
        paramList.add(endYr);
        paramList.add(partnerCode);
        return paramList;
    }
    private void setPartnerList(User user,HttpSession session)
    {
        String partnerCodes=null;
        DaoUtility util=new DaoUtility();
        try
        {
            if(user !=null)
            {
                List partnerList=new ArrayList();
                if(user.isSuperUser())
                {
                   partnerList=util.getPartnerDaoInstance().getAllPartners(); 
                }
                else
                {
                    partnerCodes=user.getPartnerCodes();
                    if(partnerCodes !=null)
                    {
                        String[] partnerCodeArray=partnerCodes.split(",");
                        if(partnerCodeArray !=null)
                        {
                            Partner partner=null;
                            for(int i=0; i<partnerCodeArray.length; i++)
                            {
                                partner=util.getPartnerDaoInstance().getPartner(partnerCodeArray[i]);
                                partnerList.add(partner);
                            }
                        }
                    }
                }
                session.setAttribute("userAssignedPartners", partnerList);
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
    }
    private List getListFromArray(String[] selectedIndicators)
    {
        List list=new ArrayList();
        if(selectedIndicators !=null)
        {
            for(int i=0; i<selectedIndicators.length; i++)
            {
                list.add(selectedIndicators[i]);
            }
        }
        return list;
    }
    private List getLevel3OuList(List level4OuList)
    {
        List level3OuList=new ArrayList();
        try
        {
        DaoUtility util=new DaoUtility();
        OrganizationUnit level3Ou=null;
        OrganizationUnit level4Ou=null;
        if(level4OuList !=null)
        {
            String level4OuId=null;
            for(Object obj:level4OuList)
            {
                level4OuId=(String)obj;
                level4Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(level4OuId);
                if(level4Ou !=null)
                {
                    level3Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(level4Ou.getParentId());
                    if(level3Ou !=null)
                    {
                        level3OuList.add(level3Ou);
                    }
                }
            }
        }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return level3OuList;
    }
}
