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
public class User implements Serializable
{
    private String username;
    private String password;
    private String userroleId;
    private String firstName;
    private String lastName;
    private String changePwd;
    private String viewClientDetails;
    private String userGroupId;
    private String orgUnitGroupId;
    private String assignedGroupId;
    private String address;
    private String phone;
    private String email;
    private Date dateCreated;
    private Date lastModifiedDate;
    private String accountStatus;
    private String recordedBy;
    private String partnerCodes;
    private boolean superUser;
    private String accessPrivileges;
    private String dataEntryOu;
    private String reportOu=null;
    
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAssignedGroupId() {
        return assignedGroupId;
    }

    public void setAssignedGroupId(String assignedGroupId) {
        this.assignedGroupId = assignedGroupId;
    }

    public String getChangePwd() {
        return changePwd;
    }

    public void setChangePwd(String changePwd) {
        this.changePwd = changePwd;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrgUnitGroupId() {
        return orgUnitGroupId;
    }

    public void setOrgUnitGroupId(String orgUnitGroupId) {
        this.orgUnitGroupId = orgUnitGroupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserroleId() {
        return userroleId;
    }

    public void setUserroleId(String userroleId) {
        this.userroleId = userroleId;
    }

    public String getViewClientDetails() {
        return viewClientDetails;
    }

    public void setViewClientDetails(String viewClientDetails) {
        this.viewClientDetails = viewClientDetails;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getPartnerCodes() {
        return partnerCodes;
    }

    public void setPartnerCodes(String partnerCodes) {
        this.partnerCodes = partnerCodes;
    }

    public boolean isSuperUser() 
    {
        //System.err.println("getUserroleId() is "+getUserroleId());
        if(getUserroleId() !=null && getUserroleId().equalsIgnoreCase("superuserxx"))
        superUser=true;
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public String getAccessPrivileges() {
        return accessPrivileges;
    }

    public void setAccessPrivileges(String accessPrivileges) {
        this.accessPrivileges = accessPrivileges;
    }

    public String getDataEntryOu() {
        return dataEntryOu;
    }

    public void setDataEntryOu(String dataEntryOu) {
        this.dataEntryOu = dataEntryOu;
    }

    public String getReportOu() {
        return reportOu;
    }

    public void setReportOu(String reportOu) {
        this.reportOu = reportOu;
    }
    
}
