package com.wtyicy.client;


import com.aliyun.oss.common.utils.StringUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.wtyicy.api.HuaWeiYunOBSApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.HuaWeiYunApiException;
import com.wtyicy.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 百度云操作文件的api：v1
 *
 * @author wtyicy
 *  2018/4/16 16:26
 */
public class HuaWeiYunApiClient extends BaseApiClient {

    @Resource
    public  BaseConfig baseConfig;

    private HuaWeiYunOBSApi huaWeiYunOBSApi;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String path;
    private String pathPrefix;

    public HuaWeiYunApiClient() {
        super("华为云");
    }

    public HuaWeiYunApiClient init(String accessKey, String secretKey, String bucketName, String baseUrl, String endpoint, String uploadType) {
        this.huaWeiYunOBSApi = new HuaWeiYunOBSApi(accessKey,secretKey,endpoint);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucketName;
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
            huaWeiYunOBSApi.uploadFile(is, this.newFileName, this.bucket);


            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath("putRet.key")
                    .setFileHash("putRet.hash")
                    .setFullFilePath(this.path + this.newFileName);
        } catch (HuaWeiYunApiException ex) {
            throw new HuaWeiYunApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        }
    }

    @Override
    public VirtualFile uploadFadfsImg(MultipartFile file) {
        this.check();
        String key = FileUtil.generateTempFileName(file.getOriginalFilename());
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        try {
            huaWeiYunOBSApi.uploadFile(file.getInputStream(), this.newFileName, this.bucket);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new VirtualFile()
                .setOriginalFileName(key)
                .setSuffix(this.suffix)
                .setUploadStartTime(startTime)
                .setUploadEndTime(new Date())
                .setFilePath("putRet.key")
                .setFileHash("putRet.hash")
                .setFullFilePath(this.path +this.newFileName);

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
            throw new HuaWeiYunApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        Configuration config = new Configuration(Region.autoRegion());
        BucketManager bucketManager = new BucketManager(auth, config);
        try {
            Response re = bucketManager.delete(this.bucket, key);
            return re.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            throw new HuaWeiYunApiException("[" + this.storageType + "]删除文件发生异常：" + r.toString());
        }
    }

    @Override
    public void check() {
        if (StringUtils.isNullOrEmpty(this.accessKey) || StringUtils.isNullOrEmpty(this.secretKey) || StringUtils.isNullOrEmpty(this.bucket)) {
            throw new HuaWeiYunApiException("[" + this.storageType + "]尚未配置华为云，文件上传功能暂时不可用！");
        }
    }


}
