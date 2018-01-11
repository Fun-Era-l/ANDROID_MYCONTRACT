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
    var mContract:Contract? = null
    var mTemplate:Template? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contract_act)

        //取出使用的模板或者待编辑的合同内容  并在Content区域显示
        mPresenter= AddEditContractPresenter(ContractLocalDataSource().getInstance(),this)
        mTemplate  = intent.getSerializableExtra("ConfirmedItem") as Template?
        mContract = intent.getSerializableExtra("ContractToEdit") as Contract?

        contentEditText = findViewById(R.id.add_contract_content)
        titleEditText = findViewById(R.id.add_contract_title)
        if(mTemplate != null) {
            val template_content: String = mTemplate?.template_content!!
            val template_title:String = mTemplate?.template_name!!
            titleEditText.setText(template_title.toCharArray(),0,template_title.length)
            contentEditText.setText(template_content.toCharArray(), 0, template_content.length)
        }
        if(mContract != null) {
            /*
            对合同进行二次编辑时，不允许修改合同名称，若合同已签名则关闭合同的编辑功能；
             */
            if(mContract?.signed!!)
            {
                titleEditText.setText(mContract?.title)
                titleEditText.inputType = InputType.TYPE_NULL
                contentEditText.setText(mContract?.content)
                TextWithImage().show(contentEditText,mContract?.content!!,this@AddEditContractActivity)
                EditTextInputControl().disableShowSoftInput(contentEditText)
            } else {
                val contract_title: String = mContract?.title!!
                val contract_content: String = mContract?.content!!
                titleEditText.setText(contract_title.toCharArray(), 0, contract_title.length)
                EditTextInputControl().EditControl(false, titleEditText)
                contentEditText.setText(contract_content.toCharArray(), 0, contract_content.length)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_contract,menu)
        /*
        新建合同不能被删除或签名 / 已签名合同不能再签名
         */
        if(mTemplate != null){
            menu?.removeItem(R.id.delete_contract)
            menu?.removeItem(R.id.sign_contract)
        }
        if(mContract != null && mContract?.signed!!){
            menu?.removeItem(R.id.sign_contract)
        }
        return true
    }

    /*
    菜单item监听
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            /*
            完成编辑，保存合同
             */
            R.id.finish_edit-> {
                val inputContent: String = contentEditText.text.toString()
                val inputTitle: String = titleEditText.text.toString()
                mPresenter.saveContract(inputTitle, inputContent)
            }
            /*
            对合同进行签名
             */
            R.id.sign_contract->{
                val inputContent: String = contentEditText.text.toString()
                val inputTitle: String = titleEditText.text.toString()
                val contractToSign:Contract = Contract(inputTitle,inputContent)
                val edit2Sign:Intent = Intent(this@AddEditContractActivity,SignContractActivity::class.java)
                edit2Sign.putExtra("ContractToSign",contractToSign)
                startActivity(edit2Sign)
            }
            /*
            删除合同
             */
            R.id.delete_contract->{
                mPresenter.deleteContract(mContract?.id!!)
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
        Toast.makeText(this@AddEditContractActivity,"合同成功保存",LENGTH_SHORT).show()
        var contratEdit2ContractList:Intent = Intent(this@AddEditContractActivity, ContractListActivity::class.java)
        startActivity(contratEdit2ContractList)
    }

    override fun updateSucceeded(title: String) {
        Toast.makeText(this@AddEditContractActivity,"Contract \" $title \" is Updated", LENGTH_SHORT).show()
        val contractEdit2ContractList:Intent = Intent(this@AddEditContractActivity,ContractListActivity::class.java)
        startActivity(contractEdit2ContractList)
    }

    override fun saveFailed() {
        Toast.makeText(this@AddEditContractActivity,"失败，当前无法保存该合同", LENGTH_SHORT).show()
    }
    override fun updateFailed(title: String) {
        Toast.makeText(this@AddEditContractActivity,"失败，当前无法更新该合同", LENGTH_SHORT).show()
    }

    override fun deleteSucceeded() {
        Toast.makeText(this@AddEditContractActivity,"成功删除合同",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AddEditContractActivity,ContractListActivity::class.java))
    }

    override fun  deleteFailed() {
        Toast.makeText(this@AddEditContractActivity,"删除合同失败",Toast.LENGTH_SHORT).show()
    }

}
