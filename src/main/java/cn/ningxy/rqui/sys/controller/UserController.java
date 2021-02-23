package cn.ningxy.rqui.sys.controller;


import cn.ningxy.rqui.sys.dto.PageVO;
import cn.ningxy.rqui.sys.dto.UserVO;
import cn.ningxy.rqui.sys.entity.User;
import cn.ningxy.rqui.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import cn.ningxy.rqui.sys.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 用户模块
 *
 * @author ningxy
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return UserVO
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{username}/profile")
    public UserVO selectUserByUsername(@PathVariable(name = "username") String username) {
        try {
            User user = (User) userService.loadUserByUsername(username);
            if (isNotRoleAdmin()) {
                isOperateMyself(username);
            }
            return UserVO.fromEntity(user);
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    /**
     * 查询所有用户（分页）（可按条件筛选）
     *
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @param university 筛选条件-学校（部门）
     * @param realName   筛选条件-真实姓名
     * @param type       筛选条件-学生类型
     * @param role       筛选条件-用户角色
     * @return List<UserVO>
     */
    @RequestMapping(method = RequestMethod.GET, path = "/list")
    public PageVO<UserVO> selectAllUsersPaged(@RequestParam("pageNum") int pageNum,
                                              @RequestParam("pageSize") int pageSize,
                                              @RequestParam(value = "university", required = false) String university,
                                              @RequestParam(value = "realName", required = false) String realName,
                                              @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "role", required = false) String role) {
        UserVO userVoFilter = UserVO.builder()
                .university(university)
                .realName(realName)
                .type(type)
                .role(role)
                .build();
        return userService.selectUsersPaged(pageNum, pageSize, userVoFilter);
    }
    // TODO: 2021/1/10 应设置管理员权限

    /**
     * 更新用户信息（普通用户）
     * <p>
     * 普通用户只能操作自己的信息，且不能自行修改权限。
     *
     * @param userVO UserVO
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{username}/profile")
    public void updateUserProfile(@RequestBody UserVO userVO) {
        if (isNotRoleAdmin()) {
            // 普通用户只能操作自己的信息
            isOperateMyself(userVO.getUserName());
            // 普通用户不能自行修改权限
            userVO.setRole(null);
        }
        if (!userService.update(userVO.toEntity())) {
            throw new RuntimeException("更新失败。");
        }
    }

    /**
     * 用户更新头像
     *
     * @param username 用户名
     * @param avatar   头像文件
     * @return 预览地址
     * @throws IOException IOException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{username}/avatar")
    public String updateAvatar(@PathVariable("username") String username,
                               @RequestParam("avatar") MultipartFile avatar) throws IOException {
        if (isNotRoleAdmin()) {
            isOperateMyself(username);
        }
        return userService.updateAvatar(username, avatar);
    }

    // TODO: 2021/1/27 更新头像仅能更新自己的内容
    // TODO: 2021/1/27 更换头像之后需要删除老头像
    // TODO: 2021/1/27 返回完整路径

    /**
     * （批量）生成评审专家账号
     *
     * @param userVOList 评审专家账号列表
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public void generateExpertAccount(@RequestBody List<UserVO> userVOList) {
        userService.generateExpertAccounts(userVOList);
    }

    /**
     * 模糊查询用户（根据姓名）
     *
     * @param queryKey 关键字
     * @return 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public List<UserVO> searchUsers(@RequestParam("key") String queryKey) {
        return userService.searchUsers(queryKey);
    }

    /**
     * 获取用户学校（单位）列表
     *
     * @return 用户学校（单位）列表
     */
    @RequestMapping(method = RequestMethod.GET, path = "/universities")
    public List<String> selectUserUniversities() {
        return userService.selectUserUniversities();
    }


    /**
     * 从 SecurityContextHolder 中获取当前会话的用户名，与输入的用户名进行匹配。
     * 不一致的话会抛出异常。
     *
     * @param username username
     */
    private void isOperateMyself(String username) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (username == null || !StringUtils.equals(principal, username)) {
            throw new RuntimeException("只能操作自己的信息。");
        }
    }

    /**
     * 从 SecurityContextHolder 中获取当前会话的用户权限，检查其是否为管理员。
     *
     * @return 是否为管理员
     */
    private boolean isNotRoleAdmin() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return !authorities.contains(UserRoleEnum.ROLE_ADMIN.authority());
    }
}
