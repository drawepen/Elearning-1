package com.zhiku.controller;

import com.zhiku.mapper.SectionMapper;
import com.zhiku.service.temp.md2Pag;
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
        System.out.println( "输出《《《《《《《《》》》》》》》》》》" );
//        Knowledge course=new Knowledge();
//        course.setKnowledgeSeq(1040201);
////        course.setSid( 10401 );
//        course.setSectionName( "测试md" );
//        course.setSectionRecommendPath( "a/c.txt" );
//        course.setSectionSeq("10401" );
//        course.setSectionCourse( 104 );
//        int konwid=sectionMapper.selectSectionMaxID(104);
        md2Pag.toolRun("E:\\Workbench\\IDEA\\Zhiku_workbench\\写作模板.md",104,104);
//        System.out.println( "查询《《《《"+konwid );
        return "login";
    }
}
