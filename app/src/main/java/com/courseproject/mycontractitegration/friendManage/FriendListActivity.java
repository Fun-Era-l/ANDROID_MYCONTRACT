package com.courseproject.mycontractitegration.friendManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.Friend;
import com.courseproject.mycontractitegration.sendMessage.ChatActivity;
import com.courseproject.mycontractitegration.showContractList.ContractListActivity;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    //private String[] data=FriendModel.getInstance().friendListName();
    //private List<Friend> friendList=new ArrayList<>();
    //private TextView text_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Button button=findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendListActivity.this,AddFriendActivity.class);
                startActivity(intent);
            }
        });
        final String[] data=FriendModel.getInstance().friendListName();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(FriendListActivity.this, android.R.layout.simple_list_item_1,data);
        ListView listView=findViewById(R.id.lt_friend);
        //text_tip=findViewById(R.id.txt_tip);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //listView.setEmptyView(text_tip);
        //点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入主页
                Intent intent=new Intent(FriendListActivity.this,HomepageActivity.class);
                intent.putExtra("name",data[position]);
                startActivity(intent);
            }
        });
        //长按
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(FriendListActivity.this);
                builder.setMessage("删除该联系人？");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (data.length>0){
                            FriendModel.getInstance().deleteFriend(data[position]);
                            Toast.makeText(FriendListActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}
