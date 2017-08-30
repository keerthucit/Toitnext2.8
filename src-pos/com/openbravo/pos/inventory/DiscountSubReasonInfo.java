/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author archana
 */
public class DiscountSubReasonInfo implements SerializableRead, SerializableWrite {
   private static final long serialVersionUID = 7640633837719L;
   private String subreasonId;
   private String reasonId;
   private String subReason;
   private String active;
   
   public DiscountSubReasonInfo(){
       
   }
   
   public DiscountSubReasonInfo(String reasonId,String subreasonid,String subreason,String active){
       subreasonId=subreasonid;
      this.reasonId=reasonId;
      subReason=subreason;
      this.active=active;
   }
    public DiscountSubReasonInfo(String reasonId,String subreasonid,String subreason){
       subreasonId=subreasonid;
      this.reasonId=reasonId;
      subReason=subreason;
    
   }

    public String getSubreasonId() {
        return subreasonId;
    }

    public void setSubreasonId(String id) {
        this.subreasonId = id;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reason) {
        this.reasonId = reason;
    }

    public String getSubReason() {
        return subReason;
    }

    public void setSubReason(String subReason) {
        this.subReason = subReason;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
           subreasonId = dr.getString(1);
        reasonId=dr.getString(2);
        subReason=dr.getString(3);
        active=dr.getString(4);
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
         dp.setString(1, subreasonId);
        dp.setString(2, reasonId);
        dp.setString(3, subReason);
        dp.setString(4, active);
    }

//  //  @Override
//    public boolean equals(final DiscountSubReasonInfo obj) {
//       if(this.reasonId.equals(obj.reasonId) && this.subreasonId.equals(obj.subreasonId)) {
//          return true;
//       }else{
//       return false;
//       }
//        
//    }
//    
//    public int hashCode() {
//        return reasonId.hashCode()+subreasonId.hashCode();
//    }

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
