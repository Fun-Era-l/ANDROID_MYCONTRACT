package com.courseproject.mycontractitegration.addSignature

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import android.media.Image
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Contract
import kotlinx.android.synthetic.main.activity_signature.view.*
import util.ImageOperation
import java.io.*


class SignatureView(context: Context, attrs: AttributeSet) : View(context, attrs),AddSignatureVP.View {

    private val paint = Paint()
    private val path = Path()
    private var mBitmap: Bitmap? = null
    lateinit private var mPresenter: AddSignatureVP.Presenter
    /**
     * Optimizes painting by invalidating the smallest possible area.
     */
    private var lastTouchX: Float = 0.toFloat()
    private var lastTouchY: Float = 0.toFloat()
    private val dirtyRect = RectF()

    init {

        paint.setAntiAlias(true)
        paint.setColor(Color.BLACK)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeJoin(Paint.Join.ROUND)
        paint.setStrokeWidth(STROKE_WIDTH)
    }
    /*
        override interfaces from AddSignatureVP.View
    */
    override fun setPresenter(presenter: AddSignatureVP.Presenter) {
        mPresenter = presenter
    }

    override fun signatureSaved() {
        Toast.makeText(context,"成功保存签名",Toast.LENGTH_SHORT).show()
    }
    /**
     * Erases the signature.
     */
   fun clear() {
        path.reset()
        // Repaints the entire view.
        invalidate()
    }
    fun saveToSDCard(bitmap:Bitmap?):Boolean
    {
        var fout :FileOutputStream? = null
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            val sdCardDir:String = "${Environment.getExternalStorageDirectory()}/BitmapTrial"
            val dirFile:File = File(sdCardDir)
            if(!dirFile.exists())
            {
                dirFile.mkdirs()
//                if(dirFile.mkdirs()) {
//                    Toast.makeText(context, "创建文件夹：BitmapTrial", Toast.LENGTH_LONG).show()
//                }
            }
            val bitmapFile = File(sdCardDir,"${System.currentTimeMillis()}_bitmap.png")
            if (bitmapFile.exists()) {
                bitmapFile.delete()
            }
            try {
                bitmapFile.createNewFile()
//                Toast.makeText(context, "成功保存签名到SD卡", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try{
                fout = FileOutputStream(bitmapFile)
                bitmap?.compress(Bitmap.CompressFormat.PNG,100,fout)
//                Toast.makeText(context,"压缩，保存",Toast.LENGTH_LONG)
            }
            catch (e:FileNotFoundException)
            {
                e.printStackTrace();

            }
            try {
                fout?.flush()
                fout?.close()
            }
            catch (e:IOException)
            {
                e.printStackTrace()
            }
            return true;
        }
        return false
    }
    fun save(signature_name:String,signatureView:View) {
        if (mBitmap == null) {
            signatureView.buildDrawingCache()  //启用DrawingCache并创建位图
            mBitmap = Bitmap.createBitmap(signatureView.getDrawingCache()) //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        }
        //val smallSignature: Bitmap = ImageOperation().scaleImage(mBitmap) bitmap缩放
        /*save signature in database through mPresenter*/
        mPresenter.saveSignature(signature_name,mBitmap!!)
        /*
        save Bitmap to the SDCard
         */
        saveToSDCard(mBitmap)
    }
    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(eventX, eventY)
                lastTouchX = eventX
                lastTouchY = eventY
                // There is no end point yet, so don't waste cycles invalidating.
                return true
            }

            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                // Start tracking the dirty region.
                resetDirtyRect(eventX, eventY)

                // When the hardware tracks events faster than they are delivered, the
                // event will contain a history of those skipped points.
                val historySize = event.historySize
                for (i in 0 until historySize) {
                    val historicalX = event.getHistoricalX(i)
                    val historicalY = event.getHistoricalY(i)
                    expandDirtyRect(historicalX, historicalY)
                    path.lineTo(historicalX, historicalY)
                }

                // After replaying history, connect the line to the touch point.
                path.lineTo(eventX, eventY)
            }

            else -> {
                Log.d("Ignored touch event: " ," ${event.toString()}")
                return false
            }
        }

        // Include half the stroke width to avoid clipping.
        invalidate(
                (dirtyRect.left - HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.top - HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.right + HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.bottom + HALF_STROKE_WIDTH).toInt())

        lastTouchX = eventX
        lastTouchY = eventY

        return true
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * points.
     */
    private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private fun resetDirtyRect(eventX: Float, eventY: Float) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX)
        dirtyRect.right = Math.max(lastTouchX, eventX)
        dirtyRect.top = Math.min(lastTouchY, eventY)
        dirtyRect.bottom = Math.max(lastTouchY, eventY)
    }

    companion object {

        private val STROKE_WIDTH = 5f
        /** Need to track this so the dirty region can accommodate the stroke.  */
        private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2
    }
}
