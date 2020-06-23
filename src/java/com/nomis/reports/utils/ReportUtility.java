/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.utils;


import com.nomis.ovc.util.DateManager;

/**
 *
 * @author smomoh
 */
public class ReportUtility 
{
    private int[] finerAgeDisaggregation={0,0,1,4,5,9,10,14,15,17,18,24,25,200};
    private int[] zimAgeDisaggregation={0,0,1,4,5,9,10,14,15,17,18,19,20,24,25,200};
    public int[] getFinerAgeDisaggregation() {
        return finerAgeDisaggregation;
    }

    public void setFinerAgeDisaggregation(int[] finerAgeDisaggregation) {
        this.finerAgeDisaggregation = finerAgeDisaggregation;
    }

    public int[] getZimAgeDisaggregation() {
        return zimAgeDisaggregation;
    }

    public void setZimAgeDisaggregation(int[] zimAgeDisaggregation) {
        this.zimAgeDisaggregation = zimAgeDisaggregation;
    }
    
    public static String getDefaultStartDateForReports()
    {
        String startDate="10/01/2019";
        try
        {
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return startDate;
    }
    public static String getDefaultEndDateForReports()
    {
        String endDate=DateManager.getMthDayYearFromMySqlDate(DateManager.getCurrentDate());
        try
        {
            if(DateManager.compareDates(endDate, getDefaultStartDateForReports()))
            endDate=getDefaultStartDateForReports();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return endDate;
    }
    /*public static void setDefaultReportDates(HttpSession session)
    {
        try
        {
            String startDate="1900-01-01";
            String endDate=DateManager.getMthDayYearFromMySqlDate(DateManager.getCurrentDate());
            DaoUtility util=new DaoUtility();
            List dateOfAssessmentList=util.getHouseholdEnrollmentDaoInstance().getDistinctDateOfAssessmentAscending();
            if(dateOfAssessmentList !=null && !dateOfAssessmentList.isEmpty())
            {
                Date earliestDateOfAssessment=(Date)dateOfAssessmentList.get(0);
                if(earliestDateOfAssessment !=null)
                startDate=DateManager.getMthDayYearFromMySqlDate(DateManager.convertDateToString(earliestDateOfAssessment, DateManager.DB_DATE_FORMAT));
            }
            session.setAttribute("reportStartDate", startDate);
            session.setAttribute("reportEndDate", endDate);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }*/
}
