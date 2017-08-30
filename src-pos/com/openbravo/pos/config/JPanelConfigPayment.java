//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

package com.openbravo.pos.config;

import com.openbravo.data.user.DirtyManager;
import java.awt.Component;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.payment.ConfigPaymentPanelCaixa;
import com.openbravo.pos.payment.ConfigPaymentPanelEmpty;
import com.openbravo.pos.payment.ConfigPaymentPanelGeneric;
import com.openbravo.pos.payment.ConfigPaymentPanelLinkPoint;
import com.openbravo.pos.payment.PaymentConfiguration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author adrianromero
 * @author Mikel Irurita
 */
public class JPanelConfigPayment extends javax.swing.JPanel implements PanelConfig {

    private DirtyManager dirty = new DirtyManager();
    private Map<String, PaymentConfiguration> paymentsName = new HashMap<String, PaymentConfiguration>();
    private PaymentConfiguration pc;
    
    /** Creates new form JPanelConfigPayment */
    public JPanelConfigPayment() {
        
        initComponents();
                
        // dirty manager
        jcboCardReader.addActionListener(dirty);
        jcboPaymentGateway.addActionListener(dirty);
        jchkPaymentTest.addActionListener(dirty);
        
        // Payment Provider                
        initPayments("Not defined", new ConfigPaymentPanelEmpty());
        initPayments("external", new ConfigPaymentPanelEmpty());
        initPayments("PayPoint / SecPay", new ConfigPaymentPanelGeneric());
        initPayments("AuthorizeNet", new ConfigPaymentPanelGeneric());
        initPayments("Cyberauthorize", new ConfigPaymentPanelGeneric());
        initPayments("Firs Data / LinkPoint / YourPay", new ConfigPaymentPanelLinkPoint());
        initPayments("PaymentsGateway.net", new ConfigPaymentPanelGeneric());
        initPayments("La Caixa (Spain)", new ConfigPaymentPanelCaixa());
        
        // Lector de tarjetas.
        jcboCardReader.addItem("Not defined");
        jcboCardReader.addItem("Generic");
        jcboCardReader.addItem("Intelligent");
        jcboCardReader.addItem("Keyboard");
    }
    
    public boolean hasChanged() {
        return dirty.isDirty();
    }
    
    public Component getConfigComponent() {
        return this;
    }
   
    public void loadProperties(AppConfig config) {

        jcboCardReader.setSelectedItem(config.getProperty("payment.magcardreader"));
        jcboPaymentGateway.setSelectedItem(config.getProperty("payment.gateway"));
        jchkPaymentTest.setSelected(Boolean.valueOf(config.getProperty("payment.testmode")).booleanValue());       
        pc.loadProperties(config);
        dirty.setDirty(false);
    }
   
    public void saveProperties(AppConfig config) {
        
        config.setProperty("payment.magcardreader", comboValue(jcboCardReader.getSelectedItem()));
        config.setProperty("payment.gateway", comboValue(jcboPaymentGateway.getSelectedItem()));
        config.setProperty("payment.testmode", Boolean.toString(jchkPaymentTest.isSelected()));
        pc.saveProperties(config);
        dirty.setDirty(false);
    }
    
    private void initPayments(String name, PaymentConfiguration pc) {
        jcboPaymentGateway.addItem(name);
        paymentsName.put(name, pc);
    }
     
    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jcboCardReader = new javax.swing.JComboBox();
        jcboPaymentGateway = new javax.swing.JComboBox();
        jchkPaymentTest = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("Label.Payment"))); // NOI18N

        jLabel13.setText(AppLocal.getIntString("label.paymentgateway")); // NOI18N

        jLabel11.setText(AppLocal.getIntString("label.magcardreader")); // NOI18N

        jcboPaymentGateway.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPaymentGatewayActionPerformed(evt);
            }
        });

        jchkPaymentTest.setText(AppLocal.getIntString("label.paymenttestmode")); // NOI18N

        jPanel2.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPaymentGateway, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchkPaymentTest, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(165, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboCardReader, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(297, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jcboCardReader, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jcboPaymentGateway, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchkPaymentTest, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jcboPaymentGatewayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPaymentGatewayActionPerformed
    pc = paymentsName.get(comboValue(jcboPaymentGateway.getSelectedItem()));

    if (pc != null) {
        jPanel2.removeAll();
        jPanel2.add(pc.getComponent());
        jPanel2.revalidate();
        jPanel2.repaint(); 
    }
}//GEN-LAST:event_jcboPaymentGatewayActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox jcboCardReader;
    private javax.swing.JComboBox jcboPaymentGateway;
    private javax.swing.JCheckBox jchkPaymentTest;
    // End of variables declaration//GEN-END:variables
    
}
