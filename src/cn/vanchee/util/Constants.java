package cn.vanchee.util;

import java.awt.*;

/**
 * @author vanchee
 * @date 13-1-29
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class Constants {

    public static final String FILE_NAME_OWNER = "Owner.bin";
    public static final String FILE_NAME_CONSUMER = "Consumer.bin";
    public static final String FILE_NAME_FRUIT = "Fruit.bin";
    public static final String FILE_NAME_USER = "User.bin";
    public static final String FILE_NAME_OUT_DETAIL = "OutDetail.bin";
    public static final String FILE_NAME_IN_DETAIL = "InDetail.bin";
    public static final String FILE_NAME_PAID_DETAIL = "PaidDetail.bin";
    public static final String FILE_NAME_MY_PAID = "MyPaidDetail.bin";
    public static final String FILE_NAME_CONSUMPTION = "Consumption.bin";
    public static final String FILE_NAME_RESOURCE = "Resource.bin";

    public static final String OUT_DETAIL_BACKUP_FILE_NAME = "OutDetailBackup.bin";

    public static final int OUT_STATUS_ORIGINAL = 0;
    public static final int OUT_STATUS_PAID_NOT_ENOUGH = 1;
    public static final int OUT_STATUS_PAID_ENOUGH = 2;

    public static final String OUT_STATUS_ORIGINAL_STR = "未还钱";
    public static final String OUT_STATUS_PAID_NOT_ENOUGH_STR = "未还清";
    public static final String OUT_STATUS_PAID_ENOUGH_STR = "还清";
    public static final String OUT_UNKNOWN_STR = "还清";

    public static final int CREATE = 1;
    public static final int DELETE = 2;
    public static final int UPDATE = 3;

    public static final int CENSORED_ORIGINAL = 0;
    public static final int CENSORED_PASS = 1;
    public static final int CENSORED_REFUSE = 2;

    private static Dimension inputDimension;
    private static Dimension dateDimension;
    private static Dimension textAreaDimension;
    private static Dimension addPanelDimension;

    public static Dimension getInputDimension() {
        return inputDimension == null ? new Dimension(100, 20) : inputDimension;
    }

    public static Dimension getDateDimension() {
        return dateDimension == null ? new Dimension(100, 20) : dateDimension;
    }

    public static Dimension getTextAreaDimension() {
        return textAreaDimension == null ? new Dimension(400, 50) : textAreaDimension;
    }

    public static Dimension getAddPanelDimension() {
        return addPanelDimension == null ? new Dimension(300, 40) : addPanelDimension;
    }

    public static String getCensorName(int status) {
        switch (status) {
            case CENSORED_ORIGINAL:
                return "未审";
            case CENSORED_PASS:
                return "通过";
            case CENSORED_REFUSE:
                return "不通过";
            default:
                return "";
        }
    }
}
