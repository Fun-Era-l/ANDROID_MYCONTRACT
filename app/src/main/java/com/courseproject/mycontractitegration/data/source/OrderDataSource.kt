package com.courseproject.mycontractitegration.data.source

import com.courseproject.mycontractitegration.data.CustomOrder

interface OrderDataSource
{
    interface LoadOrdersCallBack
    {
        fun onOrdersLoaded(orderList:List<CustomOrder>?)
    }
    fun loadOrders(callBack: LoadOrdersCallBack)
}