package com.courseproject.mycontractitegration.data

import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobUser

class OrderBmob():BmobObject() {
    var order_name:String = ""
    var detail:String = ""
    var customer_id:String = BmobUser.getCurrentUser().objectId
    var lawyer_id:String? = null
    /*
    订单状态：    0- 未处理；    1-处理中；  2-已完成
     */
    val status:Int = 0
    constructor(order: CustomOrder):this()
    {
        this.order_name = order.order_name
        this.detail = order.order_detail
    }


}