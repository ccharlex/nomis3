/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.utils;


import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.operationsManagement.OrganizationUnitAttributesManager;
import com.nomis.ovc.business.CommunityBasedOrganization;
import com.nomis.ovc.business.CustomIndicatorsReport;
import com.nomis.ovc.dao.CustomIndicatorsReportDao;
import com.nomis.ovc.dao.CustomIndicatorsReportDaoImpl;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.DateManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class CustomIndicatorsReportManager 
{
    List parameterList=null;
    ReportPeriod fy=null;
    String mainReportPeriod="";
    IndicatorDictionary ind=new IndicatorDictionary();
    int[] ovcAgeDisaggregation={0,0,1,4,5,9,10,14,15,17};
    int[] ovcAndCaregiverAgeDisaggregation={0,0,1,4,5,9,10,14,15,17,18,24,25,200};
    String beneficiariesNewlyEnrolled=ind.getIndicatorForNumberOfNewOvcEnrolled().getIndicatorId();
    String ovcActiveAndServeId=ind.getIndicatorForNumberOfOvcCurrentlyEnrolledAndServedInReportPeriod().getIndicatorId();
    String ovcGraduatedAndServeId=ind.getIndicatorForNumberOfOvcGraduatedButServedInReportPeriod().getIndicatorId();
    String ovcServeAndTransferedId=ind.getIndicatorForNumberOfOvcTransferedOutButServedInReportPeriod().getIndicatorId();
    String ovcExitedWithoutGraduationId=ind.getIndicatorForNumberOfOvcExitedWithoutGraduation().getIndicatorId();
    
    String cgActiveAndServeId=ind.getIndicatorForNumberOfActiveCaregiversServedWithinTheReportPeriod().getIndicatorId();
    String cgServedAndTransferedId=ind.getIndicatorForNumberOfCaregiversServedAndTransferedWithinTheReportPeriod().getIndicatorId();
    String cgServedAndGraduatedId=ind.getIndicatorForNumberOfCaregiversServedAndGraduatedWithinTheReportPeriod().getIndicatorId();
    String cgExitedWithoutGraduationId=ind.getIndicatorForNumberOfCaregiversExitedWithoutGraduation().getIndicatorId();
    String userName="auto";
    public void processCustomIndicatorsByLevel2Ou(ReportParameterTemplate rpt,List selectedIndicators,String currentUser)
    {
        DateManager dm=new DateManager();
        
        if(selectedIndicators==null || selectedIndicators.isEmpty())
        selectedIndicators=CustomIndicatorsReportManager.getCustomIndicators();
        fy=ReportPeriodManager.getStartOfFinancialYear(rpt.getStartMth(), rpt.getStartYear());
        rpt.setStartAge(1);
        rpt.setEndAge(17);
        //Create a report template for exited beneficiaries. Their start period is from the begining of the FY
        ReportParameterTemplate rptForExited=rpt;
        rptForExited.setStartMth(fy.getStartMonth());
        rptForExited.setStartYear(fy.getStartYear());
        //parameterList=paramList;
        if(currentUser !=null)
        userName=currentUser;
        
        String partnerCode=rpt.getPartnerCode();
        //String[] params=getQueryParam(paramList);
        //String[] queryParam={stateCode,lgaCode,cboCode,wardCode,startMonth,startYear,endMonth,endYear,null,null,null,null,null,null,partnerName,partnerCode,null,null,partnerCode};
        String[] reportParams={rpt.getLevel2OuId(),rpt.getLevel3OuId(),rpt.getCboId(),rpt.getLevel4OuId(),"All","All","All",rpt.getStartMth()+"",rpt.getStartYear()+"",rpt.getEndMth()+"",rpt.getEndYear()+"","All","All","All","All","0","17","All",rpt.getPartnerCode(),rpt.getLevel4OuId()};
        String[] reportParamsForExited={rpt.getLevel2OuId(),rpt.getLevel3OuId(),rpt.getCboId(),rpt.getLevel4OuId(),"All","All","All",fy.getStartMonth()+"",fy.getStartYear()+"",rpt.getEndMth()+"",rpt.getEndYear()+"","All","All","All","All","0","17","All",rpt.getPartnerCode(),rpt.getLevel4OuId()};
        mainReportPeriod=dm.getMonthAsString(rpt.getStartMth())+" "+rpt.getStartYear()+"-"+dm.getMonthAsString(rpt.getEndMth())+" "+rpt.getEndYear();
        
        IndicatorDictionary ind=new IndicatorDictionary();
        //Indicators ind=null;
        String indicatorId=null;
        for(int i=0; i<selectedIndicators.size(); i++)
        {
            indicatorId=selectedIndicators.get(i).toString();
            
            if(indicatorId.equalsIgnoreCase(ind.getOvc_ENROLLEDIndicator().getIndicatorId()))
            {//OVC_ENROLLED
                processsOVCNewEnrolledAndServed(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_SERVIndicator().getIndicatorId()))
            {//OVC_SERV
                processsOVCCurrentlyEnrolledAndServed(rpt,partnerCode);      
                processsOVCGraduatedAndServed(rptForExited,partnerCode);
                processsOVCTransferedAndServed(rptForExited,partnerCode);
                processsOVCExitedWithoutGraduationAndServed(rptForExited,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_HIVSTATIndicator().getIndicatorId()))
            {//OVC_HIVSTAT
                processsOVC_HIVSTATPositive(rpt,partnerCode);
                processsOVC_HIVSTATNegative(rpt,partnerCode);
                processsOVC_HIVSTATUnknown(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_TXLINKIndicator().getIndicatorId()))
            {//OVC_TXLINK
                processsOVCIdentifiedPositiveAndServed(rpt,partnerCode);
                processsOVCNewlyTestedPositiveEnrolledOnTreatmentAndServed(rpt,partnerCode);
                //processsEnrolledOnTreatment(reportParams,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_ARTSUPPIndicator().getIndicatorId()))
            {//OVC_ARTSUPP
                processsOVCIdentifiedPositiveAndServed(rpt,partnerCode);
                processsOVCAdherenceToTreatment(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_HIVRISKASSIndicator().getIndicatorId()))
            {//OVC_HIVRISKASS
                processsOVCWithUnknownHivAssessedForHIVRisk(rpt,partnerCode);
                processsOVCWithNegativeHivAssessedForHIVRisk(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_BIRTHCERTIndicator().getIndicatorId()))
            {//OVC_BIRTHCERT
                processsOVCWithBirthCertificateServed(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_PROTECTIndicator().getIndicatorId()))
            {//OVC_PROTECT
                processsOVCAbusedAndExploited(rpt,partnerCode);
                processsOVCPostViolenceLinkedToGovt(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_HTSLINKIndicator().getIndicatorId()))
            {//OVC_HTSLINK
                //processsHivUnknownOrNegativeOVCServedInReportPeriod(rpt,partnerCode);
                //processsOVCReferredForTestingOnly(reportParams,partnerCode);
                processsOVCReferredForTestingAndTestedAndObtainedResult(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_NUTRITIONIndicator().getIndicatorId()))
            {//OVC_NUTRITION
                processsOVCSeverelyMalnourished(rpt,partnerCode);
                processsOVCSeverelyMalnourishedAndServedNutrition(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_EDUIndicator().getIndicatorId()))
            {//OVC_EDU
                processsOVCCurrentlyInSchool(rpt,partnerCode);
                processsOVCRegularlyAttendingSchool(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_HIVPREVIndicator().getIndicatorId()))
            {//OVC_HHGRAD
                processsNumberOfAdolescentsProvidedHIVPreventionServices(rpt,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_HHGRADIndicator().getIndicatorId()))
            {//OVC_HHGRAD
                processsNumberOfActiveHouseholdsWhoseOvcWereServed(rpt,partnerCode);
                processsNumberOfGraduatedHouseholdsWhoseOvcWereServed(rptForExited,partnerCode);
            }
            else if(indicatorId.equalsIgnoreCase(ind.getOvc_ECONSIndicator().getIndicatorId()))
            {//OVC_ECONS
                processsNumberOfHouseholdsThatReceivedEmergencyNeeds(rpt,partnerCode);
            }
        }
        /*processsCaregiversCurrentlyEnrolledAndServed(reportParams,partnerCode);
        processsCaregiversGraduatedAndServed(reportParams,partnerCode);
        processsCaregiversTransferedAndServed(reportParams,partnerCode);
        processsCaregiversExitedWithoutGraduationAndServed(reportParams,partnerCode);*/
    }
    public void processsOVCCurrentlyEnrolledAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcCurrentlyEnrolledAndServedInReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCGraduatedAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcGraduatedButServedInReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCTransferedAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcTransferedOutButServedInReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCExitedWithoutGraduationAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcExitedWithoutGraduation().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCNewEnrolledAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfNewOvcEnrolled().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    
    public void processsOVC_HIVSTATPositive(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOVC_HIVSTATPOSITIVE().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVC_HIVSTATNegative(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOVC_HIVSTATNEGATIVE().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVC_HIVSTATUnknown(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOVC_HIVSTATUNKNOWN().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsEnrolledOnTreatment(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfHIVPositiveOvcCurrentlyEnrolledOnART().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCIdentifiedPositiveAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcNewlyTestedPositiveWithinTheReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    
    public void processsOVCNewlyTestedPositiveEnrolledOnTreatmentAndServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfHivPositiveOvcEnrolledOnARTWithinTheReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCAdherenceToTreatment(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcSelfReportingAdherenceToTreatment().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCWithUnknownHivAssessedForHIVRisk(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getNoOfHivUnknownOvcAssessedForHIVRiskAndServedWithinReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCWithNegativeHivAssessedForHIVRisk(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getNoOfHivNegativeOvcAssessedForHIVRiskAndServedWithinReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCWithBirthCertificateServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getOvc_BIRTHCERTIndicator().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    //
    public void processsOVCAbusedAndExploited(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcAbusedWithinReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCPostViolenceLinkedToGovt(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcLinkedToGovtForPostViolenceServicesWithinReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    /*public void processsHivUnknownOrNegativeOVCServedInReportPeriod(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfHivUnknownOrNegativeOvcServedWithinTheReportPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }*/
    /*public void processsOVCReferredForTestingOnly(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcTestedForHIV().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }*/
    public void processsOVCReferredForTestingAndTestedAndObtainedResult(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcProvidedReferralForHIVRelatedTestingService().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
        String cgindicatorCode=ind.getIndicatorForNumberOfAdultMembersProvidedReferralForHIVRelatedTestingService().getIndicatorId();
        processDataByCBO(rpt,cgindicatorCode,partnerCode);
    }
    
    public void processsOVCSeverelyMalnourished(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfSeverelyMalnourishedOvcCurrently().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCSeverelyMalnourishedAndServedNutrition(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfSeverelyMalnourishedOvcServedNutritonalServices().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCRegularlyAttendingSchool(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcRegularlyAttendingSchool().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsOVCCurrentlyInSchool(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfOvcCurrentlyInSchool().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsNumberOfAdolescentsProvidedHIVPreventionServices(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorForNumberOfAdolescentsProvidedHIVPreventionServices().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    
    //Household indicators starts here
    public void processsNumberOfActiveHouseholdsWhoseOvcWereServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorNumberOfHouseholdsWhoseOvcWereServedWithinReportingPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsNumberOfGraduatedHouseholdsWhoseOvcWereServed(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorNumberOfGraduatedHouseholdsWhoseOvcWereServedWithinReportingPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    public void processsNumberOfHouseholdsThatReceivedEmergencyNeeds(ReportParameterTemplate rpt,String partnerCode)
    {
        String indicatorCode=ind.getIndicatorNumberOfHouseholdsThatCanSolveEmergencyNeedsWithinReportingPeriod().getIndicatorId();
        processDataByCBO(rpt,indicatorCode,partnerCode);
    }
    
    
    private void processDataByCBO(ReportParameterTemplate rpt,String indicatorCode,String partnerCode)
    {
        
        OrganizationUnitAttributesManager ouam=new OrganizationUnitAttributesManager();
        ListOfIndicatorsReportGenerator lirg=new ListOfIndicatorsReportGenerator();
        List maleList=new ArrayList();
        List femaleList=new ArrayList();
        ReportTemplate rt=null;
        //Get the Organization unit names for use in report
        String level2OuName=ouam.getOrganizationUnitName(rpt.getLevel2OuId());
        String level3OuName=ouam.getOrganizationUnitName(rpt.getLevel3OuId());
        String cboName=" ";
        int[] ageDisaggregation=ovcAndCaregiverAgeDisaggregation; //ovcAgeDisaggregation;
        if(indicatorCode.equalsIgnoreCase(beneficiariesNewlyEnrolled))
        ageDisaggregation=ovcAndCaregiverAgeDisaggregation;
        //String period=dm.getMonthAsString(Integer.parseInt(reportParams[7]))+" "+reportParams[8]+"-"+dm.getMonthAsString(Integer.parseInt(reportParams[9]))+" "+reportParams[10];
        try
        {
            String[] indicatorArray={indicatorCode};
            List list=null;
            //List valueList=new ArrayList();
            String cboId="All";
            rpt.setCboId(cboId);
            //Get the data by age disaggregation
            for(int j=0; j<ageDisaggregation.length; j+=2)
            {
                System.err.println("About to pull for "+ageDisaggregation[j]+" to "+ageDisaggregation[j+1]);
                //set the start age and end age
                rpt.setStartAge(ageDisaggregation[j]);
                rpt.setEndAge(ageDisaggregation[j+1]);
                if(rpt.getStartAge()<18)
                {
                    list=lirg.getOvcEnrolledSummStatistics(rpt, indicatorArray);                   
                }
                else
                {
                    list=processDataFor18AndAboveByCBO(rpt,indicatorCode);
                    //maleList=(List)mainList.get(0);
                    //femaleList=(List)mainList.get(1);
                }
                if(list !=null && !list.isEmpty())
                {
                    rt=(ReportTemplate)list.get(0);
                    maleList.add(rt);
                    femaleList.add(rt);
                    //valueList.add(list.get(0));
                    //maleList.addAll(getMaleList(valueList));
                    //femaleList.addAll(getFemaleList(valueList));
                    System.err.println("Pulled data for "+ageDisaggregation[j]+" to "+ageDisaggregation[j+1]);
                }
            }
            /*if(indicatorCode.equalsIgnoreCase(ovcActiveAndServeId) || indicatorCode.equalsIgnoreCase(ovcGraduatedAndServeId) || indicatorCode.equalsIgnoreCase(ovcServeAndTransferedId) || indicatorCode.equalsIgnoreCase(ovcExitedWithoutGraduationId))
            {
                List mainList=processDataFor18AndAboveByCBO(rpt,maleList,femaleList,indicatorCode);
                maleList=(List)mainList.get(0);
                femaleList=(List)mainList.get(1);
            }*/
           CustomIndicatorsReport cirt=getReportTemplate(rpt.getLevel2OuId(),rpt.getLevel3OuId(),rpt.getCboId(),partnerCode,indicatorCode,maleList,femaleList,mainReportPeriod);
           saveReportTemplate(cirt);      
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        //return mainList;
    }
    
    private List processDataFor18AndAboveByCBO(ReportParameterTemplate rpt,String indicatorCode)
    {
        //List paramList This is a method arguments
        List mainList=new ArrayList();
        try
        {
            ListOfIndicatorsReportGenerator lirg=new ListOfIndicatorsReportGenerator();
            int executed=1;
            //ReportParameterManager rpm=new ReportParameterManager();
            //ReportParameterTemplate rpt=rpm.getPopulatedReportParameterTemplate(paramList);
            DatimCaregiverReport dcr=new DatimCaregiverReport();
            ReportTemplate rt=new ReportTemplate();
            /*ReportTemplate maleRt=new ReportTemplate();
            ReportTemplate femaleRt=new ReportTemplate();*/
            String[] indicatorArray={indicatorCode};
            if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfNewOvcEnrolled().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfAdultMembersEnrolledWithinTheReportPeriod().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcCurrentlyEnrolledAndServedInReportPeriod().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfActiveCaregiversServedWithinDatimReportPeriod().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcGraduatedButServedInReportPeriod().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfCaregiversServedAndGraduatedWithinDatimReportPeriod().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcTransferedOutButServedInReportPeriod().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfCaregiversServedAndTransferedWithinDatimReportPeriod().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcExitedWithoutGraduation().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfCaregiversServedAndExitedWithinDatimReportPeriod().getIndicatorId();
            
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcProvidedReferralForHIVRelatedTestingService().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfAdultMembersProvidedReferralForHIVRelatedTestingService().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcNewlyTestedPositiveWithinTheReportPeriod().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfCaregiversNewlyTestedPositive().getIndicatorId();
            
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfHivPositiveOvcEnrolledOnARTWithinTheReportPeriod().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfHIVPositiveCaregiversNewlyEnrolledOnARTWithinTheReportPeriod().getIndicatorId();
            else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcSelfReportingAdherenceToTreatment().getIndicatorId()))
            indicatorArray[0]=ind.getIndicatorForNumberOfAdultMembersSelfReportingAdherenceToTreatment().getIndicatorId();
            else
            executed=0;
            if(executed==1)
            {
                mainList=lirg.getOvcEnrolledSummStatistics(rpt, indicatorArray);
                if(mainList==null || mainList.isEmpty())
                mainList.add(rt);
            }
            else
            {
                mainList.add(rt);
                
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    
    public CustomIndicatorsReport getReportTemplate(String level2OuId,String level3OuId,String cboName,String partnerCode,String indicatorId,List maleList,List femaleList,String period)
    {
        ReportTemplate maleRt=null;
        ReportTemplate femaleRt=null;
        IndicatorWarehouse indw=new IndicatorWarehouse();
        Indicator ind=indw.getIndicatorById(indicatorId);
        if(ind.getAlternateName()==null)
        ind.setAlternateName(ind.getIndicatorName());
        //ReportTemplate rt
        //String hhgradOvcServedId=ind.getIndicatorId();
        //String ovcEconsId=ind.getIndicatorId();
        CustomIndicatorsReport cirt=new CustomIndicatorsReport();
        cirt.setLevel2OuId(level2OuId);
        cirt.setLevel3OuId(level3OuId);
        cirt.setLevel4OuId("xxxxxxxxxxx");
        cirt.setCboId(cboName);
        cirt.setIndicator(ind);
        cirt.setIndicatorId(indicatorId);
        cirt.setIndicatorName(indicatorId);
        cirt.setReportPeriod(period);
        cirt.setPartnerCode(partnerCode);
        //rt.setp
        
        if(ind !=null)
        {
            cirt.setIndicatorName(ind.getAlternateName());
            cirt.setMerCode(ind.getMerCode());
            cirt.setOtherDisaggregation("other");
            cirt.setUserName(userName);
            cirt.setDateCreated(DateManager.getDateInstance(DateManager.getCurrentDate()));
        }        
        
        int maleValue=0;
        int femaleValue=0;
        int maleTotal=0;
        int femaleTotal=0;
        if(maleList !=null && !maleList.isEmpty() && femaleList !=null && !femaleList.isEmpty())
        {
            maleRt=(ReportTemplate)maleList.get(0);
            femaleRt=(ReportTemplate)femaleList.get(0);
            
            if(ind !=null && ind.getIndicatorType().equalsIgnoreCase(AppConstant.HOUSEHOLD_TYPE))
            {
                if(!maleList.isEmpty())
                maleValue=maleRt.getMaleTotal();//Integer.parseInt(maleList.get(0).toString());
                cirt.setGrandTotal(maleValue);
            }
            else
            {
                for(int i=0; i<maleList.size(); i++)
                {
                    maleRt=(ReportTemplate)maleList.get(i);
                    femaleRt=(ReportTemplate)femaleList.get(i);
                    maleValue=maleRt.getMaleTotal();
                    femaleValue=femaleRt.getFemaleTotal();
                    //maleValue=Integer.parseInt(maleList.get(i).toString());
                    //femaleValue=Integer.parseInt(femaleList.get(i).toString());
                    maleTotal+=maleValue;
                    femaleTotal+=femaleValue;
                    if(i==0)
                    {
                        cirt.setMaleLessThan1(maleValue);
                        cirt.setFemaleLessThan1(femaleValue);
                    }
                    else if(i==1)
                    {
                        cirt.setMale1to4(maleValue);
                        cirt.setFemale1to4(femaleValue);
                    }
                    else if(i==2)
                    {
                        cirt.setMale5to9(maleValue);
                        cirt.setFemale5to9(femaleValue);
                    }
                    else if(i==3)
                    {
                        cirt.setMale10to14(maleValue);
                        cirt.setFemale10to14(femaleValue);
                    }
                    else if(i==4)
                    {
                        cirt.setMale15to17(maleValue);
                        cirt.setFemale15to17(femaleValue);
                    }
                    else if(i==5)
                    {
                        cirt.setMale18to24(maleValue);
                        cirt.setFemale18to24(femaleValue);
                    }
                    else if(i==6)
                    {
                        cirt.setMale25Plus(maleValue);
                        cirt.setFemale25Plus(femaleValue);
                    }
                }
                int grandTotal=maleTotal+femaleTotal;
                cirt.setMaleTotal(maleTotal);
                cirt.setFemaleTotal(femaleTotal);
                cirt.setGrandTotal(grandTotal);
                //System.err.println("rt.getMaleLessThan1()="+cirt.getMaleLessThan1()+" cirt.getMale1to4()="+cirt.getMale1to4()+" rt.getMale5to9()="+cirt.getMale5to9()+" rt.getMale10to14()"+cirt.getMale10to14()+" rt.getMale15to17()="+cirt.getMale15to17()+" rt.getMale18to24()="+cirt.getMale18to24()+" rt.getMale25Plus()="+cirt.getMale25Plus());
                //System.err.println("rt.getFemaleLessThan1()="+cirt.getFemaleLessThan1()+" rt.getFemale1to4()="+cirt.getFemale1to4()+" rt.getFemale5to9()="+cirt.getFemale5to9()+" rt.getFemale10to14()"+cirt.getFemale10to14()+" rt.getFemale15to17()="+cirt.getFemale15to17()+" rt.getFemale18to24()="+cirt.getFemale18to24()+" rt.getFemale25Plus()="+cirt.getFemale25Plus());
            }
            
        }
        return cirt;
    }
    
    /*private List getMaleList(List valueList)
    {
        ReportTemplate rt=null;
        List maleList=new ArrayList();
        if(valueList==null) //|| valueList.size()<2)
        {
            maleList.add(0);
        }
        else
        {
            rt=(ReportTemplate)valueList.get(0);
            //valueList.get(1)
            maleList.add(rt.getMaleTotal());
        }
        return maleList;
    }
    private List getFemaleList(List valueList)
    {
        ReportTemplate rt=null;
        List femaleList=new ArrayList();
        if(valueList==null || valueList.size()<3)
        {
            femaleList.add(0);
        }
        else
        {
            rt=(ReportTemplate)valueList.get(0);
            //valueList.get(2)
            femaleList.add(rt.getFemaleTotal());
        }
        return femaleList;
    }*/
    public static void processCustomIndicatorsReport(List list)
    {
        try
        {
        CustomIndicatorsReportDao cirbdao=new CustomIndicatorsReportDaoImpl();
        if(list !=null)
        {
            CustomIndicatorsReport rt=null;
            for(Object obj:list)
            {
                rt = (CustomIndicatorsReport)obj; 
                saveReportTemplate(rt);
                //cirbdao.saveOrUpdateCustomIndicatorsReport(rt);
            }
        }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
    }
    public static void saveReportTemplate(CustomIndicatorsReport rt)
    {
        try
        {
            if(rt !=null)
            {
                CustomIndicatorsReportDao cirbdao=new CustomIndicatorsReportDaoImpl();
                cirbdao.saveOrUpdateCustomIndicatorsReport(rt);
                /*CustomIndicatorsReport dupRt=cirbdao.getCustomIndicatorsReport(rt);
                if(dupRt !=null)
                {
                    rt.setRecordId(dupRt.getRecordId());
                    cirbdao.updateCustomIndicatorsReport(rt);
                    System.err.println("Record with "+rt.getIndicatorName()+" updated");
                }
                else
                {
                    cirbdao.saveOrUpdateCustomIndicatorsReport(rt);
                    System.err.println("Record with "+rt.getIndicatorName()+" saved");
                }*/
            }        
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
    }
    public List getListOfCBOs(ReportParameterTemplate rpt)
    {
        //String lgaCode
        List cboList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            List cboCodeList=util.getHouseholdEnrollmentDaoInstance().getDistinctCommunityBasedOrganizationIds(rpt);
            if(cboCodeList !=null)
            { 
                CommunityBasedOrganization cbo=null;
                String cboId=null;
                for(int i=0; i<cboCodeList.size(); i++)
                {
                    if(cboCodeList.get(i) !=null)
                    {
                        cboId=cboCodeList.get(i).toString();
                        cbo=util.getCommunityBasedOrganizationDaoInstance().getCommunityBasedOrganization(cboId);
                        if(cbo==null)
                        {
                            cbo=new CommunityBasedOrganization();
                            cbo.setCboCode(null);
                            cbo.setUniqueId(cboId);
                            cbo.setCboName(cboId);
                            cbo.setAssignedLevel3OuIds(rpt.getLevel3OuId());
                            cbo.setLevel2OuId(rpt.getLevel2OuId());
                        }
                        cboList.add(cbo);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return cboList;
    }
    public static List getCustomIndicators()
    {
        List list=new ArrayList();
        IndicatorDictionary ind=new IndicatorDictionary();
        list.add(ind.getOvc_ARTSUPPIndicator());
        list.add(ind.getOvc_BIRTHCERTIndicator());
        list.add(ind.getOvc_ECONSIndicator());
        list.add(ind.getOvc_ENROLLEDIndicator());
        list.add(ind.getOvc_EDUIndicator());
        list.add(ind.getOvc_HHGRADIndicator());
        list.add(ind.getOvc_HIVPREVIndicator());
        list.add(ind.getOvc_HIVRISKASSIndicator());
        list.add(ind.getOvc_HIVSTATIndicator());
        list.add(ind.getOvc_HTSLINKIndicator());
        list.add(ind.getOvc_PROTECTIndicator());
        list.add(ind.getOvc_SERVIndicator());
        list.add(ind.getOvc_TXLINKIndicator());
        list.add(ind.getOvc_NUTRITIONIndicator());
        return list;
    }
    /*public String[] getQueryParam(List paramList)
    {
        OrganizationUnitAttributesManager ouam=new OrganizationUnitAttributesManager();
        String stateCode=(String)paramList.get(0);
        String lgaCode=(String)paramList.get(1);
        String cboCode=(String)paramList.get(2);
        String wardCode=(String)paramList.get(3);
        String startMonth=paramList.get(4).toString();
        String startYear=paramList.get(5).toString();
        String endMonth=paramList.get(6).toString();
        String endYear=paramList.get(7).toString();
        String partnerCode="All";
        if(paramList.size()>7)
        partnerCode=(String)paramList.get(8);
        
        System.err.println("partnerCode in rutil.getQueryParam(List paramList) is "+partnerCode);
        String partnerName=ouam.getPartnerName(partnerCode);
        String[] queryParam={stateCode,lgaCode,cboCode,wardCode,startMonth,startYear,endMonth,endYear,null,null,null,null,null,null,partnerName,partnerCode,null,null,partnerCode};
        return queryParam;
    }*/
}
