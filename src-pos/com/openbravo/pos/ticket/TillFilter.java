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

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.pos.forms.AppLocal;

import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.util.List;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.reports.ReportEditorCreator;
import com.sysfore.pos.creditsale.JCreditSales;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TillFilter extends javax.swing.JPanel implements ReportEditorCreator {
    
    private SentenceList m_sentTilt;
  //   private SentenceList m_sentUser;
    private ComboBoxValModel m_TillModel;
   // private ComboBoxValModel m_TiltUserModel;

    /** Creates new form JQBFProduct */
    public TillFilter() {
System.out.println("inside section filter");
        initComponents();
    }
    
    public void init(AppView app) {
         
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_sentTilt = dlSales.getTillList();
        m_TillModel = new ComboBoxValModel(); 
       // m_sentUser=dlSales.getTiltUserList();
       // m_TiltUserModel = new ComboBoxValModel(); 
        
    }
    
    public void activate() throws BasicException {

        List tiltList = m_sentTilt.list();
        tiltList.add(0, null);
        m_TillModel = new ComboBoxValModel(tiltList);
        m_jTillFilter.setModel(m_TillModel);
        
//        List userList = m_sentUser.list();
//        userList.add(0, null);
//        m_TiltUserModel = new ComboBoxValModel(userList);
//        m_jTiltUserFilter.setModel(m_TiltUserModel);
//        
    }
    
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(
                new Datas[] {Datas.OBJECT, Datas.STRING});
    }

    public Component getComponent() {
        return this;
    }
   


    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jTillFilter = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(514, 32));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jTillFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTillFilterActionPerformed(evt);
            }
        });
        add(m_jTillFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 210, 20));

        jLabel3.setText("Tilt No.");
        jLabel3.setPreferredSize(new java.awt.Dimension(110, 14));
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 4, 60, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jTillFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTillFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jTillFilterActionPerformed
   
      private JPanel getLabelRedPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.red);
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
 private void showMsg(TillFilter aThis, String msg) {
       JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
     }
    
////    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox m_jTillFilter;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object createValue() throws BasicException {
      
          return new Object[] {
             m_TillModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_TillModel.getSelectedKey(),
                QBFCompareEnum.COMP_NONE, null         
            };
       
  //  }
    }
    }

