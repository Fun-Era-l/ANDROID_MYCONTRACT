package com.courseproject.mycontractitegration.data

import cn.bmob.v3.BmobUser
import org.litepal.crud.DataSupport
import java.io.Serializable

class CustomOrder():DataSupport(),Serializable {
    var id:Long = 0
    var bmob_id:String = ""
    var order_name:String = ""
    var order_detail:String = ""
    var customer_id:String = BmobUser.getCurrentUser().objectId
    var lawyer_id:String? = null
    var lawyer_name:String = ""
    /*
    订单状态：   0-编辑中<本地>    1- 已编辑但未处理；    2-处理中；  3-已完成
     */
    var status:Int = 0
    constructor(name:String,detail:String):this()
    {
        this.order_name = name
        this.order_detail = detail
    }
    constructor(orderBmob: OrderBmob):this(){
        this.order_name = orderBmob.order_name
        this.order_detail = orderBmob.detail
        this.status = orderBmob.status
        this.bmob_id = orderBmob.objectId
        this.lawyer_id = orderBmob.lawyer_id

    }
    override fun save():Boolean{
        val orderList:List<CustomOrder>? = DataSupport.select().where("order_name=?",this.order_name).find(CustomOrder::class.java)
        if(orderList == null || orderList.isEmpty())
        {
           return(super.save())
        }
        else
        {
            //先去重 再更新
            for (i in 1 until orderList.size) {
                orderList.get(i).delete()
            }
            val orderToUpdate:CustomOrder = orderList.get(0)
            return (this.update(orderToUpdate.id) > 0)
        }
    }
}