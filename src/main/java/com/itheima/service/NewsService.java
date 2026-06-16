package com.itheima.service;

import com.itheima.common.ResponseResult;
import com.itheima.dto.ArticleHomeDto;
import com.itheima.dto.NewsDto;
import com.itheima.dto.NewsPageReqDto;
import com.itheima.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章信息表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
public interface NewsService extends IService<News> {


    ResponseResult submitNews(NewsDto dto);

    ResponseResult findList(NewsPageReqDto dto);

    ResponseResult load(ArticleHomeDto dto);
}
