package com.example.demo.ui.discuss;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo.R;
import com.example.demo.models.BackAnswer;
import com.example.demo.models.BackComment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<BackAnswer> BackAnswerList;
    private Context context;

    public CommentExpandAdapter(Context context, List<BackAnswer> BackAnswerList)               {
        this.context = context;
        this.BackAnswerList = BackAnswerList;
    }

    @Override
    public int getGroupCount() {
        return BackAnswerList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(BackAnswerList.get(i).getComments()== null){
            return 0;
        }else {
            return BackAnswerList.get(i).getComments().size()>0 ? BackAnswerList.get(i).getComments().size():0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return BackAnswerList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return BackAnswerList.get(i).getComments().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Glide.with(context).load(BackAnswerList.get(groupPosition).getUser().getHead_url())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);
        /*Picasso.get()
                .load(BackAnswerList.get(groupPosition).getUser().getHead_url())
                .placeholder(R.drawable.touxiang)//默认占位符
                .error(R.drawable.touxiang)//加载失败占位符
                .centerCrop()
                .into(groupHolder.logo);*/
        groupHolder.tv_name.setText(BackAnswerList.get(groupPosition).getUser().getUsername());
        groupHolder.tv_time.setText(BackAnswerList.get(groupPosition).getAnswer().getCreate_time());
        groupHolder.tv_content.setText(BackAnswerList.get(groupPosition).getAnswer().getContent());
        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#aaaaaa"));
                }else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        String replyUser = BackAnswerList.get(groupPosition).getComments().get(childPosition).getUser().getUsername();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }

        childHolder.tv_content.setText(BackAnswerList.get(groupPosition).getComments().get(childPosition).getComment().getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        public GroupHolder(View view) {
            logo =  view.findViewById(R.id.comment_item_logo);
            tv_content = view.findViewById(R.id.comment_item_content);
            tv_name = view.findViewById(R.id.comment_item_userName);
            tv_time = view.findViewById(R.id.comment_item_time);
            iv_like = view.findViewById(R.id.comment_item_like);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }
    public void addTheReplyData(BackComment replyDetail, int groupPosition){
        if(replyDetail!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetail.toString() );
            if(BackAnswerList.get(groupPosition).getComments()!= null ){
                BackAnswerList.get(groupPosition).getComments().add(replyDetail);
            }else {
                List<BackComment> replyList = new ArrayList<>();
                replyList.add(replyDetail);
                BackAnswerList.get(groupPosition).setComments(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }
    public void addanswerData(BackAnswer answer1){
        if(answer1!=null){

            BackAnswerList.add(answer1);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }
}
