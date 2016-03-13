package cn.vanchee.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtil {

    public static Field[] getFields(Class clz) {
        Field[] fields = clz.getDeclaredFields();
        if (null != fields)
            return fields;
        return null;
    }

    public static Method[] getMethods(Class clz) {
        Method[] ms = clz.getDeclaredMethods();
        if (null != ms)
            return ms;
        return null;
    }

    public static void main(String[] args) {
        /*
        Field[] f = ClassUtil.getFields(User.class);
        if (null != f)
            for (Field s : f) {
                System.out.println(s.getName() + "-----------" + s.getType());
            }

        Method[] m = ClassUtil.getMethods(User.class);
        if (null != m)
            for (Method mm : m) {
                System.out.println(mm.getName());
            }
            */
    }

}
