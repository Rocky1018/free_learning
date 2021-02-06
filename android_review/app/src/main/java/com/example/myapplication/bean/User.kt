package com.example.myapplication.bean

import cn.bmob.v3.BmobObject

data class User(val userId: String) : BmobObject() {
    var username: String? = null
    var phoneNum: String? = null
    var password: String? = null
    var shoppingCar: List<Stuff>? = null
    var stuffs: List<Stuff>? = null
    var collections: List<Stuff>? = null
    var orderList: List<Order>? = null
}
