package cn.vanchee.model;

import cn.vanchee.service.ConsumerService;
import cn.vanchee.service.FruitService;
import cn.vanchee.service.OwnerService;
import cn.vanchee.util.MyFactory;

import java.io.Serializable;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class OutDetailHistory implements Comparable<OutDetailHistory>, Serializable {

    private int id;
    private long date;
    private long backupDate;
    private int owner;
    private int consumer;
    private int fruit;
    private double price;
    private int num;
    private int color;

    /**
     * 0:haven't paid. original status
     * 1:paid but not enough
     * 2:paid and enough
     * @see cn.vanchee.util.Constants
     */
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getConsumer() {
        return consumer;
    }

    public void setConsumer(int consumer) {
        this.consumer = consumer;
    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(long backupDate) {
        this.backupDate = backupDate;
    }

    @Override
    public int compareTo(OutDetailHistory o) {
        return this.getDate() > o.getDate() ? this.getDate() == o.getDate() ? 0 : -1 : 0;
    }
}
