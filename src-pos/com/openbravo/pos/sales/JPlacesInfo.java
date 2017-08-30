/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializerRead;
import java.io.Serializable;

/**
 *
 * @author archana
 */
public class JPlacesInfo  implements Serializable, IKeyed {
    private static final long serialVersionUID = 8959679342805L;
    private String id;
    private String name;

    public JPlacesInfo(String plcId, String plcName) {
      id= plcId;
      name =plcName;
    }
    
   public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getKey() {
        return id;
    }
    
        @Override
    public String toString(){
        return name;
    }
            public static SerializerRead getSerializerRead() {
        return new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
            return new JPlacesInfo(dr.getString(1), dr.getString(2));
        }};
    }  

    
}
