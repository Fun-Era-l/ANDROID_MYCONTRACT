package com.courseproject.mycontractitegration.data

import android.util.Log
import android.widget.Toast
import cn.bmob.v3.BmobObject
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import rx.Subscription

class SignatureBmob(): BmobObject(){
    var signature_string:String  = ""
    constructor(signature_source:Signature):this()
    {
        this.signature_string = signature_source.signature_string
    }
    constructor(signature:String):this()
    {
        this.signature_string = signature
    }


}