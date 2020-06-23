/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.maintenance.controller;

import com.nomis.operationsManagement.AccessManager;
import com.nomis.ovc.business.DatasetSetting;
import com.nomis.ovc.business.User;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.DatabasetManager;
import com.nomis.ovc.util.DateManager;
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
public class DatasetManagementAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    //String hhEnrolmentModuleId="hhEnrolment";
    //String hivRiskAsmtModuleId="hivRiskAsmt";
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
        DatasetManagementForm dstform=(DatasetManagementForm)form;
        HttpSession session=request.getSession();
        AppManager appManager=new AppManager();
        User user=appManager.getCurrentUser(session);
        if(AccessManager.isUserInDataEntryRole(user))
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
        
        
        String hhEnrolment=dstform.getHhEnrollmentFormId();
        String childEnrolmentFormId=dstform.getChildEnrollmentFormId();
        String hhServiceFormId=dstform.getHhServiceFormId();
        String childServiceFormId=dstform.getChildServiceFormId();
        String hivRiskAsmt=dstform.getHivRiskAssessmentFormId();
        
        String requiredAction=dstform.getActionName();
        
        if(requiredAction==null)
        {
            getDatasetManagementForm(dstform);
            return mapping.findForward(SUCCESS);
        }
        else if(requiredAction.equalsIgnoreCase("save"))
        {
            DaoUtility util=new DaoUtility();
            DatasetSetting hhEnrollmentDataset=new DatasetSetting();
            hhEnrollmentDataset.setModuleId(DatabasetManager.getHhEnrolmentModuleId());
            hhEnrollmentDataset.setDatasetId(hhEnrolment);
            hhEnrollmentDataset.setLastModifiedDate(DateManager.getCurrentDateInstance());
            hhEnrollmentDataset.setRecordedBy(userName);
            util.getDatasetSettingDaoInstance().saveDatasetSetting(hhEnrollmentDataset);
            
            DatasetSetting childEnrollmentDataset=new DatasetSetting();
            childEnrollmentDataset.setModuleId(DatabasetManager.getChildEnrollmentModuleId());
            childEnrollmentDataset.setDatasetId(childEnrolmentFormId);
            childEnrollmentDataset.setLastModifiedDate(DateManager.getCurrentDateInstance());
            childEnrollmentDataset.setRecordedBy(userName);
            util.getDatasetSettingDaoInstance().saveDatasetSetting(childEnrollmentDataset);
            
            DatasetSetting hhServiceDataset=new DatasetSetting();
            hhServiceDataset.setModuleId(DatabasetManager.getHouseholdServiceModuleId());
            hhServiceDataset.setDatasetId(hhServiceFormId);
            hhServiceDataset.setLastModifiedDate(DateManager.getCurrentDateInstance());
            hhServiceDataset.setRecordedBy(userName);
            util.getDatasetSettingDaoInstance().saveDatasetSetting(hhServiceDataset);
            
            DatasetSetting childServiceDataset=new DatasetSetting();
            childServiceDataset.setModuleId(DatabasetManager.getChildServiceModuleId());
            childServiceDataset.setDatasetId(childServiceFormId);
            childServiceDataset.setLastModifiedDate(DateManager.getCurrentDateInstance());
            childServiceDataset.setRecordedBy(userName);
            util.getDatasetSettingDaoInstance().saveDatasetSetting(childServiceDataset);
            
            DatasetSetting hivRiskAssessmentDataset=new DatasetSetting();
            hivRiskAssessmentDataset.setModuleId(DatabasetManager.getHivRiskAsmtModuleId());
            hivRiskAssessmentDataset.setDatasetId(hivRiskAsmt);
            hivRiskAssessmentDataset.setLastModifiedDate(DateManager.getCurrentDateInstance());
            hivRiskAssessmentDataset.setRecordedBy(userName);
            util.getDatasetSettingDaoInstance().saveDatasetSetting(hivRiskAssessmentDataset);
            
            getDatasetManagementForm(dstform);
            return mapping.findForward(SUCCESS);
        }
        
        return mapping.findForward(SUCCESS);
    }
    public void setButtonState(HttpSession session,String saveDisabled,String modifyDisabled)
    {
        session.setAttribute("dstsSaveDisabled", saveDisabled);
        session.setAttribute("dstsModifyDisabled", modifyDisabled);
    }
    private DatasetManagementForm getDatasetManagementForm(DatasetManagementForm dstform) throws Exception
    {
        DaoUtility util=new DaoUtility();
        List list=util.getDatasetSettingDaoInstance().getDatasetSettings();
        DatasetSetting dst=null;
        if(list !=null && !list.isEmpty())
        {
            for(Object obj:list)
            {
                dst=(DatasetSetting)obj;
                if(dst.getModuleId().equalsIgnoreCase(DatabasetManager.getHhEnrolmentModuleId()))
                dstform.setHhEnrollmentFormId(dst.getDatasetId());
                else if(dst.getModuleId().equalsIgnoreCase(DatabasetManager.getChildEnrollmentModuleId()))
                dstform.setChildEnrollmentFormId(dst.getDatasetId());
                else if(dst.getModuleId().equalsIgnoreCase(DatabasetManager.getHouseholdServiceModuleId()))
                dstform.setHhServiceFormId(dst.getDatasetId());
                else if(dst.getModuleId().equalsIgnoreCase(DatabasetManager.getChildServiceModuleId()))
                dstform.setChildServiceFormId(dst.getDatasetId());
                else if(dst.getModuleId().equalsIgnoreCase(DatabasetManager.getHivRiskAsmtModuleId()))
                dstform.setHivRiskAssessmentFormId(dst.getDatasetId());
            }
        }
        return dstform;
    }
}
