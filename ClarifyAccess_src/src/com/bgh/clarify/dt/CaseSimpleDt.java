/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.dt;

/**
 *
 * @author BASTING
 */
public class CaseSimpleDt {

    private String caseId;
     private String type;
     private String currentStatus;
     private CaseSingleStatusDt newStatus;

    @Override
    public String toString() {
        return caseId;
    }
    
     
     
    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public CaseSingleStatusDt getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(CaseSingleStatusDt newStatus) {
        this.newStatus = newStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
     
}
