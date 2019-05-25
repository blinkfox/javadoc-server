package com.blinkfox.javadoc.consts;

import com.blinkfox.javadoc.exception.RunException;
import com.blinkfox.javadoc.kits.StringKit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 主要操作系统平台的枚举类.
 *
 * @author blinkfox on 2019-05-25.
 */
@Slf4j
public enum OsEnum {

    /**
     * Windows 系统.
     */
    WINDOWS("windows", "mc.exe", "mc/windows/mc.exe"),

    /**
     * Linux 系统.
     */
    LINUX("linux", "mc", "mc/linux/mc"),

    /**
     * Mac 系统.
     */
    MAC("mac", "mc", "mc/mac/mc");

    /**
     * 操作系统名称.
     */
    private String name;

    /**
     * MinIO Client 的命令行指令标识.
     */
    @Getter
    private String mc;

    /**
     * mc 在本项目的资源路径.
     */
    @Getter
    private String sourcePath;

    /**
     * 构造方法.
     *
     * @param name 操作系统名称
     * @param mc MinIO Client 的命令行指令标识
     * @param sourcePath mc 在本项目的资源路径
     */
    OsEnum(String name, String mc, String sourcePath) {
        this.name = name;
        this.mc = mc;
        this.sourcePath = sourcePath;
    }

    /**
     * 获得当前操作系统的枚举实例.
     *
     * @return 操作系统的枚举实例
     */
    public static OsEnum of() {
        String os = System.getProperty("os.name").toLowerCase();
        log.info("操作系统名称:【{}】.", os);

        // 判断操作系统，返回对应操作系统的 MinIO Client资源路径.
        if (os.contains(WINDOWS.name)) {
            return WINDOWS;
        } else if (os.contains(LINUX.name)) {
            return LINUX;
        } else if (os.contains(MAC.name)) {
            return MAC;
        }

        throw new RunException(StringKit.format("没有【{}】操作系统的 MinIO Client，请使用主流操作系统部署.", os));
    }

}
