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
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.inventory.StaffInfo;
import com.openbravo.pos.payment.JPaymentNotifier;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.RoundUtils;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.List;

public class JPaymentEditor extends JDialog {

    private static boolean completed = false;
    private JPaymentNotifier m_notifier;
    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicReceipts dlReceipts = null;
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
    public static JRetailPanelTicket JRetailPanelTicket;
    private boolean enablity;
    int x = 350;
    int y = 200;
    int width = 650;
    int height = 400;
    public static String tinfotype;
    public PaymentInfoList m_aPaymentInfo;
    PaymentInfoCash cash = null;
    PaymentInfoCard card = null;
    double totalAmount = 0;
    double cashAmount = 0;
    double cardAmount = 0;
    private double m_dPaid;
    private double m_dTotal;
    private int payMode = 0;
    Logger logger = Logger.getLogger("MyLog");
    List<StaffInfo> staffList = null;
    List<MobileTypeInfo> mobileTypeList = null;

    public static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, JRetailPanelTicket retailTicket) {

        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        JRetailPanelTicket = retailTicket;
        dlCustomers = retailTicket.dlCustomers;
        boolean completed = showMessage(parent, dlReceipts, 1);
        return completed;
    }

    private static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JPaymentEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JPaymentEditor((Frame) window, true);
        } else {
            myMsg = new JPaymentEditor((Dialog) window, true);
        }
        boolean completed = myMsg.init(dlReceipts);
        return completed;
    }

    private JPaymentEditor(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JPaymentEditor(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }

    private void populateStaff() {
        try {
            staffList = dlCustomers.getActiveStaffList();
        } catch (BasicException ex) {
            Logger.getLogger(JPaymentEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (StaffInfo st : staffList) {
            jComboStaff.addItem(st.getName());
        }
    }

    private void populateMobileTypes() {
        try {
            mobileTypeList = dlCustomers.getMobileTypeList();
        } catch (BasicException ex) {
            Logger.getLogger(JPaymentEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (MobileTypeInfo mf : mobileTypeList) {
            jComboMobileType.addItem(mf.getMobileType());
            jComboSplitPayMobile.addItem(mf.getMobileType());
        }
    }

    private void printState() {
        m_jChangeEuros.setDoubleValue(null);
        String billamount = m_jTxtBillAmount.getText().toString();
        double billAmt = tinfoLocal.getTotal();
        //   Double billamt=Double.parseDouble(billamount.substring(4));

        Double cashValue;
        if (m_jCash.getDoubleValue() == null) {
            cashValue = 0.0;
        } else {
            cashValue = m_jCash.getDoubleValue();
        }


        //        m_dPaid = cashValue;

        Double differenceAmt = billAmt - new Double(cashValue);
        Double change = new Double(cashValue) - billAmt;

        if (differenceAmt > 0) {
            m_jCard.setDoubleValue(differenceAmt);
        } else {
            m_jCard.setDoubleValue(null);
            m_jChangeEuros.setDoubleValue(change);
        }


    }

    private void CardprintState() {
        m_jChangeEuros.setDoubleValue(null);
        String billamount = m_jTxtBillAmount.getText().toString();
        // Double billamt=Double.parseDouble(billamount.substring(4));
        double billAmt = tinfoLocal.getTotal();

        Double cardValue;
        if (m_jCard.getDoubleValue() == null) {
            cardValue = 0.0;
        } else {
            cardValue = m_jCard.getDoubleValue();
        }

        //     m_dPaid = cardValue;


        Double differenceAmt = billAmt - new Double(cardValue);
        Double change = new Double(cardValue) - billAmt;
        if (differenceAmt > 0) {
            m_jCash.setDoubleValue(differenceAmt);
        } else {
            m_jCash.setDoubleValue(null);
            m_jChangeEuros.setDoubleValue(change);
        }

        //m_notifier.setStatus(m_dPaid > 0.0, iCompare >= 0);
    }

    private void ChangeState() {
        m_jChangeEuros.setDoubleValue(null);
        String billamount = m_jTxtBillAmount.getText().toString();
        double billAmt = tinfoLocal.getTotal();
        //   Double billamt=Double.parseDouble(billamount.substring(4));

        Double cashValue;
        if (m_jCash.getDoubleValue() == null) {
            cashValue = 0.0;
        } else {
            cashValue = m_jCash.getDoubleValue();
        }
        Double cardValue;
        if (m_jCard.getDoubleValue() == null) {
            cardValue = 0.0;
        } else {
            cardValue = m_jCard.getDoubleValue();
        }

        Double voucherValue;
        if (m_jVoucher.getDoubleValue() == null) {
            voucherValue = 0.0;
        } else {
            voucherValue = m_jVoucher.getDoubleValue();
        }

        Double mobileValue;
        if (m_jMobile.getDoubleValue() == null) {
            mobileValue = 0.0;
        } else {
            mobileValue = m_jMobile.getDoubleValue();
        }


        Double differenceAmt = billAmt - (new Double(cashValue) + new Double(cardValue) + new Double(voucherValue) + new Double(mobileValue));
        Double change = (new Double(cashValue) + new Double(cardValue) + new Double(voucherValue) + new Double(mobileValue)) - billAmt;

        if (differenceAmt <= 0) {
            m_jChangeEuros.setDoubleValue(change);
        }

    }

    public boolean init(DataLogicReceipts dlReceipts) {
        initComponents();
        jVoucherPanel.setVisible(false);
        jMobilepanel.setVisible(false);
        jChequePanel.setVisible(false);
        jStaffPanel.setVisible(false);
        m_jTxtBillAmount.setEditable(false);
        populateStaff();
        populateMobileTypes();
//        if (!tinfoLocal.getMomoePhoneNo().equals("")) {
//            m_jBtnMobile.setVisible(true);
//            jLabelMobile.setVisible(true);
//            m_jMobile.setVisible(true);
//            m_jTextMobileSplit.setVisible(true);
//        } else {
//            m_jBtnMobile.setVisible(false);
//            jLabelMobile.setVisible(false);
//            m_jMobile.setVisible(false);
//            m_jTextMobileSplit.setVisible(false);
//        }
        double billAmt = tinfoLocal.getTotal();
        m_jTxtBillAmount.setText(Double.toString(tinfoLocal.getTotal()));
        //   m_jCash.addPropertyChangeListener("Edition", new RecalculateState());
        //   m_jCard.addPropertyChangeListener("Edition", new CardRecalculateState());
        m_jCash.addPropertyChangeListener("Edition", new ChangeValueState());
        m_jCard.addPropertyChangeListener("Edition", new ChangeValueState());
        m_jMobile.addPropertyChangeListener("Edition", new ChangeValueState());
        m_jVoucher.addPropertyChangeListener("Edition", new ChangeValueState());

        m_jCash.addEditorKeys(m_jkeys);
        m_jCard.addEditorKeys(m_jkeys);
        m_jMobile.addEditorKeys(m_jkeys);
        m_jVoucher.addEditorKeys(m_jkeys);
        m_jVoucherNum.addEditorKeys(m_jkeys);
        m_jChequeNum.addEditorKeys(m_jkeys);
        m_jMobileNo.addEditorKeys(m_jkeys);
        m_jSplitMobileNo.addEditorKeys(m_jkeys);

        m_jCash.setVisible(false);
        m_jCard.setVisible(false);
        m_jVoucherNum.setVisible(false);
        //   m_jMobileNo.setVisible(false);
        m_jChequeNum.setVisible(false);
        m_jTxtCash.setEditable(false);
        m_jTxtCard.setEditable(false);
        m_jMobile.setVisible(false);
        m_jVoucher.setVisible(false);
        m_jTextMobileSplit.setEditable(false);
        m_jTxtVoucherSplit.setEditable(false);
        m_jTxtVoucherNum.setEditable(false);
        m_jTxtChequeNum.setEditable(false);
        m_jSplitMobileNo.setEnabled(false);
        jComboSplitPayMobile.setEnabled(false);

        jComboStaff.setEditable(true);
        AutoCompletion.enable(jComboStaff);
        setTitle("Payment Editor");
        setVisible(true);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
        return completed;
    }

    private class RecalculateState implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            printState();
        }
    }

    private class CardRecalculateState implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            CardprintState();
        }
    }

    private class ChangeValueState implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            ChangeState();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jTxtBillAmount = new javax.swing.JTextField();
        m_jBtnCash = new javax.swing.JButton();
        m_jBtnCard = new javax.swing.JButton();
        m_jSplit = new javax.swing.JButton();
        m_jPay = new javax.swing.JButton();
        m_jClose = new javax.swing.JButton();
        m_jkeys = new com.openbravo.editor.JEditorNumberKeys();
        jLabel4 = new javax.swing.JLabel();
        m_jChangeEuros = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jBtnStaff = new javax.swing.JButton();
        m_jBtnComplimentary = new javax.swing.JButton();
        m_jBtnVoucher = new javax.swing.JButton();
        m_jBtnCheque = new javax.swing.JButton();
        jViewPanel = new javax.swing.JPanel();
        jPaymentPanel = new javax.swing.JPanel();
        jLabelCash = new javax.swing.JLabel();
        jLabelMobile = new javax.swing.JLabel();
        m_jCash = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jCard = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jTxtCash = new javax.swing.JTextField();
        m_jTxtCard = new javax.swing.JTextField();
        jLabelVoucher = new javax.swing.JLabel();
        m_jMobile = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jTextMobileSplit = new javax.swing.JTextField();
        jLabelCard = new javax.swing.JLabel();
        m_jVoucher = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jTxtVoucherSplit = new javax.swing.JTextField();
        jComboSplitPayMobile = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        m_jSplitMobileNo = new com.openbravo.editor.JEditorCurrencyPositive();
        jVoucherPanel = new javax.swing.JPanel();
        jVLabel = new javax.swing.JLabel();
        jVNumLabel = new javax.swing.JLabel();
        m_jVoucherNum = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jTxtVoucherNum = new javax.swing.JTextField();
        m_jTxtVoucher = new javax.swing.JTextField();
        jChequePanel = new javax.swing.JPanel();
        jCLabel = new javax.swing.JLabel();
        jCNumLabel = new javax.swing.JLabel();
        m_jChequeNum = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jTxtChequeNum = new javax.swing.JTextField();
        m_jTxtCheque = new javax.swing.JTextField();
        jMobilepanel = new javax.swing.JPanel();
        jComboMobileType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        m_jMobileNo = new com.openbravo.editor.JEditorCurrencyPositive();
        jStaffPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboStaff = new javax.swing.JComboBox();
        m_jBtnMobile = new javax.swing.JButton();

        jScrollPane1.setViewportView(jEditorPane1);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Bill Amount");

        m_jTxtBillAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        m_jBtnCash.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Tcash.png"))); // NOI18N
        m_jBtnCash.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnCash.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnCash.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCashActionPerformed(evt);
            }
        });

        m_jBtnCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Tcard.png"))); // NOI18N
        m_jBtnCard.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnCard.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnCard.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCardActionPerformed(evt);
            }
        });

        m_jSplit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/split-payment.png"))); // NOI18N
        m_jSplit.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jSplit.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jSplit.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jSplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSplitActionPerformed(evt);
            }
        });

        m_jPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/paytoit.png"))); // NOI18N
        m_jPay.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jPay.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jPay.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPayActionPerformed(evt);
            }
        });

        m_jClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Tclose.png"))); // NOI18N
        m_jClose.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseActionPerformed(evt);
            }
        });

        m_jkeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jkeysActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Change");

        m_jChangeEuros.setEnabled(false);

        m_jBtnStaff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/staff.png"))); // NOI18N
        m_jBtnStaff.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnStaff.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnStaff.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnStaffActionPerformed(evt);
            }
        });

        m_jBtnComplimentary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/complementary.png"))); // NOI18N
        m_jBtnComplimentary.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnComplimentary.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnComplimentary.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnComplimentary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnComplimentaryActionPerformed(evt);
            }
        });

        m_jBtnVoucher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/voucher.png"))); // NOI18N
        m_jBtnVoucher.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnVoucher.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnVoucher.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnVoucherActionPerformed(evt);
            }
        });

        m_jBtnCheque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/cheque.png"))); // NOI18N
        m_jBtnCheque.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnCheque.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnCheque.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnChequeActionPerformed(evt);
            }
        });

        jPaymentPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPaymentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelCash.setText("Cash");
        jPaymentPanel.add(jLabelCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 72, 20));

        jLabelMobile.setText("Mobile");
        jPaymentPanel.add(jLabelMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 60, 40));

        m_jCash.setPreferredSize(new java.awt.Dimension(200, 25));
        m_jCash.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                m_jCashFocusGained(evt);
            }
        });
        m_jCash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jCashKeyTyped(evt);
            }
        });
        jPaymentPanel.add(m_jCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 180, -1));

        m_jCard.setPreferredSize(new java.awt.Dimension(200, 25));
        jPaymentPanel.add(m_jCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 180, -1));

        m_jTxtCash.setPreferredSize(new java.awt.Dimension(6, 25));
        jPaymentPanel.add(m_jTxtCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 170, -1));
        jPaymentPanel.add(m_jTxtCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 170, 25));

        jLabelVoucher.setText("Voucher");
        jPaymentPanel.add(jLabelVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 60, 40));

        m_jMobile.setPreferredSize(new java.awt.Dimension(200, 25));
        jPaymentPanel.add(m_jMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 100, -1));

        m_jTextMobileSplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTextMobileSplitActionPerformed(evt);
            }
        });
        jPaymentPanel.add(m_jTextMobileSplit, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 80, 25));

        jLabelCard.setText("Card");
        jPaymentPanel.add(jLabelCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 60, 20));

        m_jVoucher.setPreferredSize(new java.awt.Dimension(200, 25));
        jPaymentPanel.add(m_jVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 180, -1));

        m_jTxtVoucherSplit.setPreferredSize(new java.awt.Dimension(6, 25));
        jPaymentPanel.add(m_jTxtVoucherSplit, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 170, -1));

        jPaymentPanel.add(jComboSplitPayMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 80, -1));

        jLabel6.setText("Mobile No.");
        jPaymentPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        m_jSplitMobileNo.setPreferredSize(new java.awt.Dimension(200, 25));
        jPaymentPanel.add(m_jSplitMobileNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 180, -1));

        jVoucherPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jVoucherPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jVLabel.setText("Voucher");
        jVoucherPanel.add(jVLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 15, 90, 20));

        jVNumLabel.setText("Voucher No.");
        jVoucherPanel.add(jVNumLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 58, 90, 20));

        m_jVoucherNum.setPreferredSize(new java.awt.Dimension(200, 25));
        jVoucherPanel.add(m_jVoucherNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 58, 170, -1));
        jVoucherPanel.add(m_jTxtVoucherNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 170, 25));

        m_jTxtVoucher.setEditable(false);
        m_jTxtVoucher.setPreferredSize(new java.awt.Dimension(6, 25));
        jVoucherPanel.add(m_jTxtVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 140, -1));

        jChequePanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jChequePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jCLabel.setText("Amount");
        jChequePanel.add(jCLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 15, 90, 20));

        jCNumLabel.setText("Cheque  No.");
        jChequePanel.add(jCNumLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 58, 90, 20));

        m_jChequeNum.setPreferredSize(new java.awt.Dimension(200, 25));
        jChequePanel.add(m_jChequeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 58, 170, -1));
        jChequePanel.add(m_jTxtChequeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 170, 25));

        m_jTxtCheque.setEditable(false);
        m_jTxtCheque.setPreferredSize(new java.awt.Dimension(6, 25));
        jChequePanel.add(m_jTxtCheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 140, -1));

        jMobilepanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(185, 176, 176)));
        jMobilepanel.setForeground(new java.awt.Color(204, 204, 204));
        jMobilepanel.setAlignmentX(0.0F);
        jMobilepanel.setAlignmentY(0.0F);
        jMobilepanel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jMobilepanel.setMinimumSize(new java.awt.Dimension(270, 38));
        jMobilepanel.setPreferredSize(new java.awt.Dimension(270, 38));

        jLabel3.setText("Mobile Type");

        jLabel5.setText("Mobile No.");

        m_jMobileNo.setPreferredSize(new java.awt.Dimension(200, 25));

        org.jdesktop.layout.GroupLayout jMobilepanelLayout = new org.jdesktop.layout.GroupLayout(jMobilepanel);
        jMobilepanel.setLayout(jMobilepanelLayout);
        jMobilepanelLayout.setHorizontalGroup(
            jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jMobilepanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jMobilepanelLayout.createSequentialGroup()
                        .add(jComboMobileType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 52, Short.MAX_VALUE))
                    .add(jMobilepanelLayout.createSequentialGroup()
                        .add(m_jMobileNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jMobilepanelLayout.setVerticalGroup(
            jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jMobilepanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboMobileType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .add(18, 18, 18)
                .add(jMobilepanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(m_jMobileNo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap(244, Short.MAX_VALUE))
        );

        jStaffPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jStaffPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Name:");
        jStaffPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jComboStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jComboStaffMousePressed(evt);
            }
        });
        jComboStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboStaffActionPerformed(evt);
            }
        });
        jStaffPanel.add(jComboStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 200, -1));

        org.jdesktop.layout.GroupLayout jViewPanelLayout = new org.jdesktop.layout.GroupLayout(jViewPanel);
        jViewPanel.setLayout(jViewPanelLayout);
        jViewPanelLayout.setHorizontalGroup(
            jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPaymentPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jVoucherPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jChequePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jMobilepanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jStaffPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
        );
        jViewPanelLayout.setVerticalGroup(
            jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jViewPanelLayout.createSequentialGroup()
                .add(jPaymentPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 11, Short.MAX_VALUE))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jViewPanelLayout.createSequentialGroup()
                    .add(jVoucherPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 331, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jViewPanelLayout.createSequentialGroup()
                    .add(jChequePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 331, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jViewPanelLayout.createSequentialGroup()
                    .add(jMobilepanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .add(jViewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jViewPanelLayout.createSequentialGroup()
                    .add(jStaffPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 1, Short.MAX_VALUE)))
        );

        m_jBtnMobile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Mobile.png"))); // NOI18N
        m_jBtnMobile.setMaximumSize(new java.awt.Dimension(90, 40));
        m_jBtnMobile.setMinimumSize(new java.awt.Dimension(90, 40));
        m_jBtnMobile.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnMobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnMobileActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jTxtBillAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(47, 47, 47)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jBtnComplimentary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jBtnVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(1, 1, 1)
                                .add(m_jBtnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(m_jBtnCash, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(m_jBtnCard, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(m_jBtnStaff, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(m_jBtnMobile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jViewPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jChangeEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(m_jkeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))))
            .add(jPanel1Layout.createSequentialGroup()
                .add(163, 163, 163)
                .add(m_jSplit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jClose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {m_jChangeEuros, m_jTxtBillAmount}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jChangeEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(m_jTxtBillAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jkeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(m_jBtnCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnStaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnComplimentary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jBtnMobile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jViewPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(m_jPay, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jClose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jSplit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 5, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(642, 532));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jBtnCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCashActionPerformed
        String billAmt = m_jTxtBillAmount.getText().toString();

        int res = JOptionPane.showConfirmDialog(this, "Payment of " + billAmt + " will be received as Cash.Click Ok to confirm.", AppLocal.getIntString("title.editor"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            logger.info("Start Settle Bill Cash Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()) + ";");
            logger.info("No. of Line Items during Cash Settlement : " + tinfoLocal.getLinesCount() + ";");
            m_jCash.setVisible(false);
            m_jCard.setVisible(false);
            m_jTxtCash.setVisible(true);
            m_jTxtCard.setVisible(true);
            m_jTxtCash.setText(tinfoLocal.printTotal());
            m_jTxtCard.setEnabled(false);
            m_jMobile.setEnabled(false);
            jComboSplitPayMobile.setEnabled(false);
            m_jMobile.setVisible(false);
            m_jVoucher.setEnabled(false);
            m_jVoucher.setVisible(false);
            if (!tinfoLocal.getMomoePhoneNo().equals("")) {
                m_jTextMobileSplit.setVisible(true);
            }
            m_jTxtVoucherSplit.setVisible(true);
            m_jTxtCash.setEnabled(true);
            JRetailPanelTicket.cashPayment(1, tinfoLocal);// TODO add your handling code here:
            completed = true;

            dispose();
            //logger.info("End Settle Bill Cash Button :"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));  
        }

    }//GEN-LAST:event_m_jBtnCashActionPerformed

    private void m_jBtnCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCardActionPerformed


        String billAmt = m_jTxtBillAmount.getText().toString();
        int res = JOptionPane.showConfirmDialog(this, "Payment of " + billAmt + " will be received as Card.Click Ok to confirm.", AppLocal.getIntString("title.editor"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            logger.info("Start Settle Bill Card Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()) + ";");
            logger.info("No. of Line Items during Card Settlement : " + tinfoLocal.getLinesCount() + ";");
            m_jCash.setVisible(false);
            m_jCard.setVisible(false);
            m_jTxtCash.setVisible(true);
            m_jTxtCard.setVisible(true);
            m_jTxtCash.setEnabled(false);
            m_jMobile.setVisible(false);
            m_jVoucher.setVisible(false);
            if (!tinfoLocal.getMomoePhoneNo().equals("")) {
                m_jTextMobileSplit.setVisible(true);
            }
            m_jTxtVoucherSplit.setVisible(true);
            m_jTextMobileSplit.setEnabled(false);
            m_jTxtVoucherSplit.setEnabled(false);
            m_jTxtCard.setEnabled(true);
            m_jTxtCard.setText(tinfoLocal.printTotal());
            JRetailPanelTicket.cardPayment(1, tinfoLocal);// TODO add your handling code here:
            completed = true;
            dispose();
            logger.info("End Settle Bill Card Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()) + ";");
        }
    }//GEN-LAST:event_m_jBtnCardActionPerformed

    private void m_jCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseActionPerformed
        completed = false;
        dispose();
    }//GEN-LAST:event_m_jCloseActionPerformed

    private void m_jSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSplitActionPerformed
        jStaffPanel.setVisible(false);
        jVoucherPanel.setVisible(false);
        jMobilepanel.setVisible(false);
        m_jSplitMobileNo.setEnabled(true);
        jChequePanel.setVisible(false);
        jPaymentPanel.setVisible(true);
        m_jCash.setVisible(true);
        m_jCard.setVisible(true);        // TODO add your handling code here:
        //     if (!tinfoLocal.getMomoePhoneNo().equals("")) {
        m_jMobile.setVisible(true);
        //      }
        m_jVoucher.setVisible(true);
        m_jTxtVoucherSplit.setVisible(false);
        m_jTextMobileSplit.setVisible(false);
        m_jTxtCash.setVisible(false);
        m_jTxtCard.setVisible(false);
        jComboSplitPayMobile.setEnabled(true);

        m_jVoucherNum.reset();
        m_jChequeNum.reset();
        m_jMobileNo.reset();
        payMode = 3;


    }//GEN-LAST:event_m_jSplitActionPerformed

    private void m_jPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPayActionPerformed
        double totalBillValue = 0;
        //this is for cheque payment
       
        if (payMode == 1) {
          //  logger.info("CHEQUE PAYMENT");
            String chequeNo = null;
            Pattern pattern = Pattern.compile(".*[^0-9].*");
            String input = m_jChequeNum.getText();
            if (m_jChequeNum.getText().equals("") || pattern.matcher(input).matches()) {
                showMessage(this, "Please enter the valid Cheque Number");
            } else {
                chequeNo = m_jChequeNum.getText();
                m_jChequeNum.reset();
                JRetailPanelTicket.chequePayment(1, tinfoLocal, chequeNo);
                completed = true;
                dispose();
            }

        } //this is for voucher payment
        
        else if (payMode == 2) {
          //   logger.info("VOUCHER PAYMENT");
            String voucherno = null;
            Pattern pattern = Pattern.compile(".*[^0-9].*");
            String input = m_jVoucherNum.getText();
            if (m_jVoucherNum.getText().equals("") || pattern.matcher(input).matches()) {
                showMessage(this, "Please enter the valid Voucher Number");
                m_jVoucherNum.reset();
            } else {
                voucherno = m_jVoucherNum.getText();
                JRetailPanelTicket.voucherPayment(1, tinfoLocal, voucherno);
                m_jVoucherNum.reset();
                completed = true;
                dispose();
            }

        } //Staff payment
        else if (payMode == 4) {
           //  logger.info("STAFF PAYMENT");
            String staffName = null;
            if (jComboStaff.getSelectedIndex() == -1 || jComboStaff.getSelectedItem().equals(null) || jComboStaff.getSelectedItem().equals("")) {
                showMessage(this, "Please enter the Staff name");
            } else {
                //    staffName = jComboStaff.getSelectedItem().toString();
                int index = jComboStaff.getSelectedIndex();
                staffName = staffList.get(index).getId();
                System.out.println("STAFF ID:" + staffName);
                JRetailPanelTicket.staffPayment(1, tinfoLocal, staffName);
                completed = true;
                dispose();
            }
        } //Mobile Payment
        else if (payMode == 5) {
            // logger.info("MOBILE PAYMENT");
            String mobileType = null;
            String mobileNo = null;
            Pattern pattern = Pattern.compile(".*[^0-9].*");
            String input = m_jMobileNo.getText();

            if (jComboMobileType.getSelectedIndex() == -1 || jComboMobileType.getSelectedItem().equals(null) || jComboMobileType.getSelectedItem().equals("")) {
                showMessage(this, "Please enter the Mobile Type");
            }

            if (m_jMobileNo.getText().equals("")) {
                mobileNo = null;
                int index = jComboMobileType.getSelectedIndex();
                mobileType = mobileTypeList.get(index).getMobileType();
                JRetailPanelTicket.mobilePayment(1, tinfoLocal, mobileType, mobileNo);
                completed = true;
                dispose();
            } else {

                if (pattern.matcher(input).matches()) {
                    showMessage(this, "Please enter the valid Mobile Number");
                    m_jMobileNo.reset();
                } else if (input.length() > 10 || input.length() < 10) {
                    showMessage(this, "Please enter the valid Mobile Number");
                    m_jMobileNo.reset();
                } else {
                    mobileNo = m_jMobileNo.getText();
                    int index = jComboMobileType.getSelectedIndex();
                    mobileType = mobileTypeList.get(index).getMobileType();
                    JRetailPanelTicket.mobilePayment(1, tinfoLocal, mobileType, mobileNo);
                    completed = true;
                    dispose();
                }
            }
        } //this is for split payment
        else {
             //logger.info("SPLIT PAYMENT");
            double cashAmount = 0;
            double cardAmount = 0;
            double voucherAmount = 0;
            double mobileAmount = 0;
            String mobileType = null;
            String mobileNo = null;
            if (m_jCash.getText().equals("")) {
                cashAmount = 0;
            } else {
                try {
                    cashAmount = Double.parseDouble(m_jCash.getText());
                } catch (NumberFormatException ex) {
                    showMessage(this, "Please enter the valid cash amount");
                }
            }

            if (m_jCard.getText().equals("")) {
                cardAmount = 0;
            } else {
                try {
                    cardAmount = Double.parseDouble(m_jCard.getText());
                } catch (NumberFormatException ex) {
                    showMessage(this, "Please enter the valid card amount");
                }
            }

            if (m_jVoucher.getText().equals("")) {
                voucherAmount = 0;
            } else {
                try {
                    voucherAmount = Double.parseDouble(m_jVoucher.getText());
                } catch (NumberFormatException ex) {
                    showMessage(this, "Please enter the valid Voucher amount");
                }
            }

            if (m_jMobile.getText().equals("")) {
                mobileAmount = 0;
            } else {
                try {
                    mobileAmount = Double.parseDouble(m_jMobile.getText());
                    mobileType = jComboSplitPayMobile.getSelectedItem().toString();
                } catch (NumberFormatException ex) {
                    showMessage(this, "Please enter the valid Mobile amount");
                }
            }
            if (m_jSplitMobileNo.getText().equals("")) {
                mobileNo = null;
            } else {
                String input1 = m_jSplitMobileNo.getText();
                Pattern pattern = Pattern.compile(".*[^0-9].*");
                if (pattern.matcher(input1).matches()) {
                    showMessage(this, "Please enter the valid Mobile Number");
                    m_jSplitMobileNo.reset();
                    return;
                } else if (input1.length() > 10 || input1.length() < 10) {
                    showMessage(this, "Please enter the valid Mobile Number");
                    m_jSplitMobileNo.reset();
                    return;
                } else {
                    mobileNo = m_jSplitMobileNo.getText();
                }

            }
            if (m_jCash.getText().equals("") && m_jCard.getText().equals("") && m_jMobile.getText().equals("") && m_jVoucher.getText().equals("")) {
                showMessage(this, "Please enter the tender types");
            } else {
                totalBillValue = cashAmount + cardAmount + mobileAmount + voucherAmount;
                JRetailPanelTicket.settleBill(totalBillValue, cashAmount, cardAmount, mobileAmount, voucherAmount, mobileType, mobileNo);   // TODO add your handling code here:
                if (JRetailPanelTicket.getClosePayment() == true) {
                    completed = true;
                    dispose();
                }
            }

        }

    }//GEN-LAST:event_m_jPayActionPerformed

    private void WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosing
        completed = false;
        dispose();
    }//GEN-LAST:event_WindowClosing

    private void m_jBtnStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnStaffActionPerformed
        jPaymentPanel.setVisible(false);
        jVoucherPanel.setVisible(false);
        jMobilepanel.setVisible(false);

        jChequePanel.setVisible(false);
        jStaffPanel.setVisible(true);
        payMode = 4;
        if (jComboStaff.isEnabled()) {
            if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
                return;
            } else {
                try {
                    Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
                } catch (IOException ex) {
                    Logger.getLogger(JRetailProductLineEdit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_m_jBtnStaffActionPerformed

    private void m_jBtnComplimentaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnComplimentaryActionPerformed
        String billAmt = m_jTxtBillAmount.getText().toString();
        int res = JOptionPane.showConfirmDialog(this, "Payment of " + billAmt + " will be received as Complimentary.Click Ok to confirm.", AppLocal.getIntString("title.editor"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            m_jCash.setVisible(false);
            m_jCard.setVisible(false);
            m_jTxtCash.setVisible(true);
            m_jTxtCard.setVisible(true);
            m_jTxtCash.setEnabled(false);
            m_jTxtCard.setEnabled(true);
            m_jMobile.setVisible(false);
            m_jVoucher.setVisible(false);
            if (!tinfoLocal.getMomoePhoneNo().equals("")) {
                m_jTextMobileSplit.setVisible(true);
            }
            m_jTxtVoucherSplit.setVisible(true);
            m_jTextMobileSplit.setEnabled(false);
            m_jTxtVoucherSplit.setEnabled(false);
            m_jTxtCard.setText(tinfoLocal.printTotal());
            JRetailPanelTicket.complimentaryPayment(1, tinfoLocal);// TODO add your handling code here:
            completed = true;
            dispose();
        }
    }//GEN-LAST:event_m_jBtnComplimentaryActionPerformed

    private void m_jBtnVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnVoucherActionPerformed
        jStaffPanel.setVisible(false);
        jPaymentPanel.setVisible(false);
        jChequePanel.setVisible(false);
        jMobilepanel.setVisible(false);
        jVoucherPanel.setVisible(true);
        m_jTxtVoucher.setText(m_jTxtBillAmount.getText().toString());
        m_jTxtVoucherNum.setVisible(false);
        m_jVoucherNum.setVisible(true);        // TODO add your handling code here:
        m_jCash.reset();
        m_jCard.reset();
        m_jMobile.reset();
        m_jVoucher.reset();
        m_jChequeNum.reset();
        payMode = 2;
    }//GEN-LAST:event_m_jBtnVoucherActionPerformed

    private void m_jBtnChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnChequeActionPerformed
        jStaffPanel.setVisible(false);
        jPaymentPanel.setVisible(false);
        jVoucherPanel.setVisible(false);
        jMobilepanel.setVisible(false);

        jChequePanel.setVisible(true);
        m_jTxtCheque.setText(m_jTxtBillAmount.getText().toString());
        m_jTxtChequeNum.setVisible(false);
        m_jChequeNum.setVisible(true);        // TODO add your handling code here:
        m_jCash.reset();
        m_jCard.reset();
        m_jMobile.reset();
        m_jVoucher.reset();
        m_jVoucherNum.reset();
        payMode = 1;
    }//GEN-LAST:event_m_jBtnChequeActionPerformed

    private void m_jCashFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jCashFocusGained

        m_jCard.setVisible(false);
        m_jTxtCard.setVisible(true);
        m_jTxtCard.setEnabled(true);
        m_jMobile.setVisible(false);
        m_jVoucher.setVisible(false);
        if (!tinfoLocal.getMomoePhoneNo().equals("")) {
            m_jTextMobileSplit.setVisible(true);
            m_jTextMobileSplit.setEnabled(true);
        }
        m_jTxtVoucherSplit.setVisible(true);
        m_jTxtVoucherSplit.setEnabled(true);
        m_jTxtCard.setText(Formats.CURRENCY.formatValue(tinfoLocal.getTotal() - Double.parseDouble(m_jCash.getText())));

    }//GEN-LAST:event_m_jCashFocusGained

    private void m_jCashKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jCashKeyTyped

        m_jCard.setVisible(false);
        m_jTxtCard.setVisible(true);
        m_jTxtCard.setEnabled(true);
        m_jMobile.setVisible(false);
        m_jVoucher.setVisible(false);
        m_jTextMobileSplit.setVisible(true);
        m_jTextMobileSplit.setEnabled(true);
        m_jTxtVoucherSplit.setVisible(true);
        m_jTxtVoucherSplit.setEnabled(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCashKeyTyped

    private void m_jkeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jkeysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jkeysActionPerformed

    private void m_jBtnMobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnMobileActionPerformed

        jVoucherPanel.setVisible(false);
        jChequePanel.setVisible(false);
        jStaffPanel.setVisible(false);
        jPaymentPanel.setVisible(false);
        jMobilepanel.setVisible(true);

        payMode = 5;

//        String billAmt = m_jTxtBillAmount.getText().toString();
//        System.out.println("split value testing issue IN PAYMENT EDITOR " + tinfoLocal.getSplitValue());
//      int res = JOptionPane.showConfirmDialog(this, "Payment of " + billAmt + " will be received as Mobile.Click Ok to confirm.", AppLocal.getIntString("title.editor"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
//      if (res == JOptionPane.OK_OPTION) {
        m_jCash.setVisible(false);
        m_jCard.setVisible(false);
        m_jTxtCash.setVisible(true);
        m_jTxtCard.setVisible(true);
        m_jTxtCash.setText(tinfoLocal.printTotal());
        m_jTxtCard.setEnabled(false);
        m_jTxtCash.setEnabled(true);
        m_jMobile.setVisible(false);
        m_jVoucher.setVisible(false);

        m_jTextMobileSplit.setVisible(true);
        m_jTxtVoucherSplit.setVisible(true);
        m_jTextMobileSplit.setEnabled(false);
        m_jTxtVoucherSplit.setEnabled(false);
//           JRetailPanelTicket.mobilePayment(1, tinfoLocal);// TODO add your handling code here:
//            completed = true;
//            dispose();
//        }
    }//GEN-LAST:event_m_jBtnMobileActionPerformed

    private void m_jTextMobileSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTextMobileSplitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jTextMobileSplitActionPerformed

    private void jComboStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboStaffActionPerformed

    private void jComboStaffMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboStaffMousePressed
    }//GEN-LAST:event_jComboStaffMousePressed

//    public void paymentDetail(){
//    m_aPaymentInfo = new PaymentInfoList();
// 
//      if(!m_jCash.getText().equals("")){
//          totalAmount = cashAmount+cardAmount;
//          double change = totalAmount - tinfoLocal.getTotal();
//          cash= new PaymentInfoCash(tinfoLocal.getTotal(),(Double.parseDouble(m_jCash.getText())),change);
//            if (cash != null) {
//              m_aPaymentInfo.add(cash);
//
//           }
//      }
//      if(!m_jCard.getText().equals("")){
//          card = new PaymentInfoCard(tinfoLocal.getTotal(),(Double.parseDouble(m_jCard.getText())));
//            if (card != null) {
//              m_aPaymentInfo.add(card);
//
//           }
//       }
//
//    }
    private void showMessage(JPaymentEditor aThis, String msg) {
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

    private void calculateDiscount() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jCLabel;
    private javax.swing.JLabel jCNumLabel;
    private javax.swing.JPanel jChequePanel;
    private javax.swing.JComboBox jComboMobileType;
    private javax.swing.JComboBox jComboSplitPayMobile;
    private javax.swing.JComboBox jComboStaff;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCard;
    private javax.swing.JLabel jLabelCash;
    private javax.swing.JLabel jLabelMobile;
    private javax.swing.JLabel jLabelVoucher;
    private javax.swing.JPanel jMobilepanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPaymentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jStaffPanel;
    private javax.swing.JLabel jVLabel;
    private javax.swing.JLabel jVNumLabel;
    private javax.swing.JPanel jViewPanel;
    private javax.swing.JPanel jVoucherPanel;
    private javax.swing.JButton m_jBtnCard;
    private javax.swing.JButton m_jBtnCash;
    private javax.swing.JButton m_jBtnCheque;
    private javax.swing.JButton m_jBtnComplimentary;
    private javax.swing.JButton m_jBtnMobile;
    private javax.swing.JButton m_jBtnStaff;
    private javax.swing.JButton m_jBtnVoucher;
    private com.openbravo.editor.JEditorCurrencyPositive m_jCard;
    private com.openbravo.editor.JEditorCurrencyPositive m_jCash;
    private com.openbravo.editor.JEditorCurrencyPositive m_jChangeEuros;
    private com.openbravo.editor.JEditorCurrencyPositive m_jChequeNum;
    private javax.swing.JButton m_jClose;
    private com.openbravo.editor.JEditorCurrencyPositive m_jMobile;
    private com.openbravo.editor.JEditorCurrencyPositive m_jMobileNo;
    private javax.swing.JButton m_jPay;
    private javax.swing.JButton m_jSplit;
    private com.openbravo.editor.JEditorCurrencyPositive m_jSplitMobileNo;
    private javax.swing.JTextField m_jTextMobileSplit;
    private javax.swing.JTextField m_jTxtBillAmount;
    private javax.swing.JTextField m_jTxtCard;
    private javax.swing.JTextField m_jTxtCash;
    private javax.swing.JTextField m_jTxtCheque;
    private javax.swing.JTextField m_jTxtChequeNum;
    private javax.swing.JTextField m_jTxtVoucher;
    private javax.swing.JTextField m_jTxtVoucherNum;
    private javax.swing.JTextField m_jTxtVoucherSplit;
    private com.openbravo.editor.JEditorCurrencyPositive m_jVoucher;
    private com.openbravo.editor.JEditorCurrencyPositive m_jVoucherNum;
    private com.openbravo.editor.JEditorNumberKeys m_jkeys;
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
}