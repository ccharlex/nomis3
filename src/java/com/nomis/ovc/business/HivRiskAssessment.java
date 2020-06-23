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
public class HivRiskAssessment implements Serializable
{
    private int recordId;
    private String ovcId;
    private String cboId;
    private String level2OuId;
    private String level3OuId;
    private String organizationUnitId;
    private int hivParentQuestion;
    private int motherSicknessQuestion;
    private int hivSibblingQuestion;
    private int sibblingSicknessQuestion;
    private int childSickQuestion;
    private int childHasMoreThanTwoIllnessQuestion;
    private int bloodTransfusionQuestion;
    private int childCircumcisedOrEarPierced;
    private int childEverPregnantQuestion;
    private int sexualAbuseQuestion;
    private int childAtRiskQuestion;
    private Date dateOfAssessment;
    private Date dateCreated;
    private Date lastModifiedDate;
    private int childAgeQuestion;
    private int childTestedQuestion;
    private int hivStatusQuestion;
    private int hivStatusAtRiskAssessment;
    
    private int chronicallyIllQuestion;
    private int sexuallyActiveQuestion;
    private int muacbmiQuestion;
    
    private int ageAtRiskAssessment;
    private int ageUnitAtRiskAssessment;
    private int markedForDelete;
    private String recordedBy;
    private String nameOfAssessor;
    int serialNo=0;
    String rowColor="#FFFFFF";

    public int getAgeAtRiskAssessment() {
        return ageAtRiskAssessment;
    }

    public void setAgeAtRiskAssessment(int ageAtRiskAssessment) {
        this.ageAtRiskAssessment = ageAtRiskAssessment;
    }

    public int getAgeUnitAtRiskAssessment() {
        return ageUnitAtRiskAssessment;
    }

    public void setAgeUnitAtRiskAssessment(int ageUnitAtRiskAssessment) {
        this.ageUnitAtRiskAssessment = ageUnitAtRiskAssessment;
    }

    public int getBloodTransfusionQuestion() {
        return bloodTransfusionQuestion;
    }

    public void setBloodTransfusionQuestion(int bloodTransfusionQuestion) {
        this.bloodTransfusionQuestion = bloodTransfusionQuestion;
    }

    public String getCboId() {
        return cboId;
    }

    public void setCboId(String cboId) {
        this.cboId = cboId;
    }

    public int getChildAgeQuestion() {
        return childAgeQuestion;
    }

    public void setChildAgeQuestion(int childAgeQuestion) {
        this.childAgeQuestion = childAgeQuestion;
    }

    public int getChildAtRiskQuestion() {
        return childAtRiskQuestion;
    }

    public void setChildAtRiskQuestion(int childAtRiskQuestion) {
        this.childAtRiskQuestion = childAtRiskQuestion;
    }

    public int getChildCircumcisedOrEarPierced() {
        return childCircumcisedOrEarPierced;
    }

    public void setChildCircumcisedOrEarPierced(int childCircumcisedOrEarPierced) {
        this.childCircumcisedOrEarPierced = childCircumcisedOrEarPierced;
    }

    public int getChildEverPregnantQuestion() {
        return childEverPregnantQuestion;
    }

    public void setChildEverPregnantQuestion(int childEverPregnantQuestion) {
        this.childEverPregnantQuestion = childEverPregnantQuestion;
    }

    public int getChildHasMoreThanTwoIllnessQuestion() {
        return childHasMoreThanTwoIllnessQuestion;
    }

    public void setChildHasMoreThanTwoIllnessQuestion(int childHasMoreThanTwoIllnessQuestion) {
        this.childHasMoreThanTwoIllnessQuestion = childHasMoreThanTwoIllnessQuestion;
    }

    public int getChildSickQuestion() {
        return childSickQuestion;
    }

    public void setChildSickQuestion(int childSickQuestion) {
        this.childSickQuestion = childSickQuestion;
    }

    public int getChildTestedQuestion() {
        return childTestedQuestion;
    }

    public void setChildTestedQuestion(int childTestedQuestion) {
        this.childTestedQuestion = childTestedQuestion;
    }

    public Date getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(Date dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public int getHivParentQuestion() {
        return hivParentQuestion;
    }

    public void setHivParentQuestion(int hivParentQuestion) {
        this.hivParentQuestion = hivParentQuestion;
    }

    public int getHivSibblingQuestion() {
        return hivSibblingQuestion;
    }

    public void setHivSibblingQuestion(int hivSibblingQuestion) {
        this.hivSibblingQuestion = hivSibblingQuestion;
    }

    public int getHivStatusAtRiskAssessment() {
        return hivStatusAtRiskAssessment;
    }

    public void setHivStatusAtRiskAssessment(int hivStatusAtRiskAssessment) {
        this.hivStatusAtRiskAssessment = hivStatusAtRiskAssessment;
    }

    public int getHivStatusQuestion() {
        return hivStatusQuestion;
    }

    public void setHivStatusQuestion(int hivStatusQuestion) {
        this.hivStatusQuestion = hivStatusQuestion;
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

    public int getMotherSicknessQuestion() {
        return motherSicknessQuestion;
    }

    public void setMotherSicknessQuestion(int motherSicknessQuestion) {
        this.motherSicknessQuestion = motherSicknessQuestion;
    }

    public String getOrganizationUnitId() {
        return organizationUnitId;
    }

    public void setOrganizationUnitId(String organizationUnitId) {
        this.organizationUnitId = organizationUnitId;
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

    public int getSexualAbuseQuestion() {
        return sexualAbuseQuestion;
    }

    public void setSexualAbuseQuestion(int sexualAbuseQuestion) {
        this.sexualAbuseQuestion = sexualAbuseQuestion;
    }

    public int getSibblingSicknessQuestion() {
        return sibblingSicknessQuestion;
    }

    public void setSibblingSicknessQuestion(int sibblingSicknessQuestion) {
        this.sibblingSicknessQuestion = sibblingSicknessQuestion;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getNameOfAssessor() {
        return nameOfAssessor;
    }

    public void setNameOfAssessor(String nameOfAssessor) {
        this.nameOfAssessor = nameOfAssessor;
    }

    public int getChronicallyIllQuestion() {
        return chronicallyIllQuestion;
    }

    public void setChronicallyIllQuestion(int chronicallyIllQuestion) {
        this.chronicallyIllQuestion = chronicallyIllQuestion;
    }

    public int getMuacbmiQuestion() {
        return muacbmiQuestion;
    }

    public void setMuacbmiQuestion(int muacbmiQuestion) {
        this.muacbmiQuestion = muacbmiQuestion;
    }

    public int getSexuallyActiveQuestion() {
        return sexuallyActiveQuestion;
    }

    public void setSexuallyActiveQuestion(int sexuallyActiveQuestion) {
        this.sexuallyActiveQuestion = sexuallyActiveQuestion;
    }

    public String getRowColor() {
        return rowColor;
    }

    public void setRowColor(String rowColor) {
        this.rowColor = rowColor;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

}
