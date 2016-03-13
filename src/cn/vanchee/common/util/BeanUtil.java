package cn.vanchee.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;

public class BeanUtil {

    /**
     * 将ResultSet结果集映射到一个Bean中
     *
     * @param rs
     * @param clz
     * @return
     */
    public static Object getBeanFromResultSet(ResultSet rs, Class clz) throws Exception {
        Object obj = null;
        obj = clz.newInstance();
        // 首先获取所有列的名字和声明方法
        Field[] fields = ClassUtil.getFields(clz);
        Method[] method = ClassUtil.getMethods(clz);
        // 遍历所有列
        for (Field f : fields) {
            String setMethodName = "set" + f.getName();
            // 遍历所有声明方法，如果set方面名字和生命的方法一致，则执行set方法
            for (Method m : method) {
                if (m.getName().equalsIgnoreCase(setMethodName.toString())) {
                    setMethodName = m.getName();
                    // 获取值
                    Object value = rs.getObject(getFieldName(f.getName(), true));

                    try {
                        Method setMethod = obj.getClass().getMethod(setMethodName, value.getClass());
                        setMethod.invoke(obj, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return obj;
    }

    /**
     * 将属性名字变换成 _
     *
     * @param _name
     * @param erveryBigWord
     * @return
     * @throws Exception
     */
    public static String getFieldName(String _name, boolean erveryBigWord) throws Exception {
        char[] nameChars = _name.toCharArray();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < nameChars.length; i++) {
            if (StringUtil.isBigChr(nameChars[i])) {
                if (i > 0 && (StringUtil.isSmallChr(nameChars[i - 1]) || erveryBigWord)) {
                    sb.append("_");
                }
                sb.append(nameChars[i]);
            } else
                sb.append(StringUtil.getRefChar(nameChars[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
//		System.out.println(BeanUtil);
    }
}
