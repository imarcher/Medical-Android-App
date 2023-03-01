package com.example.demo.ui.head;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Testmethod_bbs;
import com.example.demo.method.Then;
import com.example.demo.models.Post;
import com.example.demo.ui.discuss.Postdetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAndFavorAdapter extends RecyclerView.Adapter<MyAndFavorAdapter.MyAndFavorViewHolder> {



    private Context context;
    private List<Post> posts;
    private boolean choice;

    public MyAndFavorAdapter(Context context1, List<Post> posts1, boolean choice1) {

        context = context1;
        posts = posts1;
        choice = choice1;
    }

    @NonNull
    @Override
    public MyAndFavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAndFavorViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAndFavorViewHolder holder, int position) {

        holder.postWhole.setTag(posts.get(position).getId());
        holder.postName.setText(posts.get(position).getTitle());


        Testmethod.getuserpage2(context, posts.get(position).getAuthor_id(), new Then() {
            @Override
            public void then() {
                Picasso.get()
                        .load(MainActivity.userpage2.getUser().getHead_url())
                        .placeholder(R.drawable.touxiang)//默认占位符
                        .error(R.drawable.touxiang)//加载失败占位符
                        .into(holder.userTouxiang);
                holder.postUsername.setText(MainActivity.userpage2.getUser().getUsername());
                holder.postTime.setText(posts.get(position).getCreate_time());
            }
        });


        if (choice == true) {
            holder.checkBox.setVisibility(View.VISIBLE);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Testmethod_bbs.dellikedpost(context, posts.get(position).getId(), new Then() {
                        @Override
                        public void then() {
                            posts.remove(position);
                            // notifyItemRemoved(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.postWhole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转帖子
                    Toast.makeText(context, "跳转帖子", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount() {

        return posts.size();
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
        @BindView(R.id.check_box)
        ImageView checkBox;
        @BindView(R.id.user_touxiang)
        CircleImageView userTouxiang;

        public MyAndFavorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
