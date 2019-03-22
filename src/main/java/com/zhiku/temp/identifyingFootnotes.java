package com.zhiku.temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class identifyingFootnotes {
    public boolean string2footnote(ArrayList<String> strArr){


        return true;
    }
    public static void main(String[] args){
        Scanner in =new Scanner( System.in );
        String fileAddress="E:\\Workbench\\IDEA\\TEST\\src\\智库工程\\test.txt";
        String[] str=new String[1000];
        int[] type=new int[1000];//1普通字符串且换行，11普通字符串且不换行，22脚标型链接(不换行）
        String finalstr="";//每节最后一段
        int nowi=0;
        int p=1;//储存脚标前p个字符作为脚标型链接
        File file = new File(fileAddress);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String str0 = null;
            // 一次读入一行，直到读入null为文件结束
            while ((str0  = reader.readLine()) != null) {
                if(str0.equals( "" )){
                    continue;
                }
                str0=str0+"@";
                String[] strs=str0.split("\\[\\^.+\\]");
                System.out.println(str0+" "+strs.length);
                if(strs[0].equals( str0)){//普通段落

                }else if(strs[0].equals( "" )){//脚注注释部分
                    if(strs[1].charAt( 0 )==':')//如：“[^we]er”为普通字符串
                        finalstr+=strs[1].substring(1,strs[1].length()-1)+"\n";
                    else{

                    }
                }else{
                    str[nowi]=strs[0].substring(0,strs[0].length()-p);
                    type[nowi++]=1;
                    for(int i=1;i<strs.length;i++){
                        str[nowi]=strs[i-1].substring( strs[i-1].length()-p);
                        type[nowi++]=22;
//                        System.out.println( i+"          "+strs[i] );///////////////
                        str[nowi]=strs[i].substring(0,strs[i].length()-p);
                        type[nowi++]=11;
                    }
                    if(strs[strs.length-1].equals( "@" )){//原字符串最后是[^]
                        nowi--;
                    }
                }
                if(nowi>=997){
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        for(int i=0;i<nowi;i++){
            System.out.println(type[i]+"  "+ str[i] );
        }
        System.out.println(finalstr );
    }
}

