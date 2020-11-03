package com.wtyicy.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.conn.TrackerConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.List;

/**
 * 导入FastDFS-Client组件
 *解决jmx重复注册bean的问题
 * @author tobato
 *
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConfigurationProperties(prefix = "wtyicy.fdfs")
public class FastDFSConfig {

    /**
     * 访问 域名
     */
    private String url;

    /**
     * 上传的地址
     */
    private List<String> trackerList;

    @Bean
    public TrackerConnectionManager getTrackerConnectionManager(TrackerConnectionManager trackerConnectionManager){
        trackerConnectionManager.setTrackerList(trackerList);
        return trackerConnectionManager;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTrackerList() {
        return trackerList;
    }

    public void setTrackerList(List<String> trackerList) {
        this.trackerList = trackerList;
    }
}
