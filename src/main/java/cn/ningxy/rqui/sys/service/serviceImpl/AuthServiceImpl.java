package cn.ningxy.rqui.sys.service.serviceImpl;


import cn.ningxy.rqui.sys.dto.JwtUser;
import cn.ningxy.rqui.sys.dto.UserVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import cn.ningxy.rqui.sys.constant.AuthConstants;
import cn.ningxy.rqui.sys.entity.User;
import cn.ningxy.rqui.sys.enums.UserRoleEnum;
import cn.ningxy.rqui.sys.service.AuthService;
import cn.ningxy.rqui.sys.service.UserService;
import cn.ningxy.rqui.sys.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author ningxy
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Redis Key 前缀
     */
    public static final String REDIS_KEY_PREFIX = "captcha:";
    /**
     * 验证码长度
     */
    private static final int CAPTCHA_LENGTH = 4;
    /**
     * 验证码有效期（秒）
     */
    private static final long CAPTCHA_EXPIRE = 60 * 5L;

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public JwtUser loginWithPassword(String accountInput, String passwordInput, boolean isRemember) {
        User user = userService.loadUserByUsernameOrPhone(accountInput);
        boolean isPasswordMatches = validatePassword(passwordInput, user.getPassword());
        if (!isPasswordMatches) {
            throw new BadCredentialsException("Invalid account or password.");
        }
        return setAuthenticationInfo(user, isRemember);
    }

    private JwtUser setAuthenticationInfo(User user, boolean isRemember) {
        final String token = JwtUtils.generateToken(user.getUsername(), user.getRoleId(), isRemember);
        Authentication authentication = JwtUtils.generateAuthentication(
                user.getUsername(),
                token,
                UserRoleEnum.getNameByCode(user.getRoleId())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new JwtUser(UserVO.fromEntity(user), token);
    }

    @Override
    public JwtUser loginWithCaptcha(String accountInput, String captchaInput, boolean isRemember) {
        boolean verifyResult = verifyCaptcha(accountInput, captchaInput, AuthConstants.REDIS_PREFIX_LOGIN, true);
        if (!verifyResult) {
            throw new RuntimeException("验证码无效");
        }
        User user = userService.loadUserByUsernameOrPhone(accountInput);
        return setAuthenticationInfo(user, isRemember);
    }

    @Override
    public boolean register(User user, String captcha) {
        final String username = user.getUsername();
        final String phoneNumber = user.getPhone();
//        if (!verifyCaptcha(phoneNumber, captcha, AuthConstants.REDIS_PREFIX_REGISTER, true)) {
//            throw new RuntimeException("验证码有误。");
//        } else
        if (userService.isUsernameExist(username) || userService.isPhoneExist(phoneNumber)) {
            throw new RuntimeException("用户已存在。");
        }
        return userService.signUp(user);
    }

    @Override
    public String generateCaptcha(String keyInput, String customPrefix) {
        customPrefix = (customPrefix == null) ? "" : customPrefix;
        final String key = REDIS_KEY_PREFIX + customPrefix + keyInput;
        final String captcha = RandomStringUtils.randomNumeric(CAPTCHA_LENGTH);
        stringRedisTemplate.opsForValue().set(key, captcha, CAPTCHA_EXPIRE, TimeUnit.SECONDS);
        return captcha;
    }

    /**
     * 核验验证码有效性
     *
     * @param keyInput     不含前缀的 key
     * @param captchaInput 用户输入的验证码
     * @param customPrefix 自定义 Redis key 前缀
     * @param destroy      验证成功之后是否删除 key
     * @return 核验结果
     */
    public boolean verifyCaptcha(String keyInput, String captchaInput, String customPrefix, boolean destroy) {
        customPrefix = (customPrefix == null) ? "" : customPrefix;
        String key = REDIS_KEY_PREFIX + customPrefix + keyInput;
        String captcha = stringRedisTemplate.opsForValue().get(key);
        boolean res = !StringUtils.isEmpty(captcha) && StringUtils.equals(captcha, captchaInput);
        if (res && destroy) {
            stringRedisTemplate.delete(key);
        }
        return res;
    }

    /**
     * 验证密码
     *
     * @param rawPassword     password1
     * @param encodedPassword password2
     * @return 验证结果
     */
    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
