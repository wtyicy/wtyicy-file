package com.wtyicy.api;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ListObjectsRequest;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.exception.BaiduYunApiException;
import com.wtyicy.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化百度云BOS
 * @author wtyicy
 * @date 2020-10-14 08:43
 */
public class BaiDuYunBOSApi {

    private BosClient client;

    public BaiDuYunBOSApi(BosClient client) {
        this.client = client;
    }

    public BaiDuYunBOSApi(String accessKeyId, String accessKeySecret, String endpoint) {
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(accessKeyId, accessKeySecret));
        config.setEndpoint(endpoint);
        this.client = new BosClient(config);
    }
    
    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     * @return 是否存在
     */
    public boolean isExistFile(String fileName, String bucketName) {
        try {
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
     *
     */
    public List<BosObjectSummary> listFile(String bucketName, ObjectsRequestEntity request) {
        try {
            ListObjectsRequest listRequest = new ListObjectsRequest(bucketName);
            if (null != request) {
                listRequest.withDelimiter(request.getDelimiter())
                        .withMarker(request.getMarker())
                        .withMaxKeys(request.getMaxKeys())
                        .withPrefix(request.getPrefix());
            }
            // 列举Object
            ListObjectsResponse listObjectsResponse = this.client.listObjects(listRequest);
            return listObjectsResponse.getContents();
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
            if (!this.client.doesObjectExist(bucketName, fileName)) {
                throw new BaiduYunApiException("[百度云BOS] 文件删除失败！文件不存在：" + bucketName + "/" + fileName);
            }
            this.client.deleteObject(bucketName, fileName);
        } finally {
            this.shutdown();
        }
    }
    
    /**
     * 文件方式上传
     *
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucket    需要上传到的目标bucket
     * @return
     */
    public String uploadFile(File localFile, String fileName, String bucket) {
        try {
            InputStream inputStream = new FileInputStream(localFile);
            return this.uploadFile(inputStream, fileName, bucket);
        } catch (Exception e) {
            throw new BaiduYunApiException("[百度云BOS] 文件上传失败！" + localFile, e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * 文件流方式上传
     *
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @param bucketName  需要上传到的目标bucket
     *@return 文件标签
     */
    public String uploadFile(InputStream inputStream, String fileName, String bucketName) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(FileUtil.getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            PutObjectResponse putObjectResponse = this.client.putObject(bucketName, fileName, inputStream,objectMetadata);
            return putObjectResponse.getETag();
        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
        this.client.shutdown();
    }

}
