package com.courseproject.mycontractitegration.showSignatureList

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.source.repository.SignatureLocalDataSource
import kotlinx.android.synthetic.main.selected_signature_act.*
import util.ImageOperation

class SelectedSignatureActivity : AppCompatActivity(),SignatureListVP.View {
    lateinit var signatureItem: Signature
    lateinit var mImageHolder:ImageView
    lateinit var mDeleteButton:Button
    lateinit var mPresenter:SignatureListVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selected_signature_act)
        /*
        获取选中的签名并转化为bitmap
         */
        signatureItem = intent.getSerializableExtra("SelectedItem") as Signature
        if(signatureItem.is_default)
        {
            Toast.makeText(this@SelectedSignatureActivity,"默认签名",Toast.LENGTH_LONG).show()
        }
        val sig_bitmap:Bitmap? = ImageOperation().stringToImage(signatureItem.signature_string)
        mImageHolder = findViewById(R.id.selected_signature) //显示签名图片区域
        mImageHolder.setImageBitmap(sig_bitmap)
        mPresenter = SignatureListPresenter(SignatureLocalDataSource().getInstance(), this)
/*
删除签名按键
 */
        delete_signature.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mPresenter.deleteSignatureById(signatureItem.id)
                val backToList = Intent(this@SelectedSignatureActivity,SignatureListActivity::class.java)
                startActivity(backToList)
            }
        })
        /*
        设置默认签名按键
         */
        set_default.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(p0: View?) {
                mPresenter.setDefaultSignature(signatureItem.id)
            }
        })
    }

    override fun showDeleteResult(isDeleted:Boolean) {
        if(isDeleted){
        Toast.makeText(this,"Signature Deleted!",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this,"Fail To Delete!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun showEmptyListWarning() {}

    override fun showSignatureList(signatureList: List<Signature>) {}

    override fun setPresenter(presenter: SignatureListVP.Presenter) {}

    override fun showSetDefaultResult(isSet: Boolean) {
        if(isSet)
        {
            Toast.makeText(this@SelectedSignatureActivity,"成功设置默认签名",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@SelectedSignatureActivity,SignatureListActivity::class.java))
        }
        else
        {
            Toast.makeText(this@SelectedSignatureActivity,"设置默认签名失败",Toast.LENGTH_LONG).show()
        }
    }
}
