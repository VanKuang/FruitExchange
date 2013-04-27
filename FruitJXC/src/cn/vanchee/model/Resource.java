package cn.vanchee.model;

/**
 * @author vanchee
 * @date 13-3-25
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class Resource {

    public static final int IN_R = 1;
    public static final int IN_W = 2;
    public static final int OUT_R = 3;
    public static final int OUT_W = 4;
    public static final int CONSUMPTION_R = 5;
    public static final int CONSUMPTION_W = 6;
    public static final int MY_PAID_R = 7;
    public static final int MY_PAID_W = 8;
    public static final int PAID_R = 9;
    public static final int PAID_W = 10;
    public static final int MONEY_REPORT_R = 11;
    public static final int PAID_REPORT_R = 12;
    public static final int MONTH_REPORT_R = 13;
    public static final int USER_MNG = 14;
    public static final int DATA_INIT = 15;
    public static final int MY_PAID_EXPORT = 16;
    public static final int PAID_EXPORT = 17;
    public static final int CENSORED = 18;
    public static final int BACKUP = 19;
    public static final int GET_OTHERS_DATA = 20;
    public static final int IN_O = 21;
    public static final int OUT_O = 22;
    public static final int MY_OWN_R = 23;
    public static final int OTHER_OWN_R = 24;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
