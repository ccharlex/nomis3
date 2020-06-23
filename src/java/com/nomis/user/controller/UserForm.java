/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author smomoh
 */
public class UserForm extends org.apache.struts.action.ActionForm {
    
    private String actionName;
    private String username;
    private String password;
    private String confirmPwd;
    private String userrole;
    private String firstname;
    private String surname;
    private String changePwd;
    private String viewClientDetails;
    private String usergroup;
    private String orgunitGroup;
    private String assignedGroup;
    private String address;
    private String phone;
    private String email;
    private String accountStatus;
    private String[] privileges;
    private String[] dataEntryOu;
    private String[] reportOu;
    
    public UserForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
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

    public String getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(String assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public String getChangePwd() {
        return changePwd;
    }

    public void setChangePwd(String changePwd) {
        this.changePwd = changePwd;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String[] getDataEntryOu() {
        return dataEntryOu;
    }

    public void setDataEntryOu(String[] dataEntryOu) {
        this.dataEntryOu = dataEntryOu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getOrgunitGroup() {
        return orgunitGroup;
    }

    public void setOrgunitGroup(String orgunitGroup) {
        this.orgunitGroup = orgunitGroup;
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

    public String[] getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String[] privileges) {
        this.privileges = privileges;
    }

    public String[] getReportOu() {
        return reportOu;
    }

    public void setReportOu(String[] reportOu) {
        this.reportOu = reportOu;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getViewClientDetails() {
        return viewClientDetails;
    }

    public void setViewClientDetails(String viewClientDetails) {
        this.viewClientDetails = viewClientDetails;
    }

@Override
public void reset(ActionMapping mapping, HttpServletRequest request)
{
    actionName=null;
    username=null;
    password=null;
    confirmPwd=null;
    userrole=null;
    firstname=null;
    surname=null;
    changePwd=null;
    viewClientDetails=null;
    usergroup=null;
    orgunitGroup=null;
    assignedGroup=null;
    address=null;
    phone=null;
    email=null;
    accountStatus=null;
    privileges=null;
    dataEntryOu=null;
    reportOu=null;
}
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if(this.getActionName()==null)
        return errors;
        if(this.getUsername()==null || this.getUsername().trim().length()==0)
        errors.add("username", new ActionMessage("error.username.required"));
        else if(this.getPassword()==null || this.getPassword().trim().length()==0)
        errors.add("password", new ActionMessage("error.password.required"));
        else if(this.getConfirmPwd()==null || this.getConfirmPwd().trim().length()==0)
        errors.add("confirmPwd", new ActionMessage("error.confirmPwd.required"));
        else if(!getPassword().trim().equalsIgnoreCase(getConfirmPwd()))
        errors.add("password", new ActionMessage("error.password.confirmPwd.notmatch"));
        else if(this.getFirstname()==null || this.getFirstname().trim().length()==0)
        errors.add("firstname", new ActionMessage("error.firstname.required"));
        else if(this.getSurname()==null || this.getSurname().trim().length()==0)
        errors.add("surname", new ActionMessage("error.surname.required"));
        else if(this.getUserrole()==null || this.getUserrole().trim().length()==0 || getUserrole().equalsIgnoreCase("select"))
        errors.add("userrole", new ActionMessage("error.userrole.required"));
        return errors;
    }
}
