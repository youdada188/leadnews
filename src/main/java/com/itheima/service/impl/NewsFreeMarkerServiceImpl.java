package com.itheima.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.entity.News;
import com.itheima.service.NewsFreeMarkerService;
import com.itheima.service.FileStorageService;
import com.itheima.service.NewsService;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import freemarker.template.Template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
// [Bug9修复] 移除 @Transactional：@Async 方法在新线程中执行，事务上下文不会传播，注解无效
public class NewsFreeMarkerServiceImpl implements NewsFreeMarkerService {

    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private NewsService newsService;

    @Async
    @Override
    public void buildArticleToMinIO(News news, String content) {
        if (StringUtils.isNotBlank(content)) {
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                Map<String, Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                contentDataModel.put("article", news);
                template.process(contentDataModel, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", news.getId() + ".html", in);
            newsService.update(Wrappers.<News>lambdaUpdate()
                    .eq(News::getId, news.getId())
                    .set(News::getStaticUrl, path));
        }
    }
}