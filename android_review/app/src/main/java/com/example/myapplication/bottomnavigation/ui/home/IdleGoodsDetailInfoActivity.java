package com.example.myapplication.bottomnavigation.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.bean.CommentItem;
import com.example.myapplication.myview.MyTitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class IdleGoodsDetailInfoActivity extends AppCompatActivity {
    private MyTitleBar idleGoodsDetailInfoMyTitleBar;
    private EditText editComment;
    private LinearLayout ll_leave_comment;
    private RecyclerView rv_comments;

    private List<CommentItem> initTestComments() {
        List<CommentItem> commentItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CommentItem item = new CommentItem(UUID.randomUUID().toString());
            item.setContent("fake content" + i);
            item.setUsername("fake username" + i);
            item.setPortrait("fake content" + i);
            item.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            commentItems.add(item);
        }
        return commentItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_goods_detail_info);

        idleGoodsDetailInfoMyTitleBar = findViewById(R.id.myTitleBar_idleGoodsDetailInfo);
        editComment = findViewById(R.id.et_commemt);
        ll_leave_comment = findViewById(R.id.ll_leave_comment);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(this));
        rv_comments.setAdapter(new CommentAdapter(this, initTestComments()));

        getSupportActionBar().hide();
        editComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Log.d("onEditorAction", "" + v.getText());
            }
            return false;
        });
        ll_leave_comment.setOnClickListener(v -> {
            editComment.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) editComment.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.showSoftInput(editComment, 0);
            }
        });

        // 更改顶部菜单栏标题
        idleGoodsDetailInfoMyTitleBar.setTvTitleText("闲置物详情");
        idleGoodsDetailInfoMyTitleBar.getTvForward().setVisibility(View.GONE);


    }
}