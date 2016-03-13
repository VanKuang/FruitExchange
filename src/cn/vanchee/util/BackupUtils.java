package cn.vanchee.util;

import org.apache.commons.lang.time.DateUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vanchee
 * @date 13-2-14
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class BackupUtils {

    private BackupUtils() {
    }

    public static void backupDaily() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String backupPath = FileUtil.getDataBackupPath() + "\\daily\\backup_daily_"
                + DateUtils.addDays(new Date(), -1) + "\\";

        //first need judge backup if exist
        File backupYesterday = new File(backupPath);
        if (backupYesterday.exists()) {
            return;
        }

        backupPath = FileUtil.getDataBackupPath() + "\\daily\\backup_daily_"
                + sdf.format(DateUtils.addDays(new Date(), -1)) + "\\";
        final String finalBackupPath = backupPath;
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                FileUtil.copyDirectory(FileUtil.getDataPath(), finalBackupPath);
            }
        });
    }

    public static void backupNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String backupPath = FileUtil.getDataBackupPath() + "\\now\\backup_now_" + sdf.format(new Date()) + "\\";
        FileUtil.copyDirectory(FileUtil.getDataPath(), backupPath);
    }

    public static void backupInit() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        String backupPath = FileUtil.getDataBackupPath() + "\\init\\backup_init" + sdf.format(new Date()) + "\\";
        FileUtil.copyDirectory(FileUtil.getDataPath(), backupPath);
    }
}
