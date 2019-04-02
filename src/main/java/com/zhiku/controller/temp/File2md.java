package com.zhiku.controller.temp;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class File2md {

    public static void main(String[] args) {
        String s = "",s1 = "";
        try {
//            String pat = "\\[.*\\]\\((http|ftp|https):\\/\\/w{3}.(.+.)+.(com|cn|org)(\\/(.*))*\\)";
//            Pattern pa = Pattern.compile(pat);
//            String mm =  "咩咩咩咩咩咩咩咩吗[lll](http://www.baidu.com)宿;舍的[lll](http://www.baidu.com)打法[lll](http://www.baidu.com风格";
//            Matcher m = pa.matcher(mm);
//
//            while(m.find()){
//
//                System.out.println(m.group(5));
//                System.out.println("start："+m.start());
//                System.out.println("end："+m.end());
//            }
          //  Matcher mm = mm.
//            System.out.println(mm.split("]")[1]);
//            Connection conn = File.getCon();
            File2md f = new File2md();
//            f.dealFile("C:\\Users\\luminkuan\\Desktop\\lll.md","GBK");
            f.dealFile("E:\\Workbench\\IDEA\\Zhiku_workbench\\写作模板.md","UTF-8");
           // String sss = "ILOVEYOU!";
           // System.out.println(sss.substring(1));

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("error");
            System.out.println("happy");
        }
        System.out.println(s);
        System.out.println(s1);
//        String str2 = "abd123:adad46587:asdadasadsfgi#%^^9090";
//        System.out.println(str2.replaceAll("[0-9]", "*"));
//        System.out.println(str2.replaceAll("\\d", "*"));
    }
    public static Connection getCon(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://114.115.145.178:3306/zhiku";
            String user = "root";
            String password = "Zhiku_123";
            Connection conn = DriverManager.getConnection(url,user,password);

            Statement stat = conn.createStatement();

            String command = "select * from user";

            ResultSet result = stat.executeQuery(command);

            while(result.next()){
                System.out.println(result.getString(1));
            }
            result.close();
            return conn;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void dealFile(String path,String encode) throws IOException{
        ///////
        int jid=0;

        //图片对应的变量
        String picUrl = "";
        //链接的文字与URL对应的变量
        String content = "", link = "";
        //链接对应的正则
        String pat = "\\[.*\\]\\((http|ftp|https):\\/\\/w{3}.(.+.)+.(com|cn|org)(\\/(.*))*\\)";
        Pattern pa = Pattern.compile(pat);
        //链接那一行除了链接以外的文本
        String unlink = "";
        //标题对应的标志
        String title_1 = "#",title_2 = "##",title_3 = "###";
        //三级标题各自对应的变量
        String title1 = "",title2 = "",title3 = "",paragraph = "";
        //按行读文件
        FileInputStream fis=new FileInputStream(path);
        InputStreamReader isr=new InputStreamReader(fis, encode);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        boolean isHead = false;
        while((line = br.readLine())!=null){
            jid++;
            boolean tag = false;
            Matcher m = pa.matcher(line);
            while(m.find()){
                tag = true;
                String s = m.group();
                for(int i=0;i<s.length();i++){

                    if(s.charAt(i)=='('){
                        i++;
                        while(s.charAt(i)!=')'){
                            link += s.charAt(i);
                            i++;
                        }
                        i++;
//                        System.out.print("link   ");
                        System.out.print(jid+"link   ");
                        System.out.println(link);
                        link = "";
                    }
                    unlink += s.charAt(i);
                }
            }
            if(!unlink.isEmpty()){
                System.out.println(unlink);
                unlink = "";
            }
            if(tag){
                line = "";
            }


            if (line.startsWith(title_3)) {
                if(!paragraph.equals("")){
//                    System.out.print("para     ");
                    System.out.print(jid+"para     ");
                    System.out.println(paragraph);
                    paragraph = "";
                }
                title3 = line.substring(3);
                System.out.print(jid+"titlt3    ");
                System.out.println(title3);
                continue;

            }//最下面的情况是段落的情况
            else if (line.startsWith(title_2)) {
                if(!paragraph.equals("")){
                    System.out.print(jid+"para     ");
                    System.out.println(paragraph);
                    paragraph = "";
                }
                System.out.print(jid+"titlt2    ");
                title2 = line.substring(2);
                System.out.println(title2);
                continue;

            }
            else if (line.startsWith(title_1)) {
                if(!paragraph.equals("")){
                    System.out.print(jid+"para     ");
                    System.out.println(paragraph);
                    paragraph = "";
                }
                isHead = true;
                title1 = line.substring(1, line.length() - 1);
                System.out.print(jid+"titlt1    ");
                System.out.println(title1);
//                File2md.writeFile("C:\\Users\\luminkuan\\Desktop\\biaoti1.txt", title1);
                File2md.writeFile("E:\\duanluo.txt",paragraph);
                continue;
            }
            else if(line.startsWith("!")){
                if(!paragraph.equals("")){
                    System.out.print(jid+"para     ");
                    System.out.println(paragraph);
                    paragraph = "";
                }
                picUrl = line.split("]")[1];
                System.out.print(jid+"pic   ");
                System.out.println(picUrl.substring(1,picUrl.length()-1));
                continue;
            }
//            else if(line.startsWith("[")){
//                content = line.substring(0,line.indexOf("]"));
//                link = line.substring(line.indexOf("("),line.indexOf(")"));
//                System.out.println("link    "+link);
//                System.out.println("content    "+content);
//                continue;
//            }
            else if(line.endsWith("\n")){
                System.out.print(jid+"para     ");
                System.out.println(paragraph);
                paragraph = "";
            }
            else if(!line.isEmpty()){
                paragraph += line;
                continue;
            }
            if(isHead&&!title1.equals("")&&!title3.equals("")&&!paragraph.equals("")){

                isHead = false; //此处与数据库交互
//                File2md.writeFile("C:\\Users\\luminkuan\\Desktop\\duanluo.txt",paragraph);
                File2md.writeFile("E:\\duanluo.txt",paragraph);
                System.out.print(jid+"para     ");
                System.out.println(paragraph);
                paragraph = "";
            }
            if(line.isEmpty()){
                if(!paragraph.equals("")){
                    System.out.print(jid+"para     ");
                    System.out.println(paragraph);
                    paragraph = "";
                }
            }
        }
        if(!paragraph.isEmpty()){
            System.out.println(paragraph);
        }
        br.close();
        isr.close();
        fis.close();
    }
    public static void writeFile(String path, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append(content);
        osw.flush();
    }
    /////////////
    public static boolean dealFile2(ArrayList<String> strArr,ArrayList<tempParagraph> saveP){
        //图片对应的变量
        String picUrl = "";
        //链接的文字与URL对应的变量
        String content = "", link = "";
        //链接对应的正则
        String pat = "\\[.*\\]\\((http|ftp|https):\\/\\/w{3}.(.+.)+.(com|cn|org)(\\/(.*))*\\)";
        Pattern pa = Pattern.compile(pat);
        //链接那一行除了链接以外的文本
        String unlink = "";
        //标题对应的标志
        String title_1 = "#",title_2 = "##",title_3 = "###";
        //三级标题各自对应的变量
        String title1 = "",title2 = "",title3 = "",paragraph = "";
        int jid=0;
        boolean isHead = false;
        for(String line:strArr){
            jid++;
            boolean tag = false;
            Matcher m = pa.matcher(line);
            while(m.find()){
                tag = true;
                String s = m.group();
                for(int i=0;i<s.length();i++){

                    if(s.charAt(i)=='('){
                        i++;
                        while(s.charAt(i)!=')'){
                            link += s.charAt(i);
                            i++;
                        }
                        i++;
//                        System.out.print("link    ");
//                        System.out.println(link);
                        saveP.add(new tempParagraph( jid,md2txt.type[9],link ));
                        link = "";
                    }
                    unlink += s.charAt(i);
                }
            }
            if(!unlink.isEmpty()){
//                System.out.println(unlink);
                saveP.add(new tempParagraph( jid,md2txt.type[9],unlink ));
                unlink = "";
            }
            if(tag){
                line = "";
            }


            if (line.startsWith(title_3)) {
                if(!paragraph.equals("")){
//                    System.out.print("para     ");
//                    System.out.println(paragraph);
                    paragraph = "";
                }
                title3 = line.substring(3);
//                System.out.print("titlt3    ");
//                System.out.println(title3);
                saveP.add(new tempParagraph( jid,md2txt.type[2],title3 ));
                continue;

            }//最下面的情况是段落的情况
            else if (line.startsWith(title_2)) {
                if(!paragraph.equals("")){
//                    System.out.print(jid+"para     ");
//                    System.out.println(paragraph);
                    paragraph = "";
                }
//                System.out.print(jid+"titlt2    ");
                title2 = line.substring(2);
//                System.out.println(title2);
                saveP.add(new tempParagraph( jid,md2txt.type[1],title2 ));
                continue;

            }
            else if (line.startsWith(title_1)) {
                if(!paragraph.equals("")){
//                    System.out.print(jid+"para     ");
//                    System.out.println(paragraph);
                    paragraph = "";
                }
                isHead = true;
                title1 = line.substring(1, line.length() - 1);
//                System.out.print(jid+"titlt1    ");
//                System.out.println(title1);
//                File2md.writeFile("C:\\Users\\luminkuan\\Desktop\\biaoti1.txt", title1);
                saveP.add(new tempParagraph( jid,md2txt.type[0],title1 ));
//                File2md.writeFile("E:\\duanluo.txt",paragraph);
                continue;
            }
            else if(line.startsWith("!")){
                if(!paragraph.equals("")){
//                    System.out.print(jid+"para     ");
//                    System.out.println(paragraph);
                    paragraph = "";
                }
                picUrl = line.split("]")[1];
//                System.out.print(jid+"pic   ");
//                System.out.println(picUrl.substring(1,picUrl.length()-1));
                saveP.add(new tempParagraph( jid,md2txt.type[3],picUrl.substring(1,picUrl.length()-1) ));
                continue;
            }
//            else if(line.startsWith("[")){
//                content = line.substring(0,line.indexOf("]"));
//                link = line.substring(line.indexOf("("),line.indexOf(")"));
//                System.out.println("link    "+link);
//                System.out.println("content    "+content);
//                continue;
//            }
            else if(line.endsWith("\n")){
//                System.out.print(jid+"para     ");
//                System.out.println(paragraph);
                paragraph = "";
            }
            else if(!line.isEmpty()){
                paragraph += line;
                continue;
            }
            if(isHead&&!title1.equals("")&&!title3.equals("")&&!paragraph.equals("")){

                isHead = false; //此处与数据库交互
//                File2md.writeFile("C:\\Users\\luminkuan\\Desktop\\duanluo.txt",paragraph);
//                File2md.writeFile("E:\\duanluo.txt",paragraph);
//                System.out.print(jid+"para     ");
//                System.out.println(paragraph);
                paragraph = "";
            }
            if(line.isEmpty()){
                if(!paragraph.equals("")){
//                    System.out.print("para     ");
//                    System.out.println(paragraph);
                    paragraph = "";
                }
            }
        }

        return true;
    }
}
