package com.eduhk.alic.alicbackend.common.exception;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.vo.Result;
import com.eduhk.alic.alicbackend.model.vo.ResultResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FuSu
 * @date 2025/1/15 11:23
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = BaseException.class)
//    public Result baseExceptionHandler(HttpServletRequest req, BaseException e) {
//        log.error("发生业务异常！原因是：{}", e.getMessage());
//        return ResultResp.failure(ResultCode.INTERNAL_SERVER_ERROR);
//    }


    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResultResp.failure(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return ResultResp.failure(ResultCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public Result exceptionHandler(HttpServletRequest req, DuplicateKeyException e) {
        log.error("发生DuplicateKeyException！原因是:", e);
        return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
    }

    @ExceptionHandler(value = BaseException.class)
    public Result exceptionHandler(HttpServletRequest req, BaseException e) {
        log.error("BaseException！原因是:", e);
        return ResultResp.failure(e.getErrorCode());
    }


}
