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
public class ChildEducationPerformanceAssessment implements Serializable
{
    private int recordId;
    private String ovcId;
    private Date dateOfAssessment;
    private Date dateCreated;
    private Date lastModifiedDate;
    private int childHasInjuriesOrMarks;
    private int childIsSociallyWithdrawn;
    private int signsOfFatigueAndTiredness;
    private int regularSchoolAttendance;
    private int steadyImprovementInClassWork;
    private int earlyResumptionInSchool;
    private int goodPerformanceInLastExam;
    private int childProgressedInSchool;
    private int childMissVocTraining;
    private int earlyResumptionInTrainingCenter;
    private int steadyImprovementInVocWork;
    private String classTeacherComment;
    private String classTeacherName;
    private String volunteerName;
    private String recordedBy;
    private int markedForDelete;

    public int getChildHasInjuriesOrMarks() {
        return childHasInjuriesOrMarks;
    }

    public void setChildHasInjuriesOrMarks(int childHasInjuriesOrMarks) {
        this.childHasInjuriesOrMarks = childHasInjuriesOrMarks;
    }

    public int getChildIsSociallyWithdrawn() {
        return childIsSociallyWithdrawn;
    }

    public void setChildIsSociallyWithdrawn(int childIsSociallyWithdrawn) {
        this.childIsSociallyWithdrawn = childIsSociallyWithdrawn;
    }

    public int getChildMissVocTraining() {
        return childMissVocTraining;
    }

    public void setChildMissVocTraining(int childMissVocTraining) {
        this.childMissVocTraining = childMissVocTraining;
    }

    public int getChildProgressedInSchool() {
        return childProgressedInSchool;
    }

    public void setChildProgressedInSchool(int childProgressedInSchool) {
        this.childProgressedInSchool = childProgressedInSchool;
    }

    public String getClassTeacherComment() {
        return classTeacherComment;
    }

    public void setClassTeacherComment(String classTeacherComment) {
        this.classTeacherComment = classTeacherComment;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getEarlyResumptionInSchool() {
        return earlyResumptionInSchool;
    }

    public void setEarlyResumptionInSchool(int earlyResumptionInSchool) {
        this.earlyResumptionInSchool = earlyResumptionInSchool;
    }

    public int getEarlyResumptionInTrainingCenter() {
        return earlyResumptionInTrainingCenter;
    }

    public void setEarlyResumptionInTrainingCenter(int earlyResumptionInTrainingCenter) {
        this.earlyResumptionInTrainingCenter = earlyResumptionInTrainingCenter;
    }

    public int getGoodPerformanceInLastExam() {
        return goodPerformanceInLastExam;
    }

    public void setGoodPerformanceInLastExam(int goodPerformanceInLastExam) {
        this.goodPerformanceInLastExam = goodPerformanceInLastExam;
    }

    public String getOvcId() {
        return ovcId;
    }

    public void setOvcId(String ovcId) {
        this.ovcId = ovcId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getRegularSchoolAttendance() {
        return regularSchoolAttendance;
    }

    public void setRegularSchoolAttendance(int regularSchoolAttendance) {
        this.regularSchoolAttendance = regularSchoolAttendance;
    }

    public int getSignsOfFatigueAndTiredness() {
        return signsOfFatigueAndTiredness;
    }

    public void setSignsOfFatigueAndTiredness(int signsOfFatigueAndTiredness) {
        this.signsOfFatigueAndTiredness = signsOfFatigueAndTiredness;
    }

    public int getSteadyImprovementInClassWork() {
        return steadyImprovementInClassWork;
    }

    public void setSteadyImprovementInClassWork(int steadyImprovementInClassWork) {
        this.steadyImprovementInClassWork = steadyImprovementInClassWork;
    }

    public int getSteadyImprovementInVocWork() {
        return steadyImprovementInVocWork;
    }

    public void setSteadyImprovementInVocWork(int steadyImprovementInVocWork) {
        this.steadyImprovementInVocWork = steadyImprovementInVocWork;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public int getMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(int markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
    
}
