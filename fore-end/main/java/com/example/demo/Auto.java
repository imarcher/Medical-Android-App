package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.demo.Callbacks.login1CallBack;
import com.example.demo.Callbacks.login2CallBack;
import com.example.demo.manager.SessionManager;
import com.example.demo.method.IMreg;
import com.example.demo.models.UserDocterinfo;
import com.example.demo.models.Users;
import com.example.demo.models.name_head_url;
import com.example.demo.ui.login.LoginFragment;
import com.example.demo.ui.login.Login_Register;
import com.zhy.http.okhttp.OkHttpUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;

import static java.lang.Thread.sleep;


public class Auto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(1000);//休眠1秒
                    autologin();
                }catch (Exception e){

                };

            }


        }.start();
    }

    private void autologin() {
        //自动登录
        if(SessionManager.instance.isHasSession(Auto.this)){
            login1();
        }
        else {

            toLogin();
        }
    }

    private void toLogin(){
        Intent intent = new Intent(Auto.this, Login_Register.class);
        startActivity(intent);
        finish();
    }


    //连接数据库登录
    private void login1(){

        String url = this.getResources().getString(R.string.myip)+"/login1";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", SessionManager.instance.getUsername(Auto.this))
                .addParams("password", SessionManager.instance.getPassword(Auto.this))
                .build()
                .execute(new login1CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Trans_login1", "onError: 连接错误");
                        Toast.makeText(Auto.this,"自动登录失败，手动登录",Toast.LENGTH_SHORT).show();
                        toLogin();
                    }

                    @Override
                    public void onResponse(UserDocterinfo response, int id) {

                        if(response.getUser().getId()==0){

                            toLogin();
                        }
                        else if (SessionManager.instance.isBeyondTime(Auto.this)){
                            Toast.makeText(Auto.this,"距离上次登陆时间过长，请重新登陆",Toast.LENGTH_SHORT).show();
                            toLogin();
                        }
                        else {
                        MainActivity.user = response.getUser();
                        MainActivity.docterinfo = response.getDocterinfo();
                        login2();
                        }
                    }
                });


    }

    private void login2() {
        String url = this.getResources().getString(R.string.myip)+"/login2";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new login2CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Trans_login2", "onError: 连接错误");
                        Toast.makeText(Auto.this,"自动登录失败，手动登录",Toast.LENGTH_SHORT).show();
                        toLogin();
                    }

                    @Override
                    public void onResponse(Users response, int id) {

                        MainActivity.allusers = response;

                        connectIM(MainActivity.user.getToken());

                        userinfo();

                        SessionManager.instance.saveSession(Auto.this,SessionManager.instance.getUsername(Auto.this),
                                SessionManager.instance.getPassword(Auto.this));
                        Intent intent = new Intent(Auto.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    private void userinfo() {

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {

                name_head_url name_head_url = IMreg.getname_head_url(MainActivity.allusers,Integer.parseInt(userId));
                UserInfo userInfo = new UserInfo(userId, name_head_url.getName(), Uri.parse(name_head_url.getHead_url()));
                return userInfo;
            }

        }, true);
    }


    private void connectIM(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("trans_connectIM", "onSuccess: ");
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                Log.d("trans_connectIM", "onError: ");
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                Log.d("trans_connectIM", "onDatabaseOpened: ");
            }

        });
    }




}