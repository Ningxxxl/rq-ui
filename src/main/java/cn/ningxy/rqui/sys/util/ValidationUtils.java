package cn.ningxy.rqui.sys.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * @author ningxy
 */
@UtilityClass
public class ValidationUtils {

    /**
     * 是否包含至少一个字母（大小写均可）
     */
    private static final String CONTAINS_ALPHABET_REGEX = ".*[a-zA-Z]+.*";
    private static final Pattern CONTAINS_ALPHABET_PATTERN = Pattern.compile(CONTAINS_ALPHABET_REGEX);

    /**
     * 是否包含至少一个字母（大小写均可）
     *
     * @param input CharSequence
     * @return boolean
     */
    public static boolean containsAlphabet(CharSequence input) {
        return CONTAINS_ALPHABET_PATTERN.matcher(input).matches();
    }
}
