package com.courseproject.mycontractitegration.showContractList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Contract

class ContractAdapter(context: Context, itemResourceId: Int, objects:List<Contract>):
        ArrayAdapter<Contract>(context,itemResourceId,objects){
    var resourceId: Int = itemResourceId;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val mContract: Contract = getItem(position)
        var mView :View = LayoutInflater.from(context).inflate(resourceId,parent,false)
        val contractTitle: TextView =mView.findViewById(R.id.item_contract_title)
        val contractContent:TextView = mView.findViewById(R.id.item_contract_content)
        contractTitle.setText(mContract.title)
        contractContent.setText(mContract.content)
        return mView;
    }
}