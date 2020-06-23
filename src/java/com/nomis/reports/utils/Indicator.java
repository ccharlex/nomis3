/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nomis.reports.utils;

import java.io.Serializable;

/**
 *
 * @author smomoh
 */
public class Indicator implements Serializable{
    private int id;
    private String indicatorId;
    private String merCode;
    private String indicatorName;
    private String alternateName;
    private String indicatorType;
    private String indicatorSubtype;
    private String query;
    private String queryCriteria;
    private String categoryOptionCombo;
    private String description;
    private String source;
    private String disaggregations;
    private String otherInformation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCategoryOptionCombo() {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo(String categoryOptionCombo) {
        this.categoryOptionCombo = categoryOptionCombo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getIndicatorSubtype() {
        return indicatorSubtype;
    }

    public void setIndicatorSubtype(String indicatorSubtype) {
        this.indicatorSubtype = indicatorSubtype;
    }

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryCriteria() {
        return queryCriteria;
    }

    public void setQueryCriteria(String queryCriteria) {
        this.queryCriteria = queryCriteria;
    }
    public static Indicator getInstance()
    {
        return new Indicator();
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getDisaggregations() {
        return disaggregations;
    }

    public void setDisaggregations(String disaggregations) {
        this.disaggregations = disaggregations;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }
    
}
