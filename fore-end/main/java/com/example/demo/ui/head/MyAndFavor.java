package com.example.demo.ui.head;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod_bbs;
import com.example.demo.method.Then;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAndFavor extends AppCompatActivity {

    @BindView(R.id.post_back)
    ImageView postBack;
    @BindView(R.id.info_text)
    TextView infoText;
    @BindView(R.id.post_list)
    RecyclerView postList;
    @BindView(R.id.info_clear)
    TextView infoClear;
    @BindView(R.id.post_rela)
    RelativeLayout postRela;
    private String state;
    private boolean choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        state = bundle.getString("kind");
        choice = false;
        init();
    }

    private void init() {

        postList.setLayoutManager(new LinearLayoutManager(MyAndFavor.this));
        if (state.equals("tiezi")) {
            infoText.setText("我的帖子");
            Testmethod_bbs.get_my_post(MyAndFavor.this,MainActivity.user.getId(), new Then() {
                @Override
                public void then() {
                    postList.setAdapter(new MyAndFavorAdapter(MyAndFavor.this, MainActivity.myPosts.getPosts(),false));
                }
            });
        }
        else if (state.equals("shoucang")) {
            infoText.setText("我的收藏");
            Testmethod_bbs.get_my_likedpost(MyAndFavor.this, new Then() {
                @Override
                public void then() {
                    if(choice == false){
                        infoClear.setText("编辑");
                        postList.setAdapter(new MyAndFavorAdapter(MyAndFavor.this, MainActivity.myCollectPosts.getPosts(),choice));

                        infoClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                choice = true;
                                init();
                            }
                        });

                    }else {

                        infoClear.setText("退出编辑");
                        postList.setAdapter(new MyAndFavorAdapter(MyAndFavor.this, MainActivity.myCollectPosts.getPosts(),choice));

                        infoClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                choice = false;
                                init();
                            }
                        });

                    }
                }
            });


        } else if (state.equals("jilu")) {
            infoText.setText("浏览记录");
            infoClear.setText("清空");
            Testmethod_bbs.get_my_sawposts(MyAndFavor.this, new Then() {
                @Override
                public void then() {
                    postList.setAdapter(new MyAndFavorAdapter2(MyAndFavor.this, MainActivity.userHistory.getHistory()));
                }
            });


            infoClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Testmethod_bbs.del_all_my_sawposts(MyAndFavor.this, new Then() {
                        @Override
                        public void then() {
                            Toast.makeText(MyAndFavor.this,"成功清空",Toast.LENGTH_SHORT).show();
                            init();

                        }
                    });
                }
            });
        }

        else if(state.equals("huida")){

            infoText.setText("我的回答");
            Testmethod_bbs.get_my_answer(MyAndFavor.this,MainActivity.user.getId(), new Then() {
                @Override
                public void then() {
                    postList.setAdapter(new MyAndFavorAdapter3(MyAndFavor.this,MainActivity.answers.getAnswers()));
                }
            });



        }


    }

    @OnClick(R.id.post_back)
    public void onViewClicked() {
        finish();
    }


}