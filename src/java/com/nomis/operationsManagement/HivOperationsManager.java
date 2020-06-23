/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;

import com.nomis.ovc.business.Beneficiary;
import com.nomis.ovc.util.AppConstant;

/**
 *
 * @author smomoh
 */
public class HivOperationsManager 
{
    public Beneficiary processBeneficiaryHivStatus(Beneficiary beneficiary)
    {
        //if current hiv status is not positive, beneficiary cannot be on treatment. Set facility as null
        if(beneficiary.getCurrentHivStatus() !=AppConstant.HIV_POSITIVE_NUM)
        {
            beneficiary.setEnrolledOnTreatment(0);
            beneficiary.setHivTreatmentFacilityId(null);
        }
        else
        {
            //if current hiv status is positive but beneficiary is not on treatment, set facility as null 
            if(beneficiary.getEnrolledOnTreatment() !=AppConstant.ENROLLED_ON_TREATMENT_YES_NUM)
            beneficiary.setHivTreatmentFacilityId(null);
        }
        return beneficiary;
    }
    public boolean isHivStatusKnownAtBaseline(Beneficiary beneficiary)
    {
        boolean hivStatusKnownAtBaseline=true;
        if((beneficiary.getBaselineHivStatus()==AppConstant.HIV_POSITIVE_NUM || beneficiary.getBaselineHivStatus()==AppConstant.HIV_NEGATIVE_NUM) && beneficiary.getCurrentHivStatus() ==AppConstant.HIV_UNKNOWN_NUM)
        {
            hivStatusKnownAtBaseline=false;
        }
        return hivStatusKnownAtBaseline;
    }
    public boolean isBaselineHivStatusPositive(Beneficiary beneficiary)
    {
        boolean baselineHivStatusPositive=true;
        if((beneficiary.getBaselineHivStatus()==AppConstant.HIV_POSITIVE_NUM) && beneficiary.getCurrentHivStatus() !=AppConstant.HIV_POSITIVE_NUM)
        {
            baselineHivStatusPositive=false;
        }
        return baselineHivStatusPositive;
    }
}
