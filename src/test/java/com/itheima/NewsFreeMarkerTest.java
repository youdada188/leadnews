package com.itheima;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itheima.entity.News;
import com.itheima.mapper.NewsMapper;
import com.itheima.service.NewsFreeMarkerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
@SpringBootTest(classes = LeadNewsApplication.class)
@ComponentScan(basePackages = "com.heima")
public class NewsFreeMarkerTest {
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private NewsFreeMarkerService newsFreeMarkerService;
    @Test
    public void createStaticUrlTest() {
        //文章Id
        Long newsId=1691284001146327043L;
        News news = newsMapper.selectOne(Wrappers.<News>lambdaQuery().eq(News::getId, newsId));
        newsFreeMarkerService.buildArticleToMinIO(news,news.getContent());
    }
}