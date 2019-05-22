package com.blinkfox.javadoc.entity;

import com.blinkfox.javadoc.kits.StringKit;

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
     * 获取 javadoc 文件的 jar 包名称.
     *
     * @return jar 包名称
     */
    public String getJavadocJarName() {
        return this.artifactId + "-" + this.version + "-javadoc.jar";
    }

    /**
     * 根据 Maven 仓库的 url 地址 及本 jar 的相关信息拼接出下载 javadoc 的 url 地址.
     *
     * @param repo Maven 仓库的 url 地址
     * @return 下载 javadoc 的 url 地址
     */
    public String joinJavadocDownloadUrl(String repo) {
        return StringKit.format("{}/{}/{}/{}/{}", repo, this.getGroupIdWithSlash(),
                this.artifactId, this.version, this.getJavadocJarName());
    }

}
