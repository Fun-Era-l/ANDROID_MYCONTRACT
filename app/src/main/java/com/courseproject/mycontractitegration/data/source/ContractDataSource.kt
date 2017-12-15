package com.courseproject.mycontractitegration.data.source

import com.courseproject.mycontractitegration.data.Contract

interface ContractDataSource {

    interface SaveContractCallback {
        fun onContractSaved(isSucceeded: Boolean)
        fun onContractUpdated(updatedContractTitle:String)
        fun onContractFailToSave()
        fun onContractFailToUpdate(updatedContractTitle:String)

    }

    interface loadContractsCallback {
        fun onContractsLoaded(templates: List<Contract>?)
        fun onContractsNotAvailable()
    }

    fun getContracts(callback: loadContractsCallback)
    fun saveContract(title:String, content:String,callback:SaveContractCallback)
}
