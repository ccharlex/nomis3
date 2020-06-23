/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.util;

import com.nomis.ovc.business.User;
import com.nomis.ovc.util.DateManager;
import java.util.Date;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smomoh
 */
public class AppManager 
{
    public User getCurrentUser(HttpSession session)
    {
        User user=(User)session.getAttribute("currentUser");
        if(user !=null)
        {
            System.err.println("Username is "+user.getUsername());
        }
        return user;
    }
    public String getCurrentUserName(HttpSession session)
    {
        String userName="test";
        User user=(User)session.getAttribute("currentUser");
        if(user !=null)
        {
            userName=user.getUsername();
            System.err.println("Username is "+userName);
        }
        return userName;
    }
    /*public Ovc getOvcWithCurrentAgeAge(Ovc ovc)
    {
        String dateOfBirth=DateManager.convertDateToString(ovc.getDateOfBirth(), DateManager.DB_DATE_FORMAT);
        String currentDate=DateManager.getCurrentDate();
        //int baselineAge=0;
        int currentMth=0;
        int currentYear=0;
        int birthMonth=0;
        int birthYear=0;
        if(dateOfBirth !=null && dateOfBirth.indexOf("-") !=-1)
        {
            String[] dateOfBirthArray=dateOfBirth.split("-");
            if(dateOfBirthArray.length>2)
            {
                birthMonth=Integer.parseInt(dateOfBirthArray[1]);
                birthYear=Integer.parseInt(dateOfBirthArray[0]);
            }
        }
        if(currentDate !=null && currentDate.indexOf("-") !=-1)
        {
            String[] dateOfEnrollmentArray=currentDate.split("-");
            if(dateOfEnrollmentArray.length>2)
            {
                currentMth=Integer.parseInt(dateOfEnrollmentArray[1]);
                currentYear=Integer.parseInt(dateOfEnrollmentArray[0]);
            }
        }
        int mthDiff=DateManager.getDateDifference(birthMonth,birthYear,currentMth,currentYear);
        if(mthDiff <0)
        mthDiff=0;
        if(mthDiff <12)
        {
            ovc.setCurrentAge(mthDiff);
            ovc.setCurrentAgeUnit(AppConstant.MONTH_SYMBOL);
        }
        else
        {
            ovc.setCurrentAge(getAgeInYears(mthDiff));
            ovc.setCurrentAgeUnit(AppConstant.YEAR_SYMBOL);
        }
        return ovc;
    }
    public Ovc getOvcWithBaselineAge(Ovc ovc)
    {
        String dateOfBirth=DateManager.convertDateToString(ovc.getDateOfBirth(), DateManager.DB_DATE_FORMAT);
        String dateOfEnrollment=DateManager.convertDateToString(ovc.getDateOfEnrollment(), DateManager.DB_DATE_FORMAT);;
        //int baselineAge=0;
        int mthOfEnrollment=0;
        int yearOfEnrollment=0;
        int birthMonth=0;
        int birthYear=0;
        if(dateOfBirth !=null && dateOfBirth.indexOf("-") !=-1)
        {
            String[] dateOfBirthArray=dateOfBirth.split("-");
            if(dateOfBirthArray.length>2)
            {
                birthMonth=Integer.parseInt(dateOfBirthArray[1]);
                birthYear=Integer.parseInt(dateOfBirthArray[0]);
            }
        }
        if(dateOfEnrollment !=null && dateOfEnrollment.indexOf("-") !=-1)
        {
            String[] dateOfEnrollmentArray=dateOfEnrollment.split("-");
            if(dateOfEnrollmentArray.length>2)
            {
                mthOfEnrollment=Integer.parseInt(dateOfEnrollmentArray[1]);
                yearOfEnrollment=Integer.parseInt(dateOfEnrollmentArray[0]);
            }
        }
        int mthDiff=DateManager.getDateDifference(birthMonth,birthYear,mthOfEnrollment,yearOfEnrollment);
        if(mthDiff <0)
        mthDiff=0;
        if(mthDiff <12)
        {
            ovc.setAgeAtBaseline(mthDiff);
            ovc.setAgeUnit(AppConstant.MONTH_SYMBOL);
        }
        else
        {
            ovc.setAgeAtBaseline(getAgeInYears(mthDiff));
            ovc.setAgeUnit(AppConstant.YEAR_SYMBOL);
        }
        return ovc;
    }*/
    public int getAgeInYears(int ageInMonths)
    {
        int ageInYears=0;
        if(ageInMonths>11)
        {
            ageInYears =Math.round(ageInMonths/12);
        }
        return ageInYears;
    }
    public static int getAgeAtBaseline(Date dateOfBirth,Date dateOfEnrollment)
    {
        String strDateOfBirth=DateManager.convertDateToString(dateOfBirth, DateManager.DB_DATE_FORMAT);
        String strDateOfEnrollment=DateManager.convertDateToString(dateOfEnrollment, DateManager.DB_DATE_FORMAT);
        int ageAtBaseline=getAgeAtBaseline(strDateOfBirth, strDateOfEnrollment);
        return ageAtBaseline;    
    }
    public static int getAgeAtBaseline(String dateOfBirth,String dateOfEnrollment) 
    {
        int ageAtBaseline=getAgeFromDates(dateOfBirth, dateOfEnrollment);
        /*int monthDifference=DateManager.getDateDifferenceInMonths(dateOfBirth, dateOfEnrollment);
        double age=Math.floor((monthDifference/12d));
        int ageAtBaseline=(int)age;*/
        //System.err.println("Age of type double is "+age+", age converted to int is "+ageAtBaseline);
        return ageAtBaseline;
    }
    public static int getBaselineAgeUnit(String dateOfBirth,String dateOfEnrollment) 
    {
        int currentAgeUnit=1;
        int monthDifference=DateManager.getDateDifferenceInMonths(dateOfBirth, dateOfEnrollment);
        if(monthDifference>11)
        currentAgeUnit=2;
        return currentAgeUnit;
    }
    public static int getCurrentAge(String dateOfEnrollment,int ageAtEnrollment) 
    {
        int currentAge=ageAtEnrollment;
        if(dateOfEnrollment !=null && dateOfEnrollment.indexOf("-") !=-1)
        {
            String currentDate=DateManager.getCurrentDate();
            int ageDifference=getAgeFromDates(dateOfEnrollment, currentDate);
            currentAge=ageDifference+ageAtEnrollment;
        }
        System.err.println("currentAge is "+currentAge);
        return currentAge;
    }
    public static int getCurrentAge(String dateOfBirth) 
    {
        String currentDate=DateManager.getCurrentDate();
        int currentAge=getAgeFromDates(dateOfBirth, currentDate);
        /*int monthDifference=DateManager.getDateDifferenceInMonths(dateOfBirth, currentDate);
        double age=Math.floor((monthDifference/12d));
        int currentAge=(int)age;*/
        return currentAge;
    }
    public static int getCurrentAgeUnit(String dateOfBirth) 
    {
        int currentAgeUnit=1;
        String currentDate=DateManager.getCurrentDate();
        int monthDifference=DateManager.getDateDifferenceInMonths(dateOfBirth, currentDate);
        if(monthDifference>11)
        currentAgeUnit=2;
        return currentAgeUnit;
    }
    public static int getAgeFromDates(String startDate, String endDate)
    {
        int age=-1;
        if(startDate !=null && endDate !=null)
        {
            int monthDifference=DateManager.getDateDifferenceInMonths(startDate, endDate);
            double dage=Math.floor((monthDifference/12d));
            age=(int)dage;
        }
        return age;
    }
    public static int getAgeFromDates(Date startDate, Date endDate)
    {
        int age=-1;
        if(startDate !=null && endDate !=null)
        {
            String strStartDate=DateManager.convertDateToString(startDate, DateManager.DB_DATE_FORMAT);
            String strEndDate=DateManager.convertDateToString(endDate, DateManager.DB_DATE_FORMAT);
            int monthDifference=DateManager.getDateDifferenceInMonths(strStartDate, strEndDate);
            //System.err.println("strStartDate is "+strStartDate+" and strEndDate is "+strEndDate);
            //System.err.println("monthDifference is "+monthDifference);
            
            double dage=Math.floor((monthDifference/12d));
            age=(int)dage;
        }
        return age;
    }
    public static int getCurrentAgeUnitFromDates(Date startDate, Date endDate) 
    {
        int currentAgeUnit=1;
        String strStartDate=DateManager.convertDateToString(startDate, DateManager.DB_DATE_FORMAT);
        String strEndDate=DateManager.convertDateToString(endDate, DateManager.DB_DATE_FORMAT);
        int monthDifference=DateManager.getDateDifferenceInMonths(strStartDate, strEndDate);
        if(monthDifference>11)
        currentAgeUnit=2;
        return currentAgeUnit;
    }
}
