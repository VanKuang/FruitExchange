package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.common.jdbc.DatabaseUtil;
import cn.vanchee.common.jdbc.JdbcResult;
import cn.vanchee.common.util.StringUtil;
import cn.vanchee.model.Owner;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class OwnerDao extends BaseDAO<Owner> {

    public OwnerDao() {
        super(Owner.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + Owner.class.getSimpleName());
        String sql = "CREATE TABLE " + Owner.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(50) not null)";
        return executeUpdate(sql);
    }

    public boolean isExist(String name) {
        String sql = "SELECT COUNT(*) FROM " + Owner.class.getSimpleName() + " WHERE name='" + name + "'";
        return super.isExist(sql);
    }

    public boolean create(String name) {
        String sql = "INSERT INTO " + Owner.class.getSimpleName() + " (name) VALUES('" + name + "')";
        return super.executeUpdate(sql);
    }

    public Owner findByName(String name) {
        Owner fruit = null;

        String sql = "SELECT * FROM " + Owner.class.getSimpleName() + " where name = '" + name + "'";

        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            List<Owner> list = JdbcResult.getList(result, Owner.class);
            if (!StringUtil.isEmpty(list)) {
                fruit = list.get(0);
            }
        } catch (Exception e) {
            log.error("find " + Owner.class.getSimpleName() + " by name error:", e);
            return null;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return fruit;
    }
}
