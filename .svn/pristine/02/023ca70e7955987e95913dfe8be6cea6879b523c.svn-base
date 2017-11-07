package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.dal.entity.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectByExampleWithPaging(OrderExample example);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByExampleForUpdate(OrderExample example);

    Order selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Order> record);
}