package com.example.demo.ui.friend;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Then;
import com.example.demo.models.User;
import com.example.demo.ui.doctor.DocAdapter;
import com.example.demo.ui.doctor.DocSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.demo.MainActivity.docters;
import static com.example.demo.MainActivity.search_friend_list;
import static com.example.demo.MainActivity.user;
import static com.example.demo.method.Testmethod.getdocters;

public class FriSearch extends AppCompatActivity {

    @BindView(R.id.friend_search)
    EditText friendSearch;
    @BindView(R.id.friend_text)
    TextView friendText;
    @BindView(R.id.friend_info)
    RecyclerView friendInfo;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_search);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.friend_text)
    public void onViewClicked() {
        String filter = friendSearch.getText().toString();
        if(filter.equals("")){
            Toast.makeText(this,"关键字为空",Toast.LENGTH_SHORT).show();
        }
        else {
            userList = new ArrayList<> ();
            Testmethod.search_friend_list(this, filter, new Then() {
                @Override
                public void then() {
                    if ((MainActivity.search_friend_list) != null) {
                        for (User item : MainActivity.search_friend_list.getUsers()) {
                            userList.add(item);
                        }
                    }
                    Toast.makeText(FriSearch.this,"共"+userList.size()+"条结果",Toast.LENGTH_SHORT).show();
                    friendInfo.setLayoutManager(new LinearLayoutManager(FriSearch.this));
                    friendInfo.setItemAnimator(new DefaultItemAnimator());
                    friendInfo.setAdapter(new FriendAdapter(FriSearch.this, userList));
                }
            });
        }
    }
}