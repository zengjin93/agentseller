package com.baidu.agentseller.biz.order.dto;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.service.api.model.Entity;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;

public class BusOrderInfoListDto extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -8342377447693853608L;

    private PageList<OrderInfoDto> orderList;

    /**
     * 待处理数量
     */
    private int pending;

    /**
     * 待结算运费数量
     */
    private int unPaidPos;

    /**
     * 待发货数量
     */
    private int overhang;

    /**
     * 已发货数量
     */
    private int delivered;

    public PageList<OrderInfoDto> getOrderList() {
        return orderList;
    }

    public void setOrderList(PageList<OrderInfoDto> orderList) {
        this.orderList = orderList;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getUnPaidPos() {
        return unPaidPos;
    }

    public void setUnPaidPos(int unPaidPos) {
        this.unPaidPos = unPaidPos;
    }

    public int getOverhang() {
        return overhang;
    }

    public void setOverhang(int overhang) {
        this.overhang = overhang;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

}
