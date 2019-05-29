package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.config.SystemConfig;
import com.blinkfox.javadoc.entity.JarInfo;
import com.blinkfox.javadoc.exception.RunException;
import com.blinkfox.javadoc.kits.StringKit;
import com.blinkfox.javadoc.pojo.Record;
import com.blinkfox.javadoc.repository.RecordRepository;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.Cacheable;
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
    private MinioClientService minioClientService;

    @Resource
    private SystemConfig systemConfig;

    @Resource
    private RecordRepository recordRepository;

    /**
     * 下载 jar 包并解压缩 jar 包上传资源到 MinIO.
     *
     * @param jarInfo jar 包的信息
     */
    @Cacheable(cacheNames = "javadoc", key = "#jarInfo.toString()")
    public String getOrGenerateJavadocUrl(JarInfo jarInfo) {
        if (jarInfo == null || jarInfo.valid()) {
            throw new RunException("需要下载的 jar 包相关信息不全！");
        }

        // 查询该 jar 包是否有上传记录，如果有的话，就直接返回地址.
        Record record = recordRepository.queryByJarInfo(jarInfo.getGroupId(),
                jarInfo.getArtifactId(), jarInfo.getVersion());
        if (record != null) {
            log.info("该 jar 信息已经上传过了，将直接返回 MinIO 地址.");
            return record.getUrl();
        }

        // 下载 jar 包.
        this.download(jarInfo);

        // 生成 javadoc 的路径，如：/Users/blinkfox/.javadoc-server/{uuid}/com/blinkfox/zealot/1.3.1/zealot-1.3.1-javadoc
        String tempDir = minioClientService.getServerHomeFile() + File.separator + StringKit.getUuid();
        String assetsDir = tempDir + File.separator + jarInfo.getJavadocSlashPath();
        try {
            FileUtils.forceMkdir(new File(assetsDir));
        } catch (IOException e) {
            throw new RunException(StringKit.format("创建临时存放 javadoc 资源的目录失败，目录为:【{}】.", assetsDir), e);
        }

        // 解压 javadoc.jar 的文件资源到资源目录中
        // 资源目录结构如：/Users/blinkfox/.javadoc-server/{uuid}/com/blinkfox/zealot/1.3.1/zealot-1.3.1-javadoc
        this.decompressJar(assetsDir, jarInfo);

        // 递归上传（如：`/Users/blinkfox/.javadoc-server/{uuid}/com`）目录下的所有资源到 MinIO 中.
        minioClientService.uploadJardocFiles(tempDir + File.separator + jarInfo.getGroupIdFirstSplitName());

        // 保存记录信息，并清除相关的数据，然后返回 MinIO 中 javadoc 资源的地址即可.
        Record savedRecord = this.saveRecord(jarInfo);
        this.clean(tempDir);
        return savedRecord.getUrl();
    }

    /**
     * 根据 jar 包的信息下载对应的 jar 包.
     *
     * @param jarInfo jar 包的信息
     */
    private void download(JarInfo jarInfo) {
        List<String> mvnRepos = systemConfig.getMvnRepos();
        if (CollectionUtils.isEmpty(mvnRepos)) {
            throw new RunException("没有配置可以下载 jar 的 Maven 仓库地址！");
        }

        long start = System.currentTimeMillis();
        // 循环遍历下载各个 Maven 仓库的 javadoc jar 包，直到下载到为止.
        for (int i = 0, len = mvnRepos.size(); i < len; i++) {
            String repo = mvnRepos.get(i);
            String url = jarInfo.joinJavadocDownloadUrl(repo);
            File jarFile = new File(FileUtils.getTempDirectoryPath() + File.separator + jarInfo.getJavadocJarName());

            try {
                log.info("第【{}】次开始从【{}】仓库链接中下载 jar 包...", i + 1, url);
                // 下载 javadoc.jar 文件，并写入到服务器临时文件中，然后返回，否则就继续从其他仓库去下载.
                ResponseEntity<byte[]> response = new RestTemplate().getForEntity(url, byte[].class);
                if (this.hasDownloadedJar(response) && response.getBody() != null)  {

                    FileUtils.writeByteArrayToFile(jarFile, response.getBody());
                    if (jarFile.exists()) {
                        log.info("从 {} 仓库下载 jar 包到服务器完毕，下载总耗时: {} ms.", repo, System.currentTimeMillis() - start);
                        jarInfo.setJarFile(jarFile);
                        return;
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
     * @param jarInfo jar 包信息
     */
    private void decompressJar(String destDir, JarInfo jarInfo) {
        try (JarFile jar = new JarFile(jarInfo.getJarFile())) {
            Enumeration<?> enumEntries = jar.entries();
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
     * 清除相关的临时资源.
     *
     * @param tempDir 该 jar 包的临时目录
     */
    private void clean(String tempDir) {
        FileUtils.deleteQuietly(new File(tempDir));
    }

    /**
     * 根据 jar 包信息保存相关的上传记录到数据库中.
     *
     * @param jarInfo jar 包信息
     * @return 保存的 record 对象
     */
    private Record saveRecord(JarInfo jarInfo) {
        Record record = new Record()
                .setId(StringKit.getUuid())
                .setGroupId(jarInfo.getGroupId())
                .setArtifactId(jarInfo.getArtifactId())
                .setVersion(jarInfo.getVersion())
                .setUrl(this.getJavadocMinioUrl(jarInfo))
                .setUptime(new Date());
        recordRepository.save(record);
        return record;
    }

    /**
     * 获取 MinIO 中 javadoc 可访问的 url 地址.
     *
     * @param jarInfo jar 包信息
     * @return url地址
     */
    private String getJavadocMinioUrl(JarInfo jarInfo) {
        return StringKit.format("{}/{}/{}/index.html", systemConfig.getEndpoint(), systemConfig.getBucket(),
                jarInfo.getJavadocSlashPath());
    }

}
