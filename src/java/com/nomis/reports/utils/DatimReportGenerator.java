/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.utils;


import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.operationsManagement.FinancialYearManager;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.SubQueryGenerator;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.DateManager;
import com.nomis.reports.controller.DatimReportForm;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class DatimReportGenerator 
{
    FinancialYearManager fym=new FinancialYearManager();
    public DatimReportTemplate getDatimReport(ReportParameterTemplate rpt)
    {
        String startDate=rpt.getStartDate();
        String endDate=rpt.getEndDate();
        String startOfFinancialYear=rpt.getFinancialYear().getStartYear()+"-"+rpt.getFinancialYear().getStartMonth()+"-01";
        DatimReportTemplate dform=new DatimReportTemplate();
        //Datim2017Form dform=new Datim2017Form();
        DatimReportTemplate activeRt=getNoOfActiveOvcServed(rpt,startDate, endDate,0,17,"All");
        DatimReportTemplate gradRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,0,17,"All");
        DatimReportTemplate childrenTransferedToPEPFARRt=getNoOfOvcServedAndTransfered(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate childrenTransferedToNonPEPFARRt=getNoOfOvcServedAndTransferedToNonPEPFAR(rpt,startOfFinancialYear, endDate,"All");
        
        DatimReportTemplate caregiversTransferedToPEPFARRt=getNoOfCaregiversServedAndTransferedToPEPFAR(rpt,startOfFinancialYear,endDate,"");
        DatimReportTemplate caregiversTransferedToNonPEPFARRt=getNoOfCaregiversServedAndTransferedToNonPEPFAR(rpt,startOfFinancialYear,endDate,"");
        
        int totalTransferedToPEPFAR=childrenTransferedToPEPFARRt.getOvc_servTransfered()+caregiversTransferedToPEPFARRt.getOvc_servTransfered();
        int totalTransferedToNonPEPFAR=childrenTransferedToNonPEPFARRt.getOvc_servTransfered()+caregiversTransferedToNonPEPFARRt.getOvc_servTransfered();
        int totalChildrenTransfered=childrenTransferedToPEPFARRt.getOvc_servTransfered()+childrenTransferedToNonPEPFARRt.getOvc_servTransfered();
        int totalCaregiversTransfered=caregiversTransferedToPEPFARRt.getOvc_servTransfered()+caregiversTransferedToNonPEPFARRt.getOvc_servTransfered();
        //ReportTemplate transferedRt=getNoOfOvcServedAndTransfered(rpt,startDate, endDate,"All");
        //ReportTemplate caregiversTransferedRt=getNoOfCaregiversServedAndTransfered(rpt,startDate, endDate,"All");
        
        DatimReportTemplate exitedWithoutGraduationRt=getNoOfOvcExitedWithoutGraduationServed(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate caregiversExitedWithoutGraduationRt=getNoOfCaregiversExitedWithoutGraduationServed(rpt,startOfFinancialYear, endDate,"All");
        
        DatimReportTemplate hivPosActiveRt=getNoOfActiveHivPositiveOvcServed(rpt,startDate, endDate,"All");
        DatimReportTemplate hivPosGraduatedRt=getNoOfGraduatedHivPositiveOvcServed(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate hivNegActiveRt=getNoOfActiveHivNegativeOvcServed(rpt,startDate, endDate,"All");
        DatimReportTemplate hivNegGraduatedRt=getNoOfGraduatedHivNegativeOvcServed(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate hivUnknownActiveRt=getNoOfActiveHivUnknownOvcServed(rpt,startDate, endDate,"All");
        DatimReportTemplate hivUnknownGraduatedRt=getNoOfGraduatedHivUnknownOvcServed(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate activeHivPositiveOnTreatmentRt=getNoOfActiveHivPositiveOvcOnTreatmentServed(rpt,startDate, endDate,"All");
        DatimReportTemplate graduatedHivPositiveOnTreatmentRt=getNoOfGraduatedHivPositiveOvcOnTreatmentServed(rpt,startOfFinancialYear, endDate,"All");
        DatimReportTemplate activeTestNotIndicatedRt=getNoOfActiveOvcWithTestNotIndicatedStatusServed(rpt,startDate, endDate,"All");
        DatimReportTemplate graduatedTestNotIndicatedRt=getNoOfGraduatedOvcWithTestNotIndicatedStatusServed(rpt,startOfFinancialYear, endDate,"All");
        
        DatimReportTemplate activeLessThan1Rt=getNoOfActiveOvcServed(rpt,startDate, endDate,0,0,"All");
        
        DatimReportTemplate activeLessThan1MaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,0,0,"M");
        DatimReportTemplate activeLessThan1FemaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,0,0,"F");
        DatimReportTemplate active1to4MaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,1,4,"M");
        DatimReportTemplate active1to4FemaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,1,4,"F");
        DatimReportTemplate active5to9MaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,5,9,"M");
        DatimReportTemplate active5to9FemaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,5,9,"F");
        DatimReportTemplate active1to9Rt=getNoOfActiveOvcServed(rpt,startDate, endDate,1,9,"All");
        DatimReportTemplate active10to14MaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,10,14,"M");
        DatimReportTemplate active10to14FemaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,10,14,"F");
        DatimReportTemplate active15to17MaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,15,17,"M");
        DatimReportTemplate active15to17FemaleRt=getNoOfActiveOvcServed(rpt,startDate, endDate,15,17,"F");
        DatimReportTemplate active18AndAboveMaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,18,150,"M");
        DatimReportTemplate active18AndAboveFemaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,18,150,"F");
        
        
        DatimReportTemplate gradLessThan1Rt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,0,0,"All");
        
        DatimReportTemplate gradLessThan1MaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,0,0,"M");
        DatimReportTemplate gradLessThan1FemaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,0,0,"F");
        DatimReportTemplate grad1to4MaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,1,4,"M");
        DatimReportTemplate grad1to4FemaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,1,4,"F");
        DatimReportTemplate grad5to9MaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,5,9,"M");
        DatimReportTemplate grad5to9FemaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,5,9,"F");
        DatimReportTemplate grad1to9Rt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,1,9,"All");
        DatimReportTemplate grad10to14MaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,10,14,"M");
        DatimReportTemplate grad10to14FemaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,10,14,"F");
        DatimReportTemplate grad15to17MaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,15,17,"M");
        DatimReportTemplate grad15to17FemaleRt=getNoOfGraduatedOvcServed(rpt,startOfFinancialYear, endDate,15,17,"F");
        DatimReportTemplate grad18AndAboveMaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,18,150,"M");
        DatimReportTemplate grad18AndAboveFemaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,18,150,"F");
        
        int totalActive18AndAbove=active18AndAboveMaleRt.getOvc_servActive()+active18AndAboveFemaleRt.getOvc_servActive();
        int totalGraduated18AndAbove=grad18AndAboveMaleRt.getOvc_servActive()+grad18AndAboveFemaleRt.getOvc_servActive();
        
        DatimReportTemplate active18to24MaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,18,24,"M");
        DatimReportTemplate grad18to24MaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,18,24,"M");
        DatimReportTemplate active18to24FemaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,18,24,"F");
        DatimReportTemplate grad18to24FemaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,18,24,"F");
        
        DatimReportTemplate active25AndAboveMaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,25,150,"M");
        DatimReportTemplate grad25AndAboveMaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,25,150,"M");
        DatimReportTemplate active25AndAboveFemaleRt=getNoOfActiveCaregiversServed(rpt,startDate, endDate,25,150,"F");
        DatimReportTemplate grad25AndAboveFemaleRt=getNoOfGraduatedCaregiversServed(rpt,startOfFinancialYear, endDate,25,150,"F");
        
              
        int totalPositive=hivPosActiveRt.getHiv_statNumerator()+hivPosGraduatedRt.getHiv_statNumerator();
        int totalNegative=hivNegActiveRt.getHiv_statNumerator()+hivNegGraduatedRt.getHiv_statNumerator();
        int totalUnknown=hivUnknownActiveRt.getHiv_statNumerator()+hivUnknownGraduatedRt.getHiv_statNumerator();
        
        int totalPositiveOnTreatment=activeHivPositiveOnTreatmentRt.getEnrolledOnART()+graduatedHivPositiveOnTreatmentRt.getEnrolledOnART();
        int totalPositiveNotOnTreatment=totalPositive-totalPositiveOnTreatment;
        int totalTestNotIndicated=activeTestNotIndicatedRt.getTestNotIndicated()+graduatedTestNotIndicatedRt.getTestNotIndicated();
        //int totalUnknownWithoutTestNotIndicated=totalUnknown-totalTestNotIndicated;
        int hivUnknownOtherReasons=totalUnknown-totalTestNotIndicated;
        
        int totalLessThan1=activeLessThan1Rt.getOvc_servActive()+gradLessThan1Rt.getOvc_servGraduated();
        int total1to9=active1to9Rt.getOvc_servActive()+grad1to9Rt.getOvc_servGraduated();
        int total10to14Male=active10to14MaleRt.getOvc_servActive()+grad10to14MaleRt.getOvc_servGraduated();
        int total10to14Female=active10to14FemaleRt.getOvc_servActive()+grad10to14FemaleRt.getOvc_servGraduated();
        int total15to17Male=active15to17MaleRt.getOvc_servActive()+grad15to17MaleRt.getOvc_servGraduated();
        int total15to17Female=active15to17FemaleRt.getOvc_servActive()+grad15to17FemaleRt.getOvc_servGraduated();
        
        int total18to24Male=active18to24MaleRt.getOvc_servActive()+grad18to24MaleRt.getOvc_servGraduated();
        int total18to24Female=active18to24FemaleRt.getOvc_servActive()+grad18to24FemaleRt.getOvc_servGraduated();
        int total25AndAboveMale=active25AndAboveMaleRt.getOvc_servActive()+grad25AndAboveMaleRt.getOvc_servGraduated();
        int total25AndAboveFemale=active25AndAboveFemaleRt.getOvc_servActive()+grad25AndAboveFemaleRt.getOvc_servGraduated();
        
        //calculate total active for caregivers
        int totalActiveCaregivers=active18to24MaleRt.getOvc_servActive()+active18to24FemaleRt.getOvc_servActive()+active25AndAboveMaleRt.getOvc_servActive()+active25AndAboveFemaleRt.getOvc_servActive();
        
        //calculate total caregivers graduated and served
        int totalGraduatedCaregivers=grad18to24MaleRt.getOvc_servGraduated()+grad18to24FemaleRt.getOvc_servGraduated()+grad25AndAboveMaleRt.getOvc_servGraduated()+grad25AndAboveFemaleRt.getOvc_servGraduated();
        
        //calculate total caregivers 18+
        int total18to24=total18to24Male+total18to24Female;
        int total25AndAbove=total25AndAboveMale+total25AndAboveFemale;
        //int caregiversTransfered=caregiversTransferedRt.getOvc_servTransfered();
        int caregiversExitedWithoutGraduation=caregiversExitedWithoutGraduationRt.getOvc_servExitedWithoutGraduation();
        dform.setLevel2Ou(rpt.getLevel2OuId());
        dform.setLevel3Ou(rpt.getLevel3OuId());
        dform.setLevel4Ou("All");
        dform.setDateCreated(DateManager.getCurrentDateInstance());
        dform.setCbo("All");
        dform.setPartnerCode("All");
        dform.setPartnerName("All");
        dform.setPeriod(startDate+"-"+endDate);
        dform.setLastModifiedDate(DateManager.getCurrentDateInstance());
        dform.setRecordedBy("Siaka");
        
        dform.setOvc_servActiveLessThan1Female(activeLessThan1FemaleRt.getOvc_servActive());
        dform.setOvc_servActiveLessThan1Male(activeLessThan1MaleRt.getOvc_servActive());
        dform.setOvc_servActive1to4Female(active1to4FemaleRt.getOvc_servActive());
        dform.setOvc_servActive1to4Male(active1to4MaleRt.getOvc_servActive());
        dform.setOvc_servActive5to9Female(active5to9FemaleRt.getOvc_servActive());
        dform.setOvc_servActive5to9Male(active5to9MaleRt.getOvc_servActive());
        dform.setOvc_servActive10to14Female(active10to14FemaleRt.getOvc_servActive());
        dform.setOvc_servActive10to14Male(active10to14MaleRt.getOvc_servActive());
        dform.setOvc_servActive15to17Female(active15to17FemaleRt.getOvc_servActive());
        dform.setOvc_servActive15to17Male(active15to17MaleRt.getOvc_servActive());
        dform.setOvc_servActive18AndAboveFemale(active18AndAboveFemaleRt.getOvc_servActive());
        dform.setOvc_servActive18AndAboveMale(active18AndAboveMaleRt.getOvc_servActive());
        
        dform.setOvc_servGraduatedLessThan1Female(gradLessThan1FemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduatedLessThan1Male(gradLessThan1MaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated1to4Female(grad1to4FemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated1to4Male(grad1to4MaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated5to9Female(grad5to9FemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated5to9Male(grad5to9MaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated10to14Female(grad10to14FemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated10to14Male(grad10to14MaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated15to17Female(grad15to17FemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated15to17Male(grad15to17MaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated18AndAboveFemale(grad18AndAboveFemaleRt.getOvc_servGraduated());
        dform.setOvc_servGraduated18AndAboveMale(grad18AndAboveMaleRt.getOvc_servGraduated());
                
        dform.setOvc_servActive(activeRt.getOvc_servActive()+totalActiveCaregivers);
        dform.setOvc_servGraduated(gradRt.getOvc_servGraduated()+totalGraduatedCaregivers);
        dform.setOvc_servNumerator(activeRt.getOvc_servActive()+gradRt.getOvc_servGraduated()+totalActive18AndAbove+totalGraduated18AndAbove);
        //dform.setOvc_servNumerator(activeRt.getOvc_servActive()+gradRt.getOvc_servGraduated()+total18to24+total25AndAbove);
        dform.setOvc_servTransfered(totalChildrenTransfered+totalCaregiversTransfered);
        dform.setOvc_servExitedWithoutGraduation(exitedWithoutGraduationRt.getOvc_servExitedWithoutGraduation()+caregiversExitedWithoutGraduation);
        dform.setTotalPositive(totalPositive);
        dform.setTotalNegative(totalNegative);
        dform.setTotalUnknown(totalUnknown);
        //dform.setTotalUnknown(totalUnknownWithoutTestNotIndicated);
        dform.setHiv_statNumerator(totalPositive+totalNegative+totalUnknown+totalTestNotIndicated);
        dform.setEnrolledOnART(totalPositiveOnTreatment);
        dform.setNotEnrolledOnART(totalPositiveNotOnTreatment);
        dform.setTestNotIndicated(totalTestNotIndicated);
        dform.setOtherReasons(hivUnknownOtherReasons);
        
        /*dform.setOvc_servLessThan1(totalLessThan1);
        dform.setOvc_serv1To9(total1to9);
        
        //dform.setOvc_servActive5to9Male(total15to17Male);
        //dform.setOvc_serv5to9(total15to17Male);
        dform.setOvc_servMale10To14(total10to14Male);
        dform.setOvc_servFemale10To14(total10to14Female);
        dform.setOvc_servMale15To17(total15to17Male);
        dform.setOvc_servFemale15To17(total15to17Female);
        
        dform.setOvc_servFemale18To24(total18to24Female);
        dform.setOvc_servMale18To24(total18to24Male);
        dform.setOvc_servMale25AndAbove(total25AndAboveMale);
        dform.setOvc_servFemale25AndAbove(total25AndAboveFemale);*/
        dform.setTransferedToPEPFAR(totalTransferedToPEPFAR);
        dform.setTransferedToNonPEPFAR(totalTransferedToNonPEPFAR);
        
        
        return dform;
    }
    
    
    public DatimReportTemplate getNoOfActiveCaregiversServed(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfActiveCaregiversServedByEnrollmentStatus(rpt,startOfLastQuarter,endDate,startAge,endAge,sex);
        //int count=getNoOfCaregiversServedByEnrollmentStatus(rpt,AppConstant.ACTIVE_NUM,startOfLastQuarter,endDate,startAge,endAge,sex);
        rt.setOvc_servActive(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedCaregiversServed(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int count=getNoOfCaregiversServedByEnrollmentStatus(rpt,AppConstant.GRADUATED_NUM,startDate,endDate,startAge,endAge,sex);
        rt.setOvc_servGraduated(count);
        return rt;
    }
    public DatimReportTemplate getNoOfCaregiversServedAndTransfered(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        DatimReportTemplate pepfarRt=getNoOfCaregiversServedAndTransferedToPEPFAR(rpt,startDate,endDate,sex);
        DatimReportTemplate nonPepfarRt=getNoOfCaregiversServedAndTransferedToNonPEPFAR(rpt,startDate,endDate,sex);
        rt.setOvc_servTransfered(pepfarRt.getOvc_servTransfered()+nonPepfarRt.getOvc_servTransfered());
        return rt;
    }
    public DatimReportTemplate getNoOfCaregiversServedAndTransferedToPEPFAR(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int pepfarcount=getNoOfCaregiversServedByEnrollmentStatus(rpt,AppConstant.TRANSFERED_PEPFAR_NUM,startDate,endDate,18,200,sex);
        rt.setOvc_servTransfered(pepfarcount);
        return rt;
    }
    public DatimReportTemplate getNoOfCaregiversServedAndTransferedToNonPEPFAR(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int nonPepfarcount=getNoOfCaregiversServedByEnrollmentStatus(rpt,AppConstant.TRANSFERED_NONPEPFAR_NUM,startDate,endDate,18,200,sex);
        rt.setOvc_servTransfered(nonPepfarcount);
        return rt;
    }
    public DatimReportTemplate getNoOfCaregiversExitedWithoutGraduationServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        try
        {
            //System.err.println("endDate in DatimReportGenerator.getNoOfCaregiversExitedWithoutGraduationServed is "+endDate);
            String nextMonth=DateManager.getNextMonth(endDate, 0);
            //System.err.println("DateManager.getNextMonth(2017-01-31, 0) is "+DateManager.getNextMonth("2017-01-31", 0));
            //System.err.println("endDate is DatimReportGenerator.getNoOfCaregiversExitedWithoutGraduationServed is "+endDate);
            DaoUtility util=new DaoUtility();
            int count=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersExitedWithoutGraduation(rpt, startDate, endDate, sex);
                    //getNoOfCaregiversServedByEnrollmentStatus(rpt,AppConstant.EXITED_WITHOUT_GRADUATION_NUM,startDate,endDate,0,17,sex);
            rt.setOvc_servExitedWithoutGraduation(count);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public DatimReportTemplate getNoOfActiveOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        //An active beneficiary must have been served in the last quarter, hence the start date should be the start of the last quarter
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatus(rpt,AppConstant.ACTIVE_NUM,startOfLastQuarter,endDate,startAge,endAge,sex);
        rt.setOvc_servActive(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int count=getNoOfOvcServedByEnrollmentStatus(rpt,AppConstant.GRADUATED_NUM,startDate,endDate,startAge,endAge,sex);
        rt.setOvc_servGraduated(count);
        return rt;
    }
    public DatimReportTemplate getNoOfOvcServedAndTransfered(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        DatimReportTemplate pepfarRt=getNoOfOvcServedAndTransferedToPEPFAR(rpt,startDate,endDate,sex);
        DatimReportTemplate nonPepfarRt=getNoOfOvcServedAndTransferedToPEPFAR(rpt,startDate,endDate,sex);
        rt.setOvc_servTransfered(pepfarRt.getOvc_servTransfered()+nonPepfarRt.getOvc_servTransfered());
        return rt;
    }
    public DatimReportTemplate getNoOfOvcServedAndTransferedToPEPFAR(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int pepfarcount=getNoOfOvcServedByEnrollmentStatus(rpt,AppConstant.TRANSFERED_PEPFAR_NUM,startDate,endDate,0,17,sex);
        rt.setOvc_servTransfered(pepfarcount);
        return rt;
    }
    public DatimReportTemplate getNoOfOvcServedAndTransferedToNonPEPFAR(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();;
        int nonPepfarcount=getNoOfOvcServedByEnrollmentStatus(rpt,AppConstant.TRANSFERED_NONPEPFAR_NUM,startDate,endDate,0,17,sex);
        rt.setOvc_servTransfered(nonPepfarcount);
        return rt;
    }
    public DatimReportTemplate getNoOfOvcExitedWithoutGraduationServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        try
        {
            String nextMonth=DateManager.getNextMonth(endDate, 0);
            DaoUtility util=new DaoUtility();
            int count=util.getChildEnrollmentDaoInstance().getNumberOfOvcExitedWithoutGraduation(rpt, startDate, endDate, sex);
            //int count=util.getChildEnrollmentDaoInstance().getNumberOfOvcExitedWithoutGraduation(rpt, startDate, nextMonth, sex);
            //int count=getNoOfOvcServedByEnrollmentStatus(rpt,AppConstant.EXITED_WITHOUT_GRADUATION_NUM,startDate,endDate,0,17,sex);
            rt.setOvc_servExitedWithoutGraduation(count);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public DatimReportTemplate getNoOfActiveHivPositiveOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.ACTIVE_NUM,AppConstant.HIV_POSITIVE_NUM,startOfLastQuarter,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedHivPositiveOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.GRADUATED_NUM,AppConstant.HIV_POSITIVE_NUM,startDate,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfActiveHivNegativeOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.ACTIVE_NUM,AppConstant.HIV_NEGATIVE_NUM,startOfLastQuarter,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedHivNegativeOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.GRADUATED_NUM,AppConstant.HIV_NEGATIVE_NUM,startDate,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfActiveHivUnknownOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.ACTIVE_NUM,AppConstant.HIV_UNKNOWN_NUM,startOfLastQuarter,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedHivUnknownOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,AppConstant.GRADUATED_NUM,AppConstant.HIV_UNKNOWN_NUM,startDate,endDate,0,17,sex);
        rt.setHiv_statNumerator(count);
        return rt;
    }
    public DatimReportTemplate getNoOfActiveHivPositiveOvcOnTreatmentServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfHivPositiveOvcOnTreatmentServedByEnrollmentStatus(rpt,AppConstant.ACTIVE_NUM,startOfLastQuarter,endDate,0,17,sex);
        rt.setEnrolledOnART(count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedHivPositiveOvcOnTreatmentServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfHivPositiveOvcOnTreatmentServedByEnrollmentStatus(rpt,AppConstant.GRADUATED_NUM,startDate,endDate,0,17,sex);
        rt.setEnrolledOnART(count);
        return rt;
    }
    public DatimReportTemplate getNoOfActiveOvcWithTestNotIndicatedStatusServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNumberOfOvcNotAtRiskAndServed(rpt,AppConstant.ACTIVE_NUM,startOfLastQuarter,endDate,0,17,sex);
        rt.setTestNotIndicated(count);
        System.err.println("count in getNumberOfOvcNotAtRiskAndServed is "+count);
        return rt;
    }
    public DatimReportTemplate getNoOfGraduatedOvcWithTestNotIndicatedStatusServed(ReportParameterTemplate rpt,String startDate,String endDate,String sex)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNumberOfOvcNotAtRiskAndServed(rpt,AppConstant.GRADUATED_NUM,startDate,endDate,0,17,sex);
        rt.setTestNotIndicated(count);
        System.err.println("count in getNumberOfOvcNotAtRiskAndServed is "+count);
        return rt;
    }
    /*public DatimReportTemplate getNoOfActiveOvcLessThan1YearServed(ReportParameterTemplate rpt,String startDate,String endDate)
    {
        DatimReportTemplate rt=new DatimReportTemplate();
        int count=getNoOfOvcServedByEnrollmentStatus(rpt,1,startDate,endDate,0,0);
        rt.setOvc_servActive(count);
        return rt;
    }*/
        
    public int getNoOfActiveCaregiversServedByEnrollmentStatus(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfCaregivers=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfCaregivers=util.getHouseholdServiceDaoInstance().getNumberOfActiveAdultMembersServedForDatim(orgUnitQuery, startDate, endDate, startAge, endAge, sex);//orgUnitQuery, enrollmentStatus, startDate, endDate,startAge,endAge,sex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfCaregivers;
    }
    public int getNoOfCaregiversServedByEnrollmentStatus(ReportParameterTemplate rpt,int enrollmentStatus,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfCaregivers=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfCaregivers=util.getHouseholdServiceDaoInstance().getNumberOfAdultMembersServedByEnrollmentStatusForDatim(orgUnitQuery, enrollmentStatus, startDate, endDate, startAge, endAge, sex);
            //countOfCaregivers=util.getHouseholdServiceDaoInstance().getNumberOfBeneficiariesServedByEnrollmentStatus(orgUnitQuery, enrollmentStatus, startDate, endDate, startAge, endAge, sex);//orgUnitQuery, enrollmentStatus, startDate, endDate,startAge,endAge,sex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfCaregivers;
    }
    public int getNoOfOvcServedByEnrollmentStatus(ReportParameterTemplate rpt,int enrollmentStatus,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfOvc=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            //endDate=DateManager.getCurrentDate();
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            //countOfOvc=util.getChildEnrollmentDaoInstance().getNumberOfOvcExitedWithoutGraduation(rpt, startDate, endDate, sex);
            //getNumberOfOvcServedByEnrollmentStatusForDatim
            if(enrollmentStatus==AppConstant.ACTIVE_NUM)
            countOfOvc=util.getChildServiceDaoInstance().getNumberOfActiveOvcServedForDatim(orgUnitQuery, startDate, endDate,startAge,endAge,sex);
            else
            countOfOvc=util.getChildServiceDaoInstance().getNumberOfOvcServedByEnrollmentStatusForDatim(orgUnitQuery, enrollmentStatus, startDate, endDate,startAge,endAge,sex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfOvc;
    }
    public int getNoOfOvcServedByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,int enrollmentStatus,int hivStatus,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfOvc=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfOvc=util.getChildServiceDaoInstance().getNumberOfOvcServedByEnrollmentStatusAndHivStatusForDatim(orgUnitQuery, enrollmentStatus,hivStatus, startDate, endDate,startAge,endAge,sex,AppConstant.NO_TREATMENT_CRITERIA);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfOvc;
    }//
    public int getNumberOfOvcNotAtRiskAndServed(ReportParameterTemplate rpt,int enrollmentStatus,String startDate,String endDate,int startAge,int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfOvc=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfOvc=getNoOfOvcServedByEnrollmentStatusAndHivStatus(rpt,enrollmentStatus,AppConstant.HIV_TEST_NOT_INDICATED_NUM,startDate,endDate,0,17,sex);
            //countOfOvc=util.getChildServiceDaoInstance().getNumberOfHivUnknownOvcNotAtRiskServed(orgUnitQuery, enrollmentStatus,startDate, endDate,startAge,endAge,sex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfOvc;
    }
    public int getNoOfOvcServedByEnrollmentStatusAndHivRiskAssessmentStatus(ReportParameterTemplate rpt,int enrollmentStatus,int hivStatus,String startDate,String endDate,int startAge,int endAge,String sex,int hivRiskStatus)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfOvc=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfOvc=util.getChildServiceDaoInstance().getNumberOfOvcServedAndRiskAssessedByEnrollmentStatusAndHivStatus(orgUnitQuery, enrollmentStatus,hivStatus, startDate, endDate,startAge,endAge,sex,hivRiskStatus);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfOvc;
    }
    public int getNoOfHivPositiveOvcOnTreatmentServedByEnrollmentStatus(ReportParameterTemplate rpt,int enrollmentStatus,String startDate,String endDate,int startAge, int endAge,String sex)
    {
        SubQueryGenerator sqg=new SubQueryGenerator();
        int countOfOvc=0;
        DaoUtility util=new DaoUtility();           
        try
        {
            String orgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            countOfOvc=util.getChildServiceDaoInstance().getNumberOfOvcServedByEnrollmentStatusAndHivStatusForDatim(orgUnitQuery, enrollmentStatus,1, startDate, endDate,startAge,endAge,sex,AppConstant.ENROLLED_ON_TREATMENT_YES_NUM);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return countOfOvc;
    }
    public List getDatimFormList(ReportParameterTemplate rpt)
    {
        DaoUtility util=new DaoUtility();
        List datimFormList=new ArrayList();
        try
        {
            if(rpt !=null)
            {
                List level3OuList=util.getHouseholdEnrollmentDaoInstance().getDistinctLevel3OuList(rpt);
                if(level3OuList !=null)
                {
                    OrganizationUnit level3Ou=null;
                    String level3OuId=null;
                    for(int i=0; i<level3OuList.size(); i++)
                    {
                        level3Ou=(OrganizationUnit)level3OuList.get(i);
                        level3OuId=level3Ou.getUid();
                        rpt.setLevel3OuId(level3OuId);
                        datimFormList.add(getDatimReport(rpt));
                        //if(i==2)
                        //break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return datimFormList;
    }
    public List convertDatimReportTemplateToReportTemplates(DatimReportTemplate drt)
    {
        List reportTemplateList=new ArrayList();
        try
        {
            if(drt !=null)
            {
                DaoUtility util=new DaoUtility();
                OrganizationUnit level2Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(drt.getLevel2Ou());
                OrganizationUnit level3Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(drt.getLevel3Ou());
                String level2OuName=null;
                String level3OuName=null;
                if(level2Ou !=null)
                level2OuName=level2Ou.getName();
                if(level3Ou !=null)
                level3OuName=level3Ou.getName();
                String ageDisaggregation="All";
                String allDisaggregation="All";
                String bothSexes="All";
                ReportTemplate rt=new ReportTemplate();
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"HIV Stat Numerator",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getHiv_statNumerator()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"Reported HIV positive to IP",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getTotalPositive()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"Of those positive: Currently receiving ART",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getEnrolledOnART()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"Of those positive: Not currently receiving ART",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getNotEnrolledOnART()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"Reported HIV negative to IP",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getTotalNegative()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"Test not required based on risk assessment",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getTestNotIndicated()));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"No HIV status reported to IP (HIV status unknown)",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getTotalUnknown()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV_Numerator",bothSexes,ageDisaggregation,allDisaggregation,drt.getPeriod(),drt.getOvc_servNumerator()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","Unknown age","Active",drt.getPeriod(),0));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","<1","Active",drt.getPeriod(),drt.getOvc_servActiveLessThan1Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","1-4","Active",drt.getPeriod(),drt.getOvc_servActive1to4Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","5-9","Active",drt.getPeriod(),drt.getOvc_servActive5to9Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","10-14","Active",drt.getPeriod(),drt.getOvc_servActive10to14Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","15-17","Active",drt.getPeriod(),drt.getOvc_servActive15to17Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","18+","Active",drt.getPeriod(),drt.getOvc_servActive18AndAboveMale()));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","<1","Active",drt.getPeriod(),drt.getOvc_servActiveLessThan1Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","1-4","Active",drt.getPeriod(),drt.getOvc_servActive1to4Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","5-9","Active",drt.getPeriod(),drt.getOvc_servActive5to9Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","10-14","Active",drt.getPeriod(),drt.getOvc_servActive10to14Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","15-17","Active",drt.getPeriod(),drt.getOvc_servActive15to17Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","18+","Active",drt.getPeriod(),drt.getOvc_servActive18AndAboveFemale()));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","<1","Graduated",drt.getPeriod(),drt.getOvc_servGraduatedLessThan1Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","1-4","Graduated",drt.getPeriod(),drt.getOvc_servGraduated1to4Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","5-9","Graduated",drt.getPeriod(),drt.getOvc_servGraduated5to9Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","10-14","Graduated",drt.getPeriod(),drt.getOvc_servGraduated10to14Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","15-17","Graduated",drt.getPeriod(),drt.getOvc_servGraduated15to17Male()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Male","18+","Graduated",drt.getPeriod(),drt.getOvc_servGraduated18AndAboveMale()));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","<1","Graduated",drt.getPeriod(),drt.getOvc_servGraduatedLessThan1Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","1-4","Graduated",drt.getPeriod(),drt.getOvc_servGraduated1to4Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","5-9","Graduated",drt.getPeriod(),drt.getOvc_servGraduated5to9Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","10-14","Graduated",drt.getPeriod(),drt.getOvc_servGraduated10to14Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","15-17","Graduated",drt.getPeriod(),drt.getOvc_servGraduated15to17Female()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV","Female","18+","Graduated",drt.getPeriod(),drt.getOvc_servGraduated18AndAboveFemale()));
                
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV",bothSexes,ageDisaggregation,"Transfered to PEPFAR Supported program",drt.getPeriod(),drt.getTransferedToPEPFAR()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV",bothSexes,ageDisaggregation,"Transfered to Non-PEPFAR Supported program",drt.getPeriod(),drt.getTransferedToNonPEPFAR()));
                reportTemplateList.add(getReportTemplate(level2OuName,level3OuName,"OVC_SERV",bothSexes,ageDisaggregation,"Exited without graduation",drt.getPeriod(),drt.getOvc_servExitedWithoutGraduation()));
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex); 
        }
        return reportTemplateList;
    }
    private ReportTemplate getReportTemplate(String level2OuName,String level3OuName,String indicatorName,String sex,String ageDisaggregation,String otherDisaggregation,String period,int value)
    {
        ReportTemplate rt=new ReportTemplate();
        rt.setLevel2OuName(level2OuName);
        rt.setLevel3OuName(level3OuName);
        rt.setIndicatorName(indicatorName);
        rt.setValue(value);
        rt.setPeriod(period);
        rt.setSex(sex);
        rt.setAgeDisaggregation(ageDisaggregation);
        rt.setOtherDisaggregation(otherDisaggregation);
        return rt;
    }
}
