package com.courseproject.mycontractitegration.signContract

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView
import com.courseproject.mycontractitegration.data.Signature

class SignContractVP
{
    interface View:BaseView<Presenter>
    {
        fun onSigned()
        fun onSignFailed()
        fun onDefaultSigGot(default_sig:Signature)
        fun onFailToGetDefaultSig()
    }
    interface Presenter:BasePresenter
    {
        fun signContract(title:String?,content:String?)
        fun getDefaultSig()
    }
}