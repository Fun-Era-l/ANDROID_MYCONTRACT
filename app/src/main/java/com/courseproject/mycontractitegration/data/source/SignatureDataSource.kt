package com.courseproject.mycontractitegration.data.source

import android.graphics.Bitmap
import com.courseproject.mycontractitegration.data.Signature

interface SignatureDataSource
{
    interface SaveSignatureCallBack
    {
        fun onSignatureSaved()
        fun oSignatureFailToSave()
    }
    interface LoadSignatureCallBack
    {
        fun onSignatureLoaded(signatureList:List<Signature>)
        fun onLoadSignatureFailed()
    }
    interface GetSignatureCallBack
    {
        fun onSignatureGot(signature: Signature)
        fun onFailToGetSignature()
    }
    interface DeleteSignatureCallBack
    {
        fun onSignatureDelete()
        fun onFailToDelete()
    }
    interface GetDefaultSigCallBack
    {
        fun onDefaultSigGot(defaultSignature:Signature)
        fun onFailToGetDefaultSig()
    }

    fun saveSignature(name:String,bitmap: Bitmap,callBack: SaveSignatureCallBack)
    fun getSignatureById(signatureId:Long, callBack: GetSignatureCallBack)
    fun getSignatureByName(signature_name:String,callBack: GetSignatureCallBack)
    fun loadSignature(callBack: LoadSignatureCallBack)
    fun deleteSignatureById(sig_id:Long,callBack: DeleteSignatureCallBack)
    fun getDefaultSignature(callBack: GetDefaultSigCallBack)
    fun setDefaultSignature(id:Long):Boolean
}