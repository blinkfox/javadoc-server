package com.blinkfox.javadoc.kits;

import com.blinkfox.javadoc.exception.RunException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 项目中断言用到的相关工具类.
 *
 * @author chenjiayin on 2019/3/26.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertKit {

    /**
     * 断言空对象，如果为null，则抛出 TcsException 异常.
     *
     * @param t 对象实例
     * @param <T> 范型对象
     */
    public static <T> void isNull(T t) {
        isNull(t, "");
    }

    /**
     * 断言空对象，如果为null，则抛出带占位符 Message 的 TcsException 异常.
     * @param t 对象实例
     * @param pattern 断言成功时的消息，含占位符
     * @param objs 值
     * @param <T> 范型对象
     */
    public static <T> void isNull(T t, String pattern, Object... objs) {
        if (t == null) {
            throw new RunException(getMessage(pattern, objs));
        }
    }

    /**
     * 断言空字符串，如果为null或者没有内容，则抛出 TcsException 异常.
     *
     * @param s 字符串对象
     */
    public static void isEmpty(String s) {
        isEmpty(s, "");
    }

    /**
     * 断言空字符串，如果为null或者没有内容，则抛出 TcsException 异常.
     * <p>如：</p>
     * @param s 字符串对象
     * @param pattern 断言成功时的消息，含占位符
     * @param objs 不定参数对象
     */
    public static void isEmpty(String s, String pattern, Object... objs) {
        isTrue(s == null || s.length() == 0, getMessage(pattern, objs));
    }

    /**
     * 断言是否为 true，如果为 true，则抛出 TcsException 异常.
     *
     * @param b 布尔条件的值
     */
    public static void isTrue(boolean b) {
        isTrue(b, "");
    }

    /**
     * 断言是否为 true，如果为 true，则抛出带占位符 Message 的 TcsException 异常.
     *
     * @param b 布尔条件的值
     * @param pattern 含站位符的消息
     * @param objs 不定参数对象
     */
    public static void isTrue(boolean b, String pattern, Object... objs) {
        if (b) {
            throw new RunException(getMessage(pattern, objs));
        }
    }

    /**
     * 断言是否为 false，如果为 false，则抛出 TcsException 异常.
     *
     * @param b 布尔条件的值
     */
    public static void isFalse(boolean b) {
        isFalse(b, "");
    }

    /**
     * 断言是否为 false，如果为 false，则抛出带占位符的 Message 的 TcsException 异常.
     *
     * @param b 布尔条件的值
     * @param pattern 含站位符的消息
     * @param objs 不定参数对象
     */
    public static void isFalse(boolean b, String pattern, Object... objs) {
        if (!b) {
            throw new RunException(getMessage(pattern, objs));
        }
    }

    /**
     * 断言是否为0，如果为 0，则抛出 TcsException 异常.
     *
     * @param i int型的值
     */
    public static void isZero(int i) {
        isZero(i, "");
    }

    /**
     * 断言是否为 0，如果为 0，则抛出带 Message 的 TcsException 异常.
     *
     * @param i int型的值
     * @param pattern 消息
     * @param objs 不定参数
     */
    public static void isZero(int i, String pattern, Object... objs) {
        isTrue(i == 0, getMessage(pattern, objs));
    }

    /**
     * 断言是否抛出了 Exception，如果是的话，则抛出带 pattern 的 TcsException 异常.
     *
     * @param runnable runnable
     * @param pattern 含占位符的消息字符串
     * @param objs 不定参数
     */
    public static void isException(Runnable runnable, String pattern, Object... objs) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RunException(getMessage(pattern, objs), e);
        }
    }

    /**
     * 断言是否抛出了 RuntimeException，如果是的话，则抛出带 pattern 的 TcsException 异常.
     *
     * @param runnable runnable
     * @param pattern 含占位符的消息字符串
     * @param objs 不定参数
     */
    public static void isRuntimeException(Runnable runnable, String pattern, Object... objs) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            throw new RunException(getMessage(pattern, objs), e);
        }
    }

    /**
     * 解析获取含占位符字符串结果字符串消息.
     *
     * @param pattern 含`{}`占位符的字符串
     * @param objs 不定参数的对象
     * @return 字符串消息
     */
    private static String getMessage(String pattern, Object... objs) {
        return objs.length == 0 ? pattern : StringKit.format(pattern, objs);
    }

}
