package com.blinkfox.javadoc.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

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

}
