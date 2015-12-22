package com.education.ws;

import com.education.auth.Public;
import com.education.service.WeChatService;
import com.education.service.WeChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/wechat")
public class WeChatAPI {

    private static final Logger logger = Logger.getLogger(WeChatAPI.class.getName());

    @Autowired
    private WeChatService weChatService;

    @Path("/connect")
    @GET
    public Response connectWeChatServer(@QueryParam("signature") String signature,
                                        @QueryParam("timestamp") String timeStamp,
                                        @QueryParam("nonce") String nonce,
                                        @QueryParam("echostr") String echoStr) {
        logger.info("signature " + signature);
        boolean validate = weChatService.validateConnection(signature, timeStamp, nonce);
        if (validate) {
            logger.info("connection verify success");
            return Response.ok(echoStr).build();
        }
        logger.info("connection verify failed.");
        return Response.ok().build();
    }

    @Path("/connect")
    @POST
    public Response weChatServer() {
        logger.info("post response");
        return Response.ok().build();
    }

    @Path("/login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Public(requireWeChatCode = true, requireWeChatUser = false)
    public Response login(@QueryParam("code") String code, @QueryParam("state") String state) {
        logger.info(" login code=" + code + ", state= " + state);
        WeChatUserInfo webUserInfo = weChatService.getWebUserInfo(code);
        return Response.ok(webUserInfo).build();
    }

    @Path("/getopenid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenId(@QueryParam("code") String code, @QueryParam("state") String state) {
        WeChatUserInfo webUserInfo = weChatService.getWebUserInfo(code);
        return Response.ok(webUserInfo.getOpenId()).build();
    }

    @Path("/userlist")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserOpenIdList() {
        List<String> userOpenIDList = weChatService.getUserOpenIDList();
        return Response.ok(userOpenIDList).build();
    }

    @Path("/userinfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@QueryParam("openid") String openid) {
        return Response.ok(weChatService.getUserInfo(openid)).build();
    }

    @Path("/jsapi")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJsApiTicket(ContainerRequestContext context){
        URI baseUri = context.getUriInfo().getBaseUri();
        URI absolutePath = context.getUriInfo().getAbsolutePath();
        logger.info("base uri:"+baseUri.toString());
        logger.info("absout path:"+absolutePath.toString());
        Map<String, String> webJSSignature = weChatService.getWebJSSignature("http://www.imzao.com/education/");
        return Response.ok(webJSSignature).build();
    }

    @Path("/barcode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBarCodeURL(@QueryParam("code") String code) {
        return Response.ok(weChatService.getQRBarTicket(code)).build();
    }

}
