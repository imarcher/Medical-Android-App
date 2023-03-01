package com.example.demo.ui.doctor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.Callbacks.GetDocterCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Then;
import com.example.demo.models.Docters;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.demo.MainActivity.docters;
import static com.example.demo.method.Testmethod.getdocters;

public class DocSearch extends AppCompatActivity {

    @BindView(R.id.search_search)
    EditText searchSearch;
    @BindView(R.id.search_info)
    RecyclerView searchInfo;
    @BindView(R.id.search_text)
    TextView searchText;
    private int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_search);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.search_text)
    public void onViewClicked() {
        a++;
        String filter = searchSearch.getText().toString();
        if(filter.equals("")){
            Toast.makeText(this,"关键字为空",Toast.LENGTH_SHORT).show();
        }
        else {
            getdocters(this, "", filter, new Then() {
                @Override
                public void then() {
                    int sum = docters.getDocters().size();
                    Toast.makeText(DocSearch.this,  "共" + sum + "位医生", Toast.LENGTH_SHORT).show();

                    searchInfo.setLayoutManager(new LinearLayoutManager(DocSearch.this));
                    searchInfo.setAdapter(new DocAdapter(DocSearch.this, a));
                }
            });
        }

    }

}