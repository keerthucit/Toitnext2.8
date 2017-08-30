/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;
import com.openbravo.pos.sales.DataLogicReceipts;
import java.util.Date;

/**
 *
 * @author sysfore
 */
public class ShiftTallyLineInfo implements SerializableRead, SerializableWrite ,Serializable{
//   private static final long serialVersionUID = 7640633837719L;
    
    private String id;
    private String payment_mode;
    private Date txndate;
    private double total;
 
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
 public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    
   
    public Date getTxndate() {
        return txndate;
    }

    public void setTxndate(java.util.Date txndate) {
        this.txndate = txndate;
    }
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
 public ShiftTallyLineInfo(String id, String payment_mode,Date txndate, double total) {
        this.id = id;
        this.payment_mode = payment_mode;
        this.txndate = txndate;
        this.total = total;
    }
   
   
   

    
   
    public void readValues(DataRead dr) throws BasicException {
    id =dr.getString(1);
    payment_mode=dr.getString(2);
 txndate=dr.getTimestamp(3);

    total=dr.getDouble(4);
    

    
    }
    public ShiftTallyLineInfo()
    {
        payment_mode="Cash";
        total=0;
    }
    public ShiftTallyLineInfo copyTicket() {
          System.out.println("copyPayment");
        ShiftTallyLineInfo t = new ShiftTallyLineInfo();
        t.id=id;
        t.payment_mode=payment_mode;
        t.txndate=txndate;
        t.total=total;
        return t;
    }

    public ShiftTallyLineInfo(String payment_mode,double total)
    {
        this.payment_mode=payment_mode;
        this.total=total;
    }
    
    public void writeValues(DataWrite dp) throws BasicException {
dp.setString(1,id);      
dp.setString(2,payment_mode);      
    dp.setTimestamp(3, txndate);
dp.setDouble(4,total);     


    }

  
    
}
