package com.zhiku.temp.model;

import com.zhiku.temp.utils.TableUtils;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * Table表格的实体类
 *
 *
 * @version        jdk-11.0.2, 19/03/21
 * @author         ChenYanyu
 */
public class Table implements Serializable {

    /**
     * 长度
     */
    private int length = 0;

    /**
     * 宽度
     */
    private int hight = 0;

    /**
     * 转化成的二维数组
     */
    private ArrayList<String[]> table = null;

    /**
     * 无参构造器
     */
    public Table() {}

    /**
     * 初始化table
     * @param strs
     */
    public Table(ArrayList<String> strs) {

        // 初始化
        new TableUtils().convert(this, strs);

        // 去掉第二行的|--|--|(如果这个表格的行数大于1的话)
        if (hight > 1) {
            table.remove(1);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return 第x行，第y列的字符串
     */
    public String getElement(int x, int y) {
        return getTable().get(x)[y];
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getHight() {
        return hight;
    }

    /**
     * Method description
     *
     *
     * @param hight
     */
    public void setHight(int hight) {
        this.hight = hight;
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
    public ArrayList<String[]> getTable() {
        return table;
    }

    /**
     * Method description
     *
     *
     * @param table
     */
    public void setTable(ArrayList<String[]> table) {
        this.table = table;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
