package com.courseproject.mycontractitegration.addSignature

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import cn.bmob.v3.Bmob
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.SignatureBmob
import com.courseproject.mycontractitegration.data.source.repository.SignatureLocalDataSource
import com.courseproject.mycontractitegration.showSignatureList.SignatureListActivity
import util.ShakeDetector

class AddSignatureActivity : AppCompatActivity() {
    lateinit var saveSignature:Button
    lateinit var signatureView: SignatureView
    lateinit var signatureNameEditText:EditText
    lateinit var imgHolder:ImageView
    lateinit var mShakeDetector:ShakeDetector
    var sig_name:String = ""
    lateinit var mPresenter: AddSignatureVP.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        signatureView = findViewById(R.id.signature_view)
        saveSignature = findViewById(R.id.save_signature)
        signatureNameEditText = findViewById(R.id.signature_name)
        imgHolder = findViewById(R.id.signatureHolder)

        mShakeDetector = ShakeDetector(this@AddSignatureActivity)
        mShakeDetector.registerOnShakeListener(object: ShakeDetector.OnShakeListener {
            override fun onShake() {
                signatureView.clear()
            }
        })

       /*
        初始化view 和 presenter
         */
        mPresenter = AddSignaturePresenter(SignatureLocalDataSource().getInstance(),signatureView)
        signatureView.setPresenter(mPresenter)
        saveSignature.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                sig_name = signatureNameEditText.text.toString()
                if(sig_name.length == 0)
                {
                    Toast.makeText(this@AddSignatureActivity,"请输入该签名的名称",Toast.LENGTH_LONG).show()
                }
                else{

                ActivityCompat.requestPermissions(this@AddSignatureActivity, arrayOf(android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE,android
                        .Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_PHONE_STATE), 1);
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            1->if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                signatureView.setDrawingCacheEnabled(true)
                signatureView.save(sig_name,signatureView)
                val backToList = Intent(this@AddSignatureActivity,SignatureListActivity::class.java)
                startActivity(backToList)

                //signatureView?.setDrawingCacheEnabled(false)
            }
        }
    }
    override fun onResume()
    {
        this.mShakeDetector.start()
        super.onResume()
    }

    override fun onPause() {
        this.mShakeDetector.stop()
        super.onPause()
    }

}
