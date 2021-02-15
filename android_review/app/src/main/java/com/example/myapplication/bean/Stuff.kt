package com.example.myapplication.bean

import cn.bmob.v3.BmobObject

data class Stuff(var name: String) : BmobObject() {
    var desc: String? = null
    var img: String? = null
    var ownerId: String? = null
    var ownerName: String? = null
    var ownerAddress: String? = null
    var status: String? = null //暂时用不上 但是可以保留 存一下95新 9新之类的 伊拉克成色
    var price: String? = null
    var comments = mutableListOf<Comment>()
    var category: String? = null

}
