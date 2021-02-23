package cn.ningxy.rqui.sys.component;

import cn.ningxy.rqui.sys.enums.ResponseStatus;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import cn.ningxy.rqui.sys.dto.ResponseResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author ningxy
 */
@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler implements ErrorController {
    /**
     * 处理未经过详细处理的异常
     *
     * @param e Exception
     * @return ResponseResult
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<Object> commonExceptionHandler(Exception e) {
        log.error(e.toString());
        return ResponseResult.error(e);
    }

    /**
     * 处理登录
     *
     * @param e Exception
     * @return ResponseResult
     */
    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseResult<Void> badCredentialsExceptionHandler(Exception e) {
        return ResponseResult.error(ResponseStatus.BAD_CREDENTIALS);
    }

    /**
     * 处理 JWT 验证异常
     *
     * @param e Exception
     * @return ResponseResult
     */
    @ExceptionHandler(value = JWTVerificationException.class)
    public ResponseResult<Void> jwtVerificationExceptionHandler(Exception e) {
        return ResponseResult.error(ResponseStatus.INVALID_TOKEN);
    }

    /**
     * 处理 401, 403, 404 等错误
     *
     * @param request HttpServletRequest
     * @return ResponseResult
     */
    @RequestMapping("/error")
    public ResponseResult<Void> catchError(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        switch (status) {
            case HttpServletResponse.SC_UNAUTHORIZED:
                return ResponseResult.error(ResponseStatus.UNAUTHORIZED);
            case HttpServletResponse.SC_FORBIDDEN:
                return ResponseResult.error(ResponseStatus.ACCESS_DENIED);
            case HttpServletResponse.SC_NOT_FOUND:
                return ResponseResult.error(ResponseStatus.NOT_FOUND);
            default:
                return ResponseResult.error();
        }

    }

    /**
     * 该方法已过时，已经被 Spring 弃用，返回 null 即可
     *
     * @return null
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}
