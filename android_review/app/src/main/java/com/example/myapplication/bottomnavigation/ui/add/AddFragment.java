package com.example.myapplication.bottomnavigation.ui.add;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.bottomnavigation.ui.SelectPicPopupWindow;
import com.example.myapplication.myview.MyTitleBar;
import com.example.myapplication.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment implements View.OnClickListener {
    private SelectPicPopupWindow menuWindow;
    private View root;
    public static final int REQUEST_PHOTO = 4;
    public static final int REQUEST_IMAGE = 5;
    private Uri mImageUri, mImageUriFromFile;
    private File imageFile;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.myapplication.fileprovider";

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


    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = "test";
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (Exception e) {
            Log.w("createImageFile", "" + e.getMessage());
        }
        return imageFile;
    }

    /**
     * 将拍的照片添加到相册
     *
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PHOTO) {
            try {
                /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mImageUri));
                Log.i("onActivityResult", "onActivityResult: imageUri " + mImageUri);
                stuffPic.setImageBitmap(bitmap);//显示到ImageView上
                delStuffPic.setVisibility(View.VISIBLE);
                galleryAddPic(mImageUriFromFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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


    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        imageFile = createImageFile();//创建用来保存照片的文件
        mImageUriFromFile = Uri.fromFile(imageFile);
        Log.i("takePhoto", "takePhoto: uriFromFile " + mImageUriFromFile);
        if (imageFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /*7.0以上要通过FileProvider将File转化为Uri*/
                mImageUri = FileProvider.getUriForFile(getActivity(), FILE_PROVIDER_AUTHORITY, imageFile);
            } else {
                /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                mImageUri = Uri.fromFile(imageFile);
            }
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
            startActivityForResult(takePhotoIntent, REQUEST_PHOTO);//打开相机
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