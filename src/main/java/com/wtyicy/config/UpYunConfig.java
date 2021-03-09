package com.wtyicy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * upyun oss 配置
 * @author wtyicy
 *  2020-10-14 08:43
 */
@Configuration
public class UpYunConfig {

    /**
     * 服务名
     */
    @Value("${wtyicy.upyun.serviceName}")
    private String serviceName;
    /**
     * 操作员名
     */
    @Value("${wtyicy.upyun.operatorName}")
    private String operatorName;
    /**
     * 操作员密码
     */
    @Value("${wtyicy.upyun.operatorPwd}")
    private String operatorPwd;

    /**
     * 访问地址
     */
    @Value("${wtyicy.upyun.path}")
    private String path;

    public UpYunConfig() {
    }

    public UpYunConfig(String serviceName, String operatorName, String operatorPwd,String path) {
        this.serviceName = serviceName;
        this.operatorName = operatorName;
        this.operatorPwd = operatorPwd;
        this.path = path;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPwd() {
        return operatorPwd;
    }

    public void setOperatorPwd(String operatorPwd) {
        this.operatorPwd = operatorPwd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
