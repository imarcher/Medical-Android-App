package com.example.demo.ui.discuss;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Then;
import com.example.demo.ui.friend.FriSearch;
import com.example.demo.ui.friend.FriendAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.demo.method.Testmethod_bbs.getposts;

public class postsearch extends AppCompatActivity {

    @BindView(R.id.post_filter)
    EditText postfilter;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.search_info)
    RecyclerView searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsearch);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        String filter = postfilter.getText().toString();
        if(filter.equals("")){
            Toast.makeText(this,"关键字为空",Toast.LENGTH_SHORT).show();
        }
        getposts(this,filter, new Then() {
            @Override
            public void then() {
                if(MainActivity.posts!=null)
                {
                    searchInfo.setLayoutManager(new LinearLayoutManager(postsearch.this));
                    searchInfo.setItemAnimator(new DefaultItemAnimator());
                    searchInfo.setAdapter(new PostsAdapter(postsearch.this,MainActivity.posts.getPosts()));
                }
            }
        });
    }
}