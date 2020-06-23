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
public class CareplanAchievementChecklist implements Serializable
{
    private int recordId;
    private String hhUniqueId;
    private Date dateOfAssessment;
    private Date dateCreated;
    private Date lastModifiedDate;
    private int childrenHivStatusknown;
    private int hivPosAdolscentsLinked;
    private int documentedViralLoadResult;
    private int hivPreventionKnowledge;
    private int childrenNotUndernourished;
    private int allChildrenHaveBirthCert;
    private int stableAdultInHousehold;
    private int violenceIncidenceReport;
    private int childrenEnrolledInSchool;
    private int regularSchoolAttendance;
    private int cgiversEconomicActivity;
    private int emotionalSupportTeamIdentification;
    private int markedForDelete;
    private String recordedBy;
    private String volunteerName;
    private int score;

    public int getAllChildrenHaveBirthCert() {
        return allChildrenHaveBirthCert;
    }

    public void setAllChildrenHaveBirthCert(int allChildrenHaveBirthCert) {
        this.allChildrenHaveBirthCert = allChildrenHaveBirthCert;
    }

    public int getCgiversEconomicActivity() {
        return cgiversEconomicActivity;
    }

    public void setCgiversEconomicActivity(int cgiversEconomicActivity) {
        this.cgiversEconomicActivity = cgiversEconomicActivity;
    }

    public int getChildrenEnrolledInSchool() {
        return childrenEnrolledInSchool;
    }

    public void setChildrenEnrolledInSchool(int childrenEnrolledInSchool) {
        this.childrenEnrolledInSchool = childrenEnrolledInSchool;
    }

    public int getChildrenHivStatusknown() {
        return childrenHivStatusknown;
    }

    public void setChildrenHivStatusknown(int childrenHivStatusknown) {
        this.childrenHivStatusknown = childrenHivStatusknown;
    }

    public int getChildrenNotUndernourished() {
        return childrenNotUndernourished;
    }

    public void setChildrenNotUndernourished(int childrenNotUndernourished) {
        this.childrenNotUndernourished = childrenNotUndernourished;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(Date dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public int getDocumentedViralLoadResult() {
        return documentedViralLoadResult;
    }

    public void setDocumentedViralLoadResult(int documentedViralLoadResult) {
        this.documentedViralLoadResult = documentedViralLoadResult;
    }

    public int getEmotionalSupportTeamIdentification() {
        return emotionalSupportTeamIdentification;
    }

    public void setEmotionalSupportTeamIdentification(int emotionalSupportTeamIdentification) {
        this.emotionalSupportTeamIdentification = emotionalSupportTeamIdentification;
    }

    public String getHhUniqueId() {
        return hhUniqueId;
    }

    public void setHhUniqueId(String hhUniqueId) {
        this.hhUniqueId = hhUniqueId;
    }

    public int getHivPosAdolscentsLinked() {
        return hivPosAdolscentsLinked;
    }

    public void setHivPosAdolscentsLinked(int hivPosAdolscentsLinked) {
        this.hivPosAdolscentsLinked = hivPosAdolscentsLinked;
    }

    public int getHivPreventionKnowledge() {
        return hivPreventionKnowledge;
    }

    public void setHivPreventionKnowledge(int hivPreventionKnowledge) {
        this.hivPreventionKnowledge = hivPreventionKnowledge;
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

    public int getRegularSchoolAttendance() {
        return regularSchoolAttendance;
    }

    public void setRegularSchoolAttendance(int regularSchoolAttendance) {
        this.regularSchoolAttendance = regularSchoolAttendance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStableAdultInHousehold() {
        return stableAdultInHousehold;
    }

    public void setStableAdultInHousehold(int stableAdultInHousehold) {
        this.stableAdultInHousehold = stableAdultInHousehold;
    }

    public int getViolenceIncidenceReport() {
        return violenceIncidenceReport;
    }

    public void setViolenceIncidenceReport(int violenceIncidenceReport) {
        this.violenceIncidenceReport = violenceIncidenceReport;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    } 
}
