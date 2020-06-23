/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.careandsupport.controller;

import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.ValidationManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author smomoh
 */
public class CareAndSupportForm extends org.apache.struts.action.ActionForm {
    
    
    private int recordId;
    private String actionName;
    private String cboId;
    private String level2OuId;
    private String level3OuId;
    private String organizationUnitId;
    private int hivStatus;
        
    private int age;
    private String ageUnit;
    private int hhSerialNo; 
    private String hhUniqueId;
    private String beneficiaryId;
    private String surname;
    private String firstname;
    private String address;
    private String occupation;
    private String sex;
    private String phoneNumber;
    private String dateOfEnrollment;
    private String volunteerName;
    
    private int coughSymptom;
    private int childLossinWeight;
    private int childHasFever;
    private int childHasNightSweat;
    private int childHasSwelling;
    private int enrolledOnTreatment;
    private String facilityId;
    private int pickedUpMedication;
    private int missedARVsRecently;
    private String[] reasonsPeopleSkipARV;
    private int viralLoadTestDone;
    private String dateOfViralLoadTest;
    private int viralLoadResultKnown;
    private int viralLoadResult;
    private String reasonViralLoadNotDone;
    private int receivedTransportationSupport;
    private int monthsOfTransportationSupport;
    private int soresRashPainExperience;
    private String dateOfNextAppointment;
    private String dateOfAssessment;
    
    public CareAndSupportForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    public String getCboId() {
        return cboId;
    }

    public void setCboId(String cboId) {
        this.cboId = cboId;
    }

    public int getChildHasFever() {
        return childHasFever;
    }

    public void setChildHasFever(int childHasFever) {
        this.childHasFever = childHasFever;
    }

    public int getChildHasNightSweat() {
        return childHasNightSweat;
    }

    public void setChildHasNightSweat(int childHasNightSweat) {
        this.childHasNightSweat = childHasNightSweat;
    }

    public int getChildHasSwelling() {
        return childHasSwelling;
    }

    public void setChildHasSwelling(int childHasSwelling) {
        this.childHasSwelling = childHasSwelling;
    }

    public int getChildLossinWeight() {
        return childLossinWeight;
    }

    public void setChildLossinWeight(int childLossinWeight) {
        this.childLossinWeight = childLossinWeight;
    }

    public int getCoughSymptom() {
        return coughSymptom;
    }

    public void setCoughSymptom(int coughSymptom) {
        this.coughSymptom = coughSymptom;
    }

    public String getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(String dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public String getDateOfNextAppointment() {
        return dateOfNextAppointment;
    }

    public void setDateOfNextAppointment(String dateOfNextAppointment) {
        this.dateOfNextAppointment = dateOfNextAppointment;
    }

    public String getDateOfViralLoadTest() {
        return dateOfViralLoadTest;
    }

    public void setDateOfViralLoadTest(String dateOfViralLoadTest) {
        this.dateOfViralLoadTest = dateOfViralLoadTest;
    }

    public int getEnrolledOnTreatment() {
        return enrolledOnTreatment;
    }

    public void setEnrolledOnTreatment(int enrolledOnTreatment) {
        this.enrolledOnTreatment = enrolledOnTreatment;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public int getHivStatus() {
        return hivStatus;
    }

    public void setHivStatus(int hivStatus) {
        this.hivStatus = hivStatus;
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

    public int getMissedARVsRecently() {
        return missedARVsRecently;
    }

    public void setMissedARVsRecently(int missedARVsRecently) {
        this.missedARVsRecently = missedARVsRecently;
    }

    public int getMonthsOfTransportationSupport() {
        return monthsOfTransportationSupport;
    }

    public void setMonthsOfTransportationSupport(int monthsOfTransportationSupport) {
        this.monthsOfTransportationSupport = monthsOfTransportationSupport;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOrganizationUnitId() {
        return organizationUnitId;
    }

    public void setOrganizationUnitId(String organizationUnitId) {
        this.organizationUnitId = organizationUnitId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPickedUpMedication() {
        return pickedUpMedication;
    }

    public void setPickedUpMedication(int pickedUpMedication) {
        this.pickedUpMedication = pickedUpMedication;
    }

    public String getReasonViralLoadNotDone() {
        return reasonViralLoadNotDone;
    }

    public void setReasonViralLoadNotDone(String reasonViralLoadNotDone) {
        this.reasonViralLoadNotDone = reasonViralLoadNotDone;
    }

    public String[] getReasonsPeopleSkipARV() {
        return reasonsPeopleSkipARV;
    }

    public void setReasonsPeopleSkipARV(String[] reasonsPeopleSkipARV) {
        this.reasonsPeopleSkipARV = reasonsPeopleSkipARV;
    }

    public int getReceivedTransportationSupport() {
        return receivedTransportationSupport;
    }

    public void setReceivedTransportationSupport(int receivedTransportationSupport) {
        this.receivedTransportationSupport = receivedTransportationSupport;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSoresRashPainExperience() {
        return soresRashPainExperience;
    }

    public void setSoresRashPainExperience(int soresRashPainExperience) {
        this.soresRashPainExperience = soresRashPainExperience;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getViralLoadResult() {
        return viralLoadResult;
    }

    public void setViralLoadResult(int viralLoadResult) {
        this.viralLoadResult = viralLoadResult;
    }

    public int getViralLoadResultKnown() {
        return viralLoadResultKnown;
    }

    public void setViralLoadResultKnown(int viralLoadResultKnown) {
        this.viralLoadResultKnown = viralLoadResultKnown;
    }

    public int getViralLoadTestDone() {
        return viralLoadTestDone;
    }

    public void setViralLoadTestDone(int viralLoadTestDone) {
        this.viralLoadTestDone = viralLoadTestDone;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }
@Override
public void reset(ActionMapping mapping, HttpServletRequest request)
{
    actionName=null;
    
    //hhSerialNo=0; 
    //hhUniqueId=null;
    //beneficiaryId=null;
    surname=null;
    firstname=null;
    address=null;
    occupation=null;
    sex=null;
    phoneNumber=null;
    dateOfEnrollment=null;
    volunteerName=null;
    facilityId=null;
    reasonsPeopleSkipARV=null;
    dateOfViralLoadTest=null;
    reasonViralLoadNotDone=null;
    dateOfNextAppointment=null;
    dateOfAssessment=null;
    recordId=0;
    hivStatus=0;
    age=0;
    ageUnit=null;
    coughSymptom=0;
    childLossinWeight=0;
    childHasFever=0;
    childHasNightSweat=0;
    childHasSwelling=0;
    enrolledOnTreatment=0;
    pickedUpMedication=0;
    missedARVsRecently=0;
    viralLoadTestDone=0;
    viralLoadResultKnown=0;
    viralLoadResult=0;
    receivedTransportationSupport=0;
    monthsOfTransportationSupport=0;
    soresRashPainExperience=0;
}
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if(this.getActionName()==null || getActionName().equalsIgnoreCase("householdDetails") || getActionName().equalsIgnoreCase("beneficiaryDetails") || getActionName().equalsIgnoreCase("assessmentDetails") || getActionName().equalsIgnoreCase("level3OuList") || getActionName().equalsIgnoreCase("level4OuList") || getActionName().equalsIgnoreCase("delete"))
        return errors;
        else if(this.getDateOfAssessment()==null || this.getDateOfAssessment().indexOf("/")==-1)
        errors.add("dateOfAssessment", new ActionMessage("errors.dateOfAssessment.required"));
        else if(!ValidationManager.isValidDate(getDateOfAssessment()))
        errors.add("dateOfAssessment", new ActionMessage("errors.dateOfAssessment.required"));
        else if(!ValidationManager.compareDateWithCurrentDate(getDateOfAssessment()))
        errors.add("dateOfAssessment", new ActionMessage("errors.dateOfAssessment.postdated"));
        else if(!ValidationManager.dateAfterEnrollmentDate(this.getBeneficiaryId(),getDateOfAssessment(),AppConstant.OVC_TYPE_NUM))
        errors.add("dateOfAssessment", new ActionMessage("errors.dateOfAssessment.beforeEnrollment"));
        else if(this.getBeneficiaryId()==null || this.getBeneficiaryId().equalsIgnoreCase("select"))
        errors.add("beneficiaryId", new ActionMessage("errors.beneficiaryId.required"));
        return errors;
        
    }
}
