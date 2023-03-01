package com.example.demo.ui.head;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Testmethod_bbs;
import com.example.demo.method.Then;
import com.example.demo.models.UserDocterinfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.example.demo.MainActivity.friend_list;
import static com.example.demo.MainActivity.name;
import static com.example.demo.MainActivity.userpage;
import static com.example.demo.method.Testmethod.apply_for_friend;
import static com.example.demo.method.Testmethod.friend_list;
import static com.example.demo.method.Testmethod.getuserpage;

public class UserInfo extends AppCompatActivity {

    @BindView(R.id.user_back)
    ImageView userBack;
    @BindView(R.id.info_text)
    TextView infoText;
    @BindView(R.id.user_touxiang)
    CircleImageView userTouxiang;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_friend)
    Button userFriend;
    @BindView(R.id.info_tiezi)
    TextView infoTiezi;
    @BindView(R.id.info_huida)
    TextView infoHuida;
    @BindView(R.id.info_list)
    RecyclerView infoList;

    private int id, parent;
    private int flag = -1;
    private UserDocterinfo userDocterinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("ID", -1);
        parent = getIntent().getIntExtra("parent", -1);
        friend_list(UserInfo.this, new Then() {
            @Override
            public void then() {
                flag = -1;
                for (int i = 0; i < friend_list.getUsers().size(); i++) {
                    if (friend_list.getUsers().get(i).getId() == id) {
                        flag = 1;
                        break;
                    }
                }
                init();
            }
        });

    }

    private void init() {
        if (id >= 0)
            getuserpage(this, id, new Then() {
                @Override
                public void then() {
                    Picasso.get()
                            .load(userpage.getUser().getHead_url())
                            .placeholder(R.drawable.touxiang)//默认占位符
                            .error(R.drawable.touxiang)//加载失败占位符
                            .into(userTouxiang);
                    userName.setText(userpage.getUser().getUsername());
                    infoText.setText(userpage.getUser().getUsername() + "的个人主页");
                    userDocterinfo = userpage;
                    if (flag > 0) {
                        userFriend.setText("删除好友");
                        userFriend.setBackgroundResource(R.drawable.friend_red);
                    }
                    Log.d("Userinfo", "onViewClicked: "+userDocterinfo.getUser().getId());
                    infoList.setLayoutManager(new LinearLayoutManager(UserInfo.this));

                    infoHuida.setTextColor(Color.rgb(34, 59, 83));
                    infoTiezi.setTextColor(Color.rgb(18, 150, 219));


                    Testmethod_bbs.get_my_post(UserInfo.this, userDocterinfo.getUser().getId(), new Then() {
                        @Override
                        public void then() {
                            infoList.setAdapter(new MyAndFavorAdapter(UserInfo.this, MainActivity.myPosts.getPosts(),false));
                        }
                    });

                }
            });
    }

    @OnClick({R.id.user_back, R.id.user_friend, R.id.user_talk,R.id.info_tiezi, R.id.info_huida})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                finish();
                break;
            case R.id.user_friend:
                if (flag < 0) {
                    apply_for_friend(UserInfo.this, id, new Then() {
                        @Override
                        public void then() {
                            Toast.makeText(UserInfo.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
                            userFriend.setText("已发送申请");
                            userFriend.setBackgroundResource(R.drawable.friend_blue);
                            userFriend.setClickable(false);
                        }
                    });
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UserInfo.this);
                    dialog.setMessage("是否删除该好友？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Testmethod.delfriend(UserInfo.this, id, new Then() {
                                @Override
                                public void then() {
                                    Toast.makeText(UserInfo.this, "已删除好友", Toast.LENGTH_SHORT).show();
                                    flag = -1;
                                    userFriend.setText("重新加为好友");
                                    userFriend.setBackgroundResource(R.drawable.friend_green);
                                }
                            });

                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.user_talk:
                name = userName.getText().toString();
                startConversation(id);
                break;
            case R.id.info_tiezi:

                infoHuida.setTextColor(Color.rgb(34, 59, 83));
                infoTiezi.setTextColor(Color.rgb(18, 150, 219));
                Log.d("Userinfo", "onViewClicked: "+userDocterinfo.getUser().getId());
                Testmethod_bbs.get_my_post(UserInfo.this, userDocterinfo.getUser().getId(), new Then() {
                    @Override
                    public void then() {
                        infoList.setAdapter(new MyAndFavorAdapter(UserInfo.this, MainActivity.myPosts.getPosts(),false));
                    }
                });
                break;
            case R.id.info_huida:
                infoTiezi.setTextColor(Color.rgb(34, 59, 83));
                infoHuida.setTextColor(Color.rgb(18, 150, 219));


                Testmethod_bbs.get_my_answer(UserInfo.this,userDocterinfo.getUser().getId(), new Then() {
                    @Override
                    public void then() {
                        infoList.setAdapter(new MyAndFavorAdapter3(UserInfo.this,MainActivity.answers.getAnswers()));
                    }
                });



                break;
        }
    }

    private void startConversation(int id) {

        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        String targetId = String.valueOf(id);//对话人的userid，String类型
        String title = "test";//这个随便

        RongIM.getInstance().startConversation(UserInfo.this, conversationType, targetId, title, null);


    }


}