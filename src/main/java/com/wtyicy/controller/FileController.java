package com.wtyicy.controller;

import com.wtyicy.config.BaseConfig;
import com.wtyicy.entity.VirtualFile;
import com.wtyicy.uploader.FileUploader;
import com.wtyicy.uploader.GlobalFileUploader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wtyicy
 */
@RestController
public class FileController {

    @Resource
    private BaseConfig baseConfig;

    @Resource
    private FileUploader fileUploader;


    @RequestMapping("file/add")
    public String add(MultipartFile[] file) {
        if (null == file || file.length == 0) {
            return "请至少选择一张图片！";
        }
        if (StringUtils.isEmpty(baseConfig.getFilePath())){
            baseConfig.setFilePath("wtyicy");
        }
        List<VirtualFile> virtualFiles = new ArrayList<>();
        for (MultipartFile multipartFile : file) {
//            FileUploader uploader = new GlobalFileUploader();
            VirtualFile upload = fileUploader.upload(multipartFile, baseConfig.getFilePath(), true);
            virtualFiles.add(upload);
        }
        System.out.println(virtualFiles);

        return "成功上传" + virtualFiles.size() + "张图片";
    }
}
