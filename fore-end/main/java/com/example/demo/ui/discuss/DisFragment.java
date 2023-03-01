package com.example.demo.ui.discuss;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.ui.doctor.DocSearch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DisFragment extends Fragment {
    @BindView(R.id.filter_post)
    EditText filterPost;
    @BindView(R.id.put)
    Button put;
    private DisViewModel mViewModel;
    private Unbinder unbinder;
    public static DisFragment newInstance() {
        return new DisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discuss, container, false);
        unbinder = ButterKnife.bind(this, view);
        filterPost.setFocusable(false);
        filterPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), postsearch.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DisViewModel.class);
        // TODO: Use the ViewModel
    }
    @OnClick(R.id.put)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), putpost.class);
        startActivity(intent);
        Toast.makeText(getActivity(), "帖子", Toast.LENGTH_LONG).show();
    }

}