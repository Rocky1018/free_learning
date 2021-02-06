package com.example.myapplication.view

import com.example.myapplication.bean.User

interface UserView {
    fun getUserInfo(userId: String): User
    fun updateUserInfo(user: User)
    fun insertUser(userInfo: User)
    fun deleteUser(userId: User)
}