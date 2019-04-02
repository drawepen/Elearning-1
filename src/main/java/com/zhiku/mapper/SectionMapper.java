package com.zhiku.mapper;

import com.zhiku.entity.Section;
import com.zhiku.view.SectionView;
import org.apache.ibatis.annotations.Param;

public interface SectionMapper {
    int deleteByPrimaryKey(Integer sid);

    int insert(Section record);

    int insertSelective(Section record);

    Section selectByPrimaryKey(Integer sid);

    int updateByPrimaryKeySelective(Section record);

    int updateByPrimaryKey(Section record);

    //自定义方法
    SectionView getSectionViewBySid(Integer sid);

    Integer selectSectionID(@Param( "sectionName" ) String sectionName, @Param( "sectionCourse" ) Integer sectionCourse);
}