package com.blinkfox.javadoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 应用服务主入口类.
 *
 * @author blinkfox on 2019-05-20.
 */
@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties
public class JavadocServerApplication {

    /**
     * 应用服务主入口.
     *
     * @param args 数组参数
     */
    public static void main(String[] args) {
        SpringApplication.run(JavadocServerApplication.class, args);
    }

}
