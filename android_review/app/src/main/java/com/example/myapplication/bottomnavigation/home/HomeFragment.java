package com.example.myapplication.bottomnavigation.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.R;
import com.example.myapplication.activity.MySearchResultActivity;
import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.bean.Category;
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
    public List<Stuff> idleGoodsInfoList = new ArrayList<>();

    StuffAdapter idleGoodsAdapter;

    private SearchView homeFragmentSearchView;
    private ListPopupWindow searchRecordsListPopupWindow;
    private RecyclerView idlePropertyRecyclerView, categoryRecyclerview;
    private Toolbar homeFragmentHeadToolbar;
    private SwipeRefreshLayout refreshStuff;
    private HomeViewModel homeViewModel;
    private final List<Category> categoryList = new ArrayList<>();

    private List<Category> getCategoryList() {
        BmobQuery<Category> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (list != null && list.size() > 0) {
                    categoryList.addAll(list);
                    categoryRecyclerview.setAdapter(new CategoryAdapter(getActivity(), categoryList));
                }
            }
        });
        return categoryList;
    }

    private void getStuffList() {
        BmobQuery<Stuff> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(10).order("-publishTime").findObjects(new FindListener<Stuff>() {
            @Override
            public void done(List<Stuff> list, BmobException e) {
                idleGoodsInfoList.clear();
                idleGoodsInfoList.addAll(list);
                idleGoodsAdapter = new StuffAdapter(idleGoodsInfoList, getActivity());
                idlePropertyRecyclerView.setAdapter(idleGoodsAdapter);
            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        //获取搜索记录
        searchRecords = homeViewModel.getSearchRecords(getActivity());
        //根据id获取控件
        homeFragmentSearchView = root.findViewById(R.id.searchView_homeSearch);
        categoryRecyclerview = root.findViewById(R.id.rv_category);
        homeFragmentSearchView.setSubmitButtonEnabled(true); //设置右端搜索键显示
        homeFragmentHeadToolbar = root.findViewById(R.id.toolbar_homeSearch);
        refreshStuff = root.findViewById(R.id.refresh_stuff_layout);
        //创建下拉列表
        searchRecordsListPopupWindow = new ListPopupWindow(getActivity());
        //创建下拉列表数据项
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, searchRecords);
        //绑定下拉列表数据
        searchRecordsListPopupWindow.setAdapter(adapter);
        //绑定锚点，从什么控件下开始展开
        searchRecordsListPopupWindow.setAnchorView(homeFragmentHeadToolbar);

        searchRecordsListPopupWindow.setHeight(ScreenUtils.getScreenHeight(getActivity()) / 2);

        searchRecordsListPopupWindow.setModal(false);
        idlePropertyRecyclerView = root.findViewById(R.id.rv_goods);
        idlePropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        // 初始化闲置物列表
        getStuffList();
        getCategoryList();

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
//                Toast.makeText(getActivity(), "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void keyBoardHide(int height) {
                homeFragmentSearchView.clearFocus(); //去除搜索框焦点
//                Toast.makeText(getActivity(), "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        });
        refreshStuff.setOnRefreshListener(() -> {
            refreshStuff.setRefreshing(false);
            getStuffList();
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
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
                                            FilePersistenceUtils.remove(getActivity(), record, "searchrecords");

                                            homeFragmentSearchView.setQuery("", false);
                                            Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                                        } catch (Exception ex) {
                                            Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "搜索内容：" + query, Toast.LENGTH_SHORT).show();

                    //添加记录至本地文件
                    boolean flag = FilePersistenceUtils.addHeadDistinct(getActivity(), query, FilePersistenceUtils.SREARCH_RECORDS_FILE_NAME);

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

                        Intent intent = new Intent(getActivity(), MySearchResultActivity.class);
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
                try {
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
                            searchRecordsListPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, recordsFilter));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {

    }
}