package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MyCollectedListAdapter;
import com.example.myapplication.bean.Stuff;
import com.example.myapplication.bean.User;
import com.example.myapplication.myview.MyTitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MyCollectedListActivity extends AppCompatActivity {
    //闲置物列表
    public List<Stuff> idleGoodsInfoList = new ArrayList<>();
    private RecyclerView myCollectedListRecyclerView;

    private MyTitleBar myCollectedMyTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collected_list);

        // 获取userId
        String localUserId = getIntent().getStringExtra("userId");

        myCollectedMyTitleBar = findViewById(R.id.myTitleBar_myCollected);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        myCollectedListRecyclerView = findViewById(R.id.rv_myCollectedList);
        LinearLayoutManager manager = new LinearLayoutManager(MyCollectedListActivity.this);
        myCollectedListRecyclerView.setLayoutManager(manager);


        myCollectedMyTitleBar.setTvTitleText("收藏夹");
        myCollectedMyTitleBar.getTvForward().setVisibility(View.INVISIBLE);

        BmobQuery query = new BmobQuery<User>();
        query.getObject(localUserId, new QueryListener<User>() {
            @Override
            public void done(User o, BmobException e) {
                if (e == null) {
                    idleGoodsInfoList = o.getCollections();
                    MyCollectedListAdapter myCollectedListAdapter = new MyCollectedListAdapter(idleGoodsInfoList);

                    myCollectedListRecyclerView.setAdapter(myCollectedListAdapter);
                }
            }
        });

    }
}