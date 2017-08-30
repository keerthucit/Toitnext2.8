/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author preethi
 */
public class CampaignIdInfo implements SerializableRead, SerializableWrite {

    private String campaignId;


  public CampaignIdInfo() {
    }

  public void readValues(DataRead dr) throws BasicException {
       campaignId = dr.getString(1);

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, campaignId);     
    }

    public String getcampaignId(){
        return campaignId;
    }
    public void setcampaignId(String campaignId){
        this.campaignId = campaignId;
    }
}