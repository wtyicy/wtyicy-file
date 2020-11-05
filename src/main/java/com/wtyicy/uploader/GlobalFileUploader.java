package com.wtyicy.uploader;

import com.wtyicy.client.ApiClient;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.GlobalFileException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 全局文件上传
 * @author wtyicy
 *  2020-10-14 09:43
 */
public class GlobalFileUploader extends BaseFileUploader implements FileUploader {

    @Override
    public VirtualFile upload(InputStream is, String uploadType, String imageUrl, boolean save) {
        ApiClient apiClient = this.getApiClient(uploadType);
        VirtualFile virtualFile = apiClient.uploadImg(is, imageUrl);
        return this.saveFile(virtualFile, save, uploadType);
    }

    @Override
    public VirtualFile upload(File file, String uploadType, boolean save) {
        ApiClient apiClient = this.getApiClient(uploadType);
        VirtualFile virtualFile = apiClient.uploadImg(file);
        return this.saveFile(virtualFile, save, uploadType);
    }

    @Override
    public VirtualFile upload(MultipartFile file, String uploadType, boolean save) {

        ApiClient apiClient = this.getApiClient(uploadType);
        VirtualFile virtualFile = apiClient.uploadImg(file);
        return this.saveFile(virtualFile, save, uploadType);
    }

    @Override
    public boolean delete(String filePath, String uploadType) {
        if (StringUtils.isEmpty(filePath)) {
            throw new GlobalFileException("[文件服务]文件删除失败，文件为空！");
        }

        ApiClient apiClient = this.getApiClient(uploadType);
        return this.removeFile(apiClient, filePath, uploadType);
    }
}
