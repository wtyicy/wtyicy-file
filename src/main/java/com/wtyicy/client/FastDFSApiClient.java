package com.wtyicy.client;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wtyicy.api.FastDFSApi;
import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.exception.FdfsApiException;
import com.wtyicy.holder.SpringContextHolder;
import com.wtyicy.util.FileUtil;
import com.wtyicy.util.StreamUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * fastDFS客户端
 * @author wtyicy
 */
public class FastDFSApiClient extends BaseApiClient {

    @Resource
    public BaseConfig baseConfig;

    private FastDFSApi fastDFSApi;

    private String url;
    private String pathPrefix;

    public FastDFSApiClient() {
        super("FastDFS存储");
    }

    public FastDFSApiClient init(String baseUrl, String uploadType) {
        this.fastDFSApi = new FastDFSApi();
        this.url = baseUrl;

        this.pathPrefix = StringUtils.isEmpty(uploadType) ? baseConfig.getFilePath() : uploadType.endsWith("/") ? uploadType : uploadType + "/";
        return this;
    }


    @Override
    public VirtualFile uploadImg(InputStream is, String imageUrl) {
        this.check();
        FastFileStorageClient fastFileStorageClient = SpringContextHolder.getBean(FastFileStorageClient.class);

        String key = FileUtil.generateTempFileName(imageUrl);
        this.createNewFileName(key, pathPrefix);
        Date startTime = new Date();

        try {
            InputStream fileHashIs = StreamUtil.clone(is);

            String fullPath = fastFileStorageClient.uploadFile(is, fileHashIs.available(), FilenameUtils.getExtension(imageUrl), null).getFullPath();

            return new VirtualFile()
                    .setOriginalFileName(FileUtil.getName(key))
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.url + fullPath);
        } catch (Exception e) {
            throw new FdfsApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage() + imageUrl);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public VirtualFile uploadFadfsImg(MultipartFile file) {
        this.check();

        String key = FileUtil.generateTempFileName(file.getOriginalFilename());
        this.createNewFileName(key, pathPrefix);
        Date startTime = new Date();

        try {
            InputStream fileHashIs = StreamUtil.clone(file.getInputStream());

            String fullPath = this.fastDFSApi.uploadFile(file);

            return new VirtualFile()
                    .setOriginalFileName(FileUtil.getName(key))
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.url + fullPath);
        } catch (Exception e) {
            throw new FdfsApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage() + file.getOriginalFilename());
        }
    }

    @Override
    public boolean removeFile(String key) {
        this.check();
        if (org.springframework.util.StringUtils.isEmpty(key)) {
            throw new FdfsApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }
        File file = new File(this.url + key);
        if (!file.exists()) {
            throw new FdfsApiException("[" + this.storageType + "]删除文件失败：文件不存在[" + this.url + key + "]");
        }
        try {
            return file.delete();
        } catch (Exception e) {
            throw new FdfsApiException("[" + this.storageType + "]删除文件失败：" + e.getMessage());
        }
    }

    @Override
    protected void check() {
        if (StringUtils.isEmpty(url) ) {
            throw new FdfsApiException("[" + this.storageType + "]尚未配置FDFS文件服务器，文件上传功能暂时不可用！");
        }
    }

}
