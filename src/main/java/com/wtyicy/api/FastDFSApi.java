package com.wtyicy.api;


import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wtyicy.entity.ObjectsRequestEntity;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.FdfsApiException;
import com.wtyicy.holder.SpringContextHolder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 初始化FastDFS
 * @author wtyicy
 *  2020-10-14 08:43
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
     * @return 是否存在
     */
    public boolean isExistFile(String fileName, String bucketName) {

        return false;
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储空间名
     * @param request    查询条件
     * @return 文件列表
     */
    public List<VirtualFile>  listFile(String bucketName, ObjectsRequestEntity request) {

        return null;
    }

    /**
     * 删除文件
     *
     * @param bucketName 保存文件的目标bucket
     * @param fileName   BOS中保存的文件名
     */
    public void deleteFile(String fileName, String bucketName) {

    }

    /**
     * 文件方式上传文件
     * @param file 待上传的文件
     * @return 文件地址
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
     * 文件流方式 上传文件
     * @param inputStream 待上传的文件流
     * @param size    文件名:最终保存到云端的文件名
     * @param originalFilename  需要上传到的目标bucket
     * @return 文件地址
     */
    public String uploadFile(InputStream inputStream, Long size, String originalFilename) {
        try {

            return this.fastFileStorageClient.uploadFile(inputStream, size, originalFilename, null).getFullPath();

        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {

    }

}
