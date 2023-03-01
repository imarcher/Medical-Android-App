package com.example.demo.method;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.demo.manager.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timemethod {


    public static boolean isbeyonddays(Context context,String time, int days){


        Date thisTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date lastTime = sdf.parse(time);
            long from = lastTime.getTime();
            long to = thisTime.getTime();
            int dayss = (int) ((to - from) / (1000 * 60 * 60 * 24));
            /**
             * 过了days天返回true
             */
            if(dayss >= days)
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("时间转换异常");
        }
        return false;



    }



}
