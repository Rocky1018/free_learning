package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.example.myapplication.R;
import com.example.myapplication.bean.User;
import com.example.myapplication.service.UserRegister;
import com.example.myapplication.utils.Config;
import com.example.myapplication.utils.MD5Util;
import com.example.myapplication.utils.SharePreferencesUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    private EditText userloginidEditText;
    private EditText passwordEditText;
    private EditText repeatpasswordEditText;
    private String username, password, repeatPassword;
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
        username = userloginidEditText.getText().toString();
        password = passwordEditText.getText().toString();
        repeatPassword = repeatpasswordEditText.getText().toString();
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


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userloginidEditText.getText().toString().isEmpty() &&
                        !passwordEditText.getText().toString().isEmpty() &&
                        !repeatpasswordEditText.getText().toString().isEmpty() &&
                        userloginidEditText.getError() == null &&
                        passwordEditText.getError() == null &&
                        repeatpasswordEditText.getError() == null) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    BmobQuery query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", username).
                            addWhereEqualTo("password", MD5Util.getMD5Str(password))
                            .findObjects(new FindListener<User>() {

                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if (list != null && list.size() == 0 && e == null) {
                                        userRegister();
                                    } else {
                                        Log.w("register", "error" + e.getMessage());
                                        Toast.makeText(RegisterActivity.this,
                                                "error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //先查一下能不能注册

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

    private void userRegister() {
        User user = new User(username);
        user.setPassword(password);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //跳转主页面
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Config.INSTANCE.setUser(user);
                    SharePreferencesUtils.setParam(RegisterActivity.this, "userId", user.getObjectId());
                } else {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
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

}