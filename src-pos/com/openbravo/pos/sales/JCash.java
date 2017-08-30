/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SentenceList;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Properties;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurantMap;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JTable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keerthana
 */
public class JCash extends javax.swing.JDialog {

    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    public String[] strings = {""};
    public DefaultListModel model = null;
    static JRetailTicketsBagRestaurantMap restaurantMap;
    public JRetailPanelTicket JRetailPanelTicket;
    //Must code
    int x = 350;
    int y = 150;
    int width = 500;
    int height = 200;
    public static boolean isNewTable1;
    public static String splitTableId1;
    protected AppView m_App;
    public String role;
    public String loginUserId;
    public String roleName;
    public javax.swing.table.TableModel cashtablemodel;
    public javax.swing.table.DefaultTableModel cashtabmodel;
    public javax.swing.table.JTableHeader cashheader;
    public String[][] datacell = new String[10][10];
    public int rows;
    public int cols;
    public String[] datastring = new String[100];
    private String modenam;
    public String modename;
    private java.util.List<ShiftTallyLineInfo> shiftInfo = null;
    protected ShiftTallyLineInfo sinfo;
    static DataLogicReceipts dlReceipts;
    private SentenceList sentlist;
    public java.util.List<ShiftTallyLineInfo> ShiftTallyList = null;
    public java.util.List<ShiftTallyInfo> ShiftTallyMain = new ArrayList<ShiftTallyInfo>();
    Logger logger = Logger.getLogger("MyLog");

    //Toit Latest   
//public jTableHeader jcashtable.tableHeader;
    /**
     * Creates new form JCash
     */
    /**
     * Creates new form JRetailBufferWindow
     */
    private void init(DataLogicReceipts dlReceipt, ShiftTallyLineInfo sinfo) {
        initComponents();
        this.setResizable(false);
        setVisible(true);

    }

    public JCash(java.awt.Frame frame, boolean modal) {
        //  System.out.println("11"); //- triggers this also
        super(frame, true);
        setBounds(x, y, width, height);
    }

    private JCash(Dialog dialog, boolean b) {
        //  System.out.println("12");
        super(dialog, true);
        setBounds(x, y, width, height);
    }

    public static void showMessage(Component parent, String cashloginid, DataLogicReceipts dlReceipt, ShiftTallyLineInfo sinfo) {
        //System.out.println("13"+cashloginid);//-Triggers  here        
        Window window = getWindow(parent);
        JCash myMsg;
        dlReceipts = dlReceipt;
        if (window instanceof Frame) {
            myMsg = new JCash((Frame) window, true);
        } else {
            myMsg = new JCash((Dialog) window, true);
        }
        myMsg.loadContent(cashloginid, dlReceipts, sinfo);

    }

    public void loadContent(String cashloginid, DataLogicReceipts dlReceipt, ShiftTallyLineInfo sinfo) {
        // System.out.println("13-2");
        initComponents();
        System.out.println("13-3");
        // dlReceipts=dlReceipt;
        String loginUserId = cashloginid;
        jcashtext.setText(loginUserId);
        jcashtext.setEditable(false);

        System.out.println("Cash Header color set");
        //  printDebugData(jcashtable,dlReceipts);
        setVisible(true);

    }

    private void printDebugData(JTable table, DataLogicReceipts dlReceipt, ShiftTallyLineInfo sinfo) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        System.out.println("Rows:" + numRows + "Columns:" + numCols);
        javax.swing.table.TableModel model = table.getModel();
        dlReceipts = dlReceipt;
        System.out.println("Value of data: ");
        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
        }
    }

    //TPR Release
    private static Window getWindow(Component parent) {
        // System.out.println("14");
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }

    }

    public String getModenam() {
        return modename;
    }

    public void setModenam(String modenam1) {
        this.modename = modenam1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        panel1 = new java.awt.Panel();
        jcashtext = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jcashtable = new javax.swing.JTable();
        button1 = new java.awt.Button();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jcashtext.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N

        jcashtable.setBackground(java.awt.Color.lightGray);
        jcashtable.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 153, 153)));
        jcashtable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jcashtable.setForeground(java.awt.Color.darkGray);
        jcashtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Cash", null, null, null, null},
                {"Card", null, null, null, null},
                {"Cheque", null, null, null, null},
                {"Voucher", null, null, null, null},
                {"Complementary", null, null, null, null},
                {"Mobile", null, null, null, null}
            },
            new String [] {
                "Mode", "Opening Cash (A)", "Txn Cash (B)", "Closing Cash (C)", "Difference"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jcashtable.setToolTipText("");
        jcashtable.setGridColor(new java.awt.Color(0, 153, 0));
        jcashtable.setPreferredSize(new java.awt.Dimension(375, 200));
        jcashtable.setRowHeight(25);
        jcashtable.setSelectionBackground(java.awt.Color.white);
        jcashtable.setSelectionForeground(java.awt.Color.black);
        jScrollPane5.setViewportView(jcashtable);
        jcashtable.getColumnModel().getColumn(0).setPreferredWidth(100);
        jcashtable.getColumnModel().getColumn(1).setMinWidth(100);
        jcashtable.getColumnModel().getColumn(1).setPreferredWidth(100);
        jcashtable.getColumnModel().getColumn(1).setMaxWidth(100);

        button1.setBackground(new java.awt.Color(21, 12, 21));
        button1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button1.setForeground(java.awt.Color.white);
        button1.setLabel("Calculate Difference");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(java.awt.Color.black);
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(java.awt.Color.white);
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("User");

        jLabel2.setText("Login Time");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jcashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcashtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed

        jcashtable.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));
        jcashtable.setIntercellSpacing(new java.awt.Dimension(10, 10));
        jcashtable.getTableHeader().setBackground(Color.GRAY);
        jcashtable.getTableHeader().setSize(20, 20);
        printCalculateDifferenceData(jcashtable, dlReceipts, sinfo);

    }

    private void printCalculateDifferenceData(JTable table, DataLogicReceipts dlReceipt, ShiftTallyLineInfo pinfo) {
        System.out.println("From Difference Calculation Function");
        double A, B, C, expr;
        String mod_nam;
        dlReceipts = dlReceipt;
        shiftInfo = new ArrayList<ShiftTallyLineInfo>();

        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        System.out.println("Rows:" + numRows + "Columns:" + numCols);
        javax.swing.table.TableModel model = table.getModel();

        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setWidth(0);

        System.out.println("Value of data: ");
        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("    Column " + j + ":" + model.getValueAt(i, j).toString());
                setModenam(model.getValueAt(i, j).toString());
                modename = getModenam();
                // pinfo.setPayment_mode(modename);            
                //        System.out.print("    mode  " + modename);

                Object x = model.getValueAt(i, ++j);
                A = new Double(x.toString());

                Object y = model.getValueAt(i, ++j);
                B = new Double(y.toString());

                Object z = model.getValueAt(i, ++j);
                C = new Double(z.toString());

                try {
                    expr = A + B - C;
                    //  pinfo.setTotal(expr);
                    System.out.print("  " + modename + expr);
                    //  paymInfo.add(pinfo);                          
       //             dlReceipts.insertShiftCollection(modename, expr);

                    model.setValueAt(0000, i, ++j);
                } catch (ArithmeticException ex) {
                    //Logger.log(Level.INFO, "exception in JCash{0}", ex.getMessage());
                    Logger.getLogger(JCash.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String[][] getTableData() {
        //String[][] data = new String[this.getRowCount()][this.getColumnCount()];
        String[][] data = new String[3][3];
        // for (int r = 0; r  <  getRowCount(); r++)
        for (int r = 0; r < 6; r++) {
            //for (int c = 0; c  <  getColumnCount(); c++)
            for (int c = 0; c < 5; c++) {
                data[r][c] = (String) jcashtable.getValueAt(rows, cols);
                System.out.println("Cell value of 3 column and 3 row :" + data[r][c]);
                //  data[r][c] = jcashtable.getValueforCell(r, c);
            }
        }
        return data;
    }

    public String getValueforCell(int row, int col) {
        return (String) jcashtable.getModel().getValueAt(row, col).toString();
    }

    public String[] getTableData(String delim) {
        String[] data = new String[jcashtable.getRowCount()];

        int colCount = jcashtable.getColumnCount();
        int rowCount = jcashtable.getRowCount();

        //get the row data
        StringBuffer currRow = new StringBuffer();
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                currRow.append(jcashtable.getValueAt(row, col));
                currRow.append(delim);
            }
            data[row] = currRow.toString();
            currRow = new StringBuffer();
        }
        return data;
    }

    public Object GetData(JTable table, int row_index, int col_index) {
        return table.getModel().getValueAt(row_index, col_index);
    }

    private void WindowClosingEvent(java.awt.event.WindowEvent evt) {
        dispose();

    }//GEN-LAST:event_button1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //      // TODO add your handling code here:
        //
        //        printShiftCollection(jcashtable,dlReceipts,sinfo);
        //        dispose();
        //       // System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed
    private void printShiftCollection(JTable table, DataLogicReceipts dlReceipt, ShiftTallyLineInfo sinfo) { //

//        dlReceipts = dlReceipt;
//        try {
//
//            ShiftTallyList = dlReceipts.getShiftTallyInfoRecords();
//            String[] sListId = null;
//            System.out.println("Size of sentencelist" + ShiftTallyList.size());
//            for (int i = 0; i < ShiftTallyList.size(); i++) {
//                String listid = ShiftTallyList.get(i).getPayment_mode().toString();
//                System.out.println("1." + ShiftTallyList.get(i).getId().toString());
//                System.out.println("2." + ShiftTallyList.get(i).getPayment_mode().toLowerCase(Locale.FRENCH));
//            }
//        } catch (BasicException ex) {
//            Logger.getLogger(JCash.class.getName()).log(Level.SEVERE, null, ex);
//        }
  }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable jcashtable;
    private javax.swing.JTextField jcashtext;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}