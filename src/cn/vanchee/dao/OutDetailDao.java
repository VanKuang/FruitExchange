package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.model.OutDetail;

import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class OutDetailDao extends BaseDAO<OutDetail> {

    public OutDetailDao() {
        super(OutDetail.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + OutDetail.class.getSimpleName());
        String sql = "CREATE TABLE " + OutDetail.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " iid int(10) DEFAULT 0,"
                + " cid int(5) DEFAULT 0,"
                + " uid int(5) DEFAULT 0,"
                + " num int(5) DEFAULT 0,"
                + " price double(5) DEFAULT 0,"
                + " color int(10) DEFAULT 0,"
                + " censored int(1) DEFAULT 0,"
                + " status int(1) DEFAULT 0,"
                + " create_at DATE)";
        return executeUpdate(sql);
    }

    public boolean create(OutDetail outDetail) {
        String sql = "INSERT INTO " + OutDetail.class.getSimpleName()
                + " (iid, cid, uid, num, price, color, censored, create_at) VALUES("
                + outDetail.getIid() + ", "
                + outDetail.getCid() + ","
                + outDetail.getUid() + ","
                + outDetail.getNum() + ","
                + outDetail.getPrice() + ","
                + outDetail.getColor() + ","
                + outDetail.getCensored() + ","
                + outDetail.getStatus() + ",'"
                + outDetail.getCreateAt() + "')";
        return super.executeUpdate(sql);
    }

    public boolean update(OutDetail outDetail) {
        String sql = "UPDATE " + OutDetail.class.getSimpleName() + " SET "
                + " iid = " + outDetail.getIid()
                + ", cid = " + outDetail.getCid()
                + ", uid = " + outDetail.getUid()
                + ", num = " + outDetail.getNum()
                + ", price = " + outDetail.getPrice()
                + ", color = " + outDetail.getColor()
                + ", censored = " + outDetail.getCensored()
                + ", status = " + outDetail.getStatus()
                + " WHERE id = " + outDetail.getId();
        return super.executeUpdate(sql);
    }

    public List<OutDetail> queryOutDetail(int id, int iid, int cid, int censored,
                                          Date from, Date to, int status, int uid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(OutDetail.class.getSimpleName()).append(" WHERE 1=1");
        if (id != -1) {
            sql.append(" AND id = ").append(id);
        }
        if (iid != -1) {
            sql.append(" AND iid = ").append(iid);
        }
        if (uid != -1) {
            sql.append(" AND uid = ").append(uid);
        }
        if (cid != -1) {
            sql.append(" AND cid = ").append(cid);
        }
        if (censored != -1) {
            sql.append(" AND censored = ").append(censored);
        }
        if (status != -1) {
            sql.append(" AND status = ").append(status);
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
