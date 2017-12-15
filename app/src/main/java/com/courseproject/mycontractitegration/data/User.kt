package com.courseproject.mycontractitegration.data

import org.litepal.crud.DataSupport
import util.DBSupport
import java.util.Date
import java.util.*


class User() : DataSupport(){
    var user_name : String = ""
    var register_time : Date = Date()
    var phone_number: Long = 0
    init
    {
        var time_calendar: Calendar = Calendar.getInstance()
        register_time = time_calendar.getTime()
    }
    constructor(name:String, p_number: Long = 0):this() {
        this.user_name = name
        this.phone_number = p_number
    }

}

