package com.example.demo.method;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.fragment.ConversationFragment;

import static com.example.demo.MainActivity.name;

public class ConversationActivity extends FragmentActivity {

    @BindView(R.id.talk_back)
    ImageView talkBack;
    @BindView(R.id.talk_text)
    TextView talkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        ConversationFragment conversationFragment = new ConversationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container1, conversationFragment);
        transaction.commit();
        talkText.setText(name);
    }


    @OnClick(R.id.talk_back)
    public void onViewClicked() {
        finish();
    }
}