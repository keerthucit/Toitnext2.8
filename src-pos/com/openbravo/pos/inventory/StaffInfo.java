/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.inventory;

/**
 *
 * @author mateen
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class StaffInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String searchkey;
    private String name;
   private String phno;
   private String active;
   

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        searchkey=dr.getString(2);
        name=dr.getString(3);
        phno=dr.getString(4);
        setActive(dr.getString(5));
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, searchkey);
        dp.setString(3, name);
        dp.setString(4, phno);
        dp.setString(5, getActive());
    }

    public String getId() {
        return id;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the searchkey
     */
    public String getSearchkey() {
        return searchkey;
    }

    /**
     * @param searchkey the searchkey to set
     */
    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    /**
     * @return the description
     */
    public String getPhNo() {
        return phno;
    }

    /**
     * @param description the description to set
     */
    public void setPhNo(String phno) {
        this.phno = phno;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }


}
