/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.sitesetup.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.ovc.business.SiteSetup;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.OrganizationUnitDao;
import com.nomis.ovc.dao.OrganizationUnitHierarchyDao;
import com.nomis.ovc.dao.PartnerDao;
import com.nomis.ovc.dao.PartnerDaoImpl;
import com.nomis.ovc.dao.SiteSetupDao;
import com.nomis.ovc.dao.SiteSetupDaoImpl;
import com.nomis.ovc.metadata.OrganizationUnitHierarchy;
import com.nomis.ovc.util.AppConstant;
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
public class SiteSetupAction extends org.apache.struts.action.Action {

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
        SiteSetupForm ssform=(SiteSetupForm)form;
        String moduleName="Site setup";
        HttpSession session=request.getSession();
        DaoUtility daoutil=new DaoUtility();
        OrganizationUnitHierarchyDao ouhdao=daoutil.getOrganizationUnitHierarchyDaoInstance();
        OrganizationUnitDao oudao=daoutil.getOrganizationUnitDaoInstance();
        PartnerDao pdao=new PartnerDaoImpl();
        AppManager appManager=new AppManager();
        String userName=appManager.getCurrentUserName(session);
        String ouhl2Name="State";
        OrganizationUnitHierarchy level2orgunitHierarchy=ouhdao.getOrganizationUnitHierarchyByLevel(2);
        if(level2orgunitHierarchy !=null)
        ouhl2Name=level2orgunitHierarchy.getName();
        
        User user=appManager.getCurrentUser(session);
        if(AccessManager.isUserInOrganizationUnitSetupRole(user))
        {
            setButtonState(session,"false","true");
        }
        else
        {
            setButtonState(session,"true","true");
            request.setAttribute("accessErrorMsg", AppConstant.DEFAULT_ACCESS_MSG);
            return mapping.findForward(SUCCESS);
        }
        List level2orgUnitList=oudao.getOrganizationUnit(2);
        if(level2orgUnitList==null)
        level2orgUnitList=new ArrayList();
        
        List partnerList=pdao.getAllPartners();
        if(partnerList==null)
        partnerList=new ArrayList();
        
        session.setAttribute("ouhLevel2", ouhl2Name);
        session.setAttribute("level2orgUnitList", level2orgUnitList);
        session.setAttribute("partnerList", partnerList);
        generateSiteSetupList(session);
        setUserList(session);
        //setButtonState(session,"false","true");
        
        String requiredAction=ssform.getActionName();
        String param=request.getParameter("p");
        if(param !=null)
        requiredAction=param;
        
        if(requiredAction==null)
        {
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            SiteSetup setup=getSiteSetup(ssform);
            daoutil.getSiteSetupDaoInstance().saveSiteSetup(setup);
            saveUserActivity(userName,moduleName,"Saved new Site setup record for user "+setup.getUserName());
            generateSiteSetupList(session);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("modify"))
        {
            SiteSetup setup=getSiteSetup(ssform);
            daoutil.getSiteSetupDaoInstance().updateSiteSetup(setup);
            saveUserActivity(userName,moduleName,"Modified Site setup record for user "+setup.getUserName());
            generateSiteSetupList(session);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("de"))
        {
            SiteSetup setup=getSiteSetup(ssform);
            daoutil.getSiteSetupDaoInstance().deleteSiteSetup(setup);
            saveUserActivity(userName,moduleName,"Requested Site setup record for user "+setup.getUserName()+" be deleted");
            generateSiteSetupList(session);
            //return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("ed"))
        {
            String userId=request.getParameter("id");
            SiteSetup setup=daoutil.getSiteSetupDaoInstance().getSiteSetup(userId);
            if(setup !=null)
            {
                ssform.setPartner(setup.getPartnerCode());
                ssform.setOrgUnitId(setup.getOrgUnitId());
                ssform.setUserName(userId);
                setButtonState(session,"true","false");
            }
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("delete"))
        {
            SiteSetup setup=getSiteSetup(ssform);
            daoutil.getSiteSetupDaoInstance().deleteSiteSetup(setup);
            saveUserActivity(userName,moduleName,"Requested Site setup record for user "+setup.getUserName()+" be deleted");
            generateSiteSetupList(session);
            return mapping.findForward(SUCCESS);
        }
        return mapping.findForward(SUCCESS);
    }
    private void saveUserActivity(String userName,String userAction,String description)
    {
        UserActivityManager uam=new UserActivityManager();
        uam.saveUserActivity(userName, userAction,description);
    }
    private SiteSetup getSiteSetup(SiteSetupForm ssform)
    {
        SiteSetup setup=new SiteSetup();
        setup.setCreatedBy(ssform.getUserName());
        setup.setDateCreated(DateManager.getDateInstance(DateManager.getCurrentDate()));
        setup.setLastModifiedDate(DateManager.getDateInstance(DateManager.getCurrentDate()));
        setup.setOrgUnitId(ssform.getOrgUnitId());
        setup.setPartnerCode(ssform.getPartner());
        setup.setUserName(ssform.getUserName());
        
        return setup;
    }
    private void generateSiteSetupList(HttpSession session)
    {
        try
        {
            SiteSetupDao ssdao=new SiteSetupDaoImpl();
            List list=ssdao.getAllSiteSetups();
            if(list==null)
            list=new ArrayList();
            session.setAttribute("siteSetupList", list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("ssSaveDisabled", saveDisabled);
        session.setAttribute("ssModifyDisabled", modifyDisabled);
    }
    private void setUserList(HttpSession session)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            List userList=util.getUserDaoInstance().getAllUsers();
            if(userList==null)
            userList=new ArrayList();
            session.setAttribute("siteSetupUserList", userList);
            //siteSetupUserList
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
