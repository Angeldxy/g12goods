package cn.edu.xmu.g12.g12ooadgoods.dao;

import cn.edu.xmu.g12.g12ooadgoods.OrderOtherUnion.CustomerServiceUnion;
import cn.edu.xmu.g12.g12ooadgoods.OrderOtherUnion.ShareServiceUnion;
import cn.edu.xmu.g12.g12ooadgoods.mapper.*;
import cn.edu.xmu.g12.g12ooadgoods.model.bo.ListBo;
import cn.edu.xmu.g12.g12ooadgoods.model.bo.UserOverview;
import cn.edu.xmu.g12.g12ooadgoods.model.bo.good.*;
import cn.edu.xmu.g12.g12ooadgoods.model.po.*;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.good.*;
import cn.edu.xmu.g12.g12ooadgoods.util.ResponseCode;
import cn.edu.xmu.g12.g12ooadgoods.util.ResponseUtil;
import cn.edu.xmu.g12.g12ooadgoods.util.ReturnObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class GoodDao {

    private static final Logger logger = LoggerFactory.getLogger(GoodDao.class);
    ObjectMapper jsonMapper = new ObjectMapper();

    @Resource
    GoodsSpuPoMapper spuPoMapper;
    @Resource
    GoodsSkuPoMapper skuPoMapper;
    @Resource
    BrandPoMapper brandPoMapper;
    @Resource
    GoodsCategoryPoMapper goodsCategoryPoMapper;
    @Resource
    FreightModelPoMapper freightModelPoMapper;
    @Resource
    ShopPoMapper shopPoMapper;
    @Resource
    SkuPriceDao skuPriceDao;
    @Resource
    FloatPricePoMapper floatPricePoMapper;

    @Autowired
    CustomerServiceUnion customerUnion;
    @Autowired
    ShareServiceUnion shareUnion;

    public Object getStates() {
        return ResponseUtil.ok(GoodState.getAllStates());
    }

    private ReturnObject<ListBo<SkuOverview>> emptyReturnListBo(Integer page, Integer pageSize) {
        if (page == null && pageSize == null) return new ReturnObject<>(
                    new ListBo<>(1, 0, 0L, 1, new ArrayList<>()));
        else return new ReturnObject<>(
                    new ListBo<>(page, pageSize, 0L, 1, new ArrayList<>()));
    }

    private ReturnObject<ListBo<SkuOverview>> makeReturnListBo(
            List<GoodsSkuPo> poList, List<SkuOverview> boList,
            Integer page, Integer pageSize) {
        if (page != null) {
            // 返回分页信息
            var pageInfo = new PageInfo<>(poList);
            return new ReturnObject<>(new ListBo<>(
                    page, pageSize, pageInfo.getTotal(), pageInfo.getPages(), boList));
        } else
            return new ReturnObject<>(new ListBo<>(
                    1, boList.size(), (long) boList.size(), 1, boList));
    }

    public ReturnObject<ListBo<SkuOverview>> getSkuList(
            Long    shopId,
            String  skuSn,
            Long    spuId,
            String  spuSn,
            Integer page,
            Integer pageSize
    ) {
        List<Long> spuIdList = null;
        if (shopId != null) {
            var spuExample = new GoodsSpuPoExample();
            spuExample.createCriteria().andShopIdEqualTo(shopId);
            var spuList = spuPoMapper.selectByExample(spuExample);
            if (spuSn != null) spuList = spuList.stream()
                    .filter(item->item.getGoodsSn().equals(spuSn))
                    .collect(Collectors.toList());
            if (spuId != null) spuList = spuList.stream()
                    .filter(item->item.getId().equals(spuId))
                    .collect(Collectors.toList());
            spuIdList = spuList.stream().map(GoodsSpuPo::getId).collect(Collectors.toList());
            if (spuIdList.size() == 0) return emptyReturnListBo(page, pageSize);
        }
        if (spuIdList == null && (spuId != null || spuSn != null)) {
            var spuExample = new GoodsSpuPoExample();
            var criteria = spuExample.createCriteria();
            if (spuId != null) criteria.andIdEqualTo(spuId);
            if (spuSn != null) criteria.andGoodsSnEqualTo(spuSn);
            var spuPoList = spuPoMapper.selectByExample(spuExample);
            if (spuPoList.size() == 0) return emptyReturnListBo(page, pageSize);
            spuIdList = new ArrayList<>();
            spuIdList.add(spuPoList.get(0).getId());
        }
        var skuExample = new GoodsSkuPoExample();
        var criteria = skuExample.createCriteria();
        if (skuSn != null) criteria.andSkuSnEqualTo(skuSn);
        if (spuIdList != null) criteria.andGoodsSpuIdIn(spuIdList);
        if (page != null && pageSize != null) PageHelper.startPage(page, pageSize); // 设置整个线程的Page选项
        var skuPoList = skuPoMapper.selectByExample(skuExample);
        var skuBoList = skuPoList.stream().map(item -> new SkuOverview(item,
                skuPriceDao.getSkuPrice(item))).collect(Collectors.toList());
        var returnObject
                = makeReturnListBo(skuPoList, skuBoList, page, pageSize);
        PageHelper.clearPage();
        return returnObject;
    }

    public ReturnObject<SkuBo> getSkuBoById(Long skuId) {
        var targetSku = skuPoMapper.selectByPrimaryKey(skuId);
        if (targetSku == null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        var targetSkuPrice = skuPriceDao.getSkuPrice(targetSku);

        var returnObject = shareUnion.skuSharable(skuId);
        var targetSkuShareable = (returnObject.getCode() == ResponseCode.OK && returnObject.getData());

        var spuBo = getSpu(targetSku.getGoodsSpuId());
        var skuBo = new SkuBo(targetSku, targetSkuPrice, targetSkuShareable, spuBo);
        return new ReturnObject<>(skuBo);
    }

    public ReturnObject<SkuOverview> newSku(Long shopId, Long spuId, NewSkuVo vo) {
        var goodsSkuExample = new GoodsSkuPoExample();
        goodsSkuExample.createCriteria().andSkuSnEqualTo(vo.getSn());
        var confictList = skuPoMapper.selectByExample(goodsSkuExample);
        if (confictList.size()!=0) return new ReturnObject<>(ResponseCode.SKUSN_SAME);

        var goodsSpu = spuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpu == null || !goodsSpu.getShopId().equals(shopId))
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        var newSkuPo = new GoodsSkuPo();
        newSkuPo.setGoodsSpuId(spuId);
        newSkuPo.setName(vo.getName());
        newSkuPo.setOriginalPrice(vo.getOriginalPrice());
        newSkuPo.setConfiguration(vo.getConfiguration());
        newSkuPo.setWeight(vo.getWeight());
        newSkuPo.setImageUrl(vo.getImageUrl());
        newSkuPo.setInventory(vo.getInventory());
        newSkuPo.setDetail(vo.getDetail());
        newSkuPo.setDisabled((byte)0);
        newSkuPo.setGmtCreate(LocalDateTime.now());
        newSkuPo.setGmtModified(LocalDateTime.now());
        newSkuPo.setState((byte)0);

        skuPoMapper.insert(newSkuPo);

        return new ReturnObject<>(new SkuOverview(newSkuPo, skuPriceDao.getSkuPrice(newSkuPo)));
    }

    public ResponseCode uploadSkuImg(Long shopId, Long skuId) {
        // TODO upload image...
        return ResponseCode.OK;
    }

    public ResponseCode changeSkuState(Long skuId, Byte state) {
        var skuPo = new GoodsSkuPo();
        skuPo.setId(skuId);
        skuPo.setState(state);
        skuPo.setGmtModified(LocalDateTime.now());
        skuPoMapper.updateByPrimaryKey(skuPo);
        return ResponseCode.OK;
    }

    public ResponseCode modifySku(Long skuId, ModifySkuVo vo) {
        var skuPo = vo.convertToGoodsSkuPo();
        skuPo.setId(skuId);
        skuPo.setGmtModified(LocalDateTime.now());
        skuPoMapper.updateByPrimaryKey(skuPo);
        return ResponseCode.OK;
    }

    public ReturnObject<List<GoodsCategoryPo>> getSubCategory(Long pid) {
        logger.info("getSubCategory(Long pid): pid="+pid);

        // if pid==zero no subcategory
        var categoryPo = goodsCategoryPoMapper.selectByPrimaryKey(pid);
        if (categoryPo == null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);

        var categoryExample = new GoodsCategoryPoExample();
        categoryExample.createCriteria().andPidEqualTo(pid);
        var subCategoryPoList = goodsCategoryPoMapper.selectByExample(categoryExample);
        return new ReturnObject<>(subCategoryPoList);
    }

    public ReturnObject<CategoryBo> newCategory(Long pid, String name) {
        if (pid != 0) {
            var categoryParent = goodsCategoryPoMapper.selectByPrimaryKey(pid);
            if (categoryParent == null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        var categoryPo = new GoodsCategoryPo();
        categoryPo.setName(name);
        categoryPo.setPid(pid);
        categoryPo.setGmtCreate(LocalDateTime.now());
        categoryPo.setGmtModified(LocalDateTime.now());
        goodsCategoryPoMapper.insert(categoryPo);
        return new ReturnObject<>(new CategoryBo(categoryPo));
    }

    public ResponseCode modifyCategory(Long categoryId, String name) {
        var categoryPo = new GoodsCategoryPo();
        categoryPo.setId(categoryId);
        categoryPo.setName(name);
        int rows = goodsCategoryPoMapper.updateByPrimaryKey(categoryPo);
        if (rows == 0) return ResponseCode.RESOURCE_ID_NOTEXIST;
        return ResponseCode.OK;
    }

    public ResponseCode deleteCategory(Long categoryId) {
        // 将子类目的pid设置为被删除的类目的pid
        // 将spu中的类目设置为类目的pid

        var targetCategory = goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        if (targetCategory == null) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var childCategoryExample = new GoodsCategoryPoExample();
        var childCategoryPo = new GoodsCategoryPo();
        childCategoryExample.createCriteria().andPidEqualTo(targetCategory.getId());
        childCategoryPo.setPid(targetCategory.getPid());
        var childSpuExample = new GoodsSpuPoExample();
        var childSpuPo = new GoodsSpuPo();
        childSpuExample.createCriteria().andCategoryIdEqualTo(targetCategory.getId());
        childSpuPo.setCategoryId(targetCategory.getPid());

        goodsCategoryPoMapper.updateByExampleSelective(childCategoryPo, childCategoryExample);
        spuPoMapper.updateByExampleSelective(childSpuPo, childSpuExample);
        goodsCategoryPoMapper.deleteByPrimaryKey(categoryId);
        return ResponseCode.OK;
    }

    public ReturnObject<SpuBo> getSpuById(Long spuId) {
        var spuBo = getSpu(spuId);
        if (spuBo == null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        return new ReturnObject<>(spuBo);
    }

    private SpuBo getSpu(Long spuId) {
        if (spuId == null || spuId < 0) return null;
        var spu = spuPoMapper.selectByPrimaryKey(spuId);
        if (spu == null) return null;

        var brand = brandPoMapper.selectByPrimaryKey(spu.getBrandId());
        var category = goodsCategoryPoMapper.selectByPrimaryKey(spu.getCategoryId());
        var freightModel = freightModelPoMapper.selectByPrimaryKey(spu.getFreightId());
        var shop = shopPoMapper.selectByPrimaryKey(spu.getShopId());
        SpecOverview spec;
        try {
            spec = jsonMapper.readValue(spu.getSpec(), SpecOverview.class);
        } catch (JsonProcessingException e) {
            spec = null;
        }

        var goodsSkuExample = new GoodsSkuPoExample();
        goodsSkuExample.createCriteria().andGoodsSpuIdEqualTo(spu.getId());
        var skuList = skuPoMapper.selectByExample(goodsSkuExample);

        var skuOverviewList = new ArrayList<SkuOverview>();
        for (var item : skuList) skuOverviewList.add(new SkuOverview(item, skuPriceDao.getSkuPrice(item)));

        return new SpuBo(spu, brand, category, freightModel, shop, spec, skuOverviewList);
    }

    public ReturnObject<SpuBo> newSpu(NewSpuVo vo, Long shopId) {
        String uuid = "";
        while (true) {
            String tempuuid = UUID.randomUUID().toString();
            var spuExample = new GoodsSpuPoExample();
            spuExample.createCriteria().andGoodsSnEqualTo(tempuuid);
            var sameSn = spuPoMapper.selectByExample(spuExample);
            if (sameSn.size() == 0) break;
        }

        var spuPo = new GoodsSpuPo();
        spuPo.setName(vo.getName());
        spuPo.setBrandId(0L);
        spuPo.setCategoryId(0L);
        spuPo.setFreightId(0L);
        spuPo.setShopId(shopId);
        spuPo.setGoodsSn(uuid);
        spuPo.setDetail(vo.getDecription());
        spuPo.setImageUrl("");
        spuPo.setSpec(vo.getSpecs());
        spuPo.setDisabled((byte)0);
        spuPo.setGmtCreate(LocalDateTime.now());
        spuPo.setGmtModified(LocalDateTime.now());

        spuPoMapper.insert(spuPo);
        var spuBo = getSpu(spuPo.getId());
        return new ReturnObject<>(spuBo);
    }

    public ResponseCode modifySpu(ModifySpuVo vo, Long spuId) {
        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setName(vo.getName());
        spuPo.setDetail(vo.getDecription());
        spuPo.setSpec(vo.getSpecs());
        spuPo.setGmtModified(LocalDateTime.now());

        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }

    public ResponseCode deleteSpu(Long spuId) {
        var skuExample = new GoodsSkuPoExample();
        skuExample.createCriteria().andGoodsSpuIdEqualTo(spuId);
        var updateSkuPo = new GoodsSkuPo();
        updateSkuPo.setGoodsSpuId(0L);
        updateSkuPo.setState((byte)6);
        skuPoMapper.updateByExample(updateSkuPo, skuExample);

        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setDisabled((byte)1);
        spuPo.setGmtModified(LocalDateTime.now());
        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }

    // Sku onshelves -> change sku state

    // Sku offshelve -> change sku state

    public ReturnObject<FloatPriceBo> newFloatPrice(NewFloatPriceVo vo, Long skuId, Long userId) {
        var floatPriceExample = new FloatPricePoExample();
        floatPriceExample.createCriteria()
                .andGoodsSkuIdEqualTo(skuId)
                .andValidEqualTo((byte)1);
        var floatPriceList = floatPricePoMapper.selectByExample(floatPriceExample);

        var conflictList = floatPriceList.stream().filter(item ->
                        item.getBeginTime().isAfter(vo.getBeginTime())
                                && item.getBeginTime().isBefore(vo.getEndTime())
                                || item.getEndTime().isAfter(vo.getBeginTime())
                                && item.getEndTime().isBefore(vo.getEndTime())
                ).collect(Collectors.toList());
        if (conflictList.size() != 0) return new ReturnObject<>(ResponseCode.SKUPRICE_CONFLICT);

        var floatPricePo = new FloatPricePo();
        floatPricePo.setGoodsSkuId(skuId);
        floatPricePo.setActivityPrice(vo.getActivityPrice());
        floatPricePo.setBeginTime(vo.getBeginTime());
        floatPricePo.setEndTime(vo.getEndTime());
        floatPricePo.setQuantity(vo.getQuantity());
        floatPricePo.setCreatedBy(userId);
        floatPricePo.setInvalidBy(0L);
        floatPricePo.setValid((byte)1);
        floatPricePo.setGmtCreate(LocalDateTime.now());
        floatPricePo.setGmtModified(LocalDateTime.now());
        floatPricePoMapper.insert(floatPricePo);

        var customerDTO = customerUnion.findCustomerByUserId(userId).getData();
        var userOverview = new UserOverview(userId, customerDTO);

        var floatPriceBo = new FloatPriceBo(floatPricePo, userOverview, userOverview);
        return new ReturnObject<>(floatPriceBo);
    }

    public ResponseCode endisableFloatPrice(Long floatPriceId, Long userId) {
        var floatPricePo = new FloatPricePo();
        floatPricePo.setId(floatPriceId);
        floatPricePo.setInvalidBy(userId);
        floatPricePo.setValid((byte)0);
        floatPricePo.setGmtModified(LocalDateTime.now());
        floatPricePoMapper.updateByPrimaryKey(floatPricePo);
        return ResponseCode.OK;
    }

    public ReturnObject<BrandBo> newBrand(NewBrandVo vo) {
        var brandPo = new BrandPo();
        brandPo.setName(vo.getName());
        brandPo.setDetail(vo.getDetail());
        brandPo.setImageUrl("");
        brandPo.setGmtCreate(LocalDateTime.now());
        brandPo.setGmtCreate(LocalDateTime.now());
        brandPoMapper.insert(brandPo);
        return new ReturnObject<>(new BrandBo(brandPo));
    }

    // TODO updateBrandImg() { }
    public ResponseCode uploadBrandImg(Long shopId, Long brandId) {
        return ResponseCode.OK;
    }

    public ReturnObject<ListBo<BrandBo>> getAllBrands(Integer page, Integer pageSize) {
        var brandPoExample = new BrandPoExample();
        brandPoExample.createCriteria().andGmtCreateIsNotNull();
        if (page != null) PageHelper.startPage(page, pageSize); // 设置整个线程的Page选项
        var brandPoList = brandPoMapper.selectByExample(brandPoExample);

        List<BrandBo> brandBoList = new ArrayList<>();
        for (var item : brandPoList) brandBoList.add(new BrandBo(item));

        if (page != null) {
            // 返回分页信息
            var pageInfo = new PageInfo<>(brandPoList);
            return new ReturnObject<>(new ListBo<>(page, pageSize, pageInfo.getTotal(), pageInfo.getPages(), brandBoList));
        } else
            return new ReturnObject<>(new ListBo<>(1, brandPoList.size(), (long) brandPoList.size(), 1, brandBoList));
    }

    public ResponseCode modifyBrand(ModifyBrandVo vo, Long brandId) {
        var brandPo = new BrandPo();
        brandPo.setId(brandId);
        brandPo.setName(vo.getName());
        brandPo.setDetail(vo.getDetail());
        brandPo.setGmtModified(LocalDateTime.now());
        int rows = brandPoMapper.updateByPrimaryKey(brandPo);
        if (rows == 0) return ResponseCode.RESOURCE_ID_NOTEXIST;
        return ResponseCode.OK;
    }

    public ResponseCode deleteBrand(Long brandId) {
        int rows = brandPoMapper.deleteByPrimaryKey(brandId);
        if (rows == 0) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var spuExample = new GoodsSpuPoExample();
        var spuPo = new GoodsSpuPo();
        spuExample.createCriteria().andBrandIdEqualTo(brandId);
        spuPo.setBrandId(0L);
        spuPoMapper.updateByExample(spuPo, spuExample);
        return ResponseCode.OK;
    }

    public ResponseCode addSpuIntoCategory(Long spuId, Long categoryId) {
        // spuId 由Controller层检查
        var category = goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        if (category == null) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setCategoryId(categoryId);
        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }

    public ResponseCode removeSpuFromCategory(Long spuId, Long categoryId) {
        // spuId 由Controller层检查
        var category = goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        if (category == null) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setCategoryId(0L);
        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }

    public ResponseCode addSpuIntoBrand(Long spuId, Long brandId) {
        // spuId 由Controller层检查
        var brandPo = brandPoMapper.selectByPrimaryKey(brandId);
        if (brandPo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setBrandId(brandId);
        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }

    public ResponseCode removeSpuFromBrand(Long spuId, Long brandId) {
        // spuId 由Controller层检查
        var brandPo = brandPoMapper.selectByPrimaryKey(brandId);
        if (brandPo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;

        var spuPo = new GoodsSpuPo();
        spuPo.setId(spuId);
        spuPo.setBrandId(0L);
        spuPoMapper.updateByPrimaryKey(spuPo);
        return ResponseCode.OK;
    }
}
