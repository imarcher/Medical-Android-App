package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.demo.method.Then;
import com.example.demo.models.Answer;
import com.example.demo.models.Answers;
import com.example.demo.models.BackPost;
import com.example.demo.models.Comment;
import com.example.demo.models.DocterComments;
import com.example.demo.models.DocterOrders;
import com.example.demo.models.Docterinfo;
import com.example.demo.models.Docters;
import com.example.demo.models.Post;
import com.example.demo.models.PostUserName;
import com.example.demo.models.Posts;
import com.example.demo.models.User;
import com.example.demo.models.UserDocterinfo;
import com.example.demo.models.UserHistory;
import com.example.demo.models.UserHistorys;
import com.example.demo.models.UserOrders;
import com.example.demo.models.Users;
import com.example.demo.ui.OnCircleImageClick;
import com.example.demo.ui.discuss.putpost;
import com.example.demo.ui.head.About;
import com.example.demo.ui.head.DingDan;
import com.example.demo.ui.head.MyAndFavor;
import com.example.demo.ui.head.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.demo.method.Testmethod.changeisavailable;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnCircleImageClick {

    public static User user = new User();//我本人user
    public static Docterinfo docterinfo;//我本人的医生信息
    public static Users allusers;//所有的user信息
    public static Docters docters;//查找docter时的信息
    public static UserOrders userOrders;//病人打开自己的订单
    public static DocterOrders docterOrders;//医生打开自己的订单
    public static User your_order_docter_userinfo;//你点开医生获得的医生user信息，需要获得id用来IM
    public static UserDocterinfo userpage;//用来实现个人主页的信息
    public static Users my_friend_apply;//向我申请好友的人
    public static Users friend_list;//好友列表
    public static Users search_friend_list;//搜索用户来添加好友的列表
    public static DocterComments docterComments;//医生界面的病人评价


    public static Post post;//我发表的帖子
    public static Posts posts;//所有帖子列表
    public static Answer answer;//我发布的回答
    public static Comment comment;//我发布的评论
    public static Posts myPosts;//我发表的帖子列表
    public static Answers answers;//我发布的回答列表
    public static Posts myCollectPosts;//我收藏的帖子列表
    public static BackPost backPost;//打开帖子
    public static UserHistorys userHistory;//用户历史记录
    public static UserDocterinfo userpage2;//临时存一个帖子的作者
    public static User postUser;//临时存一个帖子的作者
    public static Post apost;//临时存一个帖子的信息
    public static String name;//会话界面标题


    private ImageView imageView;
    private Configuration config;
    public static UploadManager uploadManager;//使用MainActivity.uploadManager上传
    private NavigationView nva;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPictureConfig();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ButterKnife.bind(this);
        loadinfo();

    }


    @Override
    public void onClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void loadinfo() {

        nva = (NavigationView) findViewById(R.id.haerder);
        View view;
        view = nva.getHeaderView(0);
        imageView = (ImageView) view.findViewById(R.id.User_head);
        CheckBox online = (CheckBox) view.findViewById(R.id.Online_check);
        TextView ontxt = (TextView) view.findViewById(R.id.Online_txt);
        if (!user.getUsername().equals("default")) {
            if (!user.getIsdocter()) {
                online.setChecked(true);
                online.setClickable(false);
                ontxt.setText("在线");
            } else {
                if (docterinfo.isIsavailable()) {
                    online.setChecked(true);
                    ontxt.setText("接受问诊");
                } else {
                    online.setChecked(false);
                    ontxt.setText("离线");
                }
                online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        changeisavailable(MainActivity.this, new Then() {
                            @Override
                            public void then() {
                                if (b) {
                                    online.setChecked(true);
                                    ontxt.setText("接受问诊");
                                    Toast.makeText(MainActivity.this, "上线，开始接受问诊", Toast.LENGTH_SHORT).show();
                                } else {
                                    online.setChecked(false);
                                    ontxt.setText("离线");
                                    Toast.makeText(MainActivity.this, "下线，停止在线问诊", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                ontxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        online.performClick();
                    }
                });

            }

        } else online.setClickable(false);


        nva.setNavigationItemSelectedListener(this);


        Picasso.get()
                .load(user.getHead_url())
                .placeholder(R.drawable.touxiang)//默认占位符
                .error(R.drawable.touxiang)//加载失败占位符
                .into(imageView);
        TextView textView;
        textView = (TextView) view.findViewById(R.id.User_name);
        textView.setText(user.getUsername());

        int parent = getIntent().getIntExtra("parent", -1);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        bundle.putString("kind", "default");
        switch (item.getItemId()) {
            case R.id.dingdan:
                intent = new Intent(MainActivity.this, DingDan.class);
                Toast.makeText(MainActivity.this, "订单", Toast.LENGTH_LONG).show();
                break;
            case R.id.tiezi:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "tiezi");

                Toast.makeText(MainActivity.this, "帖子", Toast.LENGTH_LONG).show();
                break;

            case R.id.shoucang:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "shoucang");
                Toast.makeText(MainActivity.this, "收藏", Toast.LENGTH_LONG).show();
                break;
            case R.id.jilu:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "jilu");
                Toast.makeText(MainActivity.this, "记录", Toast.LENGTH_LONG).show();
                break;
            case R.id.huida:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "huida");
                Toast.makeText(MainActivity.this, "回答", Toast.LENGTH_LONG).show();
                break;
            case R.id.set:
                intent = new Intent(MainActivity.this, Setting.class);
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                intent = new Intent(MainActivity.this, About.class);
                Toast.makeText(MainActivity.this, "关于", Toast.LENGTH_LONG).show();
            default:
                ;

        }
        intent.putExtras(bundle);
        startActivity(intent);

        return true;
    }

    private void initPictureConfig() {
        config = new Configuration.Builder()
                // 默认华东，这个好用
                .build();
        uploadManager = new UploadManager(config);
    }//初始化图片上传功能


}