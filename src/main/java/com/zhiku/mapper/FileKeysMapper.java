package com.zhiku.mapper;

import com.zhiku.entity.FileKeys;

public interface FileKeysMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(FileKeys record);

    int insertSelective(FileKeys record);

    FileKeys selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(FileKeys record);

    int updateByPrimaryKey(FileKeys record);
}