/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.util;

import com.nomis.ovc.business.Partner;
import com.nomis.ovc.dao.DaoUtility;
import com.nomis.ovc.metadata.OrganizationUnit;
import com.nomis.reports.utils.Indicator;
import com.nomis.reports.utils.ReportTemplate;
import jxl.write.Label;
import java.io.Serializable;
import java.io.OutputStream;
import java.util.List;
import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author smomoh
 */
public class ExcelWriter implements Serializable {

    AppUtility appUtil = new AppUtility();
    DaoUtility util = new DaoUtility();
    int numberOfServices = 0;
    public WritableWorkbook writeRevisedQuarterlyReportTemplateToExcel(OutputStream os, List mainList) 
    {
        WritableWorkbook wworkbook = null;
        //HSSFWorkbook wb = new HSSFWorkbook();
        WritableSheet wsheet = null;
        Label label = null;
        Number number = null;
        String state=null;
        String lga=null;
        String cbo=null;
        String merCode=null;
        String partnerName=null;
        Indicator ind=null;
        String indicatorId=null;
        String indicatorName=null;
        String sex=null;
        String ageCategory=null;
        try 
        {
            wworkbook = Workbook.createWorkbook(os);
            wsheet = wworkbook.createSheet("Report Sheet", 0);

            int t = 0;
            int row = 0;
            String[] columnHeadings = {"State","Lga","CBO","Partner","Indicator","Sex","Age","Other Disagg","Value","Period"};
            String[] maleAgeDisaggregations = {"<1", "1-4", "5-9", "10-14","15-17","18-24","25+"};
            String[] sexDisaggregations = {"male", "female"};
            String period=null;
            ReportTemplate rt = null;
            for (int i = 0; i < columnHeadings.length; i++) {
                label = new Label(i, 0, columnHeadings[i]);
                wsheet.addCell(label);
            }
            if (mainList != null) 
            {
                List stateValueList=null;
                int cell = 0;
                for (int j = 0; j < mainList.size(); j++) 
                {  
                                       
                    stateValueList=(List)mainList.get(j);
                    for(int k=0; k<stateValueList.size(); k++)
                    {
                        rt = (ReportTemplate) stateValueList.get(k);
                        state=rt.getLevel2OuId();
                        lga=rt.getLevel3OuId();
                        cbo=rt.getCboId();
                        partnerName=null;//util.getPartnerName(rt.getPartnerCode());
                        merCode=rt.getMerCode();
                        ind=rt.getIndicator();
                        indicatorName=rt.getIndicatorName();
                        period=rt.getPeriod();//.getStartMth()+""+rt.getStartYr()+"-"+rt.getEndMth()+""+rt.getEndYr();
                        if(ind !=null && (ind.getIndicatorType() !=null && ind.getIndicatorType().equalsIgnoreCase(AppConstant.HOUSEHOLD_TYPE)))
                        {
                            cell = 0;
                            row++;
                            sex="NIL";
                            label = new Label(cell, row, state);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, lga);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, cbo);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, partnerName);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, merCode);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, sex);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, ageCategory);
                            wsheet.addCell(label);
                            label = new Label(++cell, row, indicatorName);
                            wsheet.addCell(label);
                            number = new Number(++cell, row, rt.getGrandTotal());
                            wsheet.addCell(number);
                            label = new Label(++cell, row, period);
                            wsheet.addCell(label);
                        }
                        else
                        {
                            for(int l=0; l<maleAgeDisaggregations.length; l++)
                            {
                                ageCategory=maleAgeDisaggregations[l];
                                for(int m=0; m<sexDisaggregations.length; m++)
                                {

                                    sex=sexDisaggregations[m];
                                    if(sex.equalsIgnoreCase("male"))
                                    {
                                        cell = 0;
                                        row++;
                                        label = new Label(cell, row, state);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, lga);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, cbo);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, partnerName);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, merCode);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, sex);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, ageCategory);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, indicatorName);
                                        wsheet.addCell(label);
                                        

                                        if(ageCategory.equalsIgnoreCase("<1"))
                                        number = new Number(++cell, row, rt.getMaleLessThan1());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("1-4"))
                                        number = new Number(++cell, row, rt.getMale1to4());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("5-9"))
                                        number = new Number(++cell, row, rt.getMale5to9());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("10-14"))
                                        number = new Number(++cell, row, rt.getMale10to14());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("15-17"))
                                        number = new Number(++cell, row, rt.getMale15to17());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("18-24"))
                                        number = new Number(++cell, row, rt.getMale18to24());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("25+"))
                                        number = new Number(++cell, row, rt.getMale25Plus());
                                        wsheet.addCell(number);
                                        
                                        label = new Label(++cell, row, period);
                                        wsheet.addCell(label);
                                    }
                                    else if(sex.equalsIgnoreCase("female"))
                                    {
                                        row++;
                                        cell = 0;
                                        //get for females too
                                        label = new Label(cell, row, state);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, lga);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, cbo);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, partnerName);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, merCode);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, sex);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, ageCategory);
                                        wsheet.addCell(label);
                                        label = new Label(++cell, row, indicatorName);
                                        wsheet.addCell(label);
                                        

                                        if(ageCategory.equalsIgnoreCase("<1"))
                                        number = new Number(++cell, row, rt.getFemaleLessThan1());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("1-4"))
                                        number = new Number(++cell, row, rt.getFemale1to4());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("5-9"))
                                        number = new Number(++cell, row, rt.getFemale5to9());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("10-14"))
                                        number = new Number(++cell, row, rt.getFemale10to14());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("15-17"))
                                        number = new Number(++cell, row, rt.getFemale15to17());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("18-24"))
                                        number = new Number(++cell, row, rt.getFemale18to24());
                                        //wsheet.addCell(number);
                                        else if(ageCategory.equalsIgnoreCase("25+"))
                                        number = new Number(++cell, row, rt.getFemale25Plus());
                                        wsheet.addCell(number);
                                        
                                        label = new Label(++cell, row, period);
                                        wsheet.addCell(label);
                                    }
                                    //row++;
                                }
                            }
                        }
                    }
                }
                t++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wworkbook;
    }
    public WritableWorkbook writeQuarterlyReportToExcel(OutputStream os, List mainList) 
    {
        WritableWorkbook wworkbook = null;
        //HSSFWorkbook wb = new HSSFWorkbook();
        WritableSheet wsheet = null;
        Label label = null;
        Number number = null;

        try 
        {
            wworkbook = Workbook.createWorkbook(os);
            wsheet = wworkbook.createSheet("Report Sheet", 0);

            int t = 0;
            int row = 1;
            String[] columnHeadings = {"Indicator","MER Code","State","Lga","CBO","Partner","Period", "<1", "1-4", "5-9", "10-14","15-17","18-24","25+","Male total","<1", "1-4", "5-9", "10-14","15-17","18-24","25+","Female total","Total"};
            ReportTemplate rt = null;
            OrganizationUnit level2Ou=null;
            OrganizationUnit level3Ou=null;
            String level2OuName=null;
            String level3OuName=null;
            Partner partner=null;
            String partnerName=null;
            for (int i = 0; i < columnHeadings.length; i++) {
                label = new Label(i, 0, columnHeadings[i]);
                wsheet.addCell(label);
            }
            if (mainList != null) 
            {
                List stateValueList=null;
                int cell = 0;
                for (int j = 0; j < mainList.size(); j++) 
                {  
                                       
                    stateValueList=(List)mainList.get(j);
                    for(int k=0; k<stateValueList.size(); k++)
                    {
                        partnerName=null;
                        level2OuName=null;
                        level3OuName=null;
                        cell = 0;
                        rt = (ReportTemplate) stateValueList.get(k);
                        level2Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(rt.getLevel2OuId());
                        level3Ou=util.getOrganizationUnitDaoInstance().getOrganizationUnit(rt.getLevel3OuId());
                        if(level2Ou !=null)
                        level2OuName=level2Ou.getName();
                        if(level3Ou !=null)
                        level3OuName=level3Ou.getName();
                        partner=util.getPartnerDaoInstance().getPartner(rt.getPartnerCode());
                        if(partner !=null)
                        partnerName=partner.getPartnerName();
                        label = new Label(cell, row, rt.getIndicatorName());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getMerCode());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, level2OuName);
                        wsheet.addCell(label);
                        label = new Label(++cell, row, level3OuName);
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getCboId());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, partnerName);
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getPeriod());
                        wsheet.addCell(label);
                        number = new Number(++cell, row, rt.getMaleLessThan1());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale1to4());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale5to9());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale10to14());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale15to17());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale18to24());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMale25Plus());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getMaleTotal());
                        wsheet.addCell(number);

                        //get for females too
                        number = new Number(++cell, row, rt.getFemaleLessThan1());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale1to4());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale5to9());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale10to14());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale15to17());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale18to24());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getFemale25Plus());
                        wsheet.addCell(number);

                        number = new Number(++cell, row, rt.getFemaleTotal());
                        wsheet.addCell(number);
                        number = new Number(++cell, row, rt.getGrandTotal());
                        wsheet.addCell(number);

                        row++;
                    }
                }
                t++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wworkbook;
    }
    public WritableWorkbook writeDatim2017ReportToExcel(OutputStream os, List datimFormList) 
    {
        WritableWorkbook wworkbook = null;
        //HSSFWorkbook wb = new HSSFWorkbook();
        WritableSheet wsheet = null;
        Label label = null;
        Number number = null;

        try {
            //appUtil.createReportDirectory();
            wworkbook = Workbook.createWorkbook(os);
            wsheet = wworkbook.createSheet("Report Sheet", 0);

            int t = 0;
            int row = 1;
            String[] columnHeadings = {"State", "Lga","Period","Indicator","Category", "Age disaggregation", "No of OVC"};
            ReportTemplate rt = null;
            for (int i = 0; i < columnHeadings.length; i++) {
                label = new Label(i, 0, columnHeadings[i]);
                wsheet.addCell(label);
            }
            if (datimFormList != null) {
                for (int j = 0; j < datimFormList.size(); j++) 
                {
                    int cell = 0;
                    
                        rt = (ReportTemplate) datimFormList.get(j);

                        label = new Label(cell, row, rt.getLevel2OuName());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getLevel3OuName());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getPeriod());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getIndicatorName());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getOtherDisaggregation());
                        wsheet.addCell(label);
                        label = new Label(++cell, row, rt.getAgeDisaggregation());
                        wsheet.addCell(label);
                        number = new Number(++cell, row, rt.getValue());
                        wsheet.addCell(number);

                        row++;
                    
                }
                t++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wworkbook;
    }

    
    public boolean isEmpty(String value) {
        if (value != null && !value.equalsIgnoreCase("") && !value.equalsIgnoreCase(" ") && !value.equalsIgnoreCase("  ")) {
            return false;
        }
        return true;
    }
    private String getBusinessQuarter(int mth)
    {
        String quarter=null;
        if(mth>9 && mth <13)
        quarter="Quarter1";
        else if(mth>0 && mth <4)
        quarter="Quarter2";
        else if(mth>3 && mth <7)
        quarter="Quarter3";
        else if(mth>6 && mth <10)
        quarter="Quarter4";
        
        return quarter;
    }
}
