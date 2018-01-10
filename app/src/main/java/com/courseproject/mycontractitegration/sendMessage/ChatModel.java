package com.courseproject.mycontractitegration.sendMessage;

import com.courseproject.mycontractitegration.data.Msg;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;

/**
 * Created by 27827 on 2018/1/4.
 */

public class ChatModel {
    private static ChatModel ourInstance=new ChatModel();

    public static ChatModel getInstance(){return ourInstance;}

    private ChatModel(){

    }
    //获取info
    public BmobIMUserInfo getInfo(BmobUser user){
        BmobIMUserInfo info=new BmobIMUserInfo();
        info.setName(user.getUsername());
        info.setUserId(user.getMobilePhoneNumber());
        return info;
    }

    public Msg getMsg(String content,int type){
        Msg msg=new Msg();
        msg.setType(type);
        msg.setContent(content);
        return msg;
    }
}
