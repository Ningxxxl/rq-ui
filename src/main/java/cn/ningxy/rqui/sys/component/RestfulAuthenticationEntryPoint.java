package cn.ningxy.rqui.sys.component;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录异常处理
 *
 * @author ningxy
 */
public class RestfulAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HTTP_STATUS.value());
        httpServletResponse.getWriter().println(HTTP_STATUS.getReasonPhrase());
        httpServletResponse.getWriter().flush();
    }
}
