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
public class PaymentInfoComp extends PaymentInfo {

    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private ArrayList<PaymentDetails> compnumbers;

    /**
     * Creates a new instance of PaymentInfoCash
     */
    public PaymentInfoComp(double dTotal, double dPaid) {
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;

    }

    @Override
    public PaymentInfo copyPayment() {
        return new PaymentInfoStaff(m_dTotal, m_dPaid);
    }

    public String getName() {
        return "Complementary";
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

    public List getCompnumbers() {
        return compnumbers;
    }

    @Override
    public String getVoucherNo() {
        return "";
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMobile() {
        return "";
    }
}