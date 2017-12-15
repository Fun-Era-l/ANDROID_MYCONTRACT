package com.courseproject.mycontractitegration.showTemplateList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.addeditcontract.AddEditContractActivity
import com.courseproject.mycontractitegration.data.Template


class DisplaySelectedTemplateActivity : AppCompatActivity() {
    var templateItem: Template? = Template("")
    lateinit var mTemplateName: TextView
    lateinit var mTemplateContent:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DIs","request2")
        setContentView(R.layout.display_selected_template_act)
        Log.d("DIs","request3")
        mTemplateName = findViewById(R.id.display_template_name)
        mTemplateContent = findViewById(R.id.display_template_content)
        templateItem = intent.getSerializableExtra("SelectedItem") as? Template
        if(templateItem is Template) {
            Log.d("Displate-name", "$templateItem.template_name")
            Log.d("Displate-Content", "$templateItem.template_content")
        }
        mTemplateName.setText(templateItem?.template_name)
        mTemplateContent.setText(templateItem?.template_content)

        var buttonSelect = findViewById<Button>(R.id.selectTemplate)
        buttonSelect.setOnClickListener(object: View.OnClickListener{

            override fun onClick(p0: View?) {
                var DisTemplate2AddEditContract: Intent = Intent(this@DisplaySelectedTemplateActivity, AddEditContractActivity::class.java)
                DisTemplate2AddEditContract.putExtra("ConfirmedItem",templateItem)
                startActivity(DisTemplate2AddEditContract)
            }
        })


    }
}
