/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.utils;


import com.nomis.operationsManagement.VulnerabilityStatusManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.dao.SubQueryGenerator;
import com.nomis.ovc.util.AppConstant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class ListOfIndicatorsReportGenerator 
{
    public List getOvcEnrolledSummStatistics(ReportParameterTemplate rpt,String[] selectedIndicators) throws Exception
    {
        IndicatorDictionary ind=new IndicatorDictionary();
        Indicator indicator=null;
        IndicatorWarehouse indwh=new IndicatorWarehouse();
        //CustomIndicatorsReportTemplate rt=null;//new CustomIndicatorsReportTemplate();
        List list=new ArrayList();
        List mainList=new ArrayList();
        if(selectedIndicators !=null && selectedIndicators.length>0)
        {
            SubQueryGenerator rsg=new SubQueryGenerator();
            String indicatorCode=null;
            String indicatorName=" ";
            OvcReportManager orm=new OvcReportManager();
            //boolean numberOfServicesSet=false;
            String orgUnitQuery=rsg.getOrganizationUnitQuery(rpt);
            String ageQuery=rsg.getOvcCurrentAgeQuery(rpt.getStartAge(),rpt.getEndAge());
            String ovcServiceAgeQuery=rsg.getAgeAtOvcServiceQuery(rpt.getStartAge(),rpt.getEndAge());
            String startDate=rpt.getStartDate();
            String endDate=rpt.getEndDate();
            String additionalQueryCriteria=orgUnitQuery;
            String serviceCode=null;
            for(int i=0; i<selectedIndicators.length; i++)
            {
                indicatorCode=selectedIndicators[i];// //vcnotserved
                indicator=indwh.getIndicatorById(indicatorCode);
                indicatorName=indwh.getIndicatorById(indicatorCode).getIndicatorName();
                rpt.setIndicator(indicator);
                serviceCode=ReferralReportManager.getReferralServiceCode(indicatorCode);
                //System.err.println("indicatorCode is "+indicatorCode+"; indicatorName is "+indicatorName);
                System.err.println("indicator.getIndicatorId() is "+indicator.getIndicatorId()+"; indicator.getIndicatorId() is "+indicator.getIndicatorName());
                if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfActiveOvcServedInReportPeriod().getIndicatorId()))
                list.add(orm.getNoOfActiveOvcServedByEnrollmentStatus(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.ALL_SERVICE_DOMAIN));
                else if(indicatorCode.equalsIgnoreCase("cgiverserve"))
                list.add(orm.getNoOfAdultMembersServedByEnrollmentStatusAndServiceDomain(indicator,additionalQueryCriteria,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,AppConstant.ALL_SERVICE_DOMAIN));
                else if(indicatorCode.equalsIgnoreCase("ahmnotserve"))
                list.add(orm.getNoOfAdultMembersNotServedInReportPeriod(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcservedrpe"))
                list.add(orm.getNoOfOvcServedByEnrollmentStatusAndServiceDomain(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,AppConstant.ALL_SERVICE_DOMAIN));
                else if(indicatorCode.equalsIgnoreCase("vcnotserved"))
                list.add(orm.getNoOfOvcNotServedInReportPeriod(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.CURRENTLY_ENROLLED_NUM));
                
                else if(indicatorCode.equalsIgnoreCase("vcnewlyEnro"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusWithinReportPeriod(indicator,rpt, ageQuery,AppConstant.EVER_ENROLLED_NUM));
                
                else if(indicatorCode.equalsIgnoreCase("vccenrolled"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.CURRENTLY_ENROLLED_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcevenroled"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.EVER_ENROLLED_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcgraduated"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.GRADUATED_NUM));
                else if(indicatorCode.equalsIgnoreCase("ovcmigrated"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.MIGRATED_NUM));
                else if(indicatorCode.equalsIgnoreCase("vclosttofup"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.LOST_TO_FOLLOWUP_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcknowndeat"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.DIED_NUM));
                else if(indicatorCode.equalsIgnoreCase("vctranspepf"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.TRANSFERED_PEPFAR_NUM));
                else if(indicatorCode.equalsIgnoreCase("vctranonpep"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.TRANSFERED_NONPEPFAR_NUM));
                else if(indicatorCode.equalsIgnoreCase("ovcagingout"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.AGED_OUT_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcinactivrp"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.INACTIVE_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcexitnogra"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.EXITED_WITHOUT_GRADUATION_NUM));
                else if(indicatorCode.equalsIgnoreCase("vcreenrolrp"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria, ageQuery,AppConstant.REENROLLED_NUM));
                
                else if(indicatorCode.equalsIgnoreCase("hivPosEnrol"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_POSITIVE_NUM));
                else if(indicatorCode.equalsIgnoreCase("hivNegEnrol"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_NEGATIVE_NUM));
                else if(indicatorCode.equalsIgnoreCase("hivUnkEnrol"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_UNKNOWN_NUM));
                
                else if(indicatorCode.equalsIgnoreCase("vchivposeen"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_POSITIVE_NUM));
                else if(indicatorCode.equalsIgnoreCase("vchivnegeen"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_NEGATIVE_NUM));
                else if(indicatorCode.equalsIgnoreCase("vchivunkeen"))
                list.add(orm.getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(indicator,rpt,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_UNKNOWN_NUM));
            
                else if(indicatorCode.equalsIgnoreCase("cgeenrolled"))
                list.add(orm.getNoOfAdultMembersEverEnrolled(indicator,additionalQueryCriteria,rpt.getStartAge(),rpt.getEndAge(),AppConstant.EVER_ENROLLED_NUM));
                
                else if(indicatorCode.equalsIgnoreCase("ahmenrollrp"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("ahmcurenrol"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),AppConstant.ACTIVE_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                
                else if(indicatorCode.equalsIgnoreCase("cggraduated"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.GRADUATED_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cglosttofup"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.LOST_TO_FOLLOWUP_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cgmigrated1"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.MIGRATED_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cgtransferd"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.TRANSFERED_NONPEPFAR_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cgknowndeat"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.DIED_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cgexitnogra"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EXITED_WITHOUT_GRADUATION_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                else if(indicatorCode.equalsIgnoreCase("cgreenrolrp"))
                list.add(orm.getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.REENROLLED_NUM,AppConstant.NO_TREATMENT_CRITERIA));
                
                else if(indicatorCode.equalsIgnoreCase("vccelt18cer"))
                list.add(orm.getNoOfOvcWithBirthCertificate(rpt,rpt.getStartAge(), rpt.getEndAge()));
                else if(indicatorCode.equalsIgnoreCase("vcwithnobcc"))
                list.add(orm.getNoOfOvcWithoutBirthCertificate(rpt,rpt.getStartAge(), rpt.getEndAge()));
                
                else if(indicatorCode.equalsIgnoreCase("vccurrinsch"))
                list.add(orm.getNoOfOvcBySchoolStatus(rpt,rpt.getStartAge(), rpt.getEndAge(),AppConstant.CHILD_IN_SCHOOL));
                else if(indicatorCode.equalsIgnoreCase("vcnotschool"))
                list.add(orm.getNoOfOvcBySchoolStatus(rpt,rpt.getStartAge(), rpt.getEndAge(),AppConstant.CHILD_NOT_IN_SCHOOL));
                
                
                /*else if(indicatorCode.equalsIgnoreCase("vcreferedrp"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),null));
                else if(indicatorCode.equalsIgnoreCase("cgreferedrp"))
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,null));
                else if(indicatorCode.equalsIgnoreCase("cgrefrhcrpe"))
                    
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefrhcrpe"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgrefstirpe"))
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefstirpe"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                
                else if(indicatorCode.equalsIgnoreCase("cgrefhivtrc"))
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgrefpeprpe"))
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgrefpmtcrp"))
                list.add(orm.getNoOfAdultMembersReferredByServiceType(rpt,0,200,serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefhivtrc"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                
                else if(indicatorCode.equalsIgnoreCase("vcrefeidrpe"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefimzrpe"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefmuacrp"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcrefpeprpe"))
                list.add(orm.getNoOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),serviceCode));
                */
                else if(indicatorCode.equalsIgnoreCase("vchivadhsup"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcplhasupgp"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcstimulatn"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcemergtran"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcmbbcourse"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcwashmsgrp"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcevidbpart"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcenterptrg"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vclinkdsilc"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vclinktofin"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                
                else if(indicatorCode.equalsIgnoreCase("vcinsuppgrp"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcstatnysup"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SCHOOL_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcschfeesup"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SCHOOL_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcemgshlter"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vclegalmgbv"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcbirthregn"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcpostviocn"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcchldright"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vcdevmilhiv"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vchivtrtlit"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("vchivdiscrp"))
                list.add(orm.getNumberOfOvcServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                
                
                
                else if(indicatorCode.equalsIgnoreCase("cghivadhsup"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgplhasupgp"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgstimulatn"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgemergtran"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgmbbcourse"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgwashmsgrp"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgevidbpart"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgenterptrg"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cglinkdsilc"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cglinktofin"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                
                
                
                
                else if(indicatorCode.equalsIgnoreCase("cgbetparent"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                //else if(indicatorCode.equalsIgnoreCase("cgsinovotns"))
                //list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cghivdiscrp"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgfinlitrcy"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgsmbussupp"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgemgshlter"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode));
                else if(indicatorCode.equalsIgnoreCase("cgsoftskill"))
                list.add(orm.getNumberOfAdultMembersServedByServiceDomainAndSubType(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode));
                             
                else if(indicatorCode.equalsIgnoreCase("hhcasepland"))
                {
                    rpt.setStartAge(18);
                    rpt.setEndAge(200);
                    list.add(orm.getNumberOfHouseholdsWithCasePlan(rpt,rpt.getStartDate(),rpt.getEndDate(),1));
                }
                else if(indicatorCode.equalsIgnoreCase("vccasepland"))
                list.add(orm.getNumberOfOvcWithCasePlan(rpt,rpt.getStartDate(),rpt.getEndDate(),1));
                
                //Custom indicators
                else if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfOvcCurrentlyEnrolledAndServedInReportPeriod().getIndicatorId()))
                list.add(orm.getOVC_SERV(rpt));
            }
            mainList=getReportTemplatesWithRowColorAndSerialNo(list);
        }
        return mainList;
    }
    
    public List getOvcList(ReportParameterTemplate rpt,String sex,String indicatorCode) throws Exception
    {
        IndicatorDictionary ind=new IndicatorDictionary();
        Indicator indicator=null;
        IndicatorWarehouse indwh=new IndicatorWarehouse();
        ReportTemplate rt=null;//new CustomIndicatorsReportTemplate();
        List list=new ArrayList();
        List mainList=new ArrayList();
        
            SubQueryGenerator rsg=new SubQueryGenerator();
            
            String indicatorName=" ";
            OvcReportManager orm=new OvcReportManager();
            //boolean numberOfServicesSet=false;
            String orgUnitQuery=rsg.getOrganizationUnitQuery(rpt);
            String ageQuery=rsg.getOvcCurrentAgeQuery(rpt.getStartAge(),rpt.getEndAge());
            String ovcServiceAgeQuery=rsg.getAgeAtOvcServiceQuery(rpt.getStartAge(),rpt.getEndAge());
            String additionalQueryCriteria=orgUnitQuery;
            String serviceCode=ReferralReportManager.getReferralServiceCode(indicatorCode);
            //getListOfOvcEnrolledByEnrollmentStatus(String additionalQueryCriteria,String ageQuery,String sex,int enrollmentStatus)
                
                indicator=indwh.getIndicatorById(indicatorCode);
                indicatorName=indwh.getIndicatorById(indicatorCode).getIndicatorName();
                System.err.println("indicatorCode is "+indicatorCode+"; indicatorName is "+indicatorName);
                //
                if(indicatorCode.equalsIgnoreCase(ind.getIndicatorForNumberOfActiveOvcServedInReportPeriod().getIndicatorId()))
                list=orm.getListOfActiveOvcServedByEnrollmentStatus(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.ALL_SERVICE_DOMAIN, sex);
                else if(indicatorCode.equalsIgnoreCase("vcnewlyEnro"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusWithinReportPeriod(rpt, ageQuery,sex,AppConstant.EVER_ENROLLED_NUM);
                else if(indicatorCode.equalsIgnoreCase("vccenrolled"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.CURRENTLY_ENROLLED_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcevenroled"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.EVER_ENROLLED_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcgraduated"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.GRADUATED_NUM);
                else if(indicatorCode.equalsIgnoreCase("ovcmigrated"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.MIGRATED_NUM);
                else if(indicatorCode.equalsIgnoreCase("vclosttofup"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.LOST_TO_FOLLOWUP_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcknowndeat"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.DIED_NUM);
                else if(indicatorCode.equalsIgnoreCase("vctranspepf"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.TRANSFERED_PEPFAR_NUM);
                else if(indicatorCode.equalsIgnoreCase("vctranonpep"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.TRANSFERED_NONPEPFAR_NUM);
                else if(indicatorCode.equalsIgnoreCase("ovcagingout"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.AGED_OUT_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcinactivrp"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.INACTIVE_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcexitnogra"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.EXITED_WITHOUT_GRADUATION_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcreenrolrp"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatus(additionalQueryCriteria, ageQuery,sex,AppConstant.REENROLLED_NUM);
                
                else if(indicatorCode.equalsIgnoreCase("hivPosEnrol"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_POSITIVE_NUM);
                else if(indicatorCode.equalsIgnoreCase("hivNegEnrol"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_NEGATIVE_NUM);
                else if(indicatorCode.equalsIgnoreCase("hivUnkEnrol"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.CURRENTLY_ENROLLED_NUM,AppConstant.HIV_UNKNOWN_NUM);
                
                else if(indicatorCode.equalsIgnoreCase("vchivposeen"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_POSITIVE_NUM);
                else if(indicatorCode.equalsIgnoreCase("vchivnegeen"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_NEGATIVE_NUM);
                else if(indicatorCode.equalsIgnoreCase("vchivunkeen"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_UNKNOWN_NUM);
                else if(indicatorCode.equalsIgnoreCase("vchivunkeen"))
                list=orm.getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(rpt,sex,AppConstant.EVER_ENROLLED_NUM,AppConstant.HIV_UNKNOWN_NUM);
                else if(indicatorCode.equalsIgnoreCase("vcservedrpe"))
                list=orm.getListOfOvcServedByEnrollmentStatusAndServiceDomain(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,AppConstant.ALL_SERVICE_DOMAIN,sex);
                else if(indicatorCode.equalsIgnoreCase("vcnotserved"))
                list=orm.getListOfOvcNotServedInReportPeriod(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.CURRENTLY_ENROLLED_NUM,sex);
                else if(indicatorCode.equalsIgnoreCase("ahmnotserve"))
                list=orm.getListOfAdultMembersNotServedInReportPeriod(indicator,additionalQueryCriteria, rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,sex);
                if(indicatorCode.equalsIgnoreCase("cgiverserve"))
                list=orm.getListOfAdultMembersServedInReportPeriod(indicator,additionalQueryCriteria,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,sex,AppConstant.ALL_SERVICE_DOMAIN,null);
                //
                else if(indicatorCode.equalsIgnoreCase("cgeenrolled"))
                list=orm.getListOfAdultMembersEverEnrolled(indicator,additionalQueryCriteria,sex,rpt.getStartAge(),rpt.getEndAge(),AppConstant.EVER_ENROLLED_NUM);
                else if(indicatorCode.equalsIgnoreCase("ahmenrollrp"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EVER_ENROLLED_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("ahmcurenrol"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatus(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),AppConstant.ACTIVE_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                
                else if(indicatorCode.equalsIgnoreCase("cggraduated"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.GRADUATED_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cglosttofup"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.LOST_TO_FOLLOWUP_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cgmigrated1"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.MIGRATED_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cgtransferd"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.TRANSFERED_NONPEPFAR_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cgknowndeat"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.DIED_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cgexitnogra"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.EXITED_WITHOUT_GRADUATION_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                else if(indicatorCode.equalsIgnoreCase("cgreenrolrp"))
                list=orm.getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(indicator,additionalQueryCriteria,sex,AppConstant.HIV_ALL_STATUS_NUM,rpt.getStartAge(),rpt.getEndAge(),rpt.getStartDate(),rpt.getEndDate(),AppConstant.REENROLLED_NUM,AppConstant.NO_TREATMENT_CRITERIA);
                
                else if(indicatorCode.equalsIgnoreCase("vccelt18cer"))
                list=orm.getListOfOvcWithBirthCertificate(rpt,rpt.getStartAge(), rpt.getEndAge(),sex);
                else if(indicatorCode.equalsIgnoreCase("vcwithnobcc"))
                list=orm.getListOfOvcWithoutBirthCertificate(rpt,rpt.getStartAge(), rpt.getEndAge(),sex);
                
                else if(indicatorCode.equalsIgnoreCase("vccurrinsch"))
                list=orm.getListOfOvcBySchoolEnrollmentStatus(rpt,rpt.getStartAge(), rpt.getEndAge(),sex,AppConstant.CHILD_IN_SCHOOL);
                else if(indicatorCode.equalsIgnoreCase("vcnotschool"))
                list=orm.getListOfOvcBySchoolEnrollmentStatus(rpt,rpt.getStartAge(), rpt.getEndAge(),sex,AppConstant.CHILD_NOT_IN_SCHOOL);
  
                /*else if(indicatorCode.equalsIgnoreCase("vcreferedrp"))
                list=orm.getListOfOvcReferredByServiceType(rpt,rpt.getStartAge(), rpt.getEndAge(),sex,null);
                else if(indicatorCode.equalsIgnoreCase("cgreferedrp"))
                list=orm.getListOfAdultMembersReferredByServiceType(rpt,0,200,sex,null);
                */
                else if(indicatorCode.equalsIgnoreCase("hhcasepland"))
                list=orm.getListOfHouseholdsWithCasePlan(rpt,null,null,1,sex);
                else if(indicatorCode.equalsIgnoreCase("vccasepland"))
                list=orm.getListOfChildrenWithCasePlan(rpt,null,null,1,sex);
                
                else if(indicatorCode.equalsIgnoreCase("vchivadhsup"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcplhasupgp"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcstimulatn"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcemergtran"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcmbbcourse"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcwashmsgrp"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcevidbpart"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vcenterptrg"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vclinkdsilc"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("vclinktofin"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                
                else if(indicatorCode.equalsIgnoreCase("cghivadhsup"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgplhasupgp"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgstimulatn"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgemergtran"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgmbbcourse"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgwashmsgrp"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgevidbpart"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgenterptrg"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cglinkdsilc"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cglinktofin"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                
                else if(indicatorCode.equalsIgnoreCase("cgbetparent"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cghivdiscrp"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgfinlitrcy"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgsmbussupp"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgemgshlter"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode, sex);
                else if(indicatorCode.equalsIgnoreCase("cgsoftskill"))
                list=orm.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode, sex);
                                
                
                else if(indicatorCode.equalsIgnoreCase("vcinsuppgrp"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.STABLE_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcstatnysup"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SCHOOL_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcschfeesup"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SCHOOL_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcemgshlter"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vclegalmgbv"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcbirthregn"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcpostviocn"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcchldright"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.SAFETY_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vcdevmilhiv"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vchivtrtlit"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode,sex);
                else if(indicatorCode.equalsIgnoreCase("vchivdiscrp"))
                list=orm.getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), rpt.getStartDate(), rpt.getEndDate(), AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode,sex);
                //If this is a caregiver or adult indicator, process as such
                if(indicator !=null && indicator.getIndicatorType().equalsIgnoreCase(AppConstant.CAREGIVER_TYPE))
                mainList=getAdultMembersWithRowColorAndSerialNo(list);
                else //if this a child indicator, process as such
                mainList=getOvcWithRowColorAndSerialNo(list);
                    
        return mainList;
    }
    public List getOvcWithRowColorAndSerialNo(List list)
    {
        List mainList=new ArrayList();
        if(list !=null)
        {
            for(int i=0; i<list.size(); i++)
            {
                Ovc ovc=(Ovc)list.get(i);
                ovc.setSerialNo(i+1);
                if((i%2)==1)
                ovc.setRowColor("#DDDDDD");
                mainList.add(ovc);
            }
        }
        return mainList;
    }
    public List getAdultMembersWithRowColorAndSerialNo(List list)
    {
        List mainList=new ArrayList();
        if(list !=null)
        {
            for(int i=0; i<list.size(); i++)
            {
                AdultHouseholdMember ahm=(AdultHouseholdMember)list.get(i);
                ahm.setSerialNo(i+1);
                if((i%2)==1)
                ahm.setRowColor("#DDDDDD");
                mainList.add(ahm);
            }
        }
        return mainList;
    }
    public List getReportTemplatesWithRowColorAndSerialNo(List list)
    {
        List mainList=new ArrayList();
        if(list !=null)
        {
            for(int i=0; i<list.size(); i++)
            {
                ReportTemplate rt=(ReportTemplate)list.get(i);
                System.err.println("rt.getIndicatorName() is "+rt.getIndicatorName());
                rt.setSerialNo(i+1);
                if((i%2)==1)
                rt.setRowColor("#DDDDDD");
                mainList.add(rt);
            }
        }
        return mainList;
    }
}
