package com.example.myapplication.activity.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.myapplication.R
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.bean.User
import kotlinx.android.synthetic.main.activity_all_user.*


class AllUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)
        actionBar?.hide()
        rv_all_user.layoutManager = LinearLayoutManager(this)
        rv_all_user.adapter = UserAdapter(this, getUserList())
    }

    private fun getUserList(): List<User> {
        val result = mutableListOf<User>()
        val query: BmobQuery<User> = BmobQuery<User>()
        query.findObjects(object : FindListener<User?>() {
            override fun done(p0: MutableList<User?>?, p1: BmobException?) {
                TODO("Not yet implemented")
            }

        })
        return result
    }
}