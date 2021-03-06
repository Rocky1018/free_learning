package com.example.myapplication.activity.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.adapter.AllCategoryAdapter
import com.example.myapplication.bean.Category
import kotlinx.android.synthetic.main.activity_all_category.*


class AllCategoryActivity : AppCompatActivity() {
    var adapter: AllCategoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_category)
        actionBar?.hide()
        rv_all_category.layoutManager = LinearLayoutManager(this)
        getCategoryList()
        btn_add_category.setOnClickListener {
            MaterialDialog.Builder(this)
                .title("请输入分类名称")
                .input("", "") { _: MaterialDialog?, _: CharSequence? -> }
                .cancelable(false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { dialog: MaterialDialog, _: DialogAction? ->
                    val name = dialog.inputEditText?.text.toString().trim()
                    val categoryItem = Category(name)
                    categoryItem.save(object : SaveListener<String?>() {
                        override fun done(objectId: String?, e: BmobException?) {
                            getCategoryList()
                            if (e == null) {
                                Toast.makeText(this@AllCategoryActivity, "添加成功", Toast.LENGTH_SHORT)
                            } else {
                                Toast.makeText(
                                    this@AllCategoryActivity,
                                    "添加失败${e.message}",
                                    Toast.LENGTH_SHORT
                                )
                            }
                        }
                    })
                    dialog.dismiss()
                }.show()
        }
    }

    private fun getCategoryList() {
        var categoryList = mutableListOf<Category>()
        val bmobQuery = BmobQuery<Category>()
        bmobQuery.findObjects(object : FindListener<Category>() {
            override fun done(list: List<Category>, e: BmobException?) {
                if (list != null && list.isNotEmpty()) {
                    categoryList.addAll(list)
                    adapter = AllCategoryAdapter(this@AllCategoryActivity, categoryList)
                    rv_all_category.adapter = adapter
                }
            }
        })
    }
}


