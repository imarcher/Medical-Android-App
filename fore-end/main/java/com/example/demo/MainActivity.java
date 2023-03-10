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

    public static User user = new User();//?????????user
    public static Docterinfo docterinfo;//????????????????????????
    public static Users allusers;//?????????user??????
    public static Docters docters;//??????docter????????????
    public static UserOrders userOrders;//???????????????????????????
    public static DocterOrders docterOrders;//???????????????????????????
    public static User your_order_docter_userinfo;//??????????????????????????????user?????????????????????id??????IM
    public static UserDocterinfo userpage;//?????????????????????????????????
    public static Users my_friend_apply;//????????????????????????
    public static Users friend_list;//????????????
    public static Users search_friend_list;//????????????????????????????????????
    public static DocterComments docterComments;//???????????????????????????


    public static Post post;//??????????????????
    public static Posts posts;//??????????????????
    public static Answer answer;//??????????????????
    public static Comment comment;//??????????????????
    public static Posts myPosts;//????????????????????????
    public static Answers answers;//????????????????????????
    public static Posts myCollectPosts;//????????????????????????
    public static BackPost backPost;//????????????
    public static UserHistorys userHistory;//??????????????????
    public static UserDocterinfo userpage2;//??????????????????????????????
    public static User postUser;//??????????????????????????????
    public static Post apost;//??????????????????????????????
    public static String name;//??????????????????


    private ImageView imageView;
    private Configuration config;
    public static UploadManager uploadManager;//??????MainActivity.uploadManager??????
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
                ontxt.setText("??????");
            } else {
                if (docterinfo.isIsavailable()) {
                    online.setChecked(true);
                    ontxt.setText("????????????");
                } else {
                    online.setChecked(false);
                    ontxt.setText("??????");
                }
                online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        changeisavailable(MainActivity.this, new Then() {
                            @Override
                            public void then() {
                                if (b) {
                                    online.setChecked(true);
                                    ontxt.setText("????????????");
                                    Toast.makeText(MainActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                                } else {
                                    online.setChecked(false);
                                    ontxt.setText("??????");
                                    Toast.makeText(MainActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
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
                .placeholder(R.drawable.touxiang)//???????????????
                .error(R.drawable.touxiang)//?????????????????????
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
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;
            case R.id.tiezi:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "tiezi");

                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;

            case R.id.shoucang:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "shoucang");
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;
            case R.id.jilu:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "jilu");
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;
            case R.id.huida:
                intent = new Intent(MainActivity.this, MyAndFavor.class);
                bundle.putString("kind", "huida");
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;
            case R.id.set:
                intent = new Intent(MainActivity.this, Setting.class);
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                intent = new Intent(MainActivity.this, About.class);
                Toast.makeText(MainActivity.this, "??????", Toast.LENGTH_LONG).show();
            default:
                ;

        }
        intent.putExtras(bundle);
        startActivity(intent);

        return true;
    }

    private void initPictureConfig() {
        config = new Configuration.Builder()
                // ???????????????????????????
                .build();
        uploadManager = new UploadManager(config);
    }//???????????????????????????


}