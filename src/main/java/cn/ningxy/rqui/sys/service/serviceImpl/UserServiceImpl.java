package cn.ningxy.rqui.sys.service.serviceImpl;


import cn.ningxy.rqui.sys.dto.PageVO;
import cn.ningxy.rqui.sys.dto.UserVO;
import cn.ningxy.rqui.sys.enums.BaseCodeMessageEnum;
import cn.ningxy.rqui.sys.enums.StudentTypeEnum;
import cn.ningxy.rqui.sys.service.UserService;
import cn.ningxy.rqui.sys.util.StringGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import cn.ningxy.rqui.sys.entity.User;
import cn.ningxy.rqui.sys.entity.UserExample;
import cn.ningxy.rqui.sys.enums.MimeTypeEnum;
import cn.ningxy.rqui.sys.enums.UserRoleEnum;
import cn.ningxy.rqui.sys.repository.UserRepository;
import cn.ningxy.rqui.sys.util.FileTypeUtils;
import cn.ningxy.rqui.sys.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ningxy
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String BUCKET_AVATAR = "/avatar";
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Value("${custom.server.preview-path}")
    private String previewPath;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean insertUser(User user) {
        return userRepository.insertSelective(user) == 1;
    }

    @Override
    public boolean signUp(User user) {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        return insertUser(user);
    }

    @Override
    public boolean resetPassword(User user, String newPassword) {
        user.setPwd(passwordEncoder.encode(newPassword));
        return update(user);
    }

    @Override
    public boolean update(User user) {
        user.setId(null);
        user.setUpdateTime(null);
        UserExample userExample = new UserExample();
        userExample.or().andUserNameEqualTo(user.getUsername());
        return userRepository.updateByExampleSelective(user, userExample) >= 0;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User result = userRepository.selectByUserName(s);
        return Optional
                .ofNullable(result)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("User with uname {0} cannot be found.", s)
                ));
    }

    @Override
    public User loadUserByUsernameOrPhone(String s) throws UsernameNotFoundException {

        User result;
        if (ValidationUtils.containsAlphabet(s)) {
            result = userRepository.selectByUserName(s);
        } else {
            result = userRepository.selectByPhone(s);
        }
        return Optional
                .ofNullable(result)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("User with phone {0} cannot be found.", s)
                ));
    }

    @Override
    public List<User> selectAllUsers() {
        UserExample userExample = new UserExample();
        userExample.or().andIsEnabledIsNotNull();
        return userRepository.selectByExample(userExample);
    }

    @Override
    public PageVO<UserVO> selectAllUsersPaged(int pageNum, int pageSize) {
        return selectUsersPaged(pageNum, pageSize, new UserVO());
    }

    @Override
    public PageVO<UserVO> selectUsersPaged(int pageNum, int pageSize, UserVO userVoFilter) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (StringUtils.isNotBlank(userVoFilter.getUniversity())) {
            criteria.andUniversityEqualTo(userVoFilter.getUniversity().trim());
        }
        if (StringUtils.isNotBlank(userVoFilter.getType())) {
            byte typeCode = (byte) BaseCodeMessageEnum.getCodeByMessage(userVoFilter.getType(), StudentTypeEnum.class);
            criteria.andTypeEqualTo(typeCode);
        }
        if (StringUtils.isNotBlank(userVoFilter.getRealName())) {
            criteria.andRealNameLike(userVoFilter.getRealName());
        }
        userExample.or(criteria);
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userRepository.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        List<UserVO> userVOList = userList.stream()
                .map(UserVO::fromEntity)
                .collect(Collectors.toList());
        return PageVO.from(pageInfo, userVOList);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.selectByUserName(username) != null;
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return userRepository.selectByPhone(phone) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(String username, MultipartFile file) throws IOException {
        String fileExtension = FileTypeUtils.getFileExtension(file.getOriginalFilename());
        String avatarName = RandomStringUtils.randomAlphanumeric(20) + '.' + fileExtension;
        User user = User.builder().userName(username).avatarSrc(avatarName).build();
        update(user);
        List<String> allowedFileTypes = MimeTypeEnum.getTypeList(
                MimeTypeEnum.JPG,
                MimeTypeEnum.JPEG,
                MimeTypeEnum.PNG
        );
        FileStorageService.saveFile(BUCKET_AVATAR, avatarName, file, allowedFileTypes);
        return previewPath + Paths.get(BUCKET_AVATAR, avatarName).toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateExpertAccounts(List<UserVO> userVOList) {
        List<User> userList = userVOList.stream()
                .map(userVO -> {
                    User user = userVO.toEntity();
                    user.setId(null);
                    user.setRoleId(UserRoleEnum.ROLE_EXPERT.code());
                    user.setUserName(StringGenerator.randomUsername());
                    user.setPwd(passwordEncoder.encode(StringGenerator.randomPassword()));
                    return user;
                }).collect(Collectors.toList());
        insertUserBatch(userList);
    }

    @Override
    public List<UserVO> searchUsers(String queryKey) {
        UserExample userExample = new UserExample();
        userExample.or().andRealNameLike('%' + queryKey + '%');
        List<User> userList = userRepository.selectByExample(userExample);
        return userList.stream().map(UserVO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<String> selectUserUniversities() {
        return userRepository.selectUserUniversities();
    }

    public void insertUserBatch(List<User> userList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);) {
            int batchSize = 200;
            for (int i = 0; i < userList.size(); i++) {
                userRepository.insertSelective(userList.get(i));
                if (i > 0 && i % batchSize == 0) {
                    sqlSession.flushStatements();
                }
            }
            sqlSession.flushStatements();
        }
    }
}
