package cn.ningxy.rqui.sys.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author ningxy
 */
@UtilityClass
public class StringGenerator {
    /**
     * 生成用户名前缀
     */
    private static final String USERNAME_GEN_PREFIX = "UID_";
    /**
     * 生成用户名长度
     */
    private static final int USERNAME_GEN_LENGTH = 16;

    private static final int PASSWORD_GEN_LENGTH = 12;

    /**
     * 生成随机用户名
     *
     * @return 生成的随机用户名
     */
    public static String randomUsername() {
        return USERNAME_GEN_PREFIX + RandomStringUtils.randomAlphanumeric(USERNAME_GEN_LENGTH).toLowerCase();
    }

    /**
     * 生成随机密码
     *
     * @return 生成的随机密码
     */
    public static String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(PASSWORD_GEN_LENGTH);
    }
}
