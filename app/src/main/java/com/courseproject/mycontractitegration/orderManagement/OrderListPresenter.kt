package com.courseproject.mycontractitegration.orderManagement

import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.source.OrderDataSource

class OrderListPresenter(orderDataSource: OrderDataSource,orderView: OrderListVP.View):
        OrderListVP.Presenter, OrderDataSource.LoadOrdersCallBack {

    private var mOrderDataRepo: OrderDataSource = orderDataSource

    private var mOrderListView: OrderListVP.View = orderView


    override fun start() {
        loadOrderList()
    }

    override fun loadOrderList() {
        mOrderDataRepo.loadOrders(this)
    }



    override fun onOrdersLoaded(orderList: List<CustomOrder>?) {
        if (orderList == null) {
            mOrderListView.showEmptyListWarning()
        } else {
            mOrderListView.showOrderList(orderList)
        }
    }

}