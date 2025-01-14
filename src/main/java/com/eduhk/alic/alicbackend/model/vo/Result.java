package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author FuSu
 * @date 2025/1/13 14:25
 */
@Setter
@Getter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 6308315887056661996L;
    private Integer code;
    private String message;
    private T data;


    public Result setResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }

    public Result setResult(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.setData(data);
        return this;
    }
}
