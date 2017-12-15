package com.courseproject.mycontractitegration.showTemplateList

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView
import com.courseproject.mycontractitegration.data.Template

class TemplateListVP
{
    interface View : BaseView<Presenter> {
        fun showTemplateList(tempList: List<Template>)
        fun showEmptyListWarning()
    }


    interface Presenter : BasePresenter {
        fun loadTemplateList()

    }


}