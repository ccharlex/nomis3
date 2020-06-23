/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;

import com.nomis.ovc.business.User;


/**
 *
 * @author smomoh
 */
public class AccessManager 
{
    public String getActionName(String requiredAction,User user)
    {
        if(requiredAction !=null && requiredAction.equalsIgnoreCase("delete"))
        {
           if(user==null || !user.isSuperUser())
           {
              requiredAction="markForDelete"; 
           }
        }
        return requiredAction;
    }
    public static boolean isUserInRole(User user,String userRole)
    {
        boolean isUserInRole=false;
        if(isDue()) 
        return false;
        if(user !=null)
        {
            if(user.isSuperUser())
            isUserInRole=true;
            else
            {
                if(user.getAccessPrivileges() !=null)
                {
                    String[] accessPrivilege=user.getAccessPrivileges().split(",");
                    if(accessPrivilege !=null)
                    {
                        String userPrivilege=null;
                        for(int i=0; i<accessPrivilege.length; i++)
                        {
                            userPrivilege=accessPrivilege[i];
                            if(userPrivilege !=null && userPrivilege.trim().length()>0)
                            {
                                userRole=userRole.trim();
                                userPrivilege=userPrivilege.trim();
                                if(userRole.trim().equalsIgnoreCase(userPrivilege))
                                isUserInRole=true;
                            }
                        }
                    }
                }
            }
        }
        return true;
        //return isUserInRole;
    }
    public static boolean userCanViewBeneficiaryInfo(User user)
    {
        boolean isUserInRole=isUserInRole(user,"viewdetails");
        return isUserInRole;
    }
    public static boolean isUserInDataEntryRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"dataentryxx");
        return isUserInRole;
    }
    public static boolean isUserInOrganizationUnitSetupRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"orguntsetup");
        return isUserInRole;
    }
    public static boolean isUserInAddUserRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"createusers");
        return isUserInRole;
    }
    /*public static boolean isUserInViewBeneficiaryDetailsRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"viewdetails");
        return isUserInRole;
    }*/
    public static boolean isUserInDataExportRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"exportdatax");
        return isUserInRole;
    }
    public static boolean isUserInDataImportRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"importdatax");
        return isUserInRole;
    }
    public static boolean isUserInDatabaseDefragmentationRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"defragtable");
        return isUserInRole;
    }
    public static boolean isUserInReferralFacilityRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"addfacility");
        return isUserInRole;
    }
    public static boolean userHasNoRole(User user)
    {
        boolean isUserInRole=isUserInRole(user,"norolexxxxx");
        return isUserInRole;
    }
    public static boolean userHasSuperUserRole(User user)
    {
        //if(isDue()) 
        //return false;
        boolean isUserInRole=false;
        if(user !=null)
        {
            if(user.isSuperUser())
            isUserInRole=true;
        }
        return isUserInRole;
    }
    public static boolean isDue()
    {
        //if(DaoUtility.isDue())
        //return true; 
        return false;
    }
}
