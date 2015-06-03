import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class Test {

    private JTable oCTable;
    private DefaultTableModel oDefaultTableModel;
    private JScrollPane oPane;
    private JTableHeader oTableHeader;
    private TableRowSorter sorter;

    public void adddata() {
        for (int i = 0; i < 30; i++) {
            Object[] row = new Object[1];
            String sValueA = "A";
            String sValueB = "A";
            row[0] = "";
            if (i % 2 == 0) {
                if (i < 15) {
                    sValueA = sValueA + sValueA;
                    row[1] = sValueA;
                } else {
                    if (i == 16) {
                        sValueB = "D";
                        row[1] = sValueA;
                    } else {
                        sValueB = sValueB + sValueB;
                        row[1] = sValueA;
                    }

                }
            } else {
                if (i < 15) {
                    sValueB = sValueB + sValueB;
                    row[1] = sValueB;
                } else {
                    if (i == 17) {
                        sValueB = "C";
                        row[1] = sValueB;
                    } else {
                        sValueB = sValueB + sValueB;
                        row[1] = sValueB;
                    }

                }
            }

        }
    }

    public void createTable() {
        oCTable = new JTable();
        oDefaultTableModel = new DefaultTableModel();
        oCTable.setModel(oDefaultTableModel);
        oTableHeader = oCTable.getTableHeader();
        oCTable.setAutoResizeMode(oCTable.AUTO_RESIZE_OFF);
        oCTable.setFillsViewportHeight(true);
        JTable oTable = new LineNumberTable(oCTable);
        oPane = new JScrollPane(oCTable);
        oPane.setRowHeaderView(oTable);
        JPanel oJPanel = new JPanel();
        oJPanel.setLayout(new BorderLayout());
        oJPanel.add(oPane, BorderLayout.CENTER);
        JDialog oDialog = new JDialog();
        oDialog.add(oJPanel);
        oDialog.setPreferredSize(new Dimension(500, 300));
        oDialog.pack();
        oDialog.setVisible(true);

    }

    public void insert() {
        oDefaultTableModel.addColumn("Name");
        int iColumnPlace = ((DefaultTableModel) oCTable.getModel()).findColumn("Name");
        CellRendererForRowHeader oCellRendererForRowHeader = new CellRendererForRowHeader();
        TableColumn Column = oCTable.getColumn(oTableHeader.getColumnModel().getColumn(iColumnPlace).getHeaderValue());
        Column.setPreferredWidth(300);
        Column.setMaxWidth(300);
        Column.setMinWidth(250);
        Column.setCellRenderer(oCellRendererForRowHeader);



        for (int i = 0; i < 30; i++) {
            Object[] row = new Object[1];
            String sValueA = "A";
            if (i % 2 == 0) {
                if (i < 15) {
                    sValueA = sValueA + "a";
                    oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                    oDefaultTableModel.setValueAt(sValueA, i, 0);
                } else {
                    if (i == 16) {
                        sValueA = sValueA + "b";
                        oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                        oDefaultTableModel.setValueAt(sValueA, i, 0);
                    } else {
                        sValueA = sValueA + "c";
                        oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                        oDefaultTableModel.setValueAt(sValueA, i, 0);
                    }

                }
            } else {
                if (i < 15) {
                    sValueA = sValueA + "d";
                    oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                    oDefaultTableModel.setValueAt(sValueA, i, 0);
                } else {
                    if (i == 17) {
                        sValueA = sValueA + "e";
                        oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                        oDefaultTableModel.setValueAt(sValueA, i, 0);
                    } else {
                        sValueA = sValueA + "f";
                        oDefaultTableModel.insertRow(oCTable.getRowCount(), new Object[]{""});
                        oDefaultTableModel.setValueAt(sValueA, i, 0);
                    }

                }
            }

        }
    }

    public void showTable() {
        createTable();
        insert();
        sortCTableonColumnIndex(0, true);

    }

    /*
    public void sortCTableonColumnIndex(int iColumnIndex, boolean bIsAsc) {
        sorter = new TableRowSorter(oDefaultTableModel);
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        if (bIsAsc) {
            sortKeys.add(new RowSorter.SortKey(iColumnIndex, SortOrder.ASCENDING));
        } else {
            sortKeys.add(new RowSorter.SortKey(iColumnIndex, SortOrder.DESCENDING));
        }
        sorter.setSortKeys(sortKeys);
        oDefaultTableModel.fireTableStructureChanged();
        oCTable.updateUI();


    }
    */
    
    public void sortCTableonColumnIndex(int iColumnIndex, boolean bIsAsc) {
        oCTable.setAutoCreateRowSorter(true);
        DefaultRowSorter sorter = ((DefaultRowSorter) oCTable.getRowSorter());
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        if (bIsAsc) {
            sortKeys.add(new RowSorter.SortKey(iColumnIndex, SortOrder.ASCENDING));
        } else {
            sortKeys.add(new RowSorter.SortKey(iColumnIndex, SortOrder.DESCENDING));
        }
        sorter.setSortKeys(sortKeys);
    }

    public static void main(String[] argu) {
        Test oTest = new Test();
        oTest.showTable();


    }

    class CellRendererForRowHeader extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = null;
            try {
                label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    label.setBackground(new JLabel().getBackground());
                    label.setForeground(Color.BLACK);
                }
            } catch (RuntimeException ex) {
            }
            return label;

        }
    }

    class LineNumberTable extends JTable {

        private JTable mainTable;

        public LineNumberTable(JTable table) {
            super();
            mainTable = table;
            setAutoCreateColumnsFromModel(false);
            setModel(mainTable.getModel());
            setAutoscrolls(false);
            addColumn(new TableColumn());
            getColumnModel().getColumn(0).setCellRenderer(mainTable.getTableHeader().getDefaultRenderer());

            getColumnModel().getColumn(0).setPreferredWidth(40);
            setPreferredScrollableViewportSize(getPreferredSize());

        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column) {
            return Integer.valueOf(row + 1);
        }

        @Override
        public int getRowHeight(int row) {
            return mainTable.getRowHeight();
        }
    }
}