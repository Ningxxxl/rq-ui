package cn.ningxy.rqui.sys.util;

import cn.ningxy.rqui.sys.constant.AuthConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;
import cn.ningxy.rqui.sys.enums.UserRoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * JWT 工具类
 *
 * @author ningxy
 */
@UtilityClass
public final class JwtUtils {
    private static final Algorithm SIGN_ALGORITHM = Algorithm.HMAC256(AuthConstants.JWT_SECRET_KEY);

    /**
     * 生成 JWT
     *
     * @param userName   用户名
     * @param roleId     权限ID
     * @param isRemember 是否记住
     * @return 生成的 JWT
     */
    public static String generateToken(String userName, Integer roleId, boolean isRemember) {
        return generateToken(userName, UserRoleEnum.getNameByCode(roleId), isRemember);
    }

    /**
     * 生成 JWT
     *
     * @param userName   用户名
     * @param userRole   权限
     * @param isRemember 是否记住
     * @return 生成的 JWT
     */
    public static String generateToken(String userName, String userRole, boolean isRemember) {
        final long expiration = isRemember ? AuthConstants.EXPIRATION_REMEMBER_TIME : AuthConstants.EXPIRATION_TIME;
        return JWT.create()
                .withSubject(userName)
                .withClaim(AuthConstants.TOKEN_ROLE_CLAIM, userRole)
                .withIssuer(AuthConstants.TOKEN_ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000))
                .sign(SIGN_ALGORITHM);
    }

    /**
     * 验证 JWT 是否有效
     *
     * @param token JWT
     * @return DecodedJWT
     */
    public static DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(SIGN_ALGORITHM)
                .withIssuer(AuthConstants.TOKEN_ISSUER)
                .build();
        return verifier.verify(token);
    }

    /**
     * 从JWT中获取用户认证（权限）信息
     * <p>
     * 其中JWT必须是有效的（未过期）
     *
     * @param token JWT
     * @return 用户认证（权限）信息
     */
    public static Authentication getAuthenticationFromToken(String token) {
        DecodedJWT decodedJwt = validateToken(token);
        if (decodedJwt == null) {
            return null;
        }
        final String userName = decodedJwt.getSubject();
        final String roleName = decodedJwt.getClaim(AuthConstants.TOKEN_ROLE_CLAIM).asString();
        return generateAuthentication(userName, token, roleName);
    }

    /**
     * 生成 Authentication
     *
     * @param principal   用户名
     * @param credentials 凭证 token
     * @param authority   权限（角色）
     * @return UsernamePasswordAuthenticationToken
     */
    public static Authentication generateAuthentication(String principal, String credentials, String authority) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(authority)
        );
        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }
}
