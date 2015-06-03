/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.gui.util;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BASTING
 */
public class CaseChangeStatusTableModel extends DefaultTableModel {
    
    public CaseChangeStatusTableModel(Object[][] caseTypeList, String[] caseTypeHeader) {
        super(caseTypeList, caseTypeHeader);
    }

    
    private boolean[] canEdit = new boolean [] {
                false, false, true
            };
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    } 
}
