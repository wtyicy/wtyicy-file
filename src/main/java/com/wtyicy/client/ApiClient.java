package com.wtyicy.client;

import com.wtyicy.entity.VirtualFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author wtyicy
 *  2020-11-05
 */
public interface ApiClient {

    VirtualFile uploadImg(MultipartFile file);

    VirtualFile uploadImg(File file);

    VirtualFile uploadImg(InputStream is, String key);

    VirtualFile uploadFadfsImg(MultipartFile file);

    boolean removeFile(String key);
}
