package com.courseproject.mycontractitegration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.data.Contract;
import com.courseproject.mycontractitegration.data.Msg;
import com.courseproject.mycontractitegration.friendManage.FriendListActivity;
import com.courseproject.mycontractitegration.friendManage.HomepageActivity;
import com.courseproject.mycontractitegration.sendContract.ContractMessage;
import com.courseproject.mycontractitegration.sendMessage.ChatActivity;
import com.courseproject.mycontractitegration.sendMessage.MsgAdapter;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

/**
 * Created by 27827 on 2018/1/2.
 */

public class ImMessageHandler extends BmobIMMessageHandler{
    private Context context;
    public ImMessageHandler(Context context){this.context=context;}
    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        if (messageEvent.getMessage().getMsgType().equals("contract")){
            Toast.makeText(context,"你收到来自"+messageEvent.getConversation().getConversationId()+"用户的一份合同",Toast.LENGTH_LONG).show();
            String message=messageEvent.getMessage().getContent().toString();
            Log.i("get",message);
            int i=getI(message);
            String title=message.substring(0,i);
            String content=message.substring(i+1,message.length());
            Log.i("title",title);
            Log.i("content",content);
            Contract contract=new Contract();
            contract.setTitle(title);
            contract.setContent(content);
            contract.save();

        }else {
            EventBus.getDefault().post(messageEvent);
        }
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        Log.i("TAG","有"+map.size()+"个用户发来离线消息");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            Log.i("TAG","用户" + entry.getKey() + "发来" + size + "条消息");
            /*for (int i = 0; i < size; i++) {
                //处理每条消息
                executeMessage(list.get(i));
            }*/
        }
    }

    private void executeMessage(MessageEvent messageEvent){
        BmobIMMessage msg=messageEvent.getMessage();
        processSDKMessage(msg,messageEvent);
    }

    private void processSDKMessage(BmobIMMessage msg,MessageEvent event){

        if (BmobNotificationManager.getInstance(context).isShowNotification()){
            Intent pendingIntent=new Intent(context, FriendListActivity.class);     //需要修改
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //TODO 消息接收：8.6、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
            BmobIMUserInfo info = event.getFromUserInfo();
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                    info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
        }
        else {
            executeMessage(event);
        }
    }

    private int getI(String message){
        for (int i=0;i<message.length();i++){
            if (message.charAt(i)=='+'){
                return i;
            }
        }
        return 0;
    }


}
