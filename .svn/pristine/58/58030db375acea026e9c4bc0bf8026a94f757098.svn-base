package com.baidu.agentseller.biz.repository;

import java.util.List;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.service.api.model.enums.BusOrderStatus;
import com.baidu.agentseller.service.api.model.enums.CusOrderStatus;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;

public interface OrderInfoRepository {

    int createOrderInfo(OrderInfoDto order);

    int updateOrderInfo(OrderInfoDto order);

    OrderInfoDto getOrderInfoById(Integer id, Boolean lock);

    List<OrderInfoDto> getOrdersByIds(List<Integer> ids, boolean lock);

    List<OrderInfoDto> getOrdersByFreightId(Integer freightId, boolean lock);

    PageList<OrderInfoDto> queryOrderWithPage(Integer cUserId, Integer bUserId, CusOrderStatus cStatus,
            BusOrderStatus bStatus, Integer curPage, Integer pageSize);

    int getTypeOrderCount(Integer bUserId, CusOrderStatus cStatus, BusOrderStatus bStatus);

}
