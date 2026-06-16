package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文章信息表
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@Getter
@Setter
@TableName("news")
public class News {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 文章作者的ID
     */
    @TableField("author_id")
    private Integer authorId;

    /**
     * 作者昵称
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 文章所属频道ID
     */
    @TableField("channel_id")
    private Integer channelId;

    /**
     * 频道名称
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * 文章布局	            0 无图文章	            1 单图文章	            2 多图文章
     */
    @TableField("type")
    private Short type;

    /**
     * 文章图片	            多张逗号分隔
     */
    @TableField("images")
    private String images;

    /**
     * 文章标签最多3个 逗号分隔
     */
    @TableField("labels")
    private String labels;

    /**
     * 点赞数量
     */
    @TableField("likes")
    private Integer likes;

    /**
     * 收藏数量
     */
    @TableField("collection")
    private Integer collection;

    /**
     * 评论数量
     */
    @TableField("comment")
    private Integer comment;

    /**
     * 阅读数量
     */
    @TableField("views")
    private Integer views;

    /**
     * 转发量
     */
    @TableField("forward")
    private Integer forward;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private Date publishTime;

    @TableField("static_url")
    private String staticUrl;

    /**
     * 图文内容
     */
    @TableField("content")
    private String content;

    /**
     * 当前状态	            0 草稿	            9已发布
     */
    @TableField("status")
    private Byte status;
}
