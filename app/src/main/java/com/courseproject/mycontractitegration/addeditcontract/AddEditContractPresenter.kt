package com.courseproject.mycontractitegration.addeditcontract

import com.courseproject.mycontractitegration.data.source.ContractDataSource


class AddEditContractPresenter(contractDataSource: ContractDataSource,view:AddEditContractVP.View): AddEditContractVP.Presenter, ContractDataSource.SaveContractCallback
{
    var mContractDataSource: ContractDataSource = contractDataSource
    var mView : AddEditContractVP.View = view

    override fun start() {}

    override fun saveContract(title: String, content: String) {
        mContractDataSource.saveContract(title,content,this)
    }


    override fun onContractSaved(isSucceeded:Boolean) {
       if(isSucceeded) {
           mView.saveSucceeded()
       }
       else {
           mView.saveFailed()
       }
    }

    override fun onContractUpdated(updatedContractTitle:String) {
        mView.updateSucceeded(updatedContractTitle)

    }
    override fun onContractFailToUpdate(updatedContractTitle:String) {
        mView.updateFailed(updatedContractTitle)
    }
    override fun onContractFailToSave() {
        mView.saveFailed()
    }
}
