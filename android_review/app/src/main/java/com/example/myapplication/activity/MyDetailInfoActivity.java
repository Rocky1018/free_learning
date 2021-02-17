package com.example.myapplication.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.domain.DoMainUser;
import com.example.myapplication.myview.MyItemGroup;
import com.example.myapplication.myview.MyTitleBar;
import com.example.myapplication.service.UpDateUserInfo;
import com.example.myapplication.utils.SharePreferencesUtils;

public class MyDetailInfoActivity extends AppCompatActivity {

    private MyTitleBar myInfoMyTitleBar;

    private MyItemGroup nicknameMyItemGroup;
    private MyItemGroup userEmailMyItemGroup;
    private MyItemGroup userPhoneNumMyItemGroup;
    private SharedPreferences userinformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deatil_info);

        myInfoMyTitleBar = findViewById(R.id.myTitleBar_myInfo);
        nicknameMyItemGroup = findViewById(R.id.myItemGroup_nickName);
        userEmailMyItemGroup = findViewById(R.id.myItemGroup_userEmail);
        userPhoneNumMyItemGroup = findViewById(R.id.myItemGroup_userPhoneNum);

        //隐藏系统自带顶部状态栏
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        myInfoMyTitleBar.setTvTitleText("编辑资料");

        // 取出本地缓存的个人信息填入页面
        userinformation = getSharedPreferences(SharePreferencesUtils.USER_INFORMATION_FILE, MODE_PRIVATE);
        String localUserId = userinformation.getString("userId", null);
        String localUserLoginId = userinformation.getString("userLoginId", null);
        String localUserName = userinformation.getString("userName", null);
        String localUserEmail = userinformation.getString("userEmail", null);
        String localUserPhoneNum = userinformation.getString("userPhoneNum", null);
        String localUserRegisterDate = userinformation.getString("userRegisterDate", null);

        if (localUserName != null) {
            nicknameMyItemGroup.setContentTextViewText(localUserName);
        }
        if (localUserEmail != null) {
            userEmailMyItemGroup.setContentTextViewText(localUserEmail);
        }
        if (localUserPhoneNum != null) {
            userPhoneNumMyItemGroup.setContentTextViewText(localUserPhoneNum);
        }


        nicknameMyItemGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText usernameEditText = new EditText(MyDetailInfoActivity.this);
                usernameEditText.setText(nicknameMyItemGroup.getContentTextViewText());
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

                //创建弹窗
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyDetailInfoActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("更改用户名为").setView(usernameEditText).setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 将信息更新至文本框中，待最终按下保存按钮再更新至本地共享文件并更新数据库
                        if (!usernameEditText.getText().toString().isEmpty() && usernameEditText.getError() == null) {
                            nicknameMyItemGroup.setContentTextViewText(usernameEditText.getText().toString());
                            Toast.makeText(MyDetailInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyDetailInfoActivity.this, "用户密码格式错误请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        userEmailMyItemGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText useremailEditText = new EditText(MyDetailInfoActivity.this);
                useremailEditText.setText(userEmailMyItemGroup.getContentTextViewText());
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

                //创建弹窗
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyDetailInfoActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("更改用户邮箱为").setView(useremailEditText).setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 将信息更新至文本框中，待最终按下保存按钮再更新至本地共享文件并更新数据库
                        if (!useremailEditText.getText().toString().isEmpty() && useremailEditText.getError() == null) {
                            userEmailMyItemGroup.setContentTextViewText(useremailEditText.getText().toString());
                            Toast.makeText(MyDetailInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyDetailInfoActivity.this, "用户邮箱格式错误请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        userPhoneNumMyItemGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText phonenumEditText = new EditText(MyDetailInfoActivity.this);
                phonenumEditText.setText(userPhoneNumMyItemGroup.getContentTextViewText());
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

                //创建弹窗
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyDetailInfoActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("更改用户联系方式为").setView(phonenumEditText).setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 将信息更新至文本框中，待最终按下保存按钮再更新至本地共享文件并更新数据库
                        if (!phonenumEditText.getText().toString().isEmpty() && phonenumEditText.getError() == null) {
                            userPhoneNumMyItemGroup.setContentTextViewText(phonenumEditText.getText().toString());
                            Toast.makeText(MyDetailInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyDetailInfoActivity.this, "用户联系方式格式错误请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}