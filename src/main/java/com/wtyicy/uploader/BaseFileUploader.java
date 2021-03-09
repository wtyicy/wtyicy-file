package com.wtyicy.uploader;


import com.wtyicy.client.AliyunOssApiClient;
import com.wtyicy.client.ApiClient;
import com.wtyicy.client.BaiDuYunApiClient;
import com.wtyicy.client.FastDFSApiClient;
import com.wtyicy.client.HuaWeiYunApiClient;
import com.wtyicy.client.JingDongYunApiClient;
import com.wtyicy.client.LocalApiClient;
import com.wtyicy.client.QCloudCOSApiClient;
import com.wtyicy.client.QiniuApiClient;
import com.wtyicy.client.UpYunApiClient;
import com.wtyicy.config.AliyunOSSConfig;
import com.wtyicy.config.BaiDuYunConfig;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.config.FastDFSConfig;
import com.wtyicy.config.HuaWeiYunConfig;
import com.wtyicy.config.JingDongYunConfig;
import com.wtyicy.config.QCloudCOSConfig;
import com.wtyicy.config.QiNiuYunConfig;
import com.wtyicy.config.UpYunConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.GlobalFileException;
import com.wtyicy.holder.SpringContextHolder;
import org.springframework.util.StringUtils;


/**
 * 基础文件上传
 * @author wtyicy
 *  2020-10-14 09:43
 */
public class  BaseFileUploader {



    ApiClient getApiClient(String uploadType) {
        FastDFSConfig fastDFSConfig = SpringContextHolder.getBean(FastDFSConfig.class);
        BaseConfig baseConfig = SpringContextHolder.getBean(BaseConfig.class);
        AliyunOSSConfig aliyunOSSConfig = SpringContextHolder.getBean(AliyunOSSConfig.class);
        QCloudCOSConfig qCloudCOSConfig = SpringContextHolder.getBean(QCloudCOSConfig.class);
        QiNiuYunConfig qiniuyunConfig = SpringContextHolder.getBean(QiNiuYunConfig.class);
        BaiDuYunConfig baiDuYunConfig = SpringContextHolder.getBean(BaiDuYunConfig.class);
        HuaWeiYunConfig huaWeiYunConfig = SpringContextHolder.getBean(HuaWeiYunConfig.class);
        JingDongYunConfig jingDongYunConfig = SpringContextHolder.getBean(JingDongYunConfig.class);
        UpYunConfig upYunConfig = SpringContextHolder.getBean(UpYunConfig.class);
        String defaultType = baseConfig.getDefaultType();
        String storageType = null;
        if (StringUtils.isEmpty(defaultType) ) {
            storageType = "qiniuyun";
        } else {
            storageType = defaultType;
        }

        ApiClient res = null;
        switch (storageType) {
            case "local":
//                String localFileUrl = (String) config.get(ConfigKeyEnum.LOCAL_FILE_URL.getKey()),
//                        localFilePath = (String) config.get(ConfigKeyEnum.LOCAL_FILE_PATH.getKey());
//                res = new LocalApiClient().init(localFileUrl, localFilePath, uploadType);
                break;
            case "fdfs":
                String fastDFSConfigUrl = fastDFSConfig.getUrl();
                res = new FastDFSApiClient().init( fastDFSConfigUrl, uploadType);
                break;
            case "qiniuyun":
                String accessKey = qiniuyunConfig.getAccessKeyId();
                String secretKey = qiniuyunConfig.getAccessKeySecret();
                String qiniuBucketName = qiniuyunConfig.getBucketName();
                String baseUrl = qiniuyunConfig.getEndpoint();
                res = new QiniuApiClient().init(accessKey, secretKey, qiniuBucketName, baseUrl, uploadType);
                break;
            case "aliyun":
                String endpoint = aliyunOSSConfig.getEndpoint();
                String accessKeyId = aliyunOSSConfig.getAccessKeyId();
                String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
                String url = aliyunOSSConfig.getUrl();
                String aliYunBucketName = aliyunOSSConfig.getBucketName();
                res = new AliyunOssApiClient().init(endpoint, accessKeyId, accessKeySecret, url, aliYunBucketName, uploadType);
                break;
            case "qcloudyun":
                String qcloudendpoint = qCloudCOSConfig.getBucket();
                String qcloudaccessKeyId = qCloudCOSConfig.getAccessKeyId();
                String qcloudaccessKeySecret = qCloudCOSConfig.getAccessKeySecret();
                String qcloudurl = qCloudCOSConfig.getUrl();
                String qcloudaliYunBucketName = qCloudCOSConfig.getBucketName();
                res = new QCloudCOSApiClient().init(qcloudaccessKeyId, qcloudaccessKeySecret,qcloudendpoint, qcloudaliYunBucketName,qcloudurl,  uploadType);
                break;
            case "baiduyun":
                String baiduyunaccessKey = baiDuYunConfig.getAccessKeyId();
                String baiduyunsecretKey = baiDuYunConfig.getAccessKeySecret();
                String baiduyunqiniuBucketName = baiDuYunConfig.getBucketName();
                String baiduyunbaseUrl = baiDuYunConfig.getUrl();
                String baiduyunendpoint = baiDuYunConfig.getEndpoint();
                res = new BaiDuYunApiClient().init(baiduyunaccessKey, baiduyunsecretKey, baiduyunqiniuBucketName, baiduyunbaseUrl,baiduyunendpoint, uploadType);
                break;
            case "huaweiyun":
                String huaweiyunaccessKey = huaWeiYunConfig.getAccessKeyId();
                String huaweiyunsecretKey = huaWeiYunConfig.getAccessKeySecret();
                String huaweiyunBucketName = huaWeiYunConfig.getBucketName();
                String huaweiyunbaseUrl = huaWeiYunConfig.getUrl();
                String huaweiyunEndpoint = huaWeiYunConfig.getEndpoint();
                res = new HuaWeiYunApiClient().init(huaweiyunaccessKey, huaweiyunsecretKey, huaweiyunBucketName, huaweiyunbaseUrl,huaweiyunEndpoint, uploadType);
                break;
            case "jingdongyun":
                String jingdongyunaccessKey = jingDongYunConfig.getAccessKeyId();
                String jingdongyunsecretKey = jingDongYunConfig.getAccessKeySecret();
                String jingdongyunBucketName = jingDongYunConfig.getBucketName();
                String jingdongyunBucket = jingDongYunConfig.getBucket();
                String jingdongyunbaseUrl = jingDongYunConfig.getUrl();
                String jingdongyunEndpoint = jingDongYunConfig.getEndpoint();
                res = new JingDongYunApiClient().init(jingdongyunaccessKey, jingdongyunsecretKey, jingdongyunBucketName,jingdongyunBucket, jingdongyunbaseUrl,jingdongyunEndpoint, uploadType);
                break;
            case "upyun":
                String path = upYunConfig.getPath();
                String serviceName = upYunConfig.getServiceName();
                String operatorName = upYunConfig.getOperatorName();
                String operatorPwd = upYunConfig.getOperatorPwd();
                res = new UpYunApiClient().init(operatorName,operatorPwd,serviceName,uploadType,path);
                break;
            default:
                break;
        }
        if (null == res) {
            throw new GlobalFileException("[文件服务]当前系统暂未配置文件服务相关的内容！");
        }
        return res;
    }

    VirtualFile saveFile(VirtualFile virtualFile, boolean save, String uploadType) {
        if (save) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return virtualFile;
    }

    boolean removeFile(ApiClient apiClient, String filePath, String uploadType) {
        String fileKey = filePath;
        return apiClient.removeFile(fileKey);
    }

}
