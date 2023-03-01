package com.example.demo.ui.head;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.method.Testmethod;
import com.example.demo.method.Then;
import com.example.demo.models.One_Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.example.demo.MainActivity.docterComments;
import static com.example.demo.MainActivity.docterinfo;
import static com.example.demo.MainActivity.name;
import static com.example.demo.MainActivity.user;
import static com.example.demo.MainActivity.userpage;
import static com.example.demo.MainActivity.your_order_docter_userinfo;
import static com.example.demo.method.Testmethod.getuserfromdoc;
import static com.example.demo.method.Testmethod.getuserpage;
import static com.example.demo.method.Timemethod.isbeyonddays;

public class DDAdapter extends RecyclerView.Adapter<DDAdapter.DDViewHolder> {



    private Context context;
    private boolean isdoc = user.getIsdocter();
    private int a = -1;
    private List<One_Order> list = null;
    private String don = null;
    private boolean ab;
    private boolean flag;

    public DDAdapter(Context context1, int a1, List<One_Order> list1) {
        context = context1;
        isdoc = user.getIsdocter();
        list = list1;
        if (user.getUsername().equals("default"))
            a = a1;
    }
    private boolean able(int position){
        ab = false;
        if(a<0)
            if(list.get(position).getPatient_id()==user.getId())
                ab = true;
        return ab;
    }

    private String done(int position){
        don = null;
        flag = false;
        if(a<0)
            Testmethod.mypatientordercomments(context, list.get(position).getDocter_id(), new Then() {
                @Override
                public void then() {
                    for(int i = 0;i<docterComments.getDoctercomments().size();i++){
                        if(docterComments.getDoctercomments().get(i).getPatient_id()==user.getId()){
                            don = docterComments.getDoctercomments().get(i).getContent();
                            break;
                        }
                    }
                    flag = true;
                }
            });
        while (!flag);
        return don;
    }


    @NonNull
    @Override
    public DDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (a > 0)
            list = null;
        return new DDViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_dingdan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DDViewHolder holder, int position) {
        if (list != null) {
            holder.dingdanTime.setText(list.get(position).getCreate_time());
            if (!isbeyonddays(context, list.get(position).getCreate_time(), 1)) {
                holder.dingdanGuoqi.setText("可进入");
                if(able(position))
                    holder.dingdanPingjia.setText("订单完成后可评价");
                else holder.dingdanPingjia.setVisibility(View.GONE);
                holder.dingdanGuoqi.setTextColor(Color.rgb(95, 250, 60));
            } else {
                if(ab){
                    if(done(position)!=null)
                        holder.dingdanPingjia.setText("修改评价");
                }else holder.dingdanPingjia.setVisibility(View.GONE);

            }

            if (isdoc&&docterinfo.getId() == list.get(position).getDocter_id()) {
                holder.dingdanName.setText(list.get(position).getPatient_name());
                holder.dingdanDepart.setText("");
            } else {
                holder.dingdanName.setText(list.get(position).getDocter_name());
                getuserfromdoc(context, list.get(position).getDocter_id(), new Then() {
                    @Override
                    public void then() {
                        getuserpage(context, your_order_docter_userinfo.getId(), new Then() {
                            @Override
                            public void then() {
                                holder.dingdanDepart.setText(userpage.getDocterinfo().getDepartment());
                            }
                        });
                    }
                });
            }
        } else {
            holder.dingdanName.setText("Name" + position);
        }
        holder.dingdanWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list != null) {
                    if (!isbeyonddays(context, list.get(position).getCreate_time(), 1)) {
                        if (!isdoc || docterinfo.getId() != list.get(position).getDocter_id()) {
                            getuserfromdoc(context, list.get(position).getDocter_id(), new Then() {
                                @Override
                                public void then() {
                                    name = list.get(position).getDocter_name();
                                    startConversation(your_order_docter_userinfo.getId());
                                }
                            });
                        } else {
                            name = list.get(position).getPatient_name();
                            startConversation(list.get(position).getPatient_id());
                        }

                    } else {
                        if(able(position)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View v = LayoutInflater.from(context).inflate(R.layout.make_pingjia, null);
                            EditText makePingjia = (EditText) v.findViewById(R.id.make_pingjia);
                            Button makeFabiao = (Button) v.findViewById(R.id.make_fabiao);
                            if(don!=null)
                                makePingjia.setText(don);
                            Dialog dialog = builder.setTitle("评价：" + list.get(position).getDocter_name()).setView(v).show();
                            makeFabiao.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String s = makePingjia.getText().toString();
                                    if (s.equals("")) {
                                        Toast.makeText(context, "评价不能为空", Toast.LENGTH_SHORT).show();
                                    } else
                                        Testmethod.finish_order_comment(context, list.get(position).getId(), s, new Then() {
                                            @Override
                                            public void then() {
                                                Toast.makeText(context, "发表成功", Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        });
                                }
                            });
                        }
                    }

                } else
                    Toast.makeText(context, "点击了订单:" + holder.dingdanName.getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return list == null ? a : list.size();
    }

    private void startConversation(int id) {

        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        String targetId = String.valueOf(id);//对话人的userid，String类型
        String title = "一次对话";//这个随便

        RongIM.getInstance().startConversation(context, conversationType, targetId, title, null);

    }


    class DDViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dingdan_name)
        TextView dingdanName;
        @BindView(R.id.dingdan_depart)
        TextView dingdanDepart;
        @BindView(R.id.dingdan_time)
        TextView dingdanTime;
        @BindView(R.id.dingdan_guoqi)
        TextView dingdanGuoqi;
        @BindView(R.id.dingdan_whole)
        LinearLayout dingdanWhole;
        @BindView(R.id.dingdan_pingjia)
        TextView dingdanPingjia;

        public DDViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
