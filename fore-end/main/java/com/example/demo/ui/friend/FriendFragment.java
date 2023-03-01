package com.example.demo.ui.friend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Then;
import com.example.demo.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendFragment extends Fragment {


    @BindView(R.id.friend_search)
    EditText friendSearch;
    private View rootView;
    private RecyclerView recyclerView;
    public static List<User> userList;
    private Context context;
    private FriendAdapter adapter;
    private ItemTouchHelper.Callback callback;

//    @Override
//    public void onResume() {
//        super.onResume();
//        adapter.notifyDataSetChanged();
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        context = getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.friendlist);
        ButterKnife.bind(this, rootView);
        init();

        return rootView;
    }

    private void initCallback() {
        callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                int id = userList.get(position).getId();

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("是否删除该好友？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Testmethod.delfriend(context, id, new Then() {
                            @Override
                            public void then() {
                                userList.remove(position);
                                adapter.notifyItemRemoved(position);
                                Reload();
                            }
                        });

                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Reload();
                    }
                });
                dialog.show();

            }
        };
    }

    private void init() {
        friendSearch.setFocusable(false);

        TextView new_friend = (TextView) rootView.findViewById(R.id.new_friend);
        new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Applicant.class);
                startActivity(intent);
            }
        });

    }

    public void Reload() {
        userList = new ArrayList<>();
        Testmethod.friend_list(context, new Then() {
            @Override
            public void then() {
                if ((MainActivity.friend_list) != null) {
                    for (User item : MainActivity.friend_list.getUsers()) {
                        userList.add(item);
                    }
                }

                Log.d("FriendFragment", "then: "+userList.size());

                initCallback();
                adapter = new FriendAdapter(context, userList);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            recyclerView.removeAllViews();
            Reload();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.removeAllViews();
        Reload();
    }

    @OnClick({R.id.friend_search, R.id.add_fri})
    public void onViewClicked(View view) {
        Intent intent = new Intent(context, FriSearch.class);
        startActivity(intent);
    }
}
