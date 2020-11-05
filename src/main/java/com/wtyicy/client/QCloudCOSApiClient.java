package com.wtyicy.client;


import com.aliyun.oss.common.utils.StringUtils;

import com.wtyicy.api.QCloudCOSApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.QCloudCOSApiException;
import com.wtyicy.util.FileUtil;
import com.wtyicy.util.MultipartFileToFile;
import com.wtyicy.util.StreamUtil;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Date;

/**
 * 腾讯云云操作文件的api：v1
 *
 * @author wtyicy
 *  2018/4/16 16:26
 */
public class QCloudCOSApiClient extends BaseApiClient {

    @Resource
    public  BaseConfig baseConfig;

    private QCloudCOSApi qCloudCOSApi;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String bucketName;
    private String path;
    private String pathPrefix;

    public QCloudCOSApiClient() {
        super("腾讯云");
    }

    public QCloudCOSApiClient init(String accessKey, String secretKey, String bucket,String bucketName, String baseUrl, String uploadType) {
        this.qCloudCOSApi = new QCloudCOSApi(accessKey,secretKey,bucket);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.bucketName = bucketName;
        this.path = baseUrl;
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
            qCloudCOSApi.uploadFile(is, this.newFileName, bucketName);

            InputStream fileHashIs = StreamUtil.clone(is);

            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.path + this.newFileName);
        } catch (Exception ex) {
            throw new QCloudCOSApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        }
    }

    @Override
    public VirtualFile uploadFadfsImg(MultipartFile file) {
        this.check();

        String key = FileUtil.generateTempFileName(file.getOriginalFilename());
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();


        try {

            InputStream fileHashIs = StreamUtil.clone(file.getInputStream());

            qCloudCOSApi.uploadFile(MultipartFileToFile.multipartFileToFile(file),this.newFileName,this.bucketName);

            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.path +this.newFileName);
        } catch (Exception ex) {
            throw new QCloudCOSApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        }
    }

    /**
     * 删除腾讯云图片方法
     *
     * @param key 腾讯云中文件名称
     */
    @Override
    public boolean removeFile(String key) {
        this.check();
        
        return true;
    }

    @Override
    public void check() {
        if (StringUtils.isNullOrEmpty(this.accessKey) || StringUtils.isNullOrEmpty(this.secretKey) || StringUtils.isNullOrEmpty(this.bucket)) {
            throw new QCloudCOSApiException("[" + this.storageType + "]尚未配置腾讯云，文件上传功能暂时不可用！");
        }
    }


}
