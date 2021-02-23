package cn.ningxy.rqui.sys.enums;

/**
 * 性别 Enum
 *
 * @author ningxy
 */

public enum GenderEnum implements BaseCodeMessageEnum {
    // 未知
    UNKNOWN((byte) 0, "未知"),
    // 男性
    MALE((byte) 1, "男"),
    // 女性
    FEMALE((byte) 2, "女");

    private final byte code;
    private final String message;

    GenderEnum(byte code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
