/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.mailsending;

import com.openbravo.pos.forms.AppView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * 
 * @author shilpa
 */
public class ReportAccessMail {
// FileWriter fw = null;
    static String fromuser;
     static  String mailidfrom;
     static String mailidto;
    static String senderPass;
   
   
    
  public static void sendMail(AppView m_app,Logger logger,String reportName) {
   String currentDate=null;
    try {
 Properties  properties = new Properties();
      //Reading config file
      fromuser =m_app.getProperties().getProperty("machine.fromuser");
      mailidfrom =m_app.getProperties().getProperty("machine.mailidfrom");
      mailidto=m_app.getProperties().getProperty("machine.mailidto");
     senderPass =  m_app.getProperties().getProperty("machine.senderPassword");
      String[] mailidto = m_app.getProperties().getProperty("machine.mailidto").split(",");
 //     String[] mailidcc=m_app.getProperties().getProperty("machine.mailidcc").split(",");
      //properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.port", "25");
            properties.put("mail.smtp.socketFactory.fallback", "false");
            properties.put("mail.smtp.quitwait", "true");
            //local testing
            properties.put("mail.smtp.host", "smtp.gmail.com");
            //production
            //properties.put("mail.smtp.host", "smtpout.asia.secureserver.net");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            Authenticator auth = new SMTPAuthenticator();
            Session s = Session.getDefaultInstance(properties, auth);
            Message msg = new MimeMessage(s);
            msg.setFrom(new InternetAddress(mailidfrom, fromuser));
           InternetAddress[] addressTo = new InternetAddress[mailidto.length];
            for (int i = 0; i < mailidto.length; i++) {
              addressTo[i] = new InternetAddress(mailidto[i]);
            }
          
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
//             InternetAddress[] addressCc = new InternetAddress[mailidcc.length];
//            for (int i = 0; i < mailidcc.length; i++) {
//              addressCc[i] = new InternetAddress(mailidcc[i]);
//            }
//            msg.setRecipients(MimeMessage.RecipientType.CC, addressCc);
          
            
            msg.setSubject("Report Access Information Mail");
            BodyPart bpart = new MimeBodyPart();
            bpart.setText(m_app.getAppUserView().getUser().getName()+" is accessing the " +reportName+ " Report from the system- "+m_app.getProperties().getPosNo());
            MimeMultipart msgAttach = new MimeMultipart();
            msgAttach.addBodyPart(bpart);
            msg.setContent(msgAttach);
            Transport.send(msg);
            logger.info("Report access mail sent successfully");
 //     try {
//           writer = new PrintWriter(System.getProperty("user.home")+"/BillMailLog.txt", "UTF-8");
//       } catch (FileNotFoundException ex) {
//           Logger.getLogger(ReportAccessMail.class.getName()).log(Level.SEVERE, null, ex);
//       } catch (UnsupportedEncodingException ex) {
//           Logger.getLogger(ReportAccessMail.class.getName()).log(Level.SEVERE, null, ex);
//       }
//       writer.println("Bill has been mailed successfully :"+currentDate);
//       writer.close();
//    //  System.out.println("Exported Successfully " );
    } catch (Exception e) {
      logger.info("Report access mail sending failed");
      e.printStackTrace();
    }
  }
    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailidfrom, senderPass);
        }
    };
}