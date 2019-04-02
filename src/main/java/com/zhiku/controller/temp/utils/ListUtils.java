package com.zhiku.controller.temp.utils;

import com.zhiku.controller.temp.model.ZKList;

import java.util.ArrayList;

/**
 * 列表以“+ ”开头，一行的开头不允许有空格，+后面必须有空格，空格后为实际内容
 * 仅在一段中处理
 *
 *
 * @version        jdk-11.0.2, 19/03/21
 * @author         ChenYanyu
 */
public class ListUtils {

    /**
     * Method description
     *
     *
     * @param li
     * @param strs
     */
    public void convert(ZKList li, ArrayList<String> strs) {

        // 获得Md格式的转化后列表
        li.setListMd(convertMd(strs));

        // 获得去掉+ 的转化后列表
        li.setListtxt(convertTxt(strs));
    }

    /**
     * 列表转换成md格式，保留“+ ”
     *
     *
     * @param strs
     *
     * @return
     */
    public ArrayList<String> convertMd(ArrayList<String> strs) {

        // 储存结果
        ArrayList<String> lists = new ArrayList<String>();

        for (String str : strs) {

            // 如果这一行的第一个字符是+ ，第二个字符是空格，就是列表结构
            if ((str.charAt(0) == '+') && (str.charAt(1) == ' ')) {

                // 顺便去掉首尾空格
                lists.add(str.trim());
            }

            // 如果列表中间有嵌套，或者有不是列表元素的东西，则不认为是列表
            else {
                return null;
            }
        }

        return lists;
    }

    /**
     *
     * 列表转换成纯文本，不保留“+ ”
     *
     * @param strs
     *
     * @return
     */
    public ArrayList<String> convertTxt(ArrayList<String> strs) {

        // 储存结果
        ArrayList<String> lists = new ArrayList<String>();

        for (String str : strs) {

            // 如果这一行的第一个字符是+ ，第二个字符是空格，就是列表结构
            if ((str.charAt(0) == '+') && (str.charAt(1) == ' ')) {

                // 不储存+和空格,顺便去掉首尾空格
                lists.add(str.substring(2).trim());
            }

            // 如果列表中间有嵌套，或者有不是列表元素的东西，则不认为是列表
            else {
                return null;
            }
        }

        return lists;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
