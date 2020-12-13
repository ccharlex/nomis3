/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;


import com.nomis.ovc.business.School;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
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
public class SchoolDaoImpl implements SchoolDao
{
    Session session;
    Transaction tx;
    List list;
    public List getAllSchools() throws Exception
    {
        List list=null;
        try
        {
            session=HibernateUtil.getSession();
            tx=session.beginTransaction();
            list=session.createQuery("from School school order by school.schoolName").list();
            tx.commit();
            closeSession(session);
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
        return list;
    }
    public School getSchoolsBySchoolName(String schoolName) throws Exception
    {
        School school=null;
        try
        {
            if(schoolName !=null)
            schoolName=schoolName.trim();
            session=HibernateUtil.getSession();
            tx=session.beginTransaction();
            list=session.createQuery("from School school where TRIM(school.schoolName)=:sn").setString("sn", schoolName).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                school=(School)list.get(0);
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
        return school;
    }
    public List getSchoolsByOrgUnit(ReportParameterTemplate rpt) throws Exception
    {
        List schoolList=new ArrayList();
        try
        {
            SubQueryGenerator sqg=new SubQueryGenerator();
            String additionalOrgUnitQuery="";
            if(rpt !=null && rpt.getLevel2OuId() !=null && rpt.getLevel2OuId().trim().length()>0 && !rpt.getLevel2OuId().equalsIgnoreCase("select") && !rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                additionalOrgUnitQuery=sqg.getOrganizationUnitQuery(rpt);
            }
            String query=SubQueryGenerator.getSchoolOrganizationUnitQuery()+additionalOrgUnitQuery+" order by school.schoolName";
            System.err.println(query);
            session=HibernateUtil.getSession();
            tx=session.beginTransaction();
            List list=session.createQuery(query).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                for(Object obj:list)
                {
                    Object[] objArray=(Object[])obj;
                    schoolList.add(objArray[0]);
                }
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
            throw new Exception(ex);
        }
        return schoolList;
    }
    public List getSchoolsByOrgUnit(String orgunitId) throws Exception
    {
        List list=null;
        try
        {
            session=HibernateUtil.getSession();
            tx=session.beginTransaction();
            list=session.createQuery("from School school where school.orgunitId=:ouid").setString("ouid", orgunitId).list();
            tx.commit();
            closeSession(session);
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
        return list;
    }
    public School getSchool(String id) throws Exception
    {
        School school=null;
        try
        {
            session=HibernateUtil.getSession();
            tx=session.beginTransaction();
            List list=session.createQuery("from School school where school.id= :sid").setString("sid", id).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                school=(School)list.get(0);
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
            ex.printStackTrace();
        }
        return school;
    }
    public void saveSchool(School school) throws Exception
    {
        try
        {
            System.err.println("Inside SchoolDao savedSchool 1");
            if(school !=null && school.getSchoolName() !=null && school.getOrgUnitId() !=null
                    && school.getDateCreated()  !=null && school.getLastModifiedDate() !=null)
            {
                System.err.println("Inside SchoolDao savedSchool 2");
                if(getSchool(school.getId()) ==null && getSchoolsBySchoolName(school.getSchoolName())==null)
                {
                    school.setId(getUniqueRecordId());
                    session=HibernateUtil.getSession();
                    tx=session.beginTransaction();
                    session.save(school);
                    tx.commit();
                    closeSession(session);
                    System.err.println("School info saved");
                }
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
    }
    public void updateSchool(School school) throws Exception
    {
        try
        {
            if(school !=null && school.getId() !=null && school.getSchoolName() !=null)
            {
                if(getSchool(school.getId()) !=null)
                {
                    School schoolByName=this.getSchoolsBySchoolName(school.getSchoolName());
                    if(schoolByName ==null || schoolByName.getId().equalsIgnoreCase(school.getId()))
                    {
                        if(school.getOrgUnitId() !=null && school.getDateCreated()  !=null && school.getLastModifiedDate() !=null)
                        {
                            session=HibernateUtil.getSession();
                            tx=session.beginTransaction();
                            session.update(school);
                            tx.commit();
                            closeSession(session);
                            System.err.println("School info updated");
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
    }
    public void markForDelete(School school) throws Exception
    {
        try
        {
            if(school !=null && school.getId() !=null && school.getSchoolName() !=null)
            {
                School existingSchool=getSchool(school.getId());
                if(existingSchool !=null)
                {
                    existingSchool.setMarkForDelete(1);
                    session=HibernateUtil.getSession();
                    tx=session.beginTransaction();
                    session.update(existingSchool);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
    }
    public void deleteSchool(School school) throws Exception
    {
        try
        {
            if(school !=null && school.getId() !=null)
            {
                if(getSchool(school.getId()) !=null)
                {
                    session=HibernateUtil.getSession();
                    tx=session.beginTransaction();
                    session.delete(school);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
        catch(Exception ex)
        {
            closeSession(session);
        }
    }
    public String getUniqueRecordId()
    {
        AppUtility appUtil=new AppUtility();
        String uniqueId=appUtil.generateUniqueId();
        try
        {
            if(getSchool(uniqueId) !=null)
            getUniqueRecordId();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return uniqueId;
    }
    public School createDefaultSchool(String orgUnitId,String userName) throws Exception
    {
        
        School school=getSchoolsBySchoolName("N/A");
        if(school==null)
        {
            Date currentDate=DateManager.getDateInstance(DateManager.getCurrentDate());
            school=new School();
            school.setSchoolName("N/A");
            school.setSchoolType(0);
            school.setDateCreated(currentDate);
            school.setLastModifiedDate(currentDate);
            school.setOrgUnitId(orgUnitId);
            school.setRecordedBy(userName);
            school.setId(getUniqueRecordId());
            saveSchool(school);
        }
        return school;
    }
    public School createSchool(String name,String orgUnitId,String userName,int type) throws Exception
    {
        School school=null;
        Date currentDate=DateManager.getDateInstance(DateManager.getCurrentDate());
        if(name==null || name.trim().length()==0)
        {
            school=createDefaultSchool(orgUnitId,userName);
            return school;
        }
        
        school=getSchoolsBySchoolName(name);
        if(school==null)
        {
            school=new School();
            school.setSchoolName(name);
            school.setSchoolType(type);
            school.setDateCreated(currentDate);
            school.setLastModifiedDate(currentDate);
            school.setOrgUnitId(orgUnitId);
            school.setRecordedBy(userName);
            school.setId(getUniqueRecordId());
            saveSchool(school);
        }
        return school;
    }
    public void closeSession(Session session)
    {
        if(session !=null && session.isOpen())
        {
            session.close();
        }
    }
}
