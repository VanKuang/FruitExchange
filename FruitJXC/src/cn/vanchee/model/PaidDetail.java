package cn.vanchee.model;

import cn.vanchee.util.MyFactory;

import java.io.Serializable;

/**
 * @author vanchee
 * @date 13-2-1
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class PaidDetail implements Serializable, Comparable<PaidDetail> {

    private int id;
    private int oid; //销售ID
    private double money;
    private double discount; //折扣
    private long date;
    private int color;
    private int censored;
    private int uid;

    transient int iid;
    transient int ownerId;
    transient int cid;
    transient int fid;
    transient String ownerName;
    transient String fruitName;
    transient String consumerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getCensored() {
        return censored;
    }

    public void setCensored(int censored) {
        this.censored = censored;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIid() {
        if (iid == 0 && oid != -1) {
            OutDetail outDetail = MyFactory.getOutDetailService().getOutDetail(oid);
            iid = outDetail != null ? outDetail.getIid() : 0;
        }
        return iid;
    }

    public String getConsumerName() {
        if (consumerName == null && oid != -1) {
            OutDetail outDetail = MyFactory.getOutDetailService().getOutDetail(oid);
            consumerName = outDetail != null ? outDetail.getConsumerName() : "";
        }
        return consumerName;
    }

    public int getCid() {
        if (cid == 0 && oid != -1) {
            OutDetail outDetail = MyFactory.getOutDetailService().getOutDetail(oid);
            cid = outDetail != null ? outDetail.getCid() : 0;
        }
        return cid;
    }

    public int getFid() {
        if (fid == 0 && oid != -1) {
            OutDetail outDetail = MyFactory.getOutDetailService().getOutDetail(oid);
            fid = outDetail != null ? outDetail.getFruitId() : 0;
        }
        return fid;
    }

    public String getFruitName() {
        if (fruitName == null && oid != -1) {
            OutDetail outDetail = MyFactory.getOutDetailService().getOutDetail(oid);
            fruitName = outDetail != null ? outDetail.getFruitName() : "";
        }
        return fruitName;
    }

    public int getOwnerId() {
        if (ownerId == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            ownerId = inDetail != null ? inDetail.getOwner() : 0;
        }
        return ownerId;
    }

    public String getOwnerName() {
        if (ownerName == null && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            ownerName = inDetail != null ? inDetail.getOwnerName() : "";
        }
        return ownerName;
    }

    @Override
    public int compareTo(PaidDetail o) {
        return this.getDate() > o.getDate() ? this.getDate() == o.getDate() ? 0 : -1 : 1;
    }

    @Override
    public String toString() {
        return "paid detail--[owner:" + getOwnerName()
                + ",consumer:" + getConsumerName()
                + ",money:" + this.money
                + ",discount:" + this.discount
                + ",censored:" + this.censored
                + ",uid:" + uid
                + "]";
    }
}
