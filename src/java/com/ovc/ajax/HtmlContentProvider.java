/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ovc.ajax;


import com.nomis.ovc.business.AdultHouseholdMember;
import com.nomis.ovc.business.School;
import com.nomis.ovc.business.SiteSetup;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.reports.utils.ReportParameterTemplate;
import java.util.List;

/**
 *
 * @author smomoh
 */
public class HtmlContentProvider 
{
   /* public String getChildrenSearchResult(String nameToSearch,ReportParameterTemplate rpt,String userName)
    {
        String returnValue=null;
        try
        {
            DaoUtility util=new DaoUtility();
            if(rpt.getLevel2OuId()==null || rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                SiteSetup setup=util.getSiteSetupDaoInstance().getSiteSetup(userName);
                if(setup !=null)
                {
                    rpt.setLevel2OuId(setup.getOrgUnitId());
                }
            }
            List list=util.getOvcDaoInstance().searchOvcByPartOfName(rpt,nameToSearch);
            if(list !=null && !list.isEmpty())
            {
                String html="<table border='1' style='border-collapse:collapse; background-color:white'>";
                html+="<tr><td>OVC Id</td><td style='width:250px'>Child name</td><td> </td></tr>";
                Ovc ovc=null;
                String hhdetails=null;
                String firstName=null;
                String surname=null;
                for(Object obj:list)
                {
                    ovc=(Ovc)obj;
                    hhdetails=getConcatenatedBeneficiaryDetails(ovc.getHhUniqueId(),ovc.getFirstName(),ovc.getSurname());
                    hhdetails=ovc.getOvcId()+"; "+firstName+" "+surname;
                    html+="<tr><td>"+ovc.getOvcId()+"</td><td>"+ovc.getFirstName()+" "+ovc.getSurname()+"</td><td><input type='button' name='detailsBtn' value='Load details' onclick=\"loadHouseholdDetails('"+hhdetails+"')\"/> </td></tr>";
                    returnValue=ovc.getFirstName()+" "+ovc.getSurname();
                    //System.err.println("list.get(0) is "+returnValue);
                }
                html+="</table>";
                returnValue=html;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return returnValue;
    }*/
    public String getHouseholdSearchResult(String nameToSearch,ReportParameterTemplate rpt,String userName)
    {
        String returnValue=null;
        try
        {
            DaoUtility util=new DaoUtility();
            if(rpt.getLevel2OuId()==null || rpt.getLevel2OuId().equalsIgnoreCase("All"))
            {
                SiteSetup setup=util.getSiteSetupDaoInstance().getSiteSetup(userName);
                if(setup !=null)
                {
                    rpt.setLevel2OuId(setup.getOrgUnitId());
                }
            }
            List list=util.getAdultHouseholdMemberDaoInstance().searchHouseholdProfileByPartOfName(rpt,nameToSearch);
            if(list !=null && !list.isEmpty())
            {
                String html="<table border='1' style='border-collapse:collapse; background-color:white'>";
                html+="<tr><td>HH Unique Id</td><td style='width:250px'>Household head name</td><td> </td></tr>";
                AdultHouseholdMember ahm=null;
                String hhdetails=null;
                String firstName=null;
                String surname=null;
                for(Object obj:list)
                {
                    ahm=(AdultHouseholdMember)obj;
                    hhdetails=getConcatenatedBeneficiaryDetails(ahm.getHhUniqueId(),ahm.getFirstName(),ahm.getSurname());
                    System.err.println("hhdetails is "+hhdetails);
                    html+="<tr><td>"+ahm.getHhUniqueId()+"</td><td>"+ahm.getFirstName()+" "+ahm.getSurname()+"</td><td><input type='button' name='detailsBtn' value='Load details' onclick=\"loadHouseholdDetails('"+hhdetails+"')\"/> </td></tr>";
                    returnValue=ahm.getFirstName()+" "+ahm.getSurname();
                    //System.err.println("list.get(0) is "+returnValue);
                }
                html+="</table>";
                returnValue=html;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return returnValue;
    }
    public String getSchoolList(List list)
    {
        String returnValue=null;
        try
        {
            if(list !=null && !list.isEmpty())
            {
                School school=null;
                
                for(int i=0; i<list.size(); i++)
                {
                    school=(School)list.get(i);
                    if(i==0)
                    returnValue=school.getId()+";"+school.getSchoolName();
                    else
                    returnValue+=":"+school.getId()+";"+school.getSchoolName();
                }
                
                //System.err.println(returnValue);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return returnValue;
    }
    public String getSchoolListAsHtml(List list)
    {
        String returnValue=null;
        try
        {
            if(list !=null && !list.isEmpty())
            {
                String html="<html:select property='nearestSchoolId' styleId='nearestSchoolId' style='width:150px;'>";
                        html+="<html:option value='select'>select...</html:option>";
                School school=null;
                
                for(Object obj:list)
                {
                    school=(School)obj;
                    html+="<option value="+school.getId()+">"+school.getSchoolName()+"</option>";  
                }
                returnValue=html+"</select>";
                System.err.println(returnValue);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return returnValue;
    }
    private String getConcatenatedBeneficiaryDetails(String uniqueId,String firstName,String surname)
    {      
        if(firstName !=null)
        {
            firstName=firstName.replace("'", "");
            firstName=firstName.replace("\n", " ");
        }
        if(surname !=null)
        {
            surname=surname.replace("'", "");
            surname=surname.replace("\n", " ");
        }
        if(uniqueId !=null)
        {
            uniqueId=uniqueId.replace("'", "_");
        }
        String concatenatedBeneficiaryDetails=uniqueId+"; "+firstName+" "+surname;
        return concatenatedBeneficiaryDetails;
    }
}
