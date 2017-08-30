/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.data.gui.ListKeyed;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TaxInfo;
import java.util.List;

/**
 *
 * @author archana
 */
public abstract class ReservationStatus {
    
    public abstract void getResponse();
    
   
       public static void Reservation(String status,String tableName,AppView app)
        {
          new Reservation(tableName, status,app).getResponse();
        }
}
