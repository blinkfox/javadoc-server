package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.config.SystemConfig;
import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.exception.RunException;

import java.io.File;
import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * jar 包相关的操作服务类.
 *
 * @author blinkfox on 2019-05-22.
 */
@Slf4j
@Service
public class JarService {

    @Resource
    private SystemConfig systemConfig;

    /**
     * 根据 jar 包的信息下载对应的 jar 包.
     *
     * @param jarInfo jar 包的信息
     */
    public void download(JarInfo jarInfo) {
        if (jarInfo == null || jarInfo.valid()) {
            throw new RunException("需要下载的 jar 包相关信息不全！");
        }

        List<String> mvnRepos = systemConfig.getMvnRepos();
        if (CollectionUtils.isEmpty(mvnRepos)) {
            throw new RunException("没有配置可以下载 jar 的 Maven 仓库地址！");
        }

        long start = System.currentTimeMillis();
        // 循环遍历下载各个 Maven 仓库的 javadoc jar 包，直到下载到为止.
        for (String repo : mvnRepos) {
            String url = jarInfo.joinJavadocDownloadUrl(repo);
             File jarFile = new File(FileUtils.getTempDirectoryPath() + File.separator + jarInfo.getJavadocJarName());
            log.info("\n--- 需要下载的url地址为:{}\n--- 生成的 javadoc 文件路径为: {}", url, jarFile);

            // 下载 javadoc.jar 文件，并写入到文件中.
            try {
                byte[] jarBytes = new RestTemplate().getForObject(url, byte[].class);
                if (jarBytes == null) {
                    throw new RunException("下载 Maven 仓库中的 jar 包失败.");
                }
                FileUtils.writeByteArrayToFile(jarFile, jarBytes);
            } catch (Exception e) {
                log.error("下载 {} 仓库中的 jar 包出错, 下载的 url 为: {}.", repo, url, e);
            }

            // 如果 javadoc.jar 文件已经存在，就跳出循环.
            if (jarFile.exists()) {
                log.info("该文件已经存在，将跳出循环，不再下载 jar 包了.");
                jarInfo.setJarFile(jarFile);
                log.info("下载 jar 包完成，耗时: {} ms.", System.currentTimeMillis() - start);
                break;
            }
        }
        log.info("执行完毕!");
    }

    /**
     * 解压缩 jar 文件.
     *
     * @param jarInfo jar信息
     */
    public void decompressJar(JarInfo jarInfo) {
        
    }

}
