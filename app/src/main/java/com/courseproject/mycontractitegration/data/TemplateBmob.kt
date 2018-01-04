package com.courseproject.mycontractitegration.data

import cn.bmob.v3.BmobObject

class TemplateBmob(): BmobObject() {
    var template_name: String = ""
    var template_content:String = ""
    constructor(temp_name:String,temp_content:String):this(){
        this.template_name = temp_name
        this.template_content = temp_content
    }

}