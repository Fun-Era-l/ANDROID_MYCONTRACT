package com.courseproject.mycontractitegration.data

import cn.bmob.v3.BmobObject

class IssueBmob():BmobObject() {
    var user_id:String = ""
    var user_name:String = ""
    var IssueContent:String = ""
    constructor(userId:String,userName:String,content:String):this() {
        this.user_id = userId
        this.user_name = userName
        this.IssueContent = content
    }


}