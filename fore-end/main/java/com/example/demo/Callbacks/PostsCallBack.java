package com.example.demo.Callbacks;

import com.example.demo.models.Posts;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class PostsCallBack extends Callback<Posts> {

    @Override
    public Posts parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Posts posts = new Gson().fromJson(string, Posts.class);
        return posts;
    }
}
