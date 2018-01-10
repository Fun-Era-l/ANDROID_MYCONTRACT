package com.courseproject.mycontractitegration.data;

import org.litepal.crud.DataSupport;

import cn.bmob.newim.bean.BmobIMTextMessage;

/**
 * Created by 27827 on 2018/1/4.
 */

public class Msg extends BmobIMTextMessage{
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SEND=1;

    private int type;

    /*public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }*/
    public void setType(int type){
        this.type=type;
    }


    /*public String getContent(){
        return content;
    }*/

    public int getType(){
        return type;
    }

}
