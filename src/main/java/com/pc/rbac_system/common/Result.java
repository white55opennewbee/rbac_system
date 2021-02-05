package com.pc.rbac_system.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class Result<T> implements Serializable {
    private Integer code;
    private T data;
    private String message;
    private Result(T data) {
        this.code = 200;
        this.data = data;
        this.message= message;
    }
    private Result(Integer code , T data) {
        this.code = code;
        this.data = data;
        this.message= message;
    }

    private Result(CodeMsg codeMsg) {
        this.message=codeMsg.getMsg();
        this.code=codeMsg.getCode();
    }

    private Result(CodeMsg codeMsg,T data){
        this.message=codeMsg.getMsg();
        this.code=codeMsg.getCode();
        this.data = data;
    }

    //通过构造方法只需要传data就可以
    public static <T>Result<T> success( T data ){
        return new Result<>(data);
    }
    //构建token成功 code:201 data= token
    public static <T>Result<T> tokenSuccess( T data ){
        return new Result<>(201,data);
    }

    // 这里使用codeMsg 是因为返回结果的是code码不是固定的
    public static <T>Result<T> error(CodeMsg codeMsg){
        if (codeMsg == null) {
            log.warn("codeMsg is null");
            return null;
        }
        return new Result<T>(codeMsg);
    }

    public static <T>Result<T> errorWithData(CodeMsg codeMsg,T data){
        if (codeMsg == null) {
            log.warn("codeMsg is null");
            return null;
        }
        return new Result<>(codeMsg,data);
    }
}
