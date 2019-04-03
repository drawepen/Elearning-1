package com.zhiku.service.md2Database;

import com.zhiku.entity.Knowledge;
import com.zhiku.entity.Paragraph;
import com.zhiku.entity.Section;
import com.zhiku.mapper.CourseMapper;
import com.zhiku.mapper.KnowledgeMapper;
import com.zhiku.mapper.ParagraphMapper;
import com.zhiku.mapper.SectionMapper;
import com.zhiku.service.md2Database.model.Table;
import com.zhiku.service.md2Database.model.ZKList;
import com.zhiku.service.md2Database.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
@Service
public class md2txt {
    /***********i|type|char*******************
     * |0|#|21|
     * |1|##|22|
     * |2|###|23|
     * |3|图片|30|
     * |4|表格|40|
     * |5|列表|50|
     * |6|脚注|60|
     * |7|换行文本|71|
     * |8|不换行文本|72|
     * |9|链接|80|
     * content==null表示在其他段中合并储存
     */
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private KnowledgeMapper knowledgeMapper;
    @Autowired
    private ParagraphMapper paragraphMapper;
    public static char[] type={21,22,23,30,40,50,60,71,72,80};
    public static md2txt md2txtUtils;
    @PostConstruct
    public void init() {
        md2txtUtils = this;
    }
    public static void toolRun(String filePath,Integer courseID,Integer courseSeq) throws IOException {
        //读取文件
        FileUtils fu = new FileUtils();
        ArrayList<String> strArr = fu.read2Array(filePath, "UTF-8");
        ArrayList<tempParagraph> saveP=new ArrayList<tempParagraph>( );
        //识别表格、列表
        findTableList( strArr,saveP );
        //识别脚注
        findFootnotes.string2footnote( strArr, saveP);
        //识别标题、图片、链接
        File2md.dealFile2(strArr,saveP);
        //储存其他段落
        findParagraph( strArr,saveP );
        //排序
        Collections.sort(saveP, new SortById());
        //储存到服务器
        saveParagraph(saveP,courseID,courseSeq);

        //test
        for(tempParagraph tp:saveP){
            if(tp.getContent()==null){
            }else if(tp.getType()==type[0]){
                System.out.print( "\n"+tp.id+":<标题1>:"+tp.getContent());
            }else if(tp.getType()==type[1]){
                System.out.print( "\n"+tp.id+":<标题2>:"+tp.getContent());
            }else if(tp.getType()==type[2]){
                System.out.print( "\n"+tp.id+":<标题3>:"+tp.getContent());
            }else if(tp.getType()==type[3]){
                System.out.print( "\n"+tp.id+":<图片>:"+tp.getContent());
            }else if(tp.getType()==type[4]){
                System.out.print( "\n"+tp.id+":<表格>:"+tp.getContent());
            }else if(tp.getType()==type[5]){
                System.out.print( "\n"+tp.id+":<列表>:"+tp.getContent());
            }else if(tp.getType()==type[7]){
                System.out.print( "\n"+tp.id+":<段落>:"+tp.getContent());
            }else if(tp.getType()==type[6]){//脚注
                System.out.print( "[<"+tp.getContent()+">]");
            }else if(tp.getType()==type[8]){//不换行
                System.out.print(tp.getContent());
            }else if(tp.getType()==type[9]){
                System.out.print( "\n"+tp.id+":<链接>:"+tp.getContent());
            }
        }
    }
    public static boolean findTableList(ArrayList<String> strArr,ArrayList<tempParagraph> saveP){
        int jid=0;
        int firid = 0;
        boolean islist=false,istable=false;
        ArrayList<String> strA2=new ArrayList<String> ();
        for(String str0:strArr){
            jid++;
            str0=str0.trim();
            if(str0.equals( "" )){
                continue;
            }
            String[] strs=str0.split( " +" );
            int ti=0;
            if(strs.length>1){
                if(strs[0].equals( "" )) {//开头空格
                    ti = 1;
                }else{
                    ti=0;
                }
            }else if(strs.length==1&&!strs[0].equals( "" )){//没有空格或最后有空格
                 ti=0;
            }else if(strs.length==0){//全空格，表格列表集不终结
                continue;
            }
            boolean savelist=false,savetable=false;
            if(strs[ti].charAt( 0 )=='|'){//表格
                if(islist){
                    savelist=true;
                }
                if(!istable){
                    firid=jid;//记第一个表格段
                }
                strA2.add(str0.trim());
                istable=true;
            }else if(strs[ti].charAt( 0 )=='+'&&strs[ti].length()==1){//列表 "+ ",空格分割，+号只有一个字符
                if(istable){
                    savetable=true;
                }
                if(!islist){
                    firid=jid;//记第一个表格段
                }
                strA2.add(str0.trim());
                islist=true;
            }else{
                if(islist){
                    savelist=true;
                }else if(istable){
                    savetable=true;
                }
                islist=false;
                istable=false;
            }
            if(savelist){//储存列表
                String str=getList(strA2);
                if(str!=null){//列表创建成功
//                    System.out.println( "储存列表" );//////////////////////////
                    saveP.add(new tempParagraph(firid,md2txt.type[5],str));
                    strA2.clear();
                    for(int i=firid+1;i<jid;i++){//标记这些段是特殊段落
                        saveP.add(new tempParagraph( i,md2txt.type[5],null ));
                    }
                }
                islist=false;//列表终结
            }else if(savetable){
                String str=getAtable(strA2);
                if(str!=null){//列表创建成功
                    saveP.add(new tempParagraph(firid,md2txt.type[4],str));
                    strA2.clear();
                    for(int i=firid+1;i<jid;i++){//标记这些段是特殊段落
                        saveP.add(new tempParagraph( i,md2txt.type[4],null ));
                    }
                }
                islist=false;//列表终结
            }
        }
        return true;
    }
    public static String getAtable(ArrayList<String> strArr ){
        ArrayList<String> tabArr= (ArrayList<String>) strArr.clone();//防止原数据丢失
        String tablepath="E://table.txt";
        try{
            Table table = new Table(tabArr);
            return FileUtils.TabList2Str( table ,tablepath );
        }catch (Exception e){
        }
        return null;
    }
    public static String getList(ArrayList<String> strArr ){
        ArrayList<String> listArr= (ArrayList<String>) strArr.clone();//防止原数据丢失
        String tablepath="E://list.txt";
        try{
            ZKList li = new ZKList(listArr);
            return FileUtils.TabList2Str( li ,tablepath );
        }catch (Exception e){
        }
        return null;
    }
    public static boolean findParagraph(ArrayList<String> strA,ArrayList<tempParagraph> saveP){
        int[] type=new int[strA.size()+1];
        for(tempParagraph tp:saveP){
            if(type[tp.id]==md2txt.type[4]||type[tp.id]==md2txt.type[5]){//与表格属性冲突，只存表格
                tp.setContent( null );
            }else{
                type[tp.id]=tp.getType();
            }
        }
        int jid=0;
        for(String str:strA){
            jid++;
            if(type[jid]==0&&str.trim().length()!= 0){//本段不是特殊段且不为空或空格
                saveP.add(new tempParagraph( jid,md2txt.type[7],str ));
            }
        }
        return true;
    }
    public static boolean saveParagraph(ArrayList<tempParagraph> saveP,Integer courseID,Integer courseSeq){
        Section section=new Section();
        Knowledge knowledge=new Knowledge();
        Paragraph paragraph=new Paragraph();
        int ji=0;
        //序号
        int secseq=md2txtUtils.sectionMapper.selectSectionMaxID(courseID);
        if(secseq==0){
            secseq=courseSeq*100;
        }
        int knowseq=0;
        int pagseq=0;
        //id
        int secid=-1;
        int knowid=-1;

        for(tempParagraph tp:saveP){
            if(tp.getContent()==null){
            }else if(tp.getType()==type[1]||tp.getType()==type[0]){//章或节
                section.setSid( ++secseq );
                section.setSectionCourse( courseID );
                section.setSectionName( tp.getContent() );
                section.setSectionSeq( ""+secseq );
                section.setSectionRecommendPath( "not knower" );////////////////////
                md2txtUtils.sectionMapper.insert(section);
                knowseq=0;
                pagseq=0;
                secid=secseq;
                knowid=-1;//上一个知识点id不可使用
            }else if(tp.getType()==type[2]){//知识点
                knowledge.setKnowledgeName( tp.getContent());
                knowledge.setKnowledgeSection(secid);
                knowledge.setKnowledgeSeq( secseq*100+(++knowseq) );
                md2txtUtils.knowledgeMapper.insert(knowledge);
                pagseq=0;
                knowid=md2txtUtils.knowledgeMapper.selectKnowledgeID( knowledge.getKnowledgeSeq() );
            }else{//段落
                if(knowid==-1){//这个段落没有知识点,储存节为知识点
                    if(ji==0){//无效段落
                        ji++;
                        continue;
                    }
                    knowledge.setKnowledgeName( saveP.get(ji-1).getContent());//补成三级标题
                    knowledge.setKnowledgeSection(secid);
                    knowledge.setKnowledgeSeq(  secseq*100+(++knowseq) );
                    md2txtUtils.knowledgeMapper.insert(knowledge);
                    pagseq=0;
                    knowid=md2txtUtils.knowledgeMapper.selectKnowledgeID( knowledge.getKnowledgeSeq());
                }
                paragraph.setParagraphType( ""+tp.getType() );
                paragraph.setParagraphContent( tp.getContent() );
                paragraph.setParagraphKnowledge( knowid );
                paragraph.setParagraphSeq( secseq*100000+knowseq*1000+(++pagseq) );
                if(tp.getType()==type[6]||tp.getType()==type[8]||tp.getType()==type[9]){
                    paragraph.setParagraphNewline("n");
                }else{
                    paragraph.setParagraphNewline( "y" );
                }
                md2txtUtils.paragraphMapper.insert( paragraph );
            }
            ji++;
        }
        return true;
    }
    public static void main(String arg[]) throws IOException {
        new md2txt().toolRun("E:\\Workbench\\IDEA\\Zhiku_workbench\\写作模板.md",102,101);
    }
}
class SortById implements Comparator {
    public int compare(Object o1, Object o2) {
        tempParagraph s1 = (tempParagraph) o1;
        tempParagraph s2 = (tempParagraph) o2;
        if (s1.id > s2.id){
            return 1;
        }else if(s1.id==s2.id){
            return 0;
        }
        return -1;
    }
}
