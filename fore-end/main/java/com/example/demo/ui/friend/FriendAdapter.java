package com.example.demo.ui.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.models.User;
import com.example.demo.ui.head.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.userpage;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.BaseViewHolder> {

    private Context context;
    private List<User> userList;

    public FriendAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }




    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false));
    }





    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Uri uri = Uri.parse(userList.get(position).getHead_url());
//        holder.image.setImageURI(uri);
        holder.username.setText(userList.get(position).getUsername());
        holder.id = userList.get(position).getId();
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.haoyou0)//默认占位符
                .error(R.drawable.haoyou0)//加载失败占位符
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return (userList == null ? 0 : userList.size());
    }


    class BaseViewHolder extends RecyclerView.ViewHolder{

        private ImageView image ;
        private TextView username;
        private int id;

        BaseViewHolder(View itemView) {

            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_avatar);
            username = (TextView) itemView.findViewById(R.id.item_username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfo.class);
                    intent.putExtra("ID",id);
                    startActivity(context,intent,null);
                }
            });
        }


    }
}

