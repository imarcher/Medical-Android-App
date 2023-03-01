package com.example.demo.ui.head;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.method.Then;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.demo.MainActivity.docterOrders;
import static com.example.demo.MainActivity.user;
import static com.example.demo.MainActivity.userOrders;
import static com.example.demo.method.Testmethod.getmydocterorders;
import static com.example.demo.method.Testmethod.mypatientorders;

public class DingDan extends AppCompatActivity {

    @BindView(R.id.dingdan_back)
    ImageView dingdanBack;
    @BindView(R.id.dingdan_list)
    RecyclerView dingdanList;
    @BindView(R.id.info_doc)
    TextView infoDoc;
    @BindView(R.id.info_pa)
    TextView infoPa;
    @BindView(R.id.dingdan_choose)
    LinearLayout dingdanChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ding_dan);
        ButterKnife.bind(this);
        if (!user.getUsername().equals("default")) {
            dingdanList.setLayoutManager(new LinearLayoutManager(DingDan.this));
            if (user.getIsdocter()) {
                mypatientorders(this, new Then() {
                    @Override
                    public void then() {
                        dingdanList.setAdapter(new DDAdapter(DingDan.this, 8, docterOrders.getMypatientorders()));
                    }
                });
            } else {
                dingdanChoose.setVisibility(View.GONE);
                getmydocterorders(this, new Then() {
                    @Override
                    public void then() {
                        dingdanList.setAdapter(new DDAdapter(DingDan.this, 8, userOrders.getMydocterorders()));
                    }
                });
            }

        }else dingdanChoose.setVisibility(View.GONE);


    }

    @OnClick(R.id.dingdan_back)
    public void onViewClicked() {
        finish();
    }


    @OnClick({R.id.info_doc, R.id.info_pa})
    public void onViewClicked(View view) {
        dingdanList.setLayoutManager(new LinearLayoutManager(DingDan.this));
        switch (view.getId()) {
            case R.id.info_doc:
                infoPa.setTextColor(Color.rgb(34, 59, 83));
                infoDoc.setTextColor(Color.rgb(18, 150, 219));
                getmydocterorders(this, new Then() {
                    @Override
                    public void then() {
                        dingdanList.setAdapter(new DDAdapter(DingDan.this, 8, userOrders.getMydocterorders()));
                    }
                });
                break;
            case R.id.info_pa:
                infoDoc.setTextColor(Color.rgb(34, 59, 83));
                infoPa.setTextColor(Color.rgb(18, 150, 219));
                mypatientorders(this, new Then() {
                    @Override
                    public void then() {
                        dingdanList.setAdapter(new DDAdapter(DingDan.this, 8, docterOrders.getMypatientorders()));
                    }
                });
                break;
        }
    }
}