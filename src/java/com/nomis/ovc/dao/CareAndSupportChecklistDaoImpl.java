/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.CareAndSupportChecklist;
import com.nomis.ovc.business.HouseholdEnrollment;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.util.AppConstant;
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
public class CareAndSupportChecklistDaoImpl implements CareAndSupportChecklistDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    SubQueryGenerator sqg=new SubQueryGenerator();
    String markedForDeleteQuery=" and casc.markedForDelete=0";
    public List getMostRecentCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception
    {
        List recentCareAndSupportList=new ArrayList();
        List cascList=new ArrayList();
        if(getOvcCareAndSupportRecords(rpt) !=null)
        cascList.addAll(getOvcCareAndSupportRecords(rpt));
        if(getAdultCareAndSupportRecords(rpt) !=null)
        cascList.addAll(getAdultCareAndSupportRecords(rpt));
        
        if(cascList !=null && !cascList.isEmpty())
        {
            List uniqueIdList=new ArrayList();
            CareAndSupportChecklist casc=null;
            for(Object obj:cascList)
            {
                casc=(CareAndSupportChecklist)obj;
                if(!uniqueIdList.contains(casc.getBeneficiaryId()))
                {
                    recentCareAndSupportList.add(casc);
                    uniqueIdList.add(casc.getBeneficiaryId());
                }
            }
        }
        
        return recentCareAndSupportList;
    }
    public List getOvcCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOvcCareAndSupportOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getCareAndSupportDateOfAssessmentQuery(rpt.getStartDate(),rpt.getEndDate())+" order by casc.dateOfAssessment desc";
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                Ovc ovc=null;
                HouseholdEnrollment hhe=null;
                CareAndSupportChecklist casc=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hhe=(HouseholdEnrollment)objArray[0];
                    ovc=(Ovc)objArray[1];
                    casc=(CareAndSupportChecklist)objArray[2];
                    ovc.setHhe(hhe);
                    casc.setBeneficiary(ovc);
                    mainList.add(casc);
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
    public List getAdultCareAndSupportRecords(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheAdultHouseholdMemberCareAndSupportOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getCareAndSupportDateOfAssessmentQuery(rpt.getStartDate(),rpt.getEndDate())+" order by casc.dateOfAssessment desc";
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
            {
                AdultHouseholdMember ahm=null;
                HouseholdEnrollment hhe=null;
                CareAndSupportChecklist casc=null;
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    hhe=(HouseholdEnrollment)objArray[0];
                    ahm=(AdultHouseholdMember)objArray[1];
                    casc=(CareAndSupportChecklist)objArray[2];
                    ahm.setHhe(hhe);
                    casc.setBeneficiary(ahm);
                    mainList.add(casc);
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
    public List getCareAndSupportRecordsForExport(ReportParameterTemplate rpt) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            List ovcCareAndSupportList=getOvcCareAndSupportRecordsForExport(rpt);
            List adultCareAndSupportList=getAdultCareAndSupportRecordsForExport(rpt);
            if(ovcCareAndSupportList !=null)
            mainList.addAll(ovcCareAndSupportList);
            if(adultCareAndSupportList !=null)
            mainList.addAll(adultCareAndSupportList);
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return mainList;
    }
    public List getOvcCareAndSupportRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOvcCareAndSupportOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getCareAndSupportLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
                    mainList.add(objArray[2]);
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
    public List getAdultCareAndSupportRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheAdultHouseholdMemberCareAndSupportOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getCareAndSupportLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
                    mainList.add(objArray[2]);
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
    public CareAndSupportChecklist getCareAndSupportChecklist(int recordId) throws Exception
    {
        CareAndSupportChecklist casc=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from CareAndSupportChecklist casc where casc.recordId=:id").setInteger("id", recordId).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                casc=(CareAndSupportChecklist)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return casc;
    }
    public CareAndSupportChecklist getCareAndSupportChecklist(String beneficiaryId,Date dateOfAssessment) throws Exception
    {
       CareAndSupportChecklist casc=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from CareAndSupportChecklist casc where casc.beneficiaryId=:id and casc.dateOfAssessment=:doa").setString("id", beneficiaryId).setDate("doa", dateOfAssessment).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                casc=(CareAndSupportChecklist)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return casc; 
    }
    public List getCareAndSupportChecklist(String beneficiaryId) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from CareAndSupportChecklist casc where casc.beneficiaryId=:id"+markedForDeleteQuery+"  order by casc.dateOfAssessment desc";
            System.err.println("query is "+query);
            list = session.createQuery(query).setString("id", beneficiaryId).list();
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
    public void saveCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception
    {
        try
        {
            if(casc !=null && this.getCareAndSupportChecklist(casc.getBeneficiaryId(),casc.getDateOfAssessment()) ==null)
            {
                //System.err.println("casc with Id "+casc.getBeneficiaryId()+" about to be saved after cleanup");
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.save(casc);
                tx.commit();
                closeSession(session);
                System.err.println("casc with Id "+casc.getBeneficiaryId()+" saved");
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void markForDelete(CareAndSupportChecklist casc) throws Exception
    {
        try
        {
            if(casc !=null)
            {
                 CareAndSupportChecklist casc2=getCareAndSupportChecklist(casc.getBeneficiaryId(),casc.getDateOfAssessment());
                if(casc2 !=null)
                {
                    casc2.setMarkedForDelete(AppConstant.MARKEDFORDELETE);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(casc2);
                    tx.commit();
                    closeSession(session);
                    System.err.println("casc with Id "+casc.getBeneficiaryId()+" marked for delete");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void updateCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception
    {
        try
        {
            if(casc !=null)
            {
                 CareAndSupportChecklist casc2=getCareAndSupportChecklist(casc.getBeneficiaryId(),casc.getDateOfAssessment());
                if(casc2 !=null)
                {
                    casc.setRecordId(casc2.getRecordId()); 
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(casc);
                    tx.commit();
                    closeSession(session);
                    System.err.println("casc with Id "+casc.getBeneficiaryId()+" updated");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void deleteCareAndSupportChecklist(CareAndSupportChecklist casc) throws Exception
    {
        try
        {
            if(casc !=null)
            {
                CareAndSupportChecklist casc2=getCareAndSupportChecklist(casc.getBeneficiaryId(),casc.getDateOfAssessment());
                if(casc2 !=null)
                {
                    casc.setRecordId(casc2.getRecordId()); 
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(casc);
                    tx.commit();
                    closeSession(session);
                    System.err.println("casc with Id "+casc.getBeneficiaryId()+" deleted");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
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
