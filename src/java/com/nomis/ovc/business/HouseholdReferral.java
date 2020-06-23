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
public class HouseholdReferral implements Serializable
{
    private int id;
    private String beneficiaryId;
    private int beneficiaryType;
    private Date dateOfReferral;
    private Date dateCreated;
    private Date lastModifiedDate;
    private String stableServices;
    private String healthServices;
    private String safetyServices;
    private String schooledServices;
    private String gbvServices;
    private int referralComplete;
    private String recordedBy;
    private String communityWorker;
    private int numberOfServices;
    private int markedForDelete;
    private int ageAtReferral;
    private int ageUnitAtReferral;
    private String referringOrganization;
    private String receivingOrganization;
    private String stableServiceNames;
    private String healthServiceNames;
    private String safetyServiceNames;
    private String schooledServiceNames;
    private String gbvServiceNames;
    int serialNo=0;
    String rowColor="#FFFFFF";

    public int getAgeAtReferral() {
        return ageAtReferral;
    }

    public void setAgeAtReferral(int ageAtReferral) {
        this.ageAtReferral = ageAtReferral;
    }

    public int getAgeUnitAtReferral() {
        return ageUnitAtReferral;
    }

    public void setAgeUnitAtReferral(int ageUnitAtReferral) {
        this.ageUnitAtReferral = ageUnitAtReferral;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getCommunityWorker() {
        return communityWorker;
    }

    public void setCommunityWorker(String communityWorker) {
        this.communityWorker = communityWorker;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateOfReferral() {
        return dateOfReferral;
    }

    public void setDateOfReferral(Date dateOfReferral) {
        this.dateOfReferral = dateOfReferral;
    }

    public String getGbvServiceNames() {
        return gbvServiceNames;
    }

    public void setGbvServiceNames(String gbvServiceNames) {
        this.gbvServiceNames = gbvServiceNames;
    }

    public String getGbvServices() {
        return gbvServices;
    }

    public void setGbvServices(String gbvServices) {
        this.gbvServices = gbvServices;
    }

    public String getHealthServiceNames() {
        return healthServiceNames;
    }

    public void setHealthServiceNames(String healthServiceNames) {
        this.healthServiceNames = healthServiceNames;
    }

    public String getHealthServices() {
        return healthServices;
    }

    public void setHealthServices(String healthServices) {
        this.healthServices = healthServices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNumberOfServices() {
        return numberOfServices;
    }

    public void setNumberOfServices(int numberOfServices) {
        this.numberOfServices = numberOfServices;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getRowColor() {
        return rowColor;
    }

    public void setRowColor(String rowColor) {
        this.rowColor = rowColor;
    }

    public String getSafetyServiceNames() {
        return safetyServiceNames;
    }

    public void setSafetyServiceNames(String safetyServiceNames) {
        this.safetyServiceNames = safetyServiceNames;
    }

    public String getSafetyServices() {
        return safetyServices;
    }

    public void setSafetyServices(String safetyServices) {
        this.safetyServices = safetyServices;
    }

    public String getSchooledServiceNames() {
        return schooledServiceNames;
    }

    public void setSchooledServiceNames(String schooledServiceNames) {
        this.schooledServiceNames = schooledServiceNames;
    }

    public String getSchooledServices() {
        return schooledServices;
    }

    public void setSchooledServices(String schooledServices) {
        this.schooledServices = schooledServices;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getStableServiceNames() {
        return stableServiceNames;
    }

    public void setStableServiceNames(String stableServiceNames) {
        this.stableServiceNames = stableServiceNames;
    }

    public String getStableServices() {
        return stableServices;
    }

    public void setStableServices(String stableServices) {
        this.stableServices = stableServices;
    }

    public String getReceivingOrganization() {
        return receivingOrganization;
    }

    public void setReceivingOrganization(String receivingOrganization) {
        this.receivingOrganization = receivingOrganization;
    }

    public String getReferringOrganization() {
        return referringOrganization;
    }

    public void setReferringOrganization(String referringOrganization) {
        this.referringOrganization = referringOrganization;
    }

    public int getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(int beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public int getReferralComplete() {
        return referralComplete;
    }

    public void setReferralComplete(int referralComplete) {
        this.referralComplete = referralComplete;
    }
    
    
}
