package com.courseproject.mycontractitegration.data.source

import com.courseproject.mycontractitegration.data.Template


interface TemplateDataSource {
    interface loadTemplatesCallback {
        fun onTemplatesLoaded(templates: List<Template>)
        fun onTemplatesNotAvailable()
    }
    fun getTemplates(callback: loadTemplatesCallback)

}