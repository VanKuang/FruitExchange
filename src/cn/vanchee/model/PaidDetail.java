package cn.vanchee.model;

import cn.vanchee.util.MyFactory;

/**
 * @author vanchee
 * @date 13-2-1
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class PaidDetail {

    private Integer id;
    private Integer oid; //销售ID
    private Double money;
    private Double discount; //折扣(如果是我的还款，则是-1)
    private String createAt;
    private Integer color;
    private Integer censored;
    private Integer uid;

    transient int iid;
    transient int ownerId;
    transient int cid;
    transient int fid;
    transient String ownerName;
    transient String fruitName;
    transient String consumerName;

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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
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
            ownerId = inDetail != null ? inDetail.getOid() : 0;
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
    public String toString() {
        return "paid detail--[owner:" + getOwnerName()
                + ",consumer:" + getConsumerName()
                + ",money:" + this.money
                + ",discount:" + this.discount
                + ",color:" + this.color
                + ",censored:" + this.censored
                + ",uid:" + uid
                + "]";
    }
}
