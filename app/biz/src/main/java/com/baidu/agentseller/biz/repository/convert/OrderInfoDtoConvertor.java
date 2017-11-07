package com.baidu.agentseller.biz.repository.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.baidu.agentseller.base.util.common.MoneyUtil;
import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.service.api.model.enums.BusOrderStatus;
import com.baidu.agentseller.service.api.model.enums.CusOrderStatus;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;

public class OrderInfoDtoConvertor {

    public static OrderInfoDto convertOrder2OrderInfoDto(Order order) {
        if (order == null)
            return null;
        OrderInfoDto dto = new OrderInfoDto();

        BeanUtils.copyProperties(order, dto);
        dto.setcState(CusOrderStatus.getByCode(order.getcStateId().toString()));
        dto.setbState(BusOrderStatus.getByCode(order.getbStateId().toString()));
        dto.setBillAmount(MoneyUtil.toYuanString(MoneyUtil.getMoneyFromCent(dto.getAmount())));
        return dto;
    }

    public static List<OrderInfoDto> convertOrders2OrderInfoDtos(List<Order> orders) {
        ArrayList<OrderInfoDto> arrayList = new ArrayList<OrderInfoDto>();
        ;
        if (orders == null || orders.size() == 0) {
            return arrayList;
        }

        for (Order order : orders) {
            arrayList.add(convertOrder2OrderInfoDto(order));
        }
        return arrayList;
    }

    public static Order convertOderInfoDto2Order(OrderInfoDto dto) {
        if (dto == null)
            return null;
        Order order = new Order();
        BeanUtils.copyProperties(dto, order);
        order.setbStateId(Integer.parseInt(dto.getbState().getCode()));
        order.setcStateId(Integer.parseInt(dto.getcState().getCode()));
        return order;
    }

    public static List<Order> convertOderInfoDto2Order(List<OrderInfoDto> dtos) {
        ArrayList<Order> arrayList = new ArrayList<Order>();
        ;
        if (dtos == null || dtos.size() == 0) {
            return arrayList;
        }

        for (OrderInfoDto order : dtos) {
            arrayList.add(convertOderInfoDto2Order(order));
        }
        return arrayList;
    }

}
