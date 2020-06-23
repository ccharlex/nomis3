/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.business;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author smomoh
 */
public class CareAndSupportChecklist implements Serializable
{
    private int recordId;
    private String beneficiaryId;
    private int coughSymptom;
    private int childLossinWeight;
    private int childHasFever;
    private int childHasNightSweat;
    private int childHasSwelling;
    private Date dateOfAssessment;
    private Date dateCreated;
    private Date lastModifiedDate;
    private int enrolledOnTreatment;
    private String facilityId;
    private int pickedUpMedication;
    private int missedARVsRecently;
    private String reasonsPeopleSkipARV;
    private int viralLoadTestDone;
    private Date dateOfViralLoadTest;
    private int viralLoadResultKnown;
    private int viralLoadResult;
    private String reasonViralLoadNotDone;
    private int receivedTransportationSupport;
    private int monthsOfTransportationSupport;
    private int soresRashPainExperience;
    private Date dateOfNextAppointment;
    private int markedForDelete;
    private String volunteerName;
    private String recordedBy;

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

    public Date getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(Date dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateOfNextAppointment() {
        return dateOfNextAppointment;
    }

    public void setDateOfNextAppointment(Date dateOfNextAppointment) {
        this.dateOfNextAppointment = dateOfNextAppointment;
    }

    public Date getDateOfViralLoadTest() {
        return dateOfViralLoadTest;
    }

    public void setDateOfViralLoadTest(Date dateOfViralLoadTest) {
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(int markedForDelete) {
        this.markedForDelete = markedForDelete;
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

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
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

    public String getReasonsPeopleSkipARV() {
        return reasonsPeopleSkipARV;
    }

    public void setReasonsPeopleSkipARV(String reasonsPeopleSkipARV) {
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

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public int getSoresRashPainExperience() {
        return soresRashPainExperience;
    }

    public void setSoresRashPainExperience(int soresRashPainExperience) {
        this.soresRashPainExperience = soresRashPainExperience;
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
    
}
