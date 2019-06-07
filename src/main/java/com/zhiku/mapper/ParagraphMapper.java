package com.zhiku.mapper;

import com.zhiku.entity.Paragraph;

import java.util.List;

public interface ParagraphMapper {
    int deleteByPrimaryKey(Integer pid);

    int deleteBySeqCourse(Integer cid);

    int insert(Paragraph record);

    int insertAll(List<Paragraph> record);

    int insertSelective(Paragraph record);

    Paragraph selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(Paragraph record);

    int updateByPrimaryKeyWithBLOBs(Paragraph record);

    int updateByPrimaryKey(Paragraph record);
}