package com.wtyicy.api;

import com.obs.services.ObsClient;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.exception.BaiduYunApiException;
import com.wtyicy.exception.HuaWeiYunApiException;
import com.wtyicy.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化华为云OBS
 * @author wtyicy
 * @date 2020-10-14 08:43
 */
public class HuaWeiYunOBSApi {

    private ObsClient client;

    public HuaWeiYunOBSApi(ObsClient client) {
        this.client = client;
    }

    public HuaWeiYunOBSApi(String accessKeyId, String accessKeySecret, String endpoint) {
        this.client = new ObsClient(accessKeyId,accessKeySecret,endpoint);
    }
    
    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     */
    public boolean isExistFile(String fileName, String bucketName) {
        try {
            if (!this.client.headBucket(bucketName)) {
                throw new HuaWeiYunApiException("[华为云OBS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            return this.client.doesObjectExist(bucketName, fileName);
        } finally {
            this.shutdown();
        }
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储空间名
     * @param request    查询条件
     * @return 文件列表
     */
    public List<ObsObject>  listFile(String bucketName, ObjectsRequestEntity request) {
        try {
            if (!this.client.headBucket(bucketName)) {
                throw new HuaWeiYunApiException("[华为云OBS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ListObjectsRequest listRequest = new ListObjectsRequest(bucketName);
            
            ObjectListing result = client.listObjects(listRequest);
            return result.getObjects();
        } finally {
            this.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 保存文件的目标bucket
     * @param fileName   BOS中保存的文件名
     */
    public void deleteFile(String fileName, String bucketName) {
        try {
            if (!this.client.headBucket(bucketName)) {
                throw new HuaWeiYunApiException("[华为云OBS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            this.client.deleteObject(bucketName, fileName);
        } finally {
            this.shutdown();
        }
    }
    
    /**
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucket    需要上传到的目标bucket
     */
    public String uploadFile(File localFile, String fileName, String bucket) {
        try {
            if (!this.client.headBucket(bucket)) {
                throw new HuaWeiYunApiException("[华为云OBS] 无法授权访问文件的URL！Bucket不存在：" + bucket);
            }
            InputStream inputStream = new FileInputStream(localFile);
            return this.uploadFile(inputStream, fileName, bucket);
        } catch (Exception e) {
            throw new BaiduYunApiException("[华为云OBS] 文件上传失败！" + localFile, e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @param bucketName  需要上传到的目标bucket
     */
    public String uploadFile(InputStream inputStream, String fileName, String bucketName) {
        try {
            if (!this.client.headBucket(bucketName)) {
                throw new HuaWeiYunApiException("[华为云OBS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(FileUtil.getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            PutObjectResult putObjectResult = this.client.putObject(bucketName, fileName, inputStream, objectMetadata);
            return putObjectResult.getObjectUrl();
        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
