package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.model.Consumption;

import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class ConsumptionDao extends BaseDAO<Consumption> {

    public ConsumptionDao() {
        super(Consumption.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + Consumption.class.getSimpleName());
        String sql = "CREATE TABLE " + Consumption.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " uid int(5) DEFAULT 0,"
                + " money double(5) DEFAULT 0,"
                + " desc text not null,"
                + " color int(10) DEFAULT 0,"
                + " censored int(1) DEFAULT 0,"
                + " create_at DATE)";
        return executeUpdate(sql);
    }

    public boolean create(Consumption consumption) {
        String sql = "INSERT INTO " + Consumption.class.getSimpleName()
                + " (uid, money, desc, color, censored, create_at) VALUES("
                + consumption.getUid() + ", "
                + consumption.getMoney() + ","
                + consumption.getDesc() + ","
                + consumption.getColor() + ","
                + consumption.getCensored() + ",'"
                + consumption.getCreateAt() + "')";
        return super.executeUpdate(sql);
    }

    public boolean update(Consumption consumption) {
        String sql = "UPDATE " + Consumption.class.getSimpleName() + " SET "
                + " uid = " + consumption.getUid()
                + ", money = " + consumption.getMoney()
                + ", desc = " + consumption.getDesc()
                + ", color = " + consumption.getColor()
                + ", censored = " + consumption.getCensored()
                + " WHERE id = " + consumption.getId();
        return super.executeUpdate(sql);
    }

    public List<Consumption> queryConsumption(Date from, Date to, int uid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(Consumption.class.getSimpleName()).append(" WHERE 1=1");
        if (uid != -1) {
            sql.append(" AND uid = ").append(uid);
        }
        if (from != null) {
            sql.append(" AND create_at >= ").append(from);
        }
        if (to != null) {
            sql.append(" AND create_at <= ").append(to);
            ;
        }

        return super.getList(sql.toString());
    }
}
