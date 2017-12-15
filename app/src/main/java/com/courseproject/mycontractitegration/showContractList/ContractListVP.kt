package com.courseproject.mycontractitegration.showContractList

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView
import com.courseproject.mycontractitegration.data.Contract


class ContractListVP
{
    interface View : BaseView<Presenter> {
        fun showContractList(tempList: List<Contract>)
        fun showEmptyListWarning()
    }


    interface Presenter : BasePresenter {
        fun loadContractList()

    }


}