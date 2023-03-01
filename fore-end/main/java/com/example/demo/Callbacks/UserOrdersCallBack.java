package com.example.demo.Callbacks;

import com.example.demo.models.Docters;
import com.example.demo.models.UserOrders;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class UserOrdersCallBack extends Callback<UserOrders> {

    @Override
    public UserOrders parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        UserOrders userOrders = new Gson().fromJson(string, UserOrders.class);
        return userOrders;
    }


}
