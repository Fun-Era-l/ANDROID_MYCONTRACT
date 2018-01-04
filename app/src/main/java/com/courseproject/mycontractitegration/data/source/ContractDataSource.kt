package com.courseproject.mycontractitegration.data.source

import com.courseproject.mycontractitegration.data.Contract

interface ContractDataSource {

    interface SaveContractCallback {
        fun onContractSaved()
        fun onContractUpdated(updatedContractTitle:String)
        fun onContractFailToSave()
        fun onContractFailToUpdate(updatedContractTitle:String)

    }

    interface loadContractsCallback {
        fun onContractsLoaded(templates: List<Contract>?)
        fun onContractsNotAvailable()
    }

    interface SignContractCallBack
    {
        fun onContractIsSigned()
        fun onSignFailed()
    }
    fun signContract(title: String,callback: SignContractCallBack)
    fun getContracts(callback: loadContractsCallback)
    fun saveContract(title:String, content:String,callback:SaveContractCallback)
}
