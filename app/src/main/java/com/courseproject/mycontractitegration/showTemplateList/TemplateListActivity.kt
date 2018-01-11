package com.courseproject.mycontractitegration.showTemplateList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.repository.TemplateDataRepo
import com.courseproject.mycontractitegration.showContractList.ContractListActivity


class TemplateListActivity : AppCompatActivity(),TemplateListVP.View {
    lateinit private var mPresenter: TemplateListVP.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_list_act)
        /* 初始化mPresenter
        **/
        mPresenter = TemplateListPresenter(TemplateDataRepo().getInstance(), this)
        /*
        加载模板列表
         */
        mPresenter.loadTemplateList()
        /*
        点击模板项，进入模板展示页面
         */
        val tempListView: ListView = findViewById<ListView>(R.id.template_list)
        tempListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (id.toInt() == -1) {
                    Log.d("id-positionError", "ClickOnHeader")
                }
                val realPosition: Int = id.toInt()
                val item: Template? = parent?.getItemAtPosition(realPosition) as? Template
                var templateList2display: Intent = Intent(this@TemplateListActivity, DisplaySelectedTemplateActivity::class.java)
                templateList2display.putExtra("SelectedItem", item)
                startActivity(templateList2display)

            }
        })

    }

    override fun setPresenter(presenter: TemplateListVP.Presenter) {
        mPresenter = presenter;
    }

    override fun showTemplateList(tempList: List<Template>) {

        var templateAdapter = TemplateAdapter(this,R.layout.template_item,tempList)
        var templateListView : ListView = findViewById(R.id.template_list)
        templateListView.adapter = templateAdapter;
    }

    override fun showEmptyListWarning() {
        Toast.makeText(this,"无模板数据，请检查你的网络连接是否正常!!",Toast.LENGTH_LONG).show()
        this@TemplateListActivity.finish()
    }
    /*
重写在模板列表活动下的系统返回键，直接返回到主界面
 */
    override fun onBackPressed() {
        val templist2Entrance:Intent = Intent(this@TemplateListActivity, ContractListActivity::class.java)
        startActivity(templist2Entrance)
        super.onBackPressed()
    }

}
