package cn.ningxy.rqui.sys.enums;

/**
 * @author ningxy
 */
public interface BaseCodeMessageEnum {

    /**
     * code 默认值
     */
    int DEFAULT_CODE = 0;

    /**
     * 获取 code 对应的 message
     *
     * @param code   code
     * @param tClass 枚举类的 class
     * @param <T>    枚举类
     * @return message
     */
    static <T extends BaseCodeMessageEnum> String getMessageByCode(int code, Class<T> tClass) {
        T[] enumConstants = tClass.getEnumConstants();
        for (T t : enumConstants) {
            if (code == t.code()) {
                return t.message();
            }
        }
        return null;
    }

    /**
     * 获取 message 对应的 code
     *
     * @param message message
     * @param tClass  枚举类的 class
     * @param <T>     枚举类
     * @return code
     */
    static <T extends BaseCodeMessageEnum> int getCodeByMessage(String message, Class<T> tClass) {
        if (message == null) {
            return DEFAULT_CODE;
        }
        T[] enumConstants = tClass.getEnumConstants();
        for (T t : enumConstants) {
            if (message.equals(t.message())) {
                return t.code();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 获取 code
     *
     * @return code
     */
    int code();

    /**
     * 获取 message
     *
     * @return message
     */
    String message();
}
