package com.example.myapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.myapplication.bottomnavigation.add.AddFragment;
import com.example.myapplication.bottomnavigation.home.HomeFragment;
import com.example.myapplication.bottomnavigation.my.MyFragment;
import com.example.myapplication.domain.User;
import com.example.myapplication.myview.FixFragmentNavigator;
import com.example.myapplication.service.UserRegister;
import com.example.myapplication.utils.MD5Util;
import com.example.myapplication.utils.SharePreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.bmob.v3.Bmob;

public class BottomNavigationActivity extends BaseActivity {
    String userId;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CODE = 267;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i("AddFragment", "onRequestPermissionsResult: permission granted");
        } else {
            Log.i("AddFragment", "onRequestPermissionsResult: permission denied");
            Toast.makeText(this, "You Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_navigation);
        Bmob.initialize(this, "acb82b8fb5c6b9cbc68c4464959681f7");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        userId = (String) SharePreferencesUtils.getParam(this, "userId", "");
        /*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(PERMISSION_WRITE_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_WRITE_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }
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
                    startActivity(new Intent(this, AdminActivity.class));
                    return false;
                } else if (userId == "admin") {
                    startActivity(new Intent(this, AdminActivity.class));
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


    public static class RegisterActivity extends BaseActivity {

        private EditText userloginidEditText;
        private EditText passwordEditText;
        private EditText repeatpasswordEditText;
        private EditText usernameEditText;
        private EditText useremailEditText;
        private EditText phonenumEditText;
        private ProgressBar loadingProgressBar;

        private Button registerButton;

        @SuppressLint("CutPasteId")
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            //隐藏系统自带顶部状态栏
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.hide();
            }

            userloginidEditText = findViewById(R.id.edtTxt_register_user_login_id);
            passwordEditText = findViewById(R.id.edtTxt_register_password);
            repeatpasswordEditText = findViewById(R.id.edtTxt_register_repeat_password);
            usernameEditText = findViewById(R.id.edtTxt_register_username);
            useremailEditText = findViewById(R.id.edtTxt_register_email);
            phonenumEditText = findViewById(R.id.edtTxt_register_phone_num);
            registerButton = findViewById(R.id.btn_register_register);

            loadingProgressBar = findViewById(R.id.register_loading);


            userloginidEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!userloginidEditText.getText().toString().matches("^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$")) {
                        userloginidEditText.setError("登录账号格式有误！");
                    }
                }
            });

            passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!passwordEditText.getText().toString().matches("^(\\w){8,20}$")) {
                        passwordEditText.setError("密码长度介于8到20位之间，且由字母或数字构成！");
                    }
                }
            });

            repeatpasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!repeatpasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                        repeatpasswordEditText.setError("两次密码输入不一致！");
                    }
                }
            });

            usernameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (usernameEditText.getText().toString().isEmpty()) {
                        usernameEditText.setError("用户名不能为空！");
                    }
                }
            });

            useremailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!useremailEditText.getText().toString().matches("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$")) {
                        useremailEditText.setError("邮箱格式有误！");
                    }
                }
            });

            phonenumEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (!phonenumEditText.getText().toString().matches("^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$")) {
                        phonenumEditText.setError("手机号码格式有误！");
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!phonenumEditText.getText().toString().matches("^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$")) {
                        phonenumEditText.setError("手机号码格式有误！");
                    }
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!userloginidEditText.getText().toString().isEmpty() &&
                            !passwordEditText.getText().toString().isEmpty() &&
                            !repeatpasswordEditText.getText().toString().isEmpty() &&
                            !usernameEditText.getText().toString().isEmpty() &&
                            !useremailEditText.getText().toString().isEmpty() &&
                            !phonenumEditText.getText().toString().isEmpty() &&
                            userloginidEditText.getError() == null &&
                            passwordEditText.getError() == null &&
                            repeatpasswordEditText.getError() == null &&
                            usernameEditText.getError() == null &&
                            useremailEditText.getError() == null &&
                            phonenumEditText.getError() == null) {
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        User user = new User(userloginidEditText.getText().toString(), MD5Util.getMD5Str(passwordEditText.getText().toString()),
                                usernameEditText.getText().toString(), useremailEditText.getText().toString(),
                                phonenumEditText.getText().toString());

                        boolean register = UserRegister.register(user);
                        if (register) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                            //跳转主页面
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            loadingProgressBar.setVisibility(View.INVISIBLE);

                            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setTitle("警告");
                            dialog.setMessage("注册失败！用户账号已存在！");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.show();
                        }

                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("警告");
                        dialog.setMessage("填写信息有误！请检查");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                }
            });
        }
    }
}