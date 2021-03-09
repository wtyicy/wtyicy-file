package com.wtyicy.api;


import com.UpYun;
import com.qiniu.http.Response;
import com.upyun.FormUploader;
import com.upyun.Params;
import com.upyun.Result;
import com.upyun.UpException;
import com.wtyicy.exception.UpYunApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化又拍云
 * @author wtyicy
 *  2020-10-14 08:43
 */
public class UpYunApi {

    private UpYun upYun;

    private FormUploader uploader;

    public UpYunApi(UpYun upYun,FormUploader uploader) {
        this.upYun = upYun;
        this.uploader = uploader;
    }

    public UpYunApi(String serviceName, String operatorName, String operatorPwd) {
        this.upYun = new UpYun(serviceName,operatorName,operatorPwd);
        upYun.setTimeout(30);
        upYun.setApiDomain(UpYun.ED_AUTO);
        this.uploader = new FormUploader(serviceName,operatorName,operatorPwd);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName   BOS中保存的文件名
     * @return 是否存在
     */
    public boolean isExistFile(String fileName) {
        if (this.uploader.getBucketName() == null) {
            throw new UpYunApiException("[又拍云] 无法判断文件是否存在！服务不存在" );
        }
        return true;
    }

    /**
     * 获取文件列表
     *
     */
    public void listFile() {
        if (this.uploader.getBucketName() == null) {
            throw new UpYunApiException("[又拍云] 无法判断文件是否存在！服务不存在" );
        }

    }

    /**
     * 删除文件
     *
     * @param fileName   BOS中保存的文件名
     */
    public void deleteFile(String fileName) {
        try {
            if (this.uploader.getBucketName() == null) {
                throw new UpYunApiException("[又拍云] 无法判断文件是否存在！服务不存在" );
            }
            try {
                this.upYun.deleteFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UpException e) {
                e.printStackTrace();
            }
        } finally {
            this.shutdown();
        }

    }

    //保存路径 必须设置该参数
    private static String savePath = "/uploads/{year}{mon}{day}/{random32}{.suffix}";

    /**
     * 文件方式上传文件
     * @param localFile 待上传的文件
     * @return Response
     */
    public String uploadFile(File localFile,String config) {
        try {
            if (this.uploader.getBucketName() == null) {
                throw new UpYunApiException("[又拍云] 无法判断文件是否存在！服务不存在" );
            }

            boolean b = this.upYun.writeFile(config, localFile, true);
            return "  ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpYunApiException("[又拍云] 文件上传失败！" + localFile, e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * 文件流上传文件
     * @param inputStream 待上传的文件流
     * @param fileName    文件名:最终保存到云端的文件名
     * @return 文件标签
     */
    public String uploadFile(InputStream inputStream, String fileName) {
        Map<String,String> paramsMap = new HashMap<>();
        try {
            this.upYun.writeFile(fileName, inputStream, true, paramsMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UpException e) {
            e.printStackTrace();
        }
        return "上传成功";
    }

    private void shutdown() {

    }

}
