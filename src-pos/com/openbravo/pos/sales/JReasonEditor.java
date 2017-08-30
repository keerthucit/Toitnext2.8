//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.util.date.DateFormats;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class JReasonEditor extends JDialog {

//    public javax.swing.JDialog dEdior = null;
  //  private Properties dbp = new Properties();
 //   private DataLogicReceipts dlReceipts = null;
  //  private DataLogicCustomers dlCustomers = null;
//    private AppView m_app;
 //   public String[] strings = {""};
 //   public DefaultListModel model = null;
    public java.util.List<Reasoninfo> list = null;
//    public boolean updateMode = false;
    static Component parentLocal = null;
    static RetailTicketInfo tinfoLocal = null;
     static int index = 0;
 //   public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
  //  private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 250;
    static String productName;
    public static boolean cancel =false;
    private static String deleteStatus=null;
    public static boolean billUpdated =false;
      public static Date dbUpdatedDate = null;
     private static DateFormat m_dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat m_dateformattime = new SimpleDateFormat("HH:mm:ss.SSS");
    
    

    public static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, String product, int tindex,String Status) {
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        productName=product;
        index=tindex;
        cancel=false;
        deleteStatus=Status;
          billUpdated=false;
          dbUpdatedDate = null;
        return showMessage(parent, dlReceipts, 1);
    }

    private static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JReasonEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JReasonEditor((Frame) window, true);
        } else {
            myMsg = new JReasonEditor((Dialog) window, true);
        }
        return myMsg.init(dlReceipts);
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
    private JReasonEditor(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JReasonEditor(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }



    public boolean init(DataLogicReceipts dlReceipts) {

        initComponents();

     try {
          //  m_jReason.addItem("");
            java.util.List<Reasoninfo> reasonList = dlReceipts.getReasonList();
            for (Reasoninfo dis : reasonList) {
                m_jReason.addItem(dis.getReason());

            }
            m_jReason.setSelectedIndex(-1);
        } catch (BasicException ex) {
            Logger.getLogger(JReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        setTitle("Reason Editor");
        setVisible(true);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
        return billUpdated;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jReasonLbl = new javax.swing.JLabel();
        m_jbtnOk = new javax.swing.JButton();
        m_jbtnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTxtReason = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        m_jReason = new javax.swing.JComboBox();

        m_jReasonLbl.setText("Comment");

        m_jbtnOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnOk.setText("Ok");
        m_jbtnOk.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnOkActionPerformed(evt);
            }
        });

        m_jbtnCancel.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnCancel.setText("Cancel");
        m_jbtnCancel.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jbtnCancel.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jbtnCancel.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnCancelActionPerformed(evt);
            }
        });

        m_jTxtReason.setColumns(20);
        m_jTxtReason.setRows(5);
        jScrollPane1.setViewportView(m_jTxtReason);

        jLabel1.setText("Reason");

        m_jReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReasonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jReasonLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(m_jbtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(m_jbtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jReason, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 227, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(m_jReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(m_jbtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jbtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(m_jReasonLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnOkActionPerformed
   int primaryAddon=tinfoLocal.getLine(index).getPrimaryAddon();
        String reason = m_jTxtReason.getText();
     double qty=0;  
        int reasonIndex = m_jReason.getSelectedIndex();
         if (reasonIndex == -1) {
         JOptionPane.showMessageDialog(null, "Please select the reason");
//          dbUpdated="";
        dbUpdatedDate=null;
         billUpdated =false;
            } else{
        try {
            String currentUpdated = m_dateformat.format(tinfoLocal.getObjectUpdateDate()) + " " + m_dateformattime.format(tinfoLocal.getObjectUpdateDate());
           String dbUpdated="";
            dbUpdated = localDlReceipts.getUpdatedTime(tinfoLocal.getPlaceId(), tinfoLocal.getSplitSharedId());
             Date currentUpdatedDate=DateFormats.StringToDateTime(currentUpdated);
             dbUpdatedDate=DateFormats.StringToDateTime(dbUpdated);
               if(dbUpdated.equals(null) || dbUpdated.equals("")){
                 //  JOptionPane.showMessageDialog(null, "This Bill is no longer exist ");
                billUpdated=true;
                this.dispose();
              }else if(dbUpdatedDate.compareTo(currentUpdatedDate)>0){
                 billUpdated=true;
                 this.dispose();
               }else {
         Reasoninfo reasonInfo = new Reasoninfo();
          dbUpdated="";
        dbUpdatedDate=null;
            billUpdated =false;
     String reasonItem = m_jReason.getSelectedItem().toString();
        System.out.println("enrtrr "+reasonItem+"---"+reasonInfo.getStatus());
     String reasonId = null;
        try {
            System.out.println("enrtrr1 "+reasonItem);
            reasonId = localDlReceipts.getReasonId(reasonItem);
            System.out.println("get reasonid--"+reasonId);
        } catch (BasicException ex) {
            Logger.getLogger(JReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
        }


        if(deleteStatus.equals("lineDelete")){
          qty = 1*tinfoLocal.getLine(index).getMultiply();
          String addonId=tinfoLocal.getLine(index).getAddonId();
          //changed to save with server date
         localDlReceipts.insertCancelledKot(tinfoLocal.getId(),tinfoLocal.getTicketId(),productName, "Y",qty,tinfoLocal.getLine(index).getKotid(),"Y",reason,reasonId,tinfoLocal.getPlaceId(),tinfoLocal.getUser().getId(),tinfoLocal.getOrderId());
         if(addonId!=null) {
            for(int i=0;i<tinfoLocal.getLinesCount();i++){
              if(i!=index){
               if(tinfoLocal.getLine(i).getIsKot()==1)   {
                if(addonId.equals(tinfoLocal.getLine(i).getAddonId()) && primaryAddon==1){
                    //changed to save with server date
                    localDlReceipts.insertCancelledKot(tinfoLocal.getId(),tinfoLocal.getTicketId(),tinfoLocal.getLine(i).getProductID(), "Y",1*tinfoLocal.getLine(i).getMultiply(),tinfoLocal.getLine(index).getKotid(),"Y","Addon Item Cancelled KOT",reasonId,tinfoLocal.getPlaceId(),tinfoLocal.getUser().getId(),tinfoLocal.getOrderId());
                 }
              }
              } 
           }
         }
       }else{
           qty=1; 
           //changed to save with server date
            localDlReceipts.insertCancelledKot(tinfoLocal.getId(),tinfoLocal.getTicketId(),productName, "Y",qty,tinfoLocal.getLine(index).getKotid(),"Y",reason,reasonId,tinfoLocal.getPlaceId(),tinfoLocal.getUser().getId(),tinfoLocal.getOrderId());
            String addonId=tinfoLocal.getLine(index).getAddonId();
            if(addonId!=null) {
            for(int i=0;i<tinfoLocal.getLinesCount();i++){
              if(i!=index){
               if(tinfoLocal.getLine(i).getIsKot()==1)   {   
                if(addonId.equals(tinfoLocal.getLine(i).getAddonId()) && primaryAddon==1){
                    //changed to save with server date
                    localDlReceipts.insertCancelledKot(tinfoLocal.getId(),tinfoLocal.getTicketId(),tinfoLocal.getLine(i).getProductID(), "Y",qty,tinfoLocal.getLine(index).getKotid(),"Y","Addon Item Cancelled KOT",reasonId,tinfoLocal.getPlaceId(),tinfoLocal.getUser().getId(),tinfoLocal.getOrderId());
                  }
               }
              }  
           }
         }
        
        }
       
         tinfoLocal.getLine(index).setCancelStatus("N");
       
            cancel=true;
           this.dispose();
     }  } catch (BasicException ex) {
            Logger.getLogger(JReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

}//GEN-LAST:event_m_jbtnOkActionPerformed

    private void m_jbtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnCancelActionPerformed
        // TODO add your handling code here:
        tinfoLocal.getLine(index).setCancelStatus("Y");
        cancel=false;
        this.dispose();
    }//GEN-LAST:event_m_jbtnCancelActionPerformed

    public static boolean getCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    } 
    private void m_jReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReasonActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_m_jReasonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox m_jReason;
    private javax.swing.JLabel m_jReasonLbl;
    private javax.swing.JTextArea m_jTxtReason;
    private javax.swing.JButton m_jbtnCancel;
    private javax.swing.JButton m_jbtnOk;
    // End of variables declaration//GEN-END:variables

}

  

    /**
     * @return the enablity
     */
  
    /**
     * @param enablity the enablity to set
     */
   
