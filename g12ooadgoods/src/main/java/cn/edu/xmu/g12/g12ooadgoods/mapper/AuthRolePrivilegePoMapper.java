package cn.edu.xmu.g12.g12ooadgoods.mapper;

import cn.edu.xmu.g12.g12ooadgoods.model.po.AuthRolePrivilegePo;
import cn.edu.xmu.g12.g12ooadgoods.model.po.AuthRolePrivilegePoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthRolePrivilegePoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int deleteByExample(AuthRolePrivilegePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int insert(AuthRolePrivilegePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int insertSelective(AuthRolePrivilegePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    List<AuthRolePrivilegePo> selectByExample(AuthRolePrivilegePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    AuthRolePrivilegePo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AuthRolePrivilegePo record, @Param("example") AuthRolePrivilegePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AuthRolePrivilegePo record, @Param("example") AuthRolePrivilegePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AuthRolePrivilegePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_role_privilege
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AuthRolePrivilegePo record);
}