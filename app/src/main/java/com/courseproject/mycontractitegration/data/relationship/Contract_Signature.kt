package com.courseproject.mycontractitegration.data.relationship

import org.litepal.crud.DataSupport

class Contract_Signature():DataSupport() {
    var id: Long = 0
    var contract_id: Long =0
    var signature_id :Long =0
    constructor(cont_id:Long,sign_id:Long):this()
    {
        contract_id = cont_id
        signature_id = sign_id
    }

    override fun equals(other: Any?): Boolean {
        return when(other)
        {
            !is Contract_Signature -> false
            else-> (this === other || (this.contract_id == other.contract_id && this.signature_id == other.signature_id))
        }
    }
    override fun save():Boolean{
        val con_sig_list:List<Contract_Signature>? = DataSupport.select()
                .where("contract_id=? and signature_id=?",this.contract_id.toString(),this.signature_id.toString())
                .find(Contract_Signature::class.java)
        if(con_sig_list == null || con_sig_list.isEmpty())
        {
            return super.save()
        }
        else
        {
            return false
        }
    }

}