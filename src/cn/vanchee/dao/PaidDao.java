package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.model.PaidDetail;

import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class PaidDao extends BaseDAO<PaidDetail> {

    public PaidDao() {
        super(PaidDetail.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + PaidDetail.class.getSimpleName());
        String sql = "CREATE TABLE " + PaidDetail.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " oid int(10) DEFAULT 0,"
                + " uid int(5) DEFAULT 0,"
                + " money double(5) DEFAULT 0,"
                + " color int(10) DEFAULT 0,"
                + " censored int(1) DEFAULT 0,"
                + " create_at DATE)";
        return executeUpdate(sql);
    }

    public boolean create(PaidDetail paidDetail) {
        String sql = "INSERT INTO " + PaidDetail.class.getSimpleName()
                + " (iid, uid, money, color, censored, create_at) VALUES("
                + paidDetail.getIid() + ", "
                + paidDetail.getUid() + ","
                + paidDetail.getMoney() + ","
                + paidDetail.getColor() + ","
                + paidDetail.getCensored() + ",'"
                + paidDetail.getCreateAt() + "')";
        return super.executeUpdate(sql);
    }

    public boolean update(PaidDetail paidDetail) {
        String sql = "UPDATE " + PaidDetail.class.getSimpleName() + " SET "
                + " iid = " + paidDetail.getIid()
                + ", uid = " + paidDetail.getUid()
                + ", price = " + paidDetail.getMoney()
                + ", color = " + paidDetail.getColor()
                + ", censored = " + paidDetail.getCensored()
                + " WHERE id = " + paidDetail.getId();
        return super.executeUpdate(sql);
    }

    public List<PaidDetail> queryMyPaidDetail(int id, int iid, int censored, Date from, Date to, int uid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(PaidDetail.class.getSimpleName()).append(" WHERE 1=1");
        sql.append(" AND discount = -1");
        if (id != -1) {
            sql.append(" AND id = ").append(id);
        }
        if (iid != -1) {
            sql.append(" AND oid = ").append(iid);
        }
        if (uid != -1) {
            sql.append(" AND uid = ").append(uid);
        }
        if (censored != -1) {
            sql.append(" AND censored = ").append(censored);
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

    public List<PaidDetail> queryPaidDetail(int id, int oid, int censored, Date from, Date to, int uid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(PaidDetail.class.getSimpleName()).append(" WHERE 1=1");
        if (id != -1) {
            sql.append(" AND id = ").append(id);
        }
        if (oid != -1) {
            sql.append(" AND oid = ").append(oid);
        }
        if (uid != -1) {
            sql.append(" AND uid = ").append(uid);
        }
        if (censored != -1) {
            sql.append(" AND censored = ").append(censored);
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
