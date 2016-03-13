package cn.vanchee.model;

import cn.vanchee.service.ConsumerService;
import cn.vanchee.service.FruitService;
import cn.vanchee.service.OwnerService;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;

import java.util.List;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class OutDetail {

    private Integer id;
    private Integer iid; //进货号
    private String createAt;
    private Integer cid;
    private Double price;
    private Integer num;
    private Integer color;
    private Integer censored;
    private Integer uid;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getMoney() {
        return price * num;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
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

    public double getPaidMoneyNotIncludeDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, null, null, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getMoney();
        }
        return money;
    }

    public double getPaidMoneyIncludeDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, null, null, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getMoney() + paidDetail.getDiscount();
        }
        return money;
    }

    public double getPaidMoneyDiscount() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, null, null, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getDiscount();
        }
        return money;
    }

    public double getDiscounts() {
        double money = 0;
        List<PaidDetail> result = MyFactory.getPaidDetailService().queryPaidDetail(-1, -1, id, -1, -1, -1, null, null, -1);
        for (PaidDetail paidDetail : result) {
            money += paidDetail.getDiscount();
        }
        return money;
    }

    public int getOwnerId() {
        if (ownerId == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            ownerId = inDetail.getOid();
        }
        return ownerId;
    }

    public int getFruitId() {
        if (fruitId == 0 && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            fruitId = inDetail.getFid();
        }
        return fruitId;
    }

    public String getOwnerName() {
        if (ownerName == null && iid != -1) {
            InDetail inDetail = MyFactory.getInDetailService().getInDetailById(iid);
            OwnerService ownerService = MyFactory.getOwnerService();
            ownerName = ownerService.getOwnerName(inDetail.getOid());
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
            fruitName = fruitService.getFruitName(inDetail.getFid());
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
