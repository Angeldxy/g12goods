package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.OrderOtherUnion.ShareServiceUnion;
import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.GoodDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.good.*;
import cn.edu.xmu.g12.g12ooadgoods.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
*/
@RestController
public class GoodController {
    private static final Logger logger = LoggerFactory.getLogger(GoodController.class);

    @Autowired
    GoodDao goodDao;
    @Autowired
    ExistBelongDao existBelongDao;

    @Autowired(required = false)
    ShareServiceUnion shareUnion;

    @GetMapping("/skus/states")
    public Object getStates() { return ResponseUtil.ok(goodDao.getStates()); }

    @GetMapping("/skus")
    public Object getSkuList(
            @RequestParam(required = false) Long    shopId,
            @RequestParam(required = false) String  skuSn,
            @RequestParam(required = false) Long    spuId,
            @RequestParam(required = false) String  spuSn,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (shopId != null && shopId <= 0
            || skuSn != null && skuSn.length() == 0
            || spuId != null && spuId <= 0
            || spuSn != null && spuSn.length() == 0)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateReturnObject(goodDao.getSkuList(shopId, skuSn, spuId, spuSn, page, pageSize));
    }

    @GetMapping("/skus/{skuId}")
    public Object getSkuById(@PathVariable Long skuId) {
        if (skuId == null || skuId < 0)
            return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_NOTEXIST);

        return Tool.decorateReturnObject(goodDao.getSkuBoById(skuId));
    }

    @PostMapping("/shops/{shopId}/spus/{spuId}/skus")
    public Object newSku(@PathVariable Long shopId,
                         @PathVariable Long spuId,
                         @Validated @RequestBody NewSkuVo vo, BindingResult bindingResult,
                         HttpServletRequest request, HttpServletResponse response) {
        logger.info("newSku controller shopid="+shopId+",spuid= "+spuId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(goodDao.newSku(shopId, spuId, vo));
    }

    @PostMapping("/shops/{shopId}/skus/{skuId}/uploadImg")
    public Object uploadSkuImg(@PathVariable Long shopId, @PathVariable Long skuId, HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        // TODO upload image...
        return Tool.decorateResponseCode(goodDao.uploadSkuImg(shopId, skuId));
    }

    @DeleteMapping("/shops/{shopId}/skus/{skuId}")
    public Object deleteSku(@PathVariable Long shopId, @PathVariable Long skuId,
                            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.changeSkuState(skuId, (byte)6));
    }

    @PutMapping("/shops/{shopId}/skus/{skuId}")
    public Object modifySku(@PathVariable Long shopId, @PathVariable Long skuId,
                            @RequestBody ModifySkuVo vo, BindingResult bindingResult,
                            HttpServletRequest request, HttpServletResponse response) {
        logger.info("newSku controller shopid="+shopId+",skuId= "+skuId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        if (vo.isAllFieldNull()) return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateResponseCode(goodDao.modifySku(skuId, vo));
    }

    @GetMapping("/categories/{pid}/subcategories")
    public Object getSubCategory(@PathVariable Long pid) {
        if (pid == null || pid <= 0) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_NOTEXIST);

        return Tool.decorateReturnObject(goodDao.getSubCategory(pid));
    }

    @PostMapping("/shops/{shopId}/categories/{pid}/subcategories")
    public Object newCategory(@PathVariable Long shopId, @PathVariable Long pid,
                              @Validated @RequestBody CategoryNameVo vo, BindingResult bindingResult,
                              HttpServletRequest request, HttpServletResponse response) {
        logger.info("newCategory controller shopid="+shopId+",pid= "+pid);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(goodDao.newCategory(pid, vo.getName()));
    }

    @PutMapping("/shops/{shopId}/categories/{pid}")
    public Object modifyCategory(@PathVariable Long shopId, @PathVariable Long pid,
                                 @Validated @RequestBody CategoryNameVo vo, BindingResult bindingResult,
                                 HttpServletRequest request, HttpServletResponse response) {
        logger.info("modifyCategory controller shopid="+shopId+",pid= "+pid);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(goodDao.modifyCategory(pid, vo.getName()));
    }

    @DeleteMapping("/shops/{shopId}/categories/{pid}")
    public Object deleteCategory(@PathVariable Long shopId, @PathVariable Long pid,
                                 HttpServletRequest request) {
        logger.info("deleteCategory controller shopid="+shopId+",pid= "+pid);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(goodDao.deleteCategory(pid));
    }

    @GetMapping("/spus/{spuId}")
    public Object getSpuById(@PathVariable Long spuId) {
        logger.info("getSpuById controller spuId="+spuId);
        return Tool.decorateReturnObject(goodDao.getSpuById(spuId));
    }

    @GetMapping("/share/{sid}/skus/{skuId}")
    public Object getShareSkuById(@PathVariable Long sid, @PathVariable Long skuId,
                                  HttpServletRequest request) {
        var userId = Tool.parseJwtAndGetUser(request);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.AUTH_INVALID_JWT);

        return Tool.decorateReturnObject(goodDao.getSkuBoById(skuId));
    }

    @PostMapping("/shops/{shopId}/spus")
    public Object newSpu(@PathVariable Long shopId,
                         @Validated @RequestBody NewSpuVo vo, BindingResult bindingResult,
                         HttpServletRequest request, HttpServletResponse response) {
        logger.info("newSpu controller shopid="+shopId);

        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(goodDao.newSpu(vo, shopId));
    }

    @PutMapping("/shops/{shopId}/spus/{spuId}")
    public Object modifySpu(@PathVariable Long shopId, @PathVariable Long spuId,
                          @Validated @RequestBody ModifySpuVo vo, BindingResult bindingResult,
                          HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateResponseCode(goodDao.modifySpu(vo, spuId));
    }

    @DeleteMapping("/shops/{shopId}/spus/{spuId}")
    public Object deleteSpu(@PathVariable Long shopId, @PathVariable Long spuId,
                          HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.deleteSpu(spuId));
    }

    @PutMapping("/shops/{shopId}/skus/{skuId}/onshelves")
    public Object skuOnShelves(@PathVariable Long shopId, @PathVariable Long skuId,
                             HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.changeSkuState(skuId, (byte)4));
    }

    @PutMapping("/shops/{shopId}/skus/{skuId}/offshelves")
    public Object skuOffShelves(@PathVariable Long shopId, @PathVariable Long skuId,
                              HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.changeSkuState(skuId, (byte)0));
    }

    @PostMapping("/shops/{shopId}/skus/{skuId}/floatPrices")
    public Object newFloatPrice(@PathVariable Long shopId, @PathVariable Long skuId,
                              @Validated @RequestBody NewFloatPriceVo vo, BindingResult bindingResult,
                              HttpServletRequest request, HttpServletResponse response) {
        var userId = Tool.parseJwtAndGetUser(request, shopId);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.skuBelongToShop(skuId, shopId);
        if (code != ResponseCode.OK) return code;

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(goodDao.newFloatPrice(vo, skuId, userId));
    }

    @DeleteMapping("/shops/{shopId}/floatPrices/{floatId}")
    public Object endisableFloatPrice(@PathVariable Long shopId, @PathVariable Long floatId,
                                    HttpServletRequest request) {
        var userId = Tool.parseJwtAndGetUser(request, shopId);
        if (userId == null) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.floatPriceBelongToShop(floatId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.endisableFloatPrice(floatId, userId));
    }

    @PostMapping("/shops/{shopId}/brands")
    public Object newBrand(@PathVariable Long shopId,
                         @Validated @RequestBody NewBrandVo vo, BindingResult bindingResult,
                         HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;

        return Tool.decorateReturnObject(goodDao.newBrand(vo));
    }

    @PostMapping("/shops/{shopId}/brands/{brandId}/uploadImg")
    public Object uploadBrandImg(@PathVariable Long shopId, @PathVariable Long brandId,
                               HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        // TODO upload image...
        return Tool.decorateResponseCode(goodDao.uploadBrandImg(shopId, brandId));
    }

    @GetMapping("/brands")
    public Object getAllBrands(@RequestParam(required = false) Integer page,
                             @RequestParam(required = false) Integer pageSize) {
        if (Tool.checkPageParam(page, pageSize) != ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);
        return Tool.decorateReturnObject(goodDao.getAllBrands(page, pageSize));
    }

    @PutMapping("/shops/{shopId}/brands/{brandId}")
    public Object modifyBrand(@PathVariable Long shopId, @PathVariable Long brandId,
                            @Validated @RequestBody ModifyBrandVo vo, BindingResult bindingResult,
                            HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if (object != null) return object;
        if (vo.getName() == null && vo.getDetail() == null)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        return Tool.decorateResponseCode(goodDao.modifyBrand(vo, brandId));
    }

    @DeleteMapping("/shops/{shopId}/brands/{brandId}")
    public Object deleteBrand(@PathVariable Long shopId, @PathVariable Long brandId,
                            HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        return Tool.decorateResponseCode(goodDao.deleteBrand(brandId));
    }

    @PostMapping("/shops/{shopId}/spus/{spuId}/categories/{categoryId}")
    public Object addSpuIntoCategory(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long categoryId,
                                   HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.addSpuIntoCategory(spuId, categoryId));
    }

    @DeleteMapping("/shops/{shopId}/spus/{spuId}/categories/{categoryId}")
    public Object removeSpuFromCategory(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long categoryId,
                                      HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.removeSpuFromCategory(spuId, categoryId));
    }

    @PostMapping("/shops/{shopId}/spus/{spuId}/brands/{brandId}")
    public Object addSpuIntoBrand(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long brandId,
                                HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.addSpuIntoBrand(spuId, brandId));
    }

    @DeleteMapping("/shops/{shopId}/spus/{spuId}/brands/{brandId}")
    public Object removeSpuFromBrand(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long brandId,
                                   HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.spuBelongToShop(spuId, shopId);
        if (code != ResponseCode.OK) return code;

        return Tool.decorateResponseCode(goodDao.removeSpuFromBrand(spuId, brandId));
    }
}
