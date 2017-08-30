/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppView;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author mateen
 */
public class ReprintBillMailSender {

    private static String fromuser;
    private static String mailidfrom;
    private static String mailidto;
    private static String senderPass;
    private static String filepath;
    
    
   public void sendBill(String mailId,AppView m_app) throws UnsupportedEncodingException, AddressException, MessagingException{
   
      fromuser =m_app.getProperties().getProperty("machine.fromuser");
      mailidfrom =m_app.getProperties().getProperty("machine.mailidfrom");
      mailidto=mailId;
      senderPass =  m_app.getProperties().getProperty("machine.senderPassword");
      filepath = m_app.getProperties().getProperty("machine.billpath");
     
            Properties prop = new Properties();
           // prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.port", "25");
            prop.put("mail.smtp.socketFactory.fallback", "false");
            prop.put("mail.smtp.quitwait", "true");
           // prop.put("mail.smtp.host", "smtp.gmail.com");
             prop.put("mail.smtp.host", "smtpout.asia.secureserver.net");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            Authenticator auth = new SMTPAuthenticator();
            Session s = Session.getDefaultInstance(prop, auth);
            Message msg = new MimeMessage(s);
            msg.setFrom(new InternetAddress(mailidfrom, fromuser));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailidto));
            msg.setSubject("Reprint Bill");
            BodyPart bpart = new MimeBodyPart();
            bpart.setText("Please find the attachment");
            Multipart msgAttach = new MimeMultipart();
            msgAttach.addBodyPart(bpart);
            bpart = new MimeBodyPart();
            DataSource ds = new FileDataSource(filepath);
            bpart.setDataHandler(new DataHandler(ds));
            bpart.setFileName("ReprintBill.pdf");
            msgAttach.addBodyPart(bpart);
            msg.setContent(msgAttach);
            Transport.send(msg);
  } 
    
    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailidfrom, senderPass);
        }
    };
}
