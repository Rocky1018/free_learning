package com.example.myapplication.bean

data class Stuff(var stuffId: String) {
    var name: String? = null
    var desc: String? = null
    var img: List<String>? = null
    var owner: User? = null
    var status: String? = null
    var comments: List<Comment>? = null
    var category: String? = null

    data class Comment(
        val commentId: String, val content: String, val publishTime: String,
        val fromUser: User
    )
}
