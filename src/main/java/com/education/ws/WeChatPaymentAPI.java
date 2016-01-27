package com.education.ws;

import com.education.auth.TokenAccess;
import com.education.db.entity.UserEntity;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.service.LoginHistoryService;
import com.education.service.WeChatPayService;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/18/16.
 */
@Path("/wechat/pay")
public class WeChatPaymentAPI {

    private static final Logger logger = Logger.getLogger(WeChatPaymentAPI.class.getName());

    @Autowired
    private LoginHistoryService historyService;

    @Autowired
    private WeChatPayService payService;

    @Path("request")
    @POST
    @TokenAccess(requireAccessToken = true)
    public Response requestPayment(@Context HttpServletRequest request,
                                   @Context ContainerRequestContext context,
                                   @FormParam("product_desc") String productDesc,
                                   @FormParam("product_detail") String productDetail,
                                   @FormParam("price") int price) {
        String token = (String) context.getProperty(HeaderKeys.ACCESS_TOKEN);
        UserEntity userInfo = null;
        if (token != null) {
            logger.info("login user " + token);
            userInfo = historyService.getUserByToken(token);
        } else {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }

        String openid = userInfo.getOpenid();
        payService.requestPay(openid, productDesc, productDetail, price, request.getRemoteAddr());
        return Response.ok().build();
    }

    @Path("test")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestPayment(@Context HttpServletRequest request,
                                   @Context ContainerRequestContext context,
                                   @FormParam("openid") String openId,
                                   @FormParam("product_desc") String productDesc,
                                   @FormParam("product_detail") String productDetail,
                                   @FormParam("price") int price) {
//        String openid = "oylrrviRhbTDqnuHkStG8m-S5IIA";
        Map<String, String> ret = payService.requestPay(openId, productDesc, productDetail, price, "10.17.11.111");
        return Response.ok(ret).build();
    }
}
