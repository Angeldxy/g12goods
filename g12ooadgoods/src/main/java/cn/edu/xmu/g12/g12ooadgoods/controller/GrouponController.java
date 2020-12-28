package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.GrouponDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.groupon.ModifyGrouponVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.groupon.NewGrouponVo;
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

/*
*/
@RestController
public class GrouponController {
    private static final Logger logger = LoggerFactory.getLogger(GoodController.class);

    @Autowired
    GrouponDao grouponDao;
    @Autowired
    ExistBelongDao existBelongDao;

    @GetMapping("/groupons/states")
    public Object getAllStates() {
        return ResponseUtil.ok(grouponDao.getAllStates());
    }

    @GetMapping("/groupons")
    public Object getAllGrouponByCustomer(
            @RequestParam(required = false, name = "timeline")  Integer timeline,
            @RequestParam(required = false, name = "spu_id")    Long    spuId,
            @RequestParam(required = false, name = "shopId")    Long    shopId,
            @RequestParam(required = false, name = "page")      Integer page,
            @RequestParam(required = false, name = "pageSize")  Integer pageSize) {
        logger.info("getAllGrouponByCustomer controller");

        if (timeline != null && (timeline < 0 || timeline > 3)
                || shopId != null && shopId <= 0
                || spuId != null && spuId <= 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(grouponDao.getAllGrouponByCustomer(timeline, spuId, shopId, page, pageSize));
    }

    @GetMapping("/shops/{shopId}/groupons")
    public Object getAllGrouponByAdmin(
            @PathVariable Long shopId,
            @RequestParam(required = false, name = "spuid")     Long    spuId,
            @RequestParam(required = false, name = "beginTime") String  beginTimeStr,
            @RequestParam(required = false, name = "endTime")   String  endTimeStr,
            @RequestParam(required = false, name = "state")     Byte    state,
            @RequestParam(required = false, name = "page")      Integer page,
            @RequestParam(required = false, name = "pageSize")  Integer pageSize,
            HttpServletRequest request) {
        logger.info("getAllGrouponByAdmin controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        if (spuId != null && spuId <= 0 || state != null && (state < 0 || state > 2))
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        var beginTime = Tool.parseDateTime(beginTimeStr);
        var endTime = Tool.parseDateTime(endTimeStr);
        if (beginTime == null || endTime == null || !beginTime.isBefore(endTime))
            Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return ResponseUtil.ok(
                grouponDao.getAllGrouponByAdmin(shopId, spuId, beginTime, endTime, state, page, pageSize));
    }

    @PostMapping("/shops/{shopId}/spus/{spuId}/groupons")
    public Object newGroupon(@PathVariable Long shopId, @PathVariable Long spuId,
                             @Validated @RequestBody NewGrouponVo vo, BindingResult bindingResult,
                             HttpServletRequest request, HttpServletResponse response) {
        logger.info("newGroupon controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(grouponDao.newGroupon(shopId, spuId, vo));
    }

    @PutMapping("/shops/{shopId}/groupons/{grouponId}")
    public Object modifyGroupon(@PathVariable Long shopId, @PathVariable Long grouponId,
                                @Validated @RequestBody ModifyGrouponVo vo, BindingResult bindingResult,
                                HttpServletRequest request, HttpServletResponse response) {
        logger.info("modifyGroupon controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.grouponBelongToShop(grouponId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(grouponDao.modifyGroupon(shopId, grouponId, vo));
    }

    @DeleteMapping("/shops/{shopId}/groupons/{grouponId}")
    public Object deleteGroupon(@PathVariable Long shopId, @PathVariable Long grouponId,
                                HttpServletRequest request) {
        logger.info("deleteGroupon controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.grouponBelongToShop(grouponId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(grouponDao.changeGrouponState(grouponId, (byte)2));
    }

    @PutMapping("/shops/{shopId}/groupons/{grouponId}/onshelves")
    public Object onshelvesGroupon(@PathVariable Long shopId, @PathVariable Long grouponId,
                                   HttpServletRequest request) {
        logger.info("onshelvesGroupon controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.grouponBelongToShop(grouponId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(grouponDao.changeGrouponState(grouponId, (byte)1));
    }

    @PutMapping("/shops/{shopId}/groupons/{grouponId}/offshelves")
    public Object offshelvesGroupon(@PathVariable Long shopId, @PathVariable Long grouponId,
                                    HttpServletRequest request) {
        logger.info("offshelvesGroupon controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.grouponBelongToShop(grouponId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(grouponDao.changeGrouponState(grouponId, (byte)0));
    }
}
