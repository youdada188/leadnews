package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.dto.NewsDto;
import com.itheima.dto.NewsPageReqDto;
import com.itheima.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章信息表 前端控制器
 * </p>
 *
 * @author baomidou
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody NewsDto dto){
        return newsService.submitNews(dto);
    }
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody NewsPageReqDto dto){
        return newsService.findList(dto);
    }

}
