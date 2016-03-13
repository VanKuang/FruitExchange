package cn.vanchee.common.jdbc;

import cn.vanchee.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date: 2012-5-3
 * @time: 下午5:30:15
 * @package: cn.com.vanchee.common.jdbc
 * @filename: BaseDAOImpl.java
 * @email: vancheekjh@gmial.com
 * @description:
 */
public abstract class BaseDAO<T> {

    protected static final Logger log = LoggerFactory.getLogger("DAO");

    private Class<T> clz;

    public BaseDAO(Class<T> clz) {
        this.clz = clz;
    }

    public List<Map<String, Object>> getListMap() {
        String sql = "SELECT * FROM " + clz.getSimpleName();
        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            return JdbcResult.getListMap(result);
        } catch (Exception e) {
            log.error("get list map error:", e);
            return Collections.EMPTY_LIST;
        } finally {
            DatabaseUtil.closeConnection();
        }
    }

    public List<Map<String, Object>> getListMap(String sql) {
        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            return JdbcResult.getListMap(result);
        } catch (Exception e) {
            log.error("get list map error:", e);
            return Collections.EMPTY_LIST;
        } finally {
            DatabaseUtil.closeConnection();
        }
    }

    public <T> List<T> getList() {
        String sql = "SELECT * FROM " + clz.getSimpleName();
        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            return JdbcResult.getList(result, clz);
        } catch (Exception e) {
            log.error("get list error:", e);
            return Collections.EMPTY_LIST;
        } finally {
            DatabaseUtil.closeConnection();
        }
    }

    public <T> List<T> getList(String sql) {
        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            return JdbcResult.getList(result, clz);
        } catch (Exception e) {
            log.error("get list error:", e);
            return Collections.EMPTY_LIST;
        } finally {
            DatabaseUtil.closeConnection();
        }
    }

    public boolean isExist(String sql) {
        boolean flag = false;
        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            if (result.next()) {
                if (result.getInt(1) > 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            log.error("decide " + clz.getSimpleName() + " is exist error:", e);
            return false;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return flag;
    }

    public T find(int id) {

        T obj = null;

        String sql = "SELECT * FROM " + clz.getSimpleName() + " where id = " + id;

        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            List<T> list = JdbcResult.getList(result, clz);
            if (!StringUtil.isEmpty(list)) {
                obj = list.get(0);
            }
        } catch (Exception e) {
            log.error("find " + clz.getSimpleName() + " error:", e);
            return null;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return obj;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + clz.getSimpleName() + "WHERE id = " + id;
        return executeUpdate(sql);
    }

    public boolean executeUpdate(String sql) {
        try {
            DatabaseUtil.getStatement().execute(sql);
            return true;
        } catch (Exception e) {
            log.error("execute sql error, sql:" + sql, e);
            return false;
        } finally {
            DatabaseUtil.closeConnection();
        }
    }

}
