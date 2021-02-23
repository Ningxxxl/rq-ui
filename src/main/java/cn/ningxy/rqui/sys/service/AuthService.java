package cn.ningxy.rqui.sys.service;


import cn.ningxy.rqui.sys.dto.JwtUser;
import cn.ningxy.rqui.sys.entity.User;

/**
 * @author ningxy
 */
public interface AuthService {
    /**
     * 使用密码登录
     * <p>
     * 返回结果中包含生成的 JWT
     *
     * @param accountInput  输入的账号（用户名/手机号）
     * @param passwordInput 输入的密码
     * @param isRemember    是否"记住我"
     * @return JwtUser
     */
    JwtUser loginWithPassword(String accountInput, String passwordInput, boolean isRemember);

    /**
     * 使用验证码登录
     * <p>
     * 返回结果中包含生成的 JWT
     *
     * @param accountInput 输入的账号（手机号）
     * @param captchaInput 输入的密码
     * @param isRemember   是否"记住我"
     * @return JwtUser
     */
    JwtUser loginWithCaptcha(String accountInput, String captchaInput, boolean isRemember);

    /**
     * 用户注册
     *
     * @param user    User
     * @param captcha 验证码
     * @return 注册结果
     */
    boolean register(User user, String captcha);

    /**
     * 生成验证码，保存在 Redis 中
     *
     * @param keyInput     不含前缀的 key
     * @param customPrefix 自定义前缀
     * @return 生成的验证码
     */
    String generateCaptcha(String keyInput, String customPrefix);
}
