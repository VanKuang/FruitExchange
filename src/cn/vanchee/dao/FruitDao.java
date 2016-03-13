package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.common.jdbc.DatabaseUtil;
import cn.vanchee.common.jdbc.JdbcResult;
import cn.vanchee.common.util.StringUtil;
import cn.vanchee.model.Fruit;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class FruitDao extends BaseDAO<Fruit> {

    public FruitDao() {
        super(Fruit.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + Fruit.class.getSimpleName());
        String sql = "CREATE TABLE " + Fruit.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(50) not null)";
        return executeUpdate(sql);
    }

    public boolean isExist(String name) {
        String sql = "SELECT COUNT(*) FROM " + Fruit.class.getSimpleName() + " WHERE name='" + name + "'";
        return super.isExist(sql);
    }

    public boolean create(String name) {
        String sql = "INSERT INTO " + Fruit.class.getSimpleName() + " (name) VALUES('" + name + "')";
        return super.executeUpdate(sql);
    }

    public Fruit findByName(String name) {
        Fruit fruit = null;

        String sql = "SELECT * FROM " + Fruit.class.getSimpleName() + " where name = '" + name + "'";

        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            List<Fruit> list = JdbcResult.getList(result, Fruit.class);
            if (!StringUtil.isEmpty(list)) {
                fruit = list.get(0);
            }
        } catch (Exception e) {
            log.error("find " + Fruit.class.getSimpleName() + " by name error:", e);
            return null;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return fruit;
    }
}
