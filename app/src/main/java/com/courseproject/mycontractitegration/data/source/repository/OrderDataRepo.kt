package com.courseproject.mycontractitegration.data.source.repository

import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.source.OrderDataSource
import org.litepal.crud.DataSupport

class OrderDataRepo():OrderDataSource{
    override fun loadOrders(callBack: OrderDataSource.LoadOrdersCallBack) {
        val orderFromSearch:List<CustomOrder>? = DataSupport.select().find(CustomOrder::class.java)
        callBack.onOrdersLoaded(orderFromSearch)
    }
    fun getInstance(): OrderDataRepo
    {
        val instance = OrderDataRepo()
        return instance
    }
}