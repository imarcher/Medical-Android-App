package com.example.demo.method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.demo.Callbacks.AnswerCallBack;
import com.example.demo.Callbacks.PicTokenCallBack;
import com.example.demo.Callbacks.PostsCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.models.Answer;
import com.example.demo.models.PicToken;
import com.example.demo.models.Posts;
import com.example.demo.models.Success;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import okhttp3.Call;

import static com.example.demo.MainActivity.user;

/**
 * 该类的各方法可根据需要放到ui里
 */
public class Testmethod_post_an_answer {
    /**
     * 回复帖子step1
     * @param context
     * @param content 帖子的内容
     */
    public static void post_an_answer1(Context context, String content, Then t){
        String url = context.getResources().getString(R.string.myip) + "/post_an_answer1";
        Log.d("post_an_answer1", "enter test");
        Log.d("post_an_answer1",Integer.toString(MainActivity.backPost.getHead().getPost().getId()));
        if(MainActivity.backPost == null)
            Log.d("post_an_answer1", "backPost is null");
        if(MainActivity.backPost.getHead() == null)
            Log.d("post_an_answer1", "getHead is null");
        if(MainActivity.backPost.getHead().getPost() == null)
            Log.d("post_an_answer1", "getPost is null");

        int post_id = MainActivity.backPost.getHead().getPost().getId();
        Log.d("post_an_answer1",  String.valueOf(user.getId()) + "  " + content + "  " + Integer.toString(post_id) );

        OkHttpUtils
                .post()
                .url(url)
                .addParams("author_id", String.valueOf(user.getId()))
                .addParams("content", content)
                .addParams("post_id", String.valueOf(post_id))
                .build()
                .execute(new AnswerCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("post_an_answer1", "onError: " + e.getMessage());

                        Log.d("post_an_answer1",  String.valueOf(user.getId()) + "  " + content + "  " + Integer.toString(post_id) );
                    }

                    @Override
                    public void onResponse(Answer response, int id) {
                        Log.d("post_an_answer1", "onSuccess: ");
                        MainActivity.answer = response;
                        t.then();
                    }
                });
    }

    private String path;
    private UploadManager uploadManager;
    private Configuration config;
    /**
     * 图片初始化
     */
    private void initPic() {
        config = new Configuration.Builder()
                // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        uploadManager = new UploadManager(config);

    }

    /**
     * 添加图片step1
     * 开启图片选择
     */
    private void getpicture(Activity activity) {
        PictureSelector
                .create(activity, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }

    /**
     * 选择图片时会使用，到时候移到ui里
     * 添加图片step2
     * @param requestCode
     * @param resultCode
     * @param data
     */
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                System.out.println(pictureBean.getUri());
                System.out.println(pictureBean.getPath());
                path = pictureBean.getPath();//图片在机器上的地址
                shangchuan(xxx.this);
            }
        }
    }
    */

    /**
     * 添加图片step3
     * 获得图片文件，制造图片url的key
     */
    private void shangchuan(Context context, Then t) {
        File data = new File(path);
        String key = String.valueOf(user.getId())+"/"+
                String.valueOf(MainActivity.backPost.getHead().getPost().getId())+ "/" +
                data.getName();
        gettoken(context, data, key, t);

    }

    /**
     * 添加图片step4
     * 获得token,并上传给七牛云服务器获得url
     */
    private void gettoken(Context context, File data, String key, Then t) {

        String url = context.getResources().getString(R.string.myip)+"/getpicturetoken";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("key",key)
                .build()
                .execute(new PicTokenCallBack(){
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("gettoken", "onError: 传错了");
                    }

                    @Override
                    public void onResponse(PicToken response, int id) {
                        Log.d("gettoken", "onSuccess:" + response.getToken());
                        String token = response.getToken();
                        uploadManager.put(data, key, token,
                                (key1, info, res) -> {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");

                                        post_an_answer2(context, key, t);
                                    } else {
                                        Log.i("qiniu", "Upload Fail");
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    }
                                    Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);

                                }, null);
                    }
                });
    }

    /**
     * 发布一个回答step2，添加图片step5，可能要反复调用
     */
    public static void post_an_answer2(Context context, String key, Then t){

        String url = context.getResources().getString(R.string.myip) + "/post_an_answer2";
        String pic_url =context.getResources().getString(R.string.qiniuyunip)+key;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .addParams("pic_url", pic_url)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("post_an_answer2", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("post_an_answer2", "onSuccess: ");
                        if(response.getSuccess() == 1){
                            Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
                            t.then();
                        }
                    }
                });
    }

    public static void post_a_comment(Context context, int answer_id, Then t){

    }
}
