package com.example.demo.Callbacks;



import com.example.demo.models.UserDocterinfo;
import com.example.demo.models.Users;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class login2CallBack extends Callback<Users> {

    @Override
    public Users parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Users users = new Gson().fromJson(string, Users.class);
        return users;
    }
}
