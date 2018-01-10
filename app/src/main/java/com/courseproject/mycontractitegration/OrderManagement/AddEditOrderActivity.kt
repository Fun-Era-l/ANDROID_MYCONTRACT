package com.courseproject.mycontractitegration.OrderManagement

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.OrderBmob
import kotlinx.android.synthetic.main.add_edit_order_act.*
import kotlinx.android.synthetic.main.order_display_activity.*

class AddEditOrderActivity : AppCompatActivity() {
    var currentOrder:CustomOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_order_act)
        setSupportActionBar(addedit_order_toolbar)
        currentOrder = intent.getSerializableExtra("OrderToEdit") as CustomOrder?
        if(currentOrder != null)
        {
            addedit_order_name.setText(currentOrder?.order_name.toString())
            addedit_order_detail.setText(currentOrder?.order_detail.toString())
        }
        addedit_order_toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem):Boolean {
            when (item.getItemId()) {
                R.id.store_order -> {
                    val order = CustomOrder(order_display_name.text.toString(),order_display_content.text.toString())
                    order.save()
                    Toast.makeText(this@AddEditOrderActivity, "暂存订单，可再次编辑", Toast.LENGTH_SHORT).show();
                    startActivity(Intent(this@AddEditOrderActivity,OrderListActivity::class.java))
                }
                else-> return true
            }
            return true;
        }
    });
        confirm_order.setOnClickListener(object:View.OnClickListener {
            override fun onClick(p0: View?) {
                val order = CustomOrder(addedit_order_name.text.toString(),addedit_order_detail.text.toString())
                val orderBmob = OrderBmob(order)
                orderBmob.save(object : SaveListener<String>(){
                    override fun done(p0: String?, p1: BmobException?) {
                        

                        order.bmob_id = p0!!
                        order.save()
                        Toast.makeText(this@AddEditOrderActivity, "成功提交订单，请等待系统分配律师", Toast.LENGTH_SHORT).show();
                        startActivity(Intent(this@AddEditOrderActivity,OrderListActivity::class.java))
                    }
                })
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        getMenuInflater().inflate(R.menu.menu_edit_order, menu);
        return true;
    }
}
