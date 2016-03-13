package cn.vanchee.model;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class Consumption {

    private Integer id;
    private Double money;
    private String createAt;
    private String desc;
    private Integer color;
    private Integer censored;
    private Integer uid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
