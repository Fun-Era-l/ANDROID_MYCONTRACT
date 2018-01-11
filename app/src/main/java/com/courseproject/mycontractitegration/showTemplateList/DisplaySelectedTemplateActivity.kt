package com.courseproject.mycontractitegration.showTemplateList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.addeditcontract.AddEditContractActivity
import com.courseproject.mycontractitegration.data.Template
import kotlinx.android.synthetic.main.display_selected_template_act.*


class DisplaySelectedTemplateActivity : AppCompatActivity() {
    var templateItem: Template? = Template("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_selected_template_act)
        setSupportActionBar(temp_toolbar)
        /*
        * 去除actionbar
        val actBar: ActionBar? = supportActionBar
        if(actBar != null)
        {
            actBar.hide()
        }
        **/
        /*
        获得传入的模板数据
         */
        templateItem = intent.getSerializableExtra("SelectedItem") as? Template
        if(templateItem is Template) {
            Log.d("Displate-name", "$templateItem.template_name")
            Log.d("Displate-Content", "$templateItem.template_content")
        }
        display_template_name.setText(templateItem?.template_name)
        display_template_content.setText(templateItem?.template_content)
        /*
        确认使用该模板
         */
        use_template.setOnClickListener(object: View.OnClickListener{

            override fun onClick(p0: View?) {
                var DisTemplate2AddEditContract: Intent = Intent(this@DisplaySelectedTemplateActivity, AddEditContractActivity::class.java)
                DisTemplate2AddEditContract.putExtra("ConfirmedItem",templateItem)
                startActivity(DisTemplate2AddEditContract)
            }
        })


    }
}
