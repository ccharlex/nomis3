/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.maintenance.controller;

import com.nomis.maintenance.DataCleanupManager;
import com.nomis.operationsManagement.AccessManager;
import com.nomis.operationsManagement.EnrollmentStatusManager;
import com.nomis.ovc.business.User;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.DatabaseUtilities;
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
public class AdhocOperationsAction extends org.apache.struts.action.Action {

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
            throws Exception {
        AdhocOperationsForm adoform=(AdhocOperationsForm)form;
        HttpSession session=request.getSession();
        String requiredAction=adoform.getActionName();
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        if(AccessManager.userHasSuperUserRole(user))
        {
            setButtonState(session,"false");
        }
        else
        {
            setButtonState(session,"true");
            //return mapping.findForward(PARAMPAGE);
        }
        
        //AppManager appManager=new AppManager();
        String userName=appManager.getCurrentUserName(session);
        if(requiredAction==null)
        {
            adoform.reset(mapping, request);
        }
        else if(requiredAction.equalsIgnoreCase("resetUnknownDueToRiskAssessment"))
        {
            DatabaseUtilities dbUtils=new DatabaseUtilities();
            dbUtils.revertHivUnknownDueToRiskAssessmentToBaselineHivStatusInChildEnrollment();
            String message=" HIV status unknown due to Risk assessment reset to baseline HIV status";
            request.setAttribute("adhocTaskMsg", message);
        }
        else if(requiredAction.equalsIgnoreCase("updateEnrollmentStatusHistory"))
        {
            EnrollmentStatusManager esm=new EnrollmentStatusManager();
            //esm.updateLastDateOfCurrentEnrollmentStatus();
            String message=""+esm.updateBeneficiaryEnrollmentStatusHistory(userName)+" Enrollment status records updated to history store";
            request.setAttribute("adhocTaskMsg", message);
        }
        else if(requiredAction.equalsIgnoreCase("resetCurrentHivStatusToPositive"))
        {
            EnrollmentStatusManager esm=new EnrollmentStatusManager();
            //esm.updateLastDateOfCurrentEnrollmentStatus();
            String message=""+DataCleanupManager.resetCurrentHivStatusForPositiveBaselineStatus();
            request.setAttribute("adhocTaskMsg", message);
        }
        /*else if(requiredAction.equalsIgnoreCase("updateHivStatusHistory"))
        {
            HivStatusOperationsManager hsom=new HivStatusOperationsManager();
            String message=""+hsom.updateBeneficiaryHivStatusHistory()+" HIV status records updated to history store";
            request.setAttribute("adhocTaskMsg", message);
        }*/
        
        adoform.reset(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    public void setButtonState(HttpSession session,String disabled)
    {
        session.setAttribute("hhmergeDeleteBtnDisabled", disabled);
    }
}
