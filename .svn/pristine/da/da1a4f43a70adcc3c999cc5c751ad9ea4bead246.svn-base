package com.baidu.agentseller.dal.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.dal.dao.OrderDao;
import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.dal.entity.OrderExample;
import com.baidu.agentseller.dal.mapper.auto.OrderMapper;
import com.baidu.agentseller.dal.pagination.MyBatisPaginationUtil;

@Repository("orderDao")
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int createOrderInfo(Order order) {

        return orderMapper.insertSelective(order);
    }

    @Override
    public Order getOrderInfoById(Integer id, Boolean lock) {

        return lock ? orderMapper.selectByPrimaryKeyForUpdate(id) : orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Order> getOrdersByFreightId(Integer freightId, boolean lock) {
        OrderExample example = new OrderExample();
        example.createCriteria().andFreightIdEqualTo(freightId);
        return lock ? orderMapper.selectByExampleForUpdate(example) : orderMapper.selectByExample(example);
    }

    @Override
    public int updateOrderInfo(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public List<Order> getOrdersByIds(List<Integer> ids, boolean lock) {
        OrderExample example = new OrderExample();
        example.createCriteria().andIdIn(ids);
        return lock ? orderMapper.selectByExampleForUpdate(example) : orderMapper.selectByExample(example);
    }

    @Override
    public PageList<Order> queryWithPageByExample(OrderExample example) {
        return MyBatisPaginationUtil.selectByExample(orderMapper, example);
    }

    @Override
    public int queryOrderCount(OrderExample example) {
        return orderMapper.countByExample(example);
    }

}
