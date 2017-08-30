/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializerRead;

/**
 *
 * @author archana
 */
public class TiltUserInfo implements IKeyed {

    private static final long serialVersionUID = 8612449444103L;
    private String userId;

    /**
     * Creates new CategoryInfo
     */
    public TiltUserInfo(String userID) {

        userId = userID;


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Object getKey() {
        return userId;
    }

    @Override
    public String toString() {
        return userId;
    }

    public static SerializerRead getSerializerRead() {
        return new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new com.openbravo.pos.ticket.TiltUserInfo(dr.getString(1));
            }
        };
    }
}
