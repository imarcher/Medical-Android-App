package com.example.demo.method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.demo.Callbacks.DocterCommentsCallBack;
import com.example.demo.Callbacks.DocterOrdersCallBack;
import com.example.demo.Callbacks.GetDocterCallBack;
import com.example.demo.Callbacks.UserOrdersCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.Callbacks.login1CallBack;
import com.example.demo.Callbacks.UsersCallBack;
import com.example.demo.Callbacks.UserCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.models.DocterComments;
import com.example.demo.models.DocterOrders;
import com.example.demo.models.Docters;
import com.example.demo.models.Success;
import com.example.demo.models.User;
import com.example.demo.models.UserDocterinfo;
import com.example.demo.models.UserOrders;
import com.example.demo.models.Users;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

public class Testmethod {

    /**
     * 医生改变状态
     * @param context
     */
    public static void changeisavailable(Context context, Then t){

        String url = context.getResources().getString(R.string.myip) +"/changeisavailable";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(MainActivity.docterinfo.getId()))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("changeisavailable", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("changeisavailable", "onSuccess: ");
                        if(MainActivity.docterinfo.isIsavailable())
                            MainActivity.docterinfo.setIsavailable(false);
                        else MainActivity.docterinfo.setIsavailable(true);
                        Toast.makeText(context,"更改状态成功",Toast.LENGTH_LONG).show();

                        t.then();
                    }
                });


    }


    /**
     * 获取指定的医生信息
     * @param context
     * @param department
     * @param filter
     */
    public static void getdocters(Context context,String department,String filter, Then t){

        String url = context.getResources().getString(R.string.myip) + "/getdocters";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("department", department)
                .addParams("filter", filter)
                .build()
                .execute(new GetDocterCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getdocters", "onError: ");
                    }

                    @Override
                    public void onResponse(Docters response, int id) {
                        Log.d("getdocters", "onSuccess: ");

                        MainActivity.docters = response;

                        t.then();
                    }
                });
    }


    /**
     * 添加一个订单
     * @param context
     * @param patient_id
     * @param docter_id
     */
    public static void addOne_Order(Context context,int patient_id,int docter_id, Then t){


        String url = context.getResources().getString(R.string.myip) + "/addOne_Order";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("patient_id", String.valueOf(patient_id))
                .addParams("docter_id", String.valueOf(docter_id))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("addOne_Order", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("addOne_Order", "onSuccess: ");
                        Toast.makeText(context,"添加订单成功",Toast.LENGTH_LONG).show();
                        t.then();
                    }
                });
    }


    /**
     * 病人打开自己的订单
     * @param context
     */
    public static void getmydocterorders(Context context, Then t){
        String url = context.getResources().getString(R.string.myip) + "/getmydocterorders";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(MainActivity.user.getId()))
                .build()
                .execute(new UserOrdersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getmydocterorders", "onError: ");
                    }

                    @Override
                    public void onResponse(UserOrders response, int id) {
                        Log.d("getmydocterorders", "onSuccess: ");
                        MainActivity.userOrders = response;
                        t.then();
                    }
                });
    }

    /**
     * 医生打开自己的订单
     * @param context
     */
    public static void mypatientorders(Context context, Then t){
        String url = context.getResources().getString(R.string.myip) + "/mypatientorders";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(MainActivity.docterinfo.getId()))
                .build()
                .execute(new DocterOrdersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("mypatientorders", "onError: ");
                    }



                    @Override
                    public void onResponse(DocterOrders response, int id) {
                        Log.d("mypatientorders", "onSuccess: ");
                        MainActivity.docterOrders = response;
                        t.then();
                    }
                });


    }

    /**
     * 病人点击订单获得医生的user信息，然后可以对话
     * @param context
     * @param id 医生的docterinfo id
     */
    public static void getuserfromdoc(Context context,int id, Then t){


        String url = context.getResources().getString(R.string.myip) + "/getuserfromdoc";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new UserCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getuserfromdoc", "onError: ");
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.d("getuserfromdoc", "onSuccess: ");

                        MainActivity.your_order_docter_userinfo = response;

                        t.then();
                    }
                });




    }

    /**
     * 通过id获得这个人的信息,实现个人主页
     * @param context
     * @param id user id
     */
    public static void getuserpage(Context context,int id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/getuserpage";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new login1CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getuserpage", "onError: ");
                    }

                    @Override
                    public void onResponse(UserDocterinfo response, int id) {
                        Log.d("getuserpage", "onSuccess: ");
                        MainActivity.userpage = response;
                        t.then();
                    }
                });

    }

    /**
     * 通过id获得这个人的信息,实现帖子列表
     * @param context
     * @param id user id
     */
    public static void getuserpage2(Context context,int id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/getuserpage";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new login1CallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getuserpage", "onError: ");
                    }

                    @Override
                    public void onResponse(UserDocterinfo response, int id) {
                        Log.d("getuserpage", "onSuccess: ");
                        MainActivity.userpage2 = response;
                        t.then();
                    }
                });

    }


    /**
     * 申请添加好友
     * @param context
     * @param passive_id
     */
    public static void apply_for_friend(Context context,int passive_id, Then t){

        String url = context.getResources().getString(R.string.myip) + "/apply_for_friend";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("passive_id", String.valueOf(passive_id))
                .addParams("positive_id", String.valueOf(MainActivity.user.getId()))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("apply_for_friend", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("apply_for_friend", "onSuccess: ");
                        t.then();
                    }
                });

    }


    /**
     * 对好友申请操作，opt为“1”为同意
     * @param context
     * @param positive_id
     * @param opt
     */
    public static void handle_friendapply(Context context,int positive_id,String opt, Then t){
        String url = context.getResources().getString(R.string.myip) + "/handle_friendapply";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("passive_id", String.valueOf(MainActivity.user.getId()))
                .addParams("positive_id", String.valueOf(positive_id))
                .addParams("opt",opt)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("handle_friendapply", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("handle_friendapply", "onSuccess: ");
                        t.then();
                    }
                });
    }


    /**
     * 对我的好友申请列表
     * @param context
     */
    public static void friend_apply_list(Context context, Then t){

        String url = context.getResources().getString(R.string.myip) + "/friend_apply_list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("passive_id", String.valueOf(MainActivity.user.getId()))
                .build()
                .execute(new UsersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("friend_apply_list", "onError: ");
                    }

                    @Override
                    public void onResponse(Users response, int id) {
                        Log.d("friend_apply_list", "onSuccess: ");
                        MainActivity.my_friend_apply = response;
                        t.then();
                    }
                });


    }


    /**
     * 我的好友列表
     * @param context
     */
    public static void friend_list(Context context, Then t){

        String url = context.getResources().getString(R.string.myip) + "/friend_list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(MainActivity.user.getId()))
                .build()
                        .execute(new UsersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("friend_list", "onError: ");
                    }

                    @Override
                    public void onResponse(Users response, int id) {
                        Log.d("friend_list", "onSuccess: ");
                        MainActivity.friend_list = response;
                        t.then();
                    }
                });
    }


    /**
     * 删除一个好友
     * @param context
     * @param positive_id
     */
    public static void delfriend(Context context,int positive_id, Then t){

        String url = context.getResources().getString(R.string.myip) + "/delfriend";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("passive_id", String.valueOf(MainActivity.user.getId()))
                .addParams("positive_id", String.valueOf(positive_id))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("delfriend", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("delfriend", "onSuccess: ");
                        t.then();
                    }
                });
    }

    /**
     * 搜索对应用户来添加好友
     * @param context
     * @param filter
     */
    public static void search_friend_list(Context context,String filter, Then t){

        String url = context.getResources().getString(R.string.myip) + "/search_friend_list";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("filter", filter)
                .build()
                .execute(new UsersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("search_friend_list", "onError: ");
                    }

                    @Override
                    public void onResponse(Users response, int id) {
                        Log.d("search_friend_list", "onSuccess: ");
                        MainActivity.search_friend_list = response;
                        Log.d("search_friend_list", "onResponse: "+String.valueOf(response.getUsers().size()));
                        t.then();
                    }
                });
    }


    /**
     * 病人点击完成订单发布评价
     * @param context
     * @param id  订单id
     * @param content 评价内容
     *
     */
    public static void finish_order_comment(Context context,int id,String content, Then t){

        String url = context.getResources().getString(R.string.myip) + "/finish_order_comment";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("content", content)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("finish_order_comment", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("finish_order_comment", "onSuccess: ");
                        t.then();
                    }
                });
    }

    /**
     * 医生打开自己的主页评价
     * @param context
     * @param id 医生docterinfo id
     */
    public static void mypatientordercomments(Context context,int id, Then t){

        String url = context.getResources().getString(R.string.myip) + "/mypatientordercomments";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new DocterCommentsCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("friend_list", "onError: ");
                    }

                    @Override
                    public void onResponse(DocterComments response, int id) {
                        Log.d("friend_list", "onSuccess: ");

                        MainActivity.docterComments = response;
                        t.then();
                    }
                });
    }



}
