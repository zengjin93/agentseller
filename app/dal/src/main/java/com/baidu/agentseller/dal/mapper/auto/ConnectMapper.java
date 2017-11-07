package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Connect;
import com.baidu.agentseller.dal.entity.ConnectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConnectMapper {
    int countByExample(ConnectExample example);

    int deleteByExample(ConnectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Connect record);

    int insertSelective(Connect record);

    List<Connect> selectByExample(ConnectExample example);

    Connect selectByPrimaryKey(Integer id);

    List<Connect> selectByExampleWithPaging(ConnectExample example);

    int updateByExampleSelective(@Param("record") Connect record, @Param("example") ConnectExample example);

    int updateByExample(@Param("record") Connect record, @Param("example") ConnectExample example);

    int updateByPrimaryKeySelective(Connect record);

    int updateByPrimaryKey(Connect record);

    List<Connect> selectByExampleForUpdate(ConnectExample example);

    Connect selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Connect> record);
}