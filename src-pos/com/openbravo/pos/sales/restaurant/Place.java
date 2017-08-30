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

package com.openbravo.pos.sales.restaurant;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import com.openbravo.data.gui.NullIcon;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.basic.BasicException;
import java.awt.Color;

public class Place implements SerializableRead, java.io.Serializable {
    
    private static final long serialVersionUID = 8652254694281L;
    private static final Icon ICO_OCU = new ImageIcon(Place.class.getResource("/com/openbravo/images/edit_group.png"));
    private static final Icon ICO_FRE = new NullIcon(22, 22);
    
    private String m_sId;
    private String m_sName;
    private int m_ix;
    private int m_iy;
    private String m_sfloor;
    
    private boolean m_bPeople;
    private JButton m_btn;
    private int isSplit=0;
   private String takeaway;
        
    /** Creates a new instance of TablePlace */
    public Place() {
    }        
    
    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        m_sName = dr.getString(2);
        m_ix = dr.getInt(3).intValue();
        m_iy = dr.getInt(4).intValue();
        m_sfloor = dr.getString(5);
        
        m_bPeople = false;
        m_btn = new JButton();

        m_btn.setFocusPainted(false);
        m_btn.setFocusable(false);
        m_btn.setRequestFocusEnabled(false);
        m_btn.setHorizontalTextPosition(SwingConstants.CENTER);
        m_btn.setVerticalTextPosition(SwingConstants.BOTTOM);            
        m_btn.setIcon(ICO_FRE);
        m_btn.setText(m_sName);
        m_btn.setPreferredSize(new java.awt.Dimension(70, 55));
        takeaway="N";
        
    }

    public String getId() { return m_sId; }
    
    public String getName() { return m_sName; }

    public int getX() { return m_ix; }

    public int getY() { return m_iy; }

    public String getFloor() { return m_sfloor; }
   
    public JButton getButton() { return m_btn; }

    public boolean hasPeople() {
        return m_bPeople;
    }   
    public void setPeople(boolean bValue) {
        m_bPeople = bValue;
        m_btn.setIcon(bValue ? ICO_OCU : ICO_FRE); 
    }     
    public void setButtonBounds() {
        Dimension d = m_btn.getPreferredSize();
        m_btn.setBounds(m_ix - d.width / 2, m_iy - d.height / 2, d.width, d.height); 
    }

    /**
     * @return the isSplit
     */
    public int getIsSplit() {
        return isSplit;
    }

    /**
     * @param isSplit the isSplit to set
     */
    public void setIsSplit(int isSplit) {
        this.isSplit = isSplit;
    }
    
public void setColor(String color){
        if(color.equals("RED")){
            m_btn.setForeground(Color.WHITE);
            m_btn.setBackground(Color.RED);
            m_btn.setToolTipText(String.format("%s is currently occupied", m_sName));
        }
        else if(color.equals("BLUE"))
        {
            m_btn.setForeground(Color.WHITE);
            m_btn.setBackground(Color.BLUE);
            m_btn.setToolTipText(String.format("%s is currently occupied with bill printed", m_sName));
        }else{
            m_btn.setForeground(Color.BLACK);
            m_btn.setBackground(new Color(174, 255, 255));
            m_btn.setToolTipText(String.format("%s is Free", m_sName));
        }
    }

    /**
     * @return the takeaway
     */
    public String getTakeaway() {
        return takeaway;
    }

    /**
     * @param takeaway the takeaway to set
     */
    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }
}    

    
