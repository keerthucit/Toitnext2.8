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

import com.openbravo.SwingCustomClasses.AutoCompletion;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SentenceList;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.inventory.DiscountSubReasonInfo;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.sales.DiscountReasonInfo;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class JRateEditor extends JDialog {

    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    //private DataLogicReceipts dlReceipts = null;
    private static DataLogicCustomers dlCustomers = null;
    private AppView m_app;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    static Component parentLocal = null;
    static RetailTicketInfo tinfoLocal = null;
    public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
    public JRetailPanelTicket JRetailPanelTicket;
    private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 280;
    public static String tinfotype;
    private java.util.List<DiscountReasonInfo> drList = null;
    private Map<String, DiscountSubReasonInfo> subReasonMap = null;

    public static void showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, String role, String type) {
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        userRole = role;
        tinfotype = type;
        dlCustomers = (DataLogicCustomers) tinfo.getM_App().getBean("com.openbravo.pos.customers.DataLogicCustomers");
        showMessage(parent, dlReceipts, 1);
    }

    private static void showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JRateEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JRateEditor((Frame) window, true);
        } else {
            myMsg = new JRateEditor((Dialog) window, true);
        }
        myMsg.init(dlReceipts);
    }

    private JRateEditor(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JRateEditor(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }

    public void init(DataLogicReceipts dlReceipts) {
        try {
            initComponents();

//        if (userRole.equalsIgnoreCase("admin")) {
            m_jDiscountratetxt.setVisible(true);
            m_jDiscountratelbl.setVisible(true);
            AutoCompletion.enable(m_jReason);
            AutoCompletion.enable(m_jSubReason);
//            setEnablity(true);
//
//        }

            java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
            for (DiscountRateinfo dis : list) {
                m_jDiscountPercentage.addItem(dis.getName());
            }
            m_jDiscountPercentage.setSelectedIndex(-1);

            java.util.List<DiscountReasonInfo> reasonList = dlReceipts.getDiscountReason();
            drList = reasonList;
            for (DiscountReasonInfo dis : reasonList) {
                m_jReason.addItem(dis.getReason());
            }
            m_jReason.setSelectedIndex(-1);

            //Added new functionality discount subreason 14/6/2016
            subReasonMap = new HashMap<String, DiscountSubReasonInfo>();
            java.util.List<DiscountSubReasonInfo> subReasonList = dlCustomers.getActiveDiscountSubReasonList();
            for (DiscountSubReasonInfo dis : subReasonList) {
                //m_jSubReason.addItem(dis.getSubReason());
                //  System.out.println("SUB REASONS: "+dis.getReasonId()+dis.getSubreasonId());
                subReasonMap.put(dis.getReasonId() + dis.getSubreasonId(), new DiscountSubReasonInfo(dis.getReasonId(), dis.getSubreasonId(), dis.getSubReason()));
            }
            m_jSubReason.setSelectedIndex(-1);
        } catch (BasicException ex) {
            Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        setTitle("Discounts Editor");
        setVisible(true);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jDiscountPercentage = new javax.swing.JComboBox();
        m_jdiscountOk = new javax.swing.JButton();
        m_jDiscountratetxt = new javax.swing.JTextField();
        m_jdiscountCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        m_jReason = new javax.swing.JComboBox();
        m_jSubReason = new javax.swing.JComboBox();
        jLabelsubreason = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextComments = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        m_jDiscountratelbl = new javax.swing.JLabel();

        m_jDiscountPercentage.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jDiscountPercentage.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jDiscountPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDiscountPercentageActionPerformed(evt);
            }
        });

        m_jdiscountOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jdiscountOk.setMnemonic(KeyEvent.VK_ENTER);
        m_jdiscountOk.setText("Ok");
        m_jdiscountOk.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jdiscountOk.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jdiscountOk.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jdiscountOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jdiscountOkActionPerformed(evt);
            }
        });
        m_jdiscountOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jdiscountOkKeyPressed(evt);
            }
        });

        m_jDiscountratetxt.setVisible(false);
        m_jDiscountratetxt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null));
        m_jDiscountratetxt.setMinimumSize(new java.awt.Dimension(23, 18));
        m_jDiscountratetxt.setPreferredSize(new java.awt.Dimension(43, 30));
        m_jDiscountratetxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m_jDiscountratetxtMousePressed(evt);
            }
        });
        m_jDiscountratetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDiscountratetxtActionPerformed(evt);
            }
        });
        m_jDiscountratetxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jDiscountratetxtKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                m_jDiscountratetxtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jDiscountratetxtKeyTyped(evt);
            }
        });

        m_jdiscountCancel.setBackground(new java.awt.Color(255, 255, 255));
        m_jdiscountCancel.setMnemonic(KeyEvent.VK_ENTER);
        m_jdiscountCancel.setText("Cancel");
        m_jdiscountCancel.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jdiscountCancel.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jdiscountCancel.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jdiscountCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jdiscountCancelActionPerformed(evt);
            }
        });
        m_jdiscountCancel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jdiscountCancelKeyPressed(evt);
            }
        });

        jLabel1.setText("Category");

        m_jReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReasonActionPerformed(evt);
            }
        });

        m_jSubReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jSubReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jSubReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSubReasonActionPerformed(evt);
            }
        });

        jLabelsubreason.setText("Sub-category");

        jLabel2.setText("Comments");

        jTextComments.setColumns(20);
        jTextComments.setRows(5);
        jScrollPane1.setViewportView(jTextComments);

        jLabel3.setText("Manual Rate");

        m_jDiscountratelbl.setText("Rate");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jLabelsubreason, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .add(m_jSubReason, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(m_jReason, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(2, 2, 2)
                                        .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                                    .add(m_jDiscountratelbl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(m_jDiscountratetxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(m_jDiscountPercentage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(13, 13, 13))))
                    .add(layout.createSequentialGroup()
                        .add(68, 68, 68)
                        .add(m_jdiscountOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(31, 31, 31)
                        .add(m_jdiscountCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(34, 34, 34)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jDiscountPercentage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jDiscountratelbl))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jDiscountratetxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(m_jReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jSubReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelsubreason))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jdiscountOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jdiscountCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(363, 375));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jdiscountOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jdiscountOkActionPerformed
        calculateDiscount();

}//GEN-LAST:event_m_jdiscountOkActionPerformed

//    private void calculateDiscount(){
//        System.out.println("calculateDiscount");
//          int discountIndex = m_jDiscountPercentage.getSelectedIndex();
//        String dRate = m_jDiscountratetxt.getText().toString();
//        if (discountIndex <= 0 && dRate.equals("")) {
//            if (isEnablity()) {
//                JOptionPane.showMessageDialog(null, "Select either popup or enter amount ");
//            } else {
//                JOptionPane.showMessageDialog(null, "Select Discount");
//            }
//        } else {
//            try {
//
//               // tinfoLocal.setdName(localDlReceipts.getDiscountLine(m_jDiscountPercentage.getSelectedItem().toString()));
//                tinfoLocal.setRate(localDlReceipts.getDiscountLine(m_jDiscountPercentage.getSelectedItem().toString()));
//            } catch (NullPointerException np) {
//                tinfoLocal.setdName(null);
//            } catch (BasicException ex) {
//                Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if (discountIndex > 0) {
//                RetailTicketInfo.setUserate(false);
//                JTicketsBagShared.setUserate(false);
//                String discountItem = m_jDiscountPercentage.getSelectedItem().toString();
//                String rateSelected = null;
//                try {
//                    rateSelected = localDlReceipts.getDiscountLine(discountItem);
//                } catch (BasicException ex) {
//                    Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                tinfoLocal.setCustomerDiscount(rateSelected);
//                if(tinfotype.equals("home")){
//                  tinfoLocal.refreshHomeTxtFields(1);
//                }else if(tinfotype.equals("Editsales")) {
//                  tinfoLocal.refreshEditTxtFields(1);
//
//                }else{
//                  tinfoLocal.refreshTxtFields(1);
//                }
//                this.dispose();
//              //  bookingSales = (JPanelBookingSales) app.getBean("com.openbravo.pos.sales.JPanelBookingSales");
//
//            } else {
//
//                RetailTicketInfo.setUserate(true);
//                JTicketsBagShared.setUserate(true);
//                if (dRate == null || dRate.equals("")) {
//                    if ("0".equals(userRole)) {
//                        JOptionPane.showMessageDialog(null, "You should select Percent or Enter Rate");
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Please select Percent ");
//                    }
//                } else {
//
//                    tinfoLocal.setRate(dRate);
//
//                     if(tinfotype.equals("sales")){
//                          tinfoLocal.refreshTxtFields(2);
//                    }else{
//                         tinfoLocal.refreshHomeTxtFields(2);
//                }this.dispose();
//
//                }
//            }
//        }
//
//    }
    private void calculateDiscount() {

        int discountIndex = m_jDiscountPercentage.getSelectedIndex();
        int dReasonSubcatIndex=m_jSubReason.getSelectedIndex();
        String dRate = m_jDiscountratetxt.getText().toString();
        int dReasonIndex = m_jReason.getSelectedIndex();
        String comments = jTextComments.getText().toString();
        System.out.println(dRate + "dRate printing" + discountIndex + dRate);
        if (discountIndex < 0 && (dRate == "" || dRate == null || dRate.equals(""))) {
            JOptionPane.showMessageDialog(null, "Select Discount or Enter the Rate ");
        } else if (dReasonIndex < 0 &&  dReasonSubcatIndex <= 0 ) {
            JOptionPane.showMessageDialog(null, "Select Discount Category");
        } else if (dReasonIndex > 0 && dReasonSubcatIndex <= 0) {
            JOptionPane.showMessageDialog(null, "Select Discount Sub-Category");
        } else {
            try {
                if (discountIndex > -1) {
                    tinfoLocal.setRate(localDlReceipts.getDiscountLine(m_jDiscountPercentage.getSelectedItem().toString()));
                } else {
                    Double discountRate = Double.parseDouble(dRate) / 100;
                    tinfoLocal.setRate(discountRate.toString());
                }
                tinfoLocal.setDiscountReasonId(drList.get(dReasonIndex).getId());
                tinfoLocal.setDiscountComments(comments);
                if (m_jSubReason.getSelectedItem() != null) {
                    String subreasonText = m_jSubReason.getSelectedItem().toString();
                    //  if (subReasonMap.containsKey(drList.get(dReasonIndex).getId())) {
                    for (Map.Entry<String, DiscountSubReasonInfo> subreason : subReasonMap.entrySet()) {
                        if (subreason.getKey().equals(drList.get(dReasonIndex).getId() + subreason.getValue().getSubreasonId()) && subreason.getValue().getSubReason().equals(subreasonText)) {
                            //  System.out.println("matches" + subreason.getValue().getId());
                            tinfoLocal.setDiscountSubReasonId(subreason.getValue().getSubreasonId());
                            break;
                        }
                    }

                    // }
                }
            } catch (NullPointerException np) {
                tinfoLocal.setdName(null);
            } catch (BasicException ex) {
                Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (discountIndex > -1 || dRate != "") {
                RetailTicketInfo.setUserate(false);
                JTicketsBagShared.setUserate(false);
                String rateSelected = null;
                if (discountIndex > -1) {
                    String discountItem = m_jDiscountPercentage.getSelectedItem().toString();
                    try {
                        rateSelected = localDlReceipts.getDiscountLine(discountItem);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    rateSelected = dRate;
                }
                tinfoLocal.setCustomerDiscount(rateSelected);
                tinfoLocal.refreshTxtFields(1);


                this.dispose();

            }

        }

    }
    private void m_jdiscountCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jdiscountCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_m_jdiscountCancelActionPerformed

    private void m_jDiscountPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDiscountPercentageActionPerformed
        // TODO add your handling code here:
        int index = m_jDiscountPercentage.getSelectedIndex();
        if (index > -1) {
            m_jDiscountratetxt.setText("");
        }
    }//GEN-LAST:event_m_jDiscountPercentageActionPerformed

    private void m_jdiscountOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jdiscountOkKeyPressed
        calculateDiscount();// TODO add your handling code here:
    }//GEN-LAST:event_m_jdiscountOkKeyPressed

    private void m_jdiscountCancelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jdiscountCancelKeyPressed
        this.dispose();// TODO add your handling code here:
    }//GEN-LAST:event_m_jdiscountCancelKeyPressed

    private void m_jReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReasonActionPerformed
        if (m_jReason.getSelectedIndex() > -1) {
            m_jSubReason.removeAllItems();
            m_jSubReason.addItem("");
            String reason = drList.get(m_jReason.getSelectedIndex()).getId();
            if (subReasonMap != null) {
                //    if (subReasonMap.containsKey(reason)) {
                for (Map.Entry<String, DiscountSubReasonInfo> subreason : subReasonMap.entrySet()) {
                    String subReason = subreason.getValue().getSubreasonId();
                    if (subreason.getKey().equals(reason + subReason)) {
                        m_jSubReason.addItem(subreason.getValue().getSubReason());
                    }
                }
                // }
            }
        }


}//GEN-LAST:event_m_jReasonActionPerformed

    private void m_jDiscountratetxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jDiscountratetxtKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jDiscountratetxtKeyTyped

    private void m_jDiscountratetxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jDiscountratetxtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jDiscountratetxtKeyPressed

    private void m_jDiscountratetxtMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jDiscountratetxtMousePressed
        if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
            return;
        } else {
            try {
                Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
            } catch (IOException ex) {
                Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // T        // TODO add your handling code here:
    }//GEN-LAST:event_m_jDiscountratetxtMousePressed

    private void m_jSubReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSubReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jSubReasonActionPerformed

    private void m_jDiscountratetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDiscountratetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jDiscountratetxtActionPerformed

    private void m_jDiscountratetxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jDiscountratetxtKeyReleased
        String discountText = m_jDiscountratetxt.getText();
        double discountRate = 0;
        try {
            System.out.println("discountText" + discountText);
            if (!"".equals(discountText)) {
                discountRate = Double.parseDouble(discountText);
            }
            if (discountRate > 100) {
                m_jDiscountratetxt.setText("");
                JOptionPane.showMessageDialog(null, "Please enter proper discount rate");
            }
        } catch (NumberFormatException e) {
            m_jDiscountratetxt.setText("");
            JOptionPane.showMessageDialog(null, "Please enter proper discount rate");
        } finally {
            if (!"".equals(discountText)) {
                m_jDiscountPercentage.setSelectedIndex(-1);
            } else {
                m_jDiscountPercentage.setEnabled(true);
            }
        }
    }//GEN-LAST:event_m_jDiscountratetxtKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelsubreason;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextComments;
    private javax.swing.JComboBox m_jDiscountPercentage;
    private javax.swing.JLabel m_jDiscountratelbl;
    private javax.swing.JTextField m_jDiscountratetxt;
    private javax.swing.JComboBox m_jReason;
    private javax.swing.JComboBox m_jSubReason;
    private javax.swing.JButton m_jdiscountCancel;
    private javax.swing.JButton m_jdiscountOk;
    // End of variables declaration//GEN-END:variables

    public void populateList(DataLogicCustomers dlCustomers) throws BasicException {

        list = dlCustomers.getDiscountList();
        String[] dListId = null;
        String[] dListRate = null;
        for (int i = 0; i < list.size(); i++) {
            String listid = list.get(i).getName();
            model.add(i, listid);
            //dListRate[i] = list.get(i).getRate();
        }
    }

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    /**
     * @return the enablity
     */
    public boolean isEnablity() {
        return enablity;
    }

    /**
     * @param enablity the enablity to set
     */
    public void setEnablity(boolean enablity) {
        this.enablity = enablity;
    }
}