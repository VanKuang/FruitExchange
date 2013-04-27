package cn.vanchee.model;

import cn.vanchee.service.FruitService;
import cn.vanchee.service.OwnerService;
import cn.vanchee.util.MyFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-1
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class InDetail implements Serializable, Comparable<InDetail> {

    private int id; //货号
    private int owner;
    private int fruit;
    private int num;
    private double price;
    private long date;
    private int sale; //销售数量
    private int color;
    private int censored; //审核状态
    private int uid;

    transient double money;
    transient String ownerName;
    transient String fruitName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getMoney() {
        return this.price * this.num;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
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

    public String getOwnerName() {
        if (ownerName == null && owner != -1) {
            OwnerService ownerService = MyFactory.getOwnerService();
            ownerName = ownerService.getOwnerName(owner);
        }
        return ownerName;
    }

    public String getFruitName() {
        if (fruitName == null && fruit != -1) {
            FruitService fruitService = MyFactory.getFruitService();
            fruitName = fruitService.getFruitName(fruit);
        }
        return fruitName;
    }

    public int getRemain() {
        return num - sale;
    }

    public double getPaidMoney() {
        double money = 0;
        List<MyPaid> result = MyFactory.getMyPaidService().queryMyPaid(-1, id, -1, -1, -1, -1, -1, -1);
        for (MyPaid myPaid : result) {
            money += myPaid.getMoney();
        }
        return money;
    }

    @Override
    public int compareTo(InDetail o) {
        return this.getDate() > o.getDate() ? 0 : 1;
    }

    @Override
    public String toString() {
        return "in detail--owner:" + getOwnerName() + ",fruit:" + getFruitName()
                + ",price" + this.price + ",num:" + this.num + ",sale:" + this.sale + ",color:" + this.color
                + ",censored:" + this.censored + ",uid:" + uid;
    }
}
