package com.example.demo.Callbacks;

import com.example.demo.models.PicToken;
import com.example.demo.models.rongyunToken;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract  class PicTokenCallBack extends Callback<PicToken> {

    @Override
    public PicToken parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        PicToken pictoken = new Gson().fromJson(string, PicToken.class);
        return pictoken;
    }


}
