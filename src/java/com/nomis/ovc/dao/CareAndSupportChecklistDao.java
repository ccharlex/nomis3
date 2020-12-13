/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.CareAndSupportChecklist;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author smomoh
 */
public interface CareAndSupportChecklistDao 
{
    public void saveCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception;
    public void updateCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception;
    public void markForDelete(CareAndSupportChecklist casc) throws Exception;
    public void deleteCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception;
    public CareAndSupportChecklist getCareAndSupportChecklist(int recordId) throws Exception;
    public CareAndSupportChecklist getCareAndSupportChecklist(String beneficiaryId,Date dateOfAssessment) throws Exception;
    public List getCareAndSupportChecklist(String beneficiaryId) throws Exception;
    public List getCareAndSupportRecordsForExport(ReportParameterTemplate rpt) throws Exception;
    public List getOvcCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception;
    public List getAdultCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception;
    public List getMostRecentCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception;
}
