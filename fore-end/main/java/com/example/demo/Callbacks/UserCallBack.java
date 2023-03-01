package com.example.demo.Callbacks;


import com.example.demo.models.User;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class UserCallBack extends Callback<User> {
    @Override
    public User parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        User user = new Gson().fromJson(string, User.class);
        return user;
    }


}
