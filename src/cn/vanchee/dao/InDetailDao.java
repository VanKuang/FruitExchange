package cn.vanchee.dao;

import cn.vanchee.common.jdbc.BaseDAO;
import cn.vanchee.model.InDetail;

import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.dao
 * @verson v1.0.0
 */
public class InDetailDao extends BaseDAO<InDetail> {

    public InDetailDao() {
        super(InDetail.class);
    }

    public boolean createTable() {
        executeUpdate("DROP TABLE IF EXISTS " + InDetail.class.getSimpleName());
        String sql = "CREATE TABLE " + InDetail.class.getSimpleName()
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " oid int(5) DEFAULT 0,"
                + " fid int(5) DEFAULT 0,"
                + " uid int(5) DEFAULT 0,"
                + " num int(5) DEFAULT 0,"
                + " price double(5) DEFAULT 0,"
                + " sale int(5) DEFAULT 0,"
                + " color int(10) DEFAULT 0,"
                + " censored int(1) DEFAULT 0,"
                + " create_at DATE)";
        return executeUpdate(sql);
    }

    public boolean create(InDetail inDetail) {
        String sql = "INSERT INTO " + InDetail.class.getSimpleName()
                + " (oid, fid, uid, num, price, sale, color, censored, create_at) VALUES("
                + inDetail.getOid() + ", "
                + inDetail.getFid() + ","
                + inDetail.getUid() + ","
                + inDetail.getNum() + ","
                + inDetail.getPrice() + ","
                + inDetail.getSale() + ","
                + inDetail.getColor() + ","
                + inDetail.getCensored() + ",'"
                + inDetail.getCreateAt() + "')";
        return executeUpdate(sql);
    }

    public boolean update(InDetail inDetail) {
        String sql = "UPDATE " + InDetail.class.getSimpleName() + " SET "
                + " oid = " + inDetail.getOid()
                + ", fid = " + inDetail.getFid()
                + ", uid = " + inDetail.getUid()
                + ", num = " + inDetail.getNum()
                + ", price = " + inDetail.getPrice()
                + ", sale = " + inDetail.getSale()
                + ", color = " + inDetail.getColor()
                + ", censored = " + inDetail.getCensored()
                + " WHERE id = " + inDetail.getId();
        return super.executeUpdate(sql);
    }

    public List<InDetail> queryInDetail(int id, int oid, int fid, int censored, Date from, Date to, int uid) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(InDetail.class.getSimpleName()).append(" WHERE 1=1");
        if (id != -1) {
            sql.append(" AND id = ").append(id);
        }
        if (oid != -1) {
            sql.append(" AND oid = ").append(oid);
        }
        if (fid != -1) {
            sql.append(" AND fid = ").append(fid);
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
