package cn.ningxy.rqui.sys.component;

import cn.ningxy.rqui.sys.dto.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 将所有 Controller 返回的数据封装成统一格式
 *
 * @author ningxy
 */
@RestControllerAdvice
public class RestfulResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return ResponseResult.success();
        } else if (o instanceof ResponseResult) {
            return o;
        }
        return ResponseResult.success(o);
    }
}
