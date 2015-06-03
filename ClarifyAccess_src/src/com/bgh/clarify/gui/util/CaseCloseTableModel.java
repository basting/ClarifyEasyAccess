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
public class CaseCloseTableModel extends DefaultTableModel {

    public CaseCloseTableModel(Object[][] caseList, String[] caseHeader) {
        super(caseList, caseHeader);
    }

    
    private boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true
            };
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }  
}