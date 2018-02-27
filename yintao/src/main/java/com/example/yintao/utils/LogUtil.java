package com.example.yintao.utils;

import android.util.Log;

/**
 * 日志管理
 * Created by YinTao on 2018/1/17.
 */

public class LogUtil
{
    private static boolean flag = true;//默认开启log
    private static String TAG = "----------";//默认开启log
    private static boolean isWriteToFile = false;

    private LogUtil()
    {
    }

    /**
     * log初始化，决定是否打印log
     * @param open 是否开启
     * @param tag  打印标签
     */
    public static void initLog(boolean open, String tag, boolean writeToFile)
    {
        flag = open;
        TAG = tag;
        isWriteToFile = writeToFile;
    }

    public static void e(String msg)
    {
        if (!flag)
        {
            return;
        }
        Log.e(TAG, msg);
    }

    public static void i(String msg)
    {
        if (!flag)
        {
            return;
        }
        Log.i(TAG, msg);
    }

}
