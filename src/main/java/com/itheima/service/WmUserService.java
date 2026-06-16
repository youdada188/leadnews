package com.itheima.service;

import com.itheima.common.ResponseResult;
import com.itheima.dto.WmLoginDto;
import com.itheima.entity.WmUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体用户信息表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
public interface WmUserService extends IService<WmUser> {

    /**
     *自媒体端登录
     */
    ResponseResult login(WmLoginDto wmLoginDto);
}
