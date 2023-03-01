package com.example.demo.ui.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.Callbacks.GetDocterCallBack;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Then;
import com.example.demo.models.Docters;
import com.example.demo.ui.OnCircleImageClick;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.docters;
import static com.example.demo.MainActivity.user;
import static com.example.demo.method.Testmethod.getdocters;

public class DoctorFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.doctor_my)
    CircleImageView circleImageView;
    @BindView(R.id.doc_line)
    LinearLayout docLine;
    private Unbinder unbinder;
    @BindView(R.id.doc_search)
    EditText docSearch;
    @BindView(R.id.doc_list)
    RecyclerView docList;
    @BindView(R.id.doc_scroll_l)
    ScrollView docScrollL;
    @BindView(R.id.doc_info)
    RecyclerView docInfo;
    @BindView(R.id.doc_scroll_r)
    ScrollView docScrollR;
    private OnCircleImageClick onCircleImageClick;
    private TextView reset = null;
    private int a = 0;
    private TextPaint tp = null;
    private String dep;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_doctor, container, false);
        unbinder = ButterKnife.bind(this, root);
        Picasso.get()
                .load(user.getHead_url())
                .placeholder(R.drawable.touxiang)//默认占位符
                .error(R.drawable.touxiang)//加载失败占位符
                .into(circleImageView);

        docInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        docInfo.setAdapter(new DocAdapter(getActivity(), a));

        docList.setLayoutManager(new LinearLayoutManager(getActivity()));
        docList.setAdapter(new DepartAdapter(getActivity(),this));
        docSearch.setFocusable(false);
        docSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DocSearch.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onCircleImageClick = (OnCircleImageClick) context;
    }

    @OnClick(R.id.doctor_my)
    public void onViewClicked() {
        if (onCircleImageClick != null)
            onCircleImageClick.onClick();
    }//bad look


    @Override
    public void onClick(View view) {
        TextView v = (TextView)view;
        tp = v.getPaint();
        tp.setFakeBoldText(true);
        v.setTextSize(17);
        view.setBackgroundResource(R.drawable.depart_checked);
        if(reset!=null){
            reset.setBackgroundResource(R.drawable.depart_unchecked);
            tp = reset.getPaint();
            reset.setTextSize(15);
            tp.setFakeBoldText(false);
        }

        a++;
        TextView tv = (TextView) view;
        dep = tv.getText().toString();

        if(dep.equals("全部"))
            dep = "";

        getdocters(getActivity(), dep, "", new Then() {
            @Override
            public void then() {
                int sum = docters.getDocters().size();
                Toast.makeText(getActivity(), dep + ":共" + sum + "位医生", Toast.LENGTH_SHORT).show();
                docInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
                docInfo.setAdapter(new DocAdapter(getActivity(), a));
            }
        });

        reset = (TextView)view;

    }

}
