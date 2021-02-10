package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.bean.CommentItem
import com.example.myapplication.myview.MyTitleBar
import kotlinx.android.synthetic.main.activity_idle_goods_detail_info.*
import java.text.SimpleDateFormat
import java.util.*

class IdleGoodsDetailInfoActivity : AppCompatActivity() {
    private var idleGoodsDetailInfoMyTitleBar: MyTitleBar? = null
    private var editComment: EditText? = null
    private var ll_leave_comment: LinearLayout? = null
    private var rv_comments: RecyclerView? = null
    private fun initTestComments(): List<CommentItem> {
        val commentItems: MutableList<CommentItem> = ArrayList()
        for (i in 0..9) {
            val item = CommentItem(UUID.randomUUID().toString())
            item.content = "fake content$i"
            item.username = "fake username$i"
            item.portrait = "fake content$i"
            item.date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            commentItems.add(item)
        }
        return commentItems
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idle_goods_detail_info)
        idleGoodsDetailInfoMyTitleBar = findViewById(R.id.myTitleBar_idleGoodsDetailInfo)
        editComment = findViewById(R.id.et_commemt)
        ll_leave_comment = findViewById(R.id.ll_leave_comment)
        rv_comments = findViewById(R.id.rv_comments)
        rv_comments?.layoutManager = LinearLayoutManager(this)
        rv_comments?.adapter = CommentAdapter(this, initTestComments())
        supportActionBar!!.hide()
        editComment?.setOnEditorActionListener(OnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Log.d("onEditorAction", "" + v.text)
            }
            false
        })
        ll_leave_comment?.setOnClickListener(View.OnClickListener { v: View? ->
            editComment?.requestFocus()
            val inputManager =
                editComment?.context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager?.showSoftInput(editComment, 0)
        })
        tv_want.setOnClickListener {
            banWantDialog()
        }
        // 更改顶部菜单栏标题
        idleGoodsDetailInfoMyTitleBar?.setTvTitleText("闲置物详情")
        idleGoodsDetailInfoMyTitleBar?.tvForward?.visibility = View.GONE

    }

    private fun banWantDialog() {
        MaterialDialog.Builder(this)
            .title("请输入管理密码！")
            .content(R.string.ban_want_hint)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive { _: MaterialDialog, _: DialogAction? ->
                Toast.makeText(this, "加入购物车", Toast.LENGTH_SHORT).show()
            }.show()
    }
}