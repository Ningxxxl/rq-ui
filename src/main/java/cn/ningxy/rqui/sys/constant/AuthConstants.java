package cn.ningxy.rqui.sys.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityConstants
 *
 * @author star
 **/
@Configuration
public class AuthConstants {

    /**
     * 注册验证码 Redis key 前缀
     */
    public static final String REDIS_PREFIX_REGISTER = "register:";
    /**
     * 注册验证码 Redis key 前缀
     */
    public static final String REDIS_PREFIX_LOGIN = "login:";
    /**
     * 用于登录的 url
     */
    public static final String AUTH_LOGIN_URL = "/api/auth/login";
    /**
     * 一般是在请求头里加入 Authorization，并加上 Bearer 标注
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Authorization 请求头
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_ROLE_CLAIM = "role";
    public static final String TOKEN_ISSUER = "security";
    public static final String TOKEN_AUDIENCE = "security-all";
    /**
     * JWT签名密钥，这里使用 HS512 算法的签名密钥
     * <p>
     * 注意：最好使用环境变量或 .properties 文件的方式将密钥传入程序
     * 密钥生成地址：http://www.allkeysgenerator.com/
     */
    public static String JWT_SECRET_KEY;
    /**
     * 当 Remember 是 false 时，token 有效时间 2 分钟
     * <p>
     * 实际值从 properties 文件中获取
     * </p>
     */
    public static long EXPIRATION_TIME;

    /**
     * 当 Remember 是 true 时，token 有效时间 5 分钟
     * <p>
     * 实际值从 properties 文件中获取
     * </p>
     */
    public static long EXPIRATION_REMEMBER_TIME;

    @Value("${custom.auth.jwt-security-key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        JWT_SECRET_KEY = jwtSecretKey;
    }

    @Value("#{${custom.auth.expiration-time}}")
    public void setExpirationTime(long expirationTime) {
        EXPIRATION_TIME = expirationTime;
    }

    @Value("#{${custom.auth.expiration-remember-time}}")
    public void setExpirationRememberTime(long expirationRememberTime) {
        EXPIRATION_REMEMBER_TIME = expirationRememberTime;
    }
}
