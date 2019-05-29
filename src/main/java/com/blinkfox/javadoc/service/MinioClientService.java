package com.blinkfox.javadoc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blinkfox.javadoc.config.SystemConfig;
import com.blinkfox.javadoc.consts.Const;
import com.blinkfox.javadoc.consts.OsEnum;
import com.blinkfox.javadoc.exception.RunException;
import com.blinkfox.javadoc.kits.CmdKit;
import com.blinkfox.javadoc.kits.StringKit;

import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

/**
 * MinIO Client 操作的相关 Service 类.
 *
 * @author blinkfox on 2019-05-25.
 */
@Slf4j
@Service
public class MinioClientService {

    /**
     * 本服务的目录标识名常量.
     */
    private static final String SERVER_DIR = ".javadoc-server";

    /**
     * 检查版本的 `mc -v` 的常量.
     */
    private static final String V = " -v";

    @Resource
    private SystemConfig systemConfig;

    /**
     * 本服务的主目录文件，是用户目录下的 `.javadoc-server` 目录.
     */
    @Getter
    private File serverHomeFile;

    /**
     * mc 的可执行路径.
     */
    private String mcPath;

    /**
     * MinIO Client 操作系统相关的枚举实例.
     */
    private OsEnum osEnum;

    /**
     * 初始化安装 MinIO Client（即`mc`）到服务器本地用户目录下，便于后续执行 mc 的相关命令.
     */
    @PostConstruct
    public void initInstall() throws IOException {
        // 实例化操作系统的相关信息，并创建本服务的主目录文件.
        this.osEnum = OsEnum.of();
        this.serverHomeFile = new File(FileUtils.getUserDirectoryPath() + File.separator + SERVER_DIR);
        FileUtils.forceMkdir(serverHomeFile);

        // 在操作系统中设置 MinIO 的环境变量.
        this.setMcEnvPath();

        // 给 MinIO 添加别名和初始化创建桶.
        this.checkAndAddHostConfig();
        this.checkAndInitBucket();
    }

    /**
     * 设置 MinIO Client 的环境变量.
     */
    private void setMcEnvPath() throws IOException {
        // 执行 mc -v 查看是否已经安装配置了 mc 的环境变量，如果有就直接返回.
        try {
            if (StringUtils.isNotBlank(CmdKit.exec(osEnum.getMc() + V))) {
                this.mcPath = osEnum.getMc();
                log.info("检测到了 mc 环境变量，将跳过安装 MinIO Client.");
                return;
            }
        } catch (Exception expected) {
            log.warn("检测到没有将 'mc' 加入到操作系统环境变量中，将安装和使用本服务提供的 MinIO Client.");
        }

        // 如果系统没有安装配置 mc 的环境变量，就复制 MinIO Client 到本服务的专属目录中.
        this.mcPath = serverHomeFile.getAbsolutePath() + File.separator + osEnum.getMc();
        File mcFile = new File(this.mcPath);
        if (mcFile.exists()) {
            log.debug("已经安装过了 MinIO Client，将跳过安装配置.");
            return;
        }

        // 将 resources 中对应系统的 mc 复制到用户目录的 `.javadoc-server` 目录下
        FileUtils.copyInputStreamToFile(new DefaultResourceLoader()
                .getResource(osEnum.getSourcePath()).getInputStream(), mcFile);
    }

    /**
     * 添加某个 MinIO 服务的别名配置.
     * <p>mc 命令如：`mc config host --json add {alias} {endpoint} {accessKey} {secretKey}`.</p>
     */
    private void checkAndAddHostConfig() {
        String alias = systemConfig.getAlias();
        String endpoint = systemConfig.getEndpoint();
        String accessKey = systemConfig.getAccessKey();
        String secretKey = systemConfig.getSecretKey();

        // 获取所有的 config, 判断该 alias 及对应的 endpoint 是否存在.
        String configs = CmdKit.exec(StringKit.format("{} config host --json list", this.mcPath));
        if (StringUtils.isBlank(configs)) {
            throw new RunException("获取 MinIO 的别名配置出错！");
        }

        // 将 configs 的 json数据做一定的处理，然后解析出来.
        JSONArray jsonArray = this.handleJsons2JsonArray(configs);

        // 遍历判断该 alias 及对应的 endpoint 是否存在
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (alias.equals(jsonObject.get("alias")) && endpoint.equals(jsonObject.get("URL"))
                    && accessKey.equals(jsonObject.get("accessKey")) && secretKey.equals(jsonObject.get("secretKey"))) {
                log.info("该【{}】别名和对应的【{}】Endpoint 已经存在，将不再创建此别名配置.", alias, endpoint);
                return;
            }
        }

        // 由于没有找到此别名对应的 endpoiint，则创建此别名对应的 endpoint.
        log.info("正在创建别名【{}】和Endpoint【{}】的 host 配置...", alias, endpoint);
        String result = CmdKit.exec(StringKit.format("{} config host --json add {} {} {} {}",
                this.mcPath, alias, endpoint, systemConfig.getAccessKey(), systemConfig.getSecretKey()));
        if (this.isMcSuccess(result)) {
            log.info("创建 MinIO 的别名配置成功，alias为:【{}】, host为:【{}】.", alias, endpoint);
        } else {
            log.error("创建 MinIO 的别名配置失败，alias为:【{}】, host为:【{}】.", alias, endpoint);
        }
    }

    /**
     * 将多个平行的 json 字符串处理成 JSONArray.
     *
     * @param jsons 多个平行的 json 字符串
     * @return JSONArray
     */
    private JSONArray handleJsons2JsonArray(String jsons) {
        if (StringUtils.isBlank(jsons)) {
            return new JSONArray();
        }

        jsons = jsons.replace("}", "},");
        jsons = "[" + jsons.substring(0, jsons.length() - 1) + "]";
        JSONArray jsonArray = JSON.parseArray(jsons);
        if (jsonArray == null) {
            throw new RunException("解析获取到的 MinIO 的别名配置出错！");
        }
        return jsonArray;
    }

    /**
     * 检查或者初始化 MinIO 中 bucket，如果桶不存在就新建一个.
     */
    private void checkAndInitBucket() {
        String alias = systemConfig.getAlias();
        String bucket = systemConfig.getBucket();

        // 列出该仓库下所有的桶，判断是否存在该桶，不存在就创建一个.
        JSONArray jsonArray = this.handleJsons2JsonArray(
                CmdKit.exec(StringKit.format("{} ls --json {}", this.mcPath, alias)));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if ((bucket + "/").equals(jsonObject.get("key"))) {
                log.info("该别名【{}】下的桶【{}】已经存在了，将不再创建新的桶.", alias, bucket);
                return;
            }
        }

        // 不存在此桶就创建新的桶.
        String cmd = StringKit.format("{} mb --json {}/{}", this.mcPath, alias, bucket);
        if (this.isMcSuccess(CmdKit.exec(cmd))) {
            log.info("创建别名【{}】下的桶【{}】成功.", alias, bucket);
        } else {
            log.error("创建别名【{}】下的桶【{}】失败!", alias, bucket);
        }
    }

    /**
     * 根据操作的 json 字符串结果，判断此操作是否成功.
     *
     * @param json json字符串
     * @return 布尔值
     */
    private boolean isMcSuccess(String json) {
        return Const.SUCCESS.equalsIgnoreCase((String) JSON.parseObject(json).get("status"));
    }

    /**
     * 递归上传解压后的 jar 静态资源文件到 MinIO 中.
     *
     * @param dir 解压后的 jar 文件目录
     */
    void uploadJardocFiles(String dir) {
        CmdKit.exec(StringKit.format("{} cp --recursive {} {}/{}", this.mcPath, dir,
                systemConfig.getAlias(), systemConfig.getBucket()));
        log.info("上传 javadoc 的静态资源文件到 MinIO 成功.");
    }

}
