package com.zhiku.mapper;

import com.zhiku.entity.Knowledge;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeMapper {
    int deleteByPrimaryKey(Integer kid);

    int insert(Knowledge record);

    int insertSelective(Knowledge record);

    Knowledge selectByPrimaryKey(Integer kid);

    int updateByPrimaryKeySelective(Knowledge record);

    int updateByPrimaryKey(Knowledge record);

    //
    Integer selectKnowledgeID(@Param( "knowledgeName" ) String knowledgeName , @Param( "knowledgeSection" ) Integer knowledgeSection);
}