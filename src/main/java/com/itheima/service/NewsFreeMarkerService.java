package com.itheima.service;


import com.itheima.entity.News;
public interface NewsFreeMarkerService {
    /**
     * 生成静态文件上传到MinIO中
     */
    public void buildArticleToMinIO(News news, String content);
}