package cn.ningxy.rqui.sys.service.serviceImpl;

import cn.ningxy.rqui.sys.service.SmsService;
import org.springframework.stereotype.Service;

/**
 * @author ningxy
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendMessage(String phoneNumber, String text) {
        // TODO: 2021/1/18 发送短信API
    }
}
