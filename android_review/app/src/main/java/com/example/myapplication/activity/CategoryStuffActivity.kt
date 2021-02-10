package com.example.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.myapplication.R
import com.example.myapplication.adapter.StuffAdapter
import com.example.myapplication.bean.Stuff
import kotlinx.android.synthetic.main.activity_category_stuff.*
import java.util.*

class CategoryStuffActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_stuff)
        category_stuff.layoutManager = LinearLayoutManager(this)
        category_stuff.adapter =
            StuffAdapter(getStuffList(intent.getStringExtra("categoryId")), this)
    }

    private fun getStuffList(categoryId: String?): List<Stuff>? {
        val bmobQuery = BmobQuery<Stuff>()
        val result: MutableList<Stuff> = ArrayList()
        bmobQuery.addWhereEqualTo("categoryId", categoryId)
            .findObjects(object : FindListener<Stuff>() {
                override fun done(list: List<Stuff>, e: BmobException) {
                    result.addAll(list)
                }
            })
        return result
    }

}