package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.common.jdbc.DatabaseUtil;
import cn.vanchee.common.jdbc.JdbcResult;
import cn.vanchee.common.util.StringUtil;
import cn.vanchee.model.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-5
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class ConsumerDao extends BaseDAO<Consumer> {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDao.class);

    public ConsumerDao() {
        super(Consumer.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + Consumer.class.getSimpleName());
        String sql = "CREATE TABLE " + Consumer.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(50) not null)";
        return executeUpdate(sql);
    }

    public boolean isExist(String name) {
        String sql = "SELECT COUNT(*) FROM " + Consumer.class.getSimpleName() + " WHERE name='" + name + "'";
        return super.isExist(sql);
    }

    public boolean create(String name) {
        String sql = "INSERT INTO " + Consumer.class.getSimpleName() + " (name) VALUES('" + name + "')";
        return super.executeUpdate(sql);
    }

    public Consumer findByName(String name) {
        Consumer consumer = null;

        String sql = "SELECT * FROM " + Consumer.class.getSimpleName() + " where name = '" + name + "'";

        try {
            ResultSet result = DatabaseUtil.getStatement().executeQuery(sql);
            List<Consumer> list = JdbcResult.getList(result, Consumer.class);
            if (!StringUtil.isEmpty(list)) {
                consumer = list.get(0);
            }
        } catch (Exception e) {
            log.error("find " + Consumer.class.getSimpleName() + " by name error:", e);
            return null;
        } finally {
            DatabaseUtil.closeConnection();
        }

        return consumer;
    }
}
