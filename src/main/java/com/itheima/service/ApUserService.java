package com.itheima.service;

import com.itheima.common.ResponseResult;
import com.itheima.dto.LoginDto;
import com.itheima.entity.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * APP用户信息表 服务类
 * </p>
 *
 */
public interface ApUserService extends IService<ApUser> {
    /**
     *用户登录
     */
    ResponseResult login(LoginDto dto);
}
