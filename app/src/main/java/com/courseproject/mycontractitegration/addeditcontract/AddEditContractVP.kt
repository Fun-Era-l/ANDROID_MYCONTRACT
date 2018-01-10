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
        fun deleteSucceeded()
        fun deleteFailed()

    }

    interface Presenter : BasePresenter {

        fun saveContract(title: String, content:String)
        fun deleteContract(id:Long)
      //  fun populate()
    }
}