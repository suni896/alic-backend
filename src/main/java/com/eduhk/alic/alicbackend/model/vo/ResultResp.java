package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import lombok.Data;

/**
 * @author FuSu
 * @date 2025/1/13 16:52
 */
@Data
public class ResultResp {

    // 只返回状态
    public static Result success() {
        return new Result()
                .setResult(ResultCode.SUCCESS);
    }

    // 成功返回数据
    public static Result success(Object data) {
        return new Result()
                .setResult(ResultCode.SUCCESS, data);


    }

    // 失败
    public static Result failure(ResultCode resultCode) {
        return new Result()
                .setResult(resultCode);
    }

    // 失败
    public static Result failure(ResultCode resultCode, Object data) {
        return new Result()
                .setResult(resultCode, data);
    }

}
