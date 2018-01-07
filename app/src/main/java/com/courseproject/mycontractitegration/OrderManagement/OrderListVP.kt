package com.courseproject.mycontractitegration.OrderManagement

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView
import com.courseproject.mycontractitegration.data.CustomOrder

class OrderListVP {
    interface View : BaseView<Presenter> {
        fun showOrderList(orderList: List<CustomOrder>)
        fun showEmptyListWarning()
    }


    interface Presenter : BasePresenter {
        fun loadOrderList()
    }
}