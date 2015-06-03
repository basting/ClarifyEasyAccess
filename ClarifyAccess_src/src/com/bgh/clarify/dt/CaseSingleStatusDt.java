/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.dt;

/**
 *
 * @author BASTING
 */
public class CaseSingleStatusDt {
    private String status;
    private String statusObjid;
    
    private final static String EMPTY_STR = "";
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusObjid() {
        return statusObjid;
    }

    public void setStatusObjid(String statusObjid) {
        this.statusObjid = statusObjid;
    }

    @Override
    public String toString() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        CaseSingleStatusDt statusDt = (CaseSingleStatusDt)obj;
        
        if(statusDt.status == null){
            return false;
        }
        
        if(EMPTY_STR.equals(statusDt.status.trim())){
            return false;
        }
        
        if (this.status.equalsIgnoreCase(statusDt.status)) {                
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.status != null ? this.status.hashCode() : 0);
        return hash;
    }
    
    
    
}
