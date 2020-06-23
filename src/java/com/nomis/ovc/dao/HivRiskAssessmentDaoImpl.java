/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.operationsManagement.HivRiskAssessmentManager;
import com.nomis.ovc.business.HivRiskAssessment;
import com.nomis.ovc.business.HivStatusManager;
import com.nomis.ovc.business.Ovc;
import com.nomis.ovc.util.AppConstant;
import com.nomis.ovc.util.AppManager;
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
public class HivRiskAssessmentDaoImpl implements HivRiskAssessmentDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    String markedForDeleteQuery=" and hra.markedForDelete=0";
    public List getHivRiskAssessmentRecordsByLevel4OuId(String level4OuId) throws Exception
    {
        List mainList=new ArrayList();
        try
        {
            String query=SubQueryGenerator.getHheOvcOrganizationUnitHivRiskAssessmentQuery()+" and ou.ouPath like '%"+level4OuId+"%'"+markedForDeleteQuery+" order by hra.dateOfAssessment";
            System.err.println(query);
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null)
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
    public List getRecordsWithZeroAgeAtAssessment() throws Exception
    {
        List list=new ArrayList();
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from HivRiskAssessment hra where hra.ageAtAssessment=0";
            //System.err.println("query is "+query);
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
    public List getRiskAssessmentRecordsByOvcId(String ovcId) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            String query="from HivRiskAssessment hra where hra.ovcId=:uid";
            list = session.createQuery(query).setString("uid", ovcId).list();
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
    public void saveHivRiskAssessment(HivRiskAssessment hra) throws Exception
    {
        try
        {
            //System.err.println("hra.getChildAtRiskQuestion() in saveHivRiskAssessment(HivRiskAssessment hra) is "+hra.getChildAtRiskQuestion());
            if(hra !=null && hra.getDateOfAssessment() !=null && hra.getOvcId() !=null && hra.getChildAtRiskQuestion()>0)
            {
                HivRiskAssessment hra2=this.getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment());
                if(hra2==null)
                {
                    hra=HivRiskAssessmentManager.getHivRiskAssessmentWithCleanedStatus(hra);
                    hra=getHivRiskAssessmentWithAgeAtAssessment(hra);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.save(hra);
                    tx.commit();
                    closeSession(session);
                    //System.err.println("hra.getChildAtRiskQuestion() after save in saveHivRiskAssessment(HivRiskAssessment hra) is "+hra.getChildAtRiskQuestion());
                    //updateOvcHivStatusWithTestNotIndicated(hra);
                    processHivRiskAssessmentOutcome(hra);
                    System.err.println("hra.getOvcId() "+hra.getOvcId()+" saved");
                }
                
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void updateHivRiskAssessment(HivRiskAssessment hra) throws Exception
    {
        //System.err.println("hra.getChildAtRiskQuestion() in updateHivRiskAssessment(HivRiskAssessment hra) is "+hra.getChildAtRiskQuestion());
        try
        {
            if(hra !=null && hra.getDateOfAssessment() !=null && hra.getOvcId() !=null && hra.getChildAtRiskQuestion()>0)
            {
                HivRiskAssessment hra2=this.getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment());
                if(hra2 !=null)
                {
                    hra.setRecordId(hra2.getRecordId());
                    hra=HivRiskAssessmentManager.getHivRiskAssessmentWithCleanedStatus(hra);
                    hra=getHivRiskAssessmentWithAgeAtAssessment(hra);
                    hra.setDateCreated(hra2.getDateCreated());
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.update(hra);
                    tx.commit();
                    closeSession(session);
                    //System.err.println("hra.getChildAtRiskQuestion() after update in updateHivRiskAssessment(HivRiskAssessment hra) is "+hra.getChildAtRiskQuestion());
                    //updateOvcHivStatusWithTestNotIndicated(hra);
                    processHivRiskAssessmentOutcome(hra);
                    System.err.println("hra.getOvcId() "+hra.getOvcId()+" updated");
                }
            }
        }
        catch(Exception ex)
        {
          closeSession(session);  
          ex.printStackTrace();
        }
    }
    public void deleteHivRiskAssessment(HivRiskAssessment hra) throws Exception
    {
        try
        {
            if(hra !=null && hra.getDateOfAssessment() !=null && hra.getOvcId() !=null)
            {
                HivRiskAssessment hra2=this.getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment());
                if(hra2 !=null)
                {
                    hra.setRecordId(hra2.getRecordId());
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(hra);
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
    public void markRiskAssessmentRecordsForDelete(String ovcId) throws Exception
    {
        List list=this.getRiskAssessmentRecordsByOvcId(ovcId);
        if(list !=null && !list.isEmpty())
        {
            for(Object obj:list)
            {
                HivRiskAssessment hra=(HivRiskAssessment)obj;
                this.markedForDelete(hra);
            }
        }
    }
    public void deleteRiskAssessmentRecordsPerChild(String ovcId) throws Exception
    {
        List list=this.getRiskAssessmentRecordsByOvcId(ovcId);
        if(list !=null && !list.isEmpty())
        {
            for(Object obj:list)
            {
                HivRiskAssessment hra=(HivRiskAssessment)obj;
                deleteHivRiskAssessment(hra);
            }
        }
    }
    public void markedForDelete(HivRiskAssessment hra) throws Exception
    {
        try
        {
            if(hra !=null && hra.getDateOfAssessment() !=null && hra.getOvcId() !=null)
            {
                HivRiskAssessment hra2=this.getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment());
                if(hra2 !=null)
                {
                    hra.setRecordId(hra2.getRecordId());
                    hra2.setMarkedForDelete(1);
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(hra2);
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
    public HivRiskAssessment getHivRiskAssessment(int recordId) throws Exception
    {
       HivRiskAssessment hra=null;
       try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from HivRiskAssessment hra where hra.recordId=:id").setInteger("id", recordId).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                hra=(HivRiskAssessment)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
       return hra;
    }
    public HivRiskAssessment getHivRiskAssessment(String ovcId,Date dateOfAssessment) throws Exception
    {
       HivRiskAssessment hra=null;
       try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from HivRiskAssessment hra where hra.ovcId=:id and hra.dateOfAssessment=:doa").setString("id", ovcId).setDate("doa", dateOfAssessment).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                hra=(HivRiskAssessment)list.get(0);
            }
            
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
       return hra;
    }
    public void updateOvcHivStatusWithTestNotIndicated(HivRiskAssessment hra) throws Exception
    {
        DaoUtility util=new DaoUtility();
        Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(hra.getOvcId());
        if(ovc !=null)
        {
            int childAtRisk=hra.getChildAtRiskQuestion();
            System.err.println("childAtRisk in updateOvcHivStatusWithTestNotIndicated is "+childAtRisk);
            if(childAtRisk==AppConstant.CHILD_AT_LOW_RISK_NUM || childAtRisk==AppConstant.CHILD_NOT_AT_RISK_NUM)
            {
                if(DateManager.compareDates(ovc.getDateOfCurrentHivStatus(), hra.getDateOfAssessment()));
                {
                    ovc.setCurrentHivStatus(AppConstant.HIV_TEST_NOT_INDICATED_NUM);
                    ovc.setDateOfCurrentHivStatus(hra.getDateOfAssessment());
                    util.getChildEnrollmentDaoInstance().updateOvc(ovc, true, false);
                }
            }
        }
    }
    public void processHivRiskAssessmentOutcome(HivRiskAssessment hra) throws Exception
    {
        HivStatusManager hsm=new HivStatusManager();
        //Process only for assessments done after 30/09/2018
        if(hra !=null && hra.getDateOfAssessment().after(DateManager.getDateInstance("2018-09-30")))
        {
            HivStatusManagerDao hsmdao=new HivStatusManagerDaoImpl();
            hsm.setBeneficiaryId(hra.getOvcId());
            hsm.setDateCreated(hra.getDateCreated());
            hsm.setEnrolledOnTreatment(0);
            if(hsm.getDateEnrolledOnTreatment()==null)
            hsm.setDateEnrolledOnTreatment(DateManager.getDefaultStartDateInstance());
            hsm.setDateOfNewStatus(hra.getDateOfAssessment());
            hsm.setFacilityId(null);
            hsm.setLastModifiedDate(hra.getLastModifiedDate());
            hsm.setRecordedBy(hra.getRecordedBy());
            hsm.setBeneficiaryType(AppConstant.OVC_TYPE_NUM);
            
            if(hra.getChildAtRiskQuestion()==AppConstant.CHILD_AT_LOW_RISK_NUM || hra.getChildAtRiskQuestion()==AppConstant.CHILD_NOT_AT_RISK_NUM)
            {
                //Test not indicated is for HIV unknown beneficiaries only.
                if(hra.getHivStatusAtRiskAssessment()==AppConstant.HIV_UNKNOWN_NUM || hra.getHivStatusAtRiskAssessment()==AppConstant.HIV_UNDISCLOSED_NUM)
                hsm.setNewHivStatus(AppConstant.HIV_TEST_NOT_INDICATED_NUM);
                else //if the beneficiary is not unknown, then the HIV status will not change when he/she is at low risk
                hsm.setNewHivStatus(hra.getHivStatusAtRiskAssessment());
            }
            else
            {
                if(hra.getHivStatusAtRiskAssessment()==AppConstant.HIV_POSITIVE_NUM)
                hsm.setNewHivStatus(AppConstant.HIV_POSITIVE_NUM);
                else
                hsm.setNewHivStatus(AppConstant.HIV_TEST_REQUIRED_NUM);
            }
            hsmdao.saveHivStatusManager(hsm, true);
        }
    }
    public List getHivRiskAssessmentRecords(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOvcOrganizationUnitHivRiskAssessmentQuery()+additionalOrgUnitQuery+sqg.getHivRiskAssessmentDateQuery(rpt.getStartDate(),rpt.getEndDate())+markedForDeleteQuery;
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
    public List getHivRiskAssessmentRecordsForExport(ReportParameterTemplate rpt) throws Exception
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
            String query=SubQueryGenerator.getHheOvcOrganizationUnitHivRiskAssessmentQuery()+additionalOrgUnitQuery+sqg.getHivRiskAssessmentLastModifiedDateQuery(rpt.getStartDate(),rpt.getEndDate());
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
    public List getAllHivRiskAssessments() throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from HivRiskAssessment hra where hra.ovcId is not null"+markedForDeleteQuery).list();
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
    public HivRiskAssessment getHivRiskAssessmentWithAgeAtAssessment(HivRiskAssessment hra) throws Exception
    {
        DaoUtility util=new DaoUtility();
        Ovc ovc=util.getChildEnrollmentDaoInstance().getOvc(hra.getOvcId());
        if(ovc !=null)
        {
            
            hra.setAgeAtRiskAssessment(ovc.getCurrentAge());
            hra.setAgeUnitAtRiskAssessment(ovc.getCurrentAgeUnit());
            //hra.setAgeAtAssessment(ovc.getCurrentAge());
            //hra.setAgeUnitAtAssessment(ovc.getCurrentAgeUnitCode());
            System.err.println(" HivRiskAssessment record updated with age at assessment "+hra.getAgeAtRiskAssessment()+" and age unit ("+hra.getAgeUnitAtRiskAssessment()+")");
        }
        return hra;
    }
    public void changeOvcIdInHivRiskAssessmentRecords(String oldOvcId, String newOvcId) throws Exception
    {
        List list=getRiskAssessmentRecordsByOvcId(oldOvcId);
        if(list !=null)
        {
            for(Object obj:list)
            {
                HivRiskAssessment hra=(HivRiskAssessment)obj;
                hra.setOvcId(newOvcId);
                if(getHivRiskAssessment(hra.getOvcId(), hra.getDateOfAssessment())==null)
                saveHivRiskAssessment(hra);
                else
                updateHivRiskAssessment(hra);
                System.err.println("OVC Id in HivRiskAssessment changed from "+oldOvcId+" to "+newOvcId);
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
