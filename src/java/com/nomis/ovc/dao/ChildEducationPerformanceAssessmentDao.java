/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.ChildEducationPerformanceAssessment;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author smomoh
 */
public interface ChildEducationPerformanceAssessmentDao 
{
    public void saveChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception;
    public void updateChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception;
    public void markForDelete(ChildEducationPerformanceAssessment cepa) throws Exception;
    public void deleteChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception;
    public ChildEducationPerformanceAssessment getChildEducationPerformanceAssessment(int recordId) throws Exception;
    public ChildEducationPerformanceAssessment getChildEducationPerformanceAssessment(String ovcId,Date dateOfAssessment) throws Exception;
    public List getChildEducationPerformanceAssessment(String ovcId) throws Exception;
    public List getChildEducationPerformanceAssessmentRecordsForExport(ReportParameterTemplate rpt) throws Exception;
}
