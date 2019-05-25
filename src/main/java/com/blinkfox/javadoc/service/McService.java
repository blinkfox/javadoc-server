package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.exception.RunException;
import com.blinkfox.javadoc.kits.StringKit;

import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

/**
 * MinIO Client 操作的相关 Service 类.
 *
 * @author blinkfox on 2019-05-25.
 */
@Slf4j
@Service
public class McService {

    /**
     * 本服务的目录标识名常量.
     */
    private static final String SERVER_DIR = ".javadoc-server";

    /**
     * MC 执行命令的关键标识的常量.
     */
    private static final String MC = "mc";

    /**
     * mc 的可执行路径.
     */
    @Getter
    private String mcPath;

    /**
     * 初始化安装 MinIO Client（即`mc`）到服务器本地用户目录下，便于后续执行 mc 的相关命令.
     */
    @PostConstruct
    public void initInstall() throws IOException {
        this.mcPath = FileUtils.getUserDirectoryPath() + File.separator + SERVER_DIR + File.separator + MC;
        File mcFile = new File(this.mcPath);
        FileUtils.forceMkdirParent(mcFile);
        if (mcFile.exists()) {
            log.debug("MinIO Client 文件已经存在，将跳过安装.");
            return;
        }

        // 将 resources 中的 mc 复制到用户目录的 `.javadoc-server` 目录下
        FileUtils.copyInputStreamToFile(new DefaultResourceLoader()
                .getResource(this.getMcResource()).getInputStream(), mcFile);
    }

    /**
     * 获取 MinIO Client 的资源路径.
     *
     * @return mc的资源路径
     */
    private String getMcResource() {
        String os = System.getProperty("os.name").toLowerCase();
        log.info("操作系统名称:【{}】.", os);

        // 判断操作系统，返回对应操作系统的 MinIO Client资源路径.
        if (os.contains("windows")) {
            return  "mc/windows/mc";
        } else if (os.contains("linux")) {
            return  "mc/linux/mc";
        } else if (os.contains("mac")) {
            return  "mc/mac/mc";
        }

        throw new RunException(StringKit.format("没有【{}】操作系统的 MinIO Client，请使用其他操作系统.", os));
    }

}
