package com.example.demo.Callbacks;


import com.example.demo.models.rongyunToken;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class rongyunTokenCallBack extends Callback<rongyunToken> {

    @Override
    public rongyunToken parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        rongyunToken rongyuntoken = new Gson().fromJson(string, rongyunToken.class);
        return rongyuntoken;
    }


}