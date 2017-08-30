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
public class TillInfo implements IKeyed {

    private static final long serialVersionUID = 8612449444103L;
    private String tillNo;

    /**
     * Creates new CategoryInfo
     */
    public TillInfo(String tillno) {

        tillNo = tillno;

    }

    public String getName() {
        return tillNo;
    }

    public void setName(String tillno) {
        tillNo = tillno;
    }

    @Override
    public String toString() {
        return tillNo;
    }

    public static SerializerRead getSerializerRead() {
        return new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new com.openbravo.pos.ticket.TillInfo(dr.getString(1));
            }
        };
    }

    @Override
    public Object getKey() {
        return tillNo;
    }
}
