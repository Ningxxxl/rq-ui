package cn.ningxy.rqui.sys.repository;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * DAO公共基类，由MybatisGenerator自动生成请勿修改
 *
 * @param <Model> The Model Class 这里是泛型不是Model类
 * @param <PK>    The Primary Key Class 如果是无主键，则可以用Model来跳过，如果是多主键则是Key类
 * @param <E>     The Example Class
 */
public interface MyBatisBaseDao<Model, PK extends Serializable, E> {
    /**
     * @mbg.generated
     */
    long countByExample(E example);

    /**
     * @mbg.generated
     */
    int deleteByExample(E example);

    /**
     * @mbg.generated
     */
    int deleteByPrimaryKey(PK id);

    /**
     * @mbg.generated
     */
    int insert(Model record);

    /**
     * @mbg.generated
     */
    int insertSelective(Model record);

    /**
     * @mbg.generated
     */
    List<Model> selectByExample(E example);

    /**
     * @mbg.generated
     */
    Model selectByPrimaryKey(PK id);

    /**
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Model record, @Param("example") E example);

    /**
     * @mbg.generated
     */
    int updateByExample(@Param("record") Model record, @Param("example") E example);

    /**
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Model record);

    /**
     * @mbg.generated
     */
    int updateByPrimaryKey(Model record);
}