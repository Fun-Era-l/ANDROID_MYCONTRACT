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

/*
工具类，显示签名后的合同，即带有图片tag的文本
        @tagCount ：搜索到的tag个数
        @count：    定义在while循环外，作为循环anchors的索引
        @mSpannableStringBuilder:SpannableStringBuilder：由待显示的string构造而成，对其进行操作替换后作为填入edittext的内容
        @anchors   ：存储每个tag的开始与结束位置，长度为2*tagCount
        @regStr = "<img src='..........'/>" ：用于匹配tag的正则表达式
        @pattern = Pattern.compile(regStr)： 由regStr创建模式
        @matcher = pattern.matcher(string)： 在string中进行匹配；
        matcher.find() 每搜索到一个tag就再次使用模式'..........'匹配提取出matcher.group()中的id信息

 */
class TextWithImage {
    fun show(editText: EditText,string: String,context: Context){
        var tagCount = 0
        var count = 0
        var mSpannableStringBuilder:SpannableStringBuilder = SpannableStringBuilder(string)
        var anchors = ArrayList<Int>()
        val regStr = "<img src='..........'/>"
        val pattern = Pattern.compile(regStr)
        val matcher = pattern.matcher(string)
        while (matcher.find()) {
            tagCount += 1
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
            /*
            访问，获取到签名内容后设置spannableStringBuilder
             */
            query.getObject(objId, object : QueryListener<SignatureBmob>() {
                override fun done(queryObj: SignatureBmob, e: BmobException?) {
                    if (e == null) {
                        val start: Int = anchors[count]
                        count += 1
                        val end: Int = anchors[count]
                        count += 1

                        val sig_bitmap: Bitmap? = ImageOperation().stringToImage(queryObj.signature_string)
                        val imageSpan = ImageSpan(context, sig_bitmap, ImageSpan.ALIGN_BASELINE);
                        mSpannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        editText.setText(mSpannableStringBuilder)
                    }
                    else{
                        Log.d("TAG", "失败")
                    }
                }})
        }
    }
}