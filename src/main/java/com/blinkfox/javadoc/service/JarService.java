package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.config.SystemConfig;
import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.exception.RunException;
import com.blinkfox.javadoc.kits.StringKit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    /**
     * 下载 jar 包的 MediaType 类型.
     */
    private static final String JAVA_ARCHIVE = "application/java-archive";

    @Resource
    private SystemConfig systemConfig;

    public void downloadAndDecompressJar(JarInfo jarInfo) {
        if (jarInfo == null || jarInfo.valid()) {
            throw new RunException("需要下载的 jar 包相关信息不全！");
        }

        this.decompressJar("/Users/blinkfox/Downloads/test/jarfiles", this.download(jarInfo));
    }

    /**
     * 根据 jar 包的信息下载对应的 jar 包.
     *
     * @param jarInfo jar 包的信息
     */
    public File download(JarInfo jarInfo) {
        List<String> mvnRepos = systemConfig.getMvnRepos();
        if (CollectionUtils.isEmpty(mvnRepos)) {
            throw new RunException("没有配置可以下载 jar 的 Maven 仓库地址！");
        }

        long start = System.currentTimeMillis();
        // 循环遍历下载各个 Maven 仓库的 javadoc jar 包，直到下载到为止.
        for (int i = 0, len = mvnRepos.size(); i < len; i++) {
            String repo = mvnRepos.get(i);
            String url = jarInfo.joinJavadocDownloadUrl(repo);
//            File jarFile = new File(FileUtils.getTempDirectoryPath() + File.separator + jarInfo.getJavadocJarName());
            File jarFile = new File("/Users/blinkfox/Downloads/test" + File.separator + jarInfo.getJavadocJarName());

            try {
                log.info("第【{}】次开始从【{}】仓库链接中下载 jar 包...", i + 1, url);
                // 下载 javadoc.jar 文件，并写入到服务器临时文件中，然后返回，否则就继续从其他仓库去下载.
                ResponseEntity<byte[]> response = new RestTemplate().getForEntity(url, byte[].class);
                if (this.hasDownloadedJar(response) && response.getBody() != null)  {

                    FileUtils.writeByteArrayToFile(jarFile, response.getBody());
                    if (jarFile.exists()) {
                        log.info("从 {} 仓库下载 jar 包到服务器完毕，下载总耗时: {} ms.", repo, System.currentTimeMillis() - start);
                        return jarFile;
                    }
                }
            } catch (Exception e) {
                log.error("第【{}】次从【{}】仓库链接中的下载 jar 包出错.", i + 1, url, e);
            }
        }

        log.warn("从各个仓库中下载【{}】包均失败！", jarInfo.getJavadocJarName());
        throw new RunException(StringKit.format("从各个仓库中下载【{}】包均失败！", jarInfo.getJavadocJarName()));
    }

    /**
     * 判断是否已经下载到了有效的 jar 包.
     *
     * @param response 响应结果
     * @return 布尔值
     */
    private boolean hasDownloadedJar(ResponseEntity<byte[]> response) {
        MediaType mediaType = response.getHeaders().getContentType();
        return HttpStatus.OK == response.getStatusCode()
                && mediaType != null
                && JAVA_ARCHIVE.equalsIgnoreCase(mediaType.toString());
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

    /**
     * 上传解压后的 jar 文件到 MinIO 中.
     *
     * @param dir 解压后的 jar 文件目录
     */
    private static void uploadJarFiles(String dir) throws IOException {
        // String cmd = "mc cp --recursive /Users/blinkfox/Downloads/test/jarfiles my/test";
        String cmd = "mc -v";
        Process p = new ProcessBuilder(cmd).start();
        String result = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
        log.info("命令行执行结果: {}", result);
    }

    public static void main(String[] args) throws IOException {
        uploadJarFiles("");
    }

}
