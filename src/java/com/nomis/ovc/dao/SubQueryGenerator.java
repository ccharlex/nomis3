/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.util.AppConstant;
import com.nomis.reports.utils.ReportParameterTemplate;
//import java.security.Provider.Service;



/**
 *
 * @author smomoh
 */
public class SubQueryGenerator 
{
    public static String getVulnerabilityStatusQuery(String vulnerabilityStatusCodeOrName)
    {
        String query=" ";
        if(vulnerabilityStatusCodeOrName !=null)
        query=" and ovc.vulnerabilityStatusCode like '%"+vulnerabilityStatusCodeOrName+"%'";
        return query;
    }
    public static String getReferralServiceLastModifiedDateQuery(String startDate, String endDate)
    {
        String query=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query=" and referral.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public static String getGraduationBenchmarkAchievementModifiedDateQuery(String startDate, String endDate)
    {
        String query=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query=" and gba.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public static String getReferralServiceDateQuery(String startDate, String endDate)
    {
        String query=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query=" and referral.dateOfReferral between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    
    public static String getBeneficiariesReferralDirectionQuery(int referralDirection)
    {
        String query=" ";
        if(referralDirection>0)
        {
            query+=" and referral.referralDirection="+referralDirection;
        }
        
        return query;
    }
    public static String getAgeAtHivRiskAssessmentQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (hra.ageAtRiskAssessment="+startAge+" or hra.ageUnitAtRiskAssessment=1)";
            else if(startAge ==0 && endAge >0)
            ageQuery=" and hra.ageAtRiskAssessment between "+startAge+" and "+endAge;
            else if(startAge >0)
            ageQuery=" and hra.ageAtRiskAssessment between "+startAge+" and "+endAge +" and hra.ageUnitAtRiskAssessment=2";
            else if(startAge == endAge && startAge>0)
            ageQuery=" and hra.ageAtRiskAssessment="+startAge+" and hra.ageUnitAtRiskAssessment=2";
            else
            ageQuery=" and hra.ageAtRiskAssessment between "+startAge+" and "+endAge;
         }
        
        return ageQuery;
    }
    public static String getAgeAtNutritionAssessmentQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (na.ageAtAssessment="+startAge+" or na.ageUnitAtAssessment=1)";
            else if(startAge ==0 && endAge >0)
            ageQuery=" and na.ageAtAssessment between "+startAge+" and "+endAge;
            else if(startAge >0)
            ageQuery=" and na.ageAtAssessment between "+startAge+" and "+endAge +" and na.ageUnitAtAssessment=2";
            else if(startAge == endAge && startAge>0)
            ageQuery=" and na.ageAtAssessment="+startAge+" and na.ageUnitAtAssessment=2";
            else
            ageQuery=" and na.ageAtAssessment between "+startAge+" and "+endAge;
         }
        
        return ageQuery;
    }
    public static String getHivStatusAtHivRiskAssessmentQuery(int hivStatusAtRiskAssessment)
    {
        String query="";
        if(hivStatusAtRiskAssessment>0)
        {
            query=" and hra.hivStatusAtRiskAssessment ="+hivStatusAtRiskAssessment;
        }
        return query;
    }
    public static String getHivNegativeOvcHivRiskStatusQuery(int hivRiskStatus,String startDate, String endDate)
    {
        String query="";
        if(hivRiskStatus>0)
        {
            query=" and ovc.currentHivStatus ="+AppConstant.HIV_NEGATIVE_NUM+getOvcAtRiskQuery(hivRiskStatus);//+" and hra.childAtRiskQuestion="+hivRiskStatus;
            if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
            {
                query+=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
            }
        }
        return query;
    }
    public static String getHivUnknownOvcHivRiskStatusQuery(int hivRiskStatus,String startDate, String endDate)
    {
        String query="";
        if(hivRiskStatus>0)
        {
            query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+getOvcAtRiskQuery(hivRiskStatus);//+" and hra.childAtRiskQuestion="+hivRiskStatus;
            //if(hivRiskStatus==1)
            //query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+" and (hra.childAtRiskQuestion=1 or hra.childAtRiskQuestion=5)";
            if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
            {
                query+=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
            }
        }
        return query;
    }
    private static String getOvcAtRiskQuery(int riskValue)
    {
        String query="";
        if(riskValue>0)
        {
            if(riskValue==AppConstant.CHILD_AT_RISK_NUM || riskValue==AppConstant.CHILD_AT_HIGH_RISK_NUM)
            query=" and (hra.childAtRiskQuestion="+AppConstant.CHILD_AT_RISK_NUM+" or hra.childAtRiskQuestion="+AppConstant.CHILD_AT_HIGH_RISK_NUM+")";
            else
            query=" and (hra.childAtRiskQuestion="+AppConstant.CHILD_NOT_AT_RISK_NUM+" or hra.childAtRiskQuestion="+AppConstant.CHILD_AT_LOW_RISK_NUM+")";
        }
        return query;
    }
    /*private static String getOvcAtRiskQuery()
    {
        return " and (hra.childAtRiskQuestion=1 or hra.childAtRiskQuestion=5)";
    }*/
    public static String getHivUnknownOvcAtRiskQuery(String startDate, String endDate)
    {
        String query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+getOvcAtRiskQuery(AppConstant.CHILD_AT_HIGH_RISK_NUM);
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public static String getTestNotIndicatedQuery(String startDate, String endDate)
    {
        //String query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+" and (hra.childAtRiskQuestion=0 or hra.childAtRiskQuestion=2 or hra.childAtRiskQuestion=4)";
        String query=getOvcHivStatusQuery(AppConstant.HIV_TEST_NOT_INDICATED_NUM);
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            //query+=" and ovc.dateOfCurrentHivStatus between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    /*public static String getTestNotIndicatedQuery(String startDate, String endDate)
    {
        //String query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+" and (hra.childAtRiskQuestion=0 or hra.childAtRiskQuestion=2 or hra.childAtRiskQuestion=4)";
        String query=getOvcHivStatusQuery(AppConstant.HIV_TEST_NOT_INDICATED_NUM);
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }*/
    public static String getOvcHivRiskStatusQuery(int hivRiskStatus,String startDate, String endDate)
    {
        String query=getOvcAtRiskQuery(hivRiskStatus);//" and hra.childAtRiskQuestion="+hivRiskStatus;
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public static String getHivUnknownOvcNotScreenedForHivRiskAssessmentQuery(String startDate, String endDate)
    {
        String query=getOvcHivStatusQuery(AppConstant.HIV_UNKNOWN_NUM)+" and ovc.ovcId not in (select distinct hra.ovcId from HivRiskAssessment hra ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" where hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        query+=")";
        return query;
    }
    public static String getHivNegativeOvcNotScreenedForHivRiskAssessmentQuery(String startDate, String endDate)
    {
        String query=" and ovc.currentHivStatus =2 and ovc.ovcId not in (select distinct hra.ovcId from HivRiskAssessment hra ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" where hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        query+=")";
        return query;
    }
    public static String getOvcNotScreenedForHivRiskAssessmentQuery(String startDate, String endDate)
    {
        String query=" and ovc.currentHivStatus !=1 and ovc.ovcId not in (select distinct hra.ovcId from HivRiskAssessment hra ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query+=" where hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        query+=")";
        return query;
    }
    public static String getHivRiskAssessmentLastModifiedDateQuery(String startDate, String endDate)
    {
        String query=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query=" and hra.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public static String getHivRiskAssessmentDateQuery(String startDate, String endDate)
    {
        String query=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            query=" and hra.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
        return query;
    }
    public String getAdultHouseholdMemberGenderQuery(String sex)
    {
        String sexQuery="";
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            sexQuery=" and ahm.sex='"+sex+"'";
        }
        return sexQuery;
    }
    public static String getAdultHouseholdMemberCurrentEnrollmentStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            if(status==AppConstant.EXITED_WITHOUT_GRADUATION_NUM)
            query=" and (ahm.currentEnrollmentStatus=3 or ahm.currentEnrollmentStatus =4 or ahm.currentEnrollmentStatus =5 or ahm.currentEnrollmentStatus =8 or ahm.currentEnrollmentStatus =9 or ahm.currentEnrollmentStatus =10 or ahm.currentEnrollmentStatus =11 or ahm.currentEnrollmentStatus =13)";
            else if(status==AppConstant.OVC_SERV_NUM)
            query=" and (ahm.currentEnrollmentStatus=1 or ahm.currentEnrollmentStatus =2)";
            else if(status==AppConstant.CURRENTLY_ENROLLED_NUM)
            query=" and (ahm.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or ahm.currentEnrollmentStatus ="+AppConstant.REENROLLED_NUM+")";
            else
            query=" and ahm.currentEnrollmentStatus="+status;
        }
        return query;
    }
    public String getAdultHouseholdAgeBelow18Query()
    {
        String ageQuery=" and ahm.ageAtBaseline <18";
        return ageQuery;
    }
    public String getAdultHouseholdAgeAtBaselineQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<16)
        {
            startAge=16;
            endAge=200;
        }
        if(startAge<=endAge)
        {
            if(startAge == endAge)
            ageQuery=" and ahm.ageAtBaseline="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and ahm.ageAtBaseline between "+startAge+" and "+endAge;
            }
        }
        return ageQuery;
    }
    public String getAdultHouseholdMemberCurrentAgeQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<16)
        {
            startAge=16;
            endAge=200;
        }
        if(startAge<=endAge)
        {
            if(startAge == endAge)
            ageQuery=" and ahm.currentAge="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and ahm.currentAge between "+startAge+" and "+endAge;
            }
        }
        return ageQuery;
    }
    public static String getOvcSexQuery(String sex)
    {
        String query="";
        if(sex !=null && !sex.equalsIgnoreCase("All"))
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            query=" and ovc.sex='"+sex+"'";   
        }
        return query;
    }
    public static String getOvcBaselineBirthCertificateQuery(int birthCertificateStatus)
    {
        String query=" and ovc.birthCertificate="+birthCertificateStatus;   
        //if ovc.birthCertificate=2, this means those without birth certificate and those that the value was not inidcated i.e 0
        //Data may be from excel, and not properly formatted
        if(birthCertificateStatus==2)
        query=" and (ovc.birthCertificate=0 or ovc.birthCertificate=2)";
        return query;
    }
    public static String getOvcCurrentBirthCertificateQuery(int birthCertificateStatus)
    {
        String query=" and ovc.currentBirthRegistrationStatus="+birthCertificateStatus;   
        //if ovc.birthCertificate=2, this means those without birth certificate and those that the value was not inidcated i.e 0
        //Data may be from excel, and not properly formatted
        if(birthCertificateStatus==2)
        query=" and (ovc.currentBirthRegistrationStatus=0 or ovc.currentBirthRegistrationStatus=2)";
        return query;
    }
    public static String getOvcCurrentSchoolStatusQuery(int currentSchoolStatus)
    {
        String query=" and ovc.currentSchoolStatus="+currentSchoolStatus;   
        //if ovc.birthCertificate=2, this means those without birth certificate and those that the value was not inidcated i.e 0
        //Data may be from excel, and not properly formatted
        if(currentSchoolStatus==2)
        query=" and (ovc.currentSchoolStatus=0 or ovc.currentSchoolStatus=2)";
        return query;
    }
    public static String getAdultHouseholdMemberHivStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            // or ovc.currentHivStatus=5
            if(status==3)
            query=" and (ahm.currentHivStatus=0 or ahm.currentHivStatus=3 or ahm.currentHivStatus=4)";
            else
            query=" and ahm.currentHivStatus="+status;
        }
        return query;
    }
    public static String getHouseholdMemberHivPositiveOnTreatmentQuery()
    {
        return " and (ahm.currentHivStatus="+AppConstant.HIV_POSITIVE_NUM+" and ahm.enrolledOnTreatment="+AppConstant.ENROLLED_ON_TREATMENT_YES_NUM+")";
    }
    public static String getHouseholdMemberHivPositiveNotOnTreatmentQuery()
    {
        return " and (ahm.currentHivStatus="+AppConstant.HIV_POSITIVE_NUM+" and ahm.enrolledOnTreatment !=1)";
    }
    public static String getCurrentNutritionStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            query=" and ns.currentNutritionalStatus="+status;
            if(status==AppConstant.NUTRITIONSTATUS_MALNOURISHED_ALL_NUM)
            query=" and (ns.currentNutritionalStatus="+AppConstant.NUTRITIONSTATUS_MILD_MALNUTRITION_NUM+" or ns.currentNutritionalStatus="+AppConstant.NUTRITIONSTATUS_MODERATE_MALNUTRITION_NUM+" or ns.currentNutritionalStatus="+AppConstant.NUTRITIONSTATUS_SEVERE_MALNUTRITION_NUM+")";
        }
        return query;
    }
    public static String getOvcHivStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            // or ovc.currentHivStatus=5
            if(status==AppConstant.HIV_UNKNOWN_NUM)
            query=" and (ovc.currentHivStatus=0 or ovc.currentHivStatus="+AppConstant.HIV_UNKNOWN_NUM+" or ovc.currentHivStatus="+AppConstant.HIV_UNDISCLOSED_NUM+" or ovc.currentHivStatus="+AppConstant.HIV_TEST_REQUIRED_NUM+")";
            else if(status==AppConstant.CHILD_NOT_SCREENED_NUM)
            query=" and (ovc.currentHivStatus=0 or ovc.currentHivStatus="+AppConstant.HIV_UNKNOWN_NUM+" or ovc.currentHivStatus="+AppConstant.HIV_UNDISCLOSED_NUM+")";
            else
            query=" and ovc.currentHivStatus="+status;
        }
        return query;
    }
    public static String getOvcHivPositiveOnTreatmentQuery()
    {
        return " and (ovc.currentHivStatus=1 and ovc.childEnrolledOnTreatment=1)";
    }
    public static String getOvcHivPositiveNotOnTreatmentQuery()
    {
        return " and (ovc.currentHivStatus=1 and ovc.childEnrolledOnTreatment !=1)";
    }
    /*public String getOvcGenderQuery(String sex)
    {
        String sexQuery="";
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            sexQuery=" and ovc.sex='"+sex+"'";
        }
        return sexQuery;
    }*/
    public static String getOvcCurrentEnrollmentStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            if(status==AppConstant.EXITED_WITHOUT_GRADUATION_NUM)
            query=" and (ovc.currentEnrollmentStatus=3 or ovc.currentEnrollmentStatus =4 or ovc.currentEnrollmentStatus =5 or ovc.currentEnrollmentStatus =8 or ovc.currentEnrollmentStatus =9 or ovc.currentEnrollmentStatus =10 or ovc.currentEnrollmentStatus =11 or ovc.currentEnrollmentStatus =13)";
            else if(status==AppConstant.OVC_SERV_NUM)
            query=" and (ovc.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or ovc.currentEnrollmentStatus ="+AppConstant.GRADUATED_NUM+")";
            else if(status==AppConstant.CURRENTLY_ENROLLED_NUM)
            query=" and (ovc.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or ovc.currentEnrollmentStatus ="+AppConstant.REENROLLED_NUM+")";
            else
            query=" and ovc.currentEnrollmentStatus="+status;
        }
        return query;
    }
    public String getAgeAtReferralQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (referral.ageAtReferral="+startAge+" or referral.ageUnitAtReferral="+AppConstant.AGEUNIT_MONTH_NUM+")";
            else if(startAge ==0 && endAge >0)
            ageQuery=" and referral.ageAtReferral between "+startAge+" and "+endAge;
            else if(startAge >0)
            ageQuery=" and referral.ageAtReferral between "+startAge+" and "+endAge +" and referral.ageUnitAtReferral="+AppConstant.AGEUNIT_YEAR_NUM+")";
            else if(startAge == endAge && startAge>0)
            ageQuery=" and referral.ageAtReferral="+startAge+" and referral.ageUnitAtReferral="+AppConstant.AGEUNIT_YEAR_NUM+")";
            else
            ageQuery=" and referral.ageAtReferral between "+startAge+" and "+endAge;
         }
        return ageQuery;
    }
    public String getReferralCompletedQuery(int referralCompleted)
    {
        String query="";
        if(referralCompleted >0)
        {
             query=" and referral.referralCompleted="+referralCompleted;
        }
        return query;
    }
    public String getOvcCurrentAgeQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (ovc.currentAge="+startAge+" or ovc.currentAgeUnit=1)";
            else if(startAge ==0 && endAge >0)
            ageQuery=" and ovc.currentAge between "+startAge+" and "+endAge;
            else if(startAge >0)
            ageQuery=" and ovc.currentAge between "+startAge+" and "+endAge +" and ovc.currentAgeUnit=2";
            else if(startAge == endAge && startAge>0)
            ageQuery=" and ovc.currentAge="+startAge+" and ovc.currentAgeUnit=2";
            else
            ageQuery=" and ovc.currentAge between "+startAge+" and "+endAge;
         }
        return ageQuery;
    }
    public String getAgeCriteria(String firstAge,String secondAge)
    {
        String ageQuery=" ";
        try
        {
            if(firstAge ==null || firstAge.equalsIgnoreCase("All") || secondAge ==null || secondAge.equalsIgnoreCase("All"))
            return ageQuery;
            int startAge=Integer.parseInt(firstAge);
            int endAge=18;
            if(secondAge.indexOf("+")!=-1)
            {
               return " and (ovc.currentAge >= "+startAge+")";
            }
            endAge=Integer.parseInt(secondAge);
            ageQuery=getOvcCurrentAgeQuery(startAge,endAge);
            
        }
        catch(NumberFormatException nex)
        {
            ageQuery=" ";
            nex.printStackTrace();
        }
        catch(Exception ex)
        {
           ageQuery=" ";
           ex.printStackTrace();
        }
        return ageQuery;
    }
    public String getAgeServiceQueryCriteria(String firstAge,String secondAge)
    {
        String ageQuery=" ";
        try
        {
            if(firstAge ==null || firstAge.equalsIgnoreCase("All") || secondAge ==null || secondAge.equalsIgnoreCase("All"))
            return ageQuery;
            int startAge=Integer.parseInt(firstAge);
            int endAge=17;
            if(secondAge.indexOf("+")!=-1)
            {
               return " and (ovc.currentAge >= "+startAge+")";
            }
            endAge=Integer.parseInt(secondAge);
            ageQuery=getOvcCurrentAgeQuery(startAge,endAge); 
        }
        catch(NumberFormatException nex)
        {
            ageQuery=" ";
            nex.printStackTrace();
        }
        catch(Exception ex)
        {
           ageQuery=" ";
           ex.printStackTrace();
        }
        return ageQuery;
    }
    public String getAgeAtOvcServiceQuery(int startAge,int endAge)
    {
        String ageQuery=getAgeServiceQueryCriteria(startAge+"",endAge+"");
        
        return ageQuery;
    }
    public String getAgeAtEnrollmentStatusQuery(String firstAge,String secondAge)
    {
        String ageQuery=" ";
        try
        {
            if(firstAge ==null || firstAge.equalsIgnoreCase("All") || secondAge ==null || secondAge.equalsIgnoreCase("All"))
            return ageQuery;
            int startAge=Integer.parseInt(firstAge);
            int endAge=17;
            if(secondAge.indexOf("+")!=-1)
            {
               return " and (qst.currentAge >= "+startAge+")";
            }
            endAge=Integer.parseInt(secondAge);
            if((startAge==endAge) && endAge==0)
            ageQuery=" and (qst.currentAge <12 and (qst.currentAgeUnit=1))";
            else if(startAge>0 && (startAge==endAge))
            ageQuery=" and (qst.currentAge= "+startAge+" and qst.currentAgeUnit=2)";
            else if(startAge==0 && startAge < endAge)
            ageQuery=" and (qst.currentAge >="+startAge+" and qst.currentAge <="+endAge+") ";
            else if(startAge>0 && startAge < endAge)
            ageQuery=" and (qst.currentAge >= "+startAge+" and qst.currentAge <="+endAge+" and qst.currentAgeUnit=2)";
            else 
            ageQuery=" "; 
        }
        catch(NumberFormatException nex)
        {
            ageQuery=" ";
            nex.printStackTrace();
        }
        catch(Exception ex)
        {
           ageQuery=" ";
           ex.printStackTrace();
        }
        return ageQuery;
    }
    public static String getEnrollmentStatusHivStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            // or ovc.currentHivStatus=5 
            if(status==AppConstant.HIV_UNKNOWN_NUM)
            query=" and (qst.hivStatus=0 or qst.hivStatus="+AppConstant.HIV_UNKNOWN_NUM+" or qst.hivStatus="+AppConstant.HIV_UNDISCLOSED_NUM+" or qst.hivStatus="+AppConstant.HIV_TEST_REQUIRED_NUM+")";
            else if(status==AppConstant.CHILD_NOT_SCREENED_NUM)
            query=" and (qst.hivStatus=0 or qst.hivStatus="+AppConstant.HIV_UNKNOWN_NUM+" or qst.hivStatus="+AppConstant.HIV_UNDISCLOSED_NUM+")";
            else
            query=" and qst.hivStatus="+status;
        }
        return query;
    }
    public static String getEnrollmentStatusHivPositiveOnTreatmentQuery()
    {
        return " and (qst.hivStatus="+AppConstant.HIV_POSITIVE_NUM+" and qst.enrolledOnTreatment=1)";
    }
    public static String getEnrollmentStatusHivPositiveNotOnTreatmentQuery()
    {
        return " and (qst.hivStatus="+AppConstant.HIV_POSITIVE_NUM+" and qst.enrolledOnTreatment !=1)";
    }
    public String getAgeAtReferralServiceQuery(int startAge,int endAge)
    {
        String ageQuery=getOvcCurrentAgeQuery(startAge, endAge);
        /*if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (referral.ageAtService="+startAge+" or referral.ageUnitAtService=1)";
            else if(startAge == endAge)
            ageQuery=" and referral.ageAtService="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and referral.ageAtService between "+startAge+" and "+endAge;
            }
        }*/
        return ageQuery;
    }
    /*public String getReferralServiceTypeQuery(String serviceCodeOrName)
    {
        String query="";
        if(serviceCodeOrName !=null)
        {
            Service service=OvcServiceAttributesManager.getService(serviceCodeOrName);
            if(service !=null )
            {
                String serviceName=service.getServiceName();
                if(serviceName !=null && serviceName.indexOf(";") !=-1)
                {
                    serviceName=serviceName.replaceAll(";", "");
                    serviceName=serviceName.trim().toUpperCase();
                }
                if(service.getSubDomainName() !=null)
                {
                    if(service.getSubDomainName().equalsIgnoreCase("HIV/STI Prevention"))
                    {
                        query=" and referral.hivStiPrev like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("Laboratory/Diagnosis"))
                    {
                        query=" and referral.labAndDiagnosis like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("OI/ART Services"))
                    {
                        query=" and referral.oIArtServices like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("TB Services"))
                    {
                        query=" and referral.tbServices like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("FCH Services"))
                    {
                        query=" and referral.fchServices like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("Psychosocial and economic support"))
                    {
                        query=" and referral.psychoAndEconomicServices like '%"+serviceCodeOrName+"%'";
                    }
                    else if(service.getSubDomainName().equalsIgnoreCase("Legal services"))
                    {
                        query=" and referral.legalServices like '%"+serviceCodeOrName+"%'";
                    }
                    else
                    {
                        query=" and referral.otherServices like '%"+serviceCodeOrName+"%'";
                    }
                }
                else
                {
                    query=" and UPPER(referral.otherServices) like '%"+serviceName+"%'";
                }
            }
        }
        return query;
    }*/
    public String getAgeAtHouseholdServiceQuery(int startAge,int endAge)
    {
        /*if(startAge <18)
        {
            startAge=18;
            endAge=200;
        }*/
        String ageQuery=getAdultHouseholdMemberCurrentAgeQuery(startAge,endAge);
        /*if(startAge<=endAge && startAge>17)
        {
            if(startAge == endAge)
            ageQuery=" and hhs.ageAtService="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and hhs.ageAtService between "+startAge+" and "+endAge;
            }
        }*/
        return ageQuery;
    }
    public String getAgeAtRiskAssessmentQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (hra.ageAtAssessment="+startAge+" or hra.ageUnitAtAssessment=1)";
            else if(startAge == endAge)
            ageQuery=" and hra.ageAtAssessment="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and hra.ageAtAssessment between "+startAge+" and "+endAge;
            }
        }
        return ageQuery;
    }
    public String getAgeAtGBVServiceQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge == endAge)
            ageQuery=" and gbvs.ageAtService="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and gbvs.ageAtService between "+startAge+" and "+endAge;
            }
        }
        return ageQuery;
    }
    public String getOvcBaselineAgeQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            if(startAge ==0 && endAge==0)
            ageQuery=" and (ovc.ageAtBaseline="+startAge+" or ovc.ageUnit=1)";
            else if(startAge == endAge)
            ageQuery=" and ovc.ageAtBaseline="+startAge;
            else if(startAge < endAge)
            {
                ageQuery=" and ovc.ageAtBaseline between "+startAge+" and "+endAge;
            }
        }
        return ageQuery;
    }
    public String getOvcWithCasePlanQuery(int casePlanvalue)
    {
        String query=" and ovc.childHasCasePlan="+casePlanvalue;
        if(casePlanvalue==2)
        {
            query=" and (ovc.childHasCasePlan=0 or ovc.childHasCasePlan=2)";
        }
        return query;
    }
    public String getOvcEnrolledOnTreatmentQuery(int enrolledOnTreatmentValue)
    {
        String query="";
        if(enrolledOnTreatmentValue==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
        query=" and ovc.currentHivStatus="+AppConstant.HIV_POSITIVE_NUM+" and ovc.enrolledOnTreatment="+enrolledOnTreatmentValue;
        else if(enrolledOnTreatmentValue==0 || enrolledOnTreatmentValue==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM)
        {
            query=" and ovc.currentHivStatus="+AppConstant.HIV_POSITIVE_NUM+" and (ovc.enrolledOnTreatment=0 or ovc.enrolledOnTreatment="+AppConstant.ENROLLED_ON_TREATMENT_NO_NUM+")";
        }
        return query;
    }
    public String getHouseholdHeadQuery()
    {
        String query=" and hhe.hhUniqueId=ahm.beneficiaryId ";
        return query;
    }
    public String getHouseholdsWithCasePlanQuery(int casePlanvalue)
    {
        String query=" and hhe.hhHasCasePlan="+casePlanvalue;
        if(casePlanvalue==2)
        {
            query=" and (hhe.hhHasCasePlan=0 or hhe.hhHasCasePlan=2)";
        }
        return query;
    }
    public String getHouseholdHeadBaselineAgeQuery(int startAge,int endAge)
    {
        String ageQuery="";
        if(startAge<=endAge)
        {
            ageQuery=" and hhe.ageAtBaseline between "+startAge+" and "+endAge;
        }
        return ageQuery;
    }
    public String getOrganizationUnitQuery(ReportParameterTemplate rpt)
    {
        String orgUnitQuery="";
        if(rpt !=null)
        {
            String ouPath="";
            if(rpt.getLevel2OuId() !=null && !rpt.getLevel2OuId().equalsIgnoreCase("All") && !rpt.getLevel2OuId().equalsIgnoreCase("select"))
            {
                ouPath=rpt.getLevel2OuId(); 
                if(rpt.getLevel3OuId() !=null && !rpt.getLevel3OuId().equalsIgnoreCase("All") && !rpt.getLevel3OuId().equalsIgnoreCase("select"))
                {
                    ouPath=ouPath+"/"+rpt.getLevel3OuId();
                    if(rpt.getLevel4OuId() !=null && !rpt.getLevel4OuId().equalsIgnoreCase("All") && !rpt.getLevel4OuId().equalsIgnoreCase("select"))
                    {
                        ouPath=ouPath+"/"+rpt.getLevel4OuId();
                    }
                }
               orgUnitQuery=" and ou.ouPath like '%"+ouPath+"%'"; 
            }
            if(rpt.getCboId() !=null && !rpt.getCboId().equalsIgnoreCase("All") && !rpt.getCboId().equalsIgnoreCase("select"))
            orgUnitQuery=orgUnitQuery+" and hhe.cboId='"+rpt.getCboId()+"'";
            if(rpt.getPartnerCode() !=null && !rpt.getPartnerCode().equalsIgnoreCase("All") && !rpt.getPartnerCode().equalsIgnoreCase("select") && rpt.getPartnerCode().trim().length()>0)
            orgUnitQuery=orgUnitQuery+" and hhe.partnerCode='"+rpt.getPartnerCode()+"'";
        }
        return orgUnitQuery;
    }
    public String getOrganizationUnitQueryWithoutCBO(ReportParameterTemplate rpt)
    {
        String orgUnitQuery="";
        if(rpt !=null)
        {
            String ouPath="";
            if(rpt.getLevel2OuId() !=null && !rpt.getLevel2OuId().equalsIgnoreCase("All") && !rpt.getLevel2OuId().equalsIgnoreCase("select"))
            {
                ouPath=rpt.getLevel2OuId(); 
                if(rpt.getLevel3OuId() !=null && !rpt.getLevel3OuId().equalsIgnoreCase("All") && !rpt.getLevel3OuId().equalsIgnoreCase("select"))
                {
                    ouPath=ouPath+"/"+rpt.getLevel3OuId();
                    if(rpt.getLevel4OuId() !=null && !rpt.getLevel4OuId().equalsIgnoreCase("All") && !rpt.getLevel4OuId().equalsIgnoreCase("select"))
                    {
                        ouPath=ouPath+"/"+rpt.getLevel4OuId();
                    }
                }
               orgUnitQuery=" and ou.ouPath like '%"+ouPath+"%'"; 
            }
            
        }
        return orgUnitQuery;
    }
    public String getOrganizationUnitLevelQueryForGBV(ReportParameterTemplate rpt)
    {
        String orgUnitQuery="";
        if(rpt !=null)
        {
            String ouPath="";
            if(rpt.getLevel2OuId() !=null && !rpt.getLevel2OuId().equalsIgnoreCase("All") && !rpt.getLevel2OuId().equalsIgnoreCase("select"))
            {
                ouPath=rpt.getLevel2OuId(); 
                if(rpt.getLevel3OuId() !=null && !rpt.getLevel3OuId().equalsIgnoreCase("All") && !rpt.getLevel3OuId().equalsIgnoreCase("select"))
                {
                    ouPath=ouPath+"/"+rpt.getLevel3OuId();
                    if(rpt.getLevel4OuId() !=null && !rpt.getLevel4OuId().equalsIgnoreCase("All") && !rpt.getLevel4OuId().equalsIgnoreCase("select"))
                    {
                        ouPath=ouPath+"/"+rpt.getLevel4OuId();
                    }
                }
               orgUnitQuery=" and ou.ouPath like '%"+ouPath+"%'"; 
            }
            if(rpt.getCboId() !=null && !rpt.getCboId().equalsIgnoreCase("All") && !rpt.getCboId().equalsIgnoreCase("select"))
            orgUnitQuery=orgUnitQuery+" and gbv.cboId='"+rpt.getCboId()+"'";
        }
        return orgUnitQuery;
    }
    public static String getSchoolOrganizationUnitQuery()
    {
        String orgUnitQuery=" from School school, OrganizationUnit ou where school.orgUnitId=ou.uid ";
        
        return orgUnitQuery;
    }
    public static String getReferralFacilityOrganizationUnitQuery()
    {
        String orgUnitQuery=" from ReferralFacility rf, OrganizationUnit ou where rf.organizationUnitId=ou.uid ";
        
        return orgUnitQuery;
    }
    public static String getHheOvcNutritionAssessmentOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc, NutritionAssessment na, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=na.ovcId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcNutritionStatusOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc, NutritionStatus ns, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=ns.ovcId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcNutritionAssessmentNutritionStatusOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc, NutritionAssessment na,NutritionStatus ns, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=na.ovcId and na.ovcId=ns.ovcId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberCareAndSupportOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm, CareAndSupportChecklist casc, OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ahm.beneficiaryId=casc.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcHouseholdCareplanOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, Ovc ovc, HouseholdCareplan hcp, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=hcp.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcHouseholdEducationalAssessmentOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, Ovc ovc, ChildEducationPerformanceAssessment cepa, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=cepa.ovcId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberHouseholdCareplanOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm, HouseholdCareplan hcp, OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ahm.beneficiaryId=hcp.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcCareAndSupportOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, Ovc ovc, CareAndSupportChecklist casc, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=casc.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOvcHouseholdReferralOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, Ovc ovc, HouseholdReferral referral, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=referral.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberHouseholdReferralOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm, HouseholdReferral referral, OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ahm.beneficiaryId=referral.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberCaregiverAccessToEmergencyFundOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,CaregiverAccessToEmergencyFund caef, OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ahm.beneficiaryId=caef.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheCareplanAchievementChecklistOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,CareplanAchievementChecklist cpa, OrganizationUnit ou where hhe.hhUniqueId=cpa.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }//BeneficiaryStatusUpdate
    public static String getHheChildEducationPerformanceAssessmentOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,ChildEducationPerformanceAssessment cepa, OrganizationUnit ou where hhe.hhUniqueId=ovc.hhUniqueId and ovc.ovcId=cepa.ovcId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheHHVAOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,HouseholdVulnerabilityAssessment hva, OrganizationUnit ou where hhe.hhUniqueId=hva.hhUniqueId and hhe.organizationUnit=ou.uid ";
        
        return orgUnitQuery;
    }
    public static String getHheRevisedHHVAOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,RevisedHouseholdVulnerabilityAssessment rhva, OrganizationUnit ou where hhe.hhUniqueId=rhva.hhUniqueId and hhe.organizationUnit=ou.uid ";
        
        return orgUnitQuery;
    }
    public static String getHheOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou where hhe.organizationUnit=ou.uid ";
        
        return orgUnitQuery;
    }
    /*public static String getHheOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou where hhe.organizationUnit=ou.uid ";
        
        return orgUnitQuery;
    }*/
    public static String getARTSupportQuery(int transportationSupport)
    {
        String orgUnitQuery="";
        if(transportationSupport>0)
        {
            orgUnitQuery=" and casc.receivedTransportationSupport="+transportationSupport;
        }
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOvcOrganizationUnitCareAndSupportQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,Ovc ovc,OrganizationUnit ou, CareAndSupportChecklist casc where hhe.hhUniqueId=ahm.hhUniqueId and ovc.caregiverId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid and ovc.ovcId=casc.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitCareAndSupportQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,OrganizationUnit ou, CareAndSupportChecklist casc where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=casc.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc, OrganizationUnit ou where ovc.hhUniqueId=hhe.hhUniqueId and hhe.organizationUnit=ou.uid ";
        //getHheAdultHouseholdMemberOvcOrganizationUnitQuery();
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOvcOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,Ovc ovc,OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ovc.caregiverId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOvcOrganizationUnitEnrollmentStatusHistoryQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,Ovc ovc,OrganizationUnit ou, EnrollmentStatusHistory esh where hhe.hhUniqueId=ahm.hhUniqueId and ovc.caregiverId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid and ovc.ovcId=esh.beneficiaryId";
        return orgUnitQuery;
    }
    /*public static String getHheAdultHouseholdMemberOvcOrganizationUnitQuarterlyStatusTrackerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,Ovc ovc,OrganizationUnit ou, QuarterlyStatusTracker qst where hhe.hhUniqueId=ahm.hhUniqueId and ovc.caregiverId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid and ovc.ovcId=qst.beneficiaryId";
        return orgUnitQuery;
    }*/
    public static String getHheAdultHouseholdMemberOvcOrganizationUnitQuarterlyStatusTrackerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, QuarterlyStatusTracker qst where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=qst.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getEnrollmentStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            if(status==AppConstant.EXITED_WITHOUT_GRADUATION_NUM)
            query=" and (qst.enrollmentStatus=3 or qst.enrollmentStatus =4 or qst.enrollmentStatus =5 or qst.enrollmentStatus =8 or qst.enrollmentStatus =9 or qst.enrollmentStatus =10 or qst.enrollmentStatus =11 or qst.enrollmentStatus =13)";
            else if(status==AppConstant.OVC_SERV_NUM)
            query=" and (qst.enrollmentStatus="+AppConstant.ACTIVE_NUM+" or qst.enrollmentStatus ="+AppConstant.GRADUATED_NUM+")";
            else if(status==AppConstant.CURRENTLY_ENROLLED_NUM)
            query=" and (qst.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or qst.currentEnrollmentStatus ="+AppConstant.REENROLLED_NUM+")";
            else
            query=" and qst.enrollmentStatus="+status;
        }
        return query;
    }
    public static String getHouseholdEnrollmentStatusQuery(int status)
    {
        String query="";
        if(status>0)
        {
            if(status==AppConstant.EXITED_WITHOUT_GRADUATION_NUM)
            query=" and (hhe.currentEnrollmentStatus=3 or hhe.currentEnrollmentStatus =4 or hhe.currentEnrollmentStatus =5 or hhe.currentEnrollmentStatus =8 or hhe.currentEnrollmentStatus =9 or hhe.currentEnrollmentStatus =10 or hhe.currentEnrollmentStatus =11 or hhe.currentEnrollmentStatus =13)";
            else if(status==AppConstant.OVC_SERV_NUM)
            query=" and (hhe.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or hhe.currentEnrollmentStatus ="+AppConstant.GRADUATED_NUM+")";
            else if(status==AppConstant.CURRENTLY_ENROLLED_NUM)
            query=" and (hhe.currentEnrollmentStatus="+AppConstant.ACTIVE_NUM+" or hhe.currentEnrollmentStatus ="+AppConstant.REENROLLED_NUM+")";
            else
            query=" and hhe.currentEnrollmentStatus="+status;
        }
        return query;
    }
    /*public static String getHheAdultHouseholdMemberOvcOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,Ovc ovc,OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and ovc.hhUniqueId=hhe.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }*/
    public static String getHheOvcOrganizationUnitHivRiskAssessmentQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, HivRiskAssessment hra where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=hra.ovcId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service,HivRiskAssessment hra where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId and service.ovcId=hra.ovcId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitReferralQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ReferralService referral where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=referral.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getHheAhmOrganizationUnitReferralQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,OrganizationUnit ou, ReferralService referral where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=referral.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceNutritionAssessmentQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service,NutritionAssessment na where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId and ovc.ovcId=na.ovcId";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceNutritionStatusQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service,NutritionStatus ns where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId and ovc.ovcId=ns.ovcId";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceEnrollmentStatusHistoryQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service, EnrollmentStatusHistory esh where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId and ovc.ovcId=esh.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitServiceQuarterlyStatusTrackerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,Ovc ovc,OrganizationUnit ou, ChildService service, QuarterlyStatusTracker qst where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=service.ovcId and ovc.ovcId=qst.beneficiaryId";
        return orgUnitQuery;
    }
    public String getChildEducationPerformanceAssessmentDateOfAssessmentQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and cepa.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getChildEducationPerformanceAssessmentLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and cepa.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getCareplanAchievementChecklistLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and cpa.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getCaregiverAccessToEmergencyFundLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and caef.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHheLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and hhe.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public static String getHheAssessmentDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and hhe.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public static String getOvcDateOfCurrentHivStatusQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and ovc.dateOfCurrentHivStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public static String getCareAndSupportDateOfAssessmentQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and casc.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public static String getAdultDateOfCurrentHivStatusQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and ahm.dateOfCurrentHivStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getHHVAAssessmentDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and hhe.dateOfAssessment between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHouseholdReferralLastModifiedDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and referral.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getHouseholdCareplanLastModifiedDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and hcp.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getCareAndSupportLastModifiedDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and casc.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getAdultHouseholdMemberLastModifiedDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and ahm.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getAdultHouseholdMemberEnrollmentDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && startDate.trim().length()>0 && !startDate.equalsIgnoreCase("All")) && (endDate !=null && endDate.trim().length()>0 && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and ahm.dateOfEnrollment between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getAdultHouseholdMemberEnrollmentDateOfCurrentStatusQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ahm.dateOfCurrentStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }//
    public String getEnrollmentStatusHistoryDateOfCurrentStatusQuery(String startDate, String endDate)
    {
        String dateOfCurrentStatusQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateOfCurrentStatusQuery=" and esh.dateOfEnrollmentStatus between '"+startDate+"' and '"+endDate+"'";
        }
        return dateOfCurrentStatusQuery;
    }
    public String getQuarterlyStatusTrackerDateOfCurrentStatusQuery(String startDate, String endDate)
    {
        String dateOfCurrentStatusQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateOfCurrentStatusQuery=" and qst.dateOfEnrollmentStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return dateOfCurrentStatusQuery;
    }
    public String getOvcLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ovc.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcDateOfCurrentStatusQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ovc.dateOfCurrentEnrollmentStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcStartDateOfCurrentStatusQuery(String startDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All") && startDate.trim().length()>0))
        {
            ovcDateQuery=" and ovc.dateOfCurrentEnrollmentStatus >= '"+startDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcCasePlanDevelopmentDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ovc.dateCasePlanDeveloped between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcEnrolledOnTreatmentDateQuery(String startDate, String endDate)
    {
        String dateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            dateQuery=" and ovc.dateEnrolledOnTreatment between '"+startDate+"' and '"+endDate+"'";
        }
          return dateQuery;
    }
    public String getChildCasePlanFollowupDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ccp.lastFollowupDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getChildCasePlanLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ccp.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    
    public String getHouseholdCasePlanDevelopmentDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and hhe.dateCasePlanDeveloped between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHouseholdCasePlanFollowupDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and hcp.lastFollowupDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHouseholdCasePlanLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and hcp.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcEnrollmentEndDateQuery(String endDate)
    {
        String ovcDateQuery=" ";
        if((endDate !=null && !endDate.equalsIgnoreCase("All") && endDate.trim().length()>0))
        {
            ovcDateQuery=" and ovc.dateOfEnrollment <='"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcEnrollmentDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            if(startDate.trim().length()>0 && endDate.trim().length()>0)
            ovcDateQuery=" and ovc.dateOfEnrollment between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHouseholdServiceQueryByServiceDomainAndSubType(int serviceType,String serviceCode)
    {
        String query=" ";
        if(serviceType >0)
        {
            if(serviceType==1)
            query=" and (TRIM(hhs.healthServices)='"+serviceCode+"' or hhs.healthServices like '%,"+serviceCode+"%' or hhs.healthServices like '%"+serviceCode+",%' or hhs.healthServices like '%;"+serviceCode+"%' or hhs.healthServices like '%"+serviceCode+";%')";
            else if(serviceType==2)
            query=" and (TRIM(hhs.referralServices)='"+serviceCode+"' or hhs.referralServices like '%,"+serviceCode+"%' or hhs.referralServices like '%"+serviceCode+",%' or hhs.referralServices like '%;"+serviceCode+"%' or hhs.referralServices like '%"+serviceCode+";%')";
            else if(serviceType==3)
            query=" and (TRIM(hhs.safetyServices)='"+serviceCode+"' or hhs.safetyServices like '%,"+serviceCode+"%' or hhs.safetyServices like '%"+serviceCode+",%' or hhs.safetyServices like '%;"+serviceCode+"%' or hhs.safetyServices like '%"+serviceCode+";%')";
            else if(serviceType==4)
            query=" and (TRIM(hhs.schoolServices)='"+serviceCode+"' or hhs.schoolServices like '%,"+serviceCode+"%' or hhs.schoolServices like '%"+serviceCode+",%' or hhs.schoolServices like '%;"+serviceCode+"%' or hhs.schoolServices like '%"+serviceCode+";%')";
            else if(serviceType==5)
            query=" and (TRIM(hhs.stableServices)='"+serviceCode+"' or hhs.stableServices like '%,"+serviceCode+"%' or hhs.stableServices like '%"+serviceCode+",%' or hhs.stableServices like '%;"+serviceCode+"%' or hhs.stableServices like '%"+serviceCode+";%')";
        }
        return query;
    }
    /*public String getHouseholdServiceQueryByServiceDomainAndSubType(int serviceType,String serviceCode)
    {
        String query=" ";
        if(serviceType >0)
        {
            if(serviceType==1 && serviceCode!=null)
            query=" and hhs.healthServices like '%"+serviceCode+"%'";
            else if(serviceType==2)
            query=" and hhs.referralServices like '%"+serviceCode+"%'";
            else if(serviceType==3)
            query=" and hhs.safetyServices like '%"+serviceCode+"%'";
            else if(serviceType==4)
            query=" and hhs.schoolServices like '%"+serviceCode+"%'";
            else if(serviceType==5)
            query=" and hhs.stableServices like '%"+serviceCode+"%'"; 
        }
        return query;
    }*/
    public String getHouseholdServiceQueryByServiceDomain(int serviceType)
    {
        String query=" ";
        if(serviceType >0)
        {
            if(serviceType==1)
            query=" and hhs.healthServices is not null and LENGTH(TRIM(hhs.healthServices)) >0";
            else if(serviceType==2)
            query=" and hhs.referralServices is not null and LENGTH(TRIM(hhs.referralServices)) >0";
            else if(serviceType==3)
            query=" and hhs.safetyServices is not null and LENGTH(TRIM(hhs.safetyServices)) >0";
            else if(serviceType==4)
            query=" and hhs.schoolServices is not null and LENGTH(TRIM(hhs.schoolServices)) >0";
            else if(serviceType==5)
            query=" and hhs.stableServices is not null and LENGTH(TRIM(hhs.stableServices)) >0"; 
        }
          return query;
    }
    public String getOvcServiceQueryByServiceDomainAndSubType(int serviceType,String serviceCode)
    {
        String query=" ";
        if(serviceType >0)
        {
            if(serviceType==1)
            //query=" and service.healthServices like '%"+serviceCode+"%'";
            query=" and (TRIM(service.healthServices)='"+serviceCode+"' or service.healthServices like '%,"+serviceCode+"%' or service.healthServices like '%"+serviceCode+",%' or service.healthServices like '%;"+serviceCode+"%' or service.healthServices like '%"+serviceCode+";%')";
            else if(serviceType==2)
            query=" and (TRIM(service.referralServices)='"+serviceCode+"' or service.referralServices like '%,"+serviceCode+"%' or service.referralServices like '%"+serviceCode+",%' or service.referralServices like '%;"+serviceCode+"%' or service.referralServices like '%"+serviceCode+";%')";
            //query=" and service.referralServices like '%"+serviceCode+"%'";
            else if(serviceType==3)
            query=" and (TRIM(service.safetyServices)='"+serviceCode+"' or service.safetyServices like '%,"+serviceCode+"%' or service.safetyServices like '%"+serviceCode+",%' or service.safetyServices like '%;"+serviceCode+"%' or service.safetyServices like '%"+serviceCode+";%')";
            //query=" and service.safetyServices like '%"+serviceCode+"%'";
            else if(serviceType==4)
            query=" and (TRIM(service.schooledServices)='"+serviceCode+"' or service.schooledServices like '%,"+serviceCode+"%' or service.schooledServices like '%"+serviceCode+",%' or service.schooledServices like '%;"+serviceCode+"%' or service.schooledServices like '%"+serviceCode+";%')";
            //query=" and service.schooledServices like '%"+serviceCode+"%'";
            else if(serviceType==5)
            query=" and (TRIM(service.stableServices)='"+serviceCode+"' or service.stableServices like '%,"+serviceCode+"%' or service.stableServices like '%"+serviceCode+",%' or service.stableServices like '%;"+serviceCode+"%' or service.stableServices like '%"+serviceCode+";%')";
            //query=" and service.stableServices like '%"+serviceCode+"%'"; 
        }
        return query;
    }
    public String getOvcServiceQueryByServiceDomain(int serviceType)
    {
        String query=" ";
        if(serviceType >0)
        {
            if(serviceType==1)
            query=" and service.healthServices is not null and LENGTH(TRIM(service.healthServices)) >0";
            else if(serviceType==2)
            query=" and service.referralServices is not null and LENGTH(TRIM(service.referralServices)) >0";
            else if(serviceType==3)
            query=" and service.safetyServices is not null and LENGTH(TRIM(service.safetyServices)) >0";
            else if(serviceType==4)
            query=" and service.schooledServices is not null and LENGTH(TRIM(service.schooledServices)) >0";
            else if(serviceType==5)
            query=" and service.stableServices is not null and LENGTH(TRIM(service.stableServices)) >0";
            
        }
          return query;
    }
    public String getGBVSexQuery(String sex)
    {
        String sexQuery="";
        if(sex !=null)
        {
            if(sex.equalsIgnoreCase("M") || sex.equalsIgnoreCase("F"))
            sexQuery=" and gbv.sex='"+sex+"'";
        }
        return sexQuery;
    }
    public String getGBVCategory(int gbvCategory)
    {
        String query=" ";
        if(gbvCategory>0)
        {
            query=" and gbv.gbvCategory="+gbvCategory;
            if(gbvCategory==4)
            query=" and (gbv.gbvCategory=2 or gbv.gbvCategory=3 or gbv.gbvCategory=4)";
        }
        return query;
    }
    public String getGBVCurrentAgeCategory(int startAge,int endAge)
    {
        String query="";
        if(startAge == endAge)
        query=" and gbv.currentAge="+startAge;
        else
        query=" and gbv.currentAge between "+startAge+" and "+endAge;
        return query;
    }
    public String getGBVServiceLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and gbvs.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getGBVServiceQuery(String serviceNameOrCode)
    {
        String ovcDateQuery=" ";
        if(serviceNameOrCode !=null && !serviceNameOrCode.equalsIgnoreCase("All"))
        {
            ovcDateQuery=" and gbvs.gbvServices like '%"+serviceNameOrCode+"%'";
        }
          return ovcDateQuery;
    }
    public static String getGBVPEPServiceQuery()
    {
        String gbvServiceQuery=" and gbv.gbvCategory=1 and gbvs.gbvServices like '%PEP%'";
        return gbvServiceQuery;
    }
    public String getGBVServiceDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and gbvs.serviceDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getGBVEnrollmentLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and gbv.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getGBVEnrollmentDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and gbv.dateOfEnrollment between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcServiceLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and service.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getOvcServiceDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All") && startDate.indexOf("-") !=-1) && (endDate !=null && !endDate.equalsIgnoreCase("All") && endDate.indexOf("-") !=-1))
        {
            ovcDateQuery=" and service.serviceDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getNutritionAssessmentLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and na.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getNutritionStatusLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and ns.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getDateOfCurrentNutritionStatusQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and ns.dateOfCurrentNutritionalStatus between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getBeneficiaryStatusUpdateLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and bsu.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHivStatusManagerLastModifiedDateQuery(String startDate, String endDate)
    {
        String ovcDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            ovcDateQuery=" and hsm.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return ovcDateQuery;
    }
    public String getHouseholdServiceLastModifiedDateQuery(String startDate, String endDate)
    {
        String serviceDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            serviceDateQuery=" and hhs.lastModifiedDate between '"+startDate+"' and '"+endDate+"'";
        }
          return serviceDateQuery;
    }
    public String getHouseholdServiceDateQuery(String startDate, String endDate)
    {
        String serviceDateQuery=" ";
        if((startDate !=null && !startDate.equalsIgnoreCase("All")) && (endDate !=null && !endDate.equalsIgnoreCase("All")))
        {
            serviceDateQuery=" and hhs.serviceDate between '"+startDate+"' and '"+endDate+"'";
        }
          return serviceDateQuery;
    }
    public static String getGBVOrganizationUnitQuery()
    {
        String orgUnitQuery=" from GenderBasedViolence gbv, OrganizationUnit ou where gbv.organizationUnitId=ou.uid ";
        return orgUnitQuery;
    }
    public static String getGBVServiceOrganizationUnitQuery()
    {
        String orgUnitQuery=" from GenderBasedViolence gbv, GBVService gbvs, OrganizationUnit ou where gbv.beneficiaryId=gbvs.beneficiaryId and gbv.organizationUnitId=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberRevisedHouseholdAssessmentOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,RevisedHouseholdVulnerabilityAssessment rhva,OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and hhe.hhUniqueId=rhva.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    /*public static String getHheAdultHouseholdMemberOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,OrganizationUnit ou where hhe.hhUniqueId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }*/
    public static String getHheAdultHouseholdMemberOrganizationUnitEnrollmentStatusHistoryQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,OrganizationUnit ou,EnrollmentStatusHistory esh where hhe.hhUniqueId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=esh.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitQuarterlyStatusTrackerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,OrganizationUnit ou,QuarterlyStatusTracker qst where hhe.hhUniqueId=ahm.beneficiaryId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=qst.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitHouseholdServiceEnrollmentStatusHistoryQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm,OrganizationUnit ou, HouseholdService hhs, EnrollmentStatusHistory esh where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hhs.beneficiaryId and ahm.beneficiaryId=esh.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitHouseholdServiceQuarterlyStatusTrackerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe,AdultHouseholdMember ahm, OrganizationUnit ou, HouseholdService hhs, QuarterlyStatusTracker qst where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hhs.beneficiaryId and ahm.beneficiaryId=qst.beneficiaryId";
        return orgUnitQuery;
    }
    public static String getHheAllAdultHouseholdMemberOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, AdultHouseholdMember ahm,OrganizationUnit ou where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheGraduationBenchmarkAchievementOrganizationUnitQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, GraduationBenchmarkAchievement gba,OrganizationUnit ou where hhe.hhUniqueId=gba.hhUniqueId and hhe.organizationUnit=ou.uid ";
        return orgUnitQuery;
    }
    public static String getHheOrganizationUnitAdultHouseholdMemberHouseholdCasePlanQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, AdultHouseholdMember ahm, HouseholdCasePlan hcp where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hcp.hhUniqueId ";
        return orgUnitQuery;
    }
    public static String getHheOrganizationUnitOvcChildCasePlanQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, Ovc ovc, ChildCasePlan ccp where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=ccp.ovcId ";
        return orgUnitQuery;
    }
    public static String getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, AdultHouseholdMember ahm, HouseholdService hhs where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hhs.beneficiaryId ";
        return orgUnitQuery;
    }
    /*public static String getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, AdultHouseholdMember ahm, HouseholdService hhs where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hhs.beneficiaryId ";
        return orgUnitQuery;
    }*/
    public static String getHheAdultHouseholdMemberOrganizationUnitHivStatusManagerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, AdultHouseholdMember ahm, HivStatusManager hsm where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=hsm.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitHivStatusManagerQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, Ovc ovc, HivStatusManager hsm where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=hsm.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getHheAdultHouseholdMemberOrganizationUnitBeneficiaryStatusUpdateQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, AdultHouseholdMember ahm, BeneficiaryStatusUpdate bsu where hhe.hhUniqueId=ahm.hhUniqueId and hhe.organizationUnit=ou.uid and ahm.beneficiaryId=bsu.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getHheOvcOrganizationUnitBeneficiaryStatusUpdateQuery()
    {
        String orgUnitQuery=" from HouseholdEnrollment hhe, OrganizationUnit ou, Ovc ovc, BeneficiaryStatusUpdate bsu where hhe.hhUniqueId=ovc.hhUniqueId and hhe.organizationUnit=ou.uid and ovc.ovcId=bsu.beneficiaryId ";
        return orgUnitQuery;
    }
    public static String getUserActivitySubQuery(String userName,String userAction)
    {
        String query="";
        if((userName !=null && !userName.equalsIgnoreCase("All")))
        query+=" and uat.userName='"+userName+"'";
        if(userAction !=null && !userAction.equalsIgnoreCase("All"))
        query+=" and uat.userAction='"+userAction+"'";
        return query;
    }
    public String getAdditionalOrgUnitQuery(ReportParameterTemplate rpt)
    {
        String additionalOrgUnitQuery="";
        if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
        {
            additionalOrgUnitQuery=getOrganizationUnitQuery(rpt);
        }
        return additionalOrgUnitQuery;
    }
}
