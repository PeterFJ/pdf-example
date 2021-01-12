package com.example.pdfdemo.util;

import java.io.*;

/**
 * @author yiqr
 * @date 2019-03-18 16:19
 * @Description: TODO
 */
public class FileUtils {

    /**
     * 辅助方法，本地文件转为byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] inputStream2ByteArray(String filePath) {
        byte[] data = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(filePath);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            data = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                //ignore
            }
        }
        return data;
    }


    public static void copyDir(String sourcePath, String newPath) {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

            if (new File(sourcePath + file.separator + filePath[i]).isFile()) {
                copyFile(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

        }
    }


    public static void copyFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(file);

            byte[] buffer = new byte[2097152];
            int readByte = 0;
            while ((readByte = in.read(buffer)) != -1) {
                out.write(buffer, 0, readByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 递归删除
     *
     * @param path
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files.length == 0) {
                    return;
                } else {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDir(file.getPath());
                        }
                    }
                }
            } else {
                dir.delete();
            }
        }
    }

}
