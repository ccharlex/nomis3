/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.operationsManagement.OvcServiceAttributesManager;
import com.nomis.operationsManagement.FinancialYearManager;
import com.nomis.ovc.business.ChildService;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.util.AppConstant;
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
public class ChildServiceDaoImpl implements ChildServiceDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    SubQueryGenerator sqg=new SubQueryGenerator();
    Ovc ovc=null;
    String markedForDeleteQuery=" and service.markedForDelete=0";
    String ovcMarkedForDeleteQuery=" and ovc.markedForDelete=0";
    public int getNumberOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String startDate, String endDate,int startAge, int endAge,int enrollmentStatus,int currentNutritionStatus,String sex) throws Exception
    {
        int count=0;
        try
        {
            String serviceDateQuery=sqg.getOvcServiceDateQuery(startDate, endDate);
            String ageQuery=sqg.getOvcCurrentAgeQuery(startAge, endAge);
            String sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            String enrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("select count(distinct service.ovcId)"+SubQueryGenerator.getHheOvcOrganizationUnitServiceNutritionStatusQuery()+sexQuery+ageQuery+enrollmentStatusQuery+SubQueryGenerator.getCurrentNutritionStatusQuery(currentNutritionStatus)+serviceDateQuery+markedForDeleteQuery).list();
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
    public List getListOfMalnourishedChildrenProvidedNutritionalServices(ReportParameterTemplate rpt,String startDate, String endDate,int startAge, int endAge,int enrollmentStatus,int currentNutritionStatus,String sex) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            String serviceDateQuery=sqg.getOvcServiceDateQuery(startDate, endDate);
            String ageQuery=sqg.getOvcCurrentAgeQuery(startAge, endAge);
            String sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            String enrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(enrollmentStatus);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(SubQueryGenerator.getHheOvcOrganizationUnitServiceNutritionStatusQuery()+ovcMarkedForDeleteQuery+sexQuery+ageQuery+enrollmentStatusQuery+SubQueryGenerator.getCurrentNutritionStatusQuery(currentNutritionStatus)+serviceDateQuery+markedForDeleteQuery).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                List uniqueIdList=new ArrayList();
                Ovc ovc=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    ovc=(Ovc)objArray[1];
                    if(!uniqueIdList.contains(ovc.getOvcId()))
                    {
                        uniqueIdList.add(ovc.getOvcId());
                        mainList.add(ovc);
                    }  
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
    public List getAndUpdateWashRecords() throws Exception
    {
        List list=null;
        try
        {
            String query="from ChildService service where service.healthServices like '%33h%'";
            System.err.println("query in getAndUpdateWashRecords() is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                ChildService service=null;
                String healthServices=null;
                for(Object obj:list)
                {
                    service=(ChildService)obj;
                    healthServices=service.getHealthServices();
                    healthServices=healthServices.replaceAll("33h", "24h");
                    service.setHealthServices(healthServices);
                    updateChildService(service, false);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }
    public List getDistinctYearOfserviceList() throws Exception
    {
        List list=null;
        try
        {
            String query="select distinct YEAR(service.serviceDate) from ChildService service order by YEAR(service.serviceDate) desc";
            System.err.println("query in getDistinctYearOfserviceList() is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }
    public int resetAgeAtServiceForServiceRecords(String level4OuId) throws Exception
    {
        int count=0;
        try
        {
            List list=getListOfServicesByLevel4OrganizationUnit(level4OuId);
            
            if(list !=null && !list.isEmpty())
            {
                ChildService service=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    service=(ChildService)objArray[3];
                    updateChildService(service, false); 
                    count++;
                    
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
    public int resetAgeAtServiceForChildrenWithZeroAgeAtService() throws Exception
    {
        int count=0;
        try
        {
            //String serviceQuery=sqg.getOvcServiceQueryByServiceDomainAndSubType(domain,serviveCode);
            String query="from ChildService service where (service.ageAtService=0 or service.ageUnitAtService="+AppConstant.AGEUNIT_MONTH_NUM+") order by service.serviceDate desc";
                        
            System.err.println("query in resetAgeAtServiceForChildrenWithZeroAgeAtService() is "+query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                ChildService service=null;
                for(Object obj:list)
                {
                    service=(ChildService)obj;
                    //service=getChildServiceWithAgeAtService(service);
                    this.updateChildService(service, false);
                    count++;
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
    public List getListOfServicesByDomainAndServiceTypeAndAgeLimit(int domain,String serviveCode, int ageLimit) throws Exception
    {
        List list=null;
        try
        {
            String serviceQuery=sqg.getOvcServiceQueryByServiceDomainAndSubType(domain,serviveCode);
            String query="from ChildService service where service.ovcId is not null"+serviceQuery+" and service.ageAtService>"+ageLimit+markedForDeleteQuery+" order by service.serviceDate desc";
            
            System.err.println("query in getListOfServicesByDomainAndServiceTypeAndAgeLimit is "+query);
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
    public List getServicesByPeriodPerChild(String ovcId,String startDate,String endDate) throws Exception
    {
        List list=null;
        try
        {
            String serviceDateQuery=sqg.getOvcServiceDateQuery(startDate, endDate);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from ChildService service where service.ovcId=:sid"+serviceDateQuery+markedForDeleteQuery+" order by service.serviceDate desc").setString("sid",ovcId).list();
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
    public int childServedInReportPeriod(String ovcId,String startDate,String endDate) throws Exception
    {
        int servedInReportPeriod=0;
        try
        {
            List list=getServicesByPeriodPerChild(ovcId,startDate,endDate);
           if(list !=null && !list.isEmpty()) 
           servedInReportPeriod=1;
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return servedInReportPeriod;
    }
    public int getNumberOfServiceRecordsPerChild(String ovcId) throws Exception
    {
        int count=0;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) from Ovc ovc,ChildService service where ovc.ovcId=service.ovcId and ovc.ovcId=:id"+markedForDeleteQuery;
            System.err.println("query is "+query);
            List list = session.createQuery(query).setString("id", ovcId).list();
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
    public int updateAgeAtServiceWithCurrentAge() throws Exception
    {
        int returnValue=0;
        try
        {     
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="update childservice s set s.ageAtService=(select e.currentage from childenrollment e where s.ovcid=e.ovcid)";
            System.err.println("query is "+query);
            returnValue=session.createSQLQuery(query).executeUpdate();
            tx.commit();
            closeSession(session);
            /*if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[3]);
                    
                }
            }*/
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return returnValue;
    }
    public List getListOfServicesByLevel4OrganizationUnit(String level4OuId) throws Exception
    {
        List mainList=new ArrayList();
        try
        {     
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+" and ou.ouPath like '%"+level4OuId+"%'"+markedForDeleteQuery+" order by service.serviceDate";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                mainList.addAll(list);
                /*for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[3]);
                    
                }*/
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getServiceRecordsWithZeroAgeAtService() throws Exception
    {
        List list=new ArrayList();
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from ChildService service where service.ageAtService=0";
            System.err.println("query is "+query);
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
    public int getNumberOfOvcNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct ovc.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitQuery()+ageQuery+additionalQuery+currentEnrollmentStatusQuery+sexQuery+sqg.getOvcEnrollmentEndDateQuery(endDate)+ovcMarkedForDeleteQuery+" and ovc.ovcId not in (select distinct service.ovcId from ChildService service where service.ovcId is not null "+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery+")";
            System.err.println("query in getNumberOfOvcNotServedInReportPeriod is "+query);
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
    public List getListOfOvcNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitQuery()+ageQuery+additionalQuery+currentEnrollmentStatusQuery+sexQuery+sqg.getOvcEnrollmentEndDateQuery(endDate)+ovcMarkedForDeleteQuery+" and ovc.ovcId not in (select distinct service.ovcId from ChildService service where service.ovcId is not null "+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery+") order by hhe.organizationUnit, ovc.currentAge";
            //SubQueryGenerator.getHheOvcOrganizationUnitQuery()+ageQuery+additionalQuery+currentEnrollmentStatusQuery+sexQuery+" and ovc.ovcId not in (select distinct service.ovcId from ChildService service where service.ovcId is not null "+sqg.getOvcServiceDateQuery(startDate, endDate)+")"+" order by hhe.organizationUnit, ovc.currentAge";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                List uniqueIdList=new ArrayList();
                Ovc ovc=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    ovc=(Ovc)objArray[1];
                    ovc.setHhe((HouseholdEnrollment)objArray[0]); 
                    if(!uniqueIdList.contains(ovc.getOvcId()))
                    {
                        mainList.add(ovc);
                        uniqueIdList.add(ovc.getOvcId());
                    }
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
    public int getNumberOfOvcServedAndRiskAssessedByEnrollmentStatus(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int hivRiskStatus) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String hivRiskStatusQuery=SubQueryGenerator.getHivUnknownOvcHivRiskStatusQuery(hivRiskStatus,startDate, endDate);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+hivRiskStatusQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByServiceDomainAndSubType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType,String serviceCode) throws Exception
    {
        int count=0;
        try
        {
                String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
                String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
                String sexQuery="";
                if(sex !=null)
                {
                    sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
                }
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+sqg.getOvcServiceQueryByServiceDomainAndSubType(serviceType,serviceCode)+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
                System.err.println("query is "+query);
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
    public List getListOfOvcServedByServiceDomainAndSubType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType,String serviceCode) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
                String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
                String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
                String sexQuery="";
                if(sex !=null)
                {
                    sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
                }
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                String query=SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+sqg.getOvcServiceQueryByServiceDomainAndSubType(serviceType,serviceCode)+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
                System.err.println("query is "+query);
                List list = session.createQuery(query).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    List idList=new ArrayList();
                    Ovc ovc=null;
                    for(Object obj:list)
                    {
                        Object[] objArray=(Object[])obj;
                        ovc=(Ovc)objArray[1];
                        if(!idList.contains(ovc.getOvcId()))
                        {
                            mainList.add(objArray[1]);
                            idList.add(ovc.getOvcId());
                        }
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
    public List getChildServicesWithReferralRecords() throws Exception
    {
        List list =null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from ChildService service where service.referralServices is not null and LENGTH(TRIM(service.referralServices))>0"+markedForDeleteQuery).list();
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
    public int getNumberOfOvcServedByEnrollmentStatusAndHivStatusNotScreenedForHiv(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String screenedForHivQuery="";
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+SubQueryGenerator.getHivUnknownOvcNotScreenedForHivRiskAssessmentQuery(startDate, endDate)+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+screenedForHivQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByEnrollmentStatusAndHivStatusScreenedForHiv(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String screenedForHivQuery="";
            
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()+ovcMarkedForDeleteQuery+SubQueryGenerator.getHivRiskAssessmentDateQuery(startDate,endDate)+additionalQuery+currentEnrollmentStatusQuery+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+ageQuery+sexQuery+screenedForHivQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public List getListOfOvcServedByEnrollmentStatusAndServiceType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            String statusPeriodQuery="";
            if(currentEnrollmentStatus==AppConstant.ACTIVE_NUM)
            statusPeriodQuery=sqg.getOvcStartDateOfCurrentStatusQuery(startDate);
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+sqg.getOvcServiceQueryByServiceDomain(serviceType)+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+statusPeriodQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                List uniqueIdList=new ArrayList();
                Ovc ovc=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    ovc=(Ovc)objArray[1];
                    if(!uniqueIdList.contains(ovc.getOvcId()))
                    {
                        mainList.add(ovc);
                        uniqueIdList.add(ovc.getOvcId());
                    }
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
    public int getNumberOfOvcServedByEnrollmentStatusAndServiceType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            String statusPeriodQuery="";
            if(currentEnrollmentStatus==AppConstant.ACTIVE_NUM)
            statusPeriodQuery=sqg.getOvcStartDateOfCurrentStatusQuery(startDate);
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+sqg.getOvcServiceQueryByServiceDomain(serviceType)+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+statusPeriodQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;//+" and service.serviceDate between '"+startDate+"' and '"+endDate+"'";
            System.err.println("query is "+query);
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
    public int getNumberOfHivUnknownOvcNotAtRiskServed(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String testNotIndicatedQuery=SubQueryGenerator.getTestNotIndicatedQuery(startDate, endDate);
            
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+testNotIndicatedQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfHivUnknownOvcAtRiskServed(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String testNotIndicatedQuery=SubQueryGenerator.getHivUnknownOvcAtRiskQuery(startDate, endDate);
            
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+testNotIndicatedQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedAndRiskAssessedByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,int riskAssessmentStatus) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }//getOvcAtRiskQuery(AppConstant.CHILD_AT_HIGH_RISK_NUM)
            String riskAssessmentStatusQuery=SubQueryGenerator.getOvcHivRiskStatusQuery(riskAssessmentStatus,startDate, endDate);
            
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceHivRiskAssessmentQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+ageQuery+sexQuery+riskAssessmentStatusQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,int onTreatment) throws Exception
    {
        int count=0;
        try
        {
            
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            //String currentEnrollmentStatusQuery=SubQueryGenerator.getEnrollmentStatusHistoryStatusQuery(currentEnrollmentStatus);
            //String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");//getAgeAtEnrollmentStatusQuery(String firstAge,String secondAge);
             String statusPeriodQuery="";
            if(currentEnrollmentStatus==AppConstant.ACTIVE_NUM)
            statusPeriodQuery=sqg.getOvcStartDateOfCurrentStatusQuery(startDate);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String hivTreatmentQuery="";
            if(hivStatus==AppConstant.HIV_POSITIVE_NUM && onTreatment==AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
            hivTreatmentQuery=SubQueryGenerator.getOvcHivPositiveOnTreatmentQuery();
            else if(hivStatus==AppConstant.HIV_POSITIVE_NUM && onTreatment==AppConstant.ENROLLED_ON_TREATMENT_NO_NUM)
            hivTreatmentQuery=SubQueryGenerator.getOvcHivPositiveNotOnTreatmentQuery();
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+statusPeriodQuery+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+ageQuery+sexQuery+hivTreatmentQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            //String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceEnrollmentStatusHistoryQuery()+additionalQuery+currentEnrollmentStatusQuery+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+ageQuery+sexQuery+hivTreatmentQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    
    public int getNumberOfActiveOvcServed(String additionalQuery,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            FinancialYearManager fym=new FinancialYearManager();
            String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(AppConstant.ACTIVE_NUM);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getOvcStartDateOfCurrentStatusQuery(startOfLastQuarter)+sqg.getOvcServiceDateQuery(startOfLastQuarter, endDate)+markedForDeleteQuery;
            //String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceEnrollmentStatusHistoryQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getEnrollmentStatusHistoryDateOfCurrentStatusQuery(startDate, endDate)+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByEnrollmentStatus(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfActiveOvcServedForDatim(String additionalQuery,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            FinancialYearManager fym=new FinancialYearManager();
            String startOfLastQuarter=fym.getStartDateOfQuarter(endDate);
            String currentEnrollmentStatusQuery=SubQueryGenerator.getEnrollmentStatusQuery(AppConstant.ACTIVE_NUM);
            
            //.getEnrollmentStatusHistoryStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");
            //String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuarterlyStatusTrackerQuery()+ovcMarkedForDeleteQuery+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate, endDate)+sqg.getOvcServiceDateQuery(startOfLastQuarter, endDate)+markedForDeleteQuery;
            //String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceEnrollmentStatusHistoryQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getEnrollmentStatusHistoryDateOfCurrentStatusQuery(startDate, endDate)+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByEnrollmentStatusForDatim(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            /*String statusPeriodQuery="";
            if(currentEnrollmentStatus==AppConstant.ACTIVE_NUM)
            statusPeriodQuery=sqg.getEnrollmentStatusHistoryDateOfCurrentStatusQuery(startDate, endDate);*/
            //.getOvcStartDateOfCurrentStatusQuery(startDate);
            String currentEnrollmentStatusQuery=SubQueryGenerator.getEnrollmentStatusQuery(currentEnrollmentStatus);//.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");
            //String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            //String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuarterlyStatusTrackerQuery()+ovcMarkedForDeleteQuery+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate, endDate)+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfOvcServedByEnrollmentStatusAndHivStatusForDatim(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,int onTreatment) throws Exception
    {
        int count=0;
        try
        {
            
            //String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            //String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            String currentEnrollmentStatusQuery=SubQueryGenerator.getEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");//getAgeAtEnrollmentStatusQuery(String firstAge,String secondAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=SubQueryGenerator.getOvcSexQuery(sex);
            }
            String hivTreatmentQuery="";
            if(hivStatus==1 && onTreatment==1)
            hivTreatmentQuery=SubQueryGenerator.getEnrollmentStatusHivPositiveOnTreatmentQuery();
            else if(hivStatus==1 && onTreatment==2)
            hivTreatmentQuery=SubQueryGenerator.getEnrollmentStatusHivPositiveNotOnTreatmentQuery();
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            //String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+additionalQuery+currentEnrollmentStatusQuery+SubQueryGenerator.getOvcHivStatusQuery(hivStatus)+ageQuery+sexQuery+hivTreatmentQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            String query="select count(distinct service.ovcId) "+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuarterlyStatusTrackerQuery()+ovcMarkedForDeleteQuery+additionalQuery+currentEnrollmentStatusQuery+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate, endDate) +SubQueryGenerator.getEnrollmentStatusHivStatusQuery(hivStatus)+ageQuery+sexQuery+hivTreatmentQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public int getNumberOfActiveOvcServed(String startDate, String endDate,int startAge,int endAge) throws Exception
    {
        int count=0;
        try
        {
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count (distinct service.ovcId)"+SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+" and ovc.currentEnrollmentStatus=1 "+ovcMarkedForDeleteQuery+ageQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            System.err.println("query is "+query);
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
    public List getChildServiceRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+additionalOrgUnitQuery+sqg.getOvcServiceLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
                    mainList.add(objArray[3]);
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
    public List getAllChildServices() throws Exception
    {
        List list =null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from ChildService service"+markedForDeleteQuery).list();
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
    public List getChildServices(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,boolean onTreatment) throws Exception
    {
        List mainList =new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getOvcSexQuery(sex);
            }
            String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOvcOrganizationUnitServiceQuery()+ovcMarkedForDeleteQuery+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getOvcServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
            //" and service.serviceDate between '"+startDate+"' and '"+endDate+"'";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    mainList.add(objArray[3]);
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
    public ChildService getChildService(String ovcId, Date serviceDate) throws Exception
    {
        ChildService service=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from ChildService service where service.ovcId=:sid and service.serviceDate=:sdate").setString("sid",ovcId).setDate("sdate", serviceDate).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                service=(ChildService)list.get(0);
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return service;
    }
    public List getServicesPerChild(String ovcId) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from ChildService service where service.ovcId=:sid").setString("sid",ovcId).list();
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
    public void saveChildService(ChildService service,boolean saveBirthRegistration) throws Exception     
    {
        try
        {
            if(service !=null && service.getServiceDate() !=null)
            {
                if(this.getChildService(service.getOvcId(), service.getServiceDate())==null)
                {
                    service=getChildServiceWithAgeAtService(service);
                    
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.save(service);
                    tx.commit();
                    closeSession(session);
                    if(saveBirthRegistration)
                    {
                        updateOvcCurrentBirthRegAndSchoolStatus(service);
                        updateOvcCurrentEnrollmentStatus(service);
                        updateOvc(ovc);
                    }
                }
                /*else
                {
                    updateChildService(service,true);
                }*/
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void updateChildService(ChildService service,boolean saveBirthRegistration) throws Exception
    {
       try
        {
            //System.err.println("service.getOvcId() in updateChildService is "+service.getOvcId());
            if(service !=null && service.getServiceDate() !=null)
            {
                //System.err.println("service in updateChildService is not null ");
                ChildService service2=getChildService(service.getOvcId(), service.getServiceDate());
                if(service2 !=null)
                {
                    //System.err.println("service2 in updateChildService is not null ");
                    service.setId(service2.getId());
                    service=getChildServiceWithAgeAtService(service);
                    
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(service);
                    tx.commit();
                    closeSession(session);
                    System.err.println("service with "+service.getOvcId()+" updated");
                    if(saveBirthRegistration)
                    {
                        updateOvcCurrentBirthRegAndSchoolStatus(service);
                        updateOvcCurrentEnrollmentStatus(service);
                        updateOvc(ovc);
                    }
                }
                /*else
                {
                    saveChildService(service,saveBirthRegistration);
                }*/
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        } 
    }
    public void markedForDelete(ChildService service) throws Exception
    {
        try
        {
            if(service !=null && service.getServiceDate() !=null)
            {
                ChildService service2=getChildService(service.getOvcId(), service.getServiceDate());
                if(service2 !=null)
                {
                    service.setId(service2.getId());
                    service.setMarkedForDelete(1);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(service);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void deleteChildService(String ovcId, Date serviceDate) throws Exception
    {
        
    }
    public void markChildServicesForDelete(String ovcId) throws Exception
    {
        List serviceList=getServicesPerChild(ovcId);
        if(serviceList !=null && !serviceList.isEmpty())
        {
            ChildService service=null;
            for(Object obj:serviceList)
            {
                service=(ChildService)obj;
                this.markedForDelete(service);
            }
        }
    }
    public void deleteServicesPerChild(String ovcId) throws Exception
    {
        List serviceList=getServicesPerChild(ovcId);
        if(serviceList !=null && !serviceList.isEmpty())
        {
            ChildService service=null;
            for(Object obj:serviceList)
            {
                service=(ChildService)obj;
                deleteService(service);
            }
        }
    }
    public void deleteService(ChildService service) throws Exception
    {
        try
        {
            if(service !=null && service.getServiceDate() !=null)
            {
                ChildService service2=getChildService(service.getOvcId(), service.getServiceDate());
                if(service2 !=null)
                {
                    service.setId(service2.getId());
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(service);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    private void updateOvcCurrentEnrollmentStatus(ChildService service) throws Exception
    {
        if(service !=null)
        {
            if(service.getEnrollmentStatus()>0)
            {
                DaoUtility util=new DaoUtility();
                if(ovc==null)
                ovc=util.getChildEnrollmentDaoInstance().getOvc(service.getOvcId());
                if(ovc !=null)
                {
                    if(DateManager.compareDates(ovc.getDateOfCurrentEnrollmentStatus(), service.getDateOfEnrollmentStatus()))
                    {
                        ovc.setCurrentEnrollmentStatus(service.getEnrollmentStatus());
                        ovc.setDateOfCurrentEnrollmentStatus(service.getDateOfEnrollmentStatus());
                        //util.getChildEnrollmentDaoInstance().updateOvc(ovc, false, false);
                    }
                }
            }
        }
    }
    public ChildService getChildServiceWithAgeAtService(ChildService service) throws Exception
    {
        Ovc ovc=getOvc(service.getOvcId());
        if(ovc !=null)
        {
            int ageAtService=ovc.getCurrentAge();//AppManager.getAgeFromDates(ovc.getDateOfBirth(), service.getServiceDate());
            int ageUnitAtService=ovc.getCurrentAgeUnit();//AppManager.getCurrentAgeUnitFromDates(ovc.getDateOfBirth(), service.getServiceDate());
            service.setAgeAtService(ageAtService);
            service.setAgeUnitAtService(ageUnitAtService);
            System.err.println("Ovc Service updated with age at service "+service.getAgeAtService());
        }
        return service;
    }
    public void updateOvcCurrentBirthRegAndSchoolStatus(ChildService service) throws Exception
    {
        if(service.getSafetyServices() !=null && service.getSafetyServices().trim().length()>0)
        {
            String[] safetyServices=service.getSafetyServices().split(",");
            if(safetyServices !=null && safetyServices.length>0)
            {
                String serviceCode=null;
                
                String eduPTCEService=OvcServiceAttributesManager.getEducationalSubsidiesPTCE().getServiceCode();
                String holisticScholarship=OvcServiceAttributesManager.getHolisticScholarshipService().getServiceCode();
                String eduReferralService=OvcServiceAttributesManager.getEducationReferralService().getServiceCode();
                String schollEduAssistance=OvcServiceAttributesManager.getSchoolEducationalAssistance().getServiceCode();
                String schoolPerfAssessment=OvcServiceAttributesManager.getSchoolPerformanceAssessmentService().getServiceCode();
                String schoolVisitService=OvcServiceAttributesManager.getSchoolVisit().getServiceCode();
                for(int i=0; i<safetyServices.length; i++)
                {
                    serviceCode=safetyServices[i];
                    if(serviceCode !=null && serviceCode.trim().length()>0)
                    {
                        serviceCode=serviceCode.trim();
                        //check for birth registration service code
                        if(serviceCode.equalsIgnoreCase(OvcServiceAttributesManager.getBirthRegistrationAcquisitionService().getServiceCode()))
                        {
                            if(ovc==null)
                            ovc=getOvc(service.getOvcId());
                            if(ovc !=null && DateManager.compareDates(ovc.getDateOfCurrentBirthRegStatus(),service.getServiceDate()))
                            {
                                ovc.setCurrentBirthRegistrationStatus(AppConstant.CHILD_HAS_BIRTHCERTIFICATE);
                                ovc.setDateOfCurrentBirthRegStatus(service.getServiceDate());
                            }
                        }
                        //check for school service code
                        else if(serviceCode.equalsIgnoreCase(eduPTCEService) || serviceCode.equalsIgnoreCase(holisticScholarship) || serviceCode.equalsIgnoreCase(eduReferralService) || serviceCode.equalsIgnoreCase(schollEduAssistance) || serviceCode.equalsIgnoreCase(schoolPerfAssessment) || serviceCode.equalsIgnoreCase(schoolVisitService))
                        {
                            if(ovc==null)
                            ovc=getOvc(service.getOvcId());
                            if(ovc !=null && DateManager.compareDates(ovc.getDateOfCurrentBirthRegStatus(),service.getServiceDate()))
                            {
                                ovc.setCurrentSchoolStatus(AppConstant.CHILD_IN_SCHOOL);
                                
                            }
                        }
                        
                    }
                }
            }
        }
    }
    private void updateOvc(Ovc ovc) throws Exception
    {
        DaoUtility util=new DaoUtility();
        if(ovc !=null)
        util.getChildEnrollmentDaoInstance().updateOvc(ovc, false, true);
    }
    private Ovc getOvc(String ovcId) throws Exception
    {
        DaoUtility util=new DaoUtility();
        Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(ovcId);
        return ovc;
    }
    public void changeOvcIdInService(String oldOvcId, String newOvcId) throws Exception
    {
        List list=this.getServicesPerChild(oldOvcId);
        if(list !=null)
        {
            for(Object obj:list)
            {
                ChildService service=(ChildService)obj;
                service.setOvcId(newOvcId);
                if(getChildService(service.getOvcId(), service.getServiceDate())==null)
                saveChildService(service, false);
                else
                updateChildService(service, false);
                System.err.println("Ovc Id in ChildServiceDaoImpl changed from "+oldOvcId+" to "+newOvcId);
            }
        }
    }
    public void closeSession(Session session)
    {
        if(session !=null && session.isOpen())
        {
            session.close();
        }
    }
}
