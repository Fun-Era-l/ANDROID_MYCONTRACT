package com.courseproject.mycontractitegration

import android.app.Application
import android.content.Context
//import android.support.multidex.MultiDex
import org.litepal.LitePal

class MyApplication: Application() {
    lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        context = getApplicationContext()
        LitePal.initialize(context)

    }
}