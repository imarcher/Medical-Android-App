package com.example.demo.Callbacks;

import com.example.demo.models.Answers;
import com.example.demo.models.BackPost;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class BackPostCallBack  extends Callback<BackPost> {

    @Override
    public BackPost parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        BackPost backPost = new Gson().fromJson(string, BackPost.class);
        return backPost;
    }
}
