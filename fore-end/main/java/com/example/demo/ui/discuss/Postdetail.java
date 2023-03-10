package com.example.demo.ui.discuss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.demo.Callbacks.PicTokenCallBack;
import com.example.demo.Callbacks.isSuccessCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Then;
import com.example.demo.models.BackAnswer;
import com.example.demo.models.BackComment;
import com.example.demo.models.PicToken;
import com.example.demo.models.Success;
import com.example.demo.ui.head.UserInfo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.user;
import static com.example.demo.method.Testmethod_bbs.like_a_post;
import static com.example.demo.method.Testmethod_bbs.open_a_post;
import static com.example.demo.method.Testmethod_bbs.post_a_comment;
import static com.example.demo.method.Testmethod_post_an_answer.post_an_answer1;

public class Postdetail extends AppCompatActivity {


    @BindView(R.id.createtime)
    TextView createtime;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    HtmlTextView content;
    @BindView(R.id.detail_page_lv_comment)
    CommentExpandableListView detailPageLvComment;
    @BindView(R.id.detail_page_do_comment)
    TextView detailPageDoComment;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.likepost)
    ImageView likepost;
    @BindView(R.id.detail_page_image)
    ImageView detailPageImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.detail_page_userLogo)
    CircleImageView detailPageUserLogo;
    @BindView(R.id.detail_page_focus)
    ImageView detailPageFocus;
    @BindView(R.id.detail_page_above_container)
    LinearLayout detailPageAboveContainer;
    @BindView(R.id.detail_page_comment_container)
    LinearLayout detailPageCommentContainer;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.likepostnum)
    TextView likepostnum;


    private CommentExpandAdapter adapter;
    private static final String TAG = "Postdetail";
    private BottomSheetDialog dialog;
    private String path;
    private UploadManager uploadManager;
    private Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        int post_id = bundle.getInt("post");
        initPic();
        getpostinit(post_id);

    }

    private void getpostinit(int post_id) {
        open_a_post(this, post_id, new Then() {
            @Override
            public void then() {
                title.setText(MainActivity.backPost.getHead().getPost().getTitle());
                createtime.setText(MainActivity.backPost.getHead().getPost().getCreate_time());
                content.setHtmlFromString(MainActivity.backPost.getHead().getPost().getContent(), false);
                username.setText(MainActivity.backPost.getHead().getUser().getUsername());
                collapsingToolbar.setTitle(MainActivity.backPost.getHead().getPost().getTitle());
                detailPageLvComment.setGroupIndicator(null);
                Picasso.get()
                        .load(MainActivity.backPost.getHead().getUser().getHead_url())
                        .placeholder(R.drawable.touxiang)//???????????????
                        .error(R.drawable.touxiang)//?????????????????????
                        .into(detailPageUserLogo);
                //????????????????????????
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                adapter = new CommentExpandAdapter(Postdetail.this, MainActivity.backPost.getAnswers());
                detailPageLvComment.setAdapter(adapter);
                for (int i = 0; i < MainActivity.backPost.getAnswers().size(); i++) {
                    detailPageLvComment.expandGroup(i);
                }
                detailPageLvComment.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                        boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                        Log.e(TAG, "onGroupClick: ???????????????id>>>" + MainActivity.backPost.getAnswers().get(groupPosition).getAnswer().getId());

                     //   if (isExpanded) {
                 //           expandableListView.collapseGroup(groupPosition);
                   //     } else {
                  //          expandableListView.expandGroup(groupPosition, true);
                 //       }
                        showReplyDialog(groupPosition);
                        return true;
                    }
                });

                detailPageLvComment.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        Toast.makeText(Postdetail.this, "???????????????", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                detailPageLvComment.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        //toast("?????????"+groupPosition+"?????????");

                    }
                });


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * by moos on 2018/04/20
     * func:???????????????
     */
    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * ??????bsd?????????????????????
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    post_an_answer1(Postdetail.this, commentContent, new Then() {
                        @Override
                        public void then() {
                            BackAnswer answer1 = new BackAnswer();
                            answer1.setUser(user);
                            answer1.setAnswer(MainActivity.answer);
                            adapter.addanswerData(answer1);
                            Toast.makeText(Postdetail.this, "????????????", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "????????????" + answer1.getAnswer().getContent());
                        }
                    });


                } else {
                    Toast.makeText(Postdetail.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void showReplyDialog(final int position) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("?????? " + MainActivity.backPost.getAnswers().get(position).getUser().getUsername() + " ?????????:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {

                    dialog.dismiss();
                    post_a_comment(Postdetail.this, replyContent, MainActivity.backPost.getAnswers().get(position).getAnswer().getId(),new Then() {
                        @Override
                        public void then() {
                            BackComment comment1 = new BackComment();
                            comment1.setUser(user);
                            comment1.setComment(MainActivity.comment);
                            adapter.addTheReplyData(comment1, position);
                            Toast.makeText(Postdetail.this, "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Toast.makeText(Postdetail.this, "????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Postdetail.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


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
        //????????????
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                System.out.println(pictureBean.getUri());
                System.out.println(pictureBean.getPath());
                path = pictureBean.getPath();//???????????????????????????
                shangchuan(this, new Then() {
                    @Override
                    public void then() {

                    }
                });
            }
        }
    }


    /**
     * ????????????step3
     * ?????????????????????????????????url???key
     */
    private void shangchuan(Context context, Then t) {
        File data = new File(path);
        String key = String.valueOf(user.getId()) + "/" +
                String.valueOf(MainActivity.backPost.getHead().getPost().getId()) + "/" +
                data.getName();
        //key?????????id+??????id+????????????
        gettoken(context, data, key, t);

    }

    /**
     * ????????????step4
     * ??????token,????????????????????????????????????url
     */
    private void gettoken(Context context, File data, String key, Then t) {

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

                                        postapost2(context, key, t);
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
    public static void postapost2(Context context, String key, Then t) {

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
                            t.then();
                        }
                    }
                });
    }


    @OnClick({R.id.detail_page_do_comment, R.id.likepost,R.id.detail_page_focus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail_page_do_comment:
                showCommentDialog();
                break;
            case R.id.likepost:
                like_a_post(Postdetail.this, new Then() {
                    @Override
                    public void then() {

                    }
                });
                break;
            case R.id.detail_page_focus:
                Intent intent = new Intent(Postdetail.this, UserInfo.class);
                intent.putExtra("ID",MainActivity.backPost.getHead().getUser().getHead_url());
                startActivity(intent,null);
                break;
        }
    }


}