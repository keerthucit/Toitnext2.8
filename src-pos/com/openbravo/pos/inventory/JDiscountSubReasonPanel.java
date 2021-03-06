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

package com.openbravo.pos.inventory;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;
import com.sysfore.pos.hotelmanagement.JPrinterConfigEditor;
import com.sysfore.pos.hotelmanagement.JTaxMappingPanel;
import com.sysfore.pos.hotelmanagement.PrinterDetailsInfo;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author adrianromero
 */
public class JDiscountSubReasonPanel extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
    public String[] strings = {""};
   public DefaultListModel reasonModel = null;
   private ComboBoxValModel mainReasonmodel;  
   private SentenceList reasonsent;
    
    public java.util.List<DiscountSubReasonInfo> list = null;
    public boolean updateMode = false;
    private static volatile SecureRandom numberGenerator = null;
   private static final long MSB = 0x8000000000000000L;
   private static final JDiscountSubReasonPanel RandomUtil = null;
   
    public JDiscountSubReasonPanel() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        try {
            m_App = app;
         
            m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
            dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
             initComponents();
             reasonsent=dlCustomers.getDiscountReason();
             mainReasonmodel = new ComboBoxValModel(reasonsent.list());
             jComboReason.setModel(mainReasonmodel);
            try {
                populateList();
               // populateDiscountReason();
            } catch (BasicException ex) {
                Logger.getLogger(JDiscountSubReasonPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
           m_jSubReasonText.setText("");
           jComboReason.setSelectedIndex(-1);
               jcboxIsactive.setSelected(false);
            setVisible(true);
            //add(m_jDiscountList);
            File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
            AppConfig ap = new AppConfig(file);
            ap.load();
        } catch (BasicException ex) {
            Logger.getLogger(JDiscountSubReasonPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return "Discount Sub-category";
    }

    public void activate() throws BasicException {
        
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


   

      
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jReasonList = new javax.swing.JList();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jlblSubReason = new javax.swing.JLabel();
        m_jSubReasonText = new javax.swing.JTextField();
        jComboReason = new javax.swing.JComboBox();
        jLblReason = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jcboxIsactive = new javax.swing.JCheckBox();

        m_jReasonList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jReasonListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jReasonList);

        jbtnNew.setBackground(new java.awt.Color(255, 255, 255));
        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        jbtnNew.setFocusPainted(false);
        jbtnNew.setFocusable(false);
        jbtnNew.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnNew.setRequestFocusEnabled(false);
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });

        jbtnDelete.setBackground(new java.awt.Color(255, 255, 255));
        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editdelete.png"))); // NOI18N
        jbtnDelete.setFocusPainted(false);
        jbtnDelete.setFocusable(false);
        jbtnDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });

        jbtnSave.setBackground(new java.awt.Color(255, 255, 255));
        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png"))); // NOI18N
        jbtnSave.setFocusPainted(false);
        jbtnSave.setFocusable(false);
        jbtnSave.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnSave.setRequestFocusEnabled(false);
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSaverLayout = new javax.swing.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(330, 330, 330)
                .addComponent(jbtnNew)
                .addGap(4, 4, 4)
                .addComponent(jbtnDelete)
                .addGap(4, 4, 4)
                .addComponent(jbtnSave))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jbtnNew)
            .addComponent(jbtnDelete)
            .addComponent(jbtnSave)
        );

        m_jlblSubReason.setText("Sub-category*");

        m_jSubReasonText.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jSubReasonText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSubReasonTextActionPerformed(evt);
            }
        });

        jComboReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboReasonActionPerformed(evt);
            }
        });

        jLblReason.setText("Category*");

        jLabel1.setText("Active");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblReason)
                            .addComponent(m_jlblSubReason, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcboxIsactive)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboReason, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(m_jSubReasonText, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jSubReasonText, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblSubReason, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLblReason))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jcboxIsactive))))
                .addContainerGap(236, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jReasonListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jReasonListValueChanged
        // TODO add your handling code here:
        if (evt.getValueIsAdjusting()) {
         // Map <String , DiscountSubReasonInfo> hm=new  HashMap <String , DiscountSubReasonInfo>();
        //    System.out.println("inside value changed -- list selection event fired");
            int index=m_jReasonList.getSelectedIndex();
            updateMode = true;
            String subReason = null;
            String active=list.get(index).getActive();
            try {
                subReason = m_jReasonList.getSelectedValue().toString();
            } catch (Exception ex) {
                System.out.println("unknown exception");
            }
           
            m_jSubReasonText.setText(subReason);
            mainReasonmodel.setSelectedKey(list.get(index).getReasonId());
              if(active.equals("Y")){
                jcboxIsactive.setSelected(true);
            }else{
                  jcboxIsactive.setSelected(false);
            }

           
        }
//            if (m_jReasonList.getSelectedIndex() > -1) { 
//            int index = m_jReasonList.getSelectedIndex();
//            m_jSubReasonText.setText(list.get(index).getSubReason());
//            jComboReason.setSelectedItem(reasonList.get(index).getReason());
  //       }
    }//GEN-LAST:event_m_jReasonListValueChanged

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
       
        m_jSubReasonText.setText("");
        jComboReason.setSelectedIndex(-1);
         jcboxIsactive.setSelected(false);
        m_jReasonList.clearSelection();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jReasonList.getSelectedIndex();
        System.out.println("index is " + index);

        if (index == -1) {
            JOptionPane.showMessageDialog(JDiscountSubReasonPanel.this, "Please select the Discount Sub-Category");
        } else {
            try {
                String val = m_jReasonList.getSelectedValue().toString();
                dlCustomers.deleteDiscountSubReason(val);
                 reasonModel.remove(index);
                 list.remove(index);
                 m_jSubReasonText.setText("");
                 jComboReason.setSelectedIndex(-1);
                   jcboxIsactive.setSelected(false);
              
            } catch (BasicException ex) {
                Logger.getLogger(JDiscountSubReasonPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JDiscountSubReasonPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
       if ( m_jSubReasonText.getText().equals("")) {
            JOptionPane.showMessageDialog(JDiscountSubReasonPanel.this, "Please enter the Discount Sub-Category");
           
        } else   if (jComboReason.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(JDiscountSubReasonPanel.this, "Please select the Discount Sub-Category");
        } 
       else {
            try {
                saveButtonAction();
              } catch (BasicException ex) {
                Logger.getLogger(JDiscountSubReasonPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed

    private void m_jSubReasonTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSubReasonTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jSubReasonTextActionPerformed

    private void jComboReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboReasonActionPerformed
 
    
        private void showMessage(JDiscountSubReasonPanel aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
        private JPanel getLabelPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(Color.RED);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}
    
    public void populateList() throws BasicException {

        reasonModel = new DefaultListModel();
        m_jReasonList.setModel(reasonModel);
       list = dlCustomers.getDiscountSubReasonList();
      
        for (int i = 0; i < list.size(); i++) {
             String subReason=list.get(i).getSubReason();
             reasonModel.add(i, subReason);
      }
    }
    
//         public void populateDiscountReason(){
//          jComboReason.removeAllItems();
//        try {
//            reasonList = dlCustomers.getDiscountReasonList();
//        } catch (BasicException ex) {
//            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            for (DiscountReasonInfo dis : reasonList) {
//                jComboReason.addItem(dis.getReason());
//
//            }
//            jComboReason.setSelectedIndex(-1);
//    }

private void saveButtonAction() throws BasicException {

      
        //String reason = jComboReason.getSelectedItem().toString();
        String subReason=m_jSubReasonText.getText();
        String reason=mainReasonmodel.getSelectedKey().toString();
        String active="";
        if(jcboxIsactive.isSelected()){
            active="Y";
        }else{
            active="N";
        }
        try {
           
          list = dlCustomers.getDiscountSubReasonList();
        //  String reasonId=reasonList.get(jComboReason.getSelectedIndex()).getId();
           System.out.println("updatemode"+updateMode);
                if (updateMode == false) {
                    if (reasonModel.contains(subReason)) {
                         JOptionPane.showMessageDialog(this, "Entered Reason already exists");
                     } else {
                      
                         dlCustomers.insertDiscountSubReasons(RandomUtil.genRandom32Hex(), reason,subReason,active);
                         jComboReason.setSelectedIndex(-1);
                    }
                } else {
                   String id = list.get(m_jReasonList.getSelectedIndex()).getSubreasonId();
                  dlCustomers.updateDiscountSubReasons(id, reason, subReason,active);
                   updateMode = false;
                }
                populateList();
                m_jSubReasonText.setText("");
                jComboReason.setSelectedIndex(-1);
                jcboxIsactive.setSelected(false);
              
             } catch (Exception npe) {
               npe.printStackTrace();
               // JOptionPane.showMessageDialog(this, "Enter valid Reason");
              m_jSubReasonText.setText("");
                jComboReason.setSelectedIndex(-1);
                  jcboxIsactive.setSelected(false);
             
             }
       
    }


     public static String genRandom32Hex() {
    SecureRandom ng = numberGenerator;
    if (ng == null) {
      numberGenerator = ng = new SecureRandom();
    }

    return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboReason;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLblReason;
    private javax.swing.JPanel jSaver;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JCheckBox jcboxIsactive;
    private javax.swing.JList m_jReasonList;
    private javax.swing.JTextField m_jSubReasonText;
    private javax.swing.JLabel m_jlblSubReason;
    // End of variables declaration//GEN-END:variables
    
}
