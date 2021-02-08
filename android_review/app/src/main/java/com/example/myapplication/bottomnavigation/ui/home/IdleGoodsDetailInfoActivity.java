package com.example.myapplication.bottomnavigation.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.myview.MyTitleBar;
import com.example.myapplication.utils.SharePreferencesUtils;


public class IdleGoodsDetailInfoActivity extends AppCompatActivity {
    private MyTitleBar idleGoodsDetailInfoMyTitleBar;
    private SharedPreferences userinformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_goods_detail_info);

        // 获取闲置物id
        Intent intent = getIntent();
        String goodsId = intent.getStringExtra("goodsId");

        // 获取用户id值
        userinformation = getSharedPreferences(SharePreferencesUtils.USER_INFORMATION_FILE, MODE_PRIVATE);
        String localUserId = userinformation.getString("userId", null);

        idleGoodsDetailInfoMyTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar_idleGoodsDetailInfo);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        // 更改顶部菜单栏标题
        idleGoodsDetailInfoMyTitleBar.setTvTitleText("闲置物详情");
        idleGoodsDetailInfoMyTitleBar.getTvForward().setVisibility(View.INVISIBLE);


    }
}