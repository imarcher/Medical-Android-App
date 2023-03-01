package com.example.demo.App;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.demo.R;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class App extends Application {





    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this, getResources().getString(R.string.rongyunApp_key));//初始化IM和音视频


        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {


            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                //s是用户的id，用这个进入主界面，method有方法通过id获得用户信息
                Intent intent =new Intent(context, com.example.demo.ui.head.UserInfo.class);
                intent.putExtra("ID",Integer.parseInt(s));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
 });


    }



}
