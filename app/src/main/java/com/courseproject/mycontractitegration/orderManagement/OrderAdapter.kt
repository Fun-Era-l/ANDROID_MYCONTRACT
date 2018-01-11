package com.courseproject.mycontractitegration.orderManagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.CustomOrder


class OrderAdapter(context: Context, itemResourceId: Int, objects:List<CustomOrder>):
        ArrayAdapter<CustomOrder>(context,itemResourceId,objects){
    var resourceId: Int = itemResourceId;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var mOrder: CustomOrder = getItem(position)
        var mView :View = LayoutInflater.from(context).inflate(resourceId,parent,false)
        var orderName: TextView =mView.findViewById(R.id.order_item_name)
        orderName.setText(mOrder.order_name)
        var orderDetailBrief:TextView = mView.findViewById(R.id.order_item_detail)
        orderDetailBrief.setText(mOrder.order_detail)
        return mView;
    }
}