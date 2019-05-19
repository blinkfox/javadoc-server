package com.blinkfox.javadoc;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 应用服务主入口的单元测试类.
 *
 * @author blinkfox on 2019-05-20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JavadocServerApplicationTests {

    /**
     * 一个恒为真的测试方法，用来测试 SpringBoot 服务能否正常启动.
     */
    @Test
    public void contextLoads() {
        Assert.assertTrue(new Random().nextInt(2) < 3);
    }

}
