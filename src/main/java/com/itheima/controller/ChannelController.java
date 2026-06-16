package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.entity.Channel;
import com.itheima.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * <p>
 * 频道信息表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;
    @GetMapping("/channels")
    public ResponseResult findAll(){
        List<Channel> list = channelService.list();
        return ResponseResult.okResult(list);
    }
}
