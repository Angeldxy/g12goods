package cn.edu.xmu.g12.g12ooadgoods.model.vo.coupon;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ApiModel("新 Coupon 传值对象")
public class NewCouponVo {
//  "name": "string",
//  "quantity": 0,
//  "quantityType": 0,
//  "validTerm": 0,
//  "couponTime": "string",
//  "beginTime": "string",
//  "endTime": "string",
//  "strategy": "string"

    @NotNull(message = "name 不得为空")
    @Size(min = 1)
    private String name;

    @NotNull(message = "quantity 不得为空")
    private Integer quantity;

    @NotNull(message = "quantityType 不得为空")
    private Byte quantityType;

    @NotNull(message = "validTerm 不得为空")
    private Byte validTerm;

    @NotNull(message = "couponTime 不得为空")
    private LocalDateTime couponTime;

    @NotNull(message = "beginTime 不得为空")
    private LocalDateTime beginTime;

    @NotNull(message = "endTime 不得为空")
    private LocalDateTime endTime;

    @NotNull(message = "strategy 不得为空")
    @Size(min = 1)
    private String strategy;

}
