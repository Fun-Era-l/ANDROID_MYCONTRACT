package com.courseproject.mycontractitegration.addSignature

import android.graphics.Bitmap
import com.courseproject.mycontractitegration.data.source.SignatureDataSource

class AddSignaturePresenter(dataRepo:SignatureDataSource,mView:AddSignatureVP.View):AddSignatureVP.Presenter,SignatureDataSource.SaveSignatureCallBack
{
    /*
    Model & View to manage
     */
    var mSignatureDataRepo:SignatureDataSource = dataRepo
    var mSignatureView:AddSignatureVP.View = mView

    /*
    override functions succeeded from Save/Get/Load callbacks & Presenter
     */
    override fun oSignatureFailToSave() {
    }

    override fun onSignatureSaved() {
        mSignatureView.signatureSaved()
    }

    override fun saveSignature(signature_name:String,bitmap: Bitmap) {
        mSignatureDataRepo.saveSignature(signature_name,bitmap,this)
    }
    override fun start() {}
/*
Test To Fetch Bitmap From the Database;
Implemented Interface getSignatureCallback

    override fun onFailToGetSignature() {

    }

    override fun onSignatureGot(signature: Signature) {

        var bitmapTest: Bitmap = signature.getSignatureBitmap()
        Log.d("nothing","nothing")
    }

    override fun getSignature(signatureId: Long) {
        mSignatureDataRepo.getSignature(signatureId,this)
    }
    */

}