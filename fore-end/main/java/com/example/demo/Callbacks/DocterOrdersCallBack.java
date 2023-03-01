package com.example.demo.Callbacks;

import com.example.demo.models.DocterOrders;
import com.example.demo.models.UserOrders;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

public abstract class DocterOrdersCallBack extends Callback<DocterOrders> {

    @Override
    public DocterOrders parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        DocterOrders docterOrders = new Gson().fromJson(string, DocterOrders.class);
        return docterOrders;
    }


}
