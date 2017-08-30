/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import static com.openbravo.pos.sales.JRetailTicketsBag.m_App;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.FindTicketsRenderer;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketServiceChargeInfo;
import com.openbravo.pos.ticket.TicketTaxInfo;
import com.openbravo.util.date.DateFormats;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author shilpa
 */
public class JRetailRePrintTicket extends JPanel implements JPanelView, BeanFactoryApp,EditorCreator {
     private ListProvider lpr;
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    private DataLogicSales dlSales;
    private DataLogicCustomers dlCustomers;
     protected DataLogicSystem dlSystem;
    private FindTicketsInfo selectedTicket;
    protected AppView m_App;
    private SentenceList senttax;
    private SentenceList sentcharge;
     private SentenceList sentservicetax;
    private ListKeyed taxcollection;
    private ListKeyed chargecollection;
    private SentenceList senttaxcategories;
    private  RetailTaxesLogic taxeslogic;
      private RetailOldTaxesLogic oldtaxeslogic;
    private  RetailServiceChargesLogic chargeslogic;
    private  RetailSTaxesLogic staxeslogic;
    private TicketParser m_TTP; 
    private RetailTicketInfo ticket;
     String RESULT = "";
    String text = new String();
    String[] textLines;
    private String reason=null;
    
 
    /**
     * Creates new form JRetailRePrintTicket
     */
    public void init(AppView app) throws BeanFactoryException {
         m_App=app;
         dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
         dlCustomers=(DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
         dlSystem=(DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
          RESULT=m_App.getProperties().getProperty("machine.billpath");
         initComponents();
    }
    
    
        private void initCombos() {
        String[] values = new String[] {AppLocal.getIntString("label.sales"),
                    AppLocal.getIntString("label.refunds"), AppLocal.getIntString("label.all")};
        jComboBoxTicket.setModel(new DefaultComboBoxModel(values));
        
        jcboMoney.setModel(new ListQBFModelNumber());
        
        m_sentcat = dlSales.getUserList();
        m_CategoryModel = new ComboBoxValModel(); 
        
        List catlist=null;
        try {
            catlist = m_sentcat.list();
        } catch (BasicException ex) {
            ex.getMessage();
        }
        catlist.add(0, null);
        m_CategoryModel = new ComboBoxValModel(catlist);
        jcboUser.setModel(m_CategoryModel);      
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtxtMoney = new com.openbravo.editor.JEditorCurrency();
        jcboUser = new javax.swing.JComboBox();
        jcboMoney = new javax.swing.JComboBox();
        jtxtTicketID = new com.openbravo.editor.JEditorIntegerPositive();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTxtStartDate = new javax.swing.JTextField();
        jTxtEndDate = new javax.swing.JTextField();
        btnDateStart = new javax.swing.JButton();
        btnDateEnd = new javax.swing.JButton();
        jComboBoxTicket = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jButtonReset = new javax.swing.JButton();
        jButtonExFilter = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListTickets = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jButtonMail = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorNumberKeys();

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(0, 210));

        jLabel1.setText(AppLocal.getIntString("label.ticketid")); // NOI18N

        jLabel6.setText(AppLocal.getIntString("label.user")); // NOI18N

        jLabel7.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

        jLabel3.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N

        jLabel4.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N

        jTxtStartDate.setPreferredSize(new java.awt.Dimension(200, 25));

        jTxtEndDate.setPreferredSize(new java.awt.Dimension(200, 25));

        btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        btnDateStart.setPreferredSize(new java.awt.Dimension(50, 25));
        btnDateStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateStartActionPerformed(evt);
            }
        });

        btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        btnDateEnd.setPreferredSize(new java.awt.Dimension(50, 25));
        btnDateEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateEndActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcboUser, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jcboMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jTxtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jtxtTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jTxtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(59, 59, 59))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jtxtTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jTxtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(jTxtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jcboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(jcboMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        jButtonReset.setText(AppLocal.getIntString("button.clean")); // NOI18N
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReset);

        jButtonExFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launch.png"))); // NOI18N
        jButtonExFilter.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
        jButtonExFilter.setFocusPainted(false);
        jButtonExFilter.setFocusable(false);
        jButtonExFilter.setRequestFocusEnabled(false);
        jButtonExFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExFilterActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonExFilter);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jListTickets.setFocusable(false);
        jListTickets.setRequestFocusEnabled(false);
        jListTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListTicketsMouseClicked(evt);
            }
        });
        jListTickets.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListTicketsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListTickets);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.setEnabled(false);
        jcmdOK.setFocusPainted(false);
        jcmdOK.setFocusable(false);
        jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        jcmdOK.setRequestFocusEnabled(false);
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel1.add(jcmdOK);

        jButtonMail.setText("Send Mail");
        jButtonMail.setEnabled(false);
        jButtonMail.setPreferredSize(new java.awt.Dimension(103, 50));
        jButtonMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMailActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonMail);

        jPanel8.add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 250));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(m_jKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m_jKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(432, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateStartActionPerformed
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }//GEN-LAST:event_btnDateStartActionPerformed

    private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateEndActionPerformed
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }//GEN-LAST:event_btnDateEndActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        defaultValues();
    }//GEN-LAST:event_jButtonResetActionPerformed

    private void jButtonExFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExFilterActionPerformed
        executeSearch();
    }//GEN-LAST:event_jButtonExFilterActionPerformed

    private void jListTicketsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListTicketsMouseClicked

        if (evt.getClickCount() == 2) {
            selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
            
        }

    }//GEN-LAST:event_jListTicketsMouseClicked

    private void jListTicketsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListTicketsValueChanged
        jcmdOK.setEnabled(jListTickets.getSelectedValue() != null);
        jButtonMail.setEnabled(jListTickets.getSelectedValue() != null);
    }//GEN-LAST:event_jListTicketsValueChanged

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed
         selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
         if (selectedTicket != null) {
         reason=JRetailReprintReason.showMessage(this, dlSales, selectedTicket.getTicketId(), "sales");
             if(JRetailReprintReason.status){
             readTicket(selectedTicket.getTicketId());
             defaultValues();
             }
         }
    }//GEN-LAST:event_jcmdOKActionPerformed

    private void jButtonMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMailActionPerformed
        selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
        ticket = new RetailTicketInfo();
        text = new String();
        java.util.List<TicketLineConstructor> allLines = null;
        if (selectedTicket != null) {
            System.out.println("selectedTicket.getTicketId()--"+selectedTicket.getTicketId());
            try {
                ticket=dlSales.getRetailPrintedTicket(selectedTicket.getTicketId());
                System.out.println("SELECTED TICKET"+ticket.getTicketId() );
                if(ticket!= null)      {
                    ticket.setModified(false);
                    for(int i=0;i<ticket.getLinesCount();i++){
                     ticket.getLine(i).setticketLine(ticket);
                    }
                    taxeslogic.calculateTaxes(ticket);
                    allLines = getAllLines(ticket);
                    initTextLines(allLines);
                    System.out.println("RESULT*******" +RESULT);
                    createPdf(RESULT, text);
                }
               
            JRetailReprintMailSendDialog.showMessage(this,dlSales,selectedTicket.getTicketId(),"sales",m_App);
           
            } catch (DocumentException ex) {
                Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        
    }//GEN-LAST:event_jButtonMailActionPerformed

      public void createPdf(String filename, String data) throws DocumentException, IOException {
       // step 1
       Document document = new Document();
       // step 2
       PdfWriter.getInstance(document, new FileOutputStream(filename));
  // step 3
       document.open();
       // step 4
       document.add(new Paragraph(data));
       // step 5
       document.close();

   }
        private void showMessage(JRetailRePrintTicket aThis, String msg,Color colour) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg,colour), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
      public void initTextLines(java.util.List<TicketLineConstructor> allLines) {
          textLines=null;
       if (textLines == null) {
         
           int numLines=allLines.size();
           textLines = new String[numLines];
           for (int i=0;i<numLines;i++) {
               
               textLines[i]= allLines.get(i).getLine();
               System.out.println("textlines----"+textLines[i]);
               text += textLines[i]+"\n";
           }
       }
       
   }
private JPanel getLabelPanel(String msg, Color colour) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(colour);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}
    @Override
    public Object createValue() throws BasicException {
          Object[] afilter = new Object[14];
        
        // Ticket ID
        if (jtxtTicketID.getText() == null || jtxtTicketID.getText().equals("")) {
            afilter[0] = QBFCompareEnum.COMP_NONE;
            afilter[1] = null;
        } else {
            afilter[0] = QBFCompareEnum.COMP_EQUALS;
            afilter[1] = jtxtTicketID.getValueInteger();
        }
        
        // Sale and refund checkbox        
        if (jComboBoxTicket.getSelectedIndex() == 2) {
            afilter[2] = QBFCompareEnum.COMP_DISTINCT;
            afilter[3] = 2;
        } else if (jComboBoxTicket.getSelectedIndex() == 0) {
            afilter[2] = QBFCompareEnum.COMP_EQUALS;
            afilter[3] = 0;
        } else if (jComboBoxTicket.getSelectedIndex() == 1) {
            afilter[2] = QBFCompareEnum.COMP_EQUALS;
            afilter[3] = 1;
        }
        
        // Receipt money
        afilter[5] = jtxtMoney.getDoubleValue();
        afilter[4] = afilter[5] == null ? QBFCompareEnum.COMP_NONE : jcboMoney.getSelectedItem();
        
        // Date range
        Object startdate = Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
        Object enddate = Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
        
        afilter[6] = (startdate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_GREATEROREQUALS;
        afilter[7] = startdate;
        afilter[8] = (enddate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_LESS;
        afilter[9] = enddate;

        
        
        //User
        if (jcboUser.getSelectedItem() == null) {
            afilter[10] = QBFCompareEnum.COMP_NONE;
            afilter[11] = null; 
        } else {
            afilter[10] = QBFCompareEnum.COMP_EQUALS;
            afilter[11] = ((TaxCategoryInfo)jcboUser.getSelectedItem()).getName(); 
        }
        
        afilter[12] = QBFCompareEnum.COMP_NONE;
        afilter[13] = null;
        
         return afilter;
    }
    
    
  private void readTicket(int ticketId) {
        try {
            String file;
            file = "Printer.oldbill";
            RetailTicketInfo ticket=dlSales.getRetailPrintedTicket(ticketId);
            if(ticket!=null){
             ticket.setM_App(m_App);
              ticket.setModified(false);
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date lineTaxStartdate = sdf.parse("2015-09-01");
                Date erpTaxDate = DateFormats.StringToDateTime("2016-06-01 12:00:00");
                if(ticket.getDate().before(lineTaxStartdate)){  //System.out.println("This is old bill");
                    ticket.setErrMsg("old bill");
                for(int i=0;i<ticket.getLinesCount();i++){
                ticket.getLine(i).setticketLine(ticket);  
                    ticket.getLine(i).setActualPrice(ticket.getLine(i).getPrice());
                 }
                   oldtaxeslogic.calculateTaxes(ticket);
               }else if(ticket.getDate().after(erpTaxDate)|| ("TPR").equals(m_App.getProperties().getStoreName())) {//System.out.println("this is erp date");
                   for (int i = 0; i < ticket.getLinesCount(); i++) {
                        ticket.getLine(i).setticketLine(ticket);
                    }
                    taxeslogic.calculateTaxes(ticket);
                   file = "Printer.bill";
                }else { //System.out.println("this is else");
                    for (int i = 0; i < ticket.getLinesCount(); i++) {
                        ticket.getLine(i).setticketLine(ticket);
                        ticket.getLine(i).setActualPrice(ticket.getLine(i).getPrice());
                    }
                    oldtaxeslogic.calculateTaxes(ticket);
                }
              
            printTicket(file, ticket, ticket.getTableName());
            showMessage(this,"Bill has been printed successfully",Color.green);
         //   dlSales.insertActionsLog("Reprint", ticket.getUser().getId(), m_App.getProperties().getPosNo(),ticket.getTicketId(),new Date(), ticket.getPlaceId(), null, reason);
            }else{
             showMessage(this,"Sorry! This Bill cannot be printed",Color.RED);
            }
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
             Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

       private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
        String sresource = dlSystem.getResourceAsXML(sresourcename);
          
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailRePrintTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                if(ticket.getErrMsg().equals("")){
                chargeslogic.calculateCharges(ticket);  
                staxeslogic.calculateServiceTaxes(ticket); 
                script.put("charges", chargecollection);
                script.put("chargeslogic", chargeslogic);
                script.put("staxeslogic", staxeslogic);
                }
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailRePrintTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailRePrintTicket.this);
            }catch (TaxesException e) {
                Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, e);
            }
        }


    }
    @Override
    public String getTitle() {
        return "Reprinting Bill";
    }

    @Override
    public void activate() throws BasicException {
       jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
       jComboBoxTicket.setVisible(false);
        jtxtTicketID.addEditorKeys(m_jKeys);
        jtxtMoney.addEditorKeys(m_jKeys);
        
        //jtxtTicketID.activate();
        lpr = new ListProviderCreator(dlSales.getTicketsList(), this);

        jListTickets.setCellRenderer(new FindTicketsRenderer());

        getRootPane().setDefaultButton(jcmdOK);
        
        initCombos();
        
        defaultValues();

        selectedTicket = null;
        senttax = dlSales.getRetailTaxList();
        sentcharge = dlSales.getRetailServiceChargeList();
        sentservicetax = dlSales.getRetailServiceTaxList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        taxeslogic = new RetailTaxesLogic(taxlist,m_App);
        oldtaxeslogic= new RetailOldTaxesLogic(taxlist,m_App);
        //newly added to calculate line level service charge and service tax
        java.util.List<ServiceChargeInfo> chargelist = null;
        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist,m_App);
        
         java.util.List<TaxInfo> sertaxlist = null;
        try {
            sertaxlist = sentservicetax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
       staxeslogic = new RetailSTaxesLogic(sertaxlist,m_App);
        
         m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);
    }

    @Override
    public boolean deactivate() {
        return true;
    }

    @Override
    public JComponent getComponent() {
       return this;
    }

    @Override
    public Object getBean() {
        return this;
    }

     private static class MyListData extends javax.swing.AbstractListModel {
        
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        @Override
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        @Override
        public int getSize() {
            return m_data.size();
        } 
    }
    public void executeSearch() {
        try {
            jListTickets.setModel(new MyListData(lpr.loadData()));
            if (jListTickets.getModel().getSize() > 0) {
                jListTickets.setSelectedIndex(0);
            }
        } catch (BasicException e) {
            e.printStackTrace();
        }        
    }
    
    
     private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket){
          String serviceChargeName="";
          String serviceTaxName="";
           String taxName="";
          java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
       allLines.add(new TicketLineConstructor("STX #AAFCP2203ESD001    TIN:29220881524"));
       allLines.add(new TicketLineConstructor("                        TOIT"));
       allLines.add(new TicketLineConstructor("      #298, 100ft Road, Indiranagar"));
       allLines.add(new TicketLineConstructor("          Bangalore - 560038"));
       allLines.add(new TicketLineConstructor("     Ph No: 9019713388 \\ 9019813388"));
       allLines.add(new TicketLineConstructor("-----------------------------------------------------------"));
       allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(2) + ticket.getTicketId()+"              Table:" + getSpaces(2) + ticket.getTableName()));
       allLines.add(new TicketLineConstructor("Date:" + getSpaces(2) + ticket.printDateForReceipt()+"           Time:" + getSpaces(2) + ticket.printTime()));
       allLines.add(new TicketLineConstructor("-----------------------------------------------------------"));
      allLines.add(new TicketLineConstructor("Item" + getSpaces(21) + "     Qty" +getSpaces(9)+"     Value"));
         for (RetailTicketLineInfo tLine : ticket.getUniqueLines()) {
                String prodName = tLine.printName();
                String qty = tLine.printMultiply();
                String subValue = tLine.printSubValueBeforeDiscount(); 
//            if (prodName.length() > 30) { 
//                        prodName = WordUtils.wrap(prodName, 30);
//                        String[] prodNameArray = prodName.split("\n");
//                        for (int i = 0; i < prodNameArray.length - 1; i++) {
//                        allLines.add(new TicketLineConstructor(prodNameArray[i]));
//                        }
//               allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(33 - prodNameArray[prodNameArray.length - 1].length()) + qty+ getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue));
//            }
//            else{
                  if (prodName.length() > 26) { 
                        prodName =prodName.substring(0,26);
                  }
                 allLines.add(new TicketLineConstructor(prodName + getSpaces(33 - prodName.length()) + qty+ getSpaces(7 - qty.length() + 7 - subValue.length()) + subValue));
           // }
        }
          allLines.add(new TicketLineConstructor("-----------------------------------------------------------"));
          String count = ticket.printArticlesCount();
          String lineTotal =ticket.printSubTotalValueBeforeDiscount();
           allLines.add(new TicketLineConstructor("Line Total:" + getSpaces(21) + count +getSpaces(9)+lineTotal));
           System.out.println("printDiscount"+ticket.printDiscount());
           if(!ticket.printDiscount().equals("0.00")){
              allLines.add(new TicketLineConstructor("Discount("+ticket.printDiscountRate()+")" + getSpaces(34)+ticket.printDiscount()));    
          }
            allLines.add(new TicketLineConstructor("Subtotal." + getSpaces(34)+ticket.printSubTotal()));
             allLines.add(new TicketLineConstructor(""));
            if(ticket.getErrMsg().equals("")){
              try {
                  chargeslogic.calculateCharges(ticket); 
                 staxeslogic.calculateServiceTaxes(ticket);
                 for(Object ch:chargecollection){
                  TicketServiceChargeInfo   chargeline=ticket.getSChargeLine((ServiceChargeInfo) ch);
                  if(!chargeline.printRetailSCharge().equals("0.00")){
                    serviceChargeName =chargeline.getServiceChargeInfo().getName();
                  if(serviceChargeName.length()>24){
                  serviceChargeName=serviceChargeName.substring(0,24);
                  }
                   allLines.add(new TicketLineConstructor(serviceChargeName + getSpaces(12)+chargeline.printRetailSCharge()));
                 }
                 }
                  allLines.add(new TicketLineConstructor(""));
                 for(Object ch:taxcollection){
                  TicketTaxInfo   taxline=ticket.getServiceTaxLine((TaxInfo) ch);
                  if(!taxline.printRetailServiceTax().equals("0.00")){
                    serviceTaxName =taxline.getTaxInfo().getName();
                  if(serviceTaxName.length()>18){
                  serviceTaxName=serviceTaxName.substring(0,18);
                  }
                   allLines.add(new TicketLineConstructor(serviceTaxName + getSpaces(20)+taxline.printRetailServiceTax()));
                 }
                 }
              } catch (TaxesException ex) {
                  Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
              }
            }else {
                 allLines.add(new TicketLineConstructor("Service Charge" + getSpaces(12)+ticket.printServiceChargeRate()));
                 allLines.add(new TicketLineConstructor("Service Tax" + getSpaces(12)+ticket.printServiceTax()));
            }
             allLines.add(new TicketLineConstructor(""));
             for(Object ch:taxcollection){
                  TicketTaxInfo   taxline=ticket.getTaxLine((TaxInfo) ch);
                  if(!taxline.printRetailTax().equals("0.00")){
                  taxName =taxline.getTaxInfo().getName();
                  if(taxName.length()>18){
                  taxName=taxName.substring(0,18);
                  }
                   allLines.add(new TicketLineConstructor(taxName + getSpaces(27)+taxline.printRetailTax()));
                 }
                 }
              allLines.add(new TicketLineConstructor(""));
             allLines.add(new TicketLineConstructor("Total." + getSpaces(34)+"Rs. "+ticket.printTotal()));
              allLines.add(new TicketLineConstructor(""));
             if(ticket.isPrinted() && !ticket.isModified()){
                  allLines.add(new TicketLineConstructor("                Duplicate Bill"));
             }
             allLines.add(new TicketLineConstructor("Cashier." + getSpaces(34)+ticket.printUser()));
//              allLines.add(new TicketLineConstructor(""));
//             allLines.add(new TicketLineConstructor("     Download our app and enter the"));
//             allLines.add(new TicketLineConstructor("     below code to earn free beers"));
//             allLines.add(new TicketLineConstructor("                        "+ticket.printLoyalCode()));
             allLines.add(new TicketLineConstructor("        Thank you for your visit."));
             
       return allLines;
         }
     
         private String getSpaces(int len) {
        String spaces = "";
        for (int i = 0; i < len; i++) {
            spaces = spaces + " ";
        }
        return spaces;
    }
         
  private String getDottedLine(int len) {
        String dotLine = "";
        for (int i = 0; i < len; i++) {
            dotLine = dotLine + "-";
        }
        return dotLine;
    }
    
        private void defaultValues() {
        
        jListTickets.setModel(new MyListData(new ArrayList()));
        
        jcboUser.setSelectedItem(null);
        
        jtxtTicketID.reset();
        jtxtTicketID.activate();
        
        jComboBoxTicket.setSelectedIndex(0);
        
        jcboUser.setSelectedItem(null);
        
        jcboMoney.setSelectedItem( ((ListQBFModelNumber)jcboMoney.getModel()).getElementAt(0) );
        jcboMoney.revalidate();
        jcboMoney.repaint();
                
        jtxtMoney.reset();
        
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date date = c.getTime();
        jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
        c.setTime(date); 
        c.add(Calendar.DATE, 1);
        jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(c.getTime()));
        
       
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDateEnd;
    private javax.swing.JButton btnDateStart;
    private javax.swing.JButton jButtonExFilter;
    private javax.swing.JButton jButtonMail;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JComboBox jComboBoxTicket;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jListTickets;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTxtEndDate;
    private javax.swing.JTextField jTxtStartDate;
    private javax.swing.JComboBox jcboMoney;
    private javax.swing.JComboBox jcboUser;
    private javax.swing.JButton jcmdOK;
    private com.openbravo.editor.JEditorCurrency jtxtMoney;
    private com.openbravo.editor.JEditorIntegerPositive jtxtTicketID;
    private com.openbravo.editor.JEditorNumberKeys m_jKeys;
    // End of variables declaration//GEN-END:variables
}
