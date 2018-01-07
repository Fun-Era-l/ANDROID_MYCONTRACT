package com.courseproject.mycontractitegration.OrderManagement

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.OrderBmob
import kotlinx.android.synthetic.main.order_display_activity.*

class DisplayOrderActivity : AppCompatActivity() {
    var mCurrentOrder: CustomOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_display_activity)
        setSupportActionBar(order_display_toolbar)
        mCurrentOrder = intent.getSerializableExtra("SelectedItem") as CustomOrder?
        if(mCurrentOrder != null) {
            order_display_name.setText(mCurrentOrder?.order_name)
            order_display_content.setText(mCurrentOrder?.order_detail)
            when(mCurrentOrder?.status) {
                0->order_display_status.setText("编辑中")
                1->{
                    order_display_status.setText("等待律师")
                }
                2->{
                    order_display_status.setText("律师代写中")
                    order_display_lawyer.setText(mCurrentOrder?.lawyer_name)
                }
                3->{
                    order_display_status.setText("已完成")
                    order_display_lawyer.setText(mCurrentOrder?.lawyer_name)
                }
                else->
                {

                    Log.d("ORDER STATUS ERROR","STATUS: ${mCurrentOrder?.status}")
                }
            }

        }
        order_display_toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {

            override fun onMenuItemClick(item:MenuItem):Boolean {
                when (item.getItemId()) {
                    R.id.submit_order-> {
                        val order = CustomOrder(order_display_name.text.toString(),order_display_content.text.toString())
                        val orderBmob = OrderBmob(order)
                        orderBmob.save(object :SaveListener<String>(){
                            override fun done(p0: String?, p1: BmobException?) {
                                order.bmob_id = p0!!
                                order.save()
                                Toast.makeText(this@DisplayOrderActivity, "成功提交订单，请等待系统分配律师", Toast.LENGTH_SHORT).show();
                                startActivity(Intent(this@DisplayOrderActivity,OrderListActivity::class.java))
                            }
                        })
                    }
                   R.id.edit_order -> {
                       val order = CustomOrder(order_display_name.text.toString(),order_display_content.text.toString())
                       startActivity(Intent(this@DisplayOrderActivity,AddEditOrderActivity::class.java).putExtra("OrderToEdit",order))

                   }
                    else-> {
                    }
                }
                return true;
            }
        });
    }
     override fun onCreateOptionsMenu(menu: Menu):Boolean {
       getMenuInflater().inflate(R.menu.menu_display_order, menu);
       return true;
    }


}
