package com.example.demo.Callbacks;

import com.example.demo.models.Answer;
import com.example.demo.models.Post;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class AnswerCallBack extends Callback<Answer> {

    @Override
    public Answer parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Answer answer = new Gson().fromJson(string, Answer.class);
        return answer;
    }
}
