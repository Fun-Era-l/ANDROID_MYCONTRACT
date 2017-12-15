package com.courseproject.mycontractitegration.addeditcontract

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView

class AddEditContractVP
{
    interface View : BaseView<Presenter> {
        fun saveSucceeded()
        fun saveFailed()
        fun updateSucceeded(title:String)
        fun updateFailed(title:String)


    }

    interface Presenter : BasePresenter {

        fun saveContract(title: String, content:String)

      //  fun populate()
    }
}