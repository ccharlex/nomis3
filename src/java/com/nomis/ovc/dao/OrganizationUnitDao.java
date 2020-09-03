/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

import com.nomis.ovc.metadata.OrganizationUnit;
import java.util.List;

/**
 *
 * @author smomoh
 */
public interface OrganizationUnitDao 
{
    public void saveOrganizationUnit(OrganizationUnit ou) throws Exception;
    public void updateOrganizationUnit(OrganizationUnit ou) throws Exception;
    public void deleteOrganizationUnit(OrganizationUnit ou) throws Exception;
    public OrganizationUnit getOrganizationUnit(String uid) throws Exception;
    public OrganizationUnit getOrganizationUnitByName(String ouname) throws Exception;
    public List getAllOrganizationUnit() throws Exception;
    public List getOrganizationUnitsByOuLevel(int oulevel) throws Exception;
    public List getOrganizationUnityByParentId(String pid) throws Exception;
    public OrganizationUnit getOrganizationUnitByNameAndLevel(String ouname,int level) throws Exception;
    public OrganizationUnit createWard(OrganizationUnit parentOu,String wardName) throws Exception;
    public OrganizationUnit getOrganizationUnitByParentIdAndChildName(String pid,String ouname) throws Exception;
    public List getParentOuList(List ouList) throws Exception;
}
