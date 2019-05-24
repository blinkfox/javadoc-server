package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.config.SystemConfig;
import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.exception.RunException;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

    public void downloadAndDecompressJar(JarInfo jarInfo) {
        Optional<File> jarFileOptional = this.download(jarInfo);
        if (!jarFileOptional.isPresent()) {
            throw new RunException("未下载到对应的 jar 包!");
        }

        this.decompressJar("/Users/blinkfox/Downloads/test/jarfiles", jarFileOptional.get());
    }

    /**
     * 根据 jar 包的信息下载对应的 jar 包.
     *
     * @param jarInfo jar 包的信息
     */
    public Optional<File> download(JarInfo jarInfo) {
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
//            File jarFile = new File(FileUtils.getTempDirectoryPath() + File.separator + jarInfo.getJavadocJarName());
            File jarFile = new File("/Users/blinkfox/Downloads/test" + File.separator + jarInfo.getJavadocJarName());
            log.info("\n--- 需要下载的url地址为:{}\n--- 生成的 javadoc 文件路径为: {}", url, jarFile);

            // 下载 javadoc.jar 文件，并写入到文件中.
            try {
                log.info("开始尝试从 {} 仓库下载 jar 包...", repo);
                byte[] jarBytes = new RestTemplate().getForObject(url, byte[].class);
                log.info("从 {} 仓库下载 jar 包完毕，下载耗时: {} ms.", repo, System.currentTimeMillis() - start);
                if (jarBytes == null) {
                    throw new RunException("下载 Maven 仓库中的 jar 包失败.");
                }
                FileUtils.writeByteArrayToFile(jarFile, jarBytes);
            } catch (Exception e) {
                log.error("下载 {} 仓库中的 jar 包出错, 下载的 url 为: {}.", repo, url, e);
                continue;
            }

            // 如果 javadoc.jar 文件已经存在，就跳出循环.
            if (jarFile.exists()) {
                log.info("已下载到 jar 包，耗时: {} ms.", System.currentTimeMillis() - start);
                return Optional.of(jarFile);
            }
        }

        return Optional.empty();
    }

    /**
     * 解压缩 jar 文件.
     *
     * @param destDir 解压的目标目录
     * @param jarFile jar文件
     */
    private void decompressJar(String destDir, File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumEntries.nextElement();
                File file = new File(destDir + File.separator + jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    FileUtils.forceMkdir(file);
                    continue;
                }

                FileUtils.copyInputStreamToFile(jar.getInputStream(jarEntry), file);
            }
            log.info("解压缩 jar 文件完成，压缩后的文件夾路径为: {}", destDir);
        } catch (Exception e) {
            log.error("解压缩 jar 文件出错！", e);
            throw new RunException("解压缩 jar 文件出错!", e);
        }
    }

}
