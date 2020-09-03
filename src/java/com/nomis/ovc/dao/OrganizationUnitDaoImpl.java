/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.fhi.nomis.logs.NomisLogManager;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.ovc.util.AppUtility;
import com.nomis.ovc.util.DateManager;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author smomoh
 */
public class OrganizationUnitDaoImpl implements OrganizationUnitDao
{
    Session session;
    Transaction tx;
    SessionFactory sessions;
    public List getParentOuList(List ouList) throws Exception
    {
        List parentOuList=new ArrayList();
        try
        {
            DaoUtility util=new DaoUtility();
            OrganizationUnit parentOu=null;
            OrganizationUnit ou=null;
            if(ouList !=null)
            {
                String ouId=null;
                for(Object obj:ouList)
                {
                    ouId=(String)obj;
                    ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ouId);
                    if(ou !=null)
                    {
                        parentOu=util.getOrganizationUnitDaoInstance().getOrganizationUnit(ou.getParentId());
                        if(parentOu !=null)
                        {
                            parentOuList.add(parentOu);
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            NomisLogManager.logStackTrace(ex);
        }
        return parentOuList;
    }
    public void saveOrganizationUnit(OrganizationUnit ou) throws Exception
    {
        try
        {
            if(ou !=null && this.getOrganizationUnitByName(ou.getName()) ==null)
            {
                OrganizationUnit existingOu=null;
                if(ou.getOucode() !=null)
                {
                    //All National, State, LGA (Levels 1,2 and 3) codes must be unique at their levels. Others below are only required to be unique at parent org unit level
                    if(ou.getOulevel()<4)
                    existingOu=getOrganizationUnitByOuCodeAndOuLevel(ou.getOucode(),ou.getOulevel());
                    else
                    existingOu=getOrganizationUnitByOuCodeAndParentId(ou.getOucode(),ou.getParentId());
                }
                if(existingOu==null)
                {
                    if(ou.getUid()==null || ou.getUid().trim().length() !=11)
                    {
                        String uid=getUniqueRecordId();
                        ou.setUid(uid);
                    }
                    if(ou.getOulevel()==1)
                    ou.setParentId(ou.getUid());
                    ou.setOuPath(getOuPath(ou));
                    if(ou.getDateCreated()==null)
                    ou.setDateCreated(DateManager.getCurrentDateInstance());
                    if(ou.getLastModifiedDate()==null)
                    ou.setLastModifiedDate(DateManager.getCurrentDateInstance());
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.save(ou);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
    }
    private void updateOrganizationUnitOnly(OrganizationUnit ou) throws Exception
    {
        try
        {
            if(ou !=null && this.getOrganizationUnit(ou.getUid()) !=null)
            {
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                session.update(ou);
                tx.commit();
                closeSession(session);
                //System.err.println(ou.getName()+" updated in updateOrganizationUnitOnly(OrganizationUnit ou");
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
    }
    public void updateOrganizationUnit(OrganizationUnit ou) throws Exception
    {
        try
        {
            if(ou !=null && this.getOrganizationUnit(ou.getUid()) !=null)
            {
                if(ou.getUid() !=null && ou.getOuPath() !=null && ou.getParentId() !=null && ou.getName() !=null)
                {
                    ou.setOuPath(getOuPath(ou));
                    OrganizationUnit ou2=getOrganizationUnitByName(ou.getName());
                    if(ou2 !=null)
                    {
                        if(ou.getUid() !=null && ou.getUid().equalsIgnoreCase(ou2.getUid()))
                        {
                            if(ou.getOulevel()==1 && ou.getParentId()==null)
                            ou.setParentId(ou.getUid());
                            session = HibernateUtil.getSession();
                            tx = session.beginTransaction();
                            session.update(ou);
                            tx.commit();
                            closeSession(session);
                            updateChildrenPath(ou);
                        }
                    }
                    else
                    {
                        if(getOrganizationUnit(ou.getUid()) !=null)
                        {
                            if(ou.getOulevel()==1 && ou.getParentId()==null)
                            ou.setParentId(ou.getUid());
                            session = HibernateUtil.getSession();
                            tx = session.beginTransaction();
                            session.update(ou);
                            tx.commit();
                            closeSession(session);
                            updateChildrenPath(ou);
                        }
                    }
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
    }
    public void updateChildrenPath(OrganizationUnit ou) throws Exception
    {
        List list=this.getOrganizationUnityByParentId(ou.getUid());
        //System.err.println("Inside updateChildrenPath(OrganizationUnit ou)");
        if(list !=null && !list.isEmpty())
        {
            //System.err.println("list.size() is "+list.size());
            for(Object obj:list)
            {
                OrganizationUnit ou2=(OrganizationUnit)obj;
                ou2.setParentId(ou.getUid());
                ou2.setOuPath(this.getOuPath(ou2));
                //ou2.setOuPath(ou.getOuPath()+"\\"+ou2.getUid());
                updateOrganizationUnitOnly(ou2);
            }
        }
    }
    public void deleteOrganizationUnit(OrganizationUnit ou) throws Exception
    {
        try
        {
            if(ou !=null)
            {
                OrganizationUnit ou2=getOrganizationUnit(ou.getUid());
                if(ou2 !=null)
                {
                    session = HibernateUtil.getSession();
                    tx = session.beginTransaction();
                    session.delete(ou);
                    tx.commit();
                    closeSession(session);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
    }
    public String getOuPath(OrganizationUnit ou) throws Exception
    {
        String oupath=null;
        //String fileSeperator="/";
        if(ou !=null)
        {
            if(ou.getOulevel()==1)
            oupath=ou.getUid();
            else
            {
               OrganizationUnit parent=getOrganizationUnit(ou.getParentId());
               //System.err.println("parent is "+parent);
               if(parent !=null && parent.getOuPath() !=null)
               {
                    oupath=parent.getOuPath()+"/"+ou.getUid();
                    System.err.println("parent.getOuPath() is "+parent.getOuPath());
               }
            }
            //System.err.println("oupath is "+oupath);
        }
        return oupath;
    }
    /*public String getOuPath(OrganizationUnit ou) throws Exception
    {
        String oupath=null;
        if(ou !=null)
        {
            //AppUtility appUtil=new AppUtility();
            OrganizationUnit parent=null;
            OrganizationUnit ou2=null;
            String fileSeperator="/";//appUtil.getFileSeperator();
            String parentId=ou.getParentId();
            String path=ou.getUid();
            //System.err.println("Uid is "+ou.getUid());
            //System.err.println("parentId is "+parentId);
            ou2=ou;
            while(!ou2.getUid().equalsIgnoreCase(parentId))
            {
                path=parentId+fileSeperator+path;
                System.err.println("path is "+path);
                parent=getOrganizationUnit(parentId);
                if(parent !=null)
                {
                    ou2=parent;
                    parentId=ou2.getParentId();
                    //path=ou2.getUid()+fileSeperator+path;
                    
                }
            }
            oupath=path;
        }
        return oupath;
    }*/
    public OrganizationUnit getOrganizationUnit(String uid) throws Exception
    {
        OrganizationUnit ou=null;
        try
        {
            if(uid !=null)
            {
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                List list = session.createQuery("from OrganizationUnit ou where ou.uid=:id").setString("id", uid).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    ou=(OrganizationUnit)list.get(0);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public OrganizationUnit getOrganizationUnitByOuCodeAndOuLevel(String oucode,int ouLevel) throws Exception
    {
        OrganizationUnit ou=null;
        try
        {
            if(oucode !=null)
            {
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                List list = session.createQuery("from OrganizationUnit ou where ou.oucode=:cd and ou.oulevel=:oul").setString("cd", oucode).setInteger("oul", ouLevel).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    ou=(OrganizationUnit)list.get(0);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public OrganizationUnit getOrganizationUnitByOuCodeAndParentId(String oucode,String parentId) throws Exception
    {
        OrganizationUnit ou=null;
        try
        {
            if(oucode !=null)
            {
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                List list = session.createQuery("from OrganizationUnit ou where ou.oucode=:cd and ou.parentId=:pid").setString("cd", oucode).setString("pid", parentId).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    ou=(OrganizationUnit)list.get(0);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public OrganizationUnit getOrganizationUnitByName(String ouname) throws Exception
    {
        OrganizationUnit ou=null;
        try
        {
            if(ouname !=null)
            {
                ouname=ouname.trim().toUpperCase();
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                List list = session.createQuery("from OrganizationUnit ou where UPPER(ou.name)=:oun").setString("oun", ouname).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    ou=(OrganizationUnit)list.get(0);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public OrganizationUnit getOrganizationUnitByNameAndLevel(String ouname,int level) throws Exception
    {
        OrganizationUnit ou=null;
        try
        {
            if(ouname !=null)
            {
                ouname=ouname.trim().toUpperCase();
                session = HibernateUtil.getSession();
                tx = session.beginTransaction();
                List list = session.createQuery("from OrganizationUnit ou where UPPER(ou.name)=:oun and ou.oulevel=:lv").setString("oun", ouname).setInteger("lv", level).list();
                tx.commit();
                closeSession(session);
                if(list !=null && !list.isEmpty())
                {
                    ou=(OrganizationUnit)list.get(0);
                }
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public List getAllOrganizationUnit() throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from OrganizationUnit ou").list();
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
    public List getOrganizationUnitsByOuLevel(int oulevel) throws Exception
    {
       List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from OrganizationUnit ou where ou.oulevel=:lv order by ou.name").setInteger("lv", oulevel).list();
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
    public OrganizationUnit getOrganizationUnitByParentIdAndChildName(String pid,String ouname) throws Exception
    {
       OrganizationUnit ou=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("from OrganizationUnit ou where ou.parentId=:pid and ou.name=:oun").setString("pid", pid).setString("oun", ouname).list();
            tx.commit();
            closeSession(session);
            if(list !=null && !list.isEmpty())
            {
                ou=(OrganizationUnit)list.get(0);
            }
        }
         catch (Exception ex)
         {
             closeSession(session);
            throw new Exception(ex);
         }
        return ou;
    }
    public List getOrganizationUnityByParentId(String pid) throws Exception
    {
        List list=null;
        try
        {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            list = session.createQuery("from OrganizationUnit ou where ou.parentId=:pid").setString("pid", pid).list();
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
    public OrganizationUnit createWard(OrganizationUnit parentOu,String wardName) throws Exception
    {
        OrganizationUnit ward=null;
        if(parentOu !=null && wardName !=null)
        {
            //System.err.println("Parent name is "+parentOu.getName()+" and ward name "+wardName);
            
                ward=getOrganizationUnitByParentIdAndChildName(parentOu.getUid(),wardName);
                if(ward==null)
                {
                    ward=new OrganizationUnit();
                    ward.setUid(getUniqueRecordId());
                    ward.setDateCreated(DateManager.getCurrentDateInstance());
                    ward.setLastModifiedDate(DateManager.getCurrentDateInstance());
                    ward.setName(wardName);
                    ward.setOuPath(this.getOuPath(ward)); 
                    //ward.setOuPath(parentOu.getOuPath()+"\\"+ward.getUid());
                    ward.setOulevel(parentOu.getOulevel()+1);
                    ward.setParentId(parentOu.getUid());
                    saveOrganizationUnit(ward);
                    //System.err.println("ward with name "+ward.getName()+" created");
                }
            
        }
        return ward;
    }
    public String getUniqueRecordId()
    {
        AppUtility appUtil=new AppUtility();
        String uniqueId=appUtil.generateUniqueId(11);
        try
        {
            if(this.getOrganizationUnit(uniqueId) !=null)
            getUniqueRecordId();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return uniqueId;
    }
    public void closeSession(Session session)
    {
        if(session !=null && session.isOpen())
        {
            session.close();
        }
    }
}
