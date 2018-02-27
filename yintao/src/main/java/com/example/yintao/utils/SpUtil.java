package com.example.yintao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences保存数据 使用Application初始化，防止内存泄漏
 * commit同步提交到磁盘，apply先原子提交到内存后异步提交到磁盘
 * Created by YinTao on 2017/12/5.
 */

public class SpUtil
{
    private static SharedPreferences sp;
    private static Context context;

    private SpUtil()
    {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void initSpUtil(Context con)
    {
        context = con;
    }

    //    private static SharedPreferences getSh() //我也不知道哪种方法好
    //    {
    //        return PreferenceManager.getDefaultSharedPreferences(context);
    //    }

    public static boolean saveString(String tag, String value)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.edit().putString(tag, value).commit();
    }

    public static String getString(String tag)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.getString(tag, null);
    }

    public static boolean saveInt(String tag, int value)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.edit().putInt(tag, value).commit();
    }

    public static int getInt(String tag)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.getInt(tag, -1);
    }

    public static boolean saveBoolean(String tag, boolean value)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.edit().putBoolean(tag, value).commit();
    }

    public static boolean getBoolean(String tag)
    {
        if (sp == null)
        {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp.getBoolean(tag, false);
    }

}
