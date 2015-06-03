/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.dt;

import java.util.ArrayList;

/**
 *
 * @author BASTING
 */
public class CaseChangeStatusDt {
    private String caseType;
    private String currentStatus;
    private String currentStatusObjid;

    private ArrayList<CaseSingleStatusDt> newStatusList;

    private final static String EMPTY_STR = "";
    
    @Override
    public boolean equals(Object obj) {
        
        if(obj == null){
            return false;
        }
        
        CaseChangeStatusDt dtLocal = (CaseChangeStatusDt) obj;
        
        if(dtLocal.caseType == null || dtLocal.currentStatus == null){
            return false;
        }
        
        if(EMPTY_STR.equals(dtLocal.caseType.trim()) || EMPTY_STR.equals(dtLocal.currentStatus.trim())){
            return false;
        }
        
        if ((dtLocal.caseType.equalsIgnoreCase(this.caseType)) 
                && (dtLocal.currentStatus.equalsIgnoreCase(this.currentStatus))){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.caseType != null ? this.caseType.hashCode() : 0);
        hash = 67 * hash + (this.currentStatus != null ? this.currentStatus.hashCode() : 0);
        return hash;
    }

    public ArrayList<CaseSingleStatusDt> getNewStatusList() {
        return newStatusList;
    }

    public void setNewStatusList(ArrayList<CaseSingleStatusDt> newStatusList) {
        this.newStatusList = newStatusList;
    }
    
    public String getCurrentStatusObjid() {
        return currentStatusObjid;
    }

    public void setCurrentStatusObjid(String currentStatusObjid) {
        this.currentStatusObjid = currentStatusObjid;
    }
  
    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        
        if(newStatusList == null){
            return caseType + ":" + currentStatus;
        }else{
            return caseType+ ":" + currentStatus+ ":" + newStatusList;
        }
    }
    
    
            
}
