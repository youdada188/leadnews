package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.common.AppHttpCodeEnum;
import com.itheima.common.ResponseResult;
import com.itheima.dto.WmLoginDto;
import com.itheima.entity.WmUser;
import com.itheima.mapper.WmUserMapper;
import com.itheima.service.WmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    @Override
    public ResponseResult login(WmLoginDto wmLoginDto) {
        try {
            // 1. 参数校验
            if (StringUtils.isBlank(wmLoginDto.getName()) || StringUtils.isBlank(wmLoginDto.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
            }

            // 2. 查询用户（使用 getOne 并处理可能的多条记录）
            WmUser wmUser = null;
            try {
                wmUser = getOne(Wrappers.<WmUser>lambdaQuery()
                        .eq(WmUser::getName, wmLoginDto.getName())
                        .last("limit 1"));  // 避免多条记录抛异常
            } catch (Exception e) {
                log.error("查询用户异常", e);
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "查询用户失败");
            }

            if (wmUser == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
            }

            // 3. 密码比对（假设数据库密码为明文，如果是加密请自行修改）
            if (!wmLoginDto.getPassword().equals(wmUser.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "密码错误");
            }

            // 4. 生成 token
            Long userId = wmUser.getId().longValue();  // 假设 id 不为 null
            String token = JWTUtil.getToken(userId);

            // 5. 返回结果
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            wmUser.setPassword("");  // 清空密码
            map.put("user", wmUser);
            return ResponseResult.okResult(map);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录发生未捕获异常", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "登录失败，请联系管理员");
        }
    }
}