package com.example.demo.Callbacks;


import com.example.demo.models.User;
import com.example.demo.models.UserDocterinfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class login1CallBack extends Callback<UserDocterinfo> {


    @Override
    public UserDocterinfo parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        UserDocterinfo userDocterinfo = new Gson().fromJson(string, UserDocterinfo.class);
        return userDocterinfo;
    }


}
