package com.blinkfox.javadoc.kits;

import com.blinkfox.javadoc.exception.RunException;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

/**
 * AssertKitTest.
 *
 * @author chenjiayin on 2019-3-26.
 */
@Slf4j
public class AssertKitTest {

    private static final String NULL = null;

    private static final String EMPTY = "";

    private static final String HELLO = "hello";

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isNull() {
        AssertKit.isNull(HELLO);
        AssertKit.isNull(NULL);
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isNullWithMessage() {
        AssertKit.isNull(HELLO, "这个对象不为空，将不会抛异常！");
        AssertKit.isNull(NULL, "这个对象是空的！");
    }

    /**
     * 测试断言有 Message 占位符的情况.
     */
    @Test(expected = RunException.class)
    public void isNullWithPatternMessage() {
        AssertKit.isNull(HELLO, "这个对象不为空，将不会抛异常！");
        AssertKit.isNull(NULL, "这个{}是空的！", "测试对象");
    }

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isEmptyWithNull() {
        AssertKit.isEmpty(HELLO);
        AssertKit.isEmpty(NULL);
    }

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isEmpty() {
        AssertKit.isEmpty(HELLO);
        AssertKit.isEmpty(EMPTY);
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isEmptyWithMessage() {
        AssertKit.isEmpty(HELLO);
        AssertKit.isEmpty(EMPTY, "这个字符串无内容");
    }

    /**
     * 测试断言 Message 有占位符的情况.
     */
    @Test(expected = RunException.class)
    public void isEmptyWithPatternMessage() {
        AssertKit.isEmpty(HELLO);
        AssertKit.isEmpty(EMPTY, "这个{}无内容", "测试的空字符串");
    }

    /**
     * 随机生成一个满足条件的布尔结果.
     *
     * @return 布尔值
     */
    private boolean randomCondition() {
        return HELLO.length() > String.valueOf(new Random().nextInt(9)).length();
    }

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isTrue() {
        AssertKit.isTrue(false);
        AssertKit.isTrue(this.randomCondition());
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isTrueWithMessage() {
        AssertKit.isTrue(false, "这个条件不满足");
        AssertKit.isTrue(this.randomCondition(), "这个条件是对的！");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isTrueWithPatternMessage() {
        AssertKit.isTrue(false, "这个条件不满足");
        AssertKit.isTrue(this.randomCondition(), "这个{}是对的！", "随机条件");
    }

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isFalse() {
        AssertKit.isFalse(true);
        AssertKit.isFalse(!this.randomCondition());
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isFalseWithMessage() {
        AssertKit.isFalse(true, "这个条件不满足");
        AssertKit.isFalse(!this.randomCondition(), "这个条件是错的！");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isFalseWithPatternMessage() {
        AssertKit.isFalse(true, "这个条件不满足");
        AssertKit.isFalse(!this.randomCondition(), "这个{}是错的！", "随机条件");
    }

    /**
     * 测试断言无 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isZero() {
        AssertKit.isZero(5);
        AssertKit.isZero(0);
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isZeroWithMessage() {
        AssertKit.isZero(3, "这个值不满足");
        AssertKit.isZero(0, "这个值是错的！");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isZeroWithPatternMessage() {
        AssertKit.isZero(2, "这个值不是0");
        AssertKit.isZero(0, "这个{}是0！", "值");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isException() {
        int a = 0;
        AssertKit.isException(() -> log.info("变量a的值为: {}.", a), "测试无异常的情况.");
        AssertKit.isException(() -> {
            System.out.println("执行了, 得到的a是:" + a);
            throw new RuntimeException("这是手动抛的异常!");
        }, "执行出错了！");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isExceptionWithPattern() {
        AssertKit.isException(() -> {
            log.info("该pattern执行了");
            throw new RuntimeException("这是手动抛的异常!");
        }, "该{}执行出错了！", "Pattern测试方法");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isRuntimeException() {
        int b = 0;
        AssertKit.isRuntimeException(() -> log.info("变量b的值为: {}.", b), "测试没有异常的情况.");
        AssertKit.isRuntimeException(() -> {
            System.out.println("执行了, 得到的b是:" + b);
            throw new RuntimeException("这是手动抛的运行时异常!");
        }, "所执行的代码块发生了运行时异常！");
    }

    /**
     * 测试断言有 Message 的情况.
     */
    @Test(expected = RunException.class)
    public void isRuntimeExceptionWithPattern() {
        AssertKit.isRuntimeException(() -> {
            throw new RuntimeException("这是手动抛的运行时异常!");
        }, "所执行的{}发生了运行时异常！", "代码块");
    }

}