package com.example.demo.Callbacks;

import com.example.demo.models.Success;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 以下接口使用该CallBack
 * register2 注册step2
 * changepassword 修改密码
 * changehead_url 改头像
 * bedocter 内部人员调用的，程序中不会出现
 * changeisavailable 医生改变是否在线状态
 * addOne_Order 添加一个订单
 * apply_for_friend 申请添加好友
 * handle_friendapply 对好友申请操作，opt为“1”为同意
 * delfriend 删除一个好友
 * postapost2 发布一个帖子step2，添加图片
 * post_an_answer2 发布一个回答step2,图片
 * like_a_post 收藏一个帖子
 * dellikedpost 取消收藏的一个帖子
 * del_all_my_sawposts 清空我浏览的历史记录
 */
public abstract class isSuccessCallBack extends Callback<Success> {

    @Override
    public Success parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Success success = new Gson().fromJson(string, Success.class);
        return success;
    }


}
