package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.dto.WmLoginDto;
import com.itheima.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/wmUser")
public class WmUserController {
    @Autowired
    private WmUserService wmUserService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody WmLoginDto wmLoginDto){
        return wmUserService.login(wmLoginDto);
    }
}