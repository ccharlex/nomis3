/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.HivRiskAssessment;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author smomoh
 */
public interface HivRiskAssessmentDao 
{
    public void saveHivRiskAssessment(HivRiskAssessment hra) throws Exception;
    public void updateHivRiskAssessment(HivRiskAssessment hra) throws Exception;
    public void deleteHivRiskAssessment(HivRiskAssessment hra) throws Exception;
    public void markedForDelete(HivRiskAssessment hra) throws Exception;
    public void markRiskAssessmentRecordsForDelete(String ovcId) throws Exception;
    public HivRiskAssessment getHivRiskAssessment(int recordId) throws Exception;
    public List getAllHivRiskAssessments() throws Exception;
    public HivRiskAssessment getHivRiskAssessment(String ovcId,Date dateOfAssessment) throws Exception;
    public List getHivRiskAssessmentRecordsForExport(ReportParameterTemplate rpt) throws Exception;
    public List getHivRiskAssessmentRecords(ReportParameterTemplate rpt) throws Exception;
    public List getRecordsWithZeroAgeAtAssessment() throws Exception;
    public List getRiskAssessmentRecordsByOvcId(String ovcId) throws Exception;
    public void deleteRiskAssessmentRecordsPerChild(String ovcId) throws Exception;
    public List getHivRiskAssessmentRecordsByLevel4OuId(String level4OuId) throws Exception;
    public void changeOvcIdInHivRiskAssessmentRecords(String oldOvcId, String newOvcId) throws Exception;
    public void processHivRiskAssessmentOutcome(HivRiskAssessment hra) throws Exception;
}
