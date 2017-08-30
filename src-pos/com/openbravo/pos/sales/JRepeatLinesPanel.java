//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.ListProvider;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JRepeatLinesPanel extends JDialog {

    private static DataLogicSales m_dlSales;
    private String m_ReturnProduct;
    private List<String> m_ReturnProductList = new ArrayList<String>();
    private ListProvider lpr;
    private List<String> itemList = null;
    private String primaryid = null;
    AppUser appUser = null;
    private String hiddenname;
    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private AppView m_app;
    public String[] strings = {""};
    public DefaultListModel model = null;
    // public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    public JPanel parent = null;
    static RetailTicketInfo tinfoLocal = null;
    public static JRetailPanelTicket addon;
    static Component parentLocal = null;
    int x = 530;
    int y = 270;
    int width = 320;
    int height = 50;
    private static String menuId;
    private static AppView m_App;
    private ProductInfoExt m_ReturnProductInfo;
    final javax.swing.JCheckBox m_jChkItemName = new javax.swing.JCheckBox();
    private Object values[] = null;

    private JRepeatLinesPanel(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, width, height);

    }

    private JRepeatLinesPanel(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, width, height);
    }

    public static List<String> showMessage(Component parent, DataLogicSales dlSales, AppView app, JRetailPanelTicket jaddon, String pname, String menupId, RetailTicketInfo tinfo) throws BasicException {
        tinfoLocal = tinfo;
        parentLocal = parent;
        addon = jaddon;
        menuId = menupId;
        m_App = app;
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

        return showMessage(parent, dlSales, pname);
    }

    private static List<String> showMessage(Component parent, DataLogicSales dlSales, String pname) {

        Window window = getWindow(parent);
        JRepeatLinesPanel myMsg;
        if (window instanceof Frame) {
            myMsg = new JRepeatLinesPanel((Frame) window, true);
        } else {
            myMsg = new JRepeatLinesPanel((Dialog) window, true);
        }

        return myMsg.init(dlSales, pname);

    }

    private static class MyListData extends javax.swing.AbstractListModel {

        private java.util.List m_data;

        public MyListData(java.util.List data) {
            m_data = data;
        }

        public Object getElementAt(int index) {
            return m_data.get(index);
        }

        public int getSize() {
            return m_data.size();
        }
    }

    public List<String> init(DataLogicSales dlSales, String pName) {

        initComponents();
        Window window = getWindow(parent);

        setRepeatItems();

        // jAddonList.setModel(new ComboBoxValModel(itemName));
        setTitle("Repeat Items");
        // m_ReturnProduct = null;
        setVisible(true);


        return m_ReturnProductList;

    }

    private void setRepeatItems() {
        System.out.println(" SetRepeatItems");
        jPanelRepeatLines.setFont(new Font("Arial",Font.BOLD,30));
        jPanelRepeatLines.removeAll();
        try {
            tinfoLocal = m_dlSales.getTicketLinesToRepeat(tinfoLocal.getPlaceId(), tinfoLocal.getSplitSharedId());
        } catch (BasicException ex) {
            Logger.getLogger(JRepeatLinesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.util.List<RetailTicketLineInfo> addedLines = tinfoLocal.getLines();
        int ItemCheckBox = addedLines.size();

        //Dynamically adding checkboxes
        for (int i = 0; i < ItemCheckBox; i++) {

          if (addedLines.get(i).getPrimaryAddon()!=0||addedLines.get(i).getAddonId()==null) {
               final javax.swing.JCheckBox m_jChkItemName = new javax.swing.JCheckBox();
               Font font = new Font("Arial", Font.BOLD, 16);
               m_jChkItemName.setFont(font);

                m_jChkItemName.setLabel(addedLines.get(i).getProductName());


                //assigning each checkbox a unique Id(Product Id)
                m_jChkItemName.setActionCommand(addedLines.get(i).getProductID());
                m_jChkItemName.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (m_jChkItemName.isSelected()) {
                            m_jChkItemName.setSelected(true);
                            m_jChkItemName.setForeground(Color.red);
                            m_ReturnProductList.add(m_jChkItemName.getText());
                        } else {
                            m_jChkItemName.setSelected(false);
                            m_jChkItemName.setForeground(Color.black);
                            m_ReturnProductList.remove(m_jChkItemName.getText());
                        }
                    }
                });
                jPanelRepeatLines.add(m_jChkItemName);//, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, dimension, -1, -1));
           }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        m_jBtnOk = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelRepeatLines = new javax.swing.JPanel();

        jButton1.setText("jButton1");

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(200, 200));

        jPanel1.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jBtnOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnOk.setText("Ok");
        m_jBtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnOkActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 75, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 390, 90, -1));

        jPanelRepeatLines.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelRepeatLines.setFont(getFont());
        jPanelRepeatLines.setLayout(new javax.swing.BoxLayout(jPanelRepeatLines, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanelRepeatLines);
        jPanelRepeatLines.getAccessibleContext().setAccessibleParent(jPanelRepeatLines);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 380));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(379, 471));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        m_ReturnProductList = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void m_jBtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnOkActionPerformed
        dispose();
    }//GEN-LAST:event_m_jBtnOkActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelRepeatLines;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jBtnOk;
    // End of variables declaration//GEN-END:variables

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    private void closeDialog() {
        this.dispose();
    }
}
