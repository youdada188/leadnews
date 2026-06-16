package com.itheima.mapper;

import com.itheima.dto.ArticleHomeDto;
import com.itheima.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章信息表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
public interface NewsMapper extends BaseMapper<News> {

     List<News> loadArticleList(ArticleHomeDto dto,@Param("type") Short type);
}
