/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nomis.ovc.business;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author smomoh
 */
public class NutritionStatus implements Serializable
{
    private int currentNutritionalStatus;
    private Date dateOfCurrentNutritionalStatus;
    private Date lastModifiedDate;
    private String ovcId;
    private int markedForDelete;

    public int getCurrentNutritionalStatus() {
        return currentNutritionalStatus;
    }

    public void setCurrentNutritionalStatus(int currentNutritionalStatus) {
        this.currentNutritionalStatus = currentNutritionalStatus;
    }

    public Date getDateOfCurrentNutritionalStatus() {
        return dateOfCurrentNutritionalStatus;
    }

    public void setDateOfCurrentNutritionalStatus(Date dateOfCurrentNutritionalStatus) {
        this.dateOfCurrentNutritionalStatus = dateOfCurrentNutritionalStatus;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getOvcId() {
        return ovcId;
    }

    public void setOvcId(String ovcId) {
        this.ovcId = ovcId;
    }

    public int getMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(int markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
    
}
