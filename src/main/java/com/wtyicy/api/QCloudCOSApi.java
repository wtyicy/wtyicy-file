package com.wtyicy.api;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.exception.QCloudCOSApiException;
import com.wtyicy.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化腾讯云COS
 * @author wtyicy
 *  2020-10-14 08:43
 */
public class QCloudCOSApi {

    private COSClient client;

    public QCloudCOSApi(COSClient client) {
        this.client = client;
    }

    public QCloudCOSApi(String accessKeyId, String accessKeySecret,String bucket) {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKeyId, accessKeySecret);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));

        this.client = new COSClient(cred,clientConfig);
    }
    
    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     * @return Response
     */
    public boolean isExistFile(String fileName, String bucketName) {
        try {
            if (!this.client.doesBucketExist(bucketName)) {
                throw new QCloudCOSApiException("[腾讯云COS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
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
    public List<COSObjectSummary>  listFile(String bucketName, ObjectsRequestEntity request) {
        try {

            if (!this.client.doesBucketExist(bucketName)) {
                throw new QCloudCOSApiException("[腾讯云COS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ListObjectsRequest listRequest = new ListObjectsRequest();
            
            ObjectListing result = client.listObjects(listRequest);
            return result.getObjectSummaries();
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
            if (!this.client.doesBucketExist(bucketName)) {
                throw new QCloudCOSApiException("[腾讯云COS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            this.client.deleteObject(bucketName, fileName);
        } finally {
            this.shutdown();
        }
    }
    
    /**
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucketName    需要上传到的目标bucket
     * @return Response
     */
    public String uploadFile(File localFile, String fileName, String bucketName) {
        try {
            if (!this.client.doesBucketExist(bucketName)) {
                throw new QCloudCOSApiException("[腾讯云COS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            InputStream inputStream = new FileInputStream(localFile);
            return this.uploadFile(inputStream, fileName, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new QCloudCOSApiException("[腾讯云COS] 文件上传失败！" + localFile, e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @param bucketName  需要上传到的目标bucket
     * @return Response
     */
    public String uploadFile(InputStream inputStream, String fileName, String bucketName) {
        try {
            if (!this.client.doesBucketExist(bucketName)) {
                throw new QCloudCOSApiException("[腾讯云COS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(FileUtil.getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            PutObjectResult putObjectResult = this.client.putObject(bucketName, fileName, inputStream, objectMetadata);
            return putObjectResult.getETag();
        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
        try {
            this.client.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
