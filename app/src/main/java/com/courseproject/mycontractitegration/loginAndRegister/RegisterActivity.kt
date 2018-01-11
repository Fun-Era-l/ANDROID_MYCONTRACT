package com.courseproject.mycontractitegration.loginAndRegister

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.R
import org.w3c.dom.Text

public class RegisterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        val name: EditText =findViewById(R.id.edittext_register_name);
        val phone:EditText = findViewById(R.id.edittext_register_phone);
        val password:EditText =findViewById(R.id.edittext_register_password);
        val button_register:Button =findViewById(R.id.button_register_register);
        val button_code:Button = findViewById(R.id.button_register_code);
        val textView: TextView = findViewById(R.id.textvie_rlogin);
        /*
        发送验证码按键监听
         */
        button_code.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view:View) {
                BmobSMS.requestSMSCode(phone.text.toString(), "message", object: QueryListener<Int>() {
                   override fun done(integer:Int?, e: BmobException?) {
                        if (e==null){
                            Toast.makeText(this@RegisterActivity,"验证码已成功发送",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this@RegisterActivity,"error:"+e.message,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        /*
        输入验证码后监听注册按键
         */
        button_register.setOnClickListener(object :View.OnClickListener{
            override fun onClick(view:View) {
                var code:EditText =findViewById(R.id.edittext_register_code);
                var user   = BmobUser();
                user.setUsername(name.getText().toString());
                user.setMobilePhoneNumber(phone.getText().toString());
                user.setPassword(password.getText().toString());
                user.signOrLogin(code.getText().toString(), object: SaveListener<BmobUser>() {
                    override fun done(user:BmobUser?, e:BmobException?) {
                        if (user!=null){
                            Toast.makeText(this@RegisterActivity,"注册成功",Toast.LENGTH_SHORT).show();
                            //注册成功跳转回登录界面
                            val intent: Intent =Intent(this@RegisterActivity,LoginActivity::class.java)
                            startActivity(intent);
                        }else {
                            Toast.makeText(this@RegisterActivity,"失败："+e?.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        /*
        已有账号的情况下，点击提示信息进入登录页面
         */
        textView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view:View) {
                val intent: Intent =Intent(this@RegisterActivity,LoginActivity::class.java)
                startActivity(intent);
            }
        });
    }
}

