package cn.ningxy.rqui.sys.dto;

import cn.ningxy.rqui.sys.enums.ResponseStatus;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回数据格式封装
 *
 * @author ningxy
 */
@Data
public class ResponseResult<T> implements Serializable {

    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误说明
     */
    private String message;

    /**
     * data，可能是 null
     */
    private T data;

    private ResponseResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(ResponseStatus responseStatus, T data) {
        this(responseStatus.code(), responseStatus.message(), data);
    }

    public static ResponseResult<Void> success() {
        return new ResponseResult<>(ResponseStatus.SUCCESS, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(ResponseStatus.SUCCESS, data);
    }

    public static <T> ResponseResult<T> success(ResponseStatus responseStatus, T data) {
        if (responseStatus == null) {
            return success(data);
        }
        return new ResponseResult<>(responseStatus, data);
    }

    public static ResponseResult<Void> error() {
        return new ResponseResult<>(ResponseStatus.ERROR, null);
    }

    public static ResponseResult<Object> error(Exception exception) {
        return new ResponseResult<>(ResponseStatus.ERROR, new ExceptionData(exception));
    }

    public static <T> ResponseResult<T> error(T data) {
        return new ResponseResult<>(ResponseStatus.ERROR, data);
    }

    public static <T> ResponseResult<T> error(ResponseStatus responseStatus, T data) {
        if (responseStatus == null) {
            return success(data);
        }
        return new ResponseResult<>(responseStatus, data);
    }

    public static ResponseResult<Void> error(ResponseStatus responseStatus) {
        return new ResponseResult<>(responseStatus, null);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}

class ExceptionData {
    private final String exceptionType;
    private final String exceptionMessage;

    public ExceptionData(String type, String message) {
        this.exceptionType = type;
        this.exceptionMessage = message;
    }

    public ExceptionData(Exception e) {
        this(e.getClass().getSimpleName(), e.getMessage());
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
