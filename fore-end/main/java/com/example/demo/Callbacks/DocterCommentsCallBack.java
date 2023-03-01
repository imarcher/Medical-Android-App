package com.example.demo.Callbacks;


import com.example.demo.models.DocterComments;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class DocterCommentsCallBack extends Callback<DocterComments> {

    @Override
    public DocterComments parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        DocterComments comment = new Gson().fromJson(string, DocterComments.class);
        return comment;
    }
}
