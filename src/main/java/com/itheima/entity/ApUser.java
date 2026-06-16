package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * APP用户信息表
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
@Getter
@Setter
@TableName("ap_user")
public class ApUser {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 头像
     */
    @TableField("image")
    private String image;

    /**
     * 0 男	            1 女	            2 未知
     */
    @TableField("sex")
    private Byte sex;

    /**
     * 0正常	            1锁定
     */
    @TableField("status")
    private Byte status;
}
