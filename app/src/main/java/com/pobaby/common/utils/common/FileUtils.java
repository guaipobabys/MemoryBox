package com.pobaby.common.utils.common;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件操作工具类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/24 14:30
 */
public class FileUtils {

    /**
     * 获取文件中字符内容
     *
     * @param fileName 文件全路径
     * @return
     */
    public static String getStringFromFile(String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 把字符内容写到文件中
     *
     * @param fileName 文件全路径
     * @return
     */
    public static void setStringToFile(String fileName, String cntent) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = new FileOutputStream(new File(fileName));
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(cntent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
