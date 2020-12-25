package cn.edu.xmu.oomall.order.discount;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author xincong yao
 * @date 2020-11-18
 */
public interface JsonSerializable {

	String toJsonString() throws JsonProcessingException;
}
