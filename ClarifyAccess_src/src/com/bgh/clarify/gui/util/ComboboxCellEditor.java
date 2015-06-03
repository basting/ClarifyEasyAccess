/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.clarify.gui.util;

import com.bgh.clarify.dt.CaseChangeStatusDt;
import com.bgh.clarify.dt.CaseSingleStatusDt;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author BASTING
 */
public class ComboboxCellEditor extends AbstractCellEditor implements TableCellEditor{

    private JComboBox cmbBox;
    
    private ArrayList<CaseChangeStatusDt> masterValues;
    
    public ComboboxCellEditor(ArrayList<CaseChangeStatusDt> listValues){
        masterValues = listValues;
    }
    
    @Override
    public Object getCellEditorValue() {
        return cmbBox.getSelectedItem();
    }

    private ArrayList<String> convertToStatusList(ArrayList<CaseSingleStatusDt> singStatusList) {
        int size = singStatusList.size();
        ArrayList<String> statusList = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            statusList.add(singStatusList.get(i).getStatus());
        }
        
        return statusList;
    }
    
    private ArrayList<String> getNextStatusValues(String caseType, String currentStatus){
        CaseChangeStatusDt changeStatusDtTemp = new CaseChangeStatusDt();
        changeStatusDtTemp.setCaseType(caseType);
        changeStatusDtTemp.setCurrentStatus(currentStatus);
        
        int index = masterValues.indexOf(changeStatusDtTemp);
        if(index != -1){
            CaseChangeStatusDt changeStatusDt = masterValues.get(index);
            ArrayList<CaseSingleStatusDt> singStatusList = changeStatusDt.getNewStatusList();
            return convertToStatusList(singStatusList);
        }else{
            return new ArrayList<String>();
        }
    }    
        
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        
        CaseChangeStatusTableModel changeStatusTableModel = (CaseChangeStatusTableModel)table.getModel();
        String caseType = (String)changeStatusTableModel.getValueAt(row, 0);
        String currentStatus = (String)changeStatusTableModel.getValueAt(row, 1);
        
        ArrayList<String> nextStatusValues = getNextStatusValues(caseType, currentStatus);
        
        ComboBoxModel cmbModel = new DefaultComboBoxModel(nextStatusValues.toArray());
        
        cmbBox = new JComboBox(cmbModel);
        
        return cmbBox;
    }

   

}
