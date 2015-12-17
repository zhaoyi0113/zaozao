package com.education.exception;

import javax.ws.rs.core.Response;

public enum ErrorCode {

    // * 0 ~ 10000, system error
    UNKNOWN(0, Response.Status.BAD_REQUEST, "系统未知错误", "System unknown error."),
    SYSTEM_ERROR(1, Response.Status.INTERNAL_SERVER_ERROR, "系统错误", "System error."),
    DATABASE_ERROR(2, Response.Status.INTERNAL_SERVER_ERROR, "数据库错误", "DataBase error."),
    DATA_NOT_CONSISTENT(3, Response.Status.INTERNAL_SERVER_ERROR, "数据不一致错误", "Data not consistent error."),
    NOT_PERMITTED(4, Response.Status.FORBIDDEN, "不允许访问", "Request not permitted"),
    UNAUTHORIZED(5, Response.Status.NOT_ACCEPTABLE, "未授权的访问", "Request Unauthorized"),
    ILLEGAL_REQUEST(6, Response.Status.BAD_REQUEST, "非法请求", "Illegal request"),
    FILTER_REQUEST_BODY_ERROR(7, Response.Status.BAD_REQUEST, "请求内容处理异常", "deal with request object error."),
    ACCOUNT_TOKEN_EXPIRED(10014, Response.Status.NOT_ACCEPTABLE, "account token已过期", "account token has been expired!"),

    // * 10000 ~ 20000, business related generic error
    RESOURCE_NOT_FOUND(10001, Response.Status.NOT_FOUND, "资源不存在", "Resource not found"),
    AUTHENTICATION_EXISTED(10002, Response.Status.CONFLICT, "账号已存在", "Account existed"),
    AUTHENTICATION_NOT_EXISTED(10003, Response.Status.NOT_FOUND, "账号不存在", "Account don't exist"),
    AUTHENTICATION_INVALIDATE(10004, Response.Status.NOT_ACCEPTABLE, "账号未激活", "Account has not activated"),
    AUTHENTICATION_FORBIDDEN(10005, Response.Status.FORBIDDEN, "账号已禁用或已失效,请与管理员联系",
            "Account has been disabled or deleted, Please contact the administrator"),
    EMAIL_FORMAT_ERROR(10006, Response.Status.NOT_ACCEPTABLE, "邮箱格式不正确", "Email format error"),
    LOGIN_AUTHENTICATION_NOT_EXISTED(10007, Response.Status.NOT_FOUND, "账号不存在,或用户名、密码不正确",
            "Account don't existed, or account name or password not correct."),
    INVALID_PASSWORD(10008, Response.Status.FORBIDDEN, "密码不正确", "Invalid password."),
    PASSWORD_EXPIRED(10009, Response.Status.FORBIDDEN, "密码已失效", "Password expired."),
    UNSUPPORTED_ENCODING(10010, Response.Status.BAD_REQUEST, "不支持的编码格式", "Unsupported encoding."),
    REGISTER_NEW_MEMBER_FAILED(10011, Response.Status.BAD_REQUEST, "注册失败", "Register failed"),
    DUPLICATE_ACCOUNT_ID(10012, Response.Status.CONFLICT, "账号已存在", "account has existed"),
    INVALID_ACCESS_TOKEN(10013, Response.Status.BAD_REQUEST, "无效的token", "invalid access token"),
    ACCESS_TOKEN_DELETED(10014, Response.Status.BAD_REQUEST, "token已被删除", "access token deleted"),
    SYNC_TACTICS_ERROR(10015, Response.Status.BAD_REQUEST, "同步策略出错", "sync tactics error"),
    PASSBOOK_NOT_FOUND(10016, Response.Status.NOT_FOUND, "没找到密码本", "passbook not found"),
    NO_RESOURCE(10017, Response.Status.BAD_REQUEST, "没有资源", "no resource"),
    FOLDER_EXISTED(10018, Response.Status.BAD_REQUEST, "目录已存在", "folder already existed."),
    FOLDER_NOT_EXISTED(10019, Response.Status.BAD_REQUEST, "目录不存在", "folder doesn't exist"),
    DELETE_FOLDER_ERROR(10020, Response.Status.BAD_REQUEST, "删除目录失败", "delete folder failed"),
    FILE_NOT_EXISTED(10021, Response.Status.BAD_REQUEST, "文件不存在", "file doesn't exist"),
    FILE_DOES_NOT_SUPPORT_PREVIEW(10022, Response.Status.BAD_REQUEST, "该文件不支持预览", "The file does not support Preview"),
    IMAGES_SIZE_ERR(10024, Response.Status.BAD_REQUEST, "必须指定图片的长或者宽", "images's size is error!"),
    COULD_NOT_UNBIND_USED_DEVICE(10023, Response.Status.BAD_REQUEST, "不能解绑正在使用的设备", "could not unbind used device"),
    ENCRYPTION_ERROR(10025, Response.Status.BAD_REQUEST, "加密失败", "encrypt call failed"),
    DECRYPTION_ERROR(10026, Response.Status.BAD_REQUEST, "解密失败", "decrypt call failed"),
    DATA_ERROR(10027, Response.Status.BAD_REQUEST, "数据错误", "data error!"),
    WECHAT_CODE_ERROR(10028, Response.Status.BAD_REQUEST, "没有微信Code", "can't find wechat code"),
    USER_NOT_EXISTED(10029, Response.Status.BAD_REQUEST, "用户不存在", "user doesn't exist");


    private final int code;

    private final Response.Status status;

    private final String cnMsg;

    private final String enMsg;

    ErrorCode(int code, Response.Status status, String cnMsg, String enMsg) {
        this.code = code;
        this.status = status;
        this.cnMsg = cnMsg;
        this.enMsg = enMsg;
    }

    public int getCode() {
        return code;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getCnMsg() {
        return cnMsg;
    }

    public String getEnMsg() {
        return enMsg;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", status=" + status +
                ", cnMsg='" + cnMsg + '\'' +
                ", enMsg='" + enMsg + '\'' +
                '}';
    }
}
