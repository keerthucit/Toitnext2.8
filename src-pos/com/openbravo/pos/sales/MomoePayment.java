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
public abstract class MomoePayment {
    
    public abstract void getResponse();
    
   
       public static void MomoeIntegration(List<RetailTicketLineInfo> panelNonKotLines,RetailTicketInfo ticket,java.util.List<TaxInfo> taxlist,AppView app,boolean settled,DataLogicSales dlSales)
        {
          new Momoe(panelNonKotLines,ticket,taxlist,app,settled,dlSales).getResponse();
        }
}
