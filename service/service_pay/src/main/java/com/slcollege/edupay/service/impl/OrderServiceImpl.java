package com.slcollege.edupay.service.impl;

import com.slcollege.commonutils.ordervo.CourseWebVoOrder;
import com.slcollege.commonutils.ordervo.UcenterMemberOrder;
import com.slcollege.edupay.client.CourseClient;
import com.slcollege.edupay.client.UcenterClient;
import com.slcollege.edupay.entity.Order;
import com.slcollege.edupay.mapper.OrderMapper;
import com.slcollege.edupay.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.edupay.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 生成订单的方法
    @Override
    public String saveOrders(String courseId, String userId) {
        // 通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userOrderInfo = ucenterClient.getUserOrderInfo(userId);
        // 通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseOrderInfo = courseClient.getCourseOrderInfo(courseId);

        // 创建Order对象,在order对象中设置数据
        Order order = new Order();
        // 设置订单号
        order.setOrderNo(OrderNoUtil.getOrderNo());
        // 设置课程id
        order.setCourseId(courseId);
        // 设置课程标题
        order.setCourseTitle(courseOrderInfo.getTitle());
        // 设置课程封面
        order.setCourseCover(courseOrderInfo.getCover());
        // 设置教师姓名
        order.setTeacherName(courseOrderInfo.getTeacherName());
        // 设置课程价格
        order.setTotalFee(courseOrderInfo.getPrice());
        // 设置用户id
        order.setMemberId(userId);
        // 设置用户联系方式
        order.setMobile(userOrderInfo.getMobile());
        // 设置用户昵称
        order.setNickname(userOrderInfo.getNickname());
        // 设置支付状态
        order.setStatus(0);
        // 设置类型, 微信1
        order.setPayType(1);

        baseMapper.insert(order);

        // 返回订单号
        return order.getOrderNo();
    }
}
