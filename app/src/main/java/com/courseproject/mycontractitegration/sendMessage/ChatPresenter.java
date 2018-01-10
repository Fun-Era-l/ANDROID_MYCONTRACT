package com.courseproject.mycontractitegration.sendMessage;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.courseproject.mycontractitegration.data.Msg;
import com.courseproject.mycontractitegration.friendManage.FriendModel;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 27827 on 2018/1/2.
 */

public class ChatPresenter {
    //private ChatPresenter INSTANCE;
    private Context context;

    public Context getContext(){return context;}
    //public ChatPresenter INSTANCE(){return INSTANCE;}

    private static ChatPresenter ourInstance=new ChatPresenter();

    public static ChatPresenter getInstance(){return ourInstance;}

    private ChatPresenter(){

    }

    //连接服务器
    public void connect(String phone){
        BmobIM.connect(phone, new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("TAG","服务器连接成功");
                }else {
                    Log.i("TAG","服务器连接失败");
                }
            }
        });
    }

    //开启会话
    public BmobIMConversation startConversation(BmobIMUserInfo info){
        BmobIMConversation conversationEntrance=BmobIM.getInstance().startPrivateConversation(info,null);
        BmobIMConversation messageManager=BmobIMConversation.obtain(BmobIMClient.getInstance(),conversationEntrance);
        return messageManager;
    }

    public void sendMessage(final String content, BmobIMConversation mConversationManager){
        if (TextUtils.isEmpty(content.trim())){
            Toast.makeText(context,"输入内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Msg msg=ChatModel.getInstance().getMsg(content,Msg.TYPE_SEND);
        mConversationManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e==null){
                    //发送成功
                }else {
                    Toast.makeText(context,"发送失败",Toast.LENGTH_SHORT).show();
                }
            }

        });

        //显示
    }


}
