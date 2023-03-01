package com.example.demo.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionManager {

    //静态
    public static SessionManager instance = new SessionManager();
    private SessionManager() {
    }


    /**
     * 保存自动登录的用户信息
     */
    public void saveSession(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences("session", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        String dateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        editor.putString("time", dateStr);
        editor.apply();
    }

    /**
     * 清空用户信息
     */
    public void clearSession(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("session", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }



    public String getUsername(Context context) {
        SharedPreferences sp = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return sp.getString("username", "");
    }


    public String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return sp.getString("password", "");
    }

    public String getTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("session", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        return sp.getString("time", "");
    }



    public boolean isHasSession(Context context){

        if(getUsername(context)==null) return false;
        return true;
    }


    public boolean isBeyondTime(Context context)
    {
        String lastTimeStr = getTime(context);
        Date thisTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try{
            Date lastTime = sdf.parse(lastTimeStr);
            long from = lastTime.getTime();
            long to = thisTime.getTime();
            int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
            /**
             * 30天未登录退出
             */
            if(days >= 30)
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("时间转换异常");
        }
        return false;
    }
}
