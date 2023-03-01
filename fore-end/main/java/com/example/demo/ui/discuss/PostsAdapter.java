package com.example.demo.ui.discuss;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.method.Testmethod_bbs;
import com.example.demo.method.Then;
import com.example.demo.models.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.demo.MainActivity.userpage;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> implements View.OnClickListener {

    private List<Post> posts = null;
    private Context context;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        if (posts != null)
            this.posts = posts;

    }


    @Override
    public void onClick(View view) {
        int id;
        if(view.getTag()!=null)
            id = (int)view.getTag();
        else id = -1;
        Intent intent = new Intent(context, Postdetail.class);
        intent.putExtra("post",id);
        startActivity(context,intent,null);
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostsViewHolder(LayoutInflater.from(context).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        try {
            holder.postid.setTag(posts.get(position).getId());
            holder.postName.setText(posts.get(position).getTitle());
            holder.postTime.setText(posts.get(position).getCreate_time());
            Testmethod_bbs.find_post_user(context, posts.get(position).getId(), new Then() {
                @Override
                public void then() {
                    Picasso.get()
                            .load(userpage.getUser().getHead_url())
                            .placeholder(R.drawable.touxiang)//默认占位符
                            .error(R.drawable.touxiang)//加载失败占位符
                            .into(holder.Touxiang);
                    holder.postauthorname.setText(MainActivity.postUser.getUsername());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.postid.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_name)
        TextView postName;
        @BindView(R.id.post_id)
        LinearLayout postid;
        @BindView(R.id.user_touxiang)
        CircleImageView Touxiang;
        @BindView(R.id.post_username)
        TextView postauthorname;
        @BindView(R.id.post_time)
        TextView postTime;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
