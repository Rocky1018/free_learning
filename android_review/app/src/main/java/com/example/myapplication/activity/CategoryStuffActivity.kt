package com.example.myapplication.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        actionBar?.hide()
        category_stuff.layoutManager = LinearLayoutManager(this)
        category_stuff.adapter =
            StuffAdapter(getStuffList(intent.getStringExtra("categoryId")), this)
    }

    private fun getStuffList(categoryId: String?): List<Stuff>? {
        val result: MutableList<Stuff> = ArrayList()
        val bmobQuery = BmobQuery<Stuff>()
        bmobQuery.addWhereEqualTo("categoryId", categoryId)
            .findObjects(object : FindListener<Stuff>() {
                override fun done(list: List<Stuff>, e: BmobException) {
                    if (list != null)
                        result.addAll(list)
                    if (e != null)
                        Log.w("getStuffList", "  ${e.message}")
                }
            })
        return result
    }

}