package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Attribute;
import com.baidu.agentseller.dal.entity.AttributeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AttributeMapper {
    int countByExample(AttributeExample example);

    int deleteByExample(AttributeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Attribute record);

    int insertSelective(Attribute record);

    List<Attribute> selectByExample(AttributeExample example);

    Attribute selectByPrimaryKey(Integer id);

    List<Attribute> selectByExampleWithPaging(AttributeExample example);

    int updateByExampleSelective(@Param("record") Attribute record, @Param("example") AttributeExample example);

    int updateByExample(@Param("record") Attribute record, @Param("example") AttributeExample example);

    int updateByPrimaryKeySelective(Attribute record);

    int updateByPrimaryKey(Attribute record);

    List<Attribute> selectByExampleForUpdate(AttributeExample example);

    Attribute selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Attribute> record);
}