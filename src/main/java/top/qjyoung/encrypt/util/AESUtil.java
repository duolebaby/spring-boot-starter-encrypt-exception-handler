package top.qjyoung.encrypt.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

public class AESUtil {
    
    static String KEY = "abcdef0123456789";
    
    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    
    public static void main(String args[]) throws Exception {
        System.err.println(generateKey());
        System.out.println(encrypt("测试用的数据", KEY));
        System.out.println(encrypt("helloworld", KEY));
        System.out.println(decrypt("QblMau3/rJhkBOyKifkqGaLNpojCIl7Au8ZeN0FQ2yk=", KEY));
        System.out.println(decrypt("H83LKj1VGVEtwWoA/9CN5Q==", KEY));
        System.out.println(decrypt("H0GmRshIYJRjJsNJOy6N9Q==", "d7b85f6e214abcda"));
    }
    
    public static String generateKey() {
        String key = StringUtils.getUUID().substring(0, 16);
        return key;
    }
    
    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }
    
    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
        byte[] result = cipher.doFinal(Base64Util.decode(data));
        return new String(result, "utf-8");
    }
}
