package com.example.demo.ui.head;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod_bbs;
import com.example.demo.method.Then;
import com.example.demo.models.UserHistory;
import com.example.demo.ui.discuss.Postdetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.userpage;

public class MyAndFavorAdapter2 extends RecyclerView.Adapter<MyAndFavorAdapter2.MyAndFavorViewHolder> {


    private Context context;
    private List<UserHistory> userHistories;

    public MyAndFavorAdapter2(Context context1, List<UserHistory> userHistories1) {

        context = context1;
        userHistories = userHistories1;
    }

    @NonNull
    @Override
    public MyAndFavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAndFavorViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAndFavorViewHolder holder, int position) {

        holder.postWhole.setTag(userHistories.get(position).getPost_id());
        holder.postName.setText(userHistories.get(position).getTitle());
        holder.postTime.setText(userHistories.get(position).getCreate_time());


        Testmethod_bbs.find_post_user(context, userHistories.get(position).getPost_id(), new Then() {
            @Override
            public void then() {
                Picasso.get()
                        .load(userpage.getUser().getHead_url())
                        .placeholder(R.drawable.touxiang)//默认占位符
                        .error(R.drawable.touxiang)//加载失败占位符
                        .into(holder.userTouxiang);
                holder.postUsername.setText(MainActivity.postUser.getUsername());
            }
        });



        holder.postWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转帖子
                Toast.makeText(context,"跳转帖子",Toast.LENGTH_SHORT).show();
                int id;
                if(view.getTag()!=null)
                    id = (int)view.getTag();
                else id = -1;
                Intent intent = new Intent(context, Postdetail.class);
                intent.putExtra("post",id);
                startActivity(context,intent,null);
            }
        });



    }

    @Override
    public int getItemCount() {
        
        return userHistories.size();
    }

    class MyAndFavorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_name)
        TextView postName;
        @BindView(R.id.post_username)
        TextView postUsername;
        @BindView(R.id.post_time)
        TextView postTime;
        @BindView(R.id.post_whole)
        LinearLayout postWhole;
        @BindView(R.id.user_touxiang)
        CircleImageView userTouxiang;
        public MyAndFavorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
