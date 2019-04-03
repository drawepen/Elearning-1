package com.zhiku.service.md2Database.model;

import com.zhiku.service.md2Database.utils.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 列表的实体类
 *
 *
 * @version        jdk-11.0.2, 19/03/21
 * @author         ChenYanyu
 */
public class ZKList implements Serializable {
    private int               length  = 0;
    private ArrayList<String> listMd  = null;
    private ArrayList<String> listtxt = null;

    /**
     * 无参构造
     */
    public ZKList() {}

    /**
     * 初始化列表
     * @param strs
     */
    public ZKList(ArrayList<String> strs) {

        // 初始化列表
        new ListUtils().convert(this, strs);

        // 设置长度
        length = listMd.size();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * Method description
     *
     *
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public ArrayList<String> getListMd() {
        return listMd;
    }

    /**
     * Method description
     *
     *
     * @param listMd
     */
    public void setListMd(ArrayList<String> listMd) {
        this.listMd = listMd;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public ArrayList<String> getListtxt() {
        return listtxt;
    }

    /**
     * Method description
     *
     *
     * @param listtxt
     */
    public void setListtxt(ArrayList<String> listtxt) {
        this.listtxt = listtxt;
    }

    public String toHtml(){
        StringBuilder html = new StringBuilder();
        //增加<ul> </ul>
        html.append("<ul>");
        html.append("\n");
        for (String row:listtxt){
            //增加<li> 元素 </li>
            html.append("<li>");
            html.append(row);
            html.append("</li>");
            //换行
            html.append("\n");
        }
        html.append("</ul>");
        //换行
        html.append("\n");
        return html.toString();
    }

    @Override
    public String toString() {
        return toHtml();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
