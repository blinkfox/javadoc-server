package com.blinkfox.javadoc.controller;

import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.service.JarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Javadoc 文档的控制器类.
 *
 * @author blinkfox on 2019-05-26.
 */
@Slf4j
@RestController
@RequestMapping("/javadoc")
@Api(tags = "获取 Javadoc 的接口")
public class JavadocController {

    @Resource
    private JarService jarService;

    /**
     * 根据 maven 坐标的相关信息获取或初始化生成 Javadoc，并上传到 MinIO 返回有效的 URL 地址.
     *
     * @param groupId 组 ID
     * @param artifactId artifactId
     * @param version 版本
     * @return URL 字符串
     */
    @ApiOperation(value = "获取 Javadoc 文档的 MinIO URL 地址的接口")
    @GetMapping("/{groupId}/{artifactId}/{version}")
    public ResponseEntity<String> getJavadocUrl(
            @PathVariable("groupId") String groupId,
            @PathVariable("artifactId") String artifactId,
            @PathVariable("version") String version) {
        return ResponseEntity.ok(jarService.getOrGenerateJavadocUrl(new JarInfo(groupId, artifactId, version)));
    }

}
