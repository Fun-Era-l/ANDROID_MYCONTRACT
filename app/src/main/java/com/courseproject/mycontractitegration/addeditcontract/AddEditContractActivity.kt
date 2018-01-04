package com.courseproject.mycontractitegration.addeditcontract

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.text.InputType
import android.text.Spanned
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import cn.bmob.v3.Bmob
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.SignatureBmob
import com.courseproject.mycontractitegration.showContractList.ContractListActivity
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.repository.ContractLocalDataSource
import com.courseproject.mycontractitegration.signContract.SignContractActivity
import util.EditTextInputControl
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.listener.QueryListener
import util.ImageOperation
import util.TextWithImage


class AddEditContractActivity : AppCompatActivity(),AddEditContractVP.View
{

    lateinit var mPresenter:AddEditContractVP.Presenter
    lateinit  var contentEditText: EditText
    lateinit var titleEditText :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.edit_contract_act)

        //取出使用的模板或者待编辑的合同内容  并在Content区域显示
        mPresenter= AddEditContractPresenter(ContractLocalDataSource().getInstance(),this)
        val mTemplate: Template? = intent.getSerializableExtra("ConfirmedItem") as Template?
        val mContract: Contract? = intent.getSerializableExtra("ContractToEdit") as Contract?
        //var mContract:Contract = Contract("testTITLE","jfkads;furiqeworhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsTTTT<img src='7722e9e59f'/>TTTTfkuzovzncv,mafjsklajfiourqgaksjdfkjaskffjaskdfjkasuifkasjdfioajiqewo<img src='118a5ffc3d'/>rhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsjfkuzovzncvfjaksdfjaksjfas;jfiourqgjkj<img src='6fed91b73e'/>ieriqeworhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsjfkuzovzncv,")

        contentEditText = findViewById(R.id.add_contract_content)
        titleEditText = findViewById(R.id.add_contract_title)
        if(mTemplate != null) {
            val template_content: String = mTemplate.template_content
            val template_title:String = mTemplate.template_name
            titleEditText.setText(template_title.toCharArray(),0,template_title.length)
            contentEditText.setText(template_content.toCharArray(), 0, template_content.length)
        }
        if(mContract != null) {
            if(mContract.signed)
            {
                //showTextImage(mContract.content,add_contract_content)
                /*测试textImage*/
                //var string:String = "jfkads;furiqeworhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsTTTT<img src='7722e9e59f'/>TTTTfkuzovzncv,mafjsklajfiourqgaksjdfkjaskffjaskdfjkasuifkasjdfioajiqewo<img src='490b9beda1'/>rhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsjfkuzovzncvfjaksdfjaksjfas;jfiourqgjkjieriqeworhgajsjfkrquoghajafkljqiewursjfsa;jfqkjweporqfjakldsjfkuzovzncv,"
                titleEditText.setText(mContract.title)
                titleEditText.inputType = InputType.TYPE_NULL
                contentEditText.setText(mContract.content)
                TextWithImage().show(contentEditText,mContract.content,this@AddEditContractActivity)
                EditTextInputControl().disableShowSoftInput(contentEditText)
                Log.d("TAG","FUCKYOU HERE!")
            }
            else {
                val contract_title: String = mContract.title
                val contract_content: String = mContract.content
                titleEditText.setText(contract_title.toCharArray(), 0, contract_title.length)
                EditTextInputControl().EditControl(false, titleEditText)
                contentEditText.setText(contract_content.toCharArray(), 0, contract_content.length)
            }

        }
        Log.d("TAG","WHAT THE FUCK")
    }
    /*
    添加包含 完成编辑 和 签名选项的菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_contract,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.finish_edit-> {
                val inputContent: String = contentEditText.text.toString()
                val inputTitle: String = titleEditText.text.toString()
                mPresenter.saveContract(inputTitle, inputContent)
            }
            R.id.sign_contract->{
                val inputContent: String = contentEditText.text.toString()
                val inputTitle: String = titleEditText.text.toString()
                val contractToSign:Contract = Contract(inputTitle,inputContent)
                val edit2Sign:Intent = Intent(this@AddEditContractActivity,SignContractActivity::class.java)
                edit2Sign.putExtra("ContractToSign",contractToSign)
                startActivity(edit2Sign)
            }
            else->return true
        }
        return false
    }
/*
*   Override functions Implemented from Presenter
*/
    override fun setPresenter(presenter: AddEditContractVP.Presenter) {
        mPresenter = presenter
    }
    override fun saveSucceeded() {
        Toast.makeText(this@AddEditContractActivity,"!!Contract Successfully Saved!!",LENGTH_SHORT).show()
        var contratEdit2ContractList:Intent = Intent(this@AddEditContractActivity, ContractListActivity::class.java)
        startActivity(contratEdit2ContractList)
    }

    override fun updateSucceeded(title: String) {
        Toast.makeText(this@AddEditContractActivity,"Contract \" $title \" is Updated", LENGTH_SHORT).show()
        val contractEdit2ContractList:Intent = Intent(this@AddEditContractActivity,ContractListActivity::class.java)
        startActivity(contractEdit2ContractList)
    }

    override fun saveFailed() {
        Toast.makeText(this@AddEditContractActivity,"!! Failed to Save the Contract!!", LENGTH_SHORT).show()
    }
    override fun updateFailed(title: String) {
        Toast.makeText(this@AddEditContractActivity,"!! Failed to Update the Contract!!", LENGTH_SHORT).show()
    }

    fun showTextImage(source:String,editText: EditText){

        var spanned: Spanned = Html.fromHtml(source, FROM_HTML_MODE_COMPACT,object: Html.ImageGetter {
        override fun getDrawable(source:String): Drawable {
            var sig_bmob:SignatureBmob? = null
            val query = BmobQuery<SignatureBmob>()
            query.getObject(source, object : QueryListener<SignatureBmob>() {
                override fun done(queryObj:SignatureBmob, e: BmobException?) {
                    if (e == null) {
                        sig_bmob = queryObj
                        Log.d("bmob", "失败仍失败")
                    }
                else{
                        Log.d("bmob", "失败：")
                    }
                }})
            val sig_bitmap = ImageOperation().stringToImage(sig_bmob?.signature_string)
            val drawable:Drawable = BitmapDrawable(resources,sig_bitmap)
            return drawable
        }
    }, null);
        editText.setText(spanned)
}

}
