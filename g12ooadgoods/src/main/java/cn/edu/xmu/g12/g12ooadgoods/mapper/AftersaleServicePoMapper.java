package cn.edu.xmu.g12.g12ooadgoods.mapper;

import cn.edu.xmu.g12.g12ooadgoods.model.po.AftersaleServicePo;

public interface AftersaleServicePoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int insert(AftersaleServicePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int insertSelective(AftersaleServicePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    AftersaleServicePo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AftersaleServicePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AftersaleServicePo record);
}