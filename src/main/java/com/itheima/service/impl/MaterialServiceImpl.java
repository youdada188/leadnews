package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.AppHttpCodeEnum;
import com.itheima.common.PageResponseResult;
import com.itheima.common.ResponseResult;
import com.itheima.dto.MaterialDto;
import com.itheima.entity.Material;
import com.itheima.mapper.MaterialMapper;
import com.itheima.service.FileStorageService;
import com.itheima.service.MaterialService;
import com.itheima.utils.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2.上传图片到MinIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        // [Bug6修复] originalFilename 可能为 null，需要先做 null 检查
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }

        // 3.保存到数据库中
        Material wmMaterial = new Material();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((byte) 0);
        wmMaterial.setType((byte) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(MaterialDto dto) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<Material> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(Material::getIsCollection, dto.getIsCollection());
        }
        lambdaQueryWrapper.eq(Material::getUserId, WmThreadLocalUtil.getUser().getId());
        lambdaQueryWrapper.orderByDesc(Material::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}