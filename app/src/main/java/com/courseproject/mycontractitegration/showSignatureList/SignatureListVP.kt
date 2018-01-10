package com.courseproject.mycontractitegration.showSignatureList

import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView
import com.courseproject.mycontractitegration.data.Signature


class SignatureListVP {
    interface View : BaseView<Presenter> {
        fun showSignatureList(signatureList: List<Signature>)
        fun showEmptyListWarning()
        fun showDeleteResult(isDeleted:Boolean)
        fun showSetDefaultResult(isSet:Boolean)
    }


    interface Presenter : BasePresenter {
        fun loadSignatureList()
        fun deleteSignatureById(id:Long)
        fun setDefaultSignature(id:Long)
    }
}


