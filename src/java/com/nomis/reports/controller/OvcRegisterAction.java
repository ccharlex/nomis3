/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.BeneficiaryDetailsManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.CareAndSupportChecklist;
import com.nomis.ovc.business.CaregiverAccessToEmergencyFund;
import com.nomis.ovc.business.CareplanAchievementChecklist;
import com.nomis.ovc.business.ChildService;
import com.nomis.ovc.business.HivRiskAssessment;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.HouseholdService;
import com.nomis.ovc.business.NutritionAssessment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.SubQueryGenerator;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.ExcelWriter;
import com.nomis.reports.utils.ReportParameterManager;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jxl.write.WritableWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author smomoh
 */
public class OvcRegisterAction extends org.apache.struts.action.Action {

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
        OvcRegisterForm hhrf=(OvcRegisterForm)form;
        HttpSession session=request.getSession();
        DaoUtility util=new DaoUtility();
        ReportParameterManager rpm=new ReportParameterManager();
        //HouseholdEnrollmentDao hhedao=util.getHouseholdEnrollmentDaoInstance();
        String requiredAction=hhrf.getActionName();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        String userName=appManager.getCurrentUserName(session);
        String level2OuId=hhrf.getLevel2OuId();
        String level3OuId=hhrf.getLevel3OuId();
        String level4OuId=hhrf.getOrganizationUnitId();
        String cboId=hhrf.getCboId();
        String startDate=DateManager.processMthDayYearToMysqlFormat(hhrf.getStartDate());
        String endDate=DateManager.processMthDayYearToMysqlFormat(hhrf.getEndDate());
        
        ReportParameterTemplate rpt=new ReportParameterTemplate();
        rpt.setCboId(cboId);
        rpt.setLevel2OuId(level2OuId);
        rpt.setLevel3OuId(level3OuId);
        rpt.setLevel4OuId(level4OuId);
        rpt.setStartDate(startDate);
        rpt.setEndDate(endDate);
        
        SubQueryGenerator sqg=new SubQueryGenerator();
        String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
        
        if(AccessManager.isDue())
        {
            setButtonState(session,"true","true");
            request.setAttribute("accessErrorMsg", AppConstant.LICENCE_ACCESS_MSG);
            return mapping.findForward(SUCCESS);
        }
        else
        {
            setButtonState(session,"false","true");
        }
        String registerType=hhrf.getRegisterType();
        ouaManager.getLevel2OrganizationUnitForReports(session);
        ouaManager.setOrganizationUnitAttributesByOuId(request, level2OuId, level3OuId, level4OuId,cboId);
        ouaManager.setOrganizationUnitHierarchyAttributes(session);
                
        String reportPeriod=DateManager.getDateParameter(startDate, endDate);
        request.setAttribute("reportPeriod", reportPeriod);
        
        System.err.println("requiredAction is "+requiredAction);
        System.err.println("registerType is "+registerType);
        String id=request.getParameter("id");
        String ovcId=null;
        String cgiverId=null;
        String hhUniqueId=null;
        if(id !=null )
        {
            requiredAction=id;
            if(id.indexOf(":") !=-1)
            {
                String[] paramArray=id.split(":");
                requiredAction=paramArray[0];
                if(paramArray.length>1)
                {
                    if(requiredAction.equalsIgnoreCase("childHistory"))
                    ovcId=paramArray[1];
                    else if(requiredAction.equalsIgnoreCase("cgHistory"))
                    cgiverId=paramArray[1];
                    else
                    hhUniqueId=paramArray[1];
                }
            }
            System.err.println("requiredAction is "+requiredAction);
        }
        if(requiredAction==null)
        {
            hhrf.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("cboList"))
        {
            ouaManager.getLevel3OrganizationUnitForReports(session,level2OuId);
            ouaManager.getLevel4OrganizationUnitForReports(session,null);
            ouaManager.setCBOListByLevel2Ou(session, level2OuId);
            //ouaManager.setAssignedLevel3Ou(session, hhrf.getCboId());
            session.setAttribute("assignedLevel3OuList", new ArrayList());
            ouaManager.setLevel4OuList(session, null);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level3OuList"))
        {
            
            //ouaManager.setCBOListByLevel2Ou(session, level2OuId);
            //ouaManager.setAssignedLevel3Ou(session, hhrf.getCboId());
            //session.setAttribute("assignedLevel3OuList", new ArrayList());
            //ouaManager.setLevel4OuList(session, null);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level4OuList"))
        {
            ouaManager.getLevel4OrganizationUnitForReports(session,level3OuId);
            //ouaManager.setLevel4OuList(session, level3OuId);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("register"))
        {
            if(registerType.equalsIgnoreCase("childRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List hheList=new ArrayList();
                List list=daoutil.getChildEnrollmentDaoInstance().getOvcRecords(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, AppConstant.HIV_ALL_STATUS_NUM, startDate, endDate, 0, 25, null, false);
                if(list==null)
                list=new ArrayList();
                Ovc ovc=null;
                for(int i=0; i<list.size(); i++)
                {
                    ovc=(Ovc)list.get(i);
                    ovc=BeneficiaryDetailsManager.getPreparedOvc(ovc, user);
                    ovc.setSerialNo(i+1);
                    if(i%2>0)
                    ovc.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    hheList.add(ovc);
                }
                session.setAttribute("ovcListForRegister", hheList);
                request.setAttribute("reportType", "Child Register");
                hhrf.reset(mapping, request);
                return mapping.findForward("childregister");
            }
            else if(registerType.equalsIgnoreCase("caregiverRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List beneficiaryList=new ArrayList();
                List idList=new ArrayList();
                List list=daoutil.getAdultHouseholdMemberDaoInstance().getAllAdultHouseholdMembers(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                //.getAllAdultHouseholdMembers();
                if(list==null)
                list=new ArrayList();
                AdultHouseholdMember ahm=null;
                for(int i=0; i<list.size(); i++)
                {
                    ahm=(AdultHouseholdMember)list.get(i);
                    if(!idList.contains(ahm.getBeneficiaryId()))
                    {
                        ahm=BeneficiaryDetailsManager.getPreparedAdultHouseholdMember(ahm, user);
                        ahm.setSerialNo(i+1);
                        if(i%2>0)
                        ahm.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                        beneficiaryList.add(ahm);
                        idList.add(ahm.getBeneficiaryId());
                    }
                }
                request.setAttribute("reportType", "Caregiver Register");
                session.setAttribute("cgiverListForRegister", beneficiaryList);
                hhrf.reset(mapping, request);
                return mapping.findForward("cgiverRegister");
            }
            else if(registerType.equalsIgnoreCase("householdRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List hheList=new ArrayList();
                List list=daoutil.getHouseholdEnrollmentDaoInstance().getHouseholdEnrollmentAndRevisedAssessmentRecords(rpt);
                
                if(list==null)
                list=new ArrayList();
                HouseholdEnrollment hhp=null;
                //HouseholdEnrollment hhe=null;
                for(int i=0; i<list.size(); i++)
                {
                    hhp=(HouseholdEnrollment)list.get(i);
                    hhp.setSerialNo(i+1);
                    if(i%2>0)
                    hhp.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    hheList.add(hhp);
                }
                request.setAttribute("reportType", "Household Register");
                session.setAttribute("hheListForRegister", hheList);
                hhrf.reset(mapping, request);
                return mapping.findForward("householdRegister");
            }
            else if(registerType.equalsIgnoreCase("ovcServiceRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List serviceList=new ArrayList();
                List list=daoutil.getChildServiceDaoInstance().getChildServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 24, null, false);
                if(list==null)
                list=new ArrayList();
                ChildService service=null;
                for(int i=0; i<list.size(); i++)
                {
                    service=(ChildService)list.get(i);
                    service.setSerialNo(i+1);
                    if(i%2>0)
                    service.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    serviceList.add(service);
                }
                request.setAttribute("reportType", "OVC Service Register");
                session.setAttribute("ovcServiceListForRegister", serviceList);
                hhrf.reset(mapping, request);
                return mapping.findForward("ovcServiceRegister");
            }
            else if(registerType.equalsIgnoreCase("householdServiceRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List serviceList=new ArrayList();
                List list=daoutil.getHouseholdServiceDaoInstance().getHouseholdServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                if(list==null)
                list=new ArrayList();
                HouseholdService service=null;
                for(int i=0; i<list.size(); i++)
                {
                    service=(HouseholdService)list.get(i);
                    service.setSerialNo(i+1);
                    if(i%2>0)
                    service.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    serviceList.add(service);
                }
                request.setAttribute("reportType", "Household Service Register");
                session.setAttribute("householdServiceListForRegister", serviceList);
                hhrf.reset(mapping, request);
                return mapping.findForward("householdServiceRegister");
            }
            /*else if(registerType.equalsIgnoreCase("referralRegister"))
            {
                DaoUtility daoutil=new DaoUtility();
                List referralList=new ArrayList();
                List list=daoutil.getHouseholdReferralDaoInstance().getHouseholdReferralRecordsForExport(rpt);//.getAllReferralServices();
                if(list==null)
                list=new ArrayList();
                HouseholdReferral referral=null;
                Ovc ovc=null;
                AdultHouseholdMember ahm=null;
                for(int i=0; i<list.size(); i++)
                {
                    referral=(HouseholdReferral)list.get(i);
                    referral.setSerialNo(i+1);
                    if(i%2>0)
                    referral.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    if(referral.getBeneficiaryType()==AppConstant.OVC_TYPE_NUM);
                    {
                        ovc=util.getOvcDaoInstance().getOvc(referral.getBeneficiaryId());
                        if(ovc !=null)
                        {
                            ovc=BeneficiaryDetailsManager.getPreparedOvc(ovc, user);
                            referral.setBeneficiaryFirstName(ovc.getFirstName());
                            referral.setBeneficiarySurname(ovc.getSurname());
                            referral.setBeneficiaryCurrentAge(ovc.getCurrentAge());
                            referral.setBeneficiaryDateOfEnrollment(ovc.getDateOfEnrollment());
                            referral.setBeneficiarySex(ovc.getSex());
                            referral.setBeneficiaryHivStatus(HivPropertiesManager.getHivStatus(ovc.getCurrentHivStatusCode()).getName());
                            
                            referral.setHouseholdProfile(ovc.getHhe());
                            referralList.add(referral);
                        }
                    }
                    if(referral.getBeneficiaryType()==AppConstant.CAREGIVER_TYPE_NUM)
                    {
                        ahm=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(referral.getBeneficiaryId());
                        if(ahm !=null)
                        {
                            ahm=BeneficiaryDetailsManager.getPreparedAdultHouseholdMember(ahm, user);
                            referral.setBeneficiaryFirstName(ahm.getFirstName());
                            referral.setBeneficiarySurname(ahm.getSurname());
                            referral.setBeneficiaryCurrentAge(ahm.getCurrentAge());
                            referral.setBeneficiaryDateOfEnrollment(ahm.getDateOfEnrollment());
                            referral.setBeneficiarySex(ahm.getSex());
                            referral.setBeneficiaryHivStatus(HivPropertiesManager.getHivStatus(ahm.getCurrentHivStatusCode()).getName());
                            referral.setHouseholdProfile(ahm.getHhe());
                            //HivPropertiesManager.getHivStatus(currentHivStatusCode);
                            referralList.add(referral);
                        }
                    }
                    System.err.println("referral record at "+i+" is "+referral.getBeneficiaryFirstName()+referral.getBeneficiarySurname()+" Type: "+referral.getBeneficiaryType());
                }
                request.setAttribute("reportType", "Referral Register");
                session.setAttribute("ovcReferralListForRegister", referralList);
                hhrf.reset(mapping, request);
                return mapping.findForward("referralRegister");
            }*/
            else if(registerType.equalsIgnoreCase("hivRiskAssessmentRegister"))
            {
                System.err.println("registerType in the inner block is "+registerType);
                DaoUtility daoutil=new DaoUtility();
                List hraList=new ArrayList();
                List list=daoutil.getHivRiskAssessmentDaoInstance().getHivRiskAssessmentRecords(rpt);
                //.getHouseholdServiceDaoInstance().getHouseholdServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                if(list==null)
                list=new ArrayList();
                HivRiskAssessment hra=null;
                for(int i=0; i<list.size(); i++)
                {
                    hra=(HivRiskAssessment)list.get(i);
                    hra.setSerialNo(i+1);
                    if(i%2>0)
                    hra.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    hraList.add(hra);
                }
                request.setAttribute("reportType", "HIV Risk assessment Register");
                session.setAttribute("hraForRegister", hraList);
                hhrf.reset(mapping, request);
                System.err.println("hraRegister is hraRegister");
                return mapping.findForward("hraRegister");
            }
            else if(registerType.equalsIgnoreCase("careAndSupportRegister"))
            {
                System.err.println("registerType in the inner block is "+registerType);
                DaoUtility daoutil=new DaoUtility();
                List hraList=new ArrayList();
                List list=daoutil.getCareAndSupportChecklistDaoInstance().getCareAndSupportRecordsForExport(rpt);
                //.getHouseholdServiceDaoInstance().getHouseholdServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                if(list==null)
                list=new ArrayList();
                CareAndSupportChecklist casc=null;
                for(int i=0; i<list.size(); i++)
                {
                    casc=(CareAndSupportChecklist)list.get(i);
                    casc.setSerialNo(i+1);
                    if(i%2>0)
                    casc.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    hraList.add(casc);
                }
                request.setAttribute("reportType", "Care and Support Register");
                session.setAttribute("careAndSupportListForRegister", hraList);
                hhrf.reset(mapping, request);
                //System.err.println("hraRegister is hraRegister");
                return mapping.findForward("careAndSupportRegister");
            }
            else if(registerType.equalsIgnoreCase("careAndSupportExcelTemplate"))
            {
                DaoUtility daoutil=new DaoUtility();
                List list=daoutil.getCareAndSupportChecklistDaoInstance().getMostRecentCareAndSupportRecords(rpt);
                
                if(list !=null)
                {
                    try
                    {
                        String fileName="CareAndSupportReport"+DateManager.getDefaultCurrentDateAndTime();
                        response.setContentType("application/vnd.ms-excel");
                        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
                        OutputStream os=response.getOutputStream();
                        ExcelWriter ew=new ExcelWriter();
                        WritableWorkbook wworkbook=ew.writeCareAndSupportRegisterToExcel(os, list) ;
                        if(wworkbook !=null)
                        {
                            wworkbook.write();
                            wworkbook.close();
                        }
                        os.close();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    hhrf.reset(mapping, request);
                }
                return null;
            }
            else if(registerType.equalsIgnoreCase("nutritionAssessment"))
            {
                DaoUtility daoutil=new DaoUtility();
                List naList=new ArrayList();
                List list=daoutil.getNutritionalAssessmentDaoInstance().getNutritionAssessmentRecords(rpt);
                
                if(list==null)
                list=new ArrayList();
                NutritionAssessment na=null;
                for(int i=0; i<list.size(); i++)
                {
                    na=(NutritionAssessment)list.get(i);
                    na.setSerialNo(i+1);
                    if(i%2>0)
                    na.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    naList.add(na);
                }
                request.setAttribute("reportType", "Nutrition assessment Register");
                session.setAttribute("nutritionAssessmentListForRegister", naList);
                hhrf.reset(mapping, request);
                return mapping.findForward("nutritionAssessmentRegister");
                
            }
            else if(registerType.equalsIgnoreCase("caregiverAccessToEmergencyFund"))
            {
                DaoUtility daoutil=new DaoUtility();
                List caefList=new ArrayList();
                List list=daoutil.getCaregiverAccessToEmergencyFundDaoInstance().getCaregiverAccessToEmergencyFundRecords(rpt);
                
                if(list==null)
                list=new ArrayList();
                CaregiverAccessToEmergencyFund caef=null;
                for(int i=0; i<list.size(); i++)
                {
                    caef=(CaregiverAccessToEmergencyFund)list.get(i);
                    caef.setSerialNo(i+1);
                    if(i%2>0)
                    caef.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    caefList.add(caef);
                }
                request.setAttribute("reportType", "Caregiver access to emergency fund Register");
                session.setAttribute("caregiverAccessToEmergencyFundListForRegister", caefList);
                hhrf.reset(mapping, request);
                return mapping.findForward("caregiverAccessToEmergencyFundRegister");
                
            }
            else if(registerType.equalsIgnoreCase("careplanAchievementChecklist"))
            {
                DaoUtility daoutil=new DaoUtility();
                List cpaList=new ArrayList();
                List list=daoutil.getCareplanAchievementChecklistDaoInstance().getCareplanAchievementChecklistRecords(rpt);
                
                if(list==null)
                list=new ArrayList();
                CareplanAchievementChecklist cpa=null;
                for(int i=0; i<list.size(); i++)
                {
                    cpa=(CareplanAchievementChecklist)list.get(i);
                    cpa.setSerialNo(i+1);
                    if(i%2>0)
                    cpa.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    cpaList.add(cpa);
                }
                request.setAttribute("reportType", "Careplan achievement Register");
                session.setAttribute("careplanAchievementListForRegister", cpaList);
                hhrf.reset(mapping, request);
                return mapping.findForward("careplanAchievementRegister"); 
            }
            /*else if(registerType.equalsIgnoreCase("graduationBenchmark"))
            {
                System.err.println("registerType in the inner block is "+registerType);
                DaoUtility daoutil=new DaoUtility();
                List gbmList=new ArrayList();
                List list=daoutil.getGraduationBenchmarkAchievementDaoInstance().getGraduationBenchmarkAchievementRecords();
                //.getHouseholdServiceDaoInstance().getHouseholdServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                if(list==null)
                list=new ArrayList();
                GraduationBenchmarkAchievement gbm=null;
                for(int i=0; i<list.size(); i++)
                {
                    gbm=(GraduationBenchmarkAchievement)list.get(i);
                    gbm.setSerialNo(i+1);
                    if(i%2>0)
                    gbm.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    gbmList.add(gbm);
                }
                request.setAttribute("reportType", "Graduation Benchmark Register");
                session.setAttribute("gbmForRegister", gbmList);
                hhrf.reset(mapping, request);
                //System.err.println("hraRegister is hraRegister");
                return mapping.findForward("gbmRegister");
            }
            else if(registerType.equalsIgnoreCase("trainingRegister"))
            {
                System.err.println("registerType in the inner block is "+registerType);
                DaoUtility daoutil=new DaoUtility();
                List trnList=new ArrayList();
                List list=daoutil.getTrainingDaoInstance().getTrainings();
                //.getHouseholdServiceDaoInstance().getHouseholdServices(orgUnitQuery, AppConstant.EVER_ENROLLED_NUM, 0, startDate, endDate, 0, 200, null, false);
                if(list==null)
                list=new ArrayList();
                Training trn=null;
                for(int i=0; i<list.size(); i++)
                {
                    trn=(Training)list.get(i);
                    trn.setSerialNo(i+1);
                    if(i%2>0)
                    trn.setRowColor(AppConstant.SECONDREPORTROWCOLOUR);
                    trnList.add(trn);
                }
                request.setAttribute("reportType", "Training Register");
                session.setAttribute("trainingListForRegister", trnList);
                hhrf.reset(mapping, request);
                //System.err.println("hraRegister is hraRegister");
                return mapping.findForward("trainingRegister");
            }
        }*/
        }
        return mapping.findForward(SUCCESS);
    }
    public void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("licSaveDisabled", saveDisabled);
    }
}
