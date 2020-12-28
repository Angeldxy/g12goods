package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.OrderOtherUnion.TimeServiceUnion;
import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.FlashsaleDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.flashsale.ModifyFlashSaleVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.flashsale.NewFlashSaleSkuVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.flashsale.NewFlashSaleVo;
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

/**
 * 将秒杀活动限定为平台管理员才能修改
 */

@RestController
public class FlashsaleController {
    private static final Logger logger = LoggerFactory.getLogger(FlashsaleController.class);

    @Autowired
    FlashsaleDao flashsaleDao;
    @Autowired
    ExistBelongDao existBelongDao;

    @Autowired
    TimeServiceUnion timeServiceUnion;

    /** TODO 响应式API */
    @GetMapping("/timesegments/{timesegId}/flashsales")
    public Object getFlashSaleItemInTimeSeg(@PathVariable Long timesegId) {
        return flashsaleDao.getFlashSaleItemInTimeSeg(timesegId);
    }

    @PostMapping("/shops/{shopId}/timesegments/{timesegId}/flashsales")
    public Object newFlashSale(@PathVariable Long shopId, @PathVariable Long timesegId,
                               @Validated @RequestBody NewFlashSaleVo vo, BindingResult bindingResult,
                               HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(flashsaleDao.newFlashSale(vo, timesegId));
    }

    /** TODO 响应式API */
    @GetMapping("/flashsales/current")
    public Object getFlashSaleItemTimeSegNow() {
        return flashsaleDao.getFlashSaleItemTimeSegNow();
    }

    @DeleteMapping("/shops/{shopId}/flashsales/{flashsaleId}")
    public Object deleteFlashSale(@PathVariable Long shopId, @PathVariable Long flashsaleId,
                                  HttpServletRequest request) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(flashsaleDao.chagneFlashSaleState(flashsaleId, (byte)2));
    }

    @PutMapping("/shops/{shopId}/flashsales/{flashsaleId}")
    public Object modifyFlashSale(@PathVariable Long shopId, @PathVariable Long flashsaleId,
                                  @Validated @RequestBody ModifyFlashSaleVo vo, BindingResult bindingResult,
                                  HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(flashsaleDao.modifyFlashSale(vo, flashsaleId));
    }

    @PutMapping("/shops/{shopId}/flashsales/{flashsaleId}/onshelves")
    public Object onshelvesFlashSale(@PathVariable Long shopId, @PathVariable Long flashsaleId,
                                     HttpServletRequest request) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(flashsaleDao.chagneFlashSaleState(flashsaleId, (byte)1));
    }

    @PutMapping("/shops/{shopId}/flashsales/{flashsaleId}/offshelves")
    public Object offshelvesFlashSale(@PathVariable Long shopId, @PathVariable Long flashsaleId,
                                      HttpServletRequest request) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(flashsaleDao.chagneFlashSaleState(flashsaleId, (byte)0));
    }

    @PostMapping("/shops/{shopId}/flashsales/{flashsaleId}/flashitems")
    public Object newFlashSaleItem(@PathVariable Long shopId, @PathVariable Long flashsaleId,
                                   @Validated @RequestBody NewFlashSaleSkuVo vo, BindingResult bindingResult,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(flashsaleDao.newFlashSaleItem(flashsaleId, vo));
    }

    @DeleteMapping("/shops/{shopId}/flashsales/{flashsaleId}/flashitems/{flashsaleItemId}")
    public Object deleteFlashSaleItem(@PathVariable Long shopId,
                                      @PathVariable Long flashsaleId, @PathVariable Long flashsaleItemId,
                                      HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, 1L)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(flashsaleDao.deleteFlashSaleItem(flashsaleId, flashsaleItemId));
    }
}
