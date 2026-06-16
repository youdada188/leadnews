package com.itheima.controller;

import com.itheima.common.ResponseResult;
import com.itheima.dto.ArticleHomeDto;
import com.itheima.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/appNews")
public class AppNewsController {
    @Autowired
    private NewsService newsService;
    /**
     * 加载首页
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        return newsService.load(dto);
    }

}