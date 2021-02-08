package com.example.myapplication.bottomnavigation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.domain.IdleGoods;
import com.example.myapplication.myview.MyTitleBar;
import com.example.myapplication.adapter.MyCollectedListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MySearchResultActivity extends AppCompatActivity {
    private MyTitleBar mySearchResultMyTitleBar;
    private RecyclerView mySearchResultRecyclerView;
    private List<IdleGoods> mySearchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_search_result);

        mySearchResultMyTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar_mySearchResult);
        mySearchResultRecyclerView = (RecyclerView) findViewById(R.id.rv_mySearchResult);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        mySearchResultMyTitleBar.setTvTitleText("搜索结果");
        mySearchResultMyTitleBar.getTvForward().setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String searchResult = intent.getStringExtra("searchResult");

        Log.d("searchResult", searchResult);

        mySearchResultList = IdleGoods.parseToList(searchResult);
        if (mySearchResultList == null) {
            mySearchResultList = new ArrayList<>();
        }

        LinearLayoutManager manager = new LinearLayoutManager(MySearchResultActivity.this);
        mySearchResultRecyclerView.setLayoutManager(manager);
        MyCollectedListAdapter myCollectedListAdapter = new MyCollectedListAdapter(mySearchResultList);

        mySearchResultRecyclerView.setAdapter(myCollectedListAdapter);
    }
}