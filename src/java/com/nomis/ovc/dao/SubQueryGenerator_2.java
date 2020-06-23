/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.dao;

/**
 *
 * @author smomoh
 */
public class SubQueryGenerator_2
{
    public static String getUserActivitySubQuery(String userName,String userAction)
    {
        String query="";
        if((userName !=null && !userName.equalsIgnoreCase("All")))
        query+=" and uat.userName='"+userName+"'";
        if(userAction !=null && !userAction.equalsIgnoreCase("All"))
        query+=" and uat.userAction='"+userAction+"'";
        return query;
    }
}
