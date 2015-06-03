/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.dt;

import java.util.Date;

/**
 *
 * @author BASTING
 */
public class CaseCloseDt {
     private String caseId;
     private String status;
     private String caseType;

     private String caseTitle;
     private Date impactStartTime;
     private Date impactEndTime;
     private String businessImpact;
     private String restorationAction;
     private Date creationTime;

     private String condition;

     public Date getCreationTime() {
        return creationTime;
     }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

     
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
     
     public Date getImpactStartTime() {
        return impactStartTime;
    }

    public void setImpactStartTime(Date impactStartTime) {
        this.impactStartTime = impactStartTime;
    }
     
     public Date getImpactEndTime() {
        return impactEndTime;
    }

    public void setImpactEndTime(Date impactEndTime) {
        this.impactEndTime = impactEndTime;
    }
     
     public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }
     
    public String getBusinessImpact() {
        return businessImpact;
    }

    public void setBusinessImpact(String businessImpact) {
        this.businessImpact = businessImpact;
    }

    public String getRestorationAction() {
        return restorationAction;
    }

    public void setRestorationAction(String restorationAction) {
        this.restorationAction = restorationAction;
    }
     
    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return caseId;
    }
    
    
}
