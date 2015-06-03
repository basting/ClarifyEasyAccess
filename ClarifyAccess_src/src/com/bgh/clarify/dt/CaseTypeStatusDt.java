/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.dt;

/**
 *
 * @author BASTING
 */
public class CaseTypeStatusDt {

    private String type;
    private String status;
    
    private final static String EMPTY_STR = "";
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return type + ":" + status;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        CaseTypeStatusDt typeStatusDt = (CaseTypeStatusDt)obj;
        
        if(typeStatusDt.status == null || typeStatusDt.type == null){
            return false;
        }
        
        if(EMPTY_STR.equals(typeStatusDt.status.trim()) || EMPTY_STR.equals(typeStatusDt.type.trim())){
            return false;
        }
        
        if (this.status.equalsIgnoreCase(typeStatusDt.status) && this.type.equalsIgnoreCase(typeStatusDt.type)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 11 * hash + (this.status != null ? this.status.hashCode() : 0);
        return hash;
    }
    
}
