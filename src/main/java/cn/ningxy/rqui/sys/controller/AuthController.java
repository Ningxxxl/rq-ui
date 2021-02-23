package cn.ningxy.rqui.sys.controller;


import cn.ningxy.rqui.sys.constant.AuthConstants;
import cn.ningxy.rqui.sys.dto.JwtUser;
import cn.ningxy.rqui.sys.entity.User;
import cn.ningxy.rqui.sys.service.AuthService;
import cn.ningxy.rqui.sys.service.SmsService;
import cn.ningxy.rqui.sys.util.StringGenerator;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证模块
 *
 * @author ningxy
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {


    /**
     * 登录方式：验证码登录
     */
    private static final String LOGIN_TYPE_CAPTCHA = "captcha";

    @Autowired
    AuthService authService;

    @Autowired
    SmsService smsService;

    /**
     * 发送登录验证码
     *
     * @param account 手机号
     */
    @RequestMapping(method = RequestMethod.GET, path = "/login/captcha/{account}")
    public void sendLoginCaptcha(@PathVariable("account") String account) {
        String captcha = authService.generateCaptcha(account, AuthConstants.REDIS_PREFIX_LOGIN);
        // TODO: 2021/1/22 暂时先打印输出，之后需要去掉
        log.info(String.format("=== 手机号【%s】的验证码为：【%s】\n", account, captcha));
        smsService.sendMessage(account, captcha);
    }

    // FIXME: 2021/1/18 参数校验未加入

    /**
     * 使用密码登录
     *
     * @param jsonObject 登录信息
     * @return 登录结果
     */
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public JSONObject loginWithPassword(@RequestBody JSONObject jsonObject) {

        final String accountInput = jsonObject.getString("account");
        final String passwordInput = jsonObject.getString("password");
        final boolean isRemember = jsonObject.getBooleanValue("isRemember");

        JwtUser jwtUser = authService.loginWithPassword(accountInput, passwordInput, isRemember);
        return generateLoginResult(jwtUser);
    }

    /**
     * 使用验证码登录
     *
     * @param jsonObject 登录信息
     * @return 登录结果
     */
    @RequestMapping(method = RequestMethod.POST, path = "/login", params = "type=" + LOGIN_TYPE_CAPTCHA)
    public JSONObject loginWithCaptcha(@RequestBody JSONObject jsonObject) {
        final String accountInput = jsonObject.getString("account");
        final String captchaInput = jsonObject.getString("captcha");
        final boolean isRemember = jsonObject.getBooleanValue("isRemember");

        JwtUser jwtUser = authService.loginWithCaptcha(accountInput, captchaInput, isRemember);
        return generateLoginResult(jwtUser);
    }

    /**
     * 生成登录成功返回的 JsonObject
     *
     * @param jwtUser JwtUser
     * @return
     */
    private JSONObject generateLoginResult(JwtUser jwtUser) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", AuthConstants.TOKEN_PREFIX + jwtUser.getToken());
        jsonObject.put("user", jwtUser.getUserVO());
        return jsonObject;
    }

    /**
     * 注册
     *
     * @param jsonObject 用户信息
     */
    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public void register(@RequestBody JSONObject jsonObject) {
        final String phoneInput = jsonObject.getString("phone");
        final String passwordInput = jsonObject.getString("password");
        final String passwordRepeat = jsonObject.getString("repeat");
        final String captchaInput = jsonObject.getString("captcha");

        if (!StringUtils.equals(passwordInput, passwordRepeat)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        final String username = StringGenerator.randomUsername();
        User user = User.builder()
                .userName(username)
                .phone(phoneInput)
                .pwd(passwordInput)
                .build();

        authService.register(user, captchaInput);
    }

    // TODO: 2021/1/18 考虑加入验证码发送频次限制

    // TODO: 2021/1/22 常量急需优化，现在太分散了！

    /**
     * 发送注册验证码
     *
     * @param phone 手机号
     */
    @RequestMapping(method = RequestMethod.GET, path = "/register/captcha/{phone}")
    public void sendRegisterCaptcha(@PathVariable("phone") String phone) {
        String captcha = authService.generateCaptcha(phone, AuthConstants.REDIS_PREFIX_REGISTER);
        // TODO: 2021/1/18 暂时先打印输出，之后需要去掉，接入SMS服务
        log.info(String.format("=== 手机号【%s】的验证码为：【%s】\n", phone, captcha));
        smsService.sendMessage(phone, captcha);
    }
}
