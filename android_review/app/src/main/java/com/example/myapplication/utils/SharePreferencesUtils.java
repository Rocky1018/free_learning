package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.domain.User;

/**
 * 用与存储用户信息及注销时删除用户信息
 */
public class SharePreferencesUtils {

    //存储用户用户名密码的共享文件
    public static final String USER_INFORMATION_FILE = "userinformation";

    //登录时保存信息
    public static void save(Context context, String username, String passwordm, String fileName) {
        //将用户名密码缓存至本地共享文件
        SharedPreferences.Editor edit = context.getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
        edit.putString("username", "admin");
        edit.putString("password", "123456");
        edit.apply();
    }

    //登录时保存信息
    public static void save(Context context, User user, String fileName) {
        Log.d("LoginInfo", user.toString());
        //将用户名密码缓存至本地共享文件
        SharedPreferences.Editor edit = context.getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
        edit.putString("userId", user.getUserId());
        edit.putString("userLoginId", user.getUserLoginId());
        edit.putString("userRole", String.valueOf(user.getUserRole()));
        edit.putString("userName", user.getUserName());
        edit.putString("userPassword", user.getUserPassword());
        edit.putString("userPhoneNum", user.getUserPhoneNum());
        edit.putString("userEmail", user.getUserEmail());
        edit.putString("userRegisterDate", user.getUserRegisterDate().toLocaleString());
        edit.apply();
    }

    // 编辑个人信息页时更新信息
    public static void save(Context context, String localUserLoginId, String localUserName, String localUserEmail, String localUserPhoneNum, String fileName) {
        Log.d("LoginInfo", localUserLoginId + " " + localUserName + " " + localUserEmail + " " + localUserEmail);
        //将用户名密码缓存至本地共享文件
        SharedPreferences.Editor edit = context.getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
        edit.putString("userLoginId", localUserLoginId);
        edit.putString("userName", localUserName);
        edit.putString("userPhoneNum", localUserPhoneNum);
        edit.putString("userEmail", localUserEmail);
        edit.apply();
    }

    //注销时删除信息
    public static void clear(Context context, String fileName) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.commit();
    }

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 清除指定数据
     *
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("定义的键名");
        editor.commit();
    }

}
