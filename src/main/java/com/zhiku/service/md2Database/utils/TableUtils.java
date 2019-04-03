package com.zhiku.service.md2Database.utils;

import com.zhiku.service.md2Database.model.Table;

import java.util.ArrayList;

/**
 * Markdown表格转换成纯文本的工具
 * 格式如下
 * |列1|列2|
 * |--|--|
 * |数据1|数据2|
 *
 * 仅在一段中处理
 *
 *
 * @version        jdk-11.0.2, 19/03/21
 * @author         ChenYanyu
 */
public class TableUtils {

    /**
     * 将表格转换成一个二维列表
     *
     *
     * @param strs
     *
     * @return
     */
    public ArrayList<String[]> convert(ArrayList<String> strs) {

        // 储存结果
        ArrayList<String[]> lists = new ArrayList<String[]>();

        for (String str : strs) {

            // 如果第一个和最后一个元素不是||则不是表格
            if ((str.charAt(0) == '|') && (str.charAt(str.length() - 1) == '|')) {

                // 将这一行的元素储存进结果集中
                lists.add(strSplitByl(str));
            } else {
                return null;
            }
        }

        return lists;
    }

    /**
     * 将段内容（表格）转化为Table对象，并储存在该对象中
     *
     *
     * @param tab
     * @param strs
     */
    public void convert(Table tab, ArrayList<String> strs) {
        ArrayList<String[]> tabArr = convert(strs);

        // 设置高度
        tab.setHight(tabArr.size());

        // 设置长度
        tab.setLength(tabArr.get(0).length);

        // 设置数据
        tab.setTable(tabArr);
    }

    /**
     * 处理字符串分割的第一行和最后一行是空的情况
     *
     *
     * @param s
     *
     * @return
     */
    public String[] strSplitByl(String s) {
        String[] strs  = s.split("\\|");
        String[] strs2 = new String[strs.length - 1];

        // 去头不去尾，因为头是空的，尾不是
        for (int i = 1; i <= strs.length - 1; i++) {

            // 去掉首尾空格
            strs2[i - 1] = strs[i].trim();
        }

        return strs2;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
