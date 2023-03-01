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
import com.example.demo.models.Answer;
import com.example.demo.ui.discuss.Postdetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAndFavorAdapter3 extends RecyclerView.Adapter<MyAndFavorAdapter3.MyAndFavorViewHolder> {


    private Context context;
    private List<Answer> answers;

    public MyAndFavorAdapter3(Context context1, List<Answer> answers1) {

        context = context1;
        answers = answers1;
    }

    @NonNull
    @Override
    public MyAndFavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAndFavorViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAndFavorViewHolder holder, int position) {

        holder.userTouxiang.setVisibility(View.GONE);
        holder.postWhole.setTag(answers.get(position).getPost_id());
        holder.postName.setText(answers.get(position).getContent());
        holder.postTime.setText(answers.get(position).getCreate_time());


        Testmethod_bbs.find_post_info(context, answers.get(position).getPost_id(), new Then() {
            @Override
            public void then() {
                holder.postUsername.setText(MainActivity.apost.getTitle());
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
        
        return answers.size();
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
