package com.itheima.service.impl;

import com.itheima.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-url}")
    private String accessUrl;

    /** 构建相对路径：prefix/yyyy/MM/dd/filename */
    private String buildRelativePath(String prefix, String filename) {
        String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        if (prefix != null && !prefix.isEmpty()) {
            return prefix + "/" + dateDir + "/" + filename;
        }
        return dateDir + "/" + filename;
    }

    private String saveFile(String prefix, String filename, InputStream inputStream) {
        String relativePath = buildRelativePath(prefix, filename);
        File dest = new File(uploadPath + "/" + relativePath);
        dest.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("保存文件失败: {}", relativePath, e);
            throw new RuntimeException("保存文件失败", e);
        }
        return accessUrl + "/" + relativePath;
    }

    @Override
    public String uploadImgFile(String prefix, String filename, InputStream inputStream) {
        return saveFile(prefix, filename, inputStream);
    }

    @Override
    public String uploadHtmlFile(String prefix, String filename, InputStream inputStream) {
        return saveFile(prefix, filename, inputStream);
    }

    @Override
    public void delete(String pathUrl) {
        String relativePath = pathUrl.replace(accessUrl + "/", "");
        File file = new File(uploadPath + "/" + relativePath);
        if (file.exists() && !file.delete()) {
            log.warn("删除文件失败: {}", pathUrl);
        }
    }

    @Override
    public byte[] downLoadFile(String pathUrl) {
        String relativePath = pathUrl.replace(accessUrl + "/", "");
        File file = new File(uploadPath + "/" + relativePath);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("读取文件失败: {}", pathUrl, e);
            throw new RuntimeException("读取文件失败: " + pathUrl, e);
        }
    }
}
