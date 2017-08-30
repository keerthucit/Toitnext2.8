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
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurantMap;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class JTableCover extends JDialog {

    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicReceipts dlReceipts = null;
    private DataLogicCustomers dlCustomers = null;
    private AppView m_app;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    static Component parentLocal = null;
    static RetailTicketInfo tinfoLocal = null;
    static RetailTicketsEditor m_panelticket;
    static JRetailTicketsBagRestaurantMap restaurantMap;
    static Place place;
    static Place m_PlaceCurrent;
    public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
    public JRetailPanelTicket JRetailPanelTicket;
    private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 220;
    public static boolean isNewTable;
    public static String splitTableId;
    Logger logger = Logger.getLogger("MyLog");

    public static void showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, Place place, RetailTicketsEditor m_panelticket, JRetailTicketsBagRestaurantMap restaurantMap, boolean isNew, String splitId) {
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        JTableCover.place = place;
        JTableCover.m_panelticket = m_panelticket;
        JTableCover.restaurantMap = restaurantMap;
        isNewTable = isNew;
        splitTableId = splitId;
        showMessage(parent, dlReceipts, 1);
    }

    public static void showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, boolean isNew, String splitId) {
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        isNewTable = isNew;
        splitTableId = splitId;
        showMessage(parent, dlReceipts, 1);
    }

    private static void showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JTableCover myMsg;
        if (window instanceof Frame) {
            myMsg = new JTableCover((Frame) window, true);
        } else {
            myMsg = new JTableCover((Dialog) window, true);
        }
        myMsg.init(dlReceipts);
    }

    private JTableCover(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JTableCover(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }

    public void init(DataLogicReceipts dlReceipts) {

        initComponents();
        m_jCancel.setVisible(false);
        m_jCovers.addEditorKeys(m_jKeys);
        m_jCovers.setEnabled(true);
        m_jCovers.activate();
//        if (userRole.equalsIgnoreCase("admin")) {
//            m_jDiscountratetxt.setVisible(true);
//            m_jDiscountratelbl.setVisible(true);
//            setEnablity(true);
//
//        }


        setTitle("Table Covers");
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

        m_jBtnOk = new javax.swing.JButton();
        m_jCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        m_jKeys = new com.openbravo.editor.JEditorNumberKeys();
        m_jCovers = new com.openbravo.editor.JEditorDouble();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        m_jBtnOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnOk.setMnemonic(KeyEvent.VK_ENTER);
        m_jBtnOk.setText("Ok");
        m_jBtnOk.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jBtnOk.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jBtnOk.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jBtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnOkActionPerformed(evt);
            }
        });
        m_jBtnOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jBtnOkKeyPressed(evt);
            }
        });

        m_jCancel.setBackground(new java.awt.Color(255, 255, 255));
        m_jCancel.setMnemonic(KeyEvent.VK_ENTER);
        m_jCancel.setText("Cancel");
        m_jCancel.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jCancel.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jCancel.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCancelActionPerformed(evt);
            }
        });
        m_jCancel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jCancelKeyPressed(evt);
            }
        });

        jLabel1.setText("No of Covers");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(43, 43, 43)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(m_jCovers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(76, 76, 76)
                        .add(m_jKeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(110, 110, 110)
                        .add(m_jCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .add(m_jBtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(114, 114, 114))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(44, 44, 44)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCovers, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(m_jKeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(m_jBtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(40, 40, 40)
                .add(m_jCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-337)/2, (screenSize.height-482)/2, 337, 482);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jBtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnOkActionPerformed
        Pattern pattern = Pattern.compile(".*[^0-9].*");
        System.out.println(m_jCovers.getText());
        String input = m_jCovers.getText();
        if (m_jCovers.getText().equals("")) {
            showMessage(this, "Please enter the no of covers");
            m_jCovers.reset();
            m_jCovers.activate();
        } else if (pattern.matcher(input).matches()) {
            showMessage(this, "Please enter valid no of covers");
            m_jCovers.reset();
            m_jCovers.activate();
        } else {
            if (!isNewTable) {
                localDlReceipts.updateNoOfCovers(tinfoLocal.getPlaceId(), Integer.parseInt(m_jCovers.getText()), splitTableId);
                int noOfCovers = Integer.parseInt(m_jCovers.getText());
                tinfoLocal.setNoOfCovers(noOfCovers);
            } else {
                if (restaurantMap.getStatus() == true) {
                    m_PlaceCurrent = place;
                    tinfoLocal.setNoOfCovers(Integer.parseInt(m_jCovers.getText()));
                    tinfoLocal.setPlaceid(place.getId());
                    m_panelticket.setRetailActiveTicket(tinfoLocal, place.getName());
                    tinfoLocal.setPlaceid(place.getId());
                    restaurantMap.setTable(place.getName());
                    place.setPeople(true);
                    restaurantMap.setNoOfCovers(Integer.parseInt(m_jCovers.getText()));


                    try {
                        localDlReceipts.insertTableCovers(UUID.randomUUID().toString(), place.getId(), tinfoLocal.getSplitSharedId(), Integer.parseInt(m_jCovers.getText()));
                    } catch (BasicException ex) {
                        Logger.getLogger(JTableCover.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            dispose();
        }


        // TODO add your handling code here:
        // calculateDiscount();

}//GEN-LAST:event_m_jBtnOkActionPerformed
    private void showMessage(JTableCover aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                JOptionPane.INFORMATION_MESSAGE);

    }

    private JPanel getLabelPanel(String msg) {
        JPanel panel = new JPanel();
        Font font = new Font("Verdana", Font.BOLD, 12);
        panel.setFont(font);
        panel.setOpaque(true);
        // panel.setBackground(Color.BLUE);
        JLabel label = new JLabel(msg, JLabel.LEFT);
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(label);

        return panel;
    }

    private void m_jCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCancelActionPerformed
        m_panelticket.setRetailActiveTicket(null, "");
        JTableCover.place.setPeople(false);
        dispose();
    }//GEN-LAST:event_m_jCancelActionPerformed

    private void m_jBtnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jBtnOkKeyPressed
        //calculateDiscount();// TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnOkKeyPressed

    private void m_jCancelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jCancelKeyPressed
        this.dispose();// TODO add your handling code here:
    }//GEN-LAST:event_m_jCancelKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (isNewTable) {
            try {
                logger.info("Order No :" + tinfoLocal.getOrderId() + "  Deleting Ticket by closing cover window of Table : " + JTableCover.place.getName() + "  Id : " + tinfoLocal.getPlaceId());
                localDlReceipts.deleteSharedTicket(JTableCover.place.getId());
            } catch (BasicException ex) {
                Logger.getLogger(JTableCover.class.getName()).log(Level.SEVERE, null, ex);
            }        // TODO add your handling code here:
        }
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton m_jBtnOk;
    private javax.swing.JButton m_jCancel;
    private com.openbravo.editor.JEditorDouble m_jCovers;
    private com.openbravo.editor.JEditorNumberKeys m_jKeys;
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