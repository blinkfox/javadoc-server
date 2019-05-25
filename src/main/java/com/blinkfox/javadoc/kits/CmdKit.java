package com.blinkfox.javadoc.kits;

import com.blinkfox.javadoc.exception.RunException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.io.IOUtils;

/**
 * 命令行操作的工具类.
 *
 * @author blinkfox on 2019-05-25.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CmdKit {

    /**
     * 执行对应的命令行.
     *
     * @param cmd 待执行的命令字符串
     */
    public static String exec(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            return IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RunException("执行命令失败！该命令为:【{" + cmd + "}】.", e);
        }
    }

}
