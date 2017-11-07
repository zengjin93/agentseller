package com.baidu.agentseller.web.order;

import com.baidu.agentseller.base.util.common.pagination.PageList;
import com.baidu.agentseller.biz.order.OrderService;
import com.baidu.agentseller.biz.order.dto.BusOrderInfoListDto;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.exception.AgentsellerException;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.baidu.agentseller.service.api.model.order.OrderInfoDto;
import com.baidu.agentseller.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单业务相关接口
 *
 * @author v_zengjin
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单接口
     *
     * @param request
     * @param goodsId
     * @param address
     * @param c_message
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<OrderInfoDto> createOrder(HttpServletRequest request,
                                                     @RequestParam(value = "goodsId", required = true) final Integer goodsId,
                                                     @RequestParam(value = "count", required = true) final Integer count,
                                                     @RequestParam(value = "consignee", required = true) final String consignee,
                                                     @RequestParam(value = "consigneePhone", required = true) final String consigneePhone,
                                                     @RequestParam(value = "address", required = true) final String address,
                                                     @RequestParam(value = "c_message", required = false) final String c_message) throws Exception {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.createOrder(params);

        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<OrderInfoDto>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<OrderInfoDto>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 订单确认接口
     *
     * @param request
     * @param orderId
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<String> confirmOrder(HttpServletRequest request,
                                                @RequestParam(value = "orderId", required = true) final String orderId,
                                                @RequestParam(value = "type", required = true) final Integer type) {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.orderConfirm(params, type);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 设置邮费接口
     *
     * @param request
     * @param orderIds
     * @param amount
     * @return
     */
    @RequestMapping(value = "/setFreight", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> setFreight(HttpServletRequest request,
                                              @RequestParam(value = "orderIds", required = true) final String orderIds,
                                              @RequestParam(value = "amount", required = true) final Integer amount) {

        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.setFreight(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (IllegalArgumentException e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 商家发货接口
     *
     * @param request
     * @param orderIds
     * @return
     */
    @RequestMapping(value = "/orderDeliver", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> orderDeliver(HttpServletRequest request,
                                                @RequestParam(value = "orderIds", required = true) final String orderIds,
                                                @RequestParam(value = "trackingNum", required = true) final String trackingNum,
                                                @RequestParam(value = "trackingName", required = true) final String trackingName) {

        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.orderDeliver(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (IllegalArgumentException e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 确认收货接口
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/confirmReceipt", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> confirmReceipt(HttpServletRequest request,
                                                  @RequestParam(value = "orderId", required = true) final String orderId) {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.confirmReceipt(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (IllegalArgumentException e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 请求退款接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/refundOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> refundOrder(HttpServletRequest request,
                                               @RequestParam(value = "orderId", required = true) final String orderId) {

        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.refundOrderRequest(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (IllegalArgumentException e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 退款确认接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/refundConfirm", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> refundConfirm(HttpServletRequest request,
                                                 @RequestParam(value = "orderId", required = true) final String orderId) {

        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.refundOrderConfirm(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (IllegalArgumentException e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 分页分类查询客户订单列表
     *
     * @param request
     * @param cusOrderType
     * @param cusPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryCusOrderList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<PageList<OrderInfoDto>> queryCusOrderList(HttpServletRequest request,
                                                                     @RequestParam(value = "cusOrderType", required = true) final Integer cusOrderType,
                                                                     @RequestParam(value = "cusPage", required = false, defaultValue = "1") final Integer cusPage,
                                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "5") final Integer pageSize) {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.queryCusOrderList(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<PageList<OrderInfoDto>>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<PageList<OrderInfoDto>>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 分页分类查询商户订单
     *
     * @param request
     * @param busOrderType
     * @param cusPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryBusOrderList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<BusOrderInfoListDto> queryBusOrderList(HttpServletRequest request,
                                                                  @RequestParam(value = "busOrderType", required = true) final Integer busOrderType,
                                                                  @RequestParam(value = "cusPage", required = false, defaultValue = "1") final Integer cusPage,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "5") final Integer pageSize) {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.queryBusOrderList(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<BusOrderInfoListDto>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<BusOrderInfoListDto>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 查询订单详情
     *
     * @param request
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryOrderDetails", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<OrderInfoDto> queryOrderDetail(HttpServletRequest request,
                                                          @RequestParam(value = "orderId", required = true) final String orderId) {
        try {
            Map<String, String> params = getRequestInfoAndUserInfo(request);
            return orderService.queryOrderDetail(params);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<OrderInfoDto>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<OrderInfoDto>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }


    @RequestMapping(value = "/setTempOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<String> setTempOrder(HttpServletRequest request,
                                                @RequestParam(value = "goodsId", required = true) final Integer goodsId,
                                                @RequestParam(value = "count", required = true) final Integer count,
                                                @RequestParam(value = "type", required = true) final String consignee) {
        try {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("goodsId", goodsId);
            tempMap.put("count", count);
            tempMap.put("type", consignee);
            request.getSession().setAttribute("tempOrder", tempMap);
            return new GenericResponse<String>();
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<String>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/getTempOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<Map<String, Object>> setTempOrder(HttpServletRequest request) {
        try {
            Map<String, Object> obj = (Map<String, Object>) request.getSession().getAttribute("tempOrder");
            return new GenericResponse<Map<String, Object>>(obj);
        } catch (AgentsellerException e) {
            logger.error("UserInfoController#setUserInfo AgentsellerException", e);
            return new GenericResponse<Map<String, Object>>(e.getError());
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<Map<String, Object>>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

}
