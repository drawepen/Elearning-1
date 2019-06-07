package com.zhiku.controller;

import com.zhiku.mapper.SectionMapper;
import com.zhiku.service.md2Database.Md2Save;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class test_md {
    @Autowired
    private SectionMapper sectionMapper;
    @RequestMapping(value = "testmd")
    public String testmd() throws IOException {
//        System.out.println( "输出《《《《《《《《》》》》》》》》》》" );
//        Knowledge course=new Knowledge();
//        course.setKnowledgeSeq(1040201);
////        course.setSid( 10401 );
//        course.setSectionName( "测试md" );
//        course.setSectionRecommendPath( "a/c.txt" );
//        course.setSectionSeq("10401" );
//        course.setSectionCourse( 104 );
//        List<Integer> konwid=sectionMapper.selectSectionMaxID(104);//如果sql返回结果有多个，接口不用LIst会报错
        String errorstr= Md2Save.toolRun("E:\\Workbench\\IDEA\\Zhiku_workbench\\操作系统第六章.md",108);
        System.out.println( "返回《《《《"+errorstr );
        return "login";
    }
}
