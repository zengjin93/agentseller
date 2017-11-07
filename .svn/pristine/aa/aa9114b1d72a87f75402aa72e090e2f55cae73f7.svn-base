package com.baidu.agentseller.biz.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.biz.repository.OrderInfoRepository;
import com.baidu.agentseller.biz.repository.convert.OrderInfoDtoConvertor;
import com.baidu.agentseller.dal.dao.OrderDao;
import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.dal.entity.OrderExample;
import com.baidu.agentseller.dal.entity.OrderExample.Criteria;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.exception.AgentsellerException;
import com.baidu.agentseller.service.api.model.enums.BusOrderStatus;
import com.baidu.agentseller.service.api.model.enums.CusOrderStatus;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;

@Service("orderInfoRepository")
public class OrderInfoRepositoryImpl implements OrderInfoRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoRepositoryImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Override
    public int createOrderInfo(OrderInfoDto order) {

        return orderDao.createOrderInfo(OrderInfoDtoConvertor.convertOderInfoDto2Order(order));

    }

    @Override
    public int updateOrderInfo(OrderInfoDto order) {
        return orderDao.updateOrderInfo(OrderInfoDtoConvertor.convertOderInfoDto2Order(order));
    }

    @Override
    public OrderInfoDto getOrderInfoById(Integer id, Boolean lock) {
        return OrderInfoDtoConvertor.convertOrder2OrderInfoDto(orderDao.getOrderInfoById(id, lock));
    }

    @Override
    public List<OrderInfoDto> getOrdersByIds(List<Integer> ids, boolean lock) {
        List<Order> orders = orderDao.getOrdersByIds(ids, lock);
        return OrderInfoDtoConvertor.convertOrders2OrderInfoDtos(orders);
    }

    @Override
    public List<OrderInfoDto> getOrdersByFreightId(Integer freightId, boolean lock) {
        List<Order> orders = orderDao.getOrdersByFreightId(freightId, lock);
        return OrderInfoDtoConvertor.convertOrders2OrderInfoDtos(orders);
    }

    @Override
    public PageList<OrderInfoDto> queryOrderWithPage(Integer cUserId, Integer bUserId, CusOrderStatus cStatus,
            BusOrderStatus bStatus, Integer curPage, Integer pageSize) {
        OrderExample example = new OrderExample();
        example.setPageSize(pageSize);
        example.setStartIndex(curPage < 1 ? 0 : (curPage - 1));
        Criteria createCriteria = example.createCriteria();
        if ((cUserId == null && bUserId == null) || (cUserId != null && bUserId != null)) {
            logger.warn("OrderInfoRepositoryImpl#queryOrderWithPage status param check error");
            throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
        }
        if (cUserId != null) {
            createCriteria.andCUserIdEqualTo(cUserId);
        } else {
            createCriteria.andUserIdEqualTo(bUserId);
        }
        if (cStatus != null && bStatus != null) {
            if (cStatus == CusOrderStatus.UNPAID && bStatus == BusOrderStatus.UNPAID) {
                List<Integer> bstatus = new ArrayList<Integer>();
                bstatus.add(Integer.parseInt(BusOrderStatus.UNPAID.getCode()));
                bstatus.add(Integer.parseInt(BusOrderStatus.POSTAGE.getCode()));
                createCriteria.andCStateIdEqualTo(Integer.parseInt(cStatus.getCode())).andBStateIdIn(bstatus);
            } else {
                createCriteria.andCStateIdEqualTo(Integer.parseInt(cStatus.getCode()))
                        .andBStateIdEqualTo(Integer.parseInt(bStatus.getCode()));
            }
        }

        PageList<Order> page = orderDao.queryWithPageByExample(example);
        List<OrderInfoDto> dtos = OrderInfoDtoConvertor.convertOrders2OrderInfoDtos(page.getList());
        return new PageList<OrderInfoDto>(dtos, page.getPaginator());
    }

    @Override
    public int getTypeOrderCount(Integer bUserId, CusOrderStatus cStatus, BusOrderStatus bStatus) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdEqualTo(bUserId).andCStateIdEqualTo(Integer.parseInt(cStatus.getCode()))
                .andBStateIdEqualTo(Integer.parseInt(bStatus.getCode()));

        return orderDao.queryOrderCount(example);
    }

}
