package com.example.myapplication.bottomnavigation.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.R;
import com.example.myapplication.activity.CategoryStuffActivity;
import com.example.myapplication.activity.MySearchResultActivity;
import com.example.myapplication.bean.CategoryItem;
import com.example.myapplication.bean.Stuff;
import com.example.myapplication.utils.FilePersistenceUtils;
import com.example.myapplication.adapter.StuffAdapter;
import com.example.myapplication.utils.ScreenUtils;
import com.example.myapplication.utils.SoftKeyBoardListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeFragment extends Fragment implements View.OnClickListener {

    // 数据的列表，即搜索下拉框列表元素，后从数据库获取
    private List<String> searchRecords;

    //闲置物列表
    public List<Stuff> idleGoodsInfoList;

    private SearchView homeFragmentSearchView;
    private ListPopupWindow searchRecordsListPopupWindow;
    private RecyclerView idlePropertyRecyclerView;
    private Toolbar homeFragmentHeadToolbar;
    private SwipeRefreshLayout refreshStuff;
    private HomeViewModel homeViewModel;
    private List<CategoryItem> categoryList;
    private List<View> categoryView;
    private List<TextView> categoryName;

    private List<CategoryItem> getCategoryList() {
        BmobQuery<CategoryItem> bmobQuery = new BmobQuery<>();
        List<CategoryItem> result = new ArrayList<>();
        bmobQuery.setLimit(10).order("-publishTime").findObjects(new FindListener<CategoryItem>() {
            @Override
            public void done(List<CategoryItem> list, BmobException e) {
                result.addAll(list);
            }
        });
        categoryList = result;
        return result;
    }

    private List<Stuff> getStuffList() {
        BmobQuery<Stuff> bmobQuery = new BmobQuery<>();
        List<Stuff> result = new ArrayList<>();
        bmobQuery.setLimit(10).order("-publishTime").findObjects(new FindListener<Stuff>() {
            @Override
            public void done(List<Stuff> list, BmobException e) {
                result.addAll(list);
            }
        });
        return result;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //获取搜索记录
        searchRecords = homeViewModel.getSearchRecords(getContext());
        //根据id获取控件
        homeFragmentSearchView = root.findViewById(R.id.searchView_homeSearch);
        homeFragmentSearchView.setSubmitButtonEnabled(true); //设置右端搜索键显示
        homeFragmentHeadToolbar = root.findViewById(R.id.toolbar_homeSearch);
        refreshStuff = root.findViewById(R.id.refresh_stuff_layout);
        //创建下拉列表
        searchRecordsListPopupWindow = new ListPopupWindow(getContext());
        //创建下拉列表数据项
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchRecords);
        //绑定下拉列表数据
        searchRecordsListPopupWindow.setAdapter(adapter);
        //绑定锚点，从什么控件下开始展开
        searchRecordsListPopupWindow.setAnchorView(homeFragmentHeadToolbar);

        searchRecordsListPopupWindow.setHeight(ScreenUtils.getScreenHeight(getContext()) / 2);

        searchRecordsListPopupWindow.setModal(false);
        idlePropertyRecyclerView = root.findViewById(R.id.rv_idleProperty);
        idlePropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View view = LayoutInflater.from(getContext()).
                inflate(R.layout.idle_goods_header, idlePropertyRecyclerView, false);

        // 初始化闲置物列表
        idleGoodsInfoList = getStuffList();


        StuffAdapter idleGoodsAdapter = new StuffAdapter(idleGoodsInfoList, getContext());
        idleGoodsAdapter.setHeaderView(view);

        idlePropertyRecyclerView.setAdapter(idleGoodsAdapter);

        //为每项数据项绑定事件
        searchRecordsListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchRecordsListPopupWindow.dismiss();  //隐藏下拉框
                //设置searchViewHome内部text
                homeFragmentSearchView.setQuery(searchRecords.get(position), true);
            }
        });

        //监听小键盘开启和隐藏
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Toast.makeText(getContext(), "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void keyBoardHide(int height) {
                homeFragmentSearchView.clearFocus(); //去除搜索框焦点
//                Toast.makeText(getContext(), "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        });
        refreshStuff.setOnRefreshListener(() -> {
            idleGoodsInfoList.add(new Stuff());
            refreshStuff.setRefreshing(false);
            idleGoodsAdapter.notifyDataSetChanged();
        });

        //监听搜索框被选中，即force监听
        homeFragmentSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchRecordsListPopupWindow.show();

                    /**
                     * 设置长按删除记录功能
                     * 1、首先获取ListPopupWindow中的ListView
                     * 2、然后添加OnItemLongClickListener事件
                     */
                    ListView listView = searchRecordsListPopupWindow.getListView();
                    if (listView != null) {
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                String record = searchRecords.get(position);
                                //创建弹窗
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setTitle("提示");
                                dialog.setMessage("删除" + record + "搜索记录？");
//                                dialog.setCancelable(false);
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //点击确定后删除文件记录及更新当前ListPopupWindow
                                        try {
                                            homeViewModel.removeSearchRecord(record);
                                            adapter.notifyDataSetChanged();
                                            FilePersistenceUtils.remove(getContext(), record, "searchrecords");

                                            homeFragmentSearchView.setQuery("", false);
                                            Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                                        } catch (Exception ex) {
                                            Toast.makeText(getContext(), "删除失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                /*dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });*/
                                dialog.show();
                                return true; //返回true表示长按过后的震动效果
                            }
                        });
                    }
                } else {
                    searchRecordsListPopupWindow.dismiss();
                }
            }
        });

        homeFragmentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {  //输入关键字后按下搜索键触发
                homeFragmentSearchView.clearFocus();
                if (query.length() > 0) {
                    Toast.makeText(getContext(), "搜索内容：" + query, Toast.LENGTH_SHORT).show();

                    //添加记录至本地文件
                    boolean flag = FilePersistenceUtils.addHeadDistinct(getContext(), query, FilePersistenceUtils.SREARCH_RECORDS_FILE_NAME);

                    //若文件中存在当前搜索记录
                    if (!flag) {
                        //先将记录移除
                        homeViewModel.removeSearchRecord(query);
                    }
                    //再将记录添加到头部
                    homeViewModel.addSearchRecord(query);
                    adapter.notifyDataSetChanged();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        List<Stuff> collect = idleGoodsInfoList.stream()
                                .filter(idleGoods -> {
                                    String goodsName = idleGoods.getName();

                                    return goodsName.contains(query);
                                }).collect(Collectors.toList());

                        Intent intent = new Intent(getContext(), MySearchResultActivity.class);
                        intent.putExtra("searchResult", new Gson().toJson(collect));
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {  //可用于进行关键字筛选
                query.toLowerCase();
                searchRecordsListPopupWindow.show();
                if (TextUtils.isEmpty(query)) {
                    // 清除ListView的过滤

                    searchRecordsListPopupWindow.setAdapter(adapter);
                } else {
                    List<String> recordsFilter = new ArrayList<>();
                    for (String searchRecord : searchRecords) {
                        String s = searchRecord;
                        if (s.toLowerCase().contains(query)) {
                            recordsFilter.add(searchRecord);
                        }
                    }
                    if (recordsFilter.size() >= 0) {
                        searchRecordsListPopupWindow.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recordsFilter));
                    }
                }
                return true;
            }
        });

        getCategoryList();
        initUIClick(view);
        return root;
    }

    private void initUIClick(View view) {
        categoryView = new ArrayList<>();
        categoryName = new ArrayList<>();
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction16));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction17));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction18));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction19));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction20));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction21));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction22));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction23));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction24));
        categoryView.add(view.findViewById(R.id.iv_myOtherFunction25));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction16));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction17));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction18));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction19));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction20));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction21));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction22));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction23));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction24));
        categoryName.add(view.findViewById(R.id.tv_myOtherFunction25));
        //这么写是真的丑陋！但是我想回家过年了
        //先拿到目录列表 本地显示固定10个。按照拿到的目录列表开始匹配text，数量还够就显示
        for (int i = 0; i < categoryList.size(); i++) {
            categoryName.get(i).setVisibility(View.VISIBLE);
            categoryView.get(i).setVisibility(View.VISIBLE);
            categoryName.get(i).setText(categoryList.get(i).getCategoryName());
        }
        view.findViewById(R.id.iv_myOtherFunction16).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction16).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction17).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction17).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction18).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction18).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction19).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction19).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction20).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction20).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction21).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction21).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction22).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction22).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction23).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction23).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction24).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction24).setOnClickListener(this);
        view.findViewById(R.id.iv_myOtherFunction25).setOnClickListener(this);
        view.findViewById(R.id.tv_myOtherFunction25).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryStuffActivity.class);
        try {
            switch (v.getId()) {
                case R.id.iv_myOtherFunction16:
                case R.id.tv_myOtherFunction16:
                    intent.putExtra("categoryId", categoryList.get(0).getObjectId());
                    break;
                case R.id.iv_myOtherFunction17:
                case R.id.tv_myOtherFunction17:
                    intent.putExtra("categoryId", categoryList.get(1).getObjectId());
                    break;
                case R.id.iv_myOtherFunction18:
                case R.id.tv_myOtherFunction18:
                    intent.putExtra("categoryId", categoryList.get(2).getObjectId());
                    break;
                case R.id.iv_myOtherFunction19:
                case R.id.tv_myOtherFunction19:
                    intent.putExtra("categoryId", categoryList.get(3).getObjectId());
                    break;
                case R.id.iv_myOtherFunction20:
                case R.id.tv_myOtherFunction20:
                    intent.putExtra("categoryId", categoryList.get(4).getObjectId());
                    break;
                case R.id.iv_myOtherFunction21:
                case R.id.tv_myOtherFunction21:
                    intent.putExtra("categoryId", categoryList.get(5).getObjectId());
                    break;
                case R.id.iv_myOtherFunction22:
                case R.id.tv_myOtherFunction22:
                    intent.putExtra("categoryId", categoryList.get(6).getObjectId());
                    break;
                case R.id.iv_myOtherFunction23:
                case R.id.tv_myOtherFunction23:
                    intent.putExtra("categoryId", categoryList.get(7).getObjectId());
                    break;
                case R.id.iv_myOtherFunction24:
                case R.id.tv_myOtherFunction24:
                    intent.putExtra("categoryId", categoryList.get(8).getObjectId());
                    break;
                case R.id.iv_myOtherFunction25:
                case R.id.tv_myOtherFunction25:
                    intent.putExtra("categoryId", categoryList.get(9).getObjectId());
                    break;
            }
        } catch (RuntimeException e) {
            Log.w("HomeFragment", "error" + e.getMessage());
        }

        startActivity(intent);
    }
}