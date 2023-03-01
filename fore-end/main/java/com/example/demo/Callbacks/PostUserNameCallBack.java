package com.example.demo.Callbacks;

import com.example.demo.models.PostUserName;
import com.example.demo.models.Posts;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class PostUserNameCallBack extends Callback<PostUserName> {

    @Override
    public PostUserName parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        PostUserName posts = new Gson().fromJson(string, PostUserName.class);
        return posts;
    }
}
