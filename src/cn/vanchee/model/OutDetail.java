package cn.vanchee.model;

import cn.vanchee.service.ConsumerService;
import cn.vanchee.service.FruitService;
import cn.vanchee.service.OwnerService;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class OutDetail implements Comparable<OutDetail>, Serializable {

    private int id;
    private int iid; //进货号
    private long date;
    private int cid;
    private double price;
    private int num;
    private int color;
    private int censored;
    private int uid;

    /**
     * 0:haven't paid. original status
     * 1:paid but not enough
     * 2:paid and enough
     *
     * @see cn.vanchee.util.Constants
     */
    private int status;

    transient double money; //应还款
    transient double paidMoney; //实际还款
    transient int ownerId;
    transient int fruitId;
    transient String ownerName;
    transient String consumerName;
    transient String fruitName;
    transient String statusName;

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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

    public double getMoney() {
        return price * num;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
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

    public double getPaidMoneyNotIncludeDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, -1, -1, -1, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getMoney();
        }
        return money;
    }

    public double getPaidMoneyIncludeDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, -1, -1, -1, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getMoney() + paidDetail.getDiscount();
        }
        return money;
    }

    public double getPaidMoneyDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, -1, -1, -1, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getDiscount();
        }
        return money;
    }

    public double getDiscounts() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, -1, -1, -1, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getDiscount();
        }
        return money;
    }

    public int getOwnerId() {
        if (ownerId == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            ownerId = inDetail.getOwner();
        }
        return ownerId;
    }

    public int getFruitId() {
        if (fruitId == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            fruitId = inDetail.getFruit();
        }
        return fruitId;
    }

    public String getOwnerName() {
        if (ownerName == null && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            OwnerService ownerService = MyFactory.getOwnerService();
            ownerName = ownerService.getOwnerName(inDetail.getOwner());
        }
        return ownerName;
    }

    public String getConsumerName() {
        if (consumerName == null && cid != -1) {
            ConsumerService consumerService = MyFactory.getConsumerService();
            consumerName = consumerService.getConsumerName(cid);
        }
        return consumerName;
    }

    public String getFruitName() {
        if (fruitName == null && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            FruitService fruitService = MyFactory.getFruitService();
            fruitName = fruitService.getFruitName(inDetail.getFruit());
        }
        return fruitName;
    }

    public String getStatusName() {
        if (status != -1) {
            switch (status) {
                case Constants.OUT_STATUS_ORIGINAL:
                    statusName = Constants.OUT_STATUS_ORIGINAL_STR;
                    break;
                case Constants.OUT_STATUS_PAID_NOT_ENOUGH:
                    statusName = Constants.OUT_STATUS_PAID_NOT_ENOUGH_STR;
                    break;
                case Constants.OUT_STATUS_PAID_ENOUGH:
                    statusName = Constants.OUT_STATUS_PAID_ENOUGH_STR;
                    break;
                default:
                    statusName = Constants.OUT_UNKNOWN_STR;
                    break;
            }
        }
        return statusName;
    }

    @Override
    public int compareTo(OutDetail o) {
        return this.getDate() > o.getDate() ? this.getDate() == o.getDate() ? 0 : -1 : 1;
    }

    @Override
    public String toString() {
        return "out detail--[iid:" + this.iid
                + "owner:" + getOwnerName()
                + ",consumer:" + getConsumerName()
                + ",fruit:" + getFruitName()
                + ",price:" + this.price
                + ",num:" + this.num
                + ",color:" + this.color
                + ",censored:" + this.censored
                + ",uid:" + uid
                + "]";
    }
}
