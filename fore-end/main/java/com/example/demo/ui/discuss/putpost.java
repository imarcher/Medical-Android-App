package com.example.demo.ui.discuss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.Callbacks.PicTokenCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Then;
import com.example.demo.models.PicToken;
import com.example.demo.models.Success;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.demo.MainActivity.user;
import static com.example.demo.method.Testmethod_post_a_post.postapost1;

public class putpost extends AppCompatActivity {
    @BindView(R.id.post_title)
    EditText postTitle;
    @BindView(R.id.submit_pic)
    Button submitPic;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.post_content)
    HtmlEditText postContent;
    private String path;
    private UploadManager uploadManager;
    private Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putpost);
        ButterKnife.bind(this);
        initPic();
    }


    @OnClick({R.id.submit_pic, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_pic:
                getpicture(this);
                break;
            case R.id.submit:
                String title = postTitle.getText().toString();
                String content = postContent.getEditText().getText().toString();
                postapost1(this, title, content, new Then() {
                    @Override
                    public void then() {
                        Toast.makeText(putpost.this, "????????????", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(putpost.this, Postdetail.class);
                        intent.putExtra("post",MainActivity.post.getId());
                        startActivity(intent,null);
                        finish();
                    }
                });
                break;
        }
    }


    /**
     * ???????????????
     */
    private void initPic() {
        config = new Configuration.Builder()
                // ???????????????????????????????????????????????????????????????????????????????????????????????????IP???
                .build();
        uploadManager = new UploadManager(config);

    }

    /**
     * ????????????step1
     * ??????????????????
     */
    private void getpicture(Activity activity) {
        PictureSelector
                .create(activity, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }

    /**
     * ??????????????????????????????????????????ui???
     * ????????????step2
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*????????????*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                System.out.println(pictureBean.getUri());
                System.out.println(pictureBean.getPath());
                path = pictureBean.getPath();//???????????????????????????
                shangchuan(this);
            }
        }
    }

    /**
     * ????????????step3
     * ?????????????????????????????????url???key
     */
    private void shangchuan(Context context) {
        File data = new File(path);
        String key = String.valueOf(user.getId()) + "/" + data.getName();
        gettoken(context, data, key);

    }

    /**
     * ????????????step4
     * ??????token,????????????????????????????????????url
     */
    private void gettoken(Context context, File data, String key) {

        String url = context.getResources().getString(R.string.myip) + "/getpicturetoken";

        OkHttpUtils
                .post()
                .url(url)
                .addParams("key", key)
                .build()
                .execute(new PicTokenCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("gettoken", "onError: ?????????");
                    }

                    @Override
                    public void onResponse(PicToken response, int id) {
                        Log.d("gettoken", "onSuccess:" + response.getToken());
                        String token = response.getToken();
                        uploadManager.put(data, key, token,
                                (key1, info, res) -> {
                                    //res??????hash???key??????????????????????????????????????????????????????
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");

                                        postapost2(context, key);
                                    } else {
                                        Log.i("qiniu", "Upload Fail");
                                        //??????????????????????????????info?????????????????????????????????????????????????????????????????????
                                    }
                                    Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);

                                }, null);
                    }
                });
    }

    /**
     * ??????????????????step2???????????????step5????????????????????????
     */
    private void postapost2(Context context, String key) {

        String url = context.getResources().getString(R.string.myip) + "/postapost2";
        String pic_url = context.getResources().getString(R.string.qiniuyunip) + key;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user.getId()))
                .addParams("pic_url", pic_url)
                .build()
                .execute(new isSuccessCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("postapost2", "onError: ");
                    }

                    @Override
                    public void onResponse(Success response, int id) {
                        Log.d("postapost2", "onSuccess: ");
                        if (response.getSuccess() == 1) {
                            Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
                            HtmlFile rode=new HtmlFile(path,pic_url);
                            postContent.setUploadPath(rode);
                        }
                    }
                });
    }
}