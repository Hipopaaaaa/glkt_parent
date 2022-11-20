package com.ohj.glkt.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.wechat.Menu;
import com.ohj.ggkt.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
public interface MenuService extends IService<Menu> {

    List<Menu> findMenuOneInfo();

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
