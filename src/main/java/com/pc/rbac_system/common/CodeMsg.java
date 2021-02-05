package com.pc.rbac_system.common;

import lombok.Data;

@Data
public class CodeMsg {
    private Integer code;
    private String msg;
    //使用static的原因是因为直接使用
    public static CodeMsg SUCCESS = new CodeMsg(200,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg USER_PERMISSION_ERROR = new CodeMsg(403,"用户无权限");
    public static CodeMsg USER_LOGIN_FAIL = new CodeMsg(402,"用户token认证失败");
    public static CodeMsg USER_LOGIN_USERNAME_OR_PASSWORD_ERROR = new CodeMsg(401,"账号或密码错误,一小时内错误3次账户将被锁定24小时");
    public static CodeMsg USER_LOGIN_LOCK = new CodeMsg(406,"用户登录错误超过3次，账户锁定24小时");
    public static CodeMsg INFO_NOT_EXITS = new CodeMsg(500200,"数据不存在");
    public static CodeMsg IMG_TYPE_NOT_SURPPORT = new CodeMsg(407,"图片仅支持jpg,bmp,png格式");
    public static CodeMsg SIGN_NOT_EXIST = new CodeMsg(502,"今日签到已过期");

    private CodeMsg(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }
}
