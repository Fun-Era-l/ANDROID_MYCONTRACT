package util

import android.app.Notification
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Base64
import android.util.Log


class ImageOperation() {
    /*
    transfer Bitmap To ByteArray
     */
     fun imageToByteArray(bitmap: Bitmap?): ByteArray
    {
        var baos =ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray()
    }
    /*
    transfer ByteArray To Bitmap
     */
    fun byteArrayToImage(byteArray: ByteArray):Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size);
    }
    fun imageToString(bitmap:Bitmap):String{
        val byteCode: ByteArray = ImageOperation().imageToByteArray(bitmap)
        return Base64.encodeToString(byteCode, Base64.DEFAULT)
    }
    fun stringToImage(string: String?):Bitmap? {
        if(string.isNullOrBlank()) {
            return null
        }
        else {
            val mByteArray: ByteArray = Base64.decode(string, Base64.DEFAULT)
            return ImageOperation().byteArrayToImage(mByteArray)
        }
    }

/*
将签名bitmap缩放至
 */
    public fun scaleImage(bitmap: Bitmap?):Bitmap{
        val width = bitmap?.getWidth()
        val height = bitmap?.getHeight()
        val newWidth = 144
        val newHeight = 87.5
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width!!
        val scaleHeight = newHeight.toFloat() / height!!
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth,scaleHeight)
        // 得到新的图片
        val resultBitMap =  Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        return resultBitMap
}
}