package com.example.demo.ui.login;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.demo.Auto;
import com.example.demo.Callbacks.login1CallBack;
import com.example.demo.Callbacks.login2CallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.manager.SessionManager;
import com.example.demo.method.IMreg;
import com.example.demo.models.UserDocterinfo;
import com.example.demo.models.Users;
import com.example.demo.models.name_head_url;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;

public class LoginFragment extends Fragment {
    @BindView(R.id.to_register)
    TextView toRegister;
    private TextView tv;
    private Button but;
    private String name, pass;
    EditText reg_name, reg_pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
//        tv = (TextView)view.findViewById(R.id.toregister);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), RegisterFragment.class);
//                startActivity(intent);
//            }
//        });
        ButterKnife.bind(this,view);
        toRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        toRegister.getPaint().setAntiAlias(true);
        reg_name = (EditText) view.findViewById(R.id.login_name);
        reg_pass = (EditText) view.findViewById(R.id.login_pass);
        but = (Button) view.findViewById(R.id.login);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = reg_name.getText().toString();
                pass = reg_pass.getText().toString();


            //    Toast.makeText(getActivity(), "点击了登录,用户名：" + name, Toast.LENGTH_SHORT).show();

                login1(name, pass);
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        return view;
    }


    private void toLogin() {
        reg_name.setText("");
        reg_pass.setText("");
    }

    //连接数据库登录
    private void login1(String username, String password) {

        String url = this.getResources().getString(R.string.myip) + "/login1";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(new login1CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Login_login1", "onError: 连接错误");
                    }

                    @Override
                    public void onResponse(UserDocterinfo response, int id) {

                        if (response.getUser().getId() == 0) {
                            Toast.makeText(getContext(),"登录失败，用户名或密码错误",Toast.LENGTH_SHORT).show();
                            toLogin();
                        } else {
                            MainActivity.user = response.getUser();
                            MainActivity.docterinfo = response.getDocterinfo();

                            login2();
                        }
                    }
                });


    }

    private void login2() {
        String url = this.getResources().getString(R.string.myip) + "/login2";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new login2CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("Trans_login2", "onError: 连接错误");
                    }

                    @Override
                    public void onResponse(Users response, int id) {
                        MainActivity.allusers = response;

                        connectIM(MainActivity.user.getToken());

                        userinfo();

                        SessionManager.instance.saveSession(getActivity(), name, pass);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
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
                name_head_url name_head_url = IMreg.getname_head_url(MainActivity.allusers, Integer.parseInt(userId));
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