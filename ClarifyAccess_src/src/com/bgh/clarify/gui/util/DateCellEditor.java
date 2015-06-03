package com.bgh.clarify.gui.util;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableCellEditor;


public class DateCellEditor extends AbstractCellEditor implements TableCellEditor  
{  
  private Date currentDate;  
  private JSpinner spinner;
  private final static String dtFormat = "MM/dd/yyyy HH:mm:ss a";
  
  public DateCellEditor()  
  {  
    Calendar calendar = Calendar.getInstance();  
    Date initDate = calendar.getTime();  
    calendar.add(Calendar.YEAR, -100);  
    Date earliestDate = calendar.getTime();  
    calendar.add(Calendar.YEAR, 200);  
    Date latestDate = calendar.getTime();  
    SpinnerModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH);  
    spinner = new JSpinner(dateModel);  
    spinner.setEditor(new JSpinner.DateEditor(spinner, dtFormat));  
  
  }  
  // Implement the one CellEditor method that AbstractCellEditor doesn't.  
    @Override
  public Object getCellEditorValue()  
  {  
    currentDate = ((SpinnerDateModel)spinner.getModel()).getDate();
    //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");  
    //currentDate = sdf.format(date);      
    return currentDate;  
  }  
  // Implement the one method defined by TableCellEditor.  
   @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col)  
  {  
    currentDate = (Date)value;  
    //int[] d = convertDateString(currentDate);  
    //Date date = getDate(d[0],d[1],d[2]);
    if(currentDate == null || isDefaultDate(currentDate)){
        spinner.setValue(new GregorianCalendar().getTime());
    }else{
        spinner.setValue(currentDate);  
    }
    return spinner;  
  }  
  
   
  private boolean isDefaultDate(Date inputDate){
      SimpleDateFormat dtFormatLocal = new SimpleDateFormat("MM/dd/yyyy");
      Date date = null;
        try {
            date = dtFormatLocal.parse("1/1/1753");
        } catch (ParseException ex) {
            Logger.getLogger(DateCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        if(date == null || inputDate == null){
            return false;
        }
        
        if(inputDate.equals(date)){
            return true;
        }
        
      return false;
  }
  
}