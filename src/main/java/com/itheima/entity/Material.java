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
 * 自媒体图文素材信息表
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@Getter
@Setter
@TableName("material")
public class Material {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 图片地址
     */
    @TableField("url")
    private String url;

    /**
     * 素材类型	            0 图片	            1 视频
     */
    @TableField("type")
    private Byte type;

    /**
     * 是否收藏
     */
    @TableField("is_collection")
    private Byte isCollection;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
