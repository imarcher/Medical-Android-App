package com.example.demo.ui.friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Applicant extends AppCompatActivity {

    private View rootView;
    private RecyclerView recyclerView;
    public static List<User> userList;
    private ApplicantAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant);
        ButterKnife.bind(this);
        initViews();


    }


    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.applicantlist);

        userList = new ArrayList<>();


        Testmethod.friend_apply_list(this, new Then() {

            @Override
            public void then() {
                if ((MainActivity.my_friend_apply) != null) {
                    Log.d("run", "have applicant ");
                    for (User item : MainActivity.my_friend_apply.getUsers()) {
                        if (!item.getUsername().equals(MainActivity.user.getUsername())) {//有时候返回的列表里面会有用户自身的名字，原因未知
                            userList.add(item);
                        }

                    }
                }

                adapter = new ApplicantAdapter(Applicant.this, userList);
                recyclerView.setLayoutManager(new LinearLayoutManager(Applicant.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

                if (userList.size() != 0) {

                    adapter.setmOnItemClickListener(new ApplicantAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int id = userList.get(position).getId();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Applicant.this);
                            dialog.setMessage("是否接受好友申请？");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Testmethod.handle_friendapply(Applicant.this, id, "1", new Then() {
                                        @Override
                                        public void then() {

                                        }
                                    });
                                }
                            });
                            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Testmethod.handle_friendapply(Applicant.this, id, "0", new Then() {
                                        @Override
                                        public void then() {

                                        }
                                    });
                                }
                            });
                            dialog.show();
                            userList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });

                }


            }


        });


    }

    @OnClick(R.id.appli_back)
    public void onViewClicked() {
        finish();
    }
}