package com.example.myapplication.bean

import cn.bmob.v3.BmobObject

data class Order(val orderStatus: String, val stuff: Stuff) : BmobObject()
