package com.courseproject.mycontractitegration.data;

import org.litepal.crud.DataSupport;

import cn.bmob.newim.bean.BmobIMTextMessage;

/**
 * Created by 27827 on 2018/1/4.
 */

public class Msg extends BmobIMTextMessage{
    //public static final int TYPE_RECEIVED=0;
    public static final String TYPE_RECEIVED="receive";
    //public static final int TYPE_SEND=1;
    //public static final int TYPE_CONTRACT=2;
    public static final String TYPE_SEND="send";
    public static final String TYPE_CONTRACT="contract";

    //private int type;
    private String type;

    /*public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }*/
    public void setType(String type){
        this.type=type;
    }


    /*public String getContent(){
        return content;
    }*/

    public String getType(){
        return type;
    }

}
