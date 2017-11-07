package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Freight;
import com.baidu.agentseller.dal.entity.FreightExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FreightMapper {
    int countByExample(FreightExample example);

    int deleteByExample(FreightExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Freight record);

    int insertSelective(Freight record);

    List<Freight> selectByExample(FreightExample example);

    Freight selectByPrimaryKey(Integer id);

    List<Freight> selectByExampleWithPaging(FreightExample example);

    int updateByExampleSelective(@Param("record") Freight record, @Param("example") FreightExample example);

    int updateByExample(@Param("record") Freight record, @Param("example") FreightExample example);

    int updateByPrimaryKeySelective(Freight record);

    int updateByPrimaryKey(Freight record);

    List<Freight> selectByExampleForUpdate(FreightExample example);

    Freight selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Freight> record);
}