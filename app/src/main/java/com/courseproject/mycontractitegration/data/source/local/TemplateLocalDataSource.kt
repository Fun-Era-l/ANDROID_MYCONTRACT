package com.courseproject.mycontractitegration.data.source.local


import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.TemplateDataSource
import org.litepal.LitePal
import org.litepal.crud.DataSupport

class TemplateLocalDataSource():TemplateDataSource{

    override fun getTemplates(callback: TemplateDataSource.loadTemplatesCallback) {
     var TemplateList = DataSupport.findAll(Template::class.java)
        if(TemplateList == null) {
            callback.onTemplatesNotAvailable()
        }
        else {
            callback.onTemplatesLoaded(TemplateList)
        }
    }
    fun getInstance():TemplateLocalDataSource
    {
        val instance = TemplateLocalDataSource()
        return instance
    }

}