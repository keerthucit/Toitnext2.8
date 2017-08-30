/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author sysfore
 */
public class ShiftTallyInfo implements SerializableRead, Externalizable  {
private String sId;
     private List<ShiftTallyLineInfo> sLines;
    public ShiftTallyInfo() {
       sId=UUID.randomUUID().toString();
     sLines=new ArrayList<ShiftTallyLineInfo>();
    }
    
     

    public ShiftTallyInfo(String m_sId, List<ShiftTallyLineInfo> sLines) {
        this.sId = m_sId;
        this.sLines = sLines;
    }
    

    public String getM_sId() {
        return sId;
    }

    public void setM_sId(String m_sId) {
        this.sId = m_sId;
    }

    public List<ShiftTallyLineInfo> getsLines() {
        return sLines;
    }

    public void setsLines(List<ShiftTallyLineInfo> sLines) {
        this.sLines = sLines;
    }

    
     public void insertLine(int index, ShiftTallyLineInfo oLine) {
        sLines.add(index, oLine);
        
    }

    public void setLine(int index, ShiftTallyLineInfo oLine) {
            sLines.set(index, oLine);
    }

    public void removeLine(int index) {
        sLines.remove(index);
        
    }
    @Override
    public void readValues(DataRead dr) throws BasicException {
         System.out.println("readValues");
       sId = dr.getString(1);
        sLines = new ArrayList<ShiftTallyLineInfo>();

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
         System.out.println("writeExternal");
            
        out.writeObject(sId);
         //List<RetailTicketLineInfo> check = m_aLines;
        try {
            out.writeObject(sLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
         System.out.println("ReadExternal");
        sId = (String) in.readObject();
        sLines = (List<ShiftTallyLineInfo>) in.readObject();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
     
}
