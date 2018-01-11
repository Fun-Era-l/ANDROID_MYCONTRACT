package com.courseproject.mycontractitegration.loginAndRegister

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.friendManage.FriendListActivity
import com.courseproject.mycontractitegration.sendContract.ContractActivity
import com.courseproject.mycontractitegration.showContractList.ContractListActivity

public class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        val bmobAppKey:String = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("Bmob_APP_KEY")
        Bmob.initialize(this,bmobAppKey);
        //BmobIM.init(this);
        //BmobIM.registerDefaultMessageHandler(ImMessageHandler());
        var button:Button = findViewById<Button>(R.id.button_login);
        var register: TextView =findViewById<TextView>(R.id.textview_register);
        val phone: EditText = findViewById<EditText>(R.id.edittext_phone);
        val password:EditText=findViewById<EditText>(R.id.edittext_password);
        button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?){
                BmobUser.loginByAccount( phone.text.toString(), password.text.toString(), object: LogInListener<BmobUser>()
                {
                    override fun done(user:BmobUser?, e:BmobException?) {
                        val userTemp:BmobUser? = user
                        if (userTemp != null){
                            var intent =Intent(this@LoginActivity, ContractListActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this@LoginActivity,"请检查你的账号密码与网络连接状态",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
            }
        })
        register.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view:View) {
                var intent:Intent= Intent(this@LoginActivity,RegisterActivity::class.java)
                startActivity(intent);
            }
        });
    }
}
