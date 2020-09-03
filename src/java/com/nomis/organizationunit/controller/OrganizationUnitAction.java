/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.organizationunit.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.UserActivityManager;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.OrganizationUnitDao;
import com.nomis.ovc.dao.OrganizationUnitDaoImpl;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.DateManager;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.AppUtility;
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
public class OrganizationUnitAction extends org.apache.struts.action.Action {

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
        DaoUtility util=new DaoUtility();
        OrganizationUnitForm ousform=(OrganizationUnitForm)form;
        String moduleName="Organization unit setup";
        HttpSession session=request.getSession();
        AppManager appManager=new AppManager();
        String userName=appManager.getCurrentUserName(session);
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
        
        String requiredAction=ousform.getActionName();
        
        String reqParam=request.getParameter("p");
        if(reqParam !=null)
        requiredAction=reqParam;
        
        List list=util.getOrganizationUnitHierarchyDaoInstance().getAllOrganizationUnitHierarchyRecords();
        if(list==null)
        list=new ArrayList();
        session.setAttribute("hierarchyList", list);
        System.err.println("hierarchyList is size "+list.size());
        generateParentList(session,ousform.getOulevel());
        generateOrgUnitList(session);
        
        if(requiredAction==null)
        {
            ousform.reset(mapping, request);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("parentList"))
        {
            generateParentList(session,ousform.getOulevel());
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("edit"))
        {
            String uid=request.getParameter("id");
            OrganizationUnit ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(uid);
            if(ou !=null)
            {
                ousform.reset(mapping, request);
                ousform.setOulevel(ou.getOulevel());
                ousform.setDescription(ou.getDescription());
                ousform.setName(ou.getName());
                ousform.setUid(ou.getUid());
                ousform.setParentId(ou.getParentId());
                generateParentList(session,ou.getOulevel());
                setButtonState(session,"true","false");
            }
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            OrganizationUnit ou=getOrganizationUnit(ousform);
            util.getOrganizationUnitDaoInstance().saveOrganizationUnit(ou);
            saveUserActivity(userName,moduleName,"Saved new Organization unit record with Id "+ou.getName());
            ousform.reset(mapping, request);
            generateOrgUnitList(session);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("update"))
        {
            OrganizationUnit ou=getOrganizationUnit(ousform);
            ou.setUid(ousform.getUid());
            ou.setOuPath(getOuPath(ou));
            util.getOrganizationUnitDaoInstance().updateOrganizationUnit(ou);
            saveUserActivity(userName,moduleName,"Modified Organization unit record with name "+ou.getName());
            ousform.reset(mapping, request);
            generateOrgUnitList(session);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("delete"))
        {
            String uid=request.getParameter("id");
            OrganizationUnit ou=getOrganizationUnit(ousform);
            ou.setUid(uid);
            System.err.println("uid is "+uid);
            util.getOrganizationUnitDaoInstance().deleteOrganizationUnit(ou);
            saveUserActivity(userName,moduleName,"Requested Organization unit record with name "+ou.getName()+" be deleted");
            ousform.reset(mapping, request);
            generateOrgUnitList(session);
            return mapping.findForward(SUCCESS);
        }
        return mapping.findForward(SUCCESS);
    }
    private void saveUserActivity(String userName,String userAction,String description)
    {
        UserActivityManager uam=new UserActivityManager();
        uam.saveUserActivity(userName, userAction,description);
    }
    private OrganizationUnit getOrganizationUnit(OrganizationUnitForm ousform)
    {
        OrganizationUnit ou=new OrganizationUnit();
        ou.setDateCreated(DateManager.getDateInstance(DateManager.getCurrentDate()));
        ou.setDescription(ousform.getDescription());
        ou.setLastModifiedDate(DateManager.getDateInstance(DateManager.getCurrentDate()));
        ou.setName(ousform.getName());
        ou.setParentId(ousform.getParentId());
        ou.setOucode(ousform.getOucode());
        ou.setOulevel(ousform.getOulevel());
        
        return ou;
    }
    public void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        if(AppUtility.isMetadataAccessEnabled())
        {
            session.setAttribute("ouSaveDisabled", saveDisabled);
            session.setAttribute("ouModifyDisabled", modifyDisabled);
        }
        else
        {
           session.setAttribute("ouSaveDisabled", "true");
           session.setAttribute("ouModifyDisabled", "true");
        }
    }
    private void generateParentList(HttpSession session,int oulevel)
    {
        try
        {
            DaoUtility util=new DaoUtility();
            if(oulevel>0)
            --oulevel;
            List parentList=util.getOrganizationUnitDaoInstance().getOrganizationUnitsByOuLevel(oulevel);
            if(parentList ==null)
            parentList=new ArrayList();
            session.setAttribute("ouparentList", parentList);  
            System.err.println("ouparentList is size "+parentList.size());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void generateOrgUnitList(HttpSession session)
    {
        try
        {
            OrganizationUnitDao oudao=new OrganizationUnitDaoImpl();
            List orgUnitList=oudao.getAllOrganizationUnit();
            if(orgUnitList ==null)
            orgUnitList=new ArrayList();
            session.setAttribute("orgUnitList", orgUnitList);  
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private String getOuPath(OrganizationUnit ou)
    {
        String ouPath=null;
        try
        {
            OrganizationUnitDao oudao=new OrganizationUnitDaoImpl();
            if(ou !=null)
            {
                ouPath=ou.getUid();
                OrganizationUnit parentOu;
                int parentOuLevel=ou.getOulevel();
                while(parentOuLevel>1)
                {
                    parentOu=oudao.getOrganizationUnit(ou.getParentId());
                    if(parentOu !=null)
                    {
                        ouPath=parentOu.getUid()+"/"+ouPath;
                        parentOuLevel=parentOu.getOulevel();
                        ou=parentOu;
                    }
                    else
                    break;
                }
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        if(ouPath !=null)
        ouPath=ouPath+"/";
        System.err.println("ouPath is "+ouPath);
        return ouPath;
    }
}
