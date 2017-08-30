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
import java.util.Date;

/**
 *
 * @author sysfore
 */
public class TiltSessionInfo implements SerializableRead, Externalizable {

    private String tsnId;
    private String userId;
    private Date login;
    private Date logout;
    private String tilt;

    public TiltSessionInfo() {
        tsnId = UUID.randomUUID().toString();
        tsLines = new ArrayList<TiltSessionInfo>();
    }

    public TiltSessionInfo(String tsnId, String userid, Date Login, Date Logout, String Tilt, List<TiltSessionInfo> tsLines) {
        this.tsnId = tsnId;
        this.userId = userid;
        this.login = Login;
        this.logout = Logout;
        this.tilt = Tilt;

        this.tsLines = tsLines;

    }

    public List<TiltSessionInfo> getTsLines() {
        return tsLines;
    }

    public void setTsLines(List<TiltSessionInfo> tsLines) {
        this.tsLines = tsLines;
    }
    private List<TiltSessionInfo> tsLines;

    public List<TiltSessionInfo> getTLines() {
        return tsLines;
    }

    public void setTLines(List<TiltSessionInfo> TSLines) {
        this.tsLines = TSLines;
    }

    public void insertLine(int index, TiltSessionInfo tsLine) {
        tsLines.add(index, tsLine);

    }

    public void setLine(int index, TiltSessionInfo tLine) {
        tsLines.set(index, tLine);
    }

    public void removeLine(int index) {
        tsLines.remove(index);

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTsnId() {
        return tsnId;
    }

    public void setTsnId(String tsnId) {
        this.tsnId = tsnId;
    }

    public Date getLogin() {
        return login;
    }

    public void setLogin(Date login) {
        this.login = login;
    }

    public String getTilt() {
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    public Date getLogout() {
        return logout;
    }

    public void setLogout(Date logout) {
        this.logout = logout;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
      //  System.out.println("readValues");
        tsnId = dr.getString(1);
        userId = dr.getString(2);
        login = dr.getTimestamp(3);
        logout = dr.getTimestamp(4);
        tilt = dr.getString(5);

        tsLines = new ArrayList<TiltSessionInfo>();

        //  throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
      //  System.out.println("writeExternal");

        out.writeObject(tsnId);
        out.writeObject(userId);
        out.writeObject(login);
        out.writeObject(logout);
        out.writeObject(tilt);

        out.writeObject(tsLines);
        // throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       // System.out.println("ReadExternal");
        tsnId = (String) in.readObject();
        userId = (String) in.readObject();
        login = (Date) in.readObject();
        logout = (Date) in.readObject();
        tilt = (String) in.readObject();
        tsLines = (List<TiltSessionInfo>) in.readObject();
        //  throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
}
