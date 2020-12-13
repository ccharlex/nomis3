/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class BeneficiaryStatusManager 
{
    private BeneficiaryStatus getActiveStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.ACTIVE_NUM);
        bstatus.setStatusName("Active");
        bstatus.setDescription("Beneficiary is in the program and receiving services");
        return bstatus;
    }
    private BeneficiaryStatus getGraduatedStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.GRADUATED_NUM);
        bstatus.setStatusName("Graduated");
        bstatus.setDescription("Beneficiary has graduated from the program");
        return bstatus;
    }
    private BeneficiaryStatus getMigratedStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.MIGRATED_NUM);
        bstatus.setStatusName("Migrated");
        bstatus.setDescription("Beneficiary has migrated from the place of enrollment to another location");
        return bstatus;
    }
    private BeneficiaryStatus getLostToFollowupStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.LOST_TO_FOLLOWUP_NUM);
        bstatus.setStatusName("Lost to follow-up");
        bstatus.setDescription("Beneficiary is declared lost to follow-up and cannot be located after 3 months");
        return bstatus;
    }
    private BeneficiaryStatus getDeathStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.DIED_NUM);
        bstatus.setStatusName("Died");
        bstatus.setDescription("Beneficiary is confirmed to have died");
        return bstatus;
    }
    private BeneficiaryStatus getAgedOutStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.AGED_OUT_NUM);
        bstatus.setStatusName("Aged out");
        bstatus.setDescription("Beneficiary is withdrawn from the program because he has aged above 17 years");
        return bstatus;
    }
    private BeneficiaryStatus getReenrolledStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.REENROLLED_NUM);
        bstatus.setStatusName("Re-enrolled");
        bstatus.setDescription("Beneficiary is enrolled back into the program after been exited from the program");
        return bstatus;
    }
    private BeneficiaryStatus getVoluntarilyWithdrawnStatus()
    {
        BeneficiaryStatus bstatus=new BeneficiaryStatus();
        bstatus.setStatusCode(AppConstant.VOLUNTARILY_WITHDRAWN_NUM);
        bstatus.setStatusName("Voluntarily withdrawn");
        bstatus.setDescription("Beneficiary voluntarily withdrawn from the program");
        return bstatus;
    }
    public List getAllBeneficiaryStatus()
    {
        List statusList=new ArrayList();
        statusList.add(getActiveStatus());
        statusList.add(getGraduatedStatus());
        statusList.add(getMigratedStatus());
        statusList.add(getLostToFollowupStatus());
        statusList.add(getDeathStatus());
        statusList.add(getAgedOutStatus());
        statusList.add(getReenrolledStatus());
        statusList.add(getVoluntarilyWithdrawnStatus());
        return statusList;
    }
    public List getBeneficiaryStatusListForChildren()
    {
        List statusList=new ArrayList();
        statusList.add(getAgedOutStatus());
        statusList.add(getDeathStatus());
        statusList.add(getLostToFollowupStatus());
        statusList.add(getMigratedStatus());
        statusList.add(getVoluntarilyWithdrawnStatus());
        return statusList;
    }
    public List getBeneficiaryStatusListForAdults()
    {
        List statusList=new ArrayList();
        statusList.add(getDeathStatus());
        statusList.add(getLostToFollowupStatus());
        statusList.add(getMigratedStatus());
        statusList.add(getVoluntarilyWithdrawnStatus());
        return statusList;
    }
}
