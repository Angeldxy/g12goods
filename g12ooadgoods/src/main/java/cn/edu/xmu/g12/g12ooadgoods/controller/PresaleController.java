package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.PresaleDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.presale.ModifyPreSaleVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.presale.NewPreSaleVo;
import cn.edu.xmu.g12.g12ooadgoods.util.Common;
import cn.edu.xmu.g12.g12ooadgoods.util.ResponseCode;
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
public class PresaleController {
    private static final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    PresaleDao presaleDao;
    @Autowired
    ExistBelongDao existBelongDao;

    @GetMapping("/presales/states")
    public Object getAllState() {
        return Tool.decorateReturnObject(presaleDao.getAllState());
    }

    @GetMapping("/presales")
    public Object getAllValidPresaleActivity(
            @RequestParam(required = false) Long    shopId,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long    skuId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        logger.info("getAllValidPresaleActivity controller shopid="+shopId);

        if (timeline!= null && (timeline < 0 || timeline > 3)
                || skuId!= null && skuId < 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateReturnObject(
                presaleDao.getAllValidPresaleActivity(shopId, timeline, skuId, page, pageSize));
    }

    /** 管理员 */
    @GetMapping("/shops/{shopId}/presales")
    public Object getSkuAllPresaleActivity(@PathVariable Long shopId,
                                           @RequestParam(required = false) Long skuId,
                                           @RequestParam(required = false) Byte state,
                                           HttpServletRequest request) {
        logger.info("getSkuAllPresaleActivity controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        if (state!= null && (state < 0 || state > 2)
                || skuId!= null && skuId < 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateReturnObject(presaleDao.getSkuAllPresaleActivity(shopId, skuId, state));
    }

    @PostMapping("/shops/{shopId}/skus/{skuId}/presales")
    public Object newPresale(@PathVariable Long shopId, @PathVariable Long skuId,
                             @Validated @RequestBody NewPreSaleVo vo, BindingResult bindingResult,
                             HttpServletRequest request, HttpServletResponse response) {
        logger.info("newPresale controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return Tool.decorateResponseCode(code);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(presaleDao.newPresale(shopId, skuId, vo));
    }

    @PostMapping("/shops/{shopId}/presales/{presaleId}")
    public Object modifyPresale(@PathVariable Long shopId, @PathVariable Long presaleId,
                                @Validated @RequestBody ModifyPreSaleVo vo, BindingResult bindingResult,
                                HttpServletRequest request, HttpServletResponse response) {
        logger.info("modifyPresale controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.presaleBelongToShop(presaleId, shopId);
        if (code != ResponseCode.OK) return Tool.decorateResponseCode(code);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(presaleDao.modifyPresale(presaleId, vo));
    }

    @DeleteMapping("/shops/{shopId}/presales/{presaleId}")
    public Object deletePresale(@PathVariable Long shopId, @PathVariable Long presaleId,
                                HttpServletRequest request) {
        logger.info("deletePresale controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.presaleBelongToShop(presaleId, shopId);
        if (code != ResponseCode.OK) return Tool.decorateResponseCode(code);

        return Tool.decorateResponseCode(presaleDao.changePresaleState(presaleId, (byte)2));
    }

    @PostMapping("/shops/{shopId}/presales/{presaleId}/onshelves")
    public Object onshelvesPresale(@PathVariable Long shopId, @PathVariable Long presaleId,
                                   HttpServletRequest request) {
        logger.info("onshelvesPresale controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.presaleBelongToShop(presaleId, shopId);
        if (code != ResponseCode.OK) return Tool.decorateResponseCode(code);

        return Tool.decorateResponseCode(presaleDao.changePresaleState(presaleId, (byte)1));
    }

    @PostMapping("/shops/{shopId}/presales/{presaleId}/offshelves")
    public Object offshelvesPresale(@PathVariable Long shopId, @PathVariable Long presaleId,
                                    HttpServletRequest request) {
        logger.info("offshelvesPresale controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.presaleBelongToShop(presaleId, shopId);
        if (code != ResponseCode.OK) return Tool.decorateResponseCode(code);

        return Tool.decorateResponseCode(presaleDao.changePresaleState(presaleId, (byte)0));
    }
}
