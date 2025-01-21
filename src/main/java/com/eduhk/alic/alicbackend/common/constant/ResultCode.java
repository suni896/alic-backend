package com.eduhk.alic.alicbackend.common.constant;

import lombok.Getter;

/**
 * @author FuSu
 * @date 2025/1/13 14:26
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "Success"),//成功
    //FAIL(400, "失败"),//失败
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Auth Failed"),//未认证
    NOT_FOUND(404, "API Not Exist"),//接口不存在
    INTERNAL_SERVER_ERROR(500, "Server Error"),//服务器内部错误
    CODE_SEND_ERROR(501, "Send Code Failed, Please Contact Admin"),
    METHOD_NOT_ALLOWED(405,"方法不被允许"),

    /*参数错误:1001-1999*/
    PARAMS_IS_INVALID(1001, "参数无效"),
    PARAMS_IS_BLANK(1002, "参数为空"),
    PASSWORD_ERROR(1003, "password error"),

    /*用户错误2001-2999*/
    UNREGISTERED_ACCOUNT(2001, "the account is unregistered, please register first"),
    REGISTERED_ACCOUNT(2002, "the account is registered, please login"),;

    private Integer code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
