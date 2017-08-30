//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.sales;

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.RoleInfo;
import com.openbravo.pos.inventory.RoleUserInfo;
import com.openbravo.pos.inventory.StaffInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.util.date.DateFormats;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class DataLogicReceipts extends BeanFactoryDataSingle {

    private Session s;
    private String moveTxstatus = "Default";
    public static String cashTallyId = " ";
    private Properties m_propsconfig;
    public AppConfig config;

    /**
     * Creates a new instance of DataLogicReceipts
     */
    public DataLogicReceipts() {
    }

    public void init(Session s) {
        this.s = s;
    }

    public final TicketInfo getSharedTicket(String Id) throws BasicException {

        if (Id == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (TicketInfo) record[0];
        }
    }

    public final RetailTicketInfo getRetailSharedTicket(String Id) throws BasicException {
        System.out.println("retriving content from  db" + Id);
        if (Id == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }

    public final List<SharedTicketInfo> getSharedTicketList() throws BasicException {

        return (List<SharedTicketInfo>) new StaticSentence(s, "SELECT ID, NAME,ISPRINTED,ISMODIFIED FROM SHAREDTICKETS ORDER BY ID", null, new SerializerReadClass(SharedTicketInfo.class)).list();
    }

    public final void updateSharedTicket(final String id, final TicketInfo ticket) throws BasicException {

        Object[] values = new Object[]{id, ticket.getName(), ticket};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s, "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ? WHERE ID = ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 0})).exec(values);
    }

    public final void updateSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        System.out.println("in updateSharedTicket method" + ticket.isTicketOpen());
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified(), 0};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT};
        new PreparedSentence(s, "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?, UPDATED=NOW() ,ISKDS=?  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 6, 0, 3})).exec(values);
    }

    public final void updateTakeawayTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        System.out.println("in updateSharedTicket method" + ticket.isTicketOpen());
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified(), 0};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT};
        new PreparedSentence(s, "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=?  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 6, 0, 3})).exec(values);
    }

    public final void insertSharedTicket(final String id, final TicketInfo ticket) throws BasicException {

        Object[] values = new Object[]{id, ticket.getName(), ticket};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s, "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT) VALUES (?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
    }

    public final void insertTableCovers(final String id, final String tableid, String splitId, int covers) throws BasicException {

        Object[] values = new Object[]{id, tableid, splitId, covers};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT};

        new PreparedSentence(s, "INSERT INTO TABLECOVERS (ID, TABLEID, SPLITID,NOOFCOVERS) VALUES (?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);
    }

    public final void insertRetailSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        String splitId = UUID.randomUUID().toString().replaceAll("-", "");
        ticket.setSplitSharedId(splitId);
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isModified(), 0, ticket.getTakeaway()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT, Datas.STRING};

        new PreparedSentence(s, "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT,SPLITID,ISPRINTED,ISMODIFIED,UPDATED,ISKDS,ISTAKEAWAY) VALUES (?, ?, ?,?,?,?,NOW(),?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);
    }

    public final void insertRetailTakeawayTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
//         String splitId= UUID.randomUUID().toString().replaceAll("-", "");
//         ticket.setSplitSharedId(splitId);
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isModified(), 0, ticket.getTakeaway()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT, Datas.STRING};

        new PreparedSentence(s, "INSERT INTO TAKEAWAYTICKETS (ID, NAME,CONTENT,SPLITID,ISPRINTED,ISMODIFIED,UPDATED,ISKDS,ISTAKEAWAY) VALUES (?, ?, ?,?,?,?,NOW(),?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);
    }

    public final void deleteSharedTicket(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM SHAREDTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(id);
    }

    public final void deleteTakeawayTicket(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM TAKEAWAYTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(id);
    }

    public String getRolebyName(String user) throws BasicException {
        if (user == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT R.NAME AS rolename FROM ROLES R,PEOPLE P WHERE P.ROLE=R.ID AND P.NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(user);
            return record == null ? "" : record[0].toString();
        }
    }

    public List<DiscountRateinfo> getDiscountList() throws BasicException {

        return (List<DiscountRateinfo>) new StaticSentence(s, "SELECT ID, RATE, NAME FROM DISCOUNTRATE ", null, new SerializerReadClass(DiscountRateinfo.class)).list();
    }

    public List<DiscountReasonInfo> getDiscountReason() throws BasicException {

        return (List<DiscountReasonInfo>) new StaticSentence(s, "SELECT ID, REASON,ISACTIVE FROM DISCOUNTREASON WHERE ISACTIVE='Y' ", null, new SerializerReadClass(DiscountReasonInfo.class)).list();
    }

    public String getDiscountLine(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT RATE FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? null : (String) record[0];
    }

    public List<Reasoninfo> getReasonList() throws BasicException {
        return (List<Reasoninfo>) new StaticSentence(s, "SELECT ID, REASON, STATUS FROM KOTREASON ", null, new SerializerReadClass(Reasoninfo.class)).list();
    }

    public String getReasonId(String reasonItem) throws BasicException {
        System.out.println("enrtr " + reasonItem);
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM KOTREASON WHERE REASON = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(reasonItem);
        return record == null ? null : (String) record[0];
    }
//    public void updateKotCancel(String productId, String id, String isCancelled,String reason,double qty,String reasonId) {
//
//           Object[] values = new Object[] {productId,id,qty,isCancelled,reason,reasonId};
//
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING,  Datas.STRING,  Datas.STRING};
//            try {
//                new PreparedSentence(s, "UPDATE KOT SET REASONID=? ,REASON = ?, ISCANCELLED=?,ISPRINTED='N', QTY=? WHERE TICKET = ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{5, 4, 3, 2, 1, 0})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//
//         }
//
//
//    }

    public void updateNoOfCovers(String tableid, int noofcovers, String splitId) {

        Object[] values = new Object[]{tableid, noofcovers, splitId};

        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE TABLECOVERS SET NOOFCOVERS=? WHERE SPLITID=? AND TABLEID = ?  ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void updateTableName(String tableid, String newTableid, String splitId) {

        Object[] values = new Object[]{tableid, newTableid, splitId};

        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE TABLECOVERS SET TABLEID=?, SPLITID=? WHERE TABLEID = ? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);

        }


    }
//     public void updateKotCancel(String productId, String id, String isCancelled,String reason) {
//
//           Object[] values = new Object[] {productId,id,isCancelled,reason};
//
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING,  Datas.STRING,  Datas.STRING};
//            try {
//                new PreparedSentence(s, "UPDATE KOT SET REASON = ?, ISCANCELLED=?,ISPRINTED='N' WHERE TICKET = ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{3, 2, 1, 0})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//
//         }
//
//
//    }

    public final List<KotTicketListInfo> getKotTicketList(String ticketid) throws BasicException {

        return (List<KotTicketListInfo>) new StaticSentence(s, "SELECT DISTINCT KOTID FROM KOT  WHERE TICKET='" + ticketid + "' AND ISCANCELLED='N'", null, new SerializerReadClass(KotTicketListInfo.class)).list();
    }

    public String getkotTicketId(String isPrinted) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT MAX(KOTID) FROM KOT WHERE ISPRINTED='" + isPrinted + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(isPrinted);
        return record == null ? "" : (String) record[0];
    }

    public int getTableCovers(String tableId, String splitId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT NOOFCOVERS FROM TABLECOVERS WHERE TABLEID='" + tableId + "' AND SPLITID='" + splitId + "'  ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getkotIsprinted(String productid, String ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM KOT WHERE PRODUCTID='" + productid + "' AND TICKET='" + ticketId + "' AND ISPRINTED='Y' AND ISCANCELLED='N'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getkotprinted(String ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM KOT WHERE TICKET='" + ticketId + "' AND ISPRINTED='Y'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public double getkotQty(String productid, String ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT sum(QTY) FROM KOT WHERE PRODUCTID='" + productid + "' AND TICKET='" + ticketId + "' AND ISPRINTED='Y'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        double i = Double.parseDouble(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public void updateKotIsprinted(String id) throws BasicException {
        System.out.println("eneter id" + id);
        String isPrinted = "Y";
        new StaticSentence(s, "UPDATE KOT SET ISPRINTED='" + isPrinted + "' WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(id);
    }

    //changed to save with server date
// public void insertKot(RetailTicketInfo m_oTicket, String isPrinted, String ticketId, int kotid, int a,String person) {
//
//         for (RetailTicketLineInfo l : m_oTicket.getLines()) {
//           Object[] values = new Object[] {UUID.randomUUID().toString(), ticketId,m_oTicket.getTicketId(),l.getProductID(), isPrinted, l.getMultiply(), kotid,m_oTicket.getPlaceId(),l.getInstruction(),person};
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.INT,Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.DOUBLE,Datas.STRING,Datas.STRING};
//            try {
//                new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,TABLEID,INSTRUCTION,PERSON) VALUES (?, ?,NOW(), ?, ?, ?, ?, ?, ?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8,9,10})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//            }
//         }
//    }
    //changed to save with server date
    public void insertPrintedKot(String ticketid, int billNo, String productId, String isPrinted, double qty, int kotid, String tableId, String instruction, String person) {
        System.out.println("isPrinted 2" + isPrinted);
        Object[] values = new Object[]{UUID.randomUUID().toString(), ticketid, billNo, productId, isPrinted, qty, kotid, tableId, instruction, person};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING};
        try {
            System.out.println("isPrinted 2 final" + isPrinted);
            new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID, TABLEID,INSTRUCTION,PERSON) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertRetailPrintedKot(String ticketid, int billNo, String productId, String isPrinted, double qty, int kotid, String isCancel, String reason, String reasonId, String tableId, String person, int orderNo) {

        Object[] values = new Object[]{UUID.randomUUID().toString(), ticketid, billNo, productId, isPrinted, qty, kotid, isCancel, reason, reasonId, tableId, person, orderNo};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT};
        try {
            new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,ISCANCELLED,REASON,REASONID,TABLEID,PERSON,ORDERNUM) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //changed to save with server date

    public void insertCancelledKot(String ticketid, int billNo, String productId, String isPrinted, double qty, int kotid, String isCancel, String reason, String reasonId, String tableId, String person, int orderNo) {

        Object[] values = new Object[]{UUID.randomUUID().toString(), ticketid, billNo, productId, isPrinted, qty, kotid, isCancel, reason, reasonId, tableId, person, orderNo};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT};
        try {
            new PreparedSentence(s, "INSERT INTO KOT (ID,TICKET,DATENEW,BILLNO, PRODUCTID,ISPRINTED, QTY, KOTID,ISCANCELLED,REASON,REASONID,TABLEID,PERSON,ORDERNUM) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final List<kotInfo> getKot(String ticketId) throws BasicException {

        return (List<kotInfo>) new StaticSentence(s, "SELECT k.ID,k.TICKET, k.PRODUCTID,k.ISPRINTED,k.QTY,k.KOTID,p.NAME,k.INSTRUCTION FROM KOT k, PRODUCTS p where p.ID=k.PRODUCTID and k.TICKET = '" + ticketId + "' and k.ISPRINTED='N' ORDER BY ID", null, new SerializerReadClass(kotInfo.class)).list();
    }

    public final List<kotPrintedInfo> getisPrintedKot(String ticketId) throws BasicException {

        return (List<kotPrintedInfo>) new StaticSentence(s, "SELECT PRODUCTID,sum(QTY),ISCANCELLED FROM KOT where TICKET = '" + ticketId + "' and ISPRINTED='Y' GROUP BY PRODUCTID", null, new SerializerReadClass(kotPrintedInfo.class)).list();
    }

    //Query: To Check if there is any record in KOT table for given bill No., i.e any item has been sent to KOT yet?
    public final String isKotEntered(String billNo) throws BasicException {
        Datas[] datatype = new Datas[]{Datas.STRING};
        String sqlQuery = "SELECT COUNT(ID) FROM KOT WHERE BILLNO = ?";
        String isPrinted = null;
        try {
            //first create an array of Objects , consisting of all the results returned by StaticSentence
            //here only one object will be returned always which is count of records in KOT Table.
            Object[] record = (Object[]) new StaticSentence(s, sqlQuery, SerializerWriteString.INSTANCE, new SerializerReadBasic(datatype)).find(billNo);
            isPrinted = (String) record[0]; //typecast the first element of object array into String and pass reference to isPrinted variable.
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isPrinted == null ? null : isPrinted; //return isPrinted as String , giving count of KOT records.
    }

    public final void deleteKot(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM KOt WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(id);
    }

    public void insertToken(int tokenId, String ticketid, Date dateNew, String USER_ID, int active) {
        System.out.println("m_oTicket.getTicketId() in query" + tokenId);
        Object[] values = new Object[]{tokenId, USER_ID, dateNew, active, ticketid};
        Datas[] datas = new Datas[]{Datas.INT, Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO tbl_token (TOKEN_ID,USER_ID,CREATEDDATE,MODIFIEDDATE,ACTIVE,TICKETID) VALUES (?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 2, 3, 4})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertTableOrder(int tokenId, String USER_ID, String tableId, double amount, double tax, double total, int preStatus, int active, String ticketid, Date orderDate) {

        Object[] values = new Object[]{tokenId, tableId, USER_ID, amount, tax, total, preStatus, active, ticketid, orderDate};
        Datas[] datas = new Datas[]{Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.INT, Datas.INT, Datas.STRING, Datas.TIMESTAMP};
        try {
            new PreparedSentence(s, "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//        public void insertTableOrderSample(int tokenId,String USER_ID,String tableId,String amount, double tax, double total,int preStatus, int active,String ticketid, Date orderDate) {
//
//           Object[] values = new Object[] {tokenId,tableId,USER_ID,amount,tax,total,preStatus,active,ticketid,orderDate};
//        Datas[] datas = new Datas[] {Datas.INT,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.INT,Datas.INT,Datas.STRING, Datas.TIMESTAMP};
//            try {
//                new PreparedSentence(s, "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4,5,6,7,8, 9, 9})).exec(values);
//            } catch (BasicException ex) {
//                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//    }
    public void insertTableOrderLines(String tblOrderId, String tokenId, String productId, double qty, double price, int bLink, int preStatus, int active, Date orderDate) {

        Object[] values = new Object[]{tblOrderId, tokenId, productId, qty, price, bLink, preStatus, active, orderDate};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.INT, Datas.INT, Datas.INT, Datas.TIMESTAMP};
        try {
            new PreparedSentence(s, "INSERT INTO TBL_ORDERITEM (ORDERITEM_ID,TOKEN_ID,ITEM_ID,QUANTITY,PRICE, BLINK ,PREPARESTATUS , ACTIVE,ORDERTIME) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void updateOrderItem(final String id, final String productId, String tbl_orderId) throws BasicException {
        System.out.println(tbl_orderId + "before updating tbl_order");
        Object[] values = new Object[]{id, productId, tbl_orderId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE TBL_ORDERITEM SET PREPARESTATUS = 3 WHERE TOKEN_ID = ? and ITEM_ID=? and ORDERITEM_ID=?", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
    }

    public final void updateOrder(final String id) throws BasicException {

        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE TBL_ORDER SET PREPARESTATUS = 3 WHERE TICKETID = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    //newly added

    public final void updateOrderBack(final String id) throws BasicException {

        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE TBL_ORDER SET PREPARESTATUS = 0 WHERE TICKETID = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }

    public int getTokenId() throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT MAX(TOKEN_ID) FROM tbl_token", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getTokenBasedOnTicket(String ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT TOKEN_ID FROM tbl_token WHERE TICKETID='" + ticketId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getTicketIdCount(String ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM tbl_token WHERE TICKETID='" + ticketId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public String getTokenId(String tableid) throws BasicException {
        if (tableid == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT TOKEN_ID FROM tbl_order WHERE MST_TABLE_ID=? AND (PAIDSTATUS=0 or PAIDSTATUS IS NULL)", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(tableid);
            return record == null ? "" : record[0].toString();
        }
    }

    public final void updateOrderSharedTicket(final String id, final RetailTicketInfo ticket) throws BasicException {

        Object[] values = new Object[]{id, ticket};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s, "UPDATE SHAREDTICKETS SET CONTENT = ? WHERE ID = ?", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);
    }

    public String getRoleByUser(String id) throws BasicException {

        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "select name from roles where id=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);

        return record == null ? "" : (String) record[0];
    }

    public String getFloorId(String tableid) throws BasicException {
        if (tableid == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT FLOOR FROM PLACES WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(tableid);
            return record == null ? "" : record[0].toString();
        }
    }

    public final List<kotInfo> getRetailKot(String ticketId, String productionArea) throws BasicException {

        return (List<kotInfo>) new StaticSentence(s, "SELECT k.ID,k.TICKET, k.PRODUCTID,k.ISPRINTED,k.QTY,k.KOTID,p.NAME,k.INSTRUCTION FROM KOT k, PRODUCTS p where p.ID=k.PRODUCTID and k.TICKET = '" + ticketId + "' and k.ISPRINTED='N' AND P.PRODUCTIONAREATYPE='" + productionArea + "' ORDER BY ID", null, new SerializerReadClass(kotInfo.class)).list();
    }

    public final List<ProductionPrinterInfo> getPrinterInfo(String session) throws BasicException {

        return (List<ProductionPrinterInfo>) new StaticSentence(s, "SELECT DISTINCT SM.PRODUCTIONAREATYPE,SM.PRODUCTIONAREA,PC.PATH,PA.ISKOT FROM SECTIONMAPPING SM, PRODUCTIONAREA PA, PRINTERCONFIG PC "
                + "WHERE SM.PRODUCTIONAREA=PA.ID AND PA.RESTUARANTPRINTER=PC.ID AND  SM.SECTION= '" + session + "'", null, new SerializerReadClass(ProductionPrinterInfo.class)).list();
    }

//    public final List<SharedSplitTicketInfo> getRetailSharedSplitTicket(String id) throws BasicException {
//        System.out.println("id in getRetailSharedSplitTicket"+id);
//       if (id == null) {
//            return null;
//        } else {
//            return (List<SharedSplitTicketInfo>) new StaticSentence(s
//                    , "SELECT ID,NAME,CONTENT FROM SHAREDTICKETS WHERE ID = '"+id+"'"
//                    , null
//                , new SerializerReadClass(SharedSplitTicketInfo.class)).list();
//        }
//    }
    public final List<SharedSplitTicketInfo> getRetailSharedSplitTicket(String id) throws BasicException {

        return (List<SharedSplitTicketInfo>) new StaticSentence(s, "SELECT ID, NAME,SPLITID,ISPRINTED,ISMODIFIED,UPDATED,ISKDS FROM SHAREDTICKETS WHERE ID='" + id + "'", null, new SerializerReadClass(SharedSplitTicketInfo.class)).list();
    }

    public final void updateSharedSplitTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        // System.out.println("updateSharedSplitTicket"+ticket.getSplitSharedId());      
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified(), 0};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT};
        new PreparedSentence(s, "UPDATE SHAREDTICKETS SET NAME=?,CONTENT =? , ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=? WHERE ID = ? AND SPLITID=?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 6, 0, 3})).exec(values);
    }

    public final void updateTakeawaySplitTicket(final String id, final RetailTicketInfo ticket) throws BasicException {
        // System.out.println("updateSharedSplitTicket"+ticket.getSplitSharedId());      
        Object[] values = new Object[]{id, ticket.getName(), ticket, ticket.getSplitSharedId(), ticket.isPrinted(), ticket.isListModified(), 0};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.INT};
        new PreparedSentence(s, "UPDATE TAKEAWAYTICKETS SET NAME=?,CONTENT =? , ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW(),ISKDS=? WHERE ID = ? AND SPLITID=?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 6, 0, 3})).exec(values);
    }

    public final RetailTicketInfo getRetailSharedTicketSplit(String Id, String splitid) throws BasicException {

        if (Id == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitid + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }

//   public String getRetailSharedTicketName(String id) throws BasicException {
//       if (id == null) {
//            return null;
//        } else { 
//      Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM SHAREDTICKETS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
//            return record == null ? "" : record[0].toString();
//      }
//    }
//   public final SharedTicketNameInfo getRetailSharedTicket1(String Id) throws BasicException {
//
//        if (Id == null) {
//            return null;
//        } else {
//            Object[]record = (Object[]) new StaticSentence(s
//                    , "SELECT ID,NAME,CONTENT FROM SHAREDTICKETS WHERE ID = ?"
//                    , SerializerWriteString.INSTANCE
//                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
//            return record == null ? null : (SharedTicketNameInfo) record[0];
//        }
//    }
    public final void deleteSharedSplitTicket(final String id, String splitId) throws BasicException {

        new StaticSentence(s, "DELETE FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE).exec(id);
    }

    public final void deleteTakeawaySplitTicket(final String id, String splitId) throws BasicException {

        new StaticSentence(s, "DELETE FROM TAKEAWAYTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE).exec(id);
    }

//  public final void updateSplitOrderItem(final RetailTicketInfo ticket, final String productId,String tbl_orderId) throws BasicException {
//         System.out.println(tbl_orderId+"before updating tbl_order") ;  
//        Object[] values = new Object[] {ticket.getId(),ticket.getParentId(),productId,tbl_orderId};
//        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING};
//        new PreparedSentence(s
//                , "UPDATE TBL_ORDERITEM SET TOKEN_ID = ? WHERE TOKEN_ID = ? and ITEM_ID=? and ORDERITEM_ID=?"
//                , new SerializerWriteBasicExt(datas, new int[] {0,1,2,3})).exec(values);
//    }
    //this method to delete the line which is sent to kot
    public final void deleteFromTbl_orderItem(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM TBL_ORDERITEM WHERE ORDERITEM_ID = ? ", SerializerWriteString.INSTANCE).exec(id);
    }

    //this method to update the line in tbl_orderitem which is sent to kot
    public final void updateTbl_orderItem(final double qty, final String id) throws BasicException {

        Object[] values = new Object[]{id, qty};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE};
        new PreparedSentence(s, "UPDATE TBL_ORDERITEM SET QUANTITY = ? WHERE ORDERITEM_ID = ?", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);
    }

    //below methods are called only in case of split bill
    public final void deleteTbl_orderItem(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM TBL_ORDERITEM WHERE TOKEN_ID = ? ", SerializerWriteString.INSTANCE).exec(id);
    }

    public final void deleteTbl_order(final String id) throws BasicException {

        new StaticSentence(s, "DELETE FROM TBL_ORDER WHERE TICKETID = ? ", SerializerWriteString.INSTANCE).exec(id);
    }

    public final void deleteTokenTicketId(String ticketId) throws BasicException {
        new StaticSentence(s, "DELETE FROM TBL_TOKEN WHERE TICKETID = ? ", SerializerWriteString.INSTANCE).exec(ticketId);

    }

    public final synchronized void saveToken(final RetailTicketInfo ticket, final int prepStatus)
            throws BasicException {
        Transaction t = new Transaction(s) {
            @Override
            protected Object transact() throws BasicException {
                new PreparedSentence(s, "INSERT INTO tbl_token (TOKEN_ID,USER_ID,CREATEDDATE,MODIFIEDDATE,ACTIVE,TICKETID) VALUES (?, ?, NOW(), NOW(), ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setInt(1, ticket.getTicketId());
                        setString(2, ticket.getUser().getId());
                        //  setTimestamp(3, ticket.getDate());
                        //     setTimestamp(4, ticket.getDate());
                        setInt(5, 1);
                        setString(6, String.valueOf(ticket.getOrderId()));

                    }
                });
                new PreparedSentence(s, "INSERT INTO TBL_ORDER (TOKEN_ID,MST_TABLE_ID,USER_ID,AMOUNT,TAX,TOTAL,PREPARESTATUS,ACTIVE,TICKETID,ORDERTIME,DELIVERYTIME) VALUES (?, ?, ?, ?, ?, ?, ?,?,?, NOW(),NOW())", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setInt(1, ticket.getTicketId());
                        setString(2, ticket.getPlaceId());
                        setString(3, ticket.getUser().getId());
                        setDouble(4, 0.0);
                        setDouble(5, 0.0);
                        setDouble(6, 0.0);
                        setInt(7, prepStatus);
                        setInt(8, 1);
                        setString(9, String.valueOf(ticket.getOrderId()));
                        //  setTimestamp(10, ticket.getDate());
                        //   setTimestamp(11, ticket.getDate());
                    }
                });
                return null;
            }
        };

        t.execute();
    }

    //select kot no from ticketlines table
    public String getkotNo() throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT MAX(KOTID) FROM TICKETLINES ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{})).find();
        return record == null ? "" : (String) record[0];
    }

    public final Integer getNextKotIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "KOTNUM").find();
    }

    public int getSharedTicketCount() throws BasicException {
        Object[] record = null;
        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM SHAREDTICKETS", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);
    }
    //Fetching categories to calculate product based discount

    public List<RoleInfo> getCategoriesList() throws BasicException {

        return (List<RoleInfo>) new StaticSentence(s, "SELECT ID,NAME FROM CATEGORIES WHERE PARENTID IS NULL ", null, new SerializerReadClass(RoleInfo.class)).list();
    }

    public List<StaffInfo> getActiveStaffList() throws BasicException {

        return (List<StaffInfo>) new StaticSentence(s, "SELECT ID,SEARCHKEY,NAME,PHONENO,ISACTIVE FROM STAFFS WHERE ISACTIVE='Y' ", null, new SerializerReadClass(StaffInfo.class)).list();
    }

    public String getUpdatedTime(String tableId, String splitId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT UPDATED FROM SHAREDTICKETS WHERE ID='" + tableId + "' AND SPLITID='" + splitId + "'    ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        return record == null ? "" : (String) record[0];



    }

    public List<RoleUserInfo> getUsers() throws BasicException {

        return (List<RoleUserInfo>) new StaticSentence(s, "SELECT ID, NAME FROM PEOPLE ", null, new SerializerReadClass(RoleUserInfo.class)).list();
    }

    public String getProductAvailablityStatus(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT ISAVAILABLE FROM PRODUCTS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "" : record[0].toString();

    }

    public int getKdsUpdateStatus(String tableId, String splitId) throws BasicException {
        Object[] record = null;
        record = (Object[]) new StaticSentence(s, "SELECT ISKDS FROM SHAREDTICKETS WHERE ID='" + tableId + "' AND SPLITID='" + splitId + "'    ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);
    }

    public String getServedName() throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM KODSTATUSMASTER WHERE ISLASTSTATUS='Y' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        return record == null ? "" : record[0].toString();

    }

    //New KDS functions Added on 7-03-17
    public void insertServedTransaction(RetailTicketInfo m_oTicket, String txstatus, String uniq_tableid) {
        String uniqtableid = uniq_tableid;

        int begin = 0;
        System.out.println("uniq table id" + uniq_tableid);
        for (RetailTicketLineInfo l : m_oTicket.getLines()) {

            begin++;
            if (l.getTbl_orderId().equals(uniqtableid)) {
                System.out.println("id" + begin + " check from insertserved txn" + uniqtableid);


                Object[] values = new Object[]{UUID.randomUUID().toString(), m_oTicket.getOrderId(), m_oTicket.getPlaceId(), l.getTbl_orderId(), txstatus, l.getKotid(), l.getDuplicateProductName(), l.getMultiply(), l.getPreparationTime(), l.getKdsPrepareStatus(), l.getInstruction(), l.getAddonId(), l.getPrimaryAddon(), l.getProductionAreaType(), l.getStation(), l.getPreparationStatus(), l.getServedBy(), l.getServedTime(), 0};
                Datas[] datas = new Datas[]{Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING, Datas.TIMESTAMP, Datas.INT};


                try {
                    new PreparedSentence(s, "INSERT INTO SERVEDTRANSACTION (ID,ORDERNUM,TABLEID,ORDERITEM_ID,TXSTATUS,KOTID,PRODUCTNAME,MULTIPLY,PREPARATIONTIME,KOTDATE,KDSPREPARESTATUS,INSTRUCTION,ADDONID,PRIMARYADDON,PRODUCTIONAREATYPE,STATION,PREPARATIONSTATUS,SERVEDBY,SERVEDTIME,UPDATED,ISKDS) VALUES (?,?,?,?,'ADD',?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,NOW(),?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18})).exec(values);

                } catch (BasicException ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }//end catch
            }//end if
        }//end for

    }//end fn.

    public int getOrderNumberForMove(String id) throws BasicException {
        Object[] record = null;
        record = (Object[]) new StaticSentence(s, "SELECT ORDERNUM FROM SERVEDTRANSACTION WHERE TABLEID = '" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);
    }

    public String getServedTxstatusBeforeMove(String id) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT TXSTATUS FROM SERVEDTRANSACTION WHERE TABLEID = '" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        return record == null ? "" : record[0].toString();

    }

    public void setmoveTxstatus(String TxStatus) {
        this.moveTxstatus = TxStatus;
    }

    public final void updateServedTransactionMoveOrderId(int ordernum, String moveid) {
        System.out.println("update in MoveSorderid" + ordernum + "moveid" + moveid);
        Object[] values = new Object[]{moveid, ordernum};

        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT};
        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET UPDATED=NOW(),TABLEID = ? WHERE ORDERNUM = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void updateServedTransactionMoveAsModify(RetailTicketInfo ticketclip, final String moveid, final String oldid, final String mod) throws BasicException {
        System.out.println("update in MoveServed Transaction--tableId is " + oldid + "moveid" + moveid);

        int ordernum = getOrderNumberForMove(oldid);
        System.out.println("ordernumber" + ordernum);
        updateServedTransactionMoveOrderId(ordernum, moveid);



        Object[] values = new Object[]{moveid, mod};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET TXSTATUS = ?,UPDATED=NOW() WHERE TABLEID = ? AND TxSTATUS= 'ADD' ", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        //}//end if
    }

    public void updateServedTransactionMinus(RetailTicketInfo m_oTicket, final String id, double qty) {

        for (RetailTicketLineInfo newline : m_oTicket.getLines()) {
            if (id == newline.getTbl_orderId()) {
                System.out.println("UPDATING TABLE for minus : " + m_oTicket.getPlaceId() + newline.getTbl_orderId() + "..." + newline.getMultiply() + "date:::" + newline.getKotdate());
                Object[] values = new Object[]{qty, newline.getKotdate(), newline.getTbl_orderId()};
                Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.TIMESTAMP, Datas.STRING};
                try {
                    new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET MULTIPLY = ?,KOTDATE = ?,TXSTATUS = 'MODIFY',UPDATED=NOW() WHERE ORDERITEM_ID=? ", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void updateServedTransactionMinusAddOn(RetailTicketInfo m_oTicket, final String id, double qty, final String lineId) {

        System.out.println("INSIDE MINUS ADDON" + id + " QTY : " + qty + " LINE ID : " + lineId);
        Object[] values = new Object[]{qty, id, lineId};
        Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING, Datas.STRING};

        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET MULTIPLY = ?,TXSTATUS = 'CANCEL', UPDATED=NOW() WHERE ADDONID=?  AND PRIMARYADDON=0 ", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateServedTransactionMinusAddOnModify(RetailTicketInfo m_oTicket, final String id, double qty, final String lineId) {

        System.out.println("INSIDE MINUS ADDON" + id + " QTY : " + qty + " LINE ID : " + lineId);
        Object[] values = new Object[]{qty, id, lineId};
        Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING, Datas.STRING};

        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET MULTIPLY = ?,TXSTATUS = 'MODIFY', UPDATED=NOW() WHERE ADDONID=?  AND PRIMARYADDON=0 AND ORDERITEM_ID=?", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateServedTransactionCancel(RetailTicketInfo m_oTicket, final String id) {

        for (RetailTicketLineInfo newline : m_oTicket.getLines()) {
            if (id == newline.getTbl_orderId()) {
                System.out.println("UPDATING TABLE for cancel X : " + m_oTicket.getPlaceId() + "   order ID : " + newline.getTbl_orderId() + "..." + newline.getMultiply() + "date:::" + newline.getKotdate());

                Object[] values = new Object[]{newline.getKotdate(), newline.getTbl_orderId()};
                Datas[] datas = new Datas[]{Datas.TIMESTAMP, Datas.STRING};
                try {
                    new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET KOTDATE = ?,TXSTATUS = 'CANCEL',UPDATED=NOW() WHERE ORDERITEM_ID=? ", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public void updateServedTransactionCancelAddOn(RetailTicketInfo m_oTicket, final String id, String lineId) {
        Object[] values = new Object[]{id, lineId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET TXSTATUS = 'CANCEL',UPDATED=NOW() WHERE ADDONID=? AND PRIMARYADDON=0 AND ORDERITEM_ID=?  ", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateServedTransactionTime(RetailTicketInfo m_oTicket, final String id, String role) {
        String rolename = role;

        for (RetailTicketLineInfo newline : m_oTicket.getLines()) {
            if (id == newline.getTbl_orderId()) {
                System.out.println("UPDATING TABLE for ServedTime : " + m_oTicket.getPlaceId() + newline.getTbl_orderId() + "..." + newline.getMultiply() + "date:::" + newline.getServedBy() + "Status: " + newline.getKdsPrepareStatus());

                Object[] values = new Object[]{rolename, newline.getKdsPrepareStatus(), newline.getServedTime(), newline.getTbl_orderId()};
                Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING};
                try {
                    new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET UPDATED= NOW(),SERVEDBY = ?,KDSPREPARESTATUS = ?,SERVEDTIME = ? WHERE ORDERITEM_ID=? ", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public void updateServedTransactionSplit(RetailTicketInfo m_oTicket, final String placeid, final String oldorderitemid, final String neworderitemid, int oldorderno, int neworderno) {

        for (RetailTicketLineInfo newline : m_oTicket.getLines()) {
            if (oldorderitemid == newline.getTbl_orderId()) {
                System.out.println("UPDATING TABLE for Split : " + m_oTicket.getPlaceId() + "orderitemid" + newline.getTbl_orderId() + "..." + newline.getMultiply() + "date:::" + newline.getKotdate());

                Object[] values = new Object[]{neworderno, neworderitemid, placeid, oldorderno, oldorderitemid};
                Datas[] datas = new Datas[]{Datas.INT, Datas.STRING, Datas.STRING, Datas.INT, Datas.STRING};
                try {
                    new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET ORDERNUM = ?,ORDERITEM_ID = ?,UPDATED=NOW(),TXSTATUS = 'MODIFY'  WHERE TABLEID=? AND ORDERNUM = ? AND ORDERITEM_ID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4})).exec(values);
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void updateServedTransactionCancelKotBill(RetailTicketInfo m_oTicket, final String orderId, final int ordernum) {


        Object[] values = new Object[]{ordernum, orderId};
        Datas[] datas = new Datas[]{Datas.INT, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE SERVEDTRANSACTION SET TXSTATUS = 'CANCEL',UPDATED=NOW() WHERE ORDERNUM = ? AND TABLEID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ServedTransactionInfo> getUpdateFromServedTransaction(String id) throws BasicException {
        return (List<ServedTransactionInfo>) new StaticSentence(s, "SELECT ID, SERVEDBY, SERVEDTIME , PREPARATIONSTATUS FROM SERVEDTRANSACTION WHERE ORDERITEM_ID = '" + id + "'", null, new SerializerReadClass(ServedTransactionInfo.class)).list();
    }

    public String getUserName(String id) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM PEOPLE WHERE ID= '" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        return record == null ? "" : record[0].toString();

    }

    public void updateTiltSession(String cashTallySessionId, String outtime, Map tiltMap,String remks) {
        Date endDate = DateFormats.StringToDateTime(outtime);
        try {
            System.out.println("UPDATING TABLE for " + cashTallySessionId);
            Object[] values = new Object[]{endDate, cashTallySessionId};
            Datas[] datas = new Datas[]{Datas.TIMESTAMP, Datas.STRING};
            new PreparedSentence(s, "UPDATE TILTSESSION SET LOGOUT=? WHERE ID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        updateTiltCloseBalance(cashTallySessionId, tiltMap, "N",remks);


    }

    public void insertTiltSessionBalance(String userid, String intime, String outtime, Map tiltMap, String tiltNme) {
        String tiltName = tiltNme;
        System.out.println("userid" + userid + "intime" + intime + "outime" + outtime + tiltName);
        Date startDate = DateFormats.StringToDateTime(intime);
        Date endDate = DateFormats.StringToDateTime(outtime);
        System.out.println("userid" + userid + "StartDate" + startDate);
        System.out.println("userid" + userid + "EndDate" + endDate);
        System.out.println("tiltName" + tiltName);
        String isOpen = " ";

    }

    public void insertTiltSession(String userid, String intime, String outtime, Map tiltMap, String tiltNme,String remks) {
        String tiltName = tiltNme;
        System.out.println("userid" + userid + "intime" + intime + "outime" + outtime + tiltName);
        Date startDate = DateFormats.StringToDateTime(intime);
        Date endDate = DateFormats.StringToDateTime(outtime);
        System.out.println("userid" + userid + "StartDate" + startDate);
        System.out.println("userid" + userid + "EndDate" + endDate);
        System.out.println("tiltName" + tiltName);
        String isOpen = " ";

        if (endDate == null) {
            isOpen = "Y";
            //endDate=DateFormat.getDateTimeInstance().format(endDate);
            AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
            aconfig.load();
            String PosNum = aconfig.getProperty("machine.PosNo");
            // String tilt = "1";
            cashTallyId = UUID.randomUUID().toString();
            System.out.println("userid" + userid + "StartDate" + startDate + "EndDate" + PosNum + "tiltName" + tiltName);
            Object[] values = new Object[]{cashTallyId, userid, startDate, endDate, tiltName};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING};
            try {
                new PreparedSentence(s, "INSERT INTO TILTSESSION (ID,USERID,LOGIN,LOGOUT,TILT) VALUES (?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4})).exec(values);
            } catch (BasicException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }//end catch
            insertTiltBalance(cashTallyId, tiltMap, "Y", tiltName,remks);
            
           // updateTiltOpenBalance(cashTallyId, tiltMap, "Y", tiltName);
        }//endatenull
        if (startDate == null) {
            isOpen = "N";
            System.out.println("cashTallyId" + cashTallyId + "outtime" + outtime);
            updateTiltSession(cashTallyId, outtime, tiltMap,remks);
        }
    }//end fn.

    public void updateTiltCloseBalance(String cashTallyId, Map tiltMap, String isOpen,String remks) {
        System.out.println("update Tilt Close Bal detail");
        Set mapSet = (Set) tiltMap.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            Double value = (Double) mapEntry.getValue();
            System.out.println(value);
            Object[] values = new Object[]{cashTallyId, keyValue, isOpen, value,remks};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING};
            try {
                new PreparedSentence(s, "UPDATE TILTBALANCE SET CLOSINGAMOUNT=?,ISOPEN=?,REMARKS=? WHERE SESSIONID = ? AND PAYMENTTYPE=?", new SerializerWriteBasicExt(datas, new int[]{3, 2, 4, 0, 1})).exec(values);
            } catch (BasicException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }//end catch
        }//end while


    }//end fn.
    
    
    
     public void updateTiltOpenBalance(String cashTallyId, Map tiltMap, String isOpen,String tiltName) {
        System.out.println("update TiltOpen Bal detail");
        Set mapSet = (Set) tiltMap.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            Double value = (Double) mapEntry.getValue();
            System.out.println(value);
            Object[] values = new Object[]{cashTallyId, keyValue, isOpen, value};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE};
            try {
                new PreparedSentence(s, "UPDATE TILTBALANCE SET OPENINGAMOUNT=?,ISOPEN=? WHERE SESSIONID = ? AND PAYMENTTYPE=?", new SerializerWriteBasicExt(datas, new int[]{3, 2, 0, 1})).exec(values);
            } catch (BasicException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }//end catch
        }//end while


    }//end fn.

    public void insertTiltBalance(String cashTallyId, Map tiltMap, String isOpen, String tiltName,String remks) {
        AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
        aconfig.load();
        String PosNum2 = aconfig.getProperty("machine.PosNo");
        System.out.println("insertcash txn detail" + PosNum2+remks);
        Set mapSet = (Set) tiltMap.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            Double value = (Double) mapEntry.getValue();
            System.out.println(value);
            Object[] values = new Object[]{UUID.randomUUID().toString(), cashTallyId, keyValue, isOpen, value,tiltName,remks};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE,Datas.STRING,Datas.STRING};

            try {
                new PreparedSentence(s, "INSERT INTO TILTBALANCE (ID,SESSIONID,PAYMENTTYPE,ISOPEN,OPENINGAMOUNT,TILT,REMARKS) VALUES (?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5 ,6})).exec(values);
            } catch (BasicException ex) { 
                ex.printStackTrace();
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }//end catch
        }//end while
    }//end fn.

    public void insertSetTiltBalance(String userId,Map tiltMap,String tiltNme) {
        String tiltName = tiltNme;
        String isOpen = " ";
        Set mapSet = (Set) tiltMap.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            Double value = (Double) mapEntry.getValue();
            System.out.println(value);
            Object[] values = new Object[]{UUID.randomUUID().toString(), cashTallyId, keyValue, isOpen, value, value, tiltName};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE,Datas.DOUBLE, Datas.STRING};

            try {
                new PreparedSentence(s, "INSERT INTO TILTBALANCE (ID,SESSIONID,PAYMENTTYPE,ISOPEN,OPENINGAMOUNT,CLOSINGAMOUNT,TILT,REMARKS) VALUES (?, ?, ?, ?, ?, ?,?,'ENTER')", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);
            } catch (BasicException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
            }//end catch
        }//end while
    }

    public void insertTiltTransaction(String cashTallyId, String pType, double amount, String tiltName) {
        AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
        aconfig.load();
        String PosNum1 = aconfig.getProperty("machine.PosNo");
        System.out.println("insertcash PAymentDetail" + PosNum1);
        Object[] values = new Object[]{UUID.randomUUID().toString(), cashTallyId, pType, amount, tiltName};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO TILTTRANSACTION (ID,SESSIONID,PAYMENTTYPE,AMOUNT,TILT) VALUES (?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4})).exec(values);
        } catch (BasicException ex) {
            ex.printStackTrace();
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }//end catch

    }

    public String getTiltRolebyName(String id) throws BasicException {
        System.out.println("getTiltRoleByName");
        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM ROLES WHERE ID='" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        return record == null ? "" : record[0].toString();

    }

    public final List<TiltTxnInfo> getTiltTxnInfo() throws BasicException {
        System.out.println("TiltTransaction fetch");
        return (List<TiltTxnInfo>) new StaticSentence(s, "SELECT ID,SESSIONID,PAYMENTTYPE,AMOUNT,TILT FROM TILTTRANSACTION ", null, new SerializerReadClass(TiltTxnInfo.class)).list();
    }

    public final List<TiltBalanceInfo> getTiltBalanceInfo() throws BasicException {
        System.out.println("TiltBalance fetch");
        return (List<TiltBalanceInfo>) new StaticSentence(s, "SELECT ID,SESSIONID,PAYMENTTYPE,ISOPEN,OPENINGAMOUNT,CLOSINGAMOUNT,TILT,REMARKS FROM TILTBALANCE ", null, new SerializerReadClass(TiltBalanceInfo.class)).list();
    }

    public final List<TiltSessionInfo> getTiltSessionInfo() throws BasicException {
        System.out.println("TiltSession fetch");
        return (List<TiltSessionInfo>) new StaticSentence(s, "SELECT ID,USERID,LOGIN,LOGOUT,TILT FROM TILTSESSION ", null, new SerializerReadClass(TiltSessionInfo.class)).list();
    }

    public void updateSetTilt(String tilt) {
        System.out.println("update SetTilt");

        Object[] values = new Object[]{tilt};
        Datas[] datas = new Datas[]{Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE TILT SET ACCESS='Y' WHERE TILT=?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            ex.printStackTrace();
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }//end catch

    }

    public void updateUnSetTilt(String tilt) {
        System.out.println("update UnsetTilt");

        Object[] values = new Object[]{tilt};
        Datas[] datas = new Datas[]{Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE TILT SET ACCESS='N' WHERE TILT=?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            ex.printStackTrace();
            Logger.getLogger(DataLogicReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }//end catch

    }
}
