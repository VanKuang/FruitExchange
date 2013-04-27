package cn.vanchee.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class DataUtil {

    private static final Logger log = LoggerFactory.getLogger(DataUtil.class);
    private static final int MAX_SIZE = 5000;

    public static void writeListToFile(String fileName, List list) {
        List result = new ArrayList(list);
        long start = System.currentTimeMillis();
        FileOutputStream fileOutputStream = null;
        RandomAccessFile file = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            file = new RandomAccessFile(FileUtil.getFileNameWithPath(fileName), "rw");
            fileOutputStream = new FileOutputStream(file.getFD());
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(result);
        } catch (FileNotFoundException e) {
            log.error("write list to file error:" + e.getStackTrace());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("write list to file error:" + e.getStackTrace());
            e.printStackTrace();
        } finally {
            try {
                file.close();
                fileOutputStream.close();
                objectOutputStream.close();
                result.clear();
            } catch (IOException e) {
                log.error("write list to file error, file close error:" + e.getStackTrace());
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        log.info("write file:" + fileName + " use time:" + (end - start) + ",data size:" + list.size());
    }

    public static List readListFromFile(String fileName) {
        long start = System.currentTimeMillis();
        List list = new ArrayList();
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(FileUtil.getFileNameWithPath(fileName));
            objectInputStream = new ObjectInputStream(fileInputStream);
            list = (List) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            log.error("read file error:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("read file error:" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("read file error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                objectInputStream.close();
            } catch (IOException e) {
                log.error("read from file, file input stream close error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        log.info("read file:" + fileName + " use time:" + (end - start) + ",data size:" + list.size());
        return list;
    }
}
