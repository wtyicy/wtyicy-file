package com.wtyicy.api;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.exception.JingDongApiException;
import com.wtyicy.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化京东云S3
 * @author wtyicy
 * @date 2020-10-14 08:43
 */
public class JingDongYunS3Api {

    private AmazonS3 s3;

    public JingDongYunS3Api(AmazonS3 s3) {
        this.s3 = s3;
    }

    public JingDongYunS3Api(String accessKeyId, String accessKeySecret, String bucket,String endpoint) {
        ClientConfiguration config = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, bucket);
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId,accessKeySecret);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        this.s3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     */
    public boolean isExistFile(String fileName, String bucketName) {
        try {
            if (!this.s3.doesBucketExistV2(bucketName)) {
                throw new JingDongApiException("[京东云S3] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            return this.s3.doesObjectExist(bucketName, fileName);
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
    public List<S3ObjectSummary>  listFile(String bucketName, ObjectsRequestEntity request) {
        try {

            if (!this.s3.doesBucketExistV2(bucketName)) {
                throw new JingDongApiException("[京东云S3] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ListObjectsRequest listRequest = new ListObjectsRequest();
            ObjectListing result = s3.listObjects(listRequest);
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
            if (!this.s3.doesBucketExistV2(bucketName)) {
                throw new JingDongApiException("[京东云S3] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            this.s3.deleteObject(bucketName, fileName);
        } finally {
            this.shutdown();
        }
    }

    /**
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucketName    需要上传到的目标bucket
     */
    public String uploadFile(File localFile, String fileName, String bucketName) {
        try {
            if (!this.s3.doesBucketExistV2(bucketName)) {
                throw new JingDongApiException("[京东云S3] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            InputStream inputStream = new FileInputStream(localFile);
            return this.uploadFile(inputStream, fileName, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JingDongApiException("[京东云S3] 文件上传失败！" + localFile, e);
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
            if (!this.s3.doesBucketExistV2(bucketName)) {
                throw new JingDongApiException("[京东云S3] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(FileUtil.getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            PutObjectResult putObjectResult = this.s3.putObject(bucketName, fileName, inputStream, objectMetadata);
            return putObjectResult.getETag();
        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
        try {
            this.s3.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
