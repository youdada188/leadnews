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
import org.springframework.transaction.annotation.Transactional;
import freemarker.template.Template;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class NewsFreeMarkerServiceImpl implements NewsFreeMarkerService {

    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private NewsService newsService;

    /**
     * 生成静态文件上传到minIO中
     */
    @Async
    @Override
    public void buildArticleToMinIO(News news, String content) {
        //已知文章的id
        //1 获取文章内容
        if(StringUtils.isNotBlank(content)){
            //2 文章内容通过freemarker生成HTML文件
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                //数据模型
                Map<String,Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                contentDataModel.put("article",news);

                //合成
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //3 把HTML文件上传到MinIO中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", news.getId() + ".html", in);
            //4 修改ap_article表，保存HTMT文件的地址到static_url字段
            newsService.update(Wrappers.<News>lambdaUpdate().eq(News::getId,news.getId())
                    .set(News::getStaticUrl,path));
        }
    }

}