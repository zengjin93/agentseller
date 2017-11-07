package com.baidu.agentseller.dal.dao;

import java.util.List;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.dal.entity.OrderExample;

public interface OrderDao {

    int createOrderInfo(Order order);

    int updateOrderInfo(Order order);

    Order getOrderInfoById(Integer id, Boolean lock);

    List<Order> getOrdersByIds(List<Integer> ids, boolean lock);

    List<Order> getOrdersByFreightId(Integer freightId, boolean lock);

    PageList<Order> queryWithPageByExample(OrderExample example);

    int queryOrderCount(OrderExample example);

}
