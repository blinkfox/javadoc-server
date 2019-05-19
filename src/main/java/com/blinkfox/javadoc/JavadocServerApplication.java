package com.blinkfox.javadoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用服务主入口类.
 *
 * @author blinkfox on 2019-05-20.
 */
@SpringBootApplication
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
