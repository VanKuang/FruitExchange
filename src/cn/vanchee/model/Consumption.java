package cn.vanchee.model;

import java.io.Serializable;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class Consumption implements Serializable, Comparable<Consumption> {

    private int id;
    private double money;
    private long date;
    private String desc;
    private int color;
    private int censored;
    private int uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public int compareTo(Consumption o) {
        return this.getDate() > o.getDate() ? this.getDate() == o.getDate() ? 0 : -1 : 1;
    }

    @Override
    public String toString() {
        return "consumption--money:" + this.money
                + ",desc:" + this.desc
                + ",color:" + this.color
                + ",censored:" + this.censored
                + ",uid:" + this.uid;
    }
}
