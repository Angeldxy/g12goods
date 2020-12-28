package cn.edu.xmu.g12.g12ooadgoods.controller;

import cn.edu.xmu.g12.g12ooadgoods.dao.CommentDao;
import cn.edu.xmu.g12.g12ooadgoods.dao.ExistBelongDao;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.comment.ConfirmCommentVo;
import cn.edu.xmu.g12.g12ooadgoods.model.vo.comment.NewCommentVo;
import cn.edu.xmu.g12.g12ooadgoods.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
*/
@Controller
public class CommentController {

    private  static  final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final JwtHelper jwt;

    @Autowired
    CommentDao commentDao;
    @Autowired
    ExistBelongDao existBelongDao;

    public CommentController() {
        jwt = new JwtHelper();
    }

    @GetMapping("/comments/states")
    public Object getAllStates() {
        return Tool.decorateReturnObject(commentDao.getAllStates());
    }

    @PostMapping("/orderitems/{orderItemId}/comments")
    public Object newSkuComment(@PathVariable Long orderItemId,
                                @Validated @RequestBody NewCommentVo vo, BindingResult bindingResult,
                                HttpServletRequest request, HttpServletResponse response) {
        var userId = Tool.parseJwtAndGetUser(request);

        logger.info("newSkuComment controller orderItemId="+orderItemId+" NewCommentVo="+vo.toString());
        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if(object != null) return object;

        var returnObject = commentDao.newSkuComment(orderItemId, userId, vo);
        return Tool.decorateReturnObject(returnObject);
    }

    @GetMapping("/skus/{skuId}/comments")
    public Object getSkuCommentValid(@PathVariable Long skuId,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize) {
        logger.info("getSkuCommentValid controller skuId="+skuId);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        var returnObject = commentDao.getSkuCommentValid(skuId, page, pageSize);
        return Tool.decorateReturnObject(returnObject);
    }

    @PutMapping("/shops/{shopId}/comments/{commentId}/confirm")
    public Object confirmComment(@PathVariable Long shopId,
                                 @PathVariable Long commentId,
                                 @Validated @RequestBody ConfirmCommentVo vo, BindingResult bindingResult,
                                 HttpServletRequest request, HttpServletResponse response) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);
        var code = existBelongDao.commentBelongToShop(commentId, shopId);
        if (code != ResponseCode.OK) return code;

        logger.info("confirmComment controller shopId="+shopId+" commentId="+commentId+" ConfirmCommentVo="+vo.toString());
        /* 处理参数校验错误 */
        Object object = Common.processFieldErrors(bindingResult, response);
        if(object != null) return object;

        var responseCode = commentDao.confirmComment(shopId, commentId, vo);
        return Tool.decorateResponseCode(responseCode);
    }

    @GetMapping("/comments")
    public Object getCommentOfUser(@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer pageSize,
                                   HttpServletRequest request) {
        var userId = Tool.parseJwtAndGetUser(request);

        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        var returnObject
                = commentDao.getCommentOfUser(userId, page, pageSize);
        return Tool.decorateReturnObject(returnObject);
    }

    @GetMapping("/shops/{shopId}/comments/all")
    public Object getShopCommentByAdmin(@PathVariable Long shopId,
                                        @RequestParam(required = false) Byte state,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize,
                                        HttpServletRequest request) {
        if (Tool.noAccessToShop(request, shopId)) return Tool.decorateResponseCode(ResponseCode.RESOURCE_ID_OUTSCOPE);

        if (state != null && (state < 0 || state > 2)) return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);
        if (Tool.checkPageParam(page,pageSize) !=  ResponseCode.OK)
            return Tool.decorateResponseCode(ResponseCode.FIELD_NOTVALID);

        var returnObject
                = commentDao.getShopCommentByAdmin(shopId, state, page, pageSize);
        return Tool.decorateReturnObject(returnObject);
    }

}
