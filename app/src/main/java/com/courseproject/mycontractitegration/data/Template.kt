package com.courseproject.mycontractitegration.data

import org.litepal.crud.DataSupport
import java.io.Serializable

class Template(): DataSupport(),Serializable {
    constructor(name:String,content:String =  "This is a blank template"):this()
    {
         id = 0;
        template_name = name
        template_content =content
    }
    var id:Long? = 0;
    var template_name: String = ""
    var template_content:String = ""

}