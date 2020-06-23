/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.business.ChildEducationPerformanceAssessment;
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
public class ChildEducationPerformanceAssessmentDaoImpl implements ChildEducationPerformanceAssessmentDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    SubQueryGenerator sqg=new SubQueryGenerator();
    String markedForDeleteQuery=" and cepa.markedForDelete=0";
    public List getChildEducationPerformanceAssessmentRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheChildEducationPerformanceAssessmentOrganizationUnitQuery()+additionalOrgUnitQuery+sqg.getChildEducationPerformanceAssessmentLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
    public void saveChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception
    {
        try
        {
            if(cepa !=null && this.getChildEducationPerformanceAssessment(cepa.getOvcId(),cepa.getDateOfAssessment()) ==null)
            {
                System.err.println("cepa with Id "+cepa.getOvcId()+" about to be saved after cleanup");
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.save(cepa);
                tx.commit();
                closeSession(session);
                System.err.println("Cepa with Id "+cepa.getOvcId()+" saved");
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void markForDelete(ChildEducationPerformanceAssessment cepa) throws Exception
    {
        try
        {
            if(cepa !=null)
            {
                 ChildEducationPerformanceAssessment cepa2=getChildEducationPerformanceAssessment(cepa.getOvcId(),cepa.getDateOfAssessment());
                System.err.println("cepa with Id "+cepa.getOvcId()+" about to be saved after cleanup");
                if(cepa2 !=null)
                {
                    cepa2.setMarkedForDelete(AppConstant.MARKEDFORDELETE);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(cepa2);
                    tx.commit();
                    closeSession(session);
                    System.err.println("Cepa with Id "+cepa.getOvcId()+" marked for delete");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void updateChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception
    {
        try
        {
            if(cepa !=null)
            {
                 ChildEducationPerformanceAssessment cepa2=getChildEducationPerformanceAssessment(cepa.getOvcId(),cepa.getDateOfAssessment());
                System.err.println("cepa with Id "+cepa.getOvcId()+" about to be saved after cleanup");
                if(cepa2 !=null)
                {
                    cepa.setRecordId(cepa2.getRecordId()); 
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(cepa);
                    tx.commit();
                    closeSession(session);
                    System.err.println("Cepa with Id "+cepa.getOvcId()+" saved");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void deleteChildEducationPerformanceAssessment(ChildEducationPerformanceAssessment cepa) throws Exception
    {
        try
        {
            if(cepa !=null)
            {
                ChildEducationPerformanceAssessment cepa2=getChildEducationPerformanceAssessment(cepa.getOvcId(),cepa.getDateOfAssessment());
                if(cepa2 !=null)
                {
                    cepa.setRecordId(cepa2.getRecordId()); 
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(cepa);
                    tx.commit();
                    closeSession(session);
                    System.err.println("Cepa with Id "+cepa.getOvcId()+" saved");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public ChildEducationPerformanceAssessment getChildEducationPerformanceAssessment(int recordId) throws Exception
    {
        ChildEducationPerformanceAssessment cepa=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from ChildEducationPerformanceAssessment cepa where cepa.recordId=:id").setInteger("id", recordId).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                cepa=(ChildEducationPerformanceAssessment)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return cepa;
    }
    public ChildEducationPerformanceAssessment getChildEducationPerformanceAssessment(String ovcId,Date dateOfAssessment) throws Exception
    {
       ChildEducationPerformanceAssessment cepa=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from ChildEducationPerformanceAssessment cepa where cepa.ovcId=:id and cepa.dateOfAssessment=:doa").setString("id", ovcId).setDate("doa", dateOfAssessment).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                cepa=(ChildEducationPerformanceAssessment)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return cepa; 
    }
    public List getChildEducationPerformanceAssessment(String ovcId) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from ChildEducationPerformanceAssessment cepa where cepa.ovcId=:id"+markedForDeleteQuery;;
            System.err.println("query is "+query);
            list = session.createQuery(query).setString("id", ovcId).list();
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
    public void closeSession(Session session)
    {
        if(session !=null && session.isOpen())
        {
            session.close();
        }
    }
}
