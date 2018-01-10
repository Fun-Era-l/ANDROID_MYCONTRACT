package com.courseproject.mycontractitegration.data

import android.graphics.Bitmap
import cn.bmob.v3.BmobObject
import org.litepal.crud.DataSupport
import util.ImageOperation
import java.io.Serializable

class Signature():DataSupport(),Serializable{
    var id:Long = 0
    var signature_name:String = ""
    var signature_string: String = ""
    var is_default:Boolean = false
    var bmob_id:String = ""

    constructor(name:String ,bitmap:Bitmap):this()
    {
        this.signature_name = name
        val sig_string:String = ImageOperation().imageToString(bitmap)
        this.signature_string = sig_string
    }

    fun getSignatureBitmap():Bitmap?{
            val mBitmap: Bitmap? = ImageOperation().stringToImage(this.signature_string)
            return mBitmap
    }

}