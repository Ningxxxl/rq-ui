package cn.ningxy.rqui.sys.component;

import cn.ningxy.rqui.sys.constant.AuthConstants;
import cn.ningxy.rqui.sys.util.JwtUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证过滤器
 * <p>
 * 从 request 的 header 中提取 JWT 并验证
 * </p>
 *
 * @author ningxy
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 从 request header 中获取JWT
     *
     * @param request request
     * @return JWT
     */
    private static String getTokenFromHttpRequest(HttpServletRequest request) {
        final String authorizationString = request.getHeader(AuthConstants.TOKEN_HEADER);
        if (authorizationString == null || !authorizationString.startsWith(AuthConstants.TOKEN_PREFIX)) {
            return null;
        }
        return authorizationString.replace(AuthConstants.TOKEN_PREFIX, "");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = getTokenFromHttpRequest(request);
        if (token != null) {
            // TODO: 2021/1/7 当前是通过token获取用户认证（权限）信息的，应该改为从数据库中获取。
            SecurityContextHolder.getContext().setAuthentication(JwtUtils.getAuthenticationFromToken(token));
        }
        chain.doFilter(request, response);
        // TODO: 2021/1/7 /login也能登录
    }
}
