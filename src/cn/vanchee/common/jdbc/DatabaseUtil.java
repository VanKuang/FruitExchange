package cn.vanchee.common.jdbc;

import cn.vanchee.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author vanchee
 * @date: 2012-5-3
 * @time: 下午1:00:20
 * @package: cn.com.vanchee.common
 * @filename: DatabaseUtil.java
 * @email: vancheekjh@gmial.com
 * @description: 连接数据库
 */
public class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);

    private static Connection conn = null;
    private static Statement statement = null;

    /**
     * 获取连接Connection
     *
     * @return Connection
     */
    public static Connection getConnection() throws Exception {

        // 注册
        Class.forName("org.sqlite.JDBC");
        // 获取连接
        conn = DriverManager.getConnection("jdbc:sqlite:" + FileUtil.getDataBase());
        return conn;
    }

    public static Statement getStatement() throws Exception {
        getConnection();
        return conn.createStatement();
    }

    /**
     * 关闭Connection
     */
    public static void closeConnection() {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection error:", e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("close statement error:", e);
            }
        }
    }

    public static void deleteDB() {
        File file = new File(FileUtil.getDataBase());
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

}
