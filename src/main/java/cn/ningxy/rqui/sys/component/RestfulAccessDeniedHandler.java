package cn.ningxy.rqui.sys.component;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足异常处理
 *
 * @author ningxy
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HTTP_STATUS.value());
        httpServletResponse.getWriter().println(HTTP_STATUS.getReasonPhrase());
        httpServletResponse.getWriter().flush();
    }
}
