package com.courseproject.mycontractitegration.orderManagement

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.source.repository.OrderDataRepo
import com.courseproject.mycontractitegration.showContractList.ContractListActivity
import kotlinx.android.synthetic.main.order_list_act.*

class OrderListActivity : AppCompatActivity(),OrderListVP.View {
        lateinit private var mPresenter: OrderListVP.Presenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.order_list_act)
            /* 初始化mPresenter  使用LocalData代替DataRepo */
            mPresenter = OrderListPresenter(OrderDataRepo().getInstance(), this)
            mPresenter.loadOrderList()

            /*
            注册item监听, 点击后进入order详情界面
             */
            order_list.setOnItemClickListener(object: AdapterView.OnItemClickListener {

                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (id.toInt() == -1) {
                        Log.d("id-positionError", "ClickOnHeader")
                    }
                    val realPosition: Int = id.toInt()
                    val item:  CustomOrder? = parent?.getItemAtPosition(realPosition) as?  CustomOrder
                    var orderList2display = Intent(this@OrderListActivity, DisplayOrderActivity::class.java)//SelectedOrderActivity::class.java)
                    orderList2display.putExtra("SelectedItem", item)
                    startActivity(orderList2display)
                }
            })
            /*
            新建订单按键,点击后进入订单编辑添加部分
             */

            add_order.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    var orderList2AddOrder = Intent(this@OrderListActivity, AddEditOrderActivity::class.java)
                    startActivity(orderList2AddOrder)
                }
            })

        }

        override fun showEmptyListWarning() {
            Toast.makeText(this@OrderListActivity,"No Signture Found In Database Yet",Toast.LENGTH_SHORT).show()
        }
        override fun showOrderList(orderList: List<CustomOrder>) {
            var mOrderAdapter =  OrderAdapter(this,R.layout.order_item, orderList)
            var mOrderListView : ListView = findViewById(R.id.order_list)
            mOrderListView.adapter = mOrderAdapter;
        }
        /*
        重写在订单列表活动下的系统返回键，直接返回到主界面
         */
        override fun onBackPressed() {
            val orderlist2Entrance:Intent = Intent(this@OrderListActivity, ContractListActivity::class.java)
            startActivity(orderlist2Entrance)
            super.onBackPressed()
        }
        override fun setPresenter(presenter:  OrderListVP.Presenter) {
            val order_presenter =  OrderListPresenter(OrderDataRepo().getInstance(), this)
            this.mPresenter = order_presenter
        }
    
}

