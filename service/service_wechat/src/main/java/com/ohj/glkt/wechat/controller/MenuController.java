package com.ohj.glkt.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.ohj.ggkt.model.wechat.Menu;
import com.ohj.ggkt.vo.wechat.MenuVo;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.wechat.service.MenuService;
import com.ohj.glkt.wechat.utils.ConstantPropertiesUtil;
import com.ohj.glkt.wechat.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Api(tags = "公众号菜单")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {




    @Autowired
    private MenuService menuService;

    @ApiOperation("公众号菜单的删除")
    @DeleteMapping("removeMenu")
    public Result removeMenu(){
        menuService.removeMenu();
        return Result.ok();
    }

    //同步菜单
    @ApiOperation("同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu(){
        menuService.syncMenu();
        return Result.ok();
    }


    //获取access_token (实则没用，因为同步菜单的时候已经获取了)
    @ApiOperation("获取access_token")
    @GetMapping("getAccessToken")
    public Result getAccessToken(){
        //拼接请求地址模版
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");

        //设置参数
        String url = String.format(buffer.toString(), ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        try {
            //发送get请求
            String tokenString = HttpClientUtils.get(url);

            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String accessToken = jsonObject.getString("access_token");

            return Result.ok(accessToken);

        } catch (Exception e) {
            throw new GlktException(20001,"获取access_token失败");
        }
    }


    //获取所有菜单，按照一级和二级进行封装
    @ApiOperation("获取所有菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
      List<MenuVo> menuVoList=  menuService.findMenuInfo();
      if(menuVoList.isEmpty()){
          return Result.fail().message("没有任何菜单");
      }else {
          return Result.ok(menuVoList);
      }
    }

    //获取所有的一级菜单
    @ApiOperation("获取一级菜单")
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo(){
      List<Menu> menuList= menuService.findMenuOneInfo();

      if(menuList.isEmpty()){
          return Result.fail().message("没有任何一级菜单");
      }else {
          return Result.ok(menuList);
      }
    }

    @ApiOperation("获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id")Long id){
        Menu menu = menuService.getById(id);
        if(menu!=null){
            return Result.ok(menu);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("新增")
    @PostMapping("save")
    public  Result save(@RequestBody Menu menu){
        boolean isSuccess = menuService.save(menu);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu){
        boolean isSuccess = menuService.updateById(menu);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id")Long id){
        boolean isSuccess = menuService.removeById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean isSuccess = menuService.removeByIds(idList);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}

