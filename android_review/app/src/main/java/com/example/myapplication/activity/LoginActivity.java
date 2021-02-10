package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.example.myapplication.R;
import com.example.myapplication.bean.User;
import com.example.myapplication.service.GetUserInfo;
import com.example.myapplication.service.UserLogin;
import com.example.myapplication.utils.IdentifyingCodeUtils;
import com.example.myapplication.utils.MD5Util;
import com.example.myapplication.utils.SharePreferencesUtils;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends BaseActivity {

    private EditText userloginidEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
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
        registerButton = findViewById(R.id.btn_login_register);
        loadingProgressBar = findViewById(R.id.login_loading);
        identifyingCodeEditText = findViewById(R.id.edtTxt_login_identifyingCode);
        identifyingCodeImageView = findViewById(R.id.iv_login_identifyingCode);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        userloginidEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!userloginidEditText.getText().toString().matches("^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$") &&
                        !userloginidEditText.getText().toString().equals("admin")) {
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

        //判断本地是否缓存了用户账号密码，如果缓存即直接登录否则跳转到登录界面
        SharedPreferences userinformation = getSharedPreferences(SharePreferencesUtils.USER_INFORMATION_FILE, MODE_PRIVATE);
        String localUserLoginId = userinformation.getString("userLoginId", null);
        String localUserPassword = userinformation.getString("userPassword", null);
        if (localUserLoginId != null && localUserPassword != null) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            boolean login = UserLogin.login(localUserLoginId, localUserPassword);
            if (login) {
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();

                //跳转主页面
                Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                startActivity(intent);
            } else {
                loadingProgressBar.setVisibility(View.INVISIBLE);
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

            //跳转主页面
            Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
            startActivity(intent);
        }

        //获取工具类生成的图片验证码对象
        identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
        //获取当前图片验证码的对应内容用于校验
        identifyingCode = IdentifyingCodeUtils.getInstance().getCode();

        identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
        identifyingCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identifyingCodeBitmap = IdentifyingCodeUtils.getInstance().createBitmap();
                identifyingCode = IdentifyingCodeUtils.getInstance().getCode();
                identifyingCodeImageView.setImageBitmap(identifyingCodeBitmap);
//                Toast.makeText(LoginActivity.this, "验证码修改成功", Toast.LENGTH_SHORT).show();
            }
        });

        //添加按钮click监听事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identifyingCodeEditText.getText().toString().equalsIgnoreCase(identifyingCode)) {
                    if (!userloginidEditText.getText().toString().isEmpty() &&
                            !passwordEditText.getText().toString().isEmpty() &&
                            userloginidEditText.getError() == null &&
                            passwordEditText.getError() == null) {
                        //验证码正确才进行登录
                        //简单表单校验无误后按下按钮，显示进度条
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        String inputUsername = userloginidEditText.getText().toString();
                        String encodePwd = MD5Util.getMD5Str(passwordEditText.getText().toString());
                        BmobQuery query = new BmobQuery<User>();
                        query.addWhereEqualTo("username", inputUsername).addWhereEqualTo("password", encodePwd)
                                .findObjects(new FindListener<User>() {

                                    @Override
                                    public void done(List<User> list, BmobException e) {
                                        //这肯定是只能查到一个的
                                        if (e == null && list.size() > 0) {
                                            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();

                                            //存储此次用户登录信息到本地
                                            SharePreferencesUtils.setParam(LoginActivity.this, "userId", list.get(0).getObjectId());
                                            Log.d("login", "resp list:" + list.get(0).toString());

                                            //跳转主页面
                                            Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                                            startActivity(intent);
                                        } else {
                                            showPwdErrorDialog();
                                            Log.w("login", "error " + e.getMessage());
                                        }
                                    }
                                });
                    } else {
                        showEmptyHintDialog();
                    }
                } else {
                    showIdentifyCodeErrorDialog();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Toast.makeText(LoginActivity.this, "请先注册！", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }

    private void showPwdErrorDialog() {
        loadingProgressBar.setVisibility(View.INVISIBLE);
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
        passwordEditText.setText("");
    }
}