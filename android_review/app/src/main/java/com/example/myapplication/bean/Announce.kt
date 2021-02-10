package com.example.myapplication.bean

import java.text.SimpleDateFormat
import java.util.*

data class Announce(val id: String) {
    var title: String? = null
    var content: String? = null
    val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
}
