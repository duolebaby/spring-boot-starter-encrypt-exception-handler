package top.qjyoung.encrypt.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by QJY on 2018/6/30
 */
public class Base64Util {
    /**
     * base64 编码
     *
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }
    
    /**
     * base64解码
     *
     * @param base64Code
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64Code) throws Exception {
        return Base64.decodeBase64(base64Code);
    }
    
}
