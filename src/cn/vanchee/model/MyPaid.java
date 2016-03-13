package cn.vanchee.model;

import cn.vanchee.util.MyFactory;

import java.io.Serializable;

/**
 * @author vanchee
 * @date 13-3-12
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class MyPaid implements Serializable, Comparable<MyPaid> {

    private int id;
    private int iid;
    private double money;
    private long date;
    private int color;
    private int censored;
    private int uid;

    transient int ownerId;
    transient int fid;
    transient String fruitName;
    transient String ownerName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
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

    public int getFid() {
        if (fid == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            fid = inDetail != null ? inDetail.getFruit() : 0;
        }
        return fid;
    }

    public String getFruitName() {
        if (fruitName == null && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            fruitName = inDetail != null ? inDetail.getFruitName() : "";
        }
        return fruitName;
    }

    @Override
    public int compareTo(MyPaid o) {
        return this.getDate() > o.getDate() ? 0 : 1;
    }

    @Override
    public String toString() {
        return "my detail--[owner:" + this.getOwnerName()
                + ",fruit:" + getFruitName()
                + ",money:" + this.money
                + ",censored:" + this.censored
                + ",uid:" + uid
                + "]";
    }
}
