package com.example.lwxg.changweistory.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;

public class LocalCacheUtils {
    /**
     *      * 项目图片存储的路径
     *      
     */
    private static final String BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cwccc/pic";

    /**
     *      * 存入缓存文件
     *      *
     *      * @param url
     *      * @param bitmap
     *      
     */
    public static void setCache(String url, Bitmap bitmap) throws Exception {
        File baseFile = new File(BASEPATH);
        if (!baseFile.exists() || !baseFile.isDirectory()) {
            // 如果不存在 或者不是一个文件夹 就去创建文件夹
            baseFile.mkdirs();
        }
        // 用url的md5值作为文件的名字
        String md5 = MD5Encoder.encode(url);
        // 生成图片对应的文件
        File bitemapFile = new File(baseFile, md5);
        try {
            // 写入流
            FileOutputStream fos = new FileOutputStream(bitemapFile);
            // 压缩 参1 格式 参2 100就是不压缩 参3写入流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *      * 从本地取出图片
     *      *
     *      * @param url
     *      * @return
     *      
     */
    public static Bitmap getCache(String url) throws Exception {
        Bitmap bitmap = null;
        File baseFile = new File(BASEPATH);
        if (!baseFile.exists() || !baseFile.isDirectory()) {
            // 如果不存在 直接返回
            return bitmap;
        }
        // 用url的md5值作为文件的名字
        String md5 = MD5Encoder.encode(url);
        // 生成图片对应的文件
        File bitemapFile = new File(baseFile, md5);
        if (bitemapFile.exists()) {
            try {
                // 文件转bitmap
                bitmap = BitmapFactory.decodeStream(new FileInputStream(bitemapFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static class MD5Encoder {
        public static String encode(String string) throws Exception {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        }
    }
}
