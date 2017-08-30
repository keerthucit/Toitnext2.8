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

package com.openbravo.pos.catalog;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.RetailThumbNailBuilder;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.TaxInfo;
import java.text.SimpleDateFormat;
import javax.swing.Timer;

/**
     *
 * @author adrianromero
 */
public class JRetailCatalog extends JPanel implements ListSelectionListener, CatalogSelector {
    
    protected EventListenerList listeners = new EventListenerList();
    private DataLogicSales m_dlSales;   
    private TaxesLogic taxeslogic;
    
    private boolean pricevisible;
    private boolean taxesincluded;
    
    // Set of Products panels
    private Map<String, ProductInfoExt> m_productsset = new HashMap<String, ProductInfoExt>();
    
    // Set of Categoriespanels
     private Set<String> m_categoriesset = new HashSet<String>();
        
    private RetailThumbNailBuilder tnbbutton;
    private RetailThumbNailBuilder tnbcat;
    
    private CategoryInfo showingcategory = null;
     private String menuId=null;
      //Current menu
    private java.util.List<MenuInfo> currentMenuList = null;
    private RefreshMenu autoRefreshMenu = new RefreshMenu();
    private Timer menuTimer = new Timer(1200, autoRefreshMenu);
    //Scheduling menu
    private java.util.List<MenuInfo> nextMenulist = null;
     private String day=null;
     private String menuStatus="";
        
    /** Creates new form JCatalog */
    public JRetailCatalog(DataLogicSales dlSales) {
        this(dlSales, false, false, 110, 57);
    }
    
    public JRetailCatalog(DataLogicSales dlSales, boolean pricevisible, boolean taxesincluded, int width, int height) {
        
        m_dlSales = dlSales;
        this.pricevisible = pricevisible;
        this.taxesincluded = taxesincluded;
        
        initComponents();
        
//        m_jListCategories.addListSelectionListener(this);
  //     m_jscrollcat.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
        
        tnbcat = new RetailThumbNailBuilder(110, 57, "com/openbravo/images/itemtoit.png","category");
        tnbbutton = new RetailThumbNailBuilder(110, 57, "com/openbravo/images/bluetoit.png","products");
     String args[]=null;
        AppConfig config = new AppConfig(args);
        config.load();
        menuStatus =config.getProperty("machine.menustatus");
    }
    
    public Component getComponent() {
        return this;
    }
   public void showRetailCatalogPanel(String id) {
        System.out.println("showRetailCatalogPanel");
            java.util.List<CategoryInfo> categories = null;
        try {
             if(menuStatus.equals("false")){
            categories = m_dlSales.getRootCategories();
          
            }else{
                 day = getWeekDay();
                currentMenuList = m_dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
            categories = m_dlSales.getMenuRootCategories(menuId);
             }
            
        } catch (BasicException ex) {
            Logger.getLogger(JCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (id == "Category") {
              m_jProducts.setVisible(false);
            m_jCategories.setVisible(true);
        }else{
                 m_jProducts.setVisible(true);
            m_jCategories.setVisible(false);
        }
         JRetailCategoryTab jCategoryTab = new JRetailCategoryTab();
        jCategoryTab.applyComponentOrientation(getComponentOrientation());
        m_jCategories.add(jCategoryTab, "PRODUCT."+id);

        // Add products
        for (CategoryInfo prod : categories) {
            jCategoryTab.addButton(new ImageIcon(tnbcat.getThumbNailText(null, getCategoryLabel(prod))), new SelectedCategory(prod));
        }
         //   showRootCategoriesPanel();
       // } else {
       //     showProductPanel(id);
       // }
    }

    public void showCatalogPanel(String id) {
//        System.out.println("showCatalogPanel"+id);
//            java.util.List<CategoryInfo> categories = null;
//        try {
//            categories = m_dlSales.getRootCategories();
//        } catch (BasicException ex) {
//            Logger.getLogger(JCatalog.class.getName()).log(Level.SEVERE, null, ex);
//        }
//     
//         m_jProducts.setVisible(true);
//        
//         JRetailCategoryTab jCategoryTab = new JRetailCategoryTab();
//        jCategoryTab.applyComponentOrientation(getComponentOrientation());
//        m_jProducts1.add(jCategoryTab, "PRODUCT."+id);
//
//       
//        for (CategoryInfo prod : categories) {
//            jCategoryTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(null, getCategoryLabel(prod))), new SelectedCategory(prod));
//        }
        
        
    }
    
    public void loadCatalog() {
        System.out.println("loadCatalog");
        // delete all categories panel
        m_jProducts.removeAll();
        
        m_productsset.clear();        
        m_categoriesset.clear();
        
        showingcategory = null;
        //newly added logic to keep scheduler for menu based category selection
        if(menuStatus.equals("true")){
         try {
                nextMenulist = m_dlSales.getMenuList();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailCatalog.class.getName()).log(Level.SEVERE, null, ex);
            }
            // loadMenu();
            //   if(menulist.size()>0){
            menuTimer.start();
            //   }
        }
        try {
            // Load the taxes logic
            taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());
        } catch (BasicException ex) {
            Logger.getLogger(JCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }

       // Load all categories.
        java.util.List<CategoryInfo> categories = null;
       System.out.println("MENU"+menuStatus+""+menuId);    
        try {
             if(menuStatus.equals("false")){
                 categories = m_dlSales.getRootCategories();
             }else{
                day = getWeekDay();
                currentMenuList = m_dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                categories = m_dlSales.getMenuRootCategories(menuId);
             }
        } catch (BasicException ex) {
            Logger.getLogger(JCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        

            m_jProducts.setVisible(false);
            m_jCategories.setVisible(true);
         JRetailCategoryTab jCategoryTab = new JRetailCategoryTab();
        jCategoryTab.applyComponentOrientation(getComponentOrientation());
        m_jCategories.add(jCategoryTab, "PRODUCT.");

        // Add products
        for (CategoryInfo prod : categories) {
            jCategoryTab.addButton(new ImageIcon(tnbcat.getThumbNailText(null, getCategoryLabel(prod))), new SelectedCategory(prod));
        }

        // Display catalog panel
      //  showRootCategoriesPanel();
    }
    
    public void setComponentEnabled(boolean value) {
        
//        m_jListCategories.setEnabled(value);
   //     m_jscrollcat.setEnabled(value);
   //     m_jUp.setEnabled(value);
   //     m_jDown.setEnabled(value);
  //      m_lblIndicator.setEnabled(value);
  //      m_btnBack1.setEnabled(value);
        m_jProducts.setEnabled(value); 
        synchronized (m_jProducts.getTreeLock()) {
            int compCount = m_jProducts.getComponentCount();
            for (int i = 0 ; i < compCount ; i++) {
                m_jProducts.getComponent(i).setEnabled(value);
            }
        }
     
        this.setEnabled(value);
    }
    
    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }
    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }

    public void valueChanged(ListSelectionEvent evt) {
        
//        if (!evt.getValueIsAdjusting()) {
//            int i = m_jListCategories.getSelectedIndex();
//            if (i >= 0) {
//                // Lo hago visible...
//                Rectangle oRect = m_jListCategories.getCellBounds(i, i);
//                m_jListCategories.scrollRectToVisible(oRect);
//            }
//        }
    }  
    
    protected void fireSelectedProduct(ProductInfoExt prod) {
        System.out.println("fireSelectedProduct");
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        for (int i = 0; i < l.length; i++) {
            if (e == null) {
                e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
            }
            ((ActionListener) l[i]).actionPerformed(e);	       
        }
    }   
    
    private void selectCategoryPanel(String catid) {
     System.out.println("selectCategoryPanel");
        try {
            // Load categories panel if not exists
            if (!m_categoriesset.contains(catid)) {
                JRetailCategoryTab jCategoryTab = new JRetailCategoryTab();
                jCategoryTab.applyComponentOrientation(getComponentOrientation());
                m_jCategories.add(jCategoryTab, "PRODUCT.");

                JRetailCatalogTab jcurrTab = new JRetailCatalogTab();
                jcurrTab.applyComponentOrientation(getComponentOrientation());
                m_jProducts.add(jcurrTab, catid);
                m_categoriesset.add(catid);
               
                // Add subcategories
                java.util.List<CategoryInfo> categories = m_dlSales.getSubcategories(catid);
                for (CategoryInfo cat : categories) {
                    jcurrTab.addButton(new ImageIcon(tnbcat.getThumbNailText(null, cat.getName())), new SelectedCategory(cat));
                }
                
                  // Add products 
                 java.util.List<ProductInfoExt> products=null;
                if(menuStatus.equals("false")){
                products = m_dlSales.getProductCatalog(catid);
                }else{
                // day = getWeekDay();
               //  menuId=m_dlSales.getMenuId(day);     
                     day = getWeekDay();
                    currentMenuList = m_dlSales.getMenuId(day);
                    if (currentMenuList.size() != 0) {
                        menuId = currentMenuList.get(0).getId();
                    }
                products = m_dlSales.getMenuProductCatalog(catid,menuId);
                 }
                 for (ProductInfoExt prod : products) {
                    jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
                }
            }
            
            // Show categories panel
            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, catid);  
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notactive"), e));            
        }
    }
    
    private String getProductLabel(ProductInfoExt product) {

        if (pricevisible) {
            if (taxesincluded) {
                TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID());
                return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax(tax);
            } else {
                return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
            }
        } else {
            return "<html><center>" +product.getName()+ "<br>" ;
        }
    }
      private String getCategoryLabel(CategoryInfo product) {
     //   System.out.println("getCategoryLabel");
     //  if (pricevisible) {
          //  if (taxesincluded) {
              //  TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID());
                return "<html><center>" + product.getName() + "<br>" ;
         //   } else {
         //       return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
        //    }
      //  } else {
     //       return product.getName();
      //  }
    }

    private void selectIndicatorPanel(Icon icon, String label) {
        System.out.println("selectIndicatorPanel");
//        m_lblIndicator.setText(label);
//        m_lblIndicator.setIcon(icon);
//
//     //    Show subcategories panel
        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
        cl.show(m_jProducts, "subcategories");
    }
    
    private void selectIndicatorCategories() {
//        // Show root categories panel
//        CardLayout cl = (CardLayout)(m_jCategories.getLayout());
//        cl.show(m_jCategories, "rootcategories");
    }
    
    private void showRootCategoriesPanel() {
        
//        selectIndicatorCategories();
//        // Show selected root category
//        CategoryInfo cat = (CategoryInfo) m_jListCategories.getSelectedValue();
//
//        if (cat != null) {
//            selectCategoryPanel(cat.getID());
//        }
//        showingcategory = null;
    }
    
    private void showSubcategoryPanel(CategoryInfo category) {
        selectIndicatorPanel(new ImageIcon(tnbcat.getThumbNail(category.getImage())), category.getName());
        selectCategoryPanel(category.getID());
        showingcategory = category;
    }
      
//    private void showProductPanel(String id) {
//           System.out.println("showProductPanel");
//       //      ProductInfoExt product = m_productsset.get(id);
//
//       // if (product == null) {
////            if (m_productsset.containsKey(id)) {
////                // It is an empty panel
////                if (showingcategory == null) {
////                    showRootCategoriesPanel();
////                } else {
////                    showSubcategoryPanel(showingcategory);
////                }
////            } else {
////                try {
//                    // Create  products panel
//                    //java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(id);
//
////                    if (products.size() == 0) {
////                        // no hay productos por tanto lo anado a la de vacios y muestro el panel principal.
////                        m_productsset.put(id, null);
////                        if (showingcategory == null) {
////                            showRootCategoriesPanel();
////                        } else {
////                            showSubcategoryPanel(showingcategory);
////                        }
////                    } else {
//
//                        // Load product panel
//                        java.util.List<ProductInfoExt> product = null;
//                        try {
//                            product = m_dlSales.getProductCatalog(id);
//                            //  m_productsset.put(id, product);
//                        } catch (BasicException ex) {
//                            Logger.getLogger(JCatalog.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                      //  m_productsset.put(id, product);
//
//                        JRetailCatalogTab jcurrTab = new JRetailCatalogTab();
//                        jcurrTab.applyComponentOrientation(getComponentOrientation());
//                        m_jProducts.add(jcurrTab, "PRODUCT." + id);
//
//                        // Add products
//                        for (ProductInfoExt prod : product) {
//                            jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
//                        }
//
//                       // selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());
//
//                        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
//                        cl.show(m_jProducts, "PRODUCT." + id);
//                  ///  }
////                } catch (BasicException eb) {
////                    eb.printStackTrace();
////                    m_productsset.put(id, null);
////                    if (showingcategory == null) {
////                        showRootCategoriesPanel();
////                    } else {
////                        showSubcategoryPanel(showingcategory);
////                    }
////                }
//           // }
////        } else {
////            // already exists
////            selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());
////
////            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
////            cl.show(m_jProducts, "PRODUCT." + id);
////        }
//    }
//

        private void showProductPanel(String id) {
            

        ProductInfoExt product = m_productsset.get(id);

        if (product == null) {
            if (m_productsset.containsKey(id)) {
                // It is an empty panel
                if (showingcategory == null) {
                    showRootCategoriesPanel();
                } else {
                    showSubcategoryPanel(showingcategory);
                }
            } else {
                try {
                   // Create  products panel
                      java.util.List<ProductInfoExt> products=null;
                      if(menuStatus.equals("false")){
                     products = m_dlSales.getProductComments(id);
                      }else{
                          day = getWeekDay();
                        currentMenuList = m_dlSales.getMenuId(day);
                        if (currentMenuList.size() != 0) {
                            menuId = currentMenuList.get(0).getId();
                        }
                         products = m_dlSales.getMenuProductComments(id,menuId);
                      }
                    if (products.size() == 0) {
                        // no hay productos por tanto lo anado a la de vacios y muestro el panel principal.
                        m_productsset.put(id, null);
                        if (showingcategory == null) {
                            showRootCategoriesPanel();
                        } else {
                            showSubcategoryPanel(showingcategory);
                        }
                    } else {

                        // Load product panel
                        product = m_dlSales.getProductInfo(id);
                        m_productsset.put(id, product);

                        JCatalogTab jcurrTab = new JCatalogTab();
                        jcurrTab.applyComponentOrientation(getComponentOrientation());
                        m_jProducts.add(jcurrTab, "PRODUCT." + id);
                        System.out.println("product is calling");
                        // Add products
                        for (ProductInfoExt prod : products) {
                            jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
                        }

                        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

                        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
                        cl.show(m_jProducts, "PRODUCT." + id);
                    }
                } catch (BasicException eb) {
                    eb.printStackTrace();
                    m_productsset.put(id, null);
                    if (showingcategory == null) {
                        showRootCategoriesPanel();
                    } else {
                        showSubcategoryPanel(showingcategory);
                    }
                }
            }
        } else {
            // already exists
            selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(null)), product.getName());

            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, "PRODUCT." + id);
        }
    }
    private class SelectedAction implements ActionListener {
        private ProductInfoExt prod;
        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }
        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);
        }
    }
    
    private class SelectedCategory implements ActionListener {
        private CategoryInfo category;
        public SelectedCategory(CategoryInfo category) {
            this.category = category;
        }
        public void actionPerformed(ActionEvent e) {
            showSubcategoryPanel(category);
            m_jCategories.setVisible(false);
            m_jProducts.setVisible(true);
            showProductPanel(category.getID());
        }
    }
    
    private class CategoriesListModel extends AbstractListModel {
        private java.util.List m_aCategories;
        public CategoriesListModel(java.util.List aCategories) {
            m_aCategories = aCategories;
        }
        public int getSize() { 
            return m_aCategories.size(); 
        }
        public Object getElementAt(int i) {
            return m_aCategories.get(i);
        }    
    }
    
    private class SmallCategoryRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
            CategoryInfo cat = (CategoryInfo) value;
            setText(cat.getName());
            setIcon(new ImageIcon(tnbcat.getThumbNail(cat.getImage())));
            return this;
        }      
    }      
    
            public String getWeekDay(){
        String DAY="";
        Calendar cal =Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek)
        {
            case 1:
            DAY="SUNDAY";
            break;
            case 2:
            DAY="MONDAY";
            break;
            case 3:
            DAY="TUESDAY";
            break;
            case 4:
            DAY="WEDNESDAY";
            break;
            case 5:
            DAY="THURSDAY";
            break;
            case 6:
            DAY="FRIDAY";
            break;
            case 7:
            DAY="SATURDAY";
            break;
            }
        return DAY;
    }
    
        private class RefreshMenu implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
         loadMenu();
          }

    }
         private void loadMenu() {
        //   System.out.println("loadMenu-----");    
        Date sysdate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentTime = time.format(sysdate);
        for (MenuInfo m : nextMenulist) {
          //  System.out.println("loadMenu-----" + currentTime + " " + m.getTime());
            if (m.getStartTime().equals(currentTime) || m.getEndTime().equals(currentTime)) {
                System.out.println("peak time-----");
                try {
                     day = getWeekDay();
                    currentMenuList = m_dlSales.getMenuId(day);
                    if (currentMenuList.size() != 0) {
                        menuId = currentMenuList.get(0).getId();
                    }
                    m_jProducts.removeAll();
                    m_jCategories.removeAll();
                    m_productsset.clear();
                    m_categoriesset.clear();
                    showingcategory = null;
                    showRetailCatalogPanel("Category");
                    
          } catch (BasicException ex) {
                    Logger.getLogger(JRetailCatalog.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jProducts = new javax.swing.JPanel();
        m_jCategories = new javax.swing.JPanel();

        setBackground(new java.awt.Color(222, 232, 231));
        setPreferredSize(new java.awt.Dimension(390, 1500));

        m_jProducts.setPreferredSize(new java.awt.Dimension(200, 200));
        m_jProducts.setLayout(new java.awt.CardLayout());

        m_jCategories.setPreferredSize(new java.awt.Dimension(385, 195));
        m_jCategories.setLayout(new java.awt.CardLayout());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jCategories, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .add(m_jProducts, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jProducts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 325, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCategories, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 335, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1165, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel m_jCategories;
    private javax.swing.JPanel m_jProducts;
    // End of variables declaration//GEN-END:variables
    
}
