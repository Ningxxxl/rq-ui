package cn.ningxy.rqui.sys.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 用户角色 Enum
 *
 * @author ningxy
 */

public enum UserRoleEnum {
    // COMMON_USER
    ROLE_USER(0),
    // ADMIN
    ROLE_ADMIN(9),
    // EXPERT
    ROLE_EXPERT(10),
    ;

    private final int code;

    UserRoleEnum(final int code) {
        this.code = code;
    }

    public static String getNameByCode(int code) {
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (code == userRoleEnum.code) {
                return userRoleEnum.name();
            }
        }
        return null;
    }

    public static Integer getCodeByName(String name) {
        if (name == null) {
            return null;
        }
        return UserRoleEnum.valueOf(name).code;
    }

    public int code() {
        return code;
    }

    public SimpleGrantedAuthority authority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
