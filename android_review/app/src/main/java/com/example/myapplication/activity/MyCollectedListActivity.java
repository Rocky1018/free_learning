package com.example.myapplication.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.domain.Stuff;
import com.example.myapplication.myview.MyTitleBar;
import com.example.myapplication.service.GetMyCollectedList;
import com.example.myapplication.adapter.MyCollectedListAdapter;
import com.example.myapplication.utils.SharePreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class MyCollectedListActivity extends AppCompatActivity {
    //闲置物列表
    public List<Stuff> idleGoodsInfoList;
    private RecyclerView myCollectedListRecyclerView;

    private MyTitleBar myCollectedMyTitleBar;
    private SharedPreferences userinformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collected_list);

        // 获取userId
        userinformation = getSharedPreferences(SharePreferencesUtils.USER_INFORMATION_FILE, MODE_PRIVATE);
        String localUserId = userinformation.getString("userId", null);

        myCollectedMyTitleBar = findViewById(R.id.myTitleBar_myCollected);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        myCollectedMyTitleBar.setTvTitleText("收藏夹");
        myCollectedMyTitleBar.getTvForward().setVisibility(View.INVISIBLE);

        if (localUserId != null) {
            idleGoodsInfoList = GetMyCollectedList.getMyCollectedList(localUserId);
            if (idleGoodsInfoList == null) {
                idleGoodsInfoList = new ArrayList<>();
            }

            Log.d("GetMyCollectedList", idleGoodsInfoList.toString());
            myCollectedListRecyclerView = findViewById(R.id.rv_myCollectedList);
            LinearLayoutManager manager = new LinearLayoutManager(MyCollectedListActivity.this);
            myCollectedListRecyclerView.setLayoutManager(manager);
            MyCollectedListAdapter myCollectedListAdapter = new MyCollectedListAdapter(idleGoodsInfoList);

            myCollectedListRecyclerView.setAdapter(myCollectedListAdapter);
        }
    }
}