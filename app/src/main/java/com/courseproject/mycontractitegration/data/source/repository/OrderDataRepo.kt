package com.courseproject.mycontractitegration.data.source.repository

import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.OrderBmob
import com.courseproject.mycontractitegration.data.source.OrderDataSource
import org.litepal.crud.DataSupport

class OrderDataRepo():OrderDataSource{
    override fun loadOrders(callBack: OrderDataSource.LoadOrdersCallBack) {
        //val orderFromSearch:List<CustomOrder>? = DataSupport.select().find(CustomOrder::class.java)
        //callBack.onOrdersLoaded(orderFromSearch)
        val orderList = ArrayList<CustomOrder>()
        val localCustomOrderList = DataSupport.findAll(CustomOrder::class.java)
        orderList.addAll(localCustomOrderList)
        val query = BmobQuery<OrderBmob>()
        query.addWhereEqualTo("customer_id", BmobUser.getCurrentUser().objectId)
        query.setLimit(100).findObjects(object: FindListener<OrderBmob>()
        {
            override fun done(orderBmobList: MutableList<OrderBmob>?, exception: BmobException?) {
                if(exception != null)
                {
                    Log.d("Bmob-LoadCustomOrder",exception.message)
                }
                else {
                    if (orderBmobList == null) {
                        Log.d("Bmob-LoadCustomOrder", "Empty orderBmob list")
                        callBack.onOrdersLoaded(null)
                    } else {
                        for (orderBmob in orderBmobList)
                        {
                            orderList.add(CustomOrder(orderBmob))
                        }

                        callBack.onOrdersLoaded(orderList)
                    }
                }
            }
        })
    }
    fun getInstance(): OrderDataRepo
    {
        val instance = OrderDataRepo()
        return instance
    }
}