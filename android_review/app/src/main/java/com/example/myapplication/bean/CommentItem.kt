package com.example.myapplication.bean

import cn.bmob.v3.BmobObject

data class CommentItem(val commentId: String) : BmobObject() {
    var content: String? = null
    var date: String? = null
    var username: String? = null
    var portrait: String? = null
}


