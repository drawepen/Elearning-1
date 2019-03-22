package com.zhiku.temp;

import com.zhiku.temp.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;

public class md2txt {
    public void toolRun(String filePath,String courseID) throws IOException {
        //读取文件
        FileUtils fu = new FileUtils();
        ArrayList<String> strArr = fu.read2Array(filePath, "UTF-8");
        //
    }
}
