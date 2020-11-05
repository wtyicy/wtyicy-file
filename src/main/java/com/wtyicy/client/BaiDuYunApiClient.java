package com.wtyicy.client;


import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wtyicy.api.BaiDuYunBOSApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.BaiduYunApiException;
import com.wtyicy.exception.QiniuApiException;
import com.wtyicy.util.FileUtil;
import com.wtyicy.util.MultipartFileToFile;
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
public class BaiDuYunApiClient extends BaseApiClient {

    @Resource
    public  BaseConfig baseConfig;

    private BaiDuYunBOSApi baiDuYunApi;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String path;
    private String pathPrefix;
    private String endpoint;

    public BaiDuYunApiClient() {
        super("百度云");
    }

    public BaiDuYunApiClient init(String accessKey, String secretKey, String bucketName, String baseUrl,String endpoint, String uploadType) {
        this.baiDuYunApi = new BaiDuYunBOSApi(accessKey,secretKey,endpoint);
        this.accessKey = accessKey;
        this.endpoint = endpoint;
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
        //Zone.zone0:华东
        //Zone.zone1:华北
        //Zone.zone2:华南
        //Zone.zoneNa0:北美
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Auth auth = Auth.create(this.accessKey, this.secretKey);
            String upToken = auth.uploadToken(this.bucket);
            Response response = uploadManager.put(is, this.newFileName, upToken, null, null);

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
        this.check();
        try{
            String key = FileUtil.generateTempFileName(file.getOriginalFilename());
            this.createNewFileName(key, "");
            Date startTime = new Date();

            baiDuYunApi.uploadFile(file.getInputStream(), this.newFileName, bucket);

            return new VirtualFile()
                    .setOriginalFileName(key)
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath("putRet.key")
                    .setFileHash("putRet.hash")
                    .setFullFilePath(this.path + bucket+"/"+this.newFileName);

        } catch (BaiduYunApiException | IOException ex) {
            throw new BaiduYunApiException("[" + this.storageType + "]文件上传失败：" + ex.getMessage());
        } catch (Exception e) {
            throw new BaiduYunApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage());
        }
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
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        Configuration config = new Configuration(Region.autoRegion());
        BucketManager bucketManager = new BucketManager(auth, config);
        try {
            Response re = bucketManager.delete(this.bucket, key);
            return re.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            throw new QiniuApiException("[" + this.storageType + "]删除文件发生异常：" + r.toString());
        }
    }

    @Override
    public void check() {
        if (StringUtils.isNullOrEmpty(this.accessKey) || StringUtils.isNullOrEmpty(this.secretKey) || StringUtils.isNullOrEmpty(this.bucket)) {
            throw new BaiduYunApiException("[" + this.storageType + "]尚未配置百度云，文件上传功能暂时不可用！");
        }
    }

    public String getPath() {
        return this.path;
    }


}
