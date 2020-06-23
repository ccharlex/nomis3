/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.beneficiarystatusupdate.controller;

import com.nomis.ovc.business.Beneficiary;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.ValidationManager;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author smomoh
 */
public class BeneficiaryStatusUpdateForm extends org.apache.struts.action.ActionForm {
    
    private String actionName;
    private int hhSerialNo;
    private String hhUniqueId;
    private String beneficiaryId;
    private String dateOfEnrollment;
    private String sex;
    private String cboId;
    private String level2OuId;
    private String level3OuId;
    private String organizationUnitId;
    private int lastHivStatus;
    private int educationLevel;
    private String hhName;
    private String hivTreatmentFacilityId;
    private String childTreatmentId;
    private int enrolledOnTreatment;
    private String dateEnrolledOnTreatment;
    private String dateOfNewHivStatus;
    private int childNewHivStatus;
    private String beneficiaryName;
    private int beneficiaryType;
    private int birthCertificate;
    private int schoolStatus;
    private String schoolName;
    private String grade;
    private int enrolledInVocationalTraining;
    private String nameOfVocationalTraining;
    private String caregiverId;
    private String caregiverSex;
    private int updateChildBirthRegStatus;
    private int caregiverAge;
    private String caregiverPhone;
    private int caregiverLastHivStatus;
    private String dateOfCaregiverLastHivStatus;
    private int caregiverHivStatus;
    private String dateOfCaregiverHivStatus;
    private int caregiverEnrolledOnTreatment;
    private String dateCaregiverEnrolledOnTreatment;
    private String facilityCaregiverEnrolled;
    private String caregiverTreatmentId;
    private int updateChildBirthRegAndSchoolStatus;
    private int updateChildHivStatus;
    private int updateCaregiverHivStatus;
    private String volunteerName;
    
    
    public BeneficiaryStatusUpdateForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public int getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(int beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getCboId() {
        return cboId;
    }

    public void setCboId(String cboId) {
        this.cboId = cboId;
    }

    public String getDateEnrolledOnTreatment() {
        return dateEnrolledOnTreatment;
    }

    public void setDateEnrolledOnTreatment(String dateEnrolledOnTreatment) {
        this.dateEnrolledOnTreatment = dateEnrolledOnTreatment;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public String getDateOfNewHivStatus() {
        return dateOfNewHivStatus;
    }

    public void setDateOfNewHivStatus(String dateOfNewHivStatus) {
        this.dateOfNewHivStatus = dateOfNewHivStatus;
    }

    public int getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }

    public int getEnrolledOnTreatment() {
        return enrolledOnTreatment;
    }

    public void setEnrolledOnTreatment(int enrolledOnTreatment) {
        this.enrolledOnTreatment = enrolledOnTreatment;
    }

    public String getHivTreatmentFacilityId() {
        return hivTreatmentFacilityId;
    }

    public void setHivTreatmentFacilityId(String hivTreatmentFacilityId) {
        this.hivTreatmentFacilityId = hivTreatmentFacilityId;
    }

    
    public String getHhName() {
        return hhName;
    }

    public void setHhName(String hhName) {
        this.hhName = hhName;
    }

    public int getHhSerialNo() {
        return hhSerialNo;
    }

    public void setHhSerialNo(int hhSerialNo) {
        this.hhSerialNo = hhSerialNo;
    }

    public String getHhUniqueId() {
        return hhUniqueId;
    }

    public void setHhUniqueId(String hhUniqueId) {
        this.hhUniqueId = hhUniqueId;
    }

    public int getLastHivStatus() {
        return lastHivStatus;
    }

    public void setLastHivStatus(int lastHivStatus) {
        this.lastHivStatus = lastHivStatus;
    }

    public String getLevel2OuId() {
        return level2OuId;
    }

    public void setLevel2OuId(String level2OuId) {
        this.level2OuId = level2OuId;
    }

    public String getLevel3OuId() {
        return level3OuId;
    }

    public void setLevel3OuId(String level3OuId) {
        this.level3OuId = level3OuId;
    }

    public int getChildNewHivStatus() {
        return childNewHivStatus;
    }

    public void setChildNewHivStatus(int childNewHivStatus) {
        this.childNewHivStatus = childNewHivStatus;
    }

    public String getOrganizationUnitId() {
        return organizationUnitId;
    }

    public void setOrganizationUnitId(String organizationUnitId) {
        this.organizationUnitId = organizationUnitId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getBirthCertificate() {
        return birthCertificate;
    }

    public void setBirthCertificate(int birthCertificate) {
        this.birthCertificate = birthCertificate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(int schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public int getCaregiverEnrolledOnTreatment() {
        return caregiverEnrolledOnTreatment;
    }

    public void setCaregiverEnrolledOnTreatment(int caregiverEnrolledOnTreatment) {
        this.caregiverEnrolledOnTreatment = caregiverEnrolledOnTreatment;
    }

    public int getCaregiverHivStatus() {
        return caregiverHivStatus;
    }

    public void setCaregiverHivStatus(int caregiverHivStatus) {
        this.caregiverHivStatus = caregiverHivStatus;
    }

    public int getCaregiverAge() {
        return caregiverAge;
    }

    public void setCaregiverAge(int caregiverAge) {
        this.caregiverAge = caregiverAge;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getCaregiverPhone() {
        return caregiverPhone;
    }

    public void setCaregiverPhone(String caregiverPhone) {
        this.caregiverPhone = caregiverPhone;
    }

    public String getCaregiverSex() {
        return caregiverSex;
    }

    public void setCaregiverSex(String caregiverSex) {
        this.caregiverSex = caregiverSex;
    }

    public String getDateCaregiverEnrolledOnTreatment() {
        return dateCaregiverEnrolledOnTreatment;
    }

    public void setDateCaregiverEnrolledOnTreatment(String dateCaregiverEnrolledOnTreatment) {
        this.dateCaregiverEnrolledOnTreatment = dateCaregiverEnrolledOnTreatment;
    }

    public String getDateOfCaregiverHivStatus() {
        return dateOfCaregiverHivStatus;
    }

    public void setDateOfCaregiverHivStatus(String dateOfCaregiverHivStatus) {
        this.dateOfCaregiverHivStatus = dateOfCaregiverHivStatus;
    }

    public String getFacilityCaregiverEnrolled() {
        return facilityCaregiverEnrolled;
    }

    public void setFacilityCaregiverEnrolled(String facilityCaregiverEnrolled) {
        this.facilityCaregiverEnrolled = facilityCaregiverEnrolled;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public int getCaregiverLastHivStatus() {
        return caregiverLastHivStatus;
    }

    public void setCaregiverLastHivStatus(int caregiverLastHivStatus) {
        this.caregiverLastHivStatus = caregiverLastHivStatus;
    }

    public String getDateOfCaregiverLastHivStatus() {
        return dateOfCaregiverLastHivStatus;
    }

    public void setDateOfCaregiverLastHivStatus(String dateOfCaregiverLastHivStatus) {
        this.dateOfCaregiverLastHivStatus = dateOfCaregiverLastHivStatus;
    }

    public int getEnrolledInVocationalTraining() {
        return enrolledInVocationalTraining;
    }

    public void setEnrolledInVocationalTraining(int enrolledInVocationalTraining) {
        this.enrolledInVocationalTraining = enrolledInVocationalTraining;
    }

    public String getNameOfVocationalTraining() {
        return nameOfVocationalTraining;
    }

    public void setNameOfVocationalTraining(String nameOfVocationalTraining) {
        this.nameOfVocationalTraining = nameOfVocationalTraining;
    }

    public String getCaregiverTreatmentId() {
        return caregiverTreatmentId;
    }

    public void setCaregiverTreatmentId(String caregiverTreatmentId) {
        this.caregiverTreatmentId = caregiverTreatmentId;
    }

    public String getChildTreatmentId() {
        return childTreatmentId;
    }

    public void setChildTreatmentId(String childTreatmentId) {
        this.childTreatmentId = childTreatmentId;
    }

    public int getUpdateChildBirthRegStatus() {
        return updateChildBirthRegStatus;
    }

    public void setUpdateChildBirthRegStatus(int updateChildBirthRegStatus) {
        this.updateChildBirthRegStatus = updateChildBirthRegStatus;
    }

    public int getUpdateCaregiverHivStatus() {
        return updateCaregiverHivStatus;
    }

    public void setUpdateCaregiverHivStatus(int updateCaregiverHivStatus) {
        this.updateCaregiverHivStatus = updateCaregiverHivStatus;
    }

    public int getUpdateChildBirthRegAndSchoolStatus() {
        return updateChildBirthRegAndSchoolStatus;
    }

    public void setUpdateChildBirthRegAndSchoolStatus(int updateChildBirthRegAndSchoolStatus) {
        this.updateChildBirthRegAndSchoolStatus = updateChildBirthRegAndSchoolStatus;
    }
    
    public int getUpdateChildHivStatus() {
        return updateChildHivStatus;
    }

    public void setUpdateChildHivStatus(int updateChildHivStatus) {
        this.updateChildHivStatus = updateChildHivStatus;
    }

@Override
public void reset(ActionMapping mapping, HttpServletRequest request)
{
    actionName=null;
    hhSerialNo=0;
    hhUniqueId=null;
    dateOfEnrollment=null;
    sex=null;
    lastHivStatus=0;
    educationLevel=0;
    beneficiaryId=null;
    hhName=null;
    hivTreatmentFacilityId=null;
    enrolledOnTreatment=0;
    dateEnrolledOnTreatment=null;
    dateOfNewHivStatus=null;
    childNewHivStatus=0;
    beneficiaryName=null;
    birthCertificate=0;
    schoolStatus=0;
    schoolName=null;
    grade=null;
    volunteerName=null;
    caregiverLastHivStatus=0;
    dateOfCaregiverLastHivStatus=null;
    facilityCaregiverEnrolled=null;
    childTreatmentId=null;
    caregiverTreatmentId=null;
    dateCaregiverEnrolledOnTreatment=null;
    caregiverEnrolledOnTreatment=0;
    dateOfCaregiverHivStatus=null;
    caregiverSex=null;
    caregiverPhone=null;
    caregiverId=null;
    caregiverAge=0;
    enrolledInVocationalTraining=0;
    nameOfVocationalTraining=null;
    updateChildHivStatus=2;
    updateChildBirthRegAndSchoolStatus=2;
    updateCaregiverHivStatus=2;
    updateChildBirthRegStatus=2;
}
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if(getActionName()==null || getActionName().equalsIgnoreCase("level3OuList") || getActionName().equalsIgnoreCase("beneficiaryList") || getActionName().equalsIgnoreCase("childDetails") || getActionName().equalsIgnoreCase("adultMemberDetails") || getActionName().equalsIgnoreCase("level4OuList"))
        return errors;
        int childCurrentAge=0;
        Beneficiary child=ValidationManager.getBeneficiary(this.getBeneficiaryId(),AppConstant.OVC_TYPE_NUM);
        if(child !=null)
        {
            childCurrentAge=child.getCurrentAge();
            if(this.getUpdateChildHivStatus()==1)
            {//validate HIV status
                Date dateOfBirth=DateManager.getPreviousDate(DateManager.getCurrentDateInstance(), childCurrentAge);
                Date dDateOfBaselineHivStatus=child.getDateOfBaselineHivStatus();//DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(this.getDateOfBaselineHivStatus()));
                if(this.getChildNewHivStatus()==0)
                errors.add("childNewHivStatus", new ActionMessage("errors.childNewHivStatus.required"));
                else if((getChildNewHivStatus()==AppConstant.HIV_POSITIVE_NUM || getChildNewHivStatus()==AppConstant.HIV_NEGATIVE_NUM) && (this.getDateOfNewHivStatus()==null || this.getDateOfNewHivStatus().indexOf("/")==-1))
                errors.add("dateOfNewHivStatus", new ActionMessage("errors.dateOfBaselineHivStatus.required"));
                else if(dateOfBirth !=null && dDateOfBaselineHivStatus !=null && dateOfBirth.after(dDateOfBaselineHivStatus))
                errors.add("dateOfBaselineHivStatus", new ActionMessage("errors.dateOfBaselineHivStatus.beforeDateOfBirth"));
                else if(this.getChildNewHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                {
                    if(this.getEnrolledOnTreatment()==0)
                    errors.add("enrolledOnTreatment", new ActionMessage("errors.enrolledOnTreatment.required"));
                    else if(this.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM )
                    {
                        if(this.getDateEnrolledOnTreatment()==null || this.getDateEnrolledOnTreatment().indexOf("/")==-1)
                        errors.add("dateEnrolledOnTreatment", new ActionMessage("errors.dateEnrolledOnTreatment.required"));
                        else if(!ValidationManager.isDateEnrolledOnTreatmentBeforeDateOfHivStatus(this.getDateOfNewHivStatus(),this.getDateEnrolledOnTreatment()))
                        errors.add("dateEnrolledOnTreatment", new ActionMessage("errors.dateEnrolledOnTreatment.beforeDateOfBaselineHivStatus"));
                        else if((this.getHivTreatmentFacilityId()==null || this.getHivTreatmentFacilityId().trim().equalsIgnoreCase("select")))
                        errors.add("hivTreatmentFacilityId", new ActionMessage("errors.hivTreatmentFacilityId.required"));
                    }
                }
            }
            if(this.getUpdateChildBirthRegAndSchoolStatus()==1)
            {
                if(this.getSchoolStatus()==1)
                {
                    if(this.getSchoolName()==null || this.getSchoolName().equalsIgnoreCase("select"))
                    errors.add("schoolName", new ActionMessage("errors.schoolName.required"));
                    else if(this.getGrade()==null || this.getGrade().equalsIgnoreCase("select"))
                    errors.add("grade", new ActionMessage("errors.gradeName.required"));
                }
                else if(this.getEnrolledInVocationalTraining()==1)
                {
                    if(this.getNameOfVocationalTraining()==null || this.getNameOfVocationalTraining().equalsIgnoreCase("select"))
                    errors.add("nameOfVocationalTraining", new ActionMessage("errors.nameOfVocationalTraining.required"));
                }
            }
        }
        if(this.getUpdateCaregiverHivStatus()==1)
        {
            Beneficiary adultHouseholdMember=ValidationManager.getBeneficiary(this.getCaregiverId(),AppConstant.CAREGIVER_TYPE_NUM);
            if(adultHouseholdMember !=null)
            {
                int caregiverCurrentAge=adultHouseholdMember.getCurrentAge();
                Date dateOfBirth=DateManager.getPreviousDate(DateManager.getCurrentDateInstance(), caregiverCurrentAge);
                Date dDateOfBaselineHivStatus=adultHouseholdMember.getDateOfBaselineHivStatus();//DateManager.getDateInstance(DateManager.processMthDayYearToMysqlFormat(this.getDateOfBaselineHivStatus()));
                if(this.getChildNewHivStatus()==0)
                errors.add("childNewHivStatus", new ActionMessage("errors.childNewHivStatus.required"));
                else if((getChildNewHivStatus()==AppConstant.HIV_POSITIVE_NUM || getChildNewHivStatus()==AppConstant.HIV_NEGATIVE_NUM) && (this.getDateOfNewHivStatus()==null || this.getDateOfNewHivStatus().indexOf("/")==-1))
                errors.add("dateOfNewHivStatus", new ActionMessage("errors.dateOfBaselineHivStatus.required"));
                else if(dateOfBirth !=null && dDateOfBaselineHivStatus !=null && dateOfBirth.after(dDateOfBaselineHivStatus))
                errors.add("dateOfBaselineHivStatus", new ActionMessage("errors.dateOfBaselineHivStatus.beforeDateOfBirth"));
                else if(this.getCaregiverHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                {
                    if(this.getCaregiverEnrolledOnTreatment()==0)
                    errors.add("caregiverEnrolledOnTreatment", new ActionMessage("errors.caregiverEnrolledOnTreatment.required"));
                    else if(this.getCaregiverEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM )
                    {
                        if(this.getDateCaregiverEnrolledOnTreatment()==null || this.getDateCaregiverEnrolledOnTreatment().indexOf("/")==-1)
                        errors.add("dateCaregiverEnrolledOnTreatment", new ActionMessage("errors.dateCaregiverEnrolledOnTreatment.required"));
                        else if(!ValidationManager.isDateEnrolledOnTreatmentBeforeDateOfHivStatus(this.getDateOfCaregiverHivStatus(),this.getDateCaregiverEnrolledOnTreatment()))
                        errors.add("dateCaregiverEnrolledOnTreatment", new ActionMessage("errors.dateCaregiverEnrolledOnTreatment.beforeDateOfHivStatus"));
                        else if((this.getFacilityCaregiverEnrolled()==null || this.getFacilityCaregiverEnrolled().trim().equalsIgnoreCase("select")))
                        errors.add("facilityCaregiverEnrolled", new ActionMessage("errors.facilityCaregiverEnrolled.required"));
                    }
                }
            }
        }
        return errors;
    }
}
