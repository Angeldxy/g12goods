package cn.edu.xmu.g12.g12ooadgoods.mapper;

import cn.edu.xmu.g12.g12ooadgoods.model.po.AuthNewUserPo;
import cn.edu.xmu.g12.g12ooadgoods.model.po.AuthNewUserPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthNewUserPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int deleteByExample(AuthNewUserPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int insert(AuthNewUserPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int insertSelective(AuthNewUserPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    List<AuthNewUserPo> selectByExample(AuthNewUserPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    AuthNewUserPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AuthNewUserPo record, @Param("example") AuthNewUserPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AuthNewUserPo record, @Param("example") AuthNewUserPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AuthNewUserPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_new_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AuthNewUserPo record);
}