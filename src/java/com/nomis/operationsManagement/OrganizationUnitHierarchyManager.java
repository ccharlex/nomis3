/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;

import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnitHierarchy;
import java.io.Serializable;

/**
 *
 * @author smomoh
 */
public class OrganizationUnitHierarchyManager implements Serializable
{
    DaoUtility util=new DaoUtility();
    public static OrganizationUnitHierarchy validOuHierarchyName(String ouHierarchyName)
    {
        DaoUtility util=new DaoUtility();
        OrganizationUnitHierarchy ouh=null;
        try
        {
            ouh=util.getOrganizationUnitHierarchyDaoInstance().getOrganizationUnitHierarchyByName(ouHierarchyName);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ouh;
    }
}
