package com.zhiku.mapper;

import com.zhiku.entity.Knowledge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KnowledgeMapper {
    int deleteByPrimaryKey(Integer kid);

    int deleteBySeqCourse(Integer cid);

    int insert(Knowledge record);

    int insertGetId(Knowledge record);

    int insertAllGetIds(List<Knowledge> record);

    int insertSelective(Knowledge record);

    Knowledge selectByPrimaryKey(Integer kid);

    int updateByPrimaryKeySelective(Knowledge record);

    int updateByPrimaryKey(Knowledge record);

    //
    Integer selectKnowledgeID(@Param( "knowledgeSeq" ) Integer knowledgeSeq);
}