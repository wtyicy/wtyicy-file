package com.wtyicy.client;


import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wtyicy.api.QiNiuYunApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.QiniuApiException;
import com.wtyicy.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * Qiniu云操作文件的api：v1
 *
 * @author wtyicy
 */
public class QiniuApiClient extends BaseApiClient {

    @Resource
    public  BaseConfig baseConfig;

    private QiNiuYunApi qiNiuYunApi;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String path;
    private String pathPrefix;

    public QiniuApiClient() {
        super("七牛云");
    }

    public QiniuApiClient init(String accessKey, String secretKey, String bucketName, String baseUrl, String uploadType) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucketName;
        this.path = baseUrl;
        this.pathPrefix = StringUtils.isNullOrEmpty(uploadType) ? baseConfig.getFilePath() : uploadType.endsWith("/") ? uploadType : uploadType + "/";
        this.qiNiuYunApi = new QiNiuYunApi(accessKey,secretKey,bucketName);
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
            Response response = qiNiuYunApi.uploadFile(is, this.newFileName, this.bucket);

            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(putRet.key)
                    .setFileHash(putRet.hash)
                    .setFullFilePath(this.path + putRet.key);
        } catch (QiniuException ex) {
            throw new QiniuApiException("[" + this.storageType + "]文件上传失败：" + ex.error());
        }
    }

    @Override
    public VirtualFile uploadFadfsImg(MultipartFile file) {
        return null;
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
            throw new QiniuApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        try {
            Response response = qiNiuYunApi.deleteFile(this.newFileName, this.bucket);
            return response.isOK();
        } catch (QiniuApiException e) {
            throw new QiniuApiException("[" + this.storageType + "]删除文件发生异常：" + e.getMessage());
        }
    }

    @Override
    public void check() {
        if (StringUtils.isNullOrEmpty(this.accessKey) || StringUtils.isNullOrEmpty(this.secretKey) || StringUtils.isNullOrEmpty(this.bucket)) {
            throw new QiniuApiException("[" + this.storageType + "]尚未配置七牛云，文件上传功能暂时不可用！");
        }
    }

    public String getPath() {
        return this.path;
    }
}
