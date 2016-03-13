package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.common.jdbc.DatabaseUtil;
import cn.vanchee.common.jdbc.JdbcResult;
import cn.vanchee.common.util.StringUtil;
import cn.vanchee.model.User;
import sun.misc.BASE64Encoder;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-5
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class UserDao extends BaseDAO<User> {

    public UserDao() {
        super(User.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + User.class.getSimpleName());
        String sql = "CREATE TABLE " + User.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(50) not null,"
                + " psd varchar(100) not null,"
                + " resource varchar(100) default null)";
        return executeUpdate(sql);
    }

    public boolean isExist(String name) {
        String sql = "SELECT COUNT(id) FROM " + User.class.getSimpleName() + " WHERE name='" + name + "'";
        return super.isExist(sql);
    }

    public boolean create(User user) {
        String sql = "INSERT INTO " + User.class.getSimpleName()
                + " (name, psd, resource) VALUES('"
                + user.getName()
                + "', '" + (new BASE64Encoder()).encodeBuffer(user.getPassword().getBytes()) + "',"
                + "'" + user.getResource().toString() + "')";
        return super.executeUpdate(sql);
    }

    public boolean update(User user) {
        String sql = "UPDATE " + User.class.getSimpleName() + " SET name = '"
                + user.getName() + "', psd = '"
                + (new BASE64Encoder()).encodeBuffer(user.getPassword().getBytes()) + "', resource = '"
                + user.getResource().toString() + "' WHERE id = " + user.getId();
        return super.executeUpdate(sql);
    }

    public User findByName(String name) {

        User user = null;

        String sql = "SELECT * FROM " + User.class.getSimpleName() + " where name = '" + name + "'";

        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            List<User> list = JdbcResult.getList(result, User.class);
            if (!StringUtil.isEmpty(list)) {
                user = list.get(0);
            }
        } catch (Exception e) {
            log.error("find " + User.class.getSimpleName() + " by name error:", e);
            return null;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return user;
    }
}
