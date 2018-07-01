package top.qjyoung.encrypt.util;

import top.qjyoung.encrypt.dto.RsaKeyInfoDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟密钥的存储，实际开发中可删除
 * <p>
 * Created by QJY on 2018/6/30
 */
public class KeyContainer {
    /**
     * key store uuid
     */
    public static Map<String, RsaKeyInfoDto> RSA_KEY_MAP = new ConcurrentHashMap<>();
    /**
     * key store uuid
     */
    public static Map<String, String> AES_KEY_MAP = new ConcurrentHashMap<>();
}
