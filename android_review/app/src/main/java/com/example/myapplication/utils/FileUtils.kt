package com.example.myapplication.utils

import java.io.File

object FileUtils {
    /**
     * 判断指定目录的文件夹是否存在，如果不存在则需要创建新的文件夹
     * @param fileName 指定目录
     * @return 返回创建结果 TRUE or FALSE
     */
    fun fileIsExist(fileName: String?): Boolean {
        //传入指定的路径，然后判断路径是否存在
        val file = File(fileName)
        return if (file.exists()) true else {
            //file.mkdirs() 创建文件夹的意思
            file.mkdirs()
        }
    }
}