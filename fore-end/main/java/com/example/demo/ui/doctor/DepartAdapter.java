package com.example.demo.ui.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepartAdapter extends RecyclerView.Adapter<DepartAdapter.DepartViewHolder> {


    @BindView(R.id.depart)
    TextView depart;
    private Context context;
    static String[] departList = {"全部", "妇产科", "男科", "儿科", "皮肤科", "心理咨询", "内科", "外科", "骨科", "眼科",
            "耳鼻喉科", "口腔科", "中医科", "肿瘤科", "营养科", "传染病科", "全科"};
    private View.OnClickListener changedepart;
    private View reset = null;

    public DepartAdapter(Context context, View.OnClickListener docf) {
        this.context = context;
        this.changedepart = docf;
    }

    @NonNull
    @Override
    public DepartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_depart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartViewHolder holder, int position) {


        holder.depart.setText(departList[position]);
        holder.depart.setOnClickListener(changedepart);
        if(position==0)holder.depart.performClick();
    }

    @Override
    public int getItemCount() {
        return departList.length;
    }


    class DepartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.depart)
        TextView depart;

        public DepartViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
