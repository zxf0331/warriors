package com.qm.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SPHelper {

    public static final String USERNAME = "Warriors";
    public static final String PASSWORD = "password";

    //保存数据
    public static boolean saveUserInfo(Context context,String username,String password){
        //创建 SharedPreferences 对象
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        //创建编辑对象
        SharedPreferences.Editor editor = sp.edit();
        //通过editor存入用户名和密码
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        //提交数据
        editor.apply();
        return true;
    }

    //获取数据
    public static Map<String,String> getUserInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String username = sp.getString(USERNAME, null);
        String password = sp.getString(PASSWORD, null);
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME,username);
        map.put(PASSWORD,password);
        return map;
    }
}
