package cn.vanchee.util;

import java.io.*;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class FileUtil {

    public static String getDataPath() {
        return new File("").getAbsolutePath() + "\\data\\";
    }

    public static String getDataBackupPath() {
        return new File("").getAbsolutePath() + "\\backup\\";
    }

    public static String getFileNameWithPath(String name) {
        return getDataPath() + name;
    }

    public static void createFile(String fileName, boolean recover) throws IOException {
        File file = new File(getDataPath() + fileName);
        if (!file.exists()) {
            file.createNewFile();
            return;
        }
        if (recover) {
            file.delete();
            file.createNewFile();
            return;
        }
        throw new RuntimeException("file:" + fileName +  " is exist");
    }

    public static void writeToFile(String fileName, String content, boolean append) {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(fileName, append);
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        File file = new File(fileName);

        if (!file.exists()) {
            throw new RuntimeException("this file is not exist");
        }
        if (!file.isFile()) {
            throw  new RuntimeException("this file name is not a file");
        }

        if (file.isFile() && file.exists()) {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String result = null;
            while ((result = bufferedReader.readLine()) != null) {
                sb.append(result.toString()).append(System.getProperty("line.separator"));
            }
            inputStreamReader.close();
            bufferedReader.close();
        }
        return sb.toString();
    }

    /**
     * copy file
     * @param sourcePath
     * @param targetPath
     */
    public static void copyFile (String sourcePath, String targetPath) {
        // define files
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);

        // define stream
        InputStream is = null;
        OutputStream os = null;

        int byteread = 0;
        byte[] cbuf = new byte[1024];

        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(targetFile);

            while ((byteread = is.read(cbuf)) != -1) {
                os.write(cbuf, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * copy all files that in sourceFile's directory
     * @param sourceFile
     * @param targetFile
     */
    public static void copyDirectory (String sourceFile, String targetFile) {
        File[] files = new File(sourceFile).listFiles();

        // first check the tragetFile is exist or not
        File destFile = new File(targetFile);
        if (!destFile.exists())
            destFile.mkdirs();

        for (File file : files) {
            if (file.isFile()) { // if is a file
                FileUtil.copyFile(file.getAbsolutePath(), targetFile + "\\" + file.getName());
            } else { // else it is a directory, then first create the folder
                File newFolderFile = new File(file.getAbsolutePath());
                newFolderFile.mkdirs();
                FileUtil.copyDirectory(file.getAbsolutePath(), targetFile + "\\" + FileUtil.getNewFolderName(file.getAbsolutePath()));
            }
        }
    }

    /**
     * get the folder name that should create
     * @param path
     * @return
     */
    public static String getNewFolderName (String path) {
        String name = "";
        name = path.substring(path.lastIndexOf("\\") + 1, path.length());
        return name;
    }

}
