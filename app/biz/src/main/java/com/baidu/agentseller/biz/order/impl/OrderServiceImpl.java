package com.baidu.agentseller.biz.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.agentseller.base.util.common.JsonUtil;
import com.baidu.agentseller.dal.dao.*;
import com.baidu.agentseller.dal.entity.RefundInfoWithBLOBs;
import org.apache.commons.lang3.StringUtils;
import org.apache.derby.iapi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.baidu.agentseller.base.util.common.DateUtil;
import com.baidu.agentseller.base.util.common.MoneyUtil;
import com.baidu.agentseller.base.util.common.constants.CommonConstants;
import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.biz.order.OrderService;
import com.baidu.agentseller.biz.order.dto.BusOrderInfoListDto;
import com.baidu.agentseller.biz.repository.OrderInfoRepository;
import com.baidu.agentseller.biz.weixin.TemplateMessageService;
import com.baidu.agentseller.dal.entity.Freight;
import com.baidu.agentseller.dal.entity.Goods;
import com.baidu.agentseller.dal.entity.Order;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.exception.AgentsellerException;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.baidu.agentseller.service.api.model.enums.BusOrderStatus;
import com.baidu.agentseller.service.api.model.enums.CusOrderStatus;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult.ReqInfo;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private WxPayService payService;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private FreightDao freightDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TemplateMessageService templateMessageService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RefundInfoDao refundInfoDao;

    @Override
    public GenericResponse<OrderInfoDto> createOrder(Map<String, String> params) {

        Goods goods = goodsDao.getGoodsInfoById(Integer.parseInt(params.get("goodsId")));

        Order order = assembleOrderInfo(params, goods);

        int result = orderDao.createOrderInfo(order);
        if (result <= 0) {
            logger.warn("OrderServiceImpl#createOrder insert db error,");
            return new GenericResponse<OrderInfoDto>(AgentsellerErrorCode.SYSTEM_ERROR);
        }

        OrderInfoDto dto = new OrderInfoDto();
        dto.setId(order.getId());
        dto.setAmount(order.getAmount());
        dto.setGoodsName(order.getGoodsName());

        return new GenericResponse<OrderInfoDto>(dto);

    }

    @Override
    public void preCheckOrder(String out_trade_no, Integer total_fee, int orderType) {
        if (orderType == 1) {
            // Order order = orderDao.getOrderInfoById(Integer.parseInt(out_trade_no), false);

            OrderInfoDto order = orderInfoRepository.getOrderInfoById(Integer.parseInt(out_trade_no), false);
            if (BusOrderStatus.UNPAID != order.getbState() || CusOrderStatus.UNPAID != order.getcState()) {
                // 订单状态不正确
                logger.warn("OrderServiceImpl#preCheckOrder order status error");
                throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
            }
            if (order.getAmount().intValue() != total_fee.intValue()) {
                // 金额不正确
                logger.warn("OrderServiceImpl#preCheckOrder order amout error");
                throw new AgentsellerException(AgentsellerErrorCode.CHECK_AMOUNT_ERROR);
            }

        } else {
            Freight freight = freightDao.getFreightById(Integer.parseInt(out_trade_no), false);
            if (freight.getState().intValue() != 0) {
                // 订单状态不正确
                logger.warn("OrderServiceImpl#preCheckOrder freight status error");
                throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
            }

            if (freight.getFreigh().intValue() != total_fee.intValue()) {
                // 金额不正确
                logger.warn("OrderServiceImpl#preCheckOrder freight amount error");
                throw new AgentsellerException(AgentsellerErrorCode.CHECK_AMOUNT_ERROR);
            }

        }

    }

    @Override
    public GenericResponse<String> setFreight(final Map<String, String> params) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                logger.info("OrderServiceImpl#setFreight params=", params);

                // List<Order> orders = orderDao.getOrdersByIds(getIds(params), true);

                List<OrderInfoDto> orders = orderInfoRepository.getOrdersByIds(getIds(params), true);

                if (orders == null || orders.size() == 0) {
                    logger.warn("OrderServiceImpl#setFreight orders query empty");
                    throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                }

                int userId = Integer.parseInt(params.get("userId"));
                for (OrderInfoDto order : orders) {
                    // 检查订单是否属于该商户
                    if (order.getUserId().intValue() != userId) {
                        logger.warn("OrderServiceImpl#setFreight order user check error");
                        throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                    }

                    // 检查订单状态是否可以设置邮费
                    if (order.getcState() != CusOrderStatus.CONFIRM || order.getbState() != BusOrderStatus.CONFIRM) {
                        logger.warn("OrderServiceImpl#setFreight order user check error");
                        throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                    }
                    // 检查订单是否已有邮费订单
                    if (order.getFreightId() != null) {
                        logger.warn("OrderServiceImpl#setFreight order freight existsed");
                        throw new AgentsellerException(AgentsellerErrorCode.CHECK_FREIGHT_ERROR);
                    }
                    // 检查所有订单是否是属于同一个客户
                    for (OrderInfoDto order2 : orders) {
                        if (order2.getcUserId().intValue() != order.getcUserId().intValue()) {
                            logger.warn("OrderServiceImpl#setFreight cuserId check error");
                            throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                        }
                    }

                }

                // 开始创建邮费订单

                Freight freight = new Freight();
                freight.setbUserId(userId);
                freight.setcUserId(orders.get(0).getcUserId());
                freight.setFreigh(Long.parseLong(params.get("amount")));
                freight.setState(0);

                int result = freightDao.createFreightOrder(freight);
                if (result < 1) {
                    logger.warn("OrderServiceImpl#setFreight create freight data error,db eror");
                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                }

                // 更新订单邮费
                for (OrderInfoDto order : orders) {
                    Order update = new Order();
                    update.setId(order.getId());
                    update.setFreightId(freight.getId());
                    update.setbStateId(Integer.parseInt(BusOrderStatus.POSTAGE.getCode()));
                    orderDao.updateOrderInfo(update);
                    if (result < 1) {
                        logger.warn("OrderServiceImpl#setFreight update order data error,db eror");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                }

                // TODO 邮费设置成功通知客户支付邮费
                Map<String, String> tempParams = new HashMap<String, String>();
                tempParams.put("", MoneyUtil.toYuanString(MoneyUtil.getMoneyFromCent(freight.getFreigh())));
                templateMessageService.sendPayFreightMessage(params.get(CommonConstants.OPEN_ID), tempParams);
            }

        });

        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<String> orderWechatCallBack(final Map<String, String> params) {
        logger.info("OrderServiceImpl#orderWechatCallBack params=", params);
        // 修改订单的状态为已付款
        final String outTradeNo = params.get("out_trade_no");// 订单号
        final String cashFee = params.get("cash_fee");// 现金支付金额
        final String payTimeStr = params.get("time_end");// 支付时间
        final Date payTime = DateUtil.parse(payTimeStr, DateUtil.LONG_FORMAT);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (StringUtils.startsWith(outTradeNo, CommonConstants.FREIGHT)) {
                    final String freightId = StringUtils.replaceAll(outTradeNo, CommonConstants.FREIGHT, "");
                    // 邮费付款单
                    Freight freight = freightDao.getFreightById(Integer.parseInt(freightId), true);
                    if (freight.getState().intValue() == 0) {

                        if (freight.getFreigh().intValue() != Integer.parseInt(cashFee)) {
                            // 支付金额与订单金额不一致，报警
                            logger.warn("OrderServiceImpl#orderWechatCallBack freight amout error");
                            throw new AgentsellerException(AgentsellerErrorCode.CHECK_AMOUNT_ERROR);
                        }

                        Freight updateFreight = new Freight();
                        updateFreight.setId(freight.getId());
                        updateFreight.setPayTime(payTime);
                        updateFreight.setState(1);

                        int result = freightDao.updateFreightOrder(updateFreight);
                        if (result < 1) {
                            logger.warn("OrderServiceImpl#orderWechatCallBack freight update error");
                            throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                        }
                        // 更新订单状态
                        // List<Order> orders = orderDao.getOrdersByFreightId(freight.getId(), true);
                        List<OrderInfoDto> orders = orderInfoRepository.getOrdersByFreightId(freight.getId(), true);
                        for (OrderInfoDto order : orders) {
                            if (order.getcState() == CusOrderStatus.CONFIRM
                                    && order.getbState() == BusOrderStatus.POSTAGE) {
                                Order update = new Order();
                                update.setId(order.getId());
                                update.setbStateId(Integer.parseInt(BusOrderStatus.POSTAGE_PAY.getCode()));
                                int updateResult = orderDao.updateOrderInfo(update);
                                if (updateResult < 1) {
                                    logger.warn("OrderServiceImpl#orderWechatCallBack order update error");
                                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                                }
                            }
                        }
                        // TODO 发送支付邮费成功消息，客户与商家都要发送?
                        Map<String, String> tempParams = new HashMap<String, String>();
                        tempParams.put("", MoneyUtil.toYuanString(MoneyUtil.getMoneyFromCent(freight.getFreigh())));
                        templateMessageService.sendPayFreightSuccessMessage(params.get(CommonConstants.OPEN_ID),
                                tempParams);

                        return;
                    }
                    logger.warn("OrderServiceImpl#orderWechatCallBack freight order status error,freight status="
                            + freight.getState() + ",freight id=" + freight.getId());

                } else {
                    // 商品付款单
                    // Order order = orderDao.getOrderInfoById(Integer.parseInt(outTradeNo), true);
                    OrderInfoDto order = orderInfoRepository.getOrderInfoById(Integer.parseInt(outTradeNo), true);
                    if (order == null) {
                        logger.warn("OrderServiceImpl#orderWechatCallBack order query empty");
                        throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                    }
                    if (order.getcState() == CusOrderStatus.UNPAID && order.getbState() == BusOrderStatus.UNPAID) {
                        if (order.getAmount().intValue() != Integer.parseInt(cashFee)) {
                            // 支付金额与订单金额不一致，报警
                            logger.warn("OrderServiceImpl#orderWechatCallBack order amout error");
                            throw new AgentsellerException(AgentsellerErrorCode.CHECK_AMOUNT_ERROR);
                        }
                        Order updateObj = new Order();
                        updateObj.setId(order.getId());
                        updateObj.setPayTime(payTime);
                        updateObj.setcStateId(Integer.parseInt(CusOrderStatus.PAID.getCode()));
                        updateObj.setbStateId(Integer.parseInt(BusOrderStatus.PAID.getCode()));
                        int result = orderDao.updateOrderInfo(updateObj);
                        if (result < 1) {
                            logger.warn("OrderServiceImpl#orderWechatCallBack update Order error");
                            throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                        }
                        // TODO 发送支付成功的模版消息
                        Map<String, String> tempParams = new HashMap<String, String>();
                        tempParams.put("", order.getBillAmount());
                        templateMessageService.sendPaySuccessMessage(order.getcUserId().toString(), tempParams);
                        return;
                    }
                    logger.warn("OrderServiceImpl#orderWechatCallBack order status error,bstatus=" + order.getbState()
                            + ",cstatus=" + order.getcState() + ",order id=" + order.getId());
                }

            }
        });

        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<String> refundCallBack(final ReqInfo reqInfo) {
        logger.info("OrderServiceImpl#refundCallBack params=", reqInfo);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                int orderId = Integer.parseInt(reqInfo.getOutTradeNo());
                OrderInfoDto orderInfo = orderInfoRepository.getOrderInfoById(orderId, true);
                RefundInfoWithBLOBs refundInfo = refundInfoDao.getRefundInfoByOrderId(orderId);
                if (orderInfo.getcState() == CusOrderStatus.REFUNDING) {
                    if (StringUtils.equals(reqInfo.getRefundStatus(), CommonConstants.SUCCESS)) {
                        //退款成功
                        OrderInfoDto updateDto = new OrderInfoDto();
                        updateDto.setId(orderInfo.getId());
                        updateDto.setcState(CusOrderStatus.REFUNDED);
                        if (orderInfoRepository.updateOrderInfo(updateDto) < 1) {
                            logger.warn("OrderServiceImpl#refundCallBack update Order error");
                            throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                        }
                    } else {
                        //退款失败
                        logger.warn("OrderServiceImpl#refundCallBack refund fail,orderid=" + orderId);
                    }
                    RefundInfoWithBLOBs updateRefund = new RefundInfoWithBLOBs();
                    updateRefund.setId(refundInfo.getId());
                    updateRefund.setNotifyReq(JsonUtil.transfer2JsonString(reqInfo));
                    if (refundInfoDao.updateRefundInfo(updateRefund) < 1) {
                        logger.warn("OrderServiceImpl#refundCallBack update refundInfo error");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                } else {
                    logger.warn("OrderServiceImpl#refundCallBack status check fail,orderid=" + orderId);
                }

            }
        });

        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<String> orderDeliver(final Map<String, String> params) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                logger.info("OrderServiceImpl#orderDeliver params=", params);
                // List<Order> orders = orderDao.getOrdersByIds(getIds(params), true);
                List<OrderInfoDto> orders = orderInfoRepository.getOrdersByIds(getIds(params), true);

                if (orders == null || orders.size() == 0) {
                    logger.warn("OrderServiceImpl#orderDeliver orders query empty");
                    throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                }
                int userId = Integer.parseInt(params.get("userId"));

                String trackNum = params.get("trackingNum");
                String trackName = params.get("trackingName");
                for (OrderInfoDto order : orders) {

                    // 检查订单是否属于该商户
                    if (order.getUserId().intValue() != userId) {
                        logger.warn("OrderServiceImpl#orderDeliver order user check error");
                        throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                    }

                    // 检查订单状态是否可以设置邮费
                    if (order.getcState() != CusOrderStatus.CONFIRM
                            || order.getbState() != BusOrderStatus.POSTAGE_PAY) {
                        logger.warn("OrderServiceImpl#orderDeliver order user check error");
                        throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                    }
                    // 检查所有订单是否是属于同一个客户
                    for (OrderInfoDto order2 : orders) {
                        if (order2.getcUserId().intValue() != order.getcUserId().intValue()) {
                            logger.warn("OrderServiceImpl#orderDeliver cuserId check error");
                            throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                        }
                    }
                    Order update = new Order();
                    update.setId(order.getId());
                    update.setTrackingName(trackName);
                    update.setTrackingNum(trackNum);
                    update.setDeliveryTime(new Date());
                    update.setcStateId(Integer.parseInt(CusOrderStatus.DELIVERED.getCode()));

                    if (orderDao.updateOrderInfo(update) < 1) {
                        logger.warn("OrderServiceImpl#orderDeliver update Order error");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                    Map<String, String> tempParams = new HashMap<String, String>();
                    tempParams.put("", "");
                    templateMessageService.sendDeliverMessage(params.get(CommonConstants.OPEN_ID), tempParams);
                }
            }
        });
        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<String> confirmReceipt(final Map<String, String> params) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                logger.info("OrderServiceImpl#confirmReceipt params=", params);
                int orderId = Integer.parseInt(params.get(CommonConstants.ORDER_ID));
                OrderInfoDto order = orderInfoRepository.getOrderInfoById(orderId, true);

                if (order == null) {
                    logger.warn("OrderServiceImpl#confirmReceipt order query empty");
                    throw new AgentsellerException(AgentsellerErrorCode.PARAMS_ERROR);
                }
                int userId = Integer.parseInt(params.get("userId"));


                // 检查订单是否属于该商户
                if (order.getcUserId().intValue() != userId) {
                    logger.warn("OrderServiceImpl#confirmReceipt order user check error");
                    throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                }

                // 检查订单状态是否可以收货
                if (order.getcState() != CusOrderStatus.DELIVERED
                        || order.getbState() != BusOrderStatus.POSTAGE_PAY) {
                    logger.warn("OrderServiceImpl#confirmReceipt order user check error");
                    throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                }
                Order update = new Order();
                update.setId(order.getId());
                update.setcStateId(Integer.parseInt(CusOrderStatus.ACCOMPLISH.getCode()));

                if (orderDao.updateOrderInfo(update) < 1) {
                    logger.warn("OrderServiceImpl#confirmReceipt update Order error");
                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                }

                //发送模版消息至用户
                Map<String, String> tempParams = new HashMap<String, String>();
                tempParams.put("", "");
                templateMessageService.sendDeliverMessage(params.get(CommonConstants.OPEN_ID), tempParams);

            }
        });
        return new GenericResponse<String>();
    }


    @Override
    public GenericResponse<String> refundOrderRequest(final Map<String, String> params) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                OrderInfoDto dto = orderInfoRepository
                        .getOrderInfoById(Integer.parseInt(params.get(CommonConstants.ORDER_ID)), true);

                if (dto.getcState() == CusOrderStatus.UNPAID || dto.getcState() == CusOrderStatus.CANCEL
                        || dto.getcState() == CusOrderStatus.REFUND || dto.getcState() == CusOrderStatus.REFUNDING
                        || dto.getcState() == CusOrderStatus.REFUNDED || dto.getcState() == CusOrderStatus.DELETED) {
                    logger.warn("OrderServiceImpl#refundOrderRequest order status check error");
                    throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                }

                if (dto.getcUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))) {
                    logger.warn("OrderServiceImpl#refundOrderRequest order userId check error");
                    throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                }

                OrderInfoDto update = new OrderInfoDto();
                update.setId(dto.getId());
                update.setcState(CusOrderStatus.REFUND);
                if (orderInfoRepository.updateOrderInfo(update) < 1) {
                    logger.warn("OrderServiceImpl#refundOrderRequest update db data fail");
                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                }
                // TODO 发送模版消息至商户及时确认退款请求
                Map<String, String> tempParams = new HashMap<String, String>();
                tempParams.put("", "");

                templateMessageService.sendCusRefundMessage("需要商户的openid", tempParams);
            }
        });
        return new GenericResponse<String>();
    }

    /**
     * 退款确认
     */
    @Override
    public GenericResponse<String> refundOrderConfirm(final Map<String, String> params) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                int orderId = Integer.parseInt(params.get(CommonConstants.ORDER_ID));
                OrderInfoDto dto = orderInfoRepository
                        .getOrderInfoById(orderId, true);

                RefundInfoWithBLOBs refundInfo = refundInfoDao.getRefundInfoByOrderId(orderId);

                if (dto.getcState() != CusOrderStatus.REFUND) {
                    logger.warn("OrderServiceImpl#refundOrderConfirm order status check error");
                    throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                }
                if (dto.getUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))) {
                    logger.warn("OrderServiceImpl#refundOrderConfirm order userId check error");
                    throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                }

                try {
                    WxPayRefundRequest request = WxPayRefundRequest.newBuilder().outTradeNo(dto.getId().toString())
                            .totalFee(dto.getAmount().intValue()).outRefundNo((CommonConstants.REFUND + dto.getId()))
                            .refundFee(dto.getAmount().intValue()).build();
                    WxPayRefundResult refundResp = payService.refund(request);
                    if (StringUtils.equals(refundResp.getResultCode(), CommonConstants.SUCCESS)) {
                        // 提交退款单到微信成功
                        OrderInfoDto update = new OrderInfoDto();
                        update.setId(dto.getId());
                        update.setcState(CusOrderStatus.REFUNDING);
                        if (orderInfoRepository.updateOrderInfo(update) < 1) {
                            logger.warn("OrderServiceImpl#refundOrderConfirm update db data fail");
                            throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                        }
                        // TODO 发送模版消息退款成功
                        Map<String, String> tempParams = new HashMap<String, String>();
                        tempParams.put("", "");
                        templateMessageService.sendRefundSuccessMessage("需要客户的openid", tempParams);
                    } else {
                        // 提交退款申请失败
                        logger.warn("OrderServiceImpl#refundOrderConfirm weChat refund result fail");
                        throw new AgentsellerException(AgentsellerErrorCode.REFUND_ERROR);
                    }
                    //插入微信退款相关返回信息
                    processRefundInfoData(refundInfo, orderId, refundResp);
                } catch (WxPayException e) {
                    logger.warn("OrderServiceImpl#refundOrderConfirm wechat refund error", e);
                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                }

            }

            /**
             * 处理微信退款退款返回相关信息
             * @param refundInfo
             * @param orderId
             * @param refundResp
             */
            private void processRefundInfoData(RefundInfoWithBLOBs refundInfo, int orderId, WxPayRefundResult refundResp) {
                if (refundInfo == null) {
                    refundInfo = new RefundInfoWithBLOBs();
                    refundInfo.setOrderId(orderId);
                    refundInfo.setReqResponse(JsonUtil.transfer2JsonString(refundResp));
                    if (refundInfoDao.createRefundInfo(refundInfo) < 1) {
                        logger.warn("OrderServiceImpl#refundOrderConfirm create refundinfo data fail1");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                } else {
                    RefundInfoWithBLOBs updateRefund = new RefundInfoWithBLOBs();
                    updateRefund.setId(refundInfo.getId());
                    updateRefund.setReqResponse(JsonUtil.transfer2JsonString(refundResp));
                    if (refundInfoDao.updateRefundInfo(updateRefund) < 1) {
                        logger.warn("OrderServiceImpl#refundOrderConfirm update refundinfo data fail2");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                }
            }

        });

        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<String> orderConfirm(final Map<String, String> params, final Integer type) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (type.intValue() == 1) {
                    OrderInfoDto dto = orderInfoRepository
                            .getOrderInfoById(Integer.parseInt(params.get(CommonConstants.ORDER_ID)), true);

                    if (dto.getcState() != CusOrderStatus.PAID || dto.getbState() != BusOrderStatus.PAID) {
                        logger.warn("OrderServiceImpl#busOrderConfirm order status check error");
                        throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                    }
                    if (dto.getUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))) {
                        logger.warn("OrderServiceImpl#busOrderConfirm order userId check error");
                        throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                    }

                    OrderInfoDto update = new OrderInfoDto();
                    update.setId(dto.getId());
                    update.setbState(BusOrderStatus.CONFIRM);
                    if (orderInfoRepository.updateOrderInfo(update) < 1) {
                        logger.warn("OrderServiceImpl#busOrderConfirm update db data fail");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                    // TODO 发送模版消息至买家告知商户已确认订单成功
                    Map<String, String> tempParams = new HashMap<String, String>();
                    tempParams.put("", "");
                    templateMessageService.sendOrderBusConfirmMessage("需要客户的openid", tempParams);
                } else {
                    OrderInfoDto dto = orderInfoRepository
                            .getOrderInfoById(Integer.parseInt(params.get(CommonConstants.ORDER_ID)), true);

                    if (dto.getcState() != CusOrderStatus.PAID || dto.getbState() != BusOrderStatus.CONFIRM) {
                        logger.warn("OrderServiceImpl#cusOrderConfirm order status check error");
                        throw new AgentsellerException(AgentsellerErrorCode.CHECK_STATUS_ERROR);
                    }
                    if (dto.getcUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))) {
                        logger.warn("OrderServiceImpl#cusOrderConfirm order userId check error");
                        throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
                    }

                    OrderInfoDto update = new OrderInfoDto();
                    update.setId(dto.getId());
                    update.setcState(CusOrderStatus.CONFIRM);
                    if (orderInfoRepository.updateOrderInfo(update) < 1) {
                        logger.warn("OrderServiceImpl#cusOrderConfirm update db data fail");
                        throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                    }
                }

            }

        });

        return new GenericResponse<String>();
    }

    @Override
    public GenericResponse<PageList<OrderInfoDto>> queryCusOrderList(Map<String, String> params) {

        CusOrderStatus cStatus = null;
        BusOrderStatus bStatus = null;
        int cusOrderType = Integer.parseInt(params.get(CommonConstants.CUS_ORDER_TYPE));
        switch (cusOrderType) {
            case 0:
                // 全部
                break;
            case 1:
                // 待付款
                cStatus = CusOrderStatus.UNPAID;
                bStatus = BusOrderStatus.UNPAID;
                break;
            case 2:
                // 待发货
                cStatus = CusOrderStatus.CONFIRM;
                bStatus = BusOrderStatus.POSTAGE_PAY;
                break;
            case 3:
                // 待收货
                cStatus = CusOrderStatus.DELIVERED;
                bStatus = BusOrderStatus.POSTAGE_PAY;
                break;
            default:
                logger.warn("OrderServiceImpl#busOrderConfirm update db data fail");
                throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
        }

        PageList<OrderInfoDto> pageList =
                orderInfoRepository.queryOrderWithPage(Integer.parseInt(params.get(CommonConstants.USER_ID)), null,
                        cStatus, bStatus, Integer.parseInt(params.get(CommonConstants.CUR_PAGE)),
                        Integer.parseInt(params.get(CommonConstants.PAGE_SIZE_NAME)));
        return new GenericResponse<PageList<OrderInfoDto>>(pageList);
    }

    @Override
    public GenericResponse<BusOrderInfoListDto> queryBusOrderList(Map<String, String> params) {
        BusOrderInfoListDto result = new BusOrderInfoListDto();
        CusOrderStatus cStatus = null;
        BusOrderStatus bStatus = null;
        int orderType = Integer.parseInt(params.get(CommonConstants.BUS_ORDER_TYPE));
        switch (orderType) {
            case 1:
                // 待处理
                cStatus = CusOrderStatus.PAID;
                bStatus = BusOrderStatus.PAID;
                break;
            case 2:
                // 运费结算
                cStatus = CusOrderStatus.CONFIRM;
                bStatus = BusOrderStatus.CONFIRM;
                break;
            case 3:
                // 待发货
                cStatus = CusOrderStatus.CONFIRM;
                bStatus = BusOrderStatus.POSTAGE_PAY;
                break;
            case 4:
                // 已发货
                cStatus = CusOrderStatus.DELIVERED;
                bStatus = BusOrderStatus.POSTAGE_PAY;
                break;
            case 5:
                // 已完成
                cStatus = CusOrderStatus.ACCOMPLISH;
                bStatus = BusOrderStatus.POSTAGE_PAY;
                break;
            default:
                logger.warn("OrderServiceImpl#busOrderConfirm update db data fail");
                throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
        }

        int userId = Integer.parseInt(params.get(CommonConstants.USER_ID));
        if (orderType != 5) {
            // 已发货数量
            result.setDelivered(orderInfoRepository.getTypeOrderCount(userId, CusOrderStatus.DELIVERED,
                    BusOrderStatus.POSTAGE_PAY));
            // 待发货数量
            result.setOverhang(
                    orderInfoRepository.getTypeOrderCount(userId, CusOrderStatus.CONFIRM, BusOrderStatus.POSTAGE_PAY));
            // 待结算运费数量
            result.setUnPaidPos(
                    orderInfoRepository.getTypeOrderCount(userId, CusOrderStatus.CONFIRM, BusOrderStatus.CONFIRM));
            // 待处理数量
            result.setPending(orderInfoRepository.getTypeOrderCount(userId, CusOrderStatus.PAID, BusOrderStatus.PAID));
        }

        PageList<OrderInfoDto> pageList = orderInfoRepository.queryOrderWithPage(null, userId, cStatus, bStatus,
                Integer.parseInt(params.get(CommonConstants.CUR_PAGE)),
                Integer.parseInt(params.get(CommonConstants.PAGE_SIZE_NAME)));
        result.setOrderList(pageList);
        return new GenericResponse<BusOrderInfoListDto>(result);
    }

    @Override
    public GenericResponse<OrderInfoDto> queryOrderDetail(Map<String, String> params) {

        OrderInfoDto dto = this.orderInfoRepository
                .getOrderInfoById(Integer.parseInt(params.get(CommonConstants.ORDER_ID)), false);
        if (dto.getcUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))
                && dto.getUserId().intValue() != Integer.parseInt(params.get(CommonConstants.USER_ID))) {
            logger.warn("OrderServiceImpl#refundOrderConfirm order userId check error");
            throw new AgentsellerException(AgentsellerErrorCode.PERMISSION_DENIED);
        }

        Goods goodsInfoById = goodsDao.getGoodsInfoById(dto.getGoodId());
        dto.setGoodsImg(goodsInfoById.getImg());

        return new GenericResponse<OrderInfoDto>(dto);
    }

    /**
     * 组装新增订单
     *
     * @param params
     * @param goods
     * @return
     */
    private Order assembleOrderInfo(Map<String, String> params, Goods goods) {
        Order order = new Order();
        order.setGoodId(goods.getId());
        order.setGoodsPrice(goods.getCurrentPrice());
        order.setGoodsCount(Integer.parseInt(params.get(CommonConstants.COUNT)));
        order.setUserId(goods.getUserId());
        order.setcUserId(Integer.parseInt(params.get(CommonConstants.USER_ID)));
        order.setbStateId(Integer.parseInt(BusOrderStatus.UNPAID.getCode()));
        order.setcStateId(Integer.parseInt(CusOrderStatus.UNPAID.getCode()));
        order.setAddress(params.get(CommonConstants.ADDRESS));
        order.setConsignee(params.get(CommonConstants.CONSIGNEE));
        order.setConsigneePhone(params.get(CommonConstants.CONSIGNEE_PHONE));
        order.setAddress(params.get(CommonConstants.ADDRESS));
        order.setAmount((goods.getCurrentPrice() * order.getGoodsCount()));
        order.setGoodsName(goods.getTitle());
        order.setcMessage(params.get(CommonConstants.MESSAGE));
        return order;
    }

    private List<Integer> getIds(final Map<String, String> params) {
        String[] split = StringUtil.split(params.get("orderIds"), ',');
        List<Integer> ids = new ArrayList<Integer>();
        for (String id : split) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

}
