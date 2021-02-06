package com.example.myapplication.bean

data class User(val userId: String) {
    var username: String? = null
    var phoneNum: String? = null
    var password: String? = null
    var shoppingCar: List<Stuff>? = null
    var orderList: List<Order>? = null
}
