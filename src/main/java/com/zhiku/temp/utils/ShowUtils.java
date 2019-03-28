package com.zhiku.temp.utils;

import com.zhiku.temp.model.Table;
import com.zhiku.temp.model.ZKList;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 展示工具，编码格式是写死的，需要的话替换
 *
 *
 * @version        jdk-11.0.2, 19/03/21
 * @author         ChenYanyu
 */
public class ShowUtils {

    //获得classpath
    //file:/E:/5_IdeaProjects/list2db/target/classes/
    private String NOclassPath = this.getClass().getResource("/").toString();
    private String classPath = NOclassPath.substring(6,NOclassPath.length()-1) + "\\";
    /**
     * Method description
     *
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void show() throws IOException, ClassNotFoundException {
        FileUtils fu = new FileUtils();

        // 读取文件的正确操作
        String            tablePath =  classPath + "table.md";
        ArrayList<String> tabArr    = fu.read2Array(tablePath, "UTF-8");

        // System.out.println(arr.size());
        // 转换成对象
        Table table = new Table(tabArr);

        // 表格的打印测试
        System.out.println("\n-----------输入.md格式的文件，打印表格对象：begin----------");

        for (String[] strs : table.getTable()) {
            for (String str : strs) {
                System.out.print(str + " ");
            }

            System.out.println("");
        }

        System.out.println("-----------输入.md格式的文件，打印表格对象：finish----------");
        System.out.println("\n-----------表格测试序列化:begin--------------");

        // 表格序列化
        String serPath = classPath + "table.txt";
        fu.serialize(table, serPath);
        System.out.println("-----------表格测试序列化:finish--------------");
        System.out.println("\n-----------表格测试反序列化:begin--------------");

        // 表格反序列化
        Table table2 = fu.deSerializeTable(serPath);

        // 表格的打印测试（反序列化之后）
        for (String[] strs : table2.getTable()) {
            for (String str : strs) {
                System.out.print(str + " ");
            }

            System.out.println("");
        }

        System.out.println("-----------表格测试反序列化:finish--------------");
        System.out.println("-----------输入.md格式的文件，打印列表对象：begin----------");

        // 读取文件的正确操作
        String            listPath = classPath + "list.md";
//        String            listPath = classPath + "写作模板.md";
        ArrayList<String> listArr  = fu.read2Array(listPath, "UTF-8");

        // System.out.println(arr.size());
        // 转换成对象
        ZKList li = new ZKList(listArr);

        // 列表的测试打印
        for (String str : li.getListtxt()) {
            System.out.println(str);
        }

        System.out.println("-----------输入.md格式的文件，打印列表对象：finish----------");
        System.out.println("\n-----------列表测试反序列化:begin--------------");

        // 列表的序列化与反序列化
        serPath = classPath + "list.txt";
//        serPath = classPath + "写作模板.txt";
        fu.serialize(li, serPath);
        System.out.println("-----------列表测试序列化:finish--------------");

        // 表格反序列化
        ZKList li2 = fu.deSerializeList(serPath);

        // 列表的测试打印（反序列化之后）
        System.out.println("\n-----------列表测试反序列化:begin--------------");

        for (String str : li2.getListtxt()) {
            System.out.println(str);
        }

        System.out.println("-----------列表测试反序列化:finish--------------");
    }

}


//~ Formatted by Jindent --- http://www.jindent.com
