package com.example.demo.ui.head;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Callbacks.PicTokenCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.R;
import com.example.demo.manager.SessionManager;
import com.example.demo.models.PicToken;
import com.example.demo.models.Success;
import com.example.demo.ui.login.Login_Register;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import okhttp3.Call;

import static com.example.demo.MainActivity.user;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    private TextView touxiang,nicheng,pass;
    private Button zhuxiao,ok_nicheng,ok_pass;
    private EditText in_nicheng,in_old_pass,in_new_pass,in_define_pass;
    private Dialog dialog;
    private Configuration config;
    private UploadManager uploadManager;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        touxiang = (TextView)findViewById(R.id.set_touxing);
        nicheng =(TextView)findViewById(R.id.set_nicheng);
        pass = (TextView)findViewById(R.id.set_pass);
        zhuxiao = findViewById(R.id.zhuxiao);
        touxiang.setOnClickListener(this);
        nicheng.setOnClickListener(this);
        pass.setOnClickListener(this);
        zhuxiao.setOnClickListener(this);

        initPic();
    }


    /**
     * 选择图片时会使用
     * 改头像step2
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                System.out.println(pictureBean.getUri());
                System.out.println(pictureBean.getPath());
                path = pictureBean.getPath();//图片在机器上的地址
                shangchuang();

            }
        }
    }

    /**
     *  ----修改昵称----可以不实现
     *
     * @param name 新的昵称
     *
     */
    private void setnicheng(String name){

        String url = this.getResources().getString(R.string.myip) + "/changeusername";
        Log.d("setnicheng", " " + url);
        OkHttpUtils
                .post()
                .addParams("id", String.valueOf(user.getId()))
                .addParams("username", name)
                .url(url)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("setnicheng", "onError: 连接错误" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        if (response.getSuccess() == 0) {
                            Toast.makeText(Setting.this, "修改失败，该用户名已存在", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String pass = SessionManager.instance.getPassword(Setting.this);

                            SessionManager.instance.saveSession(Setting.this, name, pass);
                            user.setUsername(name);
                            Toast.makeText(Setting.this, "修改成功", Toast.LENGTH_SHORT).show();

                            dialog.cancel();//修改成功，则关闭dialog
                        }
                    }
                });

    }

    /**
     *   ----修改密码----
     * @param old_pass 旧的密码
     * @param new_pass 新的密码
     */
    private void changepass(String old_pass,String new_pass){

        if(old_pass.equals(SessionManager.instance.getPassword(Setting.this))) {

            String url = this.getResources().getString(R.string.myip) + "/changepassword";
            OkHttpUtils
                    .post()
                    .addParams("id", String.valueOf(user.getId()))
                    .addParams("password", new_pass)
                    .url(url)
                    .build()
                    .execute(new isSuccessCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("changepass", "onError: 连接错误");
                        }

                        @Override
                        public void onResponse(Success response, int id) {
                            String name = SessionManager.instance.getUsername(Setting.this);
                            Log.d("changepass", name + "   " + new_pass);
                            SessionManager.instance.saveSession(Setting.this, name, new_pass);

                            Toast.makeText(Setting.this, "修改成功", Toast.LENGTH_SHORT).show();

                            dialog.cancel();//修改成功，则关闭dialog
                        }
                    });

        }

        else Toast.makeText(this,"原密码错误",Toast.LENGTH_LONG).show();

    }

    /**
     *   ----退出登录----
     */
    private void zhuxiao(){
        SessionManager.instance.clearSession (Setting.this);
        Toast.makeText(Setting.this, "退出成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Setting.this, Login_Register.class);
        startActivity(intent);
        Log.d("test_zhuxiao","注销成功");
        finish();
    }


    /**
     *   ----修改头像----
     */
    private void changetouxiang(){

            getpicture();
    }


    /**
     * 改头像step1
     * 开启图片选择
     */
    private void getpicture() {
        PictureSelector
                .create(Setting.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }


    /**
     * 改头像step3
     * 获得图片文件，制造图片url的key
     */
    private void shangchuang() {
        File data = new File(path);
        String key = String.valueOf(user.getId())+"/"+data.getName();
        gettoken(data, key);

    }
    /**
     * 改头像step4
     * 获得token,并上传给七牛云服务器获得url
     */
    private void gettoken(File data, String key) {

        String url = this.getResources().getString(R.string.myip)+"/getpicturetoken";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("key",key)
                .build()
                .execute(new PicTokenCallBack(){
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("gettoken", "onError: 传错了");
                    }

                    @Override
                    public void onResponse(PicToken response, int id) {
                        Log.d("gettoken", "onSuccess:" + response.getToken());
                        String token = response.getToken();
                        uploadManager.put(data, key, token,
                                (key1, info, res) -> {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");

                                        changehead_url(key);


                                    } else {
                                        Log.i("qiniu", "Upload Fail");
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    }
                                    Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);

                                }, null);
                    }
                });



    }
    /**
     * 改头像step5
     * 更改head_url
     */
    private void changehead_url(String key) {

        String url = this.getResources().getString(R.string.myip)+"/changehead_url";
        String head_url =this.getResources().getString(R.string.qiniuyunip)+key;

        OkHttpUtils
                .post()
                .addParams("id", String.valueOf(user.getId()))
                .addParams("head_url", head_url)
                .url(url)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Setting_head", "onError: 连接错误");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Toast.makeText(Setting.this, "修改成功", Toast.LENGTH_LONG).show();
                        user.setHead_url(head_url);
                        Log.d("Setting_head", "onSuccess: 连接错误");

                    }
                });

    }


    /**
     * 图片初始化
     */
    private void initPic() {
        config = new Configuration.Builder()
                // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        uploadManager = new UploadManager(config);

    }



    @Override
    public void onClick(View view) {
        Log.d("test_zhuxiao","点击设置");
        switch (view.getId()){
            case R.id.set_touxing:
                changetouxiang();
                break;
            case R.id.set_nicheng:
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                View v = LayoutInflater.from(Setting.this).inflate(R.layout.set_nicheng,null);

                in_nicheng = (EditText) v.findViewById(R.id.in_nicheng);
                ok_nicheng = (Button)v.findViewById(R.id.in_queding);
                in_nicheng.setHint(user.getUsername());
                ok_nicheng.setOnClickListener(Setting.this);

                dialog = builder.setTitle("设置昵称").setView(v).show();
                break;
            case R.id.set_pass:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Setting.this);
                View v1 = LayoutInflater.from(Setting.this).inflate(R.layout.set_pass,null);

                in_old_pass = (EditText) v1.findViewById(R.id.in_old_pass);
                in_new_pass = (EditText) v1.findViewById(R.id.in_new_pass);
                in_define_pass = (EditText) v1.findViewById(R.id.in_define_pass);
                ok_pass = (Button)v1.findViewById(R.id.in_queding_pass);
                ok_pass.setOnClickListener(Setting.this);

                dialog = builder1.setTitle("修改密码").setView(v1).show();
                break;
            case R.id.zhuxiao:
                zhuxiao();
                Log.d("test_zhuxiao","点击注销");
                break;
            case R.id.in_queding:
                Toast.makeText(Setting.this,"尝试修改昵称",Toast.LENGTH_SHORT).show();
                String n = in_nicheng.getText().toString();
                setnicheng(n);
                break;
            case R.id.in_queding_pass:
                String s = in_old_pass.getText().toString();
                String s1 = in_new_pass.getText().toString();
                if(!s1.equals(in_define_pass.getText().toString())){
                    Toast.makeText(Setting.this,"密码输入不一致",Toast.LENGTH_SHORT).show();
                }
                else
                changepass(s,s1);
                break;
            default:;
        }
    }


}