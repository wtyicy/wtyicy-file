package com.wtyicy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * huaweiyun bos 配置
 * @author wtyicy
 *  2020-10-14 08:43
 */
@Configuration
public class HuaWeiYunConfig {

    /**
     * 访问域名
     */
    @Value("${wtyicy.huaweiyun.url}")
    private String url;

    /**
     * 上传域名
     */
    @Value("${wtyicy.huaweiyun.endpoint}")
    private String endpoint;
    /**
     * 访问密钥Id
     */
    @Value("${wtyicy.huaweiyun.accessKeyId}")
    private String accessKeyId;
    /**
     * 访问秘钥密码
     */
    @Value("${wtyicy.huaweiyun.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 存储空间
     */
    @Value("${wtyicy.huaweiyun.bucketName}")
    private String bucketName;

    public HuaWeiYunConfig() {
    }

    public HuaWeiYunConfig(String url, String accessKeyId, String accessKeySecret, String bucketName) {
        this.url = url;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
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
}
