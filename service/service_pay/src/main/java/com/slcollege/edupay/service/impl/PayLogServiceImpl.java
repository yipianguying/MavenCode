package com.slcollege.edupay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.slcollege.edupay.entity.Order;
import com.slcollege.edupay.entity.PayLog;
import com.slcollege.edupay.mapper.PayLogMapper;
import com.slcollege.edupay.service.OrderService;
import com.slcollege.edupay.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.edupay.utils.HttpClient;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Autowired
    private OrderService orderService;

    // 生成微信支付二维码的方法
    @Override
    public Map nativeInit(String orderNumber) {
        try {
            // 1. 根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNumber);
            Order oneOrder = orderService.getOne(wrapper);

            // 2.使用map设置生成二维码的参数
            Map map = new HashMap<>();
            map.put("appid","wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            // 生成随机字符串
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            // 课程的标题
            map.put("body", oneOrder.getCourseTitle());
            // 订单号
            map.put("out_trade_no", orderNumber);
            // 订单的价格
            map.put("total_fee", oneOrder.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");

            // 3.发送httpClient请求,传递参数xml格式,微信支付提供固定的地址
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置xml格式的参数
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true);
            // 执行post请求发送
            httpClient.post();

            // 4.得到发送请求返回结果
            // 返回的数据,是xml格式
            String xmlContent = httpClient.getContent();
            // 将xml格式的数据转换成map集合，将map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlContent);

            // 封装返回结果集
            Map mapResult = new HashMap<>();
            // 返回订单id
            mapResult.put("out_trade_no", orderNumber);
            // 返回课程id
            mapResult.put("course_id", oneOrder.getCourseId());
            // 返回订单金额
            mapResult.put("total_fee", oneOrder.getTotalFee());
            // 返回操作的状态码
            mapResult.put("result_code", resultMap.get("result_code"));
            // 返回二维码地址
            mapResult.put("code_url", resultMap.get("code_url"));

            return mapResult;

        } catch(Exception e) {
            throw new SlCollegeException(20001,"生成微信支付二维码失败");
        }

    }

    // 根据订单号查询订单状态的方法
    @Override
    public Map<String, String> selectPayStatus(String orderNumber) {


        try {
            // 1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNumber);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2.发送httpClient
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true);
            // 发送post请求
            httpClient.post();

            // 3.得到请求返回的内容
            String xml = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            // 4.转换成Map格式并返回
            return resultMap;

        } catch (Exception e) {
            throw  new SlCollegeException(20001,"查询订单支付状态失败");
        }


    }

    // 添加记录到支付表,更新订单表订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 从map集合中获取订单号
        String orderNumber = map.get("out_trade_no");

        // 根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNumber);
        Order oneOrder = orderService.getOne(wrapper);

        // 更新订单表中订单的状态,如果状态为1,则表示已经支付。
        if(oneOrder.getStatus().intValue() == 1) {
            return;
        }
        // 1代表已经支付
        oneOrder.setStatus(1);
        // 更新订单
        orderService.updateById(oneOrder);

        // 向支付表添加记录
        PayLog payLog = new PayLog();
        // 设置订单号
        payLog.setOrderNo(orderNumber);
        // 设置支付时间
        payLog.setPayTime(new Date());
        // 设置支付类型,1代表微信
        payLog.setPayType(1);
        // 设置总金额(分)
        payLog.setTotalFee(oneOrder.getTotalFee());
        // 设置支付状态
        payLog.setTradeState(map.get("trade_state"));
        // 设置订单的流水号
        payLog.setTransactionId(map.get("transaction_id"));
        // 设置其他属性
        payLog.setAttr(JSONObject.toJSONString(map));

        // 插入到支付日志表
        baseMapper.insert(payLog);
    }
}
