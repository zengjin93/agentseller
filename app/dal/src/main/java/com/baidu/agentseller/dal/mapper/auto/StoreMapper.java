package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Store;
import com.baidu.agentseller.dal.entity.StoreExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StoreMapper {
    int countByExample(StoreExample example);

    int deleteByExample(StoreExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Store record);

    int insertSelective(Store record);

    List<Store> selectByExample(StoreExample example);

    Store selectByPrimaryKey(Integer id);

    List<Store> selectByExampleWithPaging(StoreExample example);

    int updateByExampleSelective(@Param("record") Store record, @Param("example") StoreExample example);

    int updateByExample(@Param("record") Store record, @Param("example") StoreExample example);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);

    List<Store> selectByExampleForUpdate(StoreExample example);

    Store selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Store> record);
}