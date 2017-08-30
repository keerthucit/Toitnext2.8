/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TaxInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author archana
 */
public class Momoe extends MomoePayment {
    
    private  List<RetailTicketLineInfo> panelNonKotLines;
    private  RetailTicketInfo ticket;
    private java.util.List<TaxInfo> taxcollection;
    private AppView app;
    private boolean settled;
    private SentenceList senttax;
    private  RetailTaxesLogic taxeslogic; 
    Logger logger = Logger.getLogger("MyLog");  
    

   
 Momoe(List<RetailTicketLineInfo> panelNonKotLines,RetailTicketInfo ticket,java.util.List<TaxInfo> taxlist,AppView app,boolean settled,DataLogicSales dlSales) {
       
       System.out.println("Momoe constructor");
      this.panelNonKotLines=panelNonKotLines;
      this.ticket=ticket;
      this.taxcollection=taxlist;
      this.app=app;
      this.settled=settled;
      senttax = dlSales.getRetailTaxList();
      try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = taxlist;
        taxeslogic = new RetailTaxesLogic(taxlist,app);
    }
    
public  void getResponse() {
        String string = "";
        
       logger.info("Momoe Get Response");    
        try {
            
            //Step 1: Build JSON Object
            
            
            // Items Json Array
            
             JSONObject jsonObject = new JSONObject();
             JSONObject tax = new JSONObject();
             JSONArray itemsArray = new JSONArray();
             JSONArray taxArray = new JSONArray();
             JSONArray paymentArray = new JSONArray();
           if(panelNonKotLines!=null){  
            for(RetailTicketLineInfo retail :panelNonKotLines)
            {   
            JSONObject items = new JSONObject();  
               retail.setticketLine(ticket);
               logger.info(" Item Code " +retail.getProductCode());
               logger.info(" Item name "+retail.getProductName());
               logger.info(" Item price "+retail.getPrice());
               logger.info(" Item qty "+retail.getMultiply());
               logger.info(" Item discount "+retail.getLineDiscountPrice());
           
            items.put("code", retail.getProductCode());
            items.put("name",retail.getProductName());
            items.put("perUnitPrice",retail.getPrice());
            items.put("quantity",retail.getMultiply());
            items.put("totalAmount",(retail.getPrice()*retail.getMultiply())-retail.getLineDiscountPrice());
            itemsArray.put(items);
            
            
             }
        }
           boolean billStatus=true;
           if(panelNonKotLines==null){
               billStatus=false;
           }else if(panelNonKotLines.size()==0){
               billStatus=false;
           }
            if(billStatus) {
                
            // Tax Json Object Array
            logger.info(" Item Print servicetaxrate "+ticket.getServiceTaxRate());
            logger.info(" Item Print ServiceTax "+ticket.printServiceTax());
            logger.info(" Item Print ServiceChargeRate "+ticket.getServiceChargeRate());
            logger.info(" Item Print ServiceCharge "+ticket.printServiceCharge());
           
            tax.put("name", "Service Tax");
           // tax.put("percent", new Double(ticket.getServiceTaxRate()*100));
            tax.put("percent",new DecimalFormat("#0.0000").format(ticket.getServiceTaxRate()*100));
            tax.put("amount", ticket.printServiceTax());
            taxArray.put(tax); 
            tax = new JSONObject();
            tax.put("name", "Service Charge");
            tax.put("percent", new Double(ticket.getServiceChargeRate()*100));
            tax.put("amount",ticket.printServiceCharge());
            taxArray.put(tax); 
            
            taxeslogic.calculateTaxes(ticket);
            for(TaxInfo tx:taxcollection)
            {
             tax = new JSONObject();
            if(ticket.getTaxLine(tx).getRetailTax()!=0.0){
                
             logger.info(" Item Print taxinfo "+ticket.getTaxLine(tx).getTaxInfo());
             logger.info(" Item Print taxrate "+ticket.getTaxLine(tx).getTaxInfo().getRate());
             logger.info(" Item Print retailtax "+ticket.getTaxLine(tx).printRetailTax());
            tax.put("name",ticket.getTaxLine(tx).getTaxInfo());
            tax.put("percent",new Double(ticket.getTaxLine(tx).getTaxInfo().getRate()*100));
            tax.put("amount",ticket.getTaxLine(tx).printRetailTax());   
            taxArray.put(tax); 
            }
            }
            
               logger.info(" Item Print phoneno. "+ticket.getMomoePhoneNo());
               logger.info(" Item Print roundoff "+ticket.getRoundOffvalue());
               logger.info(" Item Print total "+ticket.getTotal());
               logger.info(" Item Print customerdiscount "+ticket.getDiscountValue());
               logger.info(" Item Print oldtablename "+ticket.getOldTableName());
               logger.info(" Item Print tablename  "+ticket.getTableName());
            
            
            jsonObject.put("merchantId",app.getProperties().getProperty("machine.merchantid"));
            jsonObject.put("merchantKey", app.getProperties().getProperty("machine.merchantkey"));
            jsonObject.put("customerPhone",ticket.getMomoePhoneNo());
            jsonObject.put("roundOff",Formats.CURRENCY.formatValue(new Double(ticket.getRoundOffvalue())));
            jsonObject.put("totalAmount",ticket.getTotal());
            jsonObject.put("discountAmount",ticket.getDiscountValue());
            jsonObject.put("preDiscountTotalAmount",(ticket.getTotal() + ticket.getDiscountValue()));
            jsonObject.put("oldtable",ticket.getOldTableName());
            jsonObject.put("tableNumber",ticket.getTableName());
            jsonObject.put("items", itemsArray);
            jsonObject.put("settled",settled);
            jsonObject.put("orderNumber",ticket.getOrderId());
            jsonObject.put("billNumber",ticket.getTicketId());
            jsonObject.put("taxes", taxArray);
            if(settled){
                for ( PaymentInfo p : ticket.getPayments()) {
                JSONObject payments = new JSONObject();
                payments.put("amount",p.getTotal());
                payments.put("type",p.getName());
                logger.info(" payment total " +p.getTotal());
                logger.info(" payment name " +p.getName());
                paymentArray.put(payments);
                }
            }
            jsonObject.put("payments", paymentArray);
           
            logger.info(jsonObject.toString());
            
        }else{
            taxArray=new JSONArray(new ArrayList());
//            tax = new JSONObject();    
//            tax.put("name","");
//            tax.put("percent","");
//            tax.put("amount","");   
//            taxArray.put(tax);     
            jsonObject.put("merchantId",app.getProperties().getProperty("machine.merchantid"));
	    jsonObject.put("merchantKey", app.getProperties().getProperty("machine.merchantkey"));
            jsonObject.put("customerPhone",ticket.getMomoePhoneNo());
            jsonObject.put("roundOff","0.0");
            jsonObject.put("totalAmount","0.0");
            jsonObject.put("discountAmount","0.0");
            jsonObject.put("preDiscountTotalAmount","0.0");
            jsonObject.put("oldtable",ticket.getOldTableName());
            jsonObject.put("tableNumber",ticket.getTableName());
            jsonObject.put("items", itemsArray);
            jsonObject.put("settled",true);
            jsonObject.put("orderNumber",ticket.getOrderId());
            jsonObject.put("billNumber",ticket.getTicketId());
            jsonObject.put("taxes", taxArray);
            
             System.out.println(jsonObject);
            }
             
 
            // Step2: Now pass JSON File Data to REST Service
            try {
                
                String momoeServer=app.getProperties().getProperty("machine.momoeserver");  
                URL url = new URL(momoeServer);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                
                
                out.write(jsonObject.toString());
                out.close();
 
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                
                while (in.readLine() != null) {
                }
                  logger.info("\nREST Service Invoked Successfully..");
                 
                in.close();
            } catch (Exception e) {
                    logger.info("\nError while calling REST Service");
                    logger.info(e.getMessage());
            
                   
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
               
        }
        
         
       
    }

   


       

}

