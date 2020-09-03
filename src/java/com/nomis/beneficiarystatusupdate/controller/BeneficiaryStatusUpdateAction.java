/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.beneficiarystatusupdate.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.CommunityWorkerRecordsManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.ovc.business.Beneficiary;
import com.nomis.ovc.business.BeneficiaryStatusUpdate;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.HivPropertiesManager;
import com.nomis.ovc.util.ReferralFacilityManager;
import com.nomis.reports.utils.ReportParameterTemplate;
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
public class BeneficiaryStatusUpdateAction extends org.apache.struts.action.Action {

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
            throws Exception {
        BeneficiaryStatusUpdateForm hsmform=(BeneficiaryStatusUpdateForm)form;
        String moduleName="HIV Status manager";
        HttpSession session=request.getSession();
        DaoUtility util=new DaoUtility();
        String requiredAction=hsmform.getActionName();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        if(AccessManager.isUserInDataEntryRole(user))
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
        String level2OuId=hsmform.getLevel2OuId();
        String level3OuId=hsmform.getLevel3OuId();
        int hhSerialNo=hsmform.getHhSerialNo();
        String hhUniqueId=hsmform.getHhUniqueId();
        String beneficiaryId=hsmform.getBeneficiaryId();
        String caregiverId=hsmform.getCaregiverId();
        ouaManager.setOrganizationUnitAttributes(session, level3OuId,userName,hsmform.getCboId());
        String hhId=request.getParameter("hh");
        String reqParam=request.getParameter("p");
        //setEnrolledOnTreatmentControlStatus(request,hsmform.getNewHivStatus());
        if(reqParam !=null)
        {
            requiredAction=reqParam;
            hhUniqueId=hhId;
        }
        
        //System.err.println("hhUniqueId before required action is "+hhUniqueId);
        //setBeneficiaryListPerHousehold(session, hhUniqueId);
        //setButtonState(session,"false","true");
        setBeneficiaryList(session,hhUniqueId);
        session.setAttribute("lastHivDisabled", "true");
        session.setAttribute("newHivDisabled", "false");
        session.setAttribute("allHivStatus", HivPropertiesManager.getAllHivStatus());
        session.setAttribute("posNegUnkHivStatus", HivPropertiesManager.getThreeMainHivStatus());
        setEnrolledOnTreatmentControlStatus(session,AppConstant.ENROLLED_ON_TREATMENT_NO_NUM);
        generateSchoolList(session,hsmform);
        generateSchoolGradeList(session);
        loadfacility(session,level2OuId,null);
        CommunityWorkerRecordsManager.setEnumeratorsRegistrationList(session);
        System.err.println("requiredAction is "+requiredAction);
        
        if(requiredAction==null)
        {
            hsmform.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level3OuList"))
        {
            ouaManager.setAssignedLevel3Ou(session, hsmform.getCboId());
            ouaManager.setLevel4OuList(session, null);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("level4OuList"))
        {
            ouaManager.setLevel4OuList(session, level3OuId);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("beneficiaryList"))
        {
            String hhName=hsmform.getHhName();
            loadCaregiversPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            
            hsmform.reset(mapping, request);
            //setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryListPerHousehold(session, hhUniqueId);
            request.setAttribute("hhName", hhName);
            hsmform.setHhSerialNo(hhSerialNo);
            hsmform.setHhUniqueId(hhUniqueId);
            hsmform.setHhName(hhName);
            hsmform=setOrganizationUnitProperties(session, hhUniqueId,hsmform,userName);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("childDetails"))
        {
            //String beneficiaryId=hsmform.getBeneficiaryId();
            hsmform.reset(mapping, request);
            hsmform.setHhSerialNo(hhSerialNo);
            hsmform.setHhUniqueId(hhUniqueId);
            hsmform.setBeneficiaryId(beneficiaryId);
            setChildDetails(session,hsmform);
            setAdultMemberDetails(session,hsmform);
            loadCaregiversPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            //setBeneficiaryList(session,hhUniqueId);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("adultMemberDetails"))
        {
            //String caregiverId=hsmform.getCaregiverId();
            //hsmform.reset(mapping, request);
            hsmform.setCaregiverId(caregiverId);
            setAdultMemberDetails(session,hsmform);
            setChildDetails(session,hsmform);
            //setBeneficiaryDetails(session,hsmform);
            loadCaregiversPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            //setBeneficiaryList(session,hhUniqueId);
            return mapping.findForward(SUCCESS);
        }
        /*else if(requiredAction.equalsIgnoreCase("ed"))
        {
            String id=request.getParameter("id");
            hsmform.setBeneficiaryId(id);
            //setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryDetails(session,hsmform);
            loadCaregiversPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            //setBeneficiaryDetails(hsmform,request);
            session.setAttribute("lastHivDisabled", "false");
            session.setAttribute("newHivDisabled", "true");
            setButtonState(session,"true","false");
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("new"))
        {
            String id=request.getParameter("id");
            hsmform.setBeneficiaryId(id);
            //setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryDetails(session,hsmform);
            loadCaregiversPerHousehold(session, hhUniqueId);
            loadChildrenPerHousehold(session, hhUniqueId);
            //setBeneficiaryDetails(hsmform,request);
            //session.setAttribute("lastHivDisabled", "true");
            session.setAttribute("newHivDisabled", "false");
            return mapping.findForward(SUCCESS);
        }*/
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            BeneficiaryStatusUpdate hsm=getBeneficiaryStatusUpdate(hsmform,userName);
            util.getBeneficiaryStatusUpdateDaoInstance().saveBeneficiaryStatusUpdate(hsm,true);
            saveUserActivity(userName,moduleName,"Saved new HIV status record for beneficiary with Id "+hsm.getBeneficiaryId());
            //setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryListPerHousehold(session, hhUniqueId);
            
        }
        else if(requiredAction.equalsIgnoreCase("modify"))
        {
            BeneficiaryStatusUpdate hsm=getBeneficiaryStatusUpdate(hsmform,userName);
            util.getBeneficiaryStatusUpdateDaoInstance().updateBeneficiaryStatusUpdate(hsm,true);
            saveUserActivity(userName,moduleName,"Modified HIV status record for beneficiary with Id "+hsm.getBeneficiaryId());
            setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryListPerHousehold(session, hhUniqueId);
        }
        else if(requiredAction.equalsIgnoreCase("delete"))
        {
            BeneficiaryStatusUpdate hsm=getBeneficiaryStatusUpdate(hsmform,userName);
            util.getBeneficiaryStatusUpdateDaoInstance().markForDelete(hsm);
            saveUserActivity(userName,moduleName,"Requested HIV status record for beneficiary with Id "+hsm.getBeneficiaryId()+" be deleted");
            setBeneficiaryList(session,hhUniqueId);
            //setBeneficiaryListPerHousehold(session, hhUniqueId);
        }
        return mapping.findForward(SUCCESS);
    }
    private BeneficiaryStatusUpdateForm setOrganizationUnitProperties(HttpSession session, String hhUniqueId,BeneficiaryStatusUpdateForm form,String userName) throws Exception
    {
        DaoUtility util=new DaoUtility();
        OrganizationUnitAttributesManager ouaManager=new OrganizationUnitAttributesManager();
        HouseholdEnrollment hhe=util.getHouseholdEnrollmentDaoInstance().getHouseholdEnrollment(hhUniqueId);
        if(hhe !=null)
        {
            System.err.println("hhe.getHhUniqueId() is "+hhe.getHhUniqueId());
            OrganizationUnit ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(hhe.getOrganizationUnit());
            if(ou !=null)
            {
                System.err.println("ou.getName() is "+ou.getName());
                ouaManager.setOrganizationUnitAttributes(session, ou.getParentId(),userName,hhe.getCboId());
                form.setLevel3OuId(ou.getParentId());
            }
            form.setOrganizationUnitId(hhe.getOrganizationUnit());
            form.setCboId(hhe.getCboId());
        }
        return form;
    }
    private void saveUserActivity(String userName,String userAction,String description)
    {
        UserActivityManager uam=new UserActivityManager();
        uam.saveUserActivity(userName, userAction,description);
    }
    private void setChildDetails(HttpSession session, BeneficiaryStatusUpdateForm hsmform)
    {
        try
        {
            System.err.println("hsmform.getBeneficiaryId() is "+hsmform.getBeneficiaryId());
            DaoUtility util=new DaoUtility();
            if(hsmform.getBeneficiaryId() !=null && !hsmform.getBeneficiaryId().trim().equalsIgnoreCase("select"))
            {
                Ovc beneficiary=util.getChildEnrollmentDaoInstance().getOvc(hsmform.getBeneficiaryId());
                if(beneficiary !=null)
                {
                    hsmform.setDateOfEnrollment(DateManager.convertDateToString(beneficiary.getDateOfEnrollment(),DateManager.MM_DD_YYYY_SLASH));
                    hsmform.setLastHivStatus(beneficiary.getCurrentHivStatus());

                    hsmform.setSex(beneficiary.getSex());

                    hsmform.setChildNewHivStatus(beneficiary.getCurrentHivStatus());
                    hsmform.setDateOfNewHivStatus(DateManager.getMthDayYearStringDateFormat(beneficiary.getDateOfCurrentHivStatus(), 0));
                    //if(hsmform.getNewHivStatus()==AppConstant.HIV_UNKNOWN_NUM || dateOfNewStatus==null || dateOfNewStatus.equalsIgnoreCase(DateManager.DEFAULT_DATE))
                    //hsmform.setDateOfNewHivStatus(null);
                    hsmform.setEnrolledOnTreatment(beneficiary.getEnrolledOnTreatment());
                    hsmform.setDateEnrolledOnTreatment(DateManager.getMthDayYearStringDateFormat(beneficiary.getDateEnrolledOnTreatment(), 0));
                    //if(hsmform.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM || dateOfEnrolledOnTreatment==null || dateOfEnrolledOnTreatment.equalsIgnoreCase(DateManager.DEFAULT_DATE))
                    //hsmform.setDateEnrolledOnTreatment(null);

                    hsmform.setBirthCertificate(beneficiary.getCurrentBirthRegistrationStatus());
                    hsmform.setSchoolStatus(beneficiary.getCurrentSchoolStatus());
                    hsmform.setSchoolName(beneficiary.getSchoolName());
                    hsmform.setGrade(beneficiary.getSchoolGrade());
                    hsmform.setEnrolledInVocationalTraining(0);
                    hsmform.setNameOfVocationalTraining(null);
                    //hsmform.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getSurname());
                    if(beneficiary.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                    {
                        System.err.println("beneficiary.getCurrentHivStatus() is positive "+beneficiary.getCurrentHivStatus());
                        hsmform.setHivTreatmentFacilityId(beneficiary.getHivTreatmentFacilityId());
                        hsmform.setChildTreatmentId(beneficiary.getTreatmentId());
                        setHIVStatusProperties(session,"false");
                        setEnrolledOnTreatmentControlStatus(session,beneficiary.getEnrolledOnTreatment());
                    }
                    else
                    {
                        System.err.println("beneficiary.getCurrentHivStatus() is not positive "+beneficiary.getCurrentHivStatus());
                        setHIVStatusProperties(session,"true");
                    }
                    //hsmform.setPhoneNumber(beneficiary.getPhoneNumber());
                }
                else
                {
                    hsmform=resetChildInfo(hsmform);
                }
            }
            else
            hsmform=resetChildInfo(hsmform);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void setAdultMemberDetails(HttpSession session, BeneficiaryStatusUpdateForm hsmform)
    {
        try
        {
            System.err.println("hsmform.getCaregiverId() is "+hsmform.getCaregiverId());
            DaoUtility util=new DaoUtility();
            Beneficiary beneficiary=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(hsmform.getCaregiverId());
            if(hsmform.getBeneficiaryId() !=null && !hsmform.getBeneficiaryId().trim().equalsIgnoreCase("select"))
            {            
                if(beneficiary !=null)
                {
                    hsmform.setDateOfEnrollment(DateManager.convertDateToString(beneficiary.getDateOfEnrollment(),DateManager.MM_DD_YYYY_SLASH));
                    hsmform.setLastHivStatus(beneficiary.getCurrentHivStatus());
                    hsmform.setCaregiverAge(beneficiary.getCurrentAge());
                    hsmform.setCaregiverSex(beneficiary.getSex());
                    hsmform.setCaregiverPhone(beneficiary.getPhoneNumber());
                    hsmform.setCaregiverLastHivStatus(beneficiary.getCurrentHivStatus());
                    hsmform.setDateOfCaregiverLastHivStatus(DateManager.getMthDayYearStringDateFormat(beneficiary.getDateOfCurrentHivStatus(), 0));
                    hsmform.setCaregiverHivStatus(beneficiary.getCurrentHivStatus());
                    hsmform.setDateOfCaregiverHivStatus(DateManager.getMthDayYearStringDateFormat(beneficiary.getDateOfCurrentHivStatus(), 0));

                    //hsmform.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getSurname());
                    if(beneficiary.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                    {
                        hsmform.setCaregiverEnrolledOnTreatment(beneficiary.getEnrolledOnTreatment());
                        hsmform.setDateCaregiverEnrolledOnTreatment(DateManager.getMthDayYearStringDateFormat(beneficiary.getDateEnrolledOnTreatment(), 0));
                        hsmform.setFacilityCaregiverEnrolled(beneficiary.getHivTreatmentFacilityId());
                        hsmform.setCaregiverTreatmentId(beneficiary.getTreatmentId());
                        System.err.println("beneficiary.getCurrentHivStatus() is positive "+beneficiary.getCurrentHivStatus());
                        setHIVStatusProperties(session,"false");
                        setEnrolledOnTreatmentControlStatus(session,beneficiary.getEnrolledOnTreatment());
                    }
                    else
                    {
                        System.err.println("beneficiary.getCurrentHivStatus() is not positive "+beneficiary.getCurrentHivStatus());
                        setHIVStatusProperties(session,"true");
                    }
                    //hsmform.setPhoneNumber(beneficiary.getPhoneNumber());
                }
                else
                {
                    hsmform=resetCaregiverInfo(hsmform);
                }
            }
            else
            hsmform=resetCaregiverInfo(hsmform);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private BeneficiaryStatusUpdateForm resetChildInfo(BeneficiaryStatusUpdateForm hsmform)
    {
        hsmform.setEnrolledOnTreatment(0);
        hsmform.setChildNewHivStatus(0);
        hsmform.setBirthCertificate(0);
        hsmform.setLastHivStatus(0);
        hsmform.setDateOfNewHivStatus(null);
        hsmform.setDateEnrolledOnTreatment(null);
        hsmform.setSex(null);
        hsmform.setHivTreatmentFacilityId("select");
        hsmform.setChildTreatmentId(null);
        hsmform.setSchoolStatus(0);
        hsmform.setSchoolName("select");
        hsmform.setEnrolledInVocationalTraining(0);
        hsmform.setNameOfVocationalTraining("select");
        return hsmform;
    }
    private BeneficiaryStatusUpdateForm resetCaregiverInfo(BeneficiaryStatusUpdateForm hsmform)
    {
        hsmform.setCaregiverEnrolledOnTreatment(0);
        hsmform.setCaregiverHivStatus(0);
        hsmform.setCaregiverAge(0);
        hsmform.setCaregiverLastHivStatus(0);
        hsmform.setCaregiverPhone(null);
        hsmform.setCaregiverSex(null);
        hsmform.setDateCaregiverEnrolledOnTreatment(null);
        hsmform.setDateOfCaregiverHivStatus(null);
        hsmform.setDateOfCaregiverLastHivStatus(null);
        hsmform.setFacilityCaregiverEnrolled("select");
        hsmform.setCaregiverTreatmentId(null);
        return hsmform;
    }
    private void setHIVStatusProperties(HttpSession session,String disabled)
    {
        session.setAttribute("enrhivDisabled", disabled);
    }
    private BeneficiaryStatusUpdate getBeneficiaryStatusUpdate(BeneficiaryStatusUpdateForm hsmform,String userName)
    {
        Date dateEnrolledOnTreatment=DateManager.getDefaultStartDateInstance();
       if(hsmform.getDateEnrolledOnTreatment() !=null && hsmform.getDateEnrolledOnTreatment().indexOf("/") !=-1)
       dateEnrolledOnTreatment=DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(hsmform.getDateEnrolledOnTreatment()));
       BeneficiaryStatusUpdate hsm=new BeneficiaryStatusUpdate();
       hsm.setBeneficiaryId(getBeneficiaryId(hsmform.getBeneficiaryId()));
       hsm.setBeneficiaryType(hsmform.getBeneficiaryType());
       hsm.setDateCreated(DateManager.getCurrentDateInstance());
       hsm.setDateOfNewStatus(DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(hsmform.getDateOfNewHivStatus())));
       hsm.setDateEnrolledOnTreatment(dateEnrolledOnTreatment);
       hsm.setEnrolledOnTreatment(hsmform.getEnrolledOnTreatment());
       if(hsmform.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
       hsm.setFacilityId(hsmform.getHivTreatmentFacilityId());
       else
       hsm.setFacilityId(null);
       hsm.setLastModifiedDate(DateManager.getCurrentDateInstance());
       hsm.setNewHivStatus(hsmform.getChildNewHivStatus());
       hsm.setBirthCertificate(hsmform.getBirthCertificate());
       hsm.setSchoolStatus(hsmform.getSchoolStatus());
       hsm.setSchoolName(hsmform.getSchoolName());
       hsm.setEnrolledInVocationalTraining(hsmform.getEnrolledInVocationalTraining());
       hsm.setNameOfVocationalTraining(hsmform.getNameOfVocationalTraining());
       hsm.setChildTreatmentId(hsmform.getChildTreatmentId());
       hsm.setUpdateChildHivStatus(hsmform.getUpdateChildHivStatus());
       hsm.setUpdateChildBirthRegStatus(hsmform.getUpdateChildBirthRegAndSchoolStatus());
       
       hsm.setCaregiverId(hsmform.getCaregiverId());
       hsm.setCaregiverHivStatus(hsmform.getCaregiverHivStatus());
       hsm.setCaregiverEnrolledOnTreatment(hsmform.getCaregiverEnrolledOnTreatment());
       hsm.setFacilityCaregiverEnrolled(hsmform.getFacilityCaregiverEnrolled());
       hsm.setCaregiverTreatmentId(hsmform.getCaregiverTreatmentId());
       hsm.setUpdateCaregiverHivStatus(hsmform.getUpdateCaregiverHivStatus());
       hsm.setDateCaregiverEnrolledOnTreatment(DateManager.getDefaultStartDateInstance());
       //if caregiver is HIV positive, set treatment related information
       if(hsm.getCaregiverHivStatus()==AppConstant.HIV_POSITIVE_NUM)
       {
           hsm.setCaregiverEnrolledOnTreatment(hsmform.getCaregiverEnrolledOnTreatment());
           if(hsm.getCaregiverEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
           {
               hsm.setDateCaregiverEnrolledOnTreatment(DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(hsmform.getDateCaregiverEnrolledOnTreatment())));
               hsm.setCaregiverTreatmentId(hsmform.getCaregiverTreatmentId());
               hsm.setFacilityCaregiverEnrolled(hsmform.getFacilityCaregiverEnrolled());
           }
       }
       else
       {
           //Caregiver is not HIV positive, set treatment related parameters to default values
           hsm.setCaregiverEnrolledOnTreatment(0);
           hsm.setCaregiverTreatmentId(null);
           hsm.setFacilityCaregiverEnrolled(null);
       }
       
       hsm.setRecordedBy(userName);
       System.err.println("hsm.getBeneficiaryId in BeneficiaryStatusUpdate is "+hsm.getBeneficiaryId());
       return hsm;
    }
    private void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("serviceSaveDisabled", saveDisabled);
        session.setAttribute("serviceModifyDisabled", modifyDisabled);
    }
    private void setEnrolledOnTreatmentControlStatus(HttpSession session,int enrolledOnTreatment)
    {
        if(enrolledOnTreatment==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
        session.setAttribute("enrOnTreatmentDisabled", "false");
        else
        session.setAttribute("enrOnTreatmentDisabled", "true");
    }
    private String getBeneficiaryId(String beneficiaryFormId)
    {
        String beneficiaryId=beneficiaryFormId;
        if(beneficiaryFormId !=null && beneficiaryFormId.indexOf("_") !=-1)
        {
            String[] idArray=beneficiaryFormId.split("_");
            beneficiaryId=idArray[0];
        }
        return beneficiaryId;
    }
    private void generateSchoolList(HttpSession session,BeneficiaryStatusUpdateForm hsmform)
    {
        try
        {
            List list=new ArrayList();
            DaoUtility daoutil=new DaoUtility();
            ReportParameterTemplate rpt=new ReportParameterTemplate();
            rpt.setLevel2OuId(hsmform.getLevel2OuId());
            rpt.setLevel3OuId(hsmform.getLevel3OuId());
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
        session.setAttribute("listOfAdultMembersPerHouseholdInOSU", list);
    }
    private void loadChildrenPerHousehold(HttpSession session, String hhUniqueId) throws Exception
    {
        DaoUtility util=new DaoUtility();
        List list=util.getChildEnrollmentDaoInstance().getOvcPerHousehold(hhUniqueId);
        if(list==null)
        list=new ArrayList();
        session.setAttribute("listOfChildrenPerHouseholdInOSU", list);
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
    
    private void setBeneficiaryList(HttpSession session,String hhUniqueId) throws Exception
    {
        DaoUtility util=new DaoUtility();
        List beneficiaryList=new ArrayList();
        List childrenList=util.getChildEnrollmentDaoInstance().getOvcPerHousehold(hhUniqueId);
        if(childrenList !=null)
        {
            beneficiaryList.addAll(childrenList);
        }
        List adultList=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMembersPerHousehold(hhUniqueId);
        if(adultList !=null)
        {
            beneficiaryList.addAll(adultList);
        }
        session.setAttribute("hsmBeneficiaryList", beneficiaryList);
    }
}
