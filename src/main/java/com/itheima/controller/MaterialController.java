package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.dto.MaterialDto;
import com.itheima.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 自媒体图文素材信息表 前端控制器
 * </p>
 *
 * @author baomidou
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService wmMaterialService;
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }
    @RequestMapping("/list")
    public ResponseResult findList(@RequestBody MaterialDto dto){
        return wmMaterialService.findList(dto);
    }
}

