package com.example.demo.method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.Callbacks.AnswersCallBack;
import com.example.demo.Callbacks.BackPostCallBack;
import com.example.demo.Callbacks.CommentCallBack;
import com.example.demo.Callbacks.PicTokenCallBack;
import com.example.demo.Callbacks.PostCallBack;
import com.example.demo.Callbacks.PostUserNameCallBack;
import com.example.demo.Callbacks.PostsCallBack;
import com.example.demo.Callbacks.UserCallBack;
import com.example.demo.Callbacks.UserHistoryCallBack;
import com.example.demo.Callbacks.UsersCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.models.Answers;
import com.example.demo.models.BackPost;
import com.example.demo.models.Comment;
import com.example.demo.models.PicToken;
import com.example.demo.models.Post;
import com.example.demo.models.PostUserName;
import com.example.demo.models.Posts;
import com.example.demo.models.Success;
import com.example.demo.models.User;
import com.example.demo.models.UserHistory;
import com.example.demo.models.UserHistorys;
import com.example.demo.models.Users;
import com.example.demo.ui.head.Setting;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import okhttp3.Call;

import static com.example.demo.MainActivity.user;

public class Testmethod_bbs {
    /**
     * 获得帖子列表，排序在客户端进行
     * @param context
     * @param filter 搜索帖子的字符串(标题或帖子的内容)
     */
    public static void getposts(Context context, String filter, Then t){

        String url = context.getResources().getString(R.string.myip) + "/getposts";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("filter", filter)
                .build()
                .execute(new PostsCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getposts", "onError: ");
                    }

                    @Override
                    public void onResponse(Posts response, int id) {
                        Toast.makeText(context, "获取成功", Toast.LENGTH_LONG).show();
                        Log.d("getposts", "onSuccess: ");
                        MainActivity.posts = response;
                        t.then();
                    }
                });
    }

    /**
     * 发布一个评论
     * @param context
     * @param content 评论的内容
     */
    public static void post_a_comment(Context context, String content, int answer_id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/post_a_comment";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("author_id", String.valueOf(user.getId()))
                .addParams("content", content)
                .addParams("answer_id", String.valueOf(answer_id))
                .build()
                .execute(new CommentCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("post_a_comment", "onError: ");
                    }

                    @Override
                    public void onResponse(Comment response, int id) {
                        Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                        Log.d("post_a_comment", "onSuccess: ");
                        MainActivity.comment = response;
                        t.then();
                    }
                });
    }

    /**
     * 获取我发布的帖子
     * @param context
     */
    public static void get_my_post(Context context,int id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/get_my_post";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new PostsCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("get_my_post", "onError: ");
                    }

                    @Override
                    public void onResponse(Posts response, int id) {
                        Log.d("get_my_post", "onSuccess: "+id+" "+response.getPosts().size());
                        Toast.makeText(context, "获取成功", Toast.LENGTH_LONG).show();
                        MainActivity.myPosts = response;
                        t.then();
                    }
                });
    }

    /**
     * 获取一个人发布的回答
     * @param context
     */
    public static void get_my_answer(Context context,int id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/get_my_answer";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new AnswersCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("get_my_answer", "onError: ");
                    }

                    @Override
                    public void onResponse(Answers response, int id) {
                        Log.d("get_my_answer", "onSuccess: ");
                        Toast.makeText(context, "获取成功", Toast.LENGTH_LONG).show();
                        MainActivity.answers = response;
                        t.then();
                    }
                });
    }


    /**
     * 收藏一个帖子
     * @param context
     */
    public static void like_a_post(Context context, Then t){
        String url = context.getResources().getString(R.string.myip) + "/like_a_post";
        int post_id = MainActivity.backPost.getHead().getPost().getId();
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("like_a_post", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("like_a_post", "onSuccess: ");
                        if(response.getSuccess() == 1){
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();
                            t.then();
                        }
                    }
                });
    }

    /**
     * 取消收藏的一个帖子
     * @param context
     */
    public static void dellikedpost(Context context,int post_id ,Then t){
        String url = context.getResources().getString(R.string.myip) + "/dellikedpost";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("dellikedpost", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("dellikedpost", "onSuccess: ");
                        if(response.getSuccess() == 1){
                            Toast.makeText(context, "取消收藏成功", Toast.LENGTH_LONG).show();
                            t.then();
                        }
                    }
                });
    }

    /**
     * 获取我收藏的帖子
     * @param context
     */
    public static void get_my_likedpost(Context context, Then t){
        String url = context.getResources().getString(R.string.myip) + "/get_my_likedpost";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .build()
                .execute(new PostsCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("get_my_likedpost", "onError: ");
                    }

                    @Override
                    public void onResponse(Posts response, int id) {
                        Log.d("get_my_likedpost", "onSuccess: ");
                        MainActivity.myCollectPosts = response;
                        Toast.makeText(context, "获取收藏成功", Toast.LENGTH_LONG).show();
                        t.then();
                    }
                });
    }

    /**
     * 打开一个帖子
     * @param context
     * @param post_id 从获取帖子列表里(MainActivity.posts)中获取打开的post的id
     */
    public static void open_a_post(Context context, int post_id, Then t){
        String url = context.getResources().getString(R.string.myip) + "/open_a_post";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new BackPostCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("open_a_post", "onError: ");
                    }

                    @Override
                    public void onResponse(BackPost response, int id) {
                        Log.d("open_a_post", "onSuccess: ");
                        Toast.makeText(context, "打开成功", Toast.LENGTH_LONG).show();
                        MainActivity.backPost = response;
                        t.then();
                    }
                });
    }

    /**
     * 获取我浏览的历史记录
     * @param context
     */
    public static void get_my_sawposts(Context context, Then t) {
        String url = context.getResources().getString(R.string.myip) + "/get_my_sawposts";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .build()
                .execute(new UserHistoryCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("get_my_sawposts", "onError: ");
                    }

                    @Override
                    public void onResponse(UserHistorys response, int id) {
                        Log.d("get_my_sawposts", "onSuccess: ");
                        Toast.makeText(context, "获取成功", Toast.LENGTH_LONG).show();
                        MainActivity.userHistory = response;
                        t.then();
                    }
                });
    }

    /**
     * 清空我浏览的历史记录
     * @param context
     */
    public static void del_all_my_sawposts(Context context, Then t) {
        String url = context.getResources().getString(R.string.myip) + "/del_all_my_sawposts";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("del_all_my_sawposts", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("del_all_my_sawposts", "onSuccess: ");
                        if(response.getSuccess() == 1){
                            Toast.makeText(context, "清空成功", Toast.LENGTH_LONG).show();
                            t.then();
                        }
                    }
                });
    }

    /**
     * 获取一个帖子的作者
     * @param context
     */
    public static void find_post_user(Context context,int post_id, Then t) {
        String url = context.getResources().getString(R.string.myip) + "/find_post_user";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new UserCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("find_post_username", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        Log.d("find_post_username", "onSuccess: ");
                        MainActivity.postUser = response;
                        t.then();
                    }
                });
    }
    /**
     * 获取一个帖子的主要信息
     * @param context
     */
    public static void find_post_info(Context context,int post_id, Then t) {
        String url = context.getResources().getString(R.string.myip) + "/find_post_info";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new PostCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("find_post_info", "onError: ");
                    }

                    @Override
                    public void onResponse(Post response, int id) {
                        Log.d("find_post_info", "onSuccess: ");

                        MainActivity.apost = response;
                        t.then();
                    }
                });
    }

}
