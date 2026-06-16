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
 * 频道信息表
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@Getter
@Setter
@TableName("channel")
public class Channel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    @TableField("name")
    private String name;

    /**
     * 频道描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否默认频道
     */
    @TableField("is_default")
    private Byte isDefault;

    @TableField("status")
    private Byte status;

    /**
     * 默认排序
     */
    @TableField("ord")
    private Byte ord;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
