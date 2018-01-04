package com.courseproject.mycontractitegration.showSignatureList

import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.source.SignatureDataSource

class SignatureListPresenter(signatureDataSource: SignatureDataSource, view: SignatureListVP.View):
        SignatureListVP.Presenter, SignatureDataSource.LoadSignatureCallBack,SignatureDataSource.DeleteSignatureCallBack {
    //暂时采用LocalDataSource代替Repository
    private var mDataSource: SignatureDataSource = signatureDataSource

    private var mSignatureListView: SignatureListVP.View = view

    override fun onSignatureLoaded(signatureList: List<Signature>) {
        mSignatureListView.showSignatureList(signatureList)
    }
    override fun loadSignatureList() {
        mDataSource.loadSignature(this)
    }

    override fun deleteSignatureById(id: Long) {
     mDataSource.deleteSignatureById(id,this)
    }

    override fun onSignatureDelete() {
        mSignatureListView.showDeleteResult(true)
    }

    override fun onFailToDelete() {
        mSignatureListView.showDeleteResult(false)
    }

    override fun setDefaultSignature(id: Long) {
        if(mDataSource.setDefaultSignature(id))
        {
            mSignatureListView.showSetDefaultResult(true)
        }
        else
        {
            mSignatureListView.showSetDefaultResult(false)
        }
    }


    override fun start() {

    }

    override fun onLoadSignatureFailed() {

    }
}