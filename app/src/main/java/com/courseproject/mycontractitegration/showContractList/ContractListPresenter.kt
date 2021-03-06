package com.courseproject.mycontractitegration.showContractList

import com.courseproject.mycontractitegration.data.source.ContractDataSource
import com.courseproject.mycontractitegration.data.Contract


class ContractListPresenter(templateDataSource: ContractDataSource, templateView: ContractListVP.View):
        ContractListVP.Presenter, ContractDataSource.loadContractsCallback {
    private var mLocalDataSource: ContractDataSource = templateDataSource

    private var mContractListView: ContractListVP.View = templateView

    override fun start() {
        loadContractList()
    }

    override fun onContractsLoaded(templates: List<Contract>?) {
        if(templates != null)
        {
            mContractListView.showContractList(templates)
        }
    }
    override fun loadContractList() {
        mLocalDataSource.getContracts(this)
    }

    override fun onContractsNotAvailable() {
        mContractListView.showEmptyListWarning()
    }
}