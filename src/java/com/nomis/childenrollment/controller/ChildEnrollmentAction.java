/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.childenrollment.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.CommunityWorkerRecordsManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.operationsManagement.VulnerabilityStatusManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.DatasetSetting;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.business.ReferralFacility;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.HivPropertiesManager;
import com.nomis.reports.utils.ReportParameterTemplate;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.DatabasetManager;
import com.nomis.ovc.util.ReferralFacilityManager;
import com.nomis.ovc.util.UniqueIdManager;
import java.util.ArrayList;
import java.util.Date;
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
public class ChildEnrollmentAction extends org.apache.struts.action.Action {

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
        ChildEnrollmentForm ceform=(ChildEnrollmentForm)form;
        HttpSession session=request.getSession();
        //session.setAttribute("mainHivStatus", HivPropertiesManager.getThreeMainHivStatus());
        VulnerabilityStatusManager.setVulnerabilityStatusForDisplay(session);
        
        String moduleName="Child enrollment";
        DaoUtility util=new DaoUtility();
        
        String requiredAction=ceform.getActionName();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        AppManager appManager=new AppManager();
        String userName=appManager.getCurrentUserName(session);
        String level2OuId=ceform.getLevel2OuId();
        String level3OuId=ceform.getLevel3OuId();
        String hhUniqueId=ceform.getHhUniqueId();
        ouaManager.setOrganizationUnitAttributes(session, level3OuId, userName, ceform.getCboId());
        User user=appManager.getCurrentUser(session);
        DatasetSetting dsts=util.getDatasetSettingDaoInstance().getDatasetSettingByModuleId(DatabasetManager.getChildEnrollmentModuleId());
        if(dsts !=null && dsts.getDatasetId().equalsIgnoreCase(DatabasetManager.getNatChildEnrollmentDatasetId()))
        {
            return mapping.findForward("NationalChildEnrollmentForm");
        }
        if(AccessManager.isUserInDataEntryRole(user))
        {
            //If it caregiver details that is required, do not change button state
            if(requiredAction !=null && !requiredAction.equalsIgnoreCase("adultMemberDetails"))
            setButtonState(session,"false","true");
        }
        else
        {
            setButtonState(session,"true","true");
            request.setAttribute("accessErrorMsg", AppConstant.DEFAULT_ACCESS_MSG);
            return mapping.findForward(SUCCESS);
        }
        /*List dayList=DateManager.generateDays(1);
        List monthList=DateManager.generateSingleValueMonths();
        List yearList=DateManager.generateYears();
        session.setAttribute("hhpDayList", dayList);
        session.setAttribute("hhpMonthList", monthList);
        session.setAttribute("hhpYearList", yearList);*/
        
        setChildrenListPerHousehold(session, hhUniqueId);
        setHouseholdMemberListPerHousehold(session, hhUniqueId);
        System.err.println("requiredAction is "+requiredAction);
        generateSchoolList(session,ceform);
        generateSchoolGradeList(session);
        CommunityWorkerRecordsManager.setEnumeratorsRegistrationList(session);
        loadfacility(session,level2OuId,level3OuId);
        HivPropertiesManager.setHivStatusList(session, HivPropertiesManager.getThreeMainHivStatus());
        //session.setAttribute("mainHivStatus", HivPropertiesManager.getThreeMainHivStatus());
        if(requiredAction==null)
        {
            ceform.reset(mapping, request);
            resetBaselineInfo(ceform);
            //reset school status properties
            setSchoolStatusProperties(session,"false");
            //reset HIV status properties
            setHIVStatusProperties(session,"false");
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level3OuList"))
        {
            ouaManager.setAssignedLevel3Ou(session, ceform.getCboId());
            ouaManager.setLevel4OuList(session, null);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level4OuList"))
        {
            ouaManager.setLevel4OuList(session, level3OuId);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("householdDetails"))
        {
            int hhSerialNo=ceform.getHhSerialNo();
            if(ceform.getBeneficiaryType()==3)
            {
                //if the unique id is that of a child
                //System.err.println("ceform.getBeneficiaryType() is "+ceform.getBeneficiaryType());
                //hhUniqueId=UniqueIdManager.cleanUniqueId(hhUniqueId);
                String enrolledChildId=ceform.getEnrolledChildId();
                Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(enrolledChildId);
                if(ovc !=null)
                hhUniqueId=ovc.getHhUniqueId();
                hhUniqueId=UniqueIdManager.cleanUniqueId(hhUniqueId);
                ceform.setEnrolledChildId(enrolledChildId);
                ceform.setOvcId(enrolledChildId);
                ceform=setOvcDetails(session,ceform);
                
            }
            ceform.setHhSerialNo(hhSerialNo);
            ceform.setHhUniqueId(hhUniqueId);
            setChildrenListPerHousehold(session, hhUniqueId);
            setHouseholdMemberListPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            loadCaregiversPerHousehold(session, hhUniqueId);
            ceform=setOrganizationUnitProperties(session, hhUniqueId,ceform,userName);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("childDetails"))
        {
            int hhSerialNo=ceform.getHhSerialNo();
            int childSerialNo=ceform.getChildSerialNo();
            hhUniqueId=ceform.getHhUniqueId();
            String ovcId=ceform.getOvcId();
            System.err.println("ovcId is "+ovcId);
            String enrolledChildId=ceform.getEnrolledChildId();
            ceform.reset(mapping, request);
            ceform.setOvcId(enrolledChildId);
            ceform.setHhSerialNo(hhSerialNo);
            ceform.setChildSerialNo(childSerialNo);
            ceform.setHhUniqueId(hhUniqueId);
            ceform.setEnrolledChildId(enrolledChildId);
            ceform=setOvcDetails(session,ceform);
            
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("childDetailsBySerialNo"))
        {
            int hhSerialNo=ceform.getHhSerialNo();
            int childSerialNo=ceform.getChildSerialNo();
            hhUniqueId=ceform.getHhUniqueId();
            String ovcId=ceform.getOvcId();
            System.err.println("ovcId is "+ovcId);
            //String enrolledChildId=ceform.getEnrolledChildId();
            ceform.reset(mapping, request);
            ceform.setOvcId(ovcId);
            ceform.setHhSerialNo(hhSerialNo);
            ceform.setChildSerialNo(childSerialNo);
            ceform.setHhUniqueId(hhUniqueId);
            ceform.setEnrolledChildId(ovcId);
            ceform=setOvcDetails(session,ceform);
            
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("adultMemberDetails"))
        {
            String beneficiaryId=ceform.getCaregiverId();
            AdultHouseholdMember ahm=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(beneficiaryId);
            if(ahm !=null)
            {
                ceform.setCaregiverAge(ahm.getAgeAtBaseline());
                ceform.setCaregiverPhone(ahm.getPhoneNumber());
                ceform.setCaregiverSex(ahm.getSex());
                ceform.setCaregiverHivStatus(ahm.getCurrentHivStatusObject().getName());
                ceform.setDateOfCaregiverHivStatus(DateManager.convertDateToString(ahm.getDateOfCurrentHivStatus(),DateManager.MM_DD_YYYY_SLASH));
                ceform.setCaregiverEnrolledOnTreatment(ahm.getEnrolledOnTreatmentObject().getCode());
                ceform.setDateCaregiverEnrolledOnTreatment(DateManager.convertDateToString(ahm.getDateEnrolledOnTreatment(),DateManager.MM_DD_YYYY_SLASH));
                ceform.setFacilityCaregiverEnrolled(getFacilityName(ahm.getHivTreatmentFacilityId()));
                Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(ceform.getOvcId());
                if(ovc !=null)
                {
                    ceform.setCaregiverRelationship(ovc.getCaregiverRelationship());
                }
            }
            else
            {
                ceform.setCaregiverAge(0);
                ceform.setCaregiverPhone(null);
                ceform.setCaregiverSex(null);
                ceform.setCaregiverHivStatus(null);
                ceform.setDateOfCaregiverHivStatus(null);
                ceform.setCaregiverEnrolledOnTreatment(null);
                ceform.setDateCaregiverEnrolledOnTreatment(null);
                ceform.setFacilityCaregiverEnrolled(null);
                ceform.setCaregiverRelationship(0);
            }
            return mapping.findForward(SUCCESS);
        }
        
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            Ovc ovc=getOvc(ceform,userName);
            util.getChildEnrollmentDaoInstance().saveOvc(ovc, true, true);
            ouaManager.setLevel4OuList(session, level3OuId);
            setChildrenListPerHousehold(session, hhUniqueId);
            setHouseholdMemberListPerHousehold(session, hhUniqueId);
            saveUserActivity(userName,moduleName,"Enrolled new Child with Id "+ovc.getOvcId());
            ceform.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("modify"))
        {
            String currentOvcId=ceform.getHiddenBeneficiaryId();
            String ovcId=ceform.getOvcId();
            
            
            Ovc ovc=getOvc(ceform,userName);
            System.err.println("currentOvcId is "+currentOvcId+" ovc.getDateOfEnrollment() is "+ovc.getDateOfEnrollment());
            
            util.getChildEnrollmentDaoInstance().updateOvc(ovc, true, true);
            ouaManager.setLevel4OuList(session, level3OuId);
            setChildrenListPerHousehold(session, hhUniqueId);
            setHouseholdMemberListPerHousehold(session, hhUniqueId);
            saveUserActivity(userName,moduleName,"Modified Child record with Id "+ovc.getOvcId());
            ceform.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("delete"))
        {
            Ovc ovc=getOvc(ceform,userName);
            if(ceform.getEnrolledChildId() !=null)
            ovc.setOvcId(ceform.getEnrolledChildId());
            util.getChildEnrollmentDaoInstance().markForDelete(ovc);
            ouaManager.setLevel4OuList(session, level3OuId);
            setChildrenListPerHousehold(session, hhUniqueId);
            setHouseholdMemberListPerHousehold(session, hhUniqueId);
            saveUserActivity(userName,moduleName,"Marked Child record with Id "+ovc.getOvcId()+" for delete");
            ceform.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        return mapping.findForward(SUCCESS);
    }
    private void saveUserActivity(String userName,String userAction,String description)
    {
        UserActivityManager uam=new UserActivityManager();
        uam.saveUserActivity(userName, userAction,description);
    }
    private void setChildrenListPerHousehold(HttpSession session, String hhUniqueId)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            List list=util.getChildEnrollmentDaoInstance().getOvcPerHousehold(hhUniqueId);
            if(list==null)
            list=new ArrayList();
            session.setAttribute("childrenList", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private Ovc getOvc(ChildEnrollmentForm ceform,String userName)
    {
        AppUtility appUtil=new AppUtility();
        String formDateOfHivStatus=ceform.getDateOfBaselineHivStatus();
        String formDateEnrolledOnTreatment=ceform.getDateOfBaselineHivStatus();
        if(formDateOfHivStatus==null || formDateOfHivStatus.indexOf("/")==-1)
        formDateOfHivStatus=DateManager.DEFAULT_DATE;
        //else
        //formDateOfHivStatus=DateManager.processMthDayYearToMysqlFormat(ceform.getDateOfEnrollment());
        
        if(ceform.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
        formDateEnrolledOnTreatment=DateManager.processMthDayYearToMysqlFormat(ceform.getDateEnrolledOnTreatment());
        else
        formDateEnrolledOnTreatment=DateManager.DEFAULT_DATE;
        
        Date dateOfEnrollment=DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(ceform.getDateOfEnrollment()));
        Date dateOfHivStatus=DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(formDateOfHivStatus));
        Ovc ovc=new Ovc();
        ovc.setBaselineBirthRegistrationStatus(ceform.getBirthCertificate());
        ovc.setCurrentBirthRegistrationStatus(ceform.getBirthCertificate());
        ovc.setCaregiverId(ceform.getCaregiverId());
        ovc.setCaregiverRelationship(ceform.getCaregiverRelationship());
        ovc.setEnrolledOnTreatment(ceform.getEnrolledOnTreatment());
        ovc.setDateOfBaselineHivStatus(dateOfHivStatus);
        ovc.setDateEnrolledOnTreatment(DateManager.getDateInstance(formDateEnrolledOnTreatment));
        if(ceform.getBaselineHivStatus()==AppConstant.HIV_UNKNOWN_NUM)
        {
           ovc.setDateOfBaselineHivStatus(DateManager.getDateInstance(DateManager.DEFAULT_DATE));
           ovc.setDateEnrolledOnTreatment(DateManager.getDateInstance(DateManager.DEFAULT_DATE));
           ovc.setEnrolledOnTreatment(0);
           ovc.setTreatmentId(null);
        }
        if(ceform.getBaselineHivStatus() ==AppConstant.HIV_POSITIVE_NUM)
        {
            ovc.setHivTreatmentFacilityId(ceform.getHivTreatmentFacilityId());
            ovc.setTreatmentId(ceform.getTreatmentId());
            if(ovc.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM)
            {
                ovc.setHivTreatmentFacilityId(null);
                ovc.setTreatmentId(null);
            }
        }
        else
        {
            ovc.setEnrolledOnTreatment(0);
            ovc.setDateEnrolledOnTreatment(DateManager.getDateInstance(DateManager.DEFAULT_DATE));
            ovc.setTreatmentId(null);
        }
        ovc.setCurrentEnrollmentStatus(AppConstant.ACTIVE_NUM);
        ovc.setDateOfCurrentStatus(dateOfEnrollment);
        ovc.setDateCreated(DateManager.getDateInstance(DateManager.getCurrentDate()));
        ovc.setDateOfEnrollment(dateOfEnrollment);
        ovc.setBaselineHivStatus(ceform.getBaselineHivStatus());
        ovc.setCurrentHivStatus(ovc.getBaselineHivStatus());
        ovc.setDateOfCurrentHivStatus(ovc.getDateOfBaselineHivStatus());
        ovc.setDateOfCurrentBirthRegStatus(dateOfEnrollment);
        //ovc.setDateOfCurrentHivStatus(dateOfHivStatus);
        
        ovc.setDateCasePlanDeveloped(DateManager.getDateInstance(DateManager.DEFAULT_DATE));
        
        
        //ovc.setCurrentHivStatus(ceform.getHivStatus());
        ovc.setEnrollmentId(ceform.getOvcId());
        ovc.setCommunityWorkerName(ceform.getVolunteerName());
        
        ovc.setFirstName(ceform.getFirstName());
        ovc.setSchoolGrade(ceform.getGrade());
        ovc.setHeight(ceform.getHeight());
        ovc.setHhUniqueId(ceform.getHhUniqueId());
        ovc.setLastModifiedDate(DateManager.getDateInstance(DateManager.getCurrentDate()));
        
        ovc.setOvcId(ceform.getOvcId());
        ovc.setPhoneNumber(ceform.getPhoneNumber());
        ovc.setRecordedBy(userName);
        ovc.setSchoolName(ceform.getSchoolName());
        ovc.setBaselineSchoolStatus(ceform.getSchoolStatus());
        ovc.setDateOfCurrentSchoolStatus(DateManager.getDateInstance(DateManager.DEFAULT_DATE));
        if(ovc.getBaselineSchoolStatus()==1)
        ovc.setDateOfCurrentSchoolStatus(ovc.getDateOfEnrollment());
        //ovc.setDateOfBaselineHivStatus(DateManager.getDateInstance(ceform.getDateOfHivStatus()));
        ovc.setSex(ceform.getSex());
        ovc.setSourceOfInfo(ceform.getSourceOfInfo());
        ovc.setSurname(ceform.getSurname());
        ovc.setVulnerabilityStatusCode(appUtil.concatStr(ceform.getVulnerabilityStatus(),null));
        ovc.setWeight(ceform.getWeight());
        ovc.setAgeAtBaseline(ceform.getAgeAtBaseline());
        ovc.setAgeUnitAtBaseline(ceform.getAgeUnitAtBaseline());
        ovc.setCurrentAge(ceform.getAgeAtBaseline());
        ovc.setCurrentAgeUnit(ceform.getAgeUnitAtBaseline());
                
        return ovc;
    }
    private void setHIVStatusProperties(HttpSession session,String disabled)
    {
        session.setAttribute("enrhivDisabled", disabled);
    }
    private void setSchoolStatusProperties(HttpSession session,String disabled)
    {
        session.setAttribute("schoolDisabled", disabled);
    }
    private ChildEnrollmentForm setOvcDetails(HttpSession session,ChildEnrollmentForm ceform)
    {
        try
        {
            System.err.println("ceform.getOvcId() is "+ceform.getOvcId());
            DaoUtility util=new DaoUtility();
            AppUtility appUtil=new AppUtility();
            Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(ceform.getOvcId());
            if(ovc !=null)
            {
                String dateOfBaselineHivStatus= DateManager.convertDateToString(ovc.getDateOfBaselineHivStatus(), DateManager.DB_DATE_FORMAT);
                String dateChildEnrolledOnTreatment= DateManager.convertDateToString(ovc.getDateEnrolledOnTreatment(), DateManager.DB_DATE_FORMAT);
                String dateOfEnrollment= DateManager.getMthDayYearFromMySqlDate(DateManager.convertDateToString(ovc.getDateOfEnrollment(), DateManager.DB_DATE_FORMAT));
                int childSerialNo=0;
                int hhSerialNo=0;
                String[] ovcIdArray=ovc.getOvcId().split("/");
                if(ovcIdArray !=null && ovcIdArray.length>1)
                {
                    childSerialNo=Integer.parseInt(ovcIdArray[ovcIdArray.length-1]);
                    hhSerialNo=Integer.parseInt(ovcIdArray[ovcIdArray.length-2]);
                    ceform.setChildSerialNo(childSerialNo);
                    ceform.setHhSerialNo(hhSerialNo);
                }
                AdultHouseholdMember ahm=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(ovc.getCaregiverId());
                if(ahm !=null)
                {
                    ceform.setCaregiverPhone(ahm.getPhoneNumber());
                    ceform.setCaregiverAge(ahm.getAgeAtBaseline());
                    ceform.setCaregiverSex(ahm.getSex());
                    ceform.setCaregiverEnrolledOnTreatment(ahm.getEnrolledOnTreatmentObject().getName());
                    ceform.setCaregiverHivStatus(ahm.getCurrentHivStatusObject().getName());
                    ceform.setCaregiverPhone(ahm.getPhoneNumber());
                    ceform.setCaregiverSex(ahm.getSex());
                    ceform.setDateOfCaregiverHivStatus(DateManager.getMthDayYearStringDateFormat(ahm.getDateOfCurrentHivStatus(), 0));
                    ceform.setDateCaregiverEnrolledOnTreatment(DateManager.getMthDayYearStringDateFormat(ahm.getDateEnrolledOnTreatment(), 0));
                    ceform.setFacilityCaregiverEnrolled(getFacilityName(ahm.getHivTreatmentFacilityId()));
                    /*ReferralFacility faility=util.getReferralFacilityDaoInstance().getReferralFacility(ahm.getHivTreatmentFacilityId());
                    if(faility !=null)
                    {
                        ceform.setFacilityCaregiverEnrolled(faility.getFacilityName());
                    }*/
                }
                ceform.setDateEnrolledOnTreatment(DateManager.getMthDayYearFromMySqlDate(DateManager.convertDateToString(ovc.getDateEnrolledOnTreatment(), DateManager.DB_DATE_FORMAT)));  
                ceform.setDateOfEnrollment(dateOfEnrollment);
                ceform.setDateOfBaselineHivStatus(DateManager.getMthDayYearFromMySqlDate(DateManager.convertDateToString(ovc.getDateOfBaselineHivStatus(), DateManager.DB_DATE_FORMAT)));
                //ceform.setDateEnrolledOnTreatment(dateChildEnrolledOnTreatment);
                
                if(ceform.getBaselineHivStatus()==AppConstant.HIV_UNKNOWN_NUM || dateOfBaselineHivStatus==null || dateOfBaselineHivStatus.equalsIgnoreCase(DateManager.DEFAULT_DATE))
                ceform.setDateOfBaselineHivStatus(null);             
                if(ceform.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM || dateChildEnrolledOnTreatment==null || dateChildEnrolledOnTreatment.equalsIgnoreCase(DateManager.DEFAULT_DATE))
                ceform.setDateEnrolledOnTreatment(null);
               
                ceform.setHhUniqueId(ovc.getHhUniqueId());
                ceform.setEnrolledChildId(ovc.getOvcId());
                ceform.setOvcId(ovc.getOvcId());
                ceform.setAgeAtBaseline(ovc.getAgeAtBaseline());
                ceform.setAgeUnitAtBaseline(ovc.getAgeUnitAtBaseline());
                ceform.setBirthCertificate(ovc.getBaselineBirthRegistrationStatus());
                ceform.setHiddenBeneficiaryId(ovc.getOvcId());
                ceform.setSurname(ovc.getSurname());
                ceform.setFirstName(ovc.getFirstName());
                ceform.setSex(ovc.getSex());
                ceform.setSchoolStatus(ovc.getBaselineSchoolStatus());
                ceform.setSchoolName(ovc.getSchoolName());
                ceform.setEnrolledOnTreatment(ovc.getEnrolledOnTreatment());
                ceform.setGrade(ovc.getSchoolGrade());
                ceform.setVulnerabilityStatus(appUtil.splitString(ovc.getVulnerabilityStatusCode(),","));
                ceform.setBaselineHivStatus(ovc.getBaselineHivStatus());
                ceform.setHivTreatmentFacilityId(ovc.getHivTreatmentFacilityId());
                ceform.setTreatmentId(ovc.getTreatmentId());
                ceform.setCaregiverId(ovc.getCaregiverId());
                ceform.setCaregiverRelationship(ovc.getCaregiverRelationship());
                //AdultHouseholdMember ahm=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(ovc.getCaregiverId());
                ceform.setVolunteerName(ovc.getCommunityWorkerName());
                ceform.setSourceOfInfo(ovc.getSourceOfInfo());
                
                /*ceform.setAbuseAndExploitation(ovc.getAbuseAndExploitation());
                ceform.setCare(ovc.getCare());
                ceform.setDevelopmentAndPerformance(ovc.getDevelopmentAndPerformance());
                ceform.setEducationAndWork(ovc.getEducationAndWork());
                ceform.setEmotionalHealth(ovc.getEmotionalHealth());
                ceform.setFoodSecurity(ovc.getFoodSecurity());
                ceform.setHealthCareServices(ovc.getHealthCareServices());
                ceform.setLegalProtection(ovc.getLegalProtection());
                ceform.setNutritionAndGrowth(ovc.getNutritionAndGrowth());
                ceform.setShelter(ovc.getShelter());
                ceform.setSocialBehaviour(ovc.getSocialBehaviour());
                ceform.setWellness(ovc.getWellness());*/
                //ceform.setOvcId(ovc.getOvcId());
                //ceform.setAbuseAndExploitation(abuseAndExploitation);
                //ceform.setDateOfEnrollment(DateManager.convertDateToString(ovc.getDateOfEnrollment(),"MM/dd/yyyy"));
                //ceform.setHivStatus(ovc.getHivStatusCode());
                ceform.setSex(ovc.getSex());
                ceform.setPhoneNumber(ovc.getPhoneNumber());
                
                if(ovc.getBaselineSchoolStatus()==1)
                {
                    setSchoolStatusProperties(session,"false");
                }
                else
                setSchoolStatusProperties(session,"true");
                if(ovc.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                {
                    setHIVStatusProperties(session,"false");//session.setAttribute("enrhivDisabled", "false");
                }
                else
                setHIVStatusProperties(session,"true");
                setButtonState(session,"true","false");
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ceform;
    }
    private void resetBaselineInfo(ChildEnrollmentForm ceform)
    {
        ceform.setHhUniqueId(null);
        ceform.setHhName(null);
        ceform.setDateOfEnrollment(null);
        ceform.setBaselineHivStatus(0);
        ceform.setOvcId(null);
        ceform.setPhoneNumber(null);
        ceform.setSex(null);
    }
    private void setHouseholdMemberListPerHousehold(HttpSession session, String hhUniqueId)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            hhUniqueId=UniqueIdManager.cleanUniqueId(hhUniqueId);
            List list=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMembersPerHousehold(hhUniqueId);
            if(list==null)
            list=new ArrayList();
            session.setAttribute("ahmCgList", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private ChildEnrollmentForm setOrganizationUnitProperties(HttpSession session, String hhUniqueId,ChildEnrollmentForm ceform,String userName) throws Exception
    {
        DaoUtility util=new DaoUtility();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        HouseholdEnrollment hhe=util.getHouseholdEnrollmentDaoInstance().getHouseholdEnrollment(hhUniqueId);
        if(hhe !=null)
        {
            OrganizationUnit ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(hhe.getOrganizationUnit());
            if(ou !=null)
            {
                ouaManager.setOrganizationUnitAttributes(session, ou.getParentId(),userName,hhe.getCboId());
                ceform.setLevel3OuId(ou.getParentId());
            }
            ceform.setOrganizationUnitId(hhe.getOrganizationUnit());
            ceform.setCboId(hhe.getCboId());
        }
        return ceform;
    }
    private String getFacilityName(String facilityId) throws Exception
    {
        String facilityName=null;
        DaoUtility util=new DaoUtility();
        ReferralFacility faility=util.getReferralFacilityDaoInstance().getReferralFacility(facilityId);
        if(faility !=null)
        {
            facilityName=faility.getFacilityName();
        }
       return facilityName;
    }
    public void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("cenSaveDisabled", saveDisabled);
        session.setAttribute("cenModifyDisabled", modifyDisabled);
    }
    private void generateSchoolList(HttpSession session,ChildEnrollmentForm ceform)
    {
        try
        {
            List list=new ArrayList();
            DaoUtility daoutil=new DaoUtility();
            ReportParameterTemplate rpt=new ReportParameterTemplate();
            rpt.setLevel2OuId(ceform.getLevel2OuId());
            rpt.setLevel3OuId(ceform.getLevel3OuId());
            list=daoutil.getSchoolDaoInstance().getSchoolsByOrgUnit(rpt);
            if(list==null)
            list=new ArrayList();
            session.setAttribute("schoolListByLevel2Ou", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void generateSchoolGradeList(HttpSession session)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            List list=util.getSchoolGradeDaoInstance().getAllSchoolGrades();
            if(list==null)
            list=new ArrayList();
            session.setAttribute("schoolGradeList", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void loadCaregiversPerHousehold(HttpSession session, String hhUniqueId) throws Exception
    {
        DaoUtility util=new DaoUtility();
        List list=util.getAdultHouseholdMemberDaoInstance().getCaregiversPerHousehold(hhUniqueId);
        if(list==null)
        list=new ArrayList();
        session.setAttribute("listOfAdultMembersPerHousehold", list);
    }
    private void loadChildrenPerHousehold(HttpSession session, String hhUniqueId) throws Exception
    {
        DaoUtility util=new DaoUtility();
        List list=util.getChildEnrollmentDaoInstance().getOvcPerHousehold(hhUniqueId);
        if(list==null)
        list=new ArrayList();
        session.setAttribute("listOfChildrenPerHousehold", list);
    }
    private void loadfacility(HttpSession session,String level2OuId,String level3OuId)
    {
        try
        {
            ReferralFacilityManager rfm=new ReferralFacilityManager();
            List facilityList=rfm.loadfacility(level2OuId, level3OuId);
            session.setAttribute("ovcfacilityList", facilityList);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
