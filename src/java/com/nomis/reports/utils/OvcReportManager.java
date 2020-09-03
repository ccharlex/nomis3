/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.reports.utils;


import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.operationsManagement.FinancialYearManager;
import com.nomis.operationsManagement.OvcServiceAttributesManager;
import com.nomis.ovc.dao.AdultHouseholdMemberDao;
import com.nomis.ovc.dao.ChildEducationPerformanceAssessmentDao;
import com.nomis.ovc.dao.ChildEnrollmentDao;
import com.nomis.ovc.dao.ChildEnrollmentDaoImpl;
import com.nomis.ovc.dao.ChildServiceDao;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.dao.HivRiskAssessmentDao;
import com.nomis.ovc.dao.HouseholdReferralDao;
import com.nomis.ovc.dao.HouseholdServiceDao;
import com.nomis.ovc.dao.HouseholdServiceDaoImpl;
import com.nomis.ovc.dao.NutritionStatusDao;
import com.nomis.ovc.dao.SubQueryGenerator;
import com.nomis.ovc.util.AppConstant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class OvcReportManager 
{
    DaoUtility util=new DaoUtility();
    int childrenEndAge=24;
    public ReportTemplate getNumberOfCaregiversExitedWithoutGraduationServedWithinDatimReportPeriod(Indicator indicator,ReportParameterTemplate rpt)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            DatimReportTemplate drt=drg.getNoOfCaregiversExitedWithoutGraduationServed(rpt,rpt.getStartDate(),rpt.getEndDate(),maleSex);
            int maleCount=drt.getOvc_servExitedWithoutGraduation();
            drt=drg.getNoOfCaregiversExitedWithoutGraduationServed(rpt,rpt.getStartDate(),rpt.getEndDate(),femaleSex);
            int femaleCount=drt.getOvc_servExitedWithoutGraduation();
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfCaregiversServedAndTransferedWithinDatimReportPeriod(Indicator indicator,ReportParameterTemplate rpt)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            DatimReportTemplate drt=drg.getNoOfCaregiversServedAndTransfered(rpt,rpt.getStartDate(),rpt.getEndDate(),maleSex);
            int maleCount=drt.getOvc_servTransfered();
            drt=drg.getNoOfCaregiversServedAndTransfered(rpt,rpt.getStartDate(),rpt.getEndDate(),femaleSex);
            int femaleCount=drt.getOvc_servTransfered();
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfCaregiversServedAndGraduatedWithinDatimReportPeriod(Indicator indicator,ReportParameterTemplate rpt)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            DatimReportTemplate drt=drg.getNoOfGraduatedCaregiversServed(rpt,rpt.getStartDate(),rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(),maleSex);
            int maleCount=drt.getOvc_servGraduated();
            drt=drg.getNoOfGraduatedCaregiversServed(rpt,rpt.getStartDate(),rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(),femaleSex);
            int femaleCount=drt.getOvc_servGraduated();
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfActiveCaregiversServedWithinDatimReportPeriod(Indicator indicator,ReportParameterTemplate rpt)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            
            DatimReportTemplate drt=drg.getNoOfActiveCaregiversServed(rpt,rpt.getStartDate(),rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(),maleSex);
            int maleCount=drt.getOvc_servActive();
            drt=drg.getNoOfActiveCaregiversServed(rpt,rpt.getStartDate(),rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(),femaleSex);
            int femaleCount=drt.getOvc_servActive();
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfOvcReferredForHivRelatedTesting_HTSPMTCT(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        HouseholdReferralDao dao=util.getHouseholdReferralDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            String serviceCode=OvcServiceAttributesManager.getReferralForHivRelatedTesting_HTSPMTCT().getServiceCode();
            int maleCount=dao.getNumberOfOvcReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), maleSex);
            int femaleCount=dao.getNumberOfOvcReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcReferredForHivRelatedTesting_HTSPMTCT(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        HouseholdReferralDao dao=util.getHouseholdReferralDaoInstance();
        
        try
        {
            String serviceCode=OvcServiceAttributesManager.getReferralForHivRelatedTesting_HTSPMTCT().getServiceCode();
            List list=dao.getListOfOvcReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNumberOfAdultHouseholdMembersReferredForHivRelatedTesting_HTSPMTCT(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        HouseholdReferralDao dao=util.getHouseholdReferralDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            String serviceCode=OvcServiceAttributesManager.getReferralForHivRelatedTesting_HTSPMTCT().getServiceCode();
            int maleCount=dao.getNumberOfAdultHouseholdMembersReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), maleSex);
            int femaleCount=dao.getNumberOfAdultHouseholdMembersReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdultHouseholdMembersReferredForHivRelatedTesting_HTSPMTCT(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        HouseholdReferralDao dao=util.getHouseholdReferralDaoInstance();
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            String serviceCode=OvcServiceAttributesManager.getReferralForHivRelatedTesting_HTSPMTCT().getServiceCode();
            List list=dao.getListOfAdultHouseholdMembersReferredForServiceWithCompletedReferralsByDomainAndServiceTypeAndAgeLimit(rpt,AppConstant.HEALTH_DOMAIN,serviceCode,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNumberOfMalnourishedChildrenProvidedNutritionalServices(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao dao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            
            //getNumberOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String startDate, String endDate,int startAge, int endAge,int enrollmentStatus,int currentNutritionStatus,String sex)
            int maleCount=dao.getNumberOfMalnourishedChildrenProvidedNutritionalServices(rpt,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, maleSex);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNumberOfMalnourishedChildrenProvidedNutritionalServices(rpt, rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, femaleSex);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        ChildServiceDao dao=util.getChildServiceDaoInstance();
        
        try
        {
            //getListOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String startDate, String endDate,int startAge, int endAge,int enrollmentStatus,int currentNutritionStatus,String sex)
            List list=dao.getListOfMalnourishedChildrenProvidedNutritionalServices(rpt, rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNumberOfMalnourishedChildren(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        NutritionStatusDao dao=util.getNutritionStatusDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            
            //getNumberOfOvcAssessedByNutritionStatus(ReportParameterTemplate rpt,int startAge,int endAge,int nutritionStatus,String sex) throws Exception
            int maleCount=dao.getNumberOfOvcAssessedByNutritionStatus(rpt,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, maleSex);
            int femaleCount=dao.getNumberOfOvcAssessedByNutritionStatus(rpt, rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfMalnourishedChildren(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        NutritionStatusDao dao=util.getNutritionStatusDaoInstance();
        
        try
        {
            //getListOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String startDate, String endDate,int startAge, int endAge,int enrollmentStatus,int currentNutritionStatus,String sex)
            List list=dao.getListOfOvcAssessedByNutritionStatus(rpt,rpt.getStartDate(), rpt.getEndDate(),rpt.getStartAge(),rpt.getEndAge(), enrollmentStatus,AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNumberOfOvcAssessedForEducationalPerformance(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEducationPerformanceAssessmentDao dao=util.getChildEducationPerformanceAssessmentDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNumberOfOvcAssessedForEducationalPerformance(rpt,rpt.getStartDate(), rpt.getEndDate(),enrollmentStatus, maleSex);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNumberOfOvcAssessedForEducationalPerformance(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, femaleSex);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcAssessedForEducationalPerformance(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        AdultHouseholdMemberDao dao=util.getAdultHouseholdMemberDaoInstance();
        
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfAdultHouseholdMembersSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getAdult_ARTSUPPIndicator(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        AdultHouseholdMemberDao dao=util.getAdultHouseholdMemberDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
                        
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNumberOfAdultHouseholdMembersSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, maleSex);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNumberOfAdultHouseholdMembersSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, femaleSex);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdult_ARTSUPPIndicator(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        AdultHouseholdMemberDao dao=util.getAdultHouseholdMemberDaoInstance();
        
        try
        {
            //this method resets the age for adults
            rpt=getAdultReportTemplate(rpt);
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfAdultHouseholdMembersSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getOvc_ARTSUPPIndicator(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
                
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNumberOfOvcSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, maleSex);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNumberOfOvcSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, femaleSex);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcSupportedToAccessARTServicesInReportPeriod(ReportParameterTemplate rpt,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfOvcSupportedToAccessARTServicesInReportPeriod(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus,int hivStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
                
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNumberOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(), 0, enrollmentStatus, hivStatus,maleSex);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNumberOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(), 0, enrollmentStatus, hivStatus,femaleSex);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String sex,int enrollmentStatus,int hivStatus)
    {
        List mainList=new ArrayList();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(), 0, enrollmentStatus, hivStatus, sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    
    public ReportTemplate getNoOfAdultMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus,int hivStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        AdultHouseholdMemberDao dao=util.getAdultHouseholdMemberDaoInstance();
                
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        rpt=getAdultReportTemplate(rpt);
        try
        {
            //getNumberOfAdultHouseholdMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus,int enrolledOnTreatmentValue,int hivStatus,String sex)
            int maleCount=dao.getNumberOfAdultHouseholdMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(),enrollmentStatus, AppConstant.ENROLLED_ON_TREATMENT_NOTAPPLICABLE,hivStatus,maleSex);
            int femaleCount=dao.getNumberOfAdultHouseholdMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(),enrollmentStatus, AppConstant.ENROLLED_ON_TREATMENT_NOTAPPLICABLE,hivStatus,femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdultMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String sex,int enrollmentStatus,int hivStatus)
    {
        List mainList=new ArrayList();
        AdultHouseholdMemberDao dao=util.getAdultHouseholdMemberDaoInstance();
        
        try
        {
            rpt=getAdultReportTemplate(rpt);
            //getListOfAdultHouseholdMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus,int enrolledOnTreatmentValue,int hivStatus,String sex) throws Exception;
            List list=dao.getListOfAdultHouseholdMembersTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(rpt, rpt.getStartDate(), rpt.getEndDate(), enrollmentStatus,AppConstant.ENROLLED_ON_TREATMENT_NOTAPPLICABLE,hivStatus,sex);
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    
    
    public ReportTemplate getNumberOfOvcEnrolledOnTreatment(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {        
            //rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcEnrolledOnTreatment(rpt, startDate,endDate,enrolledOnTreatmentValue,enrollmentStatus,maleSex);
            int femaleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcEnrolledOnTreatment(rpt, startDate,endDate,enrolledOnTreatmentValue,enrollmentStatus,femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcEnrolledOnTreatment(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getChildEnrollmentDaoInstance().getListOfOvcEnrolledOnTreatment(rpt,startDate,endDate, enrolledOnTreatmentValue,enrollmentStatus, sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNumberOfAdultMembersEnrolledOnTreatment(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomain,String serviceCode)
    {
        //(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
        ReportTemplate rt=new ReportTemplate();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=0;
            int femaleCount=0;
            maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, 18, 200, maleSex,serviceDomain,serviceCode);
            femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, 18, 200, femaleSex,serviceDomain,serviceCode);
            
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfAdultMembersServedByServiceDomainAndSubType(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomain,String serviceCode)
    {
        //(indicator, additionalQueryCriteria, rpt.getStartAge(), rpt.getEndAge(), startDate, endDate, AppConstant.EVER_ENROLLED_NUM, AppConstant.HEALTH_DOMAIN, serviceCode));
        ReportTemplate rt=new ReportTemplate();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=0;
            int femaleCount=0;
            maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, 18, 200, maleSex,serviceDomain,serviceCode);
            femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, 18, 200, femaleSex,serviceDomain,serviceCode);
            
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdultMembersServedByServiceDomainAndSubType(String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomain,String serviceCode,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getHouseholdServiceDaoInstance().getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, 18, 200, sex, serviceDomain, serviceCode);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNumberOfOvcServedByServiceDomainAndSubType(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomain,String serviceCode)
    {
        ReportTemplate rt=new ReportTemplate();
        ReportTemplate child18AndAboveRt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=0;
            int femaleCount=0;
            ChildServiceDao sdao=util.getChildServiceDaoInstance();
            if(startAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex, serviceDomain,serviceCode);
                femaleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex, serviceDomain,serviceCode);
            }
            else
            {
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, maleSex, serviceDomain,serviceCode);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex, serviceDomain,serviceCode);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
                }
                maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex,serviceDomain,serviceCode);
                femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,serviceDomain,serviceCode);
            }
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcServedByServiceDomainAndSubType(String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomain,String serviceCode,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getChildServiceDaoInstance().getListOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, sex, serviceDomain, serviceCode);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNumberOfBeneficiariesWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue)
    {
        ReportTemplate rt=new ReportTemplate();
        ReportTemplate child18AndAboveRt=new ReportTemplate();
        try
        {        
            if(rpt.getStartAge()<18)
            {
                rt=getNumberOfOvcWithCasePlan(rpt,startDate,endDate,casePlanValue);
            }
            else
            {
                rt=getNumberOfHouseholdsWithCasePlan(rpt,startDate,endDate,casePlanValue);
                if(rpt.getStartAge()==18)
                {
                   rpt.setEndAge(childrenEndAge);
                   child18AndAboveRt=getNumberOfOvcWithCasePlan(rpt,startDate,endDate,casePlanValue);
                   rt.setChildrenFemale18Plus(child18AndAboveRt.getFemaleTotal());
                   rt.setChildrenMale18Plus(child18AndAboveRt.getMaleTotal());
                }
            } 
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public ReportTemplate getNumberOfHouseholdsWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {        
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getHouseholdEnrollmentDaoInstance().getNumberOfHouseholdsWithCasePlan(rpt,startDate,endDate, casePlanValue,maleSex);
            int femaleCount=util.getHouseholdEnrollmentDaoInstance().getNumberOfHouseholdsWithCasePlan(rpt,startDate,endDate, casePlanValue,femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfHouseholdsWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getHouseholdEnrollmentDaoInstance().getListOfHouseholdsWithCasePlan(rpt,startDate,endDate, casePlanValue, sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNumberOfOvcWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {        
            //rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcWithCasePlan(rpt, startDate,endDate,casePlanValue,maleSex);
            int femaleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcWithCasePlan(rpt, startDate,endDate,casePlanValue,femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfChildrenWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getChildEnrollmentDaoInstance().getListOfOvcWithCasePlan(rpt,startDate,endDate, casePlanValue, sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    /*public ReportTemplate getNoOfAdultMembersReferredByServiceType(ReportParameterTemplate rpt,int startAge,int endAge,String referralServiceType)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getHouseholdReferralDaoInstance().getNumberOfAdultMembersReferredInReportPeriod(rpt, maleSex,referralServiceType);
            int femaleCount=util.getHouseholdReferralDaoInstance().getNumberOfAdultMembersReferredInReportPeriod(rpt, femaleSex,referralServiceType);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfAdultMembersReferredByServiceType(ReportParameterTemplate rpt,int startAge,int endAge,String sex,String referralServiceType)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            List list=util.getHouseholdReferralDaoInstance().getListOfAdultMembersReferredInReportPeriod(rpt, sex, referralServiceType);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNoOfOvcReferredByServiceType(ReportParameterTemplate rpt,int startAge,int endAge,String referralServiceType)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getHouseholdReferralDaoInstance().getNumberOfOvcReferredInReportPeriod(rpt, maleSex,referralServiceType);
            int femaleCount=util.getHouseholdReferralDaoInstance().getNumberOfOvcReferredInReportPeriod(rpt, femaleSex,referralServiceType);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcReferredByServiceType(ReportParameterTemplate rpt,int startAge,int endAge,String sex,String referralServiceType)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            List list=util.getHouseholdReferralDaoInstance().getListOfOvcReferredInReportPeriod(rpt, sex, referralServiceType);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }*/
    public ReportTemplate getNoOfOvcByVulnerabilityStatus(ReportParameterTemplate rpt,int startAge,int endAge,String vulnerabilityStatusId)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcEnrollmentByVulnerabilityStatus(rpt, maleSex, vulnerabilityStatusId);
            int femaleCount=util.getChildEnrollmentDaoInstance().getNumberOfOvcEnrollmentByVulnerabilityStatus(rpt, femaleSex, vulnerabilityStatusId);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcByVulnerabilityStatus(ReportParameterTemplate rpt,int startAge,int endAge,String sex,String vulnerabilityStatus)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            List list=util.getChildEnrollmentDaoInstance().getListOfOvcEnrollmentByVulnerabilityStatus(rpt, sex, vulnerabilityStatus);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNoOfOvcBySchoolStatus(ReportParameterTemplate rpt,int startAge,int endAge,int schoolStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setSchoolStatus(schoolStatus);
            rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            int maleCount=util.getChildEnrollmentDaoInstance().getOvcSchoolEnrollmentData(rpt,maleSex);
            int femaleCount=util.getChildEnrollmentDaoInstance().getOvcSchoolEnrollmentData(rpt,femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcBySchoolEnrollmentStatus(ReportParameterTemplate rpt,int startAge,int endAge,String sex,int schoolStatus)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setSchoolStatus(schoolStatus);
            rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            List list=util.getChildEnrollmentDaoInstance().getOvcSchoolEnrollmentList(rpt, sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public List getListOfOvcWithBirthCertificate(ReportParameterTemplate rpt,int startAge,int endAge,int enrollmentStatus,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setBirthCertificateValue(1);
            rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            List list=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateList(rpt, enrollmentStatus,sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNoOfOvcWithBirthCertificate(ReportParameterTemplate rpt,int startAge,int endAge,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setBirthCertificateValue(1);
            rpt.setEnrollmentStatus(enrollmentStatus);
            int maleCount=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateData(rpt,enrollmentStatus,maleSex);
            int femaleCount=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateData(rpt,enrollmentStatus,femaleSex);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfOvcWithoutBirthCertificate(ReportParameterTemplate rpt,int startAge,int endAge,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setBirthCertificateValue(2);
            rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            int maleCount=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateData(rpt,enrollmentStatus,maleSex);
            int femaleCount=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateData(rpt,enrollmentStatus,femaleSex);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcWithoutBirthCertificate(ReportParameterTemplate rpt,int startAge,int endAge,int enrollmentStatus,String sex)
    {
        List mainList=new ArrayList();
        try
        {
            rpt.setStartAge(startAge);
            rpt.setEndAge(endAge);
            rpt.setBirthCertificateValue(2);
            rpt.setEnrollmentStatus(AppConstant.ACTIVE_NUM);
            List list=util.getChildEnrollmentDaoInstance().getOvcBirthCertificateList(rpt, enrollmentStatus,sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    public ReportTemplate getNoOfAdultMembersEverEnrolled(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        if(startAge<18)
        startAge=18;
        endAge=200;
        try
        {
            int maleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,enrollmentStatus,startAge,endAge,maleSex);
            int femaleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,enrollmentStatus,startAge,endAge,femaleSex);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfAdultMembersEverEnrolled(Indicator indicator,String additionalQueryCriteria,String sex,int startAge,int endAge,int currentEnrollmentStatus)
    {
        List mainList=new ArrayList();
        startAge=18;
        endAge=200;
        try
        {
            List list=util.getAdultHouseholdMemberDaoInstance().getListOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,currentEnrollmentStatus,startAge,endAge,sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfAdultMembersEnrolledByEnrollmentStatus(Indicator indicator,String additionalQueryCriteria,int hivStatus,int startAge,int endAge,int enrollmentStatus,int onTreatment)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        if(startAge<18)
        startAge=18;
        endAge=200;
        try
        {
            int maleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,enrollmentStatus,startAge,endAge,maleSex);
            int femaleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,enrollmentStatus,startAge,endAge,femaleSex);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfAdultMembersEnrolledByEnrollmentStatus(Indicator indicator,String additionalQueryCriteria,String sex,int hivStatus,int startAge,int endAge,int currentEnrollmentStatus,int onTreatment)
    {
        List mainList=new ArrayList();
        startAge=18;
        endAge=200;
        try
        {
            List list=util.getAdultHouseholdMemberDaoInstance().getListOfAdultHouseholdMembersEnrolled(additionalQueryCriteria,currentEnrollmentStatus,startAge,endAge,sex);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(Indicator indicator,String additionalQueryCriteria,int hivStatus,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int onTreatment)
    {
        ReportTemplate rt=new ReportTemplate();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        if(startAge<18)
        startAge=18;
        endAge=200;
        try
        {
            int maleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolledWithinReportPeriod(additionalQueryCriteria,enrollmentStatus,hivStatus,startDate, endDate,startAge,endAge,maleSex,onTreatment);
            int femaleCount=util.getAdultHouseholdMemberDaoInstance().getNumberOfAdultHouseholdMembersEnrolledWithinReportPeriod(additionalQueryCriteria,enrollmentStatus,hivStatus,startDate, endDate,startAge,endAge,femaleSex,onTreatment);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdultMembersEnrolledByEnrollmentStatusAndReportPeriod(Indicator indicator,String additionalQueryCriteria,String sex,int hivStatus,int startAge,int endAge,String startDate, String endDate,int currentEnrollmentStatus,int onTreatment)
    {
        List mainList=new ArrayList();
        startAge=18;
        endAge=200;
        try
        {
            List list=util.getAdultHouseholdMemberDaoInstance().getListOfAdultHouseholdMembersEnrolledWithinReportPeriod(additionalQueryCriteria,currentEnrollmentStatus,hivStatus,startDate, endDate,startAge,endAge,sex,onTreatment);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfAdultMembersServedByEnrollmentStatusAndServiceDomain(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomainType)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        if(startAge<16)
        startAge=16;
        endAge=200;
        try
        {
            String serviceCode=null;
            int maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex,serviceDomainType,serviceCode);
            int femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,serviceDomainType,serviceCode);
            
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfAdultMembersServedInReportPeriod(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,String sex,int serviceDomain,String serviceCode)
    {
        List mainList=new ArrayList();
        HouseholdServiceDao sdao=new HouseholdServiceDaoImpl();
        startAge=18;
        endAge=200;
        try
        {
            //getListOfAdultMembersServedByServiceDomainAndSubType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType,String serviceCode) throws Exception
            List list=sdao.getListOfAdultMembersServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge,sex,serviceDomain,serviceCode);
            if(list !=null)
            mainList.addAll(list);
                
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return mainList;
    }
    public ReportTemplate getNoOfAdultMembersNotServedInReportPeriod(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        HouseholdServiceDao sdao=new HouseholdServiceDaoImpl();
        startAge=18;
        endAge=200;
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=sdao.getNumberOfAdultMembersNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex);
            int femaleCount=sdao.getNumberOfAdultMembersNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfAdultMembersNotServedInReportPeriod(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,String sex)
    {
        List mainList=new ArrayList();
        HouseholdServiceDao sdao=new HouseholdServiceDaoImpl();
        startAge=18;
        endAge=200;
        try
        {
            //getListOfOvcNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception;
            List list=sdao.getListOfAdultMembersNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge,sex);
            if(list !=null)
            mainList.addAll(list);      
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfOvcNotServedInReportPeriod(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=sdao.getNumberOfOvcNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex);
            int femaleCount=sdao.getNumberOfOvcNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcNotServedInReportPeriod(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,String sex)
    {
        List mainList=new ArrayList();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        try
        {
            //getListOfOvcNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception;
            List list=sdao.getListOfOvcNotServedInReportPeriod(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge,sex);
            if(list !=null)
            mainList.addAll(list);
                
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNumberOfHivUnknownOvcNotAtRiskAndServed(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=sdao.getNumberOfHivUnknownOvcNotAtRiskServed(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex);
            int femaleCount=sdao.getNumberOfHivUnknownOvcNotAtRiskServed(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNumberOfHivUnknownOvcAtRiskServed(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=sdao.getNumberOfHivUnknownOvcAtRiskServed(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex);
            int femaleCount=sdao.getNumberOfHivUnknownOvcAtRiskServed(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfOvcRiskAssessedByEnrollmentStatusAndHivStatus(Indicator indicator,ReportParameterTemplate rpt,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int hivRiskStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        HivRiskAssessmentDao dao=util.getHivRiskAssessmentDaoInstance();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=0;
            int femaleCount=0;
            if(startAge <18)
            {
                maleCount=dao.getNumberOfOvcRiskAssessedByHivStatusAndEnrollmentStatus(rpt, startAge, endAge,startDate,endDate, enrollmentStatus,hivRiskStatus,maleSex);
                femaleCount=dao.getNumberOfOvcRiskAssessedByHivStatusAndEnrollmentStatus(rpt, startAge, endAge,startDate,endDate,enrollmentStatus,hivRiskStatus,femaleSex);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcRiskAssessedByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int hivRiskStatus,String sex)
    {
        List mainList=new ArrayList();
        HivRiskAssessmentDao dao=util.getHivRiskAssessmentDaoInstance();
        
        try
        {
            List list=dao.getListOfOvcRiskAssessedByHivStatusAndEnrollmentStatus(rpt, startAge, endAge,startDate,endDate, enrollmentStatus,hivRiskStatus,sex);
            if(list !=null)
            mainList.addAll(list);    
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfOvcServedAndRiskAssessedByEnrollmentStatusAndHivStatus(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int hivRiskStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=0;
            int femaleCount=0;
            if(startAge <18)
            {
                maleCount=sdao.getNumberOfOvcServedAndRiskAssessedByEnrollmentStatus(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex, hivRiskStatus);
                femaleCount=sdao.getNumberOfOvcServedAndRiskAssessedByEnrollmentStatus(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex, hivRiskStatus);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    /*public ReportTemplate getNoOfGBVBeneficiariesProvidedPEP(ReportParameterTemplate rpt,Indicator indicator)
    {
        ReportTemplate rt=new ReportTemplate();
        GBVServiceDao gbvsdao=new GBVServiceDaoImpl();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=gbvsdao.getNumberOfBeneficiariesProvidedPEP(rpt, maleSex);
            int femaleCount=gbvsdao.getNumberOfBeneficiariesProvidedPEP(rpt,femaleSex);
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }*/
    /*public ReportTemplate getNoOfGBVCasesByAbuseCategory(ReportParameterTemplate rpt,Indicator indicator,int enrollmentStatus,int abuseCategory)
    {
        ReportTemplate rt=new ReportTemplate();
        GenderBasedViolenceDao gbvdao=new GenderBasedViolenceDaoImpl();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            //getNumberOfPhysicalAndEmotionalGBVCases(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int gbvCategory) throws Exception
            int maleCount=gbvdao.getNumberOfPhysicalAndEmotionalGBVCases(rpt, enrollmentStatus, maleSex, abuseCategory);
            int femaleCount=gbvdao.getNumberOfPhysicalAndEmotionalGBVCases(rpt,enrollmentStatus, femaleSex, abuseCategory);
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }*/
    public ReportTemplate getNoOfBeneficiariesThatBenefitedFromEvidenceBasedParenting(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            ChildServiceDao sdao=util.getChildServiceDaoInstance();
            int maleCount=0;
            int femaleCount=0;
            String subServiceType=OvcServiceAttributesManager.getEvidenceBasedParentingService().getServiceCode(); //"Mother Baby courses";//
            if(startAge<18)
            {
                
                maleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex, AppConstant.SAFETY_DOMAIN,subServiceType);
                femaleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex, AppConstant.SAFETY_DOMAIN,subServiceType);
            }
            else
            {
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, maleSex, AppConstant.SAFETY_DOMAIN,subServiceType);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex, AppConstant.SAFETY_DOMAIN,subServiceType);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
                }
                
                maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex,AppConstant.SAFETY_DOMAIN,subServiceType);
                femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,AppConstant.SAFETY_DOMAIN,subServiceType);
            }
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfBeneficiariesThatBenefitedFromMotherBabyCourses(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            int maleCount=0;
            int femaleCount=0;
            String subServiceType=OvcServiceAttributesManager.getMotherBabyCourseServices().getServiceCode();//"Mother Baby courses";
            if(startAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex, AppConstant.HEALTH_DOMAIN,subServiceType);
                femaleCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex, AppConstant.HEALTH_DOMAIN,subServiceType);
            }
            else
            {
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, maleSex, AppConstant.HEALTH_DOMAIN,subServiceType);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex, AppConstant.HEALTH_DOMAIN,subServiceType);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
                }
                
                maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex,AppConstant.HEALTH_DOMAIN,subServiceType);
                femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,AppConstant.HEALTH_DOMAIN,subServiceType);
            }
            int total=maleCount+femaleCount;
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    /*public ReportTemplate getNoOfBeneficiariesReferred(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int referralDirection)
    {
        ReportTemplate rt=new ReportTemplate();
        ReferralServiceDao rdao=new ReferralServiceDaoImpl();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            int maleCount=rdao.getNumberOfBeneficiariesReferredByReferralDirection(additionalQueryCriteria,enrollmentStatus,startDate, endDate,startAge,endAge,maleSex,referralDirection);
            int femaleCount=rdao.getNumberOfBeneficiariesReferredByReferralDirection(additionalQueryCriteria,enrollmentStatus,startDate, endDate,startAge,endAge,femaleSex,referralDirection);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }*/
    public ReportTemplate getNoOfHivPositiveOvcServedByEnrollmentStatusAndHivStatus(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int onTreatment)
    {
        ReportTemplate rt=new ReportTemplate();
        
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        HouseholdServiceDao hhsdao=new HouseholdServiceDaoImpl();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=0;
            int femaleCount=0;
            if(startAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus,AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
            }
            else
            {
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus,AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, childrenEndAge, maleSex, onTreatment);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, childrenEndAge, femaleSex, onTreatment);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
                }
                maleCount=hhsdao.getNumberOfAdultHouseholdMembersServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus,AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                femaleCount=hhsdao.getNumberOfAdultHouseholdMembersServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfOvcServedButNotScreenedForHivRisk(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            int maleCount=0;
            int femaleCount=0;
            if(startAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusNotScreenedForHiv(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusNotScreenedForHiv(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex);
            }
            else if(startAge==18)
            {
                int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusNotScreenedForHiv(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, maleSex);
                int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusNotScreenedForHiv(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex);
                rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfOvcServedAndScreenedForHivRisk(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int hivStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            int maleCount=0;
            int femaleCount=0;
            if(startAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusScreenedForHiv(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, maleSex);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatusScreenedForHiv(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, femaleSex);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfActiveOvcServedByEnrollmentStatus(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int serviceDomainType)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        FinancialYearManager fym=new FinancialYearManager();
        String startDateOfQuarter=fym.getStartDateOfQuarter(endDate);
        //System.err.println("startDateOfQuarter in getNoOfActiveOvcServedByEnrollmentStatus is "+startDateOfQuarter);
        try
        {
            int maleCount=0;
            int femaleCount=0;
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, AppConstant.ACTIVE_NUM, startDateOfQuarter, endDate,startAge,endAge, maleSex,serviceDomainType);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, AppConstant.ACTIVE_NUM, startDateOfQuarter, endDate, startAge, endAge, femaleSex,serviceDomainType);
                        
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfActiveOvcServedByEnrollmentStatus(String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int serviceDomainType,String sex)
    {
        List mainList=new ArrayList();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        FinancialYearManager fym=new FinancialYearManager();
        String startDateOfQuarter=fym.getStartDateOfQuarter(endDate);
        try
        {
            List list=sdao.getListOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, AppConstant.ACTIVE_NUM, startDateOfQuarter, endDate,startAge,endAge, sex,serviceDomainType);
            if(list !=null)
            mainList.addAll(list);
                
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return mainList;
    }
    public ReportTemplate getNoOfOvcServedByEnrollmentStatusAndServiceDomain(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomainType)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        //String queryPart=additionalQueryCriteria;
        //System.err.println("startAge is "+startAge+" endAge is "+endAge);
        try
        {
            int maleCount=0;
            int femaleCount=0;
            maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge, maleSex,serviceDomainType);
            femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,serviceDomainType);
            /*if(endAge<18)
            {
                
            }
            else
            {
                
                String serviceCode=null;
                maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, maleSex,serviceDomainType,serviceCode);
                femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex,serviceDomainType,serviceCode);
                    
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,childrenEndAge, maleSex,serviceDomainType);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex,serviceDomainType);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);   
                }
            }*/
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public ReportTemplate getNoOfBeneficiariesServedByEnrollmentStatusAndServiceDomain(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomainType)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        //String queryPart=additionalQueryCriteria;
        //System.err.println("startAge is "+startAge+" endAge is "+endAge);
        try
        {
            int maleCount=0;
            int femaleCount=0;
            if(endAge<18)
            {
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge, maleSex,serviceDomainType);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,serviceDomainType);
            }
            else
            {
                
                String serviceCode=null;
                maleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, maleSex,serviceDomainType,serviceCode);
                femaleCount=util.getHouseholdServiceDaoInstance().getNumberOfHouseholdsServedByServiceDomainAndSubType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, femaleSex,serviceDomainType,serviceCode);
                    
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,childrenEndAge, maleSex,serviceDomainType);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, childrenEndAge, femaleSex,serviceDomainType);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);   
                }
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return rt;
    }
    public List getListOfOvcServedByEnrollmentStatusAndServiceDomain(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int serviceDomainType,String sex)
    {
        List mainList=new ArrayList();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        try
        {
            List list=sdao.getListOfOvcServedByEnrollmentStatusAndServiceType(additionalQueryCriteria, enrollmentStatus, startDate, endDate,startAge,endAge, sex,serviceDomainType);
            if(list !=null)
            mainList.addAll(list);
                
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return mainList;
    }
    public ReportTemplate getNoOfOvcEnrolledByEnrollmentStatusWithinReportPeriod(Indicator indicator,ReportParameterTemplate rpt,String ageQuery,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String additionalQueryCriteria=rsg.getOrganizationUnitQuery(rpt);
        String periodQuery=rsg.getOvcEnrollmentDateQuery(rpt.getStartDate(), rpt.getEndDate());
        String queryPart= ageQuery+additionalQueryCriteria+periodQuery;
        String malesql=rsg.getOvcSexQuery("M")+" "+queryPart;
        String femalesql=rsg.getOvcSexQuery("F")+" "+queryPart;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNoOfOvcByEnrollmentStatus(malesql,enrollmentStatus);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNoOfOvcByEnrollmentStatus(femalesql,enrollmentStatus);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcEnrolledByEnrollmentStatusWithinReportPeriod(ReportParameterTemplate rpt,String ageQuery,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String additionalQueryCriteria=rsg.getOrganizationUnitQuery(rpt);
        String periodQuery=rsg.getOvcEnrollmentDateQuery(rpt.getStartDate(), rpt.getEndDate());
        String queryPart= ageQuery+additionalQueryCriteria+periodQuery;
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            queryPart=rsg.getOvcSexQuery(sex)+" "+queryPart;
            
        }
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfOvcByEnrollmentStatus(queryPart,enrollmentStatus);//Integer.parseInt(maleList.get(0).toString());
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfOvcEnrolledByEnrollmentStatus(Indicator indicator,String additionalQueryCriteria,String ageQuery,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        
        String queryPart= ageQuery+additionalQueryCriteria;
        String malesql=rsg.getOvcSexQuery("M")+" "+queryPart;
        String femalesql=rsg.getOvcSexQuery("F")+" "+queryPart;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNoOfOvcByEnrollmentStatus(malesql,enrollmentStatus);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNoOfOvcByEnrollmentStatus(femalesql,enrollmentStatus);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcEnrolledByEnrollmentStatus(String additionalQueryCriteria,String ageQuery,String sex,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        
        String queryPart= ageQuery+additionalQueryCriteria;
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            queryPart=rsg.getOvcSexQuery(sex)+" "+queryPart;
            
        }
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfOvcByEnrollmentStatus(queryPart,enrollmentStatus);//Integer.parseInt(maleList.get(0).toString());
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getNoOfBeneficiariesServedByEnrollmentStatusAndHivStatus(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int hivStatus,int onTreatment)
    {
        ReportTemplate rt=new ReportTemplate();
        //DatimReportGenerator drg=new DatimReportGenerator();
        //SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        HouseholdServiceDao hhsdao=new HouseholdServiceDaoImpl();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=0;
            int femaleCount=0;
            if(startAge<18)
            {
                //maleCount=util.getChildEnrollmentDaoInstance().getNoOfOvcByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                //femaleCount=util.getChildEnrollmentDaoInstance().getNoOfOvcByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
                maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
            }
            else
            {
                if(startAge==18)
                {
                    int childrenMale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, childrenEndAge, maleSex, onTreatment);
                    int childrenFemale18AndAboveCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, childrenEndAge, femaleSex, onTreatment);
                    //int childrenMale18AndAboveCount=util.getChildEnrollmentDaoInstance().getNoOfOvcByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, childrenEndAge, maleSex, onTreatment);
                    //int childrenFemale18AndAboveCount=util.getChildEnrollmentDaoInstance().getNoOfOvcByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, childrenEndAge, femaleSex, onTreatment);
                    rt.setChildrenFemale18Plus(childrenFemale18AndAboveCount);
                    rt.setChildrenMale18Plus(childrenMale18AndAboveCount);
                }
                //maleCount=util.getAdultHouseholdMemberDaoInstance().getNoOfAdultHouseholdMembersByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                //femaleCount=util.getAdultHouseholdMemberDaoInstance().getNoOfAdultHouseholdMembersByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
                //getNoOfAdultHouseholdMembersByEnrollmentStatusAndHivStatus
                maleCount=hhsdao.getNumberOfAdultHouseholdMembersServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, maleSex, onTreatment);
                femaleCount=hhsdao.getNumberOfAdultHouseholdMembersServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, hivStatus, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
            }
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfHivPositiveBeneficiariesServedByEnrollmentStatusAndTreatmentStatus(Indicator indicator,String additionalQueryCriteria,int startAge,int endAge,String startDate,String endDate,int enrollmentStatus,int onTreatment)
    {
        ReportTemplate rt=new ReportTemplate();
        DatimReportGenerator drg=new DatimReportGenerator();
        SubQueryGenerator rsg=new SubQueryGenerator();
        ChildServiceDao sdao=util.getChildServiceDaoInstance();
        
        String maleSex=AppConstant.MALESEX;
        String femaleSex=AppConstant.FEMALESEX;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, maleSex, onTreatment);
            int femaleCount=sdao.getNumberOfOvcServedByEnrollmentStatusAndHivStatus(additionalQueryCriteria, enrollmentStatus, AppConstant.HIV_POSITIVE_NUM, startDate, endDate, startAge, endAge, femaleSex, onTreatment);
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public ReportTemplate getNoOfOvcEnrolledByEnrollmentStatusAndHivStatus(Indicator indicator,ReportParameterTemplate rpt,int enrollmentStatus,int hivStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String dateQuery="";
        if(enrollmentStatus==AppConstant.CURRENTLY_ENROLLED_NUM || enrollmentStatus==AppConstant.EVER_ENROLLED_NUM)
        {
            dateQuery=rsg.getOvcEnrollmentDateQuery(null, null);
        }
        else
        dateQuery=rsg.getOvcEnrollmentDateQuery(rpt.getStartDate(), rpt.getEndDate());
        
        String additionalQueryCriteria=rsg.getOrganizationUnitQuery(rpt);
        String ageQuery=rsg.getOvcCurrentAgeQuery(rpt.getStartAge(),rpt.getEndAge());
        
        String queryPart= ageQuery+additionalQueryCriteria;
        String malesql=rsg.getOvcSexQuery("M")+" "+queryPart+dateQuery;
        String femalesql=rsg.getOvcSexQuery("F")+" "+queryPart+dateQuery;
        
        try
        {
            //System.err.println("malesql is "+malesql);
            int maleCount=dao.getNoOfOvcByEnrollmentStatusAndHivStatus(malesql,enrollmentStatus,hivStatus);//Integer.parseInt(maleList.get(0).toString());
            int femaleCount=dao.getNoOfOvcByEnrollmentStatusAndHivStatus(femalesql,enrollmentStatus,hivStatus);//Integer.parseInt(femaleList.get(0).toString());
            int total=maleCount+femaleCount;
            
            rt.setMaleTotal(maleCount);
            rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(indicator !=null)
            {
                rt.setIndicatorId(indicator.getIndicatorId());
                rt.setIndicatorName(indicator.getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfOvcEnrolledByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String sex,int enrollmentStatus,int hivStatus)
    {
        List mainList=new ArrayList();
        ChildEnrollmentDao dao=new ChildEnrollmentDaoImpl();
        SubQueryGenerator rsg=new SubQueryGenerator();
        String additionalQueryCriteria=rsg.getOrganizationUnitQuery(rpt);
        String ageQuery=rsg.getOvcCurrentAgeQuery(rpt.getStartAge(),rpt.getEndAge());
        String dateQuery=rsg.getOvcEnrollmentDateQuery(rpt.getStartDate(), rpt.getEndDate());
        String queryPart= ageQuery+additionalQueryCriteria+dateQuery;
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            queryPart=rsg.getOvcSexQuery(sex)+" "+queryPart;
            
        }
        try
        {
            //System.err.println("malesql is "+malesql);
            List list=dao.getListOfOvcByEnrollmentStatusAndHivStatus(queryPart,enrollmentStatus,hivStatus);//Integer.parseInt(maleList.get(0).toString());
            if(list !=null)
            mainList.addAll(list);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mainList;
    }
    public ReportTemplate getOVC_SERV(ReportParameterTemplate rpt)
    {
        ReportTemplate rt=new ReportTemplate();
        
        try
        {
            //getNoOfActiveOvcServed(ReportParameterTemplate rpt,String startDate,String endDate,int startAge,int endAge,String sex)
            DatimReportGenerator drg=new DatimReportGenerator();
            DatimReportTemplate drt=drg.getNoOfActiveOvcServed(rpt, rpt.getStartDate(), rpt.getEndDate(), rpt.getStartAge(), rpt.getEndAge(), "M");
            
            rt.setMaleTotal(drt.getOvc_servActive());
            System.err.println("rt.getMaleTotal() is "+rt.getMaleTotal());
            drt=drg.getNoOfActiveOvcServed(rpt, rpt.getStartDate(), rpt.getEndDate(), rpt.getStartAge(), rpt.getEndAge(), "F");
            
            rt.setFemaleTotal(drt.getOvc_servActive());
            System.err.println("rt.getFemaleTotal() is "+rt.getFemaleTotal());
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return rt;
    }
    public ReportTemplate getNumberOfHouseholdsEnrolled(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus)
    {
        ReportTemplate rt=new ReportTemplate();
        try
        {        
            rpt.setEnrollmentStatus(AppConstant.EVER_ENROLLED_NUM);
            int maleCount=util.getHouseholdEnrollmentDaoInstance().getNumberOfHouseholdsEnrolled(rpt,startDate,endDate,enrollmentStatus);
            //int femaleCount=util.getHouseholdEnrollmentDaoInstance().getNumberOfHouseholdsEnrolled(rpt,startDate,endDate,enrollmentStatus);
            int total=maleCount;//+femaleCount;
            
            //rt.setMaleTotal(maleCount);
            //rt.setFemaleTotal(femaleCount);
            rt.setGrandTotal(total);
            if(rpt.getIndicator() !=null)
            {
                rt.setIndicatorId(rpt.getIndicator().getIndicatorId());
                rt.setIndicatorName(rpt.getIndicator().getIndicatorName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rt;
    }
    public List getListOfHouseholdsEnrolled(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus)
    {
        List mainList=new ArrayList();
        try
        {
            List list=util.getHouseholdEnrollmentDaoInstance().getListOfHouseholdsEnrolled(rpt,startDate,endDate,enrollmentStatus);
            if(list !=null)
            mainList.addAll(list);   
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       return mainList;
    }
    private ReportParameterTemplate getAdultReportTemplate(ReportParameterTemplate rpt)
    {
        //An adult age starts from 18, so, reset start age to 18 if less than 18
        if(rpt.getStartAge()<18)
        rpt.setStartAge(18);
        //If the request is from List of Indicators report, adult age is not considered, set end age to maximum 200
        if(rpt.getEndAge()<18 || rpt.getReportType()==AppConstant.LISTOFINDICATORS_REPORTTYPE)
        rpt.setEndAge(200);
        
        return rpt;
    }
}
