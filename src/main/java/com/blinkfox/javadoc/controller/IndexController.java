package com.blinkfox.javadoc.controller;

import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.service.JarService;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页根控制器.
 *
 * @author blinkfox on 2019-05-22.
 */
@Slf4j
@RestController
@RequestMapping
public class IndexController {

    @Resource
    private JarService jarService;

    /**
     * 首页请求.
     *
     * @return 字符串
     */
    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello Javadoc Server.");
    }

    /**
     * 测试下载 jar 包.
     *
     * @return 字符串
     */
    @GetMapping("/download")
    public ResponseEntity<String> download() {
        String url = jarService.downloadAndDecompressJar(new JarInfo("com.blinkfox", "jpack-maven-plugin", "1.2.0"));
        log.info("生成的url: {}.", url);
        return ResponseEntity.ok("下载 jar 文件成功.");
    }

}
