package com.courseproject.mycontractitegration.friendManage;

import android.widget.ListView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.data.Friend;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 27827 on 2017/12/31.
 */

public class FriendModel {
    private static FriendModel ourInstance=new FriendModel();

    public static FriendModel getInstance(){return ourInstance;}

    private FriendModel(){

    }
    //保存联系人
    public boolean addFriend(String name,String company,String phone){
        if (findOneByName(name).size()==0){
            if (findOneByPhone(phone).size()==0){
                Friend friend=new Friend();
                friend.setName(name);
                friend.setCompany(company);
                friend.setPhone(phone);
                friend.save();
                return true;
            }
        }
        return false;
    }

    //删除联系人
    public void deleteFriend(String name){
        //Friend friend=new Friend();
        //friend.setName(name);
        //friend.delete();
        DataSupport.deleteAll(Friend.class,"name=?",name);
    }

//更改联系人姓名
/*public void updateName(String phone,String newName){
    Friend friend=new Friend();
    friend.setPhone(phone);
    friend.setName(newName);
    friend.save();
}*/

    //更改联系人信息
    public void updateFriend(String phone,String Type,String newthing){
        Friend friend=new Friend();
        friend.setPhone(phone);
        switch (Type){
            case "name":
                friend.setName(newthing);
                friend.save();
                break;
            case "company":
                friend.setCompany(newthing);
                friend.save();
                break;
            case "phone":
                friend.setPhone(newthing);
                friend.save();
                break;
            default:
        }
    }

    //查询所有联系人
    /*public List<Friend> friendList(){
        //List<Friend> list= DataSupport.order("pinyin desc").find(Friend.class);
        List<Friend> list= DataSupport.order("pinyin desc").select("name").find(Friend.class);//只有一列数据
        return list;
    }*/

    //查询所有联系人的名字，并按拼音排序
    public String[] friendListName(){
        List<Friend> list=DataSupport.select("name").order("name").find(Friend.class);
        int size=list.size();
        String[] data=new String[size];
        for (int i=0;i<size;i++){
            data[i]=String.valueOf(list.get(i).getName());
        }
        return data;
    }


    //通过名字查询某个联系人
    public List<Friend> findOneByName(String name){
        List<Friend> list=DataSupport.where("name=?",name).find(Friend.class);
        return list;
    }
    //通过手机查询某个联系人
    public List<Friend> findOneByPhone(String phone){
        List<Friend> list=DataSupport.where("phone=?",phone).find(Friend.class);
        return list;
    }
    //将只有一个人的list转化成string
    public String nameToPhone(String name){
        List<Friend> list=DataSupport.where("name=?",name).find(Friend.class);
        String[] data=new String[1];
        for (int i=0;i<1;i++){
            data[i]=String.valueOf(list.get(i).getPhone());
        }
        return data[0];
    }
}
