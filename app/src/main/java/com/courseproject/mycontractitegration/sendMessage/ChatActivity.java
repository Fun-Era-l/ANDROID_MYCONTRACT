package com.courseproject.mycontractitegration.sendMessage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.ImMessageHandler;
import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.Msg;
import com.courseproject.mycontractitegration.friendManage.FriendListActivity;
import com.courseproject.mycontractitegration.friendManage.FriendModel;
import com.courseproject.mycontractitegration.sendContract.ContractActivity;
import com.courseproject.mycontractitegration.showContractList.ContractListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ChatActivity extends AppCompatActivity{

    private ListView msgListView;
    private MsgAdapter adapter;
    private List<Msg> msgList=new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //注册eventbus
        EventBus.getDefault().register(this);
        final Intent intent=getIntent();
        final String name=intent.getStringExtra("friendName");
        String phone= FriendModel.getInstance().nameToPhone(name);
        final EditText content=findViewById(R.id.edt_msg);
        final Button button=findViewById(R.id.btn_send);
        final Button contract=findViewById(R.id.btn_contract);
        adapter = new MsgAdapter(ChatActivity.this, R.layout.msg_item, msgList);
        msgListView = (ListView)findViewById(R.id.lv_chat);
        msgListView.setAdapter(adapter);

            final BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
            query.addWhereEqualTo("mobilePhoneNumber",phone);
            query.findObjects(new FindListener<BmobUser>() {
                @Override
                public void done(List<BmobUser> list, BmobException e) {
                    if (e==null){
                        if (list.size()>0) {
                            Log.i("TAG", "用户查询成功");
                            System.out.println(list.get(0).getMobilePhoneNumber());
                            BmobIMUserInfo info = new BmobIMUserInfo();
                            info.setName(list.get(0).getUsername());
                            info.setUserId(list.get(0).getMobilePhoneNumber());
                            //创建会话
                            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                                @Override
                                public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                    if (e == null) {
                                        final BmobIMConversation mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), bmobIMConversation);

                                        final Msg message = new Msg();
                                        //点击发送按钮
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!content.getText().toString().trim().equals("")) {
                                                    //设置Msg实例
                                                    message.setContent(content.getText().toString());
                                                    message.setType(Msg.TYPE_SEND);
                                                    //发送消息
                                                    mBmobIMConversation.sendMessage(message, new MessageSendListener() {
                                                        @Override
                                                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                                            if (e == null) {

                                                                Msg msg = new Msg();
                                                                msg.setContent(content.getText().toString());
                                                                msg.setType(Msg.TYPE_SEND);
                                                                msgList.add(msg);

                                                                adapter.notifyDataSetChanged();
                                                                msgListView.setSelection(msgList.size());
                                                                content.setText("");
                                                                //显示信息的部分未完成

                                                            } else {
                                                                Log.d("TAG", "send wrong");
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(ChatActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                        //合同按钮
                                        contract.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent1=new Intent(ChatActivity.this, ContractActivity.class);
                                                Bundle bundle=new Bundle();
                                                bundle.putSerializable("conversation",mBmobIMConversation);
                                                intent.putExtras(bundle);
                                                startActivity(intent1);
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChatActivity.this, "出错", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }else {
                            Log.i("TAG","用户查询失败");
                            Toast.makeText(ChatActivity.this,"无此用户 无法发送",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Log.i("TAG","用户查询失败");
                    }
                }

            });

    }
    @Subscribe
    public void onEVentMainThread(MessageEvent event){
       Msg msg=new Msg();
       msg.setType(Msg.TYPE_RECEIVED);
       msg.setContent(event.getMessage().getContent());
       msgList.add(msg);
       adapter.notifyDataSetChanged();
       msgListView.setSelection(msgList.size());

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*无用操作
    @Override
    protected void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        BmobIMMessage msg=list.get(0).getMessage();
        Toast.makeText(ChatActivity.this,msg.getContent()+"hhhhhh",Toast.LENGTH_SHORT);
    }*/


}
    /*public void onMessageReceiver(List<MessageEvent> list,BmobIMConversation mConversationManager){
        Log.i("TAG","聊天界面收到信息："+list.size());
        for (int i=0;i<list.size();i++){
            addMessage2Chat(list.get(i),mConversationManager);
        }

    }

    private void addMessage2Chat(MessageEvent event,BmobIMConversation mConversationManager){
        BmobIMMessage msg=event.getMessage();
        if (mConversationManager!=null&&event!=null&&mConversationManager.getConversationId().equals(event.getConversation().getConversationId())){
            Msg message=new Msg();
            message.setContent(msg.getContent());
            message.setType(Msg.TYPE_RECEIVED);
            msgList.add(message);
        }
    }*/

