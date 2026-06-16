package com.itheima.dto;

import lombok.Data;

@Data
public class WmLoginDto {
    private String name;      // 与前端发送的字段一致
    private String password;
}