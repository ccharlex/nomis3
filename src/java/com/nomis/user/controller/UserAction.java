/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.user.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.AppManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author smomoh
 */
public class UserAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        UserForm usf=(UserForm)form;
        String moduleName="User account setup";
        HttpSession session=request.getSession();
        DaoUtility util=new DaoUtility();
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        
        if(AccessManager.isUserInAddUserRole(user))
        {
            setButtonState(session,"false","true");
        }
        else
        {
            setButtonState(session,"true","true");
            request.setAttribute("accessErrorMsg", AppConstant.DEFAULT_ACCESS_MSG);
            return mapping.findForward(SUCCESS);
        }
        String userName=appManager.getCurrentUserName(session);
        //OrganizationUnitAttributesManager ouam=new OrganizationUnitAttributesManager();
        String requiredAction=usf.getActionName();
        String param=request.getParameter("p");
        if(param !=null)
        requiredAction=param;
        List ouList=new ArrayList();//ouam.getOrganizationUnitsWithParentNames(4);
        session.setAttribute("ouWithPathNamesForUserAccount", ouList);
        //setButtonState(session,"false","true");
        setUserList(session);
        if(requiredAction==null)
        {
            usf.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        
        else if(requiredAction.equalsIgnoreCase("edit"))
        {
            String username=request.getParameter("id");
            User user1=util.getUserDaoInstance().getUser(username);
            if(user1 !=null)
            {
                usf=getUserAccountForm(usf,user1);
                setButtonState(session,"true","false");
            }
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            User nomisUser=getUser(usf,userName);
            util.getUserDaoInstance().saveUser(getUser(usf,userName));
            saveUserActivity(userName,moduleName,"Saved new user record with name "+nomisUser.getUsername());
            setUserList(session);
            usf.reset(mapping, request);
        }
        else if(requiredAction.equalsIgnoreCase("update"))
        {
            User nomisUser=getUser(usf,userName);
            util.getUserDaoInstance().updateUser(getUser(usf,userName));
            saveUserActivity(userName,moduleName,"Modified user record with name "+nomisUser.getUsername());
            setUserList(session);
            usf.reset(mapping, request);
        }
        else if(requiredAction.equalsIgnoreCase("delete"))
        {
            User nomisUser=getUser(usf,userName);
            util.getUserDaoInstance().deleteUser(getUser(usf,userName));
            saveUserActivity(userName,moduleName,"Requested user record with name "+nomisUser.getUsername());
            setUserList(session);
            usf.reset(mapping, request);
        }
        return mapping.findForward(SUCCESS);
    }
    private void saveUserActivity(String userName,String userAction,String description)
    {
        UserActivityManager uam=new UserActivityManager();
        uam.saveUserActivity(userName, userAction,description);
    }
    private User getUser(UserForm usf,String userName)
    {
        AppUtility appUtil=new AppUtility();
        User user=new User();
        user.setAccessPrivileges(appUtil.concatStr(usf.getPrivileges(), null));
        user.setDataEntryOu(appUtil.concatStr(usf.getDataEntryOu(), null));
        user.setReportOu(appUtil.concatStr(usf.getReportOu(), null));
        user.setAccountStatus(usf.getAccountStatus());
        user.setAddress(usf.getAddress());
        user.setAssignedGroupId(usf.getAssignedGroup());
        user.setChangePwd(usf.getChangePwd());
        user.setDateCreated(DateManager.getCurrentDateInstance());
        user.setEmail(usf.getEmail());
        user.setFirstName(usf.getFirstname());
        user.setLastModifiedDate(DateManager.getCurrentDateInstance());
        user.setLastName(usf.getSurname());
        user.setOrgUnitGroupId(usf.getOrgunitGroup());
        user.setPassword(usf.getPassword());
        user.setPhone(usf.getPhone());
        user.setUserGroupId(usf.getUsergroup());
        user.setUsername(usf.getUsername());
        user.setUserroleId(usf.getUserrole());
        user.setViewClientDetails(user.getViewClientDetails());
        user.setRecordedBy(userName);
        return user;
    }
    private UserForm getUserAccountForm(UserForm usf,User user)
    {
        AppUtility appUtil=new AppUtility();
        usf.setAccountStatus(user.getAccountStatus());
        usf.setAddress(user.getAddress());
        usf.setAssignedGroup(user.getAssignedGroupId());
        usf.setChangePwd(user.getChangePwd());
        usf.setEmail(user.getEmail());
        usf.setFirstname(user.getFirstName());
        usf.setSurname(user.getLastName());
        usf.setOrgunitGroup(user.getOrgUnitGroupId());
        usf.setPassword(user.getPassword());
        usf.setConfirmPwd(user.getPassword());
        usf.setPhone(user.getPhone());
        usf.setUsergroup(user.getUserGroupId());
        usf.setUsername(user.getUsername());
        usf.setUserrole(user.getUserroleId());
        usf.setViewClientDetails(user.getViewClientDetails());
        usf.setPrivileges(appUtil.splitString(user.getAccessPrivileges(),","));
        usf.setDataEntryOu(appUtil.splitString(user.getDataEntryOu(),","));
        usf.setReportOu(appUtil.splitString(user.getReportOu(),","));
        return usf;
    }
    private void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("userSaveDisabled", saveDisabled);
        session.setAttribute("userModifyDisabled", modifyDisabled);
    }
    private void setUserList(HttpSession session)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            List list=util.getUserDaoInstance().getAllUsers();
            if(list==null)
            list=new ArrayList();
            session.setAttribute("userList", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
