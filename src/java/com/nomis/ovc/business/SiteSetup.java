/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.business;

import com.nomis.operationsManagement.StartupResourceManager;
import com.nomis.ovc.metadata.OrganizationUnit;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author smomoh
 */
public class SiteSetup implements Serializable
{
    private String userName;
    private String partnerCode;
    private String orgUnitId;
    private Date dateCreated;
    private Date lastModifiedDate;
    private String createdBy;
    private Partner partner;
    private OrganizationUnit organizationUnit;
    StartupResourceManager objmanager=new StartupResourceManager();
    public String getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Partner getPartner() {
        partner=objmanager.getPartner(getPartnerCode());
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public OrganizationUnit getOrganizationUnit() {
        organizationUnit=objmanager.getOrganizationUnit(getOrgUnitId());
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
}
