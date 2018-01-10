package com.courseproject.mycontractitegration.friendManage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.Friend;
import com.courseproject.mycontractitegration.data.Msg;
import com.courseproject.mycontractitegration.sendContract.ContractActivity;
import com.courseproject.mycontractitegration.sendMessage.ChatActivity;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomepageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        final Intent intent=getIntent();
        final String name=intent.getStringExtra("name");
        Friend tFriend=FriendModel.getInstance().findOneByName(name).get(0);
        final String phone= tFriend.getPhone();
        initView(tFriend);
        BmobUser user=BmobUser.getCurrentUser();
        BmobIM.connect(user.getMobilePhoneNumber(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("TAG","服务器连接成功");
                }else {
                    Log.i("TAG","服务器连接失败");
                    Toast.makeText(HomepageActivity.this,"无网络 无法发送消息",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ChatPresenter.getInstance().connect(user.getMobilePhoneNumber());

        //监听连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                Toast.makeText(HomepageActivity.this,connectionStatus.getMsg(),Toast.LENGTH_SHORT).show();
                Log.i("连接状态：",BmobIM.getInstance().getCurrentStatus().getMsg());
            }
        });
        Button button=findViewById(R.id.btn_sendtohim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(HomepageActivity.this, ChatActivity.class);
                intent1.putExtra("friendName",name);
                startActivity(intent1);
            }
        });
        Button button1=findViewById(R.id.btn_sendContract);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomepageActivity.this,ContractActivity.class);
                intent2.putExtra("fName",name);
                startActivity(intent2);
            }
        });
    }

    void initView(Friend friend){
        TextView showname=findViewById(R.id.tv_show_name);
        TextView showcompany=findViewById(R.id.tv_show_company);
        TextView showphone=findViewById(R.id.tv_show_phone);
        showname.setText("名字："+friend.getName());
        showphone.setText("手机："+friend.getPhone());
        showcompany.setText("公司："+friend.getCompany());

    }
}
