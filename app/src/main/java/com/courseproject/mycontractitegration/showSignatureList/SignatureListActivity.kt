package com.courseproject.mycontractitegration.showSignatureList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.addSignature.AddSignatureActivity
import com.courseproject.mycontractitegration.data.Signature
import com.courseproject.mycontractitegration.data.source.repository.SignatureLocalDataSource

class SignatureListActivity : AppCompatActivity(),SignatureListVP.View {

    lateinit private var mPresenter: SignatureListVP.Presenter
    lateinit var signatureListView:ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signature_list_act)

//        /*
//        2018/1/2  Bmob 应用密钥初始化
//         */
//        val bmobAppKey:String = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("BMOB_APP_KEY")
//        Bmob.initialize(this,bmobAppKey);

        /* 初始化mPresenter  使用LocalD ata代替DataRepo */
        mPresenter = SignatureListPresenter(SignatureLocalDataSource().getInstance(), this)
        mPresenter.loadSignatureList()

        signatureListView = findViewById(R.id.signature_list)
        signatureListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (id.toInt() == -1) {
                    Log.d("id-positionError", "ClickOnHeader")
                }
                val realPosition: Int = id.toInt()
                val item: Signature? = parent?.getItemAtPosition(realPosition) as? Signature
                var signatureList2display = Intent(this@SignatureListActivity, SelectedSignatureActivity::class.java)
                signatureList2display.putExtra("SelectedItem", item)
                startActivity(signatureList2display)
            }
        })
        var addSigButton: Button =findViewById(R.id.add_signature)
        addSigButton.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                var sigList2AddSignature: Intent = Intent(this@SignatureListActivity, AddSignatureActivity::class.java)
                startActivity(sigList2AddSignature)
            }
        })

    }

    override fun showEmptyListWarning() {
        Toast.makeText(this@SignatureListActivity,"No Signture Found In Database Yet",Toast.LENGTH_SHORT).show()
    }

    override fun showSignatureList(signatureList: List<Signature>) {
        var mSignatureAdapter = SignatureAdapter(this,R.layout.signature_item, signatureList)
        var mSignatureListView : ListView = findViewById(R.id.signature_list)
        mSignatureListView.adapter = mSignatureAdapter;
    }

    override fun setPresenter(presenter: SignatureListVP.Presenter) {
        val sig_presenter = SignatureListPresenter(SignatureLocalDataSource().getInstance(), this)
        this.mPresenter = sig_presenter
    }
    override fun showDeleteResult(isDeleted:Boolean) {}
    override fun showSetDefaultResult(isSet: Boolean) {}
}
