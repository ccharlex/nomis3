/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.CaregiverAccessToEmergencyFund;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author smomoh
 */
public interface CaregiverAccessToEmergencyFundDao 
{
    public void saveCaregiverAccessToEmergencyFund(CaregiverAccessToEmergencyFund ceaf) throws Exception;
    public void updateCaregiverAccessToEmergencyFund(CaregiverAccessToEmergencyFund ceaf) throws Exception;
    public void markForDelete(CaregiverAccessToEmergencyFund ceaf) throws Exception;
    public void deleteCaregiverAccessToEmergencyFund(CaregiverAccessToEmergencyFund ceaf) throws Exception;
    public CaregiverAccessToEmergencyFund getCaregiverAccessToEmergencyFund(int recordId) throws Exception;
    public CaregiverAccessToEmergencyFund getCaregiverAccessToEmergencyFund(String beneficiaryId,Date dateOfAssessment) throws Exception;
    public List getCaregiverAccessToEmergencyFund(String beneficiaryId) throws Exception;
    public List getCaregiverAccessToEmergencyFundRecordsForExport(ReportParameterTemplate rpt) throws Exception;
    public List getCaregiverAccessToEmergencyFundRecords(ReportParameterTemplate rpt) throws Exception;
}
