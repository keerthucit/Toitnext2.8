/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author archana
 */
public class MobileTypeInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String mobileType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        mobileType = dr.getString(2);
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, mobileType);
    }
}
