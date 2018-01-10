package com.courseproject.mycontractitegration.data;

import org.litepal.crud.DataSupport;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 27827 on 2017/12/31.
 */

public class Friend extends DataSupport {
    private int id;
    private String name;
    private String phone;
    private String company;
    //排序用的拼音
    //private transient String pinyin;
    public void setName(String name){
        this.name=name;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    /*public void setPinyin(String pinyin){
        this.pinyin=pinyin;
    }*/
    public void setCompany(String company){this.company=company;}
    public void setId(int id){this.id=id;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    /*public String getPinyin(){return pinyin;}*/
    public String getCompany(){return company;}
    public int getId(){return id;}
}
