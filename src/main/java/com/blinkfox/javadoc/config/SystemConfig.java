package com.blinkfox.javadoc.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SystemConfig.
 *
 * @author blinkfox on 2019-05-22.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    /**
     * 配置的可用的 Maven 仓库.
     */
    private List<String> mvnRepos;

    /**
     * MinIO 地址.
     */
    @Value("${system.minio.endpoint}")
    private String endpoint;

    /**
     * MinIO 用户访问名.
     */
    @Value("${system.minio.accessKey}")
    private String accessKey;

    /**
     * MinIO 用户访问密码.
     */
    @Value("${system.minio.secretKey}")
    private String secretKey;

    /**
     * MinIO 的别名.
     */
    @Value("${system.minio.alias:javadoc}")
    private String alias;

    /**
     * MinIO 的桶.
     */
    @Value("${system.minio.bucket:docs}")
    private String bucket;

}
