package com.example.myapplication.bean

import cn.bmob.v3.BmobObject
import java.util.*

data class Stuff(var stuffId: String = UUID.randomUUID().toString()) : BmobObject() {
    var name: String? = null
    var desc: String? = null
    var img: String? = null
    var owner: User? = null
    var status: String? = null
    var price: String? = null
    var comments: List<CommentItem>? = null
    var category: String? = null
    var publishTime: String? = null

}
