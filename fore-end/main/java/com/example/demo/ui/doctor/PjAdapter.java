package com.example.demo.ui.doctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.ui.head.UserInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.docterComments;

public class PjAdapter extends RecyclerView.Adapter<PjAdapter.PjViewHolder> {


    private Context context;

    public PjAdapter(Context context1) {
        context = context1;
    }


    @NonNull
    @Override
    public PjViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PjViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_pingjia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PjViewHolder holder, int position) {
        Picasso.get()
                .load(docterComments.getDoctercomments().get(position).getPatient_head_url())
                .placeholder(R.drawable.yisheng)//默认占位符
                .error(R.drawable.yisheng)//加载失败占位符
                .into(holder.pingjiaTouxiang);
        holder.pingjiaName.setText(docterComments.getDoctercomments().get(position).getPatient_username());
        holder.pingjiaInfo.setText(docterComments.getDoctercomments().get(position).getContent());
        holder.pingjiaData.setText(docterComments.getDoctercomments().get(position).getCreate_time());
        holder.pingjiaTouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = docterComments.getDoctercomments().get(position).getPatient_id();
                Intent intent = new Intent(context, UserInfo.class);
                intent.putExtra("ID",id);
                startActivity(context,intent,null);
            }
        });
        holder.pingjiaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.pingjiaTouxiang.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return docterComments!=null? docterComments.getDoctercomments().size():0;
    }



    class PjViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pingjia_touxiang)
        CircleImageView pingjiaTouxiang;
        @BindView(R.id.pingjia_name)
        TextView pingjiaName;
        @BindView(R.id.pingjia_info)
        TextView pingjiaInfo;
        @BindView(R.id.pingjia_data)
        TextView pingjiaData;

        public PjViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
