package com.courseproject.mycontractitegration.showTemplateList

import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.TemplateDataSource


class TemplateListPresenter(templateDataSource: TemplateDataSource,templateView: TemplateListVP.View):
        TemplateListVP.Presenter, TemplateDataSource.loadTemplatesCallback {
    //暂时采用LocalDataSource代替Repository
    private var mLocalDataSource: TemplateDataSource = templateDataSource

    private var mTemplateListView: TemplateListVP.View = templateView

    //var templateName: String = "DefaultTemplate"

    override fun start() {
        loadTemplateList()
    }

    override fun loadTemplateList() {
        mLocalDataSource.getTemplates(this)
    }

    override fun onTemplatesNotAvailable() {
        mTemplateListView.showEmptyListWarning()
    }
    override fun onTemplatesLoaded(templates: List<Template>) {
        mTemplateListView.showTemplateList(templates)
    }

}