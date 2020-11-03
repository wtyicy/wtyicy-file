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
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.exception.FdfsApiException;
import com.wtyicy.exception.FdfsApiException;
import com.wtyicy.holder.SpringContextHolder;
import com.wtyicy.util.FileUtil;
import com.wtyicy.util.MultipartFileToFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化FastDFS
 * @author wtyicy
 * @date 2020-10-14 08:43
 */
public class FastDFSApi {

    private FastFileStorageClient fastFileStorageClient ;

    public FastDFSApi(FastFileStorageClient fastFileStorageClient) {
        this.fastFileStorageClient = fastFileStorageClient;
    }

    public FastDFSApi() {
        this.fastFileStorageClient = SpringContextHolder.getBean(FastFileStorageClient.class);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     */
    public boolean isExistFile(String fileName, String bucketName) {
//        try {
//            if (!this.s3.doesBucketExistV2(bucketName)) {
//                throw new FdfsApiException("[FastDFS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
//            }
//            return this.s3.doesObjectExist(bucketName, fileName);
//        } finally {
//            this.shutdown();
//        }
        return false;
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储空间名
     * @param request    查询条件
     * @return 文件列表
     */
    public List<S3ObjectSummary>  listFile(String bucketName, ObjectsRequestEntity request) {
//        try {
//
//            if (!this.s3.doesBucketExistV2(bucketName)) {
//                throw new FdfsApiException("[FastDFS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
//            }
//            ListObjectsRequest listRequest = new ListObjectsRequest();
//            ObjectListing result = s3.listObjects(listRequest);
//            return result.getObjectSummaries();
//        } finally {
//            this.shutdown();
//        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param bucketName 保存文件的目标bucket
     * @param fileName   BOS中保存的文件名
     */
    public void deleteFile(String fileName, String bucketName) {
//        try {
//            if (!this.s3.doesBucketExistV2(bucketName)) {
//                throw new FdfsApiException("[FastDFS] 无法授权访问文件的URL！Bucket不存在：" + bucketName);
//            }
//            this.s3.deleteObject(bucketName, fileName);
//        } finally {
//            this.shutdown();
//        }
    }

    /**
     * @param file 待上传的文件
     */
    public String uploadFile(MultipartFile file) {
        try {
            return this.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()));


        } catch (Exception e) {
            e.printStackTrace();
            throw new FdfsApiException("[FastDFS] 文件上传失败！" + file, e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * @param inputStream 待上传的文件流
     * @param size    文件名:最终保存到云端的文件名
     * @param originalFilename  需要上传到的目标bucket
     */
    public String uploadFile(InputStream inputStream, Long size, String originalFilename) {
        try {

            return this.fastFileStorageClient.uploadFile(inputStream, size, originalFilename, null).getFullPath();

        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
//        try {
//            this.s3.shutdown();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
