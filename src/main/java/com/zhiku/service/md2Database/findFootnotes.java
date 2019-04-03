package com.zhiku.service.md2Database;

import java.util.ArrayList;
//class findFootnotes {
//
//}
public class findFootnotes {
    int id;//段落id
    int bs;//标识，1：脚注  type[7]：换行文本  type[8]：不换行文本//0每段第一个脚注且前面没有字符串//-1脚注注释匹配成功
    String h;//[^h]
    String p;//[^h]:p
    findFootnotes next=null;
    findFootnotes(int id,int bs, String h, String p) {
        this.id=id;
        this.bs=bs;
        this.h=h;
        this.p=p;
    }
    public static boolean string2footnote(ArrayList<String> strArr,ArrayList<tempParagraph> saveP){
        findFootnotes footh=new findFootnotes(-1,1,null,null);
        findFootnotes footp=new findFootnotes(-1,1,null,null);
        findFootnotes nowfooth=footh;
        findFootnotes nowfootp=footp;
        int jid=0;
        for(String str0:strArr) {
            jid++;
            if (str0 == null || str0.equals( "" )) {
                continue;
            }
            String[] strs=str0.split( "\\[\\^.+\\]" );
            if(strs[0].equals( str0 )){//普通段落
                continue;
            }
            boolean db = false;
            boolean boolfir = true;//[^是否是该行第一个非空格字符串
            String tempstr = "";
            for (int i = 0; i < str0.length(); i++) {
                if (db) {//堆里有[^
                    if (str0.charAt( i ) == ']') {
                        if (boolfir && i < str0.length() - 1 && str0.charAt( i + 1 ) == ':') {//脚注注释
                            String strh = tempstr;
                            tempstr = "";
                            for (int j = i + 2; j < str0.length(); j++) {//注释内容可以为空
                                tempstr += str0.charAt( j );
                            }
                            nowfootp.next= new findFootnotes( jid,1, strh, tempstr );
                            nowfootp=nowfootp.next;
                            tempstr = "";//不清空tempstr，之后会当做没有配对的文本
                            if(nowfooth.id==nowfootp.id&&nowfooth.h!=null){
                                String[] temps=nowfooth.h.split( " +" );
                                if(temps.length==0||temps[0].equals( "" )){//脚注注释同行前一个字符串是全空格，不记录
                                    nowfooth.h=null;
                                }
                            }
                            break;
                        } else {
                            if(boolfir&&strs[0].equals("")){//第一个脚注且前面没有字符串
                                nowfooth.next = new findFootnotes( jid,0, tempstr, null );
                            }else{
                                nowfooth.next = new findFootnotes( jid,1, tempstr, null );
                            }
                            nowfooth=nowfooth.next;
                            tempstr = "";
                        }
                        db = false;//从堆中移出[^
                        boolfir = false;//前有[^.+] ，之后的[^不可能为第一个非空字符串
                    } else {
                        tempstr += str0.charAt( i );
                    }
                } else if (str0.charAt( i ) == '[' && i < str0.length() - 1 && str0.charAt( i + 1 ) == '^') {//[^入堆
                    if (!tempstr.equals( "" )) {//储存文本
                        if(tempstr.equals(strs[0])){//最开头文本，换行文本
                            nowfooth.next=new findFootnotes( jid, md2txt.type[7], tempstr, null );
                            nowfooth=nowfooth.next;
//                            saveP.add( new tempParagraph( jid,md2txt.type[7] , tempstr ) );
                        }else{//脚注后面，不换行文本
                            nowfooth.next=new findFootnotes( jid, md2txt.type[8] , tempstr ,null );
                            nowfooth=nowfooth.next;
//                            saveP.add( new tempParagraph( jid, md2txt.type[8], tempstr ) );
                        }
                    }
                    tempstr = "";
                    db = true;
                    i++;
                } else {
                    tempstr += str0.charAt( i );
                    if (boolfir && str0.charAt( i ) != ' ') {//字符为不为空，之后的[^不可能为第一个非空字符串
                        boolfir = false;
                    }
                }
            }
            //最后字符串处理
//            String sumstr=tempstr;
//            if(firstr!=null){
//                sumstr=firstr+"[^"+tempstr;
//            }
            if(!tempstr.equals( "" )){
                if(db){//[^没有配对]或]:，作为普通文本的部分字符串
//                    saveP.add( new tempParagraph( jid, md2txt.type[8], "[^"+tempstr ) );
                    nowfooth.next=new findFootnotes( jid, md2txt.type[8], "[^"+tempstr ,null );
                    nowfooth=nowfooth.next;
                }else{
//                    saveP.add( new tempParagraph( jid, md2txt.type[8], tempstr ) );
                    nowfooth.next=new findFootnotes( jid, md2txt.type[8], tempstr ,null );
                    nowfooth=nowfooth.next;
                }
            }
        }
        //footp链表倒转，优先匹配后注释
        if(footp.next!=null) {
            findFootnotes jlf=footp.next;
            for(findFootnotes f=jlf.next;f!=null;) {
                findFootnotes tf=f.next;
                f.next=jlf;
                jlf=f;
                f=tf;
            }
            footp.next.next=null;//第一个真节点变成最后一个，next要指null
            footp.next=jlf;
        }
        //匹配脚注
        nowfooth=footh.next;
        nowfootp=footp.next;
        while(nowfooth!=null) { //
            if(nowfooth.h==null||nowfooth.h.equals( "" )){//没有字符串不处理
                nowfooth=nowfooth.next;
                continue;
            }
            if(nowfooth.bs>1){//不是脚注不用匹配
                saveP.add( new tempParagraph( nowfooth.id, (char) nowfooth.bs, nowfooth.h ) );
                nowfooth=nowfooth.next;
                continue;
            }
            nowfootp=footp.next;
            while(nowfootp!=null){
                if(nowfooth.h.equals( nowfootp.h )){//匹配成功
                    saveP.add( new tempParagraph( nowfooth.id, md2txt.type[6], nowfootp.p ) );
                    nowfootp.bs=-1;
                    saveP.add( new tempParagraph( nowfootp.id, md2txt.type[6], null) );
                    break;
                }
                nowfootp=nowfootp.next;
            }
            if(nowfootp==null){//nextfooth匹配失败，作为普通文本
                if(nowfooth.bs==0){//开头脚注，换行
                    saveP.add( new tempParagraph( nowfooth.id, md2txt.type[7], "[^"+nowfooth.h+"]" ) );
                }else{
                    saveP.add( new tempParagraph( nowfooth.id, md2txt.type[8], "[^"+nowfooth.h+"]" ) );
                }
            }//如果匹配成功，nextfootp也已经移出
            nowfooth=nowfooth.next;
        }
        //剩下的footp没有匹配，作为普通段落，不处理

        return true;
    }
}

