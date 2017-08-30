/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.data.loader.SerializerRead;
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
public class TiltNameInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 8612449444103L;
    private String TNid;
 private String tilt;
    private String access;
    private List<TiltNameInfo> TNLines;
    
    
     public TiltNameInfo() {
        TNid = UUID.randomUUID().toString();
        TNLines = new ArrayList<TiltNameInfo>();
    }

    
     public TiltNameInfo(List<TiltNameInfo> tnLines,String tnId, String tiltNo, String acc) {
         TNLines=tnLines;
        TNid = tnId;
        tilt = tiltNo;
        access = acc;
    }


  

     public void insertLine(int index, TiltNameInfo tnLine) {
        TNLines.add(index, tnLine);

    }

    public void setLine(int index, TiltNameInfo tnLine) {
        TNLines.set(index, tnLine);
    }

    public void removeLine(int index) {
        TNLines.remove(index);

    }
    public String getTNid() {
        return TNid;
    }

    public void setTNid(String TNid) {
        this.TNid = TNid;
    }
  public String getTilt() {
        System.out.println("Floor " + tilt);
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    public String getAccess() {
        System.out.println("Access " + access);
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
    public List<TiltNameInfo> getTNLines() {
        return TNLines;
    }

    public void setTNLines(List<TiltNameInfo> TNLines) {
        this.TNLines = TNLines;
    }
   
   

    @Override
    public void readValues(DataRead dr) throws BasicException {
        TNid = dr.getString(1);
        tilt = dr.getString(2);
        access = dr.getString(3);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, TNid);
        dp.setString(2, tilt);
        dp.setString(3, access);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
