package cn.ningxy.rqui.sys.dto;


import cn.ningxy.rqui.sys.enums.BaseCodeMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.ningxy.rqui.sys.entity.User;
import cn.ningxy.rqui.sys.enums.GenderEnum;
import cn.ningxy.rqui.sys.enums.StudentTypeEnum;
import cn.ningxy.rqui.sys.enums.UserRoleEnum;
import cn.ningxy.rqui.sys.util.BeanCopierUtils;

import java.time.LocalDate;

/**
 * UserVO
 *
 * @author ningxy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String role;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真名
     */
    private String realName;

    /**
     * 性别名称
     */
    private String gender;

    /**
     * 身份证号
     */
    private String identityNumber;

    /**
     * 学生类别(本科生/研究生)
     */
    private String type;

    /**
     * 地址
     */
    private String address;

    /**
     * 学校/单位
     */
    private String university;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像/照片路径
     */
    private String avatarSrc;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 是否锁定
     */
    private Boolean isLocked;

    public static UserVO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        // 注意 userName 大小写问题需要手动赋值
        UserVO userVO = UserVO.builder()
                .userName(user.getUsername())
                .role(UserRoleEnum.getNameByCode(user.getRoleId()))
                .gender(BaseCodeMessageEnum.getMessageByCode(user.getGender(), GenderEnum.class))
                .type(BaseCodeMessageEnum.getMessageByCode(user.getType(), StudentTypeEnum.class))
                .build();
        BeanCopierUtils.copy(user, userVO);
        return userVO;
    }

    public User toEntity() {
        // 注意 userName 大小写问题需要手动赋值
        User user = User.builder()
                .userName(userName)
                .roleId(UserRoleEnum.getCodeByName(role))
                .gender((byte) BaseCodeMessageEnum.getCodeByMessage(gender, GenderEnum.class))
                .type((byte) BaseCodeMessageEnum.getCodeByMessage(type, StudentTypeEnum.class))
                .build();
        BeanCopierUtils.copy(this, user);
        return user;
    }

}
