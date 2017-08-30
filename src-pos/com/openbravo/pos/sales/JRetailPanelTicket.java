//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//  Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//ivate
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
//updates
package com.openbravo.pos.sales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.catalog.JRetailCatalogTab;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.BillPromoRuleInfo;
import com.openbravo.pos.forms.BuyGetInfo;
import com.openbravo.pos.forms.BuyGetPriceInfo;
import com.openbravo.pos.forms.BuyGetQtyInfo;
import com.openbravo.pos.forms.CampaignIdInfo;
import com.openbravo.pos.forms.CustomerListInfo;
import com.openbravo.pos.forms.JPrincipalApp;
import com.openbravo.pos.forms.JRootApp;
import com.openbravo.pos.forms.PromoRuleIdInfo;
import com.openbravo.pos.forms.PromoRuleInfo;
import com.openbravo.pos.inventory.RoleUserInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentInterface;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoChequeDetails;
import com.openbravo.pos.payment.PaymentInfoComp;
import com.openbravo.pos.payment.PaymentInfoFoodCoupon;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoMobile;
import com.openbravo.pos.payment.PaymentInfoStaff;
import com.openbravo.pos.payment.PaymentInfoVoucherDetails;
import com.openbravo.pos.printer.printer.KotImagePrinter;

import com.openbravo.pos.printer.printer.TicketLineConstructor;
import static com.openbravo.pos.sales.JRepeatLinesPanel.tinfoLocal;
import static com.openbravo.pos.sales.JRetailKdsDetails.dlReceipts;
import static com.openbravo.pos.sales.JRetailKdsDetails.userMap;
import static com.openbravo.pos.sales.JRetailTicketsBag.m_App;
import com.openbravo.pos.sales.restaurant.JRetailBufferWindow;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurant;
import com.openbravo.pos.sales.restaurant.Place;

import com.openbravo.pos.ticket.ProductInfoExt;

import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TaxMapInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.ThumbNailBuilderPopularItems;
import com.sysfore.pos.homedelivery.DeliveryBoyInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import com.sysfore.pos.panels.PosActionsInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import javax.print.PrintService;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.WordUtils;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.NameTaxMapInfo;
import com.openbravo.util.date.DateFormats;
import java.util.NavigableSet;
import java.util.TreeMap;

/**
 *
 * @author adrianromero
 */
public abstract class JRetailPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, RetailTicketsEditor {

    // Variable numerica
    private final static int NUMBERZERO = 0;
    private final static int NUMBERVALID = 1;
    private final static int NUMBER_INPUTZERO = 0;
    private final static int NUMBER_INPUTZERODEC = 1;
    private final static int NUMBER_INPUTINT = 2;
    private final static int NUMBER_INPUTDEC = 3;
    private final static int NUMBER_PORZERO = 4;
    private final static int NUMBER_PORZERODEC = 5;
    private final static int NUMBER_PORINT = 6;
    private final static int NUMBER_PORDEC = 7;
    private final static int NUMBER_TEST = 100;
    protected JRetailTicketLines m_ticketlines;
    private TicketParser m_TTP;
    public JPrincipalApp m_principalapp = null;
    protected RetailTicketInfo m_oTicket;
    protected int kotaction = 0;
    protected int kotprintIssue = 0;
    protected Object m_oTicketExt;
    String[] args;
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
    public static DefaultListModel taxModel = null;
    private JRetailTicketsBag m_ticketsbag;
    private SentenceList senttax;
    private ListKeyed taxcollection;
    private SentenceList sentcharge;
    private ListKeyed chargecollection;
    private SentenceList sentsertax;
    private ListKeyed sertaxcollection;
    java.util.List<ServiceChargeInfo> chargelist = null;
    java.util.List<TaxInfo> sertaxlist = null;
    private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    private static RetailTaxesLogic taxeslogic;
    private static RetailServiceChargesLogic chargeslogic;
    private static RetailSTaxesLogic staxeslogic;
    private String editSaleBillId;
    protected JRetailPanelButtons m_jbtnconfig;
    protected AppView m_App;
    protected DataLogicSystem dlSystem;
    protected DataLogicSales dlSales;
    protected DataLogicCustomers dlCustomers;
    private java.util.List<DeliveryBoyInfo> deliveryBoyLines;
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;
    public java.util.ArrayList<PromoRuleInfo> promoRuleList = null;
    public static java.util.ArrayList<BusinessServiceTaxInfo> serviceTaxList = null;
    public static java.util.ArrayList<BusinessServiceChargeInfo> serviceChargeList = null;
    protected PromoRuleInfo promoDetails;
    public java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList;
    public java.util.ArrayList<PromoRuleIdInfo> pdtRuleIdList;
    public java.util.ArrayList<CampaignIdInfo> campaignIdList;
    public java.util.ArrayList<CampaignIdInfo> pdtCampaignIdList;
    public java.util.ArrayList<BuyGetInfo> pdtBuyGetList;
    public java.util.ArrayList<BuyGetQtyInfo> pdtBuyGetQtyList;
    public java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList;
    public java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
    public java.util.ArrayList<BillPromoRuleInfo> billPromoRuleList;
    public java.util.ArrayList<CustomerListInfo> customerList;
    public java.util.ArrayList<CustomerListInfo> customerListDetails;
    public java.util.ArrayList<ProductInfoExt> productList;
    public java.util.ArrayList<ProductInfoExt> productListValue;
    public int served = 0;
    public java.util.ArrayList<ProductInfoExt> productListDetails;
    double qty = 0;
    int buttonPlus = 1;
    private Border empty;
    public double productDiscount = 0;
    String selectedProduct;
    private ArrayList<PromoRuleIdInfo> promoRuleLeastList;
    public BuyGetPriceInfo buyGet;
    public String itemChange;
    boolean action1Performed = false;
    boolean action2Performed = false;
    boolean action3Performed = false;
    boolean action4Performed = false;
    boolean action5Performed = false;
    boolean action6Performed = false;
    JPaymentInterface forpayment;
    PaymentInfoCash cash = null;
    PaymentInfoCard card = null;
    PaymentInfoStaff staff = null;
    PaymentInfoComp comp = null;
    // PaymentInfoCheque cheque=null;
    PaymentInfoChequeDetails chequetransaction = null;
    PaymentInfoVoucherDetails voucher = null;
    PaymentInfoFoodCoupon foodCoupon = null;
    public PaymentInfoList m_aPaymentInfo;
    private boolean accepted;
    private JRootApp m_RootApp;
    private static JTextField cusName;
    private static JTextField cusPhoneNo;
    //private static JTextField itemName;
    private final Vector<String> vCusName = new Vector<String>();
    private final Vector<String> vCusPhoneNo = new Vector<String>();
    private final Vector<String> vItemName = new Vector<String>();
    private boolean hide_flag = false;
    double totalAmount = 0;
    double totalBillValue;
    int typeId;
    double cashAmount = 0;
    double cardAmount = 0;
    double chequeAmount = 0;
    double voucherAmount = 0;
    double creditAmount = 0;
    double foodCouponAmount = 0;
    public DataLogicReceipts dlReceipts;
    private boolean printerStatus;
    private String editSale;
    private String homeDeliverySale;
    private String pdtId;
    //protected kotInfo k_oInfo;
    private java.util.List<KotTicketListInfo> kotlist;
    private java.util.List<RetailTicketLineInfo> kotTicketlist;
    private java.util.List<kotPrintedInfo> kotPrintedlist;
    private Place m_PlaceCurrent;
    static double serviceChargeAmt;
    javax.swing.Timer timer;
    private ThumbNailBuilderPopularItems tnbbutton;
    protected EventListenerList listeners = new EventListenerList();
    public boolean cancelStatus = false;
    public final static int INTERVAL = 1000;
    private boolean closePayment = false;
    private java.util.List<ProductionPrinterInfo> printerInfo;
    String roleName = null;
    private int IsSteward = 0;
    private String addonId = null;
    private int primaryAddon = 0;
    java.util.List<TaxInfo> taxlist = null;
    String momoeStatus = null;
    String reservationStatus = null;
    PaymentInfoMobile mobile = null;
    private java.util.ArrayList<PosActionsInfo> posActions;
    Logger logger = Logger.getLogger("MyLog");
    Logger kotlogger = Logger.getLogger("KotLog");
    Logger printlogger = Logger.getLogger("PrintLog");
    Logger settlelogger = Logger.getLogger("SettleLog");
    FileHandler fh1;
    FileHandler fh2;
    FileHandler fh3;
    private Map<String, DiscountInfo> discountMap = null;
    private String day = null;
    private String menuId = null;
    private java.util.List<MenuInfo> currentMenuList = null;
    private String menuStatus = "";
    private static DateFormat m_dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat m_dateformattime = new SimpleDateFormat("HH:mm:ss");
    Map<String, String> userMap = new HashMap<String, String>();
    Date dbUpdatedDate = null;
    public int oldordernum;
    public int newordernum;
    public String loginUserId;
    private java.util.List<ProductInfoExt> list = null;
    Map<String, NameTaxMapInfo> srttaxMap = new HashMap<String, NameTaxMapInfo>();
    public Map<String, String> srtMap = new TreeMap<String, String>();
    public String tiltUserName;
    public String tiltUserRole;
    public String tiltRole;
    public JTiltCollection jtc;

    /**
     * Creates new form JTicketView
     */
    public JRetailPanelTicket() {
        initComponents();
        tnbbutton = new ThumbNailBuilderPopularItems(110, 57, "com/openbravo/images/bluetoit.png");
        TextListener txtL;
        // cusName = (JTextField) m_jCboCustName.getEditor().getEditorComponent();
        // cusPhoneNo= (JTextField) m_jCboContactNo.getEditor().getEditorComponent();
//       itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();
        //  m_jTxtItemCode.setFocusable(true);
        // m_jTxtCustomerId.setFocusable(true);
        txtL = new TextListener();
        // m_jCreditAllowed.setVisible(false);
        // m_jCreditAllowed.setSelected(false);
//       cusPhoneNo.addFocusListener(txtL);
        //cusName.addFocusListener(txtL);

        //itemName.addFocusListener(txtL);
        Action doMorething = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //    m_jPrice.setEnabled(true);
                //     m_jPrice.setFocusable(true);
                m_jKeyFactory.setFocusable(true);
                m_jKeyFactory.setText(null);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        m_jKeyFactory.requestFocus();
                    }
                });

            }
        };

//        InputMap imap = m_jPrice.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        //     imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK), "doMorething");
        //    m_jPrice.getActionMap().put("doMorething",doMorething);

        //   Action doLastBill = new AbstractAction() {
        //   public void actionPerformed(ActionEvent e) {
        //        m_jLastBillActionPerformed(e);
        //        stateToZero();
        //       }
        //   };

        //   InputMap imapLastBill = m_jLastBill.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        //  imapLastBill.put(KeyStroke.getKeyStroke("F7"), "doLastBill");
        //  m_jLastBill.getActionMap().put("doLastBill",doLastBill);

//        Action cashNoBill = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                //  cashPayment(3);
//                //  setPrinterOn();
//            }
//        };
//        InputMap imapCashNoBill = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//
//        imapCashNoBill.put(KeyStroke.getKeyStroke("F1"), "cashNoBill");
//        m_jAction.getActionMap().put("cashNoBill",
//                cashNoBill);
//        // };
//        Action cashReceipt = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                //   cashPayment(2);
//            }
//        };
//        InputMap imapCashReceipt = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCashReceipt.put(KeyStroke.getKeyStroke("F2"), "cashReceipt");
//        m_jAction.getActionMap().put("cashReceipt",
//                cashReceipt);


//        Action docashPrint = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                //  cashPayment(1);
//            }
//        };

//        InputMap imapCashPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCashPrint.put(KeyStroke.getKeyStroke("F3"), "docashPrint");
//        m_jAction.getActionMap().put("docashPrint",
//                docashPrint);
//        Action doCardnobill = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                //m_jCloseShiftActionPerformed(e);
//                //     cardPayment(1);
//            }
//        };

//        InputMap imapCardnoBill = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCardnoBill.put(KeyStroke.getKeyStroke("F4"), "doCardnobill");
//        m_jAction.getActionMap().put("doCardnobill", doCardnobill);
//
//        Action doNonChargablePrint = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    closeTicketNonChargable(m_oTicket, m_oTicketExt, m_aPaymentInfo);
//                } catch (BasicException ex) {
//                    logger.info("exception in closeTicketNonChargable" + ex.getMessage());
//                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        };

//        InputMap imapNonChargablePrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapNonChargablePrint.put(KeyStroke.getKeyStroke("F7"), "doNonChargablePrint");
//        m_jAction.getActionMap().put("doNonChargablePrint",
//                doNonChargablePrint);
//
//        Action doCreditreceipt = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
////            m_jCreditAmount.setText(Double.toString(m_oTicket.getTotal()));
////
////            try {
////                    closeCreditTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
////                } catch (BasicException ex) {
////                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//        };
//        InputMap imapCreditReceipt = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCreditReceipt.put(KeyStroke.getKeyStroke("F5"), "doCreditreceipt");
//        m_jAction.getActionMap().put("doCreditreceipt", doCreditreceipt);
//
//        Action doHomeDeliveryNotPaid = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                // m_jHomeDelivery.setSelected(true);
//                if (getEditSale() == "Edit") {
//                    //  getServiceCharge("Y");
//                    printPartialTotals();
//                    try {
//                        closeTicketHomeDelivery(m_oTicket, m_oTicketExt, m_aPaymentInfo);
//                    } catch (BasicException ex) {
//                        logger.info("exception in closeTicketHomeDelivery" + ex.getMessage());
//                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        };
//
//        InputMap imapCardPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCardPrint.put(KeyStroke.getKeyStroke("F6"), "doHomeDeliveryNotPaid");
//        m_jAction.getActionMap().put("doHomeDeliveryNotPaid", doHomeDeliveryNotPaid);
//
//
//
//        Action doLogout = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                m_jLogoutActionPerformed(e);
//
//            }
//        };
//
//        InputMap imapLog = m_jLogout.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapLog.put(KeyStroke.getKeyStroke("F12"), "doLogout");
//        m_jLogout.getActionMap().put("doLogout",
//                doLogout);
//
//
//        Action doupArrow = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
////                m_jUpActionPerformed(e);
//            }
//        };
//        InputMap imapUpArrow = m_jPlus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapUpArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "doupArrow");
//        m_jPlus.getActionMap().put("doupArrow",
//                doupArrow);

//       Action doCustomer = new AbstractAction() {
//       public void actionPerformed(ActionEvent e) {
//
////              m_jTxtCustomerId.setFocusable(true);
//
//        //    cusPhoneNo.setFocusable(true);
//       //     cusName.setFocusable(true);
//            stateToBarcode();
//            }
//        };

//        InputMap imapCustomer= m_jTxtCustomerId.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCustomer.put(KeyStroke.getKeyStroke("F8"), "doCustomer");
//        m_jTxtCustomerId.getActionMap().put("doCustomer",
//                doCustomer);



//        Action doItem = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                //   itemName.setFocusable(true);
////           m_jTxtItemCode.setFocusable(true);
//                stateToItem();
//
//            }
//        };

//        InputMap imapItem = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapItem.put(KeyStroke.getKeyStroke("F9"), "doItem");
//        m_jAction.getActionMap().put("doItem",
//                doItem);

//        Action doHomeDelivery = new AbstractAction() {
//       public void actionPerformed(ActionEvent e) {
////            m_jHomeDelivery.setFocusable(true);
//        //    m_jHomeDelivery.setSelected(true);
//      //      m_jHomeDeliveryActionPerformed(e);
//      //      m_jTxtAdvance.setFocusable(true);
//            stateToHomeDelivery();
//            }
//        };
//
//        InputMap imapHomeDelivery= m_jHomeDelivery.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapHomeDelivery.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_MASK), "doHomeDelivery");
//        m_jHomeDelivery.getActionMap().put("doHomeDelivery",
//                doHomeDelivery);
//

//        Action doPayment = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
////           m_jCash.setFocusable(true);
//                //       m_jCard.setFocusable(true);
//                //       m_jCheque.setFocusable(true);
//                //       m_jtxtChequeNo.setFocusable(true);
//                //       m_jFoodCoupon.setFocusable(true);
//                //      m_jVoucher.setFocusable(true);
//                //      m_jCreditAmount.setFocusable(true);
//                stateToPayment();
//            }
//        };
//
//        InputMap imapPayment = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapPayment.put(KeyStroke.getKeyStroke("F11"), "doPayment");
//        m_jAction.getActionMap().put("doPayment",
//                doPayment);
//
//        Action doDownpArrow = new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
////               m_jDownActionPerformed(e);
//            }
//        };
//
//        InputMap imapArrow = m_jMinus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "doDownpArrow");
//        m_jMinus.getActionMap().put("doDownpArrow",
//                doDownpArrow);



//      itemName.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//
//         
//       if(!itemName.getText().equals("") || !itemName.getText().equals(null)){
//
//        incProductByItemDetails(pdtId);
//      //  itemName.setText("");
////          m_jCboItemName.setSelectedIndex(-1);
//
//        ArrayList<String> itemCode = new ArrayList<String>();
//        ArrayList<String> itemName1 = new ArrayList<String>();
//
//        vItemName.removeAllElements();
//        try {
//            productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        for (ProductInfoExt product : productListDetails) {
//            itemCode.add(product.getItemCode());
//            itemName1.add(product.getName());
//        }
//
//          String [] productName = itemName1.toArray(new String[itemName1.size()]);
//          
//          for(int i=0;i<itemName1.size();i++){
//                  vItemName.addElement(productName[i]);
//          }
////          itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();
//
//      }else{
//            pdtId="";
//            ArrayList<String> itemCode = new ArrayList<String>();
//            ArrayList<String> itemName1 = new ArrayList<String>();
//
//            vItemName.removeAllElements();
//            try {
//                productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            for (ProductInfoExt product : productListDetails) {
//                itemCode.add(product.getItemCode());
//                itemName1.add(product.getName());
//            }
//
//              String [] productName = itemName1.toArray(new String[itemName1.size()]);
//               for(int i=0;i<itemName1.size();i++){
//                      vItemName.addElement(productName[i]);
//              }
////          itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();
//
//      }
//      }
//
//    });

    }

    public void setPrinterOn() {
        Boolean status;
        if (getPrinterStatus() == true) {
            setPrinterStatus(false);
            jLblPrinterStatus.setText("Printer Off");
        } else {
            setPrinterStatus(true);
            jLblPrinterStatus.setText("Printer On");
        }
    }

    public boolean getPrinterStatus() {
        return printerStatus;
    }

    public void setPrinterStatus(Boolean printerStatus) {
        this.printerStatus = printerStatus;
    }

    //Method is used for checking the whether the items have mandatory addon while adding the items
    private void checkMandatoryAddon(ProductInfoExt oProduct, String addonId) {
        java.util.List<ProductInfoExt> mandatoryProduct = null;
        if (menuStatus.equals("false")) {
            try {
                mandatoryProduct = dlSales.getMandatoryAddonProducts(oProduct.getID());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                day = getWeekDay();
                currentMenuList = dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                mandatoryProduct = dlSales.getMenuMandatoryAddonProducts(oProduct.getID(), menuId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!mandatoryProduct.isEmpty()) {
            for (ProductInfoExt mandatory : mandatoryProduct) {
                TaxInfo tax = taxeslogic.getTaxInfo(mandatory.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                TaxInfo exemptTax = taxeslogic.getExemptTaxInfo(mandatory.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                addTicketLine(new RetailTicketLineInfo(mandatory, mandatory.getMultiply(), mandatory.getMrp(), promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, mandatory.getName(), mandatory.getProductType(), mandatory.getProductionAreaType(), (java.util.Properties) (mandatory.getProperties().clone()), addonId, 0, null, 0, null, null, null, null, null, mandatory.getParentCatId(), mandatory.getPreparationTime(), null, mandatory.getStation(), null, 1, exemptTax));
            }
        }
    }

    public void staffPayment(int print, RetailTicketInfo ticket, String staffName) {
        try {
            logger.info("Enter StaffPayment");
            dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Staff", ticket.getTotal(), jtc.tiltName);
            boolean updated = checkTicketUpdation();
            if (!updated) {
                dbUpdatedDate = null;
                logger.info("Enter staffPayment method");
                m_aPaymentInfo = new PaymentInfoList();
                staff = new PaymentInfoStaff(ticket.getTotal(), ticket.getTotal(), staffName);
                if (staff != null) {
                    m_aPaymentInfo.add(staff);

                }
                ticket.setPrintStatus(print);
                logger.info("staffPayment Before close ticket button action");
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("staffPayment after close ticket button action");
                //activate();
            }
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in staffPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cashPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter cashPayment");
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cash", ticket.getTotal(), jtc.tiltName);
//       int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();
        cash = new PaymentInfoCash(ticket.getTotal(), ticket.getTotal(), 0);
        if (cash != null) {
            m_aPaymentInfo.add(cash);

        }
        ticket.setPrintStatus(print);
        try {
            logger.info("Before close ticket button");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("After close ticket button");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cashPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   }
    }

    public void cardPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter cardPayment");
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Card", ticket.getTotal(), jtc.tiltName);

//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        card = new PaymentInfoCard(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (card != null) {
            m_aPaymentInfo.add(card);


        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //   }

    public void chequePayment(int print, RetailTicketInfo ticket, String chequeNo) {
        logger.info("Enter chequePayment Method");
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cheque", ticket.getTotal(), jtc.tiltName);

        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            m_aPaymentInfo = new PaymentInfoList();
            chequetransaction = new PaymentInfoChequeDetails(ticket.getTotal(), ticket.getTotal(), chequeNo);
            if (chequetransaction != null) {
                m_aPaymentInfo.add(chequetransaction);

            }
            ticket.setPrintStatus(print);
            try {
                logger.info("chequePayment Before close ticket button action");
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("chequePayment after close ticket button action");
                //  activate();
            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in chequePayment closeTicketWithButton" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void staffPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter staffPayment method");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        staff = new PaymentInfoStaff(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (staff != null) {
            m_aPaymentInfo.add(staff);

        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("staffPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("staffPayment after close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in staffPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //}

    //newly added method for mobile tender type
    public void mobilePayment(int print, RetailTicketInfo ticket, String mobileType, String mobileNo) {
        logger.info("Enter Mobile method");
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Mobile", ticket.getTotal(), jtc.tiltName);
        int inventoyCount = 0;
        try {
            inventoyCount = dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (inventoyCount == 1) {
            showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
        } else {
            m_aPaymentInfo = new PaymentInfoList();

            mobile = new PaymentInfoMobile(m_oTicket.getTotal(), m_oTicket.getTotal(), mobileType, mobileNo);
            if (mobile != null) {
                m_aPaymentInfo.add(mobile);

            }
            m_oTicket.setPrintStatus(print);
            try {
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                // activate();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void complimentaryPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter complimentaryPayment method");

        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Complementary", ticket.getTotal(), jtc.tiltName);

//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        comp = new PaymentInfoComp(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (comp != null) {
            m_aPaymentInfo.add(comp);

        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("complimentaryPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("complimentaryPayment after close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in complimentaryPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }  //}
    }

    public void voucherPayment(int print, RetailTicketInfo ticket, String voucherNo) {
        logger.info("Enter Voucher method");
        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Voucher", ticket.getTotal(), jtc.tiltName);
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            int inventoyCount = 0;
            try {
                inventoyCount = dlSales.getStopInventoryCount();
            } catch (BasicException ex) {
                logger.info("exception in voucherPayment" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inventoyCount == 1) {
                showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
            } else {
                m_aPaymentInfo = new PaymentInfoList();
                voucher = new PaymentInfoVoucherDetails(ticket.getTotal(), ticket.getTotal(), voucherNo);
                if (voucher != null) {
                    m_aPaymentInfo.add(voucher);

                }
                ticket.setPrintStatus(print);
                try {
                    logger.info("voucherPayment Before close ticket button action");
                    closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                    logger.info("voucherPayment after close ticket button action");
                    //activate();
                } catch (BasicException ex) {
                    logger.info("Order No. " + m_oTicket.getOrderId() + "exception in voucherPayment closeTicketWithButton" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void customerFocus() {
//       m_jTxtCustomerId.setFocusable(true);
//       cusName.setFocusable(true);
        //   cusPhoneNo.setFocusable(true);
        //    m_jTxtItemCode.setFocusable(true);
//       itemName.setFocusable(true);
//       m_jCash.setFocusable(true);
        //     m_jCard.setFocusable(true);
        //     m_jCheque.setFocusable(true);
        //     m_jtxtChequeNo.setFocusable(true);
//       m_jPrice.setFocusable(true);
        //   m_jPrice.setEnabled(true);
        //    m_jCreditAmount.setFocusable(true);
    }

    public void setEditSale(String editSale) {
        this.editSale = editSale;
    }

    public String getEditSale() {
        return editSale;
    }
//    public void setHomeDeliverySale(String homeDeliverySale) {
//        this.homeDeliverySale = homeDeliverySale;
//    }
//    public String getHomeDeliverySale(){
//        return homeDeliverySale;
//    }

    class TextListener implements FocusListener {

        public void focusLost(FocusEvent e) {
            if (e.getSource() == cusName) {
                cusName.setFocusable(true);


            } else if (e.getSource() == cusPhoneNo) {
            }
//            else if(e.getSource() == itemName){
//
//            }

        } // close focusLost()

        public void focusGained(FocusEvent e) {
            final String type;
            if (e.getSource() == cusName) {
                type = "c";
                //setModel(new DefaultComboBoxModel(vCusName), "");
                cusName.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = cusName.getText();
                                if (text.length() == 0) {
                                    //         m_jCboCustName.hidePopup();
                                    setModel(new DefaultComboBoxModel(vCusName), "", type);
                                } else {
                                    typeId = 1;
                                    Vector<String> vCusName = new Vector<String>();
                                    ArrayList<String> cusNames = new ArrayList<String>();
                                    ArrayList<String> cusPhoneNo = new ArrayList<String>();
                                    ArrayList<String> cusId = new ArrayList<String>();
                                    try {
                                        customerListDetails = (ArrayList<CustomerListInfo>) dlSales.getCustomerListName(text);
                                    } catch (BasicException ex) {
                                        logger.info("exception in focusGained" + ex.getMessage());
                                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    for (CustomerListInfo cus : customerListDetails) {
                                        cusNames.add(cus.getName());
                                        cusPhoneNo.add((cus.getPhoneNo()));
                                        cusId.add((cus.getCustomerId()));
                                    }
                                    String[] customerNames = cusNames.toArray(new String[cusNames.size()]);
                                    for (int i = 0; i < cusNames.size(); i++) {
                                        vCusName.addElement(customerNames[i]);
                                    }
                                    DefaultComboBoxModel m = getCustNameSuggestedModel(vCusName, text);
                                    if (m.getSize() == 0 || hide_flag) {
//                        m_jCboCustName.hidePopup();
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
                                        //               m_jCboCustName.showPopup();
                                    }
                                }
                            }
                        });
                    }

                    public void keyPressed(KeyEvent e) {
                        String text = cusName.getText();
                        int code = e.getKeyCode();

                        if (code == KeyEvent.VK_ENTER) {
                            if (!vCusName.contains(text)) {
                                vCusName.addElement(text);
                                Collections.sort(vCusName);
                                setModel(getCustNameSuggestedModel(vCusName, text), text, type);

                            }
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < vCusName.size(); i++) {
                                String str = vCusName.elementAt(i);
                                if (str.startsWith(text)) {
//                        m_jCboCustName.setSelectedIndex(-1);
                                    cusName.setText(str);
                                    return;
                                }
                            }
                        }
                    }
                });
            } else if (e.getSource() == cusPhoneNo) {
                type = "n";
                cusPhoneNo.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = cusPhoneNo.getText();
                                if (text.length() == 0) {
//                    m_jCboContactNo.hidePopup();
                                    setModel(new DefaultComboBoxModel(vCusPhoneNo), "", type);
                                } else {
                                    DefaultComboBoxModel m = getContactNoSuggestedModel(vCusPhoneNo, text);
                                    if (m.getSize() == 0 || hide_flag) {
                                        //            m_jCboContactNo.hidePopup();
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
                                        //        m_jCboContactNo.showPopup();
                                    }
                                }
                            }
                        });
                    }

                    public void keyPressed(KeyEvent e) {
                        String text = cusPhoneNo.getText();
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_ENTER) {
                            if (!vCusPhoneNo.contains(text)) {
                                vCusPhoneNo.addElement(text);
                                Collections.sort(vCusPhoneNo);
                                setModel(getContactNoSuggestedModel(vCusPhoneNo, text), text, type);
                            }
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < vCusPhoneNo.size(); i++) {
                                String str = vCusPhoneNo.elementAt(i);
                                if (str.startsWith(text)) {
                                    //     m_jCboContactNo.setSelectedIndex(-1);
                                    cusPhoneNo.setText(str);
                                    return;
                                }
                            }
                        }
                    }
                });

            }

//      else if(e.getSource() == itemName){
//          type = "m";
//          itemName.addKeyListener(new KeyAdapter() {
//          public void keyTyped(KeyEvent e) {
//          EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    String text = itemName.getText();
//                    if(text.length()==0) {
////                        m_jCboItemName.hidePopup();
//                        setModel(new DefaultComboBoxModel(vItemName), "",type);
//                    }else{
//                        typeId = 2;
//                        ArrayList<String> itemCode = new ArrayList<String>();
//                        ArrayList<String> itemName = new ArrayList<String>();
//
//                        Vector<String> vItemName = new Vector<String>();
//                        try {
//                            productList = (ArrayList<ProductInfoExt>) dlSales.getProductName(text);
//                        } catch (BasicException ex) {
//                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                          for (ProductInfoExt product : productList) {
//                            itemCode.add(product.getItemCode());
//                            itemName.add(product.getName());
//                        }
//
//                          String [] productName = itemName.toArray(new String[itemName.size()]);
//                          for(int i=0;i<itemName.size();i++){
//                                  vItemName.addElement(productName[i]);
//                          }
//                        DefaultComboBoxModel m = getItemSuggestedModel(vItemName, text);
//                        if(m.getSize()==0 || hide_flag) {
//                       //     m_jCboItemName.hidePopup();
//                            hide_flag = false;
//                        }else{
//                            setModel(m, text,type);
//                         //   m_jCboItemName.showPopup();
//                        }
//                    }
//                }
//            });
//            }
//       public void keyPressed(KeyEvent e) {
//       String text = itemName.getText();
//         int code = e.getKeyCode();
//             if(code==KeyEvent.VK_ENTER) {
//            if(!vItemName.contains(text)) {
//                vItemName.addElement(text);
//                Collections.sort(vItemName);
//                setModel(getItemSuggestedModel(vItemName, text), text,type);
//            }
//            hide_flag = true;
//        }else if(code==KeyEvent.VK_ESCAPE) {
//            hide_flag = true;
//        }else if(code==KeyEvent.VK_RIGHT) {
//            for(int i=0;i<vItemName.size();i++) {
//                String str = vItemName.elementAt(i);
//                if(str.startsWith(text)) {
////                    m_jCboItemName.setSelectedIndex(-1);
//                    itemName.setText(str);
//                    return;
//                }
//            }
//        }
//      }
//      });
//
//    }
        }
    } // close TextListener, inner class

    public void init(AppView app) throws BeanFactoryException {
        System.out.println("inside init");
        m_App = app;
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");

        // borramos el boton de bascula si no hay bascula conectada
        if (!m_App.getDeviceScale().existsScale()) {
//            m_jbtnScale.setVisible(false);
        }
        if (m_App.getProperties().getProperty("machine.ticketsbag").equals("restaurant")) {
            m_jbtnPrintBill.setVisible(true);
        } else {
            m_jbtnPrintBill.setVisible(false);
            // jPanel1.setPreferredSize(new java.awt.Dimension(282, 47));
        }
        customerFocus();
        menuStatus = m_App.getProperties().getProperty("machine.menustatus");
        //setPrinterStatus(false);
        m_jPor.setVisible(false);
        m_ticketsbag = getJTicketsBag();
        //m_oTicket.setSplitValue("");
        m_oTicket.setCancelTicket(false);
        System.out.println("init----m_oTicket.getCancelTicket()--" + m_oTicket.getCancelTicket());
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);

        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JRetailTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);

        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        // Los botones configurables...
        m_jbtnconfig = new JRetailPanelButtons("Ticket.Buttons", this);
        m_jButtonsExt.add(m_jbtnconfig);

        // El panel de los productos o de las lineas...        
        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);
        catcontainer.setVisible(true);
        m_jCalculatePromotion.setVisible(false);
        // El modelo de impuestos
        senttax = dlSales.getRetailTaxList();
        sentcharge = dlSales.getRetailServiceChargeList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        sentsertax = dlSales.getRetailServiceTaxList();
        taxcategoriesmodel = new ComboBoxValModel();

        // ponemos a cero el estado
        stateToZero();

        //creating log file for kot
        String logpath = m_App.getProperties().getProperty("machine.kotlogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh1 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        kotlogger.addHandler(fh1);
        SimpleFormatter formatter = new SimpleFormatter();
        fh1.setFormatter(formatter);


        //creating log file for print bill

        logpath = m_App.getProperties().getProperty("machine.printlogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh2 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        printlogger.addHandler(fh2);
        fh2.setFormatter(formatter);

        //creating log file for settle bill
        logpath = m_App.getProperties().getProperty("machine.settlelogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh3 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        settlelogger.addHandler(fh3);
        fh3.setFormatter(formatter);

//       m_jCash.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//        m_jCard.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//        m_jCheque.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//    m_jFoodCoupon.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//    m_jVoucher.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//     m_jCreditAmount.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
    }

    public void custItemLoad() {
        loadCusDetails();
        loadItemDetails();
        // inicializamos
        m_oTicket = null;
        m_oTicketExt = null;

        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusId = new ArrayList<String>();
        //  ArrayList<String> cusName1 = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in custItemLoad" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusId.add((cus.getCustomerId()));
            //  cusName1.add(cus.getName());
        }
        vCusName.removeAllElements();
        String[] customerNames = cusNames.toArray(new String[cusNames.size()]);
        for (int i = 0; i < cusNames.size(); i++) {
            vCusName.addElement(customerNames[i]);
        }
        vCusPhoneNo.removeAllElements();
        String[] customerPhoneNo = cusPhoneNo.toArray(new String[cusPhoneNo.size()]);
        for (int i = 0; i < cusPhoneNo.size(); i++) {
            vCusPhoneNo.addElement(customerPhoneNo[i]);
        }


        ArrayList<String> itemCode = new ArrayList<String>();
        ArrayList<String> itemName = new ArrayList<String>();

        vItemName.removeAllElements();
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in productlist" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productList) {
            itemCode.add(product.getItemCode());
            itemName.add(product.getName());
        }
//          }

        String[] productName = itemName.toArray(new String[itemName.size()]);

        for (int i = 0; i < itemName.size(); i++) {
            vItemName.addElement(productName[i]);
        }
    }

    public void setTenderAmount() {
//     if (m_jCash.getText()!=null || m_jCard.getText()!=null || m_jCheque.getText()!=null || m_jFoodCoupon.getText()!=null || m_jVoucher.getText()!=null || m_jCreditAmount.getText()!=null){
//
//         Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
//         Pattern pNum1 = Pattern.compile(".");
//         if(m_jCash.getText().equals("")){
//             cashAmount = 0;
//        }else{
//             try{
//                cashAmount =Double.parseDouble(m_jCash.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid cash amount");
//
//             }
//         }
//
//         if(m_jCard.getText().equals("")){
//             cardAmount = 0;
//         }else{
//              try{
//                cardAmount = Double.parseDouble(m_jCard.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid card amount");
//             }
//
//         }
//         if(m_jCheque.getText().equals("")){
//             chequeAmount = 0;
//         }else{
//              try{
//                chequeAmount = Double.parseDouble(m_jCheque.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid cheque amount");
//             }
//
//         }
//         if(m_jFoodCoupon.getText().equals("")){
//             foodCouponAmount = 0;
//         }else{
//              try{
//                foodCouponAmount = Double.parseDouble(m_jFoodCoupon.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid food coupon amount");
//             }
//
//         }
//         if(m_jVoucher.getText().equals("")){
//             voucherAmount = 0;
//         }else{
//              try{
//                voucherAmount = Double.parseDouble(m_jVoucher.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid voucher amount");
//             }
//
//         }
//        if(m_jCreditAmount.getText().equals("")){
//             creditAmount = 0;
//         }else{
//              try{
//                creditAmount = Double.parseDouble(m_jCreditAmount.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid credit amount");
//             }
//
//         }
//
//         totalAmount = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount;
//         totalBillValue = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount+creditAmount;
//         m_jTxtTotalPaid.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(totalAmount))));
//         try{
//          if(m_oTicket.getTotal()< totalBillValue){
//              double change = totalBillValue - Math.ceil(m_oTicket.getTotal());
//              m_jTxtChange.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(change))));
//          }else{
//             m_jTxtChange.setText("");
//          }
//         }catch(NullPointerException ex){
//
//         }
//
//     }
    }

    private void setModel(DefaultComboBoxModel mdl, String str, String type) {
        if (type == "c") {
//                m_jCboCustName.setModel(mdl);
            cusName.setText(str);
        } else if (type == "n") {
            //   m_jCboContactNo.setModel(mdl);
            cusPhoneNo.setText(str);
        } else {
//                m_jCboItemName.setModel(mdl);
//                itemName.setText(str);
        }
    }

    private static DefaultComboBoxModel getSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            // if(s.startsWith(text))
            m.addElement(s);
        }
        return m;
    }

    private static DefaultComboBoxModel getCustNameSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {

            // if(s.startsWith(text))
            String nameLength = cusName.getText();
            if (nameLength.length() > 2) {
                m.addElement(s);
            }
        }
        return m;
    }

    private static DefaultComboBoxModel getContactNoSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            String phoneLength = null;

            phoneLength = cusPhoneNo.getText();
            //    if(s.startsWith(text))
            if (phoneLength.length() > 5) {
                if (s.startsWith(text)) {
                    m.addElement(s);
                }
            }
        }
        return m;

    }

    private static DefaultComboBoxModel getItemSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            String itemLength = null;

            //  itemLength = itemName.getText();
            // if(s.startsWith(text))
            if (itemLength.length() > 2) {
                //if(s.startsWith(text))
                m.addElement(s);
            }
        }
        return m;

    }

    public void paymentDetail(double cashAmount, double cardAmount, double mobileAmount, double voucherAmount) {
        try {
            boolean updated = checkTicketUpdation();
            if (!updated) {
                dbUpdatedDate = null;
                m_aPaymentInfo = new PaymentInfoList();
                totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount + mobileAmount;
                double change = totalAmount - m_oTicket.getTotal();
                cash = new PaymentInfoCash(m_oTicket.getTotal(), cashAmount, change);
                if (cash != null) {
                    m_aPaymentInfo.add(cash);

                }
                card = new PaymentInfoCard(m_oTicket.getTotal(), cardAmount);
                if (card != null) {
                    m_aPaymentInfo.add(card);
                }

                voucher = new PaymentInfoVoucherDetails(m_oTicket.getTotal(), voucherAmount);
                if (voucher != null) {
                    m_aPaymentInfo.add(voucher);
                }


                mobile = new PaymentInfoMobile(m_oTicket.getTotal(), mobileAmount);
                if (mobile != null) {
                    m_aPaymentInfo.add(mobile);
                }
                closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
            }
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void paymentDetail(double cashAmount, double cardAmount, double mobileAmount, double voucherAmount, String mobileType, String mobileNo) {
//        try {
//            boolean updated = checkTicketUpdation();
//            if (!updated) {
//                dbUpdatedDate = null;
//                m_aPaymentInfo = new PaymentInfoList();
//                totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount + mobileAmount;
//                double change = totalAmount - m_oTicket.getTotal();
//                cash = new PaymentInfoCash(m_oTicket.getTotal(), cashAmount, change);
//                if (cash != null) {
//                    m_aPaymentInfo.add(cash);
//
//                }
//                card = new PaymentInfoCard(m_oTicket.getTotal(), cardAmount);
//                if (card != null) {
//                    m_aPaymentInfo.add(card);
//                }
//
//                voucher = new PaymentInfoVoucherDetails(m_oTicket.getTotal(), voucherAmount);
//                if (voucher != null) {
//                    m_aPaymentInfo.add(voucher);
//                }
//
//
//                mobile = new PaymentInfoMobile(m_oTicket.getTotal(), mobileAmount, mobileType, mobileNo);
//                if (mobile != null) {
//                    m_aPaymentInfo.add(mobile);
//                }
//                closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
//            }
//        } catch (BasicException ex) {
//            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage() + ";");
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }

        try {
            logger.info("Enter Payment Detail for Toit Tally---" + dlReceipts.cashTallyId);

            boolean updated = checkTicketUpdation();
            if (!updated) {
                dbUpdatedDate = null;
                m_aPaymentInfo = new PaymentInfoList();
                totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount + mobileAmount;
                double change = totalAmount - m_oTicket.getTotal();
                System.out.println("PaymentInfoCash" + cashAmount + "--change--" + change + "diff" + (cashAmount - change));
                dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cash", cashAmount - change, jtc.tiltName);

                cash = new PaymentInfoCash(m_oTicket.getTotal(), cashAmount, change);
                if (cash != null) {
                    m_aPaymentInfo.add(cash);

                }
                System.out.println("PaymentInfoCard" + cardAmount);
                dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Card", cardAmount, jtc.tiltName);
                card = new PaymentInfoCard(m_oTicket.getTotal(), cardAmount);
                if (card != null) {
                    m_aPaymentInfo.add(card);
                }
                System.out.println("PaymentInfovoucherAmount" + voucherAmount);
                dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Voucher", voucherAmount, jtc.tiltName);
                voucher = new PaymentInfoVoucherDetails(m_oTicket.getTotal(), voucherAmount);
                if (voucher != null) {
                    m_aPaymentInfo.add(voucher);
                }

                System.out.println("PaymentInfoMobileAmount" + mobileAmount);
                dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Mobile", mobileAmount, jtc.tiltName);
                mobile = new PaymentInfoMobile(m_oTicket.getTotal(), mobileAmount, mobileType, mobileNo);
                if (mobile != null) {
                    m_aPaymentInfo.add(mobile);
                }
                closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
            }
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadCusDetails() {
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();


        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in loadCusDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }
//        m_jCboCustName.setModel(new ComboBoxValModel(cusNames));
        //  m_jCboContactNo.setModel(new ComboBoxValModel(cusPhoneNo));


    }

    private void loadCustomerDetails() {
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in loadCustomerDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }

    }

    private void loadItemDetails() {
        ArrayList<String> itemName = new ArrayList<String>();

        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in loadItemDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productList) {
            itemName.add(product.getName());
        }
//        m_jCboItemName.setModel(new ComboBoxValModel(itemName));
    }

    private void loadItemList() {
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in loadItemList" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public void activate() {
        logger.info("inside activate method");
        jLabelServiceTax.setVisible(false);
        m_jServiceTax.setVisible(false);
        m_jServiceCharge.setVisible(false);
        jLabelServiceCharge.setVisible(false);
        custItemLoad();
        enablePosActions();
        System.out.println(" before in map testing 4" + m_oTicket);
        showProductPanel();
        m_jbtnScale.setVisible(false);
        jLblPrinterStatus.setText("");
        // m_oTicket.setSplitValue("");
        momoeStatus = m_App.getProperties().getProperty("machine.momoestatus");
        reservationStatus = m_App.getProperties().getProperty("machine.reservationstatus");
        if (momoeStatus.equals("true")) {
            jLabelCustPhone.setVisible(true);
            jTextPhoneNo.setVisible(true);
        } else {
            jLabelCustPhone.setVisible(false);
            jTextPhoneNo.setVisible(false);
        }

        String servedStatus = m_App.getProperties().getProperty("machine.servedstatus");
        if (servedStatus.equals("true")) {
            m_jBtnServed.setVisible(true);
        } else {
            m_jBtnServed.setVisible(false);
        }
        //      m_jBtnCancelBill.setVisible(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        populateDeliveryBoy();
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(m_App);
        //    jLabel10.setVisible(false);
        //    m_jDeliveryBoy.setVisible(false);
//        jLabel11.setVisible(false);
        //   m_jTxtAdvance.setVisible(false);
//        m_jCod.setVisible(false);
        //    m_jCreditAllowed.setVisible(false);
        //      m_jCreditAmount.setText("0.00");
        //      m_jHomeDelivery.setSelected(false);
        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        // java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            logger.info("exception in activate taxcategorieslist" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
        m_jTax.setModel(taxcategoriesmodel);

        String taxesid = m_jbtnconfig.getProperty("taxcategoryid");
        if (taxesid == null) {
            if (m_jTax.getItemCount() > 0) {
                m_jTax.setSelectedIndex(0);
            }
        } else {
            taxcategoriesmodel.setSelectedKey(taxesid);
        }

        taxeslogic = new RetailTaxesLogic(taxlist, m_App);
        String businessTypeId = null;


        // getServiceCharge("Order Taking");

        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist, m_App);

        try {
            sertaxlist = sentsertax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sertaxcollection = new ListKeyed<TaxInfo>(sertaxlist);
        staxeslogic = new RetailSTaxesLogic(sertaxlist, m_App);

        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }

        // Authorization for buttons
//        btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        //      m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
//        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        //     m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));

        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());
        logger.info("before m_ticketsbag activate method");

        m_ticketsbag.activate();
        String day = getWeekDay();
        logger.info("after m_ticketsbag activate method");
//        java.util.ArrayList<String> campaignId = new ArrayList<String>();
//        try {
//            campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        promoDetails = new PromoRuleInfo();
//        if(campaignIdList.size()!=0){
//        for(int i=0;i<campaignIdList.size();i++){
//         campaignId.add("'"+campaignIdList.get(i).getcampaignId()+"'");
//     }
//
//         StringBuilder b = new StringBuilder();
//         Iterator<?> it = campaignId.iterator();
//         while (it.hasNext()) {
//         b.append(it.next());
//         if (it.hasNext()) {
//            b.append(',');
//          }
//        }
//        String Id = b.toString();
//        if(campaignIdList!=null){
//                try {
//                    promoRuleIdList = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);
//                } catch (BasicException ex) {
//                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//        }
//
//        }
        String role = m_App.getAppUserView().getUser().getRole();
        loginUserId = m_App.getAppUserView().getUser().getId();
        try {
            roleName = dlReceipts.getRoleByUser(role);
        } catch (BasicException ex) {
            logger.info("exception in roleName" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("roleName in activate" + roleName);
//            if(roleName.equals("Steward"))
//            {
//                m_jSettleBill.setVisible(false);
//                m_jSplitBtn.setVisible(false);
//                m_jbtnPrintBill.setVisible(false);
//                m_jBtnDiscount.setVisible(false);
//               
//            }else if(roleName.equals("Bartender")){
//               m_jSettleBill.setVisible(false);
//               m_jSplitBtn.setVisible(false);  
//                m_jbtnPrintBill.setVisible(true);
//               m_jBtnDiscount.setVisible(false);
//            }else{
//                 m_jSettleBill.setVisible(true);
//                m_jSplitBtn.setVisible(true);
//                m_jbtnPrintBill.setVisible(true);
//                m_jLogout.setVisible(true);
//                m_jBtnDiscount.setVisible(true);
//            }
        m_jBtnCatDiscount.setVisible(false);
        logger.info("After main activate method");
    }

    public void getServiceCharge(String isHomeDelivery) {
        String businessTypeId = null;
        int businessTypeCount = 0;
        try {
            businessTypeCount = dlSales.getBusinessTypeCount(isHomeDelivery);
        } catch (BasicException ex) {
            logger.info("exception in getServiceCharge" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (businessTypeCount == 1) {
            try {
                businessTypeId = dlSales.getBusinessTypeId(isHomeDelivery);
            } catch (BasicException ex) {
                logger.info("exception in businessTypeId" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serviceTaxList = (ArrayList<BusinessServiceTaxInfo>) dlSales.getBusinessServiceTax(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceTaxList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceChargeList = (ArrayList<BusinessServiceChargeInfo>) dlSales.getBusinessServiceCharge(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceChargeList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //method is called on logout action and closing the application
    public boolean deactivate() {
        int i = 0;
        if (m_oTicket != null) {
            try {
                m_oTicket.getAccessInfo().add(new AccessInfo(m_App.getProperties().getPosNo(), m_App.getAppUserView().getUser().getName(), new Date(), "exit"));
                logger.info("within deactivate ");
                String currentUpdated = m_dateformat.format(m_oTicket.getObjectUpdateDate()) + " " + m_dateformattime.format(m_oTicket.getObjectUpdateDate());
                String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                Date currentUpdatedDate = DateFormats.StringToDateTime(currentUpdated);
                Date dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                if (dbUpdated.equals(null) || dbUpdated.equals("")) {
                    showMessage(this, "This Bill is no longer exist");
                    logger.info("This Bill is no longer exist");
                } else if (dbUpdatedDate.compareTo(currentUpdatedDate) > 0) {
                    logger.info("The Table is being accessed by another User!Cannot update the bill");
                    // showMessage(this, "The Table is being accessed by another User!Cannot update the bill");
                    int iskds = dlReceipts.getKdsUpdateStatus(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                    if (iskds == 1) {
                        RetailTicketInfo ticket = dlReceipts.getRetailSharedTicketSplit(m_PlaceCurrent.getId(), m_oTicket.getSplitSharedId());
                        if (ticket != null) {
                            ticket.setTicketOpen(false);
                            dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), ticket);
//                            if (ticket.getTakeaway().equals("Y")) {
//                                dlReceipts.updateTakeawayTicket(m_PlaceCurrent.getId(), ticket);
//                            }

                        }
                    }
                } else {
                    //if no kot done    
                    if (m_oTicket.getOrderId() == 0) {
                        logger.info("Order No." + m_oTicket.getOrderId() + "deleting 0 order no. Bill in deactivate method of " + m_oTicket.getTableName() + " id is " + m_oTicket.getPlaceId());
                        //            if(reservationStatus.equals("true")){
                        //          ReservationStatus.Reservation(m_oTicket.getTableName(),"2",m_App);   
                        //          }
//                        if (m_oTicket.getTakeaway().equals("Y")) {
//                            dlReceipts.deleteTakeawayTicket(m_oTicket.getPlaceId());
//                        }
                        dlReceipts.deleteSharedTicket(m_oTicket.getPlaceId());
                        m_ticketsbag.deleteTicket();


                    }//if kot done but cancelled all lines
                    else if (m_oTicket.getOrderId() != 0 && m_oTicket.getLinesCount() == 0) {
                        m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        m_oTicket.setActiveCash(m_App.getActiveCashIndex());
                        m_oTicket.setActiveDay(m_App.getActiveDayIndex());
                        m_oTicket.setDate(new Date()); //
                        String ticketDocument;
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_oTicket.getTicketId();
                        String reason = "Splitted with zero lines/cancelled all kot lines";
                        try {
                            dlSales.saveRetailCancelTicket(m_oTicket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                        } catch (BasicException ex) {
                            logger.info("ORDER NO. " + m_oTicket.getOrderId() + "exception in saveRetailCancelTicket in deactivate method" + ex.getMessage());
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!m_oTicket.getMomoePhoneNo().equals("")) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in deactivate method 1");
                            MomoePayment.MomoeIntegration(null, m_oTicket, null, m_App, true, dlSales);
                        }

                        if (m_oTicket.getSplitValue().equals("")) {
                            logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot bill in deactivate method of " + m_oTicket.getTableName() + " id is" + m_oTicket.getPlaceId());
                            if (reservationStatus.equals("true")) {
                                ReservationStatus.Reservation(m_oTicket.getTableName(), "2", m_App);
                            }
//                             if(m_oTicket.getTakeaway().equals("Y")){
//                                dlReceipts.deleteTakeawayTicket(m_oTicket.getPlaceId());
//                            }
//                             dlReceipts.deleteSharedTicket(m_oTicket.getPlaceId());
                            m_ticketsbag.deleteTicket();

                        }
//                        else {
//                            try {
//                               logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot splitted Bill in deactivate method of  "+m_oTicket.getTableName() +" id is "+m_oTicket.getPlaceId());
//                                dlReceipts.deleteSharedSplitTicket(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
//                            } catch (BasicException ex) {
//                                logger.info("ORDER NO. " + m_oTicket.getOrderId() + "exception in deleteSharedSplitTicket in deactivate method" + ex.getMessage());
//                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
                    }//delete all non kot items
                    else {
                        //    if(IsSteward==0){
                        while (i < m_oTicket.getLinesCount()) {
                            if (m_oTicket.getLine(i).getIsKot() == 0) {
                                removeTicketLine(i);
                                i = 0;
                            } else {
                                i++;
                            }
                        }
                        m_oTicket.refreshTxtFields(0);
                        if (m_oTicket.getLinesCount() == 0) {
                            logger.info("Order No." + m_oTicket.getOrderId() + "deleting zero lines Bill in deactivate method of " + m_oTicket.getTableName() + " id is " + m_oTicket.getPlaceId());
                            //              if(reservationStatus.equals("true")){  
                            //              ReservationStatus.Reservation(m_oTicket.getTableName(),"2",m_App);  
                            //              }
                            if (m_oTicket.getOrderId() != 0) {
                                m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                                m_oTicket.setActiveCash(m_App.getActiveCashIndex());
                                m_oTicket.setActiveDay(m_App.getActiveDayIndex());
                                m_oTicket.setDate(new Date()); //
                                String ticketDocument;
                                ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_oTicket.getTicketId();
                                String reason = "cancelled all kot lines";
                                try {
                                    dlSales.saveRetailCancelTicket(m_oTicket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                                    if (m_App.getProperties().getProperty("machine.reservationstatus").equals("true")) {
                                        ReservationStatus.Reservation(m_oTicket.getTableName(), "2", m_App);
                                    }
                                } catch (BasicException ex) {
                                    logger.info("newTicket saveRetailCancelTicket exception 2" + ex.getMessage() + ";");
                                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            //put here split condition
//                                if(m_oTicket.getTakeaway().equals("Y")){
//                                dlReceipts.deleteTakeawayTicket(m_oTicket.getPlaceId());
//                            }
//                             dlReceipts.deleteSharedTicket(m_oTicket.getPlaceId());   
                            m_ticketsbag.deleteTicket();

                        } else {
                            m_oTicket.setTicketOpen(false);
                            dlReceipts.updateSharedTicket(m_oTicket.getPlaceId(), m_oTicket);
//                            if (m_oTicket.getTakeaway().equals("Y")) {
//                                dlReceipts.updateTakeawayTicket(m_oTicket.getPlaceId(), m_oTicket);
//                            }
                        }
                        //  }
                    }// ends condition    
                }
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return m_ticketsbag.deactivate();
    }

    protected abstract JRetailTicketsBag getJTicketsBag();

    protected abstract Component getSouthComponent();

    protected abstract void resetSouthComponent();

    protected abstract void resetSouthComponent(String value);

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt) {
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        kotprintIssue = 0;
//          m_jPanTotals1.setVisible(false);

        catcontainer.setVisible(true);
        if (m_oTicket != null) {
            if (m_oTicket.isTaxExempt()) {
                jCheckServiceCharge.setSelected(true);
            } else {
                jCheckServiceCharge.setSelected(false);
            }
            for (AccessInfo acc : m_oTicket.getAccessInfo()) {
                logger.info("bill access info :  " + acc.getStatus() + " --" + acc.getUser() + "--" + acc.getSystem() + "--" + acc.getTime() + "--");
            }
            //System.out.println("entering here?"+m_oTicket.getPlaceId()+"split"+m_oTicket.getSplitSharedId());
            JRetailTicketsBagRestaurant.stopReloadTimer();
            m_oTicket.setM_App(m_App);
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            //  if (m_oTicket.getSplitValue().equals("")) {
            if (dbUpdatedDate == null) {
                try {
                    String splitId = m_oTicket.getSplitSharedId();
                    Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                    if (record != null) {
                        m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));
                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //   } 
            kotaction = 0;
            m_oTicket.setTableName(oTicketExt.toString());  //System.out.println("order number "+m_oTicket.getOrderId());
            if (momoeStatus.equals("true")) {
                jTextPhoneNo.setText(m_oTicket.getMomoePhoneNo());
            } else {
                jTextPhoneNo.setText("");
                m_oTicket.setMomoePhoneNo("");
            }
            if (m_oTicket.getOldTableName() != null && m_oTicket.getOldTableName() != "") {
                String moveText = "<html>This Table has been moved from Table " + m_oTicket.getOldTableName() + " by " + m_oTicket.getMovedUser() + "<br>on " + m_oTicket.getMovedTime() + "</html>";
                jMoveTableText.setText(moveText);
            } else {
                jMoveTableText.setText("");
            }

            try {
                logger.info("No. of Running Tables : " + dlReceipts.getSharedTicketCount());
            } catch (BasicException ex) {
                logger.info("exception in getSharedTicketCount" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("split value testing issue IN ACTIVE TICKET " + m_oTicket.getSplitValue());
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.


            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                System.out.println("line testing" + line.getPreparationStatus());
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setRetailPanel(this);

            }

            m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
            // System.out.println(m_oTicket.getRemarks()+"m_oTicket.getRemarks()");
            if (!("").equals(m_oTicket.getRemarks()) && m_oTicket.getRemarks() != null && !m_oTicket.getRemarks().isEmpty()) {
                jButtonRemarks.setBackground(Color.green);
                jButtonRemarks.setForeground(Color.black);
            } else {
                jButtonRemarks.setBackground(new Color(95, 110, 120));
                jButtonRemarks.setForeground(Color.white);
            }
        }



        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");


        refreshTicket();

    }

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt, String editBillId) {
        this.editSaleBillId = editBillId;
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        if (getEditSale() == "Edit") {
            //     m_jLastBill.setVisible(false);
            m_jBtnKot.setVisible(false);
            m_jbtnPrintBill.setVisible(false);
        }
        if (m_oTicket != null) {
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.

            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setRetailPanel(this);

            }

        }
//        if(m_jCboCustName.getSelectedItem()!=null){
//            m_jCboCustName.setSelectedItem("");
//            m_jCboContactNo.setSelectedItem("");
//            m_jTxtCustomerId.setText("");
//            m_jCboContactNo.setSelectedItem("");
//        }
//        if(m_jTxtItemCode.getText()!=null){
//         //   m_jCboItemCode.setSelectedItem("");
//            m_jTxtItemCode.setText("");
//             m_jCboItemName.setSelectedIndex(-1);
//            m_jCboItemName.setSelectedItem("");
//        }
        //   m_jCash.setText("");
//        m_jCheque.setText("");
        //    m_jCard.setText("");
        //    m_jtxtChequeNo.setText("");
        //    m_jFoodCoupon.setText("");
        //    m_jVoucher.setText("");

        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");

        refreshTicket();
    }

    public RetailTicketInfo getActiveTicket() {
        return m_oTicket;
    }

    private void setDiscountButtonEnable() {
        int count = m_oTicket.getLinesCount();
        if (count == 0) {
            m_oTicket.setRate("0");
            m_jBtnDiscount.setEnabled(false);
        } else {
            m_jBtnDiscount.setEnabled(true);
        }
    }

    public void refreshTicket() {
        //   System.out.println("refreshTicket");
        CardLayout cl = (CardLayout) (getLayout());
        customerFocus();
        if (m_oTicket == null) {
            m_ticketlines.clearTicketLines();

            m_jSubtotalEuros1.setText(null);
            m_jTaxesEuros1.setText(null);
            m_jTotalEuros.setText(null);

            stateToZero();

            // Muestro el panel de nulos.
            cl.show(this, "null");
            // resetSouthComponent();  //commented to test performance

        } else {
            if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
            }


            String name = m_oTicket.getName(m_oTicketExt);
//             if(name.contains("-")){
//
//                String customer[]= null;
//                customer =name.split("-");
//                cusName.setText(customer[0]);
//            }else{
//                  cusName.setText("");
//            }
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();

            // Muestro el panel de tickets.
            cl.show(this, "ticket");
            //   resetSouthComponent();   //commented to test performance

            // activo el tecleador...
            m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
            m_jLblCurrentDate.setText(getDateForMe().toString());
            m_jUser.setText((m_oTicket.getUser()).getName());
            m_jLblTime.setText(getTime().toString() + "        ");
            m_jTable.setText(m_oTicket.getName(m_oTicketExt));
        }
    }

    public String getLogDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public String getDateForMe() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date m_dDate = new Date();
        StringBuffer strb = new StringBuffer();
        strb.append("DATE: ");
        return strb.append(sdf.format(m_dDate).toString()).toString();
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date m_dDate = new Date();
        StringBuffer strb = new StringBuffer();
        strb.append("LOGGED IN TIME: ");
        return strb.append(sdf.format(m_dDate).toString()).toString();
    }

    private void showProductPanel() {

        // Load product panel
        java.util.List<ProductInfoExt> product = null;
        try {
            if (menuStatus.equals("false")) {
                product = dlSales.getPopularProduct("Y");
            } else {
                day = getWeekDay();
                currentMenuList = dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                product = dlSales.getMenuPopularProduct("Y", menuId);
            }
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in showProductPanel" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


        JRetailCatalogTab jcurrTab = new JRetailCatalogTab();
        jcurrTab.applyComponentOrientation(getComponentOrientation());
        m_jProducts.add(jcurrTab, "PRODUCT.");

        // Add products
        for (ProductInfoExt prod : product) {
            tnbbutton = new ThumbNailBuilderPopularItems(97, 57, "com/openbravo/images/bluepopulartoit.png");
            jcurrTab.addPopularItemButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
        }

        // selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

        CardLayout cl = (CardLayout) (m_jProducts.getLayout());
        cl.show(m_jProducts, "PRODUCT.");

    }

    private String getProductLabel(ProductInfoExt product) {

        // if (pricevisible) {
        // if (taxesincluded) {
        //   TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID());
        //   return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax(tax);
        // } else {
        //     return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
        //  }
        // } else {
        return "<html><center>" + product.getName() + "<br>";
        // }
    }

    private class SelectedAction implements ActionListener {

        private ProductInfoExt prod;

        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }

        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);

        }
    }

    protected void fireSelectedProduct(ProductInfoExt prod) {


        System.out.println(" fireSelectedProduct for popular item");
        kotaction = 1;
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        for (int i = 0; i < l.length; i++) {
            if (e == null) {
                e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
            }
            ((ActionListener) l[i]).actionPerformed(e);
        }
        incProduct(prod);
    }

    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }

    public void printPartialTotals() {

        //  setDiscountButtonEnable();
        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros1.setText(null);
            m_jTaxesEuros1.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount1.setText(null);
//          m_jBillDiscount.setText(null);
            m_oTicket.setBillDiscount(0);
            m_jServiceCharge.setText(null);
            m_jServiceTax.setText(null);

//                m_jTaxList.removeAll();
            m_jTaxList.setModel(new DefaultListModel());
            jLabelServiceTax.setVisible(false);
        } else {

//                calculateServiceCharge();
            try {
                if (m_oTicket.getDiscountMap() != null && m_oTicket.iscategoryDiscount()) {
                    populateDiscount(m_oTicket.getDiscountMap());
                }
                //    populateServiceChargeList();
                //       populateServiceTaxList();
                populateTaxList();
            } catch (BasicException ex) {
                logger.info("exception in populateTaxList " + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("m_oTicket.printSubTotalValue()---" + m_oTicket.printSubTotalValue());
            //m_jSubtotalEuros1.setText(m_oTicket.printSubTotalValue());
            m_jSubtotalEuros1.setText(m_oTicket.printSubTotalValueBeforeDiscount());
            m_jTaxesEuros1.setText(m_oTicket.printTax());
            m_jTotalEuros.setText(m_oTicket.printTotal());
            m_jDiscount1.setText(m_oTicket.printDiscount());
            if (m_oTicket.printServiceCharge() != null) {
                //jLabel13.setVisible(true);
                m_jServiceCharge.setText(m_oTicket.printServiceCharge());
            }
            if (m_oTicket.printServiceTax() != null) {
                //     jLabelServiceTax.setVisible(true);
                //m_jServiceTax.setText(m_oTicket.printServiceTax());
            }


        }
        setTenderAmount();
    }
    //consolidating taxes based on rate

    private void consolidateTaxes(RetailTicketInfo ticket) {
        Map<String, NameTaxMapInfo> taxMap = new HashMap<String, NameTaxMapInfo>();
        double rate;
        String taxName;
        double taxValue;
        //Grouping the taxes based on rate
        for (int i = 0; i < ticket.getTaxes().size(); i++) {
            rate = ticket.getTaxes().get(i).getTaxInfo().getRate();
            taxName = ticket.getTaxes().get(i).getTaxInfo().getSplitName();

            taxValue = ticket.getTaxes().get(i).getRetailTax();


            String strTaxValue = String.format("%.2f", taxValue);

            //  System.out.println("strTaxValue : "+strTaxValue);
            taxValue = Double.parseDouble(strTaxValue);
            //      System.out.println("taxValue : "+taxValue);
            if (taxMap.get(taxName) == null) {
                taxMap.put(taxName, new NameTaxMapInfo(taxName, taxValue));
            } else {
                taxValue = taxValue + taxMap.get(taxName).getTaxValue();
                taxMap.put(taxName, new NameTaxMapInfo(taxName, taxValue));
            }

        }

//    NavigableSet<String> navig = ((TreeMap) srtMap).descendingKeySet();
//        for (Iterator<String> iter = navig.iterator(); iter.hasNext();) {
//            String key = (String) iter.next();
//            // System.out.println("Sorted"+key + "" + srtMap.get(key) );  
//            String taxName1 = key;
//            String taxValue1 = srtMap.get(key);
//            double taxVal2 = Double.parseDouble(taxValue1);
//
//            srttaxMap.put(taxName1, new NameTaxMapInfo(taxName1, taxVal2));
//            //ticket.setNametaxMap(taxMap);
//            ticket.setNametaxMap(srttaxMap);
//            System.out.println("Sorted" + taxName1 + "" + taxVal2);
//        }
        ticket.setNametaxMap(taxMap);
    }

//     private void consolidateTaxes(RetailTicketInfo ticket) {
//        Map<Double, TaxMapInfo> taxMap = new HashMap<Double, TaxMapInfo>();
//        double rate;
//        String taxName;
//        double taxValue;
//        //Grouping the taxes based on rate
//        for (int i = 0; i < ticket.getTaxes().size(); i++) {
//            rate = ticket.getTaxes().get(i).getTaxInfo().getRate();
//            taxName = ticket.getTaxes().get(i).getTaxInfo().getSplitName();
//            if (taxName.startsWith("SGST")) {
//                rate = 0.0021;
//            }
//            taxValue = ticket.getTaxes().get(i).getRetailTax();
//            String strTaxValue = String.format("%.2f", taxValue);
////            System.out.println("strTaxValue==="+strTaxValue);
//            taxValue = Double.parseDouble(strTaxValue);
//            if (taxMap.get(rate) == null) {
//                taxMap.put(rate, new TaxMapInfo(taxName, taxValue));
//            } else {
//                taxValue = taxValue + taxMap.get(rate).getTaxValue();
//                taxMap.put(rate, new TaxMapInfo(taxName, taxValue));
//            }
//
//        }
//        ticket.setTaxMap(taxMap);
//    }
    public void populateTaxList() throws BasicException {
        try {
            System.out.println("populating tax list");
            taxeslogic.calculateTaxes(m_oTicket);
        } catch (TaxesException ex) {
            logger.info("exception in populateTaxList " + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxModel = new DefaultListModel();
        m_jTaxList.setModel(taxModel);
        empty = BorderFactory.createEmptyBorder();
        m_jTaxList.setBorder(empty);
//added newly to calcuate taxes based on erp tax category 
        consolidateTaxes(m_oTicket);

        int i = 0;
        //  for (Map.Entry<Double, TaxMapInfo> entry : m_oTicket.getTaxMap().entrySet()) {
        for (Map.Entry<String, NameTaxMapInfo> entry : m_oTicket.getNametaxMap().entrySet()) {

            String taxName = entry.getValue().getName();
            String taxValue = String.format("%.2f", entry.getValue().getTaxValue());
            String taxEntry = "<html>"
                    + "<style type=\\\"text/css\\\">"
                    + "body { margin: 0px auto; }\\n"
                    + "td { "
                    + "padding: 0px;"
                    + "margin: 0px;"
                    + "}"
                    + "</style>"
                    + "<body>"
                    + "	<table style=\"width:150px\">"
                    + "		<tr bgcolor=grey>"
                    + "			<td width=\"120px\" align=\"left\">" + taxName + "</td>"
                    + "			<td width=\"30px\" align=\"right\">" + taxValue + "</td>"
                    + "		</tr>		"
                    + "	</table>"
                    + "<body>"
                    + "</html>";

            taxModel.add(i, taxEntry);
            i = i + 1;
        }

//        for (int i = 0; i < m_oTicket.getTaxes().size(); i++) {
//            System.out.println("getPercentage " + m_oTicket.getTaxes().get(i).getTax());
//            //Name of the tax
//            String taxName =m_oTicket.getTaxes().get(i).getTaxInfo().getName();
//            //decimal value of tax with two significant digits
//            String taxValue = String.format("%.2f",m_oTicket.getTaxes().get(i).getRetailTax());
//            //String variable holding HTML code to render two strings, in a table row within two columns.
//            String taxEntry = "<html>"+
//                    "<style type=\\\"text/css\\\">"+
//                    "body { margin: 0px auto; }\\n"+
//                    "td { "+
//                    "padding: 0px;"+
//                    "margin: 0px;"+
//                    "}"+
//                    "</style>"+
//                    "<body>"+
//                    "	<table style=\"width:150px\">"+
//                    "		<tr bgcolor=grey>"+
//                    "			<td width=\"120px\" align=\"left\">"+taxName+"</td>"+
//                    "			<td width=\"30px\" align=\"right\">"+taxValue+"</td>"+
//                    "		</tr>		"+
//                    "	</table>"+
//                    "<body>"+
//                    "</html>";
//            
//            taxModel.add(i, taxEntry);
//        }
    }

//public void populateServiceChargeList() throws BasicException {
//System.out.println("populating service charge list");
//double chargeTotal =0.0;
//String ServiceChargeTotal=null;
//    try {
//            chargeslogic.calculateCharges(m_oTicket);
//        } catch (TaxesException ex) {
//            logger.info("exception in populateServiceChargeList "+ex.getMessage()+";");
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    System.out.println("total charge"+m_oTicket.getCharges().size());
//      for (int i = 0; i < m_oTicket.getCharges().size(); i++) {
//         //Name of the servicecharge
//            String chargeName =m_oTicket.getCharges().get(i).getServiceChargeInfo().getName();
//            //decimal value of tax with two significant digits
//            String chargeValue = String.format("%.2f",m_oTicket.getCharges().get(i).getRetailSCharge());
//            chargeTotal=chargeTotal+Double.valueOf(chargeValue);
//         }
//      System.out.println("chargeTotal"+chargeTotal);
//     ServiceChargeTotal= Formats.CURRENCY.formatValue(chargeTotal);
//        m_oTicket.setServiceCharge(chargeTotal);
//      m_jServiceCharge.setText(ServiceChargeTotal);
//    }
//
//public void populateServiceTaxList() throws BasicException {
//System.out.println("populating service tax list");
//double staxTotal =0.0;
//String ServiceTaxTotal=null;
//    try {
//            staxeslogic.calculateServiceTaxes(m_oTicket);
//        } catch (TaxesException ex) {
//            logger.info("exception in populateServiceTaxList "+ex.getMessage()+";");
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    
//      for (int i = 0; i < m_oTicket.getServiceTaxes().size(); i++) {
//         //Name of the servicecharge
//            String staxName =m_oTicket.getServiceTaxes().get(i).getTaxInfo().getName();
//             System.out.println("staxName"+staxName);
//            //decimal value of tax with two significant digits
//            String staxValue = String.format("%.2f",m_oTicket.getServiceTaxes().get(i).getRetailServiceTax());
//           staxTotal=staxTotal+Double.valueOf(staxValue);
//            System.out.println("Double ServiceTaxTotal"+m_oTicket.getServiceTaxes().get(i).getRetailServiceTax()+" "+staxTotal+" "+staxValue); 
//         }
//     ServiceTaxTotal= Formats.CURRENCY.formatValue(staxTotal);
//       System.out.println("Double ServiceTaxTotal"+ServiceTaxTotal);
//         m_oTicket.setServiceTax(staxTotal);
//         m_jServiceTax.setText(ServiceTaxTotal);
//    }
//public void calculateServiceCharge(){
//         
////            System.out.println("print-0---"+serviceTaxList.get(0).getRate()+"---"+serviceTaxList.size()+"--"+serviceChargeList.get(0).getRate()+"--"+m_oTicket.getTotal());
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(m_oTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceCharge"+ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = m_oTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = m_oTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  m_oTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//                
//                  m_oTicket.setServiceCharge(serviceChargeAmt);
//                  m_oTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      m_oTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      m_oTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (m_oTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      System.out.println("serviceTaxAmt--"+serviceChargeAmt +"--"+serviceTax+"---"+m_oTicket.getSubTotal()+"---"+serviceChargeAmt);
//                      m_oTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
//
//      public void calculateServiceChargeSplitTicket(RetailTicketInfo splitTicket){
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(splitTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceChargeSplitTicket "+ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = splitTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = splitTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  splitTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//                
//                  splitTicket.setServiceCharge(serviceChargeAmt);
//                  splitTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      splitTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      splitTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (splitTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      splitTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
//public void calculateServiceCharge(RetailTicketInfo retail){
//
////            System.out.println("print-0---"+serviceTaxList.get(0).getRate()+"---"+serviceTaxList.size()+"--"+serviceChargeList.get(0).getRate()+"--"+m_oTicket.getTotal());
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(m_oTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceCharge "+ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = m_oTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = m_oTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  m_oTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//
//                  m_oTicket.setServiceCharge(serviceChargeAmt);
//                  m_oTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      m_oTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      m_oTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (m_oTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      System.out.println("serviceTaxAmt--"+serviceChargeAmt +"--"+serviceTax);
//                      m_oTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
    private void paintTicketLine(int index, RetailTicketLineInfo oLine) {
        System.out.println("oLine" + oLine.getIsKot());
        if (executeEventAndRefresh("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            m_oTicket.setLine(index, oLine);

            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
            visorTicketLine(oLine); // Y al visor tambien...
            if (getServedStatus() == 0) {
                printPartialTotals();
                stateToZero();
                executeEventAndRefresh("ticket.change");
            }
        }
    }

    private void paintKotTicketLine(int index, RetailTicketLineInfo oLine) {
        System.out.println("oLine" + oLine.getIsKot());
        if (executeEventAndRefreshForKot("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            System.out.println("executeEventAndRefreshForKot");
            m_oTicket.setLine(index, oLine);
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
            visorTicketLine(oLine); // Y al visor tambien...
//            if(getServedStatus()==0){
//           executeEventAndRefresh("ticket.change");
//            }
        }
    }

    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) {
        try {
            String avail = dlReceipts.getProductAvailablityStatus(oProduct.getID());
            if (avail.equals("Y")) {
                kotaction = 1;
                //RETURNS THE parent tax
                TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                ServiceChargeInfo charge = chargeslogic.getChargeInfo(oProduct.getServiceChargeID(), m_oTicket.getCustomer());
                TaxInfo sertax = staxeslogic.getTaxInfo(oProduct.getServiceTaxID(), m_oTicket.getCustomer());
                //RETURNS the exempt parent tax
                TaxInfo exemptTax = taxeslogic.getExemptTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                if (oProduct.getComboProduct().equals("Y")) {
                    addonId = UUID.randomUUID().toString();
                    addonId = addonId.replaceAll("-", "");
                    addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 0, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, oProduct.getStation(), null, 1, exemptTax));
                    checkMandatoryAddon(oProduct, addonId);
                } else {
                    addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 0, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, oProduct.getStation(), null, 0, exemptTax));
                }
                addonId = null;
            } else {
                showMessage(this, "Sorry! This item is not available");
            }

        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTicketLine(RetailTicketLineInfo oLine) {

        if (executeEventAndRefresh("ticket.addline", new ScriptArg("line", oLine)) == null) {

            if (oLine.isProductCom()) {
                // Comment then where you can
                int i = m_ticketlines.getSelectedIndex();

                // I miss the first normal product ...
                if (i >= 0 && !m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                // I jump all the auxiliary products ...
                while (i >= 0 && i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                if (i >= 0) {
                    m_oTicket.insertLine(i, oLine);
                    m_ticketlines.insertTicketLine(i, oLine); // Pintamos la linea en la vista...                 
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } else {
                // Producto normal, entonces al finalnewline.getMultiply() 

                m_oTicket.addLine(oLine);
                m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la vista...

            }
            // m_oTicket.setRate("0");
            // m_oTicket.setdAmt(0);
            //m_oTicket.refreshTxtFields(1);
            visorTicketLine(oLine);
//            setTime(m_oTicket);
            printPartialTotals();
            stateToZero();

            // event receipt
            executeEventAndRefresh("ticket.change");
            //  m_jCboItemName.setSelectedIndex(-1);
//            m_jCboItemName.setSelectedItem("");
            //           m_jTxtItemCode.setText("");
//             itemName.setText("");
            stateToItem();
        }
    }

    private void removeTicketLine(int i) {

        if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {

            if (m_oTicket.getLine(i).isProductCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);
            } else {
                // Es un producto normal, lo borro.
                if (m_oTicket.getLine(i).getPromoType().equals("BuyGet") && m_oTicket.getLine(i).getIsCrossProduct().equals("Y") && m_oTicket.getLine(i).getPrice() != 0) {
                    String crossProduct = null;
                    if (m_oTicket.getLine(i).getPromoRule().get(0).getIsSku().equals("Y")) {
                        crossProduct = m_oTicket.getLine(i).getProductID();
                    } else {
                        try {
                            crossProduct = dlSales.getCrossPromoProduct(m_oTicket.getLine(i).getpromoId());

                        } catch (BasicException ex) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in removeTicketLine" + ex.getMessage());
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                    int index = m_oTicket.getProductIndex(crossProduct);
                    m_oTicket.removeLine(index);
                    m_ticketlines.removeTicketLine(index);

                } else {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }

                // Y todos lo auxiliaries que hubiera debajo.
                while (i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }
            }

            visorTicketLine(null); // borro el visor 
            printPartialTotals(); // pinto los totales parciales...                           
            stateToZero(); // Pongo a cero    

            // event receipt
            executeEventAndRefresh("ticket.change");
        }
    }

    private ProductInfoExt getInputProduct() {
        ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
        oProduct.setReference(null);
        oProduct.setCode(null);
        oProduct.setName("");
        oProduct.setTaxCategoryID(((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem()).getID());
        oProduct.setPriceSell(includeTaxes(oProduct.getTaxCategoryID(), getInputValue()));
        return oProduct;
    }

    private double includeTaxes(String tcid, double dValue) {
        if (m_jaddtax.isSelected()) {
            TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getCustomer(), "N");
            double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);
        } else {
            return dValue;
        }
    }

    private double getInputValue() {
        //   try {
//            return Double.parseDouble(m_jPrice.getText());
        //  } catch (NumberFormatException e){
        return 0.0;
        //   }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(m_jPor.getText().substring(1));
        } catch (NumberFormatException e) {
            return 1.0;
        } catch (StringIndexOutOfBoundsException e) {
            return 1.0;
        }
    }

    public void stateToZero() {
        m_jPor.setText("");
//        m_jPrice.setText("");
        m_sBarcode = new StringBuffer();

        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }

    public void stateToBarcode() {

        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //  m_jEnter.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);

//        itemName.setFocusable(false);
//        m_jTxtItemCode.setFocusable(false);
//        m_jCash.setFocusable(false);
//        m_jCard.setFocusable(false);
        // m_jCheque.setFocusable(false);
        //  m_jtxtChequeNo.setFocusable(false);
        //   m_jFoodCoupon.setFocusable(false);
        //    m_jVoucher.setFocusable(false);
//        m_jDeliveryBoy.setFocusable(false);
        //   m_jTxtAdvance.setFocusable(false);
        //    m_jCreditAmount.setFocusable(false);
        //     m_jHomeDelivery.setFocusable(false);
        //  m_jCod.setFocusable(false);
        //     m_jCreditAllowed.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        m_jSplitBtn.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }

    public void stateToHomeDelivery() {
        cusName.setFocusable(false);
//      m_jTxtCustomerId.setFocusable(false);
//        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //   m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);

//        itemName.setFocusable(false);
//        m_jTxtItemCode.setFocusable(false);
//        m_jCash.setFocusable(false);
//        m_jCard.setFocusable(false);
        //     m_jCheque.setFocusable(false);
        //      m_jtxtChequeNo.setFocusable(false);
        //      m_jFoodCoupon.setFocusable(false);
        //      m_jVoucher.setFocusable(false);

        //     m_jCreditAmount.setFocusable(false);
        //   JRetalTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }

    public void stateToItem() {
//        cusName.setFocusable(false);
        //  cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        //   m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //     m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
//        m_jTxtCustomerId.setFocusable(false);
//        m_jCash.setFocusable(false);
        //     m_jCard.setFocusable(false);
        //    m_jCheque.setFocusable(false);
        //    m_jtxtChequeNo.setFocusable(false);
        //     m_jFoodCoupon.setFocusable(false);
        //     m_jVoucher.setFocusable(false);
        //  m_jDeliveryBoy.setFocusable(false);
        //   m_jTxtAdvance.setFocusable(false);
        //    m_jCreditAmount.setFocusable(false);
        //   m_jHomeDelivery.setFocusable(false);
        // m_jCod.setFocusable(false);
        //    m_jCreditAllowed.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
        // m_jSplitBtn.setFocusable(false);

    }

    public void stateToPay() {
        //  cusName.setFocusable(false);
        //   cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //   m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //    m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
//        m_jTxtCustomerId.setFocusable(false);
//        m_jCash.setFocusable(false);
        //   m_jCard.setFocusable(false);
        //   m_jCheque.setFocusable(false);
        // m_jFoodCoupon.setFocusable(false);
        //     m_jVoucher.setFocusable(false);
        //      m_jCreditAmount.setFocusable(false);
        //      m_jtxtChequeNo.setFocusable(false);
//        itemName.setFocusable(false);
        //   m_jTxtItemCode.setFocusable(false);
    }

    public void stateToPayment() {
        //   m_jTxtCustomerId.setFocusable(false);
//        itemName.setFocusable(false);
//        m_jTxtItemCode.setFocusable(false);
        //       cusName.setFocusable(false);
        //     cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        //    m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //    m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
//        m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
        //     m_jCreditAllowed.setFocusable(false);

    }

    private void incProductByCode(String sCode) {
        // precondicion: sCode != null
        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {

                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    public void incProductByItemDetails(String id) {
        // precondicion: sCode != null
        try {
            ProductInfoExt oProduct = dlSales.getProductInfo(id);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
//                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    private void incProductByCodePrice(String sCode, double dPriceSell) {
        // precondicion: sCode != null

        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                if (m_jaddtax.isSelected()) {
                    // debemos quitarle los impuestos ya que el precio es con iva incluido...
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                    addTicketLine(oProduct, 1.0, dPriceSell / (1.0 + tax.getRate()));
                } else {
                    addTicketLine(oProduct, 1.0, dPriceSell);
                }
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    public void getCreditDate() {
        Date Currentdate = null;
        Calendar now = Calendar.getInstance();
        int days = Integer.parseInt(m_App.getProperties().getValidity());
        now.add(Calendar.DATE, days);
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + (now.get(Calendar.YEAR));
        try {
            Currentdate = (Date) formatter.parse(str_date);
            // Currentdate =;
        } catch (java.text.ParseException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + " exception in getCreditDate" + ex.getMessage());
            Logger.getLogger(RetailTicketInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Currentdate =;
        m_oTicket.setNewDate(Currentdate);
        // Currentdate =;


    }

    private void incProduct(ProductInfoExt prod) {

        if (prod.isScale() && m_App.getDeviceScale().existsScale()) {
            try {
                Double value = m_App.getDeviceScale().readWeight();
                if (value != null) {
                    incProduct(value.doubleValue(), prod);
                }
            } catch (ScaleException e) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
                stateToZero();
            }
        } else {
            // No es un producto que se pese o no hay balanza
            incProduct(1.0, prod);
        }
    }

    private void incProduct(double dPor, ProductInfoExt prod) {
        // precondicion: prod != null
        addTicketLine(prod, dPor, prod.getPriceSell());
    }

    public String getWeekDay() {
        String DAY = "";
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                DAY = "SUNDAY";
                break;
            case 2:
                DAY = "MONDAY";
                break;
            case 3:
                DAY = "TUESDAY";
                break;
            case 4:
                DAY = "WEDNESDAY";
                break;
            case 5:
                DAY = "THURSDAY";
                break;
            case 6:
                DAY = "FRIDAY";
                break;
            case 7:
                DAY = "SATURDAY";
                break;
        }
        return DAY;
    }

    protected void buttonTransition(ProductInfoExt prod) {
        kotaction = 1;
        System.out.println("buttonTransition testing");
        buttonPlus = 1;
        // precondicion: prod != null
        // selectedProduct = prod.getID();

        if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(prod);
        } else if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(getInputValue(), prod);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

//    private void stateTransition(char cTrans) throws BasicException {
//
//        if (cTrans == '\n') {
//            // Codigo de barras introducido
//            if (m_sBarcode.length() > 0) {
//                String sCode = m_sBarcode.toString();
//                if (sCode.startsWith("c")) {
//                    // barcode of a customers card
//                    try {
//                        CustomerInfoExt newcustomer = dlSales.findCustomerExt(sCode);
//                        if (newcustomer == null) {
//                            Toolkit.getDefaultToolkit().beep();
//                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer")).show(this);
//                        } else {
//                            m_oTicket.setCustomer(newcustomer);
////                            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
//                        }
//                    } catch (BasicException e) {
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e).show(this);
//                    }
//                    stateToZero();
//                } else if (sCode.length() == 13 && sCode.startsWith("250")) {
//                    // barcode of the other machine
//                    ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
//                    oProduct.setReference(null); // para que no se grabe
//                    oProduct.setCode(sCode);
//                    oProduct.setName("Ticket " + sCode.substring(3, 7));
//                    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7, 12)) / 100);
//                    oProduct.setTaxCategoryID(((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem()).getID());
//                    // Se anade directamente una unidad con el precio y todo
//                    addTicketLine(oProduct, 1.0, includeTaxes(oProduct.getTaxCategoryID(), oProduct.getPriceSell()));
//                } else if (sCode.length() == 13 && sCode.startsWith("210")) {
//                    // barcode of a weigth product
//                    incProductByCodePrice(sCode.substring(0, 7), Double.parseDouble(sCode.substring(7, 12)) / 100);
//                } else {
//                    incProductByCode(sCode);
//                }
//            } else {
//                Toolkit.getDefaultToolkit().beep();
//            }
//        } else {
//            // otro caracter
//            // Esto es para el codigo de barras...
//            m_sBarcode.append(cTrans);
//
//            // Esto es para el los productos normales...
//            if (cTrans == '\u007f') {
//                stateToZero();
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
////                m_jPrice.setText("0");
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
//                // Un numero entero
//                //       m_jPrice.setText(Character.toString(cTrans));
//                m_iNumberStatus = NUMBER_INPUTINT;
//                m_iNumberStatusInput = NUMBERVALID;
//            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTINT)) {
//                // Un numero entero
//                //      m_jPrice.setText(m_jPrice.getText() + cTrans);
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
//                //    m_jPrice.setText("0.");
//                m_iNumberStatus = NUMBER_INPUTZERODEC;
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
//                //    m_jPrice.setText(m_jPrice.getText() + ".");
//                m_iNumberStatus = NUMBER_INPUTDEC;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                // Un numero decimal
//                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                // Un numero decimal
//                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
//                m_iNumberStatus = NUMBER_INPUTDEC;
//                m_iNumberStatusInput = NUMBERVALID;
//
//            } else if (cTrans == '*'
//                    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                m_jPor.setText("x");
//                m_iNumberStatus = NUMBER_PORZERO;
//            } else if (cTrans == '*'
//                    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
//                //  m_jPrice.setText("0");
//                m_jPor.setText("x");
//                m_iNumberStatus = NUMBER_PORZERO;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_PORZERO)) {
//                m_jPor.setText("x0");
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORZERO)) {
//                // Un numero entero
//                m_jPor.setText("x" + Character.toString(cTrans));
//                m_iNumberStatus = NUMBER_PORINT;
//                m_iNumberStatusPor = NUMBERVALID;
//            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORINT)) {
//                // Un numero entero
//                m_jPor.setText(m_jPor.getText() + cTrans);
//
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORZERO) {
//                m_jPor.setText("x0.");
//                m_iNumberStatus = NUMBER_PORZERODEC;
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORINT) {
//                m_jPor.setText(m_jPor.getText() + ".");
//                m_iNumberStatus = NUMBER_PORDEC;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
//                // Un numero decimal
//                m_jPor.setText(m_jPor.getText() + cTrans);
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
//                // Un numero decimal
//                m_jPor.setText(m_jPor.getText() + cTrans);
//                m_iNumberStatus = NUMBER_PORDEC;
//                m_iNumberStatusPor = NUMBERVALID;
//
//            } else if (cTrans == '\u00a7'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
//                // Scale button pressed and a number typed as a price
//                if (m_App.getDeviceScale().existsScale() && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                    try {
//                        Double value = m_App.getDeviceScale().readWeight();
//                        if (value != null) {
//                            ProductInfoExt product = getInputProduct();
//                            addTicketLine(product, value.doubleValue(), product.getPriceSell());
//                        }
//                    } catch (ScaleException e) {
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
//                        stateToZero();
//                    }
//                } else {
//                    // No existe la balanza;
//                    Toolkit.getDefaultToolkit().beep();
//                }
//            } else if (cTrans == '\u00a7'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
//                // Scale button pressed and no number typed.
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else if (m_App.getDeviceScale().existsScale()) {
//                    try {
//                        Double value = m_App.getDeviceScale().readWeight();
//                        if (value != null) {
//                            RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                            newline.setMultiply(value.doubleValue());
//                            newline.setPrice(Math.abs(newline.getPrice()));
//                            paintTicketLine(i, newline);
//                        }
//                    } catch (ScaleException e) {
//                        // Error de pesada.
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
//                        stateToZero();
//                    }
//                } else {
//                    // No existe la balanza;
//                    Toolkit.getDefaultToolkit().beep();
//                }
//
//                // Add one product more to the selected line
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    //If it's a refund + button means one unit less
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(newline.getMultiply() - 1.0);
//                        paintTicketLine(i, newline);
//                    } else {
//
//                        //    ProductInfoExt product = new ProductInfoExt();
//                        // add one unit to the selected line
//                        newline.setMultiply(newline.getMultiply() + 1.0);
//                        // buttonPlus = 2;
//
//                        paintTicketLine(i, newline);
//                        //    m_oTicket.refreshTxtFields(1);
//                        //below line is for promotion products
////                        ProductInfoExt product = getInputProduct();
////                        addTicketLine(product, 1.0, product.getPriceSell());
//
//                    }
//                }
//
//                // Delete one product of the selected line
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    //If it's a refund - button means one unit more
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(newline.getMultiply() + 1.0);
//                        if (newline.getMultiply() >= 0) {
//                            removeTicketLine(i);
//                        } else {
//                            paintTicketLine(i, newline);
//                        }
//                    } else {
//                        // substract one unit to the selected line
//                        newline.setMultiply(newline.getMultiply() - 1.0);
//                        // m_oTicket.refreshTxtFields(1);
//
//                        if (newline.getMultiply() > 0.0) {
//                            // removeTicketLine(i); // elimino la linea
//                            // } else {
//
//                            paintTicketLine(i, newline);
//                            // m_oTicket.refreshTxtFields(1);
//                            //below line is for promotion products
////                            ProductInfoExt product = getInputProduct();
////                            addTicketLine(product, newline.getMultiply(), product.getPriceSell());
//                        }
//                    }
//                }
//
//                // Set n products to the selected line
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    double dPor = getPorValue();
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(-dPor);
//                        newline.setPrice(Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    } else {
//                        newline.setMultiply(dPor);
//                        newline.setPrice(Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    }
//                }
//
//                // Set n negative products to the selected line
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    double dPor = getPorValue();
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
//                        newline.setMultiply(dPor);
//                        newline.setPrice(-Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    }
//                }
//
//                // Anadimos 1 producto
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, 1.0, product.getPriceSell());
//
//                // Anadimos 1 producto con precio negativo
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, 1.0, -product.getPriceSell());
//
//                // Anadimos n productos
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, getPorValue(), product.getPriceSell());
//
//                // Anadimos n productos con precio negativo ?
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, getPorValue(), -product.getPriceSell());
//
//                // Totals() Igual;
//            } else if (cTrans == ' ' || cTrans == '=') {
//                if (m_oTicket.getLinesCount() > 0) {
//
//                    if (closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo)) {
//                        // Ends edition of current receipt
//                        if (!m_oTicket.getSplitValue().equals("Split")) {
//                               logger.info("Order No." + m_oTicket.getOrderId() + " short cuts are deleting the  "+m_oTicket.getTableName());
//                            m_ticketsbag.deleteTicket();
//                        } else {
//                            //  m_ticketsbag.deleteTicket(m_oTicket.getSplitSharedId());
//                        }
//
//                    } else {
//                        // repaint current ticket
//                        refreshTicket();
//                    }
//                } else {
//                    Toolkit.getDefaultToolkit().beep();
//                }
//            }
//        }
//    }
////    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {
////
////        boolean resultok = false;
////        JPaymentEditor.showMessage(JRetailPanelTicket.this, dlReceipts, ticket,this);
////        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
////
////            try {
////                // reset the payment info
////                taxeslogic.calculateServiceTaxes(ticket);
////                if (ticket.getTotal() >= 0.0) {
////                    ticket.resetPayments(); //Only reset if is sale
////                }
////
////                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
////
////                    // Muestro el total
////                    // printTicket("Printer.TicketTotal", ticket, ticketext);
////                    System.out.println("enter 3333");
////
////                    // Select the Payments information
////                    JPaymentSelect paymentdialog = ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL
////                            ? paymentdialogreceipt
////                            : paymentdialogrefund;
////                    paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));
////
////                    paymentdialog.setTransactionID(ticket.getTransactionID());
////
////                    if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer())) {
////
////                        // assign the payments selected and calculate taxes.
////                        ticket.setPayments(paymentdialog.getSelectedPayments());
////
////                        // Asigno los valores definitivos del ticket...
////                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
////                        ticket.setActiveCash(m_App.getActiveCashIndex());
////                         ticket.setActiveDay(m_App.getActiveDayIndex());
////                        ticket.setDate(new Date()); // Le pongo la fecha de cobro
////
////                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
////                              String ticketDocNo = null;
////                            Integer ticketDocNoInt = null;
////                            String ticketDocument;
////                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){
////
////
////                      try{
////                        try {
////                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
////                        } catch (BasicException ex) {
////                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////                        }
////                        String[] ticketDocNoValue = ticketDocNo.split("-");
////                            ticketDocNo=ticketDocNoValue[2];
////                        }catch(NullPointerException ex){
////                            ticketDocNo = "10000";
////                        }
////                        if(ticketDocNo!=null){
////                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
////                            ticketDocNoInt = ticketDocNoInt + 1;
////
////                        }
////                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
////                         }else{
////                                ticketDocument = "0";
////                         }
////                            m_oTicket.setDocumentNo(ticketDocument);
////                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
////
////                                getCreditDate();
////                          }
////
////                     double creditAmt;
////             //       try{
//////                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
////              //      }catch(Exception ex){
////                        creditAmt =0;
////               //     }
////                            String chequeNos = "";//m_jtxtChequeNo.getText();
////                            String deliveryBoy = "";
////                            String cod;
////                            double advanceissued;
////                            String isCredit;
////                            String isPaidStatus;
////
//////                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
//////                            if(selectedindex!=0){
//////                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
//////                            }
////
////                           // if(m_jCod.isSelected()){
////                           //     cod = "Y";
////                            //    isPaidStatus = "N";
////                           // }else{
////                                cod = "N";
////                                isPaidStatus = "Y";
////                          //  }
////                             if(creditAmt>0){
////                                isCredit = "Y";
////                            }else{
////                                isCredit = "N";
////                            }
////
//////                            if(m_jTxtAdvance.getText().equals("")){
////                                advanceissued = 0;
//////                            }else{
//////                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
//////                            }
////                            String file;
////
////                            //       if(m_jHomeDelivery.isSelected()){
////                                 //      file = "Printer.HomeDelivery";
////                                 //  }else{
////                                        file = "Printer.Bill";
////                                //   }
////                            ticket.setDocumentNo(ticketDocument);
////                            saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,isCredit,"N");
////                            executeEvent(ticket, ticketext, "ticket.close", new ScriptArg("print", paymentdialog.isPrintSelected()));
////
////
////                            resultok = true;
////
////                        }
////                    }
////                }
////            } catch (TaxesException e) {
////                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotcalculatetaxes"));
////                msg.show(this);
////                resultok = false;
////            }
////            ticket.setSplitValue("");
////           // reset the payment info
//////            ticket.resetTaxes();
////          //  ticket.resetPayments();
////        }
////
////         cancelled the ticket.total script
////         or canceled the payment dialog
////         or canceled the ticket.close script
////
////        return resultok;
////    }
    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {
        ticket.setActiveDay(m_App.getActiveDayIndex());
        ticket.setActiveCash(m_App.getActiveCashIndex());
        System.out.println("in closeSplitTicket==============================");
        boolean completed = JPaymentEditor.showMessage(JRetailPanelTicket.this, dlReceipts, ticket, this);
        System.out.println("completed" + completed);
        return completed;
    }

    public void settleBill(double totalBillValue, double cash, double card, double mobile, double voucherAmount) {
        this.totalBillValue = totalBillValue;
        paymentDetail(cash, card, mobile, voucherAmount);

    }

//    public void settleBill(double totalBillValue, double cash, double card, double mobile, double voucherAmount, String mobileType, String mobileNo) {
//        this.totalBillValue = totalBillValue;
//        paymentDetail(cash, card, mobile, voucherAmount, mobileType, mobileNo);
//
//    }
    public void settleBill(double totalBillValue, double cash, double card, double mobile, double voucherAmount, String mobileType, String mobileNo) {
        this.totalBillValue = totalBillValue;
        paymentDetail(cash, card, mobile, voucherAmount, mobileType, mobileNo);

        logger.info("EnterSplitPayment Method");
//        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Card", card);
//        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Cash", cash);
//        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Voucher", voucherAmount);
//        dlReceipts.insertTiltTransaction(dlReceipts.cashTallyId, "Mobile", mobile);
    }

    public boolean getClosePayment() {
        return closePayment;
    }

    public void setClosePayment(boolean closePayment) {
        this.closePayment = closePayment;
    }

    private boolean closeTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("closeTicket");
        boolean resultok = false;
//          int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
////         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        if (totalBillValue == 0 && m_oTicket.getTotal() != 0) {
            showMessage(this, "Please enter the tender types");
            setClosePayment(false);
        } else if (totalBillValue < m_oTicket.getTotal()) {
            System.out.println(totalBillValue + "totalBillValue" + m_oTicket.getTotal() + "m_oTicket.getTotal()");
            showMessage(this, "Entered tender amount should be equal to total sale amount");
            setClosePayment(false);
        } else {
            //  if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {  
            try {
                //            try {
                // reset the payment info
                //  chargeslogic.calculateCharges(m_oTicket);
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                logger.info("Order No." + m_oTicket.getOrderId() + " exception while calculateTaxes in closeticket " + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
//                if (ticket.getTotal()>=0.0){
//                    ticket.resetPayments(); //Only reset if is sale
//                }
            //     setTenderAmount();

            if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                //    if()
                double creditAmt;
                //  try{
                //      creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                //  }catch(Exception ex){
                creditAmt = 0;
                //   }

                // if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
                //          showMessage(this, "Please select the customer");
                //  }else{


//                    if(m_jCreditAmount.getText().equals("")){
                creditAmount = 0;
                //   }else{
                //       creditAmount = Double.parseDouble(m_jCreditAmount.getText());
                //     }

                //   String

                //  paymentDetail();
                // assign the payments selected and calculate taxes.         


                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date()); // Le pongo la fecha de cobro
                ticket.setPayments(m_aPaymentInfo.getPayments());

                if (!jTextPhoneNo.getText().equals("")) {
                    logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in closeticket method ;");
                    m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                    MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, true, dlSales);
                }


                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                    //String[] ticketDocNoValue;
                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument = null;
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


//                      try{
//                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
//                        String[] ticketDocNoValue = ticketDocNo.split("-");
//                            ticketDocNo=ticketDocNoValue[2];
//                        }catch(NullPointerException ex){
//                            ticketDocNo = "10000";
//                        }
//                        if(ticketDocNo!=null){
//                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                            ticketDocNoInt = ticketDocNoInt + 1;
//
//                        }
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
                    }
//                         else{
//                                ticketDocument = "0";
//                         }
                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {

                        getCreditDate();
                    }


                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";
                    String cod;
                    double advanceissued;
                    String isCredit;
                    String isPaidStatus;
                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    cod = "N";
                    isPaidStatus = "Y";
                    if (creditAmt > 0) {
                        isCredit = "Y";
                    } else {
                        isCredit = "N";
                    }

                    advanceissued = 0;
                    String file;
                    file = "Printer.Bill";
                    saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                    logger.info("bill has been settled");
                    resultok = true;
                }
//                       setClosePayment(true);
//                       if(!ticket.getSplitValue().equals("Split")){
//                            logger.info("deleting splitted settled bill ");    
//                            m_ticketsbag.deleteTicket();
//                       }else{
//                          logger.info("deleting  settled bill ");     
//                         dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//                      }
            }
            //  }

        }
        return resultok;
    }

//    private boolean closeTicketHomeDelivery(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
//        System.out.println("closeTicketHomeDelivery");
//
//        boolean resultok = false;
//
//        //   if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
//        try {
//            //            try {
//            // reset the payment info
//            // chargeslogic.calculateCharges(m_oTicket);
//            taxeslogic.calculateTaxes(ticket);
//        } catch (TaxesException ex) {
//            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (ticket.getTotal() >= 0.0) {
//            ticket.resetPayments(); //Only reset if is sale
//        }
//        // setTenderAmount();
//
//        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
//            //    if()
//            double creditAmt;
//            try {
//                creditAmt = m_oTicket.getTotal();
//            } catch (Exception ex) {
//                creditAmt = 0;
//            }
//
//            if ((cusName.getText().equals("") || cusName.getText() == null)) {
//                showMessage(this, "Please select the customer");
//            } else {
////
//                //   ticket.setPayments(m_aPaymentInfo.getPayments());
//
//                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
//                ticket.setActiveCash(m_App.getActiveCashIndex());
//                ticket.setDate(new Date()); // Le pongo la fecha de cobro
//
//                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
//                    //String[] ticketDocNoValue;
//                    String ticketDocNo = null;
//                    Integer ticketDocNoInt = null;
//                    String ticketDocument;
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
//
//
//                        try {
//                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
//                            String[] ticketDocNoValue = ticketDocNo.split("-");
//                            ticketDocNo = ticketDocNoValue[2];
//                        } catch (NullPointerException ex) {
//                            ticketDocNo = "10000";
//                        }
//                        if (ticketDocNo != null) {
//                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                            ticketDocNoInt = ticketDocNoInt + 1;
//
//                        }
//                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
//                    } else {
//                        ticketDocument = "0";
//                    }
//                    m_oTicket.setDocumentNo(ticketDocument);
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        getCreditDate();
//                    }
//                    String chequeNos = "";//m_jtxtChequeNo.getText();
//                    String deliveryBoy = "";
//                    double advanceissued;
//
//                    //   int selectedindex = m_jDeliveryBoy.getSelectedIndex();
//                    //     if(selectedindex!=0){
//                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
//                    //  }
//
//                    //if(m_jTxtAdvance.getText().equals("")){
//                    advanceissued = 0;
//                    //  }else{
//                    //      advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
//                    //   }
//
//                    String file;
//                    file = "Printer.HomeDelivery";
//
//                    if (m_oTicket.getLinesCount() == 0) {
//                        showMessage(this, "Please select the products");
//                    } else {
//                        saveHomeReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "N");
//
//                        //   m_jCreditAllowed.setVisible(false);
//                        //   m_jCreditAllowed.setSelected(false);
//                        resultok = true;
//                    }
//                }
//                if (!m_oTicket.getSplitValue().equals("Split")) {
//                      logger.info("Order No." + m_oTicket.getOrderId() + " closeticket homedelivery deleting the  "+m_oTicket.getTableName() +" id is "+m_oTicket.getPlaceId());
//                   dlReceipts.deleteSharedTicket(m_oTicket.getPlaceId());
//                      m_ticketsbag.deleteTicket();
//                } else {
//                    // m_ticketsbag.deleteTicket(m_oTicket.getSplitSharedId());
//                }
//            }
//
//        }
//        // }
//        //  }
//
//        return resultok;
//    }
    private boolean closeTicketNonChargable(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketNonChargable");

        boolean resultok = false;

        //   if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info
            // chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        // setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            //    if()
            double creditAmt;
            try {
                creditAmt = m_oTicket.getTotal();
            } catch (Exception ex) {
                creditAmt = 0;
            }
            if ((cusName.getText().equals(""))) {
                showMessage(this, "Please select the customer");
            } else {
                //   ticket.setPayments(m_aPaymentInfo.getPayments());

                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date()); // Le pongo la fecha de cobro

                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                    //String[] ticketDocNoValue;
                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument;
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


                        try {
                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                            String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo = ticketDocNoValue[2];
                        } catch (NullPointerException ex) {
                            ticketDocNo = "10000";
                        }
                        if (ticketDocNo != null) {
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
                    } else {
                        ticketDocument = "0";
                    }
                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        getCreditDate();
                    }
                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";
                    double advanceissued;

                    //      int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                    // if(selectedindex!=0){
                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    //  }

                    //if(m_jTxtAdvance.getText().equals("")){
                    advanceissued = 0;
                    // }else{
                    //     advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                    // }
                    ticket.setDocumentNo(ticketDocument);
                    String file;
                    file = "Printer.NonChargableBill";

                    if (m_oTicket.getLinesCount() == 0) {
                        showMessage(this, "Please select the products");
                    } else {
                        saveNonChargableReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "Y");

                        //m_jCreditAllowed.setVisible(false);
                        //  m_jCreditAllowed.setSelected(false);
                        resultok = true;
                    }
                }

            }

        }
        // }
        //  }

        return resultok;
    }

    private boolean closeTicketWithButton(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("enter the closeTicketWithButton method" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        boolean resultok = false;
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            //  if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
            try {
                //            try {
                // reset the payment info9
                //  chargeslogic.calculateCharges(m_oTicket);
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                logger.info("exception while calculateTaxes in closeTicketWithButton " + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (ticket.getTotal() >= 0.0) {
                ticket.resetPayments(); //Only reset if is sale
            }
            //  setTenderAmount();

            if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                double creditAmt;
                //       try{
                //       creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                //      }catch(Exception ex){
                creditAmt = 0;
                //     }

                //  / if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
                //       showMessage(this, "Please select the customer");
                //  }else{
                ticket.setPayments(m_aPaymentInfo.getPayments());

                if (!jTextPhoneNo.getText().equals("")) {
                    logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in closeticketwithbutton method ;");
                    m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                    MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, true, dlSales);
                }

                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date()); // Le pongo la fecha de cobro

                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                    //String[] ticketDocNoValue;
                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument = null;
                    if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {

                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
                    }

                    ticket.setDocumentNo(ticketDocument);
                    if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        getCreditDate();
                    }
                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";

                    String homeDelivery;
                    String orderTaking;
                    String cod;
                    String isPaidStatus;
                    homeDelivery = "N";
                    orderTaking = "N";
                    cod = "N";
                    isPaidStatus = "Y";
                    String isCredit;
                    double advanceissued;
                    deliveryBoy = ""; //deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    if (creditAmt > 0) {
                        isCredit = "Y";
                    } else {
                        isCredit = "N";
                    }

                    advanceissued = 0;
                    String file;
                    if (ticket.getPrintStatus() == 1) {
                        file = "Printer.Bill";
                        if (ticket.getLinesCount() == 0) {
                            showMessage(this, "Please select the products");
                        } else {
                            logger.info("Before saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                            saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                            logger.info("After saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                            resultok = true;
                        }
                    }
//                       else if(ticket.getPrintStatus()==2){
//                           file = "Printer.Ticket";
//                           if(ticket.getLinesCount()==0){
//                               showMessage(this, "Please select the products");
//                           }else{
//                                  logger.info("Before saveReceipt if ticket.getPrintStatus()==2"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                                  dlSales.saveDraftTicket(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,homeDelivery,cod,advanceissued,creditAmount,"N",orderTaking);
//                                  saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"N",file,isCredit,"N");
//                                  logger.info("After saveReceipt if ticket.getPrintStatus()==2"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                                 resultok = true;
//                           }
//                                      
//                       }

                }

//                      if(!ticket.getSplitValue().equals("Split")){
//                           logger.info("deleting splitted settled bill");
//                            m_ticketsbag.deleteTicket();
//                       }else{
//                         logger.info("deleting settled bill");
//                         dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//                      }



            }
            //  }
            logger.info("End of closeticketButton" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        }
        return resultok;
    }

    private boolean closeCreditTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {

        boolean resultok = false;

//        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info
            //   chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            int inventoyCount = 0;
            try {
                inventoyCount = dlSales.getStopInventoryCount();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inventoyCount == 1) {
                showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
            } else {
                double creditAmt;
                //    try{
                ///     creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                // }catch(Exception ex){
                creditAmt = 0;
                // }

                if ((creditAmt != 0 && cusName.getText().equals(""))) {
                    showMessage(this, "Please select the customer");
                } else if (!cusName.getText().equals("") && creditAmt != 0 && customerList.get(0).getIsCreditCustomer() == 0) {
                    showMessage(this, "Please select the credit customer");
                } else {

                    ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                    ticket.setActiveCash(m_App.getActiveCashIndex());
                    ticket.setDate(new Date()); // Le pongo la fecha de cobro

                    if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                        //String[] ticketDocNoValue;
                        String ticketDocNo = null;
                        Integer ticketDocNoInt = null;
                        String ticketDocument;
                        if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


                            try {
                                ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                                String[] ticketDocNoValue = ticketDocNo.split("-");
                                ticketDocNo = ticketDocNoValue[2];
                            } catch (NullPointerException ex) {
                                ticketDocNo = "10000";
                            }
                            if (ticketDocNo != null) {
                                ticketDocNoInt = Integer.parseInt(ticketDocNo);
                                ticketDocNoInt = ticketDocNoInt + 1;

                            }
                            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
                        } else {
                            ticketDocument = "0";
                        }
                        m_oTicket.setDocumentNo(ticketDocument);
                        String chequeNos = "";///m_jtxtChequeNo.getText();
                        String deliveryBoy = "";
                        String homeDelivery;
                        String isCredit;
                        // if(m_jHomeDelivery.isSelected()){
                        homeDelivery = "Y";
                        ///  }else{
                        //      homeDelivery = "N";
                        //  }
                        if (creditAmt > 0) {
                            isCredit = "Y";
                        } else {
                            isCredit = "N";
                        }

                        String file;
                        file = "Printer.Bill";

                        if (m_oTicket.getLinesCount() == 0) {
                            showMessage(this, "Please select the products");
                        } else {
                            saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, "", 0, creditAmt, ticketext, "Y", file, isCredit, "N");
                            //     m_jCreditAllowed.setVisible(false);
                            //     m_jCreditAllowed.setSelected(false);
                            resultok = true;

                        }
                    }
                }
            }
        }
        //   }
        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }

    public void saveReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;

        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        logger.info("inside save receipt");
        try {
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
            logger.info("settled successfully");
            m_jTxtTotalPaid.setText("");
            m_oTicket.setRate("0");
            m_oTicket.setdAmt(0);
            taxModel.removeAllElements();
            populateDeliveryBoy();
            m_jTxtChange.setText("");
            m_oTicket.resetCharges();
            m_oTicket.resetTaxes();
            m_oTicket.resetPayments();
            m_jServiceCharge.setText("");
            activate();
            logger.info("After activate method");
            setClosePayment(true);
            if (!ticket.getSplitValue().equals("Split")) {
                if (reservationStatus.equals("true")) {
                    ReservationStatus.Reservation(ticket.getTableName(), "2", m_App);
                }
                ticket.getAccessInfo().add(new AccessInfo(m_App.getProperties().getPosNo(), m_App.getAppUserView().getUser().getName(), new Date(), "exit on settle"));
                m_ticketsbag.deleteTicket();
                //  logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after settle bill of table "+ticket.getTableName()+" id is "+ticket.getPlaceId());
                //dlReceipts.deleteSharedTicket(ticket.getPlaceId());

            }
//            else {
//                 logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after splitted settle bill of table "+ticket.getTableName()+" id is "+ticket.getPlaceId());
//             dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//            }
        } catch (Exception ex) {
            logger.info("Order No." + ticket.getOrderId() + " exception while calling saveRetailTicket " + ex.getMessage());
            ex.printStackTrace();
            showMessage(this, "Settlement could not be completed. Please try settling the bill again");
        }
        logger.info("After save receipt completed ");
        //executes only in edit sales
//        if (getEditSale() == "Edit") {
//            RetailTicketInfo editTicket = null;
//            try {
//                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
//            } catch (BasicException ex) {
//                logger.info("exception while edit ticket" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
//            } catch (BasicException ex) {
//                logger.info("exception while deleting ticket in savereceipt method" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//till here editsales
        setEditSale("");
//                try {
//                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//                 } catch (BasicException ex) {
//                     logger.info("exception while calling insertRetailTicket "+ex.getMessage());
//                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            String printrequired=m_App.getProperties().getProperty("machine.printrequired");
//             if(printrequired.equals("true")){
//                 printTicket(file, ticket, ticketext);
//              }
//                 m_jTxtTotalPaid.setText("");
//                 m_oTicket.setRate("0");
//                 m_oTicket.setdAmt(0);
//                 taxModel.removeAllElements();
//                 populateDeliveryBoy();
//                 m_jTxtChange.setText("");
//                 m_oTicket.resetTaxes();
//                 m_oTicket.resetPayments();
//                 m_jServiceCharge.setText("");
//                activate();
//                logger.info("After activate method");

    }
//
//    public void saveHomeReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
//        //    System.out.println("ticketext---"+ticketext.toString());
//        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
//        String homeDelivery;
//        String orderTaking;
//        String cod;
//        String isPaidStatus;
//
//        homeDelivery = "Y";
//        orderTaking = "Y";
//        cod = "N";
//        isPaidStatus = "Y";
//        // }
//        try {
//            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
//        } catch (Exception ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
////        if (getEditSale() == "Edit") {
////            RetailTicketInfo editTicket = null;
////            try {
////                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
////            } catch (BasicException ex) {
////                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            try {
////                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
////            } catch (BasicException ex) {
////                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////            }
////        }
//
//        setEditSale("");
//        try {
//            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //     if(getPrinterStatus()==true){
//        ///  printTicket(file, ticket, ticketext);
//        //    }
////                resultok = true;
//        //   m_jCreditAllowed.setVisible(false);
//        //    m_jCreditAllowed.setSelected(false);
////                 m_jCash.setText("");
//        //          m_jCard.setText("");
//        //          m_jCheque.setText("");
//        //          m_jFoodCoupon.setText("");
//        //          m_jVoucher.setText("");
//        m_jTxtTotalPaid.setText("");
//        m_oTicket.setRate("0");
//        m_oTicket.setdAmt(0);
//        //   m_jTxtCustomerId.setText("");
//        //    m_jHomeDelivery.setSelected(false);
//        //    m_jDeliveryBoy.removeAllItems();
//        //        m_jCreditAmount.setText("0.00");
//        populateDeliveryBoy();
//        //     m_jDeliveryBoy.setSelectedIndex(0);
//        //     m_jTxtAdvance.setText("");
//        //     m_jCod.setSelected(false);
//        //     m_jCreditAmount.setText("0.00");
//        m_jTxtChange.setText("");
//        //   jLabel10.setVisible(false);
////                 m_jDeliveryBoy.setVisible(false);
//        //     jLabel11.setVisible(false);
//        //    m_jTxtAdvance.setVisible(false);
//        //     m_jCod.setVisible(false);
//        m_oTicket.resetCharges();
//        m_oTicket.resetTaxes();
//        m_oTicket.resetPayments();
//        //      m_jTxtTips.setText("0.00");
//        m_jServiceCharge.setText("");
//
//        activate();
//
//    }

    public void saveNonChargableReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        //    System.out.println("ticketext---"+ticketext.toString());
        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;


        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        // }
        try {
            dlSales.saveNonChargableTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

//        if (getEditSale() == "Edit") {
//            RetailTicketInfo editTicket = null;
//            try {
//                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        setEditSale("");
        try {
            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        //     if(getPrinterStatus()==true){
        printTicket(file, ticket, ticketext);
        //    }
//                resultok = true;
        //    m_jCreditAllowed.setVisible(false);
        //  m_jCreditAllowed.setSelected(false);
        // m_jCash.setText("");
        //  m_jCard.setText("");
        //   m_jCheque.setText("");
        //   m_jFoodCoupon.setText("");
        //   m_jVoucher.setText("");
        m_jTxtTotalPaid.setText("");
        m_oTicket.setRate("0");
        m_oTicket.setdAmt(0);
        //   m_jTxtCustomerId.setText("");
        //  m_jHomeDelivery.setSelected(false);
        //  m_jDeliveryBoy.removeAllItems();
        //   m_jCreditAmount.setText("0.00");
        populateDeliveryBoy();
        //   m_jDeliveryBoy.setSelectedIndex(0);
        //    m_jTxtAdvance.setText("");
        //  m_jCod.setSelected(false);
        //  m_jCreditAmount.setText("0.00");
        //   m_jTxtChange.setText("");
        //    jLabel10.setVisible(false);
        //    m_jDeliveryBoy.setVisible(false);
        //    jLabel11.setVisible(false);
        //   m_jTxtAdvance.setVisible(false);
        //   m_jCod.setVisible(false);
        m_oTicket.resetCharges();
        m_oTicket.resetTaxes();
        m_oTicket.resetPayments();
        //  m_jTxtTips.setText("0.00");
        m_jServiceCharge.setText("");

        activate();

    }

//    public void saveNotReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status) {
//        double tipsAmt = 0;// Double.parseDouble(m_jTxtTips.getText());
//        String homeDelivery;
//        String orderTaking;
//        String cod;
//        String isPaidStatus;
//
////        if(getHomeDeliverySale().equals("HomeDelivery")){
////             if(m_jHomeDelivery.isSelected()){
////                 homeDelivery = "Y";
////                 orderTaking = "N";
////                  if(m_jCod.isSelected()){
////                    cod = "Y";
////                    isPaidStatus = "N";
////                  }else{
////                    cod = "N";
////                    isPaidStatus = "Y";
////                  }
////
////             }else{
////                 homeDelivery = "N";
////                 orderTaking = "Y";
////                 cod = "N";
////                 isPaidStatus = "Y";
////             }
////        }else{
//        homeDelivery = "N";
//        orderTaking = "N";
//        cod = "N";
//        isPaidStatus = "Y";
//        //  }
//        try {
//            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, "N", "Y", tipsAmt, orderTaking, "N");
//        } catch (Exception eData) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
//            msg.show(this);
//        }
//        try {
//            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //      m_jCreditAllowed.setVisible(false);
//        ///     m_jCreditAllowed.setSelected(false);
////                 m_jCash.setText("");
//        m_jTxtChange.setText("");
//        //    m_jCard.setText("");
//        //   m_jCheque.setText("");
//        //    m_jFoodCoupon.setText("");
//        //    m_jVoucher.setText("");
//        m_jTxtTotalPaid.setText("");
//        m_oTicket.setRate("0");
//        m_oTicket.setdAmt(0);
//        //   m_jHomeDelivery.setSelected(false);
//        //   m_jDeliveryBoy.removeAllItems();
//        populateDeliveryBoy();
//        //    m_jDeliveryBoy.setSelectedIndex(0);
//        //    m_jTxtAdvance.setText("");
//        //   m_jCod.setSelected(false);
//        //   m_jCod.setVisible(false);
//        m_oTicket.resetCharges();
//        m_oTicket.resetTaxes();
//        m_oTicket.resetPayments();
//        //    m_jTxtCustomerId.setText("");
////                 m_jCreditAmount.setText("0.00");
//        activate();
//
//    }
    public void clearTxtField() {
        //    cusName.setText("");
        //    cusPhoneNo.setText("");
//       itemName.setText("");
        //    m_jCash.setText("");
        //     m_jCard.setText("");
        //     m_jCheque.setText("");
        //    m_jFoodCoupon.setText("");
        //    m_jVoucher.setText("");
        //     m_jtxtChequeNo.setText("");
        //  m_jHomeDelivery.setSelected(false);
        //    m_jCod.setSelected(false);
        //   m_jCreditAmount.setText("0.00");
        //   m_jCod.setVisible(false);
        //  m_jCreditAllowed.setVisible(false);
        //   m_jCreditAllowed.setSelected(false);
    }

    public void clearFocus(boolean value) {

        //cusName.setFocusable(value);
        //  cusPhoneNo.setFocusable(value);
//       m_jTxtItemCode.setFocusable(value);
        //   m_jTxtCustomerId.setFocusable(value);
        m_jKeyFactory.setFocusable(true);
        m_jKeyFactory.setText(null);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_jKeyFactory.requestFocus();
            }
        });
//       m_jPrice.setEnabled(true);
//       itemName.setFocusable(value);
//       m_jCash.setFocusable(value);
        //     m_jCard.setFocusable(value);
        //      m_jCheque.setFocusable(value);
        //      m_jtxtChequeNo.setFocusable(value);
        //      m_jFoodCoupon.setFocusable(value);
        //      m_jCreditAmount.setFocusable(value);
        //     m_jVoucher.setFocusable(value);
//       m_jTxtItemCode.setText("");
//         itemName.setText("");

    }

    private String getDottedLine(int len) {
        String dotLine = "";
        for (int i = 0; i < len; i++) {
            dotLine = dotLine + "-";
        }
        return dotLine;
    }

    private String getSpaces(int len) {
        String spaces = "";
        for (int i = 0; i < len; i++) {
            spaces = spaces + " ";
        }
        return spaces;
    }

    private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket, Object ticketext) {


        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
        allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
        allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (cusName.getText())));

        allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ticketext));

        allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price" + getSpaces(9) + "Value(INR)"));
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue + getSpaces(7 - qty.length() + 12 - subValue.length()) + total));
        }
        String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
        String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
        String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
        String discount = Formats.DoubleValue.formatValue(ticket.getTotalDiscount());
        String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
        String total = Formats.DoubleValue.formatValue(ticket.getTotal());
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total " + getSpaces(29 - subTotal.length()) + (subTotal)));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Discount " + getSpaces(25 - discount.length()) + ("-" + discount)));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total After Discount " + getSpaces(14 - totalAfrDiscount.length()) + (totalAfrDiscount)));

        if (ticket.getTaxes().size() != 0) {
            for (int i = 0; i < ticket.getTaxes().size(); i++) {
                System.out.println("ticket.getTaxes().get(i).getTax()--" + ticket.getTaxes().get(i).getTax());
                if (ticket.getTaxes().get(i).getTax() != 0.00) {
                    allLines.add(new TicketLineConstructor(getSpaces(33) + ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(25 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()))));
                }
            }

        }

        String aCount = ticket.printTicketCount();
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Charge 6%" + getSpaces(18 - serviceCharge.length()) + serviceCharge));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Tax 4.94%" + getSpaces(18 - serviceTax.length()) + serviceTax));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Grand Total : " + getSpaces(21 - total.length()) + (total)));

        return allLines;
    }

    private java.util.List<TicketLineConstructor> getNonChargeableLines(RetailTicketInfo ticket, Object ticketext) {
        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
        allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
        allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (cusName.getText())));
        allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ticketext));
        allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));

        // }
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price" + getSpaces(9) + "Value(INR)"));
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = "0.00";
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue + getSpaces(9 - qty.length() + 11 - subValue.length()) + total));
        }
        System.out.println("ticket.getTaxes().get(0).getTaxInfo().getName();" + ticket.getTaxes().get(0).getTaxInfo().getName());
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total " + getSpaces(25) + "0.00"));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Discount " + getSpaces(22) + "0.00"));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total After Discount " + getSpaces(10) + "0.00"));
        if (ticket.getTaxes().size() != 0) {
            for (int i = 0; i < ticket.getTaxes().size(); i++) {
                if (ticket.getTaxes().get(i).getTax() != 0.00) {
                    allLines.add(new TicketLineConstructor(getSpaces(33) + ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(31 - ticket.getTaxes().get(i).getTaxInfo().getName().length()) + "0.00"));
                }
            }

        }
        String aCount = ticket.printTicketCount();
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Charge 6%" + getSpaces(14) + "0.00"));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Tax 4.94%" + getSpaces(14) + "0.00"));



        allLines.add(new TicketLineConstructor(getSpaces(33) + "Grand Total : " + getSpaces(17) + "0.00"));

        //allLines.add(new TicketLineConstructor(announce3));
        return allLines;
    }

    private void addlinesaddBlankLines(int count, java.util.List<TicketLineConstructor> allLines) {
        for (int i = 0; i < count; i++) {
            allLines.add(new TicketLineConstructor(""));
        }
    }

    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
        String sresource = dlSystem.getResourceAsXML(sresourcename);

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("charges", chargecollection);
                script.put("taxeslogic", taxeslogic);
                script.put("chargeslogic", chargeslogic);
                script.put("staxeslogic", staxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            }
        }
    }

    private void printReport(String resourcefile, RetailTicketInfo ticket, Object ticketext) {

        try {

            JasperReport jr;

            InputStream in = getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
                jr = JasperCompileManager.compileReport(jd);
            } else {
                // read the compiled reporte
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }

            // Construyo el mapa de los parametros.
            Map reportparams = new HashMap();
            // reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }
            reportparams.put("TAXESLOGIC", taxeslogic);

            Map reportfields = new HashMap();
            reportfields.put("TICKET", ticket);
            reportfields.put("PLACE", ticketext);

            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[]{reportfields}));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printername"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
    }

    public void visorTicketLine(RetailTicketLineInfo oLine) {
        if (oLine == null) {
            m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelTicket.this);
            }
        }
    }

    private void showMessage(JRetailPanelTicket aThis, String msg) {
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

    private Object evalScript(ScriptObject scr, String resource, ScriptArg... args) {

        // resource here is guaratied to be not null
        try {
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            return scr.evalScript(dlSystem.getResourceAsXML(resource), args);
        } catch (ScriptException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e);
            msg.show(this);
            return msg;
        }
    }

    public void evalScriptAndRefresh(String resource, ScriptArg... args) {

        if (resource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
            msg.show(this);
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            evalScript(scr, resource, args);
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
        }
    }

    public int getServedStatus() {
        return served;
    }

    public void setServedStatus(int served) {
        this.served = served;
    }

    public void printTicket(String resource) {
        printTicket(resource, m_oTicket, m_oTicketExt);
    }

    public Object executeEventAndRefresh(String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            Object result = evalScript(scr, resource, args);
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }

    public Object executeEventAndRefreshForKot(String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            Object result = evalScript(scr, resource, args);
            setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }

    private Object executeEvent(RetailTicketInfo ticket, Object ticketext, String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(ticket, ticketext);
            return evalScript(scr, resource, args);
        }
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getResourceAsXML(String sresourcename) {
        return dlSystem.getResourceAsXML(sresourcename);
    }

    public BufferedImage getResourceAsImage(String sresourcename) {
        return dlSystem.getResourceAsImage(sresourcename);
    }

    public void setSelectedIndex(int i) {

        if (i >= 0 && i < m_oTicket.getLinesCount()) {
            m_ticketlines.setSelectedIndex(i);
        } else if (m_oTicket.getLinesCount() > 0) {
            m_ticketlines.setSelectedIndex(m_oTicket.getLinesCount() - 1);
        }
    }

    public static class ScriptArg {

        private String key;
        private Object value;

        public ScriptArg(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }

    public class ScriptObject {

        private RetailTicketInfo ticket;
        private Object ticketext;
        private int selectedindex;

        private ScriptObject(RetailTicketInfo ticket, Object ticketext) {
            this.ticket = ticket;
            this.ticketext = ticketext;
        }

        public double getInputValue() {
            if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                return JRetailPanelTicket.this.getInputValue();
            } else {
                return 0.0;
            }
        }

        public int getSelectedIndex() {
            return selectedindex;
        }

        public void setSelectedIndex(int i) {
            selectedindex = i;
        }

        public void printReport(String resourcefile) {
            JRetailPanelTicket.this.printReport(resourcefile, ticket, ticketext);
        }

        public void printTicket(String sresourcename) {
            JRetailPanelTicket.this.printTicket(sresourcename, ticket, ticketext);
        }

        public Object evalScript(String code, ScriptArg... args) throws ScriptException {

            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
            script.put("ticket", ticket);
            script.put("place", ticketext);
            script.put("taxes", taxcollection);
            script.put("taxeslogic", taxeslogic);
            script.put("user", m_App.getAppUserView().getUser());
            script.put("sales", this);

            // more arguments
            for (ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }

            return script.eval(code);
        }
    }

    private void populateDeliveryBoy() {
//        try {
//            deliveryBoyLines = dlCustomers.getDeliveryBoyList();
//            m_jDeliveryBoy.addItem("");
//            for (int i = 0; i < deliveryBoyLines.size(); i++) {
//                m_jDeliveryBoy.addItem(deliveryBoyLines.get(i).getName());
//            }
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * This method is called from within the constructor to Tinitialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jPanelScripts = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jLogout = new javax.swing.JButton();
        m_jbtnPrintBill = new javax.swing.JButton();
        m_jSettleBill = new javax.swing.JButton();
        m_jSplitBtn = new javax.swing.JButton();
        m_jBtnDiscount = new javax.swing.JButton();
        m_jBtnCancelBill = new javax.swing.JButton();
        m_jBtnCatDiscount = new javax.swing.JButton();
        jButtonRemarks = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jLblUserInfo = new javax.swing.JLabel();
        m_jUser = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        m_jTable = new javax.swing.JLabel();
        m_jLblCurrentDate = new javax.swing.JLabel();
        m_jLblTime = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        m_jLblBillNo = new javax.swing.JLabel();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddon = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabelCustPhone = new javax.swing.JLabel();
        jTextPhoneNo = new javax.swing.JTextField();
        jMoveTableText = new javax.swing.JLabel();
        jCheckServiceCharge = new javax.swing.JCheckBox();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jPlus = new javax.swing.JButton();
        m_jMinus = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jAction = new javax.swing.JButton();
        m_jEraser = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        jButtonRepeatLines = new javax.swing.JButton();
        m_jBtnServed = new javax.swing.JButton();
        m_jBtnKot = new javax.swing.JButton();
        m_jCalculatePromotion = new javax.swing.JButton();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        catcontainer = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jTxtTotalPaid = new javax.swing.JLabel();
        m_jTxtChange = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        m_jTax = new javax.swing.JComboBox();
        m_jPor = new javax.swing.JLabel();
        m_jaddtax = new javax.swing.JToggleButton();
        jLblPrinterStatus = new javax.swing.JLabel();
        m_jKeyFactory = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jSubtotalEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros4 = new javax.swing.JLabel();
        m_jTaxesEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros5 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        m_jLblTotalEuros6 = new javax.swing.JLabel();
        m_jDiscount1 = new javax.swing.JLabel();
        m_jTotalEuros = new javax.swing.JLabel();
        jTaxPanel = new javax.swing.JPanel();
        jLabelServiceTax = new javax.swing.JLabel();
        m_jServiceTax = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTaxList = new javax.swing.JList();
        jLabelServiceCharge = new javax.swing.JLabel();
        m_jServiceCharge = new javax.swing.JLabel();
        m_jProducts = new javax.swing.JPanel();

        setBackground(new java.awt.Color(222, 232, 231));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setBackground(new java.awt.Color(222, 232, 231));
        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jButtons.setBackground(new java.awt.Color(222, 232, 231));
        m_jButtons.setPreferredSize(new java.awt.Dimension(4, 10));
        m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

        m_jPanelScripts.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanelScripts.setPreferredSize(new java.awt.Dimension(768, 47));
        m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setBackground(new java.awt.Color(222, 232, 231));
        m_jButtonsExt.setPreferredSize(new java.awt.Dimension(750, 47));
        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(222, 232, 231));
        jPanel1.setPreferredSize(new java.awt.Dimension(630, 47));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jbtnScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        m_jbtnScale.setText(AppLocal.getIntString("button.scale")); // NOI18N
        m_jbtnScale.setFocusPainted(false);
        m_jbtnScale.setFocusable(false);
        m_jbtnScale.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnScale.setRequestFocusEnabled(false);
        m_jbtnScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnScaleActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnScale, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 5, -1));

        m_jLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1logout.png"))); // NOI18N
        m_jLogout.setToolTipText("Logout");
        m_jLogout.setFocusable(false);
        m_jLogout.setPreferredSize(new java.awt.Dimension(40, 40));
        m_jLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(m_jLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, -1, -1));

        m_jbtnPrintBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Print-Bill.png"))); // NOI18N
        m_jbtnPrintBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jbtnPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnPrintBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, -1, -1));

        m_jSettleBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Settle-Bill.png"))); // NOI18N
        m_jSettleBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jSettleBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSettleBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jSettleBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 0, -1, -1));

        m_jSplitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/190-40-BUTTON.png"))); // NOI18N
        m_jSplitBtn.setMnemonic('f');
        m_jSplitBtn.setFocusPainted(false);
        m_jSplitBtn.setFocusable(false);
        m_jSplitBtn.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jSplitBtn.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jSplitBtn.setRequestFocusEnabled(false);
        m_jSplitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSplitBtnActionPerformed(evt);
            }
        });
        jPanel1.add(m_jSplitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        m_jBtnDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/bill-discount.png"))); // NOI18N
        m_jBtnDiscount.setMnemonic('i');
        m_jBtnDiscount.setToolTipText("Add Discount");
        m_jBtnDiscount.setFocusPainted(false);
        m_jBtnDiscount.setFocusable(false);
        m_jBtnDiscount.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnDiscount.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnDiscount.setRequestFocusEnabled(false);
        m_jBtnDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnDiscountActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 0, -1, -1));

        m_jBtnCancelBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/can-bill.png"))); // NOI18N
        m_jBtnCancelBill.setToolTipText("Cancel the Bill");
        m_jBtnCancelBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCancelBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancelBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnCancelBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 0, -1, -1));

        m_jBtnCatDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/bill-discount.png"))); // NOI18N
        m_jBtnCatDiscount.setMnemonic('i');
        m_jBtnCatDiscount.setToolTipText("Add Discount");
        m_jBtnCatDiscount.setFocusPainted(false);
        m_jBtnCatDiscount.setFocusable(false);
        m_jBtnCatDiscount.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnCatDiscount.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCatDiscount.setRequestFocusEnabled(false);
        m_jBtnCatDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCatDiscountActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnCatDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 0, -1, -1));

        jButtonRemarks.setText("Remarks");
        jButtonRemarks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRemarks.setPreferredSize(new java.awt.Dimension(90, 40));
        jButtonRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonRemarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 100, -1));

        m_jButtonsExt.add(jPanel1);

        m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

        m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

        m_jPanelBag.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanelBag.setFocusable(false);
        m_jPanelBag.setPreferredSize(new java.awt.Dimension(800, 35));
        m_jPanelBag.setRequestFocusEnabled(false);
        m_jPanelBag.setLayout(new java.awt.BorderLayout());
        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(80, 102, 116));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setPreferredSize(new java.awt.Dimension(1024, 90));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(222, 232, 231));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1header.png"))); // NOI18N
        jLabel3.setAutoscrolls(true);
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel3.setMaximumSize(new java.awt.Dimension(1450, 61));
        jLabel3.setMinimumSize(new java.awt.Dimension(1024, 61));
        jLabel3.setPreferredSize(new java.awt.Dimension(1024, 45));
        jLabel3.setRequestFocusEnabled(false);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1551, -1));

        m_jLblUserInfo.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblUserInfo.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblUserInfo.setText("   LOGGED IN USER:");
        m_jLblUserInfo.setFocusable(false);
        m_jLblUserInfo.setPreferredSize(new java.awt.Dimension(340, 16));
        m_jLblUserInfo.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblUserInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 140, 50));

        m_jUser.setForeground(new java.awt.Color(252, 248, 0));
        m_jUser.setText("jLabel6");
        jPanel6.add(m_jUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 70, 50));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Table:");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 50, 50));

        m_jTable.setForeground(new java.awt.Color(252, 248, 0));
        m_jTable.setText("jLabel9");
        jPanel6.add(m_jTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 60, 50));

        m_jLblCurrentDate.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblCurrentDate.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblCurrentDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Date-icon.png"))); // NOI18N
        m_jLblCurrentDate.setFocusable(false);
        m_jLblCurrentDate.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblCurrentDate.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblCurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 45, 180, 45));

        m_jLblTime.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblTime.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Clock-icon.png"))); // NOI18N
        m_jLblTime.setText("                                                                                       jLabel2");
        m_jLblTime.setFocusable(false);
        m_jLblTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblTime.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 45, 230, 45));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bill No:");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 50, 50));

        m_jLblBillNo.setForeground(new java.awt.Color(252, 248, 0));
        jPanel6.add(m_jLblBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, 90, 50));

        m_jOptions.add(jPanel6, java.awt.BorderLayout.NORTH);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(222, 232, 231));
        jPanel10.setPreferredSize(new java.awt.Dimension(803, 48));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonAddon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Add-on.png"))); // NOI18N
        jButtonAddon.setPreferredSize(new java.awt.Dimension(90, 40));
        jButtonAddon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddonActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonAddon, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 90, 40));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Category.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 0, 90, 40));

        jLabelCustPhone.setText("Mobile No.");
        jPanel10.add(jLabelCustPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 80, 20));

        jTextPhoneNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextPhoneNoMomoeNumberPressed(evt);
            }
        });
        jTextPhoneNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPhoneNoActionPerformed(evt);
            }
        });
        jTextPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPhoneNoKeyReleased(evt);
            }
        });
        jPanel10.add(jTextPhoneNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 130, 20));

        jMoveTableText.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        jMoveTableText.setForeground(new java.awt.Color(16, 11, 253));
        jPanel10.add(jMoveTableText, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 390, 30));

        jCheckServiceCharge.setText("Exempt Servicecharge");
        jCheckServiceCharge.setPreferredSize(new java.awt.Dimension(50, 24));
        jCheckServiceCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckServiceChargeActionPerformed(evt);
            }
        });
        jPanel10.add(jCheckServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, -1));

        m_jPanTicket.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        m_jPanelCentral.setFocusable(false);
        m_jPanelCentral.setRequestFocusEnabled(false);
        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(222, 232, 231));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel2.setMinimumSize(new java.awt.Dimension(66, 338));
        jPanel2.setPreferredSize(new java.awt.Dimension(61, 400));

        m_jPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TPlus.png"))); // NOI18N
        m_jPlus.setToolTipText("Increase selected item quantity by one");
        m_jPlus.setFocusPainted(false);
        m_jPlus.setFocusable(false);
        m_jPlus.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jPlus.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jPlus.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jPlus.setRequestFocusEnabled(false);
        m_jPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPlusActionPerformed(evt);
            }
        });

        m_jMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TMinus.png"))); // NOI18N
        m_jMinus.setToolTipText("Decrease selected item's quantity by one");
        m_jMinus.setFocusPainted(false);
        m_jMinus.setFocusable(false);
        m_jMinus.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jMinus.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jMinus.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jMinus.setRequestFocusEnabled(false);
        m_jMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMinusActionPerformed(evt);
            }
        });

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TEdit.png"))); // NOI18N
        m_jEditLine.setMnemonic('e');
        m_jEditLine.setToolTipText("Edit Properties of selected item");
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });

        m_jAction.setBorder(null);
        m_jAction.setBorderPainted(false);
        m_jAction.setPreferredSize(new java.awt.Dimension(10, 2));
        m_jAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jActionActionPerformed(evt);
            }
        });

        m_jEraser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TKOT.png"))); // NOI18N
        m_jEraser.setToolTipText("Send All Fresh Items to Kitchen");
        m_jEraser.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jEraser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnKotActionPerformed(evt);
            }
        });

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Tclose.png"))); // NOI18N
        m_jDelete.setMnemonic('d');
        m_jDelete.setToolTipText("Remove total quantity of selected item");
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jDelete.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });

        jButtonRepeatLines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/RepeatOrder.png"))); // NOI18N
        jButtonRepeatLines.setToolTipText(" Repeat Items");
        jButtonRepeatLines.setPreferredSize(new java.awt.Dimension(91, 73));
        jButtonRepeatLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRepeatLinesActionPerformed(evt);
            }
        });

        m_jBtnServed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/servedsymbol.png"))); // NOI18N
        m_jBtnServed.setToolTipText("Serve Selected Item");
        m_jBtnServed.setPreferredSize(new java.awt.Dimension(91, 73));
        m_jBtnServed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnServedActionPerformed(evt);
            }
        });

        m_jBtnKot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TDelete.png"))); // NOI18N
        m_jBtnKot.setMnemonic('i');
        m_jBtnKot.setToolTipText("Clear All Non KOT Items");
        m_jBtnKot.setFocusPainted(false);
        m_jBtnKot.setFocusable(false);
        m_jBtnKot.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnKot.setRequestFocusEnabled(false);
        m_jBtnKot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEraserActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jBtnKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonRepeatLines, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jPlus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jMinus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jDelete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(112, 112, 112)
                        .add(m_jAction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jEditLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(m_jPlus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(m_jMinus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(m_jDelete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(m_jEditLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(61, 61, 61)
                        .add(m_jAction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1)
                        .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(2, 2, 2)
                .add(jButtonRepeatLines, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(m_jBtnKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jCalculatePromotion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/pramo.png"))); // NOI18N
        m_jCalculatePromotion.setMnemonic('f');
        m_jCalculatePromotion.setFocusPainted(false);
        m_jCalculatePromotion.setFocusable(false);
        m_jCalculatePromotion.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jCalculatePromotion.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jCalculatePromotion.setRequestFocusEnabled(false);
        m_jCalculatePromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCalculatePromotionActionPerformed(evt);
            }
        });
        jPanel5.add(m_jCalculatePromotion, java.awt.BorderLayout.CENTER);

        m_jPanelCentral.add(jPanel5, java.awt.BorderLayout.LINE_END);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jContEntries.setFocusable(false);
        m_jContEntries.setPreferredSize(new java.awt.Dimension(501, 500));
        m_jContEntries.setRequestFocusEnabled(false);
        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setMinimumSize(new java.awt.Dimension(508, 500));
        m_jPanEntries.setPreferredSize(new java.awt.Dimension(495, 525));
        m_jPanEntries.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        catcontainer.setPreferredSize(new java.awt.Dimension(300, 200));
        catcontainer.setLayout(new java.awt.BorderLayout());
        m_jPanEntries.add(catcontainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 508, 510));

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.PAGE_START);

        m_jPanTicket.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

        jPanel12.setBackground(new java.awt.Color(222, 232, 231));
        jPanel12.setFocusable(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(600, 5));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(300, 402));
        jPanel12.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -500, 989, 509));

        m_jPanTicket.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(222, 232, 231));
        jPanel4.setPreferredSize(new java.awt.Dimension(1024, 210));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel7.setMaximumSize(new java.awt.Dimension(700, 158));
        jPanel7.setPreferredSize(new java.awt.Dimension(320, 158));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("TOTAL PAID");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("CHANGE");

        m_jTxtTotalPaid.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jTxtTotalPaid.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTxtTotalPaid.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTxtTotalPaid.setFocusable(false);
        m_jTxtTotalPaid.setOpaque(true);
        m_jTxtTotalPaid.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTxtTotalPaid.setRequestFocusEnabled(false);

        m_jTxtChange.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jTxtChange.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTxtChange.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTxtChange.setFocusable(false);
        m_jTxtChange.setOpaque(true);
        m_jTxtChange.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTxtChange.setRequestFocusEnabled(false);

        jPanel3.setPreferredSize(new java.awt.Dimension(228, 100));

        m_jTax.setFocusable(false);
        m_jTax.setRequestFocusEnabled(false);

        m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPor.setFocusable(false);
        m_jPor.setOpaque(true);
        m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        m_jPor.setRequestFocusEnabled(false);

        m_jaddtax.setText("+");
        m_jaddtax.setFocusPainted(false);
        m_jaddtax.setFocusable(false);
        m_jaddtax.setRequestFocusEnabled(false);

        jLblPrinterStatus.setForeground(new java.awt.Color(255, 0, 0));

        m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setBorder(null);
        m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(4, 4));
        m_jKeyFactory.setRequestFocusEnabled(false);
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jKeyFactory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(65, 65, 65)
                        .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jKeyFactory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(21, 21, 21)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(19, 19, 19)
                        .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 392, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(270, 270, 270)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1325, 1, 170, 160));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel8.setPreferredSize(new java.awt.Dimension(1551, 193));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Bill Details");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 188, -1));

        m_jSubtotalEuros1.setBackground(new java.awt.Color(255, 255, 255));
        m_jSubtotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros1.setFocusable(false);
        m_jSubtotalEuros1.setOpaque(true);
        m_jSubtotalEuros1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jSubtotalEuros1.setRequestFocusEnabled(false);
        jPanel8.add(m_jSubtotalEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 80, 23));

        m_jLblTotalEuros4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros4.setText("SUB TOTAL");
        jPanel8.add(m_jLblTotalEuros4, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 37, 94, 23));

        m_jTaxesEuros1.setBackground(new java.awt.Color(255, 255, 255));
        m_jTaxesEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros1.setFocusable(false);
        m_jTaxesEuros1.setOpaque(true);
        m_jTaxesEuros1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTaxesEuros1.setRequestFocusEnabled(false);
        jPanel8.add(m_jTaxesEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 80, 23));

        m_jLblTotalEuros5.setBackground(new java.awt.Color(255, 255, 255));
        m_jLblTotalEuros5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros5.setText("TAXES");
        jPanel8.add(m_jLblTotalEuros5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 60, 23));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("DISCOUNT");
        jPanel8.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, 23));

        m_jLblTotalEuros6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros6.setText("TOTAL SALES");
        jPanel8.add(m_jLblTotalEuros6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 80, 20));

        m_jDiscount1.setBackground(new java.awt.Color(255, 255, 255));
        m_jDiscount1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jDiscount1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jDiscount1.setFocusable(false);
        m_jDiscount1.setOpaque(true);
        m_jDiscount1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jDiscount1.setRequestFocusEnabled(false);
        jPanel8.add(m_jDiscount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 80, 23));

        m_jTotalEuros.setBackground(new java.awt.Color(255, 255, 255));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setFocusable(false);
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        jPanel8.add(m_jTotalEuros, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 80, 23));

        jTaxPanel.setBackground(new java.awt.Color(255, 255, 255));
        jTaxPanel.setPreferredSize(new java.awt.Dimension(202, 157));
        jTaxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelServiceTax.setText("SERVICE TAX");
        jTaxPanel.add(jLabelServiceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 100, 23));

        m_jServiceTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTaxPanel.add(m_jServiceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 73, 23));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tax Breakup");
        jTaxPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 11, 194, -1));

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(m_jTaxList);

        jTaxPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 37, 200, 130));

        jPanel8.add(jTaxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 1, -1, 178));

        jLabelServiceCharge.setText("SERVICE CHARGE");
        jPanel8.add(jLabelServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, 23));

        m_jServiceCharge.setBackground(new java.awt.Color(255, 255, 255));
        m_jServiceCharge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jServiceCharge.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        m_jServiceCharge.setFocusable(false);
        m_jServiceCharge.setOpaque(true);
        m_jServiceCharge.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jServiceCharge.setRequestFocusEnabled(false);
        jPanel8.add(m_jServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 80, 23));

        jPanel4.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 1, 434, -1));

        m_jProducts.setBackground(new java.awt.Color(255, 255, 255));
        m_jProducts.setLayout(new java.awt.CardLayout());
        jPanel4.add(m_jProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 1, 551, 190));

        m_jPanContainer.add(jPanel4, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped
//
//        m_jKeyFactory.setText(null);
//        try {
//            stateTransition(evt.getKeyChar());
//        } catch (BasicException ex) {
//            logger.info("Order No." + m_oTicket.getOrderId() + " exception while calling stateTransition" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed
        logger.info("Cancel Line Action Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int i = m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            //dlReceipts.updateServedTransactionCancel(m_oTicket, m_oTicket.getLine(i).getTbl_orderId());
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            String addonVal = m_oTicket.getLine(i).getAddonId();
            int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
            int comboAddon = m_oTicket.getLine(i).getComboAddon();
            //System.out.println("primary addon"+primaryAddon);
            if (kotStatus != 0) {
                logger.info("Its  kot item");
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {
                    boolean billUpdated = JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineDelete");
                    //NewKDS March2017
                    //dlReceipts.updateServedTransactionCancel(m_oTicket, m_oTicket.getLine(i).getTbl_orderId());

                    try {
                        if (billUpdated) {

                            // showMessage(this, "The Table is being accessed by another User!Try after sometime");
                            RetailTicketInfo dbticket = null;
                            try {
                                dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                            } catch (BasicException ex) {
                                logger.info("Exception while selecting the dbticket on cancel item action ");
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (dbticket != null) {
                                //  String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                                // dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                                dbticket.setObjectUpdateDate(JReasonEditor.dbUpdatedDate);
                                for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                                    if (m_oTicket.getLine(index).getIsKot() == 0) {
                                        m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                                        dbticket.addLine(m_oTicket.getLine(index));
                                    }
                                }
                                JRetailBufferWindow.showMessage(this);
                                logger.info("This Bill is updated one.Loading.......");
                                setRetailActiveTicket(dbticket, m_oTicketExt);
                                dbUpdatedDate = null;
                                kotaction = 1;

                            } else {
                                showMessage(this, "This Bill is no longer exist");
                                logger.info("This Bill is no longer exist");
                                m_ticketsbag.activate();
                            }
                        } else {
                            if (JReasonEditor.getCancel() == true) {
                                //NewKDS March2017
                                dlReceipts.updateServedTransactionCancel(m_oTicket, m_oTicket.getLine(i).getTbl_orderId());
                                if (m_oTicket.getLine(i).getAddonId() != null) {
                                    dlReceipts.updateServedTransactionCancelAddOn(m_oTicket, m_oTicket.getLine(i).getAddonId(), m_oTicket.getLine(i).getTbl_orderId());
                                }
                                kotlogger.info("KOT cancelled Successfully " + "," + "Username: " + m_oTicket.printUser() + "," + "Kot No: " + m_oTicket.getLine(i).getKotid() + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + m_oTicket.getOrderId() + "," + "Product Name: " + m_oTicket.getLine(i).getProductName() + "," + "Qty: " + m_oTicket.getLine(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                                removeTicketLine(i);
                                //adding the logic of deleting the addon items
                                if (addonVal != null && primaryAddon == 1 || (addonVal != null && comboAddon == 1)) {
                                    int j = 0;
                                    while (j < m_oTicket.getLinesCount()) {
                                        if (addonVal.equals(m_oTicket.getLine(j).getAddonId())) {
                                            kotlogger.info("Addon KOT cancelled Successfully " + "," + "Username: " + m_oTicket.printUser() + "," + "Kot No: " + m_oTicket.getLine(j).getKotid() + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + m_oTicket.getOrderId() + "," + "Product Name: " + m_oTicket.getLine(j).getProductName() + "," + "Qty: " + m_oTicket.getLine(j).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                                            removeTicketLine(j);
                                            j = 0;
                                        } else {
                                            j++;
                                        }
                                    }
                                }

                                m_oTicket.refreshTxtFields(0);
                                if (m_oTicket.getLinesCount() == 0) {
                                    m_jTxtChange.setText("");
                                    m_jServiceCharge.setText("Rs. 0.00");
                                    m_jTaxesEuros1.setText("Rs. 0.00");
                                    m_jTotalEuros.setText("Rs. 0.00");
                                    refreshTicket();
                                    taxModel.removeAllElements();
                                }
                                if (!jTextPhoneNo.getText().equals("")) {
                                    logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in Cancel item(cross) Action ;");
                                    m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                                    MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                                }
                                // Date updated = new Date();
                                Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                                Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                                try {
                                    new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    if (m_oTicket.getTakeaway().equals("Y")) {
//                                        new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    }
                                } catch (BasicException ex) {
                                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                String splitId = m_oTicket.getSplitSharedId();
                                Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                                if (record != null) {
                                    m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                                }


                            }
                        }
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }


                } else {
                    logger.info("Item is already sent to Production Area. Please check with the Manager or Supervisor to cancel this item");
                    showMessage(this, "Item is already sent to Production Area. Please check with the Manager or Supervisor to cancel this item");
                }
            } else {
                logger.info("Its non kot item");
                removeTicketLine(i);
                if (addonVal != null && primaryAddon == 1 | (addonVal != null && comboAddon == 1)) {
                    int j = 0;
                    while (j < m_oTicket.getLinesCount()) {
                        if (addonVal.equals(m_oTicket.getLine(j).getAddonId())) {
                            removeTicketLine(j);
                            j = 0;
                        } else {
                            j++;
                        }
                    }
                }
                m_oTicket.refreshTxtFields(0);
                if (m_oTicket.getLinesCount() == 0) {
                    m_jTxtChange.setText("");
                    //m_oTicket.refreshTxtFields(0);
                    m_jServiceCharge.setText("Rs. 0.00");
                    m_jTaxesEuros1.setText("Rs. 0.00");
                    m_jTotalEuros.setText("Rs. 0.00");
                    refreshTicket();
                    taxModel.removeAllElements();
                }

            }

        }

        //  }
    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPlusActionPerformed
        //  if(m_oTicket.getParentId()==null)  {
        //    m_ticketlines.selectionUp();
        logger.info("Plus Action Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int i = m_ticketlines.getSelectedIndex();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        if (newline.getComboAddon() == 0) {
            if (m_oTicket.getLine(i).getIsKot() != 1) {
                logger.info("its not combo addon and not kot done");
                newline.setMultiply(newline.getMultiply() + 1.0);
                paintTicketLine(i, newline);
            }
            //System.out.println("printing instruction" + newline.getInstruction());
        }
    }//GEN-LAST:event_m_jPlusActionPerformed

    private void m_jMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMinusActionPerformed
        logger.info("Minus Action Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int i = m_ticketlines.getSelectedIndex();
        String addonVal = m_oTicket.getLine(i).getAddonId();
        int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
        int comboAddon = m_oTicket.getLine(i).getComboAddon();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        if (newline.getMultiply() > 1.0) {
            logger.info("item has more than 1 qty");
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            if (kotStatus != 0) {
                logger.info("its kot item");
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {
                    boolean billUpdated = JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineMinus");
                    if (billUpdated) {
                        // showMessage(this, "The Table is being accessed by another User!Try after sometime");
                        RetailTicketInfo dbticket = null;
                        try {
                            dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                        } catch (BasicException ex) {
                            logger.info("Exception while selecting the dbticket on cancel item action ");
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (dbticket != null) {
                            //      String  dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                            // dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                            dbticket.setObjectUpdateDate(JReasonEditor.dbUpdatedDate);
                            for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                                if (m_oTicket.getLine(index).getIsKot() == 0) {
                                    m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                                    dbticket.addLine(m_oTicket.getLine(index));
                                }
                            }
                            JRetailBufferWindow.showMessage(this);
                            logger.info("This Bill is updated one.Loading.......");
                            setRetailActiveTicket(dbticket, m_oTicketExt);
                            dbUpdatedDate = null;
                            kotaction = 1;
                        } else {
                            showMessage(this, "This Bill is no longer exist");
                            logger.info("This Bill is no longer exists");
                            m_ticketsbag.activate();
                        }
                    } else {
                        if (JReasonEditor.getCancel() == true) {
                            newline.setMultiply(newline.getMultiply() - 1.0);
                            dlReceipts.updateServedTransactionMinus(m_oTicket, newline.getTbl_orderId(), newline.getMultiply());


                            if (m_oTicket.getLine(i).getPrimaryAddon() == 0 && m_oTicket.getLine(i).getAddonId() != null) {
                                dlReceipts.updateServedTransactionMinusAddOnModify(m_oTicket, newline.getAddonId(), newline.getMultiply(), newline.getTbl_orderId());

                            } else {
                                if (m_oTicket.getLine(i).getAddonId() != null) {
                                    dlReceipts.updateServedTransactionMinusAddOn(m_oTicket, m_oTicket.getLine(i).getAddonId(), m_oTicket.getLine(i).getMultiply(), m_oTicket.getLine(i).getTbl_orderId());
                                }
                            }



                            kotlogger.info("KOT cancelled Successfully one qty " + "," + "Username: " + m_oTicket.printUser() + "," + "Kot No: " + m_oTicket.getLine(i).getKotid() + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + m_oTicket.getOrderId() + "," + "Product Name: " + m_oTicket.getLine(i).getProductName() + "," + "Qty: " + m_oTicket.getLine(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

                            if (addonVal != null && primaryAddon == 1 || (addonVal != null && comboAddon == 1)) {
                                int j = 0;
                                while (j < m_oTicket.getLinesCount()) {
                                    if (addonVal.equals(m_oTicket.getLine(j).getAddonId()) && m_oTicket.getLine(j).getPrimaryAddon() == 0) {
                                        removeTicketLine(j);
                                        j = 0;
                                        kotlogger.info("Addon KOT cancelled Successfully one qty " + "," + "Username: " + m_oTicket.printUser() + "," + "Kot No: " + m_oTicket.getLine(j).getKotid() + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + m_oTicket.getOrderId() + "," + "Product Name: " + m_oTicket.getLine(j).getProductName() + "," + "Qty: " + m_oTicket.getLine(j).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

                                    } else {
                                        j++;
                                    }
                                }
                            }
                            // Date updated = new Date();
                            Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                            try {
                                try {
                                    new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    if (m_oTicket.getTakeaway().equals("Y")) {
//                                        new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    }
                                } catch (BasicException ex) {
                                    logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  MINUS action updating shared ticket" + ex.getMessage());
                                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                String splitId = m_oTicket.getSplitSharedId();
                                Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                                if (record != null) {
                                    m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                                }
                            } catch (BasicException ex) {
                                logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  printRetailKotTicket updating shared ticket" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }


                    }
                } else {
                    showMessage(this, "Item is sent to kot.Only Cashier or Manager can cancel the line.");
                    logger.info("Item is sent to kot.Only Cashier or Manager can cancel the line.");
                }
            } else {
                logger.info("Its non kot item");
                newline.setMultiply(newline.getMultiply() - 1.0);
                if (addonVal != null && primaryAddon == 1 || (addonVal != null && comboAddon == 1)) {
                    int j = 0;
                    while (j < m_oTicket.getLinesCount()) {
                        if (addonVal.equals(m_oTicket.getLine(j).getAddonId()) && m_oTicket.getLine(j).getPrimaryAddon() == 0) {
                            removeTicketLine(j);
                            j = 0;
                        } else {
                            j++;
                        }
                    }
                }

            }


            if (newline.getMultiply() > 0.0) {
                paintTicketLine(i, newline);
            }
            if (!jTextPhoneNo.getText().equals("") && kotStatus != 0) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in Cancel item(-) Action;");
                m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
            }
        }
        logger.info("End Minus Line Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()) + ";");
    }//GEN-LAST:event_m_jMinusActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
        logger.info("Edit Action Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int i = m_ticketlines.getSelectedIndex();
        if (m_oTicket.getLine(i).getIsKot() == 0) {
            if (i < 0) {
                Toolkit.getDefaultToolkit().beep(); // no line selected
            } else {
                try {
                    RetailTicketLineInfo newline = JRetailProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
                    if (newline != null) {
                        logger.info("updated in edit action");
                        // line has been modified
                        //    new TicketLineInfo(promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this);
                        // newline.setButton(2);
                        //  newline.setIndex(i);
                        paintTicketLine(i, newline);
                        //   m_oTicket.refreshTxtFields(1);
                        //below line is for promotion products
//                    ProductInfoExt product = getInputProduct();
//                    addTicketLine(product, 1.0, product.getPriceSell());
                    }
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                }
            }
        } else {
            logger.info("showing kds details");
            if (userMap.isEmpty()) {
                populateUsers();
            }
            JRetailKdsDetails.showItemsScreen(this, m_oTicket.getLine(i), dlReceipts, userMap);
        }
    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLogoutActionPerformed
        //  m_RootApp = null;
        logger.info("Start Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//        getActiveTicket().setTicketOpen(false);
//        try {
//            dlReceipts.updateSharedTicket(getActiveTicket().getPlaceId(), getActiveTicket());
//        } catch (BasicException ex) {
//            logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  logout updating shared ticket" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
        m_RootApp = (JRootApp) m_App;
        m_RootApp.closeAppView();
        logger.info("End Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

        tiltUserName = m_App.getAppUserView().getUser().getName();
        tiltUserRole = m_App.getAppUserView().getUser().getRole();
        try {
            tiltRole = dlReceipts.getTiltRolebyName(tiltUserRole);
        } catch (BasicException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (("CASHIER").equalsIgnoreCase(tiltRole)) {
            JTiltCollection.showMessage(this, dlReceipts, false, tiltUserName);
            logger.info("End Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
            logger.info("Tilt action performed in JRetailTicketBagRest.Map class");
        }//end If

}//GEN-LAST:event_m_jLogoutActionPerformed

    private void m_jCalculatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCalculatePromotionActionPerformed
        ArrayList<BuyGetPriceInfo> leastProductList = new ArrayList<BuyGetPriceInfo>();
        StringBuilder b = new StringBuilder();
        StringBuilder b1 = new StringBuilder();
        try {
            dlSales.deleteTempTicketlines();
            dlSales.saveRetailTempTicketlines(m_oTicket);
            java.util.ArrayList<String> campaignId = new ArrayList<String>();

            pdtCampaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getPdtCampaignId();
            for (int i = 0; i < pdtCampaignIdList.size(); i++) {
                campaignId.add("'" + pdtCampaignIdList.get(i).getcampaignId() + "'");
            }

            Iterator<?> it = campaignId.iterator();
            while (it.hasNext()) {
                b.append(it.next());
                if (it.hasNext()) {
                    b.append(',');
                }
            }
            String promoCampaignId = b.toString();
            if (!promoCampaignId.equals("")) {
                double price = 0;
                double taxAmount = 0;
                pdtBuyGetList = (ArrayList<BuyGetInfo>) dlSales.getbuyGetTotalQty(promoCampaignId);
                for (int i = 0; i < pdtBuyGetList.size(); i++) {
                    pdtBuyGetQtyList = (ArrayList<BuyGetQtyInfo>) dlSales.getbuyGetQty(pdtBuyGetList.get(i).getCampaignId(), pdtBuyGetList.get(i).getQty());


                    if (pdtBuyGetQtyList.size() != 0) {
                        pdtBuyGetPriceList = (ArrayList<BuyGetPriceInfo>) dlSales.getbuyGetLeastPrice(pdtBuyGetList.get(i).getCampaignId(), pdtBuyGetQtyList.get(0).getQty());
                        pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                        for (int j = 0; j < pdtBuyGetPriceList.size(); j++) {
                            price = price + pdtBuyGetPriceList.get(j).getPrice();
                            taxAmount = taxAmount + pdtBuyGetPriceList.get(j).getTaxRate();
                            if (pdtLeastPriceList.size() == 0) {
                                pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                            } else {
                                boolean flag = false;
                                for (int k = 0; k < pdtLeastPriceList.size(); k++) {
                                    if (pdtLeastPriceList.get(k).getProductID() == pdtBuyGetPriceList.get(j).getProductID()) {
                                        BuyGetPriceInfo info = pdtLeastPriceList.get(k);
                                        info.setQuantity((info.getQuantity() + 1));
                                        pdtLeastPriceList.remove(k);
                                        pdtLeastPriceList.add(info);
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag == false) {
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                                }
                            }
                        }
                        leastProductList.addAll(pdtLeastPriceList);
                    }

                }
                setPriceInfo(leastProductList);
                m_oTicket.setLeastValueDiscount(price);
                m_oTicket.setTaxValue(taxAmount);

            }
            m_oTicket.billValuePromotion(promoRuleIdList, dlSales);
            printPartialTotals();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
}//GEN-LAST:event_m_jCalculatePromotionActionPerformed

    private void m_jBtnDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDiscountActionPerformed
        logger.info("Start Discount Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        if (m_oTicket.getLinesCount() != 0) {
            try {
                String categoryDiscount = m_App.getProperties().getProperty("machine.categorydiscount");
                String user = m_oTicket.getUser().getName();
                String type = "sales";
                String role = dlCustomers.getRolebyName(user);
                if (categoryDiscount.equals("false")) {
                    //Billwise Discount
                    m_oTicket.setCategoryDiscount(false);
                    if ("Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role) || "Cashier".equalsIgnoreCase(role)) {// for admin
                        m_oTicket.setdPerson(m_oTicket.getUser().getId());
                        JRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, role, type);
                        //   calculateServiceCharge();
                        //      populateServiceChargeList();
                        //    populateServiceTaxList();


                        populateTaxList();
                        m_oTicket.refreshTxtFields(1);
                        if (!jTextPhoneNo.getText().equals("") && m_oTicket.getOrderId() != 0) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "Paasing the product information to Momoe in discount action method ;");
                            m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                            MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                        }
                    } //            else if ((role.equalsIgnoreCase("Cashier")) || (role.equalsIgnoreCase("Syscashier")) ) {//cashier
                    //                JDiscountAssign.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, m_oTicket);
                    //                populateTaxList();
                    //            } 
                    else {
                        JOptionPane.showMessageDialog(jPanel1, "Please contact the Manager to discount the bill");
                    }
                } else {
                    //Line level Discount
//                    m_oTicket.setCategoryDiscount(true);
//                    if ("Cashier".equalsIgnoreCase(role)) {// for admin
//                        boolean login = JDiscountAssign.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, m_oTicket);
//                        if (login) {
//                            m_oTicket.setdPerson(m_oTicket.getUser().getId());
//                            //Billwise Discount
//                            //JRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, role,type);
//                            //Linewise Discount
//                            java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
//                            Map<String, DiscountInfo> dRateMap = new HashMap();
//                            for (DiscountRateinfo dis : list) {
//                                dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
//                            }
//                            discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, "", list, dRateMap);
//                            m_oTicket.setDiscountMap(discountMap);
//                            if (discountMap != null) {
//                                populateDiscount(discountMap);
//                            }
//
//                        }
//                    } else {
//                        m_oTicket.setdPerson(m_oTicket.getUser().getId());
//                        java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
//                        Map<String, DiscountInfo> dRateMap = new HashMap();
//                        for (DiscountRateinfo dis : list) {
//                            dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
//                        }
//                        discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, "", list, dRateMap);
//                        m_oTicket.setDiscountMap(discountMap);
//                        if (discountMap != null) {
//                            populateDiscount(discountMap);
//                        }
//
//                    }
//               populateTaxList();
//                m_oTicket.refreshTxtFields(1);
                }

            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  discount action" + ex.getMessage());
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);

    }//GEN-LAST:event_m_jBtnDiscountActionPerformed
        }
        logger.info("End Discount Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }

    private void m_jActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jActionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jActionActionPerformed

    public boolean checkTicketUpdation() {
        boolean updated = false;
        try {
            System.out.println("m_oTicket.getObjectUpdateDate() : " + m_oTicket.getObjectUpdateDate());

            String currentUpdated = m_dateformat.format(m_oTicket.getObjectUpdateDate()) + " " + m_dateformattime.format(m_oTicket.getObjectUpdateDate());
            //  System.out.println("my current updated date"+currentUpdated);
            String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
            System.out.println("dbUpdated----" + dbUpdated);
            Date currentUpdatedDate = DateFormats.StringToDateTime(currentUpdated);
            dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
            System.out.println("dbUpdatedDate : " + dbUpdatedDate + "currentUpdatedDate : " + currentUpdatedDate);

            //if the table is deleted by (settlement/cancel/move)
            if (dbUpdated.equals(null) || dbUpdated.equals("")) {
                logger.info("This Bill is no longer exist");
                showMessage(this, "This Bill is no longer exist");
                updated = true;
                m_ticketsbag.activate();
            }//if the table is updated
            else if (dbUpdatedDate.compareTo(currentUpdatedDate) > 0) {
                //Taking the updated sharedticket
                RetailTicketInfo dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                dbticket.setObjectUpdateDate(dbUpdatedDate);
                //assigning the discount percentage to fetched sharedticket from existing sharedticket (if applied)
                for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                    if (m_oTicket.getLine(index).getIsKot() == 0) {
                        m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                        dbticket.addLine(m_oTicket.getLine(index));


                    }
                }
                //showing loading popup for 1 sec
                JRetailBufferWindow.showMessage(this);
                logger.info("This Bill is updated one.Loading.......");
                //setting the updated sharedticket to billing screen
                setRetailActiveTicket(dbticket, m_oTicketExt);
                dbUpdatedDate = null;
                //allowing to further kots
                kotaction = 1;
                updated = true;
            } else {
                updated = false;
            }

        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return updated;
    }

    public final synchronized void kotDisplay() throws BasicException {
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            kotprintIssue = 0;
            logger.info("kotaction value" + kotaction);
            int orderId = 0;
            if (m_oTicket.getOrderId() == 0) {
                if (m_oTicket.getLinesCount() != 0) {
                    orderId = dlSales.getNextTicketOrderNumber();
                    System.out.println("orderId----------- : " + orderId);
                    m_oTicket.setOrderId(orderId);
                }
            }

            final int kotTicket;
            int kotTicketId = 0;
            kotTicketId = dlSales.getNextKotIndex();
            if (kotTicketId == 0) {
                kotTicket = 1;
            } else {
                kotTicket = kotTicketId;
            }
            RetailTicketInfo info = getActiveTicket();
            java.util.List<kotPrintedInfo> kPrintedInfolist = null;
            final java.util.List<RetailTicketLineInfo> panelLines = info.getLines();
            final java.util.List<RetailTicketLineInfo> panelNonKotLines = new ArrayList();

            String sessionId = null;
            sessionId = dlReceipts.getFloorId(m_oTicket.getPlaceId());
            printerInfo = dlReceipts.getPrinterInfo(sessionId);
            Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
            Date kotDate = null;
            if (record != null) {
                kotDate = DateFormats.StringToDateTime((String) record[0]);

            }
            for (int i = 0; i < panelLines.size(); i++) {
                if (panelLines.get(i).getIsKot() == 0) {
                    String tbl_orderitemId = UUID.randomUUID().toString();
                    tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
                    panelLines.get(i).setTbl_orderId(tbl_orderitemId);
                    panelLines.get(i).setKotid(kotTicket);
                    panelLines.get(i).setKotdate(kotDate);
                    //   panelLines.get(i).setKotdate(new Date());
                    panelLines.get(i).setKottable(m_oTicket.getPlaceId());
                    panelLines.get(i).setKotuser(m_oTicket.getUser().getId());
                    panelNonKotLines.add(panelLines.get(i));
                }

            }

            printRetailKotTicket("Printer.Kot", m_oTicket, panelNonKotLines, m_oTicketExt, printerInfo, kotTicket);

            Runtime.getRuntime().gc();
            System.out.println("current time after kot" + new Date());
        }
    }

    private synchronized void printRetailKotTicket(String sresourcename, RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> kot, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo, int kotTicket) {

        java.util.List<TicketLineConstructor> allLines = null;
        logger.info("start printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        kotTicketlist = kot;
        for (int j = 0; j < printerInfo.size(); j++) {
            java.util.List<RetailTicketLineInfo> uniqueProductionAreas = new ArrayList<RetailTicketLineInfo>();
            for (int i = 0; i < kotTicketlist.size(); i++) {
                if (printerInfo.get(j).getProductionAreaType().equals(kotTicketlist.get(i).getProductionAreaType())) {
                    System.out.println("before setting GetProductionArea" + printerInfo.get(j).getProductionAreaType());
                    System.out.println("kotTicketlist.get(i).GetProductionArea" + kotTicketlist.get(i).getProductionAreaType());


                    kotTicketlist.get(i).setProductionArea(printerInfo.get(j).getProductionArea());
                    //added newly to check that is kot required or only kds 22/6/2016
                    if (printerInfo.get(j).getIskot().equals("Y")) {
                        uniqueProductionAreas.add(kotTicketlist.get(i));
                    } else {
                        kotTicketlist.get(i).setIsKot(1);
                        if (kotTicketlist.get(i).getPreparationStatus() != 3) {
                            kotTicketlist.get(i).setPreparationStatus(4);
                        }
                    }
                }
            }
            logger.info("kot print count based on production areas" + uniqueProductionAreas.size());
            //  System.out.println("unique---"+uniqueProductionAreas.get(j).printName());
            //System.out.println("uniqueProductionAreas:"+uniqueProductionAreas.size());
            if (uniqueProductionAreas.size() != 0) {
                allLines = getRetailAllLines(ticket, ticketExt, uniqueProductionAreas, kotTicket);
                try {
                    printer.printKot(allLines, printerInfo.get(j).getPath());
                    //   kotprintIssue=0;
                    // kotlogger.info("items printed successfully "+"By "+m_oTicket.printUser()+""+" in POS System "+m_App.getProperties().getPosNo()+" Table name "+m_oTicketExt.toString());
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        // kotlogger.info(uniqueProductionAreas.get(i).getProductName());
                        kotlogger.info("KOT Printed Successfully " + "," + "Username: " + m_oTicket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(1);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(4);
                        }
                        // dlReceipts.insertRetailPrintedKot(m_oTicket.getId(), m_oTicket.getTicketId(), uniqueProductionAreas.get(i).getProductID(), "Y", uniqueProductionAreas.get(i).getMultiply(), uniqueProductionAreas.get(i).getKotid(), "N", "", "", m_oTicket.getPlaceId(), m_oTicket.getUser().getId(), m_oTicket.getOrderId());

                        //New KDS Added on 7-03-17
                        String txstatus = "ADD";
                        String tableid_unique = uniqueProductionAreas.get(i).getTbl_orderId();
                        dlReceipts.insertServedTransaction(m_oTicket, txstatus, tableid_unique);
                    }


                } catch (PrinterException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + " The printer action" + ex.getMessage());
                    // kotprintIssue = 1;
                    System.out.println("within the catch of printer");
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        // kotlogger.info("items failed to print "+"By "+m_oTicket.printUser()+""+" in POS System "+m_App.getProperties().getPosNo()+" Table name "+m_oTicketExt.toString());
                        //  kotlogger.info("items failed to print :"+uniqueProductionAreas.get(i).getProductName());
                        logger.info("KOT Print Failed  " + "," + "Username: " + m_oTicket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        //uniqueProductionAreas.get(i).setIsKot(0);
                        uniqueProductionAreas.get(i).setIsKot(1);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            // uniqueProductionAreas.get(i).setPreparationStatus(0);
                            uniqueProductionAreas.get(i).setPreparationStatus(4);
                        }
                        //  dlReceipts.insertRetailPrintedKot(m_oTicket.getId(), m_oTicket.getTicketId(), uniqueProductionAreas.get(i).getProductID(), "Y", uniqueProductionAreas.get(i).getMultiply(), uniqueProductionAreas.get(i).getKotid(), "N", "", "", m_oTicket.getPlaceId(), m_oTicket.getUser().getId(), m_oTicket.getOrderId());

                        //New KDS Added on 7-03-17
                        String txstatus = "ADD";
                        String tableid_unique = uniqueProductionAreas.get(i).getTbl_orderId();
                        dlReceipts.insertServedTransaction(m_oTicket, txstatus, tableid_unique);
                    }
                    kotaction = 1;
                    showMessage(this, "KOT could not be sent to the Production Area. Please check the network connection.");
                }
            }
        }

        for (int i = 0; i < ticket.getLinesCount(); i++) {
            paintKotTicketLine(i, ticket.getLine(i));
        }
        // Date updated = new Date();
        Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
        try {
            try {
                new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                if (m_oTicket.getTakeaway().equals("Y")) {
//                    new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                }
            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  updating shared ticket" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            String splitId = m_oTicket.getSplitSharedId();
            Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
            if (record != null) {

                m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  printRetailKotTicket updating shared ticket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info("kot lines passing to print" + kotTicketlist.size());
        logger.info("end printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }

    private java.util.List<TicketLineConstructor> getRetailAllLines(RetailTicketInfo ticket, Object ticketext, java.util.List<RetailTicketLineInfo> kot, int kotTicket) {

        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        allLines.add(new TicketLineConstructor("          KITCHEN ORDER TICKET"));
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(16 - (ticket.printDate().substring(0, 12).length())) + "Kot No: " + kotTicket));
        allLines.add(new TicketLineConstructor("Time: " + (ticket.printTime()) + getSpaces(16 - (ticket.printTime().length())) + "Table No:" + getSpaces(1) + ticket.getName(m_oTicketExt)));
        allLines.add(new TicketLineConstructor("User Name: " + (m_oTicket.getUser()).getName()));
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        for (RetailTicketLineInfo tLine : kot) {
            String prodName = tLine.printName();

            if (tLine.getPrimaryAddon() == 0 && tLine.getAddonId() != null) {
                prodName = "->" + prodName;
            }
            // String qty = Formats.DoubleValue.formatValue(tLine.printQty());
            String qty = tLine.printMultiply();
            qtySum = qtySum + tLine.getMultiply();
            String instruction = tLine.printInstruction();
            if (prodName.length() > 30) {
                prodName = WordUtils.wrap(prodName, 30);
                String[] prodNameArray = prodName.split("\n");
                for (int i = 0; i < prodNameArray.length - 1; i++) {
                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
                }

                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(33 - prodNameArray[prodNameArray.length - 1].length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            } else {
                allLines.add(new TicketLineConstructor(prodName + getSpaces(33 - prodName.length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            }

        }
        logger.info("sum of kot quantities = " + qtySum);
        return allLines;
    }

    private void m_jEraserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEraserActionPerformed
        //  if(m_oTicket.getParentId()==null)  {
        logger.info("Erase Action Performed" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int kotStatus = 0;
        int kotCount = 0;
        int nonKotCount = 0;
        int linesCount = m_oTicket.getLinesCount();
        for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
            kotStatus = m_oTicket.getLine(i).getIsKot();
            if (kotStatus == 1) {
                kotCount++;
            } else {
                nonKotCount++;
            }
        }
        if (kotCount == linesCount) {
            showMessage(this, "All items are already sent as KOTs");
            logger.info("All items are already sent as KOTs");
        } else if (nonKotCount == linesCount) {
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_oTicket.getLines().clear();
            }
            refreshTicket();
            //m_oTicket.getLines().remove(i);
            taxModel.removeAllElements();
        } else {
            int i = 0;
            while (i < m_oTicket.getLinesCount()) {
                if (m_oTicket.getLine(i).getIsKot() == 0) {
                    removeTicketLine(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            m_oTicket.refreshTxtFields(0);
        }
        kotaction = 0;
        //  } 
    }//GEN-LAST:event_m_jEraserActionPerformed

    private void m_jbtnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnPrintBillActionPerformed
        logger.info("Start Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        try {
            if (m_oTicket.getLinesCount() != 0) {
                if (kotprintIssue == 0) {
                    int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrint"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (res == JOptionPane.YES_OPTION) {
                        boolean updated = checkTicketUpdation();
                        if (!updated) {
                            dbUpdatedDate = null;
                            if (kotaction == 1) {
                                kotDisplay();
                            } else {
                                Runtime.getRuntime().gc();
                            }
                            serveAllLines();
                            try {
                                logger.info("Start Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                                logger.info("No. of Line Items during Print Bill : " + m_oTicket.getLinesCount());
                                doPrintValidation();
                            } catch (BasicException ex) {
                                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill doPrintValidation" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String file;
                            file = "Printer.Bill";
                            m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
                            try {
                                taxeslogic.calculateTaxes(m_oTicket);
                                //  chargeslogic.calculateCharges(m_oTicket);
                                // staxeslogic.calculateServiceTaxes(m_oTicket);
                            } catch (TaxesException ex) {
                                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill calculateTaxes" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                            calculateServiceCharge();
                            printTicket(file, m_oTicket, m_oTicketExt);
                            logger.info("bill has been printed");
                            m_oTicket.setPrinted(true);
                            //Date updated = new Date();
                            Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                            new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                            if (m_oTicket.getTakeaway().equals("Y")) {
//                                new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                            }
                            String splitId = m_oTicket.getSplitSharedId();
                            Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                            if (record != null) {
                                m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                            }
                            logger.info("End Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                            if (reservationStatus.equals("true")) {
                                if (m_oTicket.getSplitValue().equals("")) {
                                    ReservationStatus.Reservation(m_oTicket.getTableName(), "4", m_App);
                                } else {
                                    int printCount = dlSales.getSplitTicketsNonPrintCount(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                                    if (printCount == 0) {
                                        ReservationStatus.Reservation(m_oTicket.getTableName(), "4", m_App);
                                    }
                                }
                            }
                            if (!jTextPhoneNo.getText().equals("")) {
                                logger.info("Order No. " + m_oTicket.getOrderId() + "Passing the product information to Momoe in Print Bill Action ;");
                                m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                                MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                            }
                        }
                    }
                }
                //}
            }
            if (kotprintIssue == 0) {
                if (roleName.equals("Bartender")) {
                    logger.info("Role Bartender");
                    IsSteward = 1;
                    JRetailTicketsBagRestaurant.setNewTicket();
                    m_RootApp = (JRootApp) m_App;
                    m_RootApp.closeAppView();
                }
            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("End Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jbtnPrintBillActionPerformed

    private void m_jSettleBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSettleBillActionPerformed

        logger.info("m_jSettleBillActionPerformed");
        logger.info("Start Settle Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

        if (m_oTicket.getLinesCount() == 0) {
            return;
        }
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            if (kotaction == 1) {
                try {
                    if (!jTextPhoneNo.getText().equals("")) {
                        logger.info("Order No. " + m_oTicket.getOrderId() + "Passing the product information to Momoe in Settle Bill Action ;");
                        m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                        MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                    }
                    kotDisplay();
                } catch (BasicException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking settle bill kot action" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!m_oTicket.isPrinted()) {
                showMessage(this, "Bill must first be printed before settling the bill");
                logger.info("Bill must first be printed before settling the bill");
                return;
            } else if (m_oTicket.isPrinted() & m_oTicket.isListModified()) {
                showMessage(this, "Bill has been modified since the last print. Please print the bill again before settling");
                logger.info("Bill has been modified since the last print. Please print the bill again before settling");
                return;
            }
            JPaymentEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, this);
            //  logger.info("settled successfully");
            logger.info("End Settle Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        }

}//GEN-LAST:event_m_jSettleBillActionPerformed

    public void splitBill() {
        int splitfunction = 0;
        if (m_oTicket.getLinesCount() > 0) {
            splitfunction = 1;
            if (m_oTicket.getLinesCount() == 1) {
                if (m_oTicket.getLine(0).getMultiply() > 1) {
                    splitfunction = 1;
                } else {
                    splitfunction = 0;
                }
            } else {
                splitfunction = 1;
            }
        }
        if (splitfunction == 1) {
            logger.info("splitfunction =1");
            RetailReceiptSplit splitdialog = RetailReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlSales, dlCustomers, taxeslogic);
            String placeId = m_oTicket.getPlaceId();
            String splitParentId = m_oTicket.getId();
            String splitSharedId = m_oTicket.getSplitSharedId();
            RetailTicketInfo cancelTIcket = m_oTicket;
            RetailTicketInfo ticket1 = m_oTicket.copySplitTicket(m_oTicket.getRate());
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replaceAll("-", "");
            // System.out.println("orderIdsplit : " + ticket1.getOrderId());
            ticket1.setId(uuid);
            ticket1.setOrderId(0);
            ticket1.setRate(m_oTicket.getRate());
            ticket1.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket1.setActiveCash(m_App.getActiveCashIndex());
            ticket1.setActiveDay(m_App.getActiveDayIndex());
            ticket1.setDate(new Date());
            ticket1.setSplitValue("Split");
            ticket1.setPlaceid(placeId);
            ticket1.setTicketId(0);
            ticket1.setParentId(splitParentId);
            ticket1.setSplitSharedId(splitSharedId);
            ticket1.setTicketOpen(false);
            ticket1.setDiscountMap(m_oTicket.getDiscountMap());
            ticket1.setCategoryDiscount(m_oTicket.iscategoryDiscount());
            ticket1.setRemarks(m_oTicket.getRemarks());
            ticket1.setDiscountReasonId(m_oTicket.getDiscountReasonId());
            ticket1.setDiscountSubReasonId(m_oTicket.getDiscountSubReasonId());
            ticket1.setMovedUser(m_oTicket.getMovedUser());
            ticket1.setMovedTime(m_oTicket.getMovedTime());
            ticket1.setDiscountComments(m_oTicket.getDiscountComments());
            ticket1.setTakeaway(m_oTicket.getTakeaway());
            ticket1.setTaxExempt(m_oTicket.isTaxExempt());
            RetailTicketInfo ticket2 = new RetailTicketInfo(m_oTicket.getRate());
            System.out.println("orderIdsplit-2222 : " + ticket2.getOrderId());

            ticket2.setOrderId(0);
            ticket2.setCustomer(m_oTicket.getCustomer());
            ticket2.setRate(m_oTicket.getRate());
            ticket2.setUser(m_App.getAppUserView().getUser().getUserInfo());
            ticket2.setSplitValue("Split");
            ticket2.setPlaceid(placeId);
            ticket2.setTicketId(0);
            ticket2.setParentId(splitParentId);
            ticket2.setTicketOpen(false);
            ticket2.setDiscountMap(m_oTicket.getDiscountMap());
            ticket2.setTableName(m_oTicket.getTableName());
            ticket2.setOldTableName(m_oTicket.getOldTableName());
            ticket2.setMomoePhoneNo(m_oTicket.getMomoePhoneNo());
            ticket2.setCategoryDiscount(m_oTicket.iscategoryDiscount());
            ticket2.setRemarks(m_oTicket.getRemarks());
            ticket2.setDiscountReasonId(m_oTicket.getDiscountReasonId());
            ticket2.setDiscountSubReasonId(m_oTicket.getDiscountSubReasonId());
            ticket2.setMovedUser(m_oTicket.getMovedUser());
            ticket2.setMovedTime(m_oTicket.getMovedTime());
            ticket2.setDiscountComments(m_oTicket.getDiscountComments());
            ticket2.setTakeaway(m_oTicket.getTakeaway());
            ticket2.setAccessInfo(m_oTicket.getAccessInfo());
            ticket2.setTaxExempt(m_oTicket.isTaxExempt());
            if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {
                boolean updated = checkTicketUpdation();
                if (!updated) {
                    dbUpdatedDate = null;
                    try {
                        if (splitdialog.window.equals("OK")) {
                            logger.info("splitfunction on  saying ok ");
                            setKotAndServedOnSplit(ticket1);
                            setKotAndServedOnSplit(ticket2);
                        } else if (splitdialog.window.equals("Print All")) {
                            m_oTicket = ticket1;
                            setKotServedAndPrintOnSplit(m_oTicket);
                            m_oTicket = ticket2;
                            setKotServedAndPrintOnSplit(m_oTicket);
                            if (reservationStatus.equals("true")) {
                                ReservationStatus.Reservation(m_oTicket.getTableName(), "4", m_App);
                            }
                        } else {
                            logger.info("splitfunction on saying print ");
                            m_oTicket = ticket2;
                            setKotAndServedOnSplit(ticket1);
                            setKotServedAndPrintOnSplit(m_oTicket);
                        }
                        cancelTIcket.setSplitValue("Split");
                        //cancelTIcket.setParentId(splitParentId);
                        String ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + cancelTIcket.getTicketId();
                        dlSales.saveRetailCancelSplitTicket(cancelTIcket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), "Split Bill", "", m_App.getProperties().getPosNo(), "Y");
                        if (!jTextPhoneNo.getText().equals("")) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "Passing the product information to Momoe in split bill method ;");
                            MomoePayment.MomoeIntegration(null, cancelTIcket, null, m_App, true, dlSales);
                            MomoePayment.MomoeIntegration(ticket1.getLines(), ticket1, taxlist, m_App, false, dlSales);
                            MomoePayment.MomoeIntegration(ticket2.getLines(), ticket2, taxlist, m_App, false, dlSales);
                        }
                        // dlSales.updateBillNo(cancelTIcket.getTicketId());
                    } catch (BasicException ex) {
                        logger.info("Order NO." + m_oTicket.getOrderId() + "exception while splitting the bill" + ex.getMessage());
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    logger.info("cancelled splitted older bill");
                    //  dlSales.insertActionsLog("Split Bill", cancelTIcket.getUser().getId(), m_App.getProperties().getPosNo(), cancelTIcket.getTicketId(), new Date(), cancelTIcket.getPlaceId(), null, null);
                    JRetailTicketsBagRestaurant.setNewSplitTicket(ticket1, ticket2);



                }


            }

        }

    }
    private void m_jSplitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSplitBtnActionPerformed
        logger.info("Start Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        logger.info("Split Bill Action");
        if (m_oTicket.getLinesCount() != 0) {
            try {
                boolean updated = checkTicketUpdation();
                if (!updated) {
                    dbUpdatedDate = null;
                    if (!jTextPhoneNo.getText().equals("")) {
                        logger.info("Order No. " + m_oTicket.getOrderId() + "Passing the product information to Momoe in Split Bill Action ;");
                        m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                        MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                    }
                    System.out.println("KOTACTION: " + kotaction);
                    if (kotaction == 1) {
                        kotDisplay();
                    }
                    if (kotprintIssue == 0) {
                        splitBill();
                    }

                }
            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking split bill" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.info("End Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jSplitBtnActionPerformed

    private void m_jBtnKotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnKotActionPerformed
        try {
            dbUpdatedDate = null;
            logger.info("Start kot Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
            logger.info("No. of Line Items during Kot : " + m_oTicket.getLinesCount());
            if (m_oTicket.getLinesCount() != 0) {
                if (kotaction == 1) {
                    logger.info("kotaction is 1");
                    kotDisplay();

                    if (!jTextPhoneNo.getText().equals("")) {
                        logger.info("Order No. " + m_oTicket.getOrderId() + "Passing  the order & Product information to Momoe on Kot Action ; ");
                        m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                        MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                    }
                }
                if (roleName.equals("Steward") || roleName.equals("Bartender")) {
                    logger.info("Start Logout Button : for Steward User" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                    IsSteward = 1;
                    //   JRetailTicketsBagRestaurant.setNewTicket(); not required updation is happening in kot method.
                    if (kotprintIssue == 0) {
                        m_RootApp = (JRootApp) m_App;
                        m_RootApp.closeAppView();
                    }
                    logger.info("End Logout Button : for Steward User" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }
                //  kotFlag=1;
                // kotDisplayOnAction(mTicket);
                //   } 
                //   }

            }

            logger.info("End Kot Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
            //}
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_m_jBtnKotActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //  JRetailCatalog jCatalog = new JRetailCatalog(dlSales);
        // jCatalog.showCatalogPanel(null);
        //  refreshTicket();
        resetSouthComponent("Category");

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonAddonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddonActionPerformed
        try {
            //jLayeredPanel
            String id = null;
            int i = m_ticketlines.getSelectedIndex();
            if (m_oTicket.getLine(i).getIsKot() == 0) {
                String prName = m_oTicket.getLine(i).getProductID();
                //    m_oTicket.getLine(i).setIsaddon('Y');

                ProductInfoExt getAddonProduct = null;
                if (menuStatus.equals("false")) {
                    getAddonProduct = dlSales.CountAddonProduct(prName);
                } else {
                    day = getWeekDay();
                    currentMenuList = dlSales.getMenuId(day);
                    if (currentMenuList.size() != 0) {
                        menuId = currentMenuList.get(0).getId();
                    }
                    getAddonProduct = dlSales.CountMenuAddonProduct(prName, menuId);
                }
                if (getAddonProduct == null) {
                    showMessage(this, "There are no Add-ons defined for this Item");
                    logger.info("There are no Add-ons defined for this Item");
                } else {
                    ProductInfoExt productListDetails = RetailReceiptAddonList.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, this, prName, menuId);
                    if (productListDetails != null) {
                        if (m_oTicket.getLine(i).getAddonId() == null) {
                            addonId = UUID.randomUUID().toString();
                            addonId = addonId.replaceAll("-", "");
                            m_oTicket.getLine(i).setAddonId(addonId);
                        } else {
                            addonId = m_oTicket.getLine(i).getAddonId();
                        }
                        m_oTicket.getLine(i).setPrimaryAddon(1);
                        id = productListDetails.getID();

                        ProductInfoExt productListval = null;
                        if (menuStatus.equals("false")) {
                            productListval = dlSales.getProductInfoAddon(id);
                        } else {
                            productListval = dlSales.getMenuProductInfoAddon(id, menuId);
                        }
                        productListval.setProductionAreaType(m_oTicket.getLine(i).getProductionAreaType());
                        productListval.setStation(m_oTicket.getLine(i).getStation());
                        productListval.setProductType(m_oTicket.getLine(i).getProductType());
                        //addonStatus='Y';
                        incProduct(productListval);
                    }
                }
            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking addon" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonAddonActionPerformed

    private void m_jBtnServedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnServedActionPerformed
        logger.info("Start Served Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        int i = m_ticketlines.getSelectedIndex();
        if (m_oTicket.getLinesCount() != 0 && m_oTicket.getLine(i).getIsKot() == 1) {
            logger.info("if kot done");
            try {
                dbUpdatedDate = null;

                //RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                java.util.List<ServedTransactionInfo> stList;

//                stList = dlReceipts.getUpdateFromServedTransaction(m_oTicket.getLine(i).getTbl_orderId());
//                for (ServedTransactionInfo servedTrans : stList) {
//                    System.out.println("PREPARATION STATUS : " + servedTrans.getPreparationStatus());

                if (m_oTicket.getLine(i).getPreparationStatus() != 3) {
                    //   if (servedTrans.getPreparationStatus() != 3) {
                    logger.info("if not served");
                    boolean updated = checkTicketUpdation();
                    if (!updated) {//added which was causing lock issue on 12/08/2016
                        logger.info("This Bill is not updated by anyone");
                        dbUpdatedDate = null;
                        m_oTicket.getLine(i).setPreparationStatus(3);
                        //  servedTrans.setPreparationStatus(3);
                        Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                        Date servedDate = null;
                        if (record != null) {
                            servedDate = DateFormats.StringToDateTime((String) record[0]);
                        }
                        m_oTicket.getLine(i).setServedTime(servedDate);
                        if (userMap.isEmpty()) {
                            populateUsers();
                        }
                        m_oTicket.getLine(i).setServedBy(m_oTicket.getUser().getId());
                        String servedName = dlReceipts.getServedName();

                        m_oTicket.getLine(i).setKdsPrepareStatus(servedName);
                        //Update servedTime and Served by  in ServedTransaction Table - code by B.keerthana
                        dlReceipts.updateServedTransactionTime(m_oTicket, m_oTicket.getLine(i).getTbl_orderId(), loginUserId);
                        m_oTicket.getLine(i).setTbl_orderId(m_oTicket.getLine(i).getTbl_orderId());
                        setServedStatus(1);
                        paintKotTicketLine(i, m_oTicket.getLine(i));// TODO add your handling code here:
                        setServedStatus(0);

                        //added newly on 21/07/2016
                        Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                        new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                        if (m_oTicket.getTakeaway().equals("Y")) {
//                            new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=0 WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                        }
                        String splitId = m_oTicket.getSplitSharedId();
                        record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                        if (record != null) {
                            m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                        }
                        logger.info("updated sharedticket.");
                        if (!jTextPhoneNo.getText().equals("")) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "Passing the product information to Momoe in Serve Button Action ;");
                            m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                            MomoePayment.MomoeIntegration(m_oTicket.getLines(), m_oTicket, taxlist, m_App, false, dlSales);
                        }
                    }
                }
                //   }
            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking served" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_m_jBtnServedActionPerformed

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
//        try {
//            stateTransition('\u00a7');
//        } catch (BasicException ex) {
//            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in m_jbtnScaleActionPerformed" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jBtnCancelBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancelBillActionPerformed
        logger.info("Start Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                if (m_oTicket.getLine(i).getIsKot() == 1) {
                    m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
                    JRetailTicketsBagRestaurant.clickCancel();
                    break;
                }
            }
            logger.info("End Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        }

    }//GEN-LAST:event_m_jBtnCancelBillActionPerformed

    private void jTextPhoneNoMomoeNumberPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPhoneNoMomoeNumberPressed

        if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
            return;
        } else {
            try {
                Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
            } catch (IOException ex) {
                Logger.getLogger(JRetailProductLineEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jTextPhoneNoMomoeNumberPressed

    private void jTextPhoneNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPhoneNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPhoneNoActionPerformed

    private void jTextPhoneNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPhoneNoKeyReleased
        Pattern pattern = Pattern.compile(".*[^0-9].*");
        String input = jTextPhoneNo.getText();
        if (pattern.matcher(input).matches()) {
            showMessage(this, "Please enter valid Phone No.");
            jTextPhoneNo.setText("");
        } else if (input.length() > 10) {
            showMessage(this, "Please enter valid Phone No.");
            jTextPhoneNo.setText("");
        }
        m_oTicket.setMomoePhoneNo(jTextPhoneNo.getText());
    }//GEN-LAST:event_jTextPhoneNoKeyReleased

    private void m_jBtnCatDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCatDiscountActionPerformed
//        m_oTicket.setCategoryDiscount(true);
//        logger.info("Start Category Discount Button :"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));  
//        if(m_oTicket.getLinesCount()!=0){  
//        String user = m_oTicket.getUser().getName();
//        String type = "sales";
//        try {
//            String role = dlCustomers.getRolebyName(user);
//             if ("Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role) || "Cashier".equalsIgnoreCase(role) ) {// for admin
//                m_oTicket.setdPerson(m_oTicket.getUser().getId());
//               //Linewise Discount
//                java.util.List<DiscountRateinfo> list=dlReceipts.getDiscountList();
//                  Map<String ,DiscountInfo> dRateMap=new HashMap();
//                    for (DiscountRateinfo dis : list) {
//                   dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
//                    }
//               discountMap=JLineDiscountRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, "",list,dRateMap);
//               m_oTicket.setDiscountMap(discountMap);
//               if(discountMap!=null){
//                populateDiscount(discountMap);   
//               }
//               populateServiceChargeList();
//               populateServiceTaxList();
//               populateTaxList();
//               m_oTicket.refreshTxtFields(1);
//              } 
//           else {
//                JOptionPane.showMessageDialog(jPanel1, "Manager Privilages required for this operation");
//            }
//        }
//  catch (BasicException ex) {
//      logger.info("Order NO."+m_oTicket.getOrderId()+" exception in  discount action"+ex.getMessage());
//            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//      
//    }                                              
//   }       
//        logger.info("End Category Discount Button:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));  
    }//GEN-LAST:event_m_jBtnCatDiscountActionPerformed

    private void jButtonRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemarksActionPerformed
        logger.info("Remarks Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        TableRemarksDialog.showDialog(this, m_oTicket);
        if (!("").equals(m_oTicket.getRemarks()) && m_oTicket.getRemarks() != null) {
            jButtonRemarks.setBackground(Color.green);
            jButtonRemarks.setForeground(Color.black);
        } else {
            jButtonRemarks.setBackground(new Color(95, 110, 120));
            jButtonRemarks.setForeground(Color.white);
        }
    }//GEN-LAST:event_jButtonRemarksActionPerformed

    private void CheckServiceChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckServiceChargeActionPerformed
        System.out.println("status printing Inside function---" + m_oTicket.getLine(0).isTaxExempted());
        boolean updated = checkTicketUpdation();
        System.out.println("updated----" + updated);
        if (!updated) {
            System.out.println("updated inside----" + updated);
            dbUpdatedDate = null;
            if (jCheckServiceCharge.isSelected()) {
                m_oTicket.setTaxExempt(true);
            } else {
                m_oTicket.setTaxExempt(false);
            }
            try {
                populateTaxList();
                m_oTicket.refreshTxtFields(1);
                System.out.println("status printing ---" + m_oTicket.getLine(0).isTaxExempted());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_CheckServiceChargeActionPerformed

    private void jButtonRepeatLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRepeatLinesActionPerformed
        try {

            if (m_oTicket.getLinesCount() == 0) {
                showMessage(this, "No Items are sent to KOT.");
            } else if (m_oTicket.getOrderId() == 0) {
                showMessage(this, "No Items are sent to KOT.");

            } else {
                java.util.List<String> productList = JRepeatLinesPanel.showMessage(JRetailPanelTicket.this, dlSales, m_App, this, "", menuId, m_oTicket);
                if (productList != null) {
                    for (String product : productList) {
                        String productCode = dlSales.getProductCode(product);
                        ProductInfoExt oProduct = dlSales.getProductInfoByCode(productCode);
                        incProduct(oProduct);
                        populateTaxList();
                    }
                }
            }

        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRepeatLinesActionPerformed

    private void populateDiscount(Map<String, DiscountInfo> discountMap) {
        System.out.println("populateDiscount");
        if (discountMap != null) {
            Set<String> keys = discountMap.keySet();
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_oTicket.getLine(i).setDiscountrate("");
                // String catId=m_oTicket.getLine(i).getProductCategoryID();
                // String parentCatId=dlReceipts.getParentCategory(catId);
                String parentCatId = m_oTicket.getLine(i).getParentCatId();
                System.out.println("parentCatId" + parentCatId);
                String discount = "";
                //Checking whether discount assigned for any product category 
                if (keys.contains(parentCatId)) {
                    //if its a rate
                    if (!discountMap.get(parentCatId).getDiscountRate().equals("") && !discountMap.get(parentCatId).getDiscountRate().equals(null)) {
                        discount = discountMap.get(parentCatId).getDiscountRate();
                        m_oTicket.getLine(i).setDiscountrate(discount);
                    }//if its a amount 
                    else {
                        discount = discountMap.get(parentCatId).getDiscountValue();
                        m_oTicket.getLine(i).setDiscountrate(discount);
                    }
                    //  m_oTicket.setLineDiscountRate(m_oTicket.getLine(i),discount);
                }
            }
        }
    }

    private void setKotAndServedOnSplit(RetailTicketInfo splitTicket) {
        int orderId = 0;
        try {
            orderId = dlSales.getNextTicketOrderNumber();
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //NewKds to be added to change orderid on split bill
        //  System.out.println("OldOrderNum"+ m_oTicket.getOrderId()+"TableName"+splitTicket.getPlaceId()+"TableId"+splitTicket.getPlaceId()+"New orderId"+orderId);
        oldordernum = m_oTicket.getOrderId();
        newordernum = orderId;
        splitTicket.setOrderId(orderId);

        int numlines = splitTicket.getLinesCount();
        int servedStatus = 0;
        for (int i = 0; i < numlines; i++) {
            System.out.println("old OrderItem Id" + splitTicket.getLine(i).printMultiply());
            System.out.println("old OrderItem Id" + splitTicket.getLine(i).getTbl_orderId());
            System.out.println("old OrderItem Id" + splitTicket.getLine(i).getProductName());
            splitTicket.getLine(i).setIsKot(1);
            String tbl_orderitemId = UUID.randomUUID().toString();
            tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
            if (splitTicket.getLine(i).getPreparationStatus() != 3) {
                splitTicket.getLine(i).setPreparationStatus(4);
            } else {
                servedStatus = 1;
            }
            //NewKds  added to change orderitemid on split bill
            String oldtableorderitemid = splitTicket.getLine(i).getTbl_orderId();
            //  System.out.println("old OrderItem Id"+oldtableorderitemid+"i"+i+splitTicket.getLine(i).getTbl_orderId()+"New orderItemId"+tbl_orderitemId);
            // System.out.println("kot id "+splitTicket.getLine(i).getKotid());
            dlReceipts.updateServedTransactionSplit(m_oTicket, m_oTicket.getPlaceId(), splitTicket.getLine(i).getTbl_orderId(), tbl_orderitemId, oldordernum, newordernum);
            splitTicket.getLine(i).setTbl_orderId(tbl_orderitemId);
        }


    }

    private final synchronized void setKotServedAndPrintOnSplit(final RetailTicketInfo splitTicket) throws BasicException {

        Transaction t = new Transaction(m_App.getSession()) {
            @Override
            protected Object transact() throws BasicException {
                int orderId = 0;
                orderId = dlSales.getNextTicketOrderNumber();
                splitTicket.setOrderId(orderId);
                //doPrintValidationSplitTicket(splitTicket);
                doPrintValidation();
                String file;
                file = "Printer.Bill";
                try {
                    taxeslogic.calculateTaxes(m_oTicket);
                    consolidateTaxes(m_oTicket);
                    // chargeslogic.calculateCharges(m_oTicket);
                    // staxeslogic.calculateServiceTaxes(m_oTicket);
                    // taxeslogic.calculateTaxes(m_oTicket);
                } catch (TaxesException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit calculateTaxes" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                // calculateServiceChargeSplitTicket(splitTicket);
                // calculateServiceCharge();
                printTicket(file, m_oTicket, m_oTicketExt);
                splitTicket.setPrinted(true);
                final java.util.List<RetailTicketLineInfo> panelLines = splitTicket.getLines();
                for (final RetailTicketLineInfo l : panelLines) {
                    l.setIsKot(1);
                    l.setPreparationStatus(3);
                    String tbl_orderitemId = UUID.randomUUID().toString();
                    tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
                    l.setTbl_orderId(tbl_orderitemId);
                    setServedStatus(1);
                }
                for (int i = 0; i < panelLines.size(); i++) {
                    paintKotTicketLine(i, splitTicket.getLine(i));
                    setServedStatus(0);
                }
                return null;
            }
        };
        t.execute();

    }

    private void printKotTicket(String sresourcename, RetailTicketInfo ticket, kotInfo kot, Object ticketExt, String printerName) {


        String sresource = dlSystem.getResourceAsXML(sresourcename);

        kot.setkotName("Kitchen Order Ticket");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                script.put("kot", kot);
                script.put("place", ticketExt);
                String args[] = null;

                m_TTP.printTicket(script.eval(sresource).toString());
//                aconfig.load();
//                aconfig.setProperty("machine.printer.2", null);
//                try {
//                    aconfig.save();
//                } catch (IOException ex) {
//                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                    System.out.println("aconfig--1111"+aconfig.getProperty("machine.printer.2"));
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            }
        }
    }

    public java.util.ArrayList<BuyGetPriceInfo> getPriceInfo() {
        return pdtBuyGetPriceList;
    }

    public void setPriceInfo(java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList) {
        this.pdtBuyGetPriceList = pdtBuyGetPriceList;
    }

    private synchronized void setBillNo() throws BasicException {    //acquire new bill no for bill print mutual exclusively
        int billNo = dlSales.getNextTicketIndex();  //read from db counter
        m_oTicket.setTicketId(billNo);  //set this billno in pojo & retain it during switchings of tables
    }

    private synchronized void setLoyalityCode() throws BasicException {    //get new LOYALITY CODE for bill print 
        String loyalcode = dlSales.getLoyalityCode();  //read from db 
        m_oTicket.setLoyalcode(loyalcode);  //set this LOYALITY CODE in pojo 
    }

    private synchronized void doPrintValidation() throws BasicException {  //determine suitable bill No for print  
        setLoyalityCode(); //get new LOYALITY CODE
        if (!m_oTicket.isPrinted()) {//CASE 1: determine is it first print for this table
            //first print confirmed
            setBillNo(); //get new bill no from database 
            dlSales.updateLoyalityCode(m_oTicket.getLoyalcode(), m_oTicket.getTicketId());
            m_oTicket.setPrintedLineItems(m_oTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now 
            logger.info("the first print of bill");
            closeInvoiceTicket(m_oTicket); //insert into invoice && invoicelines
        } else { //CASE 2: it is already printed once for sure.
            logger.info("Bill is already printed once for sure");
            dlSales.disableInvoiceTickets(m_oTicket.getTicketId()); //update existing record with inactive status
            m_oTicket.setBillParent(m_oTicket.getTicketId());//set current bill no as billParent
            String oldBillId = m_oTicket.getId();
            String newBillId = UUID.randomUUID().toString();
            newBillId = newBillId.replaceAll("-", "");
            m_oTicket.setId(newBillId);
            setBillNo(); //assign new bill no to this pojo
            dlSales.updateLoyalityCode(m_oTicket.getLoyalcode(), m_oTicket.getTicketId());
            m_oTicket.setPrintedLineItems(m_oTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now
            closeInvoiceTicket(m_oTicket); //insert into invoice && invoice lines
            m_oTicket.setId(oldBillId);
            m_oTicket.setModified(true);    //yes this bill is modified so don't print duplicate copy in receipt
        }



    }

//private synchronized void doPrintValidationSplitTicket(RetailTicketInfo splitTicket) throws BasicException{  //determine suitable bill No for print     
//
//        if(!splitTicket.isPrinted()){//CASE 1: determine is it first print for this table
//            //first print confirmed
//            setBillNo(); //get new bill no from database 
//           splitTicket.setPrintedLineItems(splitTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now           
//           closeInvoiceTicket(splitTicket); //insert into invoice && invoicelines
//        }
//        else{ //CASE 2: it is already printed once for sure.
//                dlSales.disableInvoiceTickets(splitTicket.getTicketId()); //update existing record with inactive status
//                splitTicket.setBillParent(splitTicket.getTicketId());//set current bill no as billParent
//                String oldBillId = splitTicket.getId();
//                String newBillId = UUID.randomUUID().toString();
//                newBillId = newBillId.replaceAll("-", "");
//                splitTicket.setId(newBillId);
//                setBillNo(); //assign new bill no to this pojo
//                splitTicket.setPrintedLineItems(splitTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now
//                closeInvoiceTicket(splitTicket); //insert into invoice && invoice lines
//                splitTicket.setId(oldBillId);
//                splitTicket.setModified(true);    //yes this bill is modified so don't print duplicate copy in receipt
//        }
//    }
    private void closeInvoiceTicket(RetailTicketInfo ticket) throws BasicException {

        try {
            //   chargeslogic.calculateCharges(ticket); 
            //    staxeslogic.calculateServiceTaxes(ticket);
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in closeInvoiceTicket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }



        double creditAmt;
        creditAmt = 0;

        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
        ticket.setActiveCash(m_App.getActiveCashIndex());
        ticket.setDate(new Date()); // Le pongo la fecha de cobro


        //String[] ticketDocNoValue;
        String ticketDocNo = null;
        Integer ticketDocNoInt = null;
        String ticketDocument = null;

        if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
        }

        ticket.setDocumentNo(ticketDocument);
        String chequeNos = "";//m_jtxtChequeNo.getText();
        String deliveryBoy = "";
        double tipsAmt = 0;
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;
        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        String isCredit;
        double advanceissued;
        deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
        if (creditAmt > 0) {
            isCredit = "Y";
        } else {
            isCredit = "N";
        }

        advanceissued = 0;

        String file;

        if (ticket.getLinesCount() == 0) {
            showMessage(this, "Please select the products");
        } else {
            dlSales.saveRetailInvoiceTicket(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmt, "Y", isCredit, isPaidStatus, tipsAmt, orderTaking, "N");
            logger.info("insertion to invoice table");
        }
    }

    public final synchronized void serveAllLines() throws BasicException {

        Transaction t = new Transaction(m_App.getSession()) {
            int isallServed = 0;

            @Override
            protected Object transact() throws BasicException {
                final java.util.List<RetailTicketLineInfo> panelLines = m_oTicket.getLines();
                logger.info("serveAllLines method items are " + panelLines.size());
                for (final RetailTicketLineInfo l : panelLines) {
                    if (l.getPreparationStatus() != 3 && l.getPreparationStatus() != 0) {
                        isallServed = 1;
                        l.setPreparationStatus(3);
                        l.setIsKot(1);
                        l.setTbl_orderId(l.getTbl_orderId());
                        setServedStatus(1);
                    }

                }
                for (int i = 0; i < panelLines.size(); i++) {
                    paintKotTicketLine(i, panelLines.get(i));
                }
                setServedStatus(0);
                return null;
            }
        };
        t.execute();
        logger.info("updated sharedticket");
    }

    private void populateUsers() {
        java.util.List<RoleUserInfo> userList = null;
        try {
            userList = dlReceipts.getUsers();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailKdsDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (RoleUserInfo user : userList) {
            userMap.put(user.getId(), user.getName());
        }

    }

    public void enablePosActions() {

        try {
            posActions = (ArrayList<PosActionsInfo>) dlSales.getPosActions(m_App.getAppUserView().getUser().getRole());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (posActions.size() != 0) {
            if (posActions.get(0).getDiscountAccess().equals("Y")) {
                m_jBtnDiscount.setVisible(true);
            } else {
                m_jBtnDiscount.setVisible(false);
            }
            if (posActions.get(0).getSplitAccess().equals("Y")) {
                m_jSplitBtn.setVisible(true);
            } else {
                m_jSplitBtn.setVisible(false);
            }
            if (posActions.get(0).getCancelAccess().equals("Y")) {
                m_jBtnCancelBill.setVisible(true);
            } else {
                m_jBtnCancelBill.setVisible(false);
            }
            if (posActions.get(0).getSettleAccess().equals("Y")) {
                m_jSettleBill.setVisible(true);
            } else {
                m_jSettleBill.setVisible(false);
            }
            if (posActions.get(0).getPrintAccess().equals("Y")) {
                m_jbtnPrintBill.setVisible(true);
            } else {
                m_jbtnPrintBill.setVisible(false);
            }
            if (posActions.get(0).getServiceChargeAccess().equals("Y")) {
                jCheckServiceCharge.setVisible(true);
            } else {
                jCheckServiceCharge.setVisible(false);
            }

        } else {
            m_jBtnDiscount.setVisible(false);
            m_jBtnCatDiscount.setVisible(false);
            m_jbtnPrintBill.setVisible(false);
            m_jSettleBill.setVisible(false);
            m_jBtnCancelBill.setVisible(false);
            m_jSplitBtn.setVisible(false);
            jCheckServiceCharge.setVisible(false);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel catcontainer;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAddon;
    private javax.swing.JButton jButtonRemarks;
    private javax.swing.JButton jButtonRepeatLines;
    private javax.swing.JCheckBox jCheckServiceCharge;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelCustPhone;
    private javax.swing.JLabel jLabelServiceCharge;
    public static javax.swing.JLabel jLabelServiceTax;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel jLblPrinterStatus;
    private javax.swing.JLabel jMoveTableText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jTaxPanel;
    private javax.swing.JTextField jTextPhoneNo;
    private javax.swing.JButton m_jAction;
    private javax.swing.JButton m_jBtnCancelBill;
    private javax.swing.JButton m_jBtnCatDiscount;
    private javax.swing.JButton m_jBtnDiscount;
    private javax.swing.JButton m_jBtnKot;
    private javax.swing.JButton m_jBtnServed;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JButton m_jCalculatePromotion;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JButton m_jDelete;
    public static javax.swing.JLabel m_jDiscount1;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEraser;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JLabel m_jLblBillNo;
    private javax.swing.JLabel m_jLblCurrentDate;
    private javax.swing.JLabel m_jLblTime;
    private javax.swing.JLabel m_jLblTotalEuros4;
    private javax.swing.JLabel m_jLblTotalEuros5;
    private javax.swing.JLabel m_jLblTotalEuros6;
    private javax.swing.JLabel m_jLblUserInfo;
    private javax.swing.JButton m_jLogout;
    private javax.swing.JButton m_jMinus;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JPanel m_jPanelScripts;
    private javax.swing.JButton m_jPlus;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JPanel m_jProducts;
    public static javax.swing.JLabel m_jServiceCharge;
    public static javax.swing.JLabel m_jServiceTax;
    private javax.swing.JButton m_jSettleBill;
    private javax.swing.JButton m_jSplitBtn;
    public static javax.swing.JLabel m_jSubtotalEuros1;
    private javax.swing.JLabel m_jTable;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JList m_jTaxList;
    public static javax.swing.JLabel m_jTaxesEuros1;
    public static javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JLabel m_jTxtChange;
    private javax.swing.JLabel m_jTxtTotalPaid;
    private javax.swing.JLabel m_jUser;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnPrintBill;
    private javax.swing.JButton m_jbtnScale;
    // End of variables declaration//GEN-END:variables
}
