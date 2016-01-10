package com.education.ws;

import com.education.auth.Public;
import com.education.exception.*;
import com.education.exception.BadRequestException;
import com.education.service.LoginHistoryService;
import com.education.service.WeChatService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/wechat")
public class WeChatAPI {

    private static final Logger logger = Logger.getLogger(WeChatAPI.class.getName());

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private LoginHistoryService historyService;

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
    public Response login(@Context HttpServletRequest request,
                          @Context ContainerRequestContext requestContext,
                          @QueryParam("code") String code, @QueryParam("state") String state) {
        logger.info(" login code=" + code + ", state= " + state+", "+requestContext);
        WeChatUserInfo userInfo = (WeChatUserInfo) requestContext.getProperty(ContextKeys.WECHAT_USER);
        if(userInfo != null) {
            HttpSession session = request.getSession(true);
            logger.info("save user info session "+userInfo +", session id:"+session.getId());
            session.setAttribute(ContextKeys.WECHAT_USER, userInfo);
            String token = historyService.saveWeChatUserLogin(userInfo);
            return Response.ok(token).build();
        }else{
            logger.severe("can't login through wechat");
        }
        return Response.ok().build();
    }

    @Path("/getopenid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenId(@QueryParam("code") String code, @QueryParam("state") String state) {
        WeChatUserInfo webUserInfo = weChatService.getWebUserInfo(code, state);
        return Response.ok(webUserInfo.getOpenid()).build();
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
    public Response getJsApiTicket(@Context ContainerRequestContext context) {
        URI baseUri = context.getUriInfo().getBaseUri();
        URI absolutePath = context.getUriInfo().getAbsolutePath();
        logger.info("base uri:" + baseUri.toString());
        logger.info("absout path:" + absolutePath.toString());
        Map<String, String> webJSSignature = weChatService.getWebJSSignature("http://www.imzao.com/education/");
        return Response.ok(webJSSignature).build();
    }

    @Path("/barcode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBarCodeURL(@QueryParam("code") String code, @QueryParam("state") String state) {
        return Response.ok(weChatService.getQRBarTicket(code, state)).build();
    }

    @Path("/qrurl")
    @GET
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getQRUrl(@QueryParam("code") String code, @QueryParam("state") String state){
        return Response.ok(weChatService.getQRBarCodeURL(code, state)).build();
    }

    @Path("/qrwebconnect")
    @GET
    public Response getQrWebConnectUrl(){
        String qrConnectUrl = weChatService.getQrWebConnectUrl();
        try {
            return Response.seeOther(new URI(qrConnectUrl)).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode.BUILD_QRCONNECT_URI_ERROR);
        }
    }

}
