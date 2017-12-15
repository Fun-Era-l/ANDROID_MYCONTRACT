package com.courseproject.mycontractitegration.data.source.local

import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.source.ContractDataSource
import org.litepal.crud.DataSupport

class ContractLocalDataSource() : ContractDataSource
{
    fun getInstance():ContractDataSource{
        return ContractLocalDataSource()
    }
    override fun saveContract(title:String,content:String,callback: ContractDataSource.SaveContractCallback) {
        val contractToSave: Contract = Contract(title,content)
        val contractFromSearch:List<Contract>? = DataSupport.select().where("title=?",title).find(Contract::class.java)
        if(contractFromSearch == null || contractFromSearch.isEmpty())
        {
            //优化： 可采用AsyncSave() 异步保存大文件；
            //save()返回值 true / false
            val saveResult:Boolean = contractToSave.save()
            callback.onContractSaved(saveResult)

        }
        else
        {
            //先去重 再更新
            for (i in 1 until contractFromSearch.size) {
                contractFromSearch.get(i).delete()
            }
            val contractToUpdate:Contract = contractFromSearch.get(0)
            contractToSave.update(contractToUpdate.id)
            callback.onContractUpdated(title)
        }
    }

    override fun getContracts(callback: ContractDataSource.loadContractsCallback) {
        val contractFromSearch:List<Contract>? = DataSupport.select().find(Contract::class.java)
        callback.onContractsLoaded(contractFromSearch)
    }
}
