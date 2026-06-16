package com.itheima.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 分页size
    Integer size;
    // 频道ID
    String tag;
    // 加载类型：1=加载更多(loadmore)，2=刷新(refresh)
    Short type;
}