package com.example.demo.ui.login;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.Callbacks.UserCallBack;
import com.example.demo.Callbacks.rongyunTokenCallBack;
import com.example.demo.R;
import com.example.demo.method.IMreg;
import com.example.demo.models.Success;
import com.example.demo.models.User;
import com.example.demo.models.rongyunToken;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Calendar;

import okhttp3.Call;

public class RegisterFragment extends Fragment {

    private Button but;
    private String name,pass;
    EditText reg_name,reg_pass,def_pass;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        reg_name = (EditText) view.findViewById(R.id.register_name);
        reg_pass = (EditText) view.findViewById(R.id.register_pass);
        def_pass = (EditText) view.findViewById(R.id.define_pass);
        but = (Button) view.findViewById(R.id.register);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = reg_name.getText().toString();
                pass = reg_pass.getText().toString();
                if(!pass.equals(def_pass.getText().toString())){
                    Toast.makeText(getActivity(),"密码输入不一致",Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(getActivity(),"点击了注册,用户名："+name,Toast.LENGTH_SHORT).show();
                reg1();
            }
        });
        return view;
    }


    private void toLogin(){
        Intent intent = new Intent(getActivity(), Login_Register.class);
        startActivity(intent);
    }
    private void reg1() {
        String url = this.getResources().getString(R.string.myip) +"/register1";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", name)
                .addParams("password", pass)
                .build()
                .execute(new UserCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                            Log.d("Register_reg1", "onError: ");
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.d("Register_reg1", "onSuccess: ");
                        if(response.getId()==0){
                            //被注册过了
                            //需要写
                            Toast.makeText(getActivity(),"该用户名已经被注册过了",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
                            getRongyun_token(response);
                        }

                    }
                });

    }

    private void getRongyun_token(User user) {

        String nonce = IMreg.getrandom();
        String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String signature = IMreg.sha1(this.getResources().getString(R.string.rongyunApp_Secret)+nonce+timestamp);

        OkHttpUtils
                .post()
                .url(this.getResources().getString(R.string.rongyunip))
                .addHeader("App-Key",this.getResources().getString(R.string.rongyunApp_key))//添加head
                .addHeader("Nonce",nonce)
                .addHeader("Timestamp",timestamp)
                .addHeader("Signature",signature)
                .addParams("userId", String.valueOf(user.getId()))
                .addParams("name",user.getUsername())
                .addParams("portraitUri",user.getHead_url())
                .build()
                .execute(new rongyunTokenCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Register_gettoken", "onError: 连接错误");
                        Log.d("Register_gettoken", "onError: "+call);
                        Log.d("Register_gettoken", "onError: "+e);
                    }

                    @Override
                    public void onResponse(rongyunToken response, int id) {
                        Log.d("Register_gettoken", "onSuccess: ");
                        String Token = response.getToken();

                        reg2(user.getId(),Token);
                    }
                });
    }

    private void reg2(int id,String token) {

        String url = this.getResources().getString(R.string.myip) +"/register2";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("token", token)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Register_reg2", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("Register_reg2", "onSuccess: ");
                        //这里可以写注册成功的toast
                        Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
                        toLogin();
                        getActivity().finish();
                    }
                });

    }
}