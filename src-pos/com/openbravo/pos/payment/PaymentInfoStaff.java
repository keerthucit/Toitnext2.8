/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.format.Formats;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author shilpa
 */
public class PaymentInfoStaff extends PaymentInfo {
   private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private String staffName;
    private ArrayList<PaymentDetails> staffnumbers;
    
    public PaymentInfoStaff(double dTotal, double dPaid,String staff){//, List cardno) {
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
        staffName=staff;
        //cardnumbers = (ArrayList<PaymentDetails>) cardno;
    }

    /** Creates a new instance of PaymentInfoCash */
    public PaymentInfoStaff(double dTotal, double dPaid){
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
      
    }
  
    @Override
    public PaymentInfo copyPayment() {
        return new PaymentInfoStaff(m_dTotal, m_dPaid,staffName);
    }

    public String getName() {
        return "Staff";
    }

    public double getTotal() {
        return m_dPaid;
    }

    public double getPaid() {
        return m_dPaid;
    }

    public String getTransactionID() {
        return "no ID";
    }

    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid));
    }

    public String printChange() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal));
    }

    public String getID() {
        return m_id;
    }

 
    public List getChequenumbers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List getStaffnumbers() {
        return staffnumbers;
    }

    @Override
    public String getVoucherNo() {
       return staffName;
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMobile() {
return "";    }


}

