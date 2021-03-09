package com.wtyicy.client;


import com.aliyun.oss.common.utils.StringUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.wtyicy.api.JingDongYunS3Api;
import com.wtyicy.api.UpYunApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.HuaWeiYunApiException;
import com.wtyicy.exception.JingDongApiException;
import com.wtyicy.exception.UpYunApiException;
import com.wtyicy.util.FileUtil;
import com.wtyicy.util.MultipartFileToFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 京东云操作文件的api：v1
 *
 * @author wtyicy
 *  2018/4/16 16:26
 */
public class UpYunApiClient extends BaseApiClient {

    @Resource
    public  BaseConfig baseConfig;

    private UpYunApi upYunApi;
    private String serviceName;
    private String operatorName;
    private String operatorPwd;

    private String pathPrefix;
    private String path;
    public UpYunApiClient() {
        super("又拍云");
    }

    public UpYunApiClient init(String operatorName, String operatorPwd, String serviceName,String uploadType,String path) {

        this.upYunApi = new UpYunApi(serviceName,operatorName,operatorPwd);
        this.serviceName = serviceName;
        this.operatorName = operatorName;
        this.operatorPwd = operatorPwd;
        this.path = path;
        this.pathPrefix = StringUtils.isNullOrEmpty(uploadType) ? baseConfig.getFilePath() : uploadType.endsWith("/") ? uploadType : uploadType + "/";
        return this;
    }

    /**
     * 上传图片
     *
     * @param is       图片流
     * @param imageUrl 图片路径
     * @return 上传后的路径
     */
    @Override
    public VirtualFile uploadImg(InputStream is, String imageUrl) {
        this.check();

        String key = FileUtil.generateTempFileName(imageUrl);
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        try {
            this.upYunApi.uploadFile(is,this.newFileName);
            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath("putRet.key")
                    .setFileHash("putRet.hash")
                    .setFullFilePath(this.path + this.newFileName);
        } catch (JingDongApiException ex) {
            throw new UpYunApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        }
    }

    @Override
    public VirtualFile uploadFadfsImg(MultipartFile file) {

        String key = FileUtil.generateTempFileName(file.getOriginalFilename());
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        try {

            this.upYunApi.uploadFile(MultipartFileToFile.multipartFileToFile(file),this.newFileName);
            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath("putRet.key")
                    .setFileHash("putRet.hash")
                    .setFullFilePath(this.path + this.newFileName);
        } catch (JingDongApiException ex) {
            throw new UpYunApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new VirtualFile();
    }

    /**
     * 删除七牛空间图片方法
     *
     * @param key 七牛空间中文件名称
     */
    @Override
    public boolean removeFile(String key) {
        this.check();

        if (StringUtils.isNullOrEmpty(key)) {
            throw new UpYunApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }

        try {
            upYunApi.deleteFile( key);
            return true;
        } catch (UpYunApiException e) {
            throw new UpYunApiException("[" + this.storageType + "]删除文件发生异常：" + e.toString());
        }
    }

    @Override
    public void check() {
        if (StringUtils.isNullOrEmpty(this.serviceName) || StringUtils.isNullOrEmpty(this.operatorName) || StringUtils.isNullOrEmpty(this.operatorPwd)) {
            throw new UpYunApiException("[" + this.storageType + "]尚未配置华为云，文件上传功能暂时不可用！");
        }
    }


}
