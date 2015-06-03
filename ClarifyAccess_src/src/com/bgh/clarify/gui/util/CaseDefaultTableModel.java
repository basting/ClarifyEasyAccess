/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.gui.util;

import java.util.Date;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author BASTING
 */
public class CaseDefaultTableModel extends DefaultTableModel {

    public CaseDefaultTableModel(Object[][] caseList, String[] caseHeader) {
        super(caseList, caseHeader);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
          case  8: return Date.class;
          case  9: return Date.class;
          default: return String.class;
        }
    }
    
    
}
