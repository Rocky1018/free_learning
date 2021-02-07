package com.example.myapplication.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.bean.User;
import com.example.myapplication.bottomnavigation.ui.add.AddFragment;
import com.example.myapplication.bottomnavigation.ui.home.HomeFragment;
import com.example.myapplication.bottomnavigation.ui.my.MyFragment;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.myview.FixFragmentNavigator;
import com.example.myapplication.utils.BaseActivity;
import com.example.myapplication.utils.SharePreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BottomNavigationActivity extends BaseActivity {
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_navigation);
        Bmob.initialize(this, "acb82b8fb5c6b9cbc68c4464959681f7");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        userId = (String) SharePreferencesUtils.getParam(this, "userId", "");

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        //获取页面容器NavHostFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //获取导航控制器
        NavController navController = NavHostFragment.findNavController(fragment);
        //创建自定义的Fragment导航器
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        //获取导航器提供者
        NavigatorProvider provider = navController.getNavigatorProvider();
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fragmentNavigator);
        //手动创建导航图
        NavGraph navGraph = initNavGraph(provider, fragmentNavigator);
        //设置导航图
        navController.setGraph(navGraph);
        //底部导航设置点击事件
        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_my) {
                if (TextUtils.isEmpty(userId)) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return false;
                } else {
                    navController.navigate(item.getItemId());
                }
            } else {
                navController.navigate(item.getItemId());
            }
            return true;
        });
    }


    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //用自定义的导航器来创建目的地
        FragmentNavigator.Destination homeFragment = fragmentNavigator.createDestination();
        homeFragment.setId(R.id.navigation_home);
        homeFragment.setClassName(HomeFragment.class.getCanonicalName());
        homeFragment.setLabel(getResources().getString(R.string.title_home));
        navGraph.addDestination(homeFragment);

        FragmentNavigator.Destination addFragment = fragmentNavigator.createDestination();
        addFragment.setId(R.id.navigation_add);
        addFragment.setClassName(AddFragment.class.getCanonicalName());
        addFragment.setLabel(getResources().getString(R.string.title_add));
        navGraph.addDestination(addFragment);

        FragmentNavigator.Destination myFragment = fragmentNavigator.createDestination();
        myFragment.setId(R.id.navigation_my);
        myFragment.setClassName(MyFragment.class.getCanonicalName());
        myFragment.setLabel(getResources().getString(R.string.title_my));
        navGraph.addDestination(myFragment);

        navGraph.setStartDestination(R.id.navigation_home);
        return navGraph;
    }


}