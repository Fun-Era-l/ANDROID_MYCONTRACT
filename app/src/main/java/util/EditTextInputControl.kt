package util

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import java.util.Locale.filter

class EditTextInputControl()
{
    fun EditControl(value:Boolean, editText:EditText) {
    if (value) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setFilters( arrayOf ( object : InputFilter {
            override fun filter(p0: CharSequence?, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int): CharSequence? {
                return null;
            }
        }
        ))
    } else {
//设置不可获取焦点
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
//输入框无法输入新的内容
        editText.setFilters(arrayOf (object : InputFilter {
            override fun filter(source:CharSequence, start: Int, end:Int,dest:Spanned ,dstart:Int, dend:Int):CharSequence? {
                return if(source.length < 1) dest.subSequence(dstart, dend) else ""
            }
        } ))
    }
}
}