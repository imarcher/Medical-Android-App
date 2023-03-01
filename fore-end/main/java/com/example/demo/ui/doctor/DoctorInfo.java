package com.example.demo.ui.doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Then;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.example.demo.MainActivity.name;
import static com.example.demo.MainActivity.user;
import static com.example.demo.MainActivity.userpage;
import static com.example.demo.MainActivity.your_order_docter_userinfo;
import static com.example.demo.method.Testmethod.addOne_Order;
import static com.example.demo.method.Testmethod.getuserfromdoc;
import static com.example.demo.method.Testmethod.getuserpage;

public class DoctorInfo extends AppCompatActivity {

    @BindView(R.id.info_zixun)
    Button infoZixun;
    @BindView(R.id.info_text)
    TextView infoText;
    @BindView(R.id.info_touxiang)
    ImageView infoTouxiang;
    @BindView(R.id.info_name)
    TextView infoName;
    @BindView(R.id.info_pos)
    TextView infoPos;
    @BindView(R.id.info_yiyuan)
    TextView infoYiyuan;
    @BindView(R.id.info_shanchang)
    TextView infoShanchang;

    int position = -2;
    @BindView(R.id.info_zaixian)
    TextView infoZaixian;
    @BindView(R.id.info_pingjia_info)
    RecyclerView infoPingjiaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("ID", position);
        init(position);

    }

    private void init(int position) {
        if (position >= 0) {
            getuserfromdoc(this, position, new Then() {
                @Override
                public void then() {
                    getuserpage(DoctorInfo.this, your_order_docter_userinfo.getId(), new Then() {
                        @Override
                        public void then() {
                            String text;
                            if (userpage != null) {
                                try {
                                    text = userpage.getDocterinfo().getName() + "    " + userpage.getDocterinfo().getDepartment();
                                    infoText.setText(text);
                                    infoName.setText(userpage.getDocterinfo().getName());
                                    infoPos.setText(userpage.getDocterinfo().getPosition());
                                    infoYiyuan.setText(userpage.getDocterinfo().getHospital());
                                    infoShanchang.setText(userpage.getDocterinfo().getDomains());
                                    if (userpage.getDocterinfo().isIsavailable()) {
                                        infoZaixian.setText("医生在线");
                                        infoZaixian.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                                    }
                                    Picasso.get()
                                            .load(userpage.getDocterinfo().getDoc_url())
                                            .placeholder(R.drawable.yisheng)//默认占位符
                                            .error(R.drawable.yisheng)//加载失败占位符
                                            .into(infoTouxiang);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });

            Testmethod.mypatientordercomments(DoctorInfo.this, position, new Then() {
                @Override
                public void then() {
                    infoPingjiaInfo.setLayoutManager(new LinearLayoutManager(DoctorInfo.this));
                    infoPingjiaInfo.setAdapter(new PjAdapter(DoctorInfo.this));
                }
            });

        }


    }


    private void startConversation(int id) {

        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        String targetId = String.valueOf(id);//对话人的userid，String类型
        String title = userpage.getDocterinfo().getName();//这个随便

        RongIM.getInstance().startConversation(this, conversationType, targetId, title, null);


    }

    @OnClick({R.id.doc_back, R.id.info_zixun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.doc_back:
                finish();
                break;
            case R.id.info_zixun:
                if (position >= 0) {
                    getuserfromdoc(this, position, new Then() {
                        @Override
                        public void then() {
                            addOne_Order(DoctorInfo.this, user.getId(), position, new Then() {
                                @Override
                                public void then() {
                                    name = infoName.getText().toString();
                                    startConversation(your_order_docter_userinfo.getId());
                                }
                            });
                        }
                    });
                } else
                    Toast.makeText(this, "点击了咨询", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}