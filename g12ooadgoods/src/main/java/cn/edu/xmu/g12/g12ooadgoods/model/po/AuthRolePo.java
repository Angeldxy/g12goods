package cn.edu.xmu.g12.g12ooadgoods.model.po;

import java.time.LocalDateTime;

public class AuthRolePo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.descr
     *
     * @mbg.generated
     */
    private String descr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.gmt_create
     *
     * @mbg.generated
     */
    private LocalDateTime gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_role.gmt_modified
     *
     * @mbg.generated
     */
    private LocalDateTime gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.id
     *
     * @return the value of auth_role.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.id
     *
     * @param id the value for auth_role.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.name
     *
     * @return the value of auth_role.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.name
     *
     * @param name the value for auth_role.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.creator_id
     *
     * @return the value of auth_role.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.creator_id
     *
     * @param creatorId the value for auth_role.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.descr
     *
     * @return the value of auth_role.descr
     *
     * @mbg.generated
     */
    public String getDescr() {
        return descr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.descr
     *
     * @param descr the value for auth_role.descr
     *
     * @mbg.generated
     */
    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.gmt_create
     *
     * @return the value of auth_role.gmt_create
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.gmt_create
     *
     * @param gmtCreate the value for auth_role.gmt_create
     *
     * @mbg.generated
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_role.gmt_modified
     *
     * @return the value of auth_role.gmt_modified
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_role.gmt_modified
     *
     * @param gmtModified the value for auth_role.gmt_modified
     *
     * @mbg.generated
     */
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}