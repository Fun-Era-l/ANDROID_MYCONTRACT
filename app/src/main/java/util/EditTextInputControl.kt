package util

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import android.text.InputType
import java.lang.reflect.Method


class EditTextInputControl()
{
    /*
    限制edittext输入功能，不显示光标
     */
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
        }))
        }
    }

    /*
    限制软件盘弹出，但仍显示光标位置，用于“在此处签名”功能
     */
    fun disableShowSoftInput(editText: EditText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL)
        } else {
            val cls = EditText::class.java
            var method: Method
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                method.setAccessible(true)
                method.invoke(editText, false)
            } catch (e: Exception) {
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
                method.setAccessible(true)
                method.invoke(editText, false)
            } catch (e: Exception) {
            }

        }
    }
}