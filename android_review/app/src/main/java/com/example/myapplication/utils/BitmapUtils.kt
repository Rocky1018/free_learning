package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object BitmapUtils {
    fun saveBitmap(name: String?, bm: Bitmap, mContext: Context) {
        Log.d("BitmapUtils", "Ready to save picture")
        //指定我们想要存储文件的地址
        val TargetPath = "/images/"
        Log.d("BitmapUtils", "Save Path=$TargetPath")
        //判断指定文件夹的路径是否存在
        if (!FileUtils.fileIsExist(TargetPath)) {
            Log.d("BitmapUtils", "TargetPath isn't exist")
        } else {
            //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
            val saveFile = File(TargetPath, name)
            try {
                val saveImgOut = FileOutputStream(saveFile)
                // compress - 压缩的意思
                bm.compress(Bitmap.CompressFormat.JPEG, 100, saveImgOut)
                //存储完成后需要清除相关的进程
                saveImgOut.flush()
                saveImgOut.close()
                Log.d("BitmapUtils", "The picture is save to your phone!")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}