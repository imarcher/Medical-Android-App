package com.example.demo.Callbacks;

import com.example.demo.models.UserHistory;
import com.example.demo.models.UserHistorys;
import com.example.demo.models.UserOrders;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class UserHistoryCallBack  extends Callback<UserHistorys> {

    @Override
    public UserHistorys parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        UserHistorys userHistory = new Gson().fromJson(string, UserHistorys.class);
        return userHistory;
    }
}
