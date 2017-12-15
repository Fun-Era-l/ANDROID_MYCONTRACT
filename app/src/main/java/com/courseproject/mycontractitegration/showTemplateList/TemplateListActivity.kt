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
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.Template
import com.courseproject.mycontractitegration.data.source.local.TemplateLocalDataSource
import org.litepal.crud.DataSupport


class TemplateListActivity : AppCompatActivity(),TemplateListVP.View {
    //private var templateList: List<Template> = ArrayList<Template> ()
    lateinit private var mPresenter: TemplateListVP.Presenter

   /* fun initList()
    {
        DataSupport.deleteAll(Template::class.java)
        val one =Template("LabourContract")
        val two =Template("StockContract")
        val three =Template("SoftwareTradeContract")
        val four =Template("IndustryTradeContract")
        /* initialize the Contract Table*/
        val contract_default: Contract = Contract("default_title","default_content")
        one.save()
        two.save()
        three.save()
        four.save()
        contract_default.save()
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_list_act)
        /* 初始化mPresenter  使用LocalData代替DataRepo */
        mPresenter = TemplateListPresenter(TemplateLocalDataSource().getInstance(),this)
        mPresenter.loadTemplateList()
        val tempListView:ListView = findViewById<ListView>(R.id.template_list)
        tempListView.setOnItemClickListener(object: AdapterView.OnItemClickListener
        {

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(id.toInt() == -1) {
                    Log.d("id-positionError", "ClickOnHeader")
                }
                val realPosition:Int = id.toInt()
                val item : Template? = parent?.getItemAtPosition(realPosition) as? Template
                var templateList2display:Intent = Intent(this@TemplateListActivity, DisplaySelectedTemplateActivity::class.java)
                templateList2display.putExtra("SelectedItem",item)
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
        Toast.makeText(this,"You Got No Templates !",Toast.LENGTH_LONG).show()
    }

}
