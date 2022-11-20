package com.ohj.glkt.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.activity.CouponInfo;
import com.ohj.ggkt.model.order.OrderDetail;
import com.ohj.ggkt.model.order.OrderInfo;
import com.ohj.ggkt.model.user.UserInfo;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.vo.order.OrderFormVo;
import com.ohj.ggkt.vo.order.OrderInfoQueryVo;
import com.ohj.ggkt.vo.order.OrderInfoVo;
import com.ohj.glkt.client.activity.CouponInfoFeignClient;
import com.ohj.glkt.client.course.CourseFeignClient;
import com.ohj.glkt.client.user.UserInfoFeignClient;
import com.ohj.glkt.order.mapper.OrderInfoMapper;
import com.ohj.glkt.order.service.OrderDetailService;
import com.ohj.glkt.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.utils.order.AuthContextHolder;
import com.ohj.glkt.utils.order.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;

    //条件分页查询
    @Override
    public Page<OrderInfo> getPage(Long page, Long limit, OrderInfoQueryVo orderInfoQueryVo) {
        Page<OrderInfo> finallyPage = new Page(page, limit);
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(orderInfoQueryVo.getUserId()!=null,OrderInfo::getUserId,orderInfoQueryVo.getUserId())
                .like(StringUtils.isNotBlank(orderInfoQueryVo.getOutTradeNo()),OrderInfo::getOutTradeNo,orderInfoQueryVo.getOutTradeNo())
                .like(StringUtils.isNotBlank(orderInfoQueryVo.getPhone()),OrderInfo::getPhone,orderInfoQueryVo.getPhone())
                .eq(orderInfoQueryVo.getOrderStatus()!=null,OrderInfo::getOrderStatus,orderInfoQueryVo.getOrderStatus())
                .ge(StringUtils.isNotBlank(orderInfoQueryVo.getCreateTimeBegin()),OrderInfo::getCreateTime,orderInfoQueryVo.getCreateTimeBegin())
                .le(StringUtils.isNotBlank(orderInfoQueryVo.getCreateTimeEnd()),OrderInfo::getCreateTime,orderInfoQueryVo.getCreateTimeEnd());

        orderInfoMapper.selectPage(finallyPage,orderInfoLambdaQueryWrapper);

        //订单里包含详情内容，因此需要封装详情数据，根据订单id查询详情
        finallyPage.getRecords().stream().forEach(orderInfo -> {
            getOrderDetail(orderInfo);
        });

        return finallyPage;
    }

    //生成订单
    @Transactional
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        //1 获取生成订单的条件： 课程id、优惠卷id、用户id
        Long courseId = orderFormVo.getCourseId();
        Long couponId = orderFormVo.getCouponId();
        //TODO 获取不到用户id
        Long userId = AuthContextHolder.getUserId();

        //2 判断当前用户是否已经生成了订单
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderDetailLambdaQueryWrapper.eq(courseId!=null,OrderDetail::getCourseId,courseId)
                .eq(userId!=null,OrderDetail::getUserId,userId);
        OrderDetail orderDetailExists = orderDetailService.getOne(orderDetailLambdaQueryWrapper);
        if(orderDetailExists!=null){
            return orderDetailExists.getId();
        }

        //3 根据课程id查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if(course==null){
            throw new GlktException(20001,"课程不存在");
        }

        //4 根据用户id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if(userInfo==null){
            throw new GlktException(20001,"用户不存在");
        }

        //5 根据优惠卷id查询优惠卷信息(用户可能没有使用优惠卷)
        BigDecimal couponReduce = new BigDecimal(0);  //优惠卷能减免的金额
        if(couponId!=null){
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce=couponInfo.getAmount();
        }


        //数据封装
        //6.1 将数据封装到 OrderInfo，并添加到订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        this.save(orderInfo);

        //6.2 将数据封装到 OrderDetail，并添加到订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);


        //7 更新优惠卷状态
        if(couponId!=null){
            couponInfoFeignClient.updateCouponInfoUseStatus(courseId,couponId);
        }

        //8 返回订单号
        return orderInfo.getId();
    }

    @Override
    public OrderInfoVo getOrderInfoById(Long id) {
        //根据id 查询订单的基本信息和详情信息
        OrderInfo orderInfo = orderInfoMapper.selectById(id);
        OrderDetail orderDetail = orderDetailService.getById(id);

        //数据封装
        OrderInfoVo orderInfoVo = CopyUtil.copyBean(orderInfo, OrderInfoVo.class);
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());
        return orderInfoVo;
    }

    @Override
    public void updateOrderStatus(String out_trade_no) {
        // 根据订单流水号更新
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(StringUtils.isNotBlank(out_trade_no),OrderInfo::getOutTradeNo,out_trade_no)
                .set(OrderInfo::getOrderStatus,1);

        orderInfoMapper.update(null,orderInfoLambdaUpdateWrapper);
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo){
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());
        if(orderDetail!=null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
