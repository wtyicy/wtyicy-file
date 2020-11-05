package com.wtyicy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * qcloud cos 配置
 * @author wtyicy
 *  2020-10-14 08:43
 */
@Configuration
public class QCloudCOSConfig {

    /**
     * 所属区域
     */
    @Value("${wtyicy.qcloudyun.bucket}")
    private String bucket;
    /**
     * 访问密钥Id
     */
    @Value("${wtyicy.qcloudyun.accessKeyId}")
    private String accessKeyId;
    /**
     * 访问秘钥密码
     */
    @Value("${wtyicy.qcloudyun.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 存储空间
     */
    @Value("${wtyicy.qcloudyun.bucketName}")
    private String bucketName;

    /**
     * 访问 域名
     */
    @Value("${wtyicy.qcloudyun.url}")
    private String url;



    public QCloudCOSConfig() {
    }

    public QCloudCOSConfig(String bucket, String accessKeyId, String accessKeySecret, String bucketName) {
        this.bucket = bucket;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
