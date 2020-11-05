package com.wtyicy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * jingdongyun bos 配置
 * @author wtyicy
 *  2020-10-14 08:43
 */
@Configuration
public class JingDongYunConfig {

    /**
     * 访问域名
     */
    @Value("${wtyicy.jingdongyun.url}")
    private String url;

    /**
     * 上传域名
     */
    @Value("${wtyicy.jingdongyun.endpoint}")
    private String endpoint;
    /**
     * 访问密钥Id
     */
    @Value("${wtyicy.jingdongyun.accessKeyId}")
    private String accessKeyId;
    /**
     * 访问秘钥密码
     */
    @Value("${wtyicy.jingdongyun.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 存储空间
     */
    @Value("${wtyicy.jingdongyun.bucketName}")
    private String bucketName;

    /**
     * 存储空间
     */
    @Value("${wtyicy.jingdongyun.bucket}")
    private String bucket;

    public JingDongYunConfig() {
    }

    public JingDongYunConfig(String url, String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String bucket) {
        this.url = url;
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.bucket = bucket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
