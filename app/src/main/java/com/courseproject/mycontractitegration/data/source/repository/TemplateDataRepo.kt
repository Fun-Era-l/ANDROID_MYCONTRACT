package com.courseproject.mycontractitegration.data.source.repository


import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.TemplateBmob
import com.courseproject.mycontractitegration.data.source.TemplateDataSource
import org.litepal.crud.DataSupport

class TemplateDataRepo():TemplateDataSource{

    override fun getTemplates(callback: TemplateDataSource.loadTemplatesCallback) {
        /*
        获取本地和服务器上的模板列表
        templateList; 模板列表
        localTemplateList ： 本地模板列表

         */
        var templateList = ArrayList<Template>()
        var localTemplateList = DataSupport.findAll(Template::class.java)
            templateList.addAll(localTemplateList)
            var query = BmobQuery<TemplateBmob>()
            query.setLimit(100).findObjects(object:FindListener<TemplateBmob>()
            {
                override fun done(templateBmobList: MutableList<TemplateBmob>?, exception: BmobException?) {
                    if(exception != null)
                    {
                        Log.d("Bmob-LoadTemplate",exception.message)
                    }
                    else {
                        if (templateBmobList == null) {
                            Log.d("Bmob-LoadTemplate", "Empty templateBmob list")
                            callback.onTemplatesNotAvailable()
                        } else {
                            for (templateBmob in templateBmobList)
                            {
                                templateList.add(Template(templateBmob.template_name,templateBmob.template_content))
                            }

                            callback.onTemplatesLoaded(templateList)
                        }
                    }
                }
            })
            callback.onTemplatesLoaded(localTemplateList)
    }
    fun getInstance():TemplateDataRepo
    {
        val instance = TemplateDataRepo()
        return instance
    }

}