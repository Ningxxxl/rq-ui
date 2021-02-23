package cn.ningxy.rqui.sys.service;


import cn.ningxy.rqui.sys.dto.PageVO;
import cn.ningxy.rqui.sys.dto.UserVO;
import cn.ningxy.rqui.sys.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ningxy
 */
public interface UserService extends UserDetailsService {
    /**
     * 增加用户（不处理密码）
     *
     * @param user user
     * @return 结果
     */
    boolean insertUser(User user);

    /**
     * 注册用户（加密密码）
     *
     * @param user user
     * @return 结果
     */
    boolean signUp(User user);

    /**
     * 重置密码
     *
     * @param user        user
     * @param newPassword 新密码
     * @return 结果
     */
    boolean resetPassword(User user, String newPassword);

    /**
     * 更新用户信息
     * <p>
     * 禁止修改 userId
     *
     * @param user user
     * @return 结果
     */
    boolean update(User user);

    User loadUserByUsernameOrPhone(String phone);

    List<User> selectAllUsers();

    PageVO<UserVO> selectAllUsersPaged(int pageNum, int pageSize);

    PageVO<UserVO> selectUsersPaged(int pageNum, int pageSize, UserVO userVoFilter);

    boolean isUsernameExist(String username);

    boolean isPhoneExist(String phone);

    String updateAvatar(String username, MultipartFile file) throws IOException;

    void generateExpertAccounts(List<UserVO> userVOList);

    List<UserVO> searchUsers(String queryKey);

    List<String> selectUserUniversities();
}
