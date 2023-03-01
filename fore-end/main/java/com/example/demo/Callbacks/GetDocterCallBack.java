package com.example.demo.Callbacks;

import com.example.demo.models.Docters;
import com.example.demo.models.Success;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class GetDocterCallBack extends Callback<Docters> {

    @Override
    public Docters parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Docters docters = new Gson().fromJson(string, Docters.class);
        return docters;
    }


}
