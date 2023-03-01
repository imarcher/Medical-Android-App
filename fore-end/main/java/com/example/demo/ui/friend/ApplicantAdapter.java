package com.example.demo.ui.friend;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;


public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.BaseViewHolder> implements View.OnClickListener{

    private Context context;
    private List<User> userList;

    public ApplicantAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{

        private ImageView image ;
        private TextView username;

        BaseViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_avatar);
            username = (TextView) itemView.findViewById(R.id.item_username);

        }

    }

       public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        view.setOnClickListener(this);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        Uri uri = Uri.parse(userList.get(position).getHead_url());
//        holder.image.setImageURI(uri);
        holder.username.setText(userList.get(position).getUsername());
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.haoyou0)//默认占位符
                .error(R.drawable.haoyou0)//加载失败占位符
                .into(holder.image);

        holder.itemView.setTag(position);


    }

    @Override
    public int getItemCount() {
        return (userList == null ? 0 : userList.size());
    }



}

