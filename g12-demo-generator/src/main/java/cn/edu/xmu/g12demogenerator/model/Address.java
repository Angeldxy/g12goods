package cn.edu.xmu.g12demogenerator.model;

import java.util.Date;

public class Address {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.customer_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Long customerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.region_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Long regionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.detail
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private String detail;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.consignee
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private String consignee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.mobile
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.be_default
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Byte beDefault;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.gmt_create
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Date gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column address.gmt_modified
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.id
     *
     * @return the value of address.id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.id
     *
     * @param id the value for address.id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.customer_id
     *
     * @return the value of address.customer_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.customer_id
     *
     * @param customerId the value for address.customer_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.region_id
     *
     * @return the value of address.region_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.region_id
     *
     * @param regionId the value for address.region_id
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.detail
     *
     * @return the value of address.detail
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public String getDetail() {
        return detail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.detail
     *
     * @param detail the value for address.detail
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.consignee
     *
     * @return the value of address.consignee
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.consignee
     *
     * @param consignee the value for address.consignee
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.mobile
     *
     * @return the value of address.mobile
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.mobile
     *
     * @param mobile the value for address.mobile
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.be_default
     *
     * @return the value of address.be_default
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Byte getBeDefault() {
        return beDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.be_default
     *
     * @param beDefault the value for address.be_default
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setBeDefault(Byte beDefault) {
        this.beDefault = beDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.gmt_create
     *
     * @return the value of address.gmt_create
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.gmt_create
     *
     * @param gmtCreate the value for address.gmt_create
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column address.gmt_modified
     *
     * @return the value of address.gmt_modified
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column address.gmt_modified
     *
     * @param gmtModified the value for address.gmt_modified
     *
     * @mbg.generated Mon Dec 14 16:11:39 CST 2020
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}