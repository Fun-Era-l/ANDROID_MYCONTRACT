package com.courseproject.mycontractitegration.data.source.repository

import android.graphics.Bitmap
import android.util.Log
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.SignatureBmob
import com.courseproject.mycontractitegration.data.source.SignatureDataSource
import org.litepal.crud.DataSupport

class SignatureLocalDataSource():SignatureDataSource
{
    fun getInstance(): SignatureLocalDataSource
    {
        val instance = SignatureLocalDataSource()
        return instance
    }
    override fun getSignatureById(signatureId:Long,callBack: SignatureDataSource.GetSignatureCallBack) {
        val signatureTemp = DataSupport.find(Signature::class.java,signatureId)
        if(signatureTemp != null)
        {
            callBack.onSignatureGot(signatureTemp)
        }
        callBack.onFailToGetSignature()
    }

    override fun loadSignature(callBack: SignatureDataSource.LoadSignatureCallBack) {

        var mSignatureList:List<Signature>? =  DataSupport.select().find(Signature::class.java)
        if(mSignatureList != null ) {
                if(mSignatureList.isNotEmpty()) {
                    callBack.onSignatureLoaded(mSignatureList)
                    return
                }
        }
        callBack.onLoadSignatureFailed()
    }

    override fun saveSignature(name:String,bitmap: Bitmap, callBack: SignatureDataSource.SaveSignatureCallBack) {

        val mSignature = Signature(name,bitmap) // 本地数据
    /*
          2018-1-1  新增保存到bmob功能
        val sig_bmob = SignatureBmob(mSignature) // Bmob端签名

    //异步执行bmob端数据的存储  并在完成后更新本地数据的bmob_id项
        sig_bmob.save(object:SaveListener<String>()
        {
            override fun done(objectId: String?, exception: BmobException?) {
                if(exception != null)
                {
                    Log.d("BMOB-FailSaveSignature","${mSignature.id}")
                }
                else
                {
                    var localSignature:Signature = DataSupport.select().where("signature_name=?",name).find(Signature::class.java).get(0)
                    if(!objectId.isNullOrEmpty())
                    {
                        localSignature.bmob_id = objectId!!
                        localSignature.save()
                    }
                }
            }
        })
*/

        var signatures:List<Signature>? = DataSupport.select().where("signature_name=?",name).find(Signature::class.java)
        if(signatures == null || signatures.isEmpty())
        {
            try {
                if(mSignature.save()) {
                    callBack.onSignatureSaved()
                }
            }
            catch (e: Exception){
                e.printStackTrace();
                callBack.oSignatureFailToSave()
            }
        }
        else
        {
            for (i in 1 until signatures.size) {
                signatures[i].delete()
            }
            val signatureToUpdate:Signature = signatures[0]
            if(mSignature.update(signatureToUpdate.id) > -1) {
                callBack.onSignatureSaved()
            }
            else{
                callBack.oSignatureFailToSave()
            }
        }
    }

    override fun getSignatureByName(signature_name: String, callBack: SignatureDataSource.GetSignatureCallBack) {
        val signatureList = DataSupport.select().where("signature_name=?",signature_name).find(Signature::class.java)
        if(!signatureList.isEmpty()) {
            val signatureTemp: Signature = signatureList[0]
            callBack.onSignatureGot(signatureTemp)
            return
        }
        callBack.onFailToGetSignature()
    }

    override fun deleteSignatureById(sig_id: Long, callBack: SignatureDataSource.DeleteSignatureCallBack) {
        if(DataSupport.delete(Signature::class.java,sig_id) != 0)
        {
            callBack.onSignatureDelete()
        }
        else
        {
            callBack.onFailToDelete()
        }
    }

    override fun getDefaultSignature(callBack: SignatureDataSource.GetDefaultSigCallBack) {
        val sigList:List<Signature>? = DataSupport.select().where("is_default=?","1").find(Signature::class.java)
        if(sigList == null || (sigList!= null &&sigList.isEmpty())){
            callBack.onFailToGetDefaultSig()
        }else{
        val defaultSignature:Signature = sigList.get(0)
            callBack.onDefaultSigGot(defaultSignature)
        }
    }
/*
在设置默认签名时自动同步到bmob上
 */
    override fun setDefaultSignature(id:Long):Boolean
    {
        val sig_list:List<Signature> = DataSupport.select().where("is_default=?","1").find(Signature::class.java)
        if(sig_list.isNotEmpty()) {
            var defaultSig: Signature = sig_list.get(0)
            defaultSig.is_default = false
            defaultSig.save()
        }

        var signatureToSet:Signature? = DataSupport.find(Signature::class.java,id)
        if(signatureToSet != null) {
            signatureToSet.is_default = true
            signatureToSet.save()

            val sig_Bmob = SignatureBmob(signatureToSet)
            sig_Bmob.save(object :SaveListener<String>()
            {
                override fun done(p0: String?, p1: BmobException?) {
                    if(p1 == null)
                    {
                        if(p0 != null) {
                            signatureToSet.bmob_id = p0
                            signatureToSet.save()

                        }
                        else
                        {
                            Log.d("SignatureBmob","${p1}")
                        }
                    }
                }
            })

            return true
        }
        else
        {
            return false
        }
    }
}
