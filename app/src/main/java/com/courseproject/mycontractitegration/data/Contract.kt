package com.courseproject.mycontractitegration.data

import org.litepal.crud.DataSupport
import util.DBSupport
import java.io.Serializable

class Contract(): DataSupport(),Serializable {

    var id: Long = 0
    var title: String = ""
    var content: String = ""
    constructor(title:String = "",content:String = ""):this()
    {
        id = 0
        this.title = title
        this.content = content
    }
}