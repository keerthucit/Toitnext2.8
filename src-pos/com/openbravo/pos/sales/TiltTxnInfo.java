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

/**
 *
 * @author sysfore
 */
public class TiltTxnInfo implements SerializableRead, Externalizable {

    private String TPid;
    private String sessionId;
    private String paymenttype;
    private Double amount;
    private String tilt;
    private List<TiltTxnInfo> TPLines;

    public TiltTxnInfo() {
        TPid = UUID.randomUUID().toString();
        TPLines = new ArrayList<TiltTxnInfo>();
    }

    public TiltTxnInfo(String TPid, List<TiltTxnInfo> tpLines, String paymentType, String sessionid, Double amt, String til) {
        this.TPid = TPid;
        this.TPLines = tpLines;
        this.paymenttype = paymentType;
        this.sessionId = sessionid;
        this.amount = amt;
        this.tilt = til;
    }

    public void insertLine(int index, TiltTxnInfo tpLine) {
        TPLines.add(index, tpLine);

    }

    public void setLine(int index, TiltTxnInfo tpLine) {
        TPLines.set(index, tpLine);
    }

    public void removeLine(int index) {
        TPLines.remove(index);

    }

    public List<TiltTxnInfo> getTPLines() {
        System.out.println(" getTPLines()" + getTPLines().size());
        return TPLines;
    }

    public void setTPLines(List<TiltTxnInfo> TPLines) {
        this.TPLines = TPLines;
    }

    public String getTPid() {
        return TPid;
    }

    public void setTPid(String TPid) {
        this.TPid = TPid;
    }

    public String getSessionId() {
        System.out.println("sessionId" + sessionId);
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPaymenttype() {
        System.out.println("paymenttype" + paymenttype);
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public Double getAmount() {
        System.out.println("amount" + amount);
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTilt() {
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        System.out.println("readValues");
        TPid = dr.getString(1);
        sessionId = dr.getString(2);
        paymenttype = dr.getString(3);
        amount = dr.getDouble(4);
        tilt = dr.getString(5);
        TPLines = new ArrayList<TiltTxnInfo>();


//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("writeExternal");

        out.writeObject(TPid);
        out.writeObject(sessionId);
        out.writeObject(paymenttype);
        out.writeObject(amount);
        out.writeObject(tilt);

        //List<RetailTicketLineInfo> check = m_aLines;
        try {
            out.writeObject(TPLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("ReadExternal");
        TPid = (String) in.readObject();
        sessionId=(String) in.readObject();
        paymenttype=(String) in.readObject();
        amount=(Double) in.readObject();
        tilt=(String) in.readObject();
        TPLines = (List<TiltTxnInfo>) in.readObject();
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
