package cn.ningxy.rqui.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ningxy
 */
@Data
@AllArgsConstructor
public class JwtUser {
    UserVO userVO;
    String token;
}
