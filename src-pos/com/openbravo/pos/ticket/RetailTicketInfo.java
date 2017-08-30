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
package com.openbravo.pos.ticket;

import bsh.ParseException;
import com.openbravo.pos.forms.BuyGetPriceInfo;
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BillPromoRuleInfo;
import com.openbravo.pos.forms.CustomerListInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.PromoRuleIdInfo;
import com.openbravo.pos.payment.PaymentInfoMagcard;
import com.openbravo.pos.sales.AccessInfo;
import com.openbravo.pos.sales.DiscountInfo;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.JRetailPanelEditTicket;
import com.openbravo.pos.sales.JRetailPanelHomeTicket;
import com.openbravo.pos.sales.JRetailPanelTicket;
import com.openbravo.pos.sales.JTicketLines;
import com.openbravo.pos.sales.kotInfo;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.util.StringUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class RetailTicketInfo implements SerializableRead, Externalizable {

    private static final long serialVersionUID = 2765650092387265178L;
    public static final int RECEIPT_NORMAL = 0;
    public static final int RECEIPT_REFUND = 1;
    public static final int RECEIPT_PAYMENT = 2;
    private static DateFormat m_dateformat = new SimpleDateFormat("MM/dd/yyyy");
    private static DateFormat m_dateformattime = new SimpleDateFormat("hh:mm");
    private String m_sId;
    private int tickettype;
    private int m_iTicketId;
    private java.util.Date m_dDate;
    private Properties attributes;
    private UserInfo m_User;
    private CustomerInfoExt m_Customer;
    private String m_sActiveCash;
    private List<RetailTicketLineInfo> m_aLines;
    private List<PaymentInfo> payments;
    private List<TicketTaxInfo> taxes;
    private String m_sResponse;
    private String CreditNote;
    private Date Currentdate;
    private String m_sActiveDay;
    private AppView m_App;
    private String promoRuleId;
    private double billDiscount;
    private double taxValue;
    private double billValue;
    public java.util.ArrayList<BillPromoRuleInfo> billPromoRuleList;
    protected DataLogicSales dlSales;
    protected RetailTicketInfo m_oTicket;
//     protected TicketInfo m_oTicket1;
    protected JTicketLines m_ticketlines;
    protected JPanelTicket jPanel;
    public java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList;
    public ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
    public double leastValueDiscount;
    private String customerDiscount;
    private static boolean userate;
    private static int discountFlag;// = 0;
    private String rate = "0";
    private String dPerson;
    private double dAmt;
    private String dName;
    double discountAmt;
    private String documentNo;
    int printStatus;
    private List<kotInfo> kotValues;
    private String placeId;
    private double serviceCharge;
    private double serviceTax;
    private String serviceChargeId;
    private String serviceTaxId;
    private double serviceTaxRate;
    private double serviceChargeRate;
    private String splitValue;
    private int noOfCovers;
    private String hdAddress;
    private static boolean cancelTicket;
    private String parentId;
    private String discountReasonId;
    private String splitSharedtkId;
    //store the discount reason for this ticket if discount is applied
    private boolean Printed = false; //represents the printed status of this table at any point of time.
    private boolean Modified = false; //flag to understand , the bill is modified or not if ever printed
    private int billParent;   //flag to store the previous bill no , who was printed previously then this bill print
    private List<RetailTicketLineInfo> printedLineItems; //sorted list of line items  printed recently from this table
    private int m_orderId;
    private boolean ticketOpen = false;
    private String tableName;
    private String loyalcode;
    //newly added variables 
    private List<TicketTaxInfo> servicetaxes;
    private List<TicketServiceChargeInfo> charges;
    private double chargeValue;
    private String errMsg = "";
    private String oldTableName;
    private String momoePhoneNo;
    private boolean categoryDiscount = false;
    private Map<String, DiscountInfo> discountMap;
    private String discountReasonText;
    private List<ResettlePaymentInfo> paymentInfo;
    private Map<Double, TaxMapInfo> taxMap;
    private double billTotal;
    private Date objectUpdateDate;
    private String remarks;
    private String discountSubReasonId;
    private String movedUser;
    private String movedTime;
    private String discountComments;
    private List<RetailTicketLineInfo> nonServedLines;
    private String takeaway;
    private ArrayList<AccessInfo> accessInfo;
    private boolean taxExempt = false;
    private Map<String, NameTaxMapInfo> nametaxMap;

    public boolean isTicketOpen() {
        return ticketOpen;
    }

    public void setTicketOpen(boolean ticketOpen) {
        this.ticketOpen = ticketOpen;
    }

    /**
     * Creates new TicketModel
     */
    public RetailTicketInfo() {
        m_sId = UUID.randomUUID().toString();
        m_sId = m_sId.replaceAll("-", "");
        tickettype = RECEIPT_NORMAL;
        m_iTicketId = 0; // incrementamos
        m_dDate = new Date();
        attributes = new Properties();
        m_User = null;
        m_Customer = null;
        dAmt = 0;
        rate = "0";
        m_sActiveCash = null;
        m_aLines = new ArrayList<RetailTicketLineInfo>(); // vacio de lineas
        payments = new ArrayList<PaymentInfo>();
        taxes = null;
        m_sResponse = null;
        documentNo = null;
        splitValue = "";
        parentId = null;
        discountReasonId = null;
        splitSharedtkId = null;
        printedLineItems = new ArrayList<RetailTicketLineInfo>();
        Printed = false;
        Modified = false;
        billParent = 0;
        m_orderId = 0;
        ticketOpen = false;
        tableName = null;
        loyalcode = null;
        servicetaxes = null;
        charges = null;
        errMsg = "";
        oldTableName = null;
        momoePhoneNo = "";
        categoryDiscount = false;
        discountMap = new HashMap();
        discountReasonText = null;
        paymentInfo = new ArrayList<ResettlePaymentInfo>();
        taxMap = new HashMap();
        billTotal = 0;
        objectUpdateDate = null;
        remarks = null;
        discountSubReasonId = null;
        movedUser = null;
        movedTime = null;
        discountComments = null;
        takeaway = "N";
        accessInfo = new ArrayList<AccessInfo>();
        taxExempt = false;
        nametaxMap = new HashMap();

    }

    public RetailTicketInfo(String rate) {
        m_sId = UUID.randomUUID().toString();
        m_sId = m_sId.replaceAll("-", "");
        tickettype = RECEIPT_NORMAL;
        m_iTicketId = 0; // incrementamos
        m_dDate = new Date();
        attributes = new Properties();
        m_User = null;
        m_Customer = null;
        dAmt = 0;
        this.rate = rate;
        m_sActiveCash = null;
        m_aLines = new ArrayList<RetailTicketLineInfo>(); // vacio de lineas
        payments = new ArrayList<PaymentInfo>();
        taxes = null;
        m_sResponse = null;
        documentNo = null;
        parentId = null;
        discountReasonId = null;
        splitValue = "";
        splitSharedtkId = null;
        printedLineItems = new ArrayList<RetailTicketLineInfo>();
        Printed = false;
        Modified = false;
        billParent = 0;
        m_orderId = 0;
        ticketOpen = false;
        servicetaxes = null;
        charges = null;
        oldTableName = null;
        momoePhoneNo = "";
        categoryDiscount = false;
        discountReasonText = null;
        taxMap = null;
        billTotal = 0;
        objectUpdateDate = null;
        remarks = null;
        discountSubReasonId = null;
        movedUser = null;
        movedTime = null;
        discountComments = null;
        takeaway = "N";
        accessInfo = new ArrayList<AccessInfo>();
        taxExempt = false;
        nametaxMap = null;

    }

    //Print before billing,settlement,exiting table
    public void writeExternal(ObjectOutput out) throws IOException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        //   System.out.println("writeExternal");
        //     System.out.println("writeExternal" + errMsg);
        out.writeObject(m_sId);
        out.writeInt(tickettype);
        out.writeInt(m_iTicketId);
        out.writeObject(m_Customer);
        out.writeObject(m_dDate);
        out.writeObject(attributes);
        out.writeDouble(dAmt);
        out.writeObject(rate);
        //List<RetailTicketLineInfo> check = m_aLines;
        try {
            out.writeObject(m_aLines);
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.writeObject(parentId);
        out.writeObject(discountReasonId);
        out.writeObject(splitValue);
        out.writeObject(splitSharedtkId);
        out.writeObject(printedLineItems);
        out.writeBoolean(Printed);
        out.writeBoolean(Modified);
        out.writeInt(getBillParent());
        out.writeObject(documentNo);
        out.writeInt(m_orderId);
        out.writeBoolean(ticketOpen);
        out.writeObject(m_User);
        out.writeDouble(serviceCharge);
        out.writeDouble(serviceTax);
        out.writeDouble(serviceTaxRate);
        out.writeDouble(serviceChargeRate);
        out.writeObject(tableName);
        out.writeObject(loyalcode);
        out.writeObject(oldTableName);
        out.writeObject(momoePhoneNo);
        out.writeObject(discountMap);

        out.writeObject(discountReasonText);
        out.writeObject(getPaymentInfo());
        //charges=null;
        try {
            out.writeObject(charges);
            out.writeObject(placeId);
            out.writeObject(taxMap);
            out.writeDouble(billTotal);
            out.writeObject(objectUpdateDate);
            out.writeBoolean(categoryDiscount);
            out.writeObject(remarks);
            out.writeObject(discountSubReasonId);
            out.writeObject(movedUser);
            out.writeObject(movedTime);
            out.writeObject(discountComments);
            out.writeObject(takeaway);
            out.writeObject(getAccessInfo());
            out.writeBoolean(taxExempt);
            out.writeObject(nametaxMap);

        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("e..." + e.getMessage());
        }


    }

    //entering table,reprint after billing
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        m_sId = (String) in.readObject();
        tickettype = in.readInt();
        m_iTicketId = in.readInt();
        m_Customer = (CustomerInfoExt) in.readObject();
        m_dDate = (Date) in.readObject();

        attributes = (Properties) in.readObject();
        dAmt = (Double) in.readDouble();
        rate = (String) in.readObject();

        m_aLines = (List<RetailTicketLineInfo>) in.readObject();
        m_User = null;
        m_sActiveCash = null;

        payments = new ArrayList<PaymentInfo>();
        taxes = null;
        parentId = (String) in.readObject();
        discountReasonId = (String) in.readObject();
        splitValue = (String) in.readObject();
        splitSharedtkId = ((String) in.readObject());
        printedLineItems = (List<RetailTicketLineInfo>) in.readObject();
        Printed = in.readBoolean();
        Modified = in.readBoolean();
        setBillParent(in.readInt());
        documentNo = (String) in.readObject();
        m_orderId = in.readInt();
        ticketOpen = in.readBoolean();
        //  refreshTxtFields(1);
        m_User = (UserInfo) in.readObject();
        serviceCharge = in.readDouble();
        serviceTax = in.readDouble();
        serviceTaxRate = in.readDouble();
        //    System.out.println("serviceTaxRate" + serviceTaxRate);
        serviceChargeRate = in.readDouble();
        //    System.out.println("serviceChargeRate" + serviceChargeRate);
        try {
            tableName = (String) in.readObject();
            loyalcode = (String) in.readObject();
            oldTableName = (String) in.readObject();
            momoePhoneNo = (String) in.readObject();
            discountMap = (Map<String, DiscountInfo>) in.readObject();
            discountReasonText = (String) in.readObject();
            paymentInfo = ((List<ResettlePaymentInfo>) in.readObject());
            charges = (List<TicketServiceChargeInfo>) in.readObject();
            charges = null;
            servicetaxes = null;
            placeId = (String) in.readObject();
            taxMap = (Map<Double, TaxMapInfo>) in.readObject();
            billTotal = in.readDouble();
            objectUpdateDate = (Date) in.readObject();
            categoryDiscount = in.readBoolean();
            remarks = (String) in.readObject();
            discountSubReasonId = ((String) in.readObject());
            movedUser = ((String) in.readObject());
            movedTime = ((String) in.readObject());
            discountComments = ((String) in.readObject());
            takeaway = ((String) in.readObject());
            setAccessInfo((ArrayList<AccessInfo>) in.readObject());
            taxExempt = in.readBoolean();
            nametaxMap = ((Map<String, NameTaxMapInfo>) in.readObject());

        } catch (IOException e) {
            // System.out.println("ERROR MSG PRINT :" + errMsg);
            e.printStackTrace();
        }
    }

    public void readValues(DataRead dr) throws BasicException {
        //   System.out.println("readValues");
        m_sId = dr.getString(1);
        tickettype = dr.getInt(2).intValue();
        m_iTicketId = dr.getInt(3).intValue();
        m_dDate = dr.getTimestamp(4);
        m_sActiveCash = dr.getString(5);
        try {
            byte[] img = dr.getBytes(6);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }
        m_User = new UserInfo(dr.getString(7), dr.getString(8));
        m_Customer = new CustomerInfoExt(dr.getString(9));
        dAmt = dr.getDouble(10).doubleValue();
        //    System.out.println("dr.getString(11)--" + dr.getString(11));
        rate = dr.getString(11);
        documentNo = dr.getString(12);
        m_aLines = new ArrayList<RetailTicketLineInfo>();

        payments = new ArrayList<PaymentInfo>();
        taxes = null;
        servicetaxes = null;
        charges = null;

    }

    public RetailTicketInfo copyTicket() {
        //   System.out.println("copyTicket");
        RetailTicketInfo t = new RetailTicketInfo();

        t.tickettype = tickettype;
        t.m_iTicketId = m_iTicketId;
        t.m_dDate = m_dDate;
        t.m_sActiveCash = m_sActiveCash;
        t.attributes = (Properties) attributes.clone();
        t.m_User = m_User;
        t.m_Customer = m_Customer;
        t.dAmt = dAmt;
        //System.out.println("rate---copy--"+rate);
        t.rate = rate;
        t.documentNo = documentNo;
        t.m_aLines = new ArrayList<RetailTicketLineInfo>();
        for (RetailTicketLineInfo l : m_aLines) {
            t.m_aLines.add(l.copyTicketLine());
        }
        t.refreshLines();

        t.payments = new LinkedList<PaymentInfo>();
        for (PaymentInfo p : payments) {
            t.payments.add(p.copyPayment());
        }
        // t.parentId=parentId;
        // taxes are not copied, must be calculated again.
        t.Printed = Printed;
        t.Modified = Modified;
        t.setBillParent(getBillParent());
        t.m_orderId = m_orderId;
        t.ticketOpen = ticketOpen;
        t.oldTableName = oldTableName;
        t.momoePhoneNo = momoePhoneNo;
        t.taxMap = taxMap;
        t.billTotal = billTotal;
        t.objectUpdateDate = objectUpdateDate;
        t.categoryDiscount = categoryDiscount;
        t.remarks = remarks;
        t.discountSubReasonId = discountSubReasonId;
        t.movedUser = movedUser;
        t.movedTime = movedTime;
        t.discountComments = discountComments;
        t.takeaway = takeaway;
        t.setAccessInfo(getAccessInfo());
        t.taxExempt = taxExempt;
        t.nametaxMap = nametaxMap;

        return t;
    }

    public RetailTicketInfo copySplitTicket(String rate) {
        //  System.out.println("copyTicket");
        RetailTicketInfo t = new RetailTicketInfo();
        String uuid = UUID.randomUUID().toString();
        uuid = m_sId.replaceAll("-", "");
        // t.m_sId=m_sId;
        t.m_sId = uuid;
        t.tickettype = tickettype;
        t.m_iTicketId = m_iTicketId;
        t.m_dDate = m_dDate;
        t.m_sActiveCash = m_sActiveCash;
        t.attributes = (Properties) attributes.clone();
        t.m_User = m_User;
        t.m_Customer = m_Customer;
        t.dAmt = dAmt;
        //System.out.println("rate---copy--"+rate);
        t.rate = rate;
        t.documentNo = documentNo;
        t.m_aLines = new ArrayList<RetailTicketLineInfo>();
        for (RetailTicketLineInfo l : m_aLines) {
            //   System.out.println("Table Order Id"+l.getTbl_orderId());
            t.m_aLines.add(l.copyTicketLine());
        }
        t.refreshLines();

        t.payments = new LinkedList<PaymentInfo>();
        for (PaymentInfo p : payments) {
            t.payments.add(p.copyPayment());
        }

        // taxes are not copied, must be calculated again.
        t.Printed = Printed;
        t.Modified = Modified;
        t.setBillParent(getBillParent());
        t.ticketOpen = ticketOpen;
        t.oldTableName = oldTableName;
        t.momoePhoneNo = momoePhoneNo;
        t.discountMap = discountMap;
        t.categoryDiscount = categoryDiscount;
        t.setPaymentInfo(getPaymentInfo());
        // t.taxMap=taxMap;
        t.billTotal = billTotal;
        t.objectUpdateDate = objectUpdateDate;
        t.categoryDiscount = categoryDiscount;
        t.remarks = remarks;
        t.discountSubReasonId = discountSubReasonId;
        t.movedUser = movedUser;
        t.movedTime = movedTime;
        t.discountComments = discountComments;
        t.takeaway = takeaway;
        t.setAccessInfo(getAccessInfo());
        t.taxExempt = taxExempt;
        return t;
    }

    public RetailTicketInfo copyEditTicket(String rate) {
        //    System.out.println("copyEditTicket");
        RetailTicketInfo t = new RetailTicketInfo();

        t.tickettype = tickettype;
        t.m_iTicketId = m_iTicketId;
        t.m_dDate = m_dDate;
        t.m_sActiveCash = m_sActiveCash;
        t.attributes = (Properties) attributes.clone();
        t.m_User = m_User;
        t.m_Customer = m_Customer;
        t.dAmt = dAmt;
        //    System.out.println("rate---copy--"+rate);
        this.rate = rate;
        t.documentNo = documentNo;
        t.m_aLines = new ArrayList<RetailTicketLineInfo>();
        for (RetailTicketLineInfo l : m_aLines) {
            t.m_aLines.add(l.copyTicketLine());
        }
        t.refreshLines();

        t.payments = new LinkedList<PaymentInfo>();
        for (PaymentInfo p : payments) {
            t.payments.add(p.copyPayment());
        }
        t.Printed = Printed;
        t.Modified = Modified;
        t.setBillParent(getBillParent());
        t.m_orderId = m_orderId;
        // taxes are not copied, must be calculated again.
        t.ticketOpen = ticketOpen;
        t.categoryDiscount = categoryDiscount;
        t.taxMap = taxMap;
        t.billTotal = billTotal;
        t.objectUpdateDate = objectUpdateDate;
        t.categoryDiscount = categoryDiscount;
        t.remarks = remarks;
        t.discountSubReasonId = discountSubReasonId;
        t.movedUser = movedUser;
        t.movedTime = movedTime;
        t.discountComments = discountComments;
        t.takeaway = takeaway;
        t.setAccessInfo(getAccessInfo());
        t.taxExempt = taxExempt;
        t.nametaxMap = nametaxMap;

        return t;
    }

    public String getId() {
        return m_sId;
    }

    public void setId(String m_sId) {
        this.m_sId = m_sId;
    }

    public int getTicketType() {
        return tickettype;
    }

    public void setTicketType(int tickettype) {
        this.tickettype = tickettype;
    }

    public int getTicketId() {
        return m_iTicketId;
    }

    public void setTicketId(int iTicketId) {
        m_iTicketId = iTicketId;
        // refreshLines();
    }

    public String getName(Object info) {

        StringBuffer name = new StringBuffer();

        if (getCustomerId() != null) {
            name.append(m_Customer.toString());
            name.append(" - ");
        }

        if (info == null) {
            //    if (m_iTicketId == 0) {
            name.append(m_dateformat.format(m_dDate) + " " + m_dateformattime.format(m_dDate) + ":" + Long.toString(m_dDate.getTime() % 1000));
            // name.append("(" + m_dateformat.format(m_dDate) +  ")");

//            } else {
//                name.append(Integer.toString(m_iTicketId));
//            }
        } else {
            name.append(info.toString());
        }

        return name.toString();
    }

    public String getName() {
        return getName(null);
    }

    public java.util.Date getDate() {
        return m_dDate;
    }

    public void setDate(java.util.Date dDate) {
        m_dDate = dDate;
    }

    public UserInfo getUser() {
        return m_User;
    }

    public void setUser(UserInfo value) {
        m_User = value;
    }

    public CustomerInfoExt getCustomer() {
        return m_Customer;
    }

    public String getCreditNote() {
        return CreditNote;
    }

    public void setCreditNote(String creditNo) {
        CreditNote = creditNo;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public void setCustomer(CustomerInfoExt value) {
        m_Customer = value;
    }

    public String getCustomerId() {
        if (m_Customer == null) {
            return null;
        } else {
            return m_Customer.getId();
        }
    }

    public String getTransactionID() {
        return (getPayments().size() > 0)
                ? (getPayments().get(getPayments().size() - 1)).getTransactionID()
                : StringUtils.getCardNumber(); //random transaction ID
    }

    public String getReturnMessage() {
        return ((getPayments().get(getPayments().size() - 1)) instanceof PaymentInfoMagcard)
                ? ((PaymentInfoMagcard) (getPayments().get(getPayments().size() - 1))).getReturnMessage()
                : LocalRes.getIntString("button.ok");
    }

    public void setActiveCash(String value) {
        m_sActiveCash = value;
    }

    public String getActiveCash() {
        return m_sActiveCash;
    }

    public String getProperty(String key) {
        return attributes.getProperty(key);
    }

    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }

    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }

    public Properties getProperties() {
        return attributes;
    }

    public RetailTicketLineInfo getLine(int index) {
        return m_aLines.get(index);
    }

    public void addLine(RetailTicketLineInfo oLine) {
        oLine.setTicket(m_sId, m_aLines.size());
        m_aLines.add(oLine);
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getServiceTax() {
        System.out.println("servicetax" + serviceTax);
        return serviceTax == 0.0 ? 0.0 : serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getServiceChargeId() {
        return serviceChargeId;
    }

    public void setServiceChargeId(String serviceChargeId) {
        this.serviceChargeId = serviceChargeId;
    }

    public String getServiceTaxId() {
        return serviceTaxId;
    }

    public void setServiceTaxId(String serviceTaxId) {
        this.serviceTaxId = serviceTaxId;
    }

    public double getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(double serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    public double getServiceTaxRate() {
        return serviceTaxRate;
    }

    public void setServiceTaxRate(double serviceTaxRate) {
        this.serviceTaxRate = serviceTaxRate;
    }

    public void insertLine(int index, RetailTicketLineInfo oLine) {
        m_aLines.add(index, oLine);
        refreshLines();
    }

    public void setLine(int index, RetailTicketLineInfo oLine) {
        oLine.setTicket(m_sId, index);
        m_aLines.set(index, oLine);
    }

    public void removeLine(int index) {
        m_aLines.remove(index);
        refreshLines();
    }

    public int getProductIndex(String productId) {
        int result = 0;
        for (int i = 0; i < m_aLines.size(); i++) {
            if (productId.equals(m_aLines.get(i).getProductID()) && m_aLines.get(i).getPrice() == 0) {
                result = i;
                break;
            }
        }

        return result;

    }

    public int getSplitProductIndex(String productId) {
        int result = 0;
        for (int i = 0; i < m_aLines.size(); i++) {
            if (productId.equals(m_aLines.get(i).getProductID())) {
                result = i;
                break;
            }
        }

        return result;

    }

    private void refreshLines() {
        for (int i = 0; i < m_aLines.size(); i++) {
            getLine(i).setTicket(m_sId, i);
        }
    }

    public int getLinesCount() {
        return m_aLines.size();
    }

    public double getArticlesCount() {
        double dArticles = 0.0;
        RetailTicketLineInfo oLine;

        for (Iterator<RetailTicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dArticles += oLine.getMultiply();
        }

        return dArticles;
    }

    public double getSubTotal() {
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            sum += line.getSubValue();
        }

        return sum;
    }

    public double getSubtotalBeforeDiscount() {
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            sum += line.getSubValueBeforeDiscount();
        }
        //     System.out.println("printing sum  " + sum);
        return sum;
    }

    public double getSubtotalAfterDiscount() {
        double subtotalValue;
        //  System.out.println("getRate()---"+getRate());
        if (getdAmt() != 0) {
            //       System.out.println("enetr if");
            subtotalValue = getSubTotal() - getdAmt();
        } else {
            //     System.out.println("enetr else");
            subtotalValue = getSubTotal() - (getSubTotal() * convertRatetodouble(getRate()));
        }
        return subtotalValue;
    }

    public double getEditSubtotal() {
        double subtotalValue;

        subtotalValue = getSubTotal() - getdAmt();

        return subtotalValue;
    }

    public double getTotalPrice() {
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            //   sum += line.getCurrentPrice();
        }
        return sum;
    }

    public double getTax() {

        double sum = 0.0;
        if (hasTaxesCalculated()) {
            for (TicketTaxInfo tax : taxes) {
                sum += tax.getTax(); // Taxes are already rounded...
            }
        } else {
            for (RetailTicketLineInfo line : m_aLines) {
                sum += line.getTax();
            }
        }
        return sum;
    }

    public double getRetailTax() {

        double sum = 0.0;
        if (hasTaxesCalculated()) {
            //   System.out.println("hasTaxesCalculated");
            for (TicketTaxInfo tax : taxes) {
                sum += tax.getRetailTax(); // Taxes are already rounded...
            }
        } else {
            //  System.out.println("hasTaxesCalculated not"); 
            for (RetailTicketLineInfo line : m_aLines) {
                sum += line.getTaxWithServiceCharge();
            }
        }
        return sum;
    }

    public double getDiscount() {
        double discount = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            discount += line.getDiscount();
        }
        return discount;
    }

    public double getLineDiscount() {
        double discount = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            discount += line.getLineDiscountPrice();
        }
        return discount;
    }

    public double getTotalBeforeTax() {
        // return getSubTotal() + getTax();
        //  return Math.ceil(getSubtotalAfterDiscount() + getTaxAfterDiscount());
        //      return getSubtotalAfterDiscount() + getTaxAfterDiscount();
        return getSubTotal() + getTaxAfterDiscount();
    }

    public double getEditTotal() {
        //    System.out.println("---"+getSubtotalAfterDiscount()+"---" + getTaxAfterDiscount()+"---"+getServiceTax()+"---"+getServiceCharge());
        billTotal = getEditSubtotal() + getTaxAfterDiscount() + getServiceTax() + getServiceCharge();
        if ((billTotal % 1) > 0.5) {
            billTotal = Math.ceil(billTotal);
            return billTotal;
        } else {
            billTotal = Math.floor(billTotal);
            return billTotal;
        }

    }

    public double getRoundOffvalue() {
        return getTotal() - getTotalWithoutRoundOff();
    }

    public double getDiscountValue() {
        double discountValue = getLineDiscount();
        return discountValue;
    }

    public double getSaletotal() {
        return (getTotal() - (getDiscount() + getBillDiscount() + getLeastValueDiscount() + getTaxValue() + getdAmt()));
    }

    public String getRefundTotal() {
        String total;
        String[] value;

        total = Double.toString(getSubTotal() + getTax());
        value = total.split("-");
        return value[1];
    }

    public double getTotalPaid() {

        double sum = 0.0;
        for (PaymentInfo p : payments) {
            if (!"debtpaid".equals(p.getName())) {
                sum += p.getTotal();
            }
        }
        return sum;
    }

    public double getLeastValueDiscount() {
        return leastValueDiscount;
    }

    public void setLeastValueDiscount(double leastValueDiscount) {
        this.leastValueDiscount = leastValueDiscount;
    }

    public Date getNewDate() {

        /*Calendar now = Calendar.getInstance();
         int days = Integer.parseInt(m_App.getProperties().getValidity());
         now.add(Calendar.DATE,days);
         DateFormat formatter ;
         formatter = new SimpleDateFormat("dd-MM-yyyy");
         String str_date = (now.get(Calendar.DATE))+ "-"+ (now.get(Calendar.MONTH) + 1)+ "-"+ (now.get(Calendar.YEAR));
         try {
         Currentdate = (Date) formatter.parse(str_date);
         // Currentdate =;
         } catch (java.text.ParseException ex) {
         Logger.getLogger(RetailTicketInfo.class.getName()).log(Level.SEVERE, null, ex);
         }
         // Currentdate =;
        
         // Currentdate =;*/

        return Currentdate;
    }

    public void setNewDate(java.util.Date Currentdate) {
        this.Currentdate = Currentdate;
    }

    public List<RetailTicketLineInfo> getLines() {
        return m_aLines;
    }

    public String getPromotionRule() {
        return promoRuleId;
    }

    public void setPromotionRule(String promoRuleId) {
        this.promoRuleId = promoRuleId;
    }

    public void setLines(List<RetailTicketLineInfo> l) {
        m_aLines = l;
    }

    public List<PaymentInfo> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentInfo> l) {
        payments = l;
    }

    public void resetPayments() {
        payments = new ArrayList<PaymentInfo>();
    }

    public List<TicketTaxInfo> getTaxes() {
        return taxes;
    }

    public boolean hasTaxesCalculated() {
        return taxes != null;
    }

    public void setTaxes(List<TicketTaxInfo> l) {
        taxes = l;
    }

    public void resetTaxes() {
        taxes = null;
    }

    //using in print bill xml
    public TicketTaxInfo getTaxLine(TaxInfo tax) {

        for (TicketTaxInfo taxline : taxes) {
            if (tax.getId().equals(taxline.getTaxInfo().getId())) {
                return taxline;
            }
        }

        return new TicketTaxInfo(tax);
    }

    //not using
    public TicketTaxInfo[] getTaxLines() {

        Map<String, TicketTaxInfo> m = new HashMap<String, TicketTaxInfo>();

        RetailTicketLineInfo oLine;
        for (Iterator<RetailTicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();

            TicketTaxInfo t = m.get(oLine.getTaxInfo().getId());
            if (t == null) {
                t = new TicketTaxInfo(oLine.getTaxInfo());
                m.put(t.getTaxInfo().getId(), t);
            }
            t.add(oLine.getSubValue());

        }

        // return dSuma;       
        Collection<TicketTaxInfo> avalues = m.values();
        return avalues.toArray(new TicketTaxInfo[avalues.size()]);
    }

    public String printId() {
        if (m_iTicketId > 0) {
            // valid ticket id
            //  return Formats.INT.formatValue(new Integer(m_iTicketId));
            return (new Integer(m_iTicketId)).toString();
        } else {
            return "";
        }
    }

    public String printDate() {
        return Formats.TIMESTAMP.formatValue(m_dDate);
    }

    public String printPhoneNo() {
        return m_Customer.getPhone() == null ? "" : m_Customer.getPhone();

    }

    public String printAddress() {
        return m_Customer.getAddress() == null ? "" : m_Customer.getAddress();

    }

    public String printAddress1() {
        return m_Customer.getAddress2() == null ? "" : m_Customer.getAddress2();

    }

    public String printCity() {
        return m_Customer.getCity() == null ? "" : m_Customer.getCity();

    }

    public String printUser() {
        return m_User == null ? "" : m_User.getName();
    }

    public String getDateForPrint() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(m_dDate).toString();
    }

    public String printCustomer() {
        return m_Customer == null ? "" : m_Customer.getName();
    }

    public String printArticlesCount() {
        return Formats.DOUBLE.formatValue(new Double(getArticlesCount()));
    }

    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }

    public String printSubTotalValue() {
        // return Formats.CURRENCY.formatValue(new Double(getSubTotal()-getLeastValueDiscount()));
        //     return Formats.CURRENCY.formatValue(new Double(getSubtotalAfterDiscount()));
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }

    public String printSubTotalValueBeforeDiscount() {
        return Formats.CURRENCY.formatValue(new Double(getSubtotalBeforeDiscount()));
    }

    public String printTax() {
        //   System.out.println("getTaxAfterDiscount()--" + getTaxAfterDiscount() + "--" + getServiceTax());
        return Formats.CURRENCY.formatValue(new Double(getTaxAfterDiscount() + getServiceTax()));
    }

    public String printDocumentNo() {
        return getDocumentNo();
    }

    public String printBillDiscount() {
        return Formats.CURRENCY.formatValue(new Double(getBillDiscount()));
    }

    public double getTotalDiscount() {
        return getBillDiscount() + getDiscount() + getLeastValueDiscount() + getdAmt();
    }

    public String printTotalDiscount() {
        return Formats.CURRENCY.formatValue(new Double(getTotalDiscount()));
    }

    public String printEditSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getEditSubtotal()));
    }

    public String printTotal() {

        //return Formats.CURRENCY.formatValue(new Double(getTotal()-(getDiscount()+getBillDiscount()+getLeastValueDiscount()+getTaxValue()+getdAmt())));
        return Formats.CURRENCY.formatValue(new Double(getTotal()));

    }

    //newly called methods for reprint settled bill option (by shilpa)
//     public String rePrintTotal() {
//         return Formats.CURRENCY.formatValue(new Double(getRePrintTotal()));
//     }
//      public String rePrintSubTotal() {
//        return Formats.CURRENCY.formatValue(new Double(getRePrintSubTotal()));
//    }
//    
//    public String rePrintSubTotalValue() {
//      return Formats.CURRENCY.formatValue(new Double(getRePrintSubTotal()));
//    }
//    
//    
//    public double getRePrintTotal() {
//       // return Math.ceil(getRePrintSubTotal() + getTaxAfterDiscount()+getServiceTax()+getServiceCharge());
//        return Math.ceil(getRePrintSubTotal() );
//    }
//    
//     public double getRePrintSubTotal() {
//        double sum = 0.0;
//        for (RetailTicketLineInfo line : m_aLines) {
//            sum += line.getrePrintSubValue();
//        }
//         
//        return sum;
//    }
    public String printTicketId() {

        return Integer.toString(getTicketId());

    }

    public String printEditTotal() {

        //return Formats.CURRENCY.formatValue(new Double(getTotal()-(getDiscount()+getBillDiscount()+getLeastValueDiscount()+getTaxValue()+getdAmt())));
        return Formats.CURRENCY.formatValue(new Double(getEditTotal()));

    }

    public String printServiceCharge() {

        //return Formats.CURRENCY.formatValue(new Double(getTotal()-(getDiscount()+getBillDiscount()+getLeastValueDiscount()+getTaxValue()+getdAmt())));
        return Formats.CURRENCY.formatValue(new Double(getServiceCharge()));

    }

    public String printServiceTax() {

        //return Formats.CURRENCY.formatValue(new Double(getTotal()-(getDiscount()+getBillDiscount()+getLeastValueDiscount()+getTaxValue()+getdAmt())));
        return Formats.CURRENCY.formatValue(new Double(getServiceTax()));

    }

    public String printServiceTaxRate() {
        return Formats.PERCENT.formatValue(new Double(getServiceTaxRate()));
    }

    public String printServiceChargeRate() {
        return Formats.PERCENT.formatValue(new Double(getServiceChargeRate()));
    }

    public String printDiscountRate() {

        //return Formats.CURRENCY.formatValue(new Double(getTotal()-(getDiscount()+getBillDiscount()+getLeastValueDiscount()+getTaxValue()+getdAmt())));
        double rate = convertRatetodouble(getRate());
        return Formats.PERCENT.formatValue(rate);

    }

    public String printTotalValue() {
        return Formats.CURRENCY.formatValue(new Double(getTotal() - (getDiscount() + getBillDiscount() + getTaxValue() + getdAmt())));
    }

    public String printTotalPaid() {
        return Formats.CURRENCY.formatValue(new Double(getTotalPaid()));
    }

    public String printDateForReceipt() {
        return getDateForPrint();
    }

    public String printRefundTotal() {
        return Formats.CURRENCY.formatValue(new Double(getRefundTotal()));
    }

    public String printCreditNote() {
        return getCreditNote();
    }

    public String printValidDateForPrint() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(getNewDate()).toString();
    }

    public void setActiveDay(String value) {
        m_sActiveDay = value;
    }

    public String getActiveDay() {
        return m_sActiveDay;
    }

    public void setticketLine(RetailTicketInfo m_oTicket) {
        this.m_oTicket = m_oTicket;
    }

    public RetailTicketInfo getticketLine() {
        return m_oTicket;
    }

    public void setPanel(JPanelTicket panel) {
        this.jPanel = panel;
    }

    public JPanelTicket getPanel() {
        return jPanel;
    }

    public java.util.ArrayList<PromoRuleIdInfo> getPromoList() {
        return promoRuleIdList;
    }

    public void setPromoList(java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList) {
        this.promoRuleIdList = promoRuleIdList;
    }

    public void setJTicketLines(JTicketLines m_ticketlines) {
        this.m_ticketlines = m_ticketlines;
    }

    public JTicketLines getJTicketLines() {
        return m_ticketlines;
    }

    public void setDatalogic(DataLogicSales dlSales) {
        this.dlSales = dlSales;
    }

    public DataLogicSales getDatalogic() {
        return dlSales;
    }

    public void setBillDiscount(double billDiscount) {
        this.billDiscount = billDiscount;
    }

    public double getBillDiscount() {
        return billDiscount;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setBillValue(double billValue) {
        this.billValue = billValue;
    }

    public double getBillValue() {
        return billValue;
    }

    public double billValuePromotion(java.util.ArrayList<PromoRuleIdInfo> promoRule, DataLogicSales dlSales) {
        int billPromotionCount = 0;
        double billDiscount = 0;
        java.util.ArrayList<String> promoId = new ArrayList<String>();

        for (int i = 0; i < promoRule.size(); i++) {
            promoId.add(promoRule.get(i).getpromoRuleId());
        }
        StringBuilder b = new StringBuilder();
        Iterator<?> it = promoId.iterator();
        while (it.hasNext()) {
            b.append(it.next());
            if (it.hasNext()) {
                b.append(',');
            }
        }
        String promoRuleId = b.toString();

        try {
            billPromotionCount = dlSales.getBillPromotionCount(promoRuleId);
        } catch (BasicException ex) {
            Logger.getLogger(RetailTicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        double billTotal = (getTotal() - (getDiscount() + getLeastValueDiscount()));
        if (billPromotionCount != 0) {
            try {
                billPromoRuleList = (ArrayList<BillPromoRuleInfo>) dlSales.getBillPromoRuleDetails(promoRuleId, billTotal);
            } catch (BasicException ex) {
                Logger.getLogger(RetailTicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (BillPromoRuleInfo bp : billPromoRuleList) {
                double billPromoValue = billPromoRuleList.get(0).getBillAmount();
                double value = billPromoRuleList.get(0).getValue();
                double billDiscountAmount = 0;
                double totalPrice = 0;

                if (billPromoRuleList.get(0).getisPrice().equals("Y")) {
                    if (billTotal >= billPromoValue) {
                        billDiscount = billPromoRuleList.get(0).getValue();
                    }
                } else {
                    if (billTotal >= billPromoValue) {
                        if (billDiscount == 0) {
                            billDiscount = billTotal * (value / 100);
                        }
                    }

                }
            }
        }
        setBillDiscount(billDiscount);
        return billDiscount;
    }

    public ArrayList<BuyGetPriceInfo> getPriceInfo() {
        return pdtLeastPriceList;
    }

    public void setPriceInfo(ArrayList<BuyGetPriceInfo> pdtLeastPriceList) {
        this.pdtLeastPriceList = pdtLeastPriceList;
    }

    public String printCustomerDiscount() {


        //return  discountTotal == 0.0?Formats.CURRENCY.formatValue(new Double(0.0)):"-"+Formats.CURRENCY.formatValue(new Double(discountTotal));
        double disc = new Double(getdAmt()) + getPriceDiscountTotal();
        return "-" + Formats.CURRENCY.formatValue(disc);
    }

    public double getPriceDiscountTotal() {
        // discountrate2 = "true";
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            //    sum += line.getpriceDiscountTotal(); // line.getTax() is extra added
        }
        return sum;
    }
//    public double getDiscountTotal() {
//
////        double loc = 0;
////        if (getDiscountFlag()==2) {
////            //number entered in txtbox
////            loc = convertRatetodouble(getRate());
////            setdAmt(loc);
////            //return loc;
////        } else if (getDiscountFlag()==1){
////            //combo box selected
////            loc = (getSaletotal()) * calculateDiscount();
////            setdAmt(loc);
////           // return loc;
////        }
//return loc;
//    }
//    public setDiscountTotal

    public double convertRatetodouble(String rate) {
        double d = 0.0;
        if (rate == null || rate.equals("")) {
            d = 0;
        } else {
            d = Double.parseDouble(rate);
        }
        return d;
    }

    private double calculateDiscount() {
        double ddiscount = 0.0;
        try {
            String getdisco = getCustomerDiscount();
            ddiscount = Double.parseDouble(getdisco);
            if (ddiscount == 0) {
                return 0;
            }
        } catch (Exception e) {
        }
        return ddiscount;
    }

    public String getCustomerDiscount() {
        try {
            if (customerDiscount.equals("") || customerDiscount == null) {
                customerDiscount = "0";
            }
        } catch (NullPointerException np) {
            customerDiscount = "0";
        }
        return customerDiscount;
    }

    public void setCustomerDiscount(String discustomerDiscountcount) {
        this.customerDiscount = customerDiscount;
        //refreshTxtFields();


    }
//public void refreshTxtFields() {
//
//        //if combo box val selected
//        JPanelTicket.m_jSubtotalEuros.setText(printSubTotal());
//        JPanelTicket.m_jTaxesEuros.setText(printTax());
//        JPanelTicket.m_jTotalEuros.setText(printTotal());
//        double disc = 0.0;
//        double discountTotal = 0.0;
//        Double parsedd = Double.parseDouble(getDiscount());
//        Double PricediscountTotal = getPriceDiscountTotal();
//        double total = getSubTotal() + getTax();
//
//        if (this.getPidiscount().equals("#") || this.getPidiscount().equals("0")) {
//            disc = (parsedd * total) + PricediscountTotal;
//        } else if (this.getPidiscount().equals("1")) {
//            disc = (Double.parseDouble(getdName()) * total) + PricediscountTotal;
//        }
//        /*
//        else if(this.getPidiscount().equals("0")){
//            disc = PricediscountTotal;
//        }
//         *
//         */
//        JPanelTicket.m_jTDiscount.setText(Formats.CURRENCY.formatValue(disc));
//        this.setdAmt(disc);
//        // this.setdAmt(discountTotal);
//    }
//

    public void refreshTxtFields(int i) {
        //    System.out.println("refreshTxtFields---" + i);
        double discountTotal = 0.0;
        //if data entered in text field

        //  Double PricediscountTotal = getPriceDiscountTotal();
        double disc = 0.0;
        if (i == 1) {
            disc = (getSubTotal()) * convertRatetodouble(getRate());
            setdAmt(disc);

        } else if (i == 2) {
            //combo box selected
            disc = convertRatetodouble(getRate());
            setdAmt(disc);
            // return loc;
        } else {
            setdAmt(0);
        }
        //  JRetailPanelTicket.calculateServiceCharge();

        JRetailPanelTicket.m_jSubtotalEuros1.setText(printSubTotalValueBeforeDiscount());
        JRetailPanelTicket.m_jTaxesEuros1.setText(printTax());
        JRetailPanelTicket.m_jTotalEuros.setText(printTotal());
        JRetailPanelTicket.m_jDiscount1.setText(printDiscount());
        if (printServiceCharge() != null) {
            //jLabel13.setVisible(true);
            //   JRetailPanelTicket.m_jServiceCharge.setText(printServiceCharge());
        }
        if (printServiceTax() != null) {
            //    JRetailPanelTicket.jLabelServiceTax.setVisible(true);
            //  JRetailPanelTicket.m_jServiceTax.setText(printServiceTax());
        }

    }

    public void refreshEditTxtFields(int i) {
        double discountTotal = 0.0;
        //if data entered in text field

        Double PricediscountTotal = getPriceDiscountTotal();
        double disc = 0.0;
        if (i == 1) {
            disc = (getSubTotal()) * convertRatetodouble(getRate());
            setdAmt(disc);

        } else if (i == 2) {
            //combo box selected
            disc = convertRatetodouble(getRate());
            setdAmt(disc);
            // return loc;
        } else {
            setdAmt(0);
        }
//        JRetailPanelTicket.calculateServiceCharge();

        JRetailPanelEditTicket.m_jSubtotalEuros.setText(printSubTotalValue());
        JRetailPanelEditTicket.m_jTaxesEuros.setText(printTax());
        JRetailPanelEditTicket.m_jTotalEuros.setText(printTotal());
        JRetailPanelEditTicket.m_jDiscount.setText(printDiscount());



    }

    public void refreshHomeTxtFields(int i) {
        double discountTotal = 0.0;
        //if data entered in text field

        Double PricediscountTotal = getPriceDiscountTotal();
        double disc = 0.0;
        if (i == 1) {
            disc = (getSubTotal()) * convertRatetodouble(getRate());
            setdAmt(disc);
            //number entered in txtbox
            //   disc = convertRatetodouble(getRate());
            //  setdAmt(disc);
            //return loc;
        } else if (i == 2) {
            //combo box selected
            disc = convertRatetodouble(getRate());
            setdAmt(disc);
            // return loc;
        } else {
            setdAmt(0);
        }


        JRetailPanelHomeTicket.m_jSubtotalEuros.setText(printSubTotalValue());
        JRetailPanelHomeTicket.m_jDiscount.setText(printDiscount());
        JRetailPanelHomeTicket.m_jTaxesEuros.setText(printTax());
        JRetailPanelHomeTicket.m_jTotalEuros.setText(printTotal());

    }

    public double getTaxAfterDiscount() {

        double taxAfterDiscount = 0;
        double totalTax = getRetailTax();//-getTaxValue();
        //if(getRate()!="0"){
        //   taxAfterDiscount = totalTax - (totalTax * convertRatetodouble(getRate()));
        //   }else{
        taxAfterDiscount = totalTax;
        //  }
        return taxAfterDiscount;
    }

    public String getdPerson() {
        return dPerson;
    }

    /**
     * @param dPerson the dPerson to set
     */
    public void setdPerson(String dPerson) {
        this.dPerson = dPerson;
    }

    /**
     * @return the dAmt
     */
    public double getdAmt() {
        return dAmt;
    }

    /**
     * @param dAmt the dAmt to set
     */
    public void setdAmt(double dAmt) {
        this.dAmt = dAmt;
    }

    /**
     * @return the dName
     */
    public String getdName() {
        return dName;
    }

    /**
     * @param dName the dName to set
     */
    public void setdName(String dName) {
        this.dName = dName;
    }

    /**
     * @return the userate
     */
    public static boolean isUserate() {
        return userate;
    }

    /**
     * @param aUserate the userate to set
     */
    public static void setUserate(boolean aUserate) {
        userate = aUserate;
    }

    /**
     * @return the rateDi
     */
    public String getRate() {

        return rate;
    }

    /**
     * @param aRate the rate to set
     */
    public void setRate(String rate) {
        this.rate = rate;
        setTicketLinesDiscount();
        //refreshTxtFields(1);

    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

//     public List<kotInfo> getKotLines() {
//        return kotValues;
//    }
    public void setKotLines(List<kotInfo> l) {
        kotValues = l;
    }

    public int getKotLinesCount() {
        return kotValues.size();
    }

    public void setPlaceid(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setNoOfCovers(int noOfCovers) {
        this.noOfCovers = noOfCovers;
    }

    public int getNoOfCovers() {
        return noOfCovers;
    }

    public int printNoOfCovers() {
        return getNoOfCovers();
    }

    public String getSplitValue() {
        return splitValue;
    }

    public void setSplitValue(String splitValue) {
        this.splitValue = splitValue;
    }

    public String getHdAddress() {
        return hdAddress;
    }

    public void setHdAddress(String hdAddress) {
        this.hdAddress = hdAddress;
    }

    public static boolean getCancelTicket() {
        return cancelTicket;
    }

    public static void setCancelTicket(boolean cancelTicket) {
        RetailTicketInfo.cancelTicket = cancelTicket;
    }

    public String printHdAddress() {
        return getHdAddress();
    }

    public String printTicketCount() {

        int count = 0;
        count = (int) getArticlesCount();
        return Formats.INT.formatValue(count);
    }

    /**
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the splitParentId
     */
    //Return new list of line-items of pos screen by consolidating all duplicate items into single entry
    public List<RetailTicketLineInfo> getUniqueLines() {
        List<RetailTicketLineInfo> allLines = new ArrayList<RetailTicketLineInfo>(); // duplicate list of original list

        for (RetailTicketLineInfo lineItem : m_aLines) {
            //adding one line to check the kot status (by shilpa ) especially used when moving table with printed bill
            if (lineItem.getIsKot() == 1) {
                allLines.add(lineItem.copyTicketLine()); //create duplicate of each line item and add it to duplicate list
            }
        }
        List<RetailTicketLineInfo> uniqueLines = new ArrayList<RetailTicketLineInfo>(); //consolidated list of all line-items
        Set<String> productNames = new HashSet<String>(); //set dealing with names of products with no duplicate
        Set<String> ZeroValueproductNames = new HashSet<String>();
        for (RetailTicketLineInfo lineItem : allLines) { //iterate over all items 
            String productName = lineItem.getProductName();
            if (lineItem.getPrice() == lineItem.getActualPrice()) {
                if (productNames.add(productName)) { //if this product name correspond to fresh entry in a set
                    uniqueLines.add(lineItem);      //then add this line-item in unique list
                } else {   //else if set do not allow duplicate entry then locate the existing entry in list of unique item 
                    double qty = lineItem.getMultiply();    // read the quantity of this duplicate line item to be added in existing entry. .
                    //now find existing entry corresponding to this consecutive entry of same product
                    findExistingEntry:
                    {
                        for (RetailTicketLineInfo addedLineItem : uniqueLines) {  //traverse through unique list 

                            if (productName.equals(addedLineItem.getProductName()) && addedLineItem.getPrice() == addedLineItem.getActualPrice()) { //if entry matches to consecutive entry name.
                                int indexExistingItem = uniqueLines.indexOf(addedLineItem); //store its index 
                                double existingQuantity = uniqueLines.get(indexExistingItem).getMultiply(); //read qty of existing entry in unique List.
                                uniqueLines.get(indexExistingItem).setMultiply(existingQuantity + qty); //add qty of consecutive entry to existing one.
                                break findExistingEntry;   //dont keep on traversing unique list if one match is found, just break the loop
                            }
                        }
                    }
                }
            } else {
                //   System.out.println("else of 0 value item 1");
                if (ZeroValueproductNames.add(productName)) { //if this product name correspond to fresh entry in a set
                    //     System.out.println("else of 0 value item 2");
                    uniqueLines.add(lineItem);      //then add this line-item in unique list
                } else {   //else if set do not allow duplicate entry then locate the existing entry in list of unique item 
                    double qty = lineItem.getMultiply();    // read the quantity of this duplicate line item to be added in existing entry. .
                    //now find existing entry corresponding to this consecutive entry of same product
                    findExistingEntry:
                    {
                        for (RetailTicketLineInfo addedLineItem : uniqueLines) {  //traverse through unique list 

                            if (productName.equals(addedLineItem.getProductName()) && addedLineItem.getPrice() != addedLineItem.getActualPrice()) { //if entry matches to consecutive entry name.

                                int indexExistingItem = uniqueLines.indexOf(addedLineItem); //store its index 
                                double existingQuantity = uniqueLines.get(indexExistingItem).getMultiply(); //read qty of existing entry in unique List.
                                uniqueLines.get(indexExistingItem).setMultiply(existingQuantity + qty); //add qty of consecutive entry to existing one.
                                break findExistingEntry;   //dont keep on traversing unique list if one match is found, just break the loop
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(uniqueLines, RetailTicketLineInfo.productNameComparator);
        return uniqueLines; //return this consolidated list
    }

    public String printTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        return sdf.format(m_dDate);
        // return Formats.TIME.formatValue(m_dDate);
    }

    /**
     * @return the discountReasonId
     */
    public String getDiscountReasonId() {
        return discountReasonId;
    }

    /**
     * @param discountReasonId the discountReasonId to set
     */
    public void setDiscountReasonId(String discountReasonId) {
        this.discountReasonId = discountReasonId;
    }

    /**
     * @return the splitSharedtkId
     */
    public String getSplitSharedId() {
        return splitSharedtkId;
    }

    /**
     * @param splitSharedtkId the splitSharedtkId to set
     */
    public void setSplitSharedId(String splitSharedtkId) {
        this.splitSharedtkId = splitSharedtkId;
    }

    public String toString() {
        return "Ticket ID " + m_sId;
    }

    /**
     * @return the Printed
     */
    public boolean isPrinted() {
        return Printed;
    }

    /**
     * @param Printed the Printed to set
     */
    public void setPrinted(boolean Printed) {
        this.Printed = Printed;
    }

    /**
     * @return the Modified
     */
    public boolean isModified() {
        return Modified;
    }

    /**
     * @param Modified the Modified to set
     */
    public void setModified(boolean Modified) {
        this.Modified = Modified;
    }

    /**
     * @return the billParent
     */
    public int getBillParent() {
        return billParent;
    }

    /**
     * @param billParent the billParent to set
     */
    public void setBillParent(int billParent) {
        this.billParent = billParent;
    }

    /**
     * @return the printedLineItems
     */
    public List<RetailTicketLineInfo> getPrintedLineItems() {
        return printedLineItems;
    }

    /**
     * @param printedLineItems the printedLineItems to set
     */
    public void setPrintedLineItems(List<RetailTicketLineInfo> printedLineItems) {
        this.printedLineItems = printedLineItems;
    }

    //Determine is there any modification, in list of line items since last bill - print taken.
    public boolean isListModified() {
        List<RetailTicketLineInfo> originalListSorted = getUniqueLines(); // get duplicate of original list of line items in consolidated and sorted form.
        //CASE 1: first just compare the sizes of originala list & last printed list.
        if (originalListSorted.size() != printedLineItems.size()) //if they differ in sizes, then its sure list is modified.
        {
            return true;
        } else {   //CASE 2: if they are of same size.
            machEachLine:
            {  //mach details of each line item among two lists, linearly & comprehensively.
                for (int i = 0; i < originalListSorted.size(); i++) { //start iterating on originallist
                    RetailTicketLineInfo ol = originalListSorted.get(i);    // retrieve (i)th line item of originallist copy
                    String prodName1 = ol.getProductName(); //extract item's product name
                    double prodQty1 = ol.getMultiply(); //extract item's quantity
                    double lineTotal1 = ol.getSubValueBeforeDiscount();   //extract item's totalamount after discount on all quantities
                    double discountvalue1 = ol.getDiscount();// to check line wise total discount
                    boolean exempt1 = ol.isTaxExempted();
                    RetailTicketLineInfo respectiveItem = printedLineItems.get(i);  // retrieve (i)th line item of printedlist
                    String prodName2 = respectiveItem.getProductName(); //extract item's product name
                    double prodQty2 = respectiveItem.getMultiply(); //extract item's quantity
                    double lineTotal2 = respectiveItem.getSubValueBeforeDiscount();   //extract item's totalamount after discount on all quantities
                    double discountvalue2 = respectiveItem.getDiscount();// to check line wise total discount
                    boolean exempt2 = respectiveItem.isTaxExempted();
                    // System.out.println("exempt1-- "+exempt1+" exempt2---"+exempt2);
                    if ((prodName1.equals(prodName2) && (prodQty1 == prodQty2) && (lineTotal1 == lineTotal2) && (discountvalue1 == discountvalue2) && (exempt1 == exempt2))) {
                        continue;    //if all three details matches , the continue iteration till last item in list
                    } else {
                        return true;    //if any single item differ from its counterpart item at same index, declare modification and exit
                    }
                }
            }
            return false;   //as both lists are exactly same, so declare No modification in printed list.
        }
    }

    private void setTicketLinesDiscount() { // read the discount value from RTicketInfo & assign to list of line items
        double discountPercent = this.convertRatetodouble(this.getRate()) * 100;
        for (RetailTicketLineInfo line : m_aLines) {
            line.setDiscount(discountPercent);   //set discount to each line item
        }
    }

    /**
     * @return the m_orderId
     */
    public int getOrderId() {
        return m_orderId;
    }

    /**
     * @param m_orderId the m_orderId to set
     */
    public void setOrderId(int m_orderId) {
        this.m_orderId = m_orderId;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the loyalcode
     */
    public String getLoyalcode() {
        return loyalcode;
    }

    /**
     * @param loyalcode the loyalcode to set
     */
    public void setLoyalcode(String loyalcode) {
        this.loyalcode = loyalcode;
    }

    public String printLoyalCode() {

        return getLoyalcode();

    }

    //newly added methods to calculate line level service charge and service tax
    public List<TicketServiceChargeInfo> getCharges() {
        return charges;
    }

    public boolean hasServiceChargeCalculated() {
        return charges != null;
    }

    public void setCharges(List<TicketServiceChargeInfo> l) {
        charges = l;
    }

    public void resetCharges() {
        charges = null;
    }

    public TicketServiceChargeInfo getSChargeLine(ServiceChargeInfo charge) {

        for (TicketServiceChargeInfo chargeline : charges) {
            if (charge.getId().equals(chargeline.getServiceChargeInfo().getId())) {
                return chargeline;
            }
        }

        return new TicketServiceChargeInfo(charge);
    }

    public double getLineServiceCharge() {

        double sum = 0.0;
        if (hasServiceChargeCalculated()) {
            for (TicketServiceChargeInfo charge : charges) {
                sum += charge.getRetailSCharge(); // Taxes are already rounded...
            }
        } else {
            for (RetailTicketLineInfo line : m_aLines) {
                sum += line.getLineServiceCharge();
            }
        }
        return sum;
    }

    public TicketServiceChargeInfo[] getChargeLines() {

        Map<String, TicketServiceChargeInfo> m = new HashMap<String, TicketServiceChargeInfo>();

        RetailTicketLineInfo oLine;
        for (Iterator<RetailTicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();

            TicketServiceChargeInfo t = m.get(oLine.getChargeInfo().getId());
            if (t == null) {
                t = new TicketServiceChargeInfo(oLine.getChargeInfo());
                m.put(t.getServiceChargeInfo().getId(), t);
            }
            t.add(oLine.getSubValue(), this);

        }

        // return dSuma;       
        Collection<TicketServiceChargeInfo> avalues = m.values();
        return avalues.toArray(new TicketServiceChargeInfo[avalues.size()]);
    }

    public List<TicketTaxInfo> getServiceTaxes() {
        return servicetaxes;
    }

    public boolean hasServiceTaxesCalculated() {
        return servicetaxes != null;
    }

    public void setServiceTaxes(List<TicketTaxInfo> l) {
        servicetaxes = l;
    }

    public void resetServiceTaxes() {
        servicetaxes = null;
    }

    public TicketTaxInfo getServiceTaxLine(TaxInfo tax) {

        for (TicketTaxInfo taxline : servicetaxes) {
            if (tax.getId().equals(taxline.getTaxInfo().getId())) {
                return taxline;
            }
        }

        return new TicketTaxInfo(tax);
    }

    public double getRetailServiceTax() {

        double sum = 0.0;
        if (hasServiceTaxesCalculated()) {
            for (TicketTaxInfo tax : servicetaxes) {
                sum += tax.getRetailTax(); // Taxes are already rounded...
            }
        } else {
            for (RetailTicketLineInfo line : m_aLines) {
                sum += line.getTaxWithServiceCharge();
            }
        }
        return sum;
    }

    public void setChargeValue(double chargeValue) {
        this.chargeValue = chargeValue;
    }

    public double getChargeValue() {
        return chargeValue;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * @return the oldTableName
     */
    public String getOldTableName() {
        return oldTableName;
    }

    /**
     * @param oldTableName the oldTableName to set
     */
    public void setOldTableName(String oldTableName) {
        this.oldTableName = oldTableName;
    }

    /**
     * @return the momoePhoneNo
     */
    public String getMomoePhoneNo() {
        return momoePhoneNo;
    }

    /**
     * @param momoePhoneNo the momoePhoneNo to set
     */
    public void setMomoePhoneNo(String momoePhoneNo) {
        this.momoePhoneNo = momoePhoneNo;
    }

    public boolean iscategoryDiscount() {
        return categoryDiscount;
    }

    public void setCategoryDiscount(boolean categoryDisc) {
        this.categoryDiscount = categoryDisc;
    }

    public double getSubTotalOnCatDiscount() {
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            sum += line.getSubValueOnProductCat();
        }

        return sum;
    }

//     public double getTotalWithoutRoundOff(){
//        return getSubTotal() + getTaxAfterDiscount()+getServiceTax()+getServiceCharge();
//    }
//     public String printDiscount() {
//        //return Formats.CURRENCY.formatValue(new Double(getLineDiscount()+getLeastValueDiscount()+getdAmt()));
//        return Formats.CURRENCY.formatValue(new Double(getLineDiscount()));
//    }
    public double getTotal() {
        // return getSubTotal() + getTax();
        //  return Math.ceil(getSubtotalAfterDiscount() + getTaxAfterDiscount());
        //      return getSubtotalAfterDiscount() + getTaxAfterDiscount();
        //System.out.println("-paymewnt--"+getSubTotal()+"---" + getTaxAfterDiscount()+"---"+getServiceTax()+"---"+getServiceCharge());
        if (this.iscategoryDiscount()) {
            billTotal = (getSubTotalOnCatDiscount() + getTaxAfterDiscount() + getServiceTax() + getServiceCharge());
            if ((billTotal % 1) > 0.5) {
                billTotal = Math.ceil(billTotal);
                //  return Math.ceil(billTotal);
                return billTotal;
            } else {
                billTotal = Math.floor(billTotal);
                return billTotal;
            }
        } else {
            billTotal = (getSubTotal() + getTaxAfterDiscount() + getServiceTax() + getServiceCharge());
            if ((billTotal % 1) > 0.5) {
                billTotal = Math.ceil(billTotal);
                return billTotal;
            } else {
                billTotal = Math.floor(billTotal);
                return billTotal;
            }
        }
    }

    public double getTotalWithoutRoundOff() {
        if (this.iscategoryDiscount()) {
            return getSubTotalOnCatDiscount() + getTaxAfterDiscount() + getServiceTax() + getServiceCharge();
        } else {
            return getSubTotal() + getTaxAfterDiscount() + getServiceTax() + getServiceCharge();
        }
    }

    public String printDiscount() {
        //return Formats.CURRENCY.formatValue(new Double(getLineDiscount()+getLeastValueDiscount()+getdAmt()));
        if (this.iscategoryDiscount()) {
            return Formats.CURRENCY.formatValue(new Double(getLineDiscountOnCategory()));
        } else {
            return Formats.CURRENCY.formatValue(new Double(getLineDiscount()));
        }
    }

    public double getLineDiscountOnCategory() {
        double discount = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            discount += line.getLineDiscountPriceOnProductCat();
        }
        return discount;
    }

    public double getSubTotalOnCategoryDiscount() {
        double sum = 0.0;
        for (RetailTicketLineInfo line : m_aLines) {
            sum += line.getSubValueOnProductCat();
        }

        return sum;
    }

    /**
     * @return the discountMap
     */
    public Map<String, DiscountInfo> getDiscountMap() {
        return discountMap;
    }

    /**
     * @param discountMap the discountMap to set
     */
    public void setDiscountMap(Map<String, DiscountInfo> discountMap) {
        this.discountMap = discountMap;
    }

    /**
     * @return the discountReasontText
     */
    public String getDiscountReasonText() {
        return discountReasonText;
    }

    /**
     * @param discountReasontText the discountReasontText to set
     */
    public void setDiscountReasontText(String discountReasontText) {
        this.discountReasonText = discountReasontText;
    }

    /**
     * @return the paymentInfo
     */
    public List<ResettlePaymentInfo> getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * @param paymentInfo the paymentInfo to set
     */
    public void setPaymentInfo(List<ResettlePaymentInfo> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * @return the taxMap
     */
    public Map<Double, TaxMapInfo> getTaxMap() {
        return taxMap;
    }

    /**
     * @param taxMap the taxMap to set
     */
    public void setTaxMap(Map<Double, TaxMapInfo> taxMap) {
        this.taxMap = taxMap;
    }

    /**
     * @return the billTotal
     */
    public double getBillTotal() {
        return billTotal;
    }

    /**
     * @param billTotal the billTotal to set
     */
    public void setBillTotal(double billTotal) {
        this.billTotal = billTotal;
    }

    /**
     * @return the objectUpdateDate
     */
    public Date getObjectUpdateDate() {
        return objectUpdateDate;
    }

    /**
     * @param objectUpdateDate the objectUpdateDate to set
     */
    public void setObjectUpdateDate(Date objectUpdateDate) {
        this.objectUpdateDate = objectUpdateDate;
    }

    /**
     * @return the m_App
     */
    public AppView getM_App() {
        return m_App;
    }

    /**
     * @param m_App the m_App to set
     */
    public void setM_App(AppView m_App) {
        this.m_App = m_App;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the discountSubReasonId
     */
    public String getDiscountSubReasonId() {
        return discountSubReasonId;
    }

    /**
     * @param discountSubReasonId the discountSubReasonId to set
     */
    public void setDiscountSubReasonId(String discountSubReasonId) {
        this.discountSubReasonId = discountSubReasonId;
    }

    /**
     * @return the movedUser
     */
    public String getMovedUser() {
        return movedUser;
    }

    /**
     * @param movedUser the movedUser to set
     */
    public void setMovedUser(String movedUser) {
        this.movedUser = movedUser;
    }

    /**
     * @return the movedTime
     */
    public String getMovedTime() {
        return movedTime;
    }

    /**
     * @param movedTime the movedTime to set
     */
    public void setMovedTime(String movedTime) {
        this.movedTime = movedTime;
    }

    /**
     * @return the discountComments
     */
    public String getDiscountComments() {
        return discountComments;
    }

    /**
     * @param discountComments the discountComments to set
     */
    public void setDiscountComments(String discountComments) {
        this.discountComments = discountComments;
    }

    public List<RetailTicketLineInfo> getNonServedLines() {
        return nonServedLines;
    }

    public void setNonServedLines(List<RetailTicketLineInfo> nonServedLines) {
        this.nonServedLines = nonServedLines;
    }

    /**
     * @return the takeaway
     */
    public String getTakeaway() {
        return takeaway;
    }

    /**
     * @param takeaway the takeaway to set
     */
    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    /**
     * @return the accessInfo
     */
    public ArrayList<AccessInfo> getAccessInfo() {
        return accessInfo;
    }

    /**
     * @param accessInfo the accessInfo to set
     */
    public void setAccessInfo(ArrayList<AccessInfo> accessInfo) {
        this.accessInfo = accessInfo;
    }

    /**
     * @return the taxExempt
     */
    public boolean isTaxExempt() {
        return taxExempt;
    }

    /**
     * @param taxExempt the taxExempt to set
     */
    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    /**
     * @return the nametaxMap
     */
    public Map<String, NameTaxMapInfo> getNametaxMap() {
        return nametaxMap;
    }

    /**
     * @param nametaxMap the nametaxMap to set
     */
    public void setNametaxMap(Map<String, NameTaxMapInfo> nametaxMap) {
        this.nametaxMap = nametaxMap;
    }
}
