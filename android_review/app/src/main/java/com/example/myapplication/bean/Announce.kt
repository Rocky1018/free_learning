package com.example.myapplication.bean

import cn.bmob.v3.BmobObject
import java.text.SimpleDateFormat
import java.util.*

data class Announce(val id: String) : BmobObject() {
    var title: String? = null
    var content: String? = null
    val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
}
