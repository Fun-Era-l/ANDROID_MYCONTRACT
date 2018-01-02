package com.courseproject.mycontractitegration

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import cn.bmob.newim.BmobIM
import cn.bmob.v3.Bmob
//import android.support.multidex.MultiDex
import org.litepal.LitePal

class MyApplication: Application() {
    lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        context = getApplicationContext()
        LitePal.initialize(context)
        //BmobIM.init(this);
        //BmobIM.registerDefaultMessageHandler(MessageHandler());
        //Bmob.initialize(this,"ebd1120bf9829898687b4f2cb332df4b");
        val bmobAppKey:String = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("Bmob_APP_KEY")
        Bmob.initialize(this,bmobAppKey);
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(MessageHandler());
    }
}