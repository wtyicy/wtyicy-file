package com.wtyicy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    /**
     * 文件存载目录
     */
    @Value("${wtyicy.filePath}")
    private String filePath;

    /**
     * 默认选择服务器
     */
    @Value("${wtyicy.defaultType}")
    private String defaultType;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }
}
