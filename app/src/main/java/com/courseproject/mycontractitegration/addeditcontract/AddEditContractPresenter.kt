package com.courseproject.mycontractitegration.addeditcontract

import com.courseproject.mycontractitegration.data.source.ContractDataSource


class AddEditContractPresenter(contractDataSource: ContractDataSource,view:AddEditContractVP.View): AddEditContractVP.Presenter,
        ContractDataSource.SaveContractCallback,ContractDataSource.DeleteContractCallBack
{
    var mContractDataSource: ContractDataSource = contractDataSource
    var mView : AddEditContractVP.View = view

    override fun start() {}

    override fun saveContract(title: String, content: String) {
        mContractDataSource.saveContract(title,content,this)
    }


    override fun onContractSaved() {
           mView.saveSucceeded()
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

    override fun deleteContract(id: Long) {
        mContractDataSource.deleteContract(id,this)
    }

    override fun onDeleteContract(isDeleted: Boolean) {
        if(isDeleted){
            mView.deleteSucceeded()
        }else{
            mView.deleteFailed()
        }
    }
}
