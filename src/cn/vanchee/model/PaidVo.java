package cn.vanchee.model;

public class PaidVo {

    private int oid;
    private double shouldPaid;
    private double hadPaid;
    private String createAt;
    private String fruitName;
    private String name;
    private int color;

    public double getShouldPaid() {
        return shouldPaid;
    }

    public void setShouldPaid(double shouldPaid) {
        this.shouldPaid = shouldPaid;
    }

    public double getHadPaid() {
        return hadPaid;
    }

    public void setHadPaid(double hadPaid) {
        this.hadPaid = hadPaid;
    }

    public int getOid() {
        return oid;

    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
