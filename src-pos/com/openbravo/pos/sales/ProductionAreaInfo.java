/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.data.loader.IKeyed;
import java.io.Serializable;

/**
 *
 * @author archana
 */
public class ProductionAreaInfo implements Serializable, IKeyed {
    private static final long serialVersionUID = 8959679342805L;
    private String id;
    private String name;
    
   public ProductionAreaInfo(String proAreaId, String proNameName) {
      id= proAreaId;
      name =proNameName;
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
    
}
