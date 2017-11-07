package com.baidu.agentseller.biz.order;

import java.util.Map;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.biz.order.dto.BusOrderInfoListDto;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult.ReqInfo;

public interface OrderService {

    /**
     * 创建订单接口
     *
     * @param params
     * @return
     */
    GenericResponse<OrderInfoDto> createOrder(Map<String, String> params);

    /**
     * 获得微信预付信息前检查订单状态
     *
     * @param out_trade_no
     * @param total_fee
     * @param orderType
     */
    void preCheckOrder(String out_trade_no, Integer total_fee, int orderType);

    /**
     * 订单支付成功回调接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> orderWechatCallBack(Map<String, String> params);

    /**
     * 退款成功回调接口
     *
     * @return
     */
    GenericResponse<String> refundCallBack(ReqInfo reqInfo);

    /**
     * 确认订单
     *
     * @param params
     * @return
     */
    GenericResponse<String> orderConfirm(Map<String, String> params, Integer type);

    /**
     * 设置邮费接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> setFreight(Map<String, String> params);

    /**
     * 商家发货接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> orderDeliver(Map<String, String> params);

    /**
     * 确认收货接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> confirmReceipt(Map<String, String> params);

    /**
     * 客户请求退款接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> refundOrderRequest(Map<String, String> params);

    /**
     * 商户确认退款接口
     *
     * @param params
     * @return
     */
    GenericResponse<String> refundOrderConfirm(Map<String, String> params);

    /**
     * 分页查询客户订单
     *
     * @param params
     * @return
     */
    GenericResponse<PageList<OrderInfoDto>> queryCusOrderList(Map<String, String> params);

    /**
     * 分页查询商户订单
     *
     * @param params
     * @return
     */
    GenericResponse<BusOrderInfoListDto> queryBusOrderList(Map<String, String> params);

    /**
     * 查询订单明细
     *
     * @param params
     * @return
     */
    GenericResponse<OrderInfoDto> queryOrderDetail(Map<String, String> params);

}
