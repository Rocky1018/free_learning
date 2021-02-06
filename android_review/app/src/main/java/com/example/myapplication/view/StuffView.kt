package com.example.myapplication.view

import com.example.myapplication.bean.Stuff

interface StuffView {
    fun getStuffInfo(stuffId: String): Stuff
    fun updateStuffInfo(stuff: Stuff)
    fun insertStuff(stuffInfo: Stuff)
    fun deleteStuff(stuffId: Stuff)
}