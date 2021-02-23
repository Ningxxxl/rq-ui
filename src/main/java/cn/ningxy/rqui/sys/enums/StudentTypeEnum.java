package cn.ningxy.rqui.sys.enums;

/**
 * @author blackdress
 */

public enum StudentTypeEnum implements BaseCodeMessageEnum {

    //未知
    UNKNOWN((byte) 0, "未分类"),
    //本科生
    UNDERGRADUATE((byte) 1, "本科生"),
    //研究生
    POSTGRADUATE((byte) 2, "研究生"),
    ;

    private final byte code;
    private final String message;

    StudentTypeEnum(byte code, String message) {
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
