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
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurantMap;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TiltNameInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class JTiltCollection extends JDialog {

    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicSales dlSales = null;
    private static DataLogicCustomers dlCustomers = null;
    protected static AppView m_app;
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
    public static JRetailPanelTicket jRetailPanelTicket;
    private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 220;
    public static boolean isNewTable;
    public static String splitTableId;
    Logger logger = Logger.getLogger("MyLog");
    public static boolean loginOutVar;
    public static String userId;
    public double cashVal, cardVal, compleVal, mobileVal, staffVal, voucherVal, chequeVal = 0.0;
    public static DataLogicReceipts dlReceipts;
    public Map<String, Double> tiltMap = new HashMap<String, Double>();
    public String previntime = " ";
    public java.util.List<ShiftTallyLineInfo> ShiftTallyList = null;
    // public List<TiltBalanceInfo> tiltBalList = new ArrayList<TiltBalanceInfo>();
    //  public List<TiltSessionInfo> tiltSsnList = new ArrayList<TiltSessionInfo>();
    //   public List<TiltTxnInfo> tiltTxnList  = new ArrayList<TiltTxnInfo>();
    public List<TiltBalanceInfo> tiltBalList = null;
    public List<TiltSessionInfo> tiltSsnList = null;
    public List<TiltTxnInfo> tiltTxnList = null;
    public List<TiltNameInfo> tiltList = null;
    public double sum, cashsum, cardsum, complesum, mobsum, staffsum, vouchersum, chequesum = 0.0;
    public String tiltPosNum, PosNum;
    public static String tiltName;
    private Properties m_propsconfig;
    public AppConfig config;
    private ComboBoxValModel jTiltComboModel;
    public String paymentModes;
    private static DataLogicSystem m_dlSystem = null;
    private TicketParser m_TTP;
    private TicketParser m_TTP2;
    private DeviceTicket m_TP;

    public JTiltCollection() {
    }

    // public boolean init(DataLogicReceipts dlReceipt) {
    public boolean init(DataLogicReceipts dlReceipt, DataLogicCustomers dlCustomers, DataLogicSystem dlSystem, AppView m_App) throws BeanFactoryException {
//       m_jTiltComboModel.setSelectedIndex(-1);
        initComponents();


        AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
        aconfig.load();
        PosNum = aconfig.getProperty("machine.PosNo");
        paymentModes = aconfig.getProperty("machine.allpaymentmodes");
    //    AppView m_app = null;

        m_dlSystem = dlSystem;
        this.m_app = m_App;
        System.out.println("m_app"+m_app + m_App);
        m_TTP = new TicketParser(m_app.getDeviceTicket(), m_dlSystem);
        m_TP = new DeviceTicket();

      

        if (loginOutVar) {
            logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            intimeval.setVisible(true);
            intimelabel.setVisible(true);
            outtimeval.setVisible(false);
            outtimelabel.setVisible(false);
            jcheckoutLabel.setVisible(false);
            intimeval.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // jTiltComboModel.setEditable(true);
            // AutoCompletion.enable( jTiltComboModel);

            populateTilt(dlCustomers);




        } else {

            logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            outtimeval.setVisible(true);
            outtimelabel.setVisible(true);
            intimeval.setVisible(false);
            intimelabel.setVisible(false);
            outtimeval.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            m_jTiltComboModel.setVisible(false);
            jcheckoutLabel.setVisible(true);
            System.out.println("tiltName" + tiltName);
            jcheckoutLabel.setText(tiltName);

            populateTilt(dlCustomers);

        }

        //  m_jKeys.setVisible(false);

        userval.setText(userId);

        if (paymentModes.equals("true")) {
            m_jcard.addEditorKeys(m_jKeys);
            m_jcard.setEnabled(true);
            m_jcard.activate();
            m_jcash.addEditorKeys(m_jKeys);
            m_jcash.setEnabled(true);
            m_jcash.activate();
            m_jcheque.addEditorKeys(m_jKeys);
            m_jcheque.setEnabled(true);
            m_jcheque.activate();
            m_jcomplementary.addEditorKeys(m_jKeys);
            m_jcomplementary.setEnabled(true);
            m_jcomplementary.activate();
            m_jmobile.addEditorKeys(m_jKeys);
            m_jmobile.setEnabled(true);
            m_jmobile.activate();
            m_jstaff.addEditorKeys(m_jKeys);
            m_jstaff.setEnabled(true);
            m_jstaff.activate();
            m_jvoucher.addEditorKeys(m_jKeys);
            m_jvoucher.setEnabled(true);
            m_jvoucher.activate();
        } else {
            m_jcash.addEditorKeys(m_jKeys);
            m_jcash.setEnabled(true);
            m_jcash.activate();
        }
        //  m_jcash.setDoubleValue(0.0);
        dlReceipts = dlReceipt;

//        
//        m_jTiltComboModel.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m_jTiltComboModelActionPerformed(evt);
//            }
//        });


//        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
//        AppConfig ap = new AppConfig(file);
//        ap.load();


        //  populateTiltBalance(dlReceipts);
//        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
//        m_sentcat = dlSales.getTillList();
//        m_TillModel = new ComboBoxValModel();          




        setTitle("Collection");
        setVisible(true);


//        MyItemListener actionListener = new MyItemListener();




        return true;


    }

    private void populateTilt(DataLogicCustomers dlCustomer) {
        System.out.println("POpulate Floor List");
        dlCustomers = dlCustomer;
        try {
            tiltList = dlCustomers.getTiltList();
            System.out.println(tiltList.size());
        } catch (BasicException ex) {
            Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jTiltComboModel.insertItemAt("", 0);
        for (int i = 0; i < tiltList.size(); i++) {
            m_jTiltComboModel.addItem(tiltList.get(i).getTilt());
            // tiltName = tiltList.get(i).getTilt();
            //storeinitialtiltbalance(dlReceipts, tiltName);

        }

    }

    private void populateTiltScreen(DataLogicReceipts dlReceipt, String tiltNme) throws NullPointerException, BasicException {
        JTiltCollection myMsg;
        dlReceipts = dlReceipt;
        tiltName = tiltNme;
        int logindex = -1;
        Object max;
        Long maxLong;
        int logoutindex = 0;
        String ssnId = " ";
        Long l = new Long(0);
        Long ll = new Long(0);
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        ArrayList<Long> logoutList = new ArrayList<Long>();
        try {
            tiltBalList = dlReceipts.getTiltBalanceInfo();
            tiltSsnList = dlReceipts.getTiltSessionInfo();
            tiltTxnList = dlReceipts.getTiltTxnInfo();

            try {
                System.out.println(tiltSsnList.size());
                for (int i = 0; i < tiltSsnList.size(); i++) {
                    System.out.println("Populate Tilt Screen session tilt check  " + tiltSsnList.get(i).getTilt());
                    tiltPosNum = tiltSsnList.get(i).getTilt();

                    String tilt = m_jTiltComboModel.getSelectedItem().toString();
                    System.out.println("tilt" + tilt + "tiltName" + tiltName + tiltPosNum);
                    if (tiltPosNum != null && !tiltPosNum.isEmpty()) {
                        try {
                            if (tiltPosNum.equals(tilt)) {
                                //  System.out.println(" session tilt max time check  " + tiltName + tiltSsnList.get(i).getLogout().getTime());
                                ll = tiltSsnList.get(i).getLogout().getTime();
                                if (ll.equals(null) || ll.equals(" ")) {
                                    System.out.println("Inside logout collection" + ll);
                                    continue;
                                }
                                logoutList.add(ll);
                            }//end if
                        } catch (NullPointerException ex) {
                            Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }//end if
                }//end for
            } catch (NullPointerException ex) {
                Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
            }


            if (!logoutList.isEmpty()) {
                max = Collections.max(logoutList);
                int maxLongIndex = logoutList.indexOf(max);
                //Remove max or current tilt selection log time
                //logoutList.remove(maxLongIndex);
                // max = Collections.max(logoutList);
                maxLongIndex = logoutList.indexOf(max);
                maxLong = ((Long) max).longValue();


                //Fetch Index of Max prev .Logout time of tilt from Tiltsession-Info


                for (TiltSessionInfo t : tiltSsnList) {
                    assert (l != null);
                    try {
                        l = t.getLogout().getTime();
                        if ((l.intValue() == 0) || (l == null) || l.equals(null) || l.equals(" ")) {
                            continue;
                            // System.out.println("llllll+" + l + "__" + maxLong);
                        }

                    } catch (NullPointerException ex) {
                        Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    if (df.format(l).toString().equals(df.format(maxLong))) {
                        logoutindex = tiltSsnList.indexOf(t);
                        ssnId = t.getTsnId();

                    }
                }

            }//end for 



        } catch (NullPointerException ex) {
            Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(tiltBalList.size());
        if (tiltBalList.size() == 0 || ssnId.equals(" ")) {
            cashsum = cardsum = 0.0;
            complesum = mobsum = staffsum = vouchersum = chequesum = 0.0;
            m_jcomplementary.setDoubleValue(complesum);
            m_jcard.setDoubleValue(cardsum);
            m_jcash.setDoubleValue(cashsum);
            m_jstaff.setDoubleValue(staffsum);
            m_jvoucher.setDoubleValue(vouchersum);
            m_jmobile.setDoubleValue(mobsum);
            m_jcheque.setDoubleValue(chequesum);
        }
        for (int i = 0; i < tiltBalList.size(); i++) {

            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Cash")) {
                double amt = tiltBalList.get(i).getClosingamount();
                cashsum = amt;
            }

            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Card")) {
                double amt = tiltBalList.get(i).getClosingamount();
                cardsum = amt;
            }
            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Cheque")) {
                double amt = tiltBalList.get(i).getClosingamount();
                chequesum = amt;
            }
            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Staff")) {
                double amt = tiltBalList.get(i).getClosingamount();
                staffsum = amt;
            }
            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Complementary")) {
                double amt = tiltBalList.get(i).getClosingamount();
                complesum = amt;
            }
            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Mobile")) {
                double amt = tiltBalList.get(i).getClosingamount();
                mobsum = amt;
            }
            if ((tiltBalList.get(i).getSessionId()).equals(ssnId) && tiltBalList.get(i).getPaymenttype().equals("Voucher")) {
                double amt = tiltBalList.get(i).getClosingamount();
                vouchersum = amt;
            }


            m_jcomplementary.setDoubleValue(complesum);
            m_jcard.setDoubleValue(cardsum);
            m_jcash.setDoubleValue(cashsum);
            m_jstaff.setDoubleValue(staffsum);
            m_jvoucher.setDoubleValue(vouchersum);
            m_jmobile.setDoubleValue(mobsum);
            m_jcheque.setDoubleValue(chequesum);

        }



    }

    private void populateTiltBalance(DataLogicReceipts dlReceipt) {
        JTiltCollection myMsg;
        dlReceipts = dlReceipt;
        try {
            tiltTxnList = dlReceipts.getTiltTxnInfo();
            //   tiltSsnList=dlReceipts.getTiltSessionInfo();
            //  tiltBalList=dlReceipts.getTiltBalanceInfo();


        } catch (BasicException ex) {
            Logger.getLogger(JPaymentEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(tiltTxnList.size());
        for (int i = 0; i < tiltTxnList.size(); i++) {
            tiltPosNum = tiltTxnList.get(i).getTilt();

            if (tiltPosNum.equals(PosNum)) {
                String paytype = tiltTxnList.get(i).getPaymenttype().toString();
                double amt = tiltTxnList.get(i).getAmount().doubleValue();
                if (paytype.equals("Cash")) {
                    cashsum = cashsum + amt;
                }
                if (paytype.equals("Card")) {
                    cardsum = cardsum + amt;
                }
                if (paytype.equals("Complementary")) {
                    complesum = complesum + Double.parseDouble(tiltTxnList.get(i).getAmount().toString());
                }

                if (paytype.equals("Staff")) {
                    staffsum = staffsum + Double.parseDouble(tiltTxnList.get(i).getAmount().toString());
                }
                if (paytype.equals("Cheque")) {
                    chequesum = chequesum + Double.parseDouble(tiltTxnList.get(i).getAmount().toString());
                }
                if (paytype.equals("Voucher")) {
                    vouchersum = vouchersum + Double.parseDouble(tiltTxnList.get(i).getAmount().toString());
                }
                if (paytype.equals("Mobile")) {
                    mobsum = mobsum + Double.parseDouble(tiltTxnList.get(i).getAmount().toString());
                }

            }//end for


            m_jcomplementary.setDoubleValue(complesum);
            m_jcard.setDoubleValue(cardsum);
            m_jcash.setDoubleValue(cashsum);
            m_jstaff.setDoubleValue(staffsum);
            m_jvoucher.setDoubleValue(vouchersum);
            m_jmobile.setDoubleValue(mobsum);
            m_jcheque.setDoubleValue(chequesum);


        }
    }

    public static void showMessage(Component parent, DataLogicReceipts dlReceipt, boolean loginout, String cashloginid, DataLogicCustomers dlCustomer, DataLogicSystem dlSystem, AppView m_App) {
        m_app = m_App;
        m_dlSystem = dlSystem;

        parentLocal = parent;
        loginOutVar = loginout;
        userId = cashloginid;
        dlReceipts = dlReceipt;
        // jRetailPanelTicket = jrpTicket;
        dlCustomers = dlCustomer;
        if (loginout) {
            //showMessage(parent, dlReceipts, 1, dlCustomers);
            showMessage(parent, dlReceipts, 1, dlCustomers, m_dlSystem, m_app);
        } else {
            // showMessage(parent, dlReceipts, 1, dlCustomers);
            showMessage(parent, dlReceipts, 1, dlCustomers, m_dlSystem, m_app);
        }
    }

    private static void showMessage(Component parent, DataLogicReceipts dlReceipt, int x) {
        dlReceipts = dlReceipt;
        Window window = getWindow(parent);
        JTiltCollection myMsg;
        if (window instanceof Frame) {
            myMsg = new JTiltCollection((Frame) window, true);
        } else {
            myMsg = new JTiltCollection((Dialog) window, true);
        }

    }

    public static void showMessage(Component parent, DataLogicReceipts dlReceipt, boolean loginout, String cashloginid) {

        parentLocal = parent;
        loginOutVar = loginout;
        userId = cashloginid;
        dlReceipts = dlReceipt;

        if (loginout) {
            showMessage(parent, dlReceipts, 1);
        } else {
            showMessage(parent, dlReceipts, 1);
        }
    }

    private static void showMessage(Component parent, DataLogicReceipts dlReceipt, int x, DataLogicCustomers dlCustomer, DataLogicSystem dlSystem, AppView m_App) {
        m_app = m_App;
        dlReceipts = dlReceipt;
        m_dlSystem = dlSystem;
        System.out.println("13" + userId + loginOutVar);//
        // JRetailPanelTicket = jrpTicket;
        dlCustomers = dlCustomer;
        Window window = getWindow(parent);
        JTiltCollection myMsg;
        if (window instanceof Frame) {
            myMsg = new JTiltCollection((Frame) window, true);
        } else {
            myMsg = new JTiltCollection((Dialog) window, true);
        }
        //boolean completed = myMsg.init(dlReceipts);

        boolean completed = myMsg.init(dlReceipts, dlCustomers, m_dlSystem, m_app);


        //  return completed;
    }

    private JTiltCollection(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JTiltCollection(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        m_jKeys = new com.openbravo.editor.JEditorNumberKeys();
        m_jcard = new com.openbravo.editor.JEditorDouble();
        m_jcash = new com.openbravo.editor.JEditorDouble();
        m_jcheque = new com.openbravo.editor.JEditorDouble();
        m_jcomplementary = new com.openbravo.editor.JEditorDouble();
        m_jmobile = new com.openbravo.editor.JEditorDouble();
        m_jstaff = new com.openbravo.editor.JEditorDouble();
        m_jvoucher = new com.openbravo.editor.JEditorDouble();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        intimelabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        outtimelabel = new javax.swing.JLabel();
        userval = new javax.swing.JLabel();
        intimeval = new javax.swing.JLabel();
        outtimeval = new javax.swing.JLabel();
        ok = new javax.swing.JButton();
        m_jTiltComboModel = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jRemarks = new javax.swing.JTextArea();
        jcheckoutLabel = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Voucher");

        m_jKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jKeysActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Card");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Cash");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Cheque");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Complementary");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Mobile");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Staff");

        intimelabel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        intimelabel.setText("In Time : ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("User :");

        outtimelabel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        outtimelabel.setText("Out Time :");

        userval.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        intimeval.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        outtimeval.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        m_jTiltComboModel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                m_jTiltComboModelItemStateChanged(evt);
            }
        });
        m_jTiltComboModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTiltComboModelActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setText("Tilt");

        jLabel8.setText("Remarks");

        jRemarks.setColumns(20);
        jRemarks.setRows(5);
        jScrollPane2.setViewportView(jRemarks);

        jcheckoutLabel.setText("Tilt");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(26, 26, 26))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(intimelabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(12, 12, 12)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(userval, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(102, 102, 102)
                                .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jcheckoutLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(1, 1, 1)
                                .add(m_jTiltComboModel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(intimeval, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(layout.createSequentialGroup()
                                .add(outtimelabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(outtimeval, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel1)
                                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(24, 24, 24)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                    .add(m_jcheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(m_jcomplementary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                    .add(m_jvoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(m_jstaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(m_jcard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(m_jcash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(m_jmobile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(ok, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 248, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(m_jKeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 181, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(14, 14, 14)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 181, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(32, 32, 32)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, userval, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jTiltComboModel)
                        .add(jcheckoutLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(intimelabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(intimeval, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outtimelabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(outtimeval, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jcash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(21, 21, 21)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jcard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(21, 21, 21)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jcheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(21, 21, 21)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(m_jcomplementary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(m_jmobile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jstaff, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(jLabel7)
                                .add(3, 3, 3))))
                    .add(m_jKeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jvoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(ok))
                    .add(layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 3, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(568, 549));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void showMessage(JTiltCollection aThis, String msg, DataLogicReceipts dlReceipt) {
        dlReceipts = dlReceipt;
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

    private void showMessage(JTiltCollection aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                JOptionPane.INFORMATION_MESSAGE);

    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        showMessage(this, "Please enter the Tilt Balance and click 'OK' ");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jKeysActionPerformed
    private void PopulatetiltTransaction(DataLogicReceipts dlReceipt, String tiltNme) {
        dlReceipts = dlReceipt;
        tiltName = tiltNme;
        System.out.println("PopulatetiltTransaction+CashTallyId" + dlReceipts.cashTallyId + "tiltName" + tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Card", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cash", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Voucher", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Complementary", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Staff", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Mobile", 0.0, tiltName);
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cheque", 0.0, tiltName);
    }
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        // TODO add your handling code here:
        System.out.println(loginOutVar);
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.Check"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            if (loginOutVar) {
                System.out.println("on checkin" + loginOutVar);
                int findex = m_jTiltComboModel.getSelectedIndex();
                findex--;
                tiltName = m_jTiltComboModel.getSelectedItem().toString();
                dlReceipts.updateSetTilt(tiltName);
                storecashtallysession(dlReceipts, tiltName);
                PopulatetiltTransaction(dlReceipts, tiltName);
                dispose();
            } else {
                dlReceipts.updateUnSetTilt(tiltName);
                storecashtallysession(dlReceipts, tiltName);
               
                dispose();

            }

    }//GEN-LAST:event_okActionPerformed
    }
    private void m_jTiltComboModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTiltComboModelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jTiltComboModelActionPerformed

    private void m_jTiltComboModelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_m_jTiltComboModelItemStateChanged

        // TODO add your handling code here:
        String OutStrng;
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            OutStrng = "Selected: " + (String) evt.getItem();
        } else {
            OutStrng = "DeSelected: " + (String) evt.getItem();
        }
        tiltName = OutStrng;
        String tiltName11 = m_jTiltComboModel.getSelectedItem().toString();
        try {
            populateTiltScreen(dlReceipts, tiltName);
        } catch (NullPointerException ex) {
            Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BasicException ex) {
            Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jTiltComboModelItemStateChanged

    private void storecashtallysession(DataLogicReceipts dlReceipt, String tiltNme) {
        System.out.println(dlReceipt);
        dlReceipts = dlReceipt;
        System.out.println("storecashtallysession");
        String intime = "";
        String outime = "";
        tiltName = tiltNme;

        if ((intimeval.getText()).equals("")) {
            intime = " ";
        } else {
            intime = intimeval.getText();
            // previntime=intime;
        }
        if ((outtimeval.getText()).equals("")) {
            outime = " ";
        } else {
            outime = outtimeval.getText();
        }

        if (getMissing(m_jcash.getText()) == false) {
            m_jcash.setDoubleValue(0.0);
        } else {
            cashVal = Double.parseDouble(m_jcash.getText());

        }


        if (getMissing(m_jcard.getText()) == false) {
            m_jcard.setDoubleValue(0.0);

        } else {
            cardVal = Double.parseDouble(m_jcard.getText());

        }


        if (getMissing(m_jcheque.getText()) == false) {
            m_jcheque.setDoubleValue(0.0);

        } else {
            chequeVal = Double.parseDouble(m_jcheque.getText());

        }

        if (getMissing(m_jcomplementary.getText()) == false) {
            m_jcomplementary.setDoubleValue(0.0);

        } else {
            compleVal = Double.parseDouble(m_jcomplementary.getText());

        }


        if (getMissing(m_jstaff.getText()) == false) {
            m_jstaff.setDoubleValue(0.0);

        } else {
            staffVal = Double.parseDouble(m_jstaff.getText());

        }

        if (getMissing(m_jmobile.getText()) == false) {
            m_jmobile.setDoubleValue(0.0);

        } else {
            mobileVal = Double.parseDouble(m_jmobile.getText());

        }

        if (getMissing(m_jvoucher.getText()) == false) {
            m_jvoucher.setDoubleValue(0.0);

        } else {
            voucherVal = Double.parseDouble(m_jvoucher.getText());

        }
        tiltMap.put("Cash", cashVal);
        tiltMap.put("Card", cardVal);
        tiltMap.put("Cheque", chequeVal);
        tiltMap.put("Complementary", compleVal);
        tiltMap.put("Mobile", mobileVal);
        tiltMap.put("Staff", staffVal);
        tiltMap.put("Voucher", voucherVal);
        if (loginOutVar) {
            String remks = jRemarks.getText();
            dlReceipts.insertTiltSession(userId, intime, outime, tiltMap, tiltName, remks);
        } else {
             try {
                System.out.println("prinber.tilt is file name -OKK -in configuration -users and roles>resources section -fn call");
                // printTicket("Printer.Tilt", tiltBalList, m_dlSystem,tiltName);
                printTicket("Printer.Tilt", m_dlSystem);
            } catch (ScriptException ex) {
                Logger.getLogger(JNonServedLinesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            String remks = jRemarks.getText();
                
    
            dlReceipts.insertTiltSession(userId, intime, outime, tiltMap, tiltName, remks);
        }
    }



private void printTicket(String sresourcename, DataLogicSystem dlSystem) throws ScriptException {
        m_dlSystem = dlSystem;
        System.out.println("printTiltCollection -fn call");
        String sresource = m_dlSystem.getResourceAsXML(sresourcename);
        ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JTiltCollection.this);
        } else {

            try {
                System.out.println("Valuesssssssssss" + userId + tiltName + cashVal + cardVal + compleVal + mobileVal + staffVal + voucherVal + chequeVal);
                //System.out.println(cashsum + cardsum + complesum + mobsum + staffsum + vouchersum + chequesum);
                script.put("UserName", userId);
                script.put("Tilt", tiltName);
                script.put("Cash", cashVal);
                script.put("Card", cardVal);
                script.put("Cheque", chequeVal);
                script.put("Complementary", compleVal);
                script.put("Mobile", mobileVal);
                script.put("Staff", staffVal);
                script.put("Voucher", voucherVal);
                m_TTP.printTicket(script.eval(sresource).toString());
                System.out.println("m_TTP.printTicket(script.eval(sresource).toString())t");

            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JTiltCollection.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                Logger.getLogger(JTiltCollection.class.getName()).log(Level.SEVERE, null, e);
                msg.show(JTiltCollection.this);
            }
        }
    }
    private void storeinitialtiltbalance(DataLogicReceipts dlReceipt, String tiltNme) {
        //Initial entry of zero records in titlbalance

        dlReceipts = dlReceipt;

        tiltName = tiltNme;

        tiltMap.put("Cash", 0.0);
        tiltMap.put("Card", 0.0);
        tiltMap.put("Cheque", 0.0);
        tiltMap.put("Complementary", 0.0);
        tiltMap.put("Mobile", 0.0);
        tiltMap.put("Staff", 0.0);
        tiltMap.put("Voucher", 0.0);
        dlReceipts.insertSetTiltBalance(userId, tiltMap, tiltName);

    }

    public boolean getMissing(String Field) {
        try {
            if (Field.equals("")) {
                return false; // has number
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException ex1) {
            ex1.printStackTrace();
        }

        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel intimelabel;
    private javax.swing.JLabel intimeval;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JTextArea jRemarks;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel jcheckoutLabel;
    private com.openbravo.editor.JEditorNumberKeys m_jKeys;
    private javax.swing.JComboBox m_jTiltComboModel;
    private com.openbravo.editor.JEditorDouble m_jcard;
    private com.openbravo.editor.JEditorDouble m_jcash;
    private com.openbravo.editor.JEditorDouble m_jcheque;
    private com.openbravo.editor.JEditorDouble m_jcomplementary;
    private com.openbravo.editor.JEditorDouble m_jmobile;
    private com.openbravo.editor.JEditorDouble m_jstaff;
    private com.openbravo.editor.JEditorDouble m_jvoucher;
    private javax.swing.JButton ok;
    private javax.swing.JLabel outtimelabel;
    private javax.swing.JLabel outtimeval;
    private javax.swing.JLabel userval;
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