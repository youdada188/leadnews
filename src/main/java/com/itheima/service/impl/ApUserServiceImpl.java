package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.common.AppHttpCodeEnum;
import com.itheima.common.ResponseResult;
import com.itheima.dto.LoginDto;
import com.itheima.entity.ApUser;
import com.itheima.mapper.ApUserMapper;
import com.itheima.service.ApUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * APP用户信息表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    public ResponseResult login(LoginDto dto) {
        //1.使用用户名和密码正常登录
        if(StringUtils.isNotBlank(dto.getPhone()) &&
                StringUtils.isNotBlank(dto.getPassword())){
            //1.1 根据手机号和密码查询用户信息
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery()
                    .eq(ApUser::getPhone, dto.getPhone())
                    .eq(ApUser::getPassword, dto.getPassword()));
            if(dbUser == null){
                return ResponseResult.errorResult(
                        AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }
            //1.2 返回数据
            String token = JWTUtil.getToken(dbUser.getId().longValue());
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dbUser.setPassword("");
            map.put("user",dbUser);
            return ResponseResult.okResult(map);
        }else {
            //2.游客登录
            Map<String,Object> map = new HashMap<>();
            map.put("token", JWTUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}
