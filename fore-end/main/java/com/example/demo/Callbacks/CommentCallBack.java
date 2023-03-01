package com.example.demo.Callbacks;

import com.example.demo.models.Comment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class CommentCallBack extends Callback<Comment> {

    @Override
    public Comment parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Comment comment = new Gson().fromJson(string, Comment.class);
        return comment;
    }
}
