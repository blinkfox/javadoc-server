package com.blinkfox.javadoc.entity;

import com.blinkfox.javadoc.kits.StringKit;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装的 jar 包信息实体类.
 *
 * @author blinkfox on 2019-05-22.
 */
@Getter
@Setter
public class JarInfo {

    /**
     * jar 包的组 ID.
     */
    private String groupId;

    /**
     * jar 包的 artifactId.
     */
    private String artifactId;

    /**
     * jar 包的版本.
     */
    private String version;

    /**
     * javadoc 的 jar 包文件.
     */
    private File jarFile;

    /**
     * 核心构造方法.
     *
     * @param groupId groupId
     * @param artifactId artifactId
     * @param version version
     */
    public JarInfo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * 校验本对象的值是否合法，只要有一个主要属性为空，就不合法.
     *
     * @return 布尔值
     */
    public boolean valid() {
        return StringUtils.isAnyBlank(this.groupId, this.artifactId, this.version);
    }

    /**
     * 转换获取使用'/'风格的 groupId 的字符串值.
     * <p>如：`com.blinkfox` 将返回`com/blinkfox`</p>
     *
     * @return 字符串
     */
    private String getGroupIdWithSlash() {
        return this.groupId.replace(".", "/");
    }

    /**
     * 获取 groupId 的第一个`.`号分割的字符串值.
     * <p>如：`com.blinkfox` 将返回`com`</p>
     *
     * @return 字符串
     */
    public String getGroupIdFirstSplitName() {
        return this.groupId.split("\\.")[0];
    }

    /**
     * 获取 javadoc 文件的 jar 包名称.
     *
     * @return jar 包名称
     */
    public String getJavadocJarName() {
        return this.artifactId + "-" + this.version + "-javadoc.jar";
    }

    /**
     * 获取 javadoc 的路径，使用`/`符号分割.
     * <p>生成的结果如：com/blinkfox/zealot/1.3.1</p>
     *
     * @return jar 包路径
     */
    public String getJavadocSlashPath() {
        return StringKit.format("{}/{}/{}", this.getGroupIdWithSlash(), this.artifactId, this.version);
    }

    /**
     * 根据 Maven 仓库的 url 地址 及本 jar 的相关信息拼接出下载 javadoc 的 url 地址.
     * <p>生成的结果如：https://repo1.maven.org/maven2/com/blinkfox/zealot/1.3.1/zealot-1.3.1-javadoc</p>
     *
     * @param repo Maven 仓库的 url 地址
     * @return 下载 javadoc 的 url 地址
     */
    public String joinJavadocDownloadUrl(String repo) {
        return StringKit.format("{}/{}/{}/{}/{}-{}-javadoc.jar", repo, this.getGroupIdWithSlash(),
                this.artifactId, this.version, this.artifactId, this.version);
    }

}
