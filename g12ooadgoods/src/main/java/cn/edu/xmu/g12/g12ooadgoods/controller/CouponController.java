package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.dao.CouponDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.coupon.ModifyCouponActivityVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.coupon.NewCouponVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.groupon.ModifyGrouponVo;
import cn.edu.xmu.g12.g12ooadgoods.util.Common;
import cn.edu.xmu.g12.g12ooadgoods.util.ResponseCode;
import cn.edu.xmu.g12.g12ooadgoods.util.ResponseUtil;
import cn.edu.xmu.g12.g12ooadgoods.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/*
*/
@RestController
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    CouponDao couponDao;
    @Autowired
    ExistBelongDao existBelongDao;

    @GetMapping("/coupons/states")
    public Object getAllState() {
        return ResponseUtil.ok(couponDao.getAllState());
    }

    @PostMapping("/shops/{shopId}/couponactivities")
    public Object NewCouponActivity(
            @PathVariable Long shopId,
            @Validated @RequestBody NewCouponVo vo, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var userId = Tool.parseJwtAndGetUser(request);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.AUTH_JWT_EXPIRED);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(couponDao.newCouponActivity(shopId, userId, vo));
    }

    @PostMapping("/shops/{shopId}/couponactivities/{couponActId}/uploadImg")
    public Object uploadCouponImg(@PathVariable Long shopId, @PathVariable Long couponActId,
                                  HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        if (existBelongDao.couponActBelongToShop(shopId, couponActId) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(couponDao.uploadCouponImg());
    }

    /** 无需登录 */
    @GetMapping("/couponactivities")
    public Object getAllCoupon(
            @RequestParam(required = false, name = "shopId")    Long    shopId,
            @RequestParam(required = false, name = "timeline")  Integer timeline,
            @RequestParam(required = false, name = "page")      Integer page,
            @RequestParam(required = false, name = "pageSize")  Integer pageSize) {
        if (shopId != null && shopId <= 0
            || timeline != null && (timeline < 0 || timeline > 3)
            || Tool.checkPageParam(page, pageSize) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(couponDao.getAllCoupon(shopId, timeline, page, pageSize));
    }

    @GetMapping("/shops/{shopId}/couponactivities/invalid")
    public Object getOffShelveCoupon(
            @PathVariable Long shopId,
            @RequestParam(required = false, name = "page")     Integer page,
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        if (Tool.checkPageParam(page, pageSize) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(couponDao.getOffShelveCoupon(shopId, page, pageSize));
    }

    @GetMapping("/couponactivities/{couponActId}/skus")
    public Object getSkuInCouponAct(
            @PathVariable Long couponActId,
            @RequestParam(required = false, name = "page")     Integer page,
            @RequestParam(required = false, name = "pageSize") Integer pageSize) {
        if (couponActId != null && couponActId <= 0
            || Tool.checkPageParam(page, pageSize) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(couponDao.getSkuInCouponAct(couponActId, page, pageSize));
    }

    @GetMapping("/shops/{shopId}/couponactivities/{couponActId}")
    public Object getCouponActivityDetail(
            @PathVariable Long shopId,
            @PathVariable Long couponActId) {
        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateReturnObject(couponDao.getCouponActivityDetail(shopId, couponActId));
    }

    @PutMapping("/shops/{shopId}/couponactivities/{couponActId}")
    public Object modifyCouponActivity(
            @PathVariable Long shopId,
            @PathVariable Long couponActId,
            @Validated @RequestBody ModifyCouponActivityVo vo, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(couponDao.modifyCouponActivity(shopId, couponActId, vo));
    }

    @DeleteMapping("/shops/{shopId}/couponactivities/{couponActId}")
    public Object deleteCouoponActivity(
            @PathVariable Long shopId,
            @PathVariable Long couponActId,
            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(couponDao.changeCouponActivityState(shopId, couponActId, (byte)2));
    }

    @PostMapping("/shops/{shopId}/couponactivities/{couponActId}/skus")
    public Object addSkuListIntoCouponSku(
            @PathVariable Long shopId,
            @PathVariable Long couponActId,
            @RequestBody List<Long> skuIdList,
            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        if (skuIdList == null || skuIdList.size() == 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);
        skuIdList = skuIdList.stream().filter(item -> item == null || item <= 0).collect(Collectors.toList());
        if (skuIdList.size() == 0) return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateResponseCode(couponDao.addSkuListIntoCouponSku(shopId, couponActId, skuIdList));
    }

    @DeleteMapping("/shops/{shopId}/couponskus/{couponActId}")
    public Object deleteSkuListFromCouponSku(
            @PathVariable Long shopId,
            @PathVariable Long couponActId,
            @RequestBody List<Long> skuIdList,
            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        if (skuIdList == null || skuIdList.size() == 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);
        skuIdList = skuIdList.stream().filter(item -> item == null || item <= 0).collect(Collectors.toList());
        if (skuIdList.size() == 0) return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateResponseCode(couponDao.deleteSkuListFromCouponSku(shopId, couponActId, skuIdList));
    }

    @GetMapping("/coupons")
    public Object getMyCouponList(
            @RequestParam(required = false, name = "state")    Byte    state,
            @RequestParam(required = false, name = "page")     Integer page,
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            HttpServletRequest request) {
        var userId = Tool.parseJwtAndGetUser(request);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.AUTH_JWT_EXPIRED);

        if (state != null && (state < 0 || state > 3) || Tool.checkPageParam(page, pageSize) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(couponDao.getMyCouponList(userId, state, page, pageSize));
    }

    @PostMapping("/couponactivities/{couponActId}/usercoupons")
    public Object customerGetCoupon(@PathVariable Long couponActId, HttpServletRequest request) {
        var userId = Tool.parseJwtAndGetUser(request);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.AUTH_JWT_EXPIRED);

        return Tool.decorateReturnObject(couponDao.customerGetCoupon(userId, couponActId));
    }

    @PutMapping("/shops/{shopId}/couponactivities/{couponActId}/onshelves")
    public Object onshelvesCouponActivity(@PathVariable Long shopId, @PathVariable Long couponActId,
                                          HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(couponDao.changeCouponActivityState(shopId, couponActId, (byte)1));
    }

    @PutMapping("/shops/{shopId}/couponactivities/{couponActId}/offshelves")
    public Object offshelvesCouponActivity(@PathVariable Long shopId, @PathVariable Long couponActId,
                                           HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        var code = existBelongDao.couponActBelongToShop(couponActId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(couponDao.changeCouponActivityState(shopId, couponActId, (byte)0));
    }


}
