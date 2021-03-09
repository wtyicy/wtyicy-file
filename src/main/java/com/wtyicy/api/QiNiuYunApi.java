package com.wtyicy.api;


import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.wtyicy.exception.QiniuApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化七牛云
 *
 * @author wtyicy
 *  2020-10-14 08:43
 */
public class QiNiuYunApi {

    private String upToken;

    private UploadManager uploadManager;

    private BucketManager bucketManager;

    public QiNiuYunApi(String upToken, UploadManager uploadManager,BucketManager bucketManager) {
        this.upToken = upToken;
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
    }

    public QiNiuYunApi(String accessKeyId, String accessKeySecret, String bucket) {
        Configuration cfg = new Configuration(Region.autoRegion());
        this.uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKeyId, accessKeySecret);
        this.bucketManager = new BucketManager(auth, cfg);
        this.upToken = auth.uploadToken(bucket);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @param bucketName 存储空间
     * @param is 文件
     * @return 是否存在
     */
    public boolean isExistFile(String fileName, String bucketName, InputStream is) {
        try {
            if (bucketManager == null) {
                throw new QiniuApiException("[七牛云] 无法判断文件是否存在！Bucket不存在："+bucketName );
            }
            this.uploadManager.put(is, fileName, upToken, null, null);
            return true;
        } catch (QiniuException e) {
            throw new QiniuApiException("[七牛云] 文件上传失败！" + fileName, e);
        }

    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储空间名
     * @return 文件列表
     */
    public List<FileInfo> listFile(String bucketName) {
        try {
            if (bucketManager == null) {
                throw new QiniuApiException("[七牛云] 无法获取文件列表！Bucket不存在："+bucketName );
            }
            //文件名前缀
            String prefix = "";
            //每次迭代的长度限制，最大1000，推荐值 1000
            int limit = 1000;
            //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            String delimiter = "";

            //列举空间文件列表
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);
            List<FileInfo> fileInfos = new ArrayList<>();
            while (fileListIterator.hasNext()) {
                //处理获取的file list结果
                FileInfo[] items = fileListIterator.next();
                for (FileInfo item : items) {
                    System.out.println(item.key);
                    fileInfos.add(item);
                }
            }

            return fileInfos;
        } catch (Exception e) {
            throw new QiniuApiException("[七牛云] 获取文件列表失败！" + bucketName, e);
        }
    }


    /**
     * 删除文件
     *
     * @param bucketName 保存文件的目标bucket
     * @param fileName   BOS中保存的文件名
     * @return Response
     */
    public Response deleteFile(String fileName, String bucketName) {
        try {
            if (bucketManager == null) {
                throw new QiniuApiException("[七牛云] 无法判断文件是否存在！Bucket不存在："+bucketName );
            }
            return this.bucketManager.delete(bucketName, fileName);
        } catch (QiniuException e) {
            throw new QiniuApiException("[七牛云] 文件上传失败！" + fileName, e);
        }

    }

    /**
     * @param localFile 待上传的文件
     * @param fileName  文件名:最终保存到云端的文件名
     * @param bucketName  文件名:最终保存到云端的文件名
     * @return Response
     */
    public Response uploadFile(File localFile, String fileName,String bucketName) {
        try {
            if (bucketManager == null) {
                throw new QiniuApiException("[七牛云] 无法判断文件是否存在！Bucket不存在："+bucketName );
            }
            InputStream inputStream = new FileInputStream(localFile);
            return this.uploadFile(inputStream, fileName, bucketName);
        } catch (Exception e) {
            throw new QiniuApiException("[七牛云] 文件上传失败！" + localFile, e);

        }
    }

    /**
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @param bucketName    文件名:最终保存到云端的文件名
     * @return Response
     */
    public Response uploadFile(InputStream inputStream, String fileName,String bucketName) {
        try {
            if (bucketManager == null) {
                throw new QiniuApiException("[七牛云] 无法判断文件是否存在！Bucket不存在："+bucketName );
            }
            return this.uploadManager.put(inputStream, fileName, upToken, null, null);
        } catch (QiniuException e) {
            throw new QiniuApiException("[七牛云] 文件上传失败！" + fileName, e);
        }
    }


}
