package com.itheima.dto;


import lombok.Data;

@Data
public class MaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
