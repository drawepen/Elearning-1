package com.zhiku.service.md2Database;

import com.zhiku.entity.Knowledge;
import com.zhiku.entity.Paragraph;
import com.zhiku.entity.Section;
import com.zhiku.mapper.CourseMapper;
import com.zhiku.mapper.KnowledgeMapper;
import com.zhiku.mapper.ParagraphMapper;
import com.zhiku.mapper.SectionMapper;
import com.zhiku.service.md2Database.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class md2Pag {
    /***********i|type|char*******************
     * |0|#|21|
     * |1|##|22|
     * |2|###|23|
     * |3|图片|'I'|
     * |4|表格|'T'|
     * |5|列表|'L'|
     * |6|脚注|60|
     * |7|换行文本|'P'|
     * |8|不换行文本|72|
     * |9|代码块|'C'|
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
    public static char[] type={21,22,23,'I','T','L',60,'P',72,'C'};
    private static md2Pag md2pagUtils;
    @PostConstruct
    public void init() {
        md2pagUtils = this;
    }
    public static String toolRun(String filePath,Integer courseID) throws IOException {
        Integer courseSeq=courseID;
        //读取文件
        FileUtils fu = new FileUtils();
        ArrayList<String> strArr = fu.read2Array(filePath, "UTF-8");
        ArrayList<tempParagraph> saveP=new ArrayList<tempParagraph>( );
        String errorstr;
        //识别表格、列表、标题、图片、代码块
        errorstr=findType( strArr,saveP );
        if(errorstr!=null){
            return errorstr;
        }
        //储存
        errorstr=saveParagraph(saveP,courseID,courseSeq);
        if(errorstr!=null){
            return errorstr;
        }

//        //test
//        for(tempParagraph tp:saveP){
//            if(tp.getContent()==null){
//            }else if(tp.getType()==type[0]){
//                System.out.print( "\n"+tp.id+":<标题1>:"+tp.getContent());
//            }else if(tp.getType()==type[1]){
//                System.out.print( "\n"+tp.id+":<标题2>:"+tp.getContent());
//            }else if(tp.getType()==type[2]){
//                System.out.print( "\n"+tp.id+":<标题3>:"+tp.getContent());
//            }else if(tp.getType()==type[3]){
//                System.out.print( "\n"+tp.id+":<图片>:"+tp.getContent());
//            }else if(tp.getType()==type[4]){
//                System.out.print( "\n"+tp.id+":<表格>:"+tp.getContent());
//            }else if(tp.getType()==type[5]){
//                System.out.print( "\n"+tp.id+":<列表>:"+tp.getContent());
//            }else if(tp.getType()==type[7]){
//                System.out.print( "\n"+tp.id+":<段落>:"+tp.getContent());
//            }else if(tp.getType()==type[6]){//脚注
//                System.out.print( "[<"+tp.getContent()+">]");
//            }else if(tp.getType()==type[8]){//不换行
//                System.out.print(tp.getContent());
//            }else if(tp.getType()==type[9]){
//                System.out.print( "\n"+tp.id+":<代码块>:"+tp.getContent());
//            }
//        }
        return null;//返回null表示没有问题
    }
    //按CSDN编辑器的标准判断，不一定符合所有的morkdown标准
    private static String findType(ArrayList<String> strArr, ArrayList<tempParagraph> saveP){
        int jid=0;//行号
        boolean islist=false,istable=false,iscode=false,
                isendList=false,//普通段前一行为空或由+-*与空格交替存在为列表的最后一行
                isstartTable=false;//表格是否开始，任何表格不连贯都判断为表格结束，所以不用记表格是否到了最后一行
        //标题对应的标志
        String title_1 = "# ",title_2 = "## ",title_3 = "### ",image="![",code="```";

        ArrayList<String> strA2=new ArrayList<String> ();
        int strArrLength=strArr.size();
        for(jid=1;jid<=strArrLength;jid++){
            String str0=strArr.get( jid-1 );
            String strp=str0.trim();
            if(strp.equals( "" )){//按照CSDN编辑器，空行也能终结表格
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;isstartTable=false;
                }
                if(islist){
                    strA2.add(str0);//空行对列表也有影响
                }
                isendList=true;
                continue;
            }
            //判断是否是+-*与空格交替存在
            boolean tempb=true;
            String[] list_str=strp.split( " " );
            for (String tstr:list_str){//不是空行没有纯空格，分割后一定有非空字符串集
                if(!tstr.equals("")&&!tstr.equals("+")&&!tstr.equals("-")&&!tstr.equals("*")){
                    tempb=false;
                }
            }
            if(tempb){//可能只有一个+,所以不能放到列表内部判断，必须提前判断
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;isstartTable=false;
                }
                strA2.add(str0);//不是空行应该保存
                islist=true;
                isendList=true;
            }else if(strp.startsWith(code)){//代码块
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;isstartTable=false;
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                strA2.add(str0);
                if(iscode){
                    saveTabListCode(strA2,saveP,md2Pag.type[9],jid);
                    iscode=false;
                }else{
                    iscode=true;
                }
            }else if(iscode){//如果不是代码块终结符，任何字符串都是代码，其他类型不需要检测
                strA2.add(str0);
            }else if (strp.startsWith(title_3)) {//知识点
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;
                    isstartTable=false;
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                saveP.add(new tempParagraph(jid,md2Pag.type[2],str0));

            }else if(strp.startsWith(title_2)){//节##
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;
                    isstartTable=false;
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                saveP.add(new tempParagraph(jid,md2Pag.type[1],str0));
            }else if(strp.startsWith(title_1)){//节#
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;
                    isstartTable=false;
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                saveP.add(new tempParagraph(jid,md2Pag.type[0],str0));
            }else if((strp.length()>1&&(strp.charAt( 0 )=='+'||strp.charAt( 0 )=='-'||strp.charAt( 0 )=='*')
                    &&strp.charAt( 1 )==' ')||(islist&&!isendList)){//列表,判断顺序不能改，开头不是+-*的也可能是列表,所以先判断列表
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;
                    isstartTable=false;
                }
                strA2.add(str0);
                islist=true;
                isendList=false;
            }else if(strp.startsWith(image)){//图片
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                    istable=false;
                    isstartTable=false;
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                saveP.add(new tempParagraph(jid,md2Pag.type[3],str0));
            }else if(isstartTable&&isHaveT( strp )){//表格
                if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                strA2.add(str0);
                istable=true;
            }else if(jid<strArrLength&&isStartTable(strp,strArr.get( jid ))){//是否即将有表格
                if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                    islist=false;
                }
                strA2.add(str0);
                istable=true;
                isstartTable=true;

            }else{//普通段落
                if(istable){
                    saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
                }else if(islist){
                    saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
                }
                islist=false;
                istable=false;
                isstartTable=false;
                saveP.add(new tempParagraph(jid,md2Pag.type[7],str0));
            }
//            /////////////////////////////
//            System.out.println( "<<<<<<<<<<<<<<<<<<<<<<<<<");
//            System.out.println( str0 );
//            System.out.println( strArr.get( jid ) );
//
//            System.out.println( "istable:"+istable );
//            System.out.println( "islist:"+islist );
//            System.out.println( "isstartTable:"+isstartTable );
//            System.out.println( "isendList:"+isendList );
//            System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
//
//            ///////////////////////////////////////
        }
        if(islist){
            saveTabListCode(strA2,saveP,md2Pag.type[5],jid);
            islist=false;
        }else if(istable){
            saveTabListCode(strA2,saveP,md2Pag.type[4],jid);
            istable=false;
            isstartTable=false;
        }
        return null;
    }

    //是否开始表格
    private static boolean isStartTable(String str1,String str2){
        int t1=0;//第一个字符串'|'的个数
        int l=str1.length();
        for(int i=0;i<l;i++){
            if(str1.charAt( i )=='|')
                t1++;
        }
//        //按照CSDN编辑器，第一行'|'个数不能大于下一行"--"加一,但分割字符串开头为|自动多一个空值
//        if(t1>strs2.length){
//            return false;
//        }
        if(t1==0){//CSDN在表格判断中\|多了就转义，少了不转义，太复杂，这里只判断有一个‘|’就为表格
            return false;
        }

        if(str2.trim().equals( "" ))
            return false;
        String[] strs2=str2.trim().split( "-" );
        if(str2.length()==0)//只有全--才长度为0
            return true;
        for (String str : strs2) {
            if (str.equals( "" ))
                continue;
            int l1 = str.length();
            int j = 0;//记'|'个数
            for (int i = 0; i < l1; i++) {
                if (str.charAt( i ) == '|') {
                    j++;
                } else if (str.charAt( i ) != ' ') {//含非"|"或空格也不是表格
                    return false;
                }
            }
            if (j != 1) {//如果'|'个数小于1也不是表格
                return false;
            }
        }
        return true;
    }

    //字符串中是否包含'|'，根据CSDN编辑器，只要有'|'，不管是不是'\|'转义的，都能继续表格，使用不判断是否转义
    private static boolean isHaveT(String str){
        int l=str.length();
        for(int i=0;i<l;i++){
            if(str.charAt( i )=='|')
                return true;
        }
        return false;
    }

    private static void saveTabListCode(ArrayList<String> strA2, ArrayList<tempParagraph> saveP, char typec, int jid){
        int l=strA2.size();
        if(l<1)
            return;
        String str=strA2.get(0);
        for(int i=1;i<l;i++){
            str+="\n"+strA2.get(i);
        }
        saveP.add(new tempParagraph(jid,typec,str));
        strA2.clear();
    }
    public static String saveParagraph(ArrayList<tempParagraph> saveP,Integer courseID,Integer courseSeq){
        Section section=new Section();
        Knowledge knowledge=new Knowledge();
        Paragraph paragraph=new Paragraph();
        int ji=0;
        //序号
        int secseq=md2pagUtils.sectionMapper.selectSectionMaxID(courseID);
        System.out.println("课程知识点序号"+secseq);////////////////////////////////////
        if(secseq==0){
            secseq=courseSeq*100;
        }else{//查重
            for(tempParagraph tp:saveP){
                if((tp.getType()==type[1]||tp.getType()==type[0])&&
                        md2pagUtils.sectionMapper.selectSectionID(tp.getContent(),courseID)!=0){
                    return "error:文件内容与数据库中章节(名)有重复！请检测文件或先清空原课程数据。";
                }
            }
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
                section.setSectionRecommendPath( secseq+".txt" );////////////////////
                md2pagUtils.sectionMapper.insert(section);
                knowseq=0;
                pagseq=0;
                secid=secseq;
                knowid=-1;//上一个知识点id不可使用
            }else if(tp.getType()==type[2]){//知识点
                knowledge.setKid( null );
                knowledge.setKnowledgeName( tp.getContent());
                knowledge.setKnowledgeSection(secid);
                knowledge.setKnowledgeSeq( secseq*100+(++knowseq) );
                md2pagUtils.knowledgeMapper.insert_getid(knowledge);
                pagseq=0;
//                knowid=md2pagUtils.knowledgeMapper.selectKnowledgeID( knowledge.getKnowledgeSeq() );
                knowid=knowledge.getKid();
            }else{//段落
                if(knowid==-1){//这个段落没有知识点,储存节为知识点
                    if(ji==0){//无效段落
                        ji++;
                        continue;
                    }
//                    if(saveP.get(ji-1).getType()==type[1]){
//                        knowledge.setKnowledgeName( "#"+saveP.get(ji-1).getContent());//补成三级标题
//                    }else{
//                        knowledge.setKnowledgeName( "##"+saveP.get(ji-1).getContent());
//                    }
                    knowledge.setKid( null );
                    knowledge.setKnowledgeName( "");
                    knowledge.setKnowledgeSection(secid);
                    knowledge.setKnowledgeSeq(  secseq*100+(++knowseq) );
                    md2pagUtils.knowledgeMapper.insert_getid(knowledge);
                    pagseq=0;
//                    knowid=md2pagUtils.knowledgeMapper.selectKnowledgeID( knowledge.getKnowledgeSeq());
                    knowid=knowledge.getKid();
                }
                paragraph.setParagraphType( ""+tp.getType() );
                paragraph.setParagraphContent( tp.getContent() );
                paragraph.setParagraphKnowledge( knowid );
                paragraph.setParagraphSeq( secseq*100000+knowseq*1000+(++pagseq) );
//                if(tp.getType()==type[6]||tp.getType()==type[8]||tp.getType()==type[9]){
//                    paragraph.setParagraphNewline("n");
//                }else{
                    paragraph.setParagraphNewline( "y" );//////////////
//                }
                md2pagUtils.paragraphMapper.insert( paragraph );
            }
            ji++;
        }
        return null;
    }
//
//    public static void main(String arg[]) throws IOException {
//        md2Pag.toolRun("E:\\Workbench\\IDEA\\Zhiku_workbench\\写作模板.md",102);
//    }
}
