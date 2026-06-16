package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.ResponseResult;
import com.itheima.dto.MaterialDto;
import com.itheima.entity.Material;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 自媒体图文素材信息表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-05-14
 */
public interface MaterialService extends IService<Material> {
    /**
     *上传素材
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);
    ResponseResult findList(@RequestBody MaterialDto dto);
}
