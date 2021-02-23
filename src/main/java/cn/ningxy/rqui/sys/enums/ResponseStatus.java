package cn.ningxy.rqui.sys.enums;

/**
 * Response 的状态码定义
 *
 * @author ningxy
 */
public enum ResponseStatus {

    // SUCCESS
    SUCCESS("OK", "SUCCESS"),
    // ERROR 默认的错误状态
    ERROR("ERROR", "似乎出现了小问题。"),
    // BadRequest
    BAD_REQUEST("BadRequest", "无效的请求。"),
    // NotFound
    NOT_FOUND("NotFound", "路径有误或资源未找到。"),
    // InternalServerError
    INTERNAL_SERVER_ERROR("InternalServerError", "服务器炸了。"),

    // 用户名或密码错误
    BAD_CREDENTIALS("BadCredentials", "用户名或密码错误。"),
    // 未经认证
    UNAUTHORIZED("Unauthorized", "未经认证"),
    // 访问权限不足
    ACCESS_DENIED("AccessDenied", "访问权限不足。"),
    // TOKEN 验证错误或失效
    INVALID_TOKEN("InvalidToken", "令牌无效。"),
    ;

    /**
     * 错误代码
     */
    private final String code;

    /**
     * 错误说明
     */
    private final String message;

    ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
