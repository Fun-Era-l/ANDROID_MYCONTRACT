package com.courseproject.mycontractitegration.issueHandler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.IssueBmob
import kotlinx.android.synthetic.main.issue_act.*

class IssueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_act)
        submit_issue.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                val curUser:BmobUser = BmobUser.getCurrentUser()
                val mIssue:IssueBmob = IssueBmob(curUser.objectId,curUser.username,"${issue_order_info.text}\n\n${issue_customer_complaint.text}")
                mIssue.save(object :SaveListener<String>() {
                    override fun done(p0: String?, p1: BmobException?) {
                        if(p1 == null){
                            Toast.makeText(this@IssueActivity,"您的申诉已经提交，我们将尽快与您联系",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@IssueActivity,"您的申诉提交失败，请检查网络条件。",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        })
    }
}
