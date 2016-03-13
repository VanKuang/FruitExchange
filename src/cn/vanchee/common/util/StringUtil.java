package cn.vanchee.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * util to String
 *
 * @author vanchee
 * @date: 2012-2-21
 * @time: 上午7:20:27
 * @package: vanchee.String
 * @filename: StringUtil.java
 */
public class StringUtil {

    final private static char[] small = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    final private static char[] big = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private StringUtil() {
    }

    /**
     * 判断数组是否为空
     */
    public static <T> boolean isEmpty(T[] obj) {
        return null == obj || 0 == obj.length;
    }

    /**
     * 判断字符串是否为空?
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        } else if (obj instanceof Number) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Collection obj) {
        return null == obj || obj.isEmpty();
    }

    /**
     * 判断Map是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Map obj) {
        return null == obj || obj.isEmpty();
    }

    /**
     * 将字符串的首字母变成大写
     *
     * @param str
     * @return
     */
    public static String getFirstUpcase(String str) {
        if (StringUtil.isEmpty(str))
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.getFirstUpcase("vanchee"));
    }

    /**
     * 判断是否是大写
     *
     * @param chr
     * @return
     */
    public static boolean isBigChr(char chr) {
        return (findChar(big, chr) > -1);
    }

    /**
     * 判断是否是小写
     *
     * @param chr
     * @return
     */
    public static boolean isSmallChr(char chr) {
        return (findChar(small, chr) > -1);
    }

    /**
     * 从字符集里找到字符的位置
     */
    public static int findChar(char[] arry, char chr) {
        for (int i = 0; i < arry.length; i++) {
            if (arry[i] == chr) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 得到大/小写对应的字符
     */
    public static char getRefChar(char chr) throws Exception {
        int i = 0;
        if ((i = findChar(small, chr)) > -1) {
            return big[i];
        } else if ((i = findChar(big, chr)) > -1) {
            return small[i];
        }
        return chr;
    }

}