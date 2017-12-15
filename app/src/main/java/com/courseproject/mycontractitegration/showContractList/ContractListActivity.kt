package com.courseproject.mycontractitegration.showContractList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.addeditcontract.AddEditContractActivity
import com.courseproject.mycontractitegration.showTemplateList.TemplateListActivity
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.source.local.ContractLocalDataSource

//cd data/data/com.contract.mycontract/databases

class ContractListActivity : AppCompatActivity(),ContractListVP.View {
    lateinit var mPresenter:ContractListVP.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_list_act)
        mPresenter = ContractListPresenter(ContractLocalDataSource().getInstance(),this)
        mPresenter.loadContractList()
        val contractListView: ListView = findViewById<ListView>(R.id.contract_list)
        contractListView.setOnItemClickListener(object: AdapterView.OnItemClickListener
        {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(id.toInt() == -1) {
                    Log.d("id-positionError", "ClickOnHeader")
                }
                val realPosition:Int = id.toInt()
                val item : Contract? = parent?.getItemAtPosition(realPosition) as? Contract
                var contractList2addEdit:Intent = Intent(this@ContractListActivity, AddEditContractActivity::class.java)
                contractList2addEdit.putExtra("ContractToEdit",item)
                startActivity(contractList2addEdit)
            }
        })
        var buttonAdd: Button = findViewById<Button>(R.id.add)
        buttonAdd.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                var main2TemplateSelect: Intent = Intent(this@ContractListActivity, TemplateListActivity::class.java)
                startActivity(main2TemplateSelect)
            }
        })
    }

    override fun showContractList(tempList: List<Contract>) {
        val mContractAdapter = ContractAdapter(this,R.layout.contract_item,tempList)
        val mContractListView : ListView = findViewById(R.id.contract_list)
        mContractListView.adapter = mContractAdapter;

    }

    override fun showEmptyListWarning() {}

    override fun setPresenter(presenter: ContractListVP.Presenter) {
        mPresenter = presenter
    }


}
