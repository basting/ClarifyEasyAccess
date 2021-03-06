/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bgh.clarify.db.ClarifyDatabaseQueryHelper;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author BASTING
 */
public class MainPanel extends javax.swing.JPanel {

    private final String WG = "WG";
    private final String WIPBIN = "WIPBIN";
    private final String QUEUE = "QUEUE";

    private ClarifyDatabaseQueryHelper queryhelper;
    
    /**
     * Creates new form mainPanel
     */
    public MainPanel() {
        initComponents();
        populateCaseListBasedOnOption("bastingomez");        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrpCaseFilter = new javax.swing.ButtonGroup();
        pnlFilter = new javax.swing.JPanel();
        rbWGCases = new javax.swing.JRadioButton();
        rbWIPBINCases = new javax.swing.JRadioButton();
        rbQueueCases = new javax.swing.JRadioButton();
        btnSearch = new javax.swing.JButton();
        pnlCases = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCase = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnAccept = new javax.swing.JButton();
        btnYank = new javax.swing.JButton();
        btnChangeStatus = new javax.swing.JButton();
        btnCloseCase = new javax.swing.JButton();

        pnlFilter.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGrpCaseFilter.add(rbWGCases);
        rbWGCases.setSelected(true);
        rbWGCases.setText("Workgroup cases");
        rbWGCases.setActionCommand("WG");

        btnGrpCaseFilter.add(rbWIPBINCases);
        rbWIPBINCases.setText("WIPBIN cases");
        rbWIPBINCases.setActionCommand("WIPBIN");

        btnGrpCaseFilter.add(rbQueueCases);
        rbQueueCases.setText("Queue cases");
        rbQueueCases.setActionCommand("QUEUE");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbWGCases)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbQueueCases)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbWIPBINCases)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(401, Short.MAX_VALUE))
        );

        pnlFilterLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rbQueueCases, rbWGCases, rbWIPBINCases});

        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbWGCases)
                    .addComponent(rbWIPBINCases)
                    .addComponent(rbQueueCases)
                    .addComponent(btnSearch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCases.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblCase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblCase);

        btnAccept.setText("Accept");

        btnYank.setText("Yank");

        btnChangeStatus.setText("Change Status");

        btnCloseCase.setText("Close case");

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap(434, Short.MAX_VALUE)
                .addComponent(btnAccept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChangeStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnYank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCloseCase))
        );

        pnlButtonsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAccept, btnChangeStatus, btnCloseCase, btnYank});

        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnAccept)
                .addComponent(btnChangeStatus)
                .addComponent(btnYank)
                .addComponent(btnCloseCase))
        );

        javax.swing.GroupLayout pnlCasesLayout = new javax.swing.GroupLayout(pnlCases);
        pnlCases.setLayout(pnlCasesLayout);
        pnlCasesLayout.setHorizontalGroup(
            pnlCasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCasesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCasesLayout.setVerticalGroup(
            pnlCasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCasesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCases, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        populateCaseListBasedOnOption("bastingomez");
    }//GEN-LAST:event_btnSearchActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Windows".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new JFrame("Clarify Easy Access");
                mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                mainFrame.getContentPane().add(new MainPanel());
                
                /* JMenuBar mnuBarMain = new JMenuBar();
                JMenu mnuApp = new JMenu();
                JMenuItem mnuItmExit = new JMenuItem();
                mnuApp.setText("File");
                mnuItmExit.setText("Exit");
                mnuApp.add(mnuItmExit);
                mnuBarMain.add(mnuApp);
                
                mainFrame.getContentPane().add(mnuBarMain);*/
                
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }
  
    
    	private Object[][] getCaseList(String loginName) {
		queryhelper = new ClarifyDatabaseQueryHelper();
		return queryhelper.getCaseList(loginName);
		//return new String[][]{{"1","2","3"},
		//		{"1","2","3"}};
		
	}
	private String[] getCaseHeaders() {
		return new String [] {
                "Case Id", "Title", "Condition", 
                "Status", "Priority", "WG", 
                "Stage Discovered", "Environment", 
                "Creation Time", "Last Modified", "Owner", "Type"
            };
	}
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnChangeStatus;
    private javax.swing.JButton btnCloseCase;
    private javax.swing.ButtonGroup btnGrpCaseFilter;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnYank;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCases;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JRadioButton rbQueueCases;
    private javax.swing.JRadioButton rbWGCases;
    private javax.swing.JRadioButton rbWIPBINCases;
    private javax.swing.JTable tblCase;
    // End of variables declaration//GEN-END:variables

    private void populateCaseListBasedOnOption(String loginName){
        ButtonModel buttonModel = btnGrpCaseFilter.getSelection();
        String actionCommand = buttonModel.getActionCommand();
        if(WG.equalsIgnoreCase(actionCommand)){
            setDataFromDBForTableDataModelForWG(loginName);
        }else if(WIPBIN.equalsIgnoreCase(actionCommand)){
            JOptionPane.showMessageDialog(this, "WIPBIN Query");
        }else if(QUEUE.equalsIgnoreCase(actionCommand)){
            JOptionPane.showMessageDialog(this, "QUEUE Query");
        }
    }
    
    /*private void setDefaultTableDataModelForWG() {
       
        String [] caseHeader = getCaseHeaders();
        String [][] caseList =  {{}};
	TableModel caseResultsTableModel = new DefaultTableModel(caseList,caseHeader);
        tblCase.setModel(caseResultsTableModel);
    }*/
    
     private void setDataFromDBForTableDataModelForWG(String loginName) {
       
        String [] caseHeader = getCaseHeaders();
        Object [][] caseList =  getCaseList(loginName);
        
        //CaseDefaultTableModel caseResultsTableModel = new CaseDefaultTableModel(caseList,caseHeader);
        TableModel caseResultsTableModel = new DefaultTableModel(caseList,caseHeader);
        tblCase.setModel(caseResultsTableModel);
    }

}
