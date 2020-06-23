/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.operationsManagement;


import com.nomis.ovc.util.DateManager;
import java.util.Date;

/**
 *
 * @author smomoh
 */
public class FinancialYearManager 
{
    public Date getDATIMStartDate(Date date)
    {
        Date startDate=null;
        String strDate=DateManager.convertDateToString(date, DateManager.DB_DATE_FORMAT);
        if(strDate !=null && strDate.indexOf("-") !=-1)
        {
            String[] dateArray=strDate.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int year=Integer.parseInt(dateArray[0]);
                int quarter=getQuarter(month);
                
                if(quarter==1)
                {
                    startDate=DateManager.getDateInstance(year+"-"+10+"-01");
                }
                else if(quarter==2)
                {
                    year=year-1;
                    startDate=DateManager.getDateInstance(year+"-"+10+"-01");
                }
                else if(quarter==3 || quarter==4)
                {
                    startDate=DateManager.getDateInstance(year+"-"+"04"+"-01");
                }  
            }
        }
        return startDate;
    }
    public Date getDATIMEndDate(Date date)
    {
        Date startDate=null;
        String strDate=DateManager.convertDateToString(date, DateManager.DB_DATE_FORMAT);
        if(strDate !=null && strDate.indexOf("-") !=-1)
        {
            String[] dateArray=strDate.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int year=Integer.parseInt(dateArray[0]);
                int quarter=getQuarter(month);
                
                if(quarter==1)
                {
                    year=year+1;
                    startDate=DateManager.getDateInstance(year+"-03-31");
                }
                else if(quarter==2)
                {
                    startDate=DateManager.getDateInstance(year+"-03-31");
                }
                else if(quarter==3 || quarter==4)
                {
                    startDate=DateManager.getDateInstance(year+"-"+"09"+"-30");
                }  
            }
        }
        return startDate;
    }
    public String getStartDateOfPreviousQuarter(String date)
    {
        String startDateOfPreviousQuarter=null;
        if(date !=null)
        {
            String strDate=date;
            if(strDate.indexOf("-") ==-1)
            strDate=DateManager.convertDateToString(DateManager.getDateInstance(strDate), DateManager.DB_DATE_FORMAT);
            if(strDate.indexOf("-") !=-1)
            {
                String[] dateArray=strDate.split("-");
                if(dateArray.length>1)
                {
                    int month=Integer.parseInt(dateArray[1]);
                    int previousQuarter=getPreviousQuarter(month);
                    int year=Integer.parseInt(dateArray[0]);
                    startDateOfPreviousQuarter=getStartDateOfQuarter(previousQuarter,year);
                }
            }
        }
        return startDateOfPreviousQuarter;
    }
    public String getStartDateOfQuarter(int quarter,int year)
    {
        String startDate=null;
        if(quarter>0)
        {
            
                //int quarter=getQuarter(month);
                if(quarter==1)
                {
                    year=year-1;
                    startDate=year+"-"+10+"-01";
                }
                else if(quarter==2)
                {
                    startDate=year+"-"+"01"+"-01";
                }
                else if(quarter==3)
                {
                    startDate=year+"-"+"04"+"-01";
                }
                else if(quarter==4)
                {
                    startDate=year+"-"+"07"+"-01";
                }
        }
        return startDate;
    }
    public String getEndDateOfQuarter(int quarter,int year)
    {
        String startDate=null;
        if(quarter>0)
        {
            
                //int quarter=getQuarter(month);
                if(quarter==1)
                {
                    year=year-1;
                    startDate=year+"-"+12+"-31";
                }
                else if(quarter==2)
                {
                    startDate=year+"-"+"03"+"-31";
                }
                else if(quarter==3)
                {
                    startDate=year+"-"+"06"+"-30";
                }
                else if(quarter==4)
                {
                    startDate=year+"-"+"09"+"-30";
                }
        }
        return startDate;
    }
    public String getStartDateOfQuarter(String date)
    {
        String startDate=null;
        if(date !=null && date.indexOf("-") !=-1)
        {
            String[] dateArray=date.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int quarter=getQuarter(month);
                if(quarter==1)
                {
                    startDate=dateArray[0]+"-"+10+"-01";
                }
                else if(quarter==2)
                {
                    startDate=dateArray[0]+"-"+"01"+"-01";
                }
                else if(quarter==3)
                {
                    startDate=dateArray[0]+"-"+"04"+"-01";
                }
                else if(quarter==4)
                {
                    startDate=dateArray[0]+"-"+"07"+"-01";
                }
                
            }
        }
        return startDate;
    }
    public String getEndDateOfQuarter(String date)
    {
        String startDate=null;
        if(date !=null && date.indexOf("-") !=-1)
        {
            String[] dateArray=date.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int quarter=getQuarter(month);
                if(quarter==1)
                {
                    startDate=dateArray[0]+"-"+12+"-31";
                }
                else if(quarter==2)
                {
                    startDate=dateArray[0]+"-"+"03"+"-31";
                }
                else if(quarter==3)
                {
                    startDate=dateArray[0]+"-"+"06"+"-30";
                }
                else if(quarter==4)
                {
                    startDate=dateArray[0]+"-"+"09"+"-30";
                }
                
            }
        }
        return startDate;
    }
    public Date getStartDateOfQuarter(Date date)
    {
        Date startDate=null;
        String strDate=DateManager.convertDateToString(date, DateManager.DB_DATE_FORMAT);
        if(strDate !=null && strDate.indexOf("-") !=-1)
        {
            String[] dateArray=strDate.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int quarter=getQuarter(month);
                if(quarter==1)
                {
                    startDate=DateManager.getDateInstance(dateArray[0]+"-"+10+"-01");
                }
                else if(quarter==2)
                {
                    startDate=DateManager.getDateInstance(dateArray[0]+"-"+"01"+"-01");
                }
                else if(quarter==3)
                {
                    startDate=DateManager.getDateInstance(dateArray[0]+"-"+"04"+"-01");
                }
                else if(quarter==4)
                {
                    startDate=DateManager.getDateInstance(dateArray[0]+"-"+"07"+"-01");
                }  
            }
        }
        return startDate;
    }
    public Date getEndDateOfQuarter(Date date)
    {
        String strDate=DateManager.convertDateToString(date, DateManager.DB_DATE_FORMAT);
        Date endDate=null;
        if(strDate !=null && strDate.indexOf("-") !=-1)
        {
            String[] dateArray=strDate.split("-");
            if(dateArray !=null && dateArray.length>2)
            {
                int month=Integer.parseInt(dateArray[1]);
                int quarter=getQuarter(month);
                if(quarter==1)
                {
                    endDate=DateManager.getDateInstance(dateArray[0]+"-"+12+"-31");
                }
                else if(quarter==2)
                {
                    endDate=DateManager.getDateInstance(dateArray[0]+"-"+"03"+"-31");
                }
                else if(quarter==3)
                {
                    endDate=DateManager.getDateInstance(dateArray[0]+"-"+"06"+"-30");
                }
                else if(quarter==4)
                {
                    endDate=DateManager.getDateInstance(dateArray[0]+"-"+"09"+"-30");
                }  
            }
        }
        return endDate;
    }
    public String getReportQuarter(Date date)
    {
        String reportQuarter=null;
        if(date !=null)
        {
            String strdate=DateManager.convertDateToString(date, DateManager.DB_DATE_FORMAT);
            if(strdate !=null && strdate.indexOf("-") !=-1)
            {
                String[] strdateArray=strdate.split("-");
                if(strdateArray.length>2)
                {
                    int year=Integer.parseInt(strdateArray[0]);
                    int month=Integer.parseInt(strdateArray[1]);
                    int quarter=getQuarter(month);
                    reportQuarter=year+"Q"+quarter;
                }
            }
        }
        return reportQuarter;
    }
    public int getQuarter(int month)
    {
        int quarter=0;
        if(month>9 && month<13)
        quarter=1;
        else if(month>0 && month<4)
        quarter=2;
        else if(month>3 && month<7)
        quarter=3;
        else if(month>6 && month<10)
        quarter=4;
        return quarter;
    }
    public int getPreviousQuarter(int month)
    {
        int previousQuarter=0;
        if(month>9 && month<13)
        previousQuarter=4;
        else if(month>0 && month<4)
        previousQuarter=1;
        else if(month>3 && month<7)
        previousQuarter=2;
        else if(month>6 && month<10)
        previousQuarter=3;
        return previousQuarter;
    }
}
