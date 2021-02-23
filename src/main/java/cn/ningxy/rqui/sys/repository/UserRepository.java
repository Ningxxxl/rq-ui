package cn.ningxy.rqui.sys.repository;

import cn.ningxy.rqui.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import cn.ningxy.rqui.sys.entity.UserExample;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserDao继承基类
 */
@Mapper
@Repository
public interface UserRepository extends MyBatisBaseDao<User, Integer, UserExample> {
    User selectByUserName(String username);

    User selectByPhone(String phone);

    @Select("SELECT university FROM `buidc`.`user` WHERE university != '' GROUP BY university;")
    List<String> selectUserUniversities();
}