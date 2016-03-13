package cn.vanchee.common.jdbc;

import cn.vanchee.common.util.BeanUtil;
import cn.vanchee.common.util.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date: 2012-5-3
 * @time: 下午5:18:31
 * @package: cn.com.vanchee.common.jdbc
 * @filename: JdbcResult.java
 * @email: vancheekjh@gmial.com
 * @description: 格式化ResultSet结果集
 */
public class JdbcResult {

    /**
     * 将结果集转换成MapList
     *
     * @param _rs
     * @return
     * @throws java.sql.SQLException
     */
    public static List<Map<String, Object>> getListMap(ResultSet _rs) throws SQLException {
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        while (_rs.next()) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            ResultSetMetaData resultSetMetaData = _rs.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String name = resultSetMetaData.getColumnName(i);
                returnMap.put(name, _rs.getObject(name));
            }
            returnList.add(returnMap);
        }
        return returnList;
    }

    /**
     * 将结果集转换成List形式
     *
     * @param rs
     * @param clz
     * @return
     * @throws java.sql.SQLException
     */
    public static <T> List<T> getList(ResultSet rs, Class clz) throws SQLException {
        List<T> list = new ArrayList<T>();
        while (rs.next()) {
            try {
                Object obj = BeanUtil.getBeanFromResultSet(rs, clz);
                if (!StringUtil.isEmpty(obj)) {
                    list.add((T) obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
