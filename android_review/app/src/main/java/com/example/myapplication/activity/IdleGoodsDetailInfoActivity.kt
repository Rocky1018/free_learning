package com.example.myapplication.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.bean.Stuff
import com.example.myapplication.utils.Config
import kotlinx.android.synthetic.main.activity_idle_goods_detail_info.*
import java.util.*

class IdleGoodsDetailInfoActivity : AppCompatActivity() {
    private var stuff: Stuff? = null
    private var isCollected: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idle_goods_detail_info)
        supportActionBar?.hide()
        // 更改顶部菜单栏标题
        myTitleBar_idleGoodsDetailInfo.setTvTitleText("闲置物详情")
        myTitleBar_idleGoodsDetailInfo.tvForward?.visibility = View.GONE
        val bmobQuery = BmobQuery<Stuff>()
        bmobQuery.getObject("", object : QueryListener<Stuff>() {
            override fun done(p0: Stuff?, p1: BmobException?) {
                stuff = p0
                rv_comments.layoutManager = LinearLayoutManager(this@IdleGoodsDetailInfoActivity)
                rv_comments.adapter =
                    stuff?.comments?.let { CommentAdapter(this@IdleGoodsDetailInfoActivity, it) }
            }

        })

        et_commemt.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Log.d("onEditorAction", "" + v.text)
            }
            false
        }
        ll_leave_comment?.setOnClickListener { v: View? ->
            et_commemt.requestFocus()
            val inputManager =
                et_commemt.context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(et_commemt, 0)
        }
        tv_want.setOnClickListener {
            banWantDialog()
        }
        ll_collect.setOnClickListener {
            collectStuff()
        }

    }

    private fun collectStuff() {
        if (stuff == null) {
            Toast.makeText(this, "收藏失败，未获取商品详情", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isCollected) {
            Config.user?.apply {
                collections?.add(stuff!!)
                update(object : UpdateListener() {
                    override fun done(p0: BmobException?) {
                        if (p0 != null) {
                            Log.w("collectStuff", "error  ${p0.message}")
                            return
                        }
                        Toast.makeText(
                            this@IdleGoodsDetailInfoActivity, "收藏成功",
                            Toast.LENGTH_SHORT
                        ).show()
                        iv_collect.setImageResource(R.drawable.ic_baseline_star_24)
                        isCollected = true
                    }
                })
            }
        } else {
            Config.user?.apply {
                collections?.remove(stuff)
                update(object : UpdateListener() {
                    override fun done(p0: BmobException?) {
                        if (p0 != null) {
                            Log.w("collectStuff", "error  ${p0.message}")
                            return
                        }
                        Toast.makeText(
                            this@IdleGoodsDetailInfoActivity, "取消收藏",
                            Toast.LENGTH_SHORT
                        ).show()
                        iv_collect.setImageResource(R.drawable.ic_baseline_star_border_24)
                        isCollected = false
                    }
                })
            }
        }

    }

    private fun banWantDialog() {
        MaterialDialog.Builder(this)
            .content(R.string.ban_want_hint)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive { _: MaterialDialog, _: DialogAction? ->
                Toast.makeText(this, "加入购物车", Toast.LENGTH_SHORT).show()
            }.show()
    }
}