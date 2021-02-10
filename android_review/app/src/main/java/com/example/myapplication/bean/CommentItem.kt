package com.example.myapplication.bean

import cn.bmob.v3.BmobObject
import java.text.SimpleDateFormat
import java.util.*

data class CommentItem(val commentId: String) : BmobObject() {
    var content: String? = null
    var date: String? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    var username: String? = null
    var portrait: String? = null
}


