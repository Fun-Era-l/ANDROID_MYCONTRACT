package com.courseproject.mycontractitegration.addSignature

import android.graphics.Bitmap
import com.courseproject.mycontractitegration.BasePresenter
import com.courseproject.mycontractitegration.BaseView

public class AddSignatureVP
{
    interface View:BaseView<Presenter>
    {
        fun signatureSaved()
    }
    interface Presenter: BasePresenter
    {
        fun saveSignature(signature_name:String,bitmap: Bitmap)
        /*
        Test to fetch signatureBitmap from the database

        fun getSignature(signatureId:Long)*/
    }

}