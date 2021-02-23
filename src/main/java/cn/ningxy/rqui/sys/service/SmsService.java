package cn.ningxy.rqui.sys.service;

/**
 * @author ningxy
 */
public interface SmsService {
    void sendMessage(String phoneNumber, String text);
}
