package com.blinkfox.javadoc.kits;

import org.junit.Assert;
import org.junit.Test;

/**
 * StringKitTest.
 *
 * @author blinkfox on 2019-05-23.
 */
public class StringKitTest {

    /**
     * 测试生成UUID.
     */
    @Test
    public void getUuid() {
        Assert.assertEquals(32, StringKit.getUuid().length());
    }

    /**
     * 测试字符串格式化方法.
     */
    @Test
    public void format() {
        Assert.assertEquals("", StringKit.format(null));
        Assert.assertEquals("aaa", StringKit.format("aaa"));
        Assert.assertEquals("", StringKit.format(null, ""));
        Assert.assertEquals("Hello 张三, I'm 李四.", StringKit.format("Hello {}, I'm {}.", "张三", "李四"));
    }

}