/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.operationsManagement.HivOperationsManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author smomoh
 */
public class ChildEnrollmentDaoImpl implements ChildEnrollmentDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    HivOperationsManager hom=new HivOperationsManager();
    SubQueryGenerator sqg=new SubQueryGenerator();
    String markedForDeleteQuery=" and ovc.markedForDelete=0";
    public int getNumberOfOvcSupportedToAccessARTServicesInReportPeriod(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus,String sex) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            //ART support value is 1 if given transportation support and 2 when not given
            String artSupportQuery=SubQueryGenerator.getARTSupportQuery(1);
            String dateOfArtSupportQuery=SubQueryGenerator.getDateOfCareAndSupportAssessmentQuery(startDate,endDate);
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheAdultHouseholdMemberOvcOrganizationUnitCareAndSupportQuery()+sqg.getOvcSexQuery(sex)+artSupportQuery+dateOfArtSupportQuery+currentEnrollmentQuery+ageQuery+additionalOrgUnitQuery+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcSupportedToAccessARTServicesInReportPeriod(ReportParameterTemplate rpt,String startDate,String endDate,int enrollmentStatus,String sex) throws Exception
    {
        List hheList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            String artSupportQuery=SubQueryGenerator.getARTSupportQuery(1);
            String dateOfArtSupportQuery=SubQueryGenerator.getDateOfCareAndSupportAssessmentQuery(startDate,endDate);
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheAdultHouseholdMemberOvcOrganizationUnitCareAndSupportQuery()+sqg.getOvcSexQuery(sex)+artSupportQuery+dateOfArtSupportQuery+currentEnrollmentQuery+ageQuery+additionalOrgUnitQuery+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hheList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return hheList;
    }
    public int getNumberOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus,int hivStatus,String sex) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            String hivStatusQuery=sqg.getOvcHivStatusQuery(hivStatus);
            String dateOfHivStatusQuery=SubQueryGenerator.getOvcDateOfCurrentHivStatusQuery(startDate,endDate);
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+hivStatusQuery+dateOfHivStatusQuery+currentEnrollmentQuery+ageQuery+additionalOrgUnitQuery+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcTestedAndRecievedResultInReportPeriodByEnrollmentStatusAndHivStatus(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus,int hivStatus,String sex) throws Exception
    {
        List hheList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            String hivStatusQuery=sqg.getOvcHivStatusQuery(hivStatus);
            String dateOfHivStatusQuery=SubQueryGenerator.getOvcDateOfCurrentHivStatusQuery(startDate,endDate);
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+hivStatusQuery+dateOfHivStatusQuery+currentEnrollmentQuery+ageQuery+additionalOrgUnitQuery+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hheList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return hheList;
    }
    public int getNumberOfOvcEnrolledOnTreatment(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus,String sex) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+currentEnrollmentQuery+ageQuery+sqg.getOvcEnrolledOnTreatmentQuery(enrolledOnTreatmentValue)+additionalOrgUnitQuery+sqg.getOvcEnrolledOnTreatmentDateQuery(startDate,endDate)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcEnrolledOnTreatment(ReportParameterTemplate rpt,String startDate,String endDate,int enrolledOnTreatmentValue,int enrollmentStatus,String sex) throws Exception
    {
        List hheList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            String currentEnrollmentQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+currentEnrollmentQuery+ageQuery+sqg.getOvcEnrolledOnTreatmentQuery(enrolledOnTreatmentValue)+additionalOrgUnitQuery+sqg.getOvcEnrolledOnTreatmentDateQuery(startDate,endDate)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hheList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return hheList;
    }
    public int getNumberOfOvcPerCaregiver(String caregiverId) throws Exception
    {
        int count=0;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count (distinct ovc.ovcId) from Ovc ovc where ovc.caregiverId=:id";
            System.err.println("query is "+query);
            List list = session.createQuery(query).setString("id", caregiverId).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
            ex.printStackTrace();
        }
        return count;
    }
    public List getRecordsWithPositiveHivStatusAtBaselineButOtherStatusCurrently() throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from Ovc ovc where ovc.baselineHivStatus="+AppConstant.HIV_POSITIVE_NUM+" and ovc.currentHivStatus !="+AppConstant.HIV_POSITIVE_NUM;
            System.err.println("query is "+query);
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
        }
        catch(Exception ex)
        {
            closeSession(session);
            ex.printStackTrace();
        }
        return list;
    }
    public List searchOvcByPartOfName(ReportParameterTemplate rpt,String partOfName) throws Exception
    {
        List hheList=new ArrayList();
        try
        {
            if(partOfName !=null)
            {
                SubQueryGenerator sqg=new SubQueryGenerator();
                String additionalOrgUnitQuery="";
                if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
                {
                    additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
                }
                
                partOfName=partOfName.trim();
                if(partOfName.indexOf("'") !=-1)
                partOfName=partOfName.substring(0, partOfName.indexOf("'"));
                if(partOfName.length() <1)
                {
                    hheList=new ArrayList();
                }
                else
                {
                    partOfName=partOfName.toUpperCase();
                    String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+" and (UPPER(ovc.firstName) like '"+partOfName+"%' or UPPER(ovc.surname) like '"+partOfName+"%')"+markedForDeleteQuery+" order by ovc.firstName";
                    
                    System.err.println("query is "+query);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    List list = session.createQuery(query).list();
                    tx.commit();
                    closeSession(session);
                    if(list !=null && !list.isEmpty())
                    {
                        for(Object obj:list)
                        {
                            Object[] objArray=(Object[])obj;
                            Ovc ovc=(Ovc)objArray[1];
                            hheList.add(ovc);
                        }
                    }
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
             ex.printStackTrace();
            //throw new Exception(ex);
         }
        return hheList;
    }
    public List getListOfOvcWithCasePlanRecords() throws Exception
    {
        List list=null;
        try
        {
            String query="from Ovc ovc, ChildCasePlan ccp where ovc.ovcId=ccp.ovcId"+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public List getListOfOvcWithZeroCasePlanRecords() throws Exception
    {
        List list=null;
        try
        {
            String query="from Ovc ovc, ChildCasePlan ccp where ovc.ovcId=ccp.ovcId and ovc.childHasCasePlan=0";
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public int updateOvcWithZeroCasePlanRecords() throws Exception
    {
        int count=0;
        try
        {
            List list=getListOfOvcWithCasePlanRecords();//getListOfOvcWithZeroCasePlanRecords();
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[0];
                    //ChildCasePlan ccp=(ChildCasePlan)objArray[1];
                    //ovc.setChildHasCasePlan(ccp.getChildHasCasePlan());
                    //ovc.setDateCasePlanDeveloped(ccp.getDateCasePlanDeveloped());
                    ovc.setLastModifiedDate(DateManager.getCurrentDateInstance());
                    updateOvc(ovc,false,false);
                    count++;
                    System.err.println(count+" Ovc case plan records updated");
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public int getNumberOfOvcExitedWithoutGraduation(ReportParameterTemplate rpt,String startDate,String endDate,String sex) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            //String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheAdultHouseholdMemberOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+additionalOrgUnitQuery+sqg.getOvcDateOfCurrentStatusQuery(startDate,endDate)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(AppConstant.EXITED_WITHOUT_GRADUATION_NUM)+markedForDeleteQuery;
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheAdultHouseholdMemberOvcOrganizationUnitQuarterlyStatusTrackerQuery()+sqg.getOvcSexQuery(sex)+additionalOrgUnitQuery+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate,endDate)+SubQueryGenerator.getEnrollmentStatusQuery(AppConstant.EXITED_WITHOUT_GRADUATION_NUM)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcByLevel4OrganizationUnit(String level4OuId) throws Exception
    {
        List mainList=new ArrayList();
        try
        {     
            //.getHheOvcOrganizationUnitQuery()
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+" and ou.ouPath like '%"+level4OuId+"%'"+markedForDeleteQuery+" order by ovc.dateOfEnrollment";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[1]); 
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getListOfOvcByLevel4OrganizationUnitByEnrollmentStatus(String level4OuId,int enrollmentStatus) throws Exception
    {
        List mainList=new ArrayList();
        try
        {     
            String enrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+" and ou.ouPath like '%"+level4OuId+"%'"+enrollmentStatusQuery+markedForDeleteQuery+" order by ovc.dateOfEnrollment";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[1]); 
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public int getNumberOfOvcWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue,String sex) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            String ageQuery=sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge());
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+sqg.getOvcSexQuery(sex)+ageQuery+sqg.getOvcWithCasePlanQuery(casePlanValue)+additionalOrgUnitQuery+sqg.getOvcCasePlanDevelopmentDateQuery(startDate,endDate)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcWithCasePlan(ReportParameterTemplate rpt,String startDate,String endDate,int casePlanValue,String sex) throws Exception
    {
        List hheList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+SubQueryGenerator.getOvcSexQuery(sex)+sqg.getOvcWithCasePlanQuery(casePlanValue)+additionalOrgUnitQuery+sqg.getOvcCasePlanDevelopmentDateQuery(startDate,endDate)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hheList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return hheList;
    }
    public List getListOfOvcByFacilityId(String facilityId) throws Exception
    {
        List list=null;
        try
        {
            String query="from Ovc ovc where ovc.facilityId=:id"+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).setString("id", facilityId).list();
            tx.commit();
            closeSession(session);
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public int getNumberOfOvcEnrollmentByVulnerabilityStatus(ReportParameterTemplate rpt,String sex,String vulnerabilityStatusCodeOrName) throws Exception
    {
        int numberOfOvc=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count (distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getVulnerabilityStatusQuery(vulnerabilityStatusCodeOrName)+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(rpt.getEnrollmentStatus())+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                numberOfOvc=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return numberOfOvc;
    }
    public List getListOfOvcEnrollmentByVulnerabilityStatus(ReportParameterTemplate rpt,String sex,String vulnerabilityStatusCodeOrName) throws Exception
    {
        List ovcList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getVulnerabilityStatusQuery(vulnerabilityStatusCodeOrName)+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(rpt.getEnrollmentStatus())+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[1];
                    ovcList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovcList;
    }
    public int getOvcSchoolEnrollmentData(ReportParameterTemplate rpt,String sex) throws Exception
    {
        int numberOfOvc=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count (distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getOvcCurrentSchoolStatusQuery(rpt.getSchoolStatus())+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(rpt.getEnrollmentStatus())+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                numberOfOvc=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return numberOfOvc;
    }
    public List getOvcSchoolEnrollmentList(ReportParameterTemplate rpt,String sex) throws Exception
    {
        List ovcList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getOvcCurrentSchoolStatusQuery(rpt.getSchoolStatus())+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(rpt.getEnrollmentStatus())+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[1];
                    ovcList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovcList;
    }
    public int getOvcBirthCertificateData(ReportParameterTemplate rpt,int enrollmentStatus,String sex) throws Exception
    {
        int numberOfOvc=0;
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query="select count (distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getOvcCurrentBirthCertificateQuery(rpt.getBirthCertificateValue())+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                numberOfOvc=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return numberOfOvc;
    }
    public List getOvcBirthCertificateList(ReportParameterTemplate rpt,int enrollmentStatus,String sex) throws Exception
    {
        List ovcList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcCurrentAgeQuery(rpt.getStartAge(), rpt.getEndAge())+SubQueryGenerator.getOvcCurrentBirthCertificateQuery(rpt.getBirthCertificateValue())+SubQueryGenerator.getOvcSexQuery(sex)+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus)+markedForDeleteQuery;
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[1];
                    ovcList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovcList;
    }
    public List getListOfOvcWithDifferentPrimCaregiverId() throws Exception
    {
        List list=null;
        try
        {
            String query="from Ovc ovc where ovc.hhUniqueId !=ovc.caregiverId"+markedForDeleteQuery;
            System.err.println("query in getListOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public int updatePrimaryCaregiverIdWithHhUniqueId() throws Exception
    {
        int count=0;
        List ovcList=getListOfOvcWithDifferentPrimCaregiverId();
        if(ovcList !=null)
        {
            for(Object obj:ovcList)
            {
                count++;
                Ovc ovc=(Ovc)obj;
                ovc.setCaregiverId(ovc.getHhUniqueId());
                updateOvc(ovc, false, false);
            }
        }
       return count; 
    }
    public int getNoOfOvcByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,int onTreatment) throws Exception
    {
        int count=0;
        try
        {
            SubQueryGenerator rqg=new SubQueryGenerator();
            //String organizationUnitQueryCriteria=rqg.getOrganizationUnitQuery(rpt);
            String ageQuery=rqg.getOvcCurrentAgeQuery(startAge,endAge);
            String dateQuery=rqg.getOvcEnrollmentDateQuery(startDate, endDate);
            String queryPart= additionalQuery+ageQuery+SubQueryGenerator.getOvcSexQuery(sex)+dateQuery;
            
            String hivTreatmentQuery="";
            if(hivStatus==AppConstant.HIV_POSITIVE_NUM && onTreatment==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
            hivTreatmentQuery=SubQueryGenerator.getOvcHivPositiveOnTreatmentQuery();
            else if(hivStatus==AppConstant.HIV_POSITIVE_NUM && onTreatment==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM)
            hivTreatmentQuery=SubQueryGenerator.getOvcHivPositiveNotOnTreatmentQuery();
            
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+queryPart+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus)+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+hivTreatmentQuery+markedForDeleteQuery;
            System.err.println("query in getNoOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public int getNoOfOvcByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus) throws Exception
    {
        int count=0;
        try
        {
            //SubQueryGenerator.getHheOvcOrganizationUnitQuery() getHheAdultHouseholdMemberOvcOrganizationUnitQuery()
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus)+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+additionalQuery+markedForDeleteQuery;
            System.err.println("query in getNoOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus) throws Exception
    {
        List ovcList=new ArrayList();
        try
        {
            //getHheOvcOrganizationUnitQuery() getHheAdultHouseholdMemberOvcOrganizationUnitQuery()
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus)+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+additionalQuery+markedForDeleteQuery;
            System.err.println("query in getListOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[1];
                    ovcList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovcList;
    }
    public int getNoOfOvcByEnrollmentStatus(String additionalQuery,int currentEnrollmentStatus) throws Exception
    {
        int count=0;
        try
        {
            //"+getHheAdultHouseholdMemberOvcOrganizationUnitQuery()
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus)+additionalQuery+markedForDeleteQuery;
            System.err.println("query in getNoOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                count=Integer.parseInt(list.get(0).toString());
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return count;
    }
    public List getListOfOvcByEnrollmentStatus(String additionalQuery,int currentEnrollmentStatus) throws Exception
    {
        List ovcList=new ArrayList();
        try
        {
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus)+additionalQuery+markedForDeleteQuery;
            System.err.println("query in getListOfOvcByEnrollmentStatus is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    Ovc ovc=(Ovc)objArray[1];
                    ovcList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovcList;
    }
    public List getOvcRecords(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,boolean onTreatment) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getOvcCurrentAgeQuery(startAge,endAge);
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getOvcEnrollmentDateQuery(startDate, endDate)+markedForDeleteQuery+" order by ovc.firstName, ovc.surname";
            System.err.println("query is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                AdultHouseholdMember ahm=null;
                Ovc ovc=null;
                HouseholdEnrollment hhe=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hhe=(HouseholdEnrollment)objArray[0];
                    //ahm=(AdultHouseholdMember)objArray[1];
                    ovc=(Ovc)objArray[1];
                    ovc.setHhe(hhe);
                    //ovc.setPrimaryCaregiver(ahm);
                    mainList.add(ovc);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getOvcRecordsForExport(ReportParameterTemplate rpt) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getOvcRecordsMarkedForDelete(ReportParameterTemplate rpt) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getOvcLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate())+" and ovc.markedForDelete=1";
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[1]);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getAllOvcRecords() throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from Ovc ovc where ovc.ovcId is not null"+markedForDeleteQuery).list();
            tx.commit();
            closeSession(session);
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public List getOvcPerHousehold(String hhUniqueId) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from Ovc ovc where ovc.hhUniqueId=:id"+markedForDeleteQuery).setString("id", hhUniqueId).list();
            tx.commit();
            closeSession(session);
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return list;
    }
    public Ovc getOvc(String ovcId) throws Exception
    {
        Ovc ovc=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from Ovc ovc where ovc.ovcId=:id").setString("id", ovcId).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                ovc=(Ovc)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovc;
    }
    public Ovc getOvcByName(String hhUniqueId,String firstName,String surname) throws Exception
    {
        Ovc ovc=null;
        try
        {
            if(hhUniqueId !=null)
            hhUniqueId=hhUniqueId.trim();
            if(firstName !=null)
            firstName=firstName.trim().toUpperCase();
            if(surname !=null)
            surname=surname.trim().toUpperCase();
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from Ovc ovc where ovc.hhUniqueId=:id and ((UPPER(ovc.firstName)=:fn and UPPER(ovc.surname)=:sn) or (UPPER(ovc.firstName)=:sn and UPPER(ovc.surname)=:fn))").setString("id", hhUniqueId).setString("fn", firstName).setString("sn", surname).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                ovc=(Ovc)list.get(0);
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ovc;
    }
    public void saveOvcCurrentHivStatus(Ovc ovc) throws Exception
    {
        /*HivStatusManager hsm=new HivStatusManager();
        if(ovc !=null)
        {
            hsm.setBeneficiaryId(ovc.getOvcId());
            hsm.setBeneficiaryType(ovc.getBeneficiaryType());
            hsm.setDateCreated(DateManager.getCurrentDateInstance());
            hsm.setDateOfNewStatus(ovc.getDateOfCurrentHivStatus());
            hsm.setEnrolledOnTreatment(ovc.getEnrolledOnTreatment());
            hsm.setFacilityId(ovc.getHivTreatmentFacilityId());
            hsm.setLastModifiedDate(ovc.getLastModifiedDate());
            hsm.setNewHivStatus(ovc.getCurrentHivStatus());
            hsm.setPointOfUpdateValue(ovc.getPointOfUpdate());
            hsm.setRecordedBy(ovc.getRecordedBy());
            HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
            hsmdao.saveHivStatusManager(hsm,true);
        }*/
    }
    public void saveOvcBaselineHivStatus(Ovc ovc) throws Exception
    {
        /*HivStatusManager hsm=new HivStatusManager();
        if(ovc !=null)
        {
            hsm.setBeneficiaryId(ovc.getOvcId());
            hsm.setBeneficiaryType(ovc.getBeneficiaryType());
            hsm.setDateCreated(ovc.getDateCreated());
            hsm.setDateOfNewStatus(ovc.getDateOfEnrollment());
            hsm.setEnrolledOnTreatment(ovc.getEnrolledOnTreatment());
            hsm.setFacilityId(ovc.getHivTreatmentFacilityId());
            hsm.setLastModifiedDate(ovc.getLastModifiedDate());
            hsm.setNewHivStatus(ovc.getBaselineHivStatus());
            hsm.setPointOfUpdateValue(ovc.getPointOfUpdate());
            hsm.setRecordedBy(ovc.getRecordedBy());
            HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
            hsmdao.saveHivStatusManager(hsm,true);
        }*/
    }
    public void updateOvcOnly(Ovc ovc) throws Exception
    {
        try
        {
            if(ovc !=null)
            {
                Ovc ovc2=getOvc(ovc.getOvcId());
                if(ovc2 !=null)
                {
                    if(ovc2.getDateOfCurrentBirthRegStatus().after(ovc.getDateOfCurrentBirthRegStatus()))
                    {
                        ovc.setCurrentBirthRegistrationStatus(ovc2.getCurrentBirthRegistrationStatus());
                        ovc.setDateOfCurrentBirthRegStatus(ovc2.getDateOfCurrentBirthRegStatus());
                        ovc=(Ovc)hom.processBeneficiaryHivStatus(ovc);
                        
                    }
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(ovc);
                    tx.commit();
                    closeSession(session);
                    System.err.println("Ovc with Id "+ovc.getOvcId()+" saved");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void saveOvc(Ovc ovc,boolean saveHiv,boolean saveBirthRegistration) throws Exception
    {
        try
        {
            //System.err.println(" ovc.getDateOfEnrollment() in SaveOvc is "+ovc.getDateOfEnrollment());
            if(ovc !=null && getOvc(ovc.getOvcId())==null)
            {
                //System.err.println("Ovc with Id "+ovc.getOvcId()+" about to be saved");
                ovc=getCleanedOvc(ovc);
                System.err.println(" ovc.getDateOfEnrollment() 2 in SaveOvc is "+ovc.getDateOfEnrollment());
                ovc=(Ovc)hom.processBeneficiaryHivStatus(ovc);
                //System.err.println(" ovc.getDateOfEnrollment() 3 in SaveOvc is "+ovc.getDateOfEnrollment());
                //System.err.println("Ovc with Id "+ovc.getOvcId()+" about to be saved after cleanup");
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.save(ovc);
                tx.commit();
                closeSession(session);
                //saveOvcHivStatus(ovc);
                System.err.println("Ovc with Id "+ovc.getOvcId()+" saved");
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public Ovc getCleanedOvc(Ovc ovc) throws Exception
    {
        //AppManager appManager=new AppManager();
        AppUtility appUtil=new AppUtility();
        if(ovc.getHivTreatmentFacilityId() !=null && ovc.getHivTreatmentFacilityId().equalsIgnoreCase("select"))
        ovc.setHivTreatmentFacilityId(null);
        ovc=getPreparedOvc(ovc);
        if(ovc.getDateOfCurrentHivStatus()==null)
        {
            ovc.setCurrentHivStatus(ovc.getBaselineHivStatus());
            ovc.setDateOfCurrentHivStatus(ovc.getDateOfEnrollment());
        }
        if(ovc.getDateOfCurrentBirthRegStatus()==null)
        {
            ovc.setCurrentBirthRegistrationStatus(ovc.getBaselineBirthRegistrationStatus());
            ovc.setDateOfCurrentBirthRegStatus(ovc.getDateOfEnrollment());
        }
        if(ovc.getDateCasePlanDeveloped()==null)
        ovc.setDateCasePlanDeveloped(ovc.getDateOfEnrollment());
        
        //else if(ovc.getDateOfCurrentSchoolStatus().)
        if(ovc.getCurrentBirthRegistrationStatus()==0)
        ovc.setCurrentBirthRegistrationStatus(ovc.getBaselineBirthRegistrationStatus());
        ovc.setFirstName(appUtil.changeFirstLetterToUpperCase(ovc.getFirstName()));
        ovc.setSurname(appUtil.changeFirstLetterToUpperCase(ovc.getSurname()));
        //if(ovc.getCurrentHivStatusCode()==0)
        return ovc;
    }
    public void updateOvc(Ovc ovc,boolean saveHiv,boolean saveBirthRegistration) throws Exception
    {
        try
        {
            System.err.println("Inside updateOvc");
            Ovc ovc2=getOvc(ovc.getOvcId());
            if(ovc !=null && ovc2 !=null)
            {
                System.err.println(" ovc.getDateOfEnrollment() in updateOvc is "+ovc.getDateOfEnrollment());
                if(ovc2.getLastModifiedDate().before(ovc.getLastModifiedDate()) || ovc2.getLastModifiedDate().equals(ovc.getLastModifiedDate()))
                {
                    System.err.println("Inside updateOvc, ovc2.getLastModifiedDate().before(ovc.getLastModifiedDate())");
                    
                    ovc=getCleanedOvc(ovc);
                    //System.err.println(" ovc.getDateOfEnrollment() 2 in updateOvc is "+ovc.getDateOfEnrollment());
                    ovc=getOvcWithUpdatedCurrentParameters(ovc,ovc2);
                    //System.err.println(" ovc.getDateOfEnrollment()3 in updateOvc is "+ovc.getDateOfEnrollment());
                    if(ovc.getDateOfCurrentHivStatus().before(ovc2.getDateOfCurrentHivStatus()))
                    {
                        ovc.setCurrentHivStatus(ovc2.getCurrentHivStatus());
                        ovc.setDateOfCurrentHivStatus(ovc2.getDateOfCurrentHivStatus());
                        ovc.setEnrolledOnTreatment(ovc2.getEnrolledOnTreatment());
                        ovc.setHivTreatmentFacilityId(ovc2.getHivTreatmentFacilityId()); 
                    }
                    updateOvcOnly(ovc);
                    /*ovc=(Ovc)hom.processBeneficiaryHivStatus(ovc);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(ovc);
                    tx.commit();*/
                    closeSession(session);
                    //System.err.println("Inside updateOvc, ovc.getCurrentHivStatusCode() "+ovc.getCurrentHivStatusCode());
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void markForDelete(Ovc ovc) throws Exception
    {
        try
        {
            Ovc ovc2=getOvc(ovc.getOvcId());
            if(ovc2 !=null)
            {
                ovc2.setMarkedForDelete(AppConstant.MARKEDFORDELETE);
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.update(ovc2);
                tx.commit();
                closeSession(session);
                markOtherChildRecordsForDelete(ovc.getOvcId());
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void deleteOvc(Ovc ovc) throws Exception
    {
        try
        {
            if(ovc !=null && this.getOvc(ovc.getOvcId())!=null)
            {
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.delete(ovc);
                tx.commit();
                closeSession(session);
                deleteServices(ovc.getOvcId());
                
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void markOtherChildRecordsForDelete(String ovcId) throws Exception
    {
        ChildServiceDao sdao=new ChildServiceDaoImpl();
        HouseholdReferralDao rdao=new HouseholdReferralDaoImpl();
        HivRiskAssessmentDao hdao=new HivRiskAssessmentDaoImpl();
        sdao.markChildServicesForDelete(ovcId);
        rdao.markChildServicesForDelete(ovcId);
        hdao.markRiskAssessmentRecordsForDelete(ovcId);
        //deleteBeneficiaryHivRecord(ovcId);
    }
    public void deleteServices(String ovcId) throws Exception
    {
        ChildServiceDao sdao=new ChildServiceDaoImpl();
        HouseholdReferralDao rdao=new HouseholdReferralDaoImpl();
        HivRiskAssessmentDao hdao=new HivRiskAssessmentDaoImpl();
        sdao.deleteServicesPerChild(ovcId);
        rdao.deleteServicesPerChild(ovcId);
        hdao.deleteRiskAssessmentRecordsPerChild(ovcId);
        deleteBeneficiaryHivRecord(ovcId);
    }
    private Ovc getOvcWithCurrentAge(Ovc ovc)
    {
        if(ovc !=null)
        {
            int currentAge=AppManager.getCurrentAge(DateManager.convertDateToString(ovc.getDateOfEnrollment(),DateManager.DB_DATE_FORMAT), ovc.getAgeAtBaseline());
            Date ddateOfBirth=DateManager.getPreviousDate(ovc.getDateOfEnrollment(), ovc.getAgeAtBaseline());
            ovc.setCurrentAge(currentAge);
            ovc.setDateOfBirth(ddateOfBirth);
        }
        return ovc;
    }
    public Ovc getPreparedOvc(Ovc ovc)
    {
        if(ovc !=null)
        {
            /*if(ovc.getAgeAtBaseline()==0)
            {
                ovc.setCurrentAge(ovc.getAgeAtBaseline());
                ovc.setCurrentAgeUnit(ovc.getAgeUnitAtBaseline());
                
            }*/
            if(ovc.getCurrentEnrollmentStatus()==0)
            {
                ovc.setCurrentEnrollmentStatus(AppConstant.ACTIVE_NUM);
                ovc.setDateOfCurrentStatus(ovc.getDateOfEnrollment());
            }   
        }
        return ovc;
    }
    private Ovc getOvcWithUpdatedCurrentParameters(Ovc ovc,Ovc ovc2)
    {
        //System.err.println("Inseide ovcdao.getOvcWithUpdatedCurrentParameters(Ovc ovc,Ovc ovc2)");
        if(ovc !=null)
        {
            if(ovc.getHivTreatmentFacilityId() !=null && ovc.getHivTreatmentFacilityId().equalsIgnoreCase("select"))
            ovc.setHivTreatmentFacilityId(null);
            ovc=getPreparedOvc(ovc);
            if(ovc.getBaselineHivStatus()==0)
            {//this ensures that HIV status record is never set to zero
                ovc.setBaselineHivStatus(ovc2.getBaselineHivStatus());
            }
            if(ovc.getCurrentHivStatus()==0 || ovc.getDateOfCurrentHivStatus()==null)
            {
                if(ovc2.getCurrentHivStatus()>0)
                ovc.setCurrentHivStatus(ovc2.getCurrentHivStatus());
                else
                ovc.setCurrentHivStatus(ovc2.getBaselineHivStatus());
                ovc.setDateOfCurrentHivStatus(ovc2.getDateOfCurrentHivStatus());
                if(ovc.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                ovc.setEnrolledOnTreatment(ovc2.getEnrolledOnTreatment());
                //System.err.println("ovc.getDateOfCurrentHivStatus() is "+ovc.getDateOfCurrentHivStatus()+" "+ovc.getCurrentHivStatusCode());
            }
            if((ovc.getDateOfCurrentHivStatus().before(ovc2.getDateOfCurrentHivStatus())))
            {
                ovc.setCurrentHivStatus(ovc2.getCurrentHivStatus());
                ovc.setDateOfCurrentStatus(ovc2.getDateOfCurrentHivStatus());
                if(ovc.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
                {
                    ovc.setEnrolledOnTreatment(ovc2.getEnrolledOnTreatment());
                    if(ovc.getEnrolledOnTreatment()==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
                    ovc.setHivTreatmentFacilityId(ovc2.getHivTreatmentFacilityId());
                    else
                    ovc.setHivTreatmentFacilityId(null);
                }
            }
            if(ovc.getCurrentHivStatus()==AppConstant.HIV_POSITIVE_NUM)
            {
                if(ovc.getEnrolledOnTreatment()==0)
                {
                    ovc.setEnrolledOnTreatment(ovc2.getEnrolledOnTreatment());
                    ovc.setHivTreatmentFacilityId(ovc2.getHivTreatmentFacilityId());
                }
            }
            
            if(ovc.getDateOfCurrentBirthRegStatus()==null)
            {
                ovc.setCurrentBirthRegistrationStatus(ovc2.getCurrentBirthRegistrationStatus());
                ovc.setDateOfCurrentBirthRegStatus(ovc2.getDateOfCurrentBirthRegStatus());
                //System.err.println("ovc.getDateOfCurrentHivStatus()3 is "+ovc.getDateOfCurrentHivStatus()+" "+ovc.getCurrentHivStatusCode());
            }
            else if(DateManager.compareDates(ovc.getDateOfCurrentBirthRegStatus(),ovc2.getDateOfCurrentBirthRegStatus()))
            {
                ovc.setCurrentBirthRegistrationStatus(ovc2.getCurrentBirthRegistrationStatus());
                ovc.setDateOfCurrentBirthRegStatus(ovc2.getDateOfCurrentBirthRegStatus());
                //System.err.println("ovc.getDateOfCurrentHivStatus()4 is "+ovc.getDateOfCurrentHivStatus()+" "+ovc.getCurrentHivStatusCode());
            }
            //check if child case plan information was set
            if(ovc.getChildHasCasePlan()==0)
            {
                ovc.setChildHasCasePlan(ovc2.getChildHasCasePlan());
                ovc.setDateCasePlanDeveloped(ovc2.getDateCasePlanDeveloped());
            }
            
            if(ovc.getCurrentEnrollmentStatus()==0)
            ovc.setCurrentEnrollmentStatus(ovc2.getCurrentEnrollmentStatus());
            if(ovc.getCurrentSchoolStatus()==0)
            ovc.setCurrentSchoolStatus(ovc2.getCurrentSchoolStatus());
            if(ovc.getCurrentBirthRegistrationStatus()==0)
            ovc.setCurrentBirthRegistrationStatus(ovc2.getCurrentBirthRegistrationStatus());
            
            if(ovc.getVulnerabilityStatusCode()==null || ovc.getVulnerabilityStatusCode().trim().length() <2)
            {
                ovc.setVulnerabilityStatusCode(ovc2.getVulnerabilityStatusCode());
            }
            if(ovc.getCurrentSchoolStatus()==0)
            {
                ovc.setCurrentSchoolStatus(ovc.getBaselineSchoolStatus());
                ovc.setDateOfCurrentSchoolStatus(ovc.getDateOfEnrollment());
            }
            else if(ovc2.getDateOfCurrentSchoolStatus().after(ovc.getDateOfCurrentSchoolStatus()))
            {
                ovc.setCurrentSchoolStatus(ovc2.getCurrentSchoolStatus());
            }
        }
        return ovc;
    }
    public void changeOvcHhUniqueId(String oldHhUniqueId, String newHhUniqueId) throws Exception
    {
        List list=this.getOvcPerHousehold(oldHhUniqueId);
        if(list !=null)
        {
            if(list !=null)
            {
                for(Object obj:list)
                {
                    Ovc ovc=(Ovc)obj;
                    ovc.setHhUniqueId(newHhUniqueId);
                    ovc.setCaregiverId(newHhUniqueId);
                    updateOvc(ovc, false, false);
                    System.err.println("HhUnique Id in OVCDaoImpl changed from "+oldHhUniqueId+" to "+newHhUniqueId);
                }
            }
        }
    }
    public void changeOvcId(String oldOvcId, String newOvcId) throws Exception
    {
        Ovc ovc=this.getOvc(oldOvcId);
        if(ovc !=null)
        {
            Ovc ovcToKeep=getOvc(newOvcId);
            if(ovcToKeep ==null)
            {
                System.err.println("ovcToKeep is null");
                ovc.setOvcId(newOvcId);
                saveOvc(ovc,false,false);
                //transfer the services and assessments for the OVC to delete to the OVC to keep
                                
                if(getOvc(newOvcId) !=null)
                {
                    /*OvcServiceDao sdao=new OvcServiceDaoImpl();
                    ReferralServiceDao rsdao=new ReferralServiceDaoImpl();
                    HivRiskAssessmentDao hdao=new HivRiskAssessmentDaoImpl();
                    GenderBasedViolenceDao gbvdao=new GenderBasedViolenceDaoImpl();
                    HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
                    EnrollmentStatusHistoryDao essdao=new EnrollmentStatusHistoryDaoImpl();
                    QuarterlyStatusTrackerDao qssdao=new QuarterlyStatusTrackerDaoImpl();
                
                    sdao.changeOvcIdInService(oldOvcId, newOvcId);
                    rsdao.changeBeneficiaryIdInReferralService(oldOvcId, newOvcId);
                    hdao.changeOvcIdInHivRiskAssessmentRecords(oldOvcId, newOvcId);
                    gbvdao.changeBeneficiaryIdInGBVEnrollment(oldOvcId, newOvcId);
                    hsmdao.changeBeneficiaryIdInHivStatusManager(oldOvcId, newOvcId);
                    essdao.changeBeneficiaryIdInEnrollmentStatusHistory(oldOvcId, newOvcId);
                    qssdao.changeBeneficiaryIdInQuarterlyStatusTracker(oldOvcId, newOvcId);
                                
                    ovc.setOvcId(oldOvcId);
                    deleteOvc(ovc);
                    System.err.println("ovc with old ovcId "+ovc.getOvcId()+" deleted");*/
                }
                System.err.println("Ovc Id in OVCDaoImpl changed from --"+oldOvcId+"-- to "+newOvcId);
            }
        }
    }
    public void mergeOvc(String ovcIdToDelete, String ovcIdToKeep) throws Exception
    {
        Ovc ovc=this.getOvc(ovcIdToDelete);
        if(ovc !=null)
        {
            Ovc ovcToKeep=getOvc(ovcIdToKeep);
            if(ovcToKeep !=null)
            {
                //transfer the services and assessments for the OVC to delete to the OVC to keep
                /*OvcServiceDao sdao=new OvcServiceDaoImpl();
                ReferralServiceDao rsdao=new ReferralServiceDaoImpl();
                HivRiskAssessmentDao hdao=new HivRiskAssessmentDaoImpl();
                GenderBasedViolenceDao gbvdao=new GenderBasedViolenceDaoImpl();
                HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
                sdao.changeOvcIdInService(ovcIdToDelete, ovcIdToKeep);
                rsdao.changeBeneficiaryIdInReferralService(ovcIdToDelete, ovcIdToKeep);
                hdao.changeOvcIdInHivRiskAssessmentRecords(ovcIdToDelete, ovcIdToKeep);
                gbvdao.changeBeneficiaryIdInGBVEnrollment(ovcIdToDelete, ovcIdToKeep);
                hsmdao.changeBeneficiaryIdInHivStatusManager(ovcIdToDelete, ovcIdToKeep);*/
                deleteOvc(ovc);
                System.err.println("Ovc Id in OVCDaoImpl changed from "+ovcIdToDelete+" to "+ovcIdToKeep);
            }
        }
    }
    private void deleteBeneficiaryHivRecord(String ovcId) throws Exception
    {
        //HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
        //hsmdao.deleteHivStatusManager(ovcId);
    }
    public void closeSession(Session session)
    {
        if(session !=null && session.isOpen())
        {
            session.close();
        }
    }
}
