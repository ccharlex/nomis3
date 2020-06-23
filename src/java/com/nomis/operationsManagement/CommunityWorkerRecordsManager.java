/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;

import com.nomis.ovc.dao.DaoUtility;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smomoh
 */
public class CommunityWorkerRecordsManager 
{
    public static void setEnumeratorsRegistrationList(HttpSession session) throws Exception
    {
        DaoUtility util=new DaoUtility();
        session.removeAttribute("enumeratorList");
        List list=util.getCommunityWorkerDaoInstance().getAllCommunityWorkers();
        if(list !=null && !list.isEmpty())
        {
            session.setAttribute("enumeratorList", list);
        }
    }
}
