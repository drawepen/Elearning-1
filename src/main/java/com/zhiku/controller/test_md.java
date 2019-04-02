package com.zhiku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class test_md {
    @RequestMapping(value = "testmd")
    public String testmd() throws IOException {
//        System.out.println( "输出《《《《《《《《《》》》》》》》》》》》》" );
//        new md2txt().toolRun("E:\\Workbench\\IDEA\\Zhiku_workbench\\写作模板.md",123);
        return "login";
    }
}
