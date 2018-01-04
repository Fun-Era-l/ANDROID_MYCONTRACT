package com.courseproject.mycontractitegration.signContract

import com.courseproject.mycontractitegration.data.source.ContractDataSource
import com.courseproject.mycontractitegration.data.source.SignatureDataSource
import com.courseproject.mycontractitegration.data.Signature

class SignContractPresenter(signatureDataSource: SignatureDataSource,contractDataSource: ContractDataSource,
                            signContractView:SignContractVP.View):
        SignContractVP.Presenter,ContractDataSource.SaveContractCallback,ContractDataSource.SignContractCallBack, SignatureDataSource.GetDefaultSigCallBack
{
    var mSigDataRepo:SignatureDataSource = signatureDataSource
    var mContractDataRepo:ContractDataSource = contractDataSource
    var mSignContractView:SignContractVP.View = signContractView

    override fun signContract(title: String?, content: String?) {
        if(title.isNullOrEmpty() || content.isNullOrEmpty())
        {
            mSignContractView.onSignFailed()
        }
        else{
        mContractDataRepo.saveContract(title!!,content!!,this)
        mContractDataRepo.signContract(title,this)
        }
    }

    override fun onContractIsSigned() {
        mSignContractView.onSigned()
    }

    override fun onSignFailed() {
        mSignContractView.onSignFailed()
    }
    override fun onDefaultSigGot(defaultSignature: Signature) {
        mSignContractView.onDefaultSigGot(defaultSignature)
    }
    override fun onFailToGetDefaultSig() {
        mSignContractView.onFailToGetDefaultSig()
    }

    override fun getDefaultSig() {
        mSigDataRepo.getDefaultSignature(this)
    }




    override fun onContractFailToSave() {}
    override fun onContractFailToUpdate(updatedContractTitle: String) {}
    override fun onContractSaved() {}
    override fun onContractUpdated(updatedContractTitle: String) {}
    override fun start() {}
}