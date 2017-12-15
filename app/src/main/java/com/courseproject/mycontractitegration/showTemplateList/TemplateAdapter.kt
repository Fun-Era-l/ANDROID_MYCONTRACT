package com.courseproject.mycontractitegration.showTemplateList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.R


class TemplateAdapter(context: Context , itemResourceId: Int , objects:List<Template>):
        ArrayAdapter<Template>(context,itemResourceId,objects){
    var resourceId: Int = itemResourceId;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //return super.getView(position, convertView, parent)
        var mTemplate: Template = getItem(position)
        var mView :View = LayoutInflater.from(context).inflate(resourceId,parent,false)
        var templateName: TextView =mView.findViewById(R.id.template_name)
        templateName.setText(mTemplate.template_name)
        return mView;
    }
}