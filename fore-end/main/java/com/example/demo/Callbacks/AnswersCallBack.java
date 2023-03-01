package com.example.demo.Callbacks;

import com.example.demo.models.Answers;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class AnswersCallBack extends Callback<Answers> {

    @Override
    public Answers parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Answers answers = new Gson().fromJson(string, Answers.class);
        return answers;
    }
}
