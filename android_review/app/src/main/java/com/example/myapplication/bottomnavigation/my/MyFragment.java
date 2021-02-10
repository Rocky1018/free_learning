package com.example.myapplication.bottomnavigation.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activity.BottomNavigationActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.MyCollectedListActivity;
import com.example.myapplication.activity.MyDetailInfoActivity;
import com.example.myapplication.utils.Config;
import com.example.myapplication.utils.SharePreferencesUtils;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MyFragment";

    private Button logoutButton;
    private Button showMyInfoDetailButton;
    private ImageView myCollectionImageView;
    private TextView myCollectionTextView;
    private LinearLayout myUserInfoLinearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        showMyInfoDetailButton = root.findViewById(R.id.btn_showMyInfoDetail);
        logoutButton = root.findViewById(R.id.btn_logout);
        myCollectionImageView = root.findViewById(R.id.iv_myCollection);
        myCollectionTextView = root.findViewById(R.id.tv_myCollection);
        myUserInfoLinearLayout = root.findViewById(R.id.ly_myUserInfo);


        // 查看我收藏的事件绑定
        myCollectionImageView.setOnClickListener(this);
        myCollectionTextView.setOnClickListener(this);

        // 简单个人信息展示点击进入详细信息展示页
        myUserInfoLinearLayout.setOnClickListener(this);
        showMyInfoDetailButton.setOnClickListener(this);

        // 退出登录事件绑定
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BottomNavigationActivity.class);
            startActivity(intent);
            getActivity().finish();
            Config.INSTANCE.setUser(null);
            SharePreferencesUtils.clear(getContext());
            Toast.makeText(getContext(), "退出登录", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_showMyInfoDetail:
                break;
            case R.id.ly_myUserInfo:
                // 简单个人信息展示点击进入详细信息展示页
                intent = new Intent(getContext(), MyDetailInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_myCollection:
            case R.id.iv_myCollection:
                intent = new Intent(getContext(), MyCollectedListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
