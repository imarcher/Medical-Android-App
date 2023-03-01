package com.example.demo.ui.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.models.Docterinfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.docters;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DocViewHolder> implements View.OnClickListener {


    private Context context;
    List<Docterinfo> docs = null;
    private int a;

    public DocAdapter(Context context, int a) {
        this.context = context;
        if (docters != null)
            docs = docters.getDocters();
        this.a = a;
    }


    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_doc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocViewHolder holder, int position) {
        if (docs != null) {

            try {
                holder.docWhole.setTag(docs.get(position).getId());
                holder.docName.setText(docs.get(position).getName());
                holder.docPos.setText(docs.get(position).getPosition());
                holder.docYiyuan.setText(docs.get(position).getHospital());
                holder.docShanchang.setText(docs.get(position).getDomains());
                Picasso.get()
                        .load(docs.get(position).getDoc_url())
                        .placeholder(R.drawable.yisheng)//默认占位符
                        .error(R.drawable.yisheng)//加载失败占位符
                        .into(holder.docTouxiang);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            holder.docWhole.setTag(-1);
            holder.docName.setText("Doctor " + position);

        }
        holder.docWhole.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return docs == null ? a : docs.size();
    }

    @Override
    public void onClick(View view) {
        int id;
        if(view.getTag()!=null)
        id = (int)view.getTag();
        else id = -1;
        Intent intent = new Intent(context,DoctorInfo.class);
        intent.putExtra("ID",id);
        startActivity(context,intent,null);
    }

    class DocViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doc_touxiang)
        ImageView docTouxiang;
        @BindView(R.id.doc_name)
        TextView docName;
        @BindView(R.id.doc_pos)
        TextView docPos;
        @BindView(R.id.doc_yiyuan)
        TextView docYiyuan;
        @BindView(R.id.doc_shanchang)
        TextView docShanchang;
        @BindView(R.id.doc_whole)
        LinearLayout docWhole;

        public DocViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
