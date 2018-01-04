package com.courseproject.mycontractitegration.data.source.repository

import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.relationship.Contract_Signature
import com.courseproject.mycontractitegration.data.source.ContractDataSource
import org.litepal.crud.DataSupport

class ContractLocalDataSource() : ContractDataSource
{
    /*返回LocalDataSource的实例*/
    fun getInstance():ContractDataSource{
        return ContractLocalDataSource()
    }

    override fun saveContract(title:String,content:String,callback: ContractDataSource.SaveContractCallback) {
        val contractToSave = Contract(title,content)
        val contractFromSearch:List<Contract>? = DataSupport.select().where("title=?",title).find(Contract::class.java)
        if(contractFromSearch == null || contractFromSearch.isEmpty())
        {
            //优化： 可采用AsyncSave() 异步保存大文件；
            //save()返回值 true / false
           if(contractToSave.save()) {
               callback.onContractSaved()
           }
            else{
               callback.onContractFailToSave()
           }
        }
        else
        {
            //先去重 再更新
            for (i in 1 until contractFromSearch.size) {
                contractFromSearch.get(i).delete()
            }
            val contractToUpdate:Contract = contractFromSearch.get(0)
            if(contractToSave.update(contractToUpdate.id) > -1) {
                callback.onContractUpdated(title)
            }
            else{
                callback.onContractFailToUpdate(title)
            }
        }
    }
    override fun getContracts(callback: ContractDataSource.loadContractsCallback) {
        val contractFromSearch:List<Contract>? = DataSupport.select().find(Contract::class.java)
        callback.onContractsLoaded(contractFromSearch)
    }
/*
合同签名
 */
override fun signContract(title: String,callback: ContractDataSource.SignContractCallBack) {
    val contractToSign:Contract = DataSupport.select().where("title=?",title).limit(1).find(Contract::class.java).get(0)
    if(!contractToSign.signed) {
        contractToSign.signed = true
        }
    if(contractToSign.save())
    {
        val sig_id:Long = DataSupport.select().where("is_default=?","1").find(Signature::class.java).get(0).id
        val contract_id:Long = DataSupport.select().where("title=?",title).find(Contract::class.java).get(0).id
        var contract_sig = Contract_Signature(contract_id,sig_id)
        contract_sig.save()
        callback.onContractIsSigned()
    }
    else
    {
        callback.onSignFailed()
    }
}
}
