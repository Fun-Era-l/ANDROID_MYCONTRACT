package com.courseproject.mycontractitegration.friendManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.courseproject.mycontractitegration.R;
import com.github.promeg.pinyinhelper.Pinyin;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        final EditText name=findViewById(R.id.edt_name);
        final EditText company=findViewById(R.id.edt_company);
        final EditText phone=findViewById(R.id.edt_phone);
        Button button=findViewById(R.id.btn_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equals("")||phone.getText().toString().trim().equals("")){
                    Toast.makeText(AddFriendActivity.this,"名字或手机不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    if ((FriendModel.getInstance().addFriend(name.getText().toString(),company.getText().toString()
                            ,phone.getText().toString()))==true){
                        Toast.makeText(AddFriendActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AddFriendActivity.this,FriendListActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(AddFriendActivity.this,"已存在该联系人",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
