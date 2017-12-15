package com.courseproject.mycontractitegration.addeditcontract

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.showContractList.ContractListActivity
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.local.ContractLocalDataSource
import util.EditTextInputControl

class AddEditContractActivity : AppCompatActivity(),AddEditContractVP.View
{

    lateinit var mPresenter:AddEditContractVP.Presenter
    lateinit  var contentEditText: EditText
    lateinit var titleEditText :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.edit_contract_act)
        //取出使用的模板或者待编辑的合同内容  并在Content区域显示
        mPresenter= AddEditContractPresenter(ContractLocalDataSource().getInstance(),this)
        val mTemplate: Template? = intent.getSerializableExtra("ConfirmedItem") as Template?
        val mContract: Contract? = intent.getSerializableExtra("ContractToEdit") as Contract?
        contentEditText = findViewById(R.id.add_contract_content)
        titleEditText = findViewById(R.id.add_contract_title)
        if(mTemplate != null) {
            val template_content: String = mTemplate.template_content
            val template_title:String = mTemplate.template_name
            titleEditText.setText(template_title.toCharArray(),0,template_title.length)
            contentEditText.setText(template_content.toCharArray(), 0, template_content.length)
        }
        if(mContract != null) {
            val contract_title:String = mContract.title
            val contract_content:String = mContract.content
            titleEditText.setText(contract_title.toCharArray(),0,contract_title.length)
            EditTextInputControl().EditControl(false,titleEditText)
            contentEditText.setText(contract_content.toCharArray(),0,contract_content.length)
        }
        //完成编辑后进行保存
        var buttonFinish = findViewById<Button>(R.id.finish_edit_button)
        buttonFinish.setOnClickListener(object: View.OnClickListener{

            override fun onClick(p0: View?) {
                val inputContent:String = contentEditText.text.toString()
                val inputTitle:String = titleEditText.text.toString()
                mPresenter.saveContract(inputTitle,inputContent)
            }
        })
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
}
