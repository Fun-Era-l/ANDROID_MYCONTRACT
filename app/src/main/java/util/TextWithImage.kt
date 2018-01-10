package util

import android.content.Context
import android.graphics.Bitmap
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.widget.EditText
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.courseproject.mycontractitegration.data.SignatureBmob
import java.util.regex.Pattern

class TextWithImage {
    fun show(editText: EditText,string: String,context: Context){
       //var source = string
        var tagCount = 0
        var count = 0
        var mSpannableStringBuilder:SpannableStringBuilder = SpannableStringBuilder(string)
        var anchors = ArrayList<Int>()
        val regStr:String = "<img src='..........'/>"
        val pattern = Pattern.compile(regStr)
        val matcher = pattern.matcher(string)
        while (matcher.find()) {
            tagCount += 1
            Log.d("TAG tagCount",tagCount.toString())
            val item:String = matcher.group()
            anchors.add(matcher.start())
            anchors.add(matcher.end())
            val i_pattern= Pattern.compile("'..........'")
            val i_matcher=i_pattern.matcher(item)
            var objId = ""
            if(i_matcher.find()) {
                objId = i_matcher.group().substring(1, 11)

            }
            val query = BmobQuery<SignatureBmob>()
            query.getObject(objId, object : QueryListener<SignatureBmob>() {
                override fun done(queryObj: SignatureBmob, e: BmobException?) {
                    if (e == null) {
                        val start: Int = anchors[count]
                        count += 1
                        val end: Int = anchors[count]
                        count += 1
                        var sig_bitmap: Bitmap? = ImageOperation().stringToImage(queryObj.signature_string)
                        val imageSpan = ImageSpan(context, sig_bitmap, ImageSpan.ALIGN_BASELINE);
                        mSpannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        editText.setText(mSpannableStringBuilder)
                    }
                    else{
                        Log.d("TAG", "失败：")
                    }
                }})
        }
    }
}