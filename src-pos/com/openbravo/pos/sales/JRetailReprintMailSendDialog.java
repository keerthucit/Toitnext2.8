/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import static com.openbravo.pos.sales.JRetailReprintMailSendDialog.parentLocal;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author archana
 */
public class JRetailReprintMailSendDialog extends javax.swing.JDialog {
 int x = 500;
    int y = 300;
    int width = 350;
    int height = 280;
    static int ticketno=0;
    static Component parentLocal = null;
    private static DataLogicSales localDlSales = null;
    protected static AppView m_App;
    private Pattern pattern;
    private Matcher matcher;
    String selectedPath;
    
    /**
     * Creates new form JRetailReprintMailSending
     */
    public JRetailReprintMailSendDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
         setBounds(x, y, width, height);
    }
    
      public static void showMessage(Component parent, DataLogicSales dlSales, int ticketnum, String type, AppView app) {
        localDlSales = dlSales;
        parentLocal = parent;
        ticketno=ticketnum;
        m_App=app;
        showMessage(parent, dlSales, 1);
    }
       private static void showMessage(Component parent, DataLogicSales dlSales, int x) {

        Window window = getWindow(parent);
        JRetailReprintMailSendDialog myMsg;
        if (window instanceof Frame) {
            myMsg = new JRetailReprintMailSendDialog((Frame) window, true);
        } else {
            myMsg = new JRetailReprintMailSendDialog((Dialog) window, true);
        }
        myMsg.init(dlSales);
    }
         public void init(DataLogicSales dlSales) {
          initComponents();
          System.out.println("INIT");
          jTextMailId.setText("");
          setTitle("Send Mail");
          if( !System.getProperty("os.name").equalsIgnoreCase("Linux")){
            try {
            Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
            } catch (IOException ex) {
            Logger.getLogger(JRetailReprintMailSendDialog.class.getName()).log(Level.SEVERE, null, ex);
          }
          }  
          setVisible(true);
          }
      

    private JRetailReprintMailSendDialog(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }
      
   private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jBtnOk = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();
        jTextMailId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("To");

        jBtnOk.setText("Ok");
        jBtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnOkActionPerformed(evt);
            }
        });

        jBtnCancel.setText("Cancel");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        jTextMailId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMailIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jBtnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextMailId, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextMailId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnCancel)
                    .addComponent(jBtnOk))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnOkActionPerformed
     String mailId= jTextMailId.getText();
     System.out.println("MAIL ID : "+mailId);
     pattern = Pattern.compile(".+@.+\\.[a-z]+");
     matcher = pattern.matcher(mailId);
     boolean matchFound = matcher.matches();
     System.out.println("MATCHFOUND : " +matchFound );
     if(!matchFound){
          JOptionPane.showMessageDialog(this, "Please enter the valid Email address");
          jTextMailId.setText("");
       }
     
     else{
                   ReprintBillMailSender repintMail = new ReprintBillMailSender();
                    try {
                    repintMail.sendBill(mailId,m_App);
                    JOptionPane.showMessageDialog(this, "Bill has been mailed successfully");
                    } catch (UnsupportedEncodingException ex) {
                         JOptionPane.showMessageDialog(this, "Sorry! Bill has not been sent");
                        Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (AddressException ex) {
                         JOptionPane.showMessageDialog(this, "Sorry! Bill has not been sent");
                        Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                         JOptionPane.showMessageDialog(this, "Sorry! Bill has not been sent");
                         Logger.getLogger(JRetailReprintMailSendDialog.class.getName()).log(Level.SEVERE, null, ex);
         }
         this.dispose();
      }
    }//GEN-LAST:event_jBtnOkActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
         this.dispose();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jTextMailIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMailIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextMailIdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOk;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextMailId;
    // End of variables declaration//GEN-END:variables
}
