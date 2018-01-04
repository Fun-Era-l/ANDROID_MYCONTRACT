package com.courseproject.mycontractitegration.signContract

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.source.repository.ContractLocalDataSource
import com.courseproject.mycontractitegration.data.source.repository.SignatureLocalDataSource
import com.courseproject.mycontractitegration.showContractList.ContractListActivity
import com.courseproject.mycontractitegration.showSignatureList.SignatureListActivity
import kotlinx.android.synthetic.main.sign_contract_act.*
import kotlinx.android.synthetic.main.titlebar_withcheckbutton.*
import util.EditTextInputControl
import util.ImageOperation

class SignContractActivity : AppCompatActivity(),SignContractVP.View {
    var mPresenter:SignContractVP.Presenter
    var mContract:Contract? = null
    var mDefaultSignature:Signature? = null

    init {
        mPresenter = SignContractPresenter(SignatureLocalDataSource(),ContractLocalDataSource(),this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_contract_act)
        val actBar: ActionBar? = supportActionBar
        if (actBar != null) {
            actBar.hide()
        }
        /*
        先获取默认签名模板  如果还未设置则退回签名列表
         */
        mPresenter.getDefaultSig()

        mContract = intent.getSerializableExtra("ContractToSign") as Contract?
        sign_contract_title.setText(mContract?.title)
        sign_contract_content.setText(mContract?.content)
        EditTextInputControl().disableShowSoftInput(sign_contract_content)
        finish_edit_button.setText("确认签名")
        finish_edit_button.visibility = View.INVISIBLE
        finish_edit_button.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                Log.d("sign","${mContract?.content}")
                mPresenter.signContract(mContract?.title,mContract?.content)
            }
        })
        /*
        在光标处签名
         */
        sign_here_button.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(p0: View?) {
                val mEditable:Editable = sign_contract_content.editableText
                var cursor_position:Int =sign_contract_content.getSelectionStart()

                val signatureTag = "<img src='${mDefaultSignature?.bmob_id}'/>"
                val mSpannableString = SpannableString(signatureTag)//(content_editText?.getText().toString());

                val test_bitmap:Bitmap? = ImageOperation().stringToImage(mDefaultSignature!!.signature_string)
                val imageSpan =  ImageSpan(this@SignContractActivity,test_bitmap, ImageSpan.ALIGN_BASELINE);

                mEditable.insert(cursor_position,"\n")
                //cursor_position =sign_contract_content.getSelectionStart()
                mSpannableString.setSpan(imageSpan,0,mSpannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                mEditable.insert(cursor_position, mSpannableString);
                mContract?.title = sign_contract_title.text.toString()
                mContract?.content = sign_contract_content.text.toString()
                finish_edit_button.visibility = View.VISIBLE
            }
        })
        back.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                this@SignContractActivity.finish()
            }
        })
    }

    override fun onSignFailed() {
        Toast.makeText(this@SignContractActivity,"签名失败",Toast.LENGTH_LONG).show()
    }

    override fun onSigned() {
        Toast.makeText(this@SignContractActivity,"签名成功",Toast.LENGTH_LONG).show()
        val sign2ContractList:Intent = Intent(this@SignContractActivity,ContractListActivity::class.java)
        startActivity(sign2ContractList)
    }

    override fun onDefaultSigGot(default_sig: Signature) {
        this.mDefaultSignature = default_sig
    }

    override fun onFailToGetDefaultSig() {
        Toast.makeText(this, "请先设置默认签名!!", Toast.LENGTH_LONG).show()
        val signcontract2Signaturelist:Intent = Intent(this@SignContractActivity,SignatureListActivity::class.java)
        startActivity(signcontract2Signaturelist)
    }


    override fun setPresenter(presenter: SignContractVP.Presenter) {

    }
}