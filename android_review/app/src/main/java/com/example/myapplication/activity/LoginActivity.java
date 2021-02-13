package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.example.myapplication.R;
import com.example.myapplication.bean.User;
import com.example.myapplication.utils.Config;
import com.example.myapplication.utils.IdentifyingCodeUtils;
import com.example.myapplication.utils.MD5Util;
import com.example.myapplication.utils.SharePreferencesUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {

    private EditText userloginidEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private EditText identifyingCodeEditText;
    private ImageView identifyingCodeImageView;
    private ProgressBar loadingProgressBar;
    private Bitmap identifyingCodeBitmap;
    private String identifyingCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userloginidEditText = findViewById(R.id.edtTxt_login_userloginid);
        passwordEditText = findViewById(R.id.edtTxt_login_password);
        loginButton = findViewById(R.id.btn_login_login);
        loadingProgressBar = findViewById(R.id.login_loading);
        identifyingCodeEditText = findViewById(R.id.edtTxt_login_identifyingCode);
        identifyingCodeImageView = findViewById(R.id.iv_login_identifyingCode);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

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

        //获取工具类生成的图片验证码对象
        identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
        //获取当前图片验证码的对应内容用于校验
        identifyingCode = IdentifyingCodeUtils.getInstance().getCode();

        identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
        identifyingCodeImageView.setOnClickListener(view -> {
            identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
            identifyingCode = IdentifyingCodeUtils.getInstance().getCode();
            identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
//                Toast.makeText(LoginActivity.this, "验证码修改成功", Toast.LENGTH_SHORT).show();
        });

        //添加按钮click监听事件
        loginButton.setOnClickListener(v -> {
            if (identifyingCodeEditText.getText().toString().equalsIgnoreCase(identifyingCode)) {
                if (!userloginidEditText.getText().toString().isEmpty() &&
                        !passwordEditText.getText().toString().isEmpty() &&
                        userloginidEditText.getError() == null &&
                        passwordEditText.getError() == null) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    String inputUsername = userloginidEditText.getText().toString();
                    String encodePwd = MD5Util.getMD5Str(passwordEditText.getText().toString());
                    BmobQuery query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", inputUsername).findObjects(new FindListener<User>() {

                        @Override
                        public void done(List<User> list, BmobException e) {
                            loadingProgressBar.setVisibility(View.GONE);
                            if (list.size() > 0) {
                                for (User user : list) {
                                    if (encodePwd.equals(user.getPassword())) {
                                        Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                        //存储此次用户登录信息到本地
                                        SharePreferencesUtils.setParam(LoginActivity.this, "userId", user.getObjectId());
                                        Config.INSTANCE.setUser(user);
                                        //跳转主页面
                                        Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                }
                                showPwdErrorDialog();
                            } else {
                                userRegister(inputUsername, encodePwd);
                            }
                        }
                    });
                }
            } else {
                showIdentifyCodeErrorDialog();
            }
        });
    }

    private void userRegister(String username, String encodePassword) {
        User user = new User(username);
        user.setPassword(encodePassword);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //跳转主页面
                    Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                    startActivity(intent);
                    Config.INSTANCE.setUser(user);
                    SharePreferencesUtils.setParam(LoginActivity.this, "userId", user.getObjectId());
                    finish();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("警告");
                    dialog.setMessage("注册失败！" + e.getMessage());
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

    private void showPwdErrorDialog() {
        //更新验证码图片
        identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
        identifyingCode = IdentifyingCodeUtils.getInstance().getCode();
        identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
        identifyingCodeEditText.setText("");
        passwordEditText.setText("");


        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("警告");
        dialog.setMessage("登录账号或密码不正确！");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void showEmptyHintDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("警告");
        dialog.setMessage("登录账号和密码不能为空！请检查");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();

        //刷新验证码
        identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
        identifyingCode = IdentifyingCodeUtils.getInstance().getCode();
        identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
        identifyingCodeEditText.setText("");
    }

    private void showIdentifyCodeErrorDialog() {
        //创建弹窗
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("验证码不正确！");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();

        //刷新验证码
        identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
        identifyingCode = IdentifyingCodeUtils.getInstance().getCode();
        identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
        identifyingCodeEditText.setText("");
    }
}