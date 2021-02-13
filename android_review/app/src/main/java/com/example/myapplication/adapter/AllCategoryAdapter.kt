package com.example.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.bean.Category


class AllCategoryAdapter(
    private val context: Context,
    private val categoryList: MutableList<Category>
) : RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>() {
    private val commonCategoryIconList = arrayOf(
        R.drawable.ic_other_function_01,
        R.drawable.ic_other_function_02, R.drawable.ic_other_function_03,
        R.drawable.ic_other_function_04, R.drawable.ic_other_function_05,
    )

    class ViewHolder(categoryItemView: View) : RecyclerView.ViewHolder(categoryItemView) {
        var image = categoryItemView.findViewById<View>(R.id.category_img) as ImageView
        var name = categoryItemView.findViewById<View>(R.id.category_name) as TextView
        var edit = categoryItemView.findViewById<View>(R.id.btn_edit) as TextView
        var del = categoryItemView.findViewById<View>(R.id.btn_del) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.all_category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = categoryList[position].categoryName
        val size = commonCategoryIconList.size
        try {
            Glide.with(context).load(commonCategoryIconList[position % size]).into(holder.image)
        } catch (e: Exception) {
            Log.w("onBindViewHolder", "error.${e.message}")
        }
        holder.del.setOnClickListener {
            val item = Category(categoryList[position].categoryName)
            item.objectId = categoryList[position].objectId
            categoryList.removeAt(position)
            item.delete(object : UpdateListener() {
                override fun done(e: BmobException?) {
                    if (e == null) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT)
                    } else {
                        Toast.makeText(context, "修改失败${e.message}", Toast.LENGTH_SHORT)
                    }
                    notifyDataSetChanged()
                }
            })
        }
        holder.edit.setOnClickListener {
            MaterialDialog.Builder(context)
                .title("请输入分类名称")
                .input("", "") { _: MaterialDialog?, _: CharSequence? -> }
                .cancelable(false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { dialog: MaterialDialog, _: DialogAction? ->
                    val name = dialog.inputEditText?.text.toString().trim()
                    categoryList[position].categoryName = name
                    val item = Category(name)
                    item.update(categoryList[position].objectId, object : UpdateListener() {
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT)
                            } else {
                                Toast.makeText(context, "删除失败${e.message}", Toast.LENGTH_SHORT)
                            }
                        }
                    })
                    dialog.dismiss()
                    notifyDataSetChanged()
                }.show()
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}