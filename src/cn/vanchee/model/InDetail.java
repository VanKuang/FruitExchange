package cn.vanchee.model;

import cn.vanchee.service.FruitService;
import cn.vanchee.service.OwnerService;
import cn.vanchee.util.MyFactory;

import java.util.List;

/**
 * @author vanchee
 * @date 13-2-1
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class InDetail {

    private Integer id; //货号
    private Integer oid;
    private Integer fid;
    private Integer num;
    private Double price;
    private String createAt;
    private Integer sale; //销售数量
    private Integer color;
    private Integer censored; //审核状态
    private Integer uid;

    transient double money;
    transient String ownerName;
    transient String fruitName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public double getMoney() {
        return this.price * this.num;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getCensored() {
        return censored;
    }

    public void setCensored(Integer censored) {
        this.censored = censored;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getOwnerName() {
        if (ownerName == null && oid != -1) {
            OwnerService ownerService = MyFactory.getOwnerService();
            ownerName = ownerService.getOwnerName(oid);
        }
        return ownerName;
    }

    public String getFruitName() {
        if (fruitName == null && fid != -1) {
            FruitService fruitService = MyFactory.getFruitService();
            fruitName = fruitService.getFruitName(oid);
        }
        return fruitName;
    }

    public Integer getRemain() {
        return num - sale;
    }

    public double getPaidMoney() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryMyPaidDetail(-1, id, -1, -1, -1, null, null, -1);
        for (PaidDetail myPaid : result) {
            money += myPaid.getMoney();
        }
        return money;
    }

    @Override
    public String toString() {
        return "in detail--[owner:" + getOwnerName()
                + ",fruit:" + getFruitName()
                + ",price:" + this.price
                + ",num:" + this.num
                + ",sale:" + this.sale
                + ",color:" + this.color
                + ",censored:" + this.censored
                + ",uid:" + uid
                + "]";
    }
}
