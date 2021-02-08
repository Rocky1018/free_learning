package com.example.myapplication.bottomnavigation.ui.add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.bottomnavigation.ui.SelectPicPopupWindow;
import com.example.myapplication.myview.MyTitleBar;

import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment implements View.OnClickListener {
    private SelectPicPopupWindow menuWindow;
    private View root;
    public static final int REQUEST_PHOTO = 4;
    public static final int REQUEST_IMAGE = 5;

    private MyTitleBar releaseIdleMyTitleBar;
    private ImageView stuffPic, delStuffPic;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add, container, false);

        releaseIdleMyTitleBar = root.findViewById(R.id.myTitleBar_releaseIdle);
        stuffPic = root.findViewById(R.id.add_stuff_pic);
        delStuffPic = root.findViewById(R.id.clear_stuff_pic);
        menuWindow = new SelectPicPopupWindow(getActivity(), this);
        stuffPic.setOnClickListener(this);
        delStuffPic.setOnClickListener(this);
        releaseIdleMyTitleBar.setTvTitleText("发布闲置物");
        releaseIdleMyTitleBar.setTvForwardText("发布");

        releaseIdleMyTitleBar.getTvForward().setOnClickListener(v -> Toast.makeText(getContext(), "发布闲置物", Toast.LENGTH_SHORT).show());

        releaseIdleMyTitleBar.getIvBackward().setVisibility(View.INVISIBLE);

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            stuffPic.setImageBitmap(imageBitmap);
            delStuffPic.setVisibility(View.VISIBLE);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE) {
            Uri fullPhotoUri = data.getData();
            stuffPic.setImageURI(fullPhotoUri);
            delStuffPic.setVisibility(View.VISIBLE);
        }
        menuWindow.dismiss();
        Toast.makeText(getActivity(), "当前版本仅支持上传一张图片", Toast.LENGTH_SHORT).show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }


    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_PHOTO);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_stuff_pic:
                menuWindow.showAtLocation(root.findViewById(R.id.ll_total_fragment),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.clear_stuff_pic:
                stuffPic.setImageResource(R.drawable.ic_pic_add_24);
                delStuffPic.setVisibility(View.GONE);
                break;

            case R.id.item_popupwindows_camera:
                takePhoto();
                break;
            case R.id.item_popupwindows_Photo:
                selectImage();
                break;
        }
    }
}