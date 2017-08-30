/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sysfore
 */
public class TiltBalanceInfo implements SerializableRead, Externalizable {

    private String Txnid;
    private String sessionId;
    private String paymenttype;
    private String isopen;
    private double openingamount;
    private double closingamount;
    private String tilt;
    private String remarks;

    public String getTilt() {
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }
    private List<TiltBalanceInfo> TLines;

    public TiltBalanceInfo() {
        Txnid = UUID.randomUUID().toString();
        TLines = new ArrayList<TiltBalanceInfo>();
    }

    public TiltBalanceInfo(String TxnId, List<TiltBalanceInfo> tLines, String sessionid, String paymentType, String isOpen,
            double openingAmount, double closingAmount, String Tilt, String Remarks) {
        this.Txnid = TxnId;
        this.TLines = tLines;

        this.sessionId = sessionid;
        this.paymenttype = paymentType;
        this.isopen = isOpen;
        this.openingamount = openingAmount;
        this.closingamount = closingAmount;
        this.tilt = Tilt;
        this.remarks = Remarks;
    }

//    
//    public TiltTxnInfo(String TPid, List<TiltTxnInfo> tpLines, String paymentType, String sessionid, Double amt, String til) {
//        this.TPid = TPid;
//        this.TPLines = tpLines;
//        this.paymenttype = paymentType;
//        this.sessionId = sessionid;
//        this.amount = amt;
//        this.tilt = til;
//    }
    public List<TiltBalanceInfo> getTLines() {
        return TLines;
    }

    public void setTLines(List<TiltBalanceInfo> TLines) {
        this.TLines = TLines;
    }

    public void insertLine(int index, TiltBalanceInfo tLine) {
        TLines.add(index, tLine);

    }

    public void setLine(int index, TiltBalanceInfo tLine) {
        TLines.set(index, tLine);
    }

    public void removeLine(int index) {
        TLines.remove(index);

    }

    public String getTxnid() {
        return Txnid;
    }

    public void setTxnid(String Txnid) {
        this.Txnid = Txnid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String isIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public double getOpeningamount() {
        return openingamount;
    }

    public void setOpeningamount(double openingamount) {
        this.openingamount = openingamount;
    }

    public double getClosingamount() {
        return closingamount;
    }

    public void setClosingamount(double closingamount) {
        this.closingamount = closingamount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
      //  System.out.println("readValues");
try{
        Txnid = dr.getString(1);

        sessionId = dr.getString(2);
        paymenttype = dr.getString(3);
        isopen = dr.getString(4);
        openingamount = dr.getDouble(5);
        if(dr.getDouble(6).equals(" ")||dr.getDouble(6).equals((null))||dr.getDouble(6).equals((0)))
        { closingamount=0.0;       
        }
        else
            closingamount = dr.getDouble(6);
        tilt = dr.getString(7);
        remarks = dr.getString(8);
        TLines = new ArrayList<TiltBalanceInfo>();
       // System.out.println(Txnid + paymenttype + tilt);
        } catch (NullPointerException ex) {
            Logger.getLogger(TiltBalanceInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
       // System.out.println("writeExternal");

        out.writeObject(Txnid);
        out.writeObject(sessionId);
        out.writeObject(paymenttype);
        out.writeObject(isopen);
        out.writeObject(openingamount);
        out.writeObject(closingamount);
        out.writeObject(tilt);
        out.writeObject(remarks);
        //List<RetailTicketLineInfo> check = m_aLines;
        try {
            out.writeObject(TLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
     //   System.out.println("ReadExternal");
        Txnid = (String) in.readObject();

        sessionId = (String) in.readObject();
        paymenttype = (String) in.readObject();
        isopen = (String) in.readObject();
        openingamount = (Double) in.readObject();
        closingamount = (Double) in.readObject();
        tilt = (String) in.readObject();
        remarks = (String) in.readObject();
        TLines = (List<TiltBalanceInfo>) in.readObject();

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
