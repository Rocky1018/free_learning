package com.example.myapplication.bottomnavigation.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.myview.MyTitleBar;


public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private MyTitleBar releaseIdleMyTitleBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        releaseIdleMyTitleBar = (MyTitleBar) root.findViewById(R.id.myTitleBar_releaseIdle);

        releaseIdleMyTitleBar.setTvTitleText("发布闲置物");
        releaseIdleMyTitleBar.setTvForwardText("发布");

        releaseIdleMyTitleBar.getTvForward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "发布闲置物", Toast.LENGTH_SHORT).show();
            }
        });

        releaseIdleMyTitleBar.getIvBackward().setVisibility(View.INVISIBLE);

        return root;
    }
}