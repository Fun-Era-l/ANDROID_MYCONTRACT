package com.courseproject.mycontractitegration.sendMessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.User;
import com.courseproject.mycontractitegration.friendManage.FriendModel;

import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String phone= FriendModel.getInstance().nameToPhone(name);
        Toast.makeText(this,name,Toast.LENGTH_LONG).show();
        User user= BmobUser.getCurrentUser(User.class);
        //连接IM服务器
        if (!TextUtils.isEmpty(user.getPhone_number())){
            BmobIM.connect(user.getPhone_number(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        //连接成功
                        Log.d("test","success");
                    }else {
                        Toast.makeText(ChatActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //监听连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                Toast.makeText(ChatActivity.this,connectionStatus.getMsg(),Toast.LENGTH_SHORT).show();
                Log.i("test",BmobIM.getInstance().getCurrentStatus().getMsg());
            }
        });
        BmobIMUserInfo info=new BmobIMUserInfo();
        info.setName(name);
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if (e==null){
                    //开始聊天
                }else {
                    Toast.makeText(ChatActivity.this,"开启会话错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
