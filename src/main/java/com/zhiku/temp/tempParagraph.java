package com.zhiku.temp;

public class tempParagraph {
    private char type;
    private String content;
    public tempParagraph(char type,String content){
        this.content=content;
        this.type=type;
    }
    public char getType(){
        return type;
    }
    public String getContent(){
        return content;
    }
}
