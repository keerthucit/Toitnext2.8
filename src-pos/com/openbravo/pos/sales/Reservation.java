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

public class Reservation extends ReservationStatus {
    
    private String status ;
    private String tableName;
    private AppView app;
    
    Logger logger = Logger.getLogger("MyLog");  
    

   
 Reservation(String status,String tableName,AppView app) {
       
       System.out.println("Reservation constructor");
       
      this.status=status;
      this.tableName=tableName;
      this.app=app;
   
    }
    
public  void getResponse() {
    //    String string = "";
        
       logger.info("Reservation Get Response");    
        try {
            
            //Step 1: Build JSON Object
           
            // reservation Json Array
            
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("restID",app.getProperties().getProperty("machine.restID"));
               jsonObject.put("tablename",tableName);
               jsonObject.put("status",status); 

            /*    jsonObject.put("restID","54acb6cc1c1a11530e7452ba");
               jsonObject.put("tablename","D1");
               jsonObject.put("status","3"); */


                logger.info(jsonObject.toString());
         
   
            // Step2: Now pass JSON File Data to REST Service
            try {
                
               // String momoeServer=app.getProperties().getProperty("machine.momoeserver");
                 String reservationServer=app.getProperties().getProperty("machine.reservationserver");
                URL url = new URL(reservationServer);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                
                
                out.write(jsonObject.toString());
                out.close();
 
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                
                while (in.readLine() != null) {
                }
                  logger.info("\nREST Service Invoked Successfully..");
                 System.out.println("\nREST Service Invoked Successfully..");
                in.close();
            } catch (Exception e) {
                    logger.info("\nError while calling REST Service");
                    logger.info(e.getMessage());
                    System.out.println(e.getMessage());
            
                   
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
               
        }
        
         
       
    }

   


       

}

