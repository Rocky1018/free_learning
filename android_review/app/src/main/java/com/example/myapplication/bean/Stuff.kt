package com.example.myapplication.bean

data class Stuff(var stuffId: String) {
    var name: String? = null
    var desc: String? = null
    var img: List<String>? = null
    var owner: User? = null
    var status: String? = null
    var comments: String? = null
    var category: String? = null
}
