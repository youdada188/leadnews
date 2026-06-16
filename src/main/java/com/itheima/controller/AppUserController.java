package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.dto.LoginDto;
import com.itheima.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/appUser")
public class AppUserController {
    @Autowired
    private ApUserService apUserService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}