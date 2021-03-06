package com.example.myapplication.bean

import cn.bmob.v3.BmobObject

data class User(val username: String) : BmobObject() {
    var nickname: String? = null
    var phoneNum: String? = null
    var address: String? = null
    var password: String? = null
    var portrait: String? = null
    var userRole: Int? = null
    var shoppingCar: MutableList<Stuff>? = null
    var stuffs: List<Stuff>? = null
    var collections: MutableList<Stuff>? = null
    var orderList: List<Order>? = null
}
