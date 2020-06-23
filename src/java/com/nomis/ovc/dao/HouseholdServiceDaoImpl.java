/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.operationsManagement.FinancialYearManager;
import com.nomis.operationsManagement.OvcServiceAttributesManager;
import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.HouseholdService;
import com.nomis.ovc.util.AppConstant;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author smomoh
 */
public class HouseholdServiceDaoImpl implements HouseholdServiceDao
{
    Session session;
    Transaction tx;
    SubQueryGenerator sqg=new SubQueryGenerator();
    String markedForDeleteQuery=" and hhs.markedForDelete=0";
    String ahmMarkedForDeleteQuery=" and ahm.markedForDelete=0";
    public List getServicesByPeriodPerBeneficiary(String beneficiaryId,String startDate,String endDate) throws Exception
    {
        List list=null;
        try
        {
            String serviceDateQuery=sqg.getHouseholdServiceDateQuery(startDate, endDate);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.beneficiaryId=:id"+serviceDateQuery+markedForDeleteQuery+" order by hhs.serviceDate desc").setString("id",beneficiaryId).list();
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
    public List getMentorshipServices() throws Exception
    {
        List mainList=new ArrayList();
        try
        {     
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+" and hhs.stableServices like '%"+OvcServiceAttributesManager.getMentorshipServices().getServiceCode()+"%'"+markedForDeleteQuery;
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
    public int getNumberOfAdultHouseholdMembersServedByEnrollmentStatusAndHivStatus(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,int onTreatment) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            String hivTreatmentQuery="";
            if(hivStatus==1 && onTreatment==1)
            hivTreatmentQuery=SubQueryGenerator.getHouseholdMemberHivPositiveOnTreatmentQuery();
            else if(hivStatus==1 && onTreatment==2)
            hivTreatmentQuery=SubQueryGenerator.getHouseholdMemberHivPositiveNotOnTreatmentQuery();
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct hhs.beneficiaryId) "+SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+currentEnrollmentStatusQuery+SubQueryGenerator.getAdultHouseholdMemberHivStatusQuery(hivStatus)+ageQuery+sexQuery+hivTreatmentQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
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
    public List getListOfServicesByLevel4OrganizationUnit(String level4OuId) throws Exception
    {
        List mainList=new ArrayList();
        try
        {     
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+" and ou.ouPath like '%"+level4OuId+"%'"+markedForDeleteQuery+" order by hhs.serviceDate";
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
            String query="from HouseholdService hhs where hhs.ageAtService=0";
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
    public int getNumberOfAdultMembersNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            session = HibernateUtil.getSession();//"+sqg.getHouseholdServiceDateQuery(startDate, endDate)ageQuery+
            tx = session.beginTransaction();
            String query="select count(distinct ahm.beneficiaryId) "+SubQueryGenerator.getHheAdultHouseholdMemberOrganizationUnitQuery()+additionalQuery+currentEnrollmentStatusQuery+sexQuery+" and ahm.beneficiaryId not in (select distinct hhs.beneficiaryId from HouseholdService hhs where hhs.beneficiaryId is not null"+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery+")";
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
    public List getListOfAdultMembersNotServedInReportPeriod(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            session = HibernateUtil.getSession();//+ageQuery
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheAdultHouseholdMemberOrganizationUnitQuery()+additionalQuery+currentEnrollmentStatusQuery+sexQuery+" and ahm.beneficiaryId not in (select distinct hhs.beneficiaryId from HouseholdService hhs where hhs.beneficiaryId is not null "+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery+")"+" order by hhe.organizationUnit, ahm.currentAge";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                List uniqueIdList=new ArrayList();
                AdultHouseholdMember ahm=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    ahm=(AdultHouseholdMember)objArray[1];
                    if(!uniqueIdList.contains(ahm.getBeneficiaryId()))
                    {
                        mainList.add(ahm);
                        uniqueIdList.add(ahm.getBeneficiaryId());
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
    public List getHouseholdServiceRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalOrgUnitQuery+sqg.getHouseholdServiceLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
    public int getNumberOfHouseholdsServedByServiceDomainAndSubType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType,String serviceCode) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            String serviceDomainQuery=sqg.getHouseholdServiceQueryByServiceDomain(serviceType);
            if(serviceCode !=null)
            serviceDomainQuery=sqg.getHouseholdServiceQueryByServiceDomainAndSubType(serviceType,serviceCode);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct hhs.beneficiaryId) "+SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+serviceDomainQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery+ahmMarkedForDeleteQuery;
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
    public List getListOfAdultMembersServedByServiceDomainAndSubType(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex,int serviceType,String serviceCode) throws Exception
    {//(additionalQueryCriteria, enrollmentStatus, startDate, endDate, startAge, endAge, sex, serviceDomain, serviceCode);
        List mainList=new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            String serviceDomainQuery=sqg.getHouseholdServiceQueryByServiceDomain(serviceType);
            if(serviceCode !=null)
            serviceDomainQuery=sqg.getHouseholdServiceQueryByServiceDomainAndSubType(serviceType,serviceCode);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
                         //SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+serviceDomainQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate);   
            String query=SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+currentEnrollmentStatusQuery+ageQuery+sexQuery+serviceDomainQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery+ahmMarkedForDeleteQuery+" order by hhe.organizationUnit, ahm.currentAge";
            System.err.println("query is "+query);
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                List uniqueIdList=new ArrayList();
                AdultHouseholdMember ahm=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    ahm=(AdultHouseholdMember)objArray[2];
                    if(!uniqueIdList.contains(ahm.getBeneficiaryId()))
                    {
                        mainList.add(ahm);
                        uniqueIdList.add(ahm.getBeneficiaryId());
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
    public List getOvcServicesWithReferralRecords() throws Exception
    {
        List list =null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.referralServices is not null and LENGTH(TRIM(hhs.referralServices))>0"+markedForDeleteQuery).list();
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
    public List getHouseholdServices(String additionalQuery,int currentEnrollmentStatus,int hivStatus,String startDate, String endDate,int startAge,int endAge,String sex,boolean onTreatment) throws Exception
    {
        List mainList =new ArrayList();
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);//getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query=SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
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
    public int getNumberOfActiveAdultMembersServedForDatim(String additionalQuery,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
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
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");
            //String ageQuery=sqg.getAgeAtOvcServiceQuery(startAge,endAge);
            //+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate, endDate)
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct hhs.beneficiaryId) "+SubQueryGenerator.getHheAdultHouseholdMemberOrganizationUnitHouseholdServiceQuarterlyStatusTrackerQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getHouseholdServiceDateQuery(startOfLastQuarter, endDate)+markedForDeleteQuery;
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
    public int getNumberOfAdultMembersServedByEnrollmentStatusForDatim(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String currentEnrollmentStatusQuery=SubQueryGenerator.getEnrollmentStatusQuery(currentEnrollmentStatus);//.getOvcCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            String sexQuery="";
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            //+sqg.getQuarterlyStatusTrackerDateOfCurrentStatusQuery(startDate, endDate)
            String ageQuery=sqg.getAgeAtEnrollmentStatusQuery(startAge+"",endAge+"");
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct hhs.beneficiaryId) "+SubQueryGenerator.getHheAdultHouseholdMemberOrganizationUnitHouseholdServiceQuarterlyStatusTrackerQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+sqg.getHouseholdServiceDateQuery(startDate, endDate)+markedForDeleteQuery;
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
    public int getNumberOfBeneficiariesServedByEnrollmentStatus(String additionalQuery,int currentEnrollmentStatus,String startDate, String endDate,int startAge,int endAge,String sex) throws Exception
    {
        int count=0;
        try
        {
            String sexQuery="";
            String currentEnrollmentStatusQuery=SubQueryGenerator.getAdultHouseholdMemberCurrentEnrollmentStatusQuery(currentEnrollmentStatus);
            if(sex !=null)
            {
                sexQuery=sqg.getAdultHouseholdMemberGenderQuery(sex);
            }
            String ageQuery=sqg.getAgeAtHouseholdServiceQuery(startAge,endAge);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="select count(distinct hhs.beneficiaryId) "+SubQueryGenerator.getHheOrganizationUnitAdultHouseholdMemberHouseholdServiceQuery()+additionalQuery+ageQuery+sexQuery+currentEnrollmentStatusQuery+" and hhs.serviceDate between '"+startDate+"' and '"+endDate+"'"+markedForDeleteQuery;
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
    public void saveHouseholdService(HouseholdService hhs,boolean calculateAgeAtService) throws Exception
    {
        try
        {
            if(hhs !=null)
            {
                HouseholdService hhs2=this.getHouseholdService(hhs.getBeneficiaryId(), hhs.getServiceDate());
                if(hhs2==null)
                {
                    hhs.setNumberOfServices(getNumberOfServicesPerServiceRecord(hhs));
                    if(calculateAgeAtService)
                    hhs=getHouseholdServiceWithAgeAtService(hhs);
                    session=HibernateUtil.getSession();
                    tx=session.beginTransaction();
                    session.save(hhs);
                    tx.commit();
                    session.close();
                }
            }
        }
        catch (Exception ex)
        {
            session.close();
        }
    }
    public void updateHouseholdService(HouseholdService hhs,boolean calculateAgeAtService) throws Exception
    {
        try
        {
        if(hhs !=null)
        {
            System.err.println("hhs is not nul");
            HouseholdService hhs2=getHouseholdService(hhs.getBeneficiaryId(),hhs.getServiceDate());
            if(hhs2 !=null)
            {
                System.err.println("hhs2 is not nul");
                hhs.setId(hhs2.getId());
                hhs.setNumberOfServices(getNumberOfServicesPerServiceRecord(hhs));
                hhs.setDateCreated(hhs2.getDateCreated());
                if(calculateAgeAtService)
                hhs=getHouseholdServiceWithAgeAtService(hhs);
                session=HibernateUtil.getSession();
                tx=session.beginTransaction();
                session.update(hhs);
                tx.commit();
                session.close();
                System.err.println("Household service record updated");
            }
         }
        }
        catch (Exception ex)
        {
            session.close();
        }
    }
    public void markedForDelete(HouseholdService hhs) throws Exception
    {
        if(hhs !=null)
        {
            HouseholdService hhs2=getHouseholdService(hhs.getBeneficiaryId(), hhs.getServiceDate());
            if(hhs2 !=null)
            {
                hhs2.setMarkedForDelete(AppConstant.MARKEDFORDELETE);
                session=HibernateUtil.getSession();
                tx=session.beginTransaction();
                session.update(hhs2);
                tx.commit();
                session.close();
            }
         }
    }
    public void deleteHouseholdService(HouseholdService hhs) throws Exception
    {
        if(hhs !=null)
        {
            System.err.println("about to delete hhs");
            HouseholdService hhs2=getHouseholdService(hhs.getBeneficiaryId(), hhs.getServiceDate());
            if(hhs2 !=null)
            {
                hhs.setId(hhs2.getId());
                session=HibernateUtil.getSession();
                tx=session.beginTransaction();
                session.delete(hhs);
                tx.commit();
                session.close();
                System.err.println("hhs deleted");
            }
         }
    }
    public void markHouseholdServicesForDelete(String beneficiaryId) throws Exception
    {
        List list=getHouseholdServicesPerBeneficiary(beneficiaryId);
        if(list !=null && !list.isEmpty())
        {
            HouseholdService hhs=null;
            for(Object obj:list)
            {
                hhs=(HouseholdService)obj;
                deleteHouseholdService(hhs);
            }
        }
    }
    public void deleteHouseholdServicesByBeneficiaryId(String beneficiaryId) throws Exception
    {
        List list=getHouseholdServicesPerBeneficiary(beneficiaryId);
        if(list !=null && !list.isEmpty())
        {
            HouseholdService hhs=null;
            for(Object obj:list)
            {
                hhs=(HouseholdService)obj;
                deleteHouseholdService(hhs);
            }
        }
    }
    public HouseholdService getHouseholdService(int id) throws Exception
    {
        HouseholdService hhs=null;
        List list = new ArrayList();
         try 
         {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.id = :mid").setInteger("mid", id).list();
            tx.commit();
            session.close();
            if( list !=null && list.size()>0)
             {
               hhs = (HouseholdService)list.get(0);
             }
        }
         catch (Exception ex)
         {
             session.close();
             ex.printStackTrace();
         }
         
        return hhs; 
    }
    public HouseholdService getHouseholdService(String beneficiaryId,Date serviceDate) throws Exception
    {
        HouseholdService hhs=null;
        List list = new ArrayList();
         try 
         {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.beneficiaryId = :mid and hhs.serviceDate=:d").setString("mid", beneficiaryId).setDate("d", serviceDate).list();
            tx.commit();
            session.close();
            if( list !=null && list.size()>0)
             {
               hhs = (HouseholdService)list.get(0);
             }
        }
         catch (Exception ex)
         {
             session.close();
         }
         
        return hhs; 
    }
    public List getHouseholdServicesPerBeneficiary(String beneficiaryId) throws Exception
    {
        List list = null;
         try 
         {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.beneficiaryId = :mid ").setString("mid", beneficiaryId).list();
            tx.commit();
            session.close();
        }
         catch (Exception ex)
         {
             session.close();
             ex.printStackTrace();
         }
         
        return list; 
    }
    public List getHouseholdServicePerBeneficiary(String beneficiaryId) throws Exception
    {
        List list = new ArrayList();
        try 
         {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.beneficiaryId=:bId").setString("bId", beneficiaryId).list();
            tx.commit();
            session.close();
        }
         catch (Exception ex)
         {
             session.close();
             ex.printStackTrace();
         }
         return list;
    }
    public List getHouseholdServiceByServiceDate(Date serviceDate) throws Exception
    {
        List list = new ArrayList();
        try 
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HouseholdService hhs where hhs.serviceDate=:sd"+markedForDeleteQuery).setDate("bId", serviceDate).list();
            tx.commit();
            session.close();
        }
         catch (Exception ex)
         {
             session.close();
             ex.printStackTrace();
         }
         return list;
    }
public int getNumberOfServicesPerServiceRecord(HouseholdService hhs) throws Exception
{
    int numberOfServices=0;
    if(hhs !=null)
    {
        if(hhs.getSafetyServices() !=null && !hhs.getSafetyServices().equals("") && !hhs.getSafetyServices().equals(" "))
        numberOfServices++;
        if(hhs.getStableServices() !=null && !hhs.getStableServices().equals("") && !hhs.getStableServices().equals(" "))
        numberOfServices++;
        if(hhs.getHealthServices() !=null && !hhs.getHealthServices().equals("") && !hhs.getHealthServices().equals(" "))
        numberOfServices++;
        if(hhs.getReferralServices() !=null && !hhs.getReferralServices().equals("") && !hhs.getReferralServices().equals(" "))
        numberOfServices++;
    }
    return numberOfServices;
}
public HouseholdService getHouseholdServiceWithAgeAtService(HouseholdService hhs) throws Exception
{
    DaoUtility util=new DaoUtility();
    AdultHouseholdMember ahm=util.getAdultHouseholdMemberDaoInstance().getAdultHouseholdMember(hhs.getBeneficiaryId());
    if(ahm !=null)
    {
        //int ageAtService=AppManager.getAgeFromDates(ahm.getDateOfBirth(), hhs.getServiceDate());
        hhs.setAgeAtService(ahm.getCurrentAge());   
        System.err.println("Household Service updated with age at service "+hhs.getAgeAtService());
    }
    return hhs;
}
public void changeCaregiverIdInHouseholdService(String oldBeneficiaryId, String newBeneficiaryId) throws Exception
{
    List list=this.getHouseholdServicePerBeneficiary(oldBeneficiaryId);
    if(list !=null)
    {
        for(Object obj:list)
        {
            HouseholdService service=(HouseholdService)obj;
            service.setBeneficiaryId(newBeneficiaryId);
            this.updateHouseholdService(service,false);
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
